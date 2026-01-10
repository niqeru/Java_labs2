package com.example.javafx;

import com.example.javafx.controller.CalculatorController;
import com.example.javafx.model.MathEngine;
import com.example.javafx.view.CalculatorView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) {
        MathEngine engine = new MathEngine();
        CalculatorView view = new CalculatorView();
        new CalculatorController(engine, view);

        stage.setScene(new Scene(view, 450, 550));
        stage.setTitle("Калькулятор");
        stage.show();
    }
    public static void main(String[] args) { launch(); }
}