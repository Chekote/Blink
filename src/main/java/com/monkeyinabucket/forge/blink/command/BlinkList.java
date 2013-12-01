package com.monkeyinabucket.forge.blink.command;

import java.util.Set;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

import com.monkeyinabucket.forge.blink.group.BlinkGroup;
import com.monkeyinabucket.forge.blink.rune.RuneManager;

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
}
