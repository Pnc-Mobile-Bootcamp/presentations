// Deliberately BROKEN validators for the "Forge a Token" demo. Never ship these.
import { createHmac, createPublicKey, verify } from "node:crypto";
import type { Jwk } from "./jwt.ts";

const b64 = (s: string) => Buffer.from(s.replace(/-/g, "+").replace(/_/g, "/"), "base64");
const json = (s: string) => JSON.parse(b64(s).toString("utf8"));

/** Flaw 1: decode only. Reads the claims and never checks the signature. */
export function decodeOnly(token: string): Record<string, unknown> {
  return json(token.split(".")[1]);
}

/** Flaws 2 and 3: believes the alg in the token header. Accepts "none", and HMAC-verifies with the public key text. */
export function trustTheHeader(token: string, jwk: Jwk, publicKeyPem: string): Record<string, unknown> {
  const [h, p, s] = token.split(".");
  const header = json(h);
  const input = h + "." + p;
  if (header.alg === "none") return json(p);                                   // flaw 2: no signature needed
  if (header.alg === "HS256") {                                                  // flaw 3: algorithm confusion
    const mac = createHmac("sha256", publicKeyPem).update(input).digest("base64url");
    if (mac !== s) throw new Error("bad signature");
    return json(p);
  }
  const ok = verify("RSA-SHA256", Buffer.from(input), createPublicKey({ key: jwk, format: "jwk" }), b64(s));
  if (!ok) throw new Error("bad signature");
  return json(p);
}

/** Flaw 4: verifies the signature properly, then forgets issuer and audience. */
export function signatureOnly(token: string, jwk: Jwk): Record<string, unknown> {
  const [h, p, s] = token.split(".");
  if (!verify("RSA-SHA256", Buffer.from(h + "." + p), createPublicKey({ key: jwk, format: "jwk" }), b64(s))) throw new Error("bad signature");
  return json(p);
}
