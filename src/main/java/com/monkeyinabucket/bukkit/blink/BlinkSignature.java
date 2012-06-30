package com.monkeyinabucket.bukkit.blink;

import org.bukkit.Material;

/**
 * Represents a blink signature. A signature is a collection of four blocks: North, South, East
 * and West. Each of these blocks is positioned one block in the specified direction from the
 * center block of the Rune.
 *
 * @author Donald Tyler (chekote69@gmail.com)
 */
public class BlinkSignature {

  /** The Material in the North position */
  protected Material north;

  /** The Material in the East position */
  protected Material east;

  /** The Material in the south position */
  protected Material south;

  /** The Material in the west position */
  protected Material west;

  public BlinkSignature(Material north, Material east, Material south, Material west) {
    this.north = north;
    this.east = east;
    this.south = south;
    this.west = west;
  }

  /**
   * Returns the material in the North position of this BlinkSignature.
   * @return the Material
   */
  public Material getNorth() {
    return north;
  }

  /**
   * Returns the material in the East position of this BlinkSignature.
   * @return the Material
   */
  public Material getEast() {
    return east;
  }

  /**
   * Returns the material in the South position of this BlinkSignature.
   * @return the Material
   */
  public Material getSouth() {
    return south;
  }

  /**
   * Returns the material in the West position of this BlinkSignature.
   * @return the Material
   */
  public Material getWest() {
    return west;
  }

  /**
   * Determines if this BlinkSignature matches the specified BlinkSignature
   * @param signature the signature to check against
   * @return true if equal, false if not
   */
  public boolean equals(BlinkSignature signature) {
    return
        signature.getNorth().equals(north) &&
        signature.getEast().equals(east)   &&
        signature.getSouth().equals(south) &&
        signature.getWest().equals(west);
  }

  /**
   * Determines if this signature is valid. Some Material types are not valid for use in a signature
   * such as Air.
   * @return true if the signature is valid, false if not.
   */
  public boolean isValid() {
    // TODO: test for validity. some blocks are invalid, such as air, signs etc.

    return true;
  }

  /**
   * Creates a copy of this BlinkSignature
   * @return the copy
   */
  public BlinkSignature clone() {
    return new BlinkSignature(north, east, south, west);
  }

  /**
   * Provides a string representation of this BlinkSignature
   * @return the string
   */
  public String toString() {
    StringBuilder b = new StringBuilder();

    b.append("BlinkSignature{n=");
    b.append(north);
    b.append(", e=");
    b.append(east);
    b.append(", s=");
    b.append(south);
    b.append(", w=");
    b.append(west);
    b.append("}");

    return b.toString();
  }
}
