package com.rudderstack.android.integrations.adobe;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
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

                        JsonArray contextDataMaps =
                                (JsonArray) (jsonObject.get("contextDataMapping"));
                        Map<String, Object> contextDataMap =
                                Utils.getMappedRudderEvents(contextDataMaps, false);

                        JsonArray rudderEventsToAdobeEventsMaps =
                                (JsonArray) (jsonObject.get("rudderEventsToAdobeEvents"));
                        Map<String, Object> rudderEventsToAdobeEventsMap =
                                Utils.getMappedRudderEvents(rudderEventsToAdobeEventsMaps, false);

                        JsonArray videoEventsMaps =
                                (JsonArray) (jsonObject.get("eventsToTypes"));
                        Map<String, Object> videoEventsMap = Utils.getMappedRudderEvents(videoEventsMaps, true);

                        return new AdobeDestinationConfig(
                                Utils.getString(jsonObject,"heartbeatTrackingServerUrl"),
                                contextDataMap,
                                rudderEventsToAdobeEventsMap,
                                videoEventsMap,
                                jsonObject.get("sslHeartbeat").getAsBoolean(),
                                Utils.getString(jsonObject, "contextDataPrefix"),
                                Utils.getString(jsonObject, "productIdentifier"),
                                rudderConfig.isTrackLifecycleEvents()
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
                destinationConfig.contextDataPrefix
        );

        // Adobe Analytics Initialization
        Config.setContext(RudderClient.getApplication());

        //Debugger of Adobe Analytics
        if (rudderConfig.getLogLevel() >= RudderLogger.RudderLogLevel.DEBUG) {
            Config.setDebugLogging(true);
            video.setDebugLogging(true);
        }

        RudderClient
                .getApplication()
                .registerActivityLifecycleCallbacks(
                        new Application.ActivityLifecycleCallbacks() {
                            @Override
                            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                                Config.setContext(activity.getApplicationContext());
                                RudderLogger.logVerbose("Config.setContext();");
                            }

                            @Override
                            public void onActivityResumed(Activity activity) {
                                Config.collectLifecycleData();
                                RudderLogger.logVerbose("Config.collectLifecycleData(" + activity + ");");
                            }

                            @Override
                            public void onActivityPaused(Activity activity) {
                                Config.pauseCollectingLifecycleData();
                                RudderLogger.logVerbose("Config.pauseCollectingLifecycleData();");
                            }

                            @SuppressLint("RestrictedApi")
                            @Override
                            public void onActivityStopped(@NonNull Activity activity) {
                                // Nothing to Implement
                            }

                            @Override
                            public void onActivitySaveInstanceState(@NonNull Activity activity, @Nullable Bundle bundle) {
                                // Nothing to implement
                            }

                            @Override
                            public void onActivityDestroyed(@NonNull Activity activity) {
                                // Nothing to implement
                            }

                            @SuppressLint("RestrictedApi")
                            @Override
                            public void onActivityStarted(@NonNull Activity activity) {
                                // Nothing to implement
                            }
                        }
                );
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

                    Map<String, Object> eventProperties = element.getProperties();

                    // If it is Video Analytics event
                    if (isVideoEvent(eventName)) {
                        video.track(getVideoEvent(eventName),element);
                        return;
                    }

                    // If it is E-Commerce event
                    if(eventsMapping.containsKey(eventName.toLowerCase())) {
                        if (destinationConfig.rudderEventsToAdobeEvents != null
                                && destinationConfig.rudderEventsToAdobeEvents.containsKey(eventName)) {
                            RudderLogger.logDebug(
                                    "Rudderstack currently does not support mapping of ecommerce events to "
                                            + "custom Adobe events.");
                            return;
                        }
                        try {
                            handleEcommerce(eventsMapping.get(eventName.toLowerCase()), eventProperties);
                        } catch (JSONException e) {
                            RudderLogger.logDebug("JSONException occurred. Aborting track call.");
                        }
                        return;
                    }

                    if(!destinationConfig.isTrackLifecycleEvents)
                        if (destinationConfig.rudderEventsToAdobeEvents == null
                                || destinationConfig.rudderEventsToAdobeEvents.size() == 0
                                || !destinationConfig.rudderEventsToAdobeEvents.containsKey(eventName)) {
                            RudderLogger.logDebug("Event must be either configured in Adobe and in the Rudderstack setting, "
                                    + "or it should be a reserved Adobe Ecommerce or Video event.");
                            return;
                        }

                    // Custom Track call
                    Map<String, Object> contextData = getContextData(eventProperties);
                    Analytics.trackAction((String) destinationConfig.rudderEventsToAdobeEvents.get(eventName), contextData);
                    break;

                case MessageType.SCREEN:
                    String screenName = element.getEventName();
                    if(screenName == null) {
                        RudderLogger.logDebug("Screen Name is empty!");
                        return;
                    }
                    if(Utils.isEmpty(element.getProperties())){
                        RudderLogger.logDebug(screenName+" eventProperties is null");
                        return;
                    }
                    eventProperties = element.getProperties();
                    Map<String, Object> contextDataScreen = getContextData(eventProperties);
                    Analytics.trackState(screenName, contextDataScreen);
                    break;

                default:
                    RudderLogger.logDebug("MessageType is not specified or supported");
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
        RudderLogger.logVerbose("Adobe Analytics setUserIdentifier(null).");
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

    private Map<String, Object> getContextData(Map<String, Object> eventProperties) {
        // Remove products just in case
        eventProperties.remove("products");
        Map<String, Object> contextData = new HashMap<>();
        contextData.putAll(getMappedAndExtraProperties(eventProperties));
        if (contextData.size() == 0) {
            return null;
        }
        return contextData;
    }

    private void handleEcommerce(Object eventName, Map<String, Object> eventProperties) throws JSONException {

        Map<String, Object> contextData = new HashMap<>();
        contextData.put("&&events", eventName);

        // If products variable is present in the payload
        String products;
        if (!Utils.isEmpty(eventProperties) && eventProperties.containsKey("products")) {
            products = getProductsString(getJSONArray(eventProperties.get("products")));
            eventProperties.remove("products");
        }
        // If product is not present, then look for category, quantity, price and productIdentifier
        // at the root level i.e., at eventProperties.
        else {
            products = getProductsStrings(eventProperties);
            eventProperties.remove("category");
            eventProperties.remove("quantity");
            eventProperties.remove("price");
            String idKey = destinationConfig.productIdentifier;
            if (idKey == null || idKey.equals("id")) {
                eventProperties.remove("productId");
                eventProperties.remove("product_id");
            } else {
                eventProperties.remove(idKey);
            }
        }

        if (products != null && products.trim().length() != 0) {
            contextData.put("&&products", products);
        }

        if (!Utils.isEmpty(eventProperties) && eventProperties.containsKey("order_id")) {
            contextData.put("purchaseid", eventProperties.get("order_id"));
            eventProperties.remove("order_id");
        }

        if (!Utils.isEmpty(eventProperties) && eventProperties.containsKey("orderId")) {
            contextData.put("purchaseid", eventProperties.get("orderId"));
            eventProperties.remove("orderId");
        }

        contextData.putAll(getMappedAndExtraProperties(eventProperties));

        // Track Call for E-Commerce event
        Analytics.trackAction((String) eventName, contextData);
    }

    // For custom mapped properties done at Rudderstack dashboard
    // and extra properties handling with prefix
    private Map<String, Object> getMappedAndExtraProperties(Map<String, Object> eventProperties) {
        Map<String, Object> contextData = new HashMap<>();

        // Handling the custom mapped properties
        if(!Utils.isEmpty(destinationConfig.contextData) && !Utils.isEmpty(eventProperties)) {
            for (Map.Entry<String, Object> contextDataVariables : destinationConfig.contextData.entrySet()) {
                if (eventProperties.containsKey(contextDataVariables.getKey())) {
                    contextData.put((String) contextDataVariables.getValue(), eventProperties.get(contextDataVariables.getKey()));
                    eventProperties.remove(contextDataVariables.getKey());
                }
            }
        }

        // Add all eventProperties which are left
        // And add prefix to the eventName
        if(!Utils.isEmpty(eventProperties)){
            for (String extraProperty : eventProperties.keySet()) {
                String propertyName = destinationConfig.contextDataPrefix + extraProperty;
                contextData.put(propertyName, Utils.getString(eventProperties.get(extraProperty)));
            }
            eventProperties.clear();
        }
        return contextData;
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

    // For handling the products array present inside the event payload.
    private String getProductsString(JSONArray products) throws JSONException {
        StringBuilder productString = new StringBuilder();
        for(int i = 0; i < products.length(); i++) {
            JSONObject productObject = (JSONObject) products.get(i);
            Map<String, Object> product = new HashMap<>();

            product.putAll(new Gson().fromJson(productObject.toString(), HashMap.class));

            if(productString.length() != 0) {
                productString.append(",");
            }

            productString.append(getProductsStrings(product));
            productString = removeSemicolon(productString);
        }
        return productString.toString();
    }

    // For constructing product string in the format as required by Adobe.
    // Also, handles the case when the products specs is present at the root level
    // i.e., inside the event payload, not in the product variable.
    private String getProductsStrings(Map<String, Object> product) {
        StringBuilder productString = new StringBuilder();

        if(product.containsKey("category")) {
            String category = Utils.getString(product.get("category"));
            productString.append(category);
        }
        productString.append(";");

        // product_id is necessary else it'll throw an error.
        productString.append(getProductId(product));
        productString.append(";");

        // Default to 1.
        int quantity= 1;
        if(product.containsKey("quantity")) {
            String q = Utils.getString(product.get("quantity"));
            productString.append(Utils.getString(q));
            if (q != null) {
                try {
                    quantity = Integer.parseInt(q);
                } catch (NumberFormatException e) {
                    // Default.
                }
            }
        }
        productString.append(";");

        // Default to 0.
        double price = 0.0;
        if(product.containsKey("price")) {
            String p = Utils.getString(product.get("price"));
            if (p != null) {
                try {
                    price = Double.parseDouble(p);
                } catch (NumberFormatException e) {
                    // Default.
                }
            }
            price = price * quantity;
            productString.append(Utils.getString(price));
        }
        return productString.toString();
    }

    /**
     * Sets the product ID using productIdentifier configured at the settings.
     *(supported values are <code>name</code>, <code>sku</code> and <code>id</code>.
     * If the field is not present, it fallbacks to "productId" and "id".
     *
     * <p>Currently we do not allow to have products without IDs. Adobe Analytics allows to send an
     * extra product for merchandising evars and event serialization, as seen over here
     * <a href="https://experienceleague.adobe.com/docs/analytics/implementation/vars/page-vars/products.html?lang=en#">
     * docs</a>, but it is not well documented and does not conform Rudderstack's spec.
     *
     * <p><b>NOTE: Ecommerce spec defines "product_id" instead of "id". We fallback to "id" to
     * keep backwards compatibility.</b>
     *
     * @param eventProduct Event's product.
     * @throws IllegalArgumentException if the product does not have an ID.
     */
    private String getProductId(Map<String, Object> eventProduct) {

        String id = null;
        String productIdentifier = destinationConfig.productIdentifier;
        if (productIdentifier != null) {
            // When productIdentifier is "id" use the default behavior.
            if (!productIdentifier.equals("id")) {
                id = Utils.getString(eventProduct.get(productIdentifier));
            }
        }

        // Fallback to "productId" as ecommerce spec
        if (id == null || id.trim().length() == 0) {
            id = Utils.getString(eventProduct.get("productId"));
        }

        // Fallback to "product_id" as ecommerce spec
        if (id == null || id.trim().length() == 0) {
            id = Utils.getString(eventProduct.get("product_id"));
        }

        // Fallback to "id" as ecommerce spec
        if (id == null || id.trim().length() == 0) {
            id = Utils.getString(eventProduct.get("id"));
        }

        if (id == null || id.trim().length() == 0) {
            throw new IllegalArgumentException("Product id is not defined.");
        }
        return id;
    }

    // Remove all the last occurrence of semicolon from the productString
    private StringBuilder removeSemicolon(StringBuilder productString) {
        for(int i = productString.length() - 1; i >= 0; i-- ){
            if (productString.charAt(i) != ';'){
                return productString.delete(i + 1, productString.length());
            }
        }
        return productString.delete(0, productString.length());
    }
}