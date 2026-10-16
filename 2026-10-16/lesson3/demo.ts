import { generateKeyPairSync, sign } from "node:crypto"
import { validateJwt, forgeHs256WithPublicKey } from './jwt.ts'
import type { Jwk } from './jwt.ts'
import { trustTheHeader, signatureOnly } from './naive_validators.ts'


const ISSUER = "https://login.pnc.example";
const AUDIENCE = "pnc-mobile";
const NOW = 1_000_000_000;

const { publicKey, privateKey } = generateKeyPairSync("rsa", { modulusLength: 2048 });

const jwk = {
    ...publicKey.export({format: "jwk"}),
    kid: "k1",
    alg: "RS256"
 };

const pem = publicKey.export({format: "pem", type: "spki"}).toString()

const enc = (o: unknown) => Buffer.from(JSON.stringify(o)).toString("base64url")

const good = {
    iss: ISSUER,
    aud: AUDIENCE,
    sub: "cust_1",
    exp: NOW + 300,
    iat: NOW - 10
};

// format: header(settings).(jwt content).(signature)
const settings = {
    alg: "RS256",
    typ: "JWT",
    kid: "k1"
}
const input = enc(settings) + "." + enc(good)

const signature = sign("RSA-SHA256", Buffer.from(input), privateKey)

const real = input + "." + signature.toString("base64url")

// suppose we are in the API and we receive <real> - can we trust it? is it accurate?


// scenario 1 - decode only
const decodeOnly = (t: string) => JSON.parse(Buffer.from(t.split(".")[1], "base64url").toString())

const tampered = real.split('.')[0] + "." + enc( { ...good, sub: "admin" } ) + "." + real.split(".")[2];

console.log("server decode-only accepts the tampered object:", decodeOnly(tampered).sub)


// scenario 2 
// attacker not only modifies the payload (JWT) to identify as someone else
// they also modify the settings to claim no encryption
const algNone = enc({alg: "none", typ: "JWT"}) + "." + enc({ ...good, sub: "admin" }) + "."

console.log("trusting the header accepts the tampered object:", trustTheHeader(algNone, jwk, pem).sub)


// scenario 3
// attacker will modify the payload
// will create a new hash and encrypt it - using our public key! (also use a single-key encryption)
// will modify the settings to mention the hash - list the single-key encryption alg used
// (hs256 confusion attack)
const confused = forgeHs256WithPublicKey({...good, sub: "admin"}, pem)

console.log("trusting the header for confustion attack:", trustTheHeader(confused, jwk, pem).sub)


const mint = (claims: Record<string, unknown>) => {
    const input = enc({alg: "RS256", typ: "JWT", kid: "k1"}) + "." + enc(claims);
    return input + "." + sign("RSA-SHA256", Buffer.from(input), privateKey).toString("base64url");
}

const otherApi = mint({ ...good, aud: "pnc-statements-api" });
// right signature, right encryption, wrong audience

console.log("correct everything except different audience (app):", trustTheHeader(otherApi, jwk, pem))






