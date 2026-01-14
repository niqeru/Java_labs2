package com.example.javafx.model;

import java.util.ArrayList;
import java.util.List;

public class ExpressionTokenizer {
    public List<String> tokenize(String expression) {
        List<String> tokens = new ArrayList<>();
        StringBuilder numberBuffer = new StringBuilder();

        expression = expression.replace(" ", "");

        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);

            if (Character.isDigit(c) || c == '.') {
                numberBuffer.append(c);
            } else {
                if (!numberBuffer.isEmpty()) {
                    tokens.add(numberBuffer.toString());
                    numberBuffer.setLength(0);
                }

                if ("+-*:^√()".indexOf(c) != -1) {
                    tokens.add(String.valueOf(c));
                }
            }
        }

        if (!numberBuffer.isEmpty()) {
            tokens.add(numberBuffer.toString());
        }

        return tokens;
    }
}