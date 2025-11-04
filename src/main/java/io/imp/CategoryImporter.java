package io.imp;

import domain.main.Category;
import domain.type.CategoryType;
import factory.CategoryFactory;
import io.imp.reader.CsvRecordReader;
import io.imp.reader.Readers;
import io.Format;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import repo.CategoryRepo;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class CategoryImporter extends AbstrImp<CategoryImporter.Row> {
    public record Row(CategoryType type, String name) {}
    private final CategoryFactory factory;
    private final CategoryRepo repo;
    public CategoryImporter(CategoryFactory factory, CategoryRepo repo) {
        this.factory = factory;
        this.repo = repo;
    }

    @Override
    @SneakyThrows
    protected List<Row> readAll(InputStream in, Format format) {
        return switch (format) {
            case CSV -> {
                var raw = CsvRecordReader.readRaw(in);
                var out = new ArrayList<Row>(raw.size());
                for (String[] p : raw) {
                    if (p.length < 2) {
                        throw new IllegalArgumentException("CSV expects: type,name");
                    }
                    String typeStr = p[0].trim().toUpperCase();
                    String name = p[1].trim();

                    try {
                        CategoryType type = CategoryType.valueOf(typeStr);
                        out.add(new Row(type, name));
                    } catch (IllegalArgumentException e) {
                        String valid = String.join(", ",
                                Arrays.stream(CategoryType.values()).map(Enum::name).toList());
                        throw new IllegalArgumentException(
                                "Invalid category type '%s'. Valid types: %s".formatted(typeStr, valid), e
                        );
                    }
                }
                yield out;
            }
            case JSON, YAML -> {
                com.fasterxml.jackson.databind.ObjectMapper m =
                        (format == io.Format.JSON)
                                ? new com.fasterxml.jackson.databind.ObjectMapper()
                                : new com.fasterxml.jackson.dataformat.yaml.YAMLMapper();

                // удобства: enum'ы без учета регистра
                m.enable(com.fasterxml.jackson.databind.MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS);

                com.fasterxml.jackson.databind.JavaType listOfRow =
                        m.getTypeFactory().constructCollectionType(java.util.List.class, Row.class);

                yield m.readValue(in, listOfRow); // теперь это List<Row>, а не List<LinkedHashMap>
            }
        };
    }

    @Override
    protected void validate(Row r) {
        if (r.name() == null || r.name().isBlank()) {
            throw new IllegalArgumentException("Category name cannot be blank");
        }
    }

    @Override
    @SneakyThrows
    protected void persist(Row r) {
        if (repo.existsNameType(r.name(), r.type())) {
            return; // дубликаты пропускаем
        }
        Category c = factory.create(r.type(), r.name());
        repo.save(c);
    }
}