package me.alpha432.oyvey.features.modules.movement;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;

public class SpeedModule extends Module {
    private final Setting<Float> multiplier = num("Speed", 1.2f, 1.0f, 3.0f);

    public SpeedModule() {
        super("Speed", "Increases movement speed", Category.MOVEMENT);
    }

    @Override
    public void onTick() {
        if (nullCheck() || !mc.player.onGround()) return;
        
        if (mc.options.keyUp.isDown() || mc.options.keyDown.isDown() || 
            mc.options.keyLeft.isDown() || mc.options.keyRight.isDown()) {
            
            double speed = 0.05 * (multiplier.getValue() - 1.0f);
            double yaw = Math.toRadians(mc.player.getYRot());
            
            double motionX = 0;
            double motionZ = 0;
            
            if (mc.options.keyUp.isDown()) {
                motionX -= Math.sin(yaw) * speed;
                motionZ += Math.cos(yaw) * speed;
            }
            if (mc.options.keyDown.isDown()) {
                motionX += Math.sin(yaw) * speed;
                motionZ -= Math.cos(yaw) * speed;
            }
            if (mc.options.keyLeft.isDown()) {
                motionX += Math.cos(yaw) * speed;
                motionZ += Math.sin(yaw) * speed;
            }
            if (mc.options.keyRight.isDown()) {
                motionX -= Math.cos(yaw) * speed;
                motionZ -= Math.sin(yaw) * speed;
            }
            
            mc.player.setDeltaMovement(mc.player.getDeltaMovement().x() + motionX, 
                                      mc.player.getDeltaMovement().y(), 
                                      mc.player.getDeltaMovement().z() + motionZ);
        }
    }

    @Override
    public String getDisplayInfo() {
        return String.format("%.1fx", multiplier.getValue());
    }
}
