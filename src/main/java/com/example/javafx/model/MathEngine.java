package com.example.javafx.model;

import com.example.javafx.model.tree.BinaryNode;
import com.example.javafx.model.tree.ExpressionNode;
import com.example.javafx.model.tree.NumberNode;
import com.example.javafx.model.tree.UnaryNode;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class MathEngine {
    private final ExpressionTokenizer tokenizer = new ExpressionTokenizer();
    private final ShuntingYardConverter converter = new ShuntingYardConverter();

    public double calculate(String expression) {
        if (expression.isEmpty()) return 0;
        List<String> tokens = tokenizer.tokenize(expression);
        ExpressionNode root = buildTree(tokens);
        return root.evaluate();
    }

    private ExpressionNode buildTree(List<String> tokens) {
        List<String> rpn = converter.convert(tokens);
        Stack<ExpressionNode> stack = new Stack<>();

        for (String t : rpn) {
            if (t.matches("-?\\d+(\\.\\d+)?")) {
                stack.push(new NumberNode(Double.parseDouble(t)));
            } else if (t.equals("√") || t.equals("u-")) {
                if (!stack.isEmpty()) {
                    stack.push(new UnaryNode(stack.pop(), t.equals("u-") ? "-" : "√"));
                }
            } else if (t.equals("-")) {
                if (stack.size() >= 2) {
                    ExpressionNode right = stack.pop();
                    ExpressionNode left = stack.pop();
                    stack.push(new BinaryNode(left, right, "-"));
                }
            } else {
                if (stack.size() >= 2) {
                    ExpressionNode right = stack.pop();
                    ExpressionNode left = stack.pop();
                    stack.push(new BinaryNode(left, right, t));
                }
            }
        }
        return stack.isEmpty() ? new NumberNode(0) : stack.pop();
    }

    public String formatResult(double d) {
        if (Double.isInfinite(d) || Double.isNaN(d)) return "Error";
        if (d == (long) d) return String.format("%d", (long) d);
        return BigDecimal.valueOf(d).setScale(8, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString();
    }
}