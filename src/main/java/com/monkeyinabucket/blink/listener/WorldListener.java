package com.monkeyinabucket.blink.listener;

import com.monkeyinabucket.blink.Blink;

/**
 * Handles world events (load, unload, etc)
 */
@SuppressWarnings("unused")
public class WorldListener {

  /**
   * Handles world load events.
   *
   * Loads the runes for the world. the file will only be loaded if the current
   * execution environment is the server.
   */
  public void onWorldLoad() {
    if (Blink.getInstance().environment.isServer()) {
      Blink.getInstance().loadRunes();
    }
  }

}
