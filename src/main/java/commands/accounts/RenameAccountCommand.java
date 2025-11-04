package commands.accounts;

import commands.Command;
import commands.io.ConsoleIO;
import domain.main.BankAccount;
import facade.BankAccountFacade;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@AllArgsConstructor
public class RenameAccountCommand implements Command {
    private final BankAccountFacade accounts;
    private final ConsoleIO io;

    @Override
    public String code() {
        return "acc:rename";
    }

    @Override
    public String title() {
        return "Переименовать счет (по номеру)";
    }

    @Override
    public void execute() throws Exception {
        List<BankAccount> list = accounts.getAcc();
        if (list.isEmpty()) {
            System.out.println("(нет счетов)");
            return;
        }

        io.printNumbered(list, a -> a.getName() + " | баланс: " + a.getBalance());
        int idx = io.pickIndex(list.size(), "Выберите номер счета для переименования: ");

        String newName = io.line("Новое название счета: ").trim();
        if (newName.isBlank()) {
            System.out.println("Название пустое. Отмена.");
            return;
        }

        UUID id = list.get(idx).getId();
        accounts.renameAccount(id, newName);

        System.out.println("Название счета изменено.");
    }
}