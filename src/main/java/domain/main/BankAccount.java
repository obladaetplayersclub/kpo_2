package domain.main;

public class BankAccount {
    private final String name;
    private final int balance;
    private final int id;

    public BankAccount(String name, int balance, int id){
        this.name = name;
        this.balance = balance;
        this.id = id;
    }
}
