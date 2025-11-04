package commands.imex;

import io.imp.CategoryImporter;
import io.Format; // если у тебя io.Format — поправь импорт
import commands.Command;
import commands.io.ConsoleIO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import java.io.FileInputStream;
import java.io.InputStream;

@Component
@AllArgsConstructor
public class ImportCategoriesCommand implements Command {
    private final CategoryImporter importer;
    private final ConsoleIO io;

    @Override
    public String code()  {
        return "imp:categories";
    }

    @Override
    public String title() {
        return "Импорт категорий (CSV/JSON/YAML)";
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