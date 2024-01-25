package me.tajam.jext.command;

import me.tajam.jext.Log;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

class ExecutorAdapter implements CommandExecutor, TabCompleter {

	private static final Log DEFAULT_MESSAGE = new Log().info().t("This command is only for ").t().t(".");
	private static final Log PERMISSION_MESSAGE = new Log().eror().t("You do not have permission to use this command.");

	private final String commandString;
	private final List<Parameter> parameters;

	ExecutorAdapter(String commandString) {
		this.parameters = new ArrayList<>();
		this.commandString = commandString;
	}

	void addParameter(Parameter parameter) throws IllegalArgumentException {
		if (!parameters.isEmpty()) {
			final Parameter lastParameter = parameters.get(parameters.size() - 1);
			if (!lastParameter.isRequired() && parameter.isRequired()) {
				throw new IllegalArgumentException("Required parameter cannot come after optional parameter!");
			}
		}
		parameters.add(parameter);
	}

	void registerTo(JavaPlugin plugin) {
		final PluginCommand command = plugin.getCommand(commandString);
		command.setExecutor(this);
		command.setTabCompleter(this);
		Log usageSMS = new Log("Usage").info().t("/").a(commandString);
		for (Parameter parameter : parameters) {
			usageSMS.rst(" ").rst(parameter.toString());
		}
		command.setUsage(usageSMS.text());
	}

	@Override
	public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
		try {
			final int index = args.length - 1;
			final Parameter parameter = parameters.get(index);
			return parameter.onComplete(args[index], sender);
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}

	@Override
	public boolean onCommand(@NotNull CommandSender sender, Command command, @NotNull String label, String[] args) {
		if (!command.testPermissionSilent(sender)) {
			PERMISSION_MESSAGE.send(sender);
			return true;
		}
		if (args.length > parameters.size()) return false;
		if (args.length < parameters.size()) {
			if (args.length == 0 || parameters.get(args.length - 1).isRequired()) {
				if (parameters.get(args.length).isRequired()) return false;
			}
		}
		return (sender instanceof Player) ? executePlayer((Player) sender, args) : executeCommand(sender, args);
	}

	boolean executePlayer(Player sender, String[] args) {
		DEFAULT_MESSAGE.send(sender, "console");
		return true;
	}

	boolean executeCommand(CommandSender sender, String[] args) {
		DEFAULT_MESSAGE.send(sender, "players");
		return true;
	}
}