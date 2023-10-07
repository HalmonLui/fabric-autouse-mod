package com.example.mixin.client;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    protected LivingEntityMixin(EntityType<? extends LivingEntity> type, World world) {
        super(type, world);
    }

    @Inject(method = "onDeath", at = @At("TAIL"))
    private void onDeath(CallbackInfo info) {
        System.out.println("LIVING ENTITY DIEDFKEJFKE");
    }
}

