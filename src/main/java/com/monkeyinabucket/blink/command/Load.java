package com.monkeyinabucket.blink.command;

import com.monkeyinabucket.blink.Blink;
import com.monkeyinabucket.blink.minecraft.command.Sender;

/**
 * Handler for the /blink-load command.
 */
public class Load extends BaseCommand {

  /** The primary name that can be used to execute this command. */
  private static final String NAME = "blink-load";

  /**
   * Constructor.
   */
  public Load() {
    super();

    this.aliases.add(Load.NAME);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getCommandName() {
    return Load.NAME;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getCommandUsage(Sender sender) {
    return Load.NAME;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void processCommand(Sender sender, String[] command) {
    if (Blink.getInstance().environment.isServer()) {
      Blink.getInstance().loadRunes();
    }
  }
}
