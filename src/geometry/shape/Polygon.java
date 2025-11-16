package geometry.shape;

import geometry.Coordinates;
import util.PolygonException;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Polygon implements Shape {

    private List<Coordinates> vertices;

    public Polygon() {
        vertices = new ArrayList<>();
    }

    public Polygon(List<Coordinates> vertices) {
        this.vertices = vertices;
    }

    public Polygon(Polygon polygon) {
        this.vertices = new ArrayList<>(polygon.getVertices());
    }

    public void addVertex(Coordinates vertex) {
        vertices.add(vertex);
    }

    @Override
    public List<Coordinates> getShape() {
        return getEdges().stream().flatMap(edge -> edge.getShape().stream()).collect(Collectors.toList());
    }

    public List<Edge> getEdges() {
        return IntStream.range(0, numberOfVertices()).mapToObj(i -> new Edge(vertices.get(i), vertices.get((i + 1) % numberOfVertices()))).collect(Collectors.toList());
    }

    public boolean isCounterClockwise() {
        return getEdges().stream().mapToDouble(edge -> (edge.getPointB().getX() - edge.getPointA().getX()) * (edge.getPointB().getY() + edge.getPointA().getY())).sum() > 0;
    }

    public Edge getNearestEdge(Coordinates point) {
        return getEdges().stream().min(Comparator.comparingDouble(edge -> edge.calculateDistance(point))).get();
    }

    public Polygon getCounterClockwise() {
        if(isCounterClockwise()) return this;
        List reversedVertices = new ArrayList<>(vertices);
        Collections.reverse(reversedVertices);
        return new Polygon(reversedVertices);
    }

    public void replaceVertex(Coordinates oldVertex, Coordinates newVertex) {
        if(vertices.stream().noneMatch(oldVertex::equals)) throw new PolygonException(PolygonException.NO_SUCH_VERTEX);
        int index = vertices.indexOf(oldVertex);
        vertices.set(index, newVertex);
    }

    public void deleteVertex(Coordinates vertex) {
        if(vertices.stream().noneMatch(vertex::equals)) throw new PolygonException(PolygonException.NO_SUCH_VERTEX);
        vertices.remove(vertex);
    }

    public void insertVertex(Coordinates newVertex, Edge edge) {
        if(getCounterClockwise().getEdges().stream().noneMatch(edge::equals)) throw new PolygonException(PolygonException.NO_SUCH_EDGE);
        Coordinates firstEdgePoint = isCounterClockwise() ? edge.getPointA() : edge.getPointB();
        int newVertexIndex = vertices.indexOf(firstEdgePoint) + 1;
        vertices.add(newVertexIndex, newVertex);
    }

    public Coordinates getPredecessingVertex(Coordinates vertex) {
        if(vertices.stream().noneMatch(vertex::equals)) throw new PolygonException(PolygonException.NO_SUCH_VERTEX);
        int predecessorIndex = vertices.indexOf(vertex) - 1;
        if(predecessorIndex >=0){
            return vertices.get(predecessorIndex);
        }else {
            return vertices.get(vertices.size() - 1);
        }
    }

    public Coordinates getSuccessingVertex(Coordinates vertex) {
        if(vertices.stream().noneMatch(vertex::equals)) throw new PolygonException(PolygonException.NO_SUCH_VERTEX);
        int successorIndex = vertices.indexOf(vertex) + 1;
        if(successorIndex < vertices.size()) {
            return vertices.get(successorIndex);
        }else {
            return vertices.get(0);
        }
    }

    public int numberOfVertices() {
        return vertices.size();
    }

    public List<Coordinates> getVertices() {
        return new ArrayList<>(vertices);
    }
}
