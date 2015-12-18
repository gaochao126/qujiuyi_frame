package com.jiuyi.frame.util;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

/**
 * @Author: xutaoyang @Date: 下午4:30:13
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
public class JsonUtil {

	private static Gson gson;
	private static Type type_map = new TypeToken<HashMap<String, Object>>() {
	}.getType();
	private static Type type_map_element = new TypeToken<HashMap<String, JsonElement>>() {
	}.getType();

	static {
		JsonSerializer<Date> ser = new JsonSerializer<Date>() {
			@Override
			public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
				return src == null ? null : new JsonPrimitive(src.getTime());
			}
		};

		JsonDeserializer<Date> deser = new JsonDeserializer<Date>() {
			@Override
			public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
				return json == null ? null : new Date(json.getAsLong());
			}
		};

		gson = new GsonBuilder().registerTypeAdapter(Date.class, deser).registerTypeAdapter(Date.class, ser).create();
	}

	public static HashMap<String, Object> parse(String json) {
		return gson.fromJson(json, type_map);
	}

	public static HashMap<String, JsonElement> parseToElement(String json) {
		return gson.fromJson(json, type_map_element);
	}

	public static String toJson(Object obj) {
		return gson.toJson(obj);
	}

	public static <T> T fromJson(String json, Class<T> clazz) {
		return gson.fromJson(json, clazz);
	}

	public static <T> T fromJson(JsonElement jsonElement, Class<T> clazz) {
		return gson.fromJson(jsonElement, clazz);
	}

	public static <T> T fromJson(JsonElement jsonElement, Type type) {
		return gson.fromJson(jsonElement, type);
	}

}
