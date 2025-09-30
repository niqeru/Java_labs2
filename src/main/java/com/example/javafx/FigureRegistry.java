package com.example.javafx;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FigureRegistry {
    private static final Map<Integer, FigureReader> readers = new HashMap<>();

    public static void register(int type, FigureReader reader) {
        readers.put(type, reader);
    }

    public static Drawable read(int type, FigureInput in) throws IOException {
        FigureReader reader = readers.get(type);
        if (reader == null) throw new IOException("Unknown figure type: " + type);
        return reader.read(in);
    }

    public interface FigureReader {
        Drawable read(FigureInput in) throws IOException;
    }
}