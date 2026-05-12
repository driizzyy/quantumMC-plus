package me.alpha432.oyvey.features.modules.client;

import me.alpha432.oyvey.event.impl.ClientEvent;
import me.alpha432.oyvey.event.system.Subscribe;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import me.alpha432.oyvey.util.TextUtil;
import me.alpha432.oyvey.util.chat.ChatUtil;
import net.minecraft.network.chat.Component;

import static me.alpha432.oyvey.util.chat.SimpleSignature.from;

public class NotificationsModule extends Module {
    private static final String MODULE_FORMAT = "{gray} %s %s%s";

    public final Setting<Boolean> moduleToggle = bool("ModuleToggle", true);
    public final Setting<Boolean> toasts = bool("Toasts", true);

    public NotificationsModule() {
        super("Notifications", "Displays notifications for various client events", Category.CLIENT);
    }

    @Subscribe
    public void onClient(ClientEvent event) {
        if (!moduleToggle.getValue()
                || event.getType() != ClientEvent.Type.TOGGLE_MODULE
                || event.getFeature() instanceof ClickGuiModule) {
            return;
        }

        boolean moduleState = event.getFeature().isEnabled();
        Component message = TextUtil.text(MODULE_FORMAT,
                event.getFeature().getName(),
                moduleState ? "{green}" : "{red}",
                moduleState ? "on" : "off");

        if (toasts.getValue()) {
            NotificationOverlay.notify(message.getString());
        }
        ChatUtil.sendMessage(message, from(event.getFeature().getName()));
    }
}
