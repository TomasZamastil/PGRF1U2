package raster;

import util.Colors;

public class Pixel {

    private final int x;

    private final int y;

    private int color = Colors.DEFAULT;

    public Pixel(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Pixel(int x, int y, int color) {
        this(x,y);
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "["+x+", "+y+"]";
    }

}
