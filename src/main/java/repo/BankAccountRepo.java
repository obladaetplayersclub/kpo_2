package repo;

import domain.main.BankAccount;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BankAccountRepo {
    BankAccount save(BankAccount account);
    boolean exName(String name);
    void deleteId(UUID id);
    List<BankAccount> findAll();
    Optional<BankAccount> findName(String name);
    Optional<BankAccount> findId(UUID id);
}
