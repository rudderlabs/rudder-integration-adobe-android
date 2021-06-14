package com.rudderstack.android.integrations.adobe;

import android.content.Context;

import com.adobe.mobile.Media;
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
            RudderLogger.logVerbose("Please enter a Heartbeat Tracking Server URL in your Rudderstack UI "
                            + "Settings in order to send video events to Adobe Analytics");
            return;
        }

        if (!eventName.equals("heartbeatPlaybackStarted") && !sessionStarted) {
            RudderLogger.logVerbose("Video session has not started yet.");
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
//        Map<String, Object> eventOptions = RudderOption.integrations().getValueMap("Adobe Analytics");
//        if (eventOptions != null && eventOptions.get("ovpName") != null) {
//            config.ovp = Utils.getString(eventOptions.get("ovpName"));
//        } else if (eventOptions != null && eventOptions.get("ovp_name") != null) {
//            config.ovp = Utils.getString(eventOptions.get("ovp_name"));
//        } else if (eventOptions != null && eventOptions.get("ovp") != null) {
//            config.ovp = Utils.getString(eventOptions.get("ovp"));
//        } else {
//            config.ovp = "unknown";
//        }
        // As of now, implementing config.ovp = "unknown"; (Should be changed later
        // when RudderOption portion is implemented.
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
                    Object value = null;
                    try {
                        value = Utils.searchValue(field, element);
                    } catch (IllegalArgumentException e) {
                        // Ignore.
                    }

                    if (value != null) {
                        String variable = (String) contextData.get(field);
                        cdata.put(variable, String.valueOf(value));
                        extraProperties.remove(field);
                    }
                }
            }

            // Add extra properties.
            if(!Utils.isEmpty(extraProperties)) {
                for (String extraProperty : extraProperties.keySet()) {
                    String variable = prefix + extraProperty;
                    cdata.put(variable, Utils.getString(extraProperties.get(extraProperty)));
                }
            }

            return cdata;
        }

        MediaObject getChapterObject() {
            if (element.getProperties() == null) {
                return null;
            }

            Map<String, Object> eventProperties = element.getProperties();

            String title = Utils.getString(eventProperties.get("title"));
            long indexPosition =
                    Utils.getLong(eventProperties.get("indexPosition"), 1);
            if (indexPosition == 1) {
                indexPosition = Utils.getLong(eventProperties.get("index_position"), 1);
            }
            double totalLength = Utils.getDouble(eventProperties.get("totalLength"), 0);
            if (totalLength == 0) {
                totalLength = Utils.getDouble(eventProperties.get("total_length"), 0);
            }
            double startTime = Utils.getDouble(eventProperties.get("startTime"), 0);
            if (startTime == 0) {
                startTime = Utils.getDouble(eventProperties.get("start_time"), 0);
            }

            MediaObject media =
                    MediaHeartbeat.createChapterObject(title, indexPosition, totalLength, startTime);
            media.setValue(MediaHeartbeat.MediaObjectKey.StandardMediaMetadata, metadata);
            return media;
        }

        MediaObject getMediaObject() {
            if (element.getProperties().isEmpty()) {
                return null;
            }

            Map<String, Object> eventProperties = element.getProperties();

            String title = Utils.getString(eventProperties.get("title"));
            String contentAssetId = Utils.getString(eventProperties.get("contentAssetId"));
            if (contentAssetId == null || contentAssetId.trim().isEmpty()) {
                contentAssetId = Utils.getString(eventProperties.get("content_asset_id"));
            }
            double totalLength = Utils.getDouble(eventProperties.get("totalLength"), 0);
            if (totalLength == 0) {
                totalLength = Utils.getDouble(eventProperties.get("total_length"), 0);
            }
            String format = MediaHeartbeat.StreamType.LIVE;
            if (!Utils.getBoolean(eventProperties.get("livestream"), false)) {
                format = MediaHeartbeat.StreamType.VOD;
            }

            MediaObject media =
                    MediaHeartbeat.createMediaObject(title, contentAssetId, totalLength, format);

            media.setValue(MediaHeartbeat.MediaObjectKey.StandardVideoMetadata, metadata);
            return media;
        }

        MediaObject getAdObject() {
            if (element.getProperties() == null) {
                return null;
            }

            Map<String, Object> eventProperties = element.getProperties();

            String title = Utils.getString(eventProperties.get("title"));
            String assetId = Utils.getString(eventProperties.get("assetId"));
            if (assetId == null || assetId.trim().isEmpty()) {
                assetId = Utils.getString(eventProperties.get("asset_id"));
            }
            long indexPosition = Utils.getLong(eventProperties.get("indexPosition"), 1);
            if (indexPosition == 1) {
                indexPosition = Utils.getLong(eventProperties.get("index_position"), 1);
            }
            double totalLength = Utils.getDouble(eventProperties.get("totalLength"), 0);
            if (totalLength == 0) {
                totalLength = Utils.getDouble(eventProperties.get("total_length"), 0);
            }

            MediaObject media = MediaHeartbeat.createAdObject(title, assetId, indexPosition, totalLength);

            media.setValue(MediaHeartbeat.MediaObjectKey.StandardAdMetadata, metadata);
            return media;
        }

        MediaObject getAdBreakObject() {
            if (element.getProperties() == null) {
                return null;
            }

            Map<String, Object> eventProperties = element.getProperties();

            String title = Utils.getString(eventProperties.get("title"));
            long indexPosition =
                    Utils.getLong(eventProperties.get("indexPosition"), 1); // Segment does not spec this
            if (indexPosition == 1) {
                indexPosition = Utils.getLong(eventProperties.get("index_position"), 1);
            }
            double startTime = Utils.getDouble(eventProperties.get("startTime"), 0);
            if (startTime == 0) {
                startTime = Utils.getDouble(eventProperties.get("start_time"), 0);
            }
            MediaObject media = MediaHeartbeat.createAdBreakObject(title, indexPosition, startTime);

            return media;
        }
    }
}
