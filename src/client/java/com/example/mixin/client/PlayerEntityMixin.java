package com.example.mixin.client;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    @Shadow
    public abstract Text getName();
    @Shadow public abstract boolean isPlayer();
    @Shadow public abstract void requestRespawn();
    @Shadow public int experienceLevel;
    private int delayTimer = 100;  // timer used for waiting ticks
    private int respawnDelayTimer = 100; // timer for respawn
    private int useItemDelayTimer = 100; // timer for useItem
    private int keyPressDelayTimer = 40; // timer for doKeyPress
    private int tickDelayTimer = 100; // timer for tick
    private boolean shouldCallHome = false;
    private boolean shouldUseItem = false;
    private boolean shouldRespawn = false;
    private boolean shouldKeyPress = false;

    private boolean notUsedYet = true;

    protected PlayerEntityMixin(EntityType<? extends PlayerEntity> type, World world) {
        super(type, world);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void tick(CallbackInfo info) throws AWTException {
        System.setProperty("java.awt.headless", "false");
        Robot robot = new Robot();

        if (this.isDead()) {
            doRequestRespawn();
        } else if (tickDelayTimer > 0) {
            tickDelayTimer--;
        } else {
            if (this.experienceLevel >= 50 && notUsedYet) {
                notUsedYet = false;
                useItem(robot);
            }
            tickDelayTimer = 100;
        }
    }

    private void doRequestRespawn() {
        shouldRespawn = true;
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            // Handle the delay
            if (shouldRespawn && respawnDelayTimer > 0 && respawnDelayTimer != 40) {
                respawnDelayTimer--;
            } else if (shouldRespawn && respawnDelayTimer == 40) {
                this.sendMessage(Text.of("You died, requesting respawn"));
                // Send the respawn request after 100 ticks
                this.requestRespawn();
                respawnDelayTimer--;
            }
            else if (shouldRespawn) {
                // Send the "/home" message after 100 ticks
                this.sendMessage(Text.of("calling /home xp"));
                MinecraftClient.getInstance().player.networkHandler.sendChatCommand("home xp"); // make sure you `/sethome xp` first. TODO: Make this field customizable.
                shouldRespawn = false;
                respawnDelayTimer = 100;  // 100 ticks
            }
        });
    }

    private void useItem(Robot robot) {
        shouldUseItem = true;

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            // Handle the delay
            if (shouldUseItem && useItemDelayTimer > 0 && useItemDelayTimer != 80 && useItemDelayTimer != 40) {
                useItemDelayTimer--;
            } else if (shouldUseItem && useItemDelayTimer == 80) {
                robot.keyPress(KeyEvent.VK_2);  // set to hotbar 2 key which should be the item. TODO: Make this customizable or automatic
                robot.keyRelease(KeyEvent.VK_2);
                useItemDelayTimer--;
            } else if (shouldUseItem && useItemDelayTimer == 40) {
                // Use item after 100 ticks
                this.sendMessage(Text.of("You are level: " + this.experienceLevel + ". Using the item"));
                robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
                robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
                useItemDelayTimer--;
            } else if (shouldUseItem) {
                this.sendMessage(Text.of("Item used"));
                shouldUseItem = false;
                notUsedYet = true;
                robot.keyPress(KeyEvent.VK_1);  // set to hotbar 2 key which should be the item. TODO: Make this customizable or automatic
                robot.keyRelease(KeyEvent.VK_1);
                useItemDelayTimer = 100;  // 100 ticks
            }
        });
    }
}