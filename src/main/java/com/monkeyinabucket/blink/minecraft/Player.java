package com.monkeyinabucket.blink.minecraft;

/**
 * Provides methods for interacting with a specific player.
 */
public interface Player {

  /**
   * Sends a message to the Player.
   *
   * @param content the content of the message
   */
  void addChatMessage(String content);

  /**
   * @return the world that this player currently occupies.
   */
  World getWorld();

  /**
   * Sets the world that this player is occupying.
   *
   * @param world the world to move the player to.
   */
  void setWorld(World world);

  /**
   *
   */
  void setPosition(double x, double y, double z);
}
