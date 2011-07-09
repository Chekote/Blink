/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.monkeyinabucket.bukkit.teleport.listener;

import com.monkeyinabucket.bukkit.teleport.Plugin;
import com.monkeyinabucket.bukkit.teleport.RuneManager;
import com.monkeyinabucket.bukkit.teleport.rune.TeleportRune;
import org.bukkit.event.block.BlockDamageEvent;

/**
 *
 * @author dtyler
 */
public class BlockListener extends org.bukkit.event.block.BlockListener {

  private final Plugin plugin;
  private final RuneManager runeManager;

  public BlockListener(final Plugin plugin, final RuneManager runeManager) {
    this.plugin = plugin;
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
}
