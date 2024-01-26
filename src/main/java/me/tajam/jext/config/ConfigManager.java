package me.tajam.jext.config;

import me.tajam.jext.config.field.ConfigField;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

public class ConfigManager {

	private static ConfigManager instance = null;

	public static ConfigManager getInstance() {
		if (instance == null) {
			instance = new ConfigManager();
		}
		return instance;
	}

	private Plugin plugin;

	private ConfigManager() {
	}

	public ConfigManager setPlugin(Plugin plugin) {
		this.plugin = plugin;
		return this;
	}

	public void load() {
		plugin.saveDefaultConfig();
		final FileConfiguration file = plugin.getConfig();

		for (ConfigData.BooleanData.Path key : ConfigData.BooleanData.DataMap.keySet()) {
			ConfigField<Boolean> field = ConfigData.BooleanData.DataMap.get(key);
			field.updateData(file, Boolean.class);
		}

		for (ConfigData.StringData.Path key : ConfigData.StringData.DataMap.keySet()) {
			ConfigField<String> field = ConfigData.StringData.DataMap.get(key);
			field.updateData(file, String.class);
		}

		ConfigDiscManager.getInstance().load(file);
	}

	public String getStringData(ConfigData.StringData.Path path) {
		return ConfigData.StringData.DataMap.get(path).getData();
	}

	public Boolean getBooleanData(ConfigData.BooleanData.Path path) {
		return ConfigData.BooleanData.DataMap.get(path).getData();
	}
}