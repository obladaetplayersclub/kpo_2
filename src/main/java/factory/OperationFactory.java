package factory;

import domain.main.Operation;
import domain.type.OperationType;

import java.time.LocalDate;
import java.util.UUID;

public class OperationFactory {
    public Operation create(OperationType operationType, int bankId, double amount, LocalDate date, String description, int categoryId) throws Exception{
        if (amount < 0 ) {
            throw new Exception("Сумма операции должна быть больше 0");
        }
        UUID id = UUID.randomUUID();
        return new Operation(id, operationType, bankId, amount, date, description, categoryId);
    }
}
