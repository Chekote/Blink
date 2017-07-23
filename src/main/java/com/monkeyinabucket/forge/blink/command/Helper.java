package com.monkeyinabucket.forge.blink.command;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;

/**
 * Helper class for commands.
 */
public class Helper {

  /**
   * Determines if the a command sender is allowed to execute commands.
   *
   * @param  sender  the command sender.
   * @return true if the sender can execute the command, false if not.
   */
  public static boolean isOperator(ICommandSender sender) {
    if (sender instanceof EntityPlayerMP) {
      EntityPlayerMP player = (EntityPlayerMP) sender;

      return player.mcServer.getPlayerList().canSendCommands(player.getGameProfile());
    }

    return false;
  }
}
