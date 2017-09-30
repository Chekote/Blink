package com.monkeyinabucket.forge.blink.group;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.monkeyinabucket.forge.blink.rune.BlinkRune;
import com.monkeyinabucket.forge.blink.rune.BlinkSignature;
import java.util.ArrayList;
import javax.annotation.Nullable;

/**
 * Represents a group of BlinkRunes in a specific order, united by a common BlinkSignature.
 */
public class BlinkGroup implements Comparable<BlinkGroup> {

  /** The BlinkSignature that unites the BlinkRunes in this group */
  protected BlinkSignature signature;

  /** The BlinkRunes in this group */
  protected ArrayList<BlinkRune> members;

  /**
   * Constructor
   * 
   * @param signature the BlinkSignature for this BlinkGroup
   */
  public BlinkGroup(BlinkSignature signature) {
    this.signature = signature;
    members = new ArrayList<>();
  }

  /**
   * Provides the BlinkSignature for this BlinkGroup
   *
   * @return the BlinkSignature
   */
  public BlinkSignature getSignature() {
    return signature.clone();
  }

  /**
   * Adds a BlinkRune to this BlinkGroup. The BlinkRune will be added to the end
   * of the members list, and will therefore be the last BlinkRune destination
   * in this BlinkGroup.
   * 
   * @param rune the BlinkRune to add
   */
  public void add(BlinkRune rune) {
    members.add(rune);
  }

  /**
   * Removes a BlinkRune from this BlinkGroup.
   * 
   * @param rune the BlinkRune to remove
   */
  public void remove(BlinkRune rune) {
    members.remove(rune);
  }

  /**
   * Provides a defensive copy of all members of this BlinkGroup.
   * 
   * @return the members
   */
  public ArrayList<BlinkRune> getMembers() {
    return new ArrayList<>(members);
  }

  /**
   * Provides the number of BlinkRunes in this BlinkGroup
   * 
   * @return the size
   */
  public int size() {
    return members.size();
  }

  /**
   * Given an existing BlinkRune from this BlinkGroup, provides the next
   * BlinkRune in the members list. This is used to determine the Players
   * destination when activating a BlinkRune.
   * 
   * @param srcRune the existing BlinkRune reference point
   * @throws NoSuchMemberException if the specified BlinkRune is not a member of
   *           this BlinkGroup
   * @return the next BlinkRune, or null if the specified BlinkRune is the only
   *         one in this BlinkGroup
   */
  public BlinkRune getNext(BlinkRune srcRune) throws NoSuchMemberException {
    // ensure the source rune is in this group
    int i = members.indexOf(srcRune);
    if (i == -1) {
      throw new NoSuchMemberException();
    }

    // return null if the source rune is the only rune in the group
    int size = members.size();
    if (size == 1) {
      return null;
    }

    // return the first rune in the group if the source rune is the last
    if (i == size - 1) {
      return members.get(0);
    }

    // return the next rune in the group
    return members.get(i + 1);
  }

  /**
   * Provides a String representation of this BlinkGroup
   * 
   * @return the string
   */
  public String toString() {
    return "BlinkGroup" + toJson().toString();
  }

  /**
   * Provides a JsonObject that describes this BlinkGroup.
   *
   * This method is intended for use in saving data, and debugging.
   *
   * @return the builder.
   */
  public JsonObject toJson() {
    JsonArray runes = new JsonArray();
    for (BlinkRune member : members) {
      runes.add(member.getLocation().toJson());
    }

    JsonObject group = new JsonObject();
    group.add("sig", signature.toJson());
    group.add("runes", runes);

    return group;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int compareTo(@Nullable BlinkGroup group) {
    if (group == null) {
      return 0;
    }

    if (group.getSignature().equals(signature)) {
      return 0;
    }

    return -1;
  }
}
