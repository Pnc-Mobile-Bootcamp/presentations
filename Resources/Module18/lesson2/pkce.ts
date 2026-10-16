// Module 18, Block 1.2: OAuth 2.0 for Mobile: Authorization Code with PKCE

import { createHash, randomBytes, timingSafeEqual } from "node:crypto";

const b64url = (buf: Buffer): string => buf.toString("base64").replace(/\+/g, "-").replace(/\//g, "_").replace(/=+$/, "");

/** RFC 7636: a high-entropy random string of 43 to 128 characters from the unreserved set. 32 random bytes give 43 characters. */
export function generateCodeVerifier(random: (n: number) => Buffer = randomBytes): string {
  return b64url(random(32));
}

export function isValidVerifier(v: string): boolean {
  return /^[A-Za-z0-9\-._~]{43,128}$/.test(v);
}

/** S256: BASE64URL(SHA256(ASCII(code_verifier))). The plain method is not used. */
export function codeChallengeS256(verifier: string): string {
  if (!isValidVerifier(verifier)) throw new Error("Invalid code_verifier");
  return b64url(createHash("sha256").update(verifier, "ascii").digest());
}

export interface AuthRequest {
  authEndpoint: string;
  clientId: string;
  redirectUri: string;
  scopes: string[];
  state: string;
  nonce: string;
  codeChallenge: string;
}

export function buildAuthorizationUrl(r: AuthRequest): string {
  if (!r.authEndpoint.startsWith("https://")) throw new Error("Authorization endpoint must use https");
  const u = new URL(r.authEndpoint);
  u.searchParams.set("response_type", "code");
  u.searchParams.set("client_id", r.clientId);
  u.searchParams.set("redirect_uri", r.redirectUri);
  u.searchParams.set("scope", r.scopes.join(" "));
  u.searchParams.set("state", r.state);
  u.searchParams.set("nonce", r.nonce);
  u.searchParams.set("code_challenge", r.codeChallenge);
  u.searchParams.set("code_challenge_method", "S256");
  return u.toString();
}

export class OAuthRedirectError extends Error {
  code: string;
  constructor(code: string, message: string) { super(message); this.code = code; }
}

const safeEqual = (a: string, b: string): boolean => {
  const x = Buffer.from(a), y = Buffer.from(b);
  return x.length === y.length && timingSafeEqual(x, y);
};

/**
 * Validate the redirect the app receives from the authorization server.
 *  - the redirect target must EXACTLY match the registered redirect URI (ignoring only the query and fragment)
 *  - an error parameter from the server is surfaced, not ignored
 *  - state must match the value the app generated for this request (CSRF defense)
 *  - a code must be present
 */
export function parseRedirect(redirectUrl: string, registeredRedirectUri: string, expectedState: string): { code: string } {
  let got: URL, want: URL;
  try { got = new URL(redirectUrl); want = new URL(registeredRedirectUri); } catch { throw new OAuthRedirectError("bad_redirect", "Malformed redirect URL"); }
  if (got.protocol !== want.protocol || got.host !== want.host || got.pathname !== want.pathname) {
    throw new OAuthRedirectError("redirect_mismatch", "Redirect does not match the registered redirect URI");
  }
  const err = got.searchParams.get("error");
  if (err) throw new OAuthRedirectError("server_error:" + err, "Authorization failed: " + err);
  const state = got.searchParams.get("state");
  if (state === null || !safeEqual(state, expectedState)) throw new OAuthRedirectError("state_mismatch", "State does not match");
  const code = got.searchParams.get("code");
  if (!code) throw new OAuthRedirectError("missing_code", "No authorization code in the redirect");
  return { code };
}

