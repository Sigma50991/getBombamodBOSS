package com.get.mod.client.gui;

import com.get.mod.client.GetConfig;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class GetMenuScreen extends Screen {
	public GetMenuScreen() {
		super(Text.literal("GET - Menu"));
	}

	@Override
	protected void init() {
		int x = this.width / 2 - 100;
		int y = this.height / 2 - 90;
		int spacing = 24;

		addToggle(x, y, "ESP", () -> GetConfig.espEnabled, v -> GetConfig.espEnabled = v);
		addToggle(x, y + spacing, "Tracery", () -> GetConfig.tracersEnabled, v -> GetConfig.tracersEnabled = v);
		addToggle(x, y + spacing * 2, "Target HUD", () -> GetConfig.targetHudEnabled, v -> GetConfig.targetHudEnabled = v);
		addToggle(x, y + spacing * 3, "Watermark", () -> GetConfig.watermarkEnabled, v -> GetConfig.watermarkEnabled = v);
		addToggle(x, y + spacing * 4, "Fullbright", () -> GetConfig.fullbrightEnabled, v -> GetConfig.fullbrightEnabled = v);
		addToggle(x, y + spacing * 5, "Autosprint", () -> GetConfig.autoSprintEnabled, v -> GetConfig.autoSprintEnabled = v);

		this.addDrawableChild(ButtonWidget.builder(Text.literal("Zamknij"), b -> this.close())
				.dimensions(x, y + spacing * 6 + 10, 200, 20)
				.build());
	}

	private void addToggle(int x, int y, String label, java.util.function.BooleanSupplier getter, java.util.function.Consumer<Boolean> setter) {
		this.addDrawableChild(ButtonWidget.builder(
				Text.literal(label + ": " + (getter.getAsBoolean() ? "WLACZONE" : "WYLACZONE")),
				b -> {
					boolean newValue = !getter.getAsBoolean();
					setter.accept(newValue);
					b.setMessage(Text.literal(label + ": " + (newValue ? "WLACZONE" : "WYLACZONE")));
				}
		).dimensions(x, y, 200, 20).build());
	}

	@Override
	public boolean shouldPause() {
		return false;
	}
}
