package com.example.mixin.client;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;
import java.awt.event.KeyEvent;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    private int delayTimer = 0;
    private boolean helloTriggered = false;
    private int hardcodeCounter = 0;


    protected LivingEntityMixin(EntityType<? extends LivingEntity> type, World world) {
        super(type, world);
    }

    @Inject(method = "onDeath", at = @At("TAIL"))
    private void onDeath(CallbackInfo info) {
//        System.out.println("LIVING ENTITY DIEDFKEJFKE");
//        helloTriggered = true;
//        delayTimer = 10;
//        ClientTickEvents.END_CLIENT_TICK.register(client -> {
//            // Handle the delay
//            if (helloTriggered && delayTimer > 0) {
//                System.out.println(delayTimer);
//                delayTimer--;
//            } else if (helloTriggered) {
//                // Send the "Hi" message after 3 seconds
//                try {
//                    sendHiMessage();
//                    delayTimer = 10;
//                } catch (AWTException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        });
//        System.out.println("Shudda said HI bruddahhhh");
    }

    private void sendHiMessage() throws AWTException {
        System.out.println("TRYNA SEND HI THO");
        System.setProperty("java.awt.headless", "false");
        Robot robot = new Robot();
        // Simulate a key press
        if (hardcodeCounter == 0) {
            robot.keyPress(KeyEvent.VK_SLASH);
            robot.keyRelease(KeyEvent.VK_SLASH);
            hardcodeCounter++;
        } else if (hardcodeCounter == 1) {
            robot.keyPress(KeyEvent.VK_H);
            robot.keyRelease(KeyEvent.VK_H);
            hardcodeCounter++;
        } else if (hardcodeCounter == 2) {
            robot.keyPress(KeyEvent.VK_O);
            robot.keyRelease(KeyEvent.VK_O);
            hardcodeCounter++;
        } else if (hardcodeCounter == 3) {
            robot.keyPress(KeyEvent.VK_M);
            robot.keyRelease(KeyEvent.VK_M);
            hardcodeCounter++;
        } else if (hardcodeCounter == 4) {
            robot.keyPress(KeyEvent.VK_E);
            robot.keyRelease(KeyEvent.VK_E);
            hardcodeCounter++;
        } else if (hardcodeCounter == 5) {
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
            hardcodeCounter = 0;
            helloTriggered = false;
        }
    }
}

