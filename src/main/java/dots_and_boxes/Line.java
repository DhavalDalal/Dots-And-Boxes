package dots_and_boxes;

import java.util.List;
import java.util.function.BiFunction;

public class Line {
    private final Dot d1, d2;
    private boolean present;

    public Line(Dot d1, Dot d2, boolean present) {
        this.d1 = d1;
        this.d2 = d2;
        this.present = present;
    }

    public boolean hasEndPointsIn(List<Dot> dots) {
        return dots.contains(d1) && dots.contains(d2);
    }

    public Line join(Dot d1, Dot d2) {
        if(hasEndPoints(d1, d2))
            return new Line(this.d1, this.d2, true);

        return this;
    }

    private boolean hasEndPoints(Dot d1, Dot d2) {
        return (this.d1.equals(d1) && this.d2.equals(d2)) ||
                (this.d1.equals(d2) && this.d2.equals(d1));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Line line = (Line) o;

        if (present != line.present) return false;
        if (d1 != null ? !d1.equals(line.d1) : line.d1 != null) return false;
        if (d2 != null ? !d2.equals(line.d2) : line.d2 != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = d1 != null ? d1.hashCode() : 0;
        result = 31 * result + (d2 != null ? d2.hashCode() : 0);
        result = 31 * result + (present ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return String.format("Line(%s, %s, %s)", d1, d2, present);
    }


    public boolean isMarked() {
        return present;
    }

    public<T> T map(BiFunction<Dot, Dot, T> mapper) {
        return mapper.apply(d1, d2);
    }
}
