package domain.main;

import domain.type.CategoryType;

public class Category {
    private final int id;
    private final CategoryType categoryType;
    private final String name;

    public Category(int id, CategoryType categoryType, String name){
        this.id = id;
        this.categoryType = categoryType;
        this.name = name;
    }
}
