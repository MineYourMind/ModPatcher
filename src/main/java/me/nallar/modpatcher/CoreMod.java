package me.nallar.modpatcher;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

import java.io.IOException;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

@IFMLLoadingPlugin.Name("ModPatcher")
@IFMLLoadingPlugin.SortingIndex(1001) // Magic value, after deobf transformer.
public class CoreMod implements IFMLLoadingPlugin {

	private static void logToFile() {

		//Logger logger = PatcherLog.LOGGER;
		Logger logger = Logger.getLogger("JavaPatcher");
		FileHandler fh;

		try {

			fh = new FileHandler("./ModPatcher.log");
			logger.addHandler(fh);
			//SimpleFormatter formatter = new SimpleFormatter();
			LogFormatter formatter = new LogFormatter();
			fh.setFormatter(formatter);

		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/*private static void logToFile() {
		FileAppender fa = FileAppender.createAppender("./logs/ModPatcher.log", "false", "false", "PatcherAppender", "true", "true", "true", null, null, null, null, null, null);
		fa.start();
		((org.apache.logging.log4j.core.Logger) LogManager.getLogger("JavaPatcher")).addAppender(fa);
	}*/

	static {
		logToFile();
	}

	@Override
	public String[] getASMTransformerClass() {
		return new String[0];
	}

	@Override
	public String getModContainerClass() {
		return null;
	}

	@Override
	public String getSetupClass() {
		return ModPatcher.getSetupClass();
	}

	@Override
	public void injectData(Map<String, Object> data) {
		ModPatcher.modPatcherAsCoreModStartup();
	}

	//@Override
	public String getAccessTransformerClass() {
		return null;
	}

}
