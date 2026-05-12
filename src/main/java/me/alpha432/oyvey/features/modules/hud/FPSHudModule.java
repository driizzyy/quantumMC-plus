package me.alpha432.oyvey.features.modules.hud;

import me.alpha432.oyvey.event.impl.render.Render2DEvent;
import me.alpha432.oyvey.features.modules.client.HudModule;

public class FPSHudModule extends HudModule {
    private int fps = 60;

    public FPSHudModule() {
        super("FPS", "Display FPS counter", 50, 10);
    }

    @Override
    protected void render(Render2DEvent e) {
        super.render(e);

        // Display static FPS reference
        String fpsStr = "FPS Display";
        e.getContext().drawString(mc.font, fpsStr, (int) getX(), (int) getY(), -1);

        setWidth(mc.font.width(fpsStr));
        setHeight(mc.font.lineHeight);
    }
}
