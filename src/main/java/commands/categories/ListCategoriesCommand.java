package commands.categories;

import domain.main.Category;
import facade.CategoryFacade;
import commands.Command;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@AllArgsConstructor
public class ListCategoriesCommand implements Command {
    private final CategoryFacade categories;

    @Override
    public String code()  {
        return "cat:list";
    }

    @Override
    public String title() {
        return "Список категорий";
    }

    @Override
    public void execute() {
        List<Category> list = categories.allCategories();
        if (list.isEmpty()) {
            System.out.println("(нет категорий)");
            return;
        }
        for (int i = 0;i < list.size(); i++) {
            var c = list.get(i);
            System.out.printf("%d) %s | %s | %s%n", i+1, c.getId(), c.getType(), c.getName());
        }
    }
}
