package com.monkeyinabucket.blink.listener;

import com.monkeyinabucket.blink.Blink;
import com.monkeyinabucket.blink.minecraft.Player;
import com.monkeyinabucket.blink.minecraft.World;
import com.monkeyinabucket.world.Coordinates;

/**
 * Handles player events (block break, damage)
 */
@SuppressWarnings("unused")
public class PlayerListener {

  /**
   * Event handler for when a player activates a block.
   *
   * @param world       the world that the activated block is located within.
   * @param loc         the location of the block that was activated.
   * @param player      the player that activated the block.
   */
  public void onBlockActivate(World world, Coordinates loc, Player player) {
    if (Blink.getInstance().environment.isServer() && Blink.runecore.hasRuneShell(world, loc)) {
      Blink.runecore.activate(world, loc, player);
    }
  }

}
