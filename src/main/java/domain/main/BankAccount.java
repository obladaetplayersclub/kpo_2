package domain.main;
import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
public class BankAccount {
    private final String name;
    private final double balance;
    private final UUID id;

    public UUID getId(){
        return id;
    }
    public String getName(){
        return name;
    }
    public double getBalance(){
        return balance;
    }
}
