package com.monkeyinabucket.forge.blink.command;

import com.monkeyinabucket.forge.blink.Blink;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

/**
 * Handler for the /blink-load command.
 */
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
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
  public String getName() {
    return BlinkLoad.NAME;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getUsage(@Nullable ICommandSender sender) {
    return BlinkLoad.NAME;
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

  /**
   * {@inheritDoc}
   */
  @Override
  public List<String> getTabCompletions(
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
}
