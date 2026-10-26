# Security

## Verified security model (source-observable)

Baqqa inherits Briar's peer-to-peer security model. Source-observable settings in `baqqa-android/src/main/AndroidManifest.xml`:

* `android:allowBackup="false"` — application backups disabled.
* Most services and receivers are `android:exported="false"`.
* The launcher activity `SplashScreenActivity` is `android:exported="true"` (expected for a launcher entry point).
* File provider authorities scoped to `${applicationId}.fileprovider` and `${applicationId}.audioprovider`.
* A `networkSecurityConfig` is declared (`@xml/network_security_config`).

These are configuration observations, **not** independently verified security properties.

## What is NOT established

* End-to-end encryption has not been audited for this fork.
* No penetration testing or threat modelling has been performed.
* No vulnerability disclosure programme is operated for this fork.

## Responsible disclosure

No private security contact or security email address is published for this fork. If you discover a security issue, contact the Briar project through its established channels and disclose responsibly; do not assume this fork maintains a separate security team.

## Limitations

* Baqqa is an early-stage fork; security properties of Briar upstream may not all carry over unchanged.
* Rebranding (package ID, name) does not change the security model.
* The licensing gate is unresolved; do not redistribute pending clarification.
