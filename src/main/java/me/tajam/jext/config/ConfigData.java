package me.tajam.jext.config;

import me.tajam.jext.config.field.ConfigField;

import java.util.HashMap;

public final class ConfigData {

	public static final String PATH = "jext";

	public static class BooleanData {

		public enum Path {

			FORCE_PACK,
			IGNORE_FAIL,
			ALLOW_OVERLAP

		}

		public static HashMap<Path, ConfigField<Boolean>> DataMap;

		static {
			HashMap<Path, ConfigField<Boolean>> map = new HashMap<>();
			map.put(Path.FORCE_PACK, new ConfigField<>("force-resource-pack", true));
			map.put(Path.IGNORE_FAIL, new ConfigField<>("ignore-failed-download", false));
			map.put(Path.ALLOW_OVERLAP, new ConfigField<>("allow-music-overlapping", false));
			DataMap = map;
		}

	}

	public static class StringData {

		public enum Path {

			KICK_DECLINE_MESSAGE,
			KICK_FAIL_MESSAGE

		}

		public static HashMap<Path, ConfigField<String>> DataMap;

		static {
			HashMap<Path, ConfigField<String>> map = new HashMap<>();
			map.put(Path.KICK_DECLINE_MESSAGE, new ConfigField<>("resource-pack-decline-kick-message", "Unset kick message"));
			map.put(Path.KICK_FAIL_MESSAGE, new ConfigField<>("failed-download-kick-message", "Unset kick message"));
			DataMap = map;
		}

	}

}