package com.monkeyinabucket.forge.blink;

import com.monkeyinabucket.forge.blink.block.RuneCore;
import com.monkeyinabucket.forge.blink.command.BlinkList;
import com.monkeyinabucket.forge.blink.command.BlinkLoad;
import com.monkeyinabucket.forge.blink.command.BlinkSave;
import com.monkeyinabucket.forge.blink.rune.BlinkRune;
import com.monkeyinabucket.forge.blink.rune.RuneManager;
import com.monkeyinabucket.forge.world.Location;
import com.monkeyinabucket.forge.world.SerializableLocation;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.monkeyinabucket.forge.blink.command.BlinkCreate;
import com.monkeyinabucket.forge.blink.rune.BlinkSignature;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.Logger;

/**
 * Main class for the Blink plugin. Handles the enabling and disabling process,
 * provides centralized logging features, and acts as the primary container for
 * all objects in the plugin.
 *
 * TODO: Handle events that may change or break a rune: ice melt, piston push, block placed in air signature slot, grass grow, etc.
 */
@Mod.EventBusSubscriber
@Mod(modid = "blink", name = "Blink", version = "1.0.0")
public class Blink {

  /** The id of this mod */
  public static String mod_id = "blink";

  /** Flag to track whether the runes save file has already been loaded */
  private Boolean runeFileLoaded = false;

  /** The name of the legacy rune save file. This will be stored in the world folder */
  private static String LEGACY_SAVE_FILE_NAME = "blink.sav";

  /** The name of the json rune save file. This will be stored in the world folder */
  private static String JSON_SAVE_FILE_NAME = "blink_runes.json";

  /** Configuration options for the mod */
  public static Configuration config;

  /** The size that runes have to be */
  public static int runeSize;

  /** Half size of the rune (excluding center). Stored for convenience */
  public static int halfRuneSize;

  /** Burning Effect on destruction of the rune **/
  public static boolean burning;

  /** The legacy save file. This will be used to load runes if it is present. */
  protected String legacySaveFile;

  /** The data file that will be used to save and load rune locations */
  protected String jsonSaveFile;

  /** The primary object used to manage runes in the plugin */
  protected final RuneManager runeManager = RuneManager.getInstance();

  public static RuneCore runecore;

  public static ItemBlock runecoreItem;

  public static Logger log;

  @Instance(value = "blink")
  public static Blink instance;

  @EventHandler
  public void onServerLoadStart(FMLServerStartingEvent event) {
    event.registerServerCommand(new BlinkCreate());
    event.registerServerCommand(new BlinkList());
    event.registerServerCommand(new BlinkLoad());
    event.registerServerCommand(new BlinkSave());
  }

  @EventHandler
  public void preInit(FMLPreInitializationEvent event) {
    log = event.getModLog();

    config = new Configuration(event.getSuggestedConfigurationFile());

    syncConfig();
  }

  /**
   * Registers this mods Blocks.
   *
   * @param event the registry event.
   */
  @SubscribeEvent
  public static void registerBlocks(RegistryEvent.Register<Block> event) {
    runecore = new RuneCore(Material.IRON);
    runecore.setHarvestLevel("pickaxe", 3);

    event.getRegistry().register(runecore);
  }

  /**
   * Registers this mods Items.
   *
   * @param event the registry event.
   */
  @SubscribeEvent
  public static void registerItems(RegistryEvent.Register<Item> event) {
    runecoreItem = new ItemBlock(runecore);
    runecoreItem.setRegistryName(runecore.getRegistryName());

    event.getRegistry().register(runecoreItem);
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
      Property burningProp = config.get(
          Configuration.CATEGORY_GENERAL,
          "burning",
          true,
          "Should the rune burn up on destruction?."
      );

      validateBurningProp(burningProp);

      burning = burningProp.getBoolean();
      loadRuneSizeConfig(config);
    } catch (Exception e) {
      log.warn("Failed loading config: " + e.getMessage());
    } finally {
      if (config.hasChanged()) {
        config.save();
      }
    }
  }

  /**
   * Loads the config for the burning option.
   *
   * @param  config the config to read from.
   * @throws IllegalArgumentException if the property is not boolean.
   */
  private static void loadBurningConfig(Configuration config) throws IllegalArgumentException {
    Property burningProp = config.get(
        Configuration.CATEGORY_GENERAL,
        "burning",
        true,
        "Should the rune burn up on destruction?."
    );

    validateBurningProp(burningProp);

    burning = burningProp.getBoolean();
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
   * Validates a burning property
   *
   * Valid options are true or false.
   *
   * @param prop
   * @throws IllegalArgumentException if the property is not boolean.
   */
  protected static void validateBurningProp(Property prop) throws IllegalArgumentException{
    if (!prop.isBooleanValue()) {
      throw new IllegalArgumentException("burning must be true or false");
    }
  }

  /**
   * Called when this Plugin is enabled. Initializes the plugin and loads all saved Runes.
   */
  @EventHandler
  public void onInit(FMLInitializationEvent event) {
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

    MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();

    String path = Files.isDirectory(Paths.get("saves"))
        ? "saves/" + server.getFolderName() + '/'
        : server.getFolderName() + '/';

    legacySaveFile = path + LEGACY_SAVE_FILE_NAME;

    jsonSaveFile = path + JSON_SAVE_FILE_NAME;

    if (!runeFileLoaded) {
      loadRunes();
    }
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

    if ((new File(legacySaveFile)).isFile()) {
      loadRunesFromLegacy();
    } else if ((new File(jsonSaveFile)).isFile()) {
      loadRunesFromJson();
    }

    runeFileLoaded = true;
  }

  protected void loadRunesFromLegacy() {
    ArrayList<SerializableLocation> locs;
    ObjectInputStream stream = null;
    try {
      stream = new ObjectInputStream(new FileInputStream(legacySaveFile));
      locs = (ArrayList<SerializableLocation>) stream.readObject();
    } catch (FileNotFoundException ex) {
      locs = new ArrayList<>();
    } catch (ClassNotFoundException | IOException ex) {
      log.error(ex.getMessage());
      return;
    } finally {
      try {
        if (stream != null) {
          stream.close();
        }
      } catch (IOException ex) {
        log.error(ex.getMessage());
      }
    }

    MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
    for (SerializableLocation savedLoc : locs) {
      Location loc = savedLoc.getLocation(server);
      BlinkRune rune = new BlinkRune(loc);
      runeManager.addRune(rune);
    }
  }

  protected void loadRunesFromJson() throws JsonParseException {
    MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
    try {
      JsonParser parser = new JsonParser();

      JsonElement element = parser.parse(new JsonReader(new FileReader(jsonSaveFile)));

      for (JsonElement groupVal : element.getAsJsonArray()) {
        JsonObject groupObj = (JsonObject) groupVal;

        JsonObject jsonSig = groupObj.getAsJsonObject("sig");

        System.out.println(groupObj.toString());

        BlinkSignature sig = new BlinkSignature(
            Block.REGISTRY.getObject(new ResourceLocation(jsonSig.getAsJsonPrimitive("n").getAsString())),
            Block.REGISTRY.getObject(new ResourceLocation(jsonSig.getAsJsonPrimitive("e").getAsString())),
            Block.REGISTRY.getObject(new ResourceLocation(jsonSig.getAsJsonPrimitive("s").getAsString())),
            Block.REGISTRY.getObject(new ResourceLocation(jsonSig.getAsJsonPrimitive("w").getAsString()))
        );

        for (JsonElement runeVal : groupObj.getAsJsonArray("runes")) {
          JsonObject runeObj = (JsonObject) runeVal;

          Location loc = new Location(
              server.getWorld(runeObj.getAsJsonPrimitive("d").getAsInt()),
              new BlockPos(
                  runeObj.getAsJsonPrimitive("x").getAsInt(),
                  runeObj.getAsJsonPrimitive("y").getAsInt(),
                  runeObj.getAsJsonPrimitive("z").getAsInt()
              )
          );

          BlinkRune rune = new BlinkRune(loc, sig);
          runeManager.addRune(rune);
        }
      }
    } catch (IOException e) {
      // nop
    }
  }

  /**
   * Saves the currently registered runes to a save file in the world folder.
   */
  public void saveRunes() {
    FileOutputStream os = null;
    String json = runeManager.toJsonBuilder().toString();
    try {
      os = new FileOutputStream(jsonSaveFile);
      os.write(json.getBytes(), 0, json.length());
      os.flush();
      os.close();
    } catch (IOException ex) {
      log.error(ex.getMessage());
    } finally {
      try {
        if (os != null) {
          os.close();
        }
      } catch (IOException ex) {
        log.error(ex.getMessage());
      }
    }

    // delete legacy file
    File file = new File(legacySaveFile);
    if (file.isFile()) {
      if (!file.delete()) {
        log.warn("Failed to delete legacy save file");
      }
    }
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
  	if (event.getWorld().isRemote) {
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
  	if (event.getWorld().isRemote) {
  	  return;
  	}

    if (event instanceof PlayerInteractEvent.LeftClickBlock) {
      onBlockDamage(event);
    }
  }
}
