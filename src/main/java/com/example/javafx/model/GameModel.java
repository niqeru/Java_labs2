package com.example.javafx.model;

public class GameModel {
    public int rows, cols, minesCount;
    public boolean[][] mines, opened, flags;
    public int[][] counts;
    public boolean firstClick = true, gameOver = false;
    public long startTime;

    public GameModel(int rows, int cols, int minesCount) {
        this.rows = rows; this.cols = cols; this.minesCount = minesCount;
        this.mines = new boolean[rows][cols];
        this.opened = new boolean[rows][cols];
        this.flags = new boolean[rows][cols];
        this.counts = new int[rows][cols];
    }

    public void openCell(int r, int c) {
        if (r < 0 || r >= rows || c < 0 || c >= cols || opened[r][c] || flags[r][c]) return;
        opened[r][c] = true;
        if (counts[r][c] == 0 && !mines[r][c]) {
            for (int i = -1; i <= 1; i++)
                for (int j = -1; j <= 1; j++) openCell(r + i, c + j);
        }
    }

    public boolean isVictory() {
        int closedSafe = 0;
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                if (!mines[r][c] && !opened[r][c]) closedSafe++;
        return closedSafe == 0;
    }
}