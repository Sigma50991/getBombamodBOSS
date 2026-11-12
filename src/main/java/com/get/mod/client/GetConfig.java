package com.get.mod.client;

/**
 * Prosty, trzymany w pamieci config (reset po restarcie gry).
 * Jesli chcesz zapis do pliku, dopisz tu serializacje np. przez Gson
 * (Fabric API / Minecraft juz go dostarczaja na classpath).
 */
public class GetConfig {
	public static boolean espEnabled = true;
	public static boolean tracersEnabled = true;
	public static boolean targetHudEnabled = true;
	public static boolean watermarkEnabled = true;
	public static boolean autoSprintEnabled = false;
	public static boolean fullbrightEnabled = false;

	// Zoom: FOV (w stopniach) ustawiany tymczasowo podczas trzymania klawisza zoom
	public static final double ZOOM_FOV = 15.0;
}
