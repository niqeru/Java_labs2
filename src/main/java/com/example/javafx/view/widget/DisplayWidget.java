package com.example.javafx.view.widget;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class DisplayWidget extends VBox {
    private final Label expressionLabel = new Label("");
    private final Label resultLabel = new Label("");

    public DisplayWidget() {
        super(5);
        this.setAlignment(Pos.CENTER_RIGHT);
        this.setStyle("-fx-background-color: white; -fx-border-color: #dee2e6; -fx-border-radius: 5; -fx-padding: 15;");
        this.setPrefHeight(100);

        expressionLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #6c757d;");
        resultLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #212529;");

        this.getChildren().addAll(expressionLabel, resultLabel);
    }

    public void setExpression(String text) { expressionLabel.setText(text); }
    public void setResult(String text) { resultLabel.setText(text); }
}