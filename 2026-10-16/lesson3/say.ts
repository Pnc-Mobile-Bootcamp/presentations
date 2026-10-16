// Tiny console helpers so demo output reads well on a projector.
export const banner = (t: string) => console.log(`\n=== ${t} ===`);
export const step = (t: string) => console.log(`\n> ${t}`);
export const ok = (t: string) => console.log(`  [ACCEPTED] ${t}`);
export const bad = (t: string) => console.log(`  [REFUSED ] ${t}`);
export const info = (t: string) => console.log(`    ${t}`);
/** Run fn; report ACCEPTED with its result, or REFUSED with the error code/message. */
export function attempt(label: string, fn: () => unknown): boolean {
  try { const r = fn(); ok(`${label}  ->  ${typeof r === "string" ? r : JSON.stringify(r)}`); return true; }
  catch (e) { const x = e as { code?: string; reason?: string; message: string }; bad(`${label}  ->  ${x.code ?? x.reason ?? x.message}`); return false; }
}
