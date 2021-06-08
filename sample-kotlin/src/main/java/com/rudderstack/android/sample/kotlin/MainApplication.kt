package com.rudderstack.android.sample.kotlin

import android.app.Application
import com.rudderstack.android.integrations.adobe.AdobeIntegrationFactory
import com.rudderstack.android.sdk.core.RudderClient
import com.rudderstack.android.sdk.core.RudderConfig
import com.rudderstack.android.sdk.core.RudderLogger

class MainApplication : Application() {
    companion object {
        private const val WRITE_KEY = "1s4gjAsjU2O41t6JwCGsCgZf6sg"
        private const val DATA_PLANE_URL = "https://a078f9a54e13.ngrok.io"
        private const val CONTROL_PLANE_URL = "https://84209663c9ea.ngrok.io"
        lateinit var rudderClient: RudderClient
    }

    override fun onCreate() {
        super.onCreate()
        rudderClient = RudderClient.getInstance(
            this,
            WRITE_KEY,
            RudderConfig.Builder()
                    .withDataPlaneUrl(DATA_PLANE_URL)
                    .withControlPlaneUrl(CONTROL_PLANE_URL)
                    .withFactory(AdobeIntegrationFactory.FACTORY)
                    .withLogLevel(RudderLogger.RudderLogLevel.VERBOSE)
                    .withTrackLifecycleEvents(false)
                    .build()
        )
    }
}