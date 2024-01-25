package me.tajam.jext.config;

import lombok.Getter;
import me.tajam.jext.config.field.ConfigField;
import me.tajam.jext.config.field.ConfigFieldListString;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.HashMap;

public class ConfigDiscData {

	public enum Path {

		NAMESPACE,
		AUTHOR,
		MODEL_DATA,
		LORES,
		CREEPER_DROP

	}

	@Getter
	private final String name;
	private final HashMap<Path, ConfigField<String>> stringMap;
	private final HashMap<Path, ConfigField<Integer>> integerMap;
	private final HashMap<Path, ConfigField<Boolean>> booleanMap;
	private final ConfigFieldListString lores;

	public ConfigDiscData(String name) {

		this.name = name;

		stringMap = new HashMap<>();
		stringMap.put(Path.NAMESPACE, new ConfigField<>("namespace", "music_disc.cat"));
		stringMap.put(Path.AUTHOR, new ConfigField<>("author", "C148"));

		integerMap = new HashMap<>();
		integerMap.put(Path.MODEL_DATA, new ConfigField<>("model-data", 0));

		booleanMap = new HashMap<>();
		booleanMap.put(Path.CREEPER_DROP, new ConfigField<>("creeper-drop", true));

		lores = new ConfigFieldListString("lore", new ArrayList<>());

	}

	public void load(ConfigurationSection section) {
		ConfigurationSection subsection = section.getConfigurationSection(name);
		for (Path key : stringMap.keySet()) {
			ConfigField<String> field = stringMap.get(key);
			field.updateData(subsection, String.class);
		}
		for (Path key : integerMap.keySet()) {
			ConfigField<Integer> field = integerMap.get(key);
			field.updateData(subsection, Integer.class);
		}
		for (Path key : booleanMap.keySet()) {
			ConfigField<Boolean> field = booleanMap.get(key);
			field.updateData(subsection, Boolean.class);
		}
		lores.updateData(subsection);
	}

	public String getStringData(Path key) {
		return stringMap.get(key).getData();
	}

	public Integer getIntegerData(Path key) {
		return integerMap.get(key).getData();
	}

	public Boolean getBooleanData(Path key) {
		return booleanMap.get(key).getData();
	}

	public ArrayList<String> getLores() {
		return new ArrayList<>(this.lores.getData());
	}

}