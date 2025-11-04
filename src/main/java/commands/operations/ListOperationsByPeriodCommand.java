package commands.operations;

import commands.Command;
import commands.SessionContext;
import commands.io.ConsoleIO;
import domain.main.Operation;
import facade.OperationFacade;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@AllArgsConstructor
public class ListOperationsByPeriodCommand implements Command {
    private final OperationFacade ops;
    private final ConsoleIO io;
    private final SessionContext session;

    @Override public String code() {
        return "op:list:period";
    }

    @Override public String title() {
        return "Список операций активного счета за период";
    }

    @Override
    public void execute() {
        session.ensureActiveAccount();
        LocalDate from = io.readDate("С (YYYY-MM-DD): ");
        LocalDate to = io.readDate("По (YYYY-MM-DD): ");
        List<Operation> list = ops.listByAccountAndPeriod(session.getCurrentAccountId(), from, to);
        if (list.isEmpty()) {
            System.out.println("(нет операций)");
            return;
        }
        list.forEach(o -> System.out.println(format(o)));
    }

    private String format(Operation o) {
        return o.getOperationType() + " | " + o.getAmount() + " | " + o.getDate()
                + " | acc=" + o.getBAI()
                + " | cat=" + o.getCI()
                + (o.getDescription() == null ? "" : " | " + o.getDescription())
                + " | id=" + o.getId();
    }
}