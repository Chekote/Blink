package com.monkeyinabucket.forge.world;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;

/**
 * Describes a position within a world.
 */
public class Location {

  /** The world that this location is within */
  public World world;

  /** The x coordinate of this location */
  public int x;

  /** The y coordinate of this location */
  public int y;

  /** The z coordinate of this location */
  public int z;

  /**
   * Constructor for creating a location from specific coordinates.
   *
   * @param world the world that the location is within.
   * @param x the x coordination for the location.
   * @param y the y coordination for the location.
   * @param z the z coordination for the location.
   */
  public Location(World world, int x, int y, int z) {
    this.world = world;
    this.x = x;
    this.y = y;
    this.z = z;
  }

  /**
   * Constructor for creating a Location from a BlockEvent.
   *
   * @param event the BlockEvent to create the Location from.
   */
  public Location(BlockEvent event) {
    this.world = event.world;
    this.x = event.x;
    this.y = event.y;
    this.z = event.z;
  }

  /**
   * Constructor for creating a Location from a PlayerInteractEvent.
   *
   * @param event the PlayerInteract to create the Location from.
   */
  public Location(PlayerInteractEvent event) {
    this.world = event.entityPlayer.worldObj;
    this.x = event.x;
    this.y = event.y;
    this.z = event.z;
  }

  /**
   * Returns the Block that this Location points to.
   *
   * @return the block.
   */
  public Block getBlock() {
    return world.getBlock(x, y, z);
  }

  /**
   * Places the specified block at this Location.
   *
   * @param block the block to place.
   */
  public void setBlock(Block block) {
    world.setBlock(x, y, z, block);
  }

  /**
   * Returns a new Location relative to this Location.
   *
   * @param direction the relative direction.
   * @return the location.
   */
  public Location getRelative(ForgeDirection direction) {
    return new Location(world, x + direction.offsetX, y + direction.offsetY, z + direction.offsetZ);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Location clone() {
    return new Location(world, x, y, z);
  }

  /**
   * Determines if this Location is equal to the specified Location.
   *
   * A Location is considered equal if it is within the same dimension, and the x, y & z coordinates
   * are the same.
   *
   * @param loc the Location to compare.
   * @return true if the location is equal, false if not.
   */
  public boolean equals(Location loc) {
    return this.world.provider.dimensionId == loc.world.provider.dimensionId && this.x == loc.x
        && this.y == loc.y && this.z == loc.z;
  }

  /**
   * {@inheritDoc}
   */
  public String toString() {
    StringBuilder sb = new StringBuilder().append("Location{").append("d=")
        .append(world.provider.dimensionId).append(", x=").append(x).append(", y=").append(y)
        .append(", z=").append(z).append("}");

    return sb.toString();
  }
}
