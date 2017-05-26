package dots_and_boxes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;

import static java.util.stream.Collectors.toList;

public class Box {
    private List<Line> lines = new ArrayList<>();
    private String takenBy = "";

    public Box(Dot bottomLeft, Dot topLeft, Dot topRight, Dot bottomRight) {
        lines.add(new Line(bottomLeft, topLeft, false));
        lines.add(new Line(topLeft, topRight, false));
        lines.add(new Line(topRight, bottomRight, false));
        lines.add(new Line(bottomRight, bottomLeft, false));
    }

    private Box(List<Line> newLines, String player) {
        this.lines = newLines;
        this.takenBy = player;
    }

    public boolean isContainedIn(List<Dot> dots) {
        return lines.stream().allMatch(line -> line.hasEndPointsIn(dots));
    }

    public Box join(Dot d1, Dot d2, String player) {
        if(isOccupied())
            return this;

        List<Line> newLines = lines.stream().map(line -> line.join(d1, d2)).collect(toList());
        String who = newLines.stream().allMatch(line -> line.isMarked()) ? player : "";
        return new Box(newLines, who);
    }

    public boolean isOccupied() {
        return !takenBy.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Box box = (Box) o;

        if (lines != null ? !lines.equals(box.lines) : box.lines != null) return false;
        if (takenBy != null ? !takenBy.equals(box.takenBy) : box.takenBy != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = lines != null ? lines.hashCode() : 0;
        result = 31 * result + (takenBy != null ? takenBy.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return String.format("Box(%s, %s)", lines, takenBy);
    }


    public<T> T map(BiFunction<List<Line>, String, T> mapper) {
        return mapper.apply(Collections.unmodifiableList(lines), takenBy);
    }
}
