package com.example.mixin.client;

import com.example.ExampleModClient;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {

    private int respawnDelayTimer = 100; // timer for respawn
    private int useItemDelayTimer = 120; // timer for useItem
    private int tickDelayTimer = 100; // timer for tick
    private boolean shouldUseItem = false;
    private boolean shouldRespawn = false;

    private boolean notUsedYet = true;
    private ClientPlayerEntity player = MinecraftClient.getInstance().player;

    protected PlayerEntityMixin(EntityType<? extends PlayerEntity> type, World world) {
        super(type, world);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void tick(CallbackInfo info) {
        if (ExampleModClient.isAutoUseOn()) {
            player = MinecraftClient.getInstance().player;

            if (player != null && player.isDead()) {
                doRequestRespawn();
            } else if (tickDelayTimer > 0) {
                tickDelayTimer--;
            } else {
                if (player != null && player.experienceLevel >= 50 && notUsedYet) {
                    notUsedYet = false;
                    useItem();
                }
                tickDelayTimer = 100;
            }
        }
    }

    private void doRequestRespawn() {
        shouldRespawn = true;
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            // Handle the delay
            if (shouldRespawn && respawnDelayTimer > 0 && respawnDelayTimer != 40) {
                respawnDelayTimer--;
            } else if (shouldRespawn && respawnDelayTimer == 40) {
                player.sendMessage(Text.of("You died, requesting respawn"));
                // Send the respawn request after 100 ticks
                player.requestRespawn();
                respawnDelayTimer--;
            }
            else if (shouldRespawn) {
                // Send the "/home" message after 100 ticks
                player.sendMessage(Text.of("calling /home xp"));
                player.networkHandler.sendChatCommand("home xp"); // make sure you `/sethome xp` first. TODO: Make this field customizable.
                shouldRespawn = false;
                respawnDelayTimer = 100;  // 100 ticks
            }
        });
    }

    private void useItem() {
        shouldUseItem = true;

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            // Handle the delay
            if (shouldUseItem && useItemDelayTimer > 0 && useItemDelayTimer != 80 && useItemDelayTimer != 40) {
                useItemDelayTimer--;
            } else if (shouldUseItem && useItemDelayTimer == 80) {
                player.getInventory().selectedSlot = 1;
                useItemDelayTimer--;
            } else if (shouldUseItem && useItemDelayTimer == 40) {
                // Use item after 100 ticks
                // TODO: find a way to send right click. Workaround currently is using the auto clicker mod to right click every 50ticks
//                player.sendMessage(Text.of("You are level: " + player.experienceLevel + ". Using the item"));
//                player.getInventory().getStack(1).use(player.getWorld(), player, Hand.MAIN_HAND);

                useItemDelayTimer--;
            } else if (shouldUseItem) {
                player.sendMessage(Text.of("Item used"));
                shouldUseItem = false;
                notUsedYet = true;
                player.getInventory().selectedSlot = 0;
                useItemDelayTimer = 120;  // 120 ticks
            }
        });
    }
}