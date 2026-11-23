
package com.rooxchicken.truehealth.client;

import com.rooxchicken.truehealth.TrueHealth;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.multiplayer.PlayerInfo;

import java.util.Locale;

public class TrueHealthOverlay implements HudElement {

    private static final int GAP = 12;

    @Override
    public void extractRenderState(
            GuiGraphicsExtractor graphics,
            DeltaTracker deltaTracker) {

        Minecraft client = Minecraft.getInstance();

        if (client.player == null) {
            return;
        }

        IndicatorConfig config = IndicatorConfig.INSTANCE;

        float health = client.player.getHealth();

        if (TrueHealth.addAbsorption) {
            health += client.player.getAbsorptionAmount();
        }

        String hp = String.format(Locale.ROOT, "%.1f", health);
        String ping = "0";
        String fps = String.valueOf(client.getFps());

        if (client.getConnection() != null) {
            PlayerInfo info = client.getConnection()
                    .getPlayerInfo(client.player.getUUID());

            if (info != null) {
                ping = String.valueOf(info.getLatency());
            }
        }

        if (config.health.enabled) {
            String text = config.health.showLabel ? "HP " + hp : hp;
            drawIndicator(graphics, client, text, config.health, 0);
        }

        if (config.ping.enabled) {
            String text = config.ping.showLabel ? ping + " ms" : ping;
            drawIndicator(graphics, client, text, config.ping, 1);
        }

        if (config.fps.enabled) {
            String text = config.fps.showLabel ? fps + " FPS" : fps;
            drawIndicator(graphics, client, text, config.fps, 2);
        }
    }

    private void drawIndicator(
            GuiGraphicsExtractor graphics,
            Minecraft client,
            String text,
            IndicatorConfig.Display display,
            int index) {

        int width = client.font.width(text);
        int screenWidth = graphics.guiWidth();
        int screenHeight = graphics.guiHeight();

        int stack = getStackIndex(display, index);
        int x;
        int y;

        switch (display.position) {
            case TOP_LEFT -> {
                x = 8;
                y = 8 + stack * GAP;
            }
            case TOP_RIGHT -> {
                x = screenWidth - width - 8;
                y = 8 + stack * GAP;
            }
            case BOTTOM_LEFT -> {
                x = 8;
                y = screenHeight - 40 - stack * GAP;
            }
            case BOTTOM_RIGHT -> {
                x = screenWidth - width - 8;
                y = screenHeight - 40 - stack * GAP;
            }
            default -> {
                x = (screenWidth - width) / 2;
                y = screenHeight - 50 - stack * GAP;
            }
        }

        // No background and no text shadow.
        graphics.text(
                client.font,
                text,
                x,
                y,
                display.color,
                false
        );
    }

    private int getStackIndex(IndicatorConfig.Display display, int index) {
        IndicatorConfig config = IndicatorConfig.INSTANCE;
        int stack = 0;

        if (index > 0 && config.health.enabled
                && config.health.position == display.position) {
            stack++;
        }

        if (index > 1 && config.ping.enabled
                && config.ping.position == display.position) {
            stack++;
        }

        return stack;
    }
}
