package com.monkeyinabucket.world;

/**
 * Describes a relative position within a world.
 */
public enum RelativeLocation
{
  /** One block down (-Y) */
  DOWN(0, -1, 0),

  /** One block up (+Y) */
  UP(0, 1, 0),

  /** One block north (-Z) */
  NORTH(0, 0, -1),

  /** One block south (+Z) */
  SOUTH(0, 0, 1),

  /** One block west (-X) */
  WEST(-1, 0, 0),

  /** One block west (+X) */
  EAST(1, 0, 0);

  /** the x coordinate offset */
  public final int offsetX;

  /** the y coordinate offset */
  public final int offsetY;

  /** the z coordinate offset */
  public final int offsetZ;

  /**
   * Constructor
   *
   * @param x the x coordinate offset
   * @param y the y coordinate offset
   * @param z the z coordinate offset
   */
  RelativeLocation(int x, int y, int z)
  {
    offsetX = x;
    offsetY = y;
    offsetZ = z;
  }
}
