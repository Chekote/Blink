package com.monkeyinabucket.forge.blink.rune;

import com.google.gson.JsonObject;
import com.monkeyinabucket.forge.blink.Blink;
import net.minecraft.block.Block;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.util.ForgeDirection;

import com.monkeyinabucket.forge.blink.group.BlinkGroup;
import com.monkeyinabucket.forge.blink.group.NoSuchMemberException;
import com.monkeyinabucket.forge.world.Location;

/**
 * Represents a BlinkRune.
 */
public class BlinkRune extends Rune implements Comparable<BlinkRune> {

  /** The BlinkSignature of this BlinkRune */
  protected BlinkSignature signature;

  /** The BlinkGroup that this BlinkRune belongs to */
  protected BlinkGroup group;

  protected RuneManager runeManager;

  /**
   * Constructor
   * 
   * @param loc	The Location of the BlinkRune
   */
  public BlinkRune(Location loc) {
    // save defensive copy
    this.loc = loc.clone();

    this.signature = new BlinkSignature(loc.getRelative(ForgeDirection.NORTH).getBlock(), loc
        .getRelative(ForgeDirection.EAST).getBlock(), loc.getRelative(ForgeDirection.SOUTH)
        .getBlock(), loc.getRelative(ForgeDirection.WEST).getBlock());

    runeManager = RuneManager.getInstance();
  }

  /**
   * Constructor
   *
   * @param loc the Location of the BlinkRune.
   * @param sig the Signature of the BlinkRune.
   */
  public BlinkRune(Location loc, BlinkSignature sig) {
    // save defensive copies
    this.loc = loc.clone();
    this.signature = sig.clone();

    runeManager = RuneManager.getInstance();
  }

  /**
   * Provides a defensive copy of the location of the center Block if this
   * BlinkRune
   *
   * @return the Location
   */
  public Location getLocation() {
    return loc.clone();
  }

  /**
   * Provides the signature of this BlinkRune
   *
   * @return the BlinkSignature
   */
  public BlinkSignature getSignature() {
    return signature;
  }

  /**
   * Sets the BlinkGroup that this BlinkRune belongs to
   * 
   * @param group the group that this BlinkRune belongs to.
   */
  public void setGroup(BlinkGroup group) {
    this.group = group;
  }

  /**
   * Gets the BlinkGroup that this BlinkRune belongs to
   * 
   * @return the BlinkGroup
   */
  public BlinkGroup getGroup() {
    return group;
  }

  /**
   * Invoked when any Block in this BlinkRune is damaged. Damaging a Block of a
   * BlinkRune will set the entire BlinkRune on fire if the "burning" config option is enabled.
   */
  public void onDamage() {
    if (Blink.burning) {
      setFire();
    }
  }

  /**
   * Sets the surface of the rune alight.
   */
  private void setFire() {
    for (Location loc : getParts()) {
      if (loc.world.isAirBlock(loc.x, loc.y + 1, loc.z)) {
        loc.getRelative(ForgeDirection.UP).setBlock(Blocks.fire);
      }
    }
  }

  /**
   * Invoked when a BlinkRune is created. A successfully created BlinkRune will
   * be struck by lightning.
   */
  public void onCreate() {
    loc.world.addWeatherEffect(new EntityLightningBolt(loc.world, loc.x, loc.y + 1, loc.z));
  }

  /**
   * Invoked when a BlinkRune is destroyed. A destroyed BlinkRune will be struck
   * by lightning.
   */
  public void onDestroy() {
    loc.world.addWeatherEffect(new EntityLightningBolt(loc.world, loc.x, loc.y + 1, loc.z));
    runeManager.removeRune(this);
  }

  /**
   * Activates the BlinkRune, teleporting the Player to the next BlinkRune in the BlinkGroup.
   * 
   * @param player the Player that activated the BlinkRune
   */
  public void activate(EntityPlayer player) {
    BlinkRune targetRune;
    try {
      targetRune = group.getNext(this);
    } catch (NoSuchMemberException e) {
      return;
    }

    if (targetRune == null) {
      return;
    }

    // We need to find two stacked blocks of empty space for the player to blink
    // safely, this could ultimately be the top of the world.
    Location targetLoc = targetRune.getLocation().getRelative(ForgeDirection.UP);
    while (targetLoc.getBlock() != null) {
      if (isValidDestination(targetLoc)) {
        break;
      }

      targetLoc = targetLoc.getRelative(ForgeDirection.UP);
    }

    if (targetLoc.world.provider.dimensionId != player.dimension) {
      player.travelToDimension(targetLoc.world.provider.dimensionId);
    }

    player.setPositionAndUpdate(targetLoc.x + 0.5, targetLoc.y, targetLoc.z + 0.5);
  }

  /**
   * Determines if a Location is a valid teleport destination for a Player. A
   * Location is considered a valid destination if it is not solid and the Block
   * directly above it is also not solid. This will provide enough room for the
   * Player to teleport in without suffocating.
   * 
   * @param loc the Block to check
   * @return true if the destination is valid, false if not
   */
  protected boolean isValidDestination(Location loc) {
    Block block = loc.getBlock();
    if (block == null) {
      return true;
    }

    if (!block.getMaterial().isSolid()) {
      Block blockAbove = loc.getRelative(ForgeDirection.UP).getBlock();

      if (blockAbove == null || !blockAbove.getMaterial().isSolid()) {
        return true;
      }
    }

    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return "BlinkRune" + toJson().toString();
  }

  /**
   * Provides a JsonObject that describes this BlinkRune.
   *
   * This method is intended for use in saving data, and debugging.
   *
   * @return the builder.
   */
  public JsonObject toJson() {
    return loc.toJson();
  }

  /**
   * {@inheritDoc}
   */
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
