package com.merp.jumpsounds.client;

import com.merp.jumpsounds.SoundRegistry;
import net.fabricmc.api.ClientModInitializer;

public class JumpSoundsClient implements ClientModInitializer {
    /**
     * Runs the mod initializer on the client environment.
     */
    public static final String MODID = "jump_sounds";
    @Override
    public void onInitializeClient() {
        SoundRegistry.init();
    }
}
