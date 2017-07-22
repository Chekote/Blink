package com.monkeyinabucket.forge.blink.command;

import com.monkeyinabucket.forge.blink.Blink;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.List;

/**
 * Handler for the /blink-save command.
 */
public class BlinkSave extends BaseCommand implements ICommand {

  /** The primary name that can be used to execute this command. */
  public static final String NAME = "blink-save";

  /**
   * Constructor.
   */
  public BlinkSave() {
    super();

    this.aliases.add(BlinkSave.NAME);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getCommandName() {
    return BlinkSave.NAME;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getCommandUsage(ICommandSender sender) {
    return BlinkSave.NAME;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void processCommand(ICommandSender sender, String[] command) {
    switch (FMLCommonHandler.instance().getEffectiveSide()) {
    case SERVER:
      Blink.instance.saveRunes();
      break;
    case CLIENT:
      // nop
      break;
    default:
      // nop
      break;
    }
  }

  /**
   * Provides a list of available options for tab auto-completion.
   *
   * @param sender the command sender.
   * @param args   the arguments that were passed to the command.
   * @param pos    the position of the block that the command is being executed against.
   * @return the list of auto-completion options.
   */
  @Override
  public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int compareTo(ICommand o) {
    return 0;
  }
}
