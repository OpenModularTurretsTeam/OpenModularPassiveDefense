package omtteam.ompd.tileentity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HeavyDoorMotionTest {
    private static final float EPSILON = 1.0E-5F;

    @Test
    void opensAndClosesInTwentyTicks() {
        float progress = 0.0F;
        for (int tick = 0; tick < 20; tick++) {
            progress = HeavyDoorMotion.advance(progress, true);
        }
        assertEquals(1.0F, progress, EPSILON);
        assertEquals(1.0F, HeavyDoorMotion.advance(progress, true), "stays open");
        for (int tick = 0; tick < 20; tick++) {
            progress = HeavyDoorMotion.advance(progress, false);
        }
        assertEquals(0.0F, progress, EPSILON);
        assertEquals(0.0F, HeavyDoorMotion.advance(progress, false), "stays closed");
    }

    @Test
    void reversesMidway() {
        float progress = HeavyDoorMotion.advance(HeavyDoorMotion.advance(0.0F, true), true);
        assertEquals(0.05F, HeavyDoorMotion.advance(progress, false), EPSILON);
    }

    @Test
    void cellsRoundTrip() {
        for (int cell = 0; cell < HeavyDoorMotion.CELLS; cell++) {
            assertEquals(cell, HeavyDoorMotion.cell(HeavyDoorMotion.column(cell), HeavyDoorMotion.row(cell)));
        }
        assertEquals(1, HeavyDoorMotion.column(3));
        assertEquals(1, HeavyDoorMotion.row(3));
        assertEquals(0, HeavyDoorMotion.column(2));
    }

    @Test
    void closedWingsFillTheirCells() {
        assertArrayEquals(new float[]{0.0F, 1.0F}, HeavyDoorMotion.wingSpan(0, 0.0F), EPSILON);
        assertArrayEquals(new float[]{0.0F, 1.0F}, HeavyDoorMotion.wingSpan(1, 0.0F), EPSILON);
    }

    @Test
    void wingsSlideOutwards() {
        assertArrayEquals(new float[]{0.0F, 0.75F}, HeavyDoorMotion.wingSpan(0, 0.25F), EPSILON);
        assertArrayEquals(new float[]{0.25F, 1.0F}, HeavyDoorMotion.wingSpan(1, 0.25F), EPSILON);
        assertArrayEquals(new float[]{-0.25F, 0.75F}, HeavyDoorMotion.wingOffset(0, 0.25F), EPSILON);
        assertArrayEquals(new float[]{1.25F, 2.25F}, HeavyDoorMotion.wingOffset(1, 0.25F), EPSILON);
    }

    @Test
    void openWingsLeaveTheDoorway() {
        for (int column = 0; column < 2; column++) {
            float[] span = HeavyDoorMotion.wingSpan(column, 1.0F);
            assertTrue(span[0] >= span[1], "column " + column);
        }
        assertArrayEquals(new float[]{0.0F, 1.0F}, HeavyDoorMotion.wingSpan(0, -1.0F), EPSILON, "clamped");
    }
}
