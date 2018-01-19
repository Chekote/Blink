package com.monkeyinabucket.forge.blink.block;

import com.monkeyinabucket.forge.blink.Blink;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

import com.monkeyinabucket.forge.blink.rune.BlinkRune;
import com.monkeyinabucket.forge.blink.rune.RuneManager;
import com.monkeyinabucket.forge.world.Location;

import cpw.mods.fml.common.FMLCommonHandler;

/**
 * The central block for the Rune.
 */
public class RuneCore extends Block {

  /** The primary object used to manage runes in the plugin */
  private final RuneManager runeManager = RuneManager.getInstance();

  /**
   * Constructor.
   *
   * @param material the material that the block is made of.
   */
  public RuneCore(Material material) {
    super(material);

    setHardness(1.0F);
    setStepSound(Block.soundTypeMetal);
    setBlockName("runecore");
    setCreativeTab(CreativeTabs.tabBlock);
    setBlockTextureName("blink:runecore");
  }

  /**
   * Called upon block activation (right click on the block.)
   */
  public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int dim,
    float playerX, float playerY, float playerZ) {

    if (hasRuneShell(world, x, y, z)) {
      switch (FMLCommonHandler.instance().getEffectiveSide()) {
      case SERVER:
        activate(new Location(world, x, y, z), player);
        break;
      case CLIENT:
        // no action required. Server will handle Rune interaction.
        break;
      default:
        // bukkit server. No action required.
        break;
      }
    }

    return true;
  }

  /**
   * Determines if the specified Block is the center of a rune (has a "Rune Shell"). Currently a rune shell is defined
   * as the obsidian shell of a BlinkRune: (Y == RuneCore, X == Obsidian)
   *
   * For example, if Blink.runeSize is 5, a valid rune would be:
   *
   * <pre>
   *  X X X X X
   *  X X   X X
   *  X   Y   X
   *  X X   X X
   *  X X X X X
   * </pre>
   * 
   * @param world The world to check.
   * @param x     The x coordinate of the block to check.
   * @param y     The y coordinate of the block to check.
   * @param z     The z coordinate of the block to check.
   * @return true if the Block has a rune shell, false if not
   */
  protected boolean hasRuneShell(World world, int x, int y, int z) {
    // We were passed the coordinates for the center, but we need to start at the
    // North/West corner
    x -= Blink.halfRuneSize;
    z -= Blink.halfRuneSize;

    int center = Blink.halfRuneSize;

    // loop over the columns and rows, checking for obsidian
    // note: the first row is row 0, not row 1. same for columns.
    for (int col = 0; col < Blink.runeSize; ++col) {
      for (int row = 0; row < Blink.runeSize; ++row) {
        Block block = world.getBlock(x + col, y, z + row);

        // blocks immediately north, south, east & west of the center cannot be obsidian
        if ((col == center && (row == center - 1 || row == center + 1)) || row == center && (col == center - 1 || col == center + 1)) {
          if (block != null && block.equals(Blocks.obsidian)) {
            return false;
          }

          continue;
        }

        // center block is the Rune Core, so we ignore it
        if (col == center && row == center) {
          continue;
        }

        if (block == null || !block.equals(Blocks.obsidian)) {
          return false;
        }
      }
    }

    return true;
  }

  /**
   * Invoked once the RuneListener has determined that the player has interacted
   * with a BlinkRune (either a new one, or an existing one).
   * 
   * This method will check with the RuneManager to determine if the BlinkRune
   * already exists, otherwise it will create the new BlinkRune.
   *
   * @param loc    the location of the block that was activated
   * @param player the player that activated the block
   */
  protected void activate(Location loc, EntityPlayer player) {
    // determine if this rune is already registered
    BlinkRune rune = runeManager.getRuneByCenter(loc);

    if (rune != null) {
      // existing rune...
      // player.addChatMessage("Activating rune.");
      rune.activate(player);
    } else {
      // potential new rune...
      rune = new BlinkRune(loc);

      // determine if any of the blocks are overlapping with another rune
      if (runeManager.runeHasOverlap(rune)) {
  	    player.addChatMessage(new ChatComponentText("Cannot create new rune, it overlaps with an existing rune"));
        return;
      }

      if (!rune.getSignature().isValid()) {
        player.addChatMessage(new ChatComponentText("Cannot create new rune, it's signature is invalid"));
        return;
      }

      runeManager.addRune(rune);

      rune.onCreate();

      Blink.instance.saveRunes();
    }
  }
}
