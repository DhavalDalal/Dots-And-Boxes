package dots_and_boxes.step_definitions;

import com.sun.xml.internal.ws.util.StreamUtils;
import cucumber.api.Delimiter;
import cucumber.api.PendingException;
import cucumber.api.Transform;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.deps.com.thoughtworks.xstream.annotations.XStreamConverter;
import dots_and_boxes.Box;
import dots_and_boxes.Dot;
import dots_and_boxes.Game;

import java.util.*;
import java.util.function.BiFunction;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

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
                .filter(line -> line.lineMarked)
                .map(line -> Arrays.asList(line.d1, line.d2, line.who))
                .forEach((List details) ->
                {
                    String player = (String) details.get(2);
                    String who = player == null ? "" : player;
                    List<?> result = game.join((Dot) details.get(0), (Dot) details.get(1), who);
                    nextPlayer = (String) result.get(0);
                    game = (Game) result.get(1);
                });

    }

    @When("^a player \"([^\"]*)\" joins dots (\\{\\d+,\\d+\\}) and (\\{\\d+,\\d+\\})$")
    public void a_player_joins_dots_and_(String player, @Transform(DotConverter.class) Dot d1, @Transform(DotConverter.class) Dot d2) throws Throwable {
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
            lineInfo.who = lineInfo.who == null ? "" : lineInfo.who;
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

    @Then("^grid looks like:$")
    public void a_grid(List<String> dotsAndLines) throws Throwable {
        List<String> lines = dotsAndLines.stream().map(line -> line.replaceAll(" ", "")).collect(toList());
        System.out.println("lines = " + lines);
        int rows = (dotsAndLines.size() - 1) / 2;
        System.out.println("rows = " + rows);
        int cols = lines.get(0).replaceAll("-", "").length() - 1;
        System.out.println("cols = " + cols);
        game = new Game(rows, cols);

        List<Dot> dotsRowWise = Stream.iterate(0, i -> i + 1).limit(rows + 1)
                .flatMap(x -> Stream.iterate(0, j -> j + 1).limit(cols + 1).map(y -> new Dot(x, y)))
                .collect(toList());
        List<List<Dot>> horizontal = chunksOf(cols + 1, dotsRowWise);
        final List<List<?>> horizontalLines = toDotPairs(horizontal);
        System.out.println("horizontalLines = " + horizontalLines);


        List<Dot> dotsColWise = Stream.iterate(0, i -> i + 1).limit(cols + 1)
                .flatMap(x -> Stream.iterate(0, j -> j + 1).limit(rows + 1).map(y -> new Dot(y, x)))
                .collect(toList());
        final List<List<Dot>> vertical = chunksOf(rows + 1, dotsColWise);
        final List<List<?>> verticalLines = toDotPairs(vertical);
        System.out.println("verticalLines = " + verticalLines);

    }

    private List<List<?>> toDotPairs(List<List<Dot>> lines) {
      return lines.stream()
              .flatMap(line -> zipWith((d1, d2) -> Arrays.asList(d1, d2), line.stream(), line.subList(1, line.size() - 1).stream()))
              .collect(toList());
    }

    private <T> List<List<T>> chunksOf(int howMany, List<T> list) {
        return chunk(new ArrayList(), howMany, list);
    }

    private <T> List<List<T>> chunk(List<List<T>> acc, int howMany, List<T> list) {
        if (list.isEmpty())
            return acc;

        acc.add(list.stream().limit(howMany).collect(toList()));
        return chunk(acc, howMany, list.stream().skip(howMany).collect(toList()));
    }

    private <T, U, R> Stream<R> zipWith(BiFunction<T, U, R> fn, Stream<T> t, Stream<U> u) {
        final Iterator<T> tIterator = t.iterator();
        final Iterator<U> uIterator = u.iterator();

        class ZippedIterator implements Iterator<R> {

            @Override
            public boolean hasNext() {
                return tIterator.hasNext() && uIterator.hasNext();
            }

            @Override
            public R next() {
                T t = tIterator.next();
                U u = uIterator.next();
                return fn.apply(t, u);
            }
        }

        final ZippedIterator zippedIterator = new ZippedIterator();
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(zippedIterator, Spliterator.ORDERED), t.isParallel() && u.isParallel());
    }
}

class LineInfo {
    int box;
    @XStreamConverter(DotConverter.class) Dot d1;
    @XStreamConverter(DotConverter.class) Dot d2;
    boolean lineMarked;
    String who;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LineInfo lineInfo = (LineInfo) o;

        if (box != lineInfo.box) return false;
        if (lineMarked != lineInfo.lineMarked) return false;
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
        result = 31 * result + (lineMarked ? 1 : 0);
        result = 31 * result + (who != null ? who.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "LineInfo{" +
                "box=" + box +
                ", d1=" + d1 +
                ", d2=" + d2 +
                ", lineMarked=" + lineMarked +
                ", who='" + who + '\'' +
                '}';
    }

    public static Stream<LineInfo> from(int boxNumber, Box box) {
        return box.map((lines, takenBy) ->
                lines.stream()
                        .map(line ->
                                line.map((d1, d2, present) ->
                                        toLineInfo(boxNumber, takenBy, d1, d2, present))));
    }

    private static LineInfo toLineInfo(int boxNumber, String takenBy, Dot d1, Dot d2, boolean present) {
        LineInfo lineInfo = new LineInfo();
        lineInfo.box = boxNumber;
        lineInfo.d1 = d1;
        lineInfo.d2 = d2;
        lineInfo.lineMarked = present;
        lineInfo.who = takenBy;
        return lineInfo;
    }
}