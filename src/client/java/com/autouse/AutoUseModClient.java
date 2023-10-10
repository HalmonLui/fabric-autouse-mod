package com.autouse;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class AutoUseModClient implements ClientModInitializer {
	private static AutoUseModClient instance;

	public static KeyBinding autoUseBind;
	public static KeyBinding longRespawnBind;
	private static boolean autoUseIsOn = false;
	private static boolean longRespawnOn = false;
	@Override
	public void onInitializeClient() {
		if (instance == null) instance = this;

		autoUseBind = KeyBindingHelper.registerKeyBinding(new KeyBinding("Toggle Auto Use", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_V, "AutoUse"));
		longRespawnBind = KeyBindingHelper.registerKeyBinding(new KeyBinding("Toggle Long Respawn", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_N, "AutoUse"));

		//Register Tick Callback
		ClientTickEvents.END_CLIENT_TICK.register(this::tick);
	}

	public void tick(MinecraftClient client) {
		if (autoUseBind.wasPressed()) {
			autoUseIsOn = !autoUseIsOn;
			MinecraftClient.getInstance().player.sendMessage(Text.of("Autouse set to: " + autoUseIsOn));
		}
		if (longRespawnBind.wasPressed()) {
			longRespawnOn = !longRespawnOn;
			MinecraftClient.getInstance().player.sendMessage(Text.of("Long respawn set to: " + longRespawnOn));
		}
	}

	public static boolean isAutoUseOn() {
		return autoUseIsOn;
	}

	public static boolean isLongRespawnOn() {
		return longRespawnOn;
	}
}