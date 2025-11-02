package facade;

import domain.main.BankAccount;
import domain.main.Category;
import domain.main.Operation;
import domain.type.CategoryType;
import domain.type.OperationType;
import factory.BankAccountFactory;
import factory.OperationFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import repo.BankAccountRepo;
import repo.CategoryRepo;
import repo.OperationRepo;

import java.time.LocalDate;
import java.util.UUID;

@Service
@AllArgsConstructor
public class OperationFacade {
    private final OperationRepo operationRepo;
    private final BankAccountRepo accountRepo;
    private final CategoryRepo categoryRepo;
    private final OperationFactory operationFactory;
    private final BankAccountFactory bankAccountFactory;

    public UUID regInc(UUID accountId, UUID categoryId, double amount, LocalDate date, String description) throws Exception{
        BankAccount bankAccount = accountRepo.findId(accountId).orElseThrow(() -> new Exception("Такого счета не существует!"));
        Category category = categoryRepo.findId(categoryId).orElseThrow(() -> new Exception("Такой категории не существует!"));
        if (category.getType() != CategoryType.INCOME){
            throw new Exception("Категория не является дохрдной");
        } else {
            Operation op = operationFactory.create(OperationType.INCOME, amount, date, description);
            operationRepo.save(op);
            workWithBalance(bankAccount, +amount);
        }
    }

    private void workWithBalance(BankAccount account, double summa){
        BankAccount update = bankAccountFactory.create(account.getName(), account.getBalance() + summa, account.getId());
    }

}
