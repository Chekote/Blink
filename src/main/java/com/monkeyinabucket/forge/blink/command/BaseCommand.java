package com.monkeyinabucket.forge.blink.command;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.dedicated.DedicatedServer;

/**
 * Handler for the /BlinkLoad command.
 */
public abstract class BaseCommand implements ICommand {

  /** List of aliases that can be used to invoke this command. */
  protected List<String> aliases;

  /**
   * Constructor.
   */
  public BaseCommand() {
    this.aliases = new ArrayList<String>();
  }

  /**
   * Returns a list of aliases that can be used to invoke this command.
   *
   * @return the aliases.
   */
  @Override
  public List getCommandAliases() {
    return this.aliases;
  }

  /**
   * Provides the primary name that can be used to execute this command.
   *
   * @return the commands primary name.
   */
  @Override
  public abstract String getCommandName();

  /**
   * Provides usage syntax for this command.
   *
   * @param sender the command sender.
   * @return the usage syntax.
   */
  @Override
  public abstract String getCommandUsage(ICommandSender sender);

  /**
   * Executes the command.
   *
   * @param sender  the command sender.
   * @param command the full command execution string.
   */
  @Override
  public abstract void processCommand(ICommandSender sender, String[] command);

  /**
   * Determines if a particular command sender can execute this command.
   *
   * Only Ops and Server processes can execute the command.
   *
   * @param sender the command sender to check.
   * @return true if the sender can execute the command, false if not.
   */
  @Override
  public boolean canCommandSenderUseCommand(ICommandSender sender) {
    try {
      Class.forName("net.minecraft.server.dedicated.DedicatedServer");

      if (sender instanceof DedicatedServer) {
        return true;
      }
    } catch (ClassNotFoundException e) {
      // nop
    }

    return Helper.isOperator(sender);
  }

  /**
   * Provides a list of available options for tab auto-completion.
   *
   * @param sender the command sender.
   * @param command the command being executed.
   * @return the list of auto-completion options.
   */
  @Override
  public List addTabCompletionOptions(ICommandSender sender, String[] command) {
    return null;
  }

  /**
   * Determines if the specified command parameter index is a username parameter.
   *
   * @param command the command being executed.
   * @param i       the index of the parameter to check.
   * @return true if the index is a username parameter, false if not.
   */
  @Override
  public boolean isUsernameIndex(String[] command, int i) {
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int compareTo(Object object) {
    return 0;
  }
}
