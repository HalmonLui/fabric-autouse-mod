package com.example.mixin.client;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralTextContent;
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
    @Shadow public int totalExperience;
    // set home stuff vvv
    private int delayTimer = 0;
    private boolean helloTriggered = false;
    private int hardcodeCounter = 0;
    private boolean notSendHomeYet = false;
    // set home stuff ^^^

    private boolean canUseItem = true;

    protected PlayerEntityMixin(EntityType<? extends PlayerEntity> type, World world) {
        super(type, world);
    }

    @Inject(method = "onDeath", at = @At("HEAD"))
    protected void checkDeath(DamageSource source, CallbackInfo ci) {
        String name = String.valueOf(this.getName());
        System.out.println(name + "DIED LOLOLOLOLlololol");
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void tick(CallbackInfo info) throws AWTException {
        System.setProperty("java.awt.headless", "false");
        Robot robot = new Robot();
        if (this.isDead()) {
             System.out.println("IN THE TICK DEADD HAHAHA");
             canUseItem = true;
             if (this.isPlayer()) {
                 System.out.println("IT A PLAYER THO");
                 this.requestRespawn();
                 System.out.println("IT DOOO BE RESPAWNNNN EZGETITxd");
                 this.sendMessage(Text.of("/say I DIED LMAO"));

                 System.out.println("notSendHomeYet so send it  lol");
                 sendHome(robot);
                 System.out.println("I chat and I go home to ligma");
            }
        }


        if (this.experienceLevel == 10 && canUseItem) {
            System.out.println("YOU ARE LEVEL USE THE ITEM NOW!!");
            System.out.println(this.experienceLevel);
            robot.keyPress(KeyEvent.VK_2);
            robot.keyRelease(KeyEvent.VK_2);
            robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
            robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
            this.sendMessage(Text.of("Use that item ex deeee"));
            canUseItem = false;
        }
    }

    private void sendHome(Robot robot) {
        System.out.println("Inside SEND HOME!!!!!!!!!");
        notSendHomeYet = true;
        helloTriggered = true;
        delayTimer = 100;
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            // Handle the delay
            if (helloTriggered && delayTimer > 0) {
                delayTimer--;
            } else if (helloTriggered) {
                // Send the "/home" message after 100 ticks
                try {
                    sendHiMessage(robot);
                    delayTimer = 10;
                } catch (AWTException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        System.out.println("Shudda said HI bruddahhhh");
    }

    private void sendHiMessage(Robot robot) throws AWTException {
        System.out.println("TRYNA SEND HI THO");
        MinecraftClient.getInstance().player.networkHandler.sendChatCommand("home ligma");
        System.out.println("we shud be home by now...");
        helloTriggered = false;

//        // Simulate a key press
//        if (hardcodeCounter == 0) {
//            robot.keyPress(KeyEvent.VK_SLASH);
//            robot.keyRelease(KeyEvent.VK_SLASH);
//            hardcodeCounter++;
//        } else if (hardcodeCounter == 1) {
//            robot.keyPress(KeyEvent.VK_H);
//            robot.keyRelease(KeyEvent.VK_H);
//            hardcodeCounter++;
//        } else if (hardcodeCounter == 2) {
//            robot.keyPress(KeyEvent.VK_O);
//            robot.keyRelease(KeyEvent.VK_O);
//            hardcodeCounter++;
//        } else if (hardcodeCounter == 3) {
//            robot.keyPress(KeyEvent.VK_M);
//            robot.keyRelease(KeyEvent.VK_M);
//            hardcodeCounter++;
//        } else if (hardcodeCounter == 4) {
//            robot.keyPress(KeyEvent.VK_E);
//            robot.keyRelease(KeyEvent.VK_E);
//            hardcodeCounter++;
//        } else if (hardcodeCounter == 5) {
//            robot.keyPress(KeyEvent.VK_ENTER);
//            robot.keyRelease(KeyEvent.VK_ENTER);
//            hardcodeCounter = 0;
//            helloTriggered = false;
//        }
    }
}