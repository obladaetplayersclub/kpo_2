package domain.main;

import domain.type.CategoryType;
import java.util.UUID;

public class Category {
    private final UUID id;
    private final CategoryType categoryType;
    private final String name;

    public Category(UUID id, CategoryType categoryType, String name){
        this.id = id;
        this.categoryType = categoryType;
        this.name = name;
    }
}
