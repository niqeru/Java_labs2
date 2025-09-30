package com.example.javafx;

import java.util.Objects;

public final class Vector {
    private final double x;
    private final double y;

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }


    public double length() {
        return Math.sqrt(x * x + y * y);
    }


    public Vector multiply(double scalar) {
        return new Vector(x * scalar, y * scalar);
    }

    public double VnaV(Vector other) {
        return x * other.x + y * other.y;
    }


    public Vector plus(Vector other) {
        return new Vector(x + other.x, y + other.y);
    }

    public Vector minus(Vector other) {
        return new Vector(x - other.x, y - other.y);
    }

    public double angleTo(Vector other) {
        double top = VnaV(other);
        double down = length() * other.length();
        return Math.acos(top / down);
    }

    public double angleX() {
        return angleTo(new Vector(1.0, 0.0));
    }

    public double angleY() {
        return angleTo(new Vector(0.0, 1.0));
    }


    public double triangleArea(Vector other) {
        return Math.abs(x * other.y - y * other.x) / 2.0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector vector2D = (Vector) o;
        return Double.compare(vector2D.x, x) == 0 &&
                Double.compare(vector2D.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

}