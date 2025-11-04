package commands.operations;

import commands.Command;
import commands.SessionContext;
import commands.io.ConsoleIO;
import commands.pickers.CategoryPicker;
import domain.main.Operation;
import domain.type.CategoryType;
import facade.OperationFacade;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Component
@AllArgsConstructor
public class EditOperationCommand implements Command {
    private final OperationFacade ops;
    private final ConsoleIO io;
    private final SessionContext session;
    private final CategoryPicker categoryPicker;

    @Override
    public String code() {
        return "op:edit";
    }

    @Override
    public String title() {
        return "Редактировать операцию (по номеру)";
    }

    @Override
    public void execute() throws Exception {
        session.ensureActiveAccount();
        List<Operation> list = ops.listAcc(session.getCurrentAccountId());
        if (list.isEmpty()) {
            System.out.println("(нет операций)");
            return;
        }
        io.printNumbered(list, this::format);
        int idx = io.pickIndex(list.size(), "Выберите номер операции: ");
        Operation op = list.get(idx);
        CategoryType need = (op.getOperationType().name().equals("INCOME")) ? CategoryType.INCOME : CategoryType.EXPENSE;
        UUID newCat = categoryPicker.pickByType(need);
        double newAmount = io.readDouble("Новая сумма: ");
        LocalDate newDate = io.readDate("Новая дата (YYYY-MM-DD): ");
        String newDesc = io.line("Новое описание (можно оставить пустым): ");
        ops.edit(op.getId(), newCat, newAmount, newDate, newDesc.isBlank() ? null : newDesc);
        System.out.println("Операция обновлена!");
    }

    private String format(Operation o) {
        return o.getOperationType() + " | " + o.getAmount() + " | " + o.getDate()
                + " | acc=" + o.getBAI()
                + " | cat=" + o.getCI()
                + (o.getDescription() == null ? "" : " | " + o.getDescription())
                + " | id=" + o.getId();
    }
}