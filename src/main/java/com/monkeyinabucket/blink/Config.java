package com.monkeyinabucket.blink;

/**
 *
 */
public class Config {

  /** The size that runes have to be */
  public int runeSize;

  /** Half size of the rune (excluding center). Stored for convenience */
  public int halfRuneSize;

  public void load() {

  }

  /**
   * Loads the mod config and saves any changes to disk.
   *
   * @throws IllegalArgumentException if the loaded runeSize is not an integer.
   * @throws IllegalArgumentException if the loaded runeSize is less than 3.
   * @throws IllegalArgumentException if the loaded runeSize is not an odd number.
   */
  public void sync() throws IllegalArgumentException {
    try {
      // Load config
      load();

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
  private void validateRuneSizeProp(Property prop) throws IllegalArgumentException {
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

}
