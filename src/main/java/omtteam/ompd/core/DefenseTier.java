package omtteam.ompd.core;

/**
 * The five tiers of hardened blocks, walls and fences: block and item metadata 0–4 are tiers 1–5 (as in 3.x); their
 * stats are in {@link DefenseStats}.
 */
public final class DefenseTier {
    public static final int MIN = 1;
    public static final int MAX = 5;

    private DefenseTier() {
    }

    /** {@code tier} limited to {@code [MIN, MAX]}. */
    public static int clamp(int tier) {
        return Math.max(MIN, Math.min(MAX, tier));
    }

    /** The tier of a block or item metadata; invalid metadata (damaged chunks, {@code /give}) gets the nearest tier. */
    public static int fromMeta(int meta) {
        return clamp(meta + 1);
    }

    /** The metadata of a tier, see {@link #fromMeta}. */
    public static int toMeta(int tier) {
        return clamp(tier) - 1;
    }

    /** A valid metadata for any metadata: {@code toMeta(fromMeta(meta))}. */
    public static int clampMeta(int meta) {
        return toMeta(fromMeta(meta));
    }
}
