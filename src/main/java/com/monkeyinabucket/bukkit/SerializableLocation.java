package com.monkeyinabucket.bukkit;

import java.io.Serializable;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;

/**
 * Wrapper for a bukkit Location that implements Serializable. This is used to save the location
 * of runes.
 *
 * @author Donald Tyler (chekote69@gmail.com)
 */
public class SerializableLocation implements Serializable {

    private UUID worldUID;
    private double x;
    private double y;
    private double z;
    private float pitch;
    private float yaw;

    /**
     * Constructor
     * @param loc the location to serialize
     */
    public SerializableLocation(Location loc) {
      worldUID = loc.getWorld().getUID();
      x = loc.getX();
      y = loc.getY();
      z = loc.getZ();
      pitch = loc.getPitch();
      yaw = loc.getYaw();
    }

    /**
     * Provides the Location that this object serialized.
     *
     * @param server the server that we are running in
     * @return the Location
     */
    public Location getLocation(Server server) {
      World world = server.getWorld(worldUID);
      return new Location(world, x, y, z, yaw, pitch);
    }
}
