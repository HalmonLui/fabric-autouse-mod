package com.example;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class ExampleModClient implements ClientModInitializer {
	private static ExampleModClient instance;

	public static KeyBinding autoUseBind;
	@Override
	public void onInitializeClient() {
		if (instance == null) instance = this;

		autoUseBind = KeyBindingHelper.registerKeyBinding(new KeyBinding("autoUseKey", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_V, "AutoUse"));

		//Register Tick Callback
		ClientTickEvents.END_CLIENT_TICK.register(this::tick);
	}

	public void tick(MinecraftClient client) {
		if (autoUseBind.wasPressed()) {
//			System.out.println("Hola Mundo");
		}
	}
}