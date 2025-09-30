package com.example.javafx;

import javafx.scene.canvas.GraphicsContext;

import java.io.IOException;
import java.util.Objects;

public final class Ellipse implements Shape {
    public static final int TYPE = 5;

    static {
        FigureRegistry.register(TYPE, Ellipse::readFromStream);
    }

    private final Vector center;
    private final double a; // большая полуось
    private final double b; // малая полуось
    private final double angleRadians; // угол поворота

    public Ellipse(Vector center, double a, double b, double angleRadians) {
        this.center = center;
        this.a = a;
        this.b = b;
        this.angleRadians = angleRadians;
    }

    public Ellipse(Vector f1, Vector f2, double a) {
        this.center = f1.plus(f2).multiply(0.5);
        this.a = a;
        double c = f1.minus(f2).length() / 2;
        this.b = Math.sqrt(a * a - c * c);
        this.angleRadians = f2.minus(f1).angleX();
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.save();
        gc.translate(center.getX(), center.getY());
        gc.rotate(Math.toDegrees(angleRadians));
        gc.strokeOval(-a, -b, 2 * a, 2 * b);
        gc.restore();
    }

    @Override
    public Ellipse move(Vector v) {
        return new Ellipse(center.plus(v), a, b, angleRadians);
    }

    @Override
    public double area() {
        return Math.PI * a * b;
    }

    @Override
    public double perimeter() {
        // Приближённая формула Раману
        return Math.PI * (3 * (a + b) - Math.sqrt((3 * a + b) * (a + 3 * b)));
    }

    @Override
    public Ellipse scale(double factor) {
        return new Ellipse(center, a * factor, b * factor, angleRadians);
    }

    @Override
    public void writeToStream(FigureOutput out) throws IOException {
        out.writeInt(TYPE);
        out.writeDouble(center.getX());
        out.writeDouble(center.getY());
        out.writeDouble(a);
        out.writeDouble(b);
        out.writeDouble(angleRadians);
    }

    public static Ellipse readFromStream(FigureInput in) throws IOException {
        double x = in.readDouble();
        double y = in.readDouble();
        double a = in.readDouble();
        double b = in.readDouble();
        double angle = in.readDouble();
        return new Ellipse(new Vector(x, y), a, b, angle);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Ellipse ellipse = (Ellipse) obj;
        return Double.compare(ellipse.a, a) == 0 &&
                Double.compare(ellipse.b, b) == 0 &&
                Double.compare(ellipse.angleRadians, angleRadians) == 0 &&
                center.equals(ellipse.center);
    }

    @Override
    public int hashCode() {
        return Objects.hash(center, a, b, angleRadians);
    }
}