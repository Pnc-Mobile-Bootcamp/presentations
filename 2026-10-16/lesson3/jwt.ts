// Module 18, Block 1.3: OpenID Connect and JWT Validation

import { createHmac, createPublicKey, verify as cryptoVerify } from "node:crypto";
import type { JsonWebKey } from "node:crypto";

export interface Jwk extends JsonWebKey { kid?: string; alg?: string; use?: string }

export interface ValidationOptions {
  keys: Jwk[];                 // the issuer's public keys, from its JWKS document
  issuer: string;
  audience: string;            // this app's client id (ID token) or this API's identifier (access token)
  algorithms?: string[];       // allow list; defaults to RS256 only
  nowSec: number;              // injected clock, seconds since the epoch
  clockSkewSec?: number;
  nonce?: string;              // expected nonce for an OpenID Connect ID token
}

export class JwtError extends Error {
  code: string;
  constructor(code: string, message: string) { super(message); this.code = code; }
}

const b64urlToBuf = (s: string): Buffer => Buffer.from(s.replace(/-/g, "+").replace(/_/g, "/"), "base64");
const parseJson = (part: string, what: string): Record<string, unknown> => {
  try { const v = JSON.parse(b64urlToBuf(part).toString("utf8")); if (v && typeof v === "object" && !Array.isArray(v)) return v; } catch { /* fall through */ }
  throw new JwtError("malformed", "Malformed " + what);
};

/**
 * Validate a signed JWT (RS256) and return its claims.
 * Order matters: decide the algorithm from YOUR allow list and YOUR key, never from the token, then verify the
 * signature, and only then trust and check the claims.
 */
export function validateJwt(token: string, opts: ValidationOptions): Record<string, unknown> {
  const skew = opts.clockSkewSec ?? 60;
  const allowed = opts.algorithms ?? ["RS256"];
  const parts = token.split(".");
  if (parts.length !== 3) throw new JwtError("malformed", "A JWT has three parts");
  const header = parseJson(parts[0], "header");
  const alg = header.alg;
  if (typeof alg !== "string" || !allowed.includes(alg)) throw new JwtError("alg_not_allowed", "Algorithm not allowed: " + String(alg));   // rejects none and HS256 confusion

  const kid = header.kid;
  const candidates = opts.keys.filter((k) => k.kty === "RSA" && (kid === undefined || k.kid === kid) && (k.alg === undefined || k.alg === alg));
  if (candidates.length === 0) throw new JwtError("unknown_key", "No matching key for kid " + String(kid));
  const signingInput = Buffer.from(parts[0] + "." + parts[1], "ascii");
  const signature = b64urlToBuf(parts[2]);
  const ok = candidates.some((jwk) => {
    try { return cryptoVerify("RSA-SHA256", signingInput, createPublicKey({ key: jwk, format: "jwk" }), signature); } catch { return false; }
  });
  if (!ok) throw new JwtError("bad_signature", "Signature verification failed");

  const claims = parseJson(parts[1], "payload");
  if (claims.iss !== opts.issuer) throw new JwtError("bad_issuer", "Unexpected issuer");
  const aud = claims.aud;
  const audOk = typeof aud === "string" ? aud === opts.audience : Array.isArray(aud) && aud.includes(opts.audience);
  if (!audOk) throw new JwtError("bad_audience", "Token was not issued for this audience");
  if (typeof claims.exp !== "number") throw new JwtError("missing_exp", "Token has no expiry");
  if (opts.nowSec - skew >= claims.exp) throw new JwtError("expired", "Token has expired");
  if (typeof claims.nbf === "number" && opts.nowSec + skew < claims.nbf) throw new JwtError("not_yet_valid", "Token is not valid yet");
  if (typeof claims.iat === "number" && claims.iat > opts.nowSec + skew) throw new JwtError("issued_in_future", "Token was issued in the future");
  if (opts.nonce !== undefined && claims.nonce !== opts.nonce) throw new JwtError("bad_nonce", "Nonce does not match");
  return claims;
}

// Only for tests and teaching: shows how an algorithm-confusion attack token is built.
export function forgeHs256WithPublicKey(claims: Record<string, unknown>, publicKeyText: string): string {
  const enc = (o: unknown) => Buffer.from(JSON.stringify(o)).toString("base64url");
  const input = enc({ alg: "HS256", typ: "JWT" }) + "." + enc(claims);
  return input + "." + createHmac("sha256", publicKeyText).update(input).digest("base64url");
}

