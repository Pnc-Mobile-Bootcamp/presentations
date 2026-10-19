// Module 18, Lesson 5: API Authorization: Scopes, Roles, and Object-Level Checks

export interface Principal {
    sub: string;
    scopes: string[];
    roles: string[];
}
export interface Account {
    id: string;
    ownerId: string;
    name: string;
    number: string;
    balanceCents: number;
}

export const accounts: Record<string, Account> = {
  "1": { id: "1", ownerId: "cust_A", name: "A checking", number: "4000111122224821", balanceCents: 125_000 },
  "2": { id: "2", ownerId: "cust_B", name: "B checking", number: "4000333344445555", balanceCents: 9_900_000 },
  "3": { id: "3", ownerId: "cust_C", name: "C savings", number: "4000777788889999", balanceCents: 42_000 },
};


export type Decision =
    | { allow: true }
    | { allow: false; status: 401 | 403 | 404; reason: string };

const deny = (status: 401 | 403 | 404, reason: string): Decision => ({
    allow: false,
    status,
    reason,
});

/**
 * Authorization for GET /accounts/{id}. Three separate questions, asked in this order:
 *  1. Who are you?                         no principal            -> 401
 *  2. Is this kind of call allowed at all?  scope accounts:read     -> 403 (function level)
 *  3. Is this object yours?                 owner or delegate       -> 404, never 403 (object level)
 * Answering 404 for someone else's account avoids confirming that the id exists.
 */
export function authorizeAccountRead(
    p: Principal | undefined,
    account: Account | undefined,
    delegations: Record<string, string[]> = {},
): Decision {
    if (!p) return deny(401, "authentication required");

    if (!p.scopes.includes("accounts:read"))
        return deny(403, "missing scope accounts:read");

    if (!account) return deny(404, "not found");

    const isOwner = account.ownerId === p.sub;
    const isDelegate = (delegations[p.sub] ?? []).includes(account.ownerId); 
    // for example a family member or an advisor assignment

    if (!isOwner && !isDelegate) return deny(404, "not found");

    return { allow: true };
}

/** A transfer may only start from an account the caller owns. The destination may be anyone's. */
export function authorizeTransferCreate(
    p: Principal | undefined,
    fromAccount: Account | undefined,
): Decision {
    if (!p) return deny(401, "authentication required");

    if (!p.scopes.includes("transfers:create"))
        return deny(403, "missing scope transfers:create");
    
    if (!fromAccount || fromAccount.ownerId !== p.sub)
        return deny(404, "not found");
    
    return { allow: true };
}

/** Administrative functions need a role, not just a valid login. */
export function authorizeAdmin(p: Principal | undefined): Decision {
    if (!p) return deny(401, "authentication required");
    if (!p.roles.includes("admin")) return deny(403, "admin role required");
    return { allow: true };
}

/** Property level: the full account number is returned only with an explicit scope; everyone else gets it masked. */
export function presentAccount(
    p: Principal,
    a: Account,
): { id: string; name: string; number: string; balanceCents: number } {
    const full = p.scopes.includes("accounts:full_number");
    return {
        id: a.id,
        name: a.name,
        number: full ? a.number : "****" + a.number.slice(-4),
        balanceCents: a.balanceCents,
    };
}


