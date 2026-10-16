// Block 1.3 live demo: Forge a Token.   Run:  node demo.ts
import { generateKeyPairSync, sign } from "node:crypto";
import { validateJwt, forgeHs256WithPublicKey } from "./jwt.ts";
import type { Jwk } from "./jwt.ts";
import { decodeOnly, trustTheHeader, signatureOnly } from "./naive_validators.ts";
import { banner, attempt, info } from "./say.ts";

const ISS = "https://login.pnc.example", AUD = "pnc-mobile", NOW = 1_800_000_000;
const { publicKey, privateKey } = generateKeyPairSync("rsa", { modulusLength: 2048 });
const jwk: Jwk = { ...publicKey.export({ format: "jwk" }), kid: "k1", alg: "RS256", use: "sig" };
const pem = publicKey.export({ type: "spki", format: "pem" }).toString();
const enc = (o: unknown) => Buffer.from(JSON.stringify(o)).toString("base64url");
const mint = (claims: Record<string, unknown>) => {
  const input = enc({ alg: "RS256", typ: "JWT", kid: "k1" }) + "." + enc(claims);
  return input + "." + sign("RSA-SHA256", Buffer.from(input), privateKey).toString("base64url");
};
const good = { iss: ISS, aud: AUD, sub: "cust_1", exp: NOW + 300, iat: NOW - 10, nonce: "n1" };
const correct = (t: string) => validateJwt(t, { keys: [jwk], issuer: ISS, audience: AUD, nowSec: NOW, nonce: "n1" });

// The three forgeries and one wrong-audience token.
const realToken = mint(good);
const parts = realToken.split(".");
const tampered = [parts[0], enc({ ...good, sub: "cust_2" }), parts[2]].join(".");           // 1. changed sub, old signature
const algNone = enc({ alg: "none", typ: "JWT" }) + "." + enc({ ...good, sub: "admin" }) + "."; // 2. unsigned
const confused = forgeHs256WithPublicKey({ ...good, sub: "admin" }, pem);                      // 3. HS256 keyed with the PUBLIC key
const otherApi = mint({ ...good, aud: "pnc-statements-api" });                                 // 4. valid signature, wrong audience

const cases: [string, string][] = [["tampered payload (sub -> cust_2)", tampered], ["alg none (sub -> admin)", algNone], ["HS256 confusion (sub -> admin)", confused]];

banner("Round 1: decode-only validator");
for (const [n, t] of cases) attempt(n, () => decodeOnly(t).sub as string);

banner("Round 2: validator that trusts the alg in the header");
for (const [n, t] of cases) attempt(n, () => trustTheHeader(t, jwk, pem).sub as string);

banner("Round 3: the correct validator (validateJwt)");
attempt("genuine token", () => correct(realToken).sub as string);
for (const [n, t] of cases) attempt(n, () => correct(t).sub as string);

banner("Round 4: audience. Same issuer, same key, token meant for another API");
info("Signature-only validator (forgot iss/aud):");
attempt("token for pnc-statements-api presented to pnc-mobile", () => signatureOnly(otherApi, jwk).sub as string);
info("Correct validator:");
attempt("token for pnc-statements-api presented to pnc-mobile", () => correct(otherApi).sub as string);

info("\nTalking point: HS256 confusion works because the attacker only needs the PUBLIC key, which the issuer publishes on purpose.");
