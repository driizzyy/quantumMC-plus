package me.alpha432.oyvey.features.gui.items.buttons;


import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.gui.OyVeyGui;
import me.alpha432.oyvey.features.modules.client.ClickGuiModule;
import me.alpha432.oyvey.features.settings.Setting;
import me.alpha432.oyvey.util.render.RenderUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvents;

public class BooleanButton
        extends Button {
    private final Setting<Boolean> setting;

    public BooleanButton(Setting<Boolean> setting) {
        super(setting.getName());
        this.setting = setting;
        this.width = 15;
    }

    @Override
    public void drawScreen(GuiGraphics context, int mouseX, int mouseY, float partialTicks) {
        boolean hovering = this.isHovering(mouseX, mouseY);
        
        // Draw background with state indication
        int bgColor = this.getState() ? 
            (hovering ? OyVey.colorManager.getColorWithAlpha(y, ClickGuiModule.getInstance().topColor.getValue().getAlpha()) :
             OyVey.colorManager.getColorWithAlpha(y, ClickGuiModule.getInstance().color.getValue().getAlpha())) :
            (hovering ? -2007673515 : 0x22555555);
        
        RenderUtil.rect(context, this.x, this.y, this.x + (float) this.width + 7.4f, this.y + (float) this.height - 0.5f, bgColor);
        
        // Draw enabled indicator (small circle)
        if (this.getState()) {
            context.fill((int)this.x + this.width + 2, (int)this.y + 2, (int)this.x + this.width + 5, (int)this.y + 5, 0xFF00FF00);
        }
        
        // Draw border on hover
        if (hovering) {
            context.fill((int)this.x, (int)this.y, (int)this.x + 1, (int)this.y + this.height, 0xFF333333);
        }
        
        drawString(this.getName(), this.x + 2.3f, this.y - 1.7f - (float) OyVeyGui.getClickGui().getTextOffset(), this.getState() ? -1 : -5592406);
    }

    @Override
    public void update() {
        this.setHidden(!this.setting.isVisible());
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.isHovering(mouseX, mouseY)) {
            mc.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1f));
        }
    }

    @Override
    public void toggle() {
        this.setting.setValue(!this.setting.getValue());
    }

    @Override
    public boolean getState() {
        return this.setting.getValue();
    }
}