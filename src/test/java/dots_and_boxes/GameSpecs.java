package dots_and_boxes;

import org.junit.Test;

import java.util.List;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GameSpecs {

    private final Dot d0_0 = new Dot(0,0);
    private final Dot d0_1 = new Dot(0,1);
    private final Dot d1_1 = new Dot(1,1);
    private final Dot d1_0 = new Dot(1,0);
    private final Dot d2_0 = new Dot(2,0);
    private Game start = new Game(1,1);

    @Test
    public void playerCompletingFourthSideGetsAnotherChance() {
        Game next1 = play_and_assertNextPlayer(start, d0_0, d0_1, "player1", "");
        Game next2 = play_and_assertNextPlayer(next1, d0_1, d1_1, "player2", "");
        Game next3 = play_and_assertNextPlayer(next2, d1_1, d1_0, "player1", "");
        List<?> nextState = next3.join(d1_0, d0_0, "player2");
        assertEquals("player2", nextPlayer(nextState));
    }

    private Game play_and_assertNextPlayer(Game game, Dot d1, Dot d2, String player, String nextPlayer) {
        List<?> nextState = game.join(d1, d2, player);
        Game next = nextGame(nextState);
        assertEquals(nextPlayer, nextState.get(0));
        return next;
    }
    private Game play(Game game, Dot d1, Dot d2, String player) {
        List<?> nextState = game.join(d1, d2, player);
        return nextGame(nextState);
    }

    private Game nextGame(List<?> nextState) {
        return (Game) nextState.get(1);
    }

    private String nextPlayer(List<?> nextState) {
        return (String) nextState.get(0);
    }

    @Test
    public void gameIsCompleteWhenAllTheBoxesAreFilled() {
        assertFalse(start.isOver());

        Game next1 = play(start, d0_0, d0_1, "player1");
        assertFalse(next1.isOver());

        Game next2 = play(next1, d0_1, d1_1, "player2");
        assertFalse(next2.isOver());

        Game next3 = play(next2, d1_1, d1_0, "player1");
        assertFalse(next3.isOver());

        Game end = play(next3, d1_0, d0_0, "player2");
        assertTrue(end.isOver());
    }

    @Test
    public void thereIsAWinner() {
        assertFalse(start.isOver());

        Game next1 = play(start, d0_0, d0_1, "player1");
        assertFalse(next1.isOver());

        Game next2 = play(next1, d0_1, d1_1, "player2");
        assertFalse(next2.isOver());

        Game next3 = play(next2, d1_1, d1_0, "player1");
        assertFalse(next3.isOver());

        Game end = play(next3, d1_0, d0_0, "player2");
        assertEquals("player2", end.winner());
    }

    @Test
    public void thereAreNoWinners() {
        Game game1x2 = new Game(1,2);
        Game next1 = play(game1x2, d0_0, d0_1, "player1");
        Game next2 = play(next1, d0_1, d1_1, "player2");
        Game next3 = play(next2, d1_1, d1_0, "player1");
        Game next4 = play(next3, d1_0, d0_0, "player2");

        Dot d2_1 = new Dot(2,1);
        Game next5 = play(next4, d1_1, d2_1, "player1");
        Game next6 = play(next5, d2_1, d2_0, "player2");
        Game end = play(next6, d1_0, d2_0, "player1");
        assertEquals("Game has drawn!", end.winner());
    }

    @Test
    public void keepsScore() {
        Game game1x2 = new Game(1,2);
        assertEquals("{}", game1x2.score().toString());

        Game next1 = play(game1x2, d0_0, d0_1, "player1");
        Game next2 = play(next1, d0_1, d1_1, "player2");
        Game next3 = play(next2, d1_1, d1_0, "player1");
        Game next4 = play(next3, d1_0, d0_0, "player2");
        assertEquals("{player2=1}", next4.score().toString());

        Dot d2_1 = new Dot(2,1);
        Game next5 = play(next4, d1_1, d2_1, "player1");
        Game next6 = play(next5, d2_1, d2_0, "player2");
        Game end = play(next6, d1_0, d2_0, "player1");
        assertEquals("{player1=1, player2=1}", end.score().toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void cannotCreateGameWithoutRows() {
        new Game(0,1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void cannotCreateGameWithoutColumns() {
        new Game(1,0);
    }

}
