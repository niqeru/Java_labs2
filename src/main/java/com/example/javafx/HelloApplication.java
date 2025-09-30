package com.example.javafx;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class HelloApplication extends Application {

    private final List<Drawable> shapes = new CopyOnWriteArrayList<>();
    private Canvas canvas;
    private GraphicsContext gc;
    private Label statusLabel;

    @Override
    public void start(Stage stage) {

        canvas = new Canvas(600, 600);
        gc = canvas.getGraphicsContext2D();

        setupGraphicsContext();

        Button openBtn = new Button("Открыть файл с фигурами");
        openBtn.setOnAction(e -> openFile(stage));

        Button clearBtn = new Button("Очистить");
        clearBtn.setOnAction(e -> {
            shapes.clear();
            redraw();
        });


        statusLabel = new Label("Готов к работе");
        statusLabel.setPadding(new Insets(0, 10, 0, 10));

        HBox topBar = new HBox(10, openBtn, clearBtn);
        topBar.setPadding(new Insets(8));
        topBar.setAlignment(Pos.CENTER_LEFT);

        HBox bottomBar = new HBox(statusLabel);
        bottomBar.setPadding(new Insets(5));
        bottomBar.setStyle("-fx-background-color: #f0f0f0;");

        BorderPane root = new BorderPane();
        root.setTop(topBar);
        root.setCenter(canvas);
        root.setBottom(bottomBar);

        Scene scene = new Scene(root, 800, 650);
        setupResizeHandlers(scene, topBar, bottomBar);

        stage.setTitle("Просмотр фигур на Canvas");
        stage.setScene(scene);
        stage.setMinWidth(400);
        stage.setMinHeight(300);
        stage.show();
        redraw();
    }

    private void setupGraphicsContext() {
        gc.setFill(Color.LIGHTBLUE);
        gc.setStroke(Color.DARKBLUE);
        gc.setLineWidth(2.0);
    }

    private void setupResizeHandlers(Scene scene, HBox topBar, HBox bottomBar) {
        scene.widthProperty().addListener((obs, oldV, newV) -> {
            canvas.setWidth(newV.doubleValue());
            redraw();
        });

        scene.heightProperty().addListener((obs, oldV, newV) -> {
            double newHeight = newV.doubleValue() - topBar.getHeight() - bottomBar.getHeight();
            canvas.setHeight(newHeight);
            redraw();
        });
    }

    private void openFile(Stage stage) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Выберите файл с фигурами");
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Текстовые файлы", "*.txt"),
                new FileChooser.ExtensionFilter("Все файлы", "*.*")
        );

        File file = chooser.showOpenDialog(stage);
        if (file != null) {
            try {
                List<Drawable> loaded = ShapesReader.read(file);
                shapes.clear();
                shapes.addAll(loaded);
                statusLabel.setText(String.format("Загружено %d фигур из файла: %s",
                        loaded.size(), file.getName()));
                redraw();
            } catch (Exception ex) {
                statusLabel.setText("Ошибка загрузки файла: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    private void redraw() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        drawBackground();

        for (Drawable shape : shapes) {
            shape.draw(gc);
        }
        drawGrid();
    }

    private void drawBackground() {
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    private void drawGrid() {
        gc.setStroke(Color.LIGHTGRAY);
        gc.setLineWidth(0.5);

        double width = canvas.getWidth();
        double height = canvas.getHeight();

        for (double x = 0; x <= width; x += 50) {
            gc.strokeLine(x, 0, x, height);
        }

        for (double y = 0; y <= height; y += 50) {
            gc.strokeLine(0, y, width, y);
        }

        gc.setLineWidth(2.0);
        gc.setStroke(Color.DARKBLUE);
    }

    public static void main(String[] args) {
        launch();
    }
}