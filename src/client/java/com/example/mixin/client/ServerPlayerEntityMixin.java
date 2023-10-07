package com.example.mixin.client;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends Entity {

    protected ServerPlayerEntityMixin(EntityType<? extends LivingEntity> type, World world) {
        super(type, world);
    }

    @Inject(method = "onDeath", at = @At("HEAD"))
    protected void checkDeath(DamageSource source, CallbackInfo ci) {
        System.out.println("SERVER PLAYER ENEITYTTTT DIED LOLOLOLOLlololol");
    }


}