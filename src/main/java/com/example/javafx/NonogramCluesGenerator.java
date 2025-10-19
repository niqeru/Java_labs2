package com.example.javafx;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NonogramCluesGenerator {

    private static List<Integer> generateCluesForLine(int[] line) {
        List<Integer> clues = new ArrayList<>();
        int currentCount = 0;

        for (int cell : line) {
            if (cell == 1) {
                currentCount++;
            } else {
                if (currentCount > 0) {
                    clues.add(currentCount);
                    currentCount = 0;
                }
            }
        }

        if (currentCount > 0) {
            clues.add(currentCount);
        }

        if (clues.isEmpty()) {
            return Collections.singletonList(0);
        }

        return clues;
    }

    public static List<List<Integer>> generateRowClues(int[][] solution) {
        List<List<Integer>> allClues = new ArrayList<>();
        int height = solution.length;

        for (int r = 0; r < height; r++) {
            int[] row = solution[r];
            allClues.add(generateCluesForLine(row));
        }
        return allClues;
    }

    public static List<List<Integer>> generateColClues(int[][] solution) {
        List<List<Integer>> allClues = new ArrayList<>();
        if (solution.length == 0) return allClues;
        int height = solution.length;
        int width = solution[0].length;

        for (int c = 0; c < width; c++) {
            int[] col = new int[height];
            for (int r = 0; r < height; r++) {
                col[r] = solution[r][c];
            }
            allClues.add(generateCluesForLine(col));
        }
        return allClues;
    }
}
