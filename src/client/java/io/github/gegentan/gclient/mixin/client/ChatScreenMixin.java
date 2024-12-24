package io.github.gegentan.gclient.mixin.client;

import io.github.gegentan.gclient.CustomChatScreen;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class ChatScreenMixin {
    @Inject(method = "openChatScreen", at=@At("HEAD"), cancellable = true)
    private void openChatScreen(String message, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();

        client.setScreen(new CustomChatScreen(""));
        ci.cancel();
    }
}
