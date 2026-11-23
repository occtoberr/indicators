
package com.rooxchicken.truehealth.client;

import com.rooxchicken.truehealth.TrueHealth;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.multiplayer.PlayerInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TrueHealthOverlay implements HudElement {

    private static final int HORIZONTAL_GAP = 8;
    private static final int VERTICAL_GAP = 12;

    private record Indicator(String text, IndicatorConfig.Display display) {}

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

        List<Indicator> indicators = new ArrayList<>();

        if (config.health.enabled) {
            indicators.add(new Indicator(
                    config.health.showLabel ? "HP " + hp : hp,
                    config.health
            ));
        }

        if (config.ping.enabled) {
            indicators.add(new Indicator(
                    config.ping.showLabel ? ping + " ms" : ping,
                    config.ping
            ));
        }

        if (config.fps.enabled) {
            indicators.add(new Indicator(
                    config.fps.showLabel ? fps + " FPS" : fps,
                    config.fps
            ));
        }

        for (Indicator indicator : indicators) {
            drawIndicator(graphics, client, indicator, indicators, config);
        }
    }

    private void drawIndicator(
            GuiGraphicsExtractor graphics,
            Minecraft client,
            Indicator indicator,
            List<Indicator> indicators,
            IndicatorConfig config) {

        IndicatorConfig.Display display = indicator.display();
        IndicatorConfig.Position position = display.position;

        if (position == IndicatorConfig.Position.ABOVE_HOTBAR) {
            position = IndicatorConfig.Position.ABOVE_HEALTH;
        }

        int width = client.font.width(indicator.text());
        int screenWidth = graphics.guiWidth();
        int screenHeight = graphics.guiHeight();

        int index = 0;
        int totalWidth = 0;
        int horizontalOffset = 0;

        for (Indicator other : indicators) {
            IndicatorConfig.Position otherPosition = other.display().position;

            if (otherPosition == IndicatorConfig.Position.ABOVE_HOTBAR) {
                otherPosition = IndicatorConfig.Position.ABOVE_HEALTH;
            }

            if (otherPosition != position) {
                continue;
            }

            int otherWidth = client.font.width(other.text());

            if (other == indicator) {
                break;
            }

            index++;
            horizontalOffset += otherWidth + HORIZONTAL_GAP;
        }

        for (Indicator other : indicators) {
            IndicatorConfig.Position otherPosition = other.display().position;

            if (otherPosition == IndicatorConfig.Position.ABOVE_HOTBAR) {
                otherPosition = IndicatorConfig.Position.ABOVE_HEALTH;
            }

            if (otherPosition == position) {
                totalWidth += client.font.width(other.text()) + HORIZONTAL_GAP;
            }
        }

        totalWidth -= HORIZONTAL_GAP;

        int x;
        int y;

        boolean horizontal = config.orientation
                == IndicatorConfig.Orientation.HORIZONTAL;

        int verticalOffset = horizontal ? 0 : index * VERTICAL_GAP;
        int horizontalOffsetFinal = horizontal ? horizontalOffset : 0;

        switch (position) {
            case TOP_LEFT -> {
                x = 8 + horizontalOffsetFinal;
                y = 8 + verticalOffset;
            }

            case TOP_RIGHT -> {
                x = screenWidth - 8 - (horizontal ? totalWidth : width)
                        + horizontalOffsetFinal;
                y = 8 + verticalOffset;
            }

            case BOTTOM_LEFT -> {
                x = 8 + horizontalOffsetFinal;
                y = screenHeight - 40 - verticalOffset;
            }

            case BOTTOM_RIGHT -> {
                x = screenWidth - 8 - (horizontal ? totalWidth : width)
                        + horizontalOffsetFinal;
                y = screenHeight - 40 - verticalOffset;
            }

            case ABOVE_FOOD -> {
                x = screenWidth / 2 + 10 + horizontalOffsetFinal;
                y = screenHeight - 52 - verticalOffset;
            }

            case ABOVE_HEALTH, ABOVE_HOTBAR -> {
                x = screenWidth / 2 - 91 + horizontalOffsetFinal;
                y = screenHeight - 52 - verticalOffset;
            }

            default -> {
                x = 8;
                y = 8;
            }
        }

        graphics.text(
                client.font,
                indicator.text(),
                x,
                y,
                display.color,
                false
        );
    }
}
