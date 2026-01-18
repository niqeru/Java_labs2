package com.example.calceng.model;

import java.util.*;

public class ShuntingYardConverter {
    private final Map<String, Integer> priority = Map.of(
            "+", 1, "-", 1,
            "*", 2, ":", 2,
            "^", 3, "√", 4,
            "u-" , 5);

    public List<String> convert(List<String> tokens) {
        List<String> output = new ArrayList<>();
        Stack<String> stack = new Stack<>();
        String lastToken = null;

        for (String t : tokens) {
            if (TokenUtils.isNumber(t)) {
                output.add(t);
            } else if (t.equals("(")) {
                stack.push(t);
            } else if (t.equals(")")) {
                while (!stack.isEmpty() && !stack.peek().equals("(")) {
                    output.add(stack.pop());
                }
                if (!stack.isEmpty()) stack.pop();
            } else {
                String currentOp = t;
                if (t.equals("-") && (lastToken == null || lastToken.equals("(") || "+-*^:√".contains(lastToken))) {
                    currentOp = "u-";
                }

                int currentPrio = priority.getOrDefault(currentOp, 0);

                while (!stack.isEmpty() && !stack.peek().equals("(")) {
                    String topOp = stack.peek();
                    int stackPrio = priority.getOrDefault(topOp, 0);

                    // Логика ассоциативности:
                    // Оператор ^ (priority 3) правоассоциативен.
                    // Если на стеке ^ и пришел ^, мы НЕ выталкиваем (ждем правую часть).
                    if (currentOp.equals("^") && topOp.equals("^")) {
                        break;
                    }

                    // Унарные операторы (priority >= 4) тоже обычно правоассоциативны
                    if (currentPrio >= 4 && stackPrio >= 4) {
                        break;
                    }

                    if (stackPrio >= currentPrio) {
                        output.add(stack.pop());
                    } else {
                        break;
                    }
                }
                stack.push(currentOp);
            }
            lastToken = t;
        }
        while (!stack.isEmpty()) output.add(stack.pop());
        return output;
    }
}
