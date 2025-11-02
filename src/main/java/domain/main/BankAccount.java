package domain.main;
import java.util.UUID;

public class BankAccount {
    private final String name;
    private final int balance;
    private final UUID id;

    public BankAccount(String name, int balance, UUID id){
        this.name = name;
        this.balance = balance;
        this.id = id;
    }
}
