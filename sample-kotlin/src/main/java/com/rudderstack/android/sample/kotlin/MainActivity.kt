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
        videoEvents()

        // Video Events:
//         trackVideoPlaybackStarted() Video event must be called before any other Video events.
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

        /*
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
        */
    }

    private fun trackVideoPlaybackStarted() {
        MainApplication.rudderClient.track("Playback Started",
            RudderProperty()
                .putValue("assetId", "123333")
                .putValue("program", "Flash")       //show
                .putValue("season", "Season-3")
                .putValue("episode", "Episode-5")
                .putValue("genre", "Sci-Fi")
                .putValue("channel", "Amaz")        //NETWORK
                .putValue("airdate", "airdate-28june")
                .putValue("publisher", "publisher3")     //ORIGINATOR
                .putValue("rating", "rating6")
                .putValue("title", "Flash_Season_3")     //Content Name
                .putValue("contentAssetId", "Flash_Season_33")      //Content
                .putValue("totalLength", 60)    //Content Length
                .putValue("livestream", false)   //Content Type (default VOD)
                .putValue("video_player", "html")     //Content Player Name

                //Custom Properties
                .putValue("full_screen", false)
                .putValue("framerate", "598")
                .putValue("ad_enabled", false)
                .putValue("quality", "720p")
                .putValue("bitrate", 144)
                .putValue("sound", 10)
        )
    }

    // Demo video events method - which calls different sets of Video methods in sequence
    private fun videoEvents() {

        // trackVideoPlaybackStarted() method should be run first before any other Video calls.
        trackVideoPlaybackStarted()

        /*
        // Case 1
        trackVideoQualityUpdated()
        //Thread.sleep(100000)
        trackVideoPlaybackCompleted()
        */

        /*
        // Case 3: playback with skipped ads
        trackVideoQualityUpdated()
        //Pre-Roll
        trackVideoAdBreakStarted()
        trackVideoAdStarted()
        trackVideoPlaybackResumed()
        trackVideoAdSkipped()
        trackVideoPlaybackCompleted()
        */

        /*
        // Case 4:
        trackVideoQualityUpdated()
        trackVideoContentStarted()
        trackVideoContentComplete()
        trackVideoPlaybackCompleted()
        */

        /*
        // Case 5: playback with buffering
        trackVideoQualityUpdated()
        trackVideoPlaybackResumed()
        trackVideoBufferStarted()
        trackVideoBufferComplete()
        trackVideoPlaybackCompleted()
        */

        /*
        // Case 6: seeking in the main content
        trackVideoQualityUpdated()
        trackVideoPlaybackResumed()
        trackVideoSeekStarted()
        trackVideoSeekComplete()
        trackVideoPlaybackCompleted()
        */

        /*
        // Case 7: playback with skipped ads
        trackVideoQualityUpdated()
        //Pre-Roll
        trackVideoAdBreakStarted()
        trackVideoAdStarted()
        trackVideoAdCompleted()
        trackVideoAdBreakCompleted()
        //Mid-Roll
        trackVideoAdBreakStarted()
        trackVideoAdStarted()
        trackVideoAdCompleted()
        trackVideoAdBreakCompleted()
        trackVideoPlaybackCompleted()
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
                .putValue("title", "You Win or You Die3")         //SP
                .putValue("contentAssetId", "1244444")         //SP
                .putValue("totalLength", 120.0)         //SP
                .putValue("startTime", 13.0)             //SP
                .putValue("indexPosition", 2L)         //SP
                .putValue("position", 50)
                .putValue("season", "2")         //SP
                .putValue("program", "GoT")         //SP
                .putValue("episode", "8")         //SP
                .putValue("genre", "Sci-fi")         //SP
                .putValue("channel", "HBO2")         //SP
                .putValue("airdate", "2017")         //SP
                .putValue("publisher", "HBO22")
                .putValue("rating", "5star")         //SP
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
        MainApplication.rudderClient.track("Seek Started")
    }

    fun trackVideoSeekComplete() {
        MainApplication.rudderClient.track("Seek Completed",
            RudderProperty()
                .putValue("seekPosition", 35L)
        )
    }

    fun trackVideoAdBreakStarted() {
        MainApplication.rudderClient.track("Ad Break Started",
            RudderProperty()
                .putValue("title", "TV Commercial23")
                .putValue("startTime", 13.0)
                .putValue("indexPosition", 3L)
        )
    }

    fun trackVideoAdBreakCompleted() {
        MainApplication.rudderClient.track("Ad Break Completed")
    }

    fun trackVideoAdStarted() {
        MainApplication.rudderClient.track("Ad Start",
            RudderProperty()
                .putValue("title", "TV Commercial44")
                .putValue("assetId", "123333")
                .putValue("totalLength", 12.0)
                .putValue("indexPosition", 2L)
                .putValue("publisher", "Lexus23")     //Standard Properties(SP) - KEY: Advertiser
        )
    }

    fun trackVideoAdSkipped() {
        MainApplication.rudderClient.track("Ad Skipped")
    }

    fun trackVideoAdCompleted() {
        MainApplication.rudderClient.track("Ad Completed")
    }

    fun trackVideoPlaybackInterrupted() {
        MainApplication.rudderClient.track("Playback Interrupted")
    }

    fun trackVideoQualityUpdated() {
        MainApplication.rudderClient.track("Quality Updated",
            RudderProperty()
                .putValue("bitrate", 246)
                .putValue("startupTime", 20)
                .putValue("fps", 140)
                .putValue("droppedFrames", 2)
        )
    }
}
