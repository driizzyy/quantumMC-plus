package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import me.alpha432.oyvey.util.inventory.InventoryUtil;
import me.alpha432.oyvey.util.inventory.Result;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Items;

public class AutoTotemModule extends Module {
    private final Setting<Float> threshold = num("Health Threshold", 8f, 1f, 20f);

    public AutoTotemModule() {
        super("AutoTotem", "Automatically equips totem when health is low", Category.COMBAT);
    }

    @Override
    public void onTick() {
        if (nullCheck()) return;

        float health = mc.player.getHealth() + mc.player.getAbsorptionAmount();
        if (health > threshold.getValue()) return;

        Result result = InventoryUtil.find(Items.TOTEM_OF_UNDYING, InventoryUtil.FULL_SCOPE);
        if (result.found()) {
            InventoryUtil.withSwap(result, () -> {
                mc.player.getInventory().setItem(45, mc.player.getInventory().getItem(result.slot()));
                mc.player.getInventory().setItem(result.slot(), net.minecraft.world.item.ItemStack.EMPTY);
            });
        }
    }

    @Override
    public String getDisplayInfo() {
        return String.format("%.1f", threshold.getValue());
    }
}
