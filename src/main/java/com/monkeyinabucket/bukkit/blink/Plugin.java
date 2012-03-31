package com.monkeyinabucket.bukkit.blink;

import com.monkeyinabucket.bukkit.SerializableLocation;
import com.monkeyinabucket.bukkit.blink.listener.BlockListener;
import com.monkeyinabucket.bukkit.blink.listener.RuneListener;
import com.monkeyinabucket.bukkit.blink.rune.BlinkRune;
import java.io.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Server;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main class for the Blink plugin. Handles the enabling and disabling process, provides centralized
 * logging features, and acts as the primary container for all objects in the plugin.
 * 
 * TODO: Handle events that may change or break a rune: ice melt, piston push, block placed in
 *       air signature slot, grass grow, etc.
 *
 * @author Donald Tyler (chekote69@gmail.com)
 */
public class Plugin extends JavaPlugin {

  /** The MineCraft Logger to send messages through */
  private static final Logger log = Logger.getLogger("Minecraft");

  /** A prefix that will be appended to all log messages from this plugin */
  private static String logPrefix = "[Blink] ";

  /** The data file that will be used to save and load rune locations */
  private static String saveFile = "plugins/blink.sav";

  /** The primary object used to manager runes in the plugin */
  private final RuneManager runeManager = RuneManager.getInstance();

  /** The listener responsible for detecting and handling player/rune interactions */
  private final RuneListener playerListener = new RuneListener();

  /** The listener responsible for detecting and handling block/rune interactions */
  private final BlockListener blockListener = new BlockListener();

  /**
   * Provides the runeManager instance
   * @return the RuneManager
   */
  public RuneManager getManager() {
    return runeManager;
  }

  /**
   * Called when this Plugin is enabled. Initializes the plugin and loads all saved Runes.
   */
  @Override
  public void onEnable() {
    // Register our events
    PluginManager pm = getServer().getPluginManager();
    pm.registerEvents(playerListener, this);
    pm.registerEvents(blockListener, this);

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
      BlinkRune rune = new BlinkRune(loc.getLocation(server).getBlock());
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
   * Called when this Plugin is disabled. Shuts down the plugin, and saves all Runes.
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

  /**
   * Logs an INFO level message to the logger.
   * @param message the thing to log
   */
  public static void logInfo(Object message) {
    StringBuilder b = new StringBuilder();
    b.append(logPrefix);
    b.append(message);
    log.info(b.toString());
  }

  /**
   * Logs a WARNING level message to the logger.
   * @param message the thing to log
   */
  public static void logWarning(Object message) {
    StringBuilder b = new StringBuilder();
    b.append(logPrefix);
    b.append(message);
    log.warning(b.toString());
  }

  /**
   * Logs a SEVERE level message to the logger.
   * @param message the thing to log
   */
  public static void logSevere(Object message) {
    StringBuilder b = new StringBuilder();
    b.append(logPrefix);
    b.append(message);
    log.severe(b.toString());
  }
}
