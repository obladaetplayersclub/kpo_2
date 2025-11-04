package commands.operations;

import commands.Command;
import commands.SessionContext;
import commands.io.ConsoleIO;
import commands.pickers.CategoryPicker;
import domain.type.CategoryType;
import facade.OperationFacade;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
@AllArgsConstructor
public class RegisterExpenseCommand implements Command {
    private final OperationFacade ops;
    private final ConsoleIO io;
    private final SessionContext session;
    private final CategoryPicker catPicker;

    @Override
    public String code() {
        return "op:expense";
    }

    @Override
    public String title() {
        return "Расход (-)";
    }

    @Override
    public void execute() throws Exception {
        session.ensureActiveAccount();
        catPicker.ensureAnyCategory();
        UUID catId = catPicker.pickByType(CategoryType.EXPENSE);
        double amt = io.readDouble("Сумма расходов: ");
        var d = io.readDate("Дата (YYYY-MM-DD): ");
        String desc = io.line("Описание (можно оставить пустым пусто): ");
        UUID id = ops.regExp(session.getCurrentAccountId(), catId, amt, d, desc.isBlank() ? null : desc);
        System.out.println("Операция создана: " + id);
    }
}