package com.example.javafx;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class FigureOutput extends DataOutputStream {
    public FigureOutput(OutputStream out) {
        super(out);
    }

    public void writeDrawable(Drawable d) throws IOException {
        d.writeToStream(this);
    }

    public void writeDrawableList(List<? extends Drawable> list) throws IOException {
        writeInt(list.size());
        for (Drawable d : list) writeDrawable(d);
    }
}