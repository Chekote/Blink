/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.monkeyinabucket.bukkit.teleport;

import com.monkeyinabucket.bukkit.teleport.group.TeleportGroup;
import com.monkeyinabucket.bukkit.teleport.rune.TeleportRune;
import java.util.ArrayList;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

/**
 *
 * @author dtyler
 */
public class RuneManager {

  private final ArrayList<TeleportRune> runes;
  private final ArrayList<TeleportGroup> groups;

  public RuneManager() {
    runes = new ArrayList<TeleportRune>();
    groups = new ArrayList<TeleportGroup>();
  }

  public TeleportRune getRuneByCenter(Block block) {
    return getRuneByCenter(block.getLocation());
  }

  public TeleportRune getRuneByCenter(Location loc) {
    for(TeleportRune rune : runes) {
      if (rune.getLocation().equals(loc)) {
        return rune;
      }
    }

    return null;
  }

  public TeleportRune getRuneByPart(Block block) {
    return getRuneByPart(block.getLocation());
  }

  public TeleportRune getRuneByPart(Location loc) {
    for(TeleportRune rune : runes) {
      if (rune.isPart(loc)) {
        return rune;
      }
    }

    return null;
  }

  public void addRune(TeleportRune rune) {
    runes.add(rune);

    TeleportGroup group = getOrCreateGroup(rune.getSignature());
    group.add(rune);

    rune.setGroup(group);
  }

  public boolean runeHasOverlap(TeleportRune newRune) {
    for(TeleportRune rune : runes) {
      if (rune.overlaps(newRune)) {
        return true;
      }
    }

    return false;
  }

  protected TeleportGroup getOrCreateGroup(TeleportSignature signature) {
    Plugin.logInfo("Looking up TeleportGroup for signature:");
    Plugin.logInfo(signature);

    TeleportGroup group = getGroup(signature);

    if (group == null) {
      Plugin.logInfo("Creating new TeleportGroup.");
      group = new TeleportGroup(signature.clone());
      groups.add(group);
    } else {
      Plugin.logInfo("Found existing TeleportGroup.");      
    }

    return group;
  }

  public TeleportGroup getGroup(TeleportSignature signature) {
    for (TeleportGroup group : groups) {
      if (group.getSignature().equals(signature)) {
        return group;
      }
      
      Plugin.logInfo("Signatures don't match:");
      Plugin.logInfo(group.getSignature());
      Plugin.logInfo(signature);
    }
 
    return null;
  }
}
