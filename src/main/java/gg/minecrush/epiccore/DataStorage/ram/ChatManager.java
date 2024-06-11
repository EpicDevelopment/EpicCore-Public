package gg.minecrush.epiccore.DataStorage.ram;

public class ChatManager {

    private boolean chatMuted;

    public ChatManager() {
        this.chatMuted = false;
    }

    public boolean isChatMuted() {
        return chatMuted;
    }

    public void setChatMuted(boolean chatMuted) {
        this.chatMuted = chatMuted;
    }
}
