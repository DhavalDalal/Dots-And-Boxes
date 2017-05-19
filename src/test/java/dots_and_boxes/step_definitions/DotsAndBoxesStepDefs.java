package dots_and_boxes.step_definitions;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import dots_and_boxes.Box;
import dots_and_boxes.Dot;
import dots_and_boxes.Game;
import dots_and_boxes.Line;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DotsAndBoxesStepDefs {
    private Game game;
    private String nextPlayer;

    @Given("^a grid of size (\\d+)x(\\d+):$")
    public void a_grid_of_size_x_(int rows, int cols, List<LineInfo> lines) throws Throwable {
        game = new Game(rows, cols);
        setupGame(lines);
    }

    private void setupGame(List<LineInfo> lines) {
        lines.stream()
            .filter(line -> line.present)
            .map(line -> Arrays.asList(line.d1, line.d2, line.who))
            .forEach((List details) ->
            {
                String player = (String) details.get(2);
                String who = player == null ? "": player;
                List<?> result = game.join((Dot) details.get(0), (Dot) details.get(1), who);
                nextPlayer = (String) result.get(0);
                game = (Game) result.get(1);
            });

    }

    @When("^a player \"([^\"]*)\" joins dots (\\{\\d+,\\d+\\}) and (\\{\\d+,\\d+\\})$")
    public void a_player_joins_dots_and_(String player, Dot d1, Dot d2) throws Throwable {
        List<?> result = game.join(d1, d2, player);
        nextPlayer = (String) result.get(0);
        game = (Game) result.get(1);
    }

    @Then("^the grid looks like:$")
    public void the_grid_looks_like(List<LineInfo> expected) throws Throwable {
        List<LineInfo> result = game.map(boxes ->
            Stream.iterate(0, i -> i + 1)
                .limit(boxes.size())
                .flatMap(i -> LineInfo.from(i + 1, boxes.get(i)))
                .collect(toList()));

        assertEquals(replaceNullTakenByWithEmpty(expected), result);
    }

    private List<LineInfo> replaceNullTakenByWithEmpty(List<LineInfo> lines) {
        return lines.stream().map(lineInfo -> {
            lineInfo.who = lineInfo.who == null? "" : lineInfo.who;
            return lineInfo;
        }).collect(toList());
    }

    @And("^the players have scores \\{\"([^\"]*)\":(\\d+), \"([^\"]*)\":(\\d+)\\}.$")
    public void the_players_have_scores_(String player1, int score1, String player2, int score2) throws Throwable {
        final Map<String, Integer> expectedScores = new HashMap() {{
            put(player1, score1);
            put(player2, score2);
        }};
        assertEquals(expectedScores.toString(), game.score().toString());
    }

    @And("^player \"([^\"]*)\" gets another chance.$")
    public void player_gets_another_chance(String expectedPlayer) throws Throwable {
        assertEquals(expectedPlayer, nextPlayer);
    }

    @And("^the game is over.$")
    public void the_game_is_over() throws Throwable {
        assertTrue(game.isOver());
    }

    @And("^the winner is \"([^\"]*)\".$")
    public void the_winner_is_(String expectedPlayer) throws Throwable {
        assertEquals(expectedPlayer, game.winner());
    }

    @And("^the players have scores \\{\\}.$")
    public void the_players_have_scores_() throws Throwable {
        assertEquals(new HashMap<String, Long>(), game.score());
    }

    @And("^the players have scores \\{\"([^\"]*)\":(\\d+)\\}.$")
    public void the_players_have_scores_(String player, int score) throws Throwable {
        final Map<String, Integer> expectedScores = new HashMap<>();
        expectedScores.put(player, score);
        assertEquals(expectedScores.toString(), game.score().toString());
    }
}

class LineInfo {
    int box;
    Dot d1;
    Dot d2;
    boolean present;
    String who;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LineInfo lineInfo = (LineInfo) o;

        if (box != lineInfo.box) return false;
        if (present != lineInfo.present) return false;
        if (!d1.equals(lineInfo.d1)) return false;
        if (!d2.equals(lineInfo.d2)) return false;
        if (who != null ? !who.equals(lineInfo.who) : lineInfo.who != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = box;
        result = 31 * result + d1.hashCode();
        result = 31 * result + d2.hashCode();
        result = 31 * result + (present ? 1 : 0);
        result = 31 * result + (who != null ? who.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "LineInfo{" +
                "box=" + box +
                ", d1=" + d1 +
                ", d2=" + d2 +
                ", present=" + present +
                ", who='" + who + '\'' +
                '}';
    }

    public static Stream<LineInfo> from(int boxNumber, Box box) {
        return box.map((lines, takenBy) ->
            lines.stream()
                .map(line ->
                    line.map((d1, d2) ->
                        toLineInfo(boxNumber, takenBy, d1, d2, line.isMarked()))));
    }

    private static LineInfo toLineInfo(int boxNumber, String takenBy, Dot d1, Dot d2, boolean present) {
        LineInfo lineInfo = new LineInfo();
        lineInfo.box = boxNumber;
        lineInfo.d1 = d1;
        lineInfo.d2 = d2;
        lineInfo.present = present;
        lineInfo.who = takenBy;
        return lineInfo;
    }
}