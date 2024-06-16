package com.merp.jumpsounds;

import com.merp.jumpsounds.client.JumpSoundsClient;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import org.jetbrains.annotations.NotNull;

public final class SoundRegistry {

    public static final SoundEvent JUMP_GENERIC = register("entity.player.jump");
    public static final SoundEvent LAND_GENERIC = register("entity.player.land");

    @NotNull
    public static SoundEvent register(@NotNull String path) {
        var id = ResourceLocation.fromNamespaceAndPath(JumpSoundsClient.MODID, path);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(id));
    }

    public static void init() {}
}
