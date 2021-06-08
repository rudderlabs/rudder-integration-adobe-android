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
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AdobeIntegrationFactory extends RudderIntegration<Void> {
    private static final String ADOBE_KEY = "Adobe Analytics";
    private AdobeDestinationConfig destinationConfig;
//    private static final String ADOBE_KEY = "ADOBE_KEY";

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
                        JsonArray eventPriorityMap = (JsonArray) (jsonObject.get("contextDataVariables"));
                        Map<String, Object> eventMap = Utils.getEventMap(eventPriorityMap);

                        return new AdobeDestinationConfig(
                                jsonObject.get("url").getAsString(),
                                eventMap
                        );
                    }
                };

        gsonBuilder.registerTypeAdapter(AdobeDestinationConfig.class, deserializer);
        Gson customGson = gsonBuilder.create();
        this.destinationConfig = customGson.fromJson(customGson.toJson(config), AdobeDestinationConfig.class);

        // Adobe Analytics Initialization
        Config.setContext(RudderClient.getApplication());

        //Debugger of Adobe Analytics
        if (rudderConfig.getLogLevel() == RudderLogger.RudderLogLevel.VERBOSE) {
            Config.setDebugLogging(true);
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

                    // If it E-Commerce event
                    if(eventsMapping.containsKey(eventName.toLowerCase())) {
                        try {
                            handleEcommerce(eventsMapping.get(eventName.toLowerCase()), element.getProperties());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        return;
                    }
                    // Custom Track call
                    Analytics.trackAction((String) eventName, element.getProperties());
                    break;

                case MessageType.SCREEN:
                    String screenName = element.getEventName();
                    if(screenName == null) {
                        return;
                    }
                    if(element.getProperties() != null || element.getProperties().size() != 0){
                        return;
                    }
                    Map<String, Object> eventProperties = element.getProperties();
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

        if (eventProperties != null && eventProperties.containsKey("products")) {
            String products = getProducts(getJSONArray(eventProperties.get("products")));
            if (products != null) {
                contextData.put("&&products", products);
            }
            eventProperties.remove("products");
        }

        //Need to confirm if there is only order_id or OrderId as well  ???
        if (eventProperties != null && eventProperties.containsKey("order_id")) {
            contextData.put("purchaseid", eventProperties.get("order_id"));
            eventProperties.remove("order_id");
        }

        // Handling the custom mapped properties
        if(!isEmpty(destinationConfig.contextDataVariables) && !isEmpty(eventProperties)) {
            for(Map.Entry<String, Object> contextDataVaribale : destinationConfig.contextDataVariables.entrySet() ) {
                if(eventProperties.containsKey(contextDataVaribale.getKey())){
                    contextData.put((String) contextDataVaribale.getValue(), eventProperties.get(contextDataVaribale.getKey()));
                    eventProperties.remove(contextDataVaribale.getKey());
                }
            }
        }

        //Add eventProperties that are left
        if(eventProperties != null){
            contextData.putAll(eventProperties);
            eventProperties.clear();
        }

        //Track Call for E-Commerce event
        Analytics.trackAction((String) eventName, contextData);
    }

    private boolean isEmpty(Map<String, Object> val){
        return (val == null && val.size() == 0);
    }


    @NonNull
    private JSONArray getJSONArray(@Nullable Object object) {
        if (object instanceof JSONArray) {
            return (JSONArray) object;
        }
        if(object instanceof List){
            ArrayList<Object> arrayList = new ArrayList<>();
            for(Object element: (List) object ) {
                arrayList.add(element);
            }
            return new JSONArray(arrayList);
        }
        return new JSONArray((ArrayList) object);
    }

    // If eventProperties contains key of Products
    private String getProducts(JSONArray products) throws JSONException {
        StringBuilder productProperties = new StringBuilder();
        for(int i = 0; i < products.length(); i++) {
            JSONObject product = (JSONObject) products.get(i);
            StringBuilder productString = new StringBuilder();
            productString.append(getProductString(product, "category"));
            productString.append(getProductString(product, "name"));
            productString.append(getProductString(product, "quantity"));
            productString.append(getProductString(product, "price"));
            productString = removeSemicolon(productString);
            if(productProperties.length() != 0) {
                productProperties.append(",");
            }
            productProperties.append(productString);
        }
        if(productProperties.length() != 0){
            return productProperties.toString();
        }
        return null;
    }

    // Look for stringbuilder alternatives
    private StringBuilder removeSemicolon(StringBuilder productString) {
        if(productString.length() != 0) {
            int lastIndex = productString.length()-1;
            for (int i = productString.length()-1; i >= 0; i--){
                if(productString.charAt(i) == ';')
                    continue;
                lastIndex = i;
                break;
            }
            return new StringBuilder(productString.substring(0,lastIndex+1));
        }
        return (productString);
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