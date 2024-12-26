package io.github.gegentan.gclient.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.gegentan.gclient.GClientClient;
import io.github.gegentan.gclient.ImGuiRenderer;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.Camera;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Random;

@Mixin(Camera.class)
public abstract class CameraMixin {
    private static final Random random = new Random();

    @Shadow
    protected abstract void moveBy(float x, float y, float z);

    @Shadow
    protected abstract void setPos(Vec3d pos);

    @Shadow
    protected abstract void setRotation(float yaw, float pitch);

    @Inject(method = "update(Lnet/minecraft/world/BlockView;Lnet/minecraft/entity/Entity;ZZF)V", at = @At("TAIL"))
    private void injected(CallbackInfo ci, @Local(ordinal = 1) boolean inverseView) {
        if (GClientClient.client.world != null) {
            List<AbstractClientPlayerEntity> players = GClientClient.client.world.getPlayers();
            if (ImGuiRenderer.spectatePlayerOn && players != null) {
                for (AbstractClientPlayerEntity player : players) {
                    if (ImGuiRenderer.spectatePlayerName.get().equals(player.getName().getString())) {
                        this.setPos(player.getEyePos());
                        this.setRotation(player.getYaw(), player.getPitch());
                    }
                }
            }
        }
        if (ImGuiRenderer.screenShakeOn) {
            float intensity = ImGuiRenderer.screenShakeIntensity[0];
            this.moveBy(random.nextFloat(intensity * 2) - intensity, random.nextFloat(intensity * 2) - intensity, random.nextFloat(intensity * 2) - intensity);
        }
    }
}