package com.monkeyinabucket.forge.blink.command;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;

import com.monkeyinabucket.forge.blink.Blink;

import cpw.mods.fml.common.FMLCommonHandler;

/**
 * Handler for the /blink-load command.
 */
public class BlinkLoad extends BaseCommand implements ICommand {

  /** The primary name that can be used to execute this command. */
  public static final String NAME = "blink-load";

  /**
   * Constructor.
   */
  public BlinkLoad() {
    super();

    this.aliases.add(BlinkLoad.NAME);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getCommandName() {
    return BlinkLoad.NAME;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getCommandUsage(ICommandSender sender) {
    return BlinkLoad.NAME;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void processCommand(ICommandSender sender, String[] command) {
    switch (FMLCommonHandler.instance().getEffectiveSide()) {
      case SERVER:
        Blink.instance.loadRunes();
        break;
      case CLIENT:
        // nop
        break;
      default:
        // nop
        break;
    }
  }
}
