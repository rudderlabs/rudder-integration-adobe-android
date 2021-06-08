package com.rudderstack.android.integrations.adobe;

import java.util.Map;

public class AdobeDestinationConfig {
    final String url;
    final Map<String, Object> contextDataVariables;

    public AdobeDestinationConfig(String url, Map<String, Object> eventMap) {
        this.contextDataVariables = eventMap;
        this.url = url;
    }
}