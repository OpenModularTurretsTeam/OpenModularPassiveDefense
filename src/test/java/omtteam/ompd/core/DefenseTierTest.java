package omtteam.ompd.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DefenseTierTest {
    private static final float EPS = 1.0E-6F;

    /** The 3.x values; both stats are the same per tier. */
    @Test
    void strengthPerTier() {
        float[] expected = {10.0F, 20.0F, 30.0F, 50.0F, 80.0F};
        for (int tier = DefenseTier.MIN; tier <= DefenseTier.MAX; tier++) {
            assertEquals(expected[tier - 1], DefenseTier.hardness(tier), EPS, "hardness of tier " + tier);
            assertEquals(expected[tier - 1], DefenseTier.explosionResistance(tier), EPS, "resistance of tier " + tier);
        }
    }

    @Test
    void metadataZeroToFourIsTierOneToFive() {
        for (int meta = 0; meta <= 4; meta++) {
            assertEquals(meta + 1, DefenseTier.fromMeta(meta));
            assertEquals(meta, DefenseTier.toMeta(meta + 1));
            assertEquals(meta, DefenseTier.clampMeta(meta));
        }
    }

    /** Damaged chunks and {@code /give} can hold any metadata; it must not crash the block state or item name. */
    @Test
    void invalidValuesGetTheNearestTier() {
        assertEquals(DefenseTier.MAX, DefenseTier.fromMeta(15));
        assertEquals(DefenseTier.MIN, DefenseTier.fromMeta(-1));
        assertEquals(4, DefenseTier.clampMeta(7));
        assertEquals(0, DefenseTier.clampMeta(-3));
        assertEquals(0, DefenseTier.toMeta(0));
        assertEquals(4, DefenseTier.toMeta(9));
        assertEquals(80.0F, DefenseTier.hardness(9), EPS);
        assertEquals(10.0F, DefenseTier.explosionResistance(0), EPS);
    }
}
