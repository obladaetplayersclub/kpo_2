package commands.operations;

import commands.SessionContext;
import facade.OperationFacade;
import commands.Command;
import commands.io.ConsoleIO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.UUID;

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
        UUID id = sessionContext.getCurrentAccountId();
        ops.deleteId(id);
        System.out.println("Операция удалена");
    }
}