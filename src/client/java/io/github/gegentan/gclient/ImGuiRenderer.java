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

    private static final ImBoolean spectatePlayerToolActive = new ImBoolean(false);
    public static ImString spectatePlayerName = new ImString();
    public static boolean spectatePlayerOn = false;

    private static final ImBoolean screenShakeToolActive = new ImBoolean(false);
    public static boolean screenShakeOn = false;
    public static float[] screenShakeIntensity = {0.1f};

    public static final ImBoolean antiAfkToolActive = new ImBoolean(false);
    public static boolean antiAfkOn = false;
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
                    spectatePlayerToolActive.set(true);
                }
                if (ImGui.menuItem("Screen Shake")) {
                    screenShakeToolActive.set(true);
                }
                if (ImGui.menuItem("Anti AFK")) {
                    antiAfkToolActive.set(true);
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

        if (spectatePlayerToolActive.get()) {
            ImGui.begin("Spectate Player", spectatePlayerToolActive);
            if (ImGui.checkbox("Active", spectatePlayerOn)) {
                spectatePlayerOn = !spectatePlayerOn;
                if (!spectatePlayerOn) {
                    client.options.setPerspective(Perspective.FIRST_PERSON);
                }
            }
            ImGui.inputText("Player name", spectatePlayerName);
            ImGui.end();
        }

        if (screenShakeToolActive.get()) {
            ImGui.begin("Screen Shake", screenShakeToolActive);
            if (ImGui.checkbox("Active", screenShakeOn)) {
                screenShakeOn = !screenShakeOn;
            }
            ImGui.sliderFloat("Intensity", screenShakeIntensity, 0.001f, 1f);
            ImGui.end();
        }

        if (antiAfkToolActive.get()) {
            ImGui.begin("Anti AFK", antiAfkToolActive);
            if (ImGui.checkbox("Active", antiAfkOn)) {
                antiAfkOn = !antiAfkOn;
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
