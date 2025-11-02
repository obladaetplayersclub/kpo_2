package repo.memory;

import domain.main.Category;
import domain.type.CategoryType;
import org.springframework.stereotype.Repository;
import repo.CategoryRepo;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class MemoryCategory implements CategoryRepo {
    private final Map<UUID, Category> storage = new ConcurrentHashMap<>();

    @Override
    public Category save(Category category) {
        storage.put(category.getId(), category);
        return category;
    }

    @Override
    public Optional<Category> findId(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public Optional<Category> findNameType(String name, CategoryType type) {
        return storage.values().stream().filter(c -> c.getName().equalsIgnoreCase(name) && c.getType() == type).findFirst();
    }

    @Override
    public boolean existsNameType(String name, CategoryType type) {
        return findNameType(name, type).isPresent();
    }

    @Override
    public List<Category> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public List<Category> findAllType(CategoryType type) {
        return storage.values().stream().filter(c -> c.getType() == type).toList();
    }

    @Override
    public void deleteId(UUID id) {
        storage.remove(id);
    }
}
