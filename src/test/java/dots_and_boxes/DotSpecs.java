package dots_and_boxes;

import dots_and_boxes.Dot;
import org.junit.Test;

import static org.junit.Assert.*;

public class DotSpecs {
    private final Dot d00 = new Dot(0, 0);
    private final Dot d01 = new Dot(0, 1);
    private final Dot d10 = new Dot(1, 0);
    private final Dot d11 = new Dot(1, 1);
    private final Dot d02 = new Dot(0, 2);
    private final Dot d20 = new Dot(2, 0);
    private final Dot d22 = new Dot(2, 2);

    @Test
    public void joinsDotsUsingHorizontalLine() throws Exception {
        assertTrue(d00.canBeJoinedHorizontallyOrVerticallyWith(d01));
        assertTrue(d00.canBeJoinedHorizontallyOrVerticallyWith(d02));
    }

    @Test
    public void joinsDotsUsingVerticalLine() throws Exception {
        assertTrue(d00.canBeJoinedHorizontallyOrVerticallyWith(d10));
        assertTrue(d00.canBeJoinedHorizontallyOrVerticallyWith(d20));
    }

    @Test
    public void doesNotJoinDiagonalLines() throws Exception {
        assertFalse(d00.canBeJoinedHorizontallyOrVerticallyWith(d11));
        assertFalse(d10.canBeJoinedHorizontallyOrVerticallyWith(d01));
        assertFalse(d11.canBeJoinedHorizontallyOrVerticallyWith(d22));
    }

    @Test
    public void translatesADot() throws Exception {
        assertEquals(d00, d00.translate(0, 0));
        assertEquals(d01, d00.translate(0, 1));
        assertEquals(d10, d00.translate(1, 0));
        assertEquals(d11, d00.translate(1, 1));
    }

    @Test
    public void isWithinBoundsInclusive() throws Exception {
        assertTrue(d11.isWithin(d00, d22));
        assertTrue(d00.isWithin(d00, d00));
        assertTrue(d00.isWithin(d00, d01));
        assertTrue(d00.isWithin(d00, d11));
        assertFalse(d00.isWithin(d11, d22));
    }

    @Test
    public void isWithinBoundsExclusive() throws Exception {
        assertTrue(d11.isWithin(d00, d22, false));
        assertFalse(d00.isWithin(d00, d00, false));
        assertFalse(d00.isWithin(d00, d01, false));
        assertFalse(d00.isWithin(d00, d11, false));
        assertFalse(d00.isWithin(d11, d22, false));
    }
}
