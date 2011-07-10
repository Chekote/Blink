package com.monkeyinabucket.bukkit.teleport;

import java.util.logging.Logger;
import com.monkeyinabucket.bukkit.teleport.listener.RuneListener;
import com.monkeyinabucket.bukkit.teleport.listener.BlockListener;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;

/**
 * TODO: Implement saving of runes (record coords only, not need to save objects)
 * TODO: Implement loading of runes
 * @author dtyler
 */
public class Plugin extends JavaPlugin {

  private static final Logger log = Logger.getLogger("Minecraft");
  private static String logPrefix = "[Teleport] ";

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

    // log that we're enabled
    PluginDescriptionFile pdfFile = this.getDescription();

    StringBuilder builder = new StringBuilder();
    builder.append(pdfFile.getName());
    builder.append(" version ");
    builder.append(pdfFile.getVersion());
    builder.append(" is enabled!");
    logInfo(builder.toString());
  }

  /**
   * Called when this Plugin is disabled
   */
  @Override
  public void onDisable() {
    PluginDescriptionFile pdfFile = this.getDescription();
    
    StringBuilder builder = new StringBuilder();
    builder.append(pdfFile.getName());
    builder.append(" version ");
    builder.append(pdfFile.getVersion());
    builder.append(" is disabled.");
    logInfo(builder.toString());
  }

  public static void logInfo(Object message) {
    StringBuilder builder = new StringBuilder();
    builder.append(logPrefix);
    builder.append(message);
    log.info(builder.toString());
  }

  public static void logWarning(Object message) {
    StringBuilder builder = new StringBuilder();
    builder.append(logPrefix);
    builder.append(message);
    log.warning(builder.toString());
  }

  public static void logSevere(Object message) {
    StringBuilder builder = new StringBuilder();
    builder.append(logPrefix);
    builder.append(message);
    log.severe(builder.toString());
  }
}
