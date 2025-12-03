package com.example.javafx.view;

import com.example.javafx.model.GameModel;
import com.example.javafx.presenter.GamePresenter;
import javafx.scene.Parent;
import javafx.scene.paint.Color;

public interface IGameView {
    String MINE_IC = "\uD83D\uDCA3";
    String FLAG_IC = "\uD83D\uDEA9";

    default Color getClosedColor() { return Color.WHITESMOKE; }
    default Color getOpenedColor() { return Color.LIGHTGRAY; }
    default Color getStrokeColor() { return Color.DARKGRAY; }

    void init(int rows, int cols, double tileSize, GamePresenter presenter);
    void render(GameModel model);
    void showMessage(String title, String text);
    Parent getRoot();

    default Color getNumberColor(int n) {
        return switch (n) {
            case 1 -> Color.BLUE;
            case 2 -> Color.GREEN;
            case 3 -> Color.RED;
            case 4 -> Color.DARKBLUE;
            case 5 -> Color.BROWN;
            case 6 -> Color.CYAN;
            case 7 -> Color.BLACK;
            default -> Color.PURPLE;
        };
    }

    default Color getCellColor(boolean opened) {
        return opened ? Color.WHITESMOKE : Color.LIGHTGRAY;
    }
}