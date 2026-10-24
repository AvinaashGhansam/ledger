# Backlog

Non-blocking findings raised during LSB-1 review. None of these gate a rung's advancement — logged here so they aren't lost across sessions or contributors.

## LSB-1 — Domain Core

1. **`Ledger.balance` throws on a multi-currency ledger.** *(Deferred to LSB-2 — structural fix, not a patch here.)*

   `balance(usdAccount, [usdPosting, eurPosting])` → `IllegalArgumentException`. Mechanism: `balanceFor` folds an uninvolved posting down to `Money.zero` seeded with *that posting's own* currency, so the EUR posting yields zero-EUR; `Ledger.balance` then tries `zero-USD.plus(zero-EUR)` and trips `Money`'s currency guard. Holds correctly today only because every test is single-currency.

   Root cause is shared with the `UnknownAccount` design note in `Progress.md`: once LSB-2 enforces "an entry's currency must match its account's currency," an account's balance is provably single-currency and this class of bug closes structurally rather than needing a guard here. The choice to type `Ledger.balance` as `(List<Posting>, Account)` instead of the originally-specified `AccountId` was the right instinct — you need the currency to seed the zero — it just hasn't been carried all the way through yet. Pull this thread at LSB-2, not before.

2. **Mixed-currency `Posting` test asserts type only, not payload.** *(Small — test quality.)*

   `create_mixedCurrencies_returnsErrCurrencyMismatch` checks `isInstanceOf(PostingError.CurrencyMismatch.class)` but never reads `.expected()`/`.found()`. That payload was modeled specifically so a caller could act on which currencies collided — assert it, the same way the `Unbalanced` test already asserts `.residual()`.

3. **B2 proves net-zero, not per-account correctness.** *(Small — test quality.)*

   The conservation test proves the *sum* across all accounts nets to zero, but that property can hold even if the per-account math is symmetrically wrong (e.g. checking and savings both off by the same amount in opposite directions). Strengthen it to also assert each account's individual balance directly — checking is `-700`, savings is `+700` — before summing them. Net-zero is the weaker check; both together is the real proof.

## Cross-repo — raised from LCX-5 (frontend, 2026-09-08)

1. **`AccountDto` has no `type` field, so debit/credit can't be derived from account type.** *(Deferred to LSB-2 — structural, needs an `AccountType` field before this closes.)*

   Frontend flagged that a transfer always shows the "from" account as DEBIT and the "to" account as CREDIT, regardless of what kind of account either side is. Root cause: the mock (`ledgerline-console`'s `src/mocks/handlers.ts` POST `/api/postings`, and `fixtures.ts`'s seed data) uses the bank-statement convention — money-out leg = DEBIT + `balance -= amount`, money-in leg = CREDIT + `balance += amount` — flat, with no account-type awareness. Real general-ledger double-entry assigns debit/credit from the account's type and normal balance (Asset/Expense: debit increases; Liability/Equity/Revenue: credit increases), and every posting must satisfy Σdebits == Σcredits.

   Needs: an `AccountType` field on the account model (and its DTO), `direction` derived from `(type, increase|decrease)` instead of hardcoded per leg, and a balanced-postings invariant enforced server-side. Until then this is a known simplification, not a bug in the mock — the frontend just renders whatever `direction` the API returns. Optional frontend-side cosmetic (deferred, non-blocking): relabel `DirectionBadge` to "Out / In" to match the convention the mock data actually follows, rather than implying real double-entry semantics it doesn't have yet.
