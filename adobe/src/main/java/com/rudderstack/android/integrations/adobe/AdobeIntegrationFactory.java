package com.rudderstack.android.integrations.adobe;


import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.adobe.mobile.Analytics;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.rudderstack.android.sdk.core.MessageType;
import com.rudderstack.android.sdk.core.RudderClient;
import com.rudderstack.android.sdk.core.RudderConfig;
import com.rudderstack.android.sdk.core.RudderIntegration;
import com.rudderstack.android.sdk.core.RudderLogger;
import com.rudderstack.android.sdk.core.RudderMessage;

import com.adobe.mobile.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AdobeIntegrationFactory extends RudderIntegration<Void> {
    private static final String ADOBE_KEY = "Adobe Analytics";
    private AdobeDestinationConfig destinationConfig;

    private RudderLogger logger;
    private VideoAnalytics video;

    private static final Map<String, Object> eventsMapping = new HashMap<String, Object>(){
        {
            put("product added", "scAdd");
            put("product removed", "scRemove");
            put("cart viewed", "scView");
            put("checkout started", "scCheckout");
            put("order completed", "purchase");
            put("product viewed", "prodView");
        }
    };

    public static Factory FACTORY = new Factory() {
        @Override
        public RudderIntegration<?> create(Object settings, RudderClient client, RudderConfig rudderConfig) {
            return new AdobeIntegrationFactory(settings, rudderConfig);
        }

        @Override
        public String key() {
            return ADOBE_KEY;
        }
    };

    private AdobeIntegrationFactory(@NonNull Object config, RudderConfig rudderConfig) {
        if (RudderClient.getApplication() == null) {
            RudderLogger.logError("Application is null. Aborting Adobe initialization.");
            return;
        }

        GsonBuilder gsonBuilder = new GsonBuilder();
        JsonDeserializer<AdobeDestinationConfig> deserializer =
                new JsonDeserializer<AdobeDestinationConfig>() {
                    @Override
                    public AdobeDestinationConfig deserialize(
                            JsonElement json,
                            Type typeOfT,
                            JsonDeserializationContext context
                    ) throws JsonParseException {
                        JsonObject jsonObject = json.getAsJsonObject();

                        JsonArray contextDataMaps = (JsonArray) (jsonObject.get("contextDataMapping"));
                        Map<String, Object> contextDataMap = Utils.getContextMap(contextDataMaps);

                        JsonArray rudderEventsToAdobeEventsMaps = (JsonArray) (jsonObject.get("rudderEventsToAdobeEvents"));
                        Map<String, Object> rudderEventsToAdobeEventsMap = Utils.getEventsMap(rudderEventsToAdobeEventsMaps);

                        JsonArray videoEventsMaps = (JsonArray) (jsonObject.get("eventsToTypes"));
                        Map<String, Object> videoEventsMap = Utils.getVideoEventsMap(videoEventsMaps);
                        return new AdobeDestinationConfig(
                                Utils.getString(jsonObject,"heartbeatTrackingServerUrl"),
                                contextDataMap,
                                rudderEventsToAdobeEventsMap,
                                videoEventsMap,
                                jsonObject.get("sslHeartbeat").getAsBoolean(),
                                Utils.getString(jsonObject, "contextDataPrefix")
                        );
                    }
                };

        gsonBuilder.registerTypeAdapter(AdobeDestinationConfig.class, deserializer);
        Gson customGson = gsonBuilder.create();
        this.destinationConfig = customGson.fromJson(customGson.toJson(config), AdobeDestinationConfig.class);

        video = new VideoAnalytics(
                RudderClient.getApplication(),
                destinationConfig.heartbeatTrackingServerUrl,
                destinationConfig.contextData,
                destinationConfig.ssl,
                destinationConfig.customDataPrefix
        );

        // Adobe Analytics Initialization
        Config.setContext(RudderClient.getApplication());

        Config.setDebugLogging(true);
        video.setDebugLogging(true);

        //Debugger of Adobe Analytics
        if (rudderConfig.getLogLevel() == RudderLogger.RudderLogLevel.VERBOSE) {
            Config.setDebugLogging(true);
            video.setDebugLogging(true);
        }
    }

    private void processRudderEvent(RudderMessage element) {
        String type = element.getType();
        if (type != null) {
            switch (type) {

                case MessageType.IDENTIFY:
                    String userId = element.getUserId();
                    if (!TextUtils.isEmpty(userId)) {
                        Config.setUserIdentifier(userId);
                    }
                    break;

                case MessageType.TRACK:
                    String eventName = element.getEventName();
                    if(eventName == null)
                        return;

                    Map<String, Object> mappedEvents = destinationConfig.rudderEventsToAdobeEvents;
                    Map<String, Object> eventProperties = element.getProperties();

                    // If it is Video Analytics event
                    // ??.toLowerCase() will be used or not with eventName ??
                    if (isVideoEvent(eventName)) {
                        video.track(getVideoEvent(eventName),element);
                        return;
                    }

                    // If it is E-Commerce event

                    if(eventsMapping.containsKey(eventName.toLowerCase())) {
                        // To check, if either mappedEvents is either null or empty, or,
                        // mappedEvents contain the eventName mapped at Rudderstack dashboard or not.
                        if(Utils.isEmpty(mappedEvents) || !mappedEvents.containsKey(eventName)) {
                            RudderLogger.logVerbose("Event must be either configured in Adobe and in the Rudderstack setting, "
                                    + "or it must be a reserved Adobe Ecommerce or Video event.");
                            return;
                        }
                        try {
                            handleEcommerce(eventsMapping.get(eventName.toLowerCase()), eventProperties);
                        } catch (JSONException e) {
                            RudderLogger.logVerbose("JSONException occurred. Aborting track call.");
                        }
                        return;
                    }

                    if(Utils.isEmpty(mappedEvents) || !mappedEvents.containsKey(eventName)) {
                        RudderLogger.logVerbose("Event must be either configured in Adobe and in the Rudderstack setting, "
                                + "or it must be a reserved Adobe Ecommerce or Video event.");
                        return;
                    }

                    // Custom Track call
                    Analytics.trackAction((String) mappedEvents.get(eventName), eventProperties);
                    break;

                case MessageType.SCREEN:
                    String screenName = element.getEventName();
                    if(screenName == null) {
                        return;
                    }
                    if(Utils.isEmpty(element.getProperties())){
                        return;
                    }
                    eventProperties = element.getProperties();
                    Map<String, Object> contextDataScreen = new HashMap<>();
                    for (Map.Entry<String, Object> entry : eventProperties.entrySet()) {
                        contextDataScreen.put(entry.getKey(), entry.getValue());
                    }
                    Analytics.trackState(screenName, contextDataScreen);
                    break;

                default:
                    RudderLogger.logWarn("MessageType is not specified or supported");
                    break;
            }
        }
    }

    private String getVideoEvent(String eventName) {
        return (String) destinationConfig.videoEvents.get(eventName);
    }


    private boolean isVideoEvent(String eventName) {
        return destinationConfig.videoEvents.containsKey(eventName);
    }

    @Override
    public void reset() {
        Config.setUserIdentifier(null);
    }

    @Override
    public void dump(@Nullable RudderMessage element) {
        try {
            if (element != null) {
                processRudderEvent(element);
            }
        } catch (Exception e) {
            RudderLogger.logError(e);
        }
    }

    private void handleEcommerce(Object eventName, Map<String, Object> eventProperties) throws JSONException {

        Map<String, Object> contextData = new HashMap<>();
        contextData.put("&&events", eventName);

        if (!Utils.isEmpty(eventProperties) && eventProperties.containsKey("products")) {
            String products = getProducts(getJSONArray(eventProperties.get("products")));
            if (products.length() != 0) {
                contextData.put("&&products", products);
            }
            eventProperties.remove("products");
        }

        if (!Utils.isEmpty(eventProperties) && eventProperties.containsKey("order_id")) {
            contextData.put("purchaseid", eventProperties.get("order_id"));
            eventProperties.remove("order_id");
        }

        if (!Utils.isEmpty(eventProperties) && eventProperties.containsKey("orderId")) {
            contextData.put("purchaseid", eventProperties.get("orderId"));
            eventProperties.remove("orderId");
        }

        // Handling the custom mapped properties
        if(!Utils.isEmpty(destinationConfig.contextData) && !Utils.isEmpty(eventProperties)) {
            for(Map.Entry<String, Object> contextDataVariables : destinationConfig.contextData.entrySet() ) {
                if(eventProperties.containsKey(contextDataVariables.getKey())){
                    contextData.put((String) contextDataVariables.getValue(), eventProperties.get(contextDataVariables.getKey()));
                    eventProperties.remove(contextDataVariables.getKey());
                }
            }
        }

        // Add all eventProperties which are left
        // And add prefix to the eventName
        if(!Utils.isEmpty(eventProperties)){
            for (String extraProperty : eventProperties.keySet()) {
                String variable = destinationConfig.customDataPrefix + extraProperty;
                contextData.put(variable, Utils.getString(eventProperties.get(extraProperty)));
                eventProperties.remove(extraProperty);
            }
        }

        // Track Call for E-Commerce event
        Analytics.trackAction((String) eventName, contextData);
    }

    @NonNull
    private JSONArray getJSONArray(@Nullable Object object) {
        if (object instanceof JSONArray) {
            return (JSONArray) object;
        }
        if(object instanceof List){
            ArrayList<Object> arrayList = new ArrayList<>();
            arrayList.addAll((Collection<?>) object);
            return new JSONArray(arrayList);
        }
        return new JSONArray((ArrayList) object);
    }

    // If eventProperties contains key of Products
    private String getProducts(JSONArray products) throws JSONException {
        StringBuilder product = new StringBuilder();

        for(int i = 0; i < products.length(); i++) {
            JSONObject productObject = (JSONObject) products.get(i);
            StringBuilder productString = new StringBuilder();
            productString.append(getProductString(productObject, "category"));
            productString.append(getProductString(productObject, "name"));
            productString.append(getProductString(productObject, "quantity"));
            productString.append(getProductString(productObject, "price"));
            productString = removeSemicolon(productString);
            if(product.length() != 0) {
                product.append(",");
            }
            product.append(productString);
        }
        return product.toString();
    }

    // Remove all the last occurrence of semicolon from the productString
    private StringBuilder removeSemicolon(StringBuilder productString) {
        int index = productString.lastIndexOf(";");
        return productString.delete(index, productString.length());
    }

    private StringBuilder getProductString(JSONObject product, String productParameter) throws JSONException {
        StringBuilder productString = new StringBuilder();
        if (product.has(productParameter)) {
            productString.append(product.get(productParameter));
        }
        productString.append(";");
        return productString;
    }

}