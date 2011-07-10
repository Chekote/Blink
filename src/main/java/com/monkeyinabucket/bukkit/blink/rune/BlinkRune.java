package com.monkeyinabucket.bukkit.blink.rune;

import com.monkeyinabucket.bukkit.blink.Plugin;
import com.monkeyinabucket.bukkit.blink.group.BlinkGroup;
import com.monkeyinabucket.bukkit.blink.BlinkSignature;
import com.monkeyinabucket.bukkit.blink.group.NoSuchMemberException;
import java.util.ArrayList;
import java.util.Collection;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

/**
 *
 * @author dtyler
 */
public class BlinkRune {

  protected Location loc;
  protected BlinkSignature signature;
  protected BlinkGroup group;

  public BlinkRune(Block block) {
    this.loc = block.getLocation();
    this.signature = new BlinkSignature(
      block.getFace(BlockFace.NORTH).getType(),
      block.getFace(BlockFace.EAST).getType(),
      block.getFace(BlockFace.SOUTH).getType(),
      block.getFace(BlockFace.WEST).getType()
    );
  }

  public BlinkRune(Location loc) {
    // save defensive copy
    this.loc = loc.clone();
  }

  public Location getLocation() {
    // return a defensive copy
    return loc.clone();
  }

  public BlinkSignature getSignature() {
    return signature;
  }

  public void setGroup(BlinkGroup group) {
    this.group = group;
  }

  public BlinkGroup getGroup() {
    return group;
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
  public boolean overlaps(BlinkRune rune) {
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

  public void onCreate() {
    loc.getWorld().strikeLightningEffect(loc);
  }

  public void onDestroy() {
    loc.getWorld().strikeLightningEffect(loc);
  }

  public void activate(Player player) {
    BlinkRune targetRune = null;
    try {
      targetRune = group.getNext(this);
    } catch (NoSuchMemberException e) {
      Plugin.logSevere("Failed to activate rune. It's group claimed it was not a member!");
      return;
    }

    if (targetRune == null) {
      Plugin.logInfo("Failed to activate rune. No other blink runes in group");
      return;
    }

    Location targetLoc = null;

    // we need to find two stacked blocks of empty space for the player to blink safely, this
    // could ultimately be the top of the world.
    Block currentBlock = targetRune.getLocation().getBlock().getFace(BlockFace.UP);
    if (currentBlock == null) {
      // there is no block above the rune, so it must be at the top of the world. Blink directly
      // above it.
      targetLoc = targetRune.getLocation();
      targetLoc.setY(targetLoc.getY() + 1);
    } else {
      // proceed to check all blocks vertically, until we get to a valid destination.
      while (currentBlock != null) {
        if (isValidDestination(currentBlock)) {
          targetLoc = currentBlock.getLocation();
          break;
        }

        currentBlock = currentBlock.getFace(BlockFace.UP);
      }
    }

    player.teleport(targetLoc);
  }

  protected boolean isValidDestination(Block block) {
    if (block == null) {
      return true;
    }

    if (!blockIsSolid(block) && !blockIsSolid(block.getFace(BlockFace.UP))) {
      return true;
    }

    return false;
  }

  protected boolean blockIsSolid(Block block) {
    if (block == null) {
      return false;
    }

    Material m = block.getType();
    if (
        m == Material.AIR ||
        m == Material.LAVA ||
        m == Material.STATIONARY_LAVA ||
        m == Material.CROPS ||
        m == Material.DEAD_BUSH ||
        m == Material.DETECTOR_RAIL ||
        m == Material.SAPLING ||
        m == Material.WATER ||
        m == Material.STATIONARY_WATER ||
        m == Material.WALL_SIGN ||
        m == Material.POWERED_RAIL ||
        m == Material.LONG_GRASS ||
        m == Material.YELLOW_FLOWER ||
        m == Material.RED_ROSE ||
        m == Material.BROWN_MUSHROOM ||
        m == Material.RED_MUSHROOM ||
        m == Material.TORCH ||
        m == Material.FIRE ||
        m == Material.REDSTONE_WIRE ||
        m == Material.CROPS ||
        m == Material.WOODEN_DOOR ||
        m == Material.LADDER ||
        m == Material.RAILS ||
        m == Material.LEVER ||
        m == Material.STONE_PLATE ||
        m == Material.IRON_DOOR_BLOCK ||
        m == Material.REDSTONE_TORCH_OFF ||
        m == Material.REDSTONE_TORCH_ON ||
        m == Material.STONE_BUTTON ||
        m == Material.PORTAL ||
        m == Material.DIODE_BLOCK_OFF ||
        m == Material.DIODE_BLOCK_ON ||
        m == Material.TRAP_DOOR
        ) {
      return false;
    }

    return true;
  }
}
