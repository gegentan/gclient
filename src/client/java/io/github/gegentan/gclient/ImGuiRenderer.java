package io.github.gegentan.gclient;

import imgui.ImDrawData;
import imgui.ImGui;
import imgui.type.ImBoolean;
import imgui.type.ImString;
import io.github.gegentan.gclient.tools.PlayerCoords;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.Perspective;
import net.minecraft.text.Text;

public class ImGuiRenderer {

    private static final ImBoolean sendMessageToolActive = new ImBoolean(false);
    private static ImString sendMessageText = new ImString();

    private static final ImBoolean spectatePlayerToolOpen = new ImBoolean(false);
    public static ImString spectatePlayerName = new ImString();
    public static boolean spectatePlayerActive = false;

    private static final ImBoolean screenShakeToolOpen = new ImBoolean(false);
    public static boolean screenShakeActive = false;
    public static float[] screenShakeIntensity = {0.1f};

    public static final ImBoolean antiAfkToolOpen = new ImBoolean(false);
    public static boolean antiAfkActive = false;
    public static boolean antiAfkJump = true;
    public static boolean antiAfkShakeCam = true;

    public static ImDrawData renderImGui(MinecraftClient client) {
        ImGui.newFrame();

        if (ImGui.beginMainMenuBar()) {
            if (ImGui.beginMenu("Tools")) {
                if (ImGui.menuItem("Send Message")) {
                    sendMessageToolActive.set(true);
                }
                if (ImGui.menuItem("Player Cords")) {
                    PlayerCoords.openTool();
                }
                if (ImGui.menuItem("Spectate Player")) {
                    spectatePlayerToolOpen.set(true);
                }
                if (ImGui.menuItem("Screen Shake")) {
                    screenShakeToolOpen.set(true);
                }
                if (ImGui.menuItem("Anti AFK")) {
                    antiAfkToolOpen.set(true);
                }
                ImGui.endMenu();
            }
            ImGui.endMainMenuBar();
        }

        if (sendMessageToolActive.get()) {
            ImGui.begin("Send Message", sendMessageToolActive);
            ImGui.inputTextMultiline("Message", sendMessageText);
            if (ImGui.button("Send")) {
                client.inGameHud.getChatHud().addMessage(Text.literal(sendMessageText.get()));
            }
            if (ImGui.button("Clear")) {
                sendMessageText = new ImString();
            }
            ImGui.end();
        }

        if (PlayerCoords.isToolOpen().get()) {
            PlayerCoords.render(client);
        }

        if (spectatePlayerToolOpen.get()) {
            ImGui.begin("Spectate Player", spectatePlayerToolOpen);
            if (ImGui.checkbox("Active", spectatePlayerActive)) {
                spectatePlayerActive = !spectatePlayerActive;
                if (!spectatePlayerActive) {
                    client.options.setPerspective(Perspective.FIRST_PERSON);
                }
            }
            ImGui.inputText("Player name", spectatePlayerName);
            ImGui.end();
        }

        if (screenShakeToolOpen.get()) {
            ImGui.begin("Screen Shake", screenShakeToolOpen);
            if (ImGui.checkbox("Active", screenShakeActive)) {
                screenShakeActive = !screenShakeActive;
            }
            ImGui.sliderFloat("Intensity", screenShakeIntensity, 0.001f, 1f);
            ImGui.end();
        }

        if (antiAfkToolOpen.get()) {
            ImGui.begin("Anti AFK", antiAfkToolOpen);
            if (ImGui.checkbox("Active", antiAfkActive)) {
                antiAfkActive = !antiAfkActive;
            }
            if (ImGui.checkbox("Jump", antiAfkJump)) {
                antiAfkJump = !antiAfkJump;
            }
            if (ImGui.checkbox("Shake Camera", antiAfkShakeCam)) {
                antiAfkShakeCam = !antiAfkShakeCam;
            }
            ImGui.end();
        }

        ImGui.render();
        return ImGui.getDrawData();
    }
}
