# Upstream and attribution

## Upstream project

Baqqa is derived from **Briar**, the secure-messaging project.

* Upstream Git: `https://code.briarproject.org/briar/briar.git` — this repository's configured `origin`
* Local branch: `master`
* HEAD commit: `b46d008aceb4c9cea306df8299fcfc1b7ce79d21` — the peeled commit of upstream tag `release-1.5.19`
* Submodule: `baqqa-mailbox` → `https://code.Baqqaproject.org/Baqqa/Baqqa-mailbox.git`

## What is inherited from Briar (unchanged, subject to verification)

* The Bramble protocol stack (`:bramble-*` modules)
* The Baqqa core/application code structure
* Peer-to-peer messaging, Bluetooth/Wi-Fi/Tor synchronisation, contacts, private groups, forums, blogs
* Original copyright and licence notices carried in source (where present)

## Observable modifications in this fork (not fully diffed)

* Rebranding to "Baqqa": application ID `org.Baqqaproject.Baqqa.android`, version `1.5.19`, namespace `org.Baqqaproject.Baqqa`
* Baqqa artwork and naming in `baqqa-android/artwork/` and `baqqa-android/fastlane/`
* Fork-specific CI (` .gitlab-ci.yml`) referencing `code.Baqqaproject.org`
* Build logs, temporary traces, and recovered files in the working tree (not source changes)

A complete, authoritative diff of this fork's changes versus Briar upstream **cannot be produced here because the `git` binary is not installed in this environment**. The committed HEAD is unchanged from upstream `release-1.5.19`; all observable differences are uncommitted working-tree modifications.

## Attribution obligations

* Never claim the original Briar application was independently built by Maaz.
* Preserve Briar's upstream attribution and licence notices.
* Rebranding does not remove the obligation to acknowledge Briar as upstream.

## Licensing question (unresolved)

* This repository's `LICENSE.txt` states the GNU General Public License v3 (GPLv3), copyright 2011–2014 Sublime Software Ltd.
* Briar upstream is distributed under the GNU Affero General Public License v3 (AGPLv3).
* Source files checked carry no individual licence/copyright headers.

This discrepancy has not been resolved. Do not redistribute or publish until the applicable licence is confirmed. See the licensing gate in the main README.
