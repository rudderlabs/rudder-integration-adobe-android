package com.rudderstack.android.integrations.adobe;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.rudderstack.android.sdk.core.RudderContext;
import com.rudderstack.android.sdk.core.RudderLogger;
import com.rudderstack.android.sdk.core.RudderMessage;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Observer;

public class Utils {

    public static Map<String, Object> getMappedRudderEvents(JsonArray mappedERudderEvents, boolean isVideoMappedEvent) {
        if(isEmpty(mappedERudderEvents)) {
            return null;
        }
        Map<String, Object> mappedEvents = new HashMap<>();
        for (int i = 0; i < mappedERudderEvents.size(); i++) {
            JsonObject eventObject = (JsonObject) mappedERudderEvents.get(i);
            String eventName = eventObject.get("from").getAsString();
            Object eventValue = eventObject.get("to").getAsString();
            if (isVideoMappedEvent &&
                    (eventValue.equals("initHeartbeat") || eventValue.equals("heartbeatUpdatePlayhead"))) {
                continue;
            }
            mappedEvents.put(eventName, eventValue);
        }
        return mappedEvents;
    }

    public static boolean isEmpty(JsonArray value) {
        return (value == null || value.size() == 0);
    }

    public static boolean isEmpty(Map<String, Object> val){
        return (val == null || val.size() == 0);
    }

    public static boolean isEmpty(String val) {
        return (val == null || val.trim().isEmpty());
    }

    public static boolean getBoolean(Object value, boolean defaultValue) {
        if (value instanceof Boolean) {
            return (boolean) value;
        } else if (value instanceof String) {
            return Boolean.valueOf((String) value);
        }
        return defaultValue;
    }

    public static String getString(Object value) {
        if (value instanceof String) {
            return (String) value;
        } else if (value != null) {
            return String.valueOf(value);
        }
        return null;
    }

    public static String getString(JsonObject jsonObject, String value) {
        if (jsonObject.has(value)) {
            return jsonObject.get(value).getAsString();
        }
        return "";
    }

    public static double getDouble(Object value, double defaultValue) {
        if (value instanceof Double) {
            return (double) value;
        }
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        } else if (value instanceof String) {
            try {
                return Double.valueOf((String) value);
            } catch (NumberFormatException ignored) {
                RudderLogger.logDebug("Unable to convert the value: " + value +
                        " to Double, using the defaultValue: " + defaultValue);
            }
        }
        return defaultValue;
    }

    public static long getLong(Object value, long defaultValue) {
        if (value instanceof Long) {
            return (long) value;
        }
        if (value instanceof Number) {
            return ((Number) value).longValue();
        } else if (value instanceof String) {
            try {
                return Long.valueOf((String) value);
            } catch (NumberFormatException ignored) {
                RudderLogger.logDebug("Unable to convert the value: " + value +
                        " to Long, using the defaultValue: " + defaultValue);
            }
        }
        return defaultValue;
    }

    /**
     * Inspects the event payload and retrieves the value described in the field. Field respects dot
     * notation (myObject.name) for event properties. If there is a dot present at the beginning of
     * the field, it will retrieve the value from the root of the payload.
     *
     * <p>Examples:
     *
     * <ul>
     *   <li><code>myObject.name</code> = <code>track.properties.myObject.name</code>
     *   <li><code>.userId</code> = <code>identify.userId</code>
     *   <li><code>.context.library</code> = <code>track.context.library</code>
     * </ul>
     *
     * @param field Field name.
     * @param element Event payload.
     * @return The value if found, <code>null</code> otherwise.
     */
    public static Object getMappedContextValue(String field, RudderMessage element) {
        if (field == null || field.trim().length() == 0) {
            throw new IllegalArgumentException("The field name must be defined");
        }
        String[] searchPaths = field.split("\\.");

        // Using the eventProperties object as starting point by default.
        Map<String, Object> payload = element.getProperties();

        // Dot is present at the beginning of the field name
        if (searchPaths[0].equals("")) {
            // Using the root of the payload as starting point
            // and converting to Map type
            Gson newGson = new Gson();
            Type type = new TypeToken<Map<String, Object>>(){}.getType();
            payload = newGson.fromJson(newGson.toJson(element), type);

            searchPaths = Arrays.copyOfRange(searchPaths, 1, searchPaths.length);
        }

        return getMappedContextValue(searchPaths, payload);
    }

    private static Object getMappedContextValue(String[] searchPath, Map<String, Object> payload) {

        if (isEmpty(payload)) {
            return null;
        }
        Map<String, Object> totalPayload = payload;
        for (int i = 0; i < searchPath.length; i++) {
            String path = searchPath[i];

            if (path.trim().length() == 0) {
                throw new IllegalArgumentException("Invalid field name");
            }

            if (!totalPayload.containsKey(path)) {
                return null;
            }

            Object currentPayload = totalPayload.get(path);
            if (currentPayload == null) {
                return null;
            }

            if (i == searchPath.length - 1) {
                return currentPayload;
            }

            if (currentPayload instanceof Map) {
                try {
                    totalPayload = (Map<String, Object>) currentPayload;
                } catch (ClassCastException e) {
                    return null;
                }
            }
        }

        return null;
    }
}
