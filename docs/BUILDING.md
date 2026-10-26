# Building Baqqa (Windows)

## Prerequisites

* JDK 17
* Android SDK with compileSdk 35 and build-tools 35.0.0
* `ANDROID_HOME` environment variable, or a `local.properties` file containing `sdk.dir=<path>` (**do not publish `local.properties`**)
* Network access to `mavenCentral()`, `google()`, and `plugins.gradle.org` (required to resolve dependencies)

## Verified commands

None yet. No Gradle command has been executed in this environment.

## Commands requiring testing (not yet run)

```powershell
.\gradlew.bat :baqqa-android:assembleDebug
.\gradlew.bat :baqqa-android:test
.\gradlew.bat :baqqa-android:assembleRelease
```

The release build requires signing configuration. No signing keys or `signingConfigs` were found in the repository, so a release build cannot be completed without adding signing material — which must not be published.

## Notes

* Build output goes to `baqqa-android/build/`.
* The build script reads the Git commit hash via `git rev-parse ...`. Since `git` is not installed in this environment, `BuildConfig.GitHash` falls back to a default string.
* `local.properties` is gitignored and machine-specific; create your own with your SDK path.
* Do not include `local.properties`, signing keys, or credentials in any publication.

## Troubleshooting

Not yet compiled; no failure modes observed.
