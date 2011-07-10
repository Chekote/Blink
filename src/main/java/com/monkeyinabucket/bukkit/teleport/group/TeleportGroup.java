package com.monkeyinabucket.bukkit.teleport.group;

import com.monkeyinabucket.bukkit.teleport.Plugin;
import com.monkeyinabucket.bukkit.teleport.TeleportSignature;
import com.monkeyinabucket.bukkit.teleport.rune.TeleportRune;
import java.util.ArrayList;
import org.bukkit.Material;

/**
 *
 * @author dtyler
 */
public class TeleportGroup {

  protected TeleportSignature signature;
  protected ArrayList<TeleportRune> members;

  public TeleportGroup(TeleportSignature signature) {
    this.signature = signature;
    members = new ArrayList<TeleportRune>();
  }

  public TeleportSignature getSignature() {
    return signature;
  }

  public void add(TeleportRune rune) {
    Plugin.logInfo("Adding: ");
    Plugin.logInfo(rune);
    Plugin.logInfo("To: ");
    Plugin.logInfo(this);

    members.add(rune);
  }

  public void remove(TeleportRune rune) {
    Plugin.logInfo("Removing: ");
    Plugin.logInfo(rune);
    Plugin.logInfo("From: ");
    Plugin.logInfo(this);

    members.remove(rune);
  }

  public TeleportRune getNext(TeleportRune srcRune) throws NoSuchMemberException {
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

    s.append("TeleportGroup{\n");
    
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
