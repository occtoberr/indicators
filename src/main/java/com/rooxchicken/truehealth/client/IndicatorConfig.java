
package com.rooxchicken.truehealth.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Files;
import java.nio.file.Path;

public class IndicatorConfig {

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    private static final Path FILE = FabricLoader.getInstance()
            .getConfigDir()
            .resolve("indicators.json");

    public static IndicatorConfig INSTANCE = new IndicatorConfig();

    public Display health = new Display(true, 0xFFFF5555, Position.ABOVE_HOTBAR, false);
    public Display ping = new Display(true, 0xFF55FF55, Position.ABOVE_HOTBAR, false);
    public Display fps = new Display(true, 0xFFAAAAAA, Position.ABOVE_HOTBAR, false);

    public static class Display {
        public boolean enabled;
        public int color;
        public Position position;
        public boolean showLabel;

        public Display(boolean enabled, int color, Position position, boolean showLabel) {
            this.enabled = enabled;
            this.color = color;
            this.position = position;
            this.showLabel = showLabel;
        }
    }

    public enum Position {
        TOP_LEFT("Top Left"),
        TOP_RIGHT("Top Right"),
        BOTTOM_LEFT("Bottom Left"),
        BOTTOM_RIGHT("Bottom Right"),
        ABOVE_HOTBAR("Above Hotbar");

        public final String displayName;

        Position(String displayName) {
            this.displayName = displayName;
        }

        @Override
        public String toString() {
            return displayName;
        }
    }

    public static void load() {
        try {
            if (Files.exists(FILE)) {
                IndicatorConfig loaded = GSON.fromJson(
                        Files.readString(FILE),
                        IndicatorConfig.class
                );

                if (loaded != null) {
                    INSTANCE = loaded;
                }
            }
        } catch (Exception e) {
            System.err.println("Could not load Indicators config: " + e);
        }

        if (INSTANCE.health == null) {
            INSTANCE.health = new Display(true, 0xFFFF5555, Position.ABOVE_HOTBAR, false);
        }
        if (INSTANCE.ping == null) {
            INSTANCE.ping = new Display(true, 0xFF55FF55, Position.ABOVE_HOTBAR, false);
        }
        if (INSTANCE.fps == null) {
            INSTANCE.fps = new Display(true, 0xFFAAAAAA, Position.ABOVE_HOTBAR, false);
        }
    }

    public static void save() {
        try {
            Files.createDirectories(FILE.getParent());
            Files.writeString(FILE, GSON.toJson(INSTANCE));
        } catch (Exception e) {
            System.err.println("Could not save Indicators config: " + e);
        }
    }
}
