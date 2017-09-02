package com.monkeyinabucket.blink.minecraft.command;

public interface Sender {

  /**
   * Sends a message to the sender.
   *
   * @param content the content of the message
   */
  void addChatMessage(String content);

  /**
   * Determines if the a command sender is an operator.
   *
   * @return true if the sender is an operator, false if not.
   */
  boolean isOperator();

  /**
   * Determines if the a command sender is a server.
   *
   * @return true if the sender is a server, false if not.
   */
  boolean isServer();

}
