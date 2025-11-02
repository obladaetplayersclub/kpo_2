package domain.main;

import domain.type.OperationType;
import java.time.LocalDate;
import java.util.UUID;

public class Operation {
    private final UUID id;
    private final OperationType operationType;
    private final int bankId;
    private final double amount;
    private final LocalDate date;
    private final String description;
    private final int categoryId;

    public Operation(UUID id, OperationType operationType, int bankId, double amount, LocalDate date, String description, int categoryId){
        this.id = id;
        this.operationType = operationType;
        this.bankId = bankId;
        this.amount = amount;
        this.date= date;
        this.description = description;
        this.categoryId = categoryId;
    }

}
