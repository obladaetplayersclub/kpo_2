package org.example.kpo2;

import commands.Command;
import commands.CommandRegistry;
import commands.SessionContext;
import facade.CategoryFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.*;
import java.util.stream.Collectors;

@SpringBootApplication(scanBasePackages = {"org.example.kpo2","facade","factory","repo","io","commands","service"})
public class Kpo2Application implements CommandLineRunner {

    @Autowired private CommandRegistry registry;
    @Autowired private SessionContext session;
    @Autowired private CategoryFacade categoryFacade;

    public static void main(String[] args) {
        var app = new SpringApplication(Kpo2Application.class);
        app.setWebApplicationType(org.springframework.boot.WebApplicationType.NONE);
        app.run(args);
    }

    @Override
    public void run(String... args) {
        final Scanner sc = new Scanner(System.in);
        System.out.println("\nДобро пожаловать во ВШЭ-Банк");
        System.out.println("Меню нашего приложения:");

        Map<String, String> groups = new LinkedHashMap<>();
        groups.put("acc", "Аккаунты");
        groups.put("cat", "Категории");
        groups.put("op",  "Операции");
        groups.put("ana", "Аналитика");
        groups.put("imp", "Импорт");
        groups.put("exp", "Экспорт");

        while (true) {
            try {
                System.out.println("\nАктивный счет: " + (session.getCurrentAccountId() == null ? "—" : session.getCurrentAccountId()));
                List<String> groupKeys = new ArrayList<>(groups.keySet());
                for (int i = 0; i < groupKeys.size(); i++) {
                    System.out.printf("%d) %s%n", i + 1, groups.get(groupKeys.get(i)));
                }
                System.out.println("0) Выход");
                System.out.print(">> ");
                String in = sc.nextLine().trim();

                if ("0".equals(in)) {
                    System.out.println("Спасибо, что используете наш банк!");
                    return;
                }
                int gIdx = parseIndex(in, groupKeys.size());
                if (gIdx < 0) {
                    System.out.println("Неверный номер!");
                    continue;
                }

                String chosenGroup = groupKeys.get(gIdx);
                if (chosenGroup.equals("op") || chosenGroup.equals("ana")) {
                    requireActiveAccount();
                    requireAnyCategory();
                }

                List<Command> groupCmds = registry.all().values().stream()
                        .filter(c -> switch (chosenGroup) {
                            case "ana" -> c.code().startsWith("ana:") || c.code().startsWith("op:analytics");
                            case "op"  -> c.code().startsWith("op:") && !c.code().startsWith("op:analytics");
                            default    -> c.code().startsWith(chosenGroup + ":") || c.code().equals(chosenGroup);
                        })
                        .sorted(Comparator.comparing(Command::title, String.CASE_INSENSITIVE_ORDER))
                        .collect(Collectors.toList());

                if (groupCmds.isEmpty()) { System.out.println("(В этой группе ещё нет команд)"); continue; }

                while (true) {
                    System.out.println("\n-- " + groups.get(chosenGroup) + " --");
                    for (int i = 0; i < groupCmds.size(); i++) {
                        System.out.printf("%d) %s%n", i + 1, groupCmds.get(i).title());
                    }
                    System.out.println("0) Назад");
                    System.out.print(">> ");
                    String sel = sc.nextLine().trim();
                    if ("0".equals(sel)) break;
                    int cIdx = parseIndex(sel, groupCmds.size());
                    if (cIdx < 0) {
                        System.out.println("Неверный номер.");
                        continue;
                    }
                    Command cmd = groupCmds.get(cIdx);
                    String code = cmd.code();
                    if (code.startsWith("op:") || code.startsWith("ana:") || code.startsWith("op:analytics")) {
                        requireActiveAccount();
                        requireAnyCategory();
                    }
                    if (code.equals("exp:account") || code.equals("exp:account:period")) {
                        requireActiveAccount();
                    }
                    cmd.execute();
                }
            } catch (Exception e) {
                System.err.println("Ошибка: " + e.getMessage());
            }
        }
    }

    private int parseIndex(String s, int size) {
        try {
            int i = Integer.parseInt(s);
            return (i >= 1 && i <= size) ? (i - 1) : -1;
        } catch (Exception ignored) {
            return -1;
        }
    }

    private void requireActiveAccount() {
        session.ensureActiveAccount();
    }

    private void requireAnyCategory() {
        if (categoryFacade.allCategories().isEmpty()) {
            throw new IllegalStateException("Ошибка! Сначала создайте хотя бы одну категорию (доход/расход)!");
        }
    }
}