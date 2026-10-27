package omtteam.ompd.core;

/**
 * Damage of spikes. Standing on them hurts by the tier once per second ({@link #STANDING_INTERVAL}); landing on them
 * hurts by that damage times a multiplier from 1 to 10 that grows with the fall distance ({@link #landingDamage}). Who
 * is hurt follows {@link FenceRules}.
 */
public final class SpikeRules {
    /** Ticks between two hits while standing on spikes. */
    public static final int STANDING_INTERVAL = 20;
    /** The landing multiplier for falls up to 1 block. */
    public static final float MIN_LANDING_MULTIPLIER = 1.0F;
    /** The landing multiplier for falls of 10 blocks and more. */
    public static final float MAX_LANDING_MULTIPLIER = 10.0F;

    private SpikeRules() {
    }

    /** Damage per hit (one hit per second) while standing on spikes of the tier. */
    public static float standingDamage(int tier) {
        return DefenseTier.clamp(tier);
    }

    /** Whether standing entities are hit in the tick with this world time. */
    public static boolean isStandingHitTick(long worldTime) {
        return worldTime % STANDING_INTERVAL == 0;
    }

    /**
     * Damage of landing on spikes of the tier after falling {@code fallDistance} blocks: {@link #standingDamage} times
     * the fall distance in blocks, at least 1 and at most 10 (reached at 10 blocks).
     */
    public static float landingDamage(int tier, float fallDistance) {
        return standingDamage(tier) * landingMultiplier(fallDistance);
    }

    public static float landingMultiplier(float fallDistance) {
        return Math.max(MIN_LANDING_MULTIPLIER, Math.min(MAX_LANDING_MULTIPLIER, fallDistance));
    }
}
