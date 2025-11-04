package commands.imex;

import commands.SessionContext;
import domain.main.Operation;
import facade.OperationFacade;
import io.Format;
import io.export.ExportService;
import commands.Command;
import commands.io.ConsoleIO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.UUID;

@Component
@AllArgsConstructor
public class ExportOperationCommand implements Command {
    private final ExportService export;
    private final ConsoleIO io;
    private final OperationFacade ops;
    private final SessionContext sessionContext;

    @Override
    public String code()  {
        return "exp:operation";
    }

    @Override
    public String title() {
        return "Экспорт одной операции";
    }

    @Override
    public void execute() throws Exception {
        Format fmt = readFormat();
        List<Operation> list = ops.listAcc(sessionContext.getCurrentAccountId());
        if (list.isEmpty()) {
            System.out.println("(У активного счета нет операций)");
            return;
        }
        io.printNumbered(list, this::format);
        int idx = io.pickIndex(list.size(), "Выберите номер операции: ");
        UUID opId = list.get(idx).getId();
        String path = io.line("Куда сохранить файл: ");
        try (OutputStream out = new FileOutputStream(path)) {
            export.exportOperation(fmt, opId, out);
        }
        System.out.println("Поздравляю! Экспорт завершен: " + path);
    }

    private String format(Operation o) {
        return o.getOperationType() + " | " + o.getAmount() + " | " + o.getDate()
                + (o.getDescription() == null ? "" : " | " + o.getDescription())
                + " | id=" + o.getId();
    }

    private Format readFormat() {
        while (true) {
            try {
                return Format.valueOf(io.line("Формат (CSV/JSON/YAML): ").toUpperCase());
            }
            catch (Exception e) {
                System.out.println("Введите CSV, JSON или YAML.");
            }
        }
    }
}