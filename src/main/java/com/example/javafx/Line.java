package com.example.javafx;

import java.io.IOException;
import java.util.Objects;
import javafx.scene.canvas.GraphicsContext;

public final class Line implements Drawable {
    public static final int TYPE = 7;

    static {
        FigureRegistry.register(TYPE, Line::readFromStream);
    }

    private final Vector start;
    private final Vector end;

    public Line(Vector start, Vector end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.strokeLine(start.getX(), start.getY(), end.getX(), end.getY());
    }


    @Override
    public Line move(Vector vector) {
        return new Line(start.plus(vector), end.plus(vector));
    }

    public double length() {
        return end.minus(start).length();
    }

    public Line scale(double factor) {
        Vector center = new Vector(
                (start.getX() + end.getX()) / 2,
                (start.getY() + end.getY()) / 2
        );
        Vector newStart = center.plus(start.minus(center).multiply(factor));
        Vector newEnd = center.plus(end.minus(center).multiply(factor));
        return new Line(newStart, newEnd);
    }

    @Override
    public void writeToStream(FigureOutput out) throws IOException {
        out.writeInt(TYPE);
        out.writeDouble(start.getX());
        out.writeDouble(start.getY());
        out.writeDouble(end.getX());
        out.writeDouble(end.getY());
    }

    public static Line readFromStream(FigureInput in) throws IOException {
        double x1 = in.readDouble();
        double y1 = in.readDouble();
        double x2 = in.readDouble();
        double y2 = in.readDouble();
        return new Line(new Vector(x1, y1), new Vector(x2, y2));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Line that = (Line) o;
        return start.equals(that.start) && end.equals(that.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }
}
