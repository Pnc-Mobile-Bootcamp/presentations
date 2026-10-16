// Block 1.4 live demo: Stolen Refresh Token.   Run:  node demo.ts
import { RefreshTokenStore } from "./refresh_store.ts";
import { banner, step, info, attempt } from "./say.ts";

function world(graceMs: number) {
  let t = 1_000_000, n = 0;
  const store = new RefreshTokenStore({ now: () => t, newToken: () => "T" + ++n, graceMs });
  return { store, advance: (ms: number) => { t += ms; } };
}

banner("Scene 1: strict reuse detection (grace 0). Thief replays a copied token.");
{
  const { store, advance } = world(0);
  const T1 = store.issue("cust_1", "mobile");
  step("Customer signs in. Thief copies T1 from the device (backup, malware, rooted phone).");
  const T2 = store.rotate(T1, "mobile").token;
  info("Real app refreshes: T1 -> " + T2 + ". T1 is now marked rotated.");
  advance(60_000);
  attempt("thief replays T1", () => store.rotate(T1, "mobile").token);
  attempt("real app now presents its newest token T2", () => store.rotate(T2, "mobile").token);
  info("Reuse detected: the whole family was revoked. The customer must sign in again; the bank got a signal.");
  info("Does the store hold plaintext tokens? " + store.storedValuesContain("T1"));
}

banner("Scene 2: 5 second grace window. A lost response is tolerated.");
{
  const { store, advance } = world(5_000);
  const T1 = store.issue("cust_1", "mobile");
  store.rotate(T1, "mobile");
  info("Server rotated T1, but the response was lost in a tunnel. App retries with T1 two seconds later.");
  advance(2_000);
  attempt("retry inside the window", () => store.rotate(T1, "mobile").token);
  advance(10_000);
  attempt("replay of T1 ten seconds later (outside the window)", () => store.rotate(T1, "mobile").token);
  info("Trade-off to discuss: a thief who acts inside the window also succeeds. Keep it short and log every use.");
}

banner("Scene 3: sign-out must revoke on the server");
{
  const { store } = world(0);
  const T1 = store.issue("cust_1", "mobile");
  store.revoke(T1);
  attempt("copied token after the user signs out", () => store.rotate(T1, "mobile").token);
  info("Deleting the token on the phone alone would NOT have stopped the copy. (This app has no sign-out at all; see app demo 1.4b.)");
}
