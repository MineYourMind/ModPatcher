package me.nallar.modpatcher;

import cpw.mods.fml.relauncher.FMLRelaunchLog;

import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings("UnusedDeclaration")
public class PatcherLog {
    /*
     * This class might get classloaded twice under different classloaders. Don't do anything important in a static {} block.
     */

    static { Logger.getLogger("JavaPatcher").setParent(FMLRelaunchLog.log.getLogger()); }

    public static final Logger LOGGER = Logger.getLogger("ModPatcher");

    static { LOGGER.setParent(FMLRelaunchLog.log.getLogger()); }

    public static void error(String msg) {
        LOGGER.severe(msg);
    }

    public static void warn(String msg) {
        LOGGER.warning(msg);
    }

    public static void info(String msg) {
        LOGGER.info(msg);
    }

    public static void trace(String msg) {
        LOGGER.finest(msg);
    }

    public static void error(String msg, Throwable t) {
        LOGGER.log(Level.SEVERE, msg, t);
    }

    public static void warn(String msg, Throwable t) {
        LOGGER.log(Level.WARNING, msg, t);
    }

    public static void info(String msg, Throwable t) {
        LOGGER.log(Level.INFO, msg, t);
    }

    public static void trace(String msg, Throwable t) {
        LOGGER.log(Level.FINEST, msg, t);
    }

    public static String classString(Object o) {
        return "c " + o.getClass().getName() + ' ';
    }

    public static void log(Level level, Throwable throwable, String s) {
        LOGGER.log(level, s, throwable);
    }
}