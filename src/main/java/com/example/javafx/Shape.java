package com.example.javafx;



public interface Shape extends Drawable {
    double area();
    double perimeter();
    Shape scale(double factor);
}