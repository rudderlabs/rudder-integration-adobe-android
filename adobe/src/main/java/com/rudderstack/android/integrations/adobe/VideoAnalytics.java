package com.rudderstack.android.integrations.adobe;

import android.content.Context;

import com.adobe.primetime.va.simple.MediaHeartbeat;
import com.adobe.primetime.va.simple.MediaHeartbeatConfig;
import com.adobe.primetime.va.simple.MediaObject;
import com.rudderstack.android.sdk.core.RudderLogger;
import com.rudderstack.android.sdk.core.RudderMessage;
import com.rudderstack.android.sdk.core.RudderOption;

import java.util.HashMap;
import java.util.Map;

public class VideoAnalytics {
    private String heartbeatTrackingServerUrl;
    private String prefix;
    private Map<String, Object> contextData;
    boolean ssl;
    boolean debug;
    private boolean sessionStarted;
    String packageName;
    private PlaybackDelegate playback;
    private MediaHeartbeat heartbeat;
    private HeartbeatFactory heartbeatFactory;


    private static final Map<String, String> VIDEO_METADATA_KEYS = new HashMap<>();
    private static final Map<String, String> AD_METADATA_KEYS = new HashMap<>();

    static class HeartbeatFactory {

        static {
            VIDEO_METADATA_KEYS.put("assetId", MediaHeartbeat.VideoMetadataKeys.ASSET_ID);
            VIDEO_METADATA_KEYS.put("asset_id", MediaHeartbeat.VideoMetadataKeys.ASSET_ID);
            VIDEO_METADATA_KEYS.put("contentAssetId", MediaHeartbeat.VideoMetadataKeys.ASSET_ID);
            VIDEO_METADATA_KEYS.put("content_asset_id", MediaHeartbeat.VideoMetadataKeys.ASSET_ID);
            VIDEO_METADATA_KEYS.put("program", MediaHeartbeat.VideoMetadataKeys.SHOW);
            VIDEO_METADATA_KEYS.put("season", MediaHeartbeat.VideoMetadataKeys.SEASON);
            VIDEO_METADATA_KEYS.put("episode", MediaHeartbeat.VideoMetadataKeys.EPISODE);
            VIDEO_METADATA_KEYS.put("genre", MediaHeartbeat.VideoMetadataKeys.GENRE);
            VIDEO_METADATA_KEYS.put("channel", MediaHeartbeat.VideoMetadataKeys.NETWORK);
            VIDEO_METADATA_KEYS.put("airdate", MediaHeartbeat.VideoMetadataKeys.FIRST_AIR_DATE);
            VIDEO_METADATA_KEYS.put("publisher", MediaHeartbeat.VideoMetadataKeys.ORIGINATOR);
            VIDEO_METADATA_KEYS.put("rating", MediaHeartbeat.VideoMetadataKeys.RATING);

            AD_METADATA_KEYS.put("publisher", MediaHeartbeat.AdMetadataKeys.ADVERTISER);
        }

        HeartbeatFactory() {}

        MediaHeartbeat get(
                MediaHeartbeat.MediaHeartbeatDelegate delegate, MediaHeartbeatConfig config) {
            return new MediaHeartbeat(delegate, config);
        }
    }

    VideoAnalytics(
            Context context,
            String serverUrl,
            Map<String, Object> contextData,
            boolean ssl,
            String prefix) {
        this(context, serverUrl, contextData, ssl, new HeartbeatFactory(), prefix);
    }
    public VideoAnalytics(
            Context context,
            String heartbeatTrackingServerUrl,
            Map<String, Object> contextData,
            boolean ssl,
            HeartbeatFactory heartbeatFactory,
            String prefix
    ) {
        this.heartbeatTrackingServerUrl = heartbeatTrackingServerUrl;
        this.contextData = contextData;
        this.ssl = ssl;
        packageName = context.getPackageName();
        this.heartbeatFactory = heartbeatFactory;
        this.prefix = prefix;

        sessionStarted = false;
        debug = false;

        if (packageName == null) {
            // Default app version to "unknown" if not otherwise present b/c Adobe requires this value
            packageName = "unknown";
        }
    }

    void setDebugLogging(boolean debug) {
        this.debug = debug;
    }

    public void track(String eventName, RudderMessage element) {

        if (heartbeatTrackingServerUrl == null || heartbeatTrackingServerUrl.length() == 0) {
            RudderLogger.logDebug("Please enter a Heartbeat Tracking Server URL in your Rudderstack UI "
                            + "Settings in order to send video events to Adobe Analytics");
            return;
        }

        if (!eventName.equals("heartbeatPlaybackStarted") && !sessionStarted) {
            RudderLogger.logDebug("Video session has not started yet.");
            return;
        }

        switch (eventName) {
            case "heartbeatPlaybackStarted":
                trackVideoPlaybackStarted(element);
                break;

            case "heartbeatPlaybackPaused":
                trackVideoPlaybackPaused();
                break;

            case "heartbeatPlaybackResumed":
                trackVideoPlaybackResumed();
                break;

            case "heartbeatPlaybackCompleted":
                trackVideoPlaybackCompleted();
                break;

            case "heartbeatContentStarted":
                trackVideoContentStarted(element);
                break;

            case "heartbeatContentComplete":
                trackVideoContentCompleted();
                break;

            case "heartbeatBufferStarted":
                trackVideoPlaybackBufferStarted();
                break;

            case "heartbeatBufferCompleted":
                trackVideoPlaybackBufferCompleted();
                break;

            case "heartbeatSeekStarted":
                trackVideoPlaybackSeekStarted();
                break;

            case "heartbeatSeekCompleted":
                trackVideoPlaybackSeekCompleted(element);
                break;

            case "heartbeatAdBreakStarted":
                trackVideoAdBreakStarted(element);
                break;

            case "heartbeatAdBreakCompleted":
                trackVideoAdBreakCompleted();
                break;

            case "heartbeatAdStarted":
                trackVideoAdStarted(element);
                break;

            case "heartbeatAdSkipped":
                trackVideoAdSkipped();
                break;

            case "heartbeatAdCompleted":
                trackVideoAdCompleted();
                break;

            case "heartbeatPlaybackInterrupted":
                trackVideoPlaybackInterrupted();
                break;

            case "heartbeatQualityUpdated":
                trackVideoQualityUpdated(element);
                break;

        }
    }

    private void trackVideoPlaybackStarted(RudderMessage element) {

        Map<String, Object> eventProperties = element.getProperties();

        if (Utils.isEmpty(eventProperties)) {
            RudderLogger.logDebug("Event properties should not be empty for Playback Start Event!");
            return;
        }

        MediaHeartbeatConfig config = new MediaHeartbeatConfig();
        config.trackingServer = heartbeatTrackingServerUrl;
        config.channel = Utils.getString(eventProperties.get("channel"));
        if (Utils.isEmpty(config.channel)) {
            config.channel = "";
        }

        config.playerName = Utils.getString(eventProperties.get("video_player"));
        if (Utils.isEmpty(config.playerName)) {
            config.playerName = Utils.getString(eventProperties.get("videoPlayer"));
            if (Utils.isEmpty(config.playerName)) {
                config.playerName = "unknown";
            }
        }

        config.appVersion = packageName;
        config.ssl = ssl;
        config.debugLogging = debug;
        config.ovp = "unknown";

        playback = new PlaybackDelegate();
        heartbeat = heartbeatFactory.get(playback, config);
        sessionStarted = true;

        VideoEvent event = new VideoEvent(element);

        heartbeat.trackSessionStart(event.getMediaObject(), event.getContextData());
        RudderLogger.logVerbose("heartbeat.trackSessionStart(MediaObject);");

    }

    private void trackVideoPlaybackPaused() {
        playback.pausePlayhead();
        heartbeat.trackPause();
        RudderLogger.logVerbose("heartbeat.trackPause();");
    }

    private void trackVideoPlaybackResumed() {
        playback.unPausePlayhead();
        heartbeat.trackPlay();
        RudderLogger.logVerbose("heartbeat.trackPlay();");
    }

    private void trackVideoContentStarted(RudderMessage track) {
        VideoEvent event = new VideoEvent(track);

        if (Utils.getDouble(event.properties.get("position"), 0) > 0) {
            playback.updatePlayheadPosition(Utils.getLong(event.properties.get("position"), 0));
        }

        heartbeat.trackPlay();
        RudderLogger.logVerbose("heartbeat.trackPlay();");
        trackAdobeEvent(
                MediaHeartbeat.Event.ChapterStart, event.getChapterObject(), event.getContextData());
    }

    private void trackVideoContentCompleted() {
        trackAdobeEvent(MediaHeartbeat.Event.ChapterComplete, null, null);
    }

    //Upon playback complete, pause playhead, call trackComplete, and end session
    private void trackVideoPlaybackCompleted() {
        playback.pausePlayhead();
        heartbeat.trackComplete();
        RudderLogger.logVerbose("heartbeat.trackComplete();");
        heartbeat.trackSessionEnd();
        RudderLogger.logVerbose("heartbeat.trackSessionEnd();");
    }

    private void trackVideoPlaybackBufferStarted() {
        playback.pausePlayhead();
        trackAdobeEvent(MediaHeartbeat.Event.BufferStart, null, null);
    }

    private void trackVideoPlaybackBufferCompleted() {
        playback.unPausePlayhead();
        trackAdobeEvent(MediaHeartbeat.Event.BufferComplete, null, null);
    }

    private void trackAdobeEvent(
            MediaHeartbeat.Event eventName, MediaObject mediaObject, Map<String, String> cdata) {
        heartbeat.trackEvent(eventName, mediaObject, cdata);
        RudderLogger.logVerbose("heartbeat.trackEvent(" + eventName + mediaObject + cdata + ");" );
    }

    private void trackVideoPlaybackSeekStarted() {
        playback.pausePlayhead();
        trackAdobeEvent(MediaHeartbeat.Event.SeekStart, null, null);
    }

    private void trackVideoPlaybackSeekCompleted(RudderMessage track) {
        Map<String, Object> seekProperties = track.getProperties();
        long seekPosition = Utils.getLong(seekProperties.get("seekPosition"), 0);
        if (seekPosition == 0) {
            seekPosition = Utils.getLong(seekProperties.get("seek_position"), 0);
        }
        playback.updatePlayheadPosition(seekPosition);
        playback.unPausePlayhead();
        trackAdobeEvent(MediaHeartbeat.Event.SeekComplete, null, null);
    }

    private void trackVideoAdBreakStarted(RudderMessage track) {
        VideoEvent event = new VideoEvent(track, true);
        trackAdobeEvent(
                MediaHeartbeat.Event.AdBreakStart, event.getAdBreakObject(), event.getContextData());
    }

    private void trackVideoAdBreakCompleted() {
        trackAdobeEvent(MediaHeartbeat.Event.AdBreakComplete, null, null);
    }

    private void trackVideoAdStarted(RudderMessage track) {
        VideoEvent event = new VideoEvent(track, true);
        trackAdobeEvent(MediaHeartbeat.Event.AdStart, event.getAdObject(), event.getContextData());
    }

    private void trackVideoAdSkipped() {
        trackAdobeEvent(MediaHeartbeat.Event.AdSkip, null, null);
    }

    private void trackVideoAdCompleted() {
        trackAdobeEvent(MediaHeartbeat.Event.AdComplete, null, null);
    }

    private void trackVideoPlaybackInterrupted() {
        playback.pausePlayhead();
    }

    private void trackVideoQualityUpdated(RudderMessage track) {
        playback.createAndUpdateQosObject(track.getProperties());
    }

    /** A wrapper for video metadata and properties. */
    class VideoEvent {
        private Map<String, String> metadata;
        private Map<String, Object> properties;
        private RudderMessage element;

        /**
         * Creates video properties from the ones provided in the event.
         *
         * @param element Event Payload.
         */
        VideoEvent(RudderMessage element) {
            this(element, false);
        }

        /**
         * Creates video properties from the ones provided in the event.
         *
         * @param element Event Payload.
         * @param isAd Determines if the video is an ad.
         */
        VideoEvent(RudderMessage element, boolean isAd) {
            this.element = element;
            metadata = new HashMap<>();
            properties = new HashMap<>();
            if (element.getProperties() != null) {
                Map<String, Object> eventProperties = element.getProperties();
                properties.putAll(eventProperties);

                if (isAd) {
                    mapAdProperties(eventProperties);
                } else {
                    mapVideoProperties(eventProperties);
                }
            }
        }

        private void mapVideoProperties(Map<String, Object> eventProperties) {
            for (String key : eventProperties.keySet()) {

                if (VIDEO_METADATA_KEYS.containsKey(key)) {
                    String propertyKey = VIDEO_METADATA_KEYS.get(key);
                    metadata.put(propertyKey, String.valueOf(eventProperties.get(key)));
                    properties.remove(key);
                }
            }

            if (properties.containsKey("livestream")) {
                String format = MediaHeartbeat.StreamType.LIVE;
                if (!Utils.getBoolean("livestream", false)) {
                    format = MediaHeartbeat.StreamType.VOD;
                }

                metadata.put(MediaHeartbeat.VideoMetadataKeys.STREAM_FORMAT, format);
                properties.remove("livestream");
            }
        }

        private void mapAdProperties(Map<String, Object> eventProperties) {
            for (String key : eventProperties.keySet()) {

                if (AD_METADATA_KEYS.containsKey(key)) {
                    String propertyKey = AD_METADATA_KEYS.get(key);
                    metadata.put(propertyKey, String.valueOf(eventProperties.get(key)));
                    properties.remove(key);
                }
            }
        }

        Map<String, String> getContextData() {

            Map<String, Object> extraProperties = new HashMap<>();
            extraProperties.putAll(properties);

            // Remove products from extra properties
            extraProperties.remove("products");

            // Remove video metadata keys
            for (String key : VIDEO_METADATA_KEYS.keySet()) {
                extraProperties.remove(key);
            }

            // Remove ad metadata keys
            for (String key : AD_METADATA_KEYS.keySet()) {
                extraProperties.remove(key);
            }

            // Remove media object keys
            for (String key :
                    new String[] {
                            "title",
                            "indexPosition",
                            "index_position",
                            "position",
                            "totalLength",
                            "total_length",
                            "startTime",
                            "start_time"
                    }) {
                extraProperties.remove(key);
            }

            Map<String, String> cdata = new HashMap<>();

            if(!Utils.isEmpty(contextData)) {
                for (String field : contextData.keySet()) {
                    Object mappedContextValue = null;
                    try {
                        mappedContextValue = Utils.getMappedContextValue(field, element);
                    } catch (IllegalArgumentException e) {
                        // Ignore.
                    }

                    if (mappedContextValue != null) {
                        String mappedContextName = Utils.getString(contextData.get(field));
                        cdata.put(mappedContextName, String.valueOf(mappedContextValue));
                        extraProperties.remove(field);
                    }
                }
            }

            // Add extra properties.
            if(!Utils.isEmpty(extraProperties)) {
                for (String extraProperty : extraProperties.keySet()) {
                    String propertyName = prefix + extraProperty;
                    cdata.put(propertyName, Utils.getString(extraProperties.get(extraProperty)));
                }
            }

            return cdata;
        }

        MediaObject getChapterObject() {
            if (element.getProperties() == null) {
                return null;
            }

            Map<String, Object> eventProperties = element.getProperties();

            String chapter_name = Utils.getString(eventProperties.get("chapter_name"));
            if (Utils.isEmpty(chapter_name)) {
                chapter_name = "no chapter name";
            }
            long position = Utils.getLong(eventProperties.get("position"), 1);
            
            double length = Utils.getDouble(eventProperties.get("length"), 6000);

            double start_time = Utils.getDouble(eventProperties.get("start_time"), 0);
            if (start_time == 0) {
                start_time = Utils.getDouble(eventProperties.get("startTime"), 0);
            }

            MediaObject media =
                    MediaHeartbeat.createChapterObject(chapter_name, position, length, start_time);
            media.setValue(MediaHeartbeat.MediaObjectKey.StandardVideoMetadata, metadata);
            return media;
        }

        MediaObject getMediaObject() {
            if (element.getProperties().isEmpty()) {
                return null;
            }
            Map<String, Object> eventProperties = element.getProperties();

            String title = Utils.getString(eventProperties.get("title"));
            if (Utils.isEmpty(title)) {
                title = "no title";
            }

            String asset_id = Utils.getString(eventProperties.get("asset_id"));
            if (Utils.isEmpty(asset_id) && eventProperties.containsKey("assetId")) {
                asset_id = Utils.getString(eventProperties.get("assetId"));
            }
            if (Utils.isEmpty(asset_id)) {
                asset_id = "default ad";
            }

            double total_length = Utils.getDouble(eventProperties.get("total_length"), 0);
            if (total_length == 0) {
                total_length = Utils.getDouble(eventProperties.get("totalLength"), 0);
            }
            String livestream = MediaHeartbeat.StreamType.LIVE;
            if (!Utils.getBoolean(eventProperties.get("livestream"), false)) {
                livestream = MediaHeartbeat.StreamType.VOD;
            }

            MediaObject media =
                    MediaHeartbeat.createMediaObject(title, asset_id, total_length, livestream);

            media.setValue(MediaHeartbeat.MediaObjectKey.StandardVideoMetadata, metadata);
            return media;
        }

        MediaObject getAdObject() {
            if (element.getProperties() == null) {
                return null;
            }

            Map<String, Object> eventProperties = element.getProperties();

            String title = Utils.getString(eventProperties.get("title"));

            String asset_id = Utils.getString(eventProperties.get("asset_id"));
            if (Utils.isEmpty(asset_id)) {
                asset_id = Utils.getString(eventProperties.get("assetId"));
            }

            long position = Utils.getLong(eventProperties.get("position"), 1);

            double total_length = Utils.getDouble(eventProperties.get("total_length"), 0);
            if (total_length == 0) {
                total_length = Utils.getDouble(eventProperties.get("totalLength"), 0);
            }

            MediaObject media = MediaHeartbeat.createAdObject(title, asset_id, position, total_length);

            media.setValue(MediaHeartbeat.MediaObjectKey.StandardAdMetadata, metadata);
            return media;
        }

        MediaObject getAdBreakObject() {
            if (element.getProperties() == null) {
                return null;
            }

            Map<String, Object> eventProperties = element.getProperties();

            String title = Utils.getString(eventProperties.get("title"));

            long position = Utils.getLong(eventProperties.get("position"), 1);

            double startTime = Utils.getDouble(eventProperties.get("startTime"), 0);
            if (startTime == 0) {
                startTime = Utils.getDouble(eventProperties.get("start_time"), 0);
            }

            MediaObject media = MediaHeartbeat.createAdBreakObject(title, position, startTime);
            return media;
        }
    }
}
