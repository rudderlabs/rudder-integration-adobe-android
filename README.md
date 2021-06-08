# What is RudderStack?

[RudderStack](https://rudderstack.com/) is a **customer data pipeline** tool for collecting, routing and processing data from your websites, apps, cloud tools, and data warehouse.

More information on RudderStack can be found [here](https://github.com/rudderlabs/rudder-server).

## Integrating Adobe Analytics for RudderStack's Android SDK

1. Add [Adobe Analytics](https://www.adobe.io/apis/experiencecloud/analytics.html) as a destination in the [RudderStack dashboard](https://app.rudderstack.com/) and define the app secret key.

2. Add the following `dependencies` to your `app/build.gradle` file as shown:

```groovy
implementation 'com.rudderstack.android.sdk:core:1.+'
implementation 'com.rudderstack.android.integration:adobe:1.0.0'
implementation 'com.google.code.gson:gson:2.8.6'

// Adobe dependencies
    implementation group: 'com.adobe.mobile', name: 'adobeMobileLibrary', version: '4.18.2'
```

5. Finally change the initialization of your `RudderClient` in your `Application` class:

```groovy
val rudderClient = RudderClient.getInstance(
    this,
    WRITE_KEY,
    RudderConfig.Builder()
        .withDataPlaneUrl(DATA_PLANE_URL)
        .withFactory(AdobeIntegrationFactory.FACTORY)
        .build()
)
```
## Information (Will be updated properly)

Configure the ADBMobile.json at [Adobe Mobile Services](https://mobilemarketing.adobe.com) and download file and save it in your app -> main -> assets (create the folder if it is not present)
Add any custom events or properties to dashboard.

## Send Events

Follow the steps from the [RudderStack Android SDK](https://github.com/rudderlabs/rudder-sdk-android).

## Contact Us

If you come across any issues while configuring or using this integration, please feel free to start a conversation on our [Slack](https://resources.rudderstack.com/join-rudderstack-slack) channel. We will be happy to help you.
