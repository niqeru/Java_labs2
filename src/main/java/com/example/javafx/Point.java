package com.example.javafx;

import java.io.IOException;
import javafx.scene.canvas.GraphicsContext;

public final class Point implements Drawable {
    public static final int TYPE = 6;

    static {
        FigureRegistry.register(TYPE, Point::readFromStream);
    }

    private final Vector position;

    public Point(Vector position) {
        this.position = position;
    }

    public Point(double x, double y) {
        this(new Vector(x, y));
    }

    public double getX() { return position.getX(); }
    public double getY() { return position.getY(); }

    @Override
    public void draw(GraphicsContext gc) {
        double radius = 5.0;
        gc.fillOval(position.getX() - radius, position.getY() - radius,
                radius * 2, radius * 2);
    }

    @Override
    public Point move(Vector vector) {
        return new Point(position.plus(vector));
    }

    @Override
    public void writeToStream(FigureOutput out) throws IOException {
        out.writeInt(TYPE);
        out.writeDouble(position.getX());
        out.writeDouble(position.getY());
    }

    public static Point readFromStream(FigureInput in) throws IOException {
        double x = in.readDouble();
        double y = in.readDouble();
        return new Point(new Vector(x, y));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return position.equals(point.position);
    }

    @Override
    public int hashCode() {
        return position.hashCode();
    }
}