package me.tajam.jext.config;

import me.tajam.jext.Log;
import me.tajam.jext.disc.DiscContainer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Set;

public class ConfigDiscManager {

	private static final String PATH = "disc";
	private static ConfigDiscManager instance = null;
	private final HashMap<String, ConfigDiscData> discMap;

	public static ConfigDiscManager getInstance() {
		if (instance == null) {
			instance = new ConfigDiscManager();
		}
		return instance;
	}

	private ConfigDiscManager() {
		discMap = new HashMap<>();
	}

	public void load(FileConfiguration file) {
		final ConfigurationSection section = file.getConfigurationSection(PATH);
		final Set<String> keys = section.getKeys(false);

		for (String key : keys) {
			ConfigDiscData discData = new ConfigDiscData(section.getConfigurationSection(key));
			discData.load(section.getConfigurationSection(key));
			discMap.put(discData.getStringData(ConfigDiscData.Path.NAMESPACE), discData);
		}

		new Log().okay().t("Loaded ").b().t(" disc(s).").send(discMap.size());
	}

	public DiscContainer getDisc(String namespace) {
		ConfigDiscData discData = discMap.get(namespace);
		if (discData == null) return null;
		return new DiscContainer(discData);
	}

	public boolean haveDisc(String namespace) {
		return discMap.containsKey(namespace);
	}

	public Set<String> getNamespaces() {
		return discMap.keySet();
	}

}