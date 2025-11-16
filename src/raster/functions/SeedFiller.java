package raster.functions;

import raster.Canvas;
import util.Colors;
import util.Pattern;
import raster.Pixel;


import java.util.*;
import java.util.stream.IntStream;

public class SeedFiller {

    private Canvas canvas;

    private int backgroundColor;

    public SeedFiller(Canvas canvas) {
        this.canvas = canvas;
    }

    public void fill(Pixel seed) {
        backgroundColor = seed.getColor();
        if(Colors.FILL == backgroundColor || Pattern.BACKGROUND == backgroundColor) return;
        Queue<Pixel> queue = new ArrayDeque<>();
        queue.add(seed);
        while (!queue.isEmpty()) {
            Pixel currentSeed = queue.poll();
            int currentY = currentSeed.getY();
            int leftBorderX = IntStream.iterate(currentSeed.getX(), i -> i - 1).filter(i -> canvas.getColor(i - 1, currentY) != backgroundColor).findFirst().getAsInt();

            boolean seedUp = false;
            boolean seedDown = false;

            for (; toBeFilled(leftBorderX, currentY); leftBorderX++) {
                canvas.drawPixel(new Pixel(leftBorderX, currentY, Colors.FILL));
                if (!seedDown && toBeFilled(leftBorderX, currentY + 1)) {
                    queue.add(canvas.getPixel(leftBorderX, currentY + 1));
                    seedDown = true;
                }
                if (!seedUp && toBeFilled(leftBorderX, currentY - 1)) {
                    queue.add(canvas.getPixel(leftBorderX, currentY - 1));
                    seedUp = true;
                }
            }
            if (toBeFilled(leftBorderX - 1, currentY + 1)) queue.add(canvas.getPixel(leftBorderX, currentY + 1));
            if (toBeFilled(leftBorderX - 1, currentY - 1)) queue.add(canvas.getPixel(leftBorderX, currentY - 1));
        }
    }

    private boolean toBeFilled(int x, int y) {
        return canvas.getColor(x, y) == backgroundColor;
    }

}
