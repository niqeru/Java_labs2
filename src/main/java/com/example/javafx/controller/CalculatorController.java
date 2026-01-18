package com.example.javafx.controller;

import com.example.calceng.model.MathEngine;
import com.example.calceng.model.ExpressionManager;
import com.example.javafx.view.CalculatorView;
import javafx.application.Platform;

public class CalculatorController {
    private final MathEngine engine;
    private final CalculatorView view;
    private final ExpressionManager expressionManager;

    public CalculatorController(MathEngine engine, CalculatorView view) {
        this.engine = engine;
        this.view = view;
        this.expressionManager = new ExpressionManager();
        this.view.setOnAction(this::handlePress);
        this.view.setupKeyboardListener(this::handlePress);

        updateUI();

        Platform.runLater(view::requestFocus);
    }

    private void handlePress(String text) {
        if (text.equals("=")) {
            calculateFinal();
        } else {
            expressionManager.processInput(text);
        }
        updateUI();
    }

    private void calculateFinal() {
        try {
            String currentExpr = expressionManager.getExpression();
            double res = engine.calculate(currentExpr);
            expressionManager.setExpression(engine.formatResult(res));
        } catch (ArithmeticException e) {
            // Ловим конкретно математические ошибки (деление на 0)
            expressionManager.setExpression(e.getMessage());
        } catch (Exception e) {
            // Ловим всё остальное (ошибки парсинга и т.д.)
            expressionManager.setExpression("Invalid Expr");
        }
        updateUI();
    }

    private void updateUI() {
        String expr = expressionManager.getExpression();
        view.setExpression(expr);

        // Если в выражении есть буквы (сообщение об ошибке), показываем его и в результате
        if (expr.matches(".*[a-zA-Z].*")) {
            view.setResult(expr);
        } else if (expr.isEmpty() || expressionManager.isLastCharOperator()) {
            view.setResult("");
        } else {
            try {
                double res = engine.calculate(expr);
                view.setResult(engine.formatResult(res));
            } catch (Exception e) {
                view.setResult("");
            }
        }
    }
}