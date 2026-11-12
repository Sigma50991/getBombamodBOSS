package com.get.mod.mixin.client;

import com.get.mod.client.GetConfig;
import com.get.mod.client.GetModClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/**
 * UWAGA (mapping-sensitive): ta klasa hookuje metode liczaca FOV gracza, zeby
 * zaimplementowac zoom bez mixowania calego renderera. Nazwa metody i jej
 * sygnatura w Yarn moga sie nieznacznie roznic pomiedzy buildami mapowan dla
 * 1.21.11 - jesli projekt nie skompiluje sie na "getFov", otworz
 * GameRenderer.class w swoim IDE (po imporcie projektu) i popraw target na
 * faktyczna nazwe/opis metody pokazana przez Twoje IDE.
 *
 * Fullbright NIE jest tu implementowany przez mixin (zbyt ryzykowne bez
 * mozliwosci kompilacji/testu w tym srodowisku) - patrz GetModClient, gdzie
 * jest prostszy, bezpieczniejszy odpowiednik przez ustawienie gamma w
 * opcjach gracza.
 */
@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

	@ModifyVariable(method = "getFov", at = @At("RETURN"))
	private double get$modifyFov(double fov, Camera camera, double tickDelta, boolean changingFov) {
		if (GetModClient.zooming) {
			return GetConfig.ZOOM_FOV;
		}
		return fov;
	}
}
