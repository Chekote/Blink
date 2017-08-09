package com.monkeyinabucket.forge.blink.rune;

import java.util.ArrayList;
import java.util.Collection;

import com.monkeyinabucket.forge.blink.Blink;
import com.monkeyinabucket.forge.world.Location;

/**
 * General Rune class. This class could eventually be the base class for other
 * Runes.
 */
public class Rune {

  /** The Location of the center Block of this Rune */
  protected Location loc;

  /**
   * provides a list of all locations that comprise this BlinkRune
   * 
   * @return the list of locations
   */
  public Collection<Location> getParts() {
    int startZ = loc.z - Blink.halfRuneSize;
    int startX = loc.x - Blink.halfRuneSize;

    ArrayList<Location> parts = new ArrayList<Location>();
    for (int col = 0; col < Blink.runeSize; ++col) {
      for (int row = 0; row < Blink.runeSize; ++row) {
        Location nextLoc = new Location(loc.world, startX + row, loc.y, startZ + col);

        parts.add(nextLoc);
      }
    }

    return parts;
  }

  /**
   * Determines if the specified Location is a part of this BlinkRune
   * 
   * @param otherLoc the Location to check
   * @return true if the Location is part of this BlinkRune, false if not
   */
  public boolean isPart(Location otherLoc) {
    return otherLoc.world.provider.dimensionId == loc.world.provider.dimensionId
        && otherLoc.y == loc.y && otherLoc.z >= loc.z - Blink.halfRuneSize && otherLoc.z <= loc.z + Blink.halfRuneSize
        && otherLoc.x >= loc.x - Blink.halfRuneSize && otherLoc.x <= loc.x + Blink.halfRuneSize;
  }

  /**
   * determines if the specified rune shares one or more blocks with this rune.
   * 
   * @param rune the rune to check
   * @return true if the runes overlap, false if not
   */
  public boolean overlaps(Rune rune) {
    Collection<Location> parts = rune.getParts();

    for (Location part : parts) {
      if (isPart(part)) {
        return true;
      }
    }

    return false;
  }
}
