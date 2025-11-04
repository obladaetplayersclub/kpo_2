package io.imp.reader;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@AllArgsConstructor
public class YamlRecordReader<D> implements RecordReader<D> {
    private final ObjectMapper mapper;
    private final Class<D> rowClass;

    @Override
    @SneakyThrows
    public List<D> readAll(InputStream in) {
        try {
            return mapper.readValue(in, new TypeReference<List<D>>() {});
        }
        catch (IOException e) {
            throw new IOException("YAML read error: " + e.getMessage(), e);
        }
    }
}