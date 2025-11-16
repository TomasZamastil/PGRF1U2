package geometry;

public class Coordinates {

    private final double x;

    private final double y;

    private final double EQUALS_PRECISION = 0.001;

    public Coordinates(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double calculateDistance(Coordinates coordinates) {
        return Math.sqrt(Math.pow(coordinates.x - x, 2) + Math.pow(coordinates.y - y, 2));
    }

    public boolean xEquals(Coordinates coord) {
        return Math.abs(coord.x - x) < EQUALS_PRECISION;
    }

    public boolean yEquals(Coordinates coord) {
        return Math.abs(coord.y - y) < EQUALS_PRECISION;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Coordinates)) return false;
        Coordinates coordinates = (Coordinates) obj;
        return xEquals(coordinates) && yEquals(coordinates);
    }

    @Override
    public String toString() {
        return "[" + x + ", " + y + "]";
    }
}
