import type { TxContext } from "./stepup.ts";
import { decideStepUp } from "./stepup.ts";
import { header } from './utilities.ts'


const base: TxContext = {
    amountCents: 20_000,
    newPayee: false,
    deviceTrusted: true,
    authAgeSec: 60,
    amr: ["pwd"],
    riskScore: 10
}

const run = (label: string, t: TxContext) => {
    console.log(`${label.padEnd(55)} ${JSON.stringify(decideStepUp(t))}`)
}


header("Same customer, same login. Only the transaction context changes.")

run('1.  $200, known payee, trusted device, fresh login', base)

run('2.  $600 (over 500 mid-level threshold)', {
    ...base,
    amountCents: 60_000
})

run('3.  $600 after a biometric 1 minute ago', {
    ...base,
    amountCents: 60_000,
    amr: ["pwd", "bio"]
})

run('4.  $6,000 after a biometric 1 minute ago', {
    ...base,
    amountCents: 600_000,
    amr: ["pwd", "bio"]
})

run('5.  $6,000 with a fresh OTP', {
    ...base,
    amountCents: 600_000,
    amr: ["pwd", "otp"]
})

run('6.  risk score 95', {
    ...base,
    riskScore: 95
})



header("Bonus: lack of freshness is not token expiry")

run('7.  New payee, strong method, but used 20 minutes ago', {
    ...base,
    newPayee: true,
    amr: ['pwd', 'bio'],
    authAgeSec: 1200
})


