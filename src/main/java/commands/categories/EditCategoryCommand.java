package commands.categories;

import commands.Command;
import commands.io.ConsoleIO;
import domain.main.Category;
import domain.type.CategoryType;
import facade.CategoryFacade;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@AllArgsConstructor
public class EditCategoryCommand implements Command {
    private final CategoryFacade categories;
    private final ConsoleIO io;

    @Override
    public String code() {
        return "cat:edit";
    }
    @Override
    public String title() {
        return "Изменить категорию";
    }

    @Override
    public void execute() throws Exception {
        List<Category> list = categories.allCategories();
        if (list.isEmpty()) {
            System.out.println("(нет категорий)");
            return;
        }
        io.printNumbered(list, c -> c.getType() + " | " + c.getName());
        int idx = io.pickIndex(list.size(), "Выберите номер категории для изменения: ");
        UUID id = list.get(idx).getId();
        String newName = io.line("Введите новое название категории: ").trim();
        if (newName.isBlank()) {
            System.out.println("Название не может быть пустым.");
            return;
        }

        CategoryType newType = readCategoryType();
        categories.editCategory(id, newName, newType);
        System.out.println("Категория изменена!");
    }

    private CategoryType readCategoryType() {
        while (true) {
            String s = io.line("Введите тип (INCOME/EXPENSE): ").trim().toUpperCase();
            try {
                return CategoryType.valueOf(s);
            } catch (Exception e) {
                System.out.println("Некорректный тип. Введите INCOME или EXPENSE.");
            }
        }
    }
}