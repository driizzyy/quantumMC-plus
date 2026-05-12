package me.alpha432.oyvey.features.modules.player;

import me.alpha432.oyvey.event.impl.entity.player.TickEvent;
import me.alpha432.oyvey.event.system.Subscribe;
import me.alpha432.oyvey.features.modules.Module;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class AutoToolModule extends Module {
    private static final int SWAP_DELAY_TICKS = 2;
    private int lastSlot = -1;
    private int tickCounter = 0;

    public AutoToolModule() {
        super("AutoTool", "Switches to best tool when mining", Category.PLAYER);
    }

    @Subscribe
    public void onTick(TickEvent event) {
        if (nullCheck()) return;

        if (tickCounter > 0) tickCounter--;

        if (!mc.player.isUsingItem() && mc.gameMode != null && mc.hitResult != null) {
            switch (mc.hitResult.getType()) {
                case BLOCK -> {
                    var blockPos = ((net.minecraft.world.phys.BlockHitResult) mc.hitResult).getBlockPos();
                    BlockState state = mc.level.getBlockState(blockPos);
                    if (state.isAir()) return;

                    int bestSlot = findBestToolSlot(state);
                    if (bestSlot != -1 && bestSlot != mc.player.getInventory().getSelectedSlot()) {
                        if (tickCounter == 0) {
                            mc.player.getInventory().setSelectedSlot(bestSlot);
                            tickCounter = SWAP_DELAY_TICKS;
                        }
                    }
                }
            }
        }
    }

    private int findBestToolSlot(BlockState state) {
        Inventory inv = mc.player.getInventory();
        int bestSlot = -1;
        float bestSpeed = 1.0f;

        for (int i = 0; i < 9; i++) {
            ItemStack stack = inv.getItem(i);
            if (stack.isEmpty()) continue;

            float speed = stack.getDestroySpeed(state);
            if (speed > bestSpeed) {
                bestSpeed = speed;
                bestSlot = i;
            }
        }
        return bestSlot;
    }
}
