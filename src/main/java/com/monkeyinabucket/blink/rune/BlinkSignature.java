package com.monkeyinabucket.blink.rune;


import com.monkeyinabucket.blink.minecraft.block.Type;

/**
 * Represents a blink signature. A signature is a collection of four blocks:
 * North, South, East and West. Each of these blocks is positioned one block in
 * the specified direction from the center block of the Rune.
 */
public class BlinkSignature implements Cloneable {

  /** The BlockEvent in the North position */
  private Type north;

  /** The BlockEvent in the East position */
  private Type east;

  /** The BlockEvent in the south position */
  private Type south;

  /** The BlockEvent in the west position */
  private Type west;

  BlinkSignature(Type north, Type east, Type south, Type west) {
    this.north = north;
    this.east = east;
    this.south = south;
    this.west = west;
  }

  /**
   * Determines if this BlinkSignature matches the specified BlinkSignature
   * 
   * @param signature the signature to check against
   * @return true if equal, false if not
   */
  public boolean equals(BlinkSignature signature) {
    return blockEquals(signature.north, north) && blockEquals(signature.east, east)
        && blockEquals(signature.south, south) && blockEquals(signature.west, west);
  }

  /**
   * Null safe comparison for Blocks that comprise this Signature.
   * 
   * @param block1 the first block to compare.
   * @param block2 the second block to compare.
   * @return true if the blocks are equal, false if not.
   */
  private boolean blockEquals(Type block1, Type block2) {
    if (block1 == null) {
      return block2 == null;
    }

    return block1.equals(block2);
  }

  /**
   * Determines if this signature is valid. Some Material types are not valid
   * for use in a signature such as Air.
   * 
   * @return true if the signature is valid, false if not.
   */
  public boolean isValid() {
    // TODO: test for validity. some blocks are invalid, such as air, signs etc.

    return true;
  }

  /**
   * Creates a copy of this BlinkSignature
   * 
   * @return the copy
   */
  @SuppressWarnings("MethodDoesntCallSuperMethod")
  public BlinkSignature clone() {
    return new BlinkSignature(north, east, south, west);
  }

  /**
   * Provides a string representation of this BlinkSignature
   * 
   * @return the string
   */
  public String toString() {
    return "BlinkSignature{" +
      "n=" + north +
      ", e=" + east +
      ", s=" + south +
      ", w=" + west +
    "}";
  }
}
