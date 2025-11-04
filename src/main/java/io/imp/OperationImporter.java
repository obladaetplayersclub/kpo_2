package io.imp;

import domain.main.Category;
import domain.main.Operation;
import domain.type.CategoryType;
import domain.type.OperationType;
import factory.OperationFactory;
import io.imp.reader.CsvRecordReader;
import io.imp.reader.Readers;
import io.Format;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import repo.BankAccountRepo;
import repo.CategoryRepo;
import repo.OperationRepo;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Component
public class OperationImporter extends AbstrImp<OperationImporter.Row> {
    public record Row(UUID accountId, UUID categoryId, OperationType type, double amount, LocalDate date, String description) {}

    private final OperationFactory factory;
    private final OperationRepo opRepo;
    private final BankAccountRepo accRepo;
    private final CategoryRepo catRepo;

    public OperationImporter(OperationFactory factory, OperationRepo opRepo, BankAccountRepo accRepo, CategoryRepo catRepo) {
        this.factory = factory;
        this.opRepo = opRepo;
        this.accRepo = accRepo;
        this.catRepo = catRepo;
    }

    @Override
    @SneakyThrows
    protected List<Row> readAll(InputStream in, Format format) {
        return switch (format) {
            case CSV -> {
                var raw = CsvRecordReader.readRaw(in);
                var out = new ArrayList<Row>(raw.size());
                for (int i = 0; i < raw.size(); i++) {
                    String[] p = raw.get(i);
                    if (p.length < 6) {
                        throw new IllegalArgumentException("CSV row #" + (i + 1) +
                                " must contain 6 columns (accountId,categoryId,type,amount,date,description), found: " + p.length);
                    }

                    try {
                        UUID accountId = UUID.fromString(p[0].trim());
                        UUID categoryId = UUID.fromString(p[1].trim());
                        OperationType type = OperationType.valueOf(p[2].trim().toUpperCase());
                        double amount = Double.parseDouble(p[3].trim());
                        LocalDate date = LocalDate.parse(p[4].trim());
                        String description = p[5].trim().isBlank() ? null : p[5].trim();

                        out.add(new Row(accountId, categoryId, type, amount, date, description));
                    } catch (Exception e) {
                        throw new IllegalArgumentException("Failed to parse CSV row #" + (i + 1) +
                                ": [" + String.join(", ", p) + "]", e);
                    }
                }
                yield out;
            }
            case JSON, YAML -> Readers.<Row>of(format, Row.class).readAll(in);
        };
    }

    @Override
    protected void validate(Row r) {
        if (r.amount() <= 0) {
            throw new IllegalArgumentException("Amount must be > 0, got: " + r.amount());
        }

        accRepo.findId(r.accountId()).orElseThrow(() -> new NoSuchElementException("Account not found: " + r.accountId()));

        Category cat = catRepo.findId(r.categoryId()).orElseThrow(() -> new NoSuchElementException("Category not found with ID: " + r.categoryId()));

        if (r.type() == OperationType.INCOME && cat.getType() != CategoryType.INCOME) {
            throw new IllegalStateException("Category '" + cat.getName() + "' (type: " + cat.getType() + ") is not compatible with INCOME operation");
        }
        if (r.type() == OperationType.EXPENSE && cat.getType() != CategoryType.EXPENSE) {
            throw new IllegalStateException("Category '" + cat.getName() + "' (type: " + cat.getType() + ") is not compatible with EXPENSE operation");
        }
    }

    @Override
    @SneakyThrows(Exception.class)
    protected void persist(Row r) {
        Operation op = factory.create(r.type(), r.accountId(), r.categoryId(), r.amount(), r.date(), r.description());
        opRepo.save(op);
    }
}