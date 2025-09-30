package com.example.javafx;

import java.io.*;
import java.util.*;

public class ShapesReader {

    public static List<Drawable> read(File file) {
        List<Drawable> out = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            int lineno = 0;
            while ((line = br.readLine()) != null) {
                lineno++;
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                try {
                    StringTokenizer st = new StringTokenizer(line);
                    String type = st.nextToken().toUpperCase();
                    switch (type) {
                        case "POINT": {
                            double x = Double.parseDouble(st.nextToken());
                            double y = Double.parseDouble(st.nextToken());
                            out.add(new Point(x, y));
                            break;
                        }
                        case "LINE": {
                            double x1 = Double.parseDouble(st.nextToken());
                            double y1 = Double.parseDouble(st.nextToken());
                            double x2 = Double.parseDouble(st.nextToken());
                            double y2 = Double.parseDouble(st.nextToken());
                            out.add(new Line(new Vector(x1, y1), new Vector(x2, y2)));
                            break;
                        }
                        case "RECT": {
                            double x = Double.parseDouble(st.nextToken());
                            double y = Double.parseDouble(st.nextToken());
                            double w = Double.parseDouble(st.nextToken());
                            double h = Double.parseDouble(st.nextToken());
                            double angle = 0.0;
                            if (st.hasMoreTokens()) {
                                angle = Double.parseDouble(st.nextToken());
                            }
                            out.add(new Rectangle(new Vector(x, y), w, h, angle));
                            break;
                        }
                        case "TRIANGLE": {
                            double x1 = Double.parseDouble(st.nextToken());
                            double y1 = Double.parseDouble(st.nextToken());
                            double x2 = Double.parseDouble(st.nextToken());
                            double y2 = Double.parseDouble(st.nextToken());
                            double x3 = Double.parseDouble(st.nextToken());
                            double y3 = Double.parseDouble(st.nextToken());
                            out.add(new Triangle(
                                    new Vector(x1, y1),
                                    new Vector(x2, y2),
                                    new Vector(x3, y3)
                            ));
                            break;
                        }
                        case "POLYGON": {
                            int n = Integer.parseInt(st.nextToken());
                            List<Vector> vertices = new ArrayList<>();
                            for (int i = 0; i < n; i++) {
                                double x = Double.parseDouble(st.nextToken());
                                double y = Double.parseDouble(st.nextToken());
                                vertices.add(new Vector(x, y));
                            }
                            out.add(new Polygon(vertices));
                            break;
                        }
                        case "CIRCLE": {
                            double x = Double.parseDouble(st.nextToken());
                            double y = Double.parseDouble(st.nextToken());
                            double r = Double.parseDouble(st.nextToken());
                            out.add(new Circle(new Vector(x, y), r));
                            break;
                        }
                        case "ELLIPSE": {
                            double x = Double.parseDouble(st.nextToken());
                            double y = Double.parseDouble(st.nextToken());
                            double a = Double.parseDouble(st.nextToken());
                            double b = Double.parseDouble(st.nextToken());
                            double angle = 0.0;
                            if (st.hasMoreTokens()) {
                                angle = Double.parseDouble(st.nextToken());
                            }
                            out.add(new Ellipse(new Vector(x, y), a, b, angle));
                            break;
                        }
                        default:
                            System.err.println("Неизвестный тип фигуры в строке " + lineno + ": " + type);
                            break;
                    }
                } catch (Exception ex) {
                    System.err.println("Пропущена некорректная строка " + lineno + ": " + ex.getMessage());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return out;
    }
}