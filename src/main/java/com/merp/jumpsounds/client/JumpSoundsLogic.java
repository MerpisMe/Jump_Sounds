package com.merp.jumpsounds.client;

import com.merp.jumpsounds.SoundRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;

public class JumpSoundsLogic {

    public static void jump(LivingEntity entity, BlockPos blockPos, BlockState primaryState) {
        if (entity.onGround() && !primaryState.isAir()) {
            entity.playSound(SoundRegistry.JUMP_GENERIC, 1.0f, 1.0f);
            soundController(entity, blockPos, primaryState);
        }
    }

    public static void land(LivingEntity entity, BlockPos blockPos, BlockState primaryState) {
        double yDelta = entity.yOld - entity.getY(); //used to test when the player lands
        if (entity.onGround() && !primaryState.isAir() && yDelta > 0) {
            if (yDelta == 0.0625 || yDelta == 0.04659999847412166) {
                // skip sounds entirely if the y Delta matches 1 or 2 pixels in height
                // it's hacky and awful but doing it this way is necessary with the
                // y Delta implementation, else we risk losing landing sounds when
                // walking off of multiples of 4 pixels in height
                return;
            }
            entity.playSound(SoundRegistry.LAND_GENERIC, 1.0f, 1.0f);
            soundController(entity, blockPos, primaryState);
        }
    }

    // everything beyond this point has basically been interpreted directly
    // from the code used for the new step sound system introduced in 1.20

    public static void soundController(LivingEntity entity, BlockPos blockPos, BlockState blockState) {
        float volume = entity.getPose().equals(Pose.CROUCHING) ? 0.6666F : 1F;
        if (entity.shouldPlayAmethystStepSound(blockState)) {
            entity.playAmethystStepSound();
        }
        if (entity.isInWater()) {
            playMuffledJumpSound(entity, blockState, volume);
        } else {
            BlockPos blockPos2 = JumpSoundsLogic.getPrimaryJumpSoundBlockPos(entity, blockPos);
            if (!blockPos.equals(blockPos2)) {
                BlockState blockState2 = entity.level().getBlockState(blockPos2);
                if (blockState2.is(BlockTags.COMBINATION_STEP_SOUND_BLOCKS)) {
                    playCombinationJumpSounds(entity, blockState2, blockState, volume);
                } else {
                    playJumpSound(entity, blockState2, volume);
                }
            } else {
                playJumpSound(entity, blockState, volume);
            }
        }
    }

    public static BlockPos getPrimaryJumpSoundBlockPos(LivingEntity entity, BlockPos blockPos){
        BlockPos blockPos2 = blockPos.above();
        BlockState blockState = entity.level().getBlockState(blockPos2);
        if (blockState.is(BlockTags.INSIDE_STEP_SOUND_BLOCKS) || blockState.is(BlockTags.COMBINATION_STEP_SOUND_BLOCKS)) {
            return blockPos2;
        }
        return blockPos;
    }

    protected static void playCombinationJumpSounds (LivingEntity entity, BlockState blockState, BlockState blockState2, float volume) {
        playJumpSound(entity, blockState, volume);
        playMuffledJumpSound(entity, blockState2, volume);
    }

    protected static void playMuffledJumpSound (LivingEntity entity, BlockState state, float volume){
        SoundType soundType = state.getSoundType();
        entity.playSound(soundType.getFallSound(), soundType.getVolume() * 0.05F * volume, soundType.getPitch() * 0.8f);
    }

    protected static void playJumpSound (LivingEntity entity, BlockState state, float volume) {
        SoundType soundType = state.getSoundType();
        entity.playSound(soundType.getFallSound(), soundType.getVolume() * 0.1F * volume, soundType.getPitch());
    }
}