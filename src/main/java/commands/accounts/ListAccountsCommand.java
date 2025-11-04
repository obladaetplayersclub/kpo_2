package commands.accounts;

import domain.main.BankAccount;
import facade.BankAccountFacade;
import commands.Command;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@AllArgsConstructor
public class ListAccountsCommand implements Command {
    private final BankAccountFacade accounts;

    @Override
    public String code()  {
        return "acc:list";
    }

    @Override
    public String title() {
        return "Список счетов";
    }

    @Override
    public void execute() {
        List<BankAccount> list = accounts.getAcc();
        if (list.isEmpty()) {
            System.out.println("(нет счетов)");
            return;
        }
        for (int i= 0;i < list.size();i++) {
            var a = list.get(i);
            System.out.printf("%d) %s | %s | %.2f%n", i+1, a.getId(), a.getName(), a.getBalance());
        }
    }
}