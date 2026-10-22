# Changelog

All notable changes to Open Modular Passive Defense for Minecraft 1.12.2.
The format follows [Keep a Changelog](https://keepachangelog.com/en/1.1.0/).

Add every user-visible change as a one-line entry under **Unreleased** (sections: Added, Changed, Fixed, Removed), by hand or with `./gradlew changelogEntry -Ptype=added -Pentry="…"`.
Before a release, `./gradlew releaseChangelog` turns *Unreleased* into the section of the version set in `gradle.properties`; the CurseForge and Modrinth uploads of that version (`./gradlew -b build.gradle release`) use exactly that section.

## [Unreleased]

### Added

- Camouflage traps have a trust list (Trusted Players button in their camouflage menu): trusted players can stand on the trap, admins also change its camouflage and manage the list

### Changed

- The camouflage trap's admins open the camouflage menu by right-clicking it (sneaking skips it); the trap now has light value and opacity settings and blocks light like a solid block by default

