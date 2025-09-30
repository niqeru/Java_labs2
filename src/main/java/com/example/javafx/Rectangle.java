package com.example.javafx;

import java.io.IOException;
import java.util.Objects;
import javafx.scene.canvas.GraphicsContext;


public final class Rectangle implements Shape {
    public static final int TYPE = 2;

    static {
        FigureRegistry.register(TYPE, Rectangle::readFromStream);
    }

    private final Vector center;
    private final double width;
    private final double height;
    private final double angleRadians; // угол поворота относительно оси X

    public Rectangle(Vector center, double width, double height, double angleRadians) {
        this.center = center;
        this.width = width;
        this.height = height;
        this.angleRadians = angleRadians;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.save();
        gc.translate(center.getX(), center.getY());
        gc.rotate(Math.toDegrees(angleRadians));
        double x = -width / 2;
        double y = -height / 2;
        gc.strokeRect(x, y, width, height);
        gc.restore();
    }

    @Override
    public Rectangle move(Vector vector) {
        return new Rectangle(center.plus(vector), width, height, angleRadians);
    }

    @Override
    public double area() {
        return width * height;
    }

    @Override
    public double perimeter() {
        return 2 * (width + height);
    }

    @Override
    public Rectangle scale(double factor) {
        return new Rectangle(center, width * factor, height * factor, angleRadians);
    }

    public Rectangle rotate(double deltaAngle) {
        return new Rectangle(center, width, height, angleRadians + deltaAngle);
    }

    public Vector[] getVertices() {
        double hw = width / 2;
        double hh = height / 2;
        double cos = Math.cos(angleRadians);
        double sin = Math.sin(angleRadians);

        Vector[] vertices = new Vector[4];
        vertices[0] = center.plus(new Vector(-hw * cos + hh * sin, -hw * sin - hh * cos)); // top-left
        vertices[1] = center.plus(new Vector(hw * cos + hh * sin, hw * sin - hh * cos));   // top-right
        vertices[2] = center.plus(new Vector(hw * cos - hh * sin, hw * sin + hh * cos));   // bottom-right
        vertices[3] = center.plus(new Vector(-hw * cos - hh * sin, -hw * sin + hh * cos)); // bottom-left
        return vertices;
    }

    public Vector getCenter() {
        return center;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public double getAngleRadians() {
        return angleRadians;
    }

    @Override
    public void writeToStream(FigureOutput out) throws IOException {
        out.writeInt(TYPE);
        out.writeDouble(center.getX());
        out.writeDouble(center.getY());
        out.writeDouble(width);
        out.writeDouble(height);
        out.writeDouble(angleRadians);
    }

    public static Rectangle readFromStream(FigureInput in) throws IOException {
        double x = in.readDouble();
        double y = in.readDouble();
        double width = in.readDouble();
        double height = in.readDouble();
        double angle = in.readDouble();
        return new Rectangle(new Vector(x, y), width, height, angle);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rectangle rectangle = (Rectangle) o;
        return Double.compare(rectangle.width, width) == 0 &&
                Double.compare(rectangle.height, height) == 0 &&
                Double.compare(rectangle.angleRadians, angleRadians) == 0 &&
                center.equals(rectangle.center);
    }

    @Override
    public int hashCode() {
        return Objects.hash(center, width, height, angleRadians);
    }
}