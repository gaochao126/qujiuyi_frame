package com.jiuyi.frame.util;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Defaults {
	private Defaults() {
	}

	private static final Map<Class<?>, Object> DEFAULTS;

	static {
		Map<Class<?>, Object> map = new HashMap<Class<?>, Object>();
		put(map, boolean.class, false);
		put(map, char.class, '\0');
		put(map, byte.class, (byte) 0);
		put(map, short.class, (short) 0);
		put(map, int.class, 0);
		put(map, long.class, 0L);
		put(map, float.class, 0f);
		put(map, double.class, 0d);

		put(map, Boolean.class, false);
		put(map, Character.class, '\0');
		put(map, Byte.class, (byte) 0);
		put(map, Short.class, (short) 0);
		put(map, Integer.class, 0);
		put(map, Long.class, 0L);
		put(map, Float.class, 0f);
		put(map, Double.class, 0d);

		put(map, String.class, "");
		put(map, BigDecimal.class, new BigDecimal(0));

		DEFAULTS = Collections.unmodifiableMap(map);
	}

	private static <T> void put(Map<Class<?>, Object> map, Class<T> type, T value) {
		map.put(type, value);
	}

	/**
	 * Returns the default value of {@code type} as defined by JLS --- {@code 0}
	 * for numbers, {@code
	 * false} for {@code boolean} and {@code '\0'} for {@code char}. For
	 * non-primitive types and {@code void}, null is returned.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T defaultValue(Class<T> type) {
		return (T) DEFAULTS.get(type);
	}
}
