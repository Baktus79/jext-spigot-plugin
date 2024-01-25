package me.tajam.jext;

import me.tajam.jext.command.CommandsRegistrant;
import me.tajam.jext.config.ConfigManager;
import me.tajam.jext.configuration.ConfigFile;
import me.tajam.jext.configuration.ConfigYmlvLegacy;
import me.tajam.jext.listener.ListenersRegistrant;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.InvalidClassException;

public class Jext extends JavaPlugin {

	private static final Log ENABLED_MESSAGE = new Log().okay().t("Enabled Jukebox Extender, Do Re Mi!");
	private static final Log DISABLED_MESSAGE = new Log().warn().t("Disabled Jukebox Extender, Mi Re Do!");

	@Override
	public void onEnable() {
		try {
			load();
		} catch (final Exception e) {
			this.getServer().getLogger().severe(e.getMessage());
			this.getServer().getPluginManager().disablePlugin(this);
		}
		ENABLED_MESSAGE.send();
	}

	@Override
	public void onDisable() {
		DISABLED_MESSAGE.send();
	}

	private void load() {

		// Register namespaces
		JextNamespace.registerNamespace(this);

		// Load configurations
		ConfigManager.getInstance().setPlugin(this).load();

		// Setup commands
		CommandsRegistrant.getInstance().registerCommands(this);

		// Register listeners
		ListenersRegistrant.getInstance().registerListeners(this);

		// test
		try {
			new ConfigFile(ConfigYmlvLegacy.class, this).load();
		} catch (final InvalidClassException e) {
			this.getServer().getLogger().severe(e.getMessage());
		}
	}

}