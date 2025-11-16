package geometry.shape;

import geometry.Coordinates;

public class Edge extends Line {


    public Edge(Coordinates from, Coordinates to) {
        super(from, to);
    }

    //To function properly, the edge has to be oriented counterclockwise as a part of it's shape
    public boolean hasInside(Coordinates point) {
        return (getPointB().getY() - getPointA().getY()) * point.getX() - (getPointB().getX() - getPointA().getX()) * point.getY() + getPointB().getX() * getPointA().getY() - getPointB().getY() * getPointA().getX() > 0;
    }

    public Edge getOrintedY() {
        if(getPointA().getY() < getPointB().getY()) {
            return this;
        }else {
            return new Edge(getPointB(), getPointA());
        }
    }

    public int getIntersection(int y) {
        double dx = getPointB().getX() - getPointA().getX();
        double dy = getPointB().getY() - getPointA().getY();
        double k = dx / dy;
        double q = getPointA().getX() - (k * getPointA().getY());
        return (int)((k * y) + q);
    }

    public boolean isIntersection(int y) {
        return (y >= getPointA().getY() && y < getPointB().getY());
    }

    public boolean hasVertex(Coordinates vertex) {
        return vertex.equals(getPointA()) || vertex.equals(getPointB());
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Edge)) return false;
        Edge edge = (Edge) obj;
        return edge.getPointA().equals(getPointA()) && edge.getPointB().equals(getPointB());    //equality requires the same vector orientation
    }

}
