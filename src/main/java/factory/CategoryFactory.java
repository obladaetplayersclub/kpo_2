package factory;

import domain.main.Category;
import domain.type.CategoryType;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
public class CategoryFactory {
    public Category create(CategoryType categoryType, String name) throws Exception {
        if (categoryType == null){
            throw new Exception("Категория не может быть пустой!");
        }
        if (name == null || name.isBlank()){
            throw new Exception("Имя не может быть пустым!");
        }
        UUID id = UUID.randomUUID();
        return new Category(id, categoryType, name);
    }

    public Category create(CategoryType categoryType, String name, UUID id) throws Exception {
        if (categoryType == null){
            throw new Exception("Категория не может быть пустой!");
        }
        if (name == null || name.isBlank()){
            throw new Exception("Имя не может быть пустым!");
        }
        return new Category(id, categoryType, name);
    }
}
