package dots_and_boxes;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

public class Game {
    private final List<Box> boxes;

    private final Predicate<Box> nonEmptyBox = box -> box.isOccupied();
    private final Function<Box, String> occupier = box -> box.map((lines, takenBy) -> takenBy);

    public Game(int rows, int cols) {
        this.boxes = boxes(dots(rows, cols));
    }

    private Game(List<Box> boxes) {
        this.boxes = boxes;
    }

    private List<Dot> dots(int rows, int cols) {
        return Stream.iterate(0, i -> i + 1).limit(cols + 1)
                .flatMap(x -> Stream.iterate(0, j -> j + 1).limit(rows + 1)
                        .map(y -> new Dot(x, y))).collect(toList());
    }

    private List<Box> boxes(List<Dot> dots) {
        return dots.stream().map(dot -> box(dot))
                .filter(box -> box.isContainedIn(dots))
                .collect(toList());
    }

    private Box box(Dot dot) {
        Dot bottomLeft = dot;
        Dot topLeft = dot.translate(0, 1);
        Dot topRight = dot.translate(1, 1);
        Dot bottomRight = dot.translate(1, 0);
        return new Box(bottomLeft, topLeft, topRight, bottomRight);
    }

    public List<?> join(Dot d1, Dot d2, String player) {
        List<Box> newBoxes = this.boxes.stream()
                .map(box -> box.join(d1, d2, player))
                .collect(toList());

        String who = filled(newBoxes) > filled(boxes) ? player : "";
        Game nextGame = new Game(newBoxes);
        return Arrays.asList(who, nextGame);
    }

    private long filled(List<Box> boxes) {
        return boxes.stream().filter(nonEmptyBox).count();
    }

    @Override
    public String toString() {
        return String.format("Game(%s)", boxes);
    }

    public<T> T map(Function<List<Box>, T> mapper) {
        return mapper.apply(Collections.unmodifiableList(boxes));
    }

    public Map<String, Long> score() {

        return boxes.stream()
                .filter(nonEmptyBox)
                .collect(groupingBy(occupier, Collectors.counting()));
    }

    public boolean isOver() {
        return boxes.stream().filter(nonEmptyBox).count() == boxes.size();
    }

    public String winner() {
        final Comparator<Map.Entry<String, Long>> ascending = comparingByValue();
        final List<Map.Entry<String, Long>> scores = score().entrySet()
                .stream()
                .sorted(ascending.reversed())
                .limit(2)
                .collect(Collectors.toList());

        if(scores.size() == 1)
            return scores.get(0).getKey();

        final Map.Entry<String, Long> first = scores.get(0);
        final Map.Entry<String, Long> second = scores.get(1);

        if(first.getValue() == second.getValue())
            return "Game has drawn!";

        return first.getKey();
    }
}
