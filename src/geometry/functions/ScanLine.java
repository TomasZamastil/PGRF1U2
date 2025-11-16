package geometry.functions;

import geometry.shape.Line;
import geometry.shape.Polygon;
import geometry.Coordinates;
import geometry.shape.Edge;
import util.Sorter;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ScanLine {

    public static List<Coordinates> getFill(Polygon polygon) {
        if(polygon.numberOfVertices() <3) return new ArrayList<>();
        List<Coordinates> filled = new ArrayList<>();

        List<Edge> edges = polygon.getEdges().stream().filter(edge -> !edge.isVertical()).map(Edge::getOrintedY).collect(Collectors.toList());

        IntStream.rangeClosed(yMin(edges), yMax(edges)).forEach(y -> {
            List<Integer> intersections = edges.stream().filter(edge -> edge.isIntersection(y)).mapToInt(edge -> edge.getIntersection(y)).boxed().collect(Collectors.toList());
            Sorter.insertionSort(intersections);
            IntStream.range(0, intersections.size()).filter(i -> i % 2 == 0).mapToObj(i -> new Line(new Coordinates(intersections.get(i)+1, y), new Coordinates(intersections.get(i + 1), y)))
                    .forEach(line -> filled.addAll(line.getShape()));
        });
        return filled;
    }

    private static int yMin(List<Edge> processedEdges) {
        return (int) processedEdges.stream().mapToDouble(edge -> edge.getPointA().getY()).min().getAsDouble();
    }

    private static int yMax(List<Edge> processedEdges) {
        return (int) processedEdges.stream().mapToDouble(edge -> edge.getPointB().getY()).max().getAsDouble();
    }

    private ScanLine() {
    }

}
