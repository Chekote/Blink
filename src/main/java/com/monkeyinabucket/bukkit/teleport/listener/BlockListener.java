/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.monkeyinabucket.bukkit.teleport.listener;

import com.monkeyinabucket.bukkit.teleport.Plugin;
import com.monkeyinabucket.bukkit.teleport.RuneManager;
import com.monkeyinabucket.bukkit.teleport.rune.TeleportRune;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;

/**
 *
 * @author dtyler
 */
public class BlockListener extends org.bukkit.event.block.BlockListener {

  private final RuneManager runeManager;

  public BlockListener(final RuneManager runeManager) {
    this.runeManager = runeManager;
  }

  @Override
  public void onBlockDamage(BlockDamageEvent event) {
    TeleportRune rune = runeManager.getRuneByPart(event.getBlock());
    if (rune == null) {
      return;
    }

    rune.onDamage();
  }

  @Override
  public void onBlockBreak(BlockBreakEvent event) {
    TeleportRune rune = runeManager.getRuneByPart(event.getBlock());
    if (rune == null) {
      return;
    }

    rune.onDestroy();

    runeManager.removeRune(rune);
  }
}
