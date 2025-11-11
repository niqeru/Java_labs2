package com.example.javafx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.util.Optional;
import java.util.Random;

public class HelloApplication extends Application {
    private int rows, cols, minesCount;
    private final int windowSize = 700;
    private double tileSize;
    private boolean[][] mines, opened, flags;
    private int[][] counts;
    private boolean firstClick = true, gameOver = false;
    private long startTime;
    private Canvas canvas;

    @Override
    public void start(Stage stage) {
        this.rows = askInt("Строки", "Введите количество строк (1-100):", 10, 1, 100);
        this.cols = askInt("Столбцы", "Введите количество столбцов (1-100):", 10, 1, 100);

        int maxPossibleMines = (rows * cols) - 1;
        this.minesCount = askInt("Мины", "Мины (макс " + maxPossibleMines + "):", (rows * cols) / 6, 1, maxPossibleMines);

        mines = new boolean[rows][cols];
        counts = new int[rows][cols];
        opened = new boolean[rows][cols];
        flags = new boolean[rows][cols];
        tileSize = (double) windowSize / Math.max(rows, cols);

        canvas = new Canvas(cols * tileSize, rows * tileSize);
        render();

        canvas.setOnMouseClicked(e -> {
            if (gameOver) return;
            int c = (int) (e.getX() / tileSize), r = (int) (e.getY() / tileSize);
            if (r >= 0 && r < rows && c >= 0 && c < cols) {
                if (e.getButton() == MouseButton.PRIMARY) onLeftClick(r, c);
                else if (e.getButton() == MouseButton.SECONDARY) flags[r][c] = !flags[r][c];
                render();
                checkWin();
            }
        });

        stage.setScene(new Scene(new StackPane(canvas)));
        stage.setTitle("Сапер");
        stage.show();
    }

    private void onLeftClick(int r, int c) {
        if (flags[r][c]) return;
        if (firstClick) {
            startTime = System.currentTimeMillis();
            generateMines(r, c);
            firstClick = false;
        }
        if (mines[r][c]) {
            gameOver = true;
            revealMines();
            showAlert("Поражение", "Вы подорвались!");
        } else recursiveOpen(r, c);
    }

    private void generateMines(int startR, int startC) {
        Random rnd = new Random();
        int placed = 0;
        while (placed < minesCount) {
            int r = rnd.nextInt(rows), c = rnd.nextInt(cols);
            if (!mines[r][c] && (r != startR || c != startC)) {
                mines[r][c] = true;
                placed++;
            }
        }
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                if (!mines[r][c]) counts[r][c] = countNearby(r, c);
    }

    private int countNearby(int r, int c) {
        int n = 0;
        for (int i = -1; i <= 1; i++)
            for (int j = -1; j <= 1; j++) {
                int nr = r + i, nc = c + j;
                if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && mines[nr][nc]) n++;
            }
        return n;
    }

    private void recursiveOpen(int r, int c) {
        if (r < 0 || r >= rows || c < 0 || c >= cols || opened[r][c] || flags[r][c]) return;
        opened[r][c] = true;
        if (counts[r][c] == 0)
            for (int i = -1; i <= 1; i++)
                for (int j = -1; j <= 1; j++) recursiveOpen(r + i, c + j);
    }

    private void render() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setFont(Font.font("MS Sans Serif", FontWeight.BOLD, tileSize * 0.6));
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                double x = c * tileSize, y = r * tileSize;
                if (opened[r][c]) {
                    gc.setFill(Color.LIGHTGRAY);
                    gc.fillRect(x, y, tileSize, tileSize);
                    if (mines[r][c]) {
                        gc.setFill(Color.BLACK);
                        gc.fillOval(x + tileSize*0.2, y + tileSize*0.2, tileSize*0.6, tileSize*0.6);
                    } else if (counts[r][c] > 0) {
                        gc.setFill(getColor(counts[r][c]));
                        gc.fillText(String.valueOf(counts[r][c]), x + tileSize*0.3, y + tileSize*0.7);
                    }
                } else {
                    gc.setFill(Color.GRAY);
                    gc.fillRect(x, y, tileSize, tileSize);
                    if (flags[r][c]) { gc.setFill(Color.RED); gc.fillText("F", x + tileSize*0.3, y + tileSize*0.7); }
                }
                gc.setStroke(Color.DARKGRAY);
                gc.strokeRect(x, y, tileSize, tileSize);
            }
        }
    }

    private void checkWin() {
        int closedSafe = 0;
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                if (!mines[r][c] && !opened[r][c]) closedSafe++;
        if (closedSafe == 0 && !gameOver) {
            showAlert("ПОБЕДА!", "Время: " + (System.currentTimeMillis() - startTime)/1000 + " сек.");
            System.exit(0);
        }
    }

    private int askInt(String title, String content, int def, int min, int max) {
        TextInputDialog dialog = new TextInputDialog(String.valueOf(def));
        dialog.setTitle(title); dialog.setHeaderText(null);
        dialog.setContentText(content);
        Optional<String> result = dialog.showAndWait();
        try {
            int val = Integer.parseInt(result.orElse(String.valueOf(def)));
            return Math.max(min, Math.min(max, val));
        } catch (Exception e) { return def; }
    }

    private Color getColor(int n) {
        return switch (n) {
            case 1 -> Color.BLUE; case 2 -> Color.GREEN; case 3 -> Color.RED;
            case 4 -> Color.DARKBLUE; default -> Color.PURPLE;
        };
    }

    private void revealMines() {
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++) if (mines[r][c]) opened[r][c] = true;
    }

    private void showAlert(String title, String text) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(title); a.setHeaderText(null); a.setContentText(text);
        a.showAndWait();
    }

    public static void main(String[] args) { launch(args); }
}