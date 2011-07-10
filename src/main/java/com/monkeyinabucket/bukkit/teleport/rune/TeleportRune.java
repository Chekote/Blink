package com.monkeyinabucket.bukkit.teleport.rune;

import com.monkeyinabucket.bukkit.teleport.Plugin;
import com.monkeyinabucket.bukkit.teleport.TeleportSignature;
import java.util.ArrayList;
import java.util.Collection;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

/**
 *
 * @author dtyler
 */
public class TeleportRune {

  protected Location loc;
  protected TeleportSignature signature;

  public TeleportRune(Block block) {
    this.loc = block.getLocation();
    this.signature = new TeleportSignature(
      block.getFace(BlockFace.NORTH).getType(),
      block.getFace(BlockFace.EAST).getType(),
      block.getFace(BlockFace.SOUTH).getType(),
      block.getFace(BlockFace.WEST).getType()
    );
  }

  public TeleportRune(Location loc) {
    this.loc = loc;
  }

  public Location getLocation() {
    return loc;
  }

  public TeleportSignature getSignature() {
    return signature;
  }

  /**
   * provides a list of all blocks that comprise this rune
   * @return the list of blocks
   */
  public Collection<Block> getParts() {
    World world = loc.getWorld();

    Location nextLoc = loc.clone();
    double startZ = nextLoc.getZ() - 2;
    double startX = nextLoc.getX() - 2;

    ArrayList<Block> parts = new ArrayList<Block>();
    for (int col = 0; col < 5; ++col) {
      nextLoc.setZ(startZ + col);

      for (int row = 0; row < 5; ++row) {
        nextLoc.setX(startX + row);

        parts.add(world.getBlockAt(nextLoc));
      }
    }

    return parts;
  }

  /**
   * determines if the specified block is a part of this rune
   * @param block the block to check
   * @return true if the block is a part of this rune, false if not
   */
  public boolean isPart(Block block) {
    return isPart(block.getLocation());
  }

  public boolean isPart(Location otherLoc) {
    return
      otherLoc.getWorld().equals(loc.getWorld()) &&
      otherLoc.getY() == loc.getY() &&
      otherLoc.getZ() >= loc.getZ() - 2 &&
      otherLoc.getZ() <= loc.getZ() + 2 &&
      otherLoc.getX() >= loc.getX() - 2 &&
      otherLoc.getX() <= loc.getX() + 2;
  }

  /**
   * determines if the specified rune shares one or more blocks with this rune.
   * @param rune the rune to check
   * @return true if the runes overlap, false if not
   */
  public boolean overlaps(TeleportRune rune) {
    Plugin.logInfo("Checking for overlap...");
    
    Collection<Block> parts = rune.getParts();

    for (Block b : parts) {
      Plugin.logInfo("Processing block: " + b);

      if (isPart(b)) {
        Plugin.logInfo("overlap found");
        return true;
      }
    }

    Plugin.logInfo("no overlap");
    return false;
  }

  public void onDamage() {
    for(Block b : getParts()) {
      Block above = b.getFace(BlockFace.UP);
      if (above.getType() == Material.AIR) {
        above.setType(Material.FIRE);
      }
    }
  }

  public void activate() {
    // TODO: implement
  }
}
