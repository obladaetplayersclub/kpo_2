package service;

import domain.main.Operation;
import domain.type.OperationType;
import facade.OperationFacade;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class TopOperationsAnalytics {

    private final OperationFacade ops;
    public Result analyze(UUID accountId) {
        List<Operation> list = ops.listAcc(accountId);
        Optional<Operation> biggestIncome = list.stream()
                .filter(o -> o.getOperationType() == OperationType.INCOME)
                .max(Comparator.comparingDouble(Operation::getAmount));
        Optional<Operation> biggestExpense = list.stream()
                .filter(o -> o.getOperationType() == OperationType.EXPENSE)
                .max(Comparator.comparingDouble(Operation::getAmount));
        return new Result(biggestIncome.orElse(null), biggestExpense.orElse(null));
    }
    public record Result(Operation biggestIncome, Operation biggestExpense) {
        public void print() {
            System.out.println("\nСамые крупные операции:");
            if (biggestIncome == null && biggestExpense == null) {
                System.out.println("(Нет операций)");
                return;
            }
            if (biggestIncome != null) {
                System.out.printf("  ➕ Доход:  %.2f | %s | %s%n",
                        biggestIncome.getAmount(),
                        biggestIncome.getDate(),
                        desc(biggestIncome.getDescription()));
            } else {
                System.out.println("  ➕ Доход:  —");
            }
            if (biggestExpense != null) {
                System.out.printf("  ➖ Расход: %.2f | %s | %s%n",
                        biggestExpense.getAmount(),
                        biggestExpense.getDate(),
                        desc(biggestExpense.getDescription()));
            } else{
                System.out.println("  ➖ Расход: —");
            }
        }
        private static String desc(String s) {
            return (s == null || s.isBlank()) ? "(без описания)" : s;
        }
    }
}