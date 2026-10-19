// Module 18, Lesson 6: Step-Up Authentication, Biometrics, and Passkeys

export interface TxContext {
    amountCents: number;
    newPayee: boolean;
    deviceTrusted: boolean;
    authAgeSec: number; // seconds since the user last authenticated (the auth_time claim)
    amr: string[]; // authentication methods used: "pwd", "bio", "otp", "mfa"
    riskScore: number; // 0 to 100 from the fraud engine
}

export type StepUpDecision =
    | { decision: "allow"; reason: string }
    | { decision: "step_up"; method: "biometric" | "otp"; reason: string }
    | { decision: "deny"; reason: string };

const STRONG = ["bio", "otp", "mfa"];

/**
 * Risk-based step-up policy for POST /transfers. Rules, in order:
 *  1. risk >= 90                                  -> deny
 *  2. amount > 5,000.00                           -> needs an out-of-band OTP used within the last 5 minutes
 *  3. new payee, amount > 500.00, risk >= 50, or an untrusted device
 *                                                 -> needs any strong method (bio, otp, mfa) used within the last 5 minutes
 *  4. otherwise                                   -> allow, provided the user authenticated within the last 15 minutes
 * When a step-up is needed the method is biometric on a trusted device and OTP otherwise.
 */
export function decideStepUp(t: TxContext): StepUpDecision {
    
    // a hard-ceiling of 90 on the risk score
    if (t.riskScore >= 90) return { decision: "deny", reason: "risk too high" };

    // a function to determine if auth is fresh enough
    // (with a variable age cutoff parameter)
    const fresh = (maxAge: number) => t.authAgeSec <= maxAge;

    // was a strong auth method used?
    const strongMethod = t.deviceTrusted ? "biometric" : "otp";

    // strictest rules for large transactions (> $5,000.00)
    if (t.amountCents > 500_000) {
        if (t.amr.includes("otp") && fresh(300))
            return { decision: "allow", reason: "high value with fresh otp" };
        return {
            decision: "step_up",
            method: "otp",
            reason: "high value requires otp",
        };
    }
    // middle-tier rules for riskier scenarios
    if (
        t.newPayee ||
        t.amountCents > 50_000 ||
        t.riskScore >= 50 ||
        !t.deviceTrusted
    ) {
        if (t.amr.some((m) => STRONG.includes(m)) && fresh(300))
            return {
                decision: "allow",
                reason: "strong recent authentication",
            };
        return {
            decision: "step_up",
            method: strongMethod,
            reason: "strong recent authentication required",
        };
    }
    // loosest rules for least risky transactions
    if (!fresh(900))
        return {
            decision: "step_up",
            method: strongMethod,
            reason: "session too old",
        };
    return { decision: "allow", reason: "low risk" };
}

