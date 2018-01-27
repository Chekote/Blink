package com.monkeyinabucket.forge.blink.command;

import com.monkeyinabucket.forge.blink.Blink;
import net.minecraft.block.Block;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

/**
 * Handler for the /blink-create command.
 */
public class BlinkCreate extends BaseCommand implements ICommand {

  /**
   * The primary name that can be used to execute this command.
   */
  public static final String NAME = "blink-create";

  /**
   * Constructor.
   */
  public BlinkCreate() {
    super();

    this.aliases.add(BlinkCreate.NAME);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getCommandName() {
    return BlinkCreate.NAME;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getCommandUsage(ICommandSender sender) {
    return BlinkCreate.NAME;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void processCommand(ICommandSender sender, String[] command) {
    if (!(sender instanceof EntityPlayerMP)) {
      return;
    }

    EntityPlayerMP player = (EntityPlayerMP) sender;

    int distance = 64;
    BlockPos targetPos = player.rayTrace(distance, 1.0F).getBlockPos();

    World world = player.getEntityWorld();

    int center = Blink.halfRuneSize;
    int cornerX = targetPos.getX() - Blink.halfRuneSize;
    int cornerZ = targetPos.getZ() - Blink.halfRuneSize;
    int y = targetPos.getY();

    for (int col = 0; col < Blink.runeSize; ++col) {
      for (int row = 0; row < Blink.runeSize; ++row) {
        int x = cornerX + col;
        int z = cornerZ + row;

        Block block;
        if ((col == center && (row == center - 1 || row == center + 1)) || row == center && (col == center - 1 || col == center + 1)) {
          // blocks immediately north, south, east & west of the center are signature.
          block = Blocks.stone;
        } else if (col == center && row == center) {
          // center block is the Rune Core
          block = Blink.runecore;
        } else {
          // the shell is obsidian
          block = Blocks.obsidian;
        }

        world.setBlockState(new BlockPos(x, y, z), block.getDefaultState());
      }
    }
  }
}
