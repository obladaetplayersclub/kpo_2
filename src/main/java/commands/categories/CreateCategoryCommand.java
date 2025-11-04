package commands.categories;

import domain.type.CategoryType;
import facade.CategoryFacade;
import commands.Command;
import commands.io.ConsoleIO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
@AllArgsConstructor
public class CreateCategoryCommand implements Command {
    private final CategoryFacade categories;
    private final ConsoleIO io;

    @Override
    public String code()  {
        return "cat:create";
    }

    @Override
    public String title() {
        return "Создать категорию";
    }

    @Override
    public void execute() throws Exception {
        CategoryType type = readType();
        String name = io.line("Название: ");
        UUID id = categories.createCategory(type, name);
        System.out.println("Поздравляю! Категория создана: ");
    }

    private CategoryType readType() {
        while (true) {
            try {
                return CategoryType.valueOf(io.line("Тип (INCOME/EXPENSE): ").toUpperCase());
            }
            catch (Exception e) {
                System.out.println("Введите INCOME или EXPENSE.");
            }
        }
    }
}