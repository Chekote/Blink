package com.monkeyinabucket.blink.minecraft.block;

/**
 * Used to fetch specific BlockTypes from the host system.
 */
public interface Registry {

  String FIRE = "fire";
  String OBSIDIAN = "obsidian";

  /**
   * Provides if the block is of the specified type
   *
   * @param  name the name of the block to fetch.
   * @throws IndexOutOfBoundsException if there is no block in the registry for the specified name.
   * @return true if this block is of the specified type. false if not.
   */
  Type get(String name);

}
