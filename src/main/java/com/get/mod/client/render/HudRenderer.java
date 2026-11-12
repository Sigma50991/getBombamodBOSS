package com.get.mod.client.render;

import com.get.mod.client.GetConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

/**
 * Rysuje watermark "GET" w rogu ekranu oraz panel target-hud z danymi
 * najblizszego gracza (nick, dystans, hp) - wszystko lokalnie, bez
 * wysylania czegokolwiek na zewnatrz.
 */
public final class HudRenderer {
	private HudRenderer() {}

	public static void render(DrawContext context) {
		MinecraftClient client = MinecraftClient.getInstance();
		if (client.player == null) return;

		if (GetConfig.watermarkEnabled) {
			renderWatermark(context, client);
		}
		if (GetConfig.targetHudEnabled) {
			renderTargetHud(context, client);
		}
	}

	private static void renderWatermark(DrawContext context, MinecraftClient client) {
		String text = "GET";
		context.drawTextWithShadow(client.textRenderer, Text.literal(text), 6, 6, 0xFFFFFF);
	}

	private static void renderTargetHud(DrawContext context, MinecraftClient client) {
		if (client.world == null || client.player == null) return;

		PlayerEntity nearest = null;
		double bestDist = Double.MAX_VALUE;
		Vec3d selfPos = client.player.getPos();

		for (PlayerEntity p : client.world.getPlayers()) {
			if (p == client.player) continue;
			double dist = p.getPos().squaredDistanceTo(selfPos);
			if (dist < bestDist) {
				bestDist = dist;
				nearest = p;
			}
		}

		if (nearest == null) return;

		int screenW = context.getScaledWindowWidth();
		int x = screenW - 150;
		int y = 10;

		context.fill(x - 4, y - 4, x + 144, y + 48, 0x80000000);
		context.drawTextWithShadow(client.textRenderer, Text.literal(nearest.getName().getString()), x, y, 0xFFFFFF);
		context.drawTextWithShadow(client.textRenderer,
				Text.literal(String.format("HP: %.1f / %.1f", nearest.getHealth(), nearest.getMaxHealth())),
				x, y + 12, 0xFF5555);
		context.drawTextWithShadow(client.textRenderer,
				Text.literal(String.format("Dystans: %.1fm", Math.sqrt(bestDist))),
				x, y + 24, 0xAAAAAA);
	}
}
