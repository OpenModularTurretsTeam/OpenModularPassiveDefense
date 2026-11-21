# Contributing to Open Modular Passive Defense

Thanks for helping out!
This branch (`1.12`) holds Open Modular Passive Defense (OMPD) for Minecraft 1.12.2 with Forge.
OMPD exists only for 1.12.2: on 1.20.1, 1.21.1 and 1.21.11 its blocks are part of [OpenModularTurrets](https://github.com/OpenModularTurretsTeam/OpenModularTurrets).

Questions are welcome on our [Discord](https://discord.gg/SEG695u).

## Reporting issues

Open an issue on the [issue tracker](https://github.com/OpenModularTurretsTeam/OpenModularPassiveDefense/issues).
For a bug, include the Forge, OMLib and OMPD versions and your log or crash report.

## Development setup

You need:

- **Java 25** to run Gradle (RetroFuturaGradle, the 1.12.2 Forge toolchain, needs it).
  This build does not pin a JDK, so `JAVA_HOME` (or the Gradle JVM in your IDE) must point to a JDK 25.
  Alternatively, set `org.gradle.java.home` to a JDK 25 in `~/.gradle/gradle.properties` for the Gradle daemon.
  `JAVA_HOME` must then still be a JDK 17 or newer, because the Gradle 9 launcher that `./gradlew` starts does not run on Java 8.
- Git and an IDE with Gradle support (IntelliJ IDEA is recommended).

The mod itself is compiled, tested and run with a Java 8 toolchain (Azul).
Gradle downloads it on the first build if no Azul JDK 8 is installed.
Gradle itself comes with the wrapper (`./gradlew`), and RetroFuturaGradle sets up Forge 14.23.5.2847 with the MCP mappings `stable_39`.

### OMLib

OMPD needs [OMLib](https://github.com/OpenModularTurretsTeam/OMLib) on the same branch `1.12`.
Build it from source and publish it to your local Maven repository (`~/.m2`), then build OMPD against it:

```bash
git clone -b 1.12 https://github.com/OpenModularTurretsTeam/OMLib.git
git clone -b 1.12 https://github.com/OpenModularTurretsTeam/OpenModularPassiveDefense.git
cd OMLib
./gradlew publishToMavenLocal                          # builds and tests OMLib, publishes omtteam:omlib:1.12.2-<version>
cd ../OpenModularPassiveDefense
./gradlew build -Pomlib_local -Pomlib_version=4.0.1    # builds OMPD against that OMLib
```

What the two flags do:

- `-Pomlib_local` takes OMLib from `~/.m2`.
  Without it, the build compiles against the OMLib file set by `omlib_curse_file_id` in `gradle.properties`, an OMLib 3.2.0 file from CurseForge that lacks the API this branch uses, so the build fails.
- `-Pomlib_version` must name the version you just published: OMLib's version as composed from `mod_version`, `release_type` and `release_number` in its `gradle.properties`, without the `1.12.2-` prefix of the Maven version.
  That is `4.0.1` on `1.12` today; an alpha or beta would be, for example, `4.0.2-beta.1`.
  OMPD's `gradle.properties` still names `omlib_version=4.0.0`, a version you only have locally if you published it before.
  The value also sets the minimum OMLib version written into the OMPD jar (`required-after:omlib@[<omlib_version>,<next major>.0.0-alpha)`, for example `[4.0.1,5.0.0-alpha)`), so it is for local builds only.

An IDE import does not pass these flags, so its Gradle sync falls back to the CurseForge OMLib and shows the sources as broken.
Give the IDE the flags as Gradle properties by adding them to `~/.gradle/gradle.properties`:

```properties
omlib_local=true
omlib_version=4.0.1
```

They then apply to every Gradle build on your machine (OpenModularTurrets reads the same names), so remove them when you are done.
Or pass `-Pomlib_local -Pomlib_version=4.0.1` in the arguments of each Gradle run configuration of the IDE.

Publishing OMLib again replaces an OMLib of the same version in `~/.m2`.
After a change in OMLib, run `./gradlew publishToMavenLocal` there again before you build OMPD.

When this repository is built as part of a Gradle build that also has an `:OMLib` project, it uses that project directly and needs neither flag.

### The shared core (OpenModularCore)

The game rules shared by the OpenModularTurretsTeam mods live in [OpenModularCore](https://github.com/OpenModularTurretsTeam/OpenModularCore).
OMPD has no core module of its own: it uses OMLib's core (`omtteam.omlib.core`, module `omlib-core-j8`), which comes bundled in OMLib's jar.
OMPD's own jar contains none of it.

`core_version` in `gradle.properties` is not read by OMPD's build.
It names the OpenModularCore release that OMLib bundles, so that the team's release checks hold every mod to the same core version.

To work on the core, clone it into the folder that holds your OMLib clone (or any folder above it):

```bash
cd ..                                                                      # from OpenModularPassiveDefense/: the folder that holds OMLib/ and OpenModularPassiveDefense/
git clone https://github.com/OpenModularTurretsTeam/OpenModularCore.git   # next to OMLib/ and OpenModularPassiveDefense/
```

The settings of OMLib and OMPD walk up from their own folder and include the first `OpenModularCore/` they find as a composite build, in place of the JitPack artifact of `core_version`.
Gradle logs `OpenModularCore: local source at <path>` when it does.
Because OMPD declares no core dependency, a core change reaches OMPD only through OMLib: run `./gradlew publishToMavenLocal` in OMLib again, then build OMPD.
To build against the JitPack artifact of `core_version` instead, as a clone without a local core does, publish OMLib with it, then build OMPD:

```bash
cd OMLib                                               # from the folder that holds OMLib/
OMM_NO_LOCAL_CORE=1 ./gradlew publishToMavenLocal      # OMLib with the JitPack core, not the local one
```

The variable makes no difference to OMPD's own build, because OMPD gets the core only from the OMLib jar in `~/.m2`.

### Useful commands

```bash
./gradlew build -Pomlib_local -Pomlib_version=4.0.1   # build and test the mod
./gradlew test -Pomlib_local -Pomlib_version=4.0.1    # unit and architecture tests only
./gradlew dependencyMap -Pomlib_local -Pomlib_version=4.0.1   # package dependency map in build/reports/dependency-map/index.html
./gradlew changelogEntry -Ptype=fixed -Pentry="…"     # add a line under Unreleased in CHANGELOG.md
```

The jars end up in `build/libs/`:

- `ompd-<version>-mc1.12.2-forge.jar` is the jar players install (SRG names).
- `ompd-<version>-mc1.12.2-forge-dev.jar` has MCP names, for development.
- `ompd-<version>-mc1.12.2-forge-sources.jar` holds the sources.

## Testing

- `./gradlew check -Pomlib_local -Pomlib_version=4.0.1` (the same flags as in "Useful commands") runs the JUnit 5 tests in `src/test/java`: the rules (`DefenseTierTest`, `DefenseStatsTest`, `FenceRulesTest`), the renaming of the 3.x tile entity names (`TENameFixTest`) and `ArchitectureTest`.
- `ArchitectureTest` fails if common code reaches the `client` package other than through the proxy, or if a new package cycle appears.
  Its frozen rules are stored in `src/test/resources/archunit_store`, and the known cycles are listed in `KNOWN_CYCLES`; don't add to either to make a test pass.
- The core checks of the shared build logic (`verifyCoreIsolation`, JaCoCo coverage of the core, `pitest`) only run in a mod with a `core` package of its own, so they do nothing in OMPD.
- There are no game tests on 1.12.
  RetroFuturaGradle adds `runClient` and `runServer` (game folder `run/`, player name `Developer`).
  In a standalone build OMLib is only on the compile and test classpath, not in the game, and OMPD requires OMLib, so these runs stop without it.
  Put OMLib's release jar (SRG names) into the mods folder of the run; FML remaps jars in the mods folder to the dev names at runtime:

  ```bash
  mkdir -p run/mods
  cp ../OMLib/build/libs/omlib-4.0.1-mc1.12.2-forge.jar run/mods/   # OMLib's jar from its ./gradlew build or publishToMavenLocal
  ./gradlew runServer -Pomlib_local -Pomlib_version=4.0.1           # or runClient; both run in run/
  ```

  Copy OMLib's jar again after every OMLib change.
  The team also tests in game with OMPD, OMLib, OpenModularTurrets and OpenModularLighting together.
  Ask on Discord if you need to test something in game.
  If you test in game yourself, use a dedicated server for anything involving networking or GUIs, and say in the pull request how you tested.

## Code layout and style

- [docs/architecture.md](docs/architecture.md) describes the layers, where things live, how the main flows run and how to add a tiered block or a rule.
- Java 8 (the toolchain), UTF-8 sources, Forge 1.12.2 with MCP names (`TileEntity`, `IBlockState`, …).
- 4-space indent, no wildcard imports.
  There is no formatter in this build, so keep to the style of the surrounding code.
- New registry ids are lowercase `snake_case`; the existing 3.x ids and metadata stay as they are, so old worlds keep loading.
- User-facing text goes through translation keys in `src/main/resources/assets/ompd/lang/` (`en_US.lang` and the other languages).
- Client code (`Minecraft`, rendering, screens) goes into the `client` package and is reached from common code only through the proxy; `ArchitectureTest` fails otherwise.
- Rules that the other mods or Minecraft versions share belong in OpenModularCore; owner, trust and camouflage code belongs in OMLib.

## Pull requests

1. Branch off `1.12` and keep each pull request focused on one change.
2. Add a one-line entry for every user-visible change under `## [Unreleased]` in `CHANGELOG.md`, by hand or with:

   ```bash
   ./gradlew changelogEntry -Ptype=fixed -Pentry="Short description of the change"
   ```

   Types are `added`, `changed`, `fixed` and `removed`.
3. Make sure `./gradlew build -Pomlib_local -Pomlib_version=<OMLib version>` passes, against an OMLib published from the current `1.12` branch.
4. Describe what you changed and how you tested it in the pull request.

## License

By contributing, you agree that your code is licensed under the [MIT license](LICENSE) and your art (textures, models, sounds) under [CC BY-NC-SA 4.0](LICENSE-ART), as [LICENSE-DUAL](LICENSE-DUAL) describes.
