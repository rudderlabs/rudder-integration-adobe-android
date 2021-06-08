package com.rudderstack.android.integrations.adobe;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.rudderstack.android.sdk.core.MessageType;
import com.rudderstack.android.sdk.core.RudderClient;
import com.rudderstack.android.sdk.core.RudderConfig;
import com.rudderstack.android.sdk.core.RudderIntegration;
import com.rudderstack.android.sdk.core.RudderLogger;
import com.rudderstack.android.sdk.core.RudderMessage;



public class AdobeIntegrationFactory extends RudderIntegration<Void> {
    private static final String ADOBE_KEY = "ADOBE_KEY";
    
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

        Gson gson = new Gson();
        AdobeDestinationConfig destinationConfig = gson.fromJson(
                gson.toJson(config),
                AdobeDestinationConfig.class
        );
}

    private void processRudderEvent(RudderMessage element) {
        String type = element.getType();
        if (type != null) {
            switch (type) {

                case MessageType.TRACK:

                    break;

                case MessageType.SCREEN:
                   
                    break;

                default:
                    RudderLogger.logWarn("MessageType is not specified or supported");
                    break;
            }
        }
    }

    @Override
    public void reset() {
       
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
}