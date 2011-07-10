package com.monkeyinabucket.bukkit.teleport;

import org.bukkit.Material;

/**
 *
 * @author dtyler
 */
public class TeleportSignature {

  protected Material north;
  protected Material east;
  protected Material south;
  protected Material west;

  public TeleportSignature(Material north, Material east, Material south, Material west) {
    this.north = north;
    this.east = east;
    this.south = south;
    this.west = west;
  }

  public Material getNorth() {
    return north;
  }

  public Material getEast() {
    return east;
  }

  public Material getSouth() {
    return south;
  }

  public Material getWest() {
    return west;
  }

  public boolean equals(TeleportSignature signature) {
    return
        signature.getNorth().equals(north) &&
        signature.getEast().equals(east)   &&
        signature.getSouth().equals(south) &&
        signature.getWest().equals(west);
  }

  public boolean isValid() {
    // TODO: test for validity. some blocks are invalid, such as air, signs etc.

    return true;
  }

  public TeleportSignature clone() {
    return new TeleportSignature(north, east, south, west);
  }

  public String toString() {
    StringBuilder b = new StringBuilder();

    b.append("TeleportSignature{north=");
    b.append(north);
    b.append(",east=");
    b.append(east);
    b.append(",south=");
    b.append(south);
    b.append(",west=");
    b.append(west);
    b.append("}");

    return b.toString();
  }
}
