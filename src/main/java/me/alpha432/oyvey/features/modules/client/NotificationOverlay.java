package me.alpha432.oyvey.features.modules.client;

import me.alpha432.oyvey.event.impl.render.Render2DEvent;
import me.alpha432.oyvey.event.system.Subscribe;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import net.minecraft.client.gui.GuiGraphics;

import java.awt.*;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class NotificationOverlay extends Module {
    private static final Queue<Notification> notifications = new ConcurrentLinkedQueue<>();
    private static final int NOTIFICATION_HEIGHT = 18;
    private static final int NOTIFICATION_GAP = 2;
    private static final int FADE_DURATION = 500;

    public final Setting<Integer> displayTime = num("Display Time", 2000, 500, 10000);
    public final Setting<Integer> maxNotifications = num("Max Notifications", 5, 1, 15);

    public NotificationOverlay() {
        super("NotificationOverlay", "Shows on-screen notification toasts", Category.CLIENT);
    }

    public static void notify(String message) {
        notifications.add(new Notification(message, System.currentTimeMillis()));
    }

    @Subscribe
    public void onRender2D(Render2DEvent event) {
        if (nullCheck() || notifications.isEmpty()) return;

        long now = System.currentTimeMillis();
        Notification next;
        int rendered = 0;
        int y = 4;

        while ((next = notifications.peek()) != null) {
            if (now - next.timestamp > displayTime.getValue() + FADE_DURATION) {
                notifications.poll();
                continue;
            }
            if (rendered >= maxNotifications.getValue()) break;

            float alpha = 1.0f;
            long elapsed = now - next.timestamp;
            if (elapsed > displayTime.getValue()) {
                alpha = 1.0f - (float) (elapsed - displayTime.getValue()) / FADE_DURATION;
                alpha = Math.max(0, alpha);
            }

            drawNotification(event.getContext(), next.message, y, alpha);
            y += NOTIFICATION_HEIGHT + NOTIFICATION_GAP;
            rendered++;
        }
    }

    private void drawNotification(GuiGraphics context, String text, int y, float alpha) {
        int textWidth = mc.font.width(text);
        int x = mc.getWindow().getGuiScaledWidth() - textWidth - 8;

        Color bg = new Color(0, 0, 0, (int) (120 * alpha));
        context.fill(x, y, x + textWidth + 8, y + NOTIFICATION_HEIGHT, bg.getRGB());
        context.drawString(mc.font, text, x + 4, y + (NOTIFICATION_HEIGHT - mc.font.lineHeight) / 2,
                new Color(255, 255, 255, (int) (255 * alpha)).getRGB());
    }

    private record Notification(String message, long timestamp) {}
}
