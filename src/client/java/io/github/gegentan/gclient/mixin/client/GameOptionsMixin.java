package io.github.gegentan.gclient.mixin.client;

import io.github.gegentan.gclient.ImGuiInitializer;
import io.github.gegentan.gclient.ImGuiRenderer;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.Perspective;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameOptions.class)
public class GameOptionsMixin {
    @Inject(method = "getPerspective", at = @At("HEAD"), cancellable = true)
    private void injectGetPerspective(CallbackInfoReturnable<Perspective> cir) {
        if (ImGuiRenderer.spectatePlayerOn &&
                ImGuiInitializer.players != null &&
                ImGuiInitializer.players.stream().anyMatch(player -> ImGuiRenderer.spectatePlayerName.get().equals(player.getName().getString())
        )) {
            cir.setReturnValue(Perspective.THIRD_PERSON_BACK);
        }
    }
}
