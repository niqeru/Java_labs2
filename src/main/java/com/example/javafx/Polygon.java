package com.example.javafx;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.canvas.GraphicsContext;
import java.util.Objects;

public final class Polygon implements Shape {
    public static final int TYPE = 4;

    static {
        FigureRegistry.register(TYPE, Polygon::readFromStream);
    }

    private final List<Vector> vertices;

    public Polygon(List<Vector> vertices) {
        this.vertices = new ArrayList<>(vertices);
    }


    @Override
    public void draw(GraphicsContext gc) {
        if (vertices.size() < 2) return;
        double[] xPoints = new double[vertices.size()];
        double[] yPoints = new double[vertices.size()];

        for (int i = 0; i < vertices.size(); i++) {
            Vector vertex = vertices.get(i);
            xPoints[i] = vertex.getX();
            yPoints[i] = vertex.getY();
        }

        // Рисуем заполненный полигон
        gc.fillPolygon(xPoints, yPoints, vertices.size());

        // Рисуем контур полигона
        gc.strokePolygon(xPoints, yPoints, vertices.size());
    }

    @Override
    public Polygon move(Vector vector) {
        List<Vector> newVertices = new ArrayList<>();
        for (Vector vertex : vertices) {
            newVertices.add(vertex.plus(vector));
        }
        return new Polygon(newVertices);
    }

    @Override
    public double area() {
        double sum = 0;
        int n = vertices.size();
        for (int i = 0; i < n; i++) {
            Vector v1 = vertices.get(i);
            Vector v2 = vertices.get((i + 1) % n);
            sum += v1.getX() * v2.getY() - v1.getY() * v2.getX();
        }
        return Math.abs(sum) / 2;
    }

    @Override
    public double perimeter() {
        double perimeter = 0.0;
        int n = vertices.size();
        for (int i = 0; i < n; i++) {
            Vector v1 = vertices.get(i);
            Vector v2 = vertices.get((i + 1) % n);
            perimeter += v2.minus(v1).length();
        }
        return perimeter;
    }

    @Override
    public Polygon scale(double factor) {
        Vector center = calculateCentroid();
        List<Vector> newVertices = new ArrayList<>();
        for (Vector vertex : vertices) {
            newVertices.add(center.plus(vertex.minus(center).multiply(factor)));
        }
        return new Polygon(newVertices);
    }

    private Vector calculateCentroid() {
        double xSum = 0, ySum = 0;
        int n = vertices.size();
        for (Vector v : vertices) {
            xSum += v.getX();
            ySum += v.getY();
        }
        return new Vector(xSum / n, ySum / n);
    }

    @Override
    public void writeToStream(FigureOutput out) throws IOException {
        out.writeInt(TYPE);
        out.writeInt(vertices.size());
        for (Vector v : vertices) {
            out.writeDouble(v.getX());
            out.writeDouble(v.getY());
        }
    }

    public static Polygon readFromStream(FigureInput in) throws IOException {
        int n = in.readInt();
        List<Vector> vertices = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            double x = in.readDouble();
            double y = in.readDouble();
            vertices.add(new Vector(x, y));
        }
        return new Polygon(vertices);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Polygon polygon = (Polygon) o;
        return vertices.equals(polygon.vertices);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vertices);
    }
}