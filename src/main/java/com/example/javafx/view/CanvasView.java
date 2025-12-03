package com.example.javafx.view;

import com.example.javafx.model.GameModel;
import com.example.javafx.presenter.GamePresenter;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.geometry.VPos;

public class CanvasView implements IGameView {
    private Canvas canvas;
    private double tileSize;

    @Override
    public void init(int rows, int cols, double tileSize, GamePresenter presenter) {
        this.tileSize = tileSize;
        this.canvas = new Canvas(cols * tileSize, rows * tileSize);
        canvas.setOnMouseClicked(e -> presenter.handleAction((int)(e.getY()/tileSize), (int)(e.getX()/tileSize), e.getButton()));
    }

    @Override
    public void render(GameModel model) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        Font numberFont = Font.font("MS Sans Serif", FontWeight.BOLD, tileSize * 0.6);
        Font emojiFont = Font.font("MS Sans Serif", FontWeight.BOLD, Math.min(tileSize * 0.6, 80));

        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);

        for (int r = 0; r < model.rows; r++) {
            for (int c = 0; c < model.cols; c++) {
                double x = c * tileSize;
                double y = r * tileSize;
                double centerX = x + tileSize / 2;
                double centerY = y + tileSize / 2;

                gc.setFill(getCellColor(model.opened[r][c]));
                gc.fillRect(x, y, tileSize, tileSize);

                if (model.opened[r][c]) {
                    if (model.mines[r][c]) {
                        gc.setFont(emojiFont);
                        gc.setFill(Color.BLACK);
                        gc.fillText(MINE_IC, centerX, centerY);
                    } else if (model.counts[r][c] > 0) {
                        gc.setFont(numberFont);
                        gc.setFill(getNumberColor(model.counts[r][c]));
                        gc.fillText(String.valueOf(model.counts[r][c]), centerX, centerY);
                    }
                } else if (model.flags[r][c]) {
                    gc.setFont(emojiFont);
                    gc.fillText(FLAG_IC, centerX, centerY);
                }

                gc.setStroke(getStrokeColor());
                gc.setLineWidth(2);
                gc.strokeRect(x, y, tileSize, tileSize);
            }
        }
    }

    @Override public void showMessage(String t, String c) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, c);
        a.setTitle(t); a.setHeaderText(null); a.showAndWait();
    }
    @Override public Parent getRoot() { return new StackPane(canvas); }
}