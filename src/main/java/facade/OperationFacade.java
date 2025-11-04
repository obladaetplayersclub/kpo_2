package facade;

import domain.main.BankAccount;
import domain.main.Category;
import domain.main.Operation;
import domain.type.CategoryType;
import domain.type.OperationType;
import factory.BankAccountFactory;
import factory.OperationFactory;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import repo.BankAccountRepo;
import repo.CategoryRepo;
import repo.OperationRepo;

import java.time.LocalDate;
import java.util.List;
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
            Operation op = operationFactory.create(OperationType.INCOME, accountId, categoryId, amount, date, description);
            operationRepo.save(op);
            workWithBalance(bankAccount, +amount);
            return op.getId();
        }
    }

    public UUID regExp(UUID accountId, UUID categoryId, double amount, LocalDate date, String description) throws Exception{
        BankAccount bankAccount = accountRepo.findId(accountId).orElseThrow(() -> new Exception("Такого счета не существует!"));
        Category category = categoryRepo.findId(categoryId).orElseThrow(() -> new Exception("Такой категории не существует!"));
        if (category.getType() != CategoryType.EXPENSE){
            throw new Exception("Категория не является дохрдной");
        } else {
            Operation op = operationFactory.create(OperationType.EXPENSE, accountId, categoryId, amount, date, description);
            operationRepo.save(op);
            workWithBalance(bankAccount, -amount);
            return op.getId();
        }
    }

    @SneakyThrows
    public Operation getOp(UUID id){
        return operationRepo.findId(id).orElseThrow(() -> new Exception("тАКОГО операции не существует"));
    }

    public List<Operation> listAcc(UUID accId){
        return operationRepo.findAllByAccount(accId);
    }

    public List<Operation> listByAccountAndPeriod(UUID accountId, LocalDate from, LocalDate to) {
        return operationRepo.findAllAccPer(accountId, from, to);
    }

    @SneakyThrows
    public void deleteId(UUID id){
        Operation op = operationRepo.findId(id).orElseThrow(() -> new Exception("Нет подходящей операции"));
        BankAccount bankAccount = accountRepo.findId(op.getBAI()).orElseThrow(() -> new Exception("Ошибка, счет не найден"));
        double delta = (op.getOperationType() == OperationType.EXPENSE) ? +op.getAmount() : +op.getAmount();
        workWithBalance(bankAccount, delta);
        operationRepo.deleteId(id);
    }

    @SneakyThrows
    public void edit(UUID opId, UUID newCategoryId, double newAmount, LocalDate newDate, String newDescription) {
        Operation op = operationRepo.findId(opId).orElseThrow(() -> new Exception("Нет подходящей операции"));
        BankAccount bankAccount = accountRepo.findId(op.getBAI()).orElseThrow(() -> new Exception("Ошибка, счет не найден"));
        double delta = (op.getOperationType() == OperationType.EXPENSE) ? +op.getAmount() : +op.getAmount();
        workWithBalance(bankAccount, delta);
        Category newCategory = categoryRepo.findId(newCategoryId).orElseThrow(() -> new Exception("Такой категории не существует!"));
        OperationType newOpType = (newCategory.getType() == CategoryType.INCOME) ? OperationType.INCOME : OperationType.EXPENSE;
        try{
            Operation updated = operationFactory.create(opId, newOpType, op.getBAI(), newCategoryId, newAmount, newDate, newDescription);
            operationRepo.save(updated);
            double newDelta = (op.getOperationType() == OperationType.INCOME) ? +op.getAmount() : -op.getAmount();
            workWithBalance(bankAccount, newDelta);
        }
         catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void workWithBalance(BankAccount account, double summa){
        try {
            BankAccount update = bankAccountFactory.create(account.getName(), account.getBalance() + summa, account.getId());
            accountRepo.save(update);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
