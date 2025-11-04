package commands.accounts;

import commands.Command;
import commands.SessionContext;
import commands.pickers.AccountPicker;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class SelectActiveAccountCommand implements Command {
    private final AccountPicker picker;
    private final SessionContext session;

    @Override
    public String code()  {
        return "acc:select";
    }
    @Override
    public String title() {
        return "Выбрать активный счет (по номеру)";
    }

    @Override
    public void execute() {
        var id = picker.pickAndSetActive();
        System.out.println("Активный счет на данный момент: " + id);
    }
}