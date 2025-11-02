package repo;

import domain.main.Operation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OperationRepo {
    Operation save(Operation operation);
    Optional<Operation> findId(UUID id);
    List<Operation> findAll();
    List<Operation> findAllByAccount(UUID accountId);
    List<Operation> findAllCat(UUID categoryId);
    List<Operation> findAllAccPer(UUID accountId, LocalDate from, LocalDate to);
    void deleteId(UUID id);
}
