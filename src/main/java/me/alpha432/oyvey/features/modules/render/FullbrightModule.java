package me.alpha432.oyvey.features.modules.render;

import me.alpha432.oyvey.event.impl.entity.player.TickEvent;
import me.alpha432.oyvey.event.system.Subscribe;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;

public class FullbrightModule extends Module {
    public final Setting<Double> brightness = num("Brightness", 1.0, 0.0, 1.0);
    private double previousBrightness = -1.0;

    public FullbrightModule() {
        super("Fullbright", "Makes the world brighter", Category.RENDER);
    }

    @Override
    public void onEnable() {
        previousBrightness = mc.options.gamma().get();
        mc.options.gamma().set(brightness.getValue());
    }

    @Override
    public void onDisable() {
        if (previousBrightness >= 0.0 && previousBrightness <= 1.0) {
            mc.options.gamma().set(previousBrightness);
        }
        previousBrightness = -1.0;
    }

    @Subscribe
    public void onTick(TickEvent event) {
        mc.options.gamma().set(brightness.getValue());
    }
}
