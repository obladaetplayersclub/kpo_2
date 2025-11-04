package commands;

import commands.decorators.TimingCommandDecorator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class CommandsConfig {
    private final CommandRegistry registry;
    private final List<Command> commands;
    @PostConstruct
    void init() {
        for (Command raw : commands) {
            registry.register(new TimingCommandDecorator(raw));
        }
        System.out.printf("Зарегистрировано %d команд%n", commands.size());
    }
}