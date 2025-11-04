package commands.analytics;

import commands.Command;
import commands.SessionContext;
import commands.io.ConsoleIO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import service.CategoryAnalytics;
import service.PeriodAnalytics;
import service.TopOperationsAnalytics;

import java.time.LocalDate;

@Component
@AllArgsConstructor
public class FullAnalyticsCommand implements Command {

    private final SessionContext session;
    private final ConsoleIO io;
    private final PeriodAnalytics period;
    private final CategoryAnalytics category;
    private final TopOperationsAnalytics top;


    @Override
    public String code() {
        return "op:analytics:full";
    }
    @Override public String title() {
        return "Полная аналитика (период, категории, топ операции)";
    }

    @Override
    public void execute() throws Exception {
        session.ensureActiveAccount();
        LocalDate from = io.readDate("Дата from (YYYY-MM-DD): ");
        LocalDate to = io.readDate("Дата to (YYYY-MM-DD): ");
        var p = period.analyze(session.getCurrentAccountId(), from, to);
        System.out.printf("\nАналитика за период %s — %s:%n", from, to);
        System.out.printf("  Доходы:  %.2f%n", p.income());
        System.out.printf("  Расходы: %.2f%n", p.expense());
        System.out.printf("  Разница: %.2f%n", p.balanceDelta());
        var c = category.groupByCategory(session.getCurrentAccountId());
        c.print();
        var t = top.analyze(session.getCurrentAccountId());
        t.print();
    }
}