package com.monkeyinabucket.bukkit.teleport.listener;


import java.util.logging.Logger;

import com.monkeyinabucket.bukkit.teleport.Plugin;
import com.monkeyinabucket.bukkit.teleport.RuneManager;
import com.monkeyinabucket.bukkit.teleport.rune.Rune;
import com.monkeyinabucket.bukkit.teleport.rune.TeleportRune;
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
 * @author Donald Tyler (chekote69@gmail.com)
 * 
 * Possible bugs:
 *  - Overlapping runes
 */
public class RuneListener extends PlayerListener {

  private final RuneManager runeManager;

  public RuneListener(final RuneManager runeManager) {
    this.runeManager = runeManager;
  }

  @Override
  public void onPlayerInteract(PlayerInteractEvent event) {
    // only handle right clicks, and only when not placing blocks
    if (event.getAction() != Action.RIGHT_CLICK_BLOCK || event.isBlockInHand()) {
      return;
    }

    Block block = event.getClickedBlock();

    Plugin.logInfo("Player clicked a block of type " + block.getType());

    if (block.getType() != Material.OBSIDIAN) {
      Plugin.logInfo("Clicked block is not obsidian");
      return;
    }

    if (!Rune.hasRuneShell(block)) {
      Plugin.logInfo("Block does not have a rune shell");
      return;
    }

    Plugin.logInfo("Block has a rune shell");
    processRuneInteract(event);
  }

  protected void processRuneInteract(PlayerInteractEvent event) {
    Block block = event.getClickedBlock();
    Player player = event.getPlayer();

    // determine if this rune is already registered
    TeleportRune rune = runeManager.getRuneByCenter(block);

    if (rune != null) {
      // existing rune...
      player.sendMessage("Activating rune.");
      Plugin.logInfo("Rune is already registered. Activating...");
      rune.activate(player);
    } else {
      // potential new rune...

      Plugin.logInfo("Found potential new rune.");

      rune = new TeleportRune(block);

      // determine if any of the blocks are overlapping with another rune
      if (runeManager.runeHasOverlap(rune)) {
        player.sendMessage("Cannot create new rune, it overlaps with an existing rune");
        Plugin.logInfo("Cannot register new rune, it overlaps with an existing rune");
        return;
      }

      if (!rune.getSignature().isValid()) {
        player.sendMessage("Cannot create new rune, it's signature is invalid");
        Plugin.logInfo("Cannot register new rune, it's signature is invalid");
        return;
      }

      player.sendMessage("A new rune has been created");
      Plugin.logInfo("Registering new rune.");
      runeManager.addRune(rune);
    }
  }
}
