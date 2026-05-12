package me.alpha432.oyvey.features.gui;

import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.util.render.RenderUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ModernOyVeyGui extends Screen {
    private static ModernOyVeyGui INSTANCE;
    
    private Module.Category selectedCategory = Module.Category.COMBAT;
    private int sidebarWidth = 180;
    private int sidebarColor = 0xFF1a1a1a;
    private int mainBgColor = 0xFF0d0d0d;
    private float scrollOffset = 0;
    private int moduleHeight = 60;
    private int moduleSpacing = 10;
    
    private List<Module> filteredModules = new ArrayList<>();
    private List<ModuleCard> moduleCards = new ArrayList<>();

    public ModernOyVeyGui() {
        super(Component.literal("QuantumMC+"));
        INSTANCE = this;
        updateFilteredModules();
    }

    public static ModernOyVeyGui getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ModernOyVeyGui();
        }
        return INSTANCE;
    }

    private void updateFilteredModules() {
        filteredModules = OyVey.moduleManager.getModulesByCategory(selectedCategory);
        filteredModules.sort(Comparator.comparing(Module::getName));
        
        moduleCards.clear();
        for (int i = 0; i < filteredModules.size(); i++) {
            moduleCards.add(new ModuleCard(filteredModules.get(i), i));
        }
    }

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        renderBackground(context);
        drawSidebar(context, mouseX, mouseY);
        drawMainContent(context, mouseX, mouseY);
    }

    private void renderBackground(GuiGraphics context) {
        context.fill(0, 0, this.width, this.height, mainBgColor);
    }

    private void drawSidebar(GuiGraphics context, int mouseX, int mouseY) {
        // Sidebar background
        context.fill(0, 0, sidebarWidth, this.height, sidebarColor);
        
        // Header
        context.fill(0, 0, sidebarWidth, 80, 0xFF000000);
        
        // QuantumMC+ branding
        int textX = sidebarWidth / 2 - this.font.width("QuantumMC+") / 2;
        context.drawString(this.font, "QuantumMC+", textX, 25, 0xFFFFFFFF);
        
        int versionX = sidebarWidth / 2 - this.font.width("v1.0.0") / 2;
        context.drawString(this.font, "v1.0.0", versionX, 40, 0xFF888888);
        
        // Divider
        context.fill(0, 80, sidebarWidth, 82, 0xFF333333);
        
        // Category list
        int y = 100;
        for (Module.Category category : Module.Category.values()) {
            if (category.name().equals("HUD")) continue;
            
            boolean isSelected = selectedCategory == category;
            int categoryColor = isSelected ? 0xFF2563eb : 0xFF555555;
            
            // Background
            if (isSelected) {
                context.fill(10, y - 5, sidebarWidth - 10, y + 20, categoryColor);
            }
            
            // Category name
            int textColor = isSelected ? 0xFFFFFFFF : 0xFF888888;
            context.drawString(this.font, category.name(), 20, y, textColor);
            
            y += 30;
        }
    }

    private void drawMainContent(GuiGraphics context, int mouseX, int mouseY) {
        int mainX = sidebarWidth;
        int mainY = 0;
        int mainWidth = this.width - sidebarWidth;
        int mainHeight = this.height;
        
        // Main background
        context.fill(mainX, mainY, mainX + mainWidth, mainY + mainHeight, mainBgColor);
        
        // Top bar
        context.fill(mainX, 0, mainX + mainWidth, 60, 0xFF1a1a1a);
        context.drawString(this.font, selectedCategory.name(), mainX + 20, 22, 0xFFFFFFFF);
        
        // Divider
        context.fill(mainX, 60, mainX + mainWidth, 62, 0xFF333333);
        
        // Module list
        int contentY = 80;
        int contentX = mainX + 20;
        int contentWidth = mainWidth - 40;
        
        for (ModuleCard card : moduleCards) {
            if (contentY + moduleHeight > mainHeight - 20) break;
            
            card.draw(context, contentX, contentY, contentWidth, moduleHeight, mouseX, mouseY, this.font);
            contentY += moduleHeight + moduleSpacing;
        }
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean consumed) {
        double mouseX = event.x();
        double mouseY = event.y();
        int button = event.buttonInfo().button();

        if (mouseX < sidebarWidth) {
            // Sidebar click
            int y = 100;
            for (Module.Category category : Module.Category.values()) {
                if (category.name().equals("HUD")) continue;

                if (mouseY > y - 5 && mouseY < y + 20) {
                    selectedCategory = category;
                    updateFilteredModules();
                    return true;
                }
                y += 30;
            }
        } else {
            // Main content click
            int contentY = 80;
            for (ModuleCard card : moduleCards) {
                if (contentY + moduleHeight > this.height - 20) break;

                int contentX = sidebarWidth + 20;
                int contentWidth = this.width - sidebarWidth - 40;

                if (card.mouseClicked((int) mouseX, (int) mouseY, contentX, contentY, contentWidth, moduleHeight)) {
                    return true;
                }
                contentY += moduleHeight + moduleSpacing;
            }
        }

        return super.mouseClicked(event, consumed);
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        if (event.key() == 256) { // ESC
            this.onClose();
            return true;
        }
        return super.keyPressed(event);
    }

    @Override
    public void onClose() {
        super.onClose();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    // Inner class for module cards
    private class ModuleCard {
        private Module module;
        private int index;

        public ModuleCard(Module module, int index) {
            this.module = module;
            this.index = index;
        }

        public void draw(GuiGraphics context, int x, int y, int width, int height, int mouseX, int mouseY, net.minecraft.client.gui.Font font) {
            boolean isHovered = mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height;
            
            // Background
            int bgColor = isHovered ? 0xFF1f1f1f : 0xFF141414;
            context.fill(x, y, x + width, y + height, bgColor);
            
            // Border
            context.fill(x, y, x + width, y + 1, 0xFF333333);
            
            // Module icon placeholder
            context.fill(x + 10, y + 10, x + 30, y + 30, 0xFF2563eb);
            
            // Module name
            context.drawString(font, module.getName(), x + 40, y + 8, 0xFFFFFFFF);
            
            // Category/description
            context.drawString(font, module.getCategory().name(), x + 40, y + 20, 0xFF888888);
            
            // Display info
            String displayInfo = module.getDisplayInfo();
            if (displayInfo != null && !displayInfo.isEmpty()) {
                context.drawString(font, displayInfo, x + 40, y + 32, 0xFF666666);
            }
            
            // Toggle button
            int toggleX = x + width - 50;
            int toggleY = y + 15;
            boolean enabled = module.isEnabled();
            int toggleColor = enabled ? 0xFF10b981 : 0xFF666666;
            
            context.fill(toggleX, toggleY, toggleX + 40, toggleY + 20, toggleColor);
            context.fill(toggleX + 3, toggleY + 3, toggleX + 37, toggleY + 17, 0xFF0d0d0d);
            
            if (enabled) {
                context.fill(toggleX + 20, toggleY + 3, toggleX + 37, toggleY + 17, toggleColor);
            } else {
                context.fill(toggleX + 3, toggleY + 3, toggleX + 20, toggleY + 17, toggleColor);
            }
        }

        public boolean mouseClicked(int mouseX, int mouseY, int x, int y, int width, int height) {
            // Toggle button area
            int toggleX = x + width - 50;
            int toggleY = y + 15;
            
            if (mouseX > toggleX && mouseX < toggleX + 40 && mouseY > toggleY && mouseY < toggleY + 20) {
                if (module.isEnabled()) {
                    module.disable();
                } else {
                    module.enable();
                }
                return true;
            }
            
            return false;
        }
    }
}
