package io.imp.reader;

import java.io.InputStream;
import java.util.List;

public interface RecordReader<D> {
    List<D> readAll(InputStream in);
}