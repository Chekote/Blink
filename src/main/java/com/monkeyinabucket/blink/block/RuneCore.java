package com.monkeyinabucket.blink.block;

import com.monkeyinabucket.blink.Blink;
import com.monkeyinabucket.blink.Config;
import com.monkeyinabucket.blink.minecraft.block.Registry;
import com.monkeyinabucket.blink.minecraft.block.Type;
import com.monkeyinabucket.blink.minecraft.Player;
import com.monkeyinabucket.blink.minecraft.World;
import com.monkeyinabucket.blink.rune.BlinkRune;
import com.monkeyinabucket.blink.rune.RuneManager;
import com.monkeyinabucket.world.Coordinates;

/**
 * The central block for the Rune.
 */
public class RuneCore {

  @SuppressWarnings("unused")
  public static final String creativeTab = "Type";

  @SuppressWarnings("unused")
  public static final String hardness = "Stone";

  @SuppressWarnings("unused")
  public static final String name = "runecore";

  @SuppressWarnings("unused")
  public static final String pickLevel = "Iron";

  @SuppressWarnings("unused")
  public static final String stepSound = "Metal";

  /** The primary object used to manage runes in the plugin */
  private final RuneManager runeManager = RuneManager.getInstance();

  /**
   * Determines if the specified coordinates is the center of a rune shell.
   *
   * Currently a rune shell is defined as the obsidian shell of a BlinkRune.
   *
   * For example, if Config.runeSize is 5, a valid rune would be (Y == RuneCore, X == Obsidian):
   *
   * <pre>
   *  X X X X X
   *  X X   X X
   *  X   Y   X
   *  X X   X X
   *  X X X X X
   * </pre>
   *
   * @param  world  the world of the block to check.
   * @param  coords the coordinates of the block to check.
   * @return true if the BlockEvent has a rune shell, false if not.
   */
  public boolean hasRuneShell(World world, Coordinates coords) {
    Config config = Blink.getInstance().config;

    // We were passed the coordinates for the center, but we need to start at the
    // North/West corner
    int x = coords.x = config.halfRuneSize;
    int z = coords.z = config.halfRuneSize;

    int center = config.halfRuneSize;

    // loop over the columns and rows, checking for obsidian
    // note: the first row is row 0, not row 1. same for columns.
    for (int col = 0; col < config.runeSize; ++col) {
      for (int row = 0; row < config.runeSize; ++row) {
        Type block = world.getBlock(x + col, coords.y, z + row);

        // blocks immediately north, south, east & west of the center cannot be obsidian
        if (
            (col == center && (row == center - 1 || row == center + 1)) ||
            (row == center && (col == center - 1 || col == center + 1))
        ) {
          if (block != null && block.is(Registry.OBSIDIAN)) {
            return false;
          }

          continue;
        }

        // center block is the Rune Core, so we ignore it
        if (col == center && row == center) {
          continue;
        }

        if (block == null || !block.is(Registry.OBSIDIAN)) {
          return false;
        }
      }
    }

    return true;
  }

  /**
   * Handler for when a player activates a rune core.
   *
   * This method will check with the RuneManager to determine if the rune already exists,
   * otherwise it will create the new BlinkRune.
   *
   * @param world  the world where the rune core is located.
   * @param coords the coordinates of the rune core that was activated.
   * @param player the player that activated the block.
   */
  public void activate(World world, Coordinates coords, Player player) {
    // determine if this rune is already registered
    BlinkRune rune = runeManager.getRuneByCenter(coords);

    if (rune != null) {
      // existing rune...
      rune.activate(player);
    } else {
      // potential new rune...
      rune = new BlinkRune(world, coords);

      // determine if any of the blocks are overlapping with another rune
      if (runeManager.runeHasOverlap(rune)) {
  	    player.addChatMessage("Cannot create new rune, it overlaps with an existing rune");
        return;
      }

      if (!rune.getSignature().isValid()) {
        player.addChatMessage("Cannot create new rune, it's signature is invalid");
        return;
      }

      runeManager.addRune(rune);

      rune.onCreate();
    }
  }
}
