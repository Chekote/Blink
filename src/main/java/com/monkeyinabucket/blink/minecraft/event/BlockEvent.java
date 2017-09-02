package com.monkeyinabucket.blink.minecraft.event;

import com.monkeyinabucket.blink.minecraft.World;
import com.monkeyinabucket.world.Coordinates;

/**
 * Provides information about an event that a BlockEvent has emitted. e.g. damage, break, use
 */
public interface BlockEvent {

    /**
     * The world from where the event was emitted.
     *
     * @return The world from where the event was emitted.
     */
    World getWorld();

    /**
     * Provides the location from where the event was emitted.
     *
     * @return the location from where the event was emitted.
     */
    Coordinates getLocation();

}
