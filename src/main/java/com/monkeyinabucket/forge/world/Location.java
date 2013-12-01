package com.monkeyinabucket.forge.world;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;

public class Location {

  public World world;
  public int x;
  public int y;
  public int z;

  public Location(World world, int x, int y, int z) {
    this.world = world;
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public Location(BlockEvent event) {
    this.world = event.world;
    this.x = event.x;
    this.y = event.y;
    this.z = event.z;
  }

  public Location(PlayerInteractEvent event) {
    this.world = event.entityPlayer.worldObj;
    this.x = event.x;
    this.y = event.y;
    this.z = event.z;
  }

  public Block getBlock() {
	return world.getBlock(x, y, z);
  }

  public void setBlock(Block block) {
    world.setBlock(x, y, z, block);
  }

  public Location getRelative(ForgeDirection direction) {
    return new Location(world, x + direction.offsetX, y + direction.offsetY, z + direction.offsetZ);
  }

  @Override
  public Location clone() {
    return new Location(world, x, y, z);
  }

  public boolean equals(Location loc) {
    return this.world.provider.dimensionId == loc.world.provider.dimensionId && this.x == loc.x
        && this.y == loc.y && this.z == loc.z;
  }

  public String toString() {
    StringBuilder sb = new StringBuilder().append("Location{").append("d=")
        .append(world.provider.dimensionId).append(", x=").append(x).append(", y=").append(y)
        .append(", z=").append(z).append("}");

    return sb.toString();
  }
}
