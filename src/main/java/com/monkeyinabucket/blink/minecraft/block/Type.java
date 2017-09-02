package com.monkeyinabucket.blink.minecraft.block;

import com.monkeyinabucket.world.Coordinates;

/**
 * Represents an type of block in Minecraft. This does NOT represent an individual block in the world. Minecraft
 * only has one instance of each block type.
 *
 * For an individual block within a world, you must have both a {@link Coordinates} and a
 * {@link Type}.
 */
public interface Type {

  /**
   * Determines if the block is of the specified type
   *
   * @param  type the type to check for.
   * @return true if this block is of the specified type. false if not.
   */
  boolean is(String type);

  /**
   * Determines if the block is air.
   *
   * @return true if this block is air, false if not.
   */
  boolean isAir();

  /**
   * Determines if the block is solid.
   *
   * @return true if this block is solid, false if not.
   */
  boolean isSolid();
}
