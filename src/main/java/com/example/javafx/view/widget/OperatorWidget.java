package com.example.javafx.view.widget;

public class OperatorWidget extends BaseKeyboard {
    public OperatorWidget() {
        String[][] layout = {
                {"CE", "C"},
                {"+", "-"},
                {"*", ":"},
                {"^", "√"},
                {"(", ")"}
        };

        for (int r = 0; r < layout.length; r++) {
            for (int c = 0; c < layout[r].length; c++) {
                String text = layout[r][c];
                String color = switch (text) {
                    case "C" -> "#ffe3e0";
                    case "CE" -> "#fff4e0";
                    default -> "#dee2e6";
                };
                this.add(createButton(text, color, 65), c, r);
            }
        }
    }
}