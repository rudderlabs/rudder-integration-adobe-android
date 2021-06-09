package com.rudderstack.android.integrations.adobe;

import android.content.Context;

import com.adobe.primetime.va.simple.MediaHeartbeat;
import com.adobe.primetime.va.simple.MediaHeartbeatConfig;
import com.adobe.primetime.va.simple.MediaObject;
import com.rudderstack.android.sdk.core.RudderLogger;
import com.rudderstack.android.sdk.core.RudderMessage;
import com.rudderstack.android.sdk.core.RudderProperty;

import java.util.HashMap;
import java.util.Map;

public class VideoAnalytics {
    String heartbeatTrackingServerUrl;
    Map<String, Object> contextDatam;
    boolean ssl;
    int logLevel;
    boolean debug = false;
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

    public VideoAnalytics(
            Context context,
            String heartbeatTrackingServerUrl,
            Map<String, Object> contextData,
            boolean ssl,
            int logLevel
    ) {
        this.heartbeatTrackingServerUrl = heartbeatTrackingServerUrl;
        this.contextDatam = contextData;
        this.ssl = ssl;
        this.logLevel = logLevel;
        packageName = context.getPackageName();

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

        if (heartbeatTrackingServerUrl == null) {
            RudderLogger.logVerbose("Please enter a Heartbeat Tracking Server URL in your Segment UI "
                            + "Settings in order to send video events to Adobe Analytics");
            return;
        }

        if (eventName != "heartbeatPlaybackStarted" && !sessionStarted) {
            RudderLogger.logVerbose("Video session has not started yet.");
            return;
        }

        switch (eventName) {
            case "heartbeatPlaybackStarted":
                trackVideoPlaybackStarted(element);
                break;

                /*
            case "heartbeatContentStarted":
                trackVideoPlaybackPaused();
                break;

            case "heartbeatPlaybackPaused":
                trackVideoPlaybackResumed();
                break;

            case "heartbeatPlaybackResumed":
                trackVideoPlaybackCompleted();
                break;

            case "heartbeatContentComplete":
                trackVideoContentStarted(eventProperties);
                break;

            case "heartbeatPlaybackCompleted":
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
                trackVideoPlaybackSeekCompleted(eventProperties);
                break;

            case "heartbeatAdBreakStarted":
                trackVideoAdBreakStarted(eventProperties);
                break;

            case "heartbeatAdBreakCompleted":
                trackVideoAdBreakCompleted();
                break;

            case "heartbeatAdStarted":
                trackVideoAdStarted(eventProperties);
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
                trackVideoQualityUpdated(eventProperties);
                break;
                //*/
        }
    }

    private void trackVideoPlaybackStarted(RudderMessage element) {

        Map<String, Object> eventProperties = element.getProperties();

        MediaHeartbeatConfig config = new MediaHeartbeatConfig();
        config.trackingServer = heartbeatTrackingServerUrl;
        config.channel = (String) eventProperties.get("channel");
        if (config.channel == null) {
            config.channel = "";
        }

        config.playerName = (String) eventProperties.get("videoPlayer");
        if (config.playerName == null) {
            config.playerName = (String) eventProperties.get("video_player");
            if (config.playerName == null) {
                config.playerName = "unknown";
            }
        }

        config.appVersion = packageName;
        config.ssl = ssl;
        config.debugLogging = debug;

        //RudderOption portion: will be implemented later
//        Map<String, Object> eventOptions = track.integrations().getValueMap("Adobe Analytics");
//        if (eventOptions != null && eventOptions.get("ovpName") != null) {
//            config.ovp = (String) eventOptions.getString("ovpName");
//        } else if (eventOptions != null && eventOptions.get("ovp_name") != null) {
//            config.ovp = (String) eventOptions.getString("ovp_name");
//        } else if (eventOptions != null && eventOptions.get("ovp") != null) {
//            config.ovp = (String) eventOptions.getString("ovp");
//        } else {
//            config.ovp = "unknown";
//        }
        // For now, implementing config.ovp = "unknown"; (Should be changed later
        // when RudderOption portion is implemented.
        config.ovp = "unknown";

        playback = new PlaybackDelegate();
//        heartbeat = heartbeatFactory.get(playback, config);
        sessionStarted = true;

        VideoEvent event = new VideoEvent(element);

//        heartbeat.trackSessionStart(event.getMediaObject(), event.getContextData());
        RudderLogger.logVerbose("heartbeat.trackSessionStart(MediaObject);");


    }

    /** A wrapper for video metadata and properties. */
    class VideoEvent {
        private Map<String, String> metadata;
        private Map<String, Object> properties;
        private RudderMessage payload;

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
            this.payload = element;
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
                if (!getBoolean("livestream", false)) {
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

        /*
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

            for (String field : contextDataConfiguration.getEventFieldNames()) {
                Object value = null;
                try {
                    value = contextDataConfiguration.searchValue(field, payload);
                } catch (IllegalArgumentException e) {
                    // Ignore.
                }

                if (value != null) {
                    String variable = contextDataConfiguration.getVariableName(field);
                    cdata.put(variable, String.valueOf(value));
                    extraProperties.remove(field);
                }
            }

            // Add extra properties.
            for (String extraProperty : extraProperties.keySet()) {
                String variable = contextDataConfiguration.getPrefix() + extraProperty;
                cdata.put(variable, extraProperties.getString(extraProperty));
            }

            return cdata;
        }

        MediaObject getChapterObject() {
            if (!payload.containsKey("properties")) {
                return null;
            }

            ValueMap eventProperties = payload.getValueMap("properties");

            String title = eventProperties.getString("title");
            long indexPosition =
                    eventProperties.getLong("indexPosition", 1); // Segment does not spec this
            if (indexPosition == 1) {
                indexPosition = eventProperties.getLong("index_position", 1);
            }
            double totalLength = eventProperties.getDouble("totalLength", 0);
            if (totalLength == 0) {
                totalLength = eventProperties.getDouble("total_length", 0);
            }
            double startTime = eventProperties.getDouble("startTime", 0);
            if (startTime == 0) {
                startTime = eventProperties.getDouble("start_time", 0);
            }

            MediaObject media =
                    MediaHeartbeat.createChapterObject(title, indexPosition, totalLength, startTime);
            media.setValue(MediaHeartbeat.MediaObjectKey.StandardVideoMetadata, metadata);
            return media;
        }

        MediaObject getMediaObject() {
            if (!payload.containsKey("properties")) {
                return null;
            }

            ValueMap eventProperties = payload.getValueMap("properties");

            String title = eventProperties.getString("title");
            String contentAssetId = eventProperties.getString("contentAssetId");
            if (contentAssetId == null || contentAssetId.trim().isEmpty()) {
                contentAssetId = eventProperties.getString("content_asset_id");
            }
            double totalLength = eventProperties.getDouble("totalLength", 0);
            if (totalLength == 0) {
                totalLength = eventProperties.getDouble("total_length", 0);
            }
            String format = MediaHeartbeat.StreamType.LIVE;
            if (!eventProperties.getBoolean("livestream", false)) {
                format = MediaHeartbeat.StreamType.VOD;
            }

            MediaObject media =
                    MediaHeartbeat.createMediaObject(title, contentAssetId, totalLength, format);
            media.setValue(MediaHeartbeat.MediaObjectKey.StandardVideoMetadata, metadata);
            return media;
        }

        MediaObject getAdObject() {
            if (!payload.containsKey("properties")) {
                return null;
            }

            ValueMap eventProperties = payload.getValueMap("properties");

            String title = eventProperties.getString("title");
            String assetId = eventProperties.getString("assetId");
            if (assetId == null || assetId.trim().isEmpty()) {
                assetId = eventProperties.getString("asset_id");
            }
            long indexPosition = eventProperties.getLong("indexPosition", 1);
            if (indexPosition == 1) {
                indexPosition = eventProperties.getLong("index_position", 1);
            }
            double totalLength = eventProperties.getDouble("totalLength", 0);
            if (totalLength == 0) {
                totalLength = eventProperties.getDouble("total_length", 0);
            }

            MediaObject media = MediaHeartbeat.createAdObject(title, assetId, indexPosition, totalLength);

            media.setValue(MediaHeartbeat.MediaObjectKey.StandardAdMetadata, metadata);
            return media;
        }

        MediaObject getAdBreakObject() {
            if (!payload.containsKey("properties")) {
                return null;
            }

            ValueMap eventProperties = payload.getValueMap("properties");

            String title = eventProperties.getString("title");
            long indexPosition =
                    eventProperties.getLong("indexPosition", 1); // Segment does not spec this
            if (indexPosition == 1) {
                indexPosition = eventProperties.getLong("index_position", 1);
            }
            double startTime = eventProperties.getDouble("startTime", 0);
            if (startTime == 0) {
                startTime = eventProperties.getDouble("start_time", 0);
            }
            MediaObject media = MediaHeartbeat.createAdBreakObject(title, indexPosition, startTime);

            return media;
        }

        Map<String, String> getMetadata() {
            return metadata;
        }

        Map<String, Object> getProperties() {
            return properties;
        }

        Map<String, Object> getEventPayload() {
            return payload;
        }
        //*/

        public boolean getBoolean(String key, boolean defaultValue) {
            Object value = (key);
            if (value instanceof Boolean) {
                return (boolean) value;
            } else if (value instanceof String) {
                return Boolean.valueOf((String) value);
            }
            return defaultValue;
        }
    }
}
