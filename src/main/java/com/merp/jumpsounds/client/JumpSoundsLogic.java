package com.merp.jumpsounds.client;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Unique;

public class JumpSoundsLogic {

    public static void jump(LivingEntity entity) {

        BlockPos blockPos = entity.blockPosition();
        BlockState state = entity.level().getBlockState(blockPos);

        BlockState primaryState = state.is(BlockTags.INSIDE_STEP_SOUND_BLOCKS) ? state : entity.getBlockStateOn();
        BlockState secondaryState = entity.level().getBlockState(blockPos.below());

        if (entity.onGround() && !primaryState.isAir()) {
            if (primaryState.is(BlockTags.COMBINATION_STEP_SOUND_BLOCKS)) {
                playJumpSound(primaryState,1,1f, entity);
                playJumpSound(secondaryState,0.5f,0.8f, entity);
            } else {
                playJumpSound(primaryState,1,1f, entity);
            }
        }
    }

    public static void land(LivingEntity entity) {

        BlockPos blockPos = entity.blockPosition();
        BlockState state = entity.level().getBlockState(blockPos);

        BlockState primaryState = state.is(BlockTags.INSIDE_STEP_SOUND_BLOCKS) ? state : entity.getBlockStateOn();
        BlockState secondaryState = entity.level().getBlockState(blockPos.below());

        if (entity.onGround() && !primaryState.isAir()) {
            if (entity.getPosition(0).y() > entity.getPosition(1).y()) {
                if (primaryState.is(BlockTags.COMBINATION_STEP_SOUND_BLOCKS)) {
                    playJumpSound(primaryState,1,0.9f, entity);
                    playJumpSound(secondaryState,0.5f,0.7f, entity);
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
            entity.playSound(soundType.getFallSound(), soundType.getVolume() * 0.1F * volMult, soundType.getPitch() * pitchMult);
        }
    }
}