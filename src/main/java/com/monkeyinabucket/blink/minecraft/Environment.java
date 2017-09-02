package com.monkeyinabucket.blink.minecraft;

import com.monkeyinabucket.blink.minecraft.block.Registry;

/**
 * Provides information about, and resources from, the current execution environment.
 */
public interface Environment {

    /**
     * @return true if this is the server environment, false if not.
     */
    boolean isServer();

    /**
     * @return true if this is the client environment, false if not.
     */
    boolean isClient();

    /**
     * @return the block registry for the current environment.
     */
    Registry getBlockRegistry();
}
