package io.export;

import domain.main.BankAccount;
import domain.main.Category;
import domain.main.Operation;

import java.io.OutputStream;

public class YamlExportVisitor extends BaseExportVisitor {

    private boolean wroteAccountsHeader = false;
    private boolean wroteCategoriesHeader = false;
    private boolean wroteOperationsHeader = false;

    public YamlExportVisitor(OutputStream os) {
        super(os);
    }

    @Override
    public void beginAll() {}

    @Override
    public void endAll() {}

    @Override
    public void visit(BankAccount a) {
        if (!wroteAccountsHeader) {
            writeLine("accounts:");
            wroteAccountsHeader = true;
        }
        writeLine("  - id: " + a.getId());
        writeLine("    name: \"" + esc(a.getName()) + "\"");
        writeLine("    balance: " + a.getBalance());
    }

    @Override
    public void visit(Category c) {
        if (!wroteCategoriesHeader) {
            if (wroteAccountsHeader) writeLine("");
            writeLine("categories:");
            wroteCategoriesHeader = true;
        }
        writeLine("  - id: " + c.getId());
        writeLine("    type: " + c.getType().name());
        writeLine("    name: \"" + esc(c.getName()) + "\"");
    }

    @Override
    public void visit(Operation o) {
        if (!wroteOperationsHeader) {
            if (wroteAccountsHeader || wroteCategoriesHeader) writeLine("");
            writeLine("operations:");
            wroteOperationsHeader = true;
        }
        writeLine("  - id: " + o.getId());
        writeLine("    accountId: " + o.getBAI());
        writeLine("    categoryId: " + o.getCI());
        writeLine("    type: " + o.getOperationType().name());
        writeLine("    amount: " + o.getAmount());
        writeLine("    date: " + o.getDate());
        String desc = o.getDescription() == null ? "" : o.getDescription();
        writeLine("    description: \"" + esc(desc) + "\"");
    }

    private String esc(String s) {
        if (s == null) return "";
        return s.replace("\"", "\\\"");
    }
}