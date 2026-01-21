package com.example.calceng.model;

import java.util.ArrayList;
import java.util.List;

public class ExpressionManager {
    private final List<String> tokens = new ArrayList<>(List.of("0"));

    public void processInput(String text) {
        String last = tokens.get(tokens.size() - 1);
        if (!TokenUtils.isNumber(last) && !TokenUtils.isOperator(last) &&
                !last.equals("(") && !last.equals(")") &&
                !last.endsWith(".") && !text.equals("CE")) {
            reset();
        }
        switch (text) {
            case "C" -> reset();
            case "CE" -> handleBackSpace();
            case "+/-" -> new SignHandler(tokens).toggle();
            case "." -> handleDot();
            case "(" -> handleOpeningBracket();
            case ")" -> handleClosingBracket();
            default -> {
                if (TokenUtils.isOperator(text)) handleOperator(text);
                else if (TokenUtils.isDigit(text)) handleDigit(text);
            }
        }
    }

    private void handleDigit(String digit) {
        String last = tokens.get(tokens.size() - 1);
        if (last.equals("0") || last.equals("Error")) {
            tokens.set(tokens.size() - 1, digit);
        }
        if (last.equals("0")) {
            tokens.set(tokens.size() - 1, digit);
        } else if (TokenUtils.isNumber(last) || (last.contains(".") && !TokenUtils.isOperator(last))) {
            tokens.set(tokens.size() - 1, last + digit);
        } else {
            if (last.equals(")")) tokens.add("*");
            tokens.add(digit);
        }
    }

    private void handleOperator(String op) {
        String last = tokens.get(tokens.size() - 1);
        if (last.endsWith(".")) {
            last = last.substring(0, last.length() - 1);
            tokens.set(tokens.size() - 1, last);
        }
        if (op.equals("√")) {
            if (last.equals("0") && tokens.size() == 1) tokens.set(0, "√");
            else {
                if (TokenUtils.isNumber(last) || last.equals(")")) tokens.add("*");
                tokens.add("√");
            }
            tokens.add("(");
            return;
        }
        if (TokenUtils.isOperator(last) && !last.equals(")") && !last.equals("(")) {
            tokens.set(tokens.size() - 1, op);
        } else {
            tokens.add(op);
        }
    }

    private void handleOpeningBracket() {
        String last = tokens.get(tokens.size() - 1);
        if (tokens.size() == 1 && last.equals("0")) tokens.set(0, "(");
        else {
            if (TokenUtils.isNumber(last) || last.equals(")")) tokens.add("*");
            tokens.add("(");
        }
    }

    private void handleClosingBracket() {
        long open = tokens.stream().filter(t -> t.equals("(")).count();
        long close = tokens.stream().filter(t -> t.equals(")")).count();
        if (open > close && !TokenUtils.isOperator(tokens.get(tokens.size() - 1))) {
            tokens.add(")");
        }
    }

    private void handleDot() {
        String last = tokens.get(tokens.size() - 1);
        if (TokenUtils.isNumber(last) && !last.contains(".")) {
            tokens.set(tokens.size() - 1, last + ".");
        } else if (TokenUtils.isOperator(last) || last.equals("(")) {
            tokens.add("0.");
        }
    }

    private void handleBackSpace() {
        int lastIdx = tokens.size() - 1;
        String last = tokens.get(lastIdx);
        if (last.length() > 1) tokens.set(lastIdx, last.substring(0, last.length() - 1));
        else tokens.remove(lastIdx);
        if (tokens.isEmpty()) tokens.add("0");
    }

    public boolean isLastCharOperator() {
        if (tokens.isEmpty()) return false;
        String last = tokens.get(tokens.size() - 1);
        return TokenUtils.isOperator(last) && !last.equals(")");
    }

    public void reset() { tokens.clear(); tokens.add("0"); }
    public String getExpression() { return String.join("", tokens); }
    public void setExpression(String text) {
        tokens.clear();
        if (text == null || text.isEmpty()) {
            tokens.add("0");
        } else {
            tokens.add(text);
        }
    }
}