import { certs } from './cert_fixtures.ts'
import { createHash, X509Certificate } from 'node:crypto'
import { spkiPin, checkPins } from './pinning.ts'
import { header } from './utilities.ts'


const NOW = Date.parse("2026-10-10T00:00:00Z")

const leaf = spkiPin(certs.leaf)
const inter = spkiPin(certs.intermediate)
const backup = spkiPin(certs.backup)


header("1. The pin is the hash of the PUBLIC KEY, not of the certificate")

const rawLeafCertificate = (new X509Certificate(certs.leaf)).raw
const hash = createHash("sha256").update(rawLeafCertificate).digest("base64")

console.log("SHA-256 of the whole certificate: ", hash)
console.log("SPKI pin (what Android and OkHttp use): ", leaf);
console.log("SPKI pin from intermediate: ", inter)


// these are different on purpose. so that renewing a cert with the SAME key
// keeps the same pin

// create a display function
const show = (label: string, r: unknown) => {
    console.log(`${label.padEnd(56)} ${JSON.stringify(r)}`)
}


header("2. checkPins outcomes")

show('real chain, pins [intermediate, backup]', 
    checkPins(
        [certs.leaf, certs.intermediate],
        { pins: [inter, backup] },
        NOW
    )
)
console.log()

show('attacker chain', 
    checkPins(
        [certs.attacker],
        { pins: [inter, backup]},
        NOW
    )
)
console.log()

show('leaf rotated to a new key', 
    checkPins(
        [certs.backup, certs.intermediate],
        { pins: [leaf, inter]},
        NOW
    )
)
console.log()


show('expires in the past', 
    checkPins(
        [certs.backup, certs.intermediate],
        { 
            pins: [leaf, inter],
            expiresAt: NOW - 1
        },
        NOW
    )
)
console.log()


show('policy with ONE pin (no backup)',
    checkPins(
        [certs.intermediate, certs.backup],
        { pins: [leaf] },
        NOW
    )
)






