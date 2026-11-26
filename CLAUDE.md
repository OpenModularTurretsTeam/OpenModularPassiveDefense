# OpenModularPassiveDefense 1.12.2: open items

Moved out of the workspace's top-level `CLAUDE.md` (`../CLAUDE.md` from here), so that it is loaded only when you work in `OpenModularPassiveDefense/`; the top-level file has the index.
Section names in quotes ("The shared core", "Running", "Versions", …) and relative paths (`../OpenModularCore`, `../CLAUDE.md` is the OMM root file) are those of the top-level file; paths are relative to the workspace folder `OMTTeam1.12/`.

## Status and open items (from "Status and open items")

- **OMPD defenses (4.1, branch `doors-gates-lights`, parked, no PR; in `next-branches.txt`):** wall and fence gates (1 and 2 high), tiered hardened doors, the 2x2 heavy door (tier 5), tiered hardened lamps and spikes, recipes "mix + plating"; all decided by the user and implemented.
  Open: a client check; the textures (placeholders, the user makes them; the list with layout hints is the worktree's git-ignored `TODO.md`, keep a copy before removing the worktree); the branch still has its own `omtteam.ompd.core` (`HeavyDoorMotion`, `PassageRules`, `SpikeRules` and the old `DefenseStats` / `DefenseTier` / `FenceRules`), which moves into OMPD's game layer (on `1.12`: `config.DefenseStats`, `config.DefenseTier`, `util.FenceRules`) when the branch is revived.
