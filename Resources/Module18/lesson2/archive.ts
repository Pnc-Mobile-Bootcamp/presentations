// Block 1.2 live demo: Intercept the Code.   Run:  node demo.ts
import { buildAuthorizationUrl, codeChallengeS256, generateCodeVerifier, parseRedirect } from "./pkce.ts";
import { MockAuthServer } from "./mock_auth_server.ts";
import { banner, step, info, attempt } from "./say.ts";

const REDIRECT = "com.pnc.mobile:/oauth2redirect";
const base = { authEndpoint: "https://login.pnc.example/authorize", clientId: "pnc-mobile", redirectUri: REDIRECT, scopes: ["openid", "accounts:read"], state: "state-123", nonce: "nonce-456" };

banner("Scene A: NO PKCE. An interceptor app registered the same redirect scheme.");
{
  const server = new MockAuthServer(false);
  // The legitimate app builds a URL with a placeholder challenge only to satisfy the helper; the server ignores it.
  const url = buildAuthorizationUrl({ ...base, codeChallenge: "ignored" }).replace(/&code_challenge=[^&]*&code_challenge_method=S256/, "");
  info("Authorization URL has no code_challenge: " + !url.includes("code_challenge"));
  const redirect = server.authorize(url);
  step("Android delivers the redirect to BOTH apps. The interceptor reads: " + redirect);
  const stolen = new URL(redirect).searchParams.get("code")!;
  attempt("interceptor redeems the stolen code (no verifier needed)", () => server.token({ grant_type: "authorization_code", code: stolen }));
}

banner("Scene B: PKCE on. Same interception, different outcome.");
{
  const server = new MockAuthServer(true);
  const verifier = generateCodeVerifier();
  const url = buildAuthorizationUrl({ ...base, codeChallenge: codeChallengeS256(verifier) });
  info("Verifier stays in the app. Does the URL contain it? " + url.includes(verifier));
  const redirect = server.authorize(url);
  const stolen = new URL(redirect).searchParams.get("code")!;
  attempt("interceptor redeems with NO verifier", () => server.token({ grant_type: "authorization_code", code: stolen }));
  attempt("interceptor guesses a verifier", () => server.token({ grant_type: "authorization_code", code: stolen, code_verifier: generateCodeVerifier() }));
  attempt("real app redeems with its verifier", () => server.token({ grant_type: "authorization_code", code: stolen, code_verifier: verifier }));
}

banner("Scene C: the RFC 7636 appendix B test vector");
{
  const v = "dBjftJeZ4CVP-mB92K27uhbUJU1p1r_wW1gFWFOEjXk";
  const expected = "E9Melhoa2OwvFrEMTJguCHaoeK1t8URWbuGJSstw-cM";
  const got = codeChallengeS256(v);
  info(`computed ${got}`); info(`published ${expected}`); info("match: " + (got === expected));
}

banner("Scene D: what the APP checks when the redirect comes back");
{
  attempt("good redirect", () => parseRedirect(`${REDIRECT}?code=abc&state=state-123`, REDIRECT, "state-123"));
  attempt("wrong state (forged redirect)", () => parseRedirect(`${REDIRECT}?code=abc&state=attacker`, REDIRECT, "state-123"));
  attempt("registered URI + /evil (prefix match would accept)", () => parseRedirect(`${REDIRECT}/evil?code=abc&state=state-123`, REDIRECT, "state-123"));
  attempt("look-alike scheme", () => parseRedirect(`com.pnc.mobile.evil:/oauth2redirect?code=abc&state=state-123`, REDIRECT, "state-123"));
  info("A startsWith() check would have accepted the /evil redirect. Show it:  " + `${REDIRECT}/evil?code=abc`.startsWith(REDIRECT));
}
