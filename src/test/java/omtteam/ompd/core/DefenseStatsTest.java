package omtteam.ompd.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DefenseStatsTest {
    private static final float EPS = 1.0E-6F;

    /** Hardness and resistance keep the 3.x values; both stats are the same per tier. */
    @Test
    void defaultsAreThe3xStrengths() {
        float[] expected = {10.0F, 20.0F, 30.0F, 50.0F, 80.0F};
        for (int tier = DefenseTier.MIN; tier <= DefenseTier.MAX; tier++) {
            assertEquals(expected[tier - 1], DefenseStats.DEFAULTS.hardness(tier), EPS, "hardness of tier " + tier);
            assertEquals(expected[tier - 1], DefenseStats.DEFAULTS.explosionResistance(tier), EPS,
                    "resistance of tier " + tier);
        }
    }

    /** 3.x hit for 5 on every tier; tier 1 keeps that and every tier above hurts more. */
    @Test
    void defaultFenceDamageGrowsWithTheTier() {
        assertEquals(5.0F, DefenseStats.DEFAULTS.fenceDamage(DefenseTier.MIN), EPS);
        for (int tier = DefenseTier.MIN + 1; tier <= DefenseTier.MAX; tier++) {
            assertTrue(DefenseStats.DEFAULTS.fenceDamage(tier) > DefenseStats.DEFAULTS.fenceDamage(tier - 1),
                    "fence damage of tier " + tier);
        }
    }

    @Test
    void configuredValuesArePerTier() {
        DefenseStats stats = DefenseStats.of(new float[]{1, 2, 3, 4, 5}, new float[]{6, 7, 8, 9, 10},
                new float[]{0, 1, 2, 3, 4});
        assertEquals(3.0F, stats.hardness(3), EPS);
        assertEquals(10.0F, stats.explosionResistance(5), EPS);
        assertEquals(0.0F, stats.fenceDamage(1), EPS);
    }

    @Test
    void anInvalidListGetsTheDefaultsAndLeavesTheOthers() {
        float[] valid = {1, 2, 3, 4, 5};
        DefenseStats stats = DefenseStats.of(new float[]{1, 2, 3}, valid, new float[]{1, -2, 3, 4, 5});
        assertEquals(DefenseStats.DEFAULTS.hardness(3), stats.hardness(3), EPS, "too short");
        assertEquals(DefenseStats.DEFAULTS.fenceDamage(2), stats.fenceDamage(2), EPS, "negative");
        assertEquals(4.0F, stats.explosionResistance(4), EPS);
    }

    @Test
    void validity() {
        assertTrue(DefenseStats.isValid(new float[]{0, 0, 0, 0, 0}));
        assertFalse(DefenseStats.isValid(null));
        assertFalse(DefenseStats.isValid(new float[]{1, 2, 3, 4, 5, 6}));
        assertFalse(DefenseStats.isValid(new float[]{1, 2, Float.NaN, 4, 5}));
        assertFalse(DefenseStats.isValid(new float[]{1, 2, 3, Float.POSITIVE_INFINITY, 5}));
    }

    /** The stats keep their own copy: later changes to the given or default arrays do not reach them. */
    @Test
    void statsAreImmutable() {
        float[] hardness = {1, 2, 3, 4, 5};
        DefenseStats stats = DefenseStats.of(hardness, hardness, hardness);
        hardness[0] = 99.0F;
        DefenseStats.defaultFenceDamage()[0] = 99.0F;
        assertEquals(1.0F, stats.hardness(1), EPS);
        assertEquals(5.0F, DefenseStats.DEFAULTS.fenceDamage(1), EPS);
    }

    @Test
    void invalidTiersGetTheNearestTier() {
        assertEquals(80.0F, DefenseStats.DEFAULTS.hardness(9), EPS);
        assertEquals(10.0F, DefenseStats.DEFAULTS.explosionResistance(0), EPS);
    }
}
