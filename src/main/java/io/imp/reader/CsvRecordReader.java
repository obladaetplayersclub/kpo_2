package io.imp.reader;

import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class CsvRecordReader<D> implements RecordReader<D> {
    public CsvRecordReader(Class<D> rowClass) {}

    @Override
    @SneakyThrows
    public List<D> readAll(InputStream in) {
        throw new Exception("Use readRaw()");
    }
    public static List<String[]> readRaw(InputStream in) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            List<String[]> rows = new ArrayList<>();
            String line;
            boolean header = true;
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;
                if (header) {
                    header = false;
                    continue;
                }
                rows.add(line.split(",", -1));
            }
            return rows;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}