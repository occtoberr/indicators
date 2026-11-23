
package com.rooxchicken.truehealth.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.resources.Identifier;

public class TrueHealthClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        IndicatorConfig.load();

        HudElementRegistry.addLast(
                Identifier.fromNamespaceAndPath("indicators", "hud"),
                new TrueHealthOverlay()
        );
    }
}
