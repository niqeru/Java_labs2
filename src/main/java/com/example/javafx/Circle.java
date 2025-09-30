package com.example.javafx;

import javafx.scene.canvas.GraphicsContext;

import java.io.IOException;
import java.util.Objects;

public final class Circle implements Shape {
    public static final int TYPE = 1;

    static {
        FigureRegistry.register(TYPE, Circle::readFromStream);
    }

    private final Vector center;
    private final double radius;

    public Circle(Vector center, double radius) {
        this.center = center;
        this.radius = radius;
    }


    @Override
    public void draw(GraphicsContext gc) {
        double x = center.getX() - radius;
        double y = center.getY() - radius;
        double diameter = 2 * radius;
        gc.strokeOval(x, y, diameter, diameter);
    }


    @Override
    public Circle move(Vector vector) {
        return new Circle(center.plus(vector), radius);
    }

    @Override
    public double area() {
        return Math.PI * radius * radius;
    }

    @Override
    public double perimeter() {
        return 2 * Math.PI * radius;
    }

    @Override
    public Circle scale(double factor) {
        return new Circle(center, radius * factor);
    }

    @Override
    public void writeToStream(FigureOutput out) throws IOException {
        out.writeInt(TYPE);
        out.writeDouble(center.getX());
        out.writeDouble(center.getY());
        out.writeDouble(radius);
    }

    public static Circle readFromStream(FigureInput in) throws IOException {
        double x = in.readDouble();
        double y = in.readDouble();
        double r = in.readDouble();
        return new Circle(new Vector(x, y), r);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Circle circle = (Circle) o;
        return Double.compare(circle.radius, radius) == 0 &&
                center.equals(circle.center);
    }

    @Override
    public int hashCode() {
        return Objects.hash(center, radius);
    }
}