package com.monkeyinabucket.forge.blink.command;

import com.monkeyinabucket.forge.blink.group.BlinkGroup;
import com.monkeyinabucket.forge.blink.rune.RuneManager;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;

import java.util.List;
import java.util.Set;

/**
 * Handler for the /blink-list command.
 */
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
  public String getCommandUsage(ICommandSender sender) {
    return BlinkList.NAME;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void processCommand(ICommandSender sender, String[] command) {
    Set<BlinkGroup> groups = runeManager.getGroups();
    if (groups.size() == 0) {
      sender.addChatMessage(new ChatComponentText("No Runes currently registered"));
    }

    for (BlinkGroup group : runeManager.getGroups()) {
      String[] list = group.toString().replaceAll("\t", " ").replaceAll("BlinkRune", "")
          .replaceAll("BlinkSignature", "").split("\n");

      for (String item : list) {
        sender.addChatMessage(new ChatComponentText(item));
      }
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
