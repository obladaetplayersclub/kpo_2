package commands;

public interface Command {
    String code();
    String title();
    void execute() throws Exception;
}