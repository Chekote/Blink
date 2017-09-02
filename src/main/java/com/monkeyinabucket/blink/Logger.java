package com.monkeyinabucket.blink;

import cpw.mods.fml.relauncher.FMLRelaunchLog;

@SuppressWarnings("unused")
public class Logger {

  /** A prefix that will be appended to all log messages from this plugin */
  private static String prefix = "[Blink] ";

  /**
   * Logs an INFO level message to the logger.
   * 
   * @param message the thing to log
   */
  public static void info(Object message) {
    FMLRelaunchLog.info(prefix + message);
  }

  /**
   * Logs a WARNING level message to the logger.
   * 
   * @param message the thing to log
   */
  public static void warning(Object message) {
    FMLRelaunchLog.warning(prefix + message);
  }

  /**
   * Logs a SEVERE level message to the logger.
   * 
   * @param message the thing to log
   */
  public static void severe(Object message) {
    FMLRelaunchLog.severe(prefix + message);
  }
}
