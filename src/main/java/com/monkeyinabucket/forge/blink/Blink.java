package com.monkeyinabucket.forge.blink;

import com.monkeyinabucket.forge.blink.block.RuneCore;
import com.monkeyinabucket.forge.blink.command.BlinkList;
import com.monkeyinabucket.forge.blink.command.BlinkLoad;
import com.monkeyinabucket.forge.blink.command.BlinkSave;
import com.monkeyinabucket.forge.blink.rune.BlinkRune;
import com.monkeyinabucket.forge.blink.rune.RuneManager;
import com.monkeyinabucket.forge.world.Location;
import com.monkeyinabucket.forge.world.SerializableLocation;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

import java.io.*;
import java.util.ArrayList;

/**
 * Main class for the Blink plugin. Handles the enabling and disabling process,
 * provides centralized logging features, and acts as the primary container for
 * all objects in the plugin.
 *
 * TODO: Handle events that may change or break a rune: ice melt, piston push, block placed in air signature slot, grass grow, etc.
 */
@Mod(modid = "blink", name = "Blink", version = "1.0.0")
public class Blink {

  /** The id of this mod */
  public static String mod_id = "blink";

  /** Flag to track whether the runes save file has already been loaded */
  private Boolean runeFileLoaded = false;

  /** The name of the rune save file. This will be stored in the world folder */
  @SuppressWarnings("FieldCanBeLocal")
  private static String SAVE_FILE_NAME = mod_id + ".sav";

  /** Configuration options for the mod */
  public static Configuration config;

  /** The size that runes have to be */
  public static int runeSize;

  /** Half size of the rune (excluding center). Stored for convenience */
  public static int halfRuneSize;

  /** The data file that will be used to save and load rune locations */
  protected String saveFile;

  /** The primary object used to manage runes in the plugin */
  protected final RuneManager runeManager = RuneManager.getInstance();

  public static RuneCore runecore;

  @Instance(value = "Blink")
  public static Blink instance;

  @EventHandler
  public void onServerLoadStart(FMLServerStartingEvent event) {
    event.registerServerCommand(new BlinkList());
    event.registerServerCommand(new BlinkLoad());
    event.registerServerCommand(new BlinkSave());
  }

  @EventHandler
  public void preInit(FMLPreInitializationEvent event) {
    runecore = new RuneCore(Material.iron);
    runecore.setHarvestLevel("pickaxe", 3);

    config = new Configuration(event.getSuggestedConfigurationFile());

    syncConfig();
  }

  /**
   * Loads the mod config and saves any changes to disk.
   *
   * @throws IllegalArgumentException if the loaded runeSize is not an integer.
   * @throws IllegalArgumentException if the loaded runeSize is less than 3.
   * @throws IllegalArgumentException if the loaded runeSize is not an odd number.
   */
  public static void syncConfig() throws IllegalArgumentException {
    try {
      // Load config
      config.load();

      // Read props from config
      Property runeSizeProp = config.get(
          Configuration.CATEGORY_GENERAL,
          "runeSize",
          5,
          "The size that valid runes must be. Must be an odd integer >= 3."
      );

      validateRuneSizeProp(runeSizeProp);

      runeSize = runeSizeProp.getInt();
      halfRuneSize = (int) Math.floor(runeSize / 2);
    } catch (Exception e) {
      Logger.warning("Failed loading config: " + e.getMessage());
    } finally {
      if (config.hasChanged()) {
        config.save();
      }
    }
  }

  /**
   * Validates a runeSize property.
   *
   * A valid runeSize is on odd integer greater than or equal to 3.
   *
   * @param  prop The property to validate
   * @throws IllegalArgumentException if the property value is not an integer.
   * @throws IllegalArgumentException if the property value is less than 3.
   * @throws IllegalArgumentException if the property value is not an odd number.
   */
  protected static void validateRuneSizeProp(Property prop) throws IllegalArgumentException {
    if (!prop.isIntValue()) {
      throw new IllegalArgumentException("runeSize must be an integer");
    }

    int runeSizeVal = prop.getInt();
    if (runeSizeVal < 3) {
      throw new IllegalArgumentException("runeSize must be greater than or equal to 3");
    }

    if (runeSizeVal % 2 == 0) {
      throw new IllegalArgumentException("runeSize must be an odd number");
    }
  }

  /**
   * Called when this Plugin is enabled. Initializes the plugin and loads all saved Runes.
   */
  @EventHandler
  public void onInit(FMLInitializationEvent event) {
    GameRegistry.registerBlock(runecore, "runecore");

    ItemStack ironStack = new ItemStack(Items.iron_ingot);
    ItemStack emeraldStack = new ItemStack(Items.emerald);
    ItemStack obsidianStack = new ItemStack(Blocks.obsidian);
    GameRegistry.addRecipe(
      new ItemStack(runecore),
      "xxx",
      "yzy",
      "xxx",
      'x',
      ironStack,
      'y',
      emeraldStack,
      'z',
      obsidianStack
    );

    // setup rendering
    if (event.getSide() == Side.CLIENT) {
      RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();

      // blocks
      renderItem.getItemModelMesher().register(
          Item.getItemFromBlock(runecore),
          0,
          new ModelResourceLocation(mod_id + ":" + runecore.getName(), "inventory")
      );
    }
  }

  /**
   * Event handler for the Server Started Event. Sets up the various event
   * listeners for the mod.
   *
   * @param event The server started event.
   */
  @EventHandler
  public void onServerStarted(FMLServerStartedEvent event) {
    MinecraftForge.EVENT_BUS.register(this);
    FMLCommonHandler.instance().bus().register(this);

    saveFile = "saves" + "/" + MinecraftServer.getServer().getFolderName() + '/'
        + SAVE_FILE_NAME;
  }

  /**
   * Called when this mod is disabled. Shuts down the mod, and saves all Runes.
   */
  @EventHandler
  public void unload(FMLServerStoppingEvent event) {
    saveRunes();
  }

  /**
   * Loads the runes that have been saved to disk. This should be called once
   * when the World is first loaded (Not the Dimension). Calling this a second
   * time would result in duplicate runes being registered.
   */
  public void loadRunes() {
    runeManager.clearRunes();

    ArrayList<SerializableLocation> locs;
    ObjectInputStream stream = null;
    try {
      stream = new ObjectInputStream(new FileInputStream(saveFile));
      locs = (ArrayList<SerializableLocation>) stream.readObject();
    } catch (FileNotFoundException ex) {
      locs = new ArrayList<SerializableLocation>();
    } catch (ClassNotFoundException ex) {
      Logger.severe(ex.getMessage());
      return;
    } catch (IOException ex) {
      Logger.severe(ex.getMessage());
      return;
    } finally {
      try {
        if (stream != null) {
          stream.close();
        }
      } catch (IOException ex) {
        Logger.severe(ex.getMessage());
      }
    }

    MinecraftServer server = MinecraftServer.getServer();
    for (SerializableLocation savedLoc : locs) {
      Location loc = savedLoc.getLocation(server);
      BlinkRune rune = new BlinkRune(loc);
      runeManager.addRune(rune);
    }

    runeFileLoaded = true;
  }

  /**
   * Saves the currently registered runes to a save file in the world folder.
   */
  public void saveRunes() {
    ArrayList<SerializableLocation> locs = runeManager.getLocationsForSave();
    ObjectOutputStream os = null;
    try {
      os = new ObjectOutputStream(new FileOutputStream(saveFile));
      os.writeObject(locs);
      os.flush();
      os.close();
    } catch (IOException ex) {
      Logger.severe(ex.getMessage());
    } finally {
      try {
        if (os != null) {
          os.close();
        }
      } catch (IOException ex) {
        Logger.severe(ex.getMessage());
      }
    }
  }

  /**
   * Handles Dimension load events (Forge calls Dimensions Worlds). Checks to
   * ensure that the rune save file is loaded.
   *
   * The file will only be loaded if the world is not remote. If the world is
   * remote, then the server is responsible for saving and loading the runes.
   *
   * @param event The world load event.
   */
  @SubscribeEvent
  public void onWorldLoad(WorldEvent.Load event) {
    if (runeFileLoaded) {
      return;
    }

    if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
      return;
    }

    loadRunes();
  }

  /**
   * Handles BlockBreakEvents. If the broken Block is part of a BlinkRune, this
   * method will invoke the BlinkRunes onDestroy event, and remove the BlinkRune
   * from the RuneManager.
   *
   * FIXME There's a bug where if the player is OP, and they attack a block that's part of a rune, the onBlockBreak event doesn't fire.
   *
   * @param event the event that describes the block break.
   */
  @SubscribeEvent
  public void onBlockBreak(BlockEvent.BreakEvent event) {
  	// The server will handle the event if the world is remote
  	if (event.world.isRemote) {
  	  return;
  	}

    if (event.isCanceled()) {
      return;
    }

    Location loc = new Location(event);
    BlinkRune rune = runeManager.getRuneByPart(loc);
    if (rune == null) {
      return;
    }

    rune.onDestroy();
  }

  /**
   * Handles BlockDamageEvents. If the damaged Block is part of a BlinkRune,
   * this method will call the BlinkRune's onDamage method.
   *
   * @param event the event that describes the block damage.
   */
  public void onBlockDamage(PlayerInteractEvent event) {
    if (event.isCanceled()) {
      return;
    }

    Location loc = new Location(event);
    BlinkRune rune = runeManager.getRuneByPart(loc);
    if (rune == null) {
      return;
    }

    rune.onDamage();
  }

  /**
   * Handles PlayerInteractEvents. If the player right clicks on a block
   * (excluding placing a block), then this method will determine if a new
   * BlinkRune is being created, or an existing BlinkRune is being activated.
   *
   * @param event the PlayerInteractEvent
   */
  @SubscribeEvent
  public void onPlayerInteract(PlayerInteractEvent event) {
  	// The server will handle the event if the world is remote
  	if (event.world.isRemote) {
  	  return;
  	}

    if (event.action == PlayerInteractEvent.Action.LEFT_CLICK_BLOCK) {
      onBlockDamage(event);
    }
  }
}
