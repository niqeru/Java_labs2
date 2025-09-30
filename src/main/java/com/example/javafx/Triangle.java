package com.example.javafx;

import java.io.IOException;
import java.util.Objects;
import javafx.scene.canvas.GraphicsContext;

public final class Triangle implements Shape {
    public static final int TYPE = 3;

    static {
        FigureRegistry.register(TYPE, Triangle::readFromStream);
    }

    private final Vector a;
    private final Vector b;
    private final Vector c;

    public Triangle(Vector a, Vector b, Vector c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    @Override
    public void draw(GraphicsContext gc) {
        double[] xPoints = {a.getX(), b.getX(), c.getX()};
        double[] yPoints = {a.getY(), b.getY(), c.getY()};

        gc.strokePolygon(xPoints, yPoints, 3);
    }

    @Override
    public Triangle move(Vector vector) {
        return new Triangle(a.plus(vector), b.plus(vector), c.plus(vector));
    }

    @Override
    public double area() {
        Vector ab = b.minus(a);
        Vector ac = c.minus(a);
        return ab.triangleArea(ac);
    }

    @Override
    public double perimeter() {
        double ab = b.minus(a).length();
        double bc = c.minus(b).length();
        double ca = a.minus(c).length();
        return ab + bc + ca;
    }

    @Override
    public Triangle scale(double factor) {
        Vector centroid = new Vector(
                (a.getX() + b.getX() + c.getX()) / 3,
                (a.getY() + b.getY() + c.getY()) / 3
        );
        return new Triangle(
                centroid.plus(a.minus(centroid).multiply(factor)),
                centroid.plus(b.minus(centroid).multiply(factor)),
                centroid.plus(c.minus(centroid).multiply(factor))
        );
    }

    @Override
    public void writeToStream(FigureOutput out) throws IOException {
        out.writeInt(TYPE);
        out.writeDouble(a.getX());
        out.writeDouble(a.getY());
        out.writeDouble(b.getX());
        out.writeDouble(b.getY());
        out.writeDouble(c.getX());
        out.writeDouble(c.getY());
    }

    public static Triangle readFromStream(FigureInput in) throws IOException {
        double ax = in.readDouble();
        double ay = in.readDouble();
        double bx = in.readDouble();
        double by = in.readDouble();
        double cx = in.readDouble();
        double cy = in.readDouble();
        return new Triangle(new Vector(ax, ay), new Vector(bx, by), new Vector(cx, cy));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Triangle triangle = (Triangle) o;
        return a.equals(triangle.a) &&
                b.equals(triangle.b) &&
                c.equals(triangle.c);
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b, c);
    }
}