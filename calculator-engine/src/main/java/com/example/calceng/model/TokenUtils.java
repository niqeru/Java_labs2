package com.example.calceng.model;

public class TokenUtils {
    public static boolean isOperator(String s) { return "+-*^:√".contains(s); }
    public static boolean isDigit(String s) { return s.matches("\\d+"); }
    public static boolean isNumber(String s) {
        return s != null && s.matches("-?\\d+(\\.\\d+)?");
    }
}