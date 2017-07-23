package dots_and_boxes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Box {

    private final Dot bottomLeft;
    private final Dot topLeft;
    private final Dot topRight;
    private final Dot bottomRight;

    public Box(Dot bottomLeft, Dot topLeft, Dot topRight, Dot bottomRight) {
        this.bottomLeft = bottomLeft;
        this.topLeft = topLeft;
        this.topRight = topRight;
        this.bottomRight = bottomRight;
    }

    public boolean canBeCreatedUsing(List<List<Dot>> lines) {
        List<List<Dot>> possibleBoxLines = lines();
        possibleBoxLines.addAll(reverseLines());

        if (numberOfBoxLinesIn(lines, possibleBoxLines) != 4)
            return false;

        return true;
//        return canMakeBoxFrom(corners(possibleBoxLines));
    }

    public List<List<Dot>> lines() {
        return new ArrayList<List<Dot>>() {
            {
                add(Arrays.asList(bottomLeft, topLeft));
                add(Arrays.asList(topLeft, topRight));
                add(Arrays.asList(topRight, bottomRight));
                add(Arrays.asList(bottomRight, bottomLeft));
            }
        };
    }

    private List<List<Dot>> reverseLines() {
        return new ArrayList<List<Dot>>() {
            {
                add(Arrays.asList(topLeft, bottomLeft));
                add(Arrays.asList(topRight, topLeft));
                add(Arrays.asList(bottomRight, topRight));
                add(Arrays.asList(bottomLeft, bottomRight));

            }
        };
    }

//    private Set<Dot> corners(List<List<Dot>> lines) {
//        return lines.stream()
//                .flatMap(line -> line.stream())
//                .collect(Collectors.toSet());
//    }

    private long numberOfBoxLinesIn(List<List<Dot>> lines, List<List<Dot>> boxLines) {
        return boxLines.stream()
                .filter(boxLine -> lines.contains(boxLine))
                .count();
    }

//    private boolean canMakeBoxFrom(Set<Dot> dots) {
//        if (dots.size() != 4)
//            return false;
//
//        final Dot average = average(dots);
//        boolean allCornersToCenterDistancesAreOfEqualLength = dots.stream()
//                .map(dot -> dot.distanceTo(average))
//                .collect(Collectors.toSet())
//                .size() == 1;
//
//        boolean allSidesAreOfEqualLength = dots.stream()
//                .flatMap(d1 -> dots.stream()
//                        .filter(d2 -> d1.canBeJoinedHorizontallyOrVerticallyWith(d2))
//                        .map(d2 -> d1.distanceTo(d2)))
//                .collect(Collectors.toSet())
//                .size() == 1;
//
//        return allCornersToCenterDistancesAreOfEqualLength && allSidesAreOfEqualLength;
//    }
//
//    private Dot average(Set<Dot> dots) {
//        Dot[] corners = new Dot[dots.size()];
//        dots.toArray(corners);
//        final Dot first = corners[0];
//        final Dot[] rest = Arrays.copyOfRange(corners, 1, corners.length);
//        return Dot.average(first, rest);
//    }

    public boolean isWithin(Dot lowerBound, Dot upperBound) {
        return Arrays.asList(bottomLeft, topLeft, topRight, bottomRight)
                .stream()
                .allMatch(dot -> dot.isWithin(lowerBound, upperBound));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Box box = (Box) o;

        return bottomLeft.equals(box.bottomLeft)
                && bottomRight.equals(box.bottomRight)
                && topLeft.equals(box.topLeft)
                && topRight.equals(box.topRight);
    }

    @Override
    public int hashCode() {
        int result = bottomLeft.hashCode();
        result = 31 * result + topLeft.hashCode();
        result = 31 * result + topRight.hashCode();
        result = 31 * result + bottomRight.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Box{" +
                "bottomLeft=" + bottomLeft +
                ", topLeft=" + topLeft +
                ", topRight=" + topRight +
                ", bottomRight=" + bottomRight +
                '}';
    }
}
