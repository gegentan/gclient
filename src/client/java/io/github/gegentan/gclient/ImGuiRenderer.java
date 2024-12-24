package io.github.gegentan.gclient;

import imgui.ImDrawData;
import imgui.ImGui;
import imgui.flag.ImGuiTableFlags;
import imgui.type.ImBoolean;
import imgui.type.ImString;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.option.Perspective;
import net.minecraft.text.Text;

public class ImGuiRenderer {

    private static final ImBoolean sendMessageToolActive = new ImBoolean(false);
    private static ImString sendMessageText = new ImString();

    private static final ImBoolean playerCordsToolActive = new ImBoolean(false);
    private static final ImString playerCordsFilter = new ImString();

    private static final ImBoolean spectatePlayerToolActive = new ImBoolean(false);
    public static ImString spectatePlayerName = new ImString();
    public static boolean spectatePlayerOn = false;

    private static final ImBoolean screenShakeToolActive = new ImBoolean(false);
    public static boolean screenShakeOn = false;
    public static float[] screenShakeIntensity = {0.1f};

    public static final ImBoolean antiAfkToolActive = new ImBoolean(false);
    public static boolean antiAfkOn = false;

    public static ImDrawData renderImGui(MinecraftClient client) {
        ImGui.newFrame();

        if (ImGui.beginMainMenuBar()) {
            if (ImGui.beginMenu("Tools")) {
                if (ImGui.menuItem("Send Message")) {
                    sendMessageToolActive.set(true);
                }
                if (ImGui.menuItem("Player Cords")) {
                    playerCordsToolActive.set(true);
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

        if (playerCordsToolActive.get()) {
            ImGui.begin("Player Cords", playerCordsToolActive);

            ImGui.inputText("Filter", playerCordsFilter);

            int columns = 4;

            if (ImGui.beginTable("Player cords", columns, ImGuiTableFlags.Borders | ImGuiTableFlags.RowBg)) {
                ImGui.tableSetupColumn("Name");
                ImGui.tableSetupColumn("X");
                ImGui.tableSetupColumn("Y");
                ImGui.tableSetupColumn("Z");

                ImGui.tableHeadersRow();

                for (AbstractClientPlayerEntity player : ImGuiInitializer.players) {
                    String name = player.getName().getString();

                    if ((name.contains(playerCordsFilter.get())) || (playerCordsFilter.get().isEmpty())) {
                        float x = Math.round(player.getX()*1000f)/1000f;
                        float y = Math.round(player.getY()*100000f)/100000f;
                        float z = Math.round(player.getZ()*1000f)/1000f;

                        ImGui.tableNextRow();
                        ImGui.tableSetColumnIndex(0);
                        ImGui.text(name);
                        ImGui.tableSetColumnIndex(1);
                        ImGui.text(String.valueOf(x));
                        ImGui.tableSetColumnIndex(2);
                        ImGui.text(String.valueOf(y));
                        ImGui.tableSetColumnIndex(3);
                        ImGui.text(String.valueOf(z));
                    }
                }

                ImGui.endTable();
            }

            ImGui.end();
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
            ImGui.begin("Anti AFK", sendMessageToolActive);
            if (ImGui.checkbox("Active", antiAfkOn)) {
                antiAfkOn = !antiAfkOn;
            }
            ImGui.end();
        }

        ImGui.render();
        return ImGui.getDrawData();
    }
}
