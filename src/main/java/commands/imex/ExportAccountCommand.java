package commands.imex;

import commands.SessionContext;
import io.Format;
import io.export.ExportService;
import commands.Command;
import commands.io.ConsoleIO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.UUID;

@Component
@AllArgsConstructor
public class ExportAccountCommand implements Command {
    private final ExportService export;
    private final ConsoleIO io;
    private final SessionContext sessionContext;

    @Override
    public String code()  {
        return "exp:account";
    }

    @Override
    public String title() {
        return "Экспорт по счёту (CSV/JSON/YAML)";
    }

    @Override
    public void execute() throws Exception {
        Format fmt = readFormat();
        UUID accId = sessionContext.getCurrentAccountId();
        String path = io.line("Куда сохранить файл: ");
        try (OutputStream out = new FileOutputStream(path)) { export.exportAccount(fmt, accId, out); }
        System.out.println("Поздравляю! Экспорт завершен: " + path);
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