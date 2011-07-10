package com.monkeyinabucket.bukkit.blink.group;

import com.monkeyinabucket.bukkit.blink.Plugin;
import com.monkeyinabucket.bukkit.blink.BlinkSignature;
import com.monkeyinabucket.bukkit.blink.rune.BlinkRune;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import org.bukkit.Material;

/**
 *
 * @author dtyler
 */
public class BlinkGroup {

  protected BlinkSignature signature;
  protected ArrayList<BlinkRune> members;

  public BlinkGroup(BlinkSignature signature) {
    this.signature = signature;
    members = new ArrayList<BlinkRune>();
  }

  public BlinkSignature getSignature() {
    return signature;
  }

  public void add(BlinkRune rune) {
    Plugin.logInfo("Adding: ");
    Plugin.logInfo(rune);
    Plugin.logInfo("To: ");
    Plugin.logInfo(this);

    members.add(rune);
  }

  public void remove(BlinkRune rune) {
    Plugin.logInfo("Removing: ");
    Plugin.logInfo(rune);
    Plugin.logInfo("From: ");
    Plugin.logInfo(this);

    members.remove(rune);
  }

  public ArrayList<BlinkRune> getMembers() {
    // return a defensive copy
    return (ArrayList<BlinkRune>) members.clone();
  }

  public int size() {
    return members.size();
  }

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
}
