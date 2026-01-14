package com.example.javafx.view;

import com.example.javafx.view.widget.DisplayWidget;
import com.example.javafx.view.widget.KeypadWidget;
import com.example.javafx.view.widget.OperatorWidget;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import java.util.function.Consumer;

public class CalculatorView extends VBox {
    private final DisplayWidget display = new DisplayWidget();
    private final KeypadWidget keypad = new KeypadWidget();
    private final OperatorWidget operators = new OperatorWidget();

    public CalculatorView() {
        this.setSpacing(15);
        this.setPadding(new Insets(20));
        this.setAlignment(Pos.CENTER);
        this.setStyle("-fx-background-color: #f8f9fa;");

        HBox keyboardWrapper = new HBox(15, keypad, operators);
        keyboardWrapper.setAlignment(Pos.CENTER);

        this.getChildren().addAll(display, keyboardWrapper);
    }

    public void setOnAction(Consumer<String> handler) {
        keypad.setOnAction(handler);
        operators.setOnAction(handler);
        setupKeyboardListener(handler);
    }

    public void setupKeyboardListener(Consumer<String> handler) {
        this.setFocusTraversable(true);
        this.setOnKeyPressed(event -> {
            String text = event.getText();

            if (event.getCode().isDigitKey()) {
                handler.accept(text);
            }
            else if ("+-*^()".contains(text) && !text.isEmpty()) {
                handler.accept(text);
            }
            else if (event.getCode() == javafx.scene.input.KeyCode.ENTER) {
                handler.accept("=");
            } else if (event.getCode() == javafx.scene.input.KeyCode.BACK_SPACE) {
                handler.accept("CE");
            } else if (event.getCode() == javafx.scene.input.KeyCode.ESCAPE) {
                handler.accept("C");
            } else if (text.equals("/")) {
                handler.accept(":");
            } else if (text.equals(".")) {
                handler.accept(".");
            }
        });
    }

    public void setExpression(String text) { display.setExpression(text); }
    public void setResult(String text) { display.setResult(text); }
}

//package com.example.javafx.view;
//
//import javafx.geometry.Insets;
//import javafx.geometry.Pos;
//import javafx.scene.control.Button;
//import javafx.scene.control.Label;
//import javafx.scene.layout.*;
//import java.util.function.Consumer;
//
//public class CalculatorView extends VBox {
//    private final Label expressionLabel = new Label("");
//    private final Label resultLabel = new Label("");
//    private final GridPane numGrid = new GridPane();
//    private final GridPane opGrid = new GridPane();
//    private Consumer<String> onActionHandler;
//
//    public CalculatorView() {
//        this.setSpacing(15);
//        this.setPadding(new Insets(20));
//        this.setAlignment(Pos.CENTER);
//        this.setStyle("-fx-background-color: #f8f9fa;");
//
//        setupDisplay();
//        setupKeyboards();
//
//        HBox keyboardWrapper = new HBox(15, numGrid, opGrid);
//        keyboardWrapper.setAlignment(Pos.CENTER);
//        this.getChildren().addAll(createDisplayBox(), keyboardWrapper);
//    }
//
//    private void setupDisplay() {
//        expressionLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #6c757d;");
//        resultLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #212529;");
//    }
//
//    private VBox createDisplayBox() {
//        VBox box = new VBox(5, expressionLabel, resultLabel);
//        box.setAlignment(Pos.CENTER_RIGHT);
//        box.setStyle("-fx-background-color: white; -fx-border-color: #dee2e6; -fx-border-radius: 5; -fx-padding: 15;");
//        box.setPrefHeight(100);
//        return box;
//    }
//
//    private void setupKeyboards() {
//        numGrid.setHgap(8); numGrid.setVgap(8);
//        opGrid.setHgap(8); opGrid.setVgap(8);
//
//        String[][] numLayout = {{"1", "2", "3"}, {"4", "5", "6"}, {"7", "8", "9"}, {".", "0", "+/-"}, {"="}};
//        for (int r = 0; r < 5; r++) {
//            for (int c = 0; c < numLayout[r].length; c++) {
//                String text = numLayout[r][c];
//                String color = text.equals("=") ? "#e0f8ff" : "#e9ecef";
//                int span = text.equals("=") ? 3 : 1;
//                addButton(text, color, true, r, c, span);
//            }
//        }
//
//        String[][] opLayout = {{"CE", "C"}, {"+", "-"}, {"*", ":"}, {"^", "√"}, {"(", ")"}};
//        for (int r = 0; r < 5; r++) {
//            for (int c = 0; c < 2; c++) {
//                String text = opLayout[r][c];
//                String color;
//                if (text.equals("C")) {
//                    color = "#ffe3e0";
//                } else if (text.equals("CE")) {
//                    color = "#fff4e0";
//                } else {
//                    color = "#dee2e6";
//                }
//
//                addButton(text, color, false, r, c, 1);
//            }
//        }
//    }
//
//    private void addButton(String text, String color, boolean isNum, int r, int c, int span) {
//        Button btn = new Button(text);
//        btn.setPrefSize(span > 1 ? 211 : 65, 55);
//        btn.setStyle("-fx-background-color: " + color + "; -fx-font-weight: bold; -fx-background-radius: 8;");
//        btn.setOnAction(e -> { if (onActionHandler != null) onActionHandler.accept(text); });
//
//        if (isNum) numGrid.add(btn, c, r, span, 1);
//        else opGrid.add(btn, c, r);
//    }
//
//    public void setupKeyboardListener() {
//        this.setOnKeyPressed(event -> {
//            String text = event.getText();
//
//            if (event.getCode().isDigitKey()) {
//                onActionHandler.accept(text);
//            } else if (event.getCode() == javafx.scene.input.KeyCode.ENTER) {
//                onActionHandler.accept("=");
//            } else if (event.getCode() == javafx.scene.input.KeyCode.BACK_SPACE) {
//                onActionHandler.accept("CE");
//            } else if (event.getCode() == javafx.scene.input.KeyCode.ESCAPE) {
//                onActionHandler.accept("C");
//            } else if (text.equals("+") || text.equals("-") || text.equals("*") ||
//                    text.equals("/") || text.equals(":") || text.equals("(") || text.equals(")")) {
//                onActionHandler.accept(text.equals("/") ? ":" : text);
//            }
//        });
//    }
//
//    public void setOnAction(Consumer<String> handler) { this.onActionHandler = handler; }
//    public void setExpression(String text) { expressionLabel.setText(text); }
//    public void setResult(String text) { resultLabel.setText(text); }
//}