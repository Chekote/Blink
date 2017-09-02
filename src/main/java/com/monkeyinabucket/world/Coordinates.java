package com.monkeyinabucket.world;

/**
 * Describes a position within a world.
 */
public class Coordinates {

  /** The x coordinate of this location */
  public int x;

  /** The y coordinate of this location */
  public int y;

  /** The z coordinate of this location */
  public int z;

  /**
   * Constructor for creating a location from specific coordinates.
   *
   * @param x the x coordination for the location.
   * @param y the y coordination for the location.
   * @param z the z coordination for the location.
   */
  public Coordinates(int x, int y, int z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  /**
   * Returns a new Coordinates relative to this Coordinates.
   *
   * @param  relativeLoc the relative location.
   * @return the location.
   */
  public Coordinates getRelative(RelativeLocation relativeLoc) {
    return new Coordinates(x + relativeLoc.offsetX, y + relativeLoc.offsetY, z + relativeLoc.offsetZ);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Coordinates clone() {
    return new Coordinates(x, y, z);
  }

  /**
   * Determines if this Coordinates is equal to the specified Coordinates.
   *
   * A Coordinates is considered equal if it is within the same dimension, and the x, y & z coordinates
   * are the same.
   *
   * @param loc the Coordinates to compare.
   * @return true if the location is equal, false if not.
   */
  public boolean equals(Coordinates loc) {
    return this.x == loc.x && this.y == loc.y && this.z == loc.z;
  }

  /**
   * {@inheritDoc}
   */
  public String toString() {
    return "Coordinates{x=" + x + ", y=" + y + ", z=" + z + "}";
  }
}
