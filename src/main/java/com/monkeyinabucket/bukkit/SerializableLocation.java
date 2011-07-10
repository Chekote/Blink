/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.monkeyinabucket.bukkit;

import java.io.Serializable;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;

/**
 *
 * @author dtyler
 */
public class SerializableLocation implements Serializable {

    private long worldUID;
    private double x;
    private double y;
    private double z;
    private float pitch;
    private float yaw;

    public SerializableLocation(Location loc) {
      worldUID = loc.getWorld().getUID();
      x = loc.getX();
      y = loc.getY();
      z = loc.getZ();
      pitch = loc.getPitch();
      yaw = loc.getYaw();
    }

    public Location getLocation(Server server) {
      World world = server.getWorld(worldUID);
      return new Location(world, x, y, z, yaw, pitch);
    }
}
