package raster;

import geometry.shape.Line;
import util.Colors;
import geometry.Coordinates;

import java.awt.image.BufferedImage;

public class CanvasPreview extends BufferedImage {

    public CanvasPreview(int width, int height) {
        super(width, height, BufferedImage.TYPE_INT_ARGB);
    }

    public void drawPreviewLine(Coordinates from, Coordinates to) {
        new Line(from, to)
                .rasterized(Colors.PREVIEW_LINE)
                .forEach(pixel -> {
                    int rgb = pixel.getColor(); // 0xRRGGBB
                    int argb = (0x80 << 24) | (rgb & 0xFFFFFF);
                    // 0x80 = poloprůhledná alpha, můžeš změnit na 0xFF (plně krycí)

                    setRGB(pixel.getX(), pixel.getY(), argb);
                });
    }
}
