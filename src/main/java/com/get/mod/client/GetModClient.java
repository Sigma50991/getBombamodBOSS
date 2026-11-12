package com.get.mod.client;

import com.get.mod.client.render.EspRenderer;
import com.get.mod.client.render.HudRenderer;
import com.get.mod.client.render.TracerRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;

public class GetModClient implements ClientModInitializer {

	/** Ustawiane co tick przez GetKeybinds, odczytywane przez mixin FOV. */
	public static boolean zooming = false;

	// Zapamietana oryginalna wartosc gamma, zeby umiec ja przywrocic po
	// wylaczeniu fullbrighta.
	private static Double savedGamma = null;

	@Override
	public void onInitializeClient() {
		GetKeybinds.register();

		ClientTickEvents.END_CLIENT_TICK.register(GetModClient::onTick);

		WorldRenderEvents.AFTER_ENTITIES.register(context -> {
			EspRenderer.render(context);
			TracerRenderer.render(context);
		});

		HudRenderCallback.EVENT.register((drawContext, tickCounter) -> HudRenderer.render(drawContext));
	}

	private static void onTick(MinecraftClient client) {
		GetKeybinds.tick(client);

		if (client.player == null) return;

		// Autosprint: trzyma sprint zawsze wlaczony gdy gracz idzie do przodu
		if (GetConfig.autoSprintEnabled && client.player.forwardSpeed > 0) {
			client.player.setSprinting(true);
		}

		applyFullbright(client);
	}

	/**
	 * Prosty, "miekki" fullbright przez podbicie opcji gamma gracza zamiast
	 * mixowania systemu swiatla - mniej ryzykowne do skompilowania bez
	 * mozliwosci testu w tym srodowisku, kosztem tego ze nie jest to
	 * "pelny" fullbright jak przez shader/lightmap.
	 */
	private static void applyFullbright(MinecraftClient client) {
		var gammaOption = client.options.getGamma();

		if (GetConfig.fullbrightEnabled) {
			if (savedGamma == null) {
				savedGamma = gammaOption.getValue();
			}
			if (gammaOption.getValue() < 5.0) {
				gammaOption.setValue(5.0);
			}
		} else if (savedGamma != null) {
			gammaOption.setValue(savedGamma);
			savedGamma = null;
		}
	}
}
