package domain.main;

import domain.type.OperationType;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
public class Operation {
    private final UUID id;
    private final OperationType operationType;
    private final UUID bankId;
    private final double amount;
    private final LocalDate date;
    private final String description;
    private final UUID categoryId;

    public UUID getId(){
        return id;
    }
    public LocalDate getDate(){
        return date;
    }
    public UUID getBAI() {
        return bankId;
    }
    public UUID getCI(){
        return categoryId;
    }

}
