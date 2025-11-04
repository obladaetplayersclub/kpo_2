package io.imp.reader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.Format;

public final class Readers {
    private Readers(){}
    public static <D> RecordReader<D> of(Format format, Class<D> rowClass) {
        return switch (format) {
            case CSV  -> new CsvRecordReader<>(rowClass);
            case JSON -> new JsonRecordReader<>(new ObjectMapper(), rowClass);
            case YAML -> new YamlRecordReader<>(new ObjectMapper(new YAMLFactory()), rowClass);
        };
    }
}
