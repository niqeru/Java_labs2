package com.example.javafx;

import com.example.javafx.config.Config;
import com.example.javafx.model.GameModel;
import com.example.javafx.presenter.GamePresenter;
import com.example.javafx.view.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import java.util.Optional;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) {
        int rows = askInt("Строки", "Введите количество строк (1-100):", 10, 1, 100);
        int cols = askInt("Столбцы", "Введите количество столбцов (1-100):", 10, 1, 100);

        int maxPossibleMines = (rows * cols) - 1;
        int mines = askInt("Мины", "Мины (макс " + maxPossibleMines + "):", (rows * cols) / 6, 1, maxPossibleMines);

        GameModel model = new GameModel(rows, cols, mines);

        IGameView view = Config.USE_CANVAS_RENDER ? new CanvasView() : new NodeView();
        GamePresenter presenter = new GamePresenter(model, view);

        double tileSize = (double) Config.WINDOW_SIZE / Math.max(rows, cols);

        view.init(rows, cols, tileSize, presenter);
        view.render(model);

        stage.setScene(new Scene(view.getRoot()));
        stage.setTitle("Сапер MVP [" + (Config.USE_CANVAS_RENDER ? "Canvas" : "Nodes") + "]");
        stage.show();
    }

    private int askInt(String title, String content, int def, int min, int max) {
        TextInputDialog dialog = new TextInputDialog(String.valueOf(def));
        dialog.setTitle(title);
        dialog.setHeaderText(null);
        dialog.setContentText(content);

        Optional<String> result = dialog.showAndWait();
        try {
            int val = Integer.parseInt(result.orElse(String.valueOf(def)));
            return Math.max(min, Math.min(max, val));
        } catch (NumberFormatException e) {
            return def;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}