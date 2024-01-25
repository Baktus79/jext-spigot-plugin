package me.tajam.jext.command;

import me.tajam.jext.Log;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

class PlayerSelector {

	private enum Selector {
		ALL,
		WORLD,
		NEARBY,
		WORLD_RANDOM,
		RANDOM,
		SELF,
		PLAYER
	}

	private static final HashMap<String, Selector> SELECTOR_SYMBOL_MAP;

	static {
		SELECTOR_SYMBOL_MAP = new HashMap<>();
		SELECTOR_SYMBOL_MAP.put("@a", Selector.ALL);
		SELECTOR_SYMBOL_MAP.put("@w", Selector.WORLD);
		SELECTOR_SYMBOL_MAP.put("@wr", Selector.WORLD_RANDOM);
		SELECTOR_SYMBOL_MAP.put("@r", Selector.RANDOM);
		SELECTOR_SYMBOL_MAP.put("@s", Selector.SELF);
	}

	static Set<String> getSelectorStrings() {
		return SELECTOR_SYMBOL_MAP.keySet();
	}

	private final CommandSender sender;
	private Selector mode;
	private final String selector;

	PlayerSelector(CommandSender sender, String selector) {
		this.sender = sender;
		this.selector = selector;
		this.mode = SELECTOR_SYMBOL_MAP.get(selector);
		if (this.mode == null) {
			this.mode = Selector.PLAYER;
		}
	}

	List<Player> getPlayers() {
		try {
			return switch (mode) {
				case ALL -> getAllPlayers();
				case WORLD -> getWorldPlayers();
				case WORLD_RANDOM -> getRandomPlayers(getWorldPlayers());
				case RANDOM -> getRandomPlayers(getAllPlayers());
				case SELF -> getSelf();
				case PLAYER -> getSpecificPlayer();
				default -> null;
			};
		} catch (IllegalStateException e) {
			new Log().eror().t("Invalid selector for console!").send(sender);
			return null;
		}

	}

	private List<Player> getAllPlayers() {
		return new ArrayList<>(Bukkit.getOnlinePlayers());
	}

	private List<Player> getWorldPlayers() throws IllegalStateException {
		if (sender instanceof Player player) {
			return player.getWorld().getPlayers();
		}
		throw new IllegalStateException();
	}

	private List<Player> getSpecificPlayer() {
		final List<Player> players = new ArrayList<>();
		final Player player = Bukkit.getPlayer(selector);
		if (player == null) {
			new Log().eror().t("Cannot find player: ").o(selector).send(sender);
			return null;
		}
		players.add(player);
		return players;
	}

	private List<Player> getRandomPlayers(List<Player> players) {
		Collections.shuffle(players);
		return players.subList(0, 1);
	}

	private List<Player> getSelf() throws IllegalStateException {
		if (sender instanceof Player) {
			final List<Player> players = new ArrayList<>();
			players.add((Player) sender);
			return players;
		}
		throw new IllegalStateException();
	}

}