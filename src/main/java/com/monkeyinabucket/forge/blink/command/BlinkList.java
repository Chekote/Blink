package com.monkeyinabucket.forge.blink.command;

import com.monkeyinabucket.forge.blink.group.BlinkGroup;
import com.monkeyinabucket.forge.blink.rune.RuneManager;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Handler for the /blink-list command.
 */
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class BlinkList extends BaseCommand implements ICommand {

  /** The primary name that can be used to execute this command. */
  public static final String NAME = "blink-list";

  /** Reference to the plugin's runeManager */
  private final RuneManager runeManager;

  /**
   * Constructor.
   */
  public BlinkList() {
    super();

    this.aliases.add(BlinkList.NAME);

    runeManager = RuneManager.getInstance();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getName() {
    return BlinkList.NAME;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getUsage(@Nullable ICommandSender sender) {
    return BlinkList.NAME;
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
    if (sender == null) {
      if (server != null) {
        server.sendMessage(new TextComponentString("Warn: Received " + NAME + " command from null sender"));
      }

      return;
    }

    Set<BlinkGroup> groups = runeManager.getGroups();
    if (groups.size() == 0) {
      sender.sendMessage(new TextComponentString("No Runes currently registered"));
    }

    for (BlinkGroup group : runeManager.getGroups()) {
      String[] list = group.toString().replaceAll("\t", " ").replaceAll("BlinkRune", "")
          .replaceAll("BlinkSignature", "").split("\n");

      for (String item : list) {
        sender.sendMessage(new TextComponentString(item));
      }
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
