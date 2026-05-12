package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class AuraModule extends Module {
    private final Setting<Float> range = num("Range", 4.0f, 1.0f, 10.0f);
    private final Setting<Boolean> players = bool("Target Players", true);
    private final Setting<Boolean> mobs = bool("Target Mobs", true);
    private final Setting<Boolean> animals = bool("Target Animals", false);

    public AuraModule() {
        super("Aura", "Automatically attacks entities in range", Category.COMBAT);
    }

    @Override
    public void onTick() {
        if (nullCheck()) return;

        double closestDist = Double.MAX_VALUE;
        Entity closest = null;

        for (Entity entity : mc.level.getEntities(mc.player, mc.player.getBoundingBox().inflate(range.getValue()))) {
            if (!(entity instanceof LivingEntity) || entity == mc.player || entity instanceof Player && !players.getValue()) continue;
            if (entity instanceof Player) {
                if (!players.getValue()) continue;
            } else if (mc.player.isAlliedTo(entity)) {
                if (!animals.getValue()) continue;
            } else {
                if (!mobs.getValue()) continue;
            }

            double dist = mc.player.distanceTo(entity);
            if (dist < closestDist) {
                closestDist = dist;
                closest = entity;
            }
        }

        if (closest != null && closestDist <= range.getValue()) {
            mc.player.attack(closest);
            mc.player.swing(net.minecraft.world.InteractionHand.MAIN_HAND);
        }
    }

    @Override
    public String getDisplayInfo() {
        return String.format("%.1f", range.getValue());
    }
}
