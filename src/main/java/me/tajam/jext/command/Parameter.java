package me.tajam.jext.command;

import me.tajam.jext.Log;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;

abstract class Parameter {

	private final boolean required;

	Parameter(boolean required) {
		this.required = required;
	}

	boolean isRequired() {
		return this.required;
	}

	abstract String getName();

	abstract List<String> onComplete(String parameter, CommandSender sender);

	@SuppressWarnings("deprecation")
	@Override
	public String toString() {
		Log sms = new Log().rst(((required) ? "[" : "<")).rst().rst(((required) ? "]" : ">"));
		return sms.text((required) ? ChatColor.LIGHT_PURPLE + getName() : ChatColor.DARK_PURPLE + getName());
	}

}