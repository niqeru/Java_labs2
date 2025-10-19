package com.example.javafx;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

public class HelloApplication extends Application {

    private NonogramPuzzle puzzle = new NonogramPuzzle();
    private final double MAX_CANVAS_SIZE = 400;
    private final double CLUE_AREA_SIZE = 150;
    private Canvas mainCanvas;
    private Canvas rowClueCanvas;
    private Canvas colClueCanvas;
    private Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;

        mainCanvas = new Canvas(MAX_CANVAS_SIZE, MAX_CANVAS_SIZE);
        rowClueCanvas = new Canvas(CLUE_AREA_SIZE, MAX_CANVAS_SIZE);
        colClueCanvas = new Canvas(MAX_CANVAS_SIZE, CLUE_AREA_SIZE);
        Canvas cornerCanvas = new Canvas(CLUE_AREA_SIZE, CLUE_AREA_SIZE);

        BorderPane root = new BorderPane();

        VBox topArea = new VBox(colClueCanvas);
        VBox leftArea = new VBox(rowClueCanvas);
        HBox topRow = new HBox(cornerCanvas, topArea);
        topRow.setAlignment(Pos.BOTTOM_RIGHT);
        HBox centerArea = new HBox(leftArea, mainCanvas);
        centerArea.setAlignment(Pos.TOP_RIGHT);

        root.setTop(topRow);
        root.setCenter(centerArea);

        Button loadButton = new Button("Загрузить файл кроссворда");
        loadButton.setOnAction(e -> openFile());
        VBox topControls = new VBox(10, loadButton);
        topControls.setAlignment(Pos.CENTER);
        root.setBottom(topControls);


        mainCanvas.setOnMouseClicked(event -> {
            if (puzzle.getWidth() > 0) {
                handleMouseClick(event.getX(), event.getY(), event.getButton());
            }
        });


        drawGrid(mainCanvas.getGraphicsContext2D(), 0, 0, 0);

        Scene scene = new Scene(root);
        primaryStage.setTitle("Японский кроссворд");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private void openFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выберите файл кроссворда");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Текстовые файлы (*.txt)", "*.txt")
        );
        File selectedFile = fileChooser.showOpenDialog(primaryStage);

        if (selectedFile != null) {
            if (puzzle.loadFromFile(selectedFile)) {

                int gridWidth = puzzle.getWidth();
                int gridHeight = puzzle.getHeight();

                double cellSize = MAX_CANVAS_SIZE / Math.max(gridWidth, gridHeight);

                drawGrid(mainCanvas.getGraphicsContext2D(), gridWidth, gridHeight, cellSize);
                drawRowClues(rowClueCanvas.getGraphicsContext2D(), gridHeight, cellSize);
                drawColClues(colClueCanvas.getGraphicsContext2D(), gridWidth, cellSize);
            } else {
                showAlert("Ошибка загрузки", "Не удалось прочитать файл. Проверьте формат данных (размеры, числа).");
            }
        }
    }

    private void handleMouseClick(double x, double y, MouseButton button) {
        int gridWidth = puzzle.getWidth();
        int gridHeight = puzzle.getHeight();
        double cellSize = MAX_CANVAS_SIZE / Math.max(gridWidth, gridHeight);

        int col = (int) (x / cellSize);
        int row = (int) (y / cellSize);

        if (row < 0 || row >= gridHeight || col < 0 || col >= gridWidth) return;

        int currentState = puzzle.getPlayerCell(row, col);
        int newState = currentState;

        if (button == MouseButton.PRIMARY) {
            if (currentState == 0) { newState = 1; }
            else if (currentState == 1) { newState = 0; }
        } else if (button == MouseButton.SECONDARY) {
            if (currentState == 2) { newState = 0; }
            else { newState = 2; }
        }

        if (newState != currentState) {
            puzzle.setPlayerCell(row, col, newState);
            drawGrid(mainCanvas.getGraphicsContext2D(), gridWidth, gridHeight, cellSize);
            drawRowClues(rowClueCanvas.getGraphicsContext2D(), gridHeight, cellSize);
            drawColClues(colClueCanvas.getGraphicsContext2D(), gridWidth, cellSize);
            if (puzzle.checkWinCondition()) {
                showWinAlert();
            }
        }
    }

    private void drawGrid(GraphicsContext gc, int gridWidth, int gridHeight, double cellSize) {
        if (gridWidth == 0 || gridHeight == 0 || cellSize == 0) {
            mainCanvas.setWidth(MAX_CANVAS_SIZE);
            mainCanvas.setHeight(MAX_CANVAS_SIZE);
            gc.clearRect(0, 0, MAX_CANVAS_SIZE, MAX_CANVAS_SIZE);
            return;
        }

        mainCanvas.setWidth(gridWidth * cellSize);
        mainCanvas.setHeight(gridHeight * cellSize);

        gc.clearRect(0, 0, mainCanvas.getWidth(), mainCanvas.getHeight());

        for (int r = 0; r < gridHeight; r++) {
            for (int c = 0; c < gridWidth; c++) {
                double x = c * cellSize;
                double y = r * cellSize;
                int state = puzzle.getPlayerCell(r, c);

                gc.setFill(state == 1 ? Color.BLACK : Color.WHITE);
                gc.fillRect(x, y, cellSize, cellSize);

                if (state == 2) {
                    gc.setFill(Color.BLACK);
                    gc.fillOval(x + cellSize * 0.35, y + cellSize * 0.35, cellSize * 0.3, cellSize * 0.3);
                }

                gc.setStroke(Color.GRAY);
                gc.strokeRect(x, y, cellSize, cellSize);
            }
        }
    }

    private void drawRowClues(GraphicsContext gc, int gridHeight, double cellSize) {
        gc.clearRect(0, 0, CLUE_AREA_SIZE, MAX_CANVAS_SIZE);

        List<List<Integer>> clues = puzzle.getRowClues();

        if (clues.isEmpty() || gridHeight == 0) return;

        rowClueCanvas.setHeight(gridHeight * cellSize);

        gc.setFont(new Font(cellSize * 0.5));
        gc.setTextAlign(javafx.scene.text.TextAlignment.RIGHT);

        for (int r = 0; r < gridHeight; r++) {
            if (r >= clues.size()) break;
            if (puzzle.isRowSolved(r)) {
                gc.setFill(Color.web("#28a745"));
            } else {
                gc.setFill(Color.BLACK);
            }
            double y = r * cellSize;
            List<Integer> rowClues = clues.get(r);

            String clueString = "";
            for (int i : rowClues) {
                clueString += i + " ";
            }
            clueString = clueString.trim();

            gc.fillText(clueString, CLUE_AREA_SIZE - 5, y + cellSize * 0.65);

            gc.setStroke(Color.GRAY);
            gc.strokeLine(0, y + cellSize, CLUE_AREA_SIZE, y + cellSize);
        }
    }

    private void drawColClues(GraphicsContext gc, int gridWidth, double cellSize) {
        gc.clearRect(0, 0, MAX_CANVAS_SIZE, CLUE_AREA_SIZE);

        List<List<Integer>> clues = puzzle.getColClues();

        if (clues.isEmpty() || gridWidth == 0) return;

        colClueCanvas.setWidth(gridWidth * cellSize);

        gc.setFont(new Font(cellSize * 0.5));
        gc.setTextAlign(javafx.scene.text.TextAlignment.CENTER);
        for (int c = 0; c < gridWidth; c++) {
            if (c >= clues.size()) break;

            if (puzzle.isColSolved(c)) {
                gc.setFill(Color.web("#28a745"));
            } else {
                gc.setFill(Color.BLACK);
            }

            double x = c * cellSize;
            List<Integer> colClues = clues.get(c);

            double currentY = CLUE_AREA_SIZE - 5;

            for (int i = colClues.size() - 1; i >= 0; i--) {
                String clue = String.valueOf(colClues.get(i));
                gc.fillText(clue, x + cellSize * 0.5, currentY);
                currentY -= cellSize * 0.6;
            }

            gc.setStroke(Color.GRAY);
            gc.strokeLine(x + cellSize, 0, x + cellSize, CLUE_AREA_SIZE);
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showWinAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Поздравляем!");
        alert.setHeaderText(null);
        alert.setContentText("Вы успешно решили кроссворд!");
        alert.showAndWait();
        mainCanvas.setWidth(MAX_CANVAS_SIZE);
        mainCanvas.setHeight(MAX_CANVAS_SIZE);
        mainCanvas.getGraphicsContext2D().clearRect(0, 0, MAX_CANVAS_SIZE, MAX_CANVAS_SIZE);
        rowClueCanvas.getGraphicsContext2D().clearRect(0, 0, CLUE_AREA_SIZE, MAX_CANVAS_SIZE);
        colClueCanvas.getGraphicsContext2D().clearRect(0, 0, MAX_CANVAS_SIZE, CLUE_AREA_SIZE);
    }
}