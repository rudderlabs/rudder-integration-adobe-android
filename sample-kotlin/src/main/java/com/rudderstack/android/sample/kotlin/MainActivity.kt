package com.rudderstack.android.sample.kotlin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.adobe.primetime.va.simple.MediaHeartbeat
import com.adobe.primetime.va.simple.MediaObject
import com.rudderlabs.android.sample.kotlin.R
import com.rudderstack.android.sdk.core.RudderProperty
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MainApplication.rudderClient.identify("Testing4")

        // Video Events:
        // trackVideoPlaybackStarted() Video event must be run first before any other Video calls.
//        trackVideoPlaybackStarted()
//        trackVideoPlaybackPaused()
//        trackVideoPlaybackResumed()
//        trackVideoContentStarted()
//        trackVideoContentComplete()
//        trackVideoPlaybackCompleted()
//        trackVideoBufferStarted()
//        trackVideoBufferComplete()
//        trackVideoSeekStarted()
//        trackVideoSeekComplete()
//        trackVideoAdBreakStarted()
//        trackVideoAdBreakCompleted()
//        trackVideoAdStarted()
//        trackVideoAdSkipped()
//        trackVideoAdCompleted()
//        trackVideoPlaybackInterrupted()
//        trackVideoQualityUpdated()



//        Standard E-Commerce Events:
//
//        val answer1 = JSONObject("""{"name":"test name", "age":25}""")
//        val map= mapOf("product_id" to "pro1", "name" to "monopoly", "price" to 1000)
//        val map1= mapOf("product_id" to "pro2", "name" to "games", "price" to 2000)
//        val list= listOf(map, map1);
//
//        MainApplication.rudderClient.track("Order Completed",
//                RudderProperty()
//                    .putValue("order_id", "order123")
//                    //.putValue("product_id","pro123")
//                    .putValue("checkout_id", "check123")
//                    .putValue("name", "test")
//                    .putValue("custom_1", "string")
//                    .putValue("custom_2", 1230)
//                    .putValue("custom_3", true)
//                    .putValue("custom_4", answer1)
//                    .putValue("revenue", 8.99)
//                    .putValue("quantity", 2)
//                    .putValue("currency", "USD")
//                    .putValue("product_id","pro1")
////                   Products specs at the root
////                    .putValue("name", "monopoly")
////                    .putValue("price", 1000)
////                    .putValue("quantity",20)
//                    .putValue("products", list)
//        )
//
//        MainApplication.rudderClient.track("Checkout Started",
//                RudderProperty()
//                        .putValue("order_id", "order123")
//                        .putValue("product_id","pro123")
//                        .putValue("name", "test")
//                        .putValue("custom_1", "string")
//                        .putValue("custom_2", 1230)
//                        .putValue("custom_3", true)
//                        .putValue("custom_4", answer1)
//                        .putValue("revenue", 8.99)
//                        .putValue("currency", "USD")
//                        .putValue("products", list)
//        )
//
//        Track call with Standard event name and Standard  property
//        payload for Ecommerce Track event
//        val payload = RudderProperty()
//        val productsArray = JSONArray()
//        payload.put("order_id", 1234)
//        payload.put("affiliation", "Apple Store")
//        payload.put("value", 20)
//        payload.put("revenue", "15.00")
//        payload.put("currency", "USD")
//        payload.put("products", productsArray)
//        val product1 = JSONObject()
//        product1.put("product_id", 123.87)
//        product1.put("sku", "G-32")
//        product1.put("name", "Monopoly")
//        product1.put("price", 14)
//        product1.put("quantity", 1)
//        product1.put("category", "Games")
//        product1.put("url", "https://www.website.com/product/path")
//        product1.put("image_url", "https://www.website.com/product/path.jpg")
//        val product2 = JSONObject()
//        product2.put("productId", "345")
//        product2.put("sku", "F-32")
//        product2.put("name", "UNO")
//        product2.put("price", 3.45)
//        product2.put("quantity", 2)
//        product2.put("category", "Games")
//        product2.put("url", "https://www.website.com/product/path")
//        product2.put("image_url", "https://www.website.com/product/path.jpg")
//        productsArray.put(product1)
//        productsArray.put(product2)
//        MainApplication.rudderClient.track(
//                "order completed",
//                payload
//                )
//
//        val payload = RudderProperty()
//        val productsArray = JSONArray()
//        payload.put("products", productsArray)
//        val product1 = JSONObject()
//        product1.put("sku", "G-32")
//        product1.put("productId", 123)
//        product1.put("name", "Monopoly")
//        product1.put("price", 14)
//        product1.put("quantity", 1)
//        productsArray.put(product1)
//
//        MainApplication.rudderClient.track(
//                "order completed",
//                payload
//        )
//
//        MainApplication.rudderClient.track(
//                "product removed",
//                payload
//        )
//        MainApplication.rudderClient.track(
//            "cart viewed",
//            payload
//        )
//
//        MainApplication.rudderClient.track(
//                "Product Added",
//                RudderProperty()
//                        .putValue("product_id", "product_001")
//        )
//
//        MainApplication.rudderClient.track("checkout started",
//                RudderProperty()
//                        .putValue("currency", "IND"))
//
//        MainApplication.rudderClient.track("product added",
//                RudderProperty()
//                        .putValue("name","Gold")
//                        .putValue("productId","678")
//                        .putValue("quantity","678.87"))
//
//        MainApplication.rudderClient.track("product viewed",
//            RudderProperty()
//                .putValue("rating", 5))
//
//        Custom Track Events - (Mapped at Adobe):
//
//        MainApplication.rudderClient.track("myapp.ActionName",
//            RudderProperty()
//                .putValue("myapp.social.SocialSource", "Facebook")
//        );
//
//        MainApplication.rudderClient.track("Track in MainActivity",
//                RudderProperty().putValue("val", Date()))
//        Tracker.sendEvent(Tracker.Event("Sample Kotlin Date")
//                .addCustom("details", "eve 4")
//                .addCustom("value", Date())
//        )
//
//        Screen Call:
//
//        MainApplication.rudderClient.screen("screen_call")

    }

    private fun trackVideoPlaybackStarted() {
        MainApplication.rudderClient.track("Playback Started",
            RudderProperty()
                .putValue("assetId", "12345")
                .putValue("program", "TMKOC2")       //show
                .putValue("season", "Grand Finale season2")
                .putValue("episode", "Last episode2")
                .putValue("genre", "Comedy genre2")
                .putValue("channel", "Channel name2")        //NETWORK
                .putValue("airdate", "airdate 15june")
                .putValue("publisher", "publisher2")     //ORIGINATOR
                .putValue("rating", "rating5")
                .putValue("title", "The name of the media2")     //Content Name
                .putValue("contentAssetId", "The unique identifier for the media2")      //Content
                .putValue("totalLength", 150)    //Content Length
                .putValue("livestream", true)   //Content Type (default VOD)
                //.putValue("position", 0)        // ContentStarted Event
                .putValue("total_length", 392)  //Content Length
                .putValue("videoPlayer", "twitch")     //Content Player Name

                //Custom Properties
                .putValue("full_screen", false)
                .putValue("framerate", "twitch2")
                .putValue("ad_enabled", true)
                .putValue("quality", "hd1080")
                .putValue("bitrate", 100)
                .putValue("sound", 8)
                .putValue("ovp", "apple_tv")
        )
    }

    // Demo video events method - which calls different sets of Video methods in sequence
    private fun videoEvents() {

        // trackVideoPlaybackStarted() method should be run first before any other Video calls.
//        trackVideoPlaybackStarted()

        /*
        // Case 1
        trackVideoQualityUpdated()
        //Thread.sleep(100000)
        MainApplication.rudderClient.track("Playback Completed")
        */

        /*
        // Case 2: playback with pre-roll ads
        trackVideoQualityUpdated()
        Thread.sleep(5000)
        //Pre-Roll
        trackVideoAdBreakStarted()
        trackVideoAdStarted()
        //trackVideoPlaybackResumed()
        Thread.sleep(30000)
        trackVideoAdCompleted()
        trackVideoAdBreakCompleted()
        Thread.sleep(20000)
        //Mid-roll
        //trackVideoAdBreakStarted():
        MainApplication.rudderClient.track("Ad Break Started",
            RudderProperty()
                .putValue("title", "Car Commercial22") // Should this be pre-roll, mid-roll or post-roll instead?
                .putValue("startTime", 30.0)
                .putValue("indexPosition", 2L)
                .putValue("contextValue", "value")
        )
        trackVideoPlaybackStarted()
        MainApplication.rudderClient.track("Ad Start",
            RudderProperty()
                .putValue("title", "Car Commercial22")
                .putValue("assetId", "123")
                .putValue("totalLength", 10.0)
                .putValue("indexPosition", 2L)
                .putValue("publisher", "Lexus2")
                .putValue("extra", "extra value")
        )
        Thread.sleep(20000)
//        trackVideoAdCompleted()
        trackVideoAdBreakCompleted()
        Thread.sleep(40000)
        //Post-roll
        MainApplication.rudderClient.track("Ad Break Started",
            RudderProperty()
                .putValue("title", "Car Commercial32") // Should this be pre-roll, mid-roll or post-roll instead?
                .putValue("startTime", 40.0)
                .putValue("indexPosition", 3L)
                .putValue("contextValue", "value")
        )
        MainApplication.rudderClient.track("Ad Start",
            RudderProperty()
                .putValue("title", "Car Commercial32")
                .putValue("assetId", "123")
                .putValue("totalLength", 10.0)
                .putValue("indexPosition", 3L)
                .putValue("publisher", "Lexus2")
                .putValue("extra", "extra value")
        )
        //Thread.sleep(20000)
        trackVideoAdCompleted()
        trackVideoAdBreakCompleted()
        MainApplication.rudderClient.track("Playback Completed")
        */

        /*
        // Case 3: playback with skipped ads
        trackVideoQualityUpdated()
        Thread.sleep(5000)
        //Pre-Roll
        trackVideoAdBreakStarted()
        trackVideoAdStarted()
        Thread.sleep(10000)
        trackVideoPlaybackResumed()
        Thread.sleep(30000)
        trackVideoAdSkipped()
        MainApplication.rudderClient.track("Playback Completed")
        */

        /*
        // Case 4: playback with skipped ads
        trackVideoQualityUpdated()
        trackVideoPlaybackResumed()
        Thread.sleep(20000)
        trackVideoContentStarted()
        Thread.sleep(30000)
        trackVideoContentComplete()
        Thread.sleep(60000)
        MainApplication.rudderClient.track("Playback Completed")
        */

        /*
        // Case 5: playback with buffering
        trackVideoQualityUpdated()
        trackVideoPlaybackResumed()
        Thread.sleep(15000)
        trackVideoBufferStarted()
        Thread.sleep(15000)
        trackVideoBufferComplete()
        Thread.sleep(30000)
        MainApplication.rudderClient.track("Playback Completed")
        */

        /*
        // Case 6: seeking in the main content
        trackVideoQualityUpdated()
        trackVideoPlaybackResumed()
        Thread.sleep(15000)
        trackVideoSeekStarted()
        Thread.sleep(25000)
        trackVideoSeekComplete()
        Thread.sleep(30000)
        MainApplication.rudderClient.track("Playback Completed")
        */

        /*
        // Case 7: playback with skipped ads
        trackVideoQualityUpdated()
//        Thread.sleep(5000)
        //Pre-Roll
        trackVideoAdBreakStarted()
        trackVideoAdStarted()
//        Thread.sleep(10000)
        trackVideoAdCompleted()
        trackVideoAdBreakCompleted()
        MainApplication.rudderClient.track("Playback Completed")
        */

    }

    private fun trackVideoPlaybackPaused() {
        MainApplication.rudderClient.track("Playback Paused")
    }

    private fun trackVideoPlaybackResumed() {
        MainApplication.rudderClient.track("Playback Resumed")
    }

    fun trackVideoContentStarted() {
        MainApplication.rudderClient.track("Content Start",
            RudderProperty()
                .putValue("title", "You Win or You Die2")         //SP
                .putValue("contentAssetId", "12345")         //SP
                .putValue("totalLength", 200.0)         //SP
                .putValue("startTime", 10.0)             //SP
                .putValue("indexPosition", 1L)         //SP
                .putValue("position", 35)
                .putValue("season", "1")         //SP
                .putValue("program", "Game of Thrones")         //SP
                .putValue("episode", "7")         //SP
                .putValue("genre", "fantasy")         //SP
                .putValue("channel", "HBO")         //SP
                .putValue("airdate", "2011")         //SP
                .putValue("publisher", "HBO")
                .putValue("rating", "MA")         //SP
        )
    }

    fun trackVideoContentComplete() {
        MainApplication.rudderClient.track("Content Complete")
    }

    fun trackVideoPlaybackCompleted() {
        MainApplication.rudderClient.track("Playback Completed")
    }

    fun trackVideoBufferStarted() {
        MainApplication.rudderClient.track("Buffer Started")
    }

    fun trackVideoBufferComplete() {
        MainApplication.rudderClient.track("Buffer Completed")
    }

    fun trackVideoSeekStarted() {
        MainApplication.rudderClient.track("Seek Started",
            RudderProperty()
                .putValue("seekPosition", null)
        )
    }

    fun trackVideoSeekComplete() {
        MainApplication.rudderClient.track("Seek Completed",
            RudderProperty()
                .putValue("seekPosition", 50L)
        )
    }

    fun trackVideoAdBreakStarted() {
        MainApplication.rudderClient.track("Ad Break Started",
            RudderProperty()
                .putValue("title", "TV Commercial2") // Should this be pre-roll, mid-roll or post-roll instead?
                .putValue("startTime", 10.0)
                .putValue("indexPosition", 2L)
        )
    }

    fun trackVideoAdBreakCompleted() {
        MainApplication.rudderClient.track("Ad Break Completed")
    }

    fun trackVideoAdStarted() {
        MainApplication.rudderClient.track("Ad Start",
            RudderProperty()
                .putValue("title", "TV Commercial")
                .putValue("assetId", "12356")
                .putValue("totalLength", 20.0)
                .putValue("indexPosition", 1L)
                .putValue("publisher", "Lexus")     //Standard Properties(SP) - KEY: Advertiser
                .putValue("extra", "extra value")
        )
    }

    fun trackVideoAdSkipped() {
        MainApplication.rudderClient.track("Ad Skipped")
    }

    fun trackVideoAdCompleted() {
        MainApplication.rudderClient.track("Ad Completed")
    }

    @Throws(Exception::class)
    fun trackVideoPlaybackInterrupted() {
        MainApplication.rudderClient.track("Playback Interrupted")
    }

    fun trackVideoQualityUpdated() {
        MainApplication.rudderClient.track("Quality Updated",
            RudderProperty()
                .putValue("bitrate", 12000)
                .putValue("startupTime", 1)
                .putValue("fps", 50)
                .putValue("droppedFrames", 1)
        )
    }
}
