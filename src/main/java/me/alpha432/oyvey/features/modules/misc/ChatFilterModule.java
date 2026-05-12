package me.alpha432.oyvey.features.modules.misc;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;

public class ChatFilterModule extends Module {
    private final Setting<Boolean> own = bool("Filter Own", false);
    private final Setting<Boolean> color = bool("Filter Colors", true);

    public ChatFilterModule() {
        super("ChatFilter", "Filters chat messages", Category.MISC);
    }

    @Override
    public void onTick() {
        // This module would work with chat events to filter messages
        // Implementation depends on ClientChatReceivedEvent availability
    }
}
