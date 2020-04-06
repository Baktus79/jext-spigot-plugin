package me.tajam.jext.config;

import java.util.HashMap;
import java.util.Set;

import me.tajam.jext.Logger;
import me.tajam.jext.disc.DiscContainer;

import org.bukkit.configuration.ConfigurationSection;

public class ConfigDiscManager {

  private static final String PATH = "disc";
  private static ConfigDiscManager instance = null;
  public static ConfigDiscManager getInstance() {
    if (instance == null) {
      instance = new ConfigDiscManager();
    }
    return instance;
  }

  HashMap<String, ConfigDiscData> discMap;

  private ConfigDiscManager() {
    discMap = new HashMap<String, ConfigDiscData>();
  }

  public void load(ConfigurationSection section) {
    ConfigurationSection subsection = section.getConfigurationSection(PATH);
    final Set<String> keys = subsection.getKeys(false);
    for (String key : keys) {
      ConfigDiscData discData = new ConfigDiscData(key);
      discData.load(subsection);
      discMap.put(discData.getStringData(ConfigDiscData.Path.NAMESPACE), discData);
    }
    Logger.success("Loaded " + discMap.size() + " disc(s).");
  }

  public DiscContainer getDisc(String namespace) {
    ConfigDiscData discData = discMap.get(namespace);
    if (discData == null) return null;
    return new DiscContainer(discData);
  }

  public Set<String> getNamespaces() {
    return discMap.keySet();
  }

}