package omtteam.ompd.core;

/**
 * The heavy door: a 2x2 door of two wings (columns 0 and 1, each 1 wide and 2 high) that slide sideways out of the
 * doorway, column 0 to the left, column 1 to the right. Open progress runs from 0 (closed) to 1 (open).
 * <p>
 * Cells are numbered {@code column + 2 * row} (row 0 below, row 1 above); cell 0 holds the door's state.
 */
public final class HeavyDoorMotion {
    /** Progress per tick: a wing takes 20 ticks (1 s) to slide fully. */
    public static final float STEP = 0.05F;
    public static final int CELLS = 4;

    private HeavyDoorMotion() {
    }

    /** The progress one tick later, moving towards 1 when open and towards 0 when closed. */
    public static float advance(float progress, boolean open) {
        return open ? Math.min(1.0F, progress + STEP) : Math.max(0.0F, progress - STEP);
    }

    public static int cell(int column, int row) {
        return column + 2 * row;
    }

    public static int column(int cell) {
        return cell & 1;
    }

    public static int row(int cell) {
        return (cell >> 1) & 1;
    }

    /**
     * The part of a cell its wing still covers, as {@code {from, to}} along the door from left (0) to right (1) within
     * that cell; {@code from >= to} once the wing has left the cell.
     */
    public static float[] wingSpan(int column, float progress) {
        float p = Math.max(0.0F, Math.min(1.0F, progress));
        return column == 0 ? new float[]{0.0F, 1.0F - p} : new float[]{p, 1.0F};
    }

    /** Where a wing is drawn at the given progress, as {@code {from, to}} along the door from the left edge of cell column 0. */
    public static float[] wingOffset(int column, float progress) {
        float p = Math.max(0.0F, Math.min(1.0F, progress));
        return column == 0 ? new float[]{-p, 1.0F - p} : new float[]{1.0F + p, 2.0F + p};
    }
}
