package geometry.shape;

import geometry.Coordinates;

import java.util.ArrayList;
import java.util.List;

public class Line implements Shape {

    private Coordinates pointA;

    private Coordinates pointB;

    public Line(Coordinates from, Coordinates to) {
        pointA = from;
        pointB = to;
    }

    @Override
    public List<Coordinates> getShape() {
        List<Coordinates> line = new ArrayList<>();
        double dx = pointB.getX() - pointA.getX();
        double dy = pointB.getY() - pointA.getY();

        boolean iteratingX = Math.abs(dx) > Math.abs(dy);

        boolean swapValues = (iteratingX && pointA.getX() > pointB.getX()) || (!iteratingX && pointA.getY() > pointB.getY());
        if (swapValues) {
            dx = -dx;
            dy = -dy;
            pointA = pointB;
        }

        double x = pointA.getX();
        double y = pointA.getY();
        double incrementX = iteratingX ? 1 : dx / dy;
        double incrementY = iteratingX ? dy / dx : 1;
        int iterateTo = iteratingX ? (int) dx : (int) dy;

        for (int i = 0; i <= iterateTo; i++) {
            line.add(new Coordinates(x, y));
            x = x + incrementX;
            y = y + incrementY;
        }
        return line;
    }

    public double calculateDistance(Coordinates point) {
        return Math.abs(((pointB.getY() - pointA.getY()) * point.getX() - (pointB.getX() - pointA.getX()) * point.getY() + pointB.getX() * pointA.getY() - pointB.getY() * pointA.getX()) /
                Math.sqrt((pointB.getY() - pointA.getY()) * (pointB.getY() - pointA.getY()) + (pointB.getX() - pointA.getX()) * (pointB.getX() - pointA.getX())));
    }

    public Coordinates calculateIntersection(Line line) {
        Coordinates coord1 = line.pointA;
        Coordinates coord2 = line.pointB;
        double x = ((coord1.getX() * coord2.getY() - coord1.getY() * coord2.getX()) * (pointA.getX() - pointB.getX()) - (pointA.getX() * pointB.getY() - pointA.getY() * pointB.getX()) * (coord1.getX() - coord2.getX()))
                / ((coord1.getX() - coord2.getX()) * (pointA.getY() - pointB.getY()) - (pointA.getX() - pointB.getX()) * (coord1.getY() - coord2.getY()));
        if(Double.isInfinite(x) || Double.isNaN(x)) return null;  //lines are equal or parallel

        double y = ((coord1.getX() * coord2.getY() - coord1.getY() * coord2.getX()) * (pointA.getY() - pointB.getY()) - (pointA.getX() * pointB.getY() - pointA.getY() * pointB.getX()) * (coord1.getY() - coord2.getY()))
                / ((coord1.getX() - coord2.getX()) * (pointA.getY() - pointB.getY()) - (pointA.getX() - pointB.getX()) * (coord1.getY() - coord2.getY()));
        return new Coordinates(x, y);
    }

    public boolean isVertical() {
        return pointA.yEquals(pointB);
    }

    public Coordinates getPointA() {
        return pointA;
    }

    public Coordinates getPointB() {
        return pointB;
    }
}
