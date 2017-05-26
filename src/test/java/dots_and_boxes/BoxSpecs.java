package dots_and_boxes;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BoxSpecs {
    private final Dot d0_0 = new Dot(0,0);
    private final Dot d0_1 = new Dot(0,1);
    private final Dot d1_1 = new Dot(1,1);
    private final Dot d1_0 = new Dot(1,0);
    private final Box box = new Box(d0_0, d0_1, d1_1, d1_0);
    private final Box box_anti_clockwise = new Box(d0_0, d1_0, d1_1, d0_1);
    private final Dot d2_2 = new Dot(2,2);

    @Test
    public void cornersAreWithinGivenDots() {
        final List<Dot> dots = Arrays.asList(d0_0, d0_1, d1_1, d1_0);

        assertTrue(box.isContainedIn(dots));
        assertTrue(box_anti_clockwise.isContainedIn(dots));
    }
    @Test
    public void cornersAreNotWithinGivenDots() {
        final List<Dot> dots = Arrays.asList(d0_0, d0_1, d1_1, d1_0);

        final Box box_outside = new Box(d0_0, d1_0, d2_2, d0_1);
        assertFalse(box_outside.isContainedIn(dots));
    }

    @Test
    public void boxIsTakenByThePlayerWhoMarksTheLastRemainingFourthSide() {
        final String player = "test";
        final Box markedFirstSide = box.join(d0_0, d0_1, player);
        assertFalse(markedFirstSide.isOccupied());
        final Box markedSecondSide = markedFirstSide.join(d0_1, d1_1, player);
        assertFalse(markedSecondSide.isOccupied());
        final Box markedThirdSide = markedSecondSide.join(d1_1, d1_0, player);
        assertFalse(markedThirdSide.isOccupied());
        final Box markedFourthSide = markedThirdSide.join(d1_0, d0_0, player);
        assertTrue(markedFourthSide.isOccupied());
        assertEquals(player, takenBy(markedFourthSide));
    }

    private String takenBy(Box box) {
        return box.map((lines, who) -> who);
    }

    @Test
    public void doesNotMarkTheBoxAgainWhenItIsAlreadyTakenByThePlayer() {
        final String player = "test";
        Box marked = box.join(d0_0, d0_1, player)
                .join(d0_1, d1_1, player)
                .join(d1_1, d1_0, player)
                .join(d1_0, d0_0, player);

        assertEquals(player, takenBy(marked.join(d1_0, d0_0, "anotherPlayer")));
        assertSame(marked, marked.join(d1_1, d1_0, "anotherPlayer"));
    }

    @Test
    public void doesNotJoinALineThatIsNotAPartOfTheBox() {
        final Box unmarked = box.join(d0_0, d2_2,"test");
        assertEquals(box, unmarked);
    }
}
