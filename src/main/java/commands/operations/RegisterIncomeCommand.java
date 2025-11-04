// src/main/java/commands/operations/RegisterIncomeCommand.java
package commands.operations;

import commands.Command;
import commands.SessionContext;
import commands.io.ConsoleIO;
import commands.pickers.CategoryPicker;
import domain.type.CategoryType;
import facade.OperationFacade;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.UUID;

@Component
@AllArgsConstructor
public class RegisterIncomeCommand implements Command {
    private final OperationFacade ops;
    private final ConsoleIO io;
    private final SessionContext session;
    private final CategoryPicker catPicker;

    @Override
    public String code() {
        return "op:income";
    }
    @Override public String title() {
        return "Доход (+)";
    }

    @Override
    public void execute() throws Exception {
        session.ensureActiveAccount();
        catPicker.ensureAnyCategory();
        UUID catId = catPicker.pickByType(CategoryType.INCOME);
        double amt = io.readDouble("Сумма доходов: ");
        LocalDate d = io.readDate("Дата (YYYY-MM-DD): ");
        String desc = io.line("Описание (можно оставить пустым): ");
        UUID id = ops.regInc(session.getCurrentAccountId(), catId, amt, d, desc.isBlank() ? null : desc);
        System.out.println("Операция создана: " + id);
    }
}