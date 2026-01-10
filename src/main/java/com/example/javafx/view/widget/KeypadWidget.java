package com.example.javafx.view.widget;

public class KeypadWidget extends BaseKeyboard {
    public KeypadWidget() {
        String[][] layout = {
                {"1", "2", "3"},
                {"4", "5", "6"},
                {"7", "8", "9"},
                {".", "0", "+/-"},
                {"="}
        };

        for (int r = 0; r < layout.length; r++) {
            for (int c = 0; c < layout[r].length; c++) {
                String text = layout[r][c];
                boolean isFullWidth = text.equals("=");
                String color = isFullWidth ? "#e0f8ff" : "#e9ecef";

                this.add(createButton(text, color, isFullWidth ? 211 : 65), c, r, isFullWidth ? 3 : 1, 1);
            }
        }
    }
}