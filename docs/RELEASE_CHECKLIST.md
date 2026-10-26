# Release checklist

No release has been created for Baqqa, and publication is currently blocked. Use this checklist before any publication.

## Licence

- [ ] Resolve the GPLv3 (`LICENSE.txt`) vs AGPLv3 (Briar upstream) discrepancy.
- [ ] Confirm the applicable licence and distribution rights with the upstream project / legal review.
- [ ] Preserve all upstream copyright and licence notices; do not remove Briar attribution.
- [ ] Do not replace, rewrite, or invent a licence to make publication easier.

## Build

- [ ] Verify a debug build: `.\gradlew.bat :baqqa-android:assembleDebug`
- [ ] Confirm the APK installs and launches on a device/emulator.
- [ ] Do not attempt a release build without signing material (none present; must not be published).

## Tests

- [ ] Run the unit-test suite: `.\gradlew.bat :baqqa-android:test`
- [ ] Record pass/fail results; do not claim test counts that have not been verified.

## Sensitive files (must NOT be published)

- [ ] `local.properties` (machine-specific SDK path) — excluded/not tracked.
- [ ] No signing keys, keystores, or credentials present or published.
- [ ] No personal machine paths, secrets, or private configuration included.

## Repository history

- [ ] Do not change `origin` (`https://code.briarproject.org/briar/briar.git`).
- [ ] Do not push to any remote without approval.
- [ ] Do not rewrite Git history.
- [ ] Do not commit the entire working tree indiscriminately.

## Screenshots and assets

- [ ] Only use genuine, available, safe-to-publish assets (SVG files in `baqqa-android/artwork/`).
- [ ] Do not fabricate screenshots or imply unverified features.

## Attribution

- [ ] Acknowledge Briar as upstream; never claim Briar was independently built by Maaz.
- [ ] Preserve upstream licence and copyright notices.

## Publication verification

- [ ] Confirm the public repository description, topics, and README are accurate.
- [ ] Confirm no release is claimed unless one has been created.
- [ ] Obtain approval before any commit or network write.
