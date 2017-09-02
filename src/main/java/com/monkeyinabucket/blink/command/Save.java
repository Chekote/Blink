package com.monkeyinabucket.blink.command;

import com.monkeyinabucket.blink.Blink;
import com.monkeyinabucket.blink.minecraft.command.Sender;

/**
 * Handler for the /blink-save command.
 */
public class Save extends BaseCommand {

  /** The primary name that can be used to execute this command. */
  private static final String NAME = "blink-save";

  /**
   * Constructor.
   */
  public Save() {
    super();

    this.aliases.add(Save.NAME);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getCommandName() {
    return Save.NAME;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getCommandUsage(Sender sender) {
    return Save.NAME;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void processCommand(Sender sender, String[] command) {
    if (Blink.getInstance().environment.isServer()) {
      Blink.getInstance().saveRunes();
    }
  }

}
