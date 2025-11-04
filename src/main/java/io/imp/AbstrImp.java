package io.imp;

import java.io.InputStream;
import io.Format;
import java.util.List;

public abstract class AbstrImp<D> {
    public final void run(InputStream in, Format format) {
        List<D> rows = readAll(in, format);
        for (D row : rows) {
            validate(row);
            persist(row);
        }
    }
    protected abstract List<D> readAll(InputStream in, Format format);
    protected void validate(D row) {}
    protected abstract void persist(D row);
}
