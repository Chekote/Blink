package com.monkeyinabucket.blink.command;

import com.monkeyinabucket.blink.minecraft.command.Sender;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for all blink commands
 */
public abstract class BaseCommand {

  ArrayList<String> aliases;

  /**
   * Constructor.
   */
  @SuppressWarnings("WeakerAccess")
  public BaseCommand() {
    this.aliases = new ArrayList<String>();
  }

  /**
   * Returns a list of aliases that can be used to invoke this command.
   *
   * @return the aliases.
   */
  @SuppressWarnings("unused")
  public List getCommandAliases() {
    return this.aliases;
  }

  /**
   * Provides the primary name that can be used to execute this command.
   *
   * @return the commands primary name.
   */
  @SuppressWarnings("unused")
  public abstract String getCommandName();

  /**
   * Provides usage syntax for this command.
   *
   * @param sender the command sender.
   * @return the usage syntax.
   */
  @SuppressWarnings("unused")
  public abstract String getCommandUsage(Sender sender);

  /**
   * Executes the command.
   *
   * @param sender      the command sender.
   * @param command     the full command execution string.
   */
  @SuppressWarnings("unused")
  public abstract void processCommand(Sender sender, String[] command);

  /**
   * Determines if a particular command sender can execute this command.
   *
   * Only Ops and Server processes can execute the command.
   *
   * @param sender the command sender to check.
   * @return true if the sender can execute the command, false if not.
   */
  @SuppressWarnings("unused")
  public boolean canCommandSenderUseCommand(Sender sender) {
    return sender.isOperator() || sender.isServer();
  }

  /**
   * Provides a list of available options for tab auto-completion.
   *
   * @param sender the command sender.
   * @param command the command being executed.
   * @return the list of auto-completion options.
   */
  @SuppressWarnings("unused")
  public List addTabCompletionOptions(Sender sender, String[] command) {
    return null;
  }

  /**
   * Determines if the specified command parameter index is a username parameter.
   *
   * @param command the command being executed.
   * @param i       the index of the parameter to check.
   * @return true if the index is a username parameter, false if not.
   */
  @SuppressWarnings("unused")
  public boolean isUsernameIndex(String[] command, int i) {
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unused")
  public int compareTo(Object object) {
    return 0;
  }
}
