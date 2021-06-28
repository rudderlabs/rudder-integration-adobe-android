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

//        MainApplication.rudderClient.identify("Testing4")

        // Video Events:
        // trackVideoPlaybackStarted() Video event must be called before any other Video events.
        trackVideoPlaybackStarted()
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

        //Test Case:
        //String Identify Call
        MainApplication.rudderClient.identify("AdobeTesting")

        //Screen Call:
//        MainApplication.rudderClient.screen("screen_call",
//            RudderProperty()
//                    //Prefix + extraProperties
//                .putValue("1","Custom Track call 1")    //Mapping to adobe as: custom_1
//                .putValue("2",false)
//        )

        //Custom track with no properties
//        MainApplication.rudderClient.track("Custom Track",    // 'Custom Track' is mapped at RS dashboard to 'custom_track_call'
//            RudderProperty()
//                    //Prefix + extraProperties
//                .putValue("1","Custom Track call 1")    //Mapping to adobe as: custom_1
//                .putValue("2","")
//        )

        //Custom track with 'custom mapped properties' & 'prefix + extraProperties' (prefix is set to: "custom_")
//        MainApplication.rudderClient.track("Custom Track",    // 'Custom Track' is mapped at RS dashboard to 'custom_track_call'
//            RudderProperty()
//                //Prefix + extraProperties
//                .putValue("1","Custom Track call 99")    //Mapping to adobe as: custom_1
//                .putValue("2","5678")                       //Mapping to adobe as: custom_2 (As it is empty, Adobe discarded while sending it)
//                .putValue("3",1555566667777)               //Mapping to adobe as: custom_3
//                .putValue("4",200077.56)                    //Mapping to adobe as: custom_4
//                //Custom mapped properties
//                .putValue("curr","INR")                 // curr -> currency
//                .putValue("rat",80)                     // rat -> rating
//                .putValue("URL","https:example_custom.com")    // URL -> url
//        );

        //E-Commerce:
        //E-Commerce track call without any properties:
//        MainApplication.rudderClient.track("Product Added")     //scAdd

//        //E-Commerce track with 'custom mapped properties' & 'prefix + extraProperties' (prefix is set to: "custom_")
//        //with only product Identifier set as: "ID/SKU/Name"
//        MainApplication.rudderClient.track("Product Added",
//            RudderProperty()
//                //Prefix + extraProperties
//                .putValue("1","Custom Track call 2")    //Mapping to adobe as: custom_1
//                .putValue("2",false)                       //Mapping to adobe as: custom_2
//                .putValue("3",12345622)               //Mapping to adobe as: custom_3
//                .putValue("4",20222.56)                    //Mapping to adobe as: custom_4
//                //Custom mapped properties
//                .putValue("curr","INR")                 // curr -> currency
//                .putValue("rat",68)                     // rat -> rating
//                .putValue("URL","https:example2.com")    // URL -> url
////                .putValue("id",12.45)                      // ProductIdentifier is set to: id
////                .putValue("name","apple")                       // ProductIdentifier is set to: name
////                .putValue("sku","etc")                  // ProductIdentifier is set to: sku
//        );

        val answer1 = JSONObject("""{"name":"RStest_name2", "age":27}""")
        val map= mapOf("product_id" to "RSpro5", "name" to "RSmonopoly5", "price" to 5000.2, "quantity" to "200", "category" to "RSCat5")
        val map1= mapOf("product_id" to "pro5", "name" to "games5", "price" to "8000.20","quantity" to "700", "category" to "RSCat25")
        //Special case where productIdentifier is missing:
//        val map1= mapOf("price" to "8000.20","quantity" to "700", "category" to "RSCat25")
        val list= listOf(map, map1);
        //E-Commerce track with 'custom mapped properties' & 'prefix + extraProperties' (prefix is set to: "custom_")
        //with products.
//        MainApplication.rudderClient.track("Product Removed",       //scRemove
//            RudderProperty()
//                //Prefix + extraProperties
//                .putValue("1","Custom Track call 2")    //Mapping to adobe as: custom_1
//                .putValue("2",answer1)                       //Mapping to adobe as: custom_2  - value is: false and answer1
//                .putValue("3",12345622)               //Mapping to adobe as: custom_3
//                .putValue("4",20222.56)                    //Mapping to adobe as: custom_4
//                //Custom mapped properties
//                .putValue("curr","INR")                 // curr -> currency
//                .putValue("rat",68)                     // rat -> rating
//                .putValue("URL","https:example2.com")    // URL -> url
//                //Product at root
////                .putValue("id",12.45)                      // ProductIdentifier is set to: id
////                .putValue("name","apple")                       // ProductIdentifier is set to: name
////                .putValue("sku","etc")                  // ProductIdentifier is set to: sku
////                .putValue("category", "RSBigBasket")
////                .putValue("quantity",50)
////                .putValue("price",500)
////                .putValue("orderId", "1001")
//                .putValue("products",list)
//        );

        //E-Commerce track with 'custom mapped properties' & 'prefix + extraProperties' (prefix is set to: "custom_")
        //with products.
//        MainApplication.rudderClient.track("Cart Viewed",       //scView
//            RudderProperty()
//                //Prefix + extraProperties
//                .putValue("1","Custom Track call 3")    //Mapping to adobe as: custom_1
//                .putValue("2",answer1)                       //Mapping to adobe as: custom_2  - value is: false and answer1
//                .putValue("3",123323344)               //Mapping to adobe as: custom_3
//                .putValue("4",2022345.56)                    //Mapping to adobe as: custom_4
//                //Custom mapped properties
//                .putValue("curr","INR")                 // curr -> currency
//                .putValue("rat",69)                     // rat -> rating
//                .putValue("URL","https:example3.com")    // URL -> url
//                //Products
//                .putValue("products",list)
//        );

//        //E-Commerce track with 'custom mapped properties' & 'prefix + extraProperties' (prefix is set to: "custom_")
//        //with products.
//        MainApplication.rudderClient.track("checkout started",       //scCheckout
//            RudderProperty()
//                //Prefix + extraProperties
//                .putValue("1","Custom Track call 4")    //Mapping to adobe as: custom_1
//                .putValue("2",answer1)                       //Mapping to adobe as: custom_2  - value is: false and answer1
//                .putValue("3",12332334455)               //Mapping to adobe as: custom_3
//                .putValue("4",202231222.56)                    //Mapping to adobe as: custom_4
//                //Custom mapped properties
//                .putValue("curr","USD")                 // curr -> currency
//                .putValue("rat",74)                     // rat -> rating
//                .putValue("URL","https:example3.com")    // URL -> url
//                //Products
//                .putValue("products",list)
//        );

//        //E-Commerce track with 'custom mapped properties' & 'prefix + extraProperties' (prefix is set to: "custom_")
//        //with products.
//        MainApplication.rudderClient.track("order completed",       //purchase
//            RudderProperty()
//                //Prefix + extraProperties
//                .putValue("1","Custom Track call 4")    //Mapping to adobe as: custom_1
//                .putValue("2",answer1)                       //Mapping to adobe as: custom_2  - value is: false and answer1
//                .putValue("3",1233233778899)               //Mapping to adobe as: custom_3
//                .putValue("4",202000.56)                    //Mapping to adobe as: custom_4
//                //Custom mapped properties
//                .putValue("curr","USD")                 // curr -> currency
//                .putValue("rat",75)                     // rat -> rating
//                .putValue("URL","https:example4.com")    // URL -> url
//                //Products
//                .putValue("products",list)
//        );

        //E-Commerce track with 'custom mapped properties' & 'prefix + extraProperties' (prefix is set to: "custom_")
        //with products.
//        MainApplication.rudderClient.track("product viewed",       //prodView
//            RudderProperty()
//                //Prefix + extraProperties
//                .putValue("1","Custom Track call 5")    //Mapping to adobe as: custom_1
//                .putValue("2",answer1)                       //Mapping to adobe as: custom_2  - value is: false and answer1
//                .putValue("3",1237774444478899)               //Mapping to adobe as: custom_3
//                .putValue("4",2020222666.56)                    //Mapping to adobe as: custom_4
//                .putValue("orderId" , "3001")                     //Mapped to adobe as: purchaseId
//                //Custom mapped properties
//                .putValue("curr","USD")                 // curr -> currency
//                .putValue("rat",76)                     // rat -> rating
//                .putValue("URL","https:example5.com")    // URL -> url
//                //Products
//                .putValue("products",list)
//        )

        //Empty E-commerce events
//        MainApplication.rudderClient.track("Product Removed")

        //------------------------------------E-Commerce event completed --------------------------------------------------------------------------------
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
