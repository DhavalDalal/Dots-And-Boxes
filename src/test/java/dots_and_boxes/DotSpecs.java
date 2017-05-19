package dots_and_boxes;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DotSpecs {
    @Test
    public void translatesByDelta() {
        Dot dot = new Dot(0, 0);
        assertEquals(new Dot(1, 0), dot.translate(1, 0));
        assertEquals(new Dot(0, 1), dot.translate(0, 1));
    }
}
