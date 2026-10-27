package omtteam.ompd.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DefenseTierTest {
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
    }
}
