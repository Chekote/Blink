package com.monkeyinabucket.forge.blink.block;

import com.monkeyinabucket.forge.blink.rune.BlinkRune;
import com.monkeyinabucket.forge.blink.rune.RuneManager;
import com.monkeyinabucket.forge.world.Location;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

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
    setUnlocalizedName("runecore");
    setCreativeTab(CreativeTabs.tabBlock);
  }

  /**
   * Called upon block activation (right click on the block.)
   */
  public boolean onBlockActivated(World world, BlockPos pos, EntityPlayer player, int dim,
    float playerX, float playerY, float playerZ) {

    if (hasRuneShell(world, pos)) {
      switch (FMLCommonHandler.instance().getEffectiveSide()) {
      case SERVER:
        activate(new Location(world, pos), player);
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
   * Determines if the specified Block is the center of a rune (has a
   * "Rune Shell"). Currently a rune shell is defined as the obsidian shell of a
   * BlinkRune: (Y == RuneCore, X == Obsidian)
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
   * @param pos   The coordinates of the block to check.
   * @return true if the Block has a rune shell, false if not
   */
  protected boolean hasRuneShell(World world, BlockPos pos) {
    // We were passed the coordinates for the center, but we need to start at the
    // North/West corner
    int x = pos.getX() - 2;
    int z = pos.getZ() - 2;
    int y = pos.getY();

    // loop over the columns and rows, checking for obsidian
    // note: the first row is row 0, not row 1. same for columns.
    for (int col = 0; col < 5; ++col) {
      for (int row = 0; row < 5; ++row) {
        Block block = world.getBlockState(new BlockPos(x + col, y, z + row)).getBlock();

        // column 3, row 2 & 4 and , column 2 & 4, cannot be obsidian
        if ((col == 2 && (row == 1 || row == 3)) || row == 2 && (col == 1 || col == 3)) {
          if (block != null && block.equals(Blocks.obsidian)) {
            return false;
          }

          continue;
        }

        // center block is the Rune Core, so we ignore it
        if (col == 2 && row == 2) {
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
    }
  }
}
