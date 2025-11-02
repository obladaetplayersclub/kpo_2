package domain.main;

import domain.type.CategoryType;
import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
public class Category {
    private final UUID id;
    private final CategoryType categoryType;
    private final String name;

    public UUID getId(){
        return id;
    }
    public CategoryType getType(){
        return categoryType;
    }
    public String getName(){
        return name;
    }
}
