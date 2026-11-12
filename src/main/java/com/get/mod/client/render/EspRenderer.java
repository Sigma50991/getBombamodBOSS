package com.get.mod.client.render;

import com.get.mod.client.GetConfig;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

/**
 * Rysuje obrysy (ESP boxy) wokol innych graczy/zywych bytow, widoczne przez
 * sciany (nie uzywa depth test). Czysto wizualne - nie zbiera ani nie wysyla
 * nigdzie zadnych danych (brak IP/HWID/trzecich serwerow).
 */
public final class EspRenderer {
	private EspRenderer() {}

	public static void render(WorldRenderContext context) {
		if (!GetConfig.espEnabled) return;

		MinecraftClient client = MinecraftClient.getInstance();
		if (client.world == null || client.player == null) return;

		MatrixStack matrices = context.matrixStack();
		if (matrices == null) return;

		Vec3d camPos = context.camera().getPos();

		VertexConsumerProvider.Immediate immediate =
				client.getBufferBuilders().getEntityVertexConsumers();

		float tickDelta = context.tickCounter().getTickDelta(true);

		for (Entity entity : client.world.getEntities()) {
			if (entity == client.player) continue;
			if (!(entity instanceof PlayerEntity) && !(entity instanceof LivingEntity)) continue;
			if (entity.isInvisible()) continue;

			float[] color = colorFor(entity);

			Box box = entity.getBoundingBox();
			// Interpolacja pozycji miedzy tickami, zeby box nie "skakal"
			double x = net.minecraft.util.math.MathHelper.lerp(tickDelta, entity.lastRenderX, entity.getX()) - entity.getX();
			double y = net.minecraft.util.math.MathHelper.lerp(tickDelta, entity.lastRenderY, entity.getY()) - entity.getY();
			double z = net.minecraft.util.math.MathHelper.lerp(tickDelta, entity.lastRenderZ, entity.getZ()) - entity.getZ();
			Box interpolated = box.offset(x, y, z).offset(-camPos.x, -camPos.y, -camPos.z);

			matrices.push();
			VertexConsumer consumer = immediate.getBuffer(RenderLayer.getLines());
			WorldRenderer.drawBox(
					matrices, consumer,
					interpolated.minX, interpolated.minY, interpolated.minZ,
					interpolated.maxX, interpolated.maxY, interpolated.maxZ,
					color[0], color[1], color[2], 1.0f
			);
			matrices.pop();
		}

		immediate.draw();
	}

	private static float[] colorFor(Entity entity) {
		if (entity instanceof PlayerEntity) {
			return new float[]{1.0f, 0.25f, 0.25f}; // czerwony - gracz
		}
		return new float[]{1.0f, 0.85f, 0.2f}; // zolty - inny mob zywy
	}
}
