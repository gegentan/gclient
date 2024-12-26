package io.github.gegentan.gclient;

import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;

public class ImGuiInitializer {
    private static final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    private static final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();
    private static MinecraftClient client;

    public static void init(MinecraftClient minecraftClient) {
        ImGui.createContext();
        ImGuiIO io = ImGui.getIO();
        io.setIniFilename(null);
        io.addConfigFlags(ImGuiConfigFlags.NavEnableKeyboard);

        client = minecraftClient;
        imGuiGlfw.init(client.getWindow().getHandle(), true);

        imGuiGl3.init();

        HudRenderCallback.EVENT.register((matrixStack, tickDelta) -> {
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
