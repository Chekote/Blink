package com.monkeyinabucket.bukkit.blink.group;

import com.monkeyinabucket.bukkit.blink.BlinkSignature;
import com.monkeyinabucket.bukkit.blink.rune.BlinkRune;
import java.util.ArrayList;

/**
 * Represents a group of BlinkRunes in a specific order, united by a common BlinkSignature.
 * @author Donald Tyler (chekote69@gmail.com)
 */
public class BlinkGroup implements Comparable<BlinkGroup> {

  /** The BlinkSignature that unites the BlinkRunes in this group */
  protected BlinkSignature signature;

  /** The BlinkRunes in this group */
  protected ArrayList<BlinkRune> members;

  /**
   * Constructor
   * @param signature the BlinkSignature for this BlinkGroup
   */
  public BlinkGroup(BlinkSignature signature) {
    this.signature = signature;
    members = new ArrayList<BlinkRune>();
  }

  /**
   * Provides the BlinkSignature for this BlinkGroup
   * TODO: Make this return a defensive copy
   * @return the BlinkSignature
   */
  public BlinkSignature getSignature() {
    return signature;
  }

  /**
   * Adds a BlinkRune to this BlinkGroup. The BlinkRune will be added to the end of the members
   * list, and will therefore be the last BlinkRune destination in this BlinkGroup.
   * @param rune the BlinkRune to add
   */
  public void add(BlinkRune rune) {
    members.add(rune);
  }

  /**
   * Removes a BlinkRune from this BlinkGroup.
   * @param rune the BlinkRune to remove
   */
  public void remove(BlinkRune rune) {
    members.remove(rune);
  }

  /**
   * Provides a defensive copy of all members of this BlinkGroup.
   * @return the members
   */
  public ArrayList<BlinkRune> getMembers() {
    return (ArrayList<BlinkRune>) members.clone();
  }

  /**
   * Provides the number of BlinkRunes in this BlinkGroup
   * @return the size
   */
  public int size() {
    return members.size();
  }

  /**
   * Given an existing BlinkRune from this BlinkGroup, provides the next BlinkRune in the members
   * list. This is used to determine the Players destination when activating a BlinkRune.
   * @param srcRune the existing BlinkRune reference point
   * @return the next BlinkRune, or null if the specified BlinkRune is the only one in this BlinkGroup
   * @throws NoSuchMemberException if the specified BlinkRune is not a member of this BlinkGroup
   */
  public BlinkRune getNext(BlinkRune srcRune) throws NoSuchMemberException {
    int size = members.size();
    if (size == 1) {
      return null;
    }

    int i = members.indexOf(srcRune);
    if (i == -1) {
      throw new NoSuchMemberException();
    }

    if (i == size - 1) {
      return members.get(0);
    } else {
      return members.get(i + 1);
    }
  }

  /**
   * Provides a String representation of this BlinkGroup
   * @return the string
   */
  public String toString() {
    StringBuilder s = new StringBuilder();

    s.append("BlinkGroup{\n");
    
    s.append("\tsignature=");
    s.append(signature);
    s.append(",\n");

    s.append("\tmembers=");
    s.append(members);
    s.append(",\n");
    
    s.append("}");

    return s.toString();
  }

  @Override
  public int compareTo(BlinkGroup group) {
    if (group == null) {
      return 0;
    }

    if (group.getSignature().equals(signature)) {
      return 0;
    }
    
    return -1;
  }
}
