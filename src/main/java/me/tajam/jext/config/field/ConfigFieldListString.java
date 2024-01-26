package me.tajam.jext.config.field;

import me.tajam.jext.Log;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;

public class ConfigFieldListString implements ConfigFieldList<String> {

	private final String path;
	private ArrayList<String> data;

	public ConfigFieldListString(String path, ArrayList<String> defaultData) {
		this.path = path;
		this.data = defaultData;
	}

	@Override
	public String getPath() {
		return path;
	}

	@Override
	public ArrayList<String> getData() {
		return data;
	}

	@Override
	public void updateData(ConfigurationSection section) {
		if (!section.contains(path)) {
			new Log().warn().t("\"").o(path).t("\" missing in configuration file, will set to default value.").send();
			return;
		}
		this.data = new ArrayList<>(section.getStringList(path));
	}

}