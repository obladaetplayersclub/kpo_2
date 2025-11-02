package repo.memory;

import domain.main.BankAccount;
import org.springframework.stereotype.Repository;
import repo.BankAccountRepo;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class MemoryBank implements BankAccountRepo {
    private final Map<UUID, BankAccount> storage = new ConcurrentHashMap<>();
    @Override
    public BankAccount save(BankAccount account) {
        storage.put(account.getId(), account);
        return account;
    }

    @Override
    public Optional<BankAccount> findId(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public Optional<BankAccount> findName(String name) {
        return storage.values().stream().filter(acc -> acc.getName().equalsIgnoreCase(name)).findFirst();
    }

    @Override
    public boolean exName(String name) {
        return findName(name).isPresent();
    }

    @Override
    public List<BankAccount> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void deleteId(UUID id) {
        storage.remove(id);
    }
}
