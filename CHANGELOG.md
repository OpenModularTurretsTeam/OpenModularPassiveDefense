# Changelog

All notable changes to Open Modular Passive Defense for Minecraft 1.12.2.
The format follows [Keep a Changelog](https://keepachangelog.com/en/1.1.0/).

Add every user-visible change as a one-line entry under **Unreleased** (sections: Added, Changed, Fixed, Removed), by hand or with `./gradlew changelogEntry -Ptype=added -Pentry="…"`.
Before a release, `./gradlew releaseChangelog` turns *Unreleased* into the section of the version set in `gradle.properties`; the CurseForge and Modrinth uploads of that version (`./gradlew -b build.gradle release`) use exactly that section.

## [Unreleased]

### Changed

- Fences no longer hurt the owner's team and their pets

### Fixed

- Crash on hardened blocks, walls, fences and camo traps with invalid metadata (damaged chunks, /give)
- Sneak-right-clicking your own block with an empty hand did not always pick it up
- Hardened blocks, walls, fences and camo traps placed by something other than a player (e.g. a deployer) crashed the game

## [4.0.0-alpha.1] - 2026-10-23

### Added

- Camouflage traps have a trust list (Trusted Players button in their camouflage menu): trusted players can stand on the trap, admins also change its camouflage and manage the list

### Changed

- The camouflage trap's admins open the camouflage menu by right-clicking it (sneaking skips it); the trap now has light value and opacity settings and blocks light like a solid block by default
