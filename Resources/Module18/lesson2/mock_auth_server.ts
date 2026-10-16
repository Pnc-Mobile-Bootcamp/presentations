// An in-memory stand-in for the bank's authorization server. Teaching only.
// requirePkce=false models a server that ignores PKCE (the pre-RFC 7636 world).
import { createHash, randomBytes } from "node:crypto";

interface CodeRecord { clientId: string; redirectUri: string; challenge?: string; used: boolean }

export class MockAuthServer {
  private codes = new Map<string, CodeRecord>();
  tokenCalls = 0;
  private requirePkce: boolean;
  constructor(requirePkce: boolean) { this.requirePkce = requirePkce; }

  /** Leg 1: the user signs in (assumed) and the server redirects back with a short-lived code. */
  authorize(authUrl: string): string {
    const u = new URL(authUrl);
    const redirectUri = u.searchParams.get("redirect_uri")!;
    const challenge = u.searchParams.get("code_challenge") ?? undefined;
    if (this.requirePkce && !challenge) throw new Error("invalid_request: code_challenge required");
    const code = randomBytes(8).toString("hex");
    this.codes.set(code, { clientId: u.searchParams.get("client_id")!, redirectUri, challenge, used: false });
    const back = new URL(redirectUri);
    back.searchParams.set("code", code);
    back.searchParams.set("state", u.searchParams.get("state")!);
    return back.toString();
  }

  /** Leg 2: whoever holds the code (and, with PKCE, the verifier) gets tokens. */
  token(form: Record<string, string>): { access_token: string; refresh_token: string } {
    this.tokenCalls++;
    const rec = this.codes.get(form.code);
    if (!rec || rec.used) throw new Error("invalid_grant: unknown or used code");
    if (rec.challenge) {
      const v = form.code_verifier;
      if (!v) throw new Error("invalid_grant: code_verifier missing");
      const expect = createHash("sha256").update(v, "ascii").digest("base64url");
      if (expect !== rec.challenge) throw new Error("invalid_grant: code_verifier does not match challenge");
    }
    rec.used = true;
    return { access_token: "ACCESS-" + randomBytes(4).toString("hex"), refresh_token: "REFRESH-" + randomBytes(4).toString("hex") };
  }
}
