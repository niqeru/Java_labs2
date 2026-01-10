package com.example.javafx.view.widget;

import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import java.util.function.Consumer;

public abstract class BaseKeyboard extends GridPane {
    protected Consumer<String> onActionHandler;

    public BaseKeyboard() {
        this.setHgap(8);
        this.setVgap(8);
    }

    public void setOnAction(Consumer<String> handler) { this.onActionHandler = handler; }

    protected Button createButton(String text, String color, double width) {
        Button btn = new Button(text);
        btn.setPrefSize(width, 55);
        btn.setStyle("-fx-background-color: " + color + "; -fx-font-weight: bold; -fx-background-radius: 8;");
        btn.setOnAction(e -> { if (onActionHandler != null) onActionHandler.accept(text); });
        return btn;
    }
}