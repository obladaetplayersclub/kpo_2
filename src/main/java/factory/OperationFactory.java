package factory;

import domain.main.Operation;
import domain.type.OperationType;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.UUID;

@Component
public class OperationFactory {
    public Operation create(OperationType operationType, double amount, LocalDate date, String description) throws Exception{
        if (amount < 0 ) {
            throw new Exception("Сумма операции должна быть больше 0");
        }
        UUID id = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        UUID bankId = UUID.randomUUID();
        return new Operation(id, operationType, bankId, amount, date, description, categoryId);
    }

    public Operation create(UUID id, OperationType type, UUID bankAccountId, UUID categoryId, double amount, LocalDate date, String description) throws Exception {
        if (amount < 0 ) {
            throw new Exception("Сумма операции должна быть больше 0");
        }
        return new Operation(id, type, bankAccountId, amount, date, description, categoryId);
    }

    public Operation create(OperationType type, UUID bankAccountId, UUID categoryId, double amount, LocalDate date, String description) throws Exception {
        if (amount <= 0){
            throw new Exception("Сумма операции должна быть больше 0");
        }
        UUID id = UUID.randomUUID();
        return new Operation(id, type, bankAccountId, amount, date, description, categoryId);
    }
}
