package com.monkeyinabucket.forge.blink.rune;

import java.util.Set;
import java.util.TreeSet;

import com.google.gson.JsonArray;
import com.monkeyinabucket.forge.blink.group.BlinkGroup;
import com.monkeyinabucket.forge.world.Location;

/**
 * Manages runes, groups and their life-cycles.
 * 
 * This class is responsible for: - Processing newly created runes, ensuring
 * they are added to the correct group - Processing destroyed runes, ensuring
 * they are removed from the correct group - Creating and destroying groups as
 * needed - Providing serializable locations of the Runes for saving
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
   * Returns the BlinkRune who's center Location matches the specified Location
   * 
   * @param loc the Location to use as the point of reference
   * @return the BlinkRune, or null if there is no BlinkRune at the specified
   *         Location
   */
  public BlinkRune getRuneByCenter(Location loc) {
    for (BlinkRune rune : runes) {
      if (rune.getLocation().equals(loc)) {
        return rune;
      }
    }

    return null;
  }

  /**
   * Returns the BlinkRune which contains the specified Location
   * 
   * @param loc the Location to use as the point of reference
   * @return the BlinkRune, or null if the specified Location is not part of a
   *         BlinkRune
   */
  public BlinkRune getRuneByPart(Location loc) {
    for (BlinkRune rune : runes) {
      if (rune.isPart(loc)) {
        return rune;
      }
    }

    return null;
  }

  /**
   * Adds a new BlinkRune to the manager, and sets up the group associations
   * based on the BlinkRunes signature.
   * 
   * @param rune the BlinkRune to add
   */
  public void addRune(BlinkRune rune) {
    runes.add(rune);

    BlinkGroup group = getOrCreateGroup(rune.getSignature());
    group.add(rune);

    rune.setGroup(group);
  }

  /**
   * Removes all runes from the manager.
   */
  public void clearRunes() {
    groups.clear();
    runes.clear();
  }

  /**
   * Removes a BlinkRune from the manager, and cleans up the group associations.
   * This method will essentially deactivate the rune.
   * 
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
   * Determines if the specified BlinkRune overlaps with any other BlinkRune
   * already registered with this RuneManager.
   * 
   * @param newRune the BlinkRune to check
   * @return true if the BlinkRune overlaps, false if not.
   */
  public boolean runeHasOverlap(BlinkRune newRune) {
    for (BlinkRune rune : runes) {
      if (rune.overlaps(newRune)) {
        return true;
      }
    }

    return false;
  }

  /**
   * Either returns the existing BlinkGroup for the specified BlinkSignature, or
   * creates a new one and returns that.
   * 
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
   * 
   * @param signature the BlinkSignature to get the BlinkGroup for
   * @return the BlinkGroup, or null if no BlinkGroup currently exists for the
   *         specified BlinkSignature
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
   * Returns all of the BlinkGroups.
   * 
   * @return the BlinkGroups
   */
  public Set<BlinkGroup> getGroups() {
    return groups;
  }

  /**
   * Provides a JsonArrayBuilder that describes all rune groups.
   *
   * This method is intended for use in saving data, and debugging.
   *
   * @return the builder.
   */
  public JsonArray toJsonBuilder() {
    JsonArray array = new JsonArray();

    for (BlinkGroup group : groups) {
      array.add(group.toJson());
    }

    return array;
  }
}
