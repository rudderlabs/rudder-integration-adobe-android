package com.rudderstack.android.integrations.adobe;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.rudderstack.android.sdk.core.RudderLogger;
import com.rudderstack.android.sdk.core.RudderMessage;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utils {

    // Get the mapping of 'custom events', 'context data' & 'video events',
    // done at RudderStack dashboard
    public static Map<String, String> getMappedRudderEvents(JsonArray mappedERudderEvents, boolean isVideoMappedEvent) {
        if(isEmpty(mappedERudderEvents)) {
            return null;
        }
        Map<String, String> mappedEvents = new HashMap<>();
        for (int i = 0; i < mappedERudderEvents.size(); i++) {
            JsonObject eventObject = (JsonObject) mappedERudderEvents.get(i);
            String eventName = eventObject.get("from").getAsString();
            String eventValue = eventObject.get("to").getAsString();
            if (isVideoMappedEvent &&
                    (eventValue.equals("initHeartbeat") || eventValue.equals("heartbeatUpdatePlayhead"))) {
                continue;
            }
            mappedEvents.put(eventName, eventValue);
        }
        return mappedEvents;
    }

    public static boolean isEmpty(Object value) {
        if(value == null){
            return true;
        }

        if (value instanceof JsonArray) {
            return (((JsonArray) value).size() == 0);
        }

        if (value instanceof JSONArray) {
            return (((JSONArray) value).length() == 0);
        }

        if (value instanceof Map) {
            return (((Map<?, ?>) value).size() == 0);
        }

        if (value instanceof String) {
            return (((String) value).trim().isEmpty());
        }

        return false;
    }

    public static String getStringFromJSON(JsonObject jsonObject, String value) {
        if (jsonObject.has(value)) {
            return jsonObject.get(value).getAsString();
        }
        return "";
    }

    public static boolean getBoolean(Object value, boolean defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        if (value instanceof String) {
            return Boolean.parseBoolean((String) value);
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

    public static double getDouble(Object value, double defaultValue) {
        if (value instanceof Double) {
            return (double) value;
        }
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        } else if (value instanceof String) {
            try {
                return Double.parseDouble((String) value);
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
                return Long.parseLong((String) value);
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
     *   <li><code>.myObject.name</code> = <code>message.properties.myObject.name</code>
     *   <li><code>.userId</code> = <code>message.userId</code>
     *   <li><code>.context.library</code> = <code>message.context.library</code>
     * </ul>
     *
     * @param field Field name.
     * @param element Event payload.
     * @return The value if found, <code>null</code> otherwise.
     */
    public static String getMappedContextValue(String field, RudderMessage element) {
        if (field == null || field.trim().length() == 0) {
            RudderLogger.logDebug("Error occurred while handling custom properties");
            return null;
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

        return Utils.getString(getMappedContextValue(searchPaths, payload));
    }

    private static Object getMappedContextValue(String[] searchPath, Map<String, Object> payload) {
        if (isEmpty(payload)) {
            return null;
        }
        Map<?, ?> totalPayload = payload;
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
                    totalPayload = (Map<?, ?>) currentPayload;
                } catch (ClassCastException e) {
                    return null;
                }
            }
        }
        return null;
    }

    static boolean isProductIsList(Object products) {
        if (products instanceof List) {
            List<?> productsList = (List<?>) products;
            return !productsList.isEmpty() && productsList.get(0) instanceof Map;
        }
        return false;
    }
}
