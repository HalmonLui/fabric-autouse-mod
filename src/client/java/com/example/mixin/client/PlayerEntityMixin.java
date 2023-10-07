package com.example.mixin.client;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    @Shadow
    public abstract Text getName();

    @Shadow public abstract boolean isPlayer();

    @Shadow public abstract void requestRespawn();

    protected PlayerEntityMixin(EntityType<? extends PlayerEntity> type, World world) {
        super(type, world);
    }

    @Inject(method = "onDeath", at = @At("HEAD"))
    protected void checkDeath(DamageSource source, CallbackInfo ci) {
        String name = String.valueOf(this.getName());
        System.out.println(name + "DIED LOLOLOLOLlololol");
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void tick(CallbackInfo info) {
        if (this.isDead()) {
             System.out.println("IN THE TICK DEADD HAHAHA");
             if (this.isPlayer()) {
                 System.out.println("IT A PLAYER THO");
                 this.requestRespawn();
                 System.out.println("IT DOOO BE RESPAWNNNN EZGETIT");
            }
        }
    }
}