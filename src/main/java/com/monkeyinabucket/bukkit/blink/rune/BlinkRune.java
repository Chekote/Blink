package com.monkeyinabucket.bukkit.blink.rune;

import com.monkeyinabucket.bukkit.blink.BlinkSignature;
import com.monkeyinabucket.bukkit.blink.Plugin;
import com.monkeyinabucket.bukkit.blink.RuneManager;
import com.monkeyinabucket.bukkit.blink.group.BlinkGroup;
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
 * Represents a BlinkRune.
 * @author Donald Tyler (chekote69@gmail.com)
 */
public class BlinkRune implements Comparable<BlinkRune> {

  /** The Location of the center Block of this BlinkRune */
  protected Location loc;

  /** The BlinkSignature of this BlinkRune */
  protected BlinkSignature signature;

  /** The BlinkGroup that this BlinkRune belongs to */
  protected BlinkGroup group;

  protected RuneManager runeManager;

  /**
   * Constructor
   * @param block 
   */
  public BlinkRune(Block block) {
    this.loc = block.getLocation();
    this.signature = new BlinkSignature(
      block.getRelative(BlockFace.NORTH).getType(),
      block.getRelative(BlockFace.EAST).getType(),
      block.getRelative(BlockFace.SOUTH).getType(),
      block.getRelative(BlockFace.WEST).getType()
    );
    
    runeManager = RuneManager.getInstance();
  }

  /**
   * Constructor
   * @param loc 
   */
  public BlinkRune(Location loc) {
    // save defensive copy
    this.loc = loc.clone();
  }

  /**
   * Provides a defensive copy of the location of the center Block if this BlinkRune
   * @return the Location
   */
  public Location getLocation() {
    return loc.clone();
  }

  /**
   * Provides the signature of this BlinkRune
   * TODO: return a defensive copy
   * @return the BlinkSignature
   */
  public BlinkSignature getSignature() {
    return signature;
  }

  /**
   * Sets the BlinkGroup that this BlinkRune belongs to
   * @param group 
   */
  public void setGroup(BlinkGroup group) {
    this.group = group;
  }

  /**
   * Gets the BlinkGroup that this BlinkRune belongs to
   * @return the BlinkGroup
   */
  public BlinkGroup getGroup() {
    return group;
  }

  /**
   * provides a list of all Blocks that comprise this BlinkRune
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
   * Determines if the specified Block is a part of this BlinkRune
   * @param block the Block to check
   * @return true if the Block is a part of this BlinkRune, false if not
   */
  public boolean isPart(Block block) {
    return isPart(block.getLocation());
  }

  /**
   * Determines if the specified Location is a part of this BlinkRune
   * @param otherLoc the Location to check
   * @return true if the Location is part of this BlinkRune, false if not
   */
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
    Collection<Block> parts = rune.getParts();

    for (Block b : parts) {
      if (isPart(b)) {
        return true;
      }
    }

    return false;
  }

  /**
   * Invoked when any Block in this BlinkRune is damaged. Damaging a Block of a BlinkRune will
   * set the entire BlinkRune on fire.
   */
  public void onDamage() {
    for(Block b : getParts()) {
      Block above = b.getRelative(BlockFace.UP);
      if (above.getType() == Material.AIR) {
        above.setType(Material.FIRE);
      }
    }
  }

  /**
   * Invoked when a BlinkRune is created. A successfully created BlinkRune will be struck by
   * lightning.
   */
  public void onCreate() {
    loc.getWorld().strikeLightningEffect(loc);
  }

  /**
   * Invoked when a BlinkRune is destroyed. A destroyed BlinkRune will be struck by lightning.
   */
  public void onDestroy() {
    loc.getWorld().strikeLightningEffect(loc);
    runeManager.removeRune(this);
  }

  /**
   * Activates the BlinkRune, teleporting the Player to the next BlinkRune in the BlinkGroup. Note:
   * @param player the Player that activated the BlinkRune
   */
  public void activate(Player player) {
    BlinkRune targetRune = null;
    try {
      targetRune = group.getNext(this);
    } catch (NoSuchMemberException e) {
      Plugin.logSevere("Failed to activate rune. It's group claimed it was not a member!");
      return;
    }

    if (targetRune == null) {
      return;
    }

    Location targetLoc = null;

    // we need to find two stacked blocks of empty space for the player to blink safely, this
    // could ultimately be the top of the world.
    Block currentBlock = targetRune.getLocation().getBlock().getRelative(BlockFace.UP);
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

        currentBlock = currentBlock.getRelative(BlockFace.UP);
      }
    }

    player.teleport(targetLoc);
  }

  /**
   * Determines if a Block is a valid teleport destination for a Pleyer. A Block is considered a
   * valid destination if it is not solid and the Block directly above it is also not solid. This
   * will provide enough room for the Player to teleport in without suffocating.
   * @param block the Block to check
   * @return true if the destination is valid, false if not
   */
  protected boolean isValidDestination(Block block) {
    if (block == null) {
      return true;
    }

    if (!blockIsSolid(block) && !blockIsSolid(block.getRelative(BlockFace.UP))) {
      return true;
    }

    return false;
  }

  /**
   * Determines if a Block is solid. A solid Block is any Block that would suffocate a Player.
   * @param block the Block to check
   * @return true if the Block is solid, false if not
   */
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

  @Override
  public int compareTo(BlinkRune rune) {
    if (rune == null) {
      return 0;
    }

    if (rune.getLocation().equals(loc)) {
      return 0;
    }
    
    return -1;
  }
}
