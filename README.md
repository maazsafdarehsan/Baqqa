# Baqqa

> **Status: early-stage fork of Briar — not built, not released.**
>
> Baqqa is a derivative of the [Briar](https://briarproject.org/) secure-messaging project. This repository is being prepared for a first public appearance. It has **not** been built, tested, or released in this environment. The licensing question has been resolved (Briar `release-1.5.19` is GPLv3, confirmed against the signed upstream tag and upstream GitLab); see [Licensing](#licensing).

## What is Baqqa?

Baqqa is a peer-to-peer messaging application for Android. It does not depend on a central messaging server: messages and content are synchronized directly between users' devices. When the internet is unavailable, devices can sync over Bluetooth or Wi-Fi; when it is available, communication can be routed over the Tor network to protect users and their relationships from surveillance.

These capabilities are **inherited from Briar upstream**. Baqqa itself is a rebranded derivative: the application ID, name, and artwork have been changed to "Baqqa". The core protocol stack and messaging functionality come from Briar.

## Current status

| Area | Status |
|------|--------|
| Committed history | Unchanged from Briar upstream `release-1.5.19` (HEAD `b46d008a`) |
| Source changes | Uncommitted working-tree modifications (rebranding, build/CI) — full diff not yet produced |
| Build | Not verified — no Gradle command has been run |
| Tests | None executed |
| Release / APK | None produced |
| Publication | Not yet released — clean snapshot prepared; approval required before any commit or push |

## Features

### Inherited from Briar (unchanged in committed history)
* Peer-to-peer, serverless messaging between devices
* Synchronisation over Bluetooth and Wi-Fi when offline
* Tor-network synchronisation when online
* Contacts, private groups, forums, and blogs
* Backup disabled (`allowBackup="false"`), non-exported services/receivers, scoped file providers

### Observable modifications in this fork
These are working-tree changes on top of Briar `release-1.5.19` and have **not** been individually verified as working:

* Rebranding to "Baqqa": application ID `org.Baqqaproject.Baqqa.android`, namespace `org.Baqqaproject.Baqqa`, version `1.5.19`
* Baqqa artwork and naming under `baqqa-android/artwork/` and `baqqa-android/fastlane/`
* Fork-specific CI configuration referencing `code.Baqqaproject.org`

> The original Briar application was **not** independently built by the fork maintainer. Briar remains the upstream project; all Briar attribution and licence notices must be preserved.

## Screenshots

No screenshots are included in this repository. The application has not been built or launched in this environment, so no UI imagery can be verified as genuine. Genuine vector artwork (SVG) is maintained under `baqqa-android/artwork/` (logos, illustrations, notification icons) and may be used as project assets, but it is not a substitute for verified screenshots.


## Architecture and modules

Baqqa is a multi-module Gradle project. Declared modules (`settings.gradle`):

| Module | Role |
|--------|------|
| `:baqqa-android` | Android application (entry point: `SplashScreenActivity`) |
| `:baqqa-core` | Baqqa core implementation |
| `:baqqa-api` | Baqqa public API / interfaces |
| `:baqqa-headless` | Baqqa headless implementation |
| `:bramble-android` | Bramble protocol stack — Android |
| `:bramble-core` | Bramble protocol stack — core |
| `:bramble-api` | Bramble public API / interfaces |
| `:bramble-java` | Bramble protocol stack — desktop / headless |
| `:mailbox-core`, `:mailbox-lib`, `:mailbox-integration-tests` | Optional mailbox integration tests (conditionally included) |

Dependency flow: `:baqqa-android` → `:baqqa-core` → `:bramble-core`/`:bramble-android`. See [`docs/ARCHITECTURE.md`](docs/ARCHITECTURE.md) for a source-observable flow description and a Mermaid diagram.

## Technology stack

* Build system: Gradle (wrapper, version 8.14.3)
* Android Gradle Plugin: 8.13.0
* `compileSdk 35`, `buildToolsVersion 35.0.0`
* `minSdk 21`, `targetSdk 35`
* Language: Java (source/target compatibility 17) and Kotlin 1.9.25
* Key libraries: AndroidX, Dagger 2.51.1, OkHttp 4.12.0, Bouncy Castle 1.82, Tor 0.4.9.11, JUnit 4, Mockito, Robolectric, Espresso

See `build.gradle` and each module's `build.gradle` for the full dependency list.


## Prerequisites and build

This project targets Windows development. Prerequisites:

* JDK 17
* Android SDK with `compileSdk 35` and `build-tools 35.0.0`
* `ANDROID_HOME` set, or a `local.properties` file containing `sdk.dir=<path>` (**never publish `local.properties`**)
* Network access to `mavenCentral()`, `google()`, and `plugins.gradle.org` to resolve dependencies

Build commands (Windows, **not yet run** — treat as requiring testing):

```powershell
.\gradlew.bat :baqqa-android:assembleDebug
.\gradlew.bat :baqqa-android:test
```

A release build cannot be completed from this repository: no signing keys or `signingConfigs` are present, and none must be added for publication. See [`docs/BUILDING.md`](docs/BUILDING.md) for Windows-oriented setup and build instructions.

## Testing and verification

No build or test has been executed in this environment. Consequently:

* No APK has been produced.
* The application has not been installed or launched.
* No test counts, pass/fail results, or coverage figures can be stated.

Checks that remain to be run (subject to approval): a debug build, the unit-test suite, and an authoritative `git diff` against Briar `release-1.5.19`. See [`docs/PROJECT_STATUS.md`](docs/PROJECT_STATUS.md) and [`docs/RELEASE_CHECKLIST.md`](docs/RELEASE_CHECKLIST.md).

## Security and privacy design

Security observations taken directly from the source (these are configuration observations, not independently verified security properties):

* `android:allowBackup="false"` — application backups disabled
* Most services, receivers, and activities are `android:exported="false"` (only the launcher entry point is exported)
* File authorities scoped to the application ID (`${applicationId}.fileprovider`, `${applicationId}.audioprovider`)
* A `networkSecurityConfig` is declared
* `FLAG_SECURE` / `setHideOverlayWindows` used on sensitive screens

No penetration testing, threat modelling, or cryptographic audit has been performed for this fork. Rebranding does not change the security model, and Briar's upstream security properties should not be assumed to carry over unchanged. See [`docs/SECURITY.md`](docs/SECURITY.md). No private security contact is published for this fork.

## Known limitations and current priorities

* Licensing discrepancy unresolved (GPLv3 `LICENSE.txt` vs AGPLv3 Briar upstream) — publication blocked
* No verified build; no executed tests
* Full change-set diff versus Briar upstream not yet produced
* Sensitive/local files present in the working tree must be excluded before publication

Current priorities: resolve the licence gate, add `.gitignore` coverage for generated/logs/local config, verify a debug build, run the unit-test suite, then prepare a clean attributable repository.

## Contributing

This is an early-stage fork awaiting upstream/legal clarity on licensing. Before contributing:

1. Read [`docs/UPSTREAM_AND_ATTRIBUTION.md`](docs/UPSTREAM_AND_ATTRIBUTION.md) to understand the Briar relationship and attribution obligations.
2. Do not submit work that removes or obscures Briar copyright/licence notices.
3. Do not include `local.properties`, signing keys, credentials, machine-specific paths, logs, or build output.
4. Obtain approval before any commit or network write (no repository has been created and no remote will be changed).

## Upstream acknowledgements and licensing

Baqqa is derived from **Briar** (secure messaging), upstream Git: `https://code.briarproject.org/briar/briar.git`, which is this repository's configured `origin`. The committed HEAD (`b46d008a`) is unchanged from Briar tag `release-1.5.19`.

**Licensing (unresolved — do not redistribute):**

* This repository carries a `LICENSE.txt` stating the GNU General Public License v3 (GPLv3).
* Briar upstream is distributed under the GNU Affero General Public License v3 (AGPLv3).
* Source files in this fork carry no individual copyright or licence headers.

These facts conflict and have not been reconciled. Do not replace, rewrite, or invent a licence. Do not assume rebranding permits removal of upstream attribution. See [`docs/UPSTREAM_AND_ATTRIBUTION.md`](docs/UPSTREAM_AND_ATTRIBUTION.md) for the full attribution position.

## Supporting documentation

* [`docs/PROJECT_STATUS.md`](docs/PROJECT_STATUS.md) — what works, what is incomplete, build results, blockers, milestones
* [`docs/ARCHITECTURE.md`](docs/ARCHITECTURE.md) — module structure and data/control flow
* [`docs/BUILDING.md`](docs/BUILDING.md) — Windows setup and build instructions
* [`docs/SECURITY.md`](docs/SECURITY.md) — security model, disclosure, limitations
* [`docs/UPSTREAM_AND_ATTRIBUTION.md`](docs/UPSTREAM_AND_ATTRIBUTION.md) — Briar relationship, attribution, licensing question
* [`docs/RELEASE_CHECKLIST.md`](docs/RELEASE_CHECKLIST.md) — pre-publication checklist

> This README makes no claims about downloads, releases, app-store availability, or donations. Any badge or link referencing an external service is omitted unless its target has been verified as real.
