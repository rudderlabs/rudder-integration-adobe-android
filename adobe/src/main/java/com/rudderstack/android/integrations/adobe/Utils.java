package com.rudderstack.android.integrations.adobe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

public class Utils {
    // returns the eventMap of the given priority from the contextVariables supplied by the destination config
    @NonNull
    static Map<String, Object> getContextMap(@NonNull JsonArray contextVariables) {
        Map<String, Object> eventMap = new HashMap<>();
        for (int i = 0; i < contextVariables.size(); i++) {
            JsonObject eventObject = (JsonObject) contextVariables.get(i);
            String eventName = eventObject.get("from").getAsString();
            Object value = eventObject.get("to");
            eventMap.put(eventName,value);
        }
        return eventMap;
    }

    public static Map<String, Object> getEventsMap(JsonArray rudderEventsToAdobeEventsMaps) {
        Map<String, Object> eventMap = new HashMap<>();
        for (int i = 0; i < rudderEventsToAdobeEventsMaps.size(); i++) {
            JsonObject eventObject = (JsonObject) rudderEventsToAdobeEventsMaps.get(i);
            String eventName = eventObject.get("from").getAsString();
            String value = eventObject.get("to").getAsString();
            eventMap.put(eventName,value);
        }
        return eventMap;
    }
}
