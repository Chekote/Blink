package com.monkeyinabucket.forge.blink.command;

import com.monkeyinabucket.forge.blink.Blink;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Handler for the /blink-save command.
 */
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
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
  public String getName() {
    return BlinkSave.NAME;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getUsage(@Nullable ICommandSender sender) {
    return BlinkSave.NAME;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void execute(
      @Nullable MinecraftServer server,
      @Nullable ICommandSender sender,
      @Nullable String[] args
  ) throws CommandException {
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
}
