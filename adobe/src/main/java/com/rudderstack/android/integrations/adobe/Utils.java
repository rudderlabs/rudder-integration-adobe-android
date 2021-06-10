package com.rudderstack.android.integrations.adobe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.rudderstack.android.sdk.core.RudderMessage;

import java.util.Arrays;
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

    public static Map<String, Object> getVideoEventsMap(JsonArray videoEventsMap) {
        Map<String, Object> videoEventMap = new HashMap<>();
        for (int i = 0; i < videoEventsMap.size(); i++) {
            JsonObject eventObject = (JsonObject) videoEventsMap.get(i);
            String eventName = eventObject.get("from").getAsString();
            String value = eventObject.get("to").getAsString();
            if(value.equals("initHeartbeat") || value.equals("heartbeatUpdatePlayhead")){
                continue;
            }
            videoEventMap.put(eventName,value);
        }
        return videoEventMap;
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
            }
        }
        return defaultValue;
    }

    public static Object searchValue(String field, RudderMessage element) {
        if (field == null || field.trim().length() == 0) {
            throw new IllegalArgumentException("The field name must be defined");
        }

        String[] searchPaths = field.split("\\.");

        // Using the properties object as starting point by default.
        Map<String, Object> values = element.getProperties();

        // Dot is present at the beginning of the field name
        if (searchPaths[0].equals("")) {
            // Using the root of the payload as starting point
//            values = eventPayload;
            searchPaths = Arrays.copyOfRange(searchPaths, 1, searchPaths.length);
//            return searchValue(searchPaths, element);
        }

        return searchValue(searchPaths, values);
    }

    /*
    private static Object searchValue(String[] searchPath, RudderMessage element) {
        RudderMessage currentValues = element;

        for (int i = 0; i < searchPath.length; i++) {
            String path = searchPath[i];

            if (path.trim().length() == 0) {
                throw new IllegalArgumentException("Invalid field name");
            }

            if(path.equalsIgnoreCase("properties"))

            if (!currentValues.containsKey(path)) {
                return null;
            }

            Object value = currentValues.get(path);
            if (value == null) {
                return null;
            }

            if (i == searchPath.length - 1) {
                return value;
            }

            if (value instanceof ValueMap) {
                currentValues = (ValueMap) value;
            } else if (value instanceof Map) {
                try {
                    currentValues = new ValueMap((Map<String, Object>) value);
                } catch (ClassCastException e) {
                    return null;
                }
            }
        }

        return null;
    }
    //*/

    private static Object searchValue(String[] searchPath, Map<String, Object> eventProperty) {

        Map<String, Object> currentValues = eventProperty;

        for (int i = 0; i < searchPath.length; i++) {
            String path = searchPath[i];

            if (path.trim().length() == 0) {
                throw new IllegalArgumentException("Invalid field name");
            }

            if (!currentValues.containsKey(path)) {
                return null;
            }

            Object value = currentValues.get(path);
            if (value == null) {
                return null;
            }

            if (i == searchPath.length - 1) {
                return value;
            }

            if (value instanceof Map) {
                try {
                    currentValues = (Map<String, Object>) value;
                } catch (ClassCastException e) {
                    return null;
                }
            }
        }

        return null;
    }
}
