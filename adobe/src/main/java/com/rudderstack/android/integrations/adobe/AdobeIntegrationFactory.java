package com.rudderstack.android.integrations.adobe;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.rudderstack.android.sdk.core.MessageType;
import com.rudderstack.android.sdk.core.RudderClient;
import com.rudderstack.android.sdk.core.RudderConfig;
import com.rudderstack.android.sdk.core.RudderIntegration;
import com.rudderstack.android.sdk.core.RudderLogger;
import com.rudderstack.android.sdk.core.RudderMessage;

import com.adobe.marketing.mobile.MobileCore;
import com.adobe.marketing.mobile.LoggingMode;
import com.adobe.marketing.mobile.Identity;
import com.adobe.marketing.mobile.Analytics;
import com.adobe.marketing.mobile.Lifecycle;
import com.adobe.marketing.mobile.Extension;
import com.adobe.marketing.mobile.Media;


import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class AdobeIntegrationFactory extends RudderIntegration<Void> {
    private static final String PRODUCT_ID = "product_id";
    private static final String APPLICATION_INSTALLED = "Application Installed";
    private static final String ADOBE_KEY = "Adobe Analytics";
    private static final String APPLICATION_UPDATED = "Application Updated";
    private static final String APPLICATION_OPENED = "Application Opened";
    private static final String APPLICATION_BACKGROUNDED = "Application Backgrounded";
    private static final String ORDER_ID = "order_id";
    private static final String PRODUCTS = "products";
    private static final String CATEGORY = "category";
    private static final String QUANTITY = "quantity";
    private static final String PRICE = "price";
    private static final String ID = "id";
    // Convert below code to Set from List
    private static final Set<String> RESERVED_ECOMMERCE_PROPERTIES = new HashSet<>(Arrays.asList(ORDER_ID, PRODUCTS, CATEGORY, QUANTITY, PRICE, PRODUCT_ID));
    private AdobeDestinationConfig destinationConfig;

    private VideoAnalytics video;

    private static final Map<String, String> eventsMapping = new HashMap<String, String>() {
        {
            put("product viewed", "prodView");
            put("product list viewed", "prodView");
            put("product added", "scAdd");
            put("product removed", "scRemove");
            put("order completed", "purchase");
            put("cart viewed", "scView");
            put("checkout started", "scCheckout");
            put("cart opened", "scOpen");               // Cart Opened event is not the RudderStack standard ECommerce event
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
                (json, typeOfT, context) -> {
                    JsonObject jsonObject = json.getAsJsonObject();

                    JsonArray contextDataMaps =
                            (JsonArray) (jsonObject.get("contextDataMapping"));
                    Map<String, String> contextDataMap =
                            Utils.getMappedRudderEvents(contextDataMaps, false);

                    JsonArray rudderEventsToAdobeEventsMaps =
                            (JsonArray) (jsonObject.get("rudderEventsToAdobeEvents"));
                    Map<String, String> rudderEventsToAdobeEventsMap =
                            Utils.getMappedRudderEvents(rudderEventsToAdobeEventsMaps, false);

                    JsonArray videoEventsMaps =
                            (JsonArray) (jsonObject.get("eventsToTypes"));
                    Map<String, String> videoEventsMap = Utils.getMappedRudderEvents(videoEventsMaps, true);

                    return new AdobeDestinationConfig(
                            Utils.getStringFromJSON(jsonObject, "heartbeatTrackingServerUrl"),
                            contextDataMap,
                            rudderEventsToAdobeEventsMap,
                            videoEventsMap,
                            jsonObject.get("sslHeartbeat").getAsBoolean(),
                            Utils.getStringFromJSON(jsonObject, "contextDataPrefix"),
                            Utils.getStringFromJSON(jsonObject, "productIdentifier"),
                            rudderConfig.isTrackLifecycleEvents()
                    );
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
        MobileCore.setApplication(RudderClient.getApplication());
        List<Class<? extends Extension>> extensions = Arrays.asList(Lifecycle.EXTENSION, Analytics.EXTENSION, Identity.EXTENSION, Media.EXTENSION);
        MobileCore.registerExtensions(extensions, o -> RudderLogger.logDebug("AEP Mobile SDK is initialized"));

        //Debugger of Adobe Analytics
        if (rudderConfig.getLogLevel() != RudderLogger.RudderLogLevel.NONE) {
            MobileCore.setLogLevel(getAdobeLogLevel(rudderConfig.getLogLevel()));
            video.setDebugLogging(true);
        }

        // Refer Adobe docs: https://developer.adobe.com/client-sdks/documentation/mobile-core/lifecycle/
        RudderClient
                .getApplication()
                .registerActivityLifecycleCallbacks(
                        new Application.ActivityLifecycleCallbacks() {
                            @Override
                            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                                MobileCore.setApplication(RudderClient.getApplication());
                                RudderLogger.logVerbose("Config.setContext();");
                            }

                            @Override
                            public void onActivityResumed(Activity activity) {
                                MobileCore.lifecycleStart(null);
                                RudderLogger.logVerbose("Config.collectLifecycleData(" + activity + ");");
                            }

                            @Override
                            public void onActivityPaused(Activity activity) {
                                MobileCore.lifecyclePause();
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

    private LoggingMode getAdobeLogLevel(int rudderLogLevel) {
        if (rudderLogLevel >= RudderLogger.RudderLogLevel.VERBOSE) {
            return LoggingMode.VERBOSE;
        }
        if (rudderLogLevel >= RudderLogger.RudderLogLevel.INFO) {
            return LoggingMode.DEBUG;
        }
        if (rudderLogLevel >= RudderLogger.RudderLogLevel.WARN) {
            return LoggingMode.WARNING;
        }
        return LoggingMode.ERROR;

    }

    private void processRudderEvent(RudderMessage element) {
        String type = element.getType();
        if (type != null) {
            switch (type) {
                case MessageType.IDENTIFY:
                    String userId = element.getUserId();
                    if (!TextUtils.isEmpty(userId)) {
                        // Refer Adobe docs: https://developer.adobe.com/client-sdks/documentation/adobe-analytics/api-reference/#setvisitoridentifier
                        Analytics.setVisitorIdentifier(userId);
                        RudderLogger.logVerbose("Adobe Analytics Identify event is made: setVisitorIdentifier(" + userId + ");");
                    }
                    break;

                case MessageType.TRACK:
                    String eventName = element.getEventName();
                    if (eventName == null) {
                        RudderLogger.logDebug("Not sending event to Adobe, as event name is empty.");
                        return;
                    }

                    // If it is Video Analytics event
                    if (isVideoEvent(eventName)) {
                        video.track(getVideoEvent(eventName), element);
                        return;
                    }

                    // If it is E-Commerce event
                    if (eventsMapping.containsKey(eventName.toLowerCase())) {
                        if (isECommerceEventsAlsoMapped(eventName)) {
                            RudderLogger.logDebug("RudderStack currently does not support " +
                                    "mapping of ecommerce events to custom Adobe events.");
                            return;
                        }
                        handleEcommerce(eventsMapping.get(eventName.toLowerCase()), element);
                        return;
                    }

                    if (destinationConfig.isTrackLifecycleEvents && isTrackLifeCycleEvents(eventName)) {
                        MobileCore.trackAction(eventName, null);
                        return;
                    }

                    if (!isCustomTrackEventMapped(eventName)) {
                        RudderLogger.logDebug("Event must be either configured in Adobe and in the RudderStack setting, "
                                + "or it should be a reserved Adobe Ecommerce or Video event.");
                        return;
                    }

                    // Custom Track call
                    String rudderToAdobeEvents = Utils.getString(destinationConfig.rudderEventsToAdobeEvents.get(eventName));
                    Map<String, String> customTrackProperties = getCustomMappedAndExtraProperties(element);
                    if (!Utils.isEmpty(rudderToAdobeEvents)) {
                        MobileCore.trackAction(rudderToAdobeEvents, customTrackProperties);
                        RudderLogger.logVerbose("Adobe Analytics Track event is made: trackAction("
                                + rudderToAdobeEvents + ", " + customTrackProperties + ");");
                    }
                    break;

                case MessageType.SCREEN:
                    String screenName = element.getEventName();
                    if (screenName == null) {
                        RudderLogger.logDebug("Screen Name is empty!");
                        return;
                    }
                    Map<String, String> screenProperties = getCustomMappedAndExtraProperties(element);
                    MobileCore.trackState(screenName, screenProperties);
                    RudderLogger.logVerbose("Adobe Analytics Screen event is made: trackState("
                            + screenName + ", " + screenProperties + ");");
                    break;

                default:
                    RudderLogger.logDebug("MessageType is not specified or supported");
                    break;
            }
        }
    }

    private boolean isECommerceEventsAlsoMapped(String eventName) {
        return destinationConfig.rudderEventsToAdobeEvents != null
                && destinationConfig.rudderEventsToAdobeEvents.containsKey(eventName);
    }

    private boolean isCustomTrackEventMapped(String eventName) {
        return !(destinationConfig.rudderEventsToAdobeEvents == null
                || destinationConfig.rudderEventsToAdobeEvents.size() == 0
                || !destinationConfig.rudderEventsToAdobeEvents.containsKey(eventName));
    }

    private boolean isTrackLifeCycleEvents(String eventName) {
        switch (eventName) {
            case APPLICATION_INSTALLED:
            case APPLICATION_UPDATED:
            case APPLICATION_OPENED:
            case APPLICATION_BACKGROUNDED:
                return true;
            default:
                return false;
        }
    }

    private String getVideoEvent(String eventName) {
        return destinationConfig.videoEvents.get(eventName);
    }

    private boolean isVideoEvent(String eventName) {
        return destinationConfig.videoEvents.containsKey(eventName);
    }

    @Override
    public void reset() {
        // Refer here: https://developer.adobe.com/client-sdks/documentation/adobe-analytics/api-reference/#resetidentities
        MobileCore.resetIdentities();
        RudderLogger.logVerbose("Adobe Analytics Reset API call is made: MobileCore.resetIdentities().");
    }

    @Override
    public void flush() {
        // Refer here: https://developer.adobe.com/client-sdks/documentation/adobe-analytics/api-reference/#sendqueuedhits
        Analytics.sendQueuedHits();
        RudderLogger.logVerbose("Adobe Analytics Flush API call is made: Analytics.sendQueuedHits().");
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

    private void handleEcommerce(String eventName, RudderMessage element) {
        Map<String, String> contextData = new HashMap<>();
        contextData.put("&&events", Utils.getString(eventName));

        Map<String, Object> eventProperties = element.getProperties();
        if (!Utils.isEmpty(eventProperties)) {
            handleProducts(eventProperties, contextData);
            if (eventProperties.containsKey(ORDER_ID)) {
                contextData.put("purchaseid", Utils.getString(eventProperties.get(ORDER_ID)));
            }
        }

        Map<String, String> customMappedAndExtraProperties = getCustomMappedAndExtraProperties(element);
        if (!Utils.isEmpty(customMappedAndExtraProperties)) {
            contextData.putAll(customMappedAndExtraProperties);
        }

        // Track Call for E-Commerce event
        MobileCore.trackAction(eventName, contextData);
        RudderLogger.logVerbose("Adobe Analytics E-Commerce event is made: " + eventName + " and contextData is: " + contextData);
    }

    private Map<String, String> getCustomMappedAndExtraProperties(RudderMessage element) {
        Map<String, Object> eventProperties = element.getProperties();
        Map<String, String> contextData = new HashMap<>();
        List<String> CUSTOM_MAPPED_PROPERTIES = new ArrayList<>();

        // Handling the custom mapped properties
        if (!Utils.isEmpty(destinationConfig.contextData)) {
            for (String field : destinationConfig.contextData.keySet()) {
                if (RESERVED_ECOMMERCE_PROPERTIES.contains(field)) {
                    continue;
                }
                String mappedContextValue = Utils.getMappedContextValue(field, element);
                if (mappedContextValue != null) {
                    contextData.put(destinationConfig.contextData.get(field), mappedContextValue);
                    CUSTOM_MAPPED_PROPERTIES.add(field);
                }
            }
        }

        // Add prefix to all the remaining eventProperties eventName
        if(!Utils.isEmpty(eventProperties)) {
            for (String extraProperty : eventProperties.keySet()) {
                if (RESERVED_ECOMMERCE_PROPERTIES.contains(extraProperty) || CUSTOM_MAPPED_PROPERTIES.contains(extraProperty)) {
                    continue;
                }
                String propertyName = destinationConfig.contextDataPrefix + extraProperty;
                contextData.put(propertyName, Utils.getString(eventProperties.get(extraProperty)));
            }
        }
        return contextData;
    }

    private void handleProducts(Map<String, Object> eventProperties, Map<String, String> contextData) {
        String products = null;
        if (eventProperties.containsKey(PRODUCTS) && Utils.isProductIsList(eventProperties.get(PRODUCTS))) {
            try {
                products = getProductsArrayInStringFormat((List<Map<String, Object>>) eventProperties.get(PRODUCTS));
            } catch (JSONException e) {
                RudderLogger.logDebug("JSONException occurred while processing products.");
            }
        }

        // If product is not present, then look for 'category', 'quantity', 'price' and
        // 'productId' at the root level.
        else {
            try {
                products = getProductString(eventProperties);
            } catch (IllegalArgumentException e) {
                RudderLogger.logDebug("The Product Identifier configured on the dashboard must be "
                        + "present for each product to pass that product to Adobe Analytics.");
            }
        }

        if (!Utils.isEmpty(products)) {
            contextData.put("&&products", products);
        }
    }

    @VisibleForTesting
    String getProductsArrayInStringFormat(List<Map<String, Object>> products) throws JSONException {
        if (Utils.isEmpty(products)) {
            return null;
        }

        StringBuilder productsString = new StringBuilder();
        for (Map<String, Object> product : products) {
            try {
                String productString = getProductString(product);
                if (productsString.length() != 0) {
                    productsString.append(",");
                }
                productsString.append(productString);
            } catch (IllegalArgumentException e) {
                RudderLogger.logDebug("The Product Identifier configured on the dashboard must be "
                        + "present for each product to pass that product to Adobe Analytics.");
            }
        }
        return productsString.toString();
    }

    /**
     * Get the individual product in the format, as required by Adobe:
     * 'Category' + 'Product Name' + 'Quantity' + 'Price'
     * <a href="https://experienceleague.adobe.com/docs/analytics/implementation/vars/page-vars/products.html?lang=en#">
     * docs</a>
     *
     * @throws IllegalArgumentException if the product does not have an ID.
     */
    private String getProductString(Map<String, Object> product) {
        StringBuilder productString = new StringBuilder();

        if (product.containsKey(CATEGORY)) {
            String category = Utils.getString(product.get(CATEGORY));
            productString.append(category);
        }
        productString.append(";");

        String product_id = getProductId(product);
        if (!Utils.isEmpty(product_id)) {
            productString.append(product_id);
        } else {
            throw new IllegalArgumentException("Product id is not defined.");
        }
        productString.append(";");

        // Default to 1.
        int quantity = 1;
        if (product.containsKey(QUANTITY)) {
            String q = Utils.getString(product.get(QUANTITY));
            productString.append(q);
            if (q != null) {
                try {
                    quantity = Integer.parseInt(q);
                } catch (NumberFormatException e) {
                    RudderLogger.logDebug("Integer.parseInt(" + q + ") error!");
                }
            }
        }
        productString.append(";");

        // Default to 0.
        double price = 0.0;
        if (product.containsKey(PRICE)) {
            String p = Utils.getString(product.get(PRICE));
            if (p != null) {
                try {
                    price = Double.parseDouble(p);
                } catch (NumberFormatException e) {
                    RudderLogger.logDebug("Double.parseDouble(" + p + ") error!");
                }
            }
            price = price * quantity;
            productString.append(Utils.getString(price));
        }
        productString = truncateProductString(productString);
        return productString.toString();
    }

    /**
     * Sets the product ID using productIdentifier configured at the settings.
     * (supported values are <code>name</code>, <code>sku</code> and <code>id</code>.
     *
     * <p>Currently we do not allow to have products without IDs. Adobe Analytics allows to send an
     * extra product for merchandising evars and event serialization, as seen over here
     * <a href="https://experienceleague.adobe.com/docs/analytics/implementation/vars/page-vars/products.html?lang=en#">
     * docs</a>, but it is not well documented and does not conform RudderStack's spec.
     *
     * @param eventProduct Event's product.
     */
    @VisibleForTesting
    String getProductId(Map<String, Object> eventProduct) {
        if (destinationConfig.productIdentifier.equals(ID)) {
            if (eventProduct.containsKey(PRODUCT_ID)) {
                return Utils.getString(eventProduct.get(PRODUCT_ID));
            }
        }

        if (eventProduct.containsKey(destinationConfig.productIdentifier)) {
            RESERVED_ECOMMERCE_PROPERTIES.add(destinationConfig.productIdentifier);
            return Utils.getString(eventProduct.get(destinationConfig.productIdentifier));
        }

        return null;
    }

    private StringBuilder truncateProductString(StringBuilder productString) {
        for (int i = productString.length() - 1; i >= 0; i--) {
            if (productString.charAt(i) != ';') {
                return productString.delete(i + 1, productString.length());
            }
        }
        return productString.delete(0, productString.length());
    }
}