package commands.io;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.IntStream;

@Component
public class ConsoleIO {
    private final Scanner sc = new Scanner(System.in);

    public String line(String str) {
        System.out.print(str);
        return sc.nextLine().trim();
    }

    public double readDouble(String str) {
        while (true) {
            try { System.out.print(str);
                return Double.parseDouble(sc.nextLine().trim().replace(',', '.'));
            } catch (Exception e) {
                System.out.println("Некорректное число!!! Попробуйте снова");
            }
        }
    }

    public LocalDate readDate(String str) {
        while (true) {
            try {
                System.out.print(str);
                return LocalDate.parse(sc.nextLine().trim());
            }
            catch (Exception e) {
                System.out.println("Формат: YYYY-MM-DD.");
            }
        }
    }

    public <T> void printNumbered(List<T> list, Function<T,String> toLine) {
        IntStream.range(0, list.size()).forEach(i -> System.out.printf("%d) %s%n", i + 1, toLine.apply(list.get(i))));
    }

    public int pickIndex(int size, String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                int i = Integer.parseInt(sc.nextLine().trim());
                if (i >= 1 && i <= size) return i - 1;
            }
            catch (Exception ignored) {
                System.out.println("Неверный номер.");
            }
        }
    }
}