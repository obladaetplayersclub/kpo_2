package commands.categories;

import commands.Command;
import commands.io.ConsoleIO;
import domain.main.Category;
import facade.CategoryFacade;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@AllArgsConstructor
public class DeleteCategoryCommand implements Command {
    private final CategoryFacade categories;
    private final ConsoleIO io;

    @Override
    public String code() {
        return "cat:delete";
    }

    @Override
    public String title() {
        return "Удалить категорию (по номеру)";
    }

    @Override
    public void execute() throws Exception {
        List<Category> list = categories.allCategories();
        if (list.isEmpty()) {
            System.out.println("(нет категорий)");
            return;
        }

        io.printNumbered(list, c -> c.getType() + " | " + c.getName());
        int idx = io.pickIndex(list.size(), "Выберите номер категории для удаления: ");

        UUID id = list.get(idx).getId();
        String confirm = io.line("Точно удалить категорию? (y/N): ").trim().toLowerCase();
        if (!confirm.equals("y")) {
            System.out.println("Отмена удаления.");
            return;
        }

        categories.deleteCategory(id);
        System.out.println("Категория удалена!");
    }
}