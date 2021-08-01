package com.kaassnuvier.dogebolt.utils;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerialization;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

public class LocationSerializerAdapter implements JsonSerializer<Location>, JsonDeserializer<Location> {

    Type objectStringMapType = new TypeToken<Map<String, Object>>() {}.getType();

    @Override
    public JsonElement serialize(Location location, Type type, JsonSerializationContext jsonSerializationContext) {
        Map<String, Object> objectMap = new LinkedHashMap<>();
        objectMap.put(ConfigurationSerialization.SERIALIZED_TYPE_KEY, ConfigurationSerialization.getAlias(Location.class));
        objectMap.putAll(location.serialize());
        return jsonSerializationContext.serialize(objectMap, objectStringMapType);
    }

    @Override
    public Location deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        Map<String, Object> objectMap = new LinkedHashMap<>();
        for (Map.Entry<String, JsonElement> entrySet : jsonElement.getAsJsonObject().entrySet()) {
            if (entrySet.getValue().isJsonObject() && entrySet.getValue().getAsJsonObject().has(ConfigurationSerialization.SERIALIZED_TYPE_KEY)) {
                objectMap.put(entrySet.getKey(), this.deserialize(entrySet.getValue(), entrySet.getValue().getClass(), jsonDeserializationContext));
            } else {
                objectMap.put(entrySet.getKey(), jsonDeserializationContext.deserialize(entrySet.getValue(), Object.class));
            }
        }
        return (Location) ConfigurationSerialization.deserializeObject(objectMap, Location.class);
    }
}
