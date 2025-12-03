package com.example.javafx.presenter;

import com.example.javafx.model.GameModel;
import com.example.javafx.service.MineService;
import com.example.javafx.view.IGameView;
import javafx.scene.input.MouseButton;

public class GamePresenter {
    private final GameModel model;
    private final IGameView view;

    public GamePresenter(GameModel model, IGameView view) {
        this.model = model;
        this.view = view;
    }

    public void handleAction(int r, int c, MouseButton button) {
        if (model.gameOver) return;

        if (button == MouseButton.SECONDARY) {
            model.flags[r][c] = !model.flags[r][c];
        } else {
            if (model.flags[r][c]) return;
            if (model.firstClick) {
                model.startTime = System.currentTimeMillis();
                MineService.generate(model.mines, model.counts, model.rows, model.cols, model.minesCount, r, c);
                model.firstClick = false;
            }
            if (model.mines[r][c]) {
                model.gameOver = true;
                revealMines();
                view.render(model);
                view.showMessage("Поражение", "Вы подорвались!");
                return;
            } else model.openCell(r, c);
        }
        view.render(model);
        if (model.isVictory()) {
            view.showMessage("Победа", "Время: " + (System.currentTimeMillis() - model.startTime)/1000 + " сек.");
            System.exit(0);
        }
    }

    private void revealMines() {
        for (int r = 0; r < model.rows; r++)
            for (int c = 0; c < model.cols; c++) if (model.mines[r][c]) model.opened[r][c] = true;
    }
}