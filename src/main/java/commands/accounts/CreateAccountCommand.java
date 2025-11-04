// src/main/java/commands/accounts/CreateAccountCommand.java
package commands.accounts;

import commands.Command;
import commands.SessionContext;
import commands.io.ConsoleIO;
import facade.BankAccountFacade;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class CreateAccountCommand implements Command {
    private final BankAccountFacade accounts;
    private final ConsoleIO io;
    private final SessionContext session;

    @Override
    public String code()  {
        return "acc:create";
    }

    @Override
    public String title() {
        return "Создать счет (сделать активным)";
    }

    @Override
    public void execute() throws Exception {
        String name = io.line("Имя счета: ");
        double bal = io.readDouble("Начальный баланс: ");
        UUID id = accounts.createAcc(name, bal);
        session.setCurrentAccountId(id);
        System.out.println("Счет создан и выбран активным: " + id);
    }
}