package me.alpha432.oyvey.features.modules.hud;

import me.alpha432.oyvey.event.impl.render.Render2DEvent;
import me.alpha432.oyvey.features.modules.client.HudModule;
import me.alpha432.oyvey.features.settings.Setting;
import me.alpha432.oyvey.util.TextUtil;

public class StatusHudModule extends HudModule {
    public final Setting<Boolean> effects = bool("Effects", true);
    public final Setting<Boolean> health = bool("Health", true);
    public final Setting<Boolean> hunger = bool("Hunger", true);

    public StatusHudModule() {
        super("Status", "Display player status", 150, 30);
    }

    @Override
    protected void render(Render2DEvent e) {
        super.render(e);

        if (nullCheck()) return;

        StringBuilder sb = new StringBuilder();
        
        if (health.getValue()) {
            sb.append("{global}Health: {red}").append((int) mc.player.getHealth()).append("/20\n");
        }
        
        if (hunger.getValue()) {
            sb.append("{global}Hunger: {yellow}").append(mc.player.getFoodData().getFoodLevel()).append("/20");
        }

        net.minecraft.network.chat.Component text = TextUtil.text(sb.toString());
        e.getContext().drawString(mc.font, text, (int) getX(), (int) getY(), -1);

        setWidth(150);
        setHeight(mc.font.lineHeight * 2);
    }
}
