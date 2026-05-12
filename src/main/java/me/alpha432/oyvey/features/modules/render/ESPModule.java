package me.alpha432.oyvey.features.modules.render;

import me.alpha432.oyvey.event.impl.render.Render3DEvent;
import me.alpha432.oyvey.event.system.Subscribe;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import me.alpha432.oyvey.util.render.RenderUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;

import java.awt.*;

public class ESPModule extends Module {
    public final Setting<Color> playerColor = color("Player Color", 255, 0, 0, 100);
    public final Setting<Color> mobColor = color("Mob Color", 0, 255, 0, 100);
    public final Setting<Float> lineWidth = num("Line Width", 1.0f, 0.1f, 5.0f);
    public final Setting<Boolean> players = bool("Players", true);
    public final Setting<Boolean> mobs = bool("Mobs", true);

    public ESPModule() {
        super("ESP", "Shows boxes around entities", Category.RENDER);
    }

    @Subscribe
    public void onRender3D(Render3DEvent event) {
        if (nullCheck()) return;

        for (Entity entity : mc.level.getEntities(mc.player, mc.player.getBoundingBox().inflate(100))) {
            if (entity == mc.player || !(entity instanceof LivingEntity)) continue;

            if (entity instanceof Player && players.getValue()) {
                AABB aabb = entity.getBoundingBox();
                RenderUtil.drawBox(event.getMatrix(), aabb, playerColor.getValue(), lineWidth.getValue());
            } else if (!(entity instanceof Player) && mobs.getValue()) {
                AABB aabb = entity.getBoundingBox();
                RenderUtil.drawBox(event.getMatrix(), aabb, mobColor.getValue(), lineWidth.getValue());
            }
        }
    }
}
