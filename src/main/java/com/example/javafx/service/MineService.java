package com.example.javafx.service;

import java.util.Random;

public class MineService {
    public static void generate(boolean[][] mines, int[][] counts, int rows, int cols, int count, int startR, int startC) {
        Random rnd = new Random();
        int placed = 0;
        while (placed < count) {
            int r = rnd.nextInt(rows), c = rnd.nextInt(cols);
            if (!mines[r][c] && (r != startR || c != startC)) {
                mines[r][c] = true;
                placed++;
            }
        }
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                if (!mines[r][c]) counts[r][c] = countAdjacent(mines, r, c, rows, cols);
    }

    private static int countAdjacent(boolean[][] mines, int r, int c, int rows, int cols) {
        int n = 0;
        for (int i = -1; i <= 1; i++)
            for (int j = -1; j <= 1; j++) {
                int nr = r + i, nc = c + j;
                if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && mines[nr][nc]) n++;
            }
        return n;
    }
}