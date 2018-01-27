package com.monkeyinabucket.forge.blink.command;

import com.monkeyinabucket.forge.blink.group.BlinkGroup;
import com.monkeyinabucket.forge.blink.rune.RuneManager;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
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
  public String getCommandName() {
    return BlinkList.NAME;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getCommandUsage(@Nullable ICommandSender sender) {
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
        server.addChatMessage(new TextComponentString("Warn: Received " + NAME + " command from null sender"));
      }

      return;
    }

    Set<BlinkGroup> groups = runeManager.getGroups();
    if (groups.size() == 0) {
      sender.addChatMessage(new TextComponentString("No Runes currently registered"));
    }

    for (BlinkGroup group : runeManager.getGroups()) {
      String[] list = group.toString().replaceAll("\t", " ").replaceAll("BlinkRune", "")
          .replaceAll("BlinkSignature", "").split("\n");

      for (String item : list) {
        sender.addChatMessage(new TextComponentString(item));
      }
    }
  }
}
