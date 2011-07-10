package com.monkeyinabucket.bukkit.teleport;

import com.monkeyinabucket.bukkit.SerializableLocation;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.monkeyinabucket.bukkit.teleport.listener.RuneListener;
import com.monkeyinabucket.bukkit.teleport.listener.BlockListener;
import com.monkeyinabucket.bukkit.teleport.rune.TeleportRune;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import org.bukkit.Server;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;

/**
 * TODO: Handle events that may change or break a rune: ice melt, piston push, block placed in
 *       air signature slot, grass grow, etc.
 * @author dtyler
 */
public class Plugin extends JavaPlugin {

  private static final Logger log = Logger.getLogger("Minecraft");
  private static String logPrefix = "[Teleport] ";
  private static String saveFile = "plugins/teleport.sav";

  private final RuneManager runeManager = new RuneManager();
  private final RuneListener playerListener = new RuneListener(runeManager);
  private final BlockListener blockListener = new BlockListener(runeManager);

  public RuneManager getManager() {
    return runeManager;
  }

  /**
   * Called when this Plugin is enabled
   */
  @Override
  public void onEnable() {
    // Register our events
    PluginManager pm = getServer().getPluginManager();
    pm.registerEvent(Event.Type.PLAYER_INTERACT, playerListener, Priority.Normal, this);
    pm.registerEvent(Event.Type.BLOCK_DAMAGE, blockListener, Priority.Normal, this);
    pm.registerEvent(Event.Type.BLOCK_BREAK, blockListener, Priority.Normal, this);

    // TODO: load saved runes
    ArrayList<SerializableLocation> locs = null;
    try {
      ObjectInputStream is = new ObjectInputStream(new FileInputStream(saveFile));
      locs = (ArrayList<SerializableLocation>) is.readObject();
    } catch (FileNotFoundException ex) {
      locs = new ArrayList<SerializableLocation>();
    } catch (IOException ex) {
      log.log(Level.SEVERE, null, ex);
      return;
    } catch (ClassNotFoundException ex) {
      log.log(Level.SEVERE, null, ex);
      return;
    }

    Server server = getServer();
    for (SerializableLocation loc : locs) {
      TeleportRune rune = new TeleportRune(loc.getLocation(server).getBlock());
      runeManager.addRune(rune);
    }

    StringBuilder b = new StringBuilder();
    b.append("Loaded ");
    b.append(locs.size());
    b.append(" runes.");
    logInfo(b);

    // log that we're enabled
    PluginDescriptionFile pdfFile = this.getDescription();

    b = new StringBuilder();
    b.append(pdfFile.getName());
    b.append(" version ");
    b.append(pdfFile.getVersion());
    b.append(" is enabled!");
    logInfo(b);
  }

  /**
   * Called when this Plugin is disabled
   */
  @Override
  public void onDisable() {
    ArrayList<SerializableLocation> locs = runeManager.getLocationsForSave();
    try {
      ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(saveFile));
      os.writeObject(locs);
      os.flush();
      os.close();
    } catch (IOException ex) {
      log.log(Level.SEVERE, null, ex);
    }

    StringBuilder b = new StringBuilder();
    b.append("Saved ");
    b.append(locs.size());
    b.append(" runes.");
    logInfo(b);

    PluginDescriptionFile pdfFile = this.getDescription();

    b = new StringBuilder();
    b.append(pdfFile.getName());
    b.append(" version ");
    b.append(pdfFile.getVersion());
    b.append(" is disabled.");
    logInfo(b);
  }

  public static void logInfo(Object message) {
    StringBuilder b = new StringBuilder();
    b.append(logPrefix);
    b.append(message);
    log.info(b.toString());
  }

  public static void logWarning(Object message) {
    StringBuilder b = new StringBuilder();
    b.append(logPrefix);
    b.append(message);
    log.warning(b.toString());
  }

  public static void logSevere(Object message) {
    StringBuilder b = new StringBuilder();
    b.append(logPrefix);
    b.append(message);
    log.severe(b.toString());
  }
}
