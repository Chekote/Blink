package com.monkeyinabucket.forge.world;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import java.io.Serializable;

/**
 * Wrapper for a Location that implements Serializable. This is used to save the location of runes.
 */
public class SerializableLocation implements Serializable {

  /** The ID of the dimension that this location is within */
  private int dimensionId;

  /** The x coordinate of this location */
  private int x;

  /** The y coordinate of this location */
  private int y;

  /** The z coordinate of this location */
  private int z;

  /**
   * Constructor
   * 
   * @param loc the location to serialize
   */
  public SerializableLocation(Location loc) {
    dimensionId = loc.world.provider.getDimensionId();
    x = loc.pos.getX();
    y = loc.pos.getY();
    z = loc.pos.getZ();
  }

  /**
   * Provides the Location that this object serialized.
   * 
   * @param server the server that we are running in
   * @return the Location
   */
  public Location getLocation(MinecraftServer server) {
    World world = null;
    for (World nextWorld : server.worldServers) {
      if (nextWorld.provider.getDimensionId() == dimensionId) {
        world = nextWorld;
        break;
      }
    }

    return new Location(world, new BlockPos(x, y, z));
  }
}
