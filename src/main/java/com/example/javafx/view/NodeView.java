package com.example.javafx.view;

import com.example.javafx.model.GameModel;
import com.example.javafx.presenter.GamePresenter;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

public class NodeView implements IGameView {
    private final GridPane grid = new GridPane();
    private Button[][] buttons;
    private double tileSize;

    @Override
    public void init(int rows, int cols, double tileSize, GamePresenter presenter) {
        this.tileSize = tileSize;
        buttons = new Button[rows][cols];

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Button btn = new Button();
                btn.setPrefSize(tileSize, tileSize);
                btn.setMinSize(tileSize, tileSize);
                btn.setMaxSize(tileSize, tileSize);
                btn.setFocusTraversable(false);
                btn.setAlignment(Pos.CENTER);

                updateButtonStyle(btn, false, tileSize * 0.6);

                int finalR = r, finalC = c;
                btn.setOnMouseClicked(e -> presenter.handleAction(finalR, finalC, e.getButton()));

                buttons[r][c] = btn;
                grid.add(btn, c, r);
            }
        }
    }

    @Override
    public void render(GameModel model) {
        double numFontSize = tileSize * 0.6;
        double emojiFontSize = Math.min(tileSize * 0.6, 80);

        for (int r = 0; r < model.rows; r++) {
            for (int c = 0; c < model.cols; c++) {
                Button btn = buttons[r][c];

                if (model.opened[r][c]) {
                    btn.setDisable(true);
                    if (model.mines[r][c]) {
                        updateButtonStyle(btn, true, emojiFontSize);
                        btn.setText(MINE_IC);
                        btn.setTextFill(Color.BLACK);
                    } else if (model.counts[r][c] > 0) {
                        updateButtonStyle(btn, true, numFontSize);
                        btn.setText(String.valueOf(model.counts[r][c]));
                        btn.setTextFill(getNumberColor(model.counts[r][c]));
                    } else {
                        updateButtonStyle(btn, true, numFontSize);
                        btn.setText("");
                    }
                } else {
                    btn.setDisable(false);
                    if (model.flags[r][c]) {
                        updateButtonStyle(btn, false, emojiFontSize);
                        btn.setText(FLAG_IC);
                    } else {
                        updateButtonStyle(btn, false, numFontSize);
                        btn.setText("");
                    }
                }
            }
        }
    }

    private void updateButtonStyle(Button btn, boolean isOpen, double fontSize) {
        String bgColor = toHex(getCellColor(isOpen));
        String strokeColor = toHex(getStrokeColor());

        String style = String.format(
                "-fx-font-size: %.1fpx; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 0; " +
                        "-fx-padding: 0; " +
                        "-fx-background-color: %s; " +
                        "-fx-border-color: %s; " +
                        "-fx-opacity: 1;",
                fontSize, bgColor, strokeColor);

        btn.setStyle(style);
    }

    private String toHex(Color c) {
        return String.format("#%02X%02X%02X",
                (int) (c.getRed() * 255),
                (int) (c.getGreen() * 255),
                (int) (c.getBlue() * 255));
    }

    @Override public void showMessage(String t, String c) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, c);
        a.setTitle(t); a.setHeaderText(null); a.showAndWait();
    }

    @Override public Parent getRoot() { return grid; }
}