// A deliberately vulnerable GET /accounts/{id} handler for the "Walk the Account ID" demo.
import { authorizeAccountRead, presentAccount } from "./authz.ts";
import type { Account, Principal } from "./authz.ts";

export const accounts: Record<string, Account> = {
  "1": { id: "1", ownerId: "cust_A", name: "A checking", number: "4000111122224821", balanceCents: 125_000 },
  "2": { id: "2", ownerId: "cust_B", name: "B checking", number: "4000333344445555", balanceCents: 9_900_000 },
  "3": { id: "3", ownerId: "cust_C", name: "C savings", number: "4000777788889999", balanceCents: 42_000 },
};
export const delegations: Record<string, string[]> = { cust_A: ["cust_C"] }; // A is C's delegate (e.g. power of attorney)

type Response = { status: number; body: unknown };

/** BEFORE: checks that you are signed in (a valid token) and nothing else. */
export function getAccountVulnerable(p: Principal | undefined, id: string): Response {
  if (!p) return { status: 401, body: "authentication required" };
  const a = accounts[id];
  return a ? { status: 200, body: a } : { status: 404, body: "not found" };
}

/** AFTER: scope, ownership/delegation, then property-level masking. */
export function getAccountFixed(p: Principal | undefined, id: string): Response {
  const a = accounts[id];
  const d = authorizeAccountRead(p, a, delegations);
  if (!d.allow) return { status: d.status, body: d.reason };
  return { status: 200, body: presentAccount(p!, a) };
}

/** The tempting-but-wrong variant, to compare status codes: 403 for a foreign object confirms the id exists. */
export function getAccountForbidden(p: Principal | undefined, id: string): Response {
  const a = accounts[id];
  if (!p) return { status: 401, body: "authentication required" };
  if (!a) return { status: 404, body: "not found" };
  if (a.ownerId !== p.sub) return { status: 403, body: "forbidden" };
  return { status: 200, body: a };
}
