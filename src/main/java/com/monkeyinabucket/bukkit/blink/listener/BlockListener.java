package com.monkeyinabucket.bukkit.blink.listener;

import com.monkeyinabucket.bukkit.blink.RuneManager;
import com.monkeyinabucket.bukkit.blink.rune.BlinkRune;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockEvent;

/**
 * Responsible for handling Block events that affect BlinkRunes.
 * @author Donald Tyler (chekote69@gmail.com)
 */
public class BlockListener implements Listener {

  /** Reference to the plugin's runeManager */
  private final RuneManager runeManager;

  /**
   * Constructor
   * @param runeManager 
   */
  public BlockListener() {
    runeManager = RuneManager.getInstance();
  }

  /**
   * Handles BlockDamageEvents. If the damaged Block is part of a BlinkRune, this method will
   * call the BlineRunes onDamage method.
   * @param event 
   */
  @EventHandler
  public void onBlockDamage(BlockDamageEvent event) {
    if (event.isCancelled()) {
      return;
    }

    BlinkRune rune = runeManager.getRuneByPart(event.getBlock());
    if (rune == null) {
      return;
    }

    rune.onDamage();
  }

  /**
   * Handles BlockBreakEvents. If the broken Block is part of a BlinkRune, this method will
   * invoke the BlinkRunes onDestroy event, and remove the BlinkRune from the RuneManager.
   * @param event 
   */
  @EventHandler
  public void onBlockBreak(BlockBreakEvent event) {
    if (event.isCancelled()) {
      return;
    }

    BlinkRune rune = runeManager.getRuneByPart(event.getBlock());
    if (rune == null) {
      return;
    }

    rune.onDestroy();  
  }
}
