package com.rudderstack.android.sample.kotlin

import com.rudderstack.android.sdk.core.RudderProperty

class VideoEvents {

    fun makeVideoEvents() {
        trackVideoPlaybackStarted() //Video event must be called before any other Video events.
        trackVideoPlaybackPaused()
        trackVideoPlaybackResumed()
        trackVideoContentStarted()
        trackVideoContentComplete()
        trackVideoPlaybackCompleted()
        trackVideoBufferStarted()
        trackVideoBufferComplete()
        trackVideoSeekStarted()
        trackVideoSeekComplete()
        trackVideoAdBreakStarted()
        trackVideoAdBreakCompleted()
        trackVideoAdStarted()
        trackVideoAdSkipped()
        trackVideoAdCompleted()
        trackVideoPlaybackInterrupted()
        trackVideoQualityUpdated()
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
        // Case 2: playback with skipped ads
        trackVideoQualityUpdated()
        //Pre-Roll
        trackVideoAdBreakStarted()
        trackVideoAdStarted()
        trackVideoPlaybackResumed()
        trackVideoAdSkipped()
        trackVideoPlaybackCompleted()
        */

        /*
        // Case 3:
        trackVideoQualityUpdated()
        trackVideoContentStarted()
        trackVideoContentComplete()
        trackVideoPlaybackCompleted()
        */

        /*
        // Case 4: playback with buffering
        trackVideoQualityUpdated()
        trackVideoPlaybackResumed()
        trackVideoBufferStarted()
        trackVideoBufferComplete()
        trackVideoPlaybackCompleted()
        */

        /*
        // Case 5: seeking in the main content
        trackVideoQualityUpdated()
        trackVideoPlaybackResumed()
        trackVideoSeekStarted()
        trackVideoSeekComplete()
        trackVideoPlaybackCompleted()
        */

        /*
        // Case 6: playback with skipped ads
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