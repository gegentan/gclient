package io.github.gegentan.gclient;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import java.util.Random;

public class GClientClient implements ClientModInitializer {
	private static KeyBinding toggleImGuiKey;

	public static MinecraftClient client;
	public static boolean showImGui = false;
	private static boolean imGuiInitialized = false;

	@Override
	public void onInitializeClient() {
		Random random = new Random();

		client = MinecraftClient.getInstance();

		toggleImGuiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key.gclient.toggle_imgui",
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_F4,
				"category.gclient.keybinds"
		));

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (toggleImGuiKey.wasPressed()) {
				showImGui = !showImGui;
			}

			if (!imGuiInitialized) {
				if (client.getWindow() != null) {
					ImGuiInitializer.init(client);
					imGuiInitialized = true;
				}
			}

			if (client.player != null) {
				if (ImGuiRenderer.antiAfkActive) {
					if (ImGuiRenderer.antiAfkJump && client.player.isOnGround() && random.nextInt(30) == 1) {
						client.player.jump();
					}
					if (ImGuiRenderer.antiAfkShakeCam) {
						float addYaw = random.nextFloat(20)-10f;
						float addPitch = random.nextFloat(20)-10f;
						client.player.setYaw(client.player.getYaw()+addYaw);
						client.player.setPitch(client.player.getPitch()+addPitch);
						if (client.player.getPitch() > 90) client.player.setPitch(80);
						if (client.player.getPitch() < -90) client.player.setPitch(-80);
					}
				}
			}
		});
	}
}
