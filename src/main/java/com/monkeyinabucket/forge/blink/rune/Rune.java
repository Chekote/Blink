package com.monkeyinabucket.forge.blink.rune;

import com.monkeyinabucket.forge.blink.Blink;
import com.monkeyinabucket.forge.world.Location;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Collection;

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
    int startZ = loc.pos.getZ() - Blink.halfRuneSize;
    int startX = loc.pos.getX() - Blink.halfRuneSize;

    ArrayList<Location> parts = new ArrayList<>();
    for (int col = 0; col < Blink.runeSize; ++col) {
      for (int row = 0; row < Blink.runeSize; ++row) {
        BlockPos pos = new BlockPos(startX + row, loc.pos.getY(), startZ + col);
        Location nextLoc = new Location(loc.world, pos);

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
    return otherLoc.world.provider.getDimension() == loc.world.provider.getDimension()
        && otherLoc.pos.getY() == loc.pos.getY()
        && otherLoc.pos.getZ() >= loc.pos.getZ() - Blink.halfRuneSize
        && otherLoc.pos.getZ() <= loc.pos.getZ() + Blink.halfRuneSize
        && otherLoc.pos.getX() >= loc.pos.getX() - Blink.halfRuneSize
        && otherLoc.pos.getX() <= loc.pos.getX() + Blink.halfRuneSize;
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
