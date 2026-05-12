package me.alpha432.oyvey.features.modules.movement;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;

public class SafeWalkModule extends Module {
    private final Setting<Boolean> onEdges = bool("On Edges", true);

    public SafeWalkModule() {
        super("SafeWalk", "Prevents falling off edges", Category.MOVEMENT);
    }

    @Override
    public void onTick() {
        if (nullCheck() || !mc.player.onGround()) return;
        if (!onEdges.getValue()) return;

        // Simple safe walk - basic edge detection
        if ((mc.options.keyUp.isDown() || mc.options.keyDown.isDown() || 
             mc.options.keyLeft.isDown() || mc.options.keyRight.isDown())) {
            
            // Prevent movement on edges by checking void below
            if (mc.player.getY() < 1) {
                mc.player.setDeltaMovement(0, mc.player.getDeltaMovement().y(), 0);
            }
        }
    }
}
