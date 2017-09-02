package com.monkeyinabucket.blink.group;

import java.util.ArrayList;

import com.google.common.base.Joiner;
import com.monkeyinabucket.blink.rune.BlinkRune;
import com.monkeyinabucket.blink.rune.BlinkSignature;

import javax.annotation.Nonnull;

/**
 * Represents a group of BlinkRunes in a specific order, united by a common BlinkSignature.
 */
public class BlinkGroup implements Comparable<BlinkGroup> {

  /** The BlinkSignature that unites the BlinkRunes in this group */
  private BlinkSignature signature;

  /** The BlinkRunes in this group */
  private ArrayList<BlinkRune> members;

  /**
   * Constructor
   * 
   * @param signature the BlinkSignature for this BlinkGroup
   */
  public BlinkGroup(BlinkSignature signature) {
    this.signature = signature;
    members = new ArrayList<BlinkRune>();
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
    return new ArrayList<BlinkRune>(members);
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
    return "BlinkGroup{\n" +
      "\tsig=" + signature + ",\n" +
      "\trunes=[\n" +
        "\t\t" + Joiner.on(",\n\t\t").join(members) + "\n" +
      "\t]\n" +
    "}";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int compareTo(@Nonnull BlinkGroup group) {
    if (group.getSignature().equals(signature)) {
      return 0;
    }

    return -1;
  }
}
