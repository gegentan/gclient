package io.github.gegentan.gclient.tools;

import imgui.ImGui;
import imgui.flag.ImGuiTableFlags;
import imgui.type.ImBoolean;
import imgui.type.ImString;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;

public class PlayerCoords {
    private static final ImBoolean toolOpen = new ImBoolean(false);
    private static final ImString filter = new ImString();

    public static void render(MinecraftClient client) {
        ImGui.begin("Player Cords", toolOpen);

        ImGui.inputText("Filter", filter);

        int columns = 4;

        if (ImGui.beginTable("Player cords", columns, ImGuiTableFlags.Borders | ImGuiTableFlags.RowBg)) {
            ImGui.tableSetupColumn("Name");
            ImGui.tableSetupColumn("X");
            ImGui.tableSetupColumn("Y");
            ImGui.tableSetupColumn("Z");

            ImGui.tableHeadersRow();

            if (client.world != null) {
                for (AbstractClientPlayerEntity player : client.world.getPlayers()) {
                    String name = player.getName().getString();

                    if ((name.contains(filter.get())) || (filter.get().isEmpty())) {
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
            }

            ImGui.endTable();
        }

        ImGui.end();
    }

    public static void openTool() {
        toolOpen.set(true);
    }

    public static ImBoolean isToolOpen() {
        return toolOpen;
    }
}
