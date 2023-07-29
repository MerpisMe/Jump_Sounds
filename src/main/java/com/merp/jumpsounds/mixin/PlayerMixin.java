package com.merp.jumpsounds.mixin;

import com.merp.jumpsounds.client.JumpSoundsLogic;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {

	@Unique
	BlockPos pos;
	@Unique
	BlockState state;


	@Environment(EnvType.CLIENT)
	protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
		super(entityType, level);
	}

	@Inject(method = "tick", at = @At("TAIL"))
	public void land(CallbackInfo ci) {

		BlockPos blockPos = this.getOnPos();
		BlockPos blockPos2 = this.getOnPosLegacy();

		boolean matchingStates = !blockPos2.equals(blockPos);

		BlockState primaryState = this.level().getBlockState(blockPos);
		BlockState secondaryState = this.level().getBlockState(blockPos2);

		pos = matchingStates ? blockPos2 : blockPos;
		state = matchingStates ? secondaryState : primaryState;

		JumpSoundsLogic.land(this, pos, state);
	}

	@Inject(method = "jumpFromGround", at = @At("TAIL"))
	public void jump(CallbackInfo ci) {
		JumpSoundsLogic.jump(this, pos,  state);
	}

	@Inject(method= "playStepSound", at = @At("HEAD"), cancellable = true)
	public void playStepSound(CallbackInfo ci) {
		if (this.yOld - this.getY() > 0) {
			// stops regular step sound from playing upon landing while moving
			// like it works on bedrock edition
			ci.cancel();
		}
	}

}