package com.monkeyinabucket.bukkit.blink;

import com.monkeyinabucket.bukkit.SerializableLocation;
import com.monkeyinabucket.bukkit.blink.group.BlinkGroup;
import com.monkeyinabucket.bukkit.blink.rune.BlinkRune;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;
import org.bukkit.Location;
import org.bukkit.block.Block;

/**
 * Managers runes, groups and their life-cycles.
 *
 * This class is responsible for:
 * - Processing newly created runes, ensuring they are added to the correct group
 * - Processing destroyed runes, ensuring they are removed from the correct group
 * - Creating and destroying groups as needed
 * - Providing serializable locations of the Runes for saving
 *
 * @author Donald Tyler (chekote69@gmail.com)
 */
public class RuneManager {

  /** All runes that are currently active on the server */
  private final Set<BlinkRune> runes;

  /** All rune groups that are currently active on the server */
  private final Set<BlinkGroup> groups;

  private static RuneManager instance;

  public static RuneManager getInstance() {
    if (instance == null) {
      instance = new RuneManager();
    }
    
    return instance;
  }

  /**
   * Constructor
   */
  private RuneManager() {
    runes = new TreeSet<BlinkRune>();
    groups = new TreeSet<BlinkGroup>();
  }

  /**
   * Returns the BlinkRune who's center Block matches the specified Block
   * @param block the block to use as the point of reference
   * @return the BlinkRune, or null if there is no BlinkRune at the specified Block
   */
  public BlinkRune getRuneByCenter(Block block) {
    return getRuneByCenter(block.getLocation());
  }

  /**
   * Returns the BlinkRune who's center Location matches the specified Location
   * @param loc the Location to use as the point of reference
   * @return the BlinkRune, or null if there is no BlinkRune at the specified Location
   */
  public BlinkRune getRuneByCenter(Location loc) {
    for(BlinkRune rune : runes) {
      if (rune.getLocation().equals(loc)) {
        return rune;
      }
    }

    return null;
  }

  /**
   * Returns the BlinkRune which contains the specified Block
   * @param block the Block to use as the point of reference
   * @return the BlinkRune, or null if the specified Block is not part of a BlinkRune
   */
  public BlinkRune getRuneByPart(Block block) {
    return getRuneByPart(block.getLocation());
  }

  /**
   * Returns the BlinkRune which contains the specified Location
   * @param loc the Location to use as the point of reference
   * @return the BlinkRune, or null if the specified Location is not part of a BlinkRune
   */
  public BlinkRune getRuneByPart(Location loc) {
    for(BlinkRune rune : runes) {
      if (rune.isPart(loc)) {
        return rune;
      }
    }

    return null;
  }

  /**
   * Adds a new BlinkRune to the manager, and sets up the group associations based on the BlinkRunes
   * signature.
   * @param rune the BlinkRune to add
   */
  public void addRune(BlinkRune rune) {
    runes.add(rune);

    BlinkGroup group = getOrCreateGroup(rune.getSignature());
    group.add(rune);

    rune.setGroup(group);
  }

  /**
   * Removes a BlinkRune from the manager, and cleans up the group associations. This method will
   * essentially deactivate the rune.
   * @param rune the BlinkRune to remove
   */
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

  /**
   * Determines if the specified BlinkRune overlaps with any other BlinkRune already registered with
   * this RuneManager.
   * @param newRune the BlinkRune to check
   * @return true if the BlinkRune overlaps, false if not.
   */
  public boolean runeHasOverlap(BlinkRune newRune) {
    for(BlinkRune rune : runes) {
      if (rune.overlaps(newRune)) {
        return true;
      }
    }

    return false;
  }

  /**
   * Either returns the existing BlinkGroup for the specified BlinkSignature, or creates a new one
   * and returns that.
   * @param signature the BlinkSignature to get the BlinkGroup for
   * @return the BlinkGroup
   */
  protected BlinkGroup getOrCreateGroup(BlinkSignature signature) {
    BlinkGroup group = getGroup(signature);

    if (group == null) {
      group = new BlinkGroup(signature.clone());
      groups.add(group);
    }

    return group;
  }

  /**
   * Returns the BlinkGroup for the specified BlinkSignature.
   * @param signature the BlinkSignature to get the BlinkGroup for
   * @return the BlinkGroup, or null if no BlinkGroup currently exists for the specified BlinkSignature
   */
  public BlinkGroup getGroup(BlinkSignature signature) {
    for (BlinkGroup group : groups) {
      if (group.getSignature().equals(signature)) {
        return group;
      }
    }
 
    return null;
  }

  /**
   * Provides a list of SerializebleLocations for each registered BlinkRune for saving.
   * @return the list of rune locations
   */
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
