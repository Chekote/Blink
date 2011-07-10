/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.monkeyinabucket.bukkit.blink.listener;

import com.monkeyinabucket.bukkit.blink.Plugin;
import com.monkeyinabucket.bukkit.blink.RuneManager;
import com.monkeyinabucket.bukkit.blink.rune.BlinkRune;
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
    BlinkRune rune = runeManager.getRuneByPart(event.getBlock());
    if (rune == null) {
      return;
    }

    rune.onDamage();
  }

  @Override
  public void onBlockBreak(BlockBreakEvent event) {
    BlinkRune rune = runeManager.getRuneByPart(event.getBlock());
    if (rune == null) {
      return;
    }

    rune.onDestroy();

    runeManager.removeRune(rune);
  }
}
