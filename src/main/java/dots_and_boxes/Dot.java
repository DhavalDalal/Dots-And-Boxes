package dots_and_boxes;

public class Dot {
    private final double x;
    private final double y;

    public Dot(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Dot dot = (Dot) o;

        if (x != dot.x) return false;
        if (y != dot.y) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(x);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return String.format("{%.0f, %.0f}", x, y);
    }

    public boolean canBeJoinedHorizontallyOrVerticallyWith(Dot other) {
        if (other == this)
            return false;

        return formsVerticalLine(other) || formsHorizontalLine(other);
    }

    private boolean formsHorizontalLine(Dot other) {
        return this.y == other.y;
    }

    private boolean formsVerticalLine(Dot other) {
        return this.x == other.x;
    }

    public boolean isWithin(Dot lowerBound, Dot upperBound) {
        final boolean includeBounds = true;
        return isWithin(lowerBound, upperBound, includeBounds);
    }

    public Dot translate(double dx, double dy) {
        return new Dot(x + dx, y + dy);
    }

    public boolean isWithin(Dot lowerBound, Dot upperBound, boolean includeBounds) {
        if (includeBounds)
            return x >= lowerBound.x && x <= upperBound.x
                && y >= lowerBound.y && y <= upperBound.y;

        return x > lowerBound.x && x < upperBound.x
                && y > lowerBound.y && y < upperBound.y;
    }

//    public static Dot average(Dot first, Dot... rest) {
//        List<Dot> allDots = new ArrayList<>(Arrays.asList(rest));
//        allDots.add(first);
//        final double sumX = allDots.stream().mapToDouble(dot -> dot.x).sum();
//        final double sumY = allDots.stream().mapToDouble(dot -> dot.y).sum();
//
//        return new Dot(sumX/allDots.size(), sumY/allDots.size());
//    }
//
//    public Double distanceTo(Dot other) {
//        return Math.sqrt(square(x - other.x) + square(y - other.y));
//    }
//
//    private Double square(double n) {
//        return n * n;
//    }
}
