package com.rudderstack.android.integrations.adobe;

import java.util.Map;

public class AdobeDestinationConfig {
    final String heartbeatTrackingServerUrl;
    final Map<String, String> contextData;
    final Map<String, String> rudderEventsToAdobeEvents;
    final Map<String, String> videoEvents;
    final boolean ssl;
    final String contextDataPrefix;
    final String productIdentifier;
    final boolean isTrackLifecycleEvents;

    public AdobeDestinationConfig (
            String heartbeatTrackingServerUrl,
            Map<String, String> contextData,
            Map<String, String> rudderEventsToAdobeEvents,
            Map<String, String> videoEvents,
            boolean ssl,
            String contextDataPrefix,
            String productIdentifier,
            boolean isTrackLifecycleEvents) {
        this.contextData = contextData;
        this.heartbeatTrackingServerUrl = heartbeatTrackingServerUrl;
        this.rudderEventsToAdobeEvents = rudderEventsToAdobeEvents;
        this.videoEvents = videoEvents;
        this.ssl = ssl;
        this.productIdentifier = productIdentifier;
        this.isTrackLifecycleEvents = isTrackLifecycleEvents;

        // "a." is reserved by Adobe Analytics
        if (contextDataPrefix == null || contextDataPrefix.equals("a.")) {
            this.contextDataPrefix = "";
        } else {
            this.contextDataPrefix = contextDataPrefix;
        }
    }
}