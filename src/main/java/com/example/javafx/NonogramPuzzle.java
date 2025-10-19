package com.example.javafx;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class NonogramPuzzle {
    private int width;
    private int height;
    private int[][] solution;
    private int[][] playerGrid;
    private List<List<Integer>> rowClues = new ArrayList<>();
    private List<List<Integer>> colClues = new ArrayList<>();

    public NonogramPuzzle() {
        this.width = 0;
        this.height = 0;
        this.solution = new int[0][0];
        this.playerGrid = new int[0][0];
    }

    public boolean loadFromFile(File file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            this.width = Integer.parseInt(br.readLine().trim());
            this.height = Integer.parseInt(br.readLine().trim());

            if (width <= 0 || height <= 0) {
                throw new IOException("Размеры кроссворда должны быть положительными.");
            }

            this.solution = new int[height][width];
            this.playerGrid = new int[height][width];

            for (int r = 0; r < height; r++) {
                String line = br.readLine();
                if (line == null) {
                    throw new IOException("Недостаточно строк в файле для заданной высоты.");
                }
                String[] values = line.trim().split("\\s+");

                if (values.length != width) {
                    throw new IOException("Ошибка: строка " + r + " имеет " + values.length + " значений, ожидалось " + width);
                }

                for (int c = 0; c < width; c++) {
                    this.solution[r][c] = Integer.parseInt(values[c]);
                }
            }

            this.rowClues = NonogramCluesGenerator.generateRowClues(solution);
            this.colClues = NonogramCluesGenerator.generateColClues(solution);

            System.out.println("Файл успешно загружен. Размер: " + width + "x" + height);
            return true;

        } catch (IOException | NumberFormatException e) {
            System.err.println("Ошибка при загрузке файла: " + e.getMessage());
            this.width = 0;
            this.height = 0;
            this.rowClues.clear();
            this.colClues.clear();
            return false;
        }
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }

    public int getPlayerCell(int r, int c) {
        if (r >= 0 && r < height && c >= 0 && c < width) {
            return playerGrid[r][c];
        }
        return 0;
    }

    public void setPlayerCell(int r, int c, int state) {
        if (r >= 0 && r < height && c >= 0 && c < width) {
            playerGrid[r][c] = state;
        }
    }

    public List<List<Integer>> getRowClues() { return rowClues; }
    public List<List<Integer>> getColClues() { return colClues; }


    public boolean checkWinCondition() {
        if (solution == null || width == 0 || height == 0) {
            return false;
        }

        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                int solutionState = solution[r][c];
                int playerState = playerGrid[r][c];

                if (solutionState == 1 && playerState != 1) {
                    return false;
                }
                if (solutionState == 0 && playerState == 1) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isRowSolved(int r) {
        if (r < 0 || r >= height) return false;

        for (int c = 0; c < width; c++) {
            int solutionState = solution[r][c];
            int playerState = playerGrid[r][c];

            if (solutionState == 1 && playerState != 1) {
                return false;
            }
            if (solutionState == 0 && playerState == 1) {
                return false;
            }
        }
        return true;
    }

    public boolean isColSolved(int c) {
        if (c < 0 || c >= width) return false;

        for (int r = 0; r < height; r++) {
            int solutionState = solution[r][c];
            int playerState = playerGrid[r][c];

            if (solutionState == 1 && playerState != 1) {
                return false;
            }

            if (solutionState == 0 && playerState == 1) {
                return false;
            }
        }
        return true;
    }

}