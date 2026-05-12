package me.alpha432.oyvey.features.modules.movement;

import me.alpha432.oyvey.event.impl.entity.player.TickEvent;
import me.alpha432.oyvey.event.system.Subscribe;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;

public class SprintModule extends Module {
    public final Setting<Boolean> multiDir = bool("MultiDirectional", true);

    public SprintModule() {
        super("Sprint", "Automatically sprints", Category.MOVEMENT);
    }

    @Subscribe
    public void onTick(TickEvent event) {
        if (nullCheck()) return;
        if (mc.player.horizontalCollision || mc.player.isSprinting()) return;

        var keys = mc.player.input.keyPresses;
        if (multiDir.getValue()) {
            if (keys.forward() || keys.backward() || keys.left() || keys.right()) {
                mc.player.setSprinting(true);
            }
        } else if (keys.forward()) {
            mc.player.setSprinting(true);
        }
    }
}
