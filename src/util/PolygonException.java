package util;

public class PolygonException extends IllegalArgumentException {

    public static final String NO_SUCH_VERTEX = "The vertex is not a part of the polygon.";
    public static final String NO_SUCH_EDGE = "The edge is not a part of the polygon.";
    public static final String CLIPPER_INSUFFICIENT_VERTICES = "A clipping polygon requires at least 3 vertices.";

    public PolygonException(String s) {
        super(s);
    }

}
