package com.monkeyinabucket.blink.rune;

import com.monkeyinabucket.blink.Blink;
import com.monkeyinabucket.blink.group.BlinkGroup;
import com.monkeyinabucket.blink.group.NoSuchMemberException;
import com.monkeyinabucket.blink.minecraft.block.Registry;
import com.monkeyinabucket.blink.minecraft.block.Type;
import com.monkeyinabucket.blink.minecraft.Player;
import com.monkeyinabucket.blink.minecraft.World;
import com.monkeyinabucket.world.Coordinates;
import com.monkeyinabucket.world.RelativeLocation;

import javax.annotation.Nonnull;

/**
 * Represents a BlinkRune.
 */
public class BlinkRune extends Rune implements Comparable<BlinkRune> {

  /** The BlinkSignature of this BlinkRune */
  private BlinkSignature signature;

  /** The BlinkGroup that this BlinkRune belongs to */
  private BlinkGroup group;

  /**
   * Constructor
   *
   * @param world  the world that the BlinkRune is located in.
   * @param coords the Coordinates of the BlinkRune
   */
  public BlinkRune(World world, Coordinates coords) {
    super(world, coords);

    this.signature = new BlinkSignature(
      world.getBlock(coords.getRelative(RelativeLocation.NORTH)),
      world.getBlock(coords.getRelative(RelativeLocation.EAST)),
      world.getBlock(coords.getRelative(RelativeLocation.SOUTH)),
      world.getBlock(coords.getRelative(RelativeLocation.WEST))
    );
  }

  /**
   * Provides a defensive copy of the location of the center BlockEvent if this
   * BlinkRune
   *
   * @return the Coordinates
   */
  Coordinates getLocation() {
    return coords.clone();
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
  void setGroup(BlinkGroup group) {
    this.group = group;
  }

  /**
   * Gets the BlinkGroup that this BlinkRune belongs to
   * 
   * @return the BlinkGroup
   */
  BlinkGroup getGroup() {
    return group;
  }

  /**
   * Invoked when any BlockEvent in this BlinkRune is damaged. Damaging a BlockEvent of a
   * BlinkRune will set the entire BlinkRune on fire.
   */
  public void onDamage() {
    for (Coordinates coords : getParts()) {
      if (world.getBlock(coords.x, coords.y + 1, coords.z).isAir()) {
        Coordinates blockAbove = coords.getRelative(RelativeLocation.UP);
        world.setBlock(
            blockAbove.x,
            blockAbove.y,
            blockAbove.z,
            Blink.getInstance().environment.getBlockRegistry().get(Registry.FIRE)
        );
      }
    }
  }

  /**
   * Invoked when a BlinkRune is created. A successfully created BlinkRune will
   * be struck by lightning.
   */
  public void onCreate() {
    world.lightningStrike(coords.x, coords.y + 1, coords.z, true);
  }

  /**
   * Invoked when a BlinkRune is destroyed. A destroyed BlinkRune will be struck
   * by lightning.
   */
  public void onDestroy() {
    world.lightningStrike(coords.x, coords.y + 1, coords.z, true);
    manager.removeRune(this);
  }

  /**
   * Activates the BlinkRune, teleporting the Player to the next BlinkRune in the BlinkGroup.
   * 
   * @param player the Player that activated the BlinkRune
   */
  public void activate(Player player) {
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
    Coordinates targetCoords = targetRune.getLocation().getRelative(RelativeLocation.UP);
    World targetWorld = targetRune.world;
    while (targetWorld.getBlock(targetCoords) != null) {
      if (isValidDestination(targetWorld, targetCoords)) {
        break;
      }

      targetCoords = targetCoords.getRelative(RelativeLocation.UP);
    }

    if (!targetWorld.equals(player.getWorld())) {
      player.setWorld(targetWorld);
    }

    player.setPosition(targetCoords.x + 0.5, targetCoords.y, targetCoords.z + 0.5);
  }

  /**
   * Determines if a the specified destination is valid teleport destination for a Player. A destination is considered
   * valid if it is not solid and the block directly above it is also not solid. This will provide enough room for the
   * Player to teleport in without suffocating.
   *
   * @param world  the world that the destination is within.
   * @param coords the coordinates of the destination.
   * @return true if the destination is valid, false if not
   */
  private boolean isValidDestination(World world, Coordinates coords) {
    Type block = world.getBlock(coords);
    if (block == null) {
      return true;
    }

    if (!block.isSolid()) {
      Type blockAbove = world.getBlock(coords.getRelative(RelativeLocation.UP));

      if (blockAbove == null || !blockAbove.isSolid()) {
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
    return "BlinkRune{" +
      "dimension=" + world.getId() +
      ", x=" + coords.x +
      ", y=" + coords.y +
      ", z=" + coords.z +
    "}";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int compareTo(@Nonnull BlinkRune rune) {
    if (rune.getLocation().equals(coords)) {
      return 0;
    }

    return -1;
  }
}
