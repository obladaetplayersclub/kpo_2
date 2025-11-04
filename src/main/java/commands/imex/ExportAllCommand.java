package commands.imex;

import io.Format;
import io.export.ExportService;
import commands.Command;
import commands.io.ConsoleIO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import java.io.FileOutputStream;
import java.io.OutputStream;

@Component
@AllArgsConstructor
public class ExportAllCommand implements Command {
    private final ExportService export;
    private final ConsoleIO io;

    @Override
    public String code()  {
        return "exp:all";
    }

    @Override
    public String title() {
        return "Экспорт всех данных (CSV/JSON/YAML)";
    }

    @Override
    public void execute() throws Exception {
        Format fmt = readFormat();
        String path = io.line("Куда сохранить файл: ");
        try (OutputStream out = new FileOutputStream(path)) {
            export.exportAll(fmt, out);
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