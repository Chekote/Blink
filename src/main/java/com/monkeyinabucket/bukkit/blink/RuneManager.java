/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.monkeyinabucket.bukkit.blink;

import com.monkeyinabucket.bukkit.SerializableLocation;
import com.monkeyinabucket.bukkit.blink.group.BlinkGroup;
import com.monkeyinabucket.bukkit.blink.rune.BlinkRune;
import java.util.ArrayList;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

/**
 *
 * @author dtyler
 */
public class RuneManager {

  private final ArrayList<BlinkRune> runes;
  private final ArrayList<BlinkGroup> groups;

  public RuneManager() {
    runes = new ArrayList<BlinkRune>();
    groups = new ArrayList<BlinkGroup>();
  }

  public BlinkRune getRuneByCenter(Block block) {
    return getRuneByCenter(block.getLocation());
  }

  public BlinkRune getRuneByCenter(Location loc) {
    for(BlinkRune rune : runes) {
      if (rune.getLocation().equals(loc)) {
        return rune;
      }
    }

    return null;
  }

  public BlinkRune getRuneByPart(Block block) {
    return getRuneByPart(block.getLocation());
  }

  public BlinkRune getRuneByPart(Location loc) {
    for(BlinkRune rune : runes) {
      if (rune.isPart(loc)) {
        return rune;
      }
    }

    return null;
  }

  public void addRune(BlinkRune rune) {
    runes.add(rune);

    BlinkGroup group = getOrCreateGroup(rune.getSignature());
    group.add(rune);

    rune.setGroup(group);
  }

  public void removeRune(BlinkRune rune) {
    runes.remove(rune);

    BlinkGroup group = rune.getGroup();
    group.remove(rune);

    rune.setGroup(null);

    // if the group is now empty, remove it
    if (group.size() == 0) {
      groups.remove(group);
    }
  }

  public boolean runeHasOverlap(BlinkRune newRune) {
    for(BlinkRune rune : runes) {
      if (rune.overlaps(newRune)) {
        return true;
      }
    }

    return false;
  }

  protected BlinkGroup getOrCreateGroup(BlinkSignature signature) {
    Plugin.logInfo("Looking up BlinkGroup for signature:");
    Plugin.logInfo(signature);

    BlinkGroup group = getGroup(signature);

    if (group == null) {
      Plugin.logInfo("Creating new BlinkGroup.");
      group = new BlinkGroup(signature.clone());
      groups.add(group);
    } else {
      Plugin.logInfo("Found existing BlinkGroup.");      
    }

    return group;
  }

  public BlinkGroup getGroup(BlinkSignature signature) {
    for (BlinkGroup group : groups) {
      if (group.getSignature().equals(signature)) {
        return group;
      }
      
      Plugin.logInfo("Signatures don't match:");
      Plugin.logInfo(group.getSignature());
      Plugin.logInfo(signature);
    }
 
    return null;
  }

  public ArrayList<SerializableLocation> getLocationsForSave() {
    ArrayList<SerializableLocation> locs = new ArrayList<SerializableLocation>();

    // we need to loop through the groups instead of the runes, so that when the runes are loaded
    // later, they are created and added back to groups in the same order.
    for (BlinkGroup group : groups) {
      ArrayList<BlinkRune> members = group.getMembers();
      for (BlinkRune rune : members) {
        locs.add(new SerializableLocation(rune.getLocation()));
      }
    }

    return locs;
  }
}
