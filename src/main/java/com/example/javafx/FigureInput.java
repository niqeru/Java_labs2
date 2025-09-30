package com.example.javafx;


import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class FigureInput extends DataInputStream {
    public FigureInput(InputStream in) {
        super(in);
    }

    public Drawable readDrawable() throws IOException {
        int type = readInt();
        return FigureRegistry.read(type, this);
    }

    public List<Drawable> readDrawableList() throws IOException {
        int n = readInt();
        List<Drawable> list = new ArrayList<>();
        for (int i = 0; i < n; i++) list.add(readDrawable());
        return list;
    }
}