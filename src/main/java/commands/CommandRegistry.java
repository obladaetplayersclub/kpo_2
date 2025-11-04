package commands;

import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class CommandRegistry {
    private final Map<String, Command> commands = new LinkedHashMap<>();

    public void register(Command cmd) {
        if (commands.containsKey(cmd.code())) {
            throw new IllegalArgumentException("Duplicate command code: " + cmd.code());
        }
        commands.put(cmd.code(), cmd);
    }

    public Command get(String code) {
        Command c = commands.get(code);
        if (c == null) throw new IllegalArgumentException("Неизвестная команда: " + code);
        return c;
    }

    public Map<String, Command> all() {
        return Map.copyOf(commands);
    }
}
