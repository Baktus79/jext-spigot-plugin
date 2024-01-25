package me.tajam.jext.config.field;

import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;

public interface ConfigFieldList<T> {

	String getPath();

	ArrayList<T> getData();

	void updateData(ConfigurationSection section);

}