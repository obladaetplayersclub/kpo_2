package service;

import domain.main.Operation;
import domain.type.OperationType;
import facade.OperationFacade;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PeriodAnalytics {
    private final OperationFacade ops;
    public Result analyze(UUID accountId, LocalDate from, LocalDate to) {
        List<Operation> list = ops.listByAccountAndPeriod(accountId, from, to);
        double income = list.stream()
                .filter(o -> o.getOperationType() == OperationType.INCOME)
                .mapToDouble(Operation::getAmount).sum();
        double expense = list.stream()
                .filter(o -> o.getOperationType() == OperationType.EXPENSE)
                .mapToDouble(Operation::getAmount).sum();
        return new Result(income, expense, income - expense, list.size());
    }
    public record Result(double income, double expense, double balanceDelta, int operationsCount) { }
}