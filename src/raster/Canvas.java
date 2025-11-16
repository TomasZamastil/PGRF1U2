package raster;

import geometry.functions.Clipper;
import geometry.shape.Polygon;
import raster.functions.SeedFiller;
import util.Colors;
import util.PolygonException;

import java.awt.image.BufferedImage;

public class Canvas extends BufferedImage {

    private Polygon polygon;
    private Polygon clippingPolygon;
    private SeedFiller seedFiller;

    public Canvas(int width, int height) {
        super(width, height, BufferedImage.TYPE_INT_RGB);

        polygon = new Polygon();
        clippingPolygon = new Polygon();
        seedFiller = new SeedFiller(this);

        // Vyplnit celé plátno pozadím (jinak by bylo 0x000000 a fill by nefungoval)
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                setRGB(x, y, Colors.BACKGROUND);
            }
        }
    }


    /* ======================== DRAWING ======================== */

    public void draw() {
        drawPolygon(polygon, Colors.MAIN_POLYGON);
        drawPolygon(clippingPolygon, Colors.CLIPPING_POLYGON);
    }

    public void drawPolygon(Polygon polygon, int color) {
        polygon.rasterized(color).forEach(this::drawPixel);
    }


    /* ======================== CLIPPING ======================== */

    public Polygon getClippedPolygon() {
        try {
            Clipper clipper = new Clipper(clippingPolygon);
            return clipper.clip(polygon);
        } catch (PolygonException e) {
            return null;
        }
    }


    /* ======================== PIXEL API ======================== */

    public Pixel getPixel(int x, int y) {
        return new Pixel(x, y, getColor(x, y));
    }

    public int getColor(int x, int y) {
        // getRGB vrací ARGB, ale my používáme RGB, takže zahodíme alfu
        return getRGB(x, y) & 0xFFFFFF;
    }

    public void drawPixel(Pixel pixel) {
        setRGB(pixel.getX(), pixel.getY(), pixel.getColor());
    }


    /* ======================== FILL ======================== */

    public void fill(Pixel seed) {
        seedFiller.fill(seed);
    }


    /* ======================== PROPERTIES ======================== */

    public Polygon getPolygon() {
        return polygon;
    }

    public void setPolygon(Polygon polygon) {
        this.polygon = polygon;
    }

    public Polygon getClippingPolygon() {
        return clippingPolygon;
    }

    public void setClippingPolygon(Polygon clippingPolygon) {
        this.clippingPolygon = clippingPolygon;
    }
}
