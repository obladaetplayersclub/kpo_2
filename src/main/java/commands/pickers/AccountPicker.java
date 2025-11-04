package commands.pickers;

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
public class AccountPicker {
    private final BankAccountFacade accounts;
    private final ConsoleIO io;
    private final SessionContext session;

    public UUID pickAndSetActive() {
        List<BankAccount> list = accounts.getAcc();
        if (list.isEmpty()) {
            throw new IllegalStateException("Сначала создайте счет!!!");
        }
        io.printNumbered(list, a -> a.getName() + " | " + a.getBalance());
        int idx = io.pickIndex(list.size(), "Выберите номер счета: ");
        UUID id = list.get(idx).getId();
        session.setCurrentAccountId(id);
        return id;
    }
}