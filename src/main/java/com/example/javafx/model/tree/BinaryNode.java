package com.example.javafx.model.tree;

public record BinaryNode(ExpressionNode left, ExpressionNode right, String operator) implements ExpressionNode {
    @Override
    public double evaluate() {
        double a = left.evaluate();
        double b = right.evaluate();
        return switch (operator) {
            case "+" -> a + b;
            case "-" -> a - b;
            case "*" -> a * b;
            case ":" -> {
                if (b == 0) throw new ArithmeticException("Division by zero");
                yield a / b;
            }
            case "^" -> Math.pow(a, b);
            default -> throw new UnsupportedOperationException("Unknown op: " + operator);
        };
    }

    @Override
    public String format() {
        return "(" + left.format() + " " + operator + " " + right.format() + ")";
    }
}