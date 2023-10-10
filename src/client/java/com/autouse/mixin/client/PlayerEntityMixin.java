package com.autouse.mixin.client;

import com.autouse.AutoUseModClient;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {

    private int respawnDelayTimer = 2000; // timer for respawn
    private int useItemDelayTimer = 200; // timer for useItem
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
        if (AutoUseModClient.isAutoUseOn()) {
            player = MinecraftClient.getInstance().player;

            if (player != null && player.isDead()) {
                System.out.println("DEAD Calling respawn");
                if (AutoUseModClient.isLongRespawnOn()) {
                    System.out.println("setting delay timer to 12000");
                    respawnDelayTimer = 120000; // set to 10 minutes

                }
                doRequestRespawn();
            } else if (tickDelayTimer % 99 == 0 && tickDelayTimer != 0 && player.experienceLevel < 50) {
                player.getInventory().selectedSlot = 0; // reset to first hotbar slot every once in a while so we aren't stuck on the 2nd slot;
                tickDelayTimer--;
            }
            else if (tickDelayTimer > 0) {
                tickDelayTimer--;
            } else {
                if (player != null && player.experienceLevel >= 50 && notUsedYet) {
                    player.getInventory().selectedSlot = 0;
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
            if (AutoUseModClient.isLongRespawnOn()) {
                if (shouldRespawn && respawnDelayTimer > 0 && respawnDelayTimer != 15000) {
                    System.out.println("respawnDelayTimer" + respawnDelayTimer);
                    respawnDelayTimer--;
                } else if (shouldRespawn && respawnDelayTimer == 15000) {
                    player.sendMessage(Text.of("You died, requesting respawn"));
                    System.out.println("requesting respawn respawnDelayTimer" + respawnDelayTimer);
                    player.requestRespawn();
                    respawnDelayTimer--;
                }
                else if (shouldRespawn) {
                    // Send the "/home" message after 100 ticks
                    System.out.println("calling home respawnDelayTimer" + respawnDelayTimer);
                    player.sendMessage(Text.of("calling /home xp"));
                    player.networkHandler.sendChatCommand("home xp"); // make sure you `/sethome xp` first. TODO: Make this field customizable.
                    shouldRespawn = false;
                    player.getInventory().selectedSlot = 0; // reset to first hotbar slot after death so we aren't stuck on the 2nd slot
                }
            } else {
                if (shouldRespawn && respawnDelayTimer > 0 && respawnDelayTimer != 500) {
                    System.out.println("respawnDelayTimer" + respawnDelayTimer);
                    respawnDelayTimer--;
                } else if (shouldRespawn && respawnDelayTimer == 500) {
                    player.sendMessage(Text.of("You died, requesting respawn"));
                    System.out.println("requesting respawn respawnDelayTimer" + respawnDelayTimer);
                    player.requestRespawn();
                    respawnDelayTimer--;
                }
                else if (shouldRespawn) {
                    // Send the "/home" message after 100 ticks
                    System.out.println("calling home respawnDelayTimer" + respawnDelayTimer);
                    player.sendMessage(Text.of("calling /home xp"));
                    player.networkHandler.sendChatCommand("home xp"); // make sure you `/sethome xp` first. TODO: Make this field customizable.
                    shouldRespawn = false;
                    player.getInventory().selectedSlot = 0; // reset to first hotbar slot after death so we aren't stuck on the 2nd slot
                    respawnDelayTimer = 1000;  // 100 ticks
                }
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
//                player.getInventory().selectedSlot = 0;
                useItemDelayTimer = 200;  // 120 ticks
            }
        });
    }
}