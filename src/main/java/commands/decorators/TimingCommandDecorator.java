package commands.decorators;

import commands.Command;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TimingCommandDecorator implements Command {
    private final Command target;

    @Override
    public String code()  {
        return target.code();
    }

    @Override
    public String title() {
        return target.title();
    }

    @Override
    public void execute() throws Exception {
        long t0 = System.nanoTime();
        try {
            target.execute();
        }
        finally {
            long t1 = System.nanoTime();
            System.out.printf("[TIMING] %s: %.3f ms%n", code(), (t1 - t0)/1_000_000.0);
        }
    }
}