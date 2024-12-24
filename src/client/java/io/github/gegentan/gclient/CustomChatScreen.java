package io.github.gegentan.gclient;

import imgui.ImGui;
import imgui.flag.ImGuiHoveredFlags;
import net.minecraft.client.gui.screen.ChatScreen;

public class CustomChatScreen extends ChatScreen {
    public CustomChatScreen(String originalChatText) {
        super(originalChatText);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        if (ImGui.isAnyItemActive()) {
            return true;
        }
        return super.charTyped(chr, modifiers);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (ImGui.isAnyItemHovered() || ImGui.isWindowHovered(ImGuiHoveredFlags.AnyWindow)) {
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }
}
