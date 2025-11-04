package facade;

import domain.main.Category;
import domain.type.CategoryType;
import factory.CategoryFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import repo.CategoryRepo;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CategoryFacade {
    private final CategoryRepo categoryRepo;
    private final CategoryFactory categoryFactory;

    public UUID createCategory(CategoryType type, String name) throws Exception{
        if (categoryRepo.existsNameType(name, type)){
            throw new Exception("Такая категория существует");
        }
        Category category = categoryFactory.create(type, name);
        categoryRepo.save(category);
        return category.getId();
    }

    public Category getCat(UUID id) throws Exception {
        return categoryRepo.findId(id).orElseThrow(() -> new Exception("Не существует такой категории"));
    }

    public List<Category> allCategories(){
        return categoryRepo.findAll();
    }

    public List<Category> getallType(CategoryType type){
        return categoryRepo.findAllType(type);
    }

    public void deleteCategory(UUID id) throws Exception{
        categoryRepo.findId(id).orElseThrow(() -> new Exception("Не существует такой категории"));
        categoryRepo.deleteId(id);
    }

    public void editCategory(UUID id, String newName, CategoryType newType) throws Exception {
        Category old = categoryRepo.findId(id)
                .orElseThrow(() -> new Exception("Категория с таким ID не найдена"));
        if (categoryRepo.existsNameType(newName, newType)) {
            throw new Exception("Категория с таким названием и типом уже существует!");
        }
        Category updated = categoryFactory.create(newType, newName, id);
        categoryRepo.save(updated);
    }

}
