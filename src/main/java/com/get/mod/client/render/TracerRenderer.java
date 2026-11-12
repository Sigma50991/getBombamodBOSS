package com.get.mod.client.render;

import com.get.mod.client.GetConfig;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

public final class TracerRenderer {
	private TracerRenderer() {}

	public static void render(WorldRenderContext context) {
		if (!GetConfig.tracersEnabled) return;

		MinecraftClient client = MinecraftClient.getInstance();
		if (client.world == null || client.player == null) return;

		Vec3d camPos = context.camera().getPos();
		MatrixStack matrices = context.matrixStack();
		if (matrices == null) return;

		VertexConsumerProvider.Immediate immediate =
				client.getBufferBuilders().getEntityVertexConsumers();
		VertexConsumer consumer = immediate.getBuffer(RenderLayer.getLines());

		float tickDelta = context.tickCounter().getTickDelta(true);

		matrices.push();
		var matrix = matrices.peek().getPositionMatrix();

		for (Entity entity : client.world.getEntities()) {
			if (entity == client.player) continue;
			if (!(entity instanceof PlayerEntity)) continue;

			Vec3d targetPos = entity.getLerpedPos(tickDelta)
					.add(0, entity.getHeight() / 2.0, 0)
					.subtract(camPos);

			consumer.vertex(matrix, 0, 0, 0).color(1.0f, 0.25f, 0.25f, 0.8f).normal(0, 1, 0);
			consumer.vertex(matrix, (float) targetPos.x, (float) targetPos.y, (float) targetPos.z)
					.color(1.0f, 0.25f, 0.25f, 0.8f).normal(0, 1, 0);
		}

		matrices.pop();
		immediate.draw();
	}
}
