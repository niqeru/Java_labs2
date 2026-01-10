package com.example.javafx.model.tree;

public record NumberNode(double value) implements ExpressionNode {
    @Override
    public double evaluate() {
        return value;
    }

    @Override
    public String format() {
        if (value == (long) value) return String.format("%d", (long) value);
        return String.valueOf(value);
    }
}
