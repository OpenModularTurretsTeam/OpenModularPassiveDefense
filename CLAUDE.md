# OpenModularPassiveDefense 1.12.2: open items

Moved out of the workspace's top-level `CLAUDE.md` (`../CLAUDE.md` from here), so that it is loaded only when you work in `OpenModularPassiveDefense/`; the top-level file has the index.
Section names in quotes ("The shared core", "Running", "Versions", …) and relative paths (`../OpenModularCore`, `../CLAUDE.md` is the OMM root file) are those of the top-level file; paths are relative to the workspace folder `OMTTeam1.12/`.

## Status and open items (from "Status and open items")

- **OMPD defenses (4.1, branch `doors-gates-lights`, parked, no PR; in `next-branches.txt`):** wall and fence gates (1 and 2 high), tiered hardened doors, the 2x2 heavy door (tier 5), tiered hardened lamps and spikes, recipes "mix + plating"; all decided by the user and implemented.
  Open: a client check; the textures (placeholders, the user makes them; the list with layout hints is the worktree's git-ignored `TODO.md`, keep a copy before removing the worktree); rebased onto `1.12` on 2026-11-27: its rules are game-layer classes like `1.12`'s (`util.PassageRules` and `util.SpikeRules` next to `util.FenceRules`, `tileentity.HeavyDoorMotion` next to `TileEntityHeavyDoor`), and spikes spare whom fences spare (the owner and shared owners).
