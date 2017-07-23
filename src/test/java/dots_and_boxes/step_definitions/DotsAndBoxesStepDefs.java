package dots_and_boxes.step_definitions;

import cucumber.api.Delimiter;
import cucumber.api.Transform;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.deps.com.thoughtworks.xstream.annotations.XStreamConverter;
import dots_and_boxes.Dot;
import dots_and_boxes.Game;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DotsAndBoxesStepDefs {
    private Game game;

    @Given("^a grid of size (\\d+)x(\\d+) with players \"(\\w[,\\s\\w]*)\"$")
    public void a_grid_of_size_x_(int rows, int cols, @Delimiter(", ") List<String> players, List<LineInfo> lines) throws Throwable {
        game = new Game(rows, cols, players);
        lines.stream()
                .filter(line -> line.lineMarked)
                .forEach(line -> game.join(line.d1, line.d2, line.who));
    }

    @When("^a player \"([^\"]*)\" joins dots (\\{\\d+,\\d+\\}) and (\\{\\d+,\\d+\\})$")
    public void a_player_joins_dots_and_(String player, @Transform(DotConverter.class) Dot d1,@Transform(DotConverter.class) Dot d2) throws Throwable {
        game.join(d1, d2, player);
    }

    @Then("^the grid looks like:$")
    public void the_grid_looks_like(List<LineInfo> lines) throws Throwable {
        final Set<LineInfo> expected = lines.stream()
                .filter(line -> line.lineMarked)
                .collect(Collectors.toSet());
        assertEquals(expected, getConnectedLines());
    }

    private Set<LineInfo> getConnectedLines() {
        final Set<LineInfo> actual = new HashSet<>();
        game.forEachConnectedLine((d1, d2) -> {
            final LineInfo lineInfo = new LineInfo();
            lineInfo.d1 = d1;
            lineInfo.d2 = d2;
            lineInfo.lineMarked = true;
            actual.add(lineInfo);
        });
        return actual;
    }

    @And("^player \"([^\"]*)\" gets another chance$")
    public void player_gets_another_chance(String expectedNextPlayer) throws Throwable {
        assertEquals(expectedNextPlayer, game.nextPlayerName());
    }

    @And("^the players have scores \\{\"([^\"]*)\" = (\\d+), \"([^\"]*)\" = (\\d+)\\}$")
    public void the_players_have_scores_(String player1, int score1, String player2, int score2) throws Throwable {
        final Map<String, Integer> expectedScores = new HashMap<String, Integer>() {{
            put(player1, score1);
            put(player2, score2);
        }};
        assertEquals(expectedScores, game.scores());
    }

    @And("^the game is over$")
    public void the_game_is_over() throws Throwable {
        assertTrue(game.isOver());
    }

    @And("^the winner is \"([^\"]*)\"$")
    public void the_winner_is(String winner) throws Throwable {
        assertEquals(winner, game.winner());
    }
}

class LineInfo {
    String box;
    @XStreamConverter(DotConverter.class)
    Dot d1;
    @XStreamConverter(DotConverter.class)
    Dot d2;
    boolean lineMarked;
    String who;

    @Override
    public String toString() {
        if(lineMarked)
            return String.format("%s---%s", d1, d2);
        return String.format("%s   %s", d1, d2);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LineInfo lineInfo = (LineInfo) o;

        if (lineMarked != lineInfo.lineMarked) return false;
        if (!d1.equals(lineInfo.d1)) return false;
        if (!d2.equals(lineInfo.d2)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = d1.hashCode();
        result = 31 * result + d2.hashCode();
        result = 31 * result + (lineMarked ? 1 : 0);
        return result;
    }
}
