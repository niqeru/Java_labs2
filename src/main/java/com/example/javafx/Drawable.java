package com.example.javafx;

import javafx.scene.canvas.GraphicsContext;
public interface Drawable {
    void draw(GraphicsContext gc);
    Drawable move(Vector vector);
    void writeToStream(FigureOutput out) throws java.io.IOException;
}
