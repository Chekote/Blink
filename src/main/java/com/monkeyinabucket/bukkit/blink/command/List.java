package com.monkeyinabucket.bukkit.blink.command;

import com.monkeyinabucket.bukkit.blink.Plugin;
import com.monkeyinabucket.bukkit.blink.RuneManager;
import com.monkeyinabucket.bukkit.blink.group.BlinkGroup;
import java.util.logging.Logger;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Handler for the /blinklist command.
 * @author Donald Tyler (chekote69@gmail.com)
 */
public class List implements CommandExecutor {

  private final Plugin plugin;

  /** Reference to the plugin's runeManager */
  private final RuneManager runeManager;

  public List(Plugin plugin) {
    this.plugin = plugin;
    runeManager = RuneManager.getInstance();
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (!(sender instanceof Player)) {
      return false;
    }
    Player player = (Player) sender;
    
    if (!player.isOp()) {
      sender.sendMessage("Sorry, you must be Op to run this command.");
      return false;
    }

    for (BlinkGroup group : runeManager.getGroups()) {
      String[] parts = group.toString()
          .replaceAll("\t", " ")
          .replaceAll("BlinkRune", "")
          .replaceAll("BlinkSignature", "")
          .split("\n");

      for (String part : parts) {
        sender.sendMessage(part);
      }
    }

    return true;
  }
}
