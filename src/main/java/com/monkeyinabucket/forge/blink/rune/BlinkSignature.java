package com.monkeyinabucket.forge.blink.rune;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.block.Block;

/**
 * Represents a blink signature. A signature is a collection of four blocks:
 * North, South, East and West. Each of these blocks is positioned one block in
 * the specified direction from the center block of the Rune.
 */
public class BlinkSignature implements Cloneable {

  /** The Block in the North position */
  protected Block north;

  /** The Block in the East position */
  protected Block east;

  /** The Block in the south position */
  protected Block south;

  /** The Block in the west position */
  protected Block west;

  public BlinkSignature(Block north, Block east, Block south, Block west) {
    this.north = north;
    this.east = east;
    this.south = south;
    this.west = west;
  }

  /**
   * Returns the material in the North position of this BlinkSignature.
   * 
   * @return the Block
   */
  public Block getNorth() {
    return north;
  }

  /**
   * Returns the material in the East position of this BlinkSignature.
   * 
   * @return the Block
   */
  public Block getEast() {
    return east;
  }

  /**
   * Returns the material in the South position of this BlinkSignature.
   * 
   * @return the Block
   */
  public Block getSouth() {
    return south;
  }

  /**
   * Returns the material in the West position of this BlinkSignature.
   * 
   * @return the Material
   */
  public Block getWest() {
    return west;
  }

  /**
   * Determines if this BlinkSignature matches the specified BlinkSignature
   * 
   * @param signature the signature to check against
   * @return true if equal, false if not
   */
  public boolean equals(BlinkSignature signature) {
    return blockEquals(signature.getNorth(), north) && blockEquals(signature.getEast(), east)
        && blockEquals(signature.getSouth(), south) && blockEquals(signature.getWest(), west);
  }

  /**
   * Null safe comparison for Blocks that comprise this Signature.
   * 
   * @param block1 the first block to compare.
   * @param block2 the second block to compare.
   * @return true if the blocks are equal, false if not.
   */
  protected boolean blockEquals(Block block1, Block block2) {
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
  public BlinkSignature clone() {
    return new BlinkSignature(north, east, south, west);
  }

  /**
   * Provides a string representation of this BlinkSignature
   * 
   * @return the string
   */
  public String toString() {
    return "BlinkSignature" + toJson().toString();
  }

  /**
   * Provides a JsonObject that describes this Location.
   *
   * This method is intended for use in saving data, and debugging.
   *
   * @return the builder.
   */
  public JsonObject toJson() {
    JsonObject object = new JsonObject();

    object.add("n", new JsonPrimitive(Block.blockRegistry.getNameForObject(north).toString()));
    object.add("e", new JsonPrimitive(Block.blockRegistry.getNameForObject(east).toString()));
    object.add("s", new JsonPrimitive(Block.blockRegistry.getNameForObject(south).toString()));
    object.add("w", new JsonPrimitive(Block.blockRegistry.getNameForObject(west).toString()));

    return object;
  }
}
