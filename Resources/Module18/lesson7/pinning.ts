// Module 18, Block 2.3: Hardening the Mobile App: Storage, Pinning, and Attestation

import { X509Certificate, createHash } from "node:crypto";

/** The standard pin: base64 of the SHA-256 of the certificate's SubjectPublicKeyInfo (SPKI) in DER form. */
export function spkiPin(certPem: string): string {
    const der = new X509Certificate(certPem).publicKey.export({
        type: "spki",
        format: "der",
    });
    return createHash("sha256").update(der).digest("base64");
}

export interface PinPolicy {
    pins: string[]; // base64 SHA-256 SPKI pins; must include at least one backup pin
    expiresAt?: number; // epoch ms; after this date pinning is switched off so a missed rotation cannot lock customers out
}

export type PinResult =
    | { ok: true; mode: "pinned"; matched: string }
    | { ok: true; mode: "pinning-expired" }
    | { ok: false; reason: "no-pin-match" | "policy-invalid" };

/**
 * Decide whether a TLS certificate chain is acceptable under a pin policy. This runs AFTER the normal chain
 * validation; pinning only narrows trust, it never replaces it.
 *  - a policy needs at least two distinct pins (one live, one backup) or it is rejected as invalid
 *  - after expiresAt pinning is disabled (the platform falls back to normal validation)
 *  - otherwise at least one certificate in the chain must match a pin
 */
export function checkPins(
    chainPems: string[],
    policy: PinPolicy,
    nowMs: number,
): PinResult {

    // need at least two pins (live and backup)
    if (new Set(policy.pins).size < 2)
        return { ok: false, reason: "policy-invalid" };

    if (policy.expiresAt !== undefined && nowMs >= policy.expiresAt)
        return { ok: true, mode: "pinning-expired" };

    for (const pem of chainPems) {
        const pin = spkiPin(pem);
        if (policy.pins.includes(pin))
            return { ok: true, mode: "pinned", matched: pin };
    }

    return { ok: false, reason: "no-pin-match" };
}
