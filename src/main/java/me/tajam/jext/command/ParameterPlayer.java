package me.tajam.jext.command;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ParameterPlayer extends Parameter {

	ParameterPlayer(boolean required) {
		super(required);
	}

	@Override
	String getName() {
		return "player";
	}

	@Override
	List<String> onComplete(String parameter, CommandSender sender) {
		final List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
		final Set<String> selectors = PlayerSelector.getSelectorStrings();
		final List<String> matches = new ArrayList<>();
		for (String selector : selectors) {
			if (matches.size() >= 12) break;
			if (selector.startsWith(parameter.toLowerCase())) {
				matches.add(selector);
			}
		}
		for (Player player : players) {
			if (matches.size() >= 12) break;
			final String name = player.getName();
			if (name.toLowerCase().startsWith(parameter.toLowerCase())) {
				matches.add(name);
			}
		}
		if (!matches.isEmpty()) return matches;
		return null;
	}

}
