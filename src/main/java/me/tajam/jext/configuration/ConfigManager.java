package me.tajam.jext.configuration;

import me.tajam.jext.Log;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.InvalidClassException;

public class ConfigManager {

	private static ConfigManager instance = null;

	public static ConfigManager getInstance() {
		if (instance == null) {
			instance = new ConfigManager();
		}
		return instance;
	}

	private JavaPlugin plugin;
	private ConfigFile configFile;

	private ConfigManager() {
	}

	public ConfigManager setPlugin(JavaPlugin plugin) {
		this.plugin = plugin;
		return this;
	}

	public void load() {
		try {
			configFile = new ConfigFile(ConfigYmlvLatest.class, this.plugin);
			if (configFile.fileExists()) {
				configFile.load();
				return;
			}
			new Log().info().t("Initializing new configuration file...");
			configFile.save(configFile.getWriter());
		} catch (InvalidClassException e) {
			Bukkit.getLogger().severe(e.getMessage());
		}
	}

	public Configurable getConfigFile() throws IllegalStateException {
		if (this.configFile == null) throw new IllegalStateException("Load method never been called!");
		return this.configFile;
	}

}
