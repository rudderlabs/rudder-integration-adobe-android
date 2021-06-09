package com.rudderstack.android.integrations.adobe;

import java.util.Map;

public class AdobeDestinationConfig {
    final String heartbeatTrackingServerUrl;
    final Map<String, Object> contextData;
    final Map<String, Object> rudderEventsToAdobeEvents;
    final Map<String, Object> videoEvents;
    final boolean ssl;

    public AdobeDestinationConfig
            (String heartbeatTrackingServerUrl,
             Map<String, Object> contextData,
             Map<String, Object> rudderEventsToAdobeEvents,
             Map<String, Object> videoEvents,
             boolean ssl) {
        this.contextData = contextData;
        this.heartbeatTrackingServerUrl = heartbeatTrackingServerUrl;
        this.rudderEventsToAdobeEvents = rudderEventsToAdobeEvents;
        this.videoEvents = videoEvents;
        this.ssl = ssl;

    }
}