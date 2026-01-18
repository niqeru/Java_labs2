package com.example.calceng.model.tree;

public record UnaryNode(ExpressionNode child, String function) implements ExpressionNode {
    @Override
    public double evaluate() {
        double val = child.evaluate();
        return switch (function) {
            case "√" -> {
                if (val < 0) throw new ArithmeticException("Negative root");
                yield Math.sqrt(val);
            }
            case "-" -> -val;
            default -> val;
        };
    }

    @Override
    public String format() {
        return function + "(" + child.format() + ")";
    }
}
