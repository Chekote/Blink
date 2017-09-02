package com.monkeyinabucket.blink.listener;

import com.monkeyinabucket.blink.Blink;
import com.monkeyinabucket.blink.rune.BlinkRune;
import com.monkeyinabucket.world.Coordinates;

/**
 * Handles block events, regardless of source (player, dynamite, etc)
 *
 * TODO: Handle events that may change or break a rune: ice melt, piston push, block placed in air signature slot, grass grow, etc.
 */
@SuppressWarnings("unused")
public class BlockListener {

  /**
   * Event handler for when a block is broken.
   *
   * @param loc         the location of the block that was broken.
   */
  public void onBlockBreak(Coordinates loc) {
    // The server will handle the event if the world is remote
    if (!Blink.getInstance().environment.isServer()) {
      return;
    }

    BlinkRune rune = Blink.runeManager.getRuneByPart(loc);
    if (rune == null) {
      return;
    }

    rune.onDestroy();
  }

  /**
   * Event handler for when a player damages a block is damaged.
   *
   * @param loc         the location of the block that was damaged.
   */
  public void onBlockDamage(Coordinates loc) {
    // We aren't responsible for handling this event if we're running on the client.
    if (Blink.getInstance().environment.isClient()) {
      return;
    }

    BlinkRune rune = Blink.runeManager.getRuneByPart(loc);
    if (rune == null) {
      return;
    }

    rune.onDamage();
  }

}
