package geometry.functions;

import geometry.shape.Line;
import geometry.shape.Polygon;
import geometry.Coordinates;
import geometry.shape.Edge;
import util.PolygonException;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public class Clipper {

    Polygon clipper;

    public Clipper(Polygon clipper) {
        if(clipper.numberOfVertices() < 3) throw new PolygonException(PolygonException.CLIPPER_INSUFFICIENT_VERTICES);
        this.clipper = clipper;
    }

    public Polygon clip(Polygon polygon) {
        List<Coordinates> outputList = polygon.getVertices();
        List<Edge> clippingEdges = clipper.getCounterClockwise().getEdges();

        for(Edge clippingEdge : clippingEdges) {
            outputList = clipByEdge(outputList, clippingEdge);
        }
        return new Polygon(outputList);
    }

    private List<Coordinates> clipByEdge(List<Coordinates> inputList, Edge clippingEdge) {
        if(inputList.isEmpty()) return inputList;
        List<Coordinates> clippedList = new ArrayList<>();
        Coordinates v1 = inputList.get(inputList.size() - 1);
        for(Coordinates v2 : inputList) {
           if(clippingEdge.hasInside(v2)) {
               if(! clippingEdge.hasInside(v1)) {
                   clippedList.add(clippingEdge.calculateIntersection(new Line(v1,v2)));
               }
               clippedList.add(v2);
           } else if(clippingEdge.hasInside(v1)) {
               clippedList.add(clippingEdge.calculateIntersection(new Line(v1,v2)));
           }
           v1 = v2;
        }
        return clippedList;
    }

}
