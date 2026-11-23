
package com.rooxchicken.truehealth.event;

import org.lwjgl.glfw.GLFW;

import com.rooxchicken.truehealth.TrueHealth;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import com.mojang.blaze3d.platform.InputConstants;

public class KeyInputHandler {

	public static final String KEY_CATEGORY_TRUEHEALTH = "key.category.truehealth";
	public static final String KEY_TOGGLE_TRUEHEALTH = "key.toggle_true_health";
	public static final String KEY_TOGGLE_TRUESATURATION = "key.toggle_true_saturation";

	private static final KeyMapping.Category CATEGORY =
			KeyMapping.Category.register(
					Identifier.fromNamespaceAndPath("indicators", "general")
			);

	public static KeyMapping toggleHealthKey;
	public static KeyMapping toggleSaturationKey;

	public static void registerKeyInputs() {
		ClientTickEvents.END_CLIENT_TICK.register(client -> {

			while (toggleHealthKey.consumeClick()) {
				TrueHealth.renderTrueHealth = !TrueHealth.renderTrueHealth;
			}

			while (toggleSaturationKey.consumeClick()) {
				TrueHealth.renderTrueSaturation = !TrueHealth.renderTrueSaturation;
			}

		});
	}

	public static void register() {

		toggleHealthKey = KeyMappingHelper.registerKeyMapping(
				new KeyMapping(
						KEY_TOGGLE_TRUEHEALTH,
						InputConstants.Type.KEYSYM,
						GLFW.GLFW_KEY_H,
						CATEGORY
				)
		);

		toggleSaturationKey = KeyMappingHelper.registerKeyMapping(
				new KeyMapping(
						KEY_TOGGLE_TRUESATURATION,
						InputConstants.Type.KEYSYM,
						GLFW.GLFW_KEY_J,
						CATEGORY
				)
		);

		registerKeyInputs();
	}
}
