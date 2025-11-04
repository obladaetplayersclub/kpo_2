package commands.pickers;

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
public class CategoryPicker {
    private final CategoryFacade categories;
    private final ConsoleIO io;

    public void ensureAnyCategory() {
        if (categories.allCategories().isEmpty())
            throw new IllegalStateException("Нет категорий!!! Сначала создайте хотя бы одну!!!");
    }

    public UUID pickByType(CategoryType type) {
        List<Category> list = (type == null) ? categories.allCategories() : categories.getallType(type);
        if (list.isEmpty()) {
            throw new IllegalStateException(type == null ? "Нет категорий." : "Нет категорий типа " + type);
        }
        io.printNumbered(list, c -> c.getType() + " | " + c.getName());
        int idx = io.pickIndex(list.size(), "Выберите номер категории: ");
        return list.get(idx).getId();
    }
}