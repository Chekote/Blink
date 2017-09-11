package com.monkeyinabucket.forge.world;

import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;

import javax.json.Json;
import javax.json.JsonObjectBuilder;

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
    this.world = event.world;
    this.pos = event.pos;
  }

  /**
   * Constructor for creating a Location from a PlayerInteractEvent.
   *
   * @param event the PlayerInteract to create the Location from.
   */
  public Location(PlayerInteractEvent event) {
    this.world = event.world;
    this.pos = event.pos;
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
    return this.world.provider.getDimensionId() == loc.world.provider.getDimensionId() && pos.equals(loc.pos);
  }

  /**
   * {@inheritDoc}
   */
  public String toString() {
    return "Location" + toJsonBuilder().build().toString();
  }

  /**
   * Provides a JsonObjectBuilder that describes this Location.
   *
   * This method is intended for use in saving data, and debugging.
   *
   * @return the builder.
   */
  public JsonObjectBuilder toJsonBuilder() {
    return Json.createObjectBuilder()
        .add("d", world.provider.getDimensionId())
        .add("x", pos.getX())
        .add("y", pos.getY())
        .add("z", pos.getZ());
  }
}
