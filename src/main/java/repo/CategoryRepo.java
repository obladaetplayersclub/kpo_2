package repo;

import domain.main.Category;
import domain.type.CategoryType;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepo {
    Category save(Category category);
    Optional<Category> findId(UUID id);
    Optional<Category> findNameType(String name, CategoryType type);
    boolean existsNameType(String name, CategoryType type);
    List<Category> findAll();
    List<Category> findAllType(CategoryType type);
    void deleteId(UUID id);
}
