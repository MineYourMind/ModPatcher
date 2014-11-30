package me.nallar.modpatcher;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import me.nallar.javapatcher.PatcherLog;

import java.io.IOException;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

@IFMLLoadingPlugin.Name("ModPatcher")
@IFMLLoadingPlugin.SortingIndex(1001) // Magic value, after deobf transformer.
public class CoreMod implements IFMLLoadingPlugin {

	private static void logToFile() {

		Logger modPatcherLogger = PatcherLog.LOGGER;
		FileHandler fh;

		try {

			fh = new FileHandler("./ModPatcher.log");
			modPatcherLogger.addHandler(fh);
			LogFormatter formatter = new LogFormatter();
			fh.setFormatter(formatter);

		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

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
