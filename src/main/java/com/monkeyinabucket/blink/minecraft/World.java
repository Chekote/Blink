package com.monkeyinabucket.blink.minecraft;

import com.monkeyinabucket.blink.minecraft.block.Type;
import com.monkeyinabucket.world.Coordinates;

/**
 * Provides methods for interacting with a specific world.
 */
public interface World {

  /**
   * Creates a weather effect in the world.
   *
   * @param x          the x coordinate where the lightning should strike.
   * @param y          the y coordinate where the lightning should strike.
   * @param z          the z coordinate where the lightning should strike.
   * @param visualOnly true if the lightning strike should be visual only, false if it should case damage/fire.
   */
  void lightningStrike(int x, int y, int z, boolean visualOnly);

  /**
   * Determines if this world is the same as the specified world.
   *
   * @param world the world to compare.
   * @return true if the worlds are equivalent, false if not.
   */
  boolean equals(World world);

  /**
   * Returns the block at the specified coordinates.
   *
   * @param  x the x coordinate of the block.
   * @param  y the y coordinate of the block.
   * @param  z the z coordinate of the block.
   * @throws IndexOutOfBoundsException if any of the coordinates are outside of the bounds of the world.
   * @return the block at the specified coordinates.
   */
  Type getBlock(int x, int y, int z) throws IndexOutOfBoundsException;

  /**
   * Returns the block at the specified coordinates.
   *
   * @param  coords the coordinates of the block to get.
   * @throws IndexOutOfBoundsException if any of the coordinates are outside of the bounds of the world.
   * @return the block at the specified coordinates.
   */
  Type getBlock(Coordinates coords) throws IndexOutOfBoundsException;

  /**
   * Provides the id number for this world.
   *
   * @return the id number for the world.
   */
  int getId();

  /**
   * Sets the block type at the specified coordinates.
   *
   * @param  x     the x coordinate for the block.
   * @param  y     the y coordinate for the block.
   * @param  z     the z coordinate for the block.
   * @param  block the block to set at the specified coordinates.
   * @throws IndexOutOfBoundsException if any of the coordinates are outside of the bounds of the world.
   */
  void setBlock(int x, int y, int z, Type block) throws IndexOutOfBoundsException;

}
