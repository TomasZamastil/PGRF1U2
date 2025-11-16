package geometry.shape;

import geometry.Coordinates;
import raster.functions.Rasterizer;
import raster.Pixel;

import java.util.Collection;
import java.util.List;

public interface Shape {

    public Collection<Coordinates> getShape();

    public default List<Pixel> rasterized(int color) {
        return Rasterizer.rasterize(getShape(), color);
    }

}
