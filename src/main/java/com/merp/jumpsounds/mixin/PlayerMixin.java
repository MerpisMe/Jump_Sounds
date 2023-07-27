package com.merp.jumpsounds.mixin;

import com.merp.jumpsounds.client.JumpSoundsLogic;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {

	@Environment(EnvType.CLIENT)
	protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
		super(entityType, level);
	}

	@Inject(method = "jumpFromGround", at = @At("TAIL"))
	public void jump(CallbackInfo ci) {
		JumpSoundsLogic.jump(this);
	}

	@Inject(method = "tick", at = @At("TAIL"))
	public void land(CallbackInfo ci) {
		JumpSoundsLogic.land(this);
	}
}