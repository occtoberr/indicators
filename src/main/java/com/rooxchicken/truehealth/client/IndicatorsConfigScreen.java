
package com.rooxchicken.truehealth.client;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public class IndicatorsConfigScreen extends Screen {

    private final Screen parent;

    private static final int[] COLORS = {
            0xFFFF5555,
            0xFF55FF55,
            0xFFAAAAAA,
            0xFFFFFF55,
            0xFF55FFFF,
            0xFFFF55FF,
            0xFFFFFFFF,
            0xFFFFAA00
    };

    private static final String[] COLOR_NAMES = {
            "Red", "Green", "Gray", "Yellow",
            "Cyan", "Pink", "White", "Orange"
    };

    public IndicatorsConfigScreen(Screen parent) {
        super(Component.literal("Indicators Settings"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int startX = this.width / 2 - 225;
        int startY = 55;
        int rowGap = 38;

        addDisplayRow("HP", IndicatorConfig.INSTANCE.health, startX, startY);
        addDisplayRow("Ping", IndicatorConfig.INSTANCE.ping, startX, startY + rowGap);
        addDisplayRow("FPS", IndicatorConfig.INSTANCE.fps, startX, startY + rowGap * 2);

        addRenderableWidget(
                Button.builder(Component.literal("Done"), button -> onClose())
                        .bounds(this.width / 2 - 100, this.height - 35, 200, 20)
                        .build()
        );
    }

    private void addDisplayRow(
            String name,
            IndicatorConfig.Display display,
            int x,
            int y) {

        int buttonX = x + 65;

        // Enable / disable
        addRenderableWidget(
                Button.builder(
                        enabledText(display),
                        button -> {
                            display.enabled = !display.enabled;
                            button.setMessage(enabledText(display));
                            IndicatorConfig.save();
                        }
                ).bounds(buttonX, y, 75, 20).build()
        );

        buttonX += 80;

        // Position
        addRenderableWidget(
                Button.builder(
                        positionText(display),
                        button -> {
                            IndicatorConfig.Position[] positions =
                                    IndicatorConfig.Position.values();

                            int next = (display.position.ordinal() + 1) % positions.length;
                            display.position = positions[next];

                            button.setMessage(positionText(display));
                            IndicatorConfig.save();
                        }
                ).bounds(buttonX, y, 130, 20).build()
        );

        buttonX += 135;

        // Color
        addRenderableWidget(
                Button.builder(
                        colorText(display),
                        button -> {
                            int current = 0;

                            for (int i = 0; i < COLORS.length; i++) {
                                if (COLORS[i] == display.color) {
                                    current = i;
                                    break;
                                }
                            }

                            display.color = COLORS[(current + 1) % COLORS.length];

                            button.setMessage(colorText(display));
                            IndicatorConfig.save();
                        }
                ).bounds(buttonX, y, 85, 20).build()
        );

        buttonX += 90;

        // Labels
        addRenderableWidget(
                Button.builder(
                        labelText(display),
                        button -> {
                            display.showLabel = !display.showLabel;
                            button.setMessage(labelText(display));
                            IndicatorConfig.save();
                        }
                ).bounds(buttonX, y, 90, 20).build()
        );

        // Row title
        // Drawn in extractRenderState below.
    }

    private Component enabledText(IndicatorConfig.Display display) {
        return Component.literal(display.enabled ? "Enabled" : "Disabled");
    }

    private Component positionText(IndicatorConfig.Display display) {
        return Component.literal(display.position.displayName);
    }

    private Component colorText(IndicatorConfig.Display display) {
        for (int i = 0; i < COLORS.length; i++) {
            if (COLORS[i] == display.color) {
                return Component.literal(COLOR_NAMES[i]);
            }
        }

        return Component.literal("Custom");
    }

    private Component labelText(IndicatorConfig.Display display) {
        return Component.literal(display.showLabel ? "Label: ON" : "Label: OFF");
    }

    @Override
    public void extractRenderState(
            GuiGraphicsExtractor graphics,
            int mouseX,
            int mouseY,
            float delta) {

        super.extractRenderState(graphics, mouseX, mouseY, delta);

        graphics.centeredText(
                this.font,
                this.title,
                this.width / 2,
                20,
                0xFFFFFFFF
        );

        int x = this.width / 2 - 225;
        int y = 61;

        graphics.text(this.font, "HP", x, y, 0xFFFF5555, false);
        graphics.text(this.font, "Ping", x, y + 38, 0xFF55FF55, false);
        graphics.text(this.font, "FPS", x, y + 76, 0xFFAAAAAA, false);

        graphics.centeredText(
                this.font,
                Component.literal("Click the buttons to cycle through settings"),
                this.width / 2,
                this.height - 55,
                0xFFAAAAAA
        );
    }

    @Override
    public void onClose() {
        IndicatorConfig.save();
        this.minecraft.gui.setScreen(parent);
    }
}
