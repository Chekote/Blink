package com.monkeyinabucket.bukkit.blink.listener;


import java.util.logging.Logger;

import com.monkeyinabucket.bukkit.blink.RuneManager;
import com.monkeyinabucket.bukkit.blink.rune.Rune;
import com.monkeyinabucket.bukkit.blink.rune.BlinkRune;
import java.util.ArrayList;
import org.bukkit.Location;
import org.bukkit.Material;

import org.bukkit.block.Block;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerListener;

/**
 * Responsible for handling Player events that affect BlinkRunes.
 * @author Donald Tyler (chekote69@gmail.com)
 */
public class RuneListener extends PlayerListener {

  /** Reference to the plugin's runeManager */
  private final RuneManager runeManager;

  /**
   * Constructor
   * @param runeManager 
   */
  public RuneListener(final RuneManager runeManager) {
    this.runeManager = runeManager;
  }

  /**
   * Handles PlayerInteractEvents. If the player right clicks on a block (excluding placing a block),
   * then this method will determine if a new BlinkRune is being created, or an existing BlinkRune
   * is being activated.
   * @param event the PlayerInteractEvent
   */
  @Override
  public void onPlayerInteract(PlayerInteractEvent event) {
    // only handle right clicks, and only when not placing blocks
    if (event.getAction() != Action.RIGHT_CLICK_BLOCK || event.isBlockInHand()) {
      return;
    }

    Block block = event.getClickedBlock();

    if (block.getType() != Material.OBSIDIAN) {
      return;
    }

    if (!Rune.hasRuneShell(block)) {
      return;
    }

    processRuneInteract(event);
  }

  /**
   * Invoked once the RuneListener has determined that the player has interacted with a BlinkRune
   * (either a new one, or an existing one).
   *
   * This method will check with the RuneManager to determine if the BlinkRune already exists,
   * otherwise it will create the new BlinkRune.
   *
   * @param event the PlayerInteractEvent
   */
  protected void processRuneInteract(PlayerInteractEvent event) {
    Block block = event.getClickedBlock();
    Player player = event.getPlayer();

    // determine if this rune is already registered
    BlinkRune rune = runeManager.getRuneByCenter(block);

    if (rune != null) {
      // existing rune...
      player.sendMessage("Activating rune.");
      rune.activate(player);
    } else {
      // potential new rune...
      rune = new BlinkRune(block);

      // determine if any of the blocks are overlapping with another rune
      if (runeManager.runeHasOverlap(rune)) {
        player.sendMessage("Cannot create new rune, it overlaps with an existing rune");
        return;
      }

      if (!rune.getSignature().isValid()) {
        player.sendMessage("Cannot create new rune, it's signature is invalid");
        return;
      }

      runeManager.addRune(rune);

      rune.onCreate();
    }
  }
}
