package facade;

import domain.main.BankAccount;
import factory.BankAccountFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import repo.BankAccountRepo;
import repo.OperationRepo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class BankAccountFacade {
    private final BankAccountRepo bankAccountRepo;
    private final OperationRepo operationRepo;
    private final BankAccountFactory bankAccountFactory;


    public UUID createAcc(String name, double balance) throws Exception{
        if (bankAccountRepo.exName(name)) {
            throw new Exception("Счет с таким именем существует");
        }
        BankAccount bankAccount = bankAccountFactory.create(name, balance);
        bankAccountRepo.save(bankAccount);
        return bankAccount.getId();
    }

    public List<BankAccount> getAcc(){
        return bankAccountRepo.findAll();
    }

    public void deleteAccount(UUID id) throws Exception {
        boolean hasOperations = !operationRepo.findAllByAccount(id).isEmpty();
        if (hasOperations) {
            throw new Exception("Удаление невозможно, тк сущ-ют операции");
        }
        bankAccountRepo.deleteId(id);
    }

    public double getBalance(UUID id) throws Exception {
        Optional<BankAccount> optionalAccount = bankAccountRepo.findId(id);
        if (optionalAccount.isEmpty()) {
            throw new Exception("Такого счета не существует!");
        }
        BankAccount account = optionalAccount.get();
        return account.getBalance();
    }
}
