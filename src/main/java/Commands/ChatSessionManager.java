package Commands;

import java.util.Set;
import java.util.HashSet;

public class ChatSessionManager {
    private final Set<Long> activeChats;

    public ChatSessionManager() {
        this.activeChats = new HashSet<>();
    }

    public boolean isActive(long chatId) {
        return activeChats.contains(chatId);
    }

    public void startSession(long chatId) {
        activeChats.add(chatId);
    }

    public void endSession(long chatId) {
        activeChats.remove(chatId);
    }
}

