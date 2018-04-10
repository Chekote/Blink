package com.monkeyinabucket.forge.blink.rune;

import com.monkeyinabucket.forge.blink.Blink;
import com.monkeyinabucket.forge.blink.group.BlinkGroup;
import com.monkeyinabucket.forge.blink.group.NoSuchMemberException;
import com.monkeyinabucket.forge.world.Location;
import com.google.gson.JsonObject;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

/**
 * Represents a BlinkRune.
 */
public class BlinkRune extends Rune implements Comparable<BlinkRune> {

  /** The BlinkSignature of this BlinkRune */
  protected BlinkSignature signature;

  /** The BlinkGroup that this BlinkRune belongs to */
  protected BlinkGroup group;

  protected RuneManager runeManager;

  /**
   * Constructor
   * 
   * @param loc	The Location of the BlinkRune
   */
  public BlinkRune(Location loc) {
    // save defensive copy
    this.loc = loc.clone();

    this.signature = new BlinkSignature(loc.getRelative(EnumFacing.NORTH).getBlock(), loc
        .getRelative(EnumFacing.EAST).getBlock(), loc.getRelative(EnumFacing.SOUTH)
        .getBlock(), loc.getRelative(EnumFacing.WEST).getBlock());

    runeManager = RuneManager.getInstance();
  }

  /**
   * Constructor
   *
   * @param loc the Location of the BlinkRune.
   * @param sig the Signature of the BlinkRune.
   */
  public BlinkRune(Location loc, BlinkSignature sig) {
    // save defensive copies
    this.loc = loc.clone();
    this.signature = sig.clone();

    runeManager = RuneManager.getInstance();
  }

  /**
   * Provides a defensive copy of the location of the center Block if this
   * BlinkRune
   *
   * @return the Location
   */
  public Location getLocation() {
    return loc.clone();
  }

  /**
   * Provides the signature of this BlinkRune
   *
   * @return the BlinkSignature
   */
  public BlinkSignature getSignature() {
    return signature;
  }

  /**
   * Sets the BlinkGroup that this BlinkRune belongs to
   * 
   * @param group the group that this BlinkRune belongs to.
   */
  public void setGroup(BlinkGroup group) {
    this.group = group;
  }

  /**
   * Gets the BlinkGroup that this BlinkRune belongs to
   * 
   * @return the BlinkGroup
   */
  public BlinkGroup getGroup() {
    return group;
  }

  /**
   * Invoked when any Block in this BlinkRune is damaged. Damaging a Block of a
   * BlinkRune will set the entire BlinkRune on fire if the "burning" config option is enabled.
   */
  public void onDamage() {
    if (Blink.burning) {
      setFire();
    }
  }

  /**
   * Sets the surface of the rune alight.
   */
  private void setFire() {
    for (Location loc : getParts()) {
      if (loc.world.isAirBlock(loc.pos.add(0, 1, 0))) {
        loc.getRelative(EnumFacing.UP).setBlock(Blocks.FIRE);
      }
    }
  }

  /**
   * Invoked when a BlinkRune is created. A successfully created BlinkRune will
   * be struck by lightning.
   */
  public void onCreate() {
    loc.world.addWeatherEffect(
        new EntityLightningBolt(loc.world, loc.pos.getX(), loc.pos.getY() + 1, loc.pos.getZ(), true)
    );
  }

  /**
   * Invoked when a BlinkRune is destroyed. A destroyed BlinkRune will be struck
   * by lightning.
   */
  public void onDestroy() {
    loc.world.addWeatherEffect(
        new EntityLightningBolt(loc.world, loc.pos.getX(), loc.pos.getY() + 1, loc.pos.getZ(), true)
    );
    runeManager.removeRune(this);
  }

  /**
   * Activates the BlinkRune, teleporting the Player to the next BlinkRune in the BlinkGroup.
   * 
   * @param player the Player that activated the BlinkRune
   */
  public void activate(EntityPlayer player) {
    BlinkRune targetRune;
    try {
      targetRune = group.getNext(this);
    } catch (NoSuchMemberException e) {
      return;
    }

    if (targetRune == null) {
      return;
    }

    Location targetLoc = targetRune.getDestination();
    int travelCost = this.getTravelCost(targetRune, player);

    if (player.experienceLevel < travelCost) {
      this.onActivateFail(player);
      return;
    }

    player.addExperienceLevel(-travelCost);

    if (targetLoc.world.provider.getDimension() != player.dimension) {
      player.changeDimension(targetLoc.world.provider.getDimension());
    }

    player.setPositionAndUpdate(targetLoc.pos.getX() + 0.5, targetLoc.pos.getY(), targetLoc.pos.getZ() + 0.5);
  }

  /**
   * Event handler for when a player attempts to activate a rune, but it fails.
   *
   * @param player the player that attempted to activate the rune.
   */
  protected void onActivateFail(EntityPlayer player) {
    EnumParticleTypes particle = EnumParticleTypes.SMOKE_LARGE;
    boolean longDistance = false;
    double x = this.loc.pos.getX() + 0.5;
    double y = this.loc.pos.getY() + 1;
    double z = this.loc.pos.getZ() + 0.5;
    int count = 1;
    double xOffset = 0.0;
    double yOffset = 0.0;
    double zOffset = 0.0;
    double speed = 0.0;

    World world = player.getEntityWorld();

    if (!(world instanceof WorldServer)) {
      return;
    }

    WorldServer worldServer = (WorldServer) world;

    if (player instanceof EntityPlayerMP) {
      EntityPlayerMP playerMP = (EntityPlayerMP) player;
      worldServer.spawnParticle(playerMP, particle, longDistance, x, y, z, count, xOffset, yOffset, zOffset, speed);
    } else {
      worldServer.spawnParticle(particle, longDistance, x, y, z, count, xOffset, yOffset, zOffset, speed);
    }

    worldServer.playSound(null, x, y, z, SoundEvents.ENTITY_BLAZE_SHOOT, SoundCategory.BLOCKS, 1.0F, 1.0F);
  }

  /**
   * Provides the cost in levels for travelling from this rune to the specified rune.
   *
   * @param  targetRune the rune being travelled to.
   * @return the cost
   */
  protected int getTravelCost(BlinkRune targetRune, EntityPlayer player) {
    Location targetLoc = targetRune.getLocation();

    int cost = 0;
    if (Blink.levelCostPerMeter > 0) {
      double distance = this.getLocation().getDistance(targetLoc, player);
      cost += Math.ceil(distance * Blink.levelCostPerMeter);
    }

    if (Blink.levelCostForDimensionTransit > 0) {
      if (this.loc.world.provider.getDimension() != targetLoc.world.provider.getDimension()) {
        cost += Blink.levelCostForDimensionTransit;
      }
    }

    return cost;
  }

  /**
   * Provides the location that the player will teleport to when teleporting to this rune.
   *
   * This might be the block above the runes center, but we need to find two stacked blocks of empty space for the
   * player to blink safely. This might be higher if the rune is buried. This could ultimately be the top of the world.
   *
   * @return the destination.
   */
  protected Location getDestination() {
    Location destination = this.getLocation().getRelative(EnumFacing.UP);
    while (destination.getBlock() != null) {
      if (isValidDestination(destination)) {
        break;
      }

      destination = destination.getRelative(EnumFacing.UP);
    }

    return destination;
  }

  /**
   * Determines if a Location is a valid teleport destination for a Player. A
   * Location is considered a valid destination if it is not solid and the Block
   * directly above it is also not solid. This will provide enough room for the
   * Player to teleport in without suffocating.
   * 
   * @param loc the Block to check
   * @return true if the destination is valid, false if not
   */
  protected boolean isValidDestination(Location loc) {
    Block block = loc.getBlock();
    if (block == null) {
      return true;
    }

    if (!loc.world.getBlockState(loc.pos).getMaterial().isSolid()) {
      Location locAbove = loc.getRelative(EnumFacing.UP);
      Block blockAbove = locAbove.getBlock();

      if (blockAbove == null || !loc.world.getBlockState(locAbove.pos).getMaterial().isSolid()) {
        return true;
      }
    }

    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return "BlinkRune" + toJson().toString();
  }

  /**
   * Provides a JsonObject that describes this BlinkRune.
   *
   * This method is intended for use in saving data, and debugging.
   *
   * @return the builder.
   */
  public JsonObject toJson() {
    return loc.toJson();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int compareTo(@Nullable BlinkRune rune) {
    if (rune == null) {
      return 0;
    }

    if (rune.getLocation().equals(loc)) {
      return 0;
    }

    return -1;
  }
}
