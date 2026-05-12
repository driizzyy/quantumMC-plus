package me.alpha432.oyvey.features.modules.player;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import me.alpha432.oyvey.util.inventory.InventoryUtil;
import me.alpha432.oyvey.util.inventory.Result;
import net.minecraft.world.item.Items;

public class AutoRepairModule extends Module {
    private final Setting<Float> threshold = num("Durability Threshold", 80.0f, 0.0f, 100.0f);

    public AutoRepairModule() {
        super("AutoRepair", "Automatically repairs items at anvil or mending", Category.PLAYER);
    }

    @Override
    public void onTick() {
        if (nullCheck()) return;

        if (mc.player.getMainHandItem().isDamaged()) {
            float durability = getDurabilityPercent(mc.player.getMainHandItem());
            if (durability < threshold.getValue()) {
                // Move to repair slot if near repair station
                // This is a simplified version - full implementation would need to track repair stations
            }
        }
    }

    private float getDurabilityPercent(net.minecraft.world.item.ItemStack item) {
        int maxDamage = item.getMaxDamage();
        if (maxDamage == 0) return 100f;
        return (float) (maxDamage - item.getDamageValue()) / maxDamage * 100f;
    }

    @Override
    public String getDisplayInfo() {
        return String.format("%.0f%%", threshold.getValue());
    }
}
