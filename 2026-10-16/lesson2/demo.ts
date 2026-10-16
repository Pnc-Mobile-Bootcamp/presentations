import { buildAuthorizationUrl, codeChallengeS256, generateCodeVerifier } from './pkce.ts'
import { MockAuthServer } from './mock_auth_server.ts'

const base = {
    authEndpoint: "https://login.pnc.example/authorize",
    clientId: "pnc-mobile",
    redirectUri: "com.pnc.mobile:/oauth2redirect",
    scopes: ["openid"],
    state: "s1",
    nonce: "n1"
};

// Scenario 1: no PKCE
const weakServer = new MockAuthServer(false);

const url = buildAuthorizationUrl({ ...base, codeChallenge: 'x' })
            .replace(/&code_challenge=[^&]*&code_challenge_method=S256/, "")

// ask the OAuth server to authorize our app
const response = weakServer.authorize(url)

// if the client device is compromised,
// both our app and the malicious app would receive this response

console.log("both apps receive: ", response)

const stolenToken = new URL(response).searchParams.get("code")!

// malicious app can use the API
console.log("interceptor gets: ", weakServer.token({ 
    grant_type: "authorization_code", 
    code: stolenToken
}))

console.log()
console.log("--------------------------------")
console.log()

// scenario 2: with PKCE

const strongServer = new MockAuthServer(true)
const verifier = generateCodeVerifier()

const url2 = buildAuthorizationUrl({
    ...base,
    codeChallenge: codeChallengeS256(verifier)
})
const response2 = strongServer.authorize(url2)

// a compromised device could have another app
// that receives the response2
// but that app would NOT have our "verifier"

const code2 = new URL(response2).searchParams.get("code")!;

// this hypothetical other app may have our code
// we will try three times to get a token
// once with no verifier, second with a new verifier
// finally with our verifier
for (const v of [undefined, generateCodeVerifier(), verifier, verifier]) {
    console.log(v === verifier ? "real app: " : "interceptor: ")
    try {
        const token2 = strongServer.token({
            grant_type: "authorization_code",
            code: code2,
            ...(v ? { code_verifier: v} : {})
        })
        console.log("token = ", token2)
    }
    catch (e) {
        console.log("server refused: ", (e as Error).message)
    }
}


