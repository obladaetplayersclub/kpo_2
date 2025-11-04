package io.export;

import domain.main.BankAccount;
import domain.main.Category;
import domain.main.Operation;

public interface ExportVisitor extends AutoCloseable {
    void beginAll();
    void endAll();

    void visit(BankAccount a);
    void visit(Category c);
    void visit(Operation o);
}
