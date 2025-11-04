package commands.accounts;

import commands.Command;
import commands.SessionContext;
import commands.io.ConsoleIO;
import domain.main.BankAccount;
import facade.BankAccountFacade;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@AllArgsConstructor
public class DeleteAccountCommand implements Command {
    private final BankAccountFacade accounts;
    private final ConsoleIO io;
    private final SessionContext session;

    @Override
    public String code() {
        return "acc:delete";
    }
    @Override
    public String title() {
        return "Удалить счет (по номеру)";
    }

    @Override
    public void execute() throws Exception {
        List<BankAccount> list = accounts.getAcc();
        if (list.isEmpty()) {
            System.out.println("нет счетов");
            return;
        }
        io.printNumbered(list, a -> a.getName() + " | баланс: " + a.getBalance());
        int idx = io.pickIndex(list.size(), "Выберите номер счета для удаления: ");
        String confirm = io.line("Точно удалить? (y/N): ").trim().toLowerCase();
        if (!confirm.equals("y")) {
            System.out.println("Отмена!");
            return;
        }
        UUID id = list.get(idx).getId();
        accounts.deleteAccount(id);
        if (id.equals(session.getCurrentAccountId())) {
            session.clearCurrentAccount();
        }
        System.out.println("Счет удален!");
    }
}