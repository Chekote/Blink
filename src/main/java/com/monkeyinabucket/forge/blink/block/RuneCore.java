package com.monkeyinabucket.forge.blink.block;

import com.monkeyinabucket.forge.blink.Blink;
import com.monkeyinabucket.forge.blink.rune.BlinkRune;
import com.monkeyinabucket.forge.blink.rune.RuneManager;
import com.monkeyinabucket.forge.world.Location;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

/**
 * The central block for the Rune.
 */
public class RuneCore extends Block {

  private final String name = "runecore";

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
    setSoundType(SoundType.METAL);
    setRegistryName(Blink.mod_id + ":" + name);
    setUnlocalizedName("blink_runecore");
    setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
  }

  /**
   * Provides the name of this block.
   *
   * @return the name.
   */
  public String getName() {
    return name;
  }

  /**
   * Called upon block activation (right click on the block.)
   */
  @Override
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
                                  EnumFacing facing, float hitX, float hitY, float hitZ) {
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
   * @param pos   The coordinates of the block to check.
   * @return true if the Block has a rune shell, false if not
   */
  protected boolean hasRuneShell(World world, BlockPos pos) {
    // We were passed the coordinates for the center, but we need to start at the
    // North/West corner
    int x = pos.getX() - Blink.halfRuneSize;
    int z = pos.getZ() - Blink.halfRuneSize;
    int y = pos.getY();

    int center = Blink.halfRuneSize;

    // loop over the columns and rows, checking for obsidian
    // note: the first row is row 0, not row 1. same for columns.
    for (int col = 0; col < Blink.runeSize; ++col) {
      for (int row = 0; row < Blink.runeSize; ++row) {
        Block block = world.getBlockState(new BlockPos(x + col, y, z + row)).getBlock();

        // blocks immediately north, south, east & west of the center cannot be obsidian
        if ((col == center && (row == center - 1 || row == center + 1)) || row == center && (col == center - 1 || col == center + 1)) {
          if (block.equals(Blocks.OBSIDIAN)) {
            return false;
          }

          continue;
        }

        // center block is the Rune Core, so we ignore it
        if (col == center && row == center) {
          continue;
        }

        if (!block.equals(Blocks.OBSIDIAN)) {
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
      // player.sendMessage(new TextComponentString("Activating rune."));
      rune.activate(player);
    } else {
      // potential new rune...
      rune = new BlinkRune(loc);

      // determine if any of the blocks are overlapping with another rune
      if (runeManager.runeHasOverlap(rune)) {
  	    player.sendMessage(new TextComponentString("Cannot create new rune, it overlaps with an existing rune"));
        return;
      }

      if (!rune.getSignature().isValid()) {
        player.sendMessage(new TextComponentString("Cannot create new rune, it's signature is invalid"));
        return;
      }

      runeManager.addRune(rune);

      rune.onCreate();

      Blink.instance.saveRunes();
    }
  }
}
