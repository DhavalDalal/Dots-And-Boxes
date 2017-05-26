package dots_and_boxes;

//import cucumber.deps.com.thoughtworks.xstream.annotations.XStreamConverter;

// Just annotate this class with XStreamConverter
// But this dependency on XStream goes to production, while its
// only needed in test.  what to do?
//@XStreamConverter(DotConverter.class)
//
// Hack!
//
// There is a file LocalizedXStreams (package cucumber.runtime.xstream)
// Solution: Add a line in the constructor where registrations are done.
//
// register(converterRegistry, new DotConverter());
//
// so that we don't carry this @XStreamConverter annotation to
// production!

public class Dot {
    private final int x, y;

    public Dot(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Dot translate(int dx, int dy) {
        return new Dot(x + dx, y + dy);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Dot dot = (Dot) o;

        return (x == dot.x) && (y == dot.y);
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }

    @Override
    public String toString() {
        return String.format("{%d,%d}", x, y);
    }
}
