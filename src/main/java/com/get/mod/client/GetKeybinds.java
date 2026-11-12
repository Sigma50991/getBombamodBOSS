package com.get.mod.client;

import com.get.mod.client.gui.GetMenuScreen;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class GetKeybinds {
	public static KeyBinding openMenuKey;
	public static KeyBinding zoomKey;

	public static void register() {
		openMenuKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key.get.menu",
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_J,
				"key.categories.get"
		));

		zoomKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key.get.zoom",
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_C,
				"key.categories.get"
		));
	}

	/** Wywolywane co tick klienta (patrz GetModClient). */
	public static void tick(MinecraftClient client) {
		if (openMenuKey.wasPressed()) {
			if (client.currentScreen == null) {
				client.setScreen(new GetMenuScreen());
			}
		}

		GetModClient.zooming = zoomKey.isPressed();
	}
}
