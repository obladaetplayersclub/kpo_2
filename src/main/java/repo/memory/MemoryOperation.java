package repo.memory;

import domain.main.Operation;
import org.springframework.stereotype.Repository;
import repo.OperationRepo;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class MemoryOperation implements OperationRepo {

    private final Map<UUID, Operation> storage = new ConcurrentHashMap<>();

    @Override
    public Operation save(Operation operation) {
        storage.put(operation.getId(), operation);
        return operation;
    }

    @Override
    public Optional<Operation> findId(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Operation> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public List<Operation> findAllByAccount(UUID accountId) {
        return storage.values().stream().filter(o -> o.getBAI().equals(accountId)).toList();
    }

    //getCI аббрев от getCategoryId
    @Override
    public List<Operation> findAllCat(UUID categoryId) {
        return storage.values().stream().filter(o -> o.getCI().equals(categoryId)).toList();
    }

    @Override
    public List<Operation> findAllAccPer(UUID accountId, LocalDate from, LocalDate to) {
        return storage.values().stream()
                .filter(o -> o.getBAI().equals(accountId)) //BAI это аббревиатура дляд BankAccountId
                .filter(o -> !o.getDate().isBefore(from) && !o.getDate().isAfter(to))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteId(UUID id) {
        storage.remove(id);
    }
}
