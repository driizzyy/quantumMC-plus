package me.alpha432.oyvey.features.modules.render;

import me.alpha432.oyvey.event.impl.render.Render3DEvent;
import me.alpha432.oyvey.event.system.Subscribe;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import me.alpha432.oyvey.util.render.RenderUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;

import java.awt.*;

public class TracersModule extends Module {
    public final Setting<Color> color = color("Color", 255, 255, 0, 200);
    public final Setting<Float> lineWidth = num("Line Width", 1.0f, 0.1f, 3.0f);
    public final Setting<Boolean> toFeet = bool("To Feet", false);

    public TracersModule() {
        super("Tracers", "Draws lines to entities", Category.RENDER);
    }

    @Subscribe
    public void onRender3D(Render3DEvent event) {
        if (nullCheck()) return;

        for (Entity entity : mc.level.getEntities(mc.player, mc.player.getBoundingBox().inflate(100))) {
            if (entity == mc.player || !(entity instanceof Player)) continue;

            AABB box = entity.getBoundingBox();
            double toX = toFeet.getValue() ? box.getCenter().x() : box.getCenter().x();
            double toY = toFeet.getValue() ? box.minY : box.getCenter().y();
            double toZ = toFeet.getValue() ? box.getCenter().z() : box.getCenter().z();

            // Draw line using box rendering as alternative
            RenderUtil.drawBox(event.getMatrix(),
                    new net.minecraft.world.phys.AABB(toX - 0.05, toY - 0.05, toZ - 0.05,
                                                      toX + 0.05, toY + 0.05, toZ + 0.05),
                    color.getValue(), lineWidth.getValue());
        }
    }
}
