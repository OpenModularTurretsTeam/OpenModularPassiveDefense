package omtteam.ompd.core;

/**
 * The per-tier stats of hardened blocks, walls and fences (from the config): hardness, explosion resistance (the value
 * of {@code Block.getExplosionResistance}, not of {@code setResistance}) and the damage a fence deals on contact.
 */
public final class DefenseStats {
    /** Hardness and explosion resistance of tiers 1–5 (3.x values; both are the same). */
    private static final float[] STRENGTH = {10.0F, 20.0F, 30.0F, 50.0F, 80.0F};
    /** Fence damage of tiers 1–5: the 3.x value 5 (all tiers) for tier 1, one more per tier. */
    private static final float[] FENCE_DAMAGE = {5.0F, 6.0F, 7.0F, 8.0F, 9.0F};
    public static final DefenseStats DEFAULTS = new DefenseStats(STRENGTH, STRENGTH, FENCE_DAMAGE);

    private final float[] hardness;
    private final float[] explosionResistance;
    private final float[] fenceDamage;

    private DefenseStats(float[] hardness, float[] explosionResistance, float[] fenceDamage) {
        this.hardness = hardness.clone();
        this.explosionResistance = explosionResistance.clone();
        this.fenceDamage = fenceDamage.clone();
    }

    /** The stats of the given per-tier lists; a list that is not {@link #isValid valid} gets the default values. */
    public static DefenseStats of(float[] hardness, float[] explosionResistance, float[] fenceDamage) {
        return new DefenseStats(orDefault(hardness, STRENGTH), orDefault(explosionResistance, STRENGTH),
                orDefault(fenceDamage, FENCE_DAMAGE));
    }

    /** Whether {@code values} has one finite, non-negative value per tier. */
    public static boolean isValid(float[] values) {
        if (values == null || values.length != DefenseTier.MAX - DefenseTier.MIN + 1) {
            return false;
        }
        for (float value : values) {
            if (!(value >= 0.0F) || Float.isInfinite(value)) {
                return false;
            }
        }
        return true;
    }

    private static float[] orDefault(float[] values, float[] defaults) {
        return isValid(values) ? values : defaults;
    }

    public float hardness(int tier) {
        return hardness[DefenseTier.clamp(tier) - 1];
    }

    public float explosionResistance(int tier) {
        return explosionResistance[DefenseTier.clamp(tier) - 1];
    }

    /** Damage per hit of a fence of the tier (the hurt cooldown of the entity limits the hits to two per second). */
    public float fenceDamage(int tier) {
        return fenceDamage[DefenseTier.clamp(tier) - 1];
    }

    /** The default hardness of each tier, for the config. */
    public static float[] defaultHardness() {
        return STRENGTH.clone();
    }

    /** The default explosion resistance of each tier, for the config. */
    public static float[] defaultExplosionResistance() {
        return STRENGTH.clone();
    }

    /** The default fence damage of each tier, for the config. */
    public static float[] defaultFenceDamage() {
        return FENCE_DAMAGE.clone();
    }
}
