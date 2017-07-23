package com.monkeyinabucket.forge.world;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;

/**
 * Describes a position within a world.
 */
public class Location {

  /** The world that this location is within */
  public World world;

  /** The coordinates of this location */
  public BlockPos pos;

  /**
   * Constructor for creating a location from specific coordinates.
   *
   * @param world the world that the location is within.
   * @param pos   the coordinates of the block.
   */
  public Location(World world, BlockPos pos) {
    this.world = world;
    this.pos = pos;
  }

  /**
   * Constructor for creating a Location from a BlockEvent.
   *
   * @param event the BlockEvent to create the Location from.
   */
  public Location(BlockEvent event) {
    this.world = event.getWorld();
    this.pos = event.getPos();
  }

  /**
   * Constructor for creating a Location from a PlayerInteractEvent.
   *
   * @param event the PlayerInteract to create the Location from.
   */
  public Location(PlayerInteractEvent event) {
    this.world = event.getWorld();
    this.pos = event.getPos();
  }

  /**
   * Returns the Block that this Location points to.
   *
   * @return the block.
   */
  public Block getBlock() {
    return world.getBlockState(pos).getBlock();
  }

  /**
   * Places the specified block at this Location.
   *
   * @param block the block to place.
   */
  public void setBlock(Block block) {
    world.setBlockState(pos, block.getDefaultState());
  }

  /**
   * Returns a new Location relative to this Location.
   *
   * @param direction the relative direction.
   * @return the location.
   */
  public Location getRelative(EnumFacing direction) {
    BlockPos relative = pos.add(direction.getFrontOffsetX(), direction.getFrontOffsetY(), direction.getFrontOffsetZ());

    return new Location(world, relative);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Location clone() {
    return new Location(world, pos);
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
    return this.world.provider.getDimension() == loc.world.provider.getDimension() && pos.equals(loc.pos);
  }

  /**
   * {@inheritDoc}
   */
  public String toString() {
    return "Location{" +
      "d=" + world.provider.getDimension() +
      ", x=" + pos.getX() +
      ", y=" + pos.getY() +
      ", z=" + pos.getZ() +
    "}";
  }
}
