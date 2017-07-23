package dots_and_boxes;

import dots_and_boxes.Dot;
import dots_and_boxes.Game;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class GameSpecs {

    private final Dot d00 = new Dot(0, 0);
    private final Dot d01 = new Dot(0, 1);
    private final Dot d10 = new Dot(1, 0);
    private final Dot d11 = new Dot(1, 1);
    private final Dot d02 = new Dot(0, 2);
    private final Dot d12 = new Dot(1, 2);
    private final String player1 = "player1";
    private final String player2 = "player2";
    private final List<String> players = Arrays.asList(player1, player2);
    private Game game = new Game(1, 1, players);
    private final Map<String, Integer> zeroScores = new HashMap<String, Integer>() {{
        put(player1, 0);
        put(player2, 0);
    }};

    @Test
    public void joinsDotsUsingHorizontalLine() throws Exception {
        assertTrue(game.join(d00, d01, player1));
    }

    @Test
    public void joinsDotsUsingVerticalLine() throws Exception {
        assertTrue(game.join(d00, d10, player1));
    }

    @Test
    public void cannotJoinDotsUsingBackwardDiagonalLine() throws Exception {
        assertFalse(game.join(d00, d11, player1));
    }

    @Test
    public void cannotJoinDotsUsingForwardDiagonalLine() throws Exception {
        assertFalse(game.join(d01, d10, player1));
    }

    @Test
    public void cannotJoinAlreadyJoinedLine() throws Exception {
        assertTrue(game.join(d00, d01, player1));
        assertFalse(game.join(d01, d00, player2));
    }

    @Test
    public void cannotJoinDotsNotPresentInTheGame() throws Exception {
        assertFalse(game.join(d00, d02, player1));
    }

    @Test
    public void beginsWithZeroScores() throws Exception {
        assertEquals(zeroScores, game.scores());
    }

    @Test
    public void zeroScoresWhenNoBoxesAreComplete() throws Exception {
        game.join(d00, d01, player1);
        assertEquals(zeroScores, game.scores());
        game.join(d00, d10, player2);
        assertEquals(zeroScores, game.scores());
        game.join(d10, d11, player1);
        assertEquals(zeroScores, game.scores());
    }

    @Test
    public void playerScoresAPointWhenTheFourthLineOfTheBoxIsMarked() throws Exception {
        player2CompletesABox(game, player1, player2);
        final HashMap<String, Integer> expected = new HashMap<String, Integer>() {{
            put(player1, 0);
            put(player2, 1);
        }};
        assertEquals(expected, game.scores());
    }

    private void player2CompletesABox(Game game, String player1, String player2) {
        game.join(d00, d01, player1);
        game.join(d00, d10, player2);
        game.join(d10, d11, player1);
        game.join(d11, d01, player2);
    }

    @Test
    public void eachPlayerScoresEqualPoints() throws Exception {
        Game game = new Game(1, 2, players);
        game.join(d00, d01, player1);
        game.join(d00, d10, player2);
        game.join(d10, d11, player1);
        game.join(d11, d12, player2);
        game.join(d11, d01, player1);
        final HashMap<String, Integer> expected = new HashMap<String, Integer>() {{
            put(player1, 1);
            put(player2, 0);
        }};
        assertEquals(expected, game.scores());

        game.join(d12, d02, player1);
        game.join(d01, d02, player2);

        expected.put(player2, 1);
        assertEquals(expected, game.scores());
    }

    @Test
    public void aPlayerScoresAllPoints() throws Exception {
        Game game = new Game(1, 2, players);
        player2CompletesABox(game, player1, player2);
        final HashMap<String, Integer> expected = new HashMap<String, Integer>() {{
            put(player1, 0);
            put(player2, 1);
        }};
        assertEquals(expected, game.scores());

        player2CompletesAnotherBox(game);
        expected.put(player2, 2);
        assertEquals(expected, game.scores());
    }

    private void player2CompletesAnotherBox(Game game) {
        game.join(d11, d12, player2);
        game.join(d02, d12, player1);
        game.join(d01, d02, player2);
    }

    @Test
    public void firstPlayerPlaysFirst() throws Exception {
        game.join(d00, d01, player1);
    }

    @Test
    public void secondPlayerPlaysSecond() throws Exception {
        game.join(d00, d01, player1);
        game.join(d00, d01, player2);
    }

    @Test
    public void shoutsWhenAnyPlayerPlaysOutOfTurn() throws Exception {
        try {
            game.join(d00, d01, player2);
        } catch (IllegalArgumentException e) {
            assertEquals("It's player1's turn!", e.getMessage());
        }
    }

    @Test
    public void playerCompletingABoxGetsSecondChance() throws Exception {
        Game game = new Game(1, 2, players);
        player2CompletesABox(game, player1, player2);
        assertEquals(player2, game.nextPlayerName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shoutsWhenAPlayerWhoCompletedABoxDoesNotPlayAgain() throws Exception {
        Game game = new Game(1, 2, players);
        player2CompletesABox(game, player1, player2);
        game.join(d01, d02, player1);
    }

    @Test
    public void isOver() throws Exception {
        Game game = new Game(1, 2, players);
        player2CompletesABox(game, player1, player2);
        assertFalse(game.isOver());

        Dot d12 = new Dot(1, 2);
        game.join(d11, d12, player2);
        game.join(d02, d12, player1);
        game.join(d01, d02, player2);
        assertTrue(game.isOver());
    }

    @Test
    public void hasWinnerWhenGameIsComplete() throws Exception {
        Game game = new Game(1, 2, players);
        player2CompletesABox(game, player1, player2);
        player2CompletesAnotherBox(game);
        assertEquals(player2, game.winner());
    }

    @Test
    public void hasNoWinnerWhenGameIsInProgress() throws Exception {
        Game game = new Game(1, 2, players);
        player2CompletesABox(game, player1, player2);
        assertEquals("Game is not complete yet!", game.winner());
    }

    @Test
    public void hasNoWinnerWhenGameHasCompletedWithEqualScores() throws Exception {
        Game game = new Game(1, 2, players);
        game.join(d00, d01, player1);
        game.join(d00, d10, player2);
        game.join(d10, d11, player1);
        game.join(d11, d12, player2);
        game.join(d11, d01, player1);
        game.join(d12, d02, player1);
        game.join(d01, d02, player2);
        assertEquals("Game has Drawn!", game.winner());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shoutsWhenMinimumRowsAreNotSatisfied() throws Exception {
        new Game(0, 1, players);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shoutsWhenMinimumColumnsAreNotSatisfied() throws Exception {
        new Game(1, 0, players);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shoutsWhenPlayersAreNotSpecified() throws Exception {
        new Game(1, 1, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shoutsWhenPlayersAreEmpty() throws Exception {
        new Game(1, 1, Collections.emptyList());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shoutsWhenPlayersAreNotAvailable() throws Exception {
        new Game(1, 1, Arrays.asList(player1));
    }
}