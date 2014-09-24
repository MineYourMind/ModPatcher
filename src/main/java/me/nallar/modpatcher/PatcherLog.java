package me.nallar.modpatcher;

import cpw.mods.fml.relauncher.FMLRelaunchLog;
import me.nallar.modpatcher.log.ColorLogFormatter;
import me.nallar.modpatcher.log.LogFormatter;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.logging.*;

@SuppressWarnings("UnusedDeclaration")
public class PatcherLog {
	/*
	 * This class might get classloaded twice under different classloaders. Don't do anything important in a static {} block.
	 */
	public static final Logger LOGGER = Logger.getLogger("ModPatcher");

    private static Handler handler;
    private static final int numberOfLogFiles = 20;
    private static final File logFolder = new File("ModPatcherLogs");

    public static void coloriseLogger() {
        try {
            Field consoleLogThreadField = FMLRelaunchLog.class.getDeclaredField("consoleLogThread");
            consoleLogThreadField.setAccessible(true);
            Thread consoleLogThreadThread = (Thread) consoleLogThreadField.get(null);
            Field target = Thread.class.getDeclaredField("target");
            target.setAccessible(true);
            Object consoleLogThread = target.get(consoleLogThreadThread);
            Field handlerField = consoleLogThread.getClass().getDeclaredField("wrappedHandler");
            handlerField.setAccessible(true);
            Handler wrappedHandler = (Handler) handlerField.get(consoleLogThreadThread);
            wrappedHandler.setFormatter(new ColorLogFormatter());
        } catch (Exception e) {
            System.err.println("Failed to replace FML logger with colorised logger");
            e.printStackTrace(System.err);
        }
    }

    static {
        if (!System.getProperty("PatchLogInited", "false").equals("false")) {
            System.err.println("Initialising extra PatchLog class under classloader " + PatcherLog.class.getClassLoader());
            System.err.println("Original was under " + System.getProperty("PatchLogInited"));
            new Throwable().printStackTrace();
            System.exit(1);
        }
        System.setProperty("PatchLogInited", PatcherLog.class.getClassLoader().toString());
        coloriseLogger();
        try {
            final Logger parent = Logger.getLogger("ForgeModLoader");
            if (parent == null) {
                throw new NoClassDefFoundError();
            }
            LOGGER.setParent(parent);
            LOGGER.setUseParentHandlers(true);
        } catch (NoClassDefFoundError ignored) {
            System.err.println("Failed to get parent logger.");
            LOGGER.setUseParentHandlers(false);
            LOGGER.addHandler(new Handler() {
                private final LogFormatter logFormatter = new LogFormatter();

                @Override
                public void publish(LogRecord record) {
                    System.out.print(logFormatter.format(record));
                }

                @Override
                public void flush() {
                }

                @Override
                public void close() throws SecurityException {
                }
            });
        }
        setFileName("patcher", LOGGER);
        LOGGER.setLevel(Level.ALL);
    }

    public static void setFileName(String name, Logger... loggers) {
        logFolder.mkdir();
        for (int i = numberOfLogFiles; i >= 1; i--) {
            File currentFile = new File(logFolder, name + '.' + i + ".log");
            if (currentFile.exists()) {
                if (i == numberOfLogFiles) {
                    currentFile.delete();
                } else {
                    currentFile.renameTo(new File(logFolder, name + '.' + (i + 1) + ".log"));
                }
            }
        }
        final File saveFile = new File(logFolder, name + ".1.log");
        saveFile.delete();
        try {
            handler = new FileHandler(saveFile.getCanonicalPath());
            handler.setFormatter(new LogFormatter());
            for (Logger logger : loggers) {
                logger.addHandler(handler);
            }
        } catch (IOException e) {
            PatcherLog.error("Can't write logs to disk", e);
        }
    }

    public static void flush() {
        if (handler != null) {
            handler.flush();
        }
    }

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
