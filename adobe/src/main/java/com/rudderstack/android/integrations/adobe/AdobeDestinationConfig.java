package com.rudderstack.android.integrations.adobe;

import java.util.Map;

public class AdobeDestinationConfig {
    final String heartbeatTrackingServerUrl;
    final Map<String, Object> contextData;
    final Map<String, Object> rudderEventsToAdobeEvents;

    public AdobeDestinationConfig(String heartbeatTrackingServerUrl, Map<String, Object> eventMap, Map<String, Object> rudderEventsToAdobeEventsMap) {
        this.contextData = eventMap;
        this.heartbeatTrackingServerUrl = heartbeatTrackingServerUrl;
        this.rudderEventsToAdobeEvents = rudderEventsToAdobeEventsMap;
    }
}