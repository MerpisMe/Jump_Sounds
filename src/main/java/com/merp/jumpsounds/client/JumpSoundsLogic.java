package com.merp.jumpsounds.client;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Unique;

public class JumpSoundsLogic {


    public static void land(LivingEntity entity) {
        BlockState primaryState = entity.getBlockStateOn();
        BlockState secondaryState = entity.getFeetBlockState();
        if (entity.onGround() && !primaryState.isAir()) {
            if (primaryState.is(BlockTags.COMBINATION_STEP_SOUND_BLOCKS)) {
                playJumpSound(primaryState,1,1f, entity);
                playJumpSound(secondaryState,0.5f,0.8f, entity);
            } else {
                playJumpSound(primaryState,1,1f, entity);
            }
        }
    }

    public static void jump(LivingEntity entity) {
        BlockState primaryState = entity.getBlockStateOn();
        BlockState secondaryState = entity.getFeetBlockState();
        if (entity.onGround() && !primaryState.isAir()) {
            if (entity.getPosition(0).y() > entity.getPosition(0).y()) {
                if (primaryState.is(BlockTags.COMBINATION_STEP_SOUND_BLOCKS)) {
                    playJumpSound(primaryState,1,0.9f, entity);
                    playJumpSound(secondaryState,0.5f,0.8f * 0.9f, entity);
                } else {
                    playJumpSound(primaryState,1,0.9f, entity);
                }
            }
        }
    }

    @Unique
    protected static void playJumpSound(BlockState state, float volMult, float pitchMult, LivingEntity entity) {
        SoundType soundType = state.getSoundType();
        if (!entity.isInWaterOrBubble()) {
            entity.playSound(soundType.getFallSound(), soundType.getVolume() * 0.075F * volMult, soundType.getPitch() * pitchMult);
        }
    }
}