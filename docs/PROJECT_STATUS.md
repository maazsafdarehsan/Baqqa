# Baqqa — Project Status

## Summary

Baqqa is an early-stage derivative of the Briar secure-messaging project. It has **not** been built, tested, or released as a standalone artifact in this environment. **Publication is blocked pending resolution of the licensing gate.**

## What works (source-observable; not executed)

* Multi-module Gradle project resolves structurally: `:baqqa-android` depends on `:baqqa-core`, the `:bramble-*` stack, and optionally the `:mailbox-*` test modules.
* `AndroidManifest.xml` declares launcher activity `org.Baqqaproject.Baqqa.android.splash.SplashScreenActivity` (`MAIN` + `LAUNCHER`).
* Application ID `org.Baqqaproject.Baqqa.android`; version `1.5.19` / code `10519`; `minSdk 21` / `targetSdk 35` / `compileSdk 35`.
* Gradle wrapper (`gradlew`, `gradlew.bat`) is present.

## What is incomplete

* End-to-end build not verified.
* No test suite executed for this fork.
* No release artefacts produced.
* Licence discrepancy unresolved (GPLv3 `LICENSE.txt` vs AGPLv3 Briar upstream).

## What was tested

**Nothing.** No build or test command was run in this environment, because:

* The `git` binary is not installed, so no authoritative baseline or diff against Briar is available.
* Running a Gradle build would require downloading dependencies (network access) and constitutes a large build; both require approval.

## Checks not yet run (and why)

| Check | Reason not run |
|-------|----------------|
| `gradlew :baqqa-android:assembleDebug` | Large build; downloads dependencies; not approved |
| `gradlew :baqqa-android:test` | Requires dependency resolution; not approved |
| `git diff` against Briar `release-1.5.19` | `git` binary not installed |
| Definitive licence determination | Upstream AGPLv3 vs repo GPLv3 `LICENSE.txt` needs maintainer/legal input |

## Unresolved blockers

1. **Licence gate (BLOCKED):** GPLv3 `LICENSE.txt` vs AGPLv3 Briar upstream; no source-file licence headers.
2. **Build not verified:** no successful build or application launch.
3. **Sensitive files in working tree:** `local.properties` (contains a machine-specific SDK path) and numerous logs/temp files must not be published.

## Next realistic milestones

1. Resolve the licence gate with upstream/legal clarity.
2. Add `.gitignore` coverage for generated files, logs, and `local.properties`.
3. Verify a debug build (`.\gradlew.bat :baqqa-android:assembleDebug`) — subject to approval.
4. Run the unit-test suite — subject to approval.
5. Prepare a clean, attributable public repository — subject to approval.

## Build result

Not built. No APK produced. No application launched.

