package commands.imex;

import io.imp.OperationImporter;
import io.Format;
import commands.Command;
import commands.io.ConsoleIO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import java.io.FileInputStream;
import java.io.InputStream;

@Component
@AllArgsConstructor
public class ImportOperationsCommand implements Command {
    private final OperationImporter importer;
    private final ConsoleIO io;

    @Override
    public String code()  {
        return "imp:operations";
    }

    @Override
    public String title() {
        return "Импорт операций (CSV/JSON/YAML)";
    }

    @Override
    public void execute() throws Exception {
        Format fmt = readFormat();
        String path = io.line("Путь к файлу: ");
        try (InputStream in = new FileInputStream(path)) {
            importer.run(in, fmt);
        }
        System.out.println("Поздравляю! Импорт категорий завершен");
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