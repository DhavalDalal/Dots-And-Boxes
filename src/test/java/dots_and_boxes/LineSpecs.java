package dots_and_boxes;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LineSpecs {
    private final Dot d0_0 = new Dot(0, 0);
    private final Dot d0_1 = new Dot(0, 1);
    private final Dot d1_1 = new Dot(1, 1);
    private final Dot d2_2 = new Dot(2,2);
    private final Line line_00_01 = new Line(d0_0, d0_1, false);
    private final Line line_01_00 = new Line(d0_1, d0_0, false);
    private final Line line_11_22 = new Line(d1_1, d2_2, false);

    @Test
    public void endPointsAreWithinGivenDots() {
        final List<Dot> dots = Arrays.asList(d0_0, d0_1, d1_1);

        assertTrue(line_00_01.hasEndPointsIn(dots));
        assertTrue(line_01_00.hasEndPointsIn(dots));
        assertFalse(line_11_22.hasEndPointsIn(dots));
    }

    @Test
    public void marksTheLine() {
        final Line marked = new Line(d0_0, d0_1, true);
        assertEquals(marked, line_00_01.join(d0_0, d0_1));
        assertEquals(marked, line_00_01.join(d0_1, d0_0));
    }

    @Test
    public void doesNotMarkTheLine() {
        assertSame(line_00_01, line_00_01.join(d0_1, d1_1));
    }
}
