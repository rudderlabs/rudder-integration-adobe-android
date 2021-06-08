package com.rudderstack.android.sample.kotlin

import android.app.Application
import com.rudderstack.android.integrations.adobe.AdobeIntegrationFactory
import com.rudderstack.android.sdk.core.RudderClient
import com.rudderstack.android.sdk.core.RudderConfig
import com.rudderstack.android.sdk.core.RudderLogger

class MainApplication : Application() {
    companion object {
        private const val WRITE_KEY = "1qnO5R5NMgI5oRHToPsWmau2Jcz"
        private const val DATA_PLANE_URL = "https://c0bed7a19cb3.ngrok.io"
        private const val CONTROL_PLANE_URL = "https://72253bc229e8.ngrok.io"
        lateinit var rudderClient: RudderClient
    }

    override fun onCreate() {
        super.onCreate()
        rudderClient = RudderClient.getInstance(
            this,
            WRITE_KEY,
            RudderConfig.Builder()
                    .withDataPlaneUrl(DATA_PLANE_URL)
                    //.withControlPlaneUrl(CONTROL_PLANE_URL)
                    .withFactory(AdobeIntegrationFactory.FACTORY)
                    .withLogLevel(RudderLogger.RudderLogLevel.VERBOSE)
                    .withTrackLifecycleEvents(false)
                    .build()
        )
    }
}