package com.example.javafx.model;

import java.util.List;

public class SignHandler {
    private final List<String> tokens;

    public SignHandler(List<String> tokens) { this.tokens = tokens; }

    public void toggle() {
        if (tokens.isEmpty()) return;
        int lastIdx = tokens.size() - 1;
        String last = tokens.get(lastIdx);

        if (TokenUtils.isNumber(last)) {
            handleNumberToggle(lastIdx);
        } else if (last.equals(")")) {
            int openIdx = findOpenBracket(lastIdx);
            if (openIdx != -1) {
                int headIdx = (openIdx > 0 && tokens.get(openIdx - 1).equals("√")) ? openIdx - 1 : openIdx;
                applyToggleAt(headIdx);
            }
        }
    }

    private void applyToggleAt(int idx) {
        if (idx > 0 && tokens.get(idx - 1).equals("-")) {
            tokens.remove(idx - 1);
        } else if (idx > 0 && tokens.get(idx - 1).equals("+")) {
            tokens.set(idx - 1, "-");
        } else {
            tokens.add(idx, "-");
        }
    }

    private void handleNumberToggle(int idx) {
        String val = tokens.get(idx);
        if (idx > 0 && tokens.get(idx - 1).equals("-")) {
            // Если перед числом минус, проверяем бинарный он или унарный
            if (idx > 1 && (TokenUtils.isNumber(tokens.get(idx - 2)) || tokens.get(idx - 2).equals(")"))) {
                tokens.set(idx - 1, "+");
            } else {
                tokens.remove(idx - 1);
            }
        } else if (idx > 0 && tokens.get(idx - 1).equals("+")) {
            tokens.set(idx - 1, "-");
        } else {
            tokens.set(idx, val.startsWith("-") ? val.substring(1) : "-" + val);
        }
    }

    private int findOpenBracket(int start) {
        int balance = 0;
        for (int i = start; i >= 0; i--) {
            if (tokens.get(i).equals(")")) balance++;
            else if (tokens.get(i).equals("(")) balance--;
            if (balance == 0) return i;
        }
        return -1;
    }
}