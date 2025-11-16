package raster.functions;

import geometry.Coordinates;
import raster.Pixel;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static app.Main.APP_MAX_HEIGHT;
import static app.Main.APP_MAX_WIDTH;

public class Rasterizer {

    public static List<Pixel> rasterize(Collection<Coordinates> object, int color) {
        return object.stream().filter(coordinate -> coordinate.getX() >= 0 && coordinate.getY() >= 0 && coordinate.getX() < APP_MAX_WIDTH && coordinate.getY() < APP_MAX_HEIGHT)      //delete coordinates beyond app screen border
                .map(coordinate -> new Pixel((int)coordinate.getX(), (int)coordinate.getY(), color))
                .collect(Collectors.toList());
    }

    private Rasterizer() {}
}
