// Module 18, Block 1.4: Token Lifecycle: Refresh Rotation, Revocation, and Sign-Out

import { createHash, randomBytes } from "node:crypto";

export type Reason = "invalid" | "expired" | "revoked" | "reuse_detected";
export class RefreshError extends Error {
  reason: Reason;
  constructor(reason: Reason) { super("Refresh rejected: " + reason); this.reason = reason; }
}

interface Entry {
  familyId: string; userId: string; clientId: string;
  status: "active" | "rotated" | "revoked";
  rotatedAt?: number;
}
interface Family { id: string; createdAt: number; lastUsedAt: number; revoked: boolean }

export interface StoreOptions {
  now: () => number;                    // milliseconds
  newToken?: () => string;
  idleTtlMs?: number;                   // unused for this long: expired
  absoluteTtlMs?: number;               // maximum age of a login, however active
  graceMs?: number;                     // a rotated token presented again within this window is tolerated (lost response)
}

const hash = (t: string): string => createHash("sha256").update(t).digest("hex");

/**
 * Server-side refresh token store with rotation and reuse detection.
 *  - only a hash of each token is stored
 *  - every use issues a new token and marks the old one rotated
 *  - presenting a rotated token again, after the grace window, is treated as theft: the whole family is revoked
 */
export class RefreshTokenStore {
  private entries = new Map<string, Entry>();
  private families = new Map<string, Family>();
  private o: Required<StoreOptions>;
  private seq = 0;

  constructor(opts: StoreOptions) {
    this.o = { newToken: () => randomBytes(32).toString("base64url"), idleTtlMs: 30 * 24 * 3600_000, absoluteTtlMs: 90 * 24 * 3600_000, graceMs: 0, ...opts };
  }

  issue(userId: string, clientId: string): string {
    const familyId = "fam_" + ++this.seq;
    const now = this.o.now();
    this.families.set(familyId, { id: familyId, createdAt: now, lastUsedAt: now, revoked: false });
    return this.mint(familyId, userId, clientId);
  }

  private mint(familyId: string, userId: string, clientId: string): string {
    const token = this.o.newToken();
    this.entries.set(hash(token), { familyId, userId, clientId, status: "active" });
    return token;
  }

  rotate(token: string, clientId: string): { token: string; userId: string } {
    const entry = this.entries.get(hash(token));
    if (!entry || entry.clientId !== clientId) throw new RefreshError("invalid");
    const fam = this.families.get(entry.familyId)!;
    const now = this.o.now();
    if (fam.revoked || entry.status === "revoked") throw new RefreshError("revoked");
    if (now - fam.createdAt > this.o.absoluteTtlMs || now - fam.lastUsedAt > this.o.idleTtlMs) throw new RefreshError("expired");
    if (entry.status === "rotated") {
      if (this.o.graceMs > 0 && entry.rotatedAt !== undefined && now - entry.rotatedAt <= this.o.graceMs) {
        fam.lastUsedAt = now;
        return { token: this.mint(entry.familyId, entry.userId, entry.clientId), userId: entry.userId };   // lost response: tolerate once within grace
      }
      this.revokeFamily(entry.familyId);
      throw new RefreshError("reuse_detected");
    }
    entry.status = "rotated";
    entry.rotatedAt = now;
    fam.lastUsedAt = now;
    return { token: this.mint(entry.familyId, entry.userId, entry.clientId), userId: entry.userId };
  }

  revokeFamily(familyId: string): void {
    const fam = this.families.get(familyId);
    if (fam) fam.revoked = true;
    for (const e of this.entries.values()) if (e.familyId === familyId) e.status = "revoked";
  }

  /** Sign out: revoke the family the presented token belongs to. Unknown tokens are ignored so logout never leaks information. */
  revoke(token: string): void {
    const entry = this.entries.get(hash(token));
    if (entry) this.revokeFamily(entry.familyId);
  }

  storedValuesContain(token: string): boolean {
    return [...this.entries.keys()].some((k) => k === token);     // true only if a plaintext token was stored: it never is
  }
}

