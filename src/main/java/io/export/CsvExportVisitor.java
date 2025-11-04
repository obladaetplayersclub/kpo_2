package io.export;

import domain.main.BankAccount;
import domain.main.Category;
import domain.main.Operation;

import java.io.OutputStream;

public class CsvExportVisitor extends BaseExportVisitor {

    private boolean accountsHeaderWritten   = false;
    private boolean categoriesHeaderWritten = false;
    private boolean operationsHeaderWritten = false;

    public CsvExportVisitor(OutputStream os) {
        super(os);
    }

    @Override
    public void beginAll() {}

    @Override
    public void endAll() {}

    @Override
    public void visit(BankAccount a) {
        if (!accountsHeaderWritten) {
            writeLine("# bank_accounts: id,name,balance");
            accountsHeaderWritten = true;
        }
        writeLine(
                joinCsv(
                        a.getId().toString(),
                        quoteIfNeeded(a.getName()),
                        String.valueOf(a.getBalance())
                )
        );
    }
    @Override
    public void visit(Category c) {
        if (!categoriesHeaderWritten) {
            if (accountsHeaderWritten) writeLine("");
            writeLine("# categories: id,type,name");
            categoriesHeaderWritten = true;
        }
        writeLine(
                joinCsv(
                        c.getId().toString(),
                        c.getType().name(),
                        quoteIfNeeded(c.getName())
                )
        );
    }
    @Override
    public void visit(Operation o) {
        if (!operationsHeaderWritten) {
            if (accountsHeaderWritten || categoriesHeaderWritten) writeLine("");
            writeLine("# operations: id,accountId,categoryId,type,amount,date,description");
            operationsHeaderWritten = true;
        }
        writeLine(
                joinCsv(
                        o.getId().toString(),
                        o.getBAI().toString(),
                        o.getCI().toString(),
                        o.getOperationType().name(),
                        String.valueOf(o.getAmount()),
                        o.getDate().toString(),
                        quoteIfNeeded(o.getDescription()) // может быть null/с запятыми/кавычками
                )
        );
    }

    private String joinCsv(String... parts) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < parts.length; i++) {
            if (i > 0) sb.append(',');
            sb.append(parts[i] == null ? "" : parts[i]);
        }
        return sb.toString();
    }

    private String quoteIfNeeded(String s) {
        if (s == null) return "";
        boolean mustQuote = s.contains(",") || s.contains("\"") || s.contains("\n") || s.contains("\r");
        String escaped = s.replace("\"", "\"\"");
        return mustQuote ? ("\"" + escaped + "\"") : escaped;
    }
}