package io.export;

import domain.main.BankAccount;
import domain.main.Category;
import domain.main.Operation;

import java.io.OutputStream;

public class JsonExportVisitor extends BaseExportVisitor {

    private boolean sectionAccountsStarted = false;
    private boolean sectionAccountsClosed = false;

    private boolean sectionCategoriesStarted = false;
    private boolean sectionCategoriesClosed = false;

    private boolean sectionOperationsStarted = false;

    private boolean hasAccounts = false;
    private boolean hasCategories = false;
    private boolean hasOperations = false;

    public JsonExportVisitor(OutputStream os) { super(os); }

    @Override
    public void beginAll() {
        writeRaw("{");
    }

    @Override
    public void endAll() {
        // Закрываем открытые секции
        if (sectionAccountsStarted && !sectionAccountsClosed) {
            writeRaw("]");
            sectionAccountsClosed = true;
        }

        if (!sectionCategoriesStarted) {
            writeRaw(",\"categories\":[]");
        } else if (!sectionCategoriesClosed) {
            writeRaw("]");
            sectionCategoriesClosed = true;
        }

        if (!sectionOperationsStarted) {
            writeRaw(",\"operations\":[]");
        } else {
            writeRaw("]");
        }

        writeRaw("}");
    }

    @Override
    public void visit(BankAccount a) {
        if (!sectionAccountsStarted) {
            writeRaw("\"accounts\":[");
            sectionAccountsStarted = true;
        }
        if (hasAccounts) writeRaw(",");
        writeRaw("{\"id\":\"" + a.getId() + "\",\"name\":\"" + esc(a.getName()) +
                "\",\"balance\":" + a.getBalance() + "}");
        hasAccounts = true;
    }

    @Override
    public void visit(Category c) {
        ensureAccountsClosed();
        if (!sectionCategoriesStarted) {
            writeRaw(",\"categories\":[");
            sectionCategoriesStarted = true;
        }
        if (hasCategories) writeRaw(",");
        writeRaw("{\"id\":\"" + c.getId() + "\",\"type\":\"" + c.getType() +
                "\",\"name\":\"" + esc(c.getName()) + "\"}");
        hasCategories = true;
    }

    @Override
    public void visit(Operation o) {
        ensureAccountsClosed();
        ensureCategoriesClosed();
        if (!sectionOperationsStarted) {
            writeRaw(",\"operations\":[");
            sectionOperationsStarted = true;
        }
        if (hasOperations) writeRaw(",");
        writeRaw("{\"id\":\"" + o.getId() + "\",\"accountId\":\"" + o.getBAI() +
                "\",\"categoryId\":\"" + o.getCI() +
                "\",\"type\":\"" + o.getOperationType() +
                "\",\"amount\":" + o.getAmount() +
                ",\"date\":\"" + o.getDate() +
                "\",\"description\":\"" + esc(o.getDescription()) + "\"}");
        hasOperations = true;
    }

    private void ensureAccountsClosed() {
        if (sectionAccountsStarted && !sectionAccountsClosed) {
            writeRaw("]");
            sectionAccountsClosed = true;
        } else if (!sectionAccountsStarted) {
            writeRaw("\"accounts\":[]");
            sectionAccountsClosed = true;
        }
    }

    private void ensureCategoriesClosed() {
        if (sectionCategoriesStarted && !sectionCategoriesClosed) {
            writeRaw("]");
            sectionCategoriesClosed = true;
        } else if (!sectionCategoriesStarted) {
            writeRaw(",\"categories\":[]");
            sectionCategoriesClosed = true;
        }
    }

    private String esc(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}