package com.monkeyinabucket.blink.rune;

import java.util.ArrayList;
import java.util.Collection;

import com.monkeyinabucket.blink.Blink;
import com.monkeyinabucket.blink.Config;
import com.monkeyinabucket.blink.minecraft.World;
import com.monkeyinabucket.world.Coordinates;

/**
 * General Rune class. This class could eventually be the base class for other
 * Runes.
 */
public class Rune {

  /** The coordinates of the rune's core within the world */
  protected Coordinates coords;

  /** The world that the rune is located within */
  protected World world;

  /** The one rune manager */
  public static RuneManager manager = RuneManager.getInstance();

  public Rune(World world, Coordinates coords) {
    this.world = world;

    // save defensive copy
    this.coords = coords.clone();
  }

  /**
   * provides a list of all locations that comprise this BlinkRune
   * 
   * @return the list of locations
   */
  public Collection<Coordinates> getParts() {
    Config config = Blink.getInstance().config;

    int startZ = coords.z - config.halfRuneSize;
    int startX = coords.x - config.halfRuneSize;

    ArrayList<Coordinates> parts = new ArrayList<Coordinates>();
    for (int col = 0; col < config.runeSize; ++col) {
      for (int row = 0; row < config.runeSize; ++row) {
        Coordinates nextLoc = new Coordinates(startX + row, coords.y, startZ + col);

        parts.add(nextLoc);
      }
    }

    return parts;
  }

  /**
   * Determines if the specified block is a part of this rune.
   * 
   * @param otherWorld the world that the block is located within.
   * @param otherLoc   the coordinates of the block to check.
   * @return true if the Coordinates is part of this BlinkRune, false if not.
   */
  public boolean isPart(World otherWorld, Coordinates otherLoc) {
    Config config = Blink.getInstance().config;

    return world.equals(otherWorld)
        && otherLoc.y == coords.y && otherLoc.z >= coords.z - config.halfRuneSize && otherLoc.z <= coords.z + config.halfRuneSize
        && otherLoc.x >= coords.x - config.halfRuneSize && otherLoc.x <= coords.x + config.halfRuneSize;
  }

  /**
   * determines if the specified rune shares one or more blocks with this rune.
   * 
   * @param rune the rune to check
   * @return true if the runes overlap, false if not
   */
  public boolean overlaps(Rune rune) {
    Collection<Coordinates> parts = rune.getParts();

    for (Coordinates part : parts) {
      if (isPart(rune.world, part)) {
        return true;
      }
    }

    return false;
  }
}
