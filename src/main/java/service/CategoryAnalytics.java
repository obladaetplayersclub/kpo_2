package service;

import domain.main.Category;
import domain.main.Operation;
import domain.type.CategoryType;
import facade.CategoryFacade;
import facade.OperationFacade;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class CategoryAnalytics {

    private final OperationFacade ops;
    private final CategoryFacade cats;

    public Result groupByCategory(UUID accountId) {
        List<Operation> allOps = ops.listAcc(accountId);
        List<Category> allCats = cats.allCategories();
        Map<Category, Double> incomeMap = new LinkedHashMap<>();
        Map<Category, Double> expenseMap = new LinkedHashMap<>();
        for (Operation o : allOps) {
            Category cat = allCats.stream().filter(c -> c.getId().equals(o.getCI())).findFirst().orElse(null);
            if (cat == null) continue;
            if (cat.getType() == CategoryType.INCOME)
                incomeMap.merge(cat, o.getAmount(), Double::sum);
            else
                expenseMap.merge(cat, o.getAmount(), Double::sum);
        }
        return new Result(incomeMap, expenseMap);
    }

    public record Result(Map<Category, Double> incomeByCat, Map<Category, Double> expenseByCat) {
        public void print() {
            System.out.println("\n— Доходы по категориям —");
            if (incomeByCat.isEmpty()) {
                System.out.println("(нет)");
            }
            else {
                incomeByCat.forEach((c, s) -> System.out.printf("%-20s %.2f%n", c.getName(), s));
            }
            System.out.println("\n— Расходы по категориям —");
            if (expenseByCat.isEmpty()){
                System.out.println("(нет)");
            }
            else {
                expenseByCat.forEach((c, s) -> System.out.printf("%-20s %.2f%n", c.getName(), s));
            }
        }
    }
}