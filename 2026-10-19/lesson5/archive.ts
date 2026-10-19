// Block 2.1 live demo: Walk the Account ID.   Run:  node demo.ts
import { getAccountVulnerable, getAccountFixed, getAccountForbidden } from "./vulnerable_handler.ts";
import { banner, info } from "../lib/say.ts";

const customerA = { sub: "cust_A", scopes: ["accounts:read"], roles: ["customer"] };
const show = (label: string, r: { status: number; body: unknown }) => console.log(`  ${label.padEnd(26)} ${r.status}  ${JSON.stringify(r.body)}`);

banner("Round 1: vulnerable handler. Customer A's VALID token, ids 1, 2, 3, 99");
for (const id of ["1", "2", "3", "99"]) show(`GET /accounts/${id}`, getAccountVulnerable(customerA, id));
info("Every call carried a perfectly valid token. Account 2 is customer B's, with the full number.");

banner("Round 2: fixed handler. Same token, same ids (A owns 1, is delegate for 3)");
for (const id of ["1", "2", "3", "99"]) show(`GET /accounts/${id}`, getAccountFixed(customerA, id));
info("2 and 99 are now indistinguishable (both 404). Numbers are masked.");

banner("Round 3: 403 vs 404 for a foreign id. What does the attacker learn?");
for (const id of ["2", "99"]) show(`403-style /accounts/${id}`, getAccountForbidden(customerA, id));
info("403 for 2 and 404 for 99 lets the attacker enumerate which ids exist.");

banner("Round 4: property level. Reveal the full number only with an explicit scope");
show("without full_number", getAccountFixed(customerA, "1"));
show("with accounts:full_number", getAccountFixed({ ...customerA, scopes: ["accounts:read", "accounts:full_number"] }, "1"));

banner("Round 5: function level and no token");
show("no token", getAccountFixed(undefined, "1"));
show("token without scope", getAccountFixed({ ...customerA, scopes: [] }, "1"));
