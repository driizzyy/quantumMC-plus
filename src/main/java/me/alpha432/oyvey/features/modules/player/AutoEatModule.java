package me.alpha432.oyvey.features.modules.player;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import me.alpha432.oyvey.util.inventory.InventoryUtil;
import me.alpha432.oyvey.util.inventory.Result;
import net.minecraft.world.item.Items;

public class AutoEatModule extends Module {
    private final Setting<Float> threshold = num("Hunger Threshold", 6.0f, 0.0f, 10.0f);
    private final Setting<Boolean> preferFood = bool("Prefer Golden Food", true);

    public AutoEatModule() {
        super("AutoEat", "Automatically eats when hungry", Category.PLAYER);
    }

    @Override
    public void onTick() {
        if (nullCheck()) return;

        if (mc.player.getFoodData().getFoodLevel() > threshold.getValue()) return;

        Result result = findFood();
        if (result.found()) {
            InventoryUtil.withSwap(result, () -> mc.gameMode.useItem(mc.player, result.hand()));
        }
    }

    private Result findFood() {
        if (preferFood.getValue()) {
            Result result = InventoryUtil.find(Items.GOLDEN_CARROT, InventoryUtil.FULL_SCOPE);
            if (result.found()) return result;
            
            result = InventoryUtil.find(Items.GLOW_BERRIES, InventoryUtil.FULL_SCOPE);
            if (result.found()) return result;
        }
        
        Result result = InventoryUtil.find(Items.BREAD, InventoryUtil.FULL_SCOPE);
        if (result.found()) return result;
        
        return InventoryUtil.find(Items.COOKED_BEEF, InventoryUtil.FULL_SCOPE);
    }

    @Override
    public String getDisplayInfo() {
        return String.format("%.1f", threshold.getValue());
    }
}
