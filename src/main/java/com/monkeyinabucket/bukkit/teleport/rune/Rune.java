/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.monkeyinabucket.bukkit.teleport.rune;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

/**
 *
 * @author dtyler
 */
public class Rune {

  public static boolean hasRuneShell(Block center) {
    // most of the time, the block clicked is not obsidian (which the center of a rune always is)
    // so for performance reasons, we check this first.
    if (center.getType() != Material.OBSIDIAN) {
      return false;
    }

    Block topLeft = center.getFace(BlockFace.NORTH, 2).getFace(BlockFace.WEST, 2);

    // loop over the columns and rows, checking for obsidian
    // note: the first row is row 0, not row 1. same for columns.
    for (int col = 0; col < 5; ++col) {
      for (int row = 0; row < 5; ++row) {
        Block block = topLeft.getFace(BlockFace.EAST, col).getFace(BlockFace.SOUTH, row);

        StringBuilder builder = new StringBuilder();
        builder.append("Block at col ");
        builder.append(col);
        builder.append(", row ");
        builder.append(row);
        builder.append(", is of type ");
        builder.append(block.getType());
        System.out.println(builder.toString());

        // column 3, row 2 & 4 and , column 2 & 4, does not need to be obsidian
        if ((col == 2 && (row == 1 || row == 3)) || row == 2 && (col == 1 || col == 3)) {
          continue;
        }

        if (block.getType() != Material.OBSIDIAN) {
          return false;
        }
      }
    }

    return true;
  }
}
