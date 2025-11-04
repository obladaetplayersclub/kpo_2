package commands.operations;

import commands.SessionContext;
import domain.main.Operation;
import facade.OperationFacade;
import commands.Command;
import commands.io.ConsoleIO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class DeleteOperationCommand implements Command {
    private final OperationFacade ops;
    private final ConsoleIO io;
    private final SessionContext sessionContext;

    @Override
    public String code()  {
        return "op:delete";
    }
    @Override
    public String title() {
        return "Удалить операцию";
    }

    @Override
    public void execute() throws Exception {
        sessionContext.ensureActiveAccount();
        List<Operation> list = ops.listAcc(sessionContext.getCurrentAccountId());
        if (list.isEmpty()) {
            System.out.println("(у активного счета нет операций)");
            return;
        }
        io.printNumbered(list, this::format);
        int idx = io.pickIndex(list.size(), "Выберите номер операции для удаления: ");

        // (опционально) подтверждение
        String confirm = io.line("Точно удалить? (y/N): ").trim().toLowerCase();
        if (!confirm.equals("y")) {
            System.out.println("Отмена.");
            return;
        }

        ops.deleteId(list.get(idx).getId());
        System.out.println("Операция удалена!");
    }

    private String format(Operation o) {
        return o.getOperationType() + " | " + o.getAmount() + " | " + o.getDate()
                + (o.getDescription() == null || o.getDescription().isBlank() ? "" : " | " + o.getDescription());
    }
}