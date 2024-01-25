package me.tajam.jext.configuration;

import me.tajam.jext.Log;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import java.lang.reflect.Field;

public class ConfigField extends Configuration {

	protected Field field;
	protected ConfigurationSection section;
	protected Object instance;

	public ConfigField(Field field, ConfigurationSection section, Object instance) {
		this.field = field;
		this.section = section;
		this.instance = instance;
	}

	public ConfigField(Field field, ConfigurationSection section) {
		this(field, section, null);
	}

	@Override
	public void load() {
		final String name = ConfigUtil.javaNametoYml(this.field.getName());
		final Object object = this.section.get(name);
		if (object == null) {
			new Log().warn().t("Configuration field ").t(name).t(" missing, using default values.").send();
			return;
		}
		try {
			this.field.set(this.instance, object);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			Bukkit.getLogger().severe(e.getMessage());
		}
	}

	@Override
	public void save(ConfigWriter writer) {
		writer.writeComment(this.field, getLevel());
		writer.writeField(this.field, getLevel(), this.instance);
	}

}
