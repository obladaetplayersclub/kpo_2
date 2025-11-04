package io.export;

import java.io.*;
import java.nio.charset.StandardCharsets;

public abstract class BaseExportVisitor implements ExportVisitor, Closeable {
    protected final Writer out;

    protected BaseExportVisitor(OutputStream os) {
        this.out = new BufferedWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8));
    }

    protected void writeLine(String s) {
        try {
            out.write(s); out.write("\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void writeRaw(String s) {
        try {
            out.write(s);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        try {
            out.flush(); out.close();
        } catch (IOException ignored) {

        }
    }
}
