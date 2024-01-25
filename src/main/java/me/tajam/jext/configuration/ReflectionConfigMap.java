package me.tajam.jext.configuration;

import lombok.Getter;
import me.tajam.jext.Log;
import me.tajam.jext.configuration.ConfigUtil.MarkAsConfigField;
import me.tajam.jext.configuration.ConfigUtil.MarkAsConfigObject;
import org.bukkit.Bukkit;

import java.lang.reflect.*;
import java.util.*;
import java.util.Map.Entry;

public class ReflectionConfigMap {

	private final Field mapField;
	private final Object instance;
	@Getter
	private Class<?> keyClass;
	@Getter
	private Class<?> valueClass;

	public ReflectionConfigMap(Field mapField, Object instance) {
		this.mapField = mapField;
		this.instance = instance;
		final ParameterizedType type = (ParameterizedType) mapField.getGenericType();
		final Type keyType = type.getActualTypeArguments()[0];
		final Type valueType = type.getActualTypeArguments()[1];
		try {
			this.keyClass = Class.forName(keyType.getTypeName());
			this.valueClass = Class.forName(valueType.getTypeName());
		} catch (final ClassNotFoundException e) {
			Bukkit.getLogger().severe(e.getMessage());
		}
	}

	public List<Field> getValueFields() {
		final List<Field> fields = new ArrayList<>();
		for (final Field field : this.valueClass.getDeclaredFields()) {
			if (field.isAnnotationPresent(MarkAsConfigField.class)) {
				fields.add(field);
			}
		}
		return fields;
	}

	@SuppressWarnings("deprecation")
	public Object instantiateValueObject() {
		try {
			return this.valueClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			Bukkit.getLogger().severe(e.getMessage());
			return null;
		}
	}

	public boolean isValid() {
		return this.keyClass.isAssignableFrom(String.class)
				&& this.valueClass.isAnnotationPresent(MarkAsConfigObject.class);
	}

	public void put(Object key, Object value) {
		if (!(this.keyClass.isAssignableFrom(key.getClass()) && this.valueClass.isAssignableFrom(value.getClass()))) {
			new Log().eror().t("Error when loading an object in map: ").t(mapField.getName()).t(", ignoring this object.")
					.send();
			return;
		}
		try {
			final Object map = getMapInstance();
			final Method method = map.getClass().getDeclaredMethod("put", Object.class, Object.class);
			method.invoke(map, key, value);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | InvocationTargetException e) {
			Bukkit.getLogger().severe(e.getMessage());
		}
	}

	public void clear() {
		try {
			final Object map = getMapInstance();
			final Method method = map.getClass().getDeclaredMethod("clear");
			method.invoke(map);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | InvocationTargetException e) {
			Bukkit.getLogger().severe(e.getMessage());
		}
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	public Set<Entry<String, Object>> entries() {
		try {
			final Object map = getMapInstance();
			final Map<String, Object> m = new HashMap<>((Map) map);
			return m.entrySet();
		} catch (final SecurityException e) {
			Bukkit.getLogger().severe(e.getMessage());
			return null;
		}
	}

	@SuppressWarnings("deprecation")
	private Object getMapInstance() {
		try {
			Object map = mapField.get(this.instance);
			if (map == null) {
				map = mapField.getType().newInstance();
				mapField.set(this.instance, map);
			}
			return map;
		} catch (IllegalArgumentException | IllegalAccessException | InstantiationException e) {
			Bukkit.getLogger().severe(e.getMessage());
			return null;
		}
	}

}
