package io.github.gegentan.gclient;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import org.lwjgl.glfw.GLFW;

public class GClientClient implements ClientModInitializer {
	private static final KeyBinding toggleImGui = new KeyBinding(
			"key.toogle_imgui",
			GLFW.GLFW_KEY_F4,
			"category.gclient"
	);

	public static boolean showImGui = false;
	private static boolean imGuiRegistered = false;

	@Override
	public void onInitializeClient() {
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (toggleImGui.wasPressed()) {
				showImGui = !showImGui;
			}

			if (!imGuiRegistered) {
				if (MinecraftClient.getInstance().getWindow() != null) {
					ImGuiInitializer.init();
					imGuiRegistered = true;
				}
			}
		});
		LivingEntityFeatureRendererRegistrationCallback.EVENT.register((entityType, entityRenderer, registrationHelper, context) -> {

		});
//		WorldRenderEvents.LAST.register(context -> {
//			MinecraftClient client = MinecraftClient.getInstance();
//			if (client.player != null) {
//				PlayerEntityRenderer renderer = (PlayerEntityRenderer) client.getEntityRenderDispatcher().getRenderer(client.player);
//				renderer.render(client.player, client.player.getYaw(), client.getTickDelta(), context.matrixStack(), context.consumers(), client.world.getLightLevel(client.player.getBlockPos()));
//			}
//		});
	}
}
