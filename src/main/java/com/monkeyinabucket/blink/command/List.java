package com.monkeyinabucket.blink.command;

import java.util.Set;

import com.monkeyinabucket.blink.group.BlinkGroup;
import com.monkeyinabucket.blink.minecraft.command.Sender;
import com.monkeyinabucket.blink.rune.RuneManager;

/**
 * Handler for the /blink-list command.
 */
public class List extends BaseCommand {

  /** The primary name that can be used to execute this command. */
  private static final String NAME = "blink-list";

  /** Reference to the plugin's manager */
  private final RuneManager runeManager;

  /**
   * Constructor.
   */
  public List() {
    super();

    this.aliases.add(List.NAME);

    runeManager = RuneManager.getInstance();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getCommandName() {
    return List.NAME;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getCommandUsage(Sender sender) {
    return List.NAME;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void processCommand(Sender sender, String[] command) {
    Set<BlinkGroup> groups = runeManager.getGroups();
    if (groups.size() == 0) {
      sender.addChatMessage("No Runes currently registered");
    }

    for (BlinkGroup group : runeManager.getGroups()) {
      String[] list = group.toString().replaceAll("\t", " ").replaceAll("BlinkRune", "")
          .replaceAll("BlinkSignature", "").split("\n");

      for (String item : list) {
        sender.addChatMessage(item);
      }
    }
  }
}
