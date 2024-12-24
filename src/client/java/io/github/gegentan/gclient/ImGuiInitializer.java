package io.github.gegentan.gclient;

import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;

import java.util.List;
import java.util.Random;

public class ImGuiInitializer {
    private static final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    private static final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();
    private static MinecraftClient client;

    public static List<AbstractClientPlayerEntity> players;

    public static void init() {
        Random random = new Random();
        ImGui.createContext();
        ImGuiIO io = ImGui.getIO();
        io.setIniFilename(null);
        io.addConfigFlags(ImGuiConfigFlags.NavEnableKeyboard);

        client = MinecraftClient.getInstance();
        imGuiGlfw.init(client.getWindow().getHandle(), true);

        imGuiGl3.init();

        HudRenderCallback.EVENT.register((matrixStack, tickDelta) -> {
            if (client.world != null) {
                players = client.world.getPlayers();
            }
            if (client.player != null) {
                if (ImGuiRenderer.antiAfkOn) {
                    if (random.nextInt(30) == 1) {
                        client.player.jump();
                    }
                    float addYaw = random.nextFloat(20)-10f;
                    float addPitch = random.nextFloat(20)-10f;
                    client.player.setYaw(client.player.getYaw()+addYaw);
                    client.player.setPitch(client.player.getPitch()+addPitch);
                    if (client.player.getPitch() > 90) client.player.setPitch(80);
                    if (client.player.getPitch() < -90) client.player.setPitch(-80);
                }
            }
            if (GClientClient.showImGui) {
                imGuiGlfw.newFrame();
                imGuiGl3.renderDrawData(ImGuiRenderer.renderImGui(client));
            }
        });
    }

    public static void dispose() {
        imGuiGl3.dispose();
        imGuiGlfw.dispose();
        ImGui.destroyContext();
    }
}
