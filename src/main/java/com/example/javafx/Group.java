package com.example.javafx;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.scene.canvas.GraphicsContext;

public class Group implements Shape {
    public static final int TYPE = 100;

    static {
        FigureRegistry.register(TYPE, Group::readFromStream);
    }

    private final List<Shape> shapes = new ArrayList<>();

    public void add(Shape shape) {
        shapes.add(shape);
    }

    public void remove(Shape shape) {
        shapes.remove(shape);
    }

    public Shape get(int index) {
        return shapes.get(index);
    }

    public void remove(int index) {
        shapes.remove(index);
    }

    public List<Shape> getShapes() {
        return Collections.unmodifiableList(shapes);
    }

    @Override
    public void draw(GraphicsContext gc) {
        for (Shape shape : shapes) {
            shape.draw(gc);
        }
    }

    @Override
    public Group move(Vector vector) {
        Group moved = new Group();
        for (Shape shape : shapes) {
            moved.add((Shape) shape.move(vector));
        }
        return moved;
    }

    @Override
    public double area() {
        double sum = 0;
        for (Shape shape : shapes) {
            sum += shape.area();
        }
        return sum;
    }

    @Override
    public double perimeter() {
        double sum = 0;
        for (Shape shape : shapes) {
            sum += shape.perimeter();
        }
        return sum;
    }

    @Override
    public Group scale(double factor) {
        Group scaled = new Group();
        for (Shape shape : shapes) {
            scaled.add(shape.scale(factor));
        }
        return scaled;
    }

    @Override
    public void writeToStream(FigureOutput out) throws IOException {
        out.writeInt(TYPE);
        out.writeInt(shapes.size());
        for (Shape shape : shapes) {
            shape.writeToStream(out);
        }
    }

    public static Group readFromStream(FigureInput in) throws IOException {
        int n = in.readInt();
        Group group = new Group();
        for (int i = 0; i < n; i++) {
            Drawable d = in.readDrawable();
            if (d instanceof Shape) {
                group.add((Shape) d);
            }
        }
        return group;
    }

    public boolean contentEquals(Group other) {
        if (this.shapes.size() != other.shapes.size()) return false;
        for (int i = 0; i < shapes.size(); i++) {
            Shape s1 = shapes.get(i);
            Shape s2 = other.shapes.get(i);
            if (s1 instanceof Group && s2 instanceof Group) {
                if (!((Group) s1).contentEquals((Group) s2)) return false;
            } else {
                if (!s1.equals(s2)) return false;
            }
        }
        return true;
    }
}