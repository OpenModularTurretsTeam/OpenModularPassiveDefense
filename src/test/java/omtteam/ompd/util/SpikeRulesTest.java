package omtteam.ompd.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SpikeRulesTest {
    private static final double EPSILON = 1.0E-5D;

    @Test
    void standingHurtsByTierOncePerSecond() {
        assertEquals(1.0F, SpikeRules.standingDamage(1), EPSILON);
        assertEquals(5.0F, SpikeRules.standingDamage(5), EPSILON);
        int hits = 0;
        for (long time = 0; time < 100; time++) {
            if (SpikeRules.isStandingHitTick(time)) {
                hits++;
            }
        }
        assertEquals(5, hits, "five hits in 100 ticks");
        assertFalse(SpikeRules.isStandingHitTick(19));
        assertTrue(SpikeRules.isStandingHitTick(20));
    }

    @Test
    void landingMultipliesByTheFallDistance() {
        assertEquals(3.0F, SpikeRules.landingDamage(1, 3.0F), EPSILON);
        assertEquals(15.0F, SpikeRules.landingDamage(5, 3.0F), EPSILON);
        assertEquals(6.25F, SpikeRules.landingDamage(5, 1.25F), EPSILON, "jumping on them");
    }

    @Test
    void shortFallsHurtAtLeastAsMuchAsStanding() {
        assertEquals(1.0F, SpikeRules.landingMultiplier(0.0F), EPSILON);
        assertEquals(1.0F, SpikeRules.landingMultiplier(0.5F), EPSILON);
        assertEquals(5.0F, SpikeRules.landingDamage(5, 0.2F), EPSILON);
    }

    @Test
    void multiplierIsCappedAtTenBlocks() {
        assertEquals(10.0F, SpikeRules.landingMultiplier(10.0F), EPSILON);
        assertEquals(10.0F, SpikeRules.landingMultiplier(40.0F), EPSILON);
        assertEquals(50.0F, SpikeRules.landingDamage(5, 25.0F), EPSILON);
    }

    @Test
    void invalidTiersAreClamped() {
        assertEquals(5.0F, SpikeRules.standingDamage(9), EPSILON);
        assertEquals(1.0F, SpikeRules.standingDamage(0), EPSILON);
        assertEquals(SpikeRules.landingDamage(5, 4.0F), SpikeRules.landingDamage(9, 4.0F), EPSILON);
    }
}
