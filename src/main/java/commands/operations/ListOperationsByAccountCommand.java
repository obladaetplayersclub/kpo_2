package commands.operations;

import commands.Command;
import commands.SessionContext;
import domain.main.Operation;
import facade.OperationFacade;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class ListOperationsByAccountCommand implements Command {
    private final OperationFacade ops;
    private final SessionContext session;

    @Override public String code() {
        return "op:list:account";
    }

    @Override public String title() {
        return "Список операций активного счета";
    }

    @Override
    public void execute() {
        session.ensureActiveAccount();
        List<Operation> list = ops.listAcc(session.getCurrentAccountId());
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