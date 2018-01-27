package com.monkeyinabucket.forge.blink.command;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

/**
 * Handler for the /BlinkLoad command.
 */
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
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
   * {@inheritDoc}
   */
  @Override
  public List<String> getTabCompletionOptions(
      MinecraftServer server,
      ICommandSender sender,
      String[] args,
      @Nullable BlockPos pos
  ) {
    return new ArrayList<String>();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int compareTo(ICommand o) {
    return 0;
  }

  /**
   * Returns a list of aliases that can be used to invoke this command.
   *
   * @return the aliases.
   */
  @Override
  public List<String> getCommandAliases() {
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
  public abstract String getCommandUsage(@Nullable ICommandSender sender);

  /**
   * Executes the command.
   *
   * @param server the server that the command is being executed against.
   * @param sender the command sender.
   * @param args   the arguments for the command.
   */
  @Override
  public abstract void execute(
      @Nullable MinecraftServer server,
      @Nullable ICommandSender sender,
      @Nullable String[] args
  ) throws CommandException;

  /**
   * Determines if a particular command sender can execute this command.
   *
   * Only Ops and Server processes can execute the command.
   *
   * @param sender the command sender to check.
   * @return true if the sender can execute the command, false if not.
   */
  @Override
  public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
    return Helper.isOperator(sender) || sender instanceof DedicatedServer;
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
}
