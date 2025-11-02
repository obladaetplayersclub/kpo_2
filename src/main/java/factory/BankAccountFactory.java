package factory;

import domain.main.BankAccount;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class BankAccountFactory {
    public BankAccount create(String name, int balance) throws Exception{
        if (name == null || name.isBlank()){
            throw new Exception("Имя не может пустым");
        }
        if (balance < 0){
            throw new Exception("В нашем банке баланс не может быть отрицательным");
        }
        UUID id = UUID.randomUUID();
        return new BankAccount(name, balance, id);
    }

}
