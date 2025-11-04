package io.export;

import io.Format;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import repo.BankAccountRepo;
import repo.CategoryRepo;
import repo.OperationRepo;

import java.io.OutputStream;
import java.time.LocalDate;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ExportService{

    private final BankAccountRepo accRepo;
    private final CategoryRepo catRepo;
    private final OperationRepo opRepo;

    @SneakyThrows
    public void exportAll(Format fmt, OutputStream out) {
        try (ExportVisitor v = build(fmt, out)) {
            v.beginAll();
            accRepo.findAll().forEach(v::visit);
            catRepo.findAll().forEach(v::visit);
            opRepo.findAll().forEach(v::visit);
            v.endAll();
        } catch (Exception e){
            throw new Exception(e);
        }
    }

    @SneakyThrows
    public void exportAccount(Format fmt, UUID accountId, OutputStream out) {
        try (ExportVisitor v = build(fmt, out)) {
            v.beginAll();
            accRepo.findId(accountId).ifPresent(v::visit);
            catRepo.findAll().forEach(v::visit);
            opRepo.findAllByAccount(accountId).forEach(v::visit);
            v.endAll();
        }
        catch (Exception e){
            throw new Exception(e);
        }
    }

    @SneakyThrows
    public void exportAccountPeriod(Format fmt, UUID accountId, LocalDate from, LocalDate to, OutputStream out) {
        try (ExportVisitor v = build(fmt, out)) {
            v.beginAll();
            accRepo.findId(accountId).ifPresent(v::visit);
            catRepo.findAll().forEach(v::visit);
            opRepo.findAllAccPer(accountId, from, to).forEach(v::visit);
            v.endAll();
        }
        catch (Exception e){
            throw new Exception(e);
        }
    }

    @SneakyThrows
    public void exportOperation(Format fmt, UUID operationId, OutputStream out) {
        try (ExportVisitor v = build(fmt, out)) {
            v.beginAll();
            opRepo.findId(operationId).ifPresent(v::visit);
            v.endAll();
        }
        catch (Exception e){
            throw new Exception(e);
        }
    }

    private ExportVisitor build(Format fmt, OutputStream out) {
        return switch (fmt) {
            case CSV  -> new CsvExportVisitor(out);
            case JSON -> new JsonExportVisitor(out);
            case YAML -> new YamlExportVisitor(out);
        };
    }
}
