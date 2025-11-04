package commands;

import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
public class SessionContext {
    private UUID currentAccountId;

    public UUID getCurrentAccountId() {
        return currentAccountId;
    }

    public void setCurrentAccountId(UUID id) {
        this.currentAccountId = id;
    }

    public void ensureActiveAccount() {
        if (currentAccountId == null) {
            throw new IllegalStateException("Сначала выберите/создайте счет!");
        }
    }
}