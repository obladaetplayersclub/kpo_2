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
import java.time.LocalDate;
import java.util.UUID;

@Component
@AllArgsConstructor
public class ExportAccountPeriodCommand implements Command {
    private final ExportService export;
    private final ConsoleIO io;
    private final SessionContext sessionContext;

    @Override public String code()  {
        return "exp:account:period";
    }

    @Override public String title() {
        return "Экспорт счета за период";
    }

    @Override
    public void execute() throws Exception {
        Format fmt = readFormat();
        UUID accId = sessionContext.getCurrentAccountId();
        LocalDate from = io.readDate("From (YYYY-MM-DD): ");
        LocalDate to = io.readDate("To (YYYY-MM-DD): ");
        String path = io.line("Куда сохранить файл: ");
        try (OutputStream out = new FileOutputStream(path)) {
            export.exportAccountPeriod(fmt, accId, from, to, out);
        }
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