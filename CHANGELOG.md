# Changelog

All notable changes to Open Modular Passive Defense for Minecraft 1.12.2.
The format follows [Keep a Changelog](https://keepachangelog.com/en/1.1.0/).

Add every user-visible change as a one-line entry under **Unreleased** (sections: Added, Changed, Fixed, Removed), by hand or with `./gradlew changelogEntry -Ptype=added -Pentry="…"`.
Before a release, `./gradlew releaseChangelog` turns *Unreleased* into the section of the version set in `gradle.properties`; the CurseForge and Modrinth uploads of that version (`./gradlew release` in the mod folder) use exactly that section.

## [Unreleased]

### Added

- Hardness and explosion resistance of the hardened blocks, walls and fences and the fence damage are configurable per tier (`hardness`, `explosionResistance`, `fenceDamage` in the `tiers` section)

### Changed

- Trusted admins can change the camouflage disguise of turret bases too, as they already could on camouflage traps, so both follow the same rule (with the next OMLib release)

### Fixed

- Fences dealt the same damage on every tier; it now grows from 5 (tier 1) to 9 (tier 5)

## [4.0.0-beta.4] - 2026-10-27

### Added

- The camouflage trap accepts transparent blocks (glass, ice, leaves) as a disguise; the config option `allowTransparentDisguises` in the `camo trap` section turns it off

### Changed

- Requires OMLib 4.0.0-beta.4 or newer

## [4.0.0-beta.3] - 2026-10-26

### Changed

- Requires OMLib 4.0.0-beta.3 or newer

## [4.0.0-beta.2] - 2026-10-26

### Added

- Hardened blocks, walls, fences and camouflage traps without an owner can be claimed by right-clicking them (OMLib); hardened blocks, walls and fences without an owner sparkle green

### Changed

- The jar files are named `ompd-<version>-mc1.12.2-forge.jar` (Minecraft version with `mc`, as on 1.20.1+)
- Hardened blocks can be used as camouflage now (other blocks with a tile entity still cannot), as on 1.20.1+
- Requires OMLib 4.0.0-beta.2 or newer

### Fixed

- Camo traps could create their tile entity on the chunk render thread (camouflage light settings, harvest tool), which can crash or corrupt the world
- WAILA/HWYLA showed no owner name for hardened blocks, walls and fences

## [4.0.0-beta.1] - 2026-10-24

### Changed

- Requires OMLib 4.0.0-beta.1 or newer

### Fixed

- The creative tab showed its translation key instead of its name

## [4.0.0-alpha.2] - 2026-10-23

### Changed

- License: the source code is now MIT (was GPLv3) and the art CC-BY-NC-SA 4.0 (see `LICENSE`, `LICENSE-ART`, `LICENSE-DUAL`)
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
