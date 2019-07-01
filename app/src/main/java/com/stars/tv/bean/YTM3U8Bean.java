package com.stars.tv.bean;

import java.util.List;

public class YTM3U8Bean {

    private PlayabilityStatusBean playabilityStatus;
    private StreamingDataBean streamingData;
    private PlaybackTrackingBean playbackTracking;
    private VideoDetailsBean videoDetails;
    private PlayerConfigBean playerConfig;
    private StoryboardsBean storyboards;
    private String trackingParams;
    private AttestationBean attestation;
    private AdSafetyReasonBean adSafetyReason;
    private List<AnnotationsBean> annotations;

    public PlayabilityStatusBean getPlayabilityStatus() {
        return playabilityStatus;
    }

    public void setPlayabilityStatus(PlayabilityStatusBean playabilityStatus) {
        this.playabilityStatus = playabilityStatus;
    }

    public StreamingDataBean getStreamingData() {
        return streamingData;
    }

    public void setStreamingData(StreamingDataBean streamingData) {
        this.streamingData = streamingData;
    }

    public PlaybackTrackingBean getPlaybackTracking() {
        return playbackTracking;
    }

    public void setPlaybackTracking(PlaybackTrackingBean playbackTracking) {
        this.playbackTracking = playbackTracking;
    }

    public VideoDetailsBean getVideoDetails() {
        return videoDetails;
    }

    public void setVideoDetails(VideoDetailsBean videoDetails) {
        this.videoDetails = videoDetails;
    }

    public PlayerConfigBean getPlayerConfig() {
        return playerConfig;
    }

    public void setPlayerConfig(PlayerConfigBean playerConfig) {
        this.playerConfig = playerConfig;
    }

    public StoryboardsBean getStoryboards() {
        return storyboards;
    }

    public void setStoryboards(StoryboardsBean storyboards) {
        this.storyboards = storyboards;
    }

    public String getTrackingParams() {
        return trackingParams;
    }

    public void setTrackingParams(String trackingParams) {
        this.trackingParams = trackingParams;
    }

    public AttestationBean getAttestation() {
        return attestation;
    }

    public void setAttestation(AttestationBean attestation) {
        this.attestation = attestation;
    }

    public AdSafetyReasonBean getAdSafetyReason() {
        return adSafetyReason;
    }

    public void setAdSafetyReason(AdSafetyReasonBean adSafetyReason) {
        this.adSafetyReason = adSafetyReason;
    }

    public List<AnnotationsBean> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(List<AnnotationsBean> annotations) {
        this.annotations = annotations;
    }

    public static class PlayabilityStatusBean {

        private String status;
        private boolean playableInEmbed;
        private LiveStreamabilityBean liveStreamability;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public boolean isPlayableInEmbed() {
            return playableInEmbed;
        }

        public void setPlayableInEmbed(boolean playableInEmbed) {
            this.playableInEmbed = playableInEmbed;
        }

        public LiveStreamabilityBean getLiveStreamability() {
            return liveStreamability;
        }

        public void setLiveStreamability(LiveStreamabilityBean liveStreamability) {
            this.liveStreamability = liveStreamability;
        }

        public static class LiveStreamabilityBean {

            private LiveStreamabilityRendererBean liveStreamabilityRenderer;

            public LiveStreamabilityRendererBean getLiveStreamabilityRenderer() {
                return liveStreamabilityRenderer;
            }

            public void setLiveStreamabilityRenderer(LiveStreamabilityRendererBean liveStreamabilityRenderer) {
                this.liveStreamabilityRenderer = liveStreamabilityRenderer;
            }

            public static class LiveStreamabilityRendererBean {

                private String videoId;
                private String pollDelayMs;

                public String getVideoId() {
                    return videoId;
                }

                public void setVideoId(String videoId) {
                    this.videoId = videoId;
                }

                public String getPollDelayMs() {
                    return pollDelayMs;
                }

                public void setPollDelayMs(String pollDelayMs) {
                    this.pollDelayMs = pollDelayMs;
                }
            }
        }
    }

    public static class StreamingDataBean {

        private String expiresInSeconds;
        private String dashManifestUrl;
        private String hlsManifestUrl;
        private String probeUrl;

        public String getExpiresInSeconds() {
            return expiresInSeconds;
        }

        public void setExpiresInSeconds(String expiresInSeconds) {
            this.expiresInSeconds = expiresInSeconds;
        }

        public String getDashManifestUrl() {
            return dashManifestUrl;
        }

        public void setDashManifestUrl(String dashManifestUrl) {
            this.dashManifestUrl = dashManifestUrl;
        }

        public String getHlsManifestUrl() {
            return hlsManifestUrl;
        }

        public void setHlsManifestUrl(String hlsManifestUrl) {
            this.hlsManifestUrl = hlsManifestUrl;
        }

        public String getProbeUrl() {
            return probeUrl;
        }

        public void setProbeUrl(String probeUrl) {
            this.probeUrl = probeUrl;
        }
    }

    public static class PlaybackTrackingBean {

        private VideostatsPlaybackUrlBean videostatsPlaybackUrl;
        private VideostatsDelayplayUrlBean videostatsDelayplayUrl;
        private VideostatsWatchtimeUrlBean videostatsWatchtimeUrl;
        private PtrackingUrlBean ptrackingUrl;
        private QoeUrlBean qoeUrl;
        private SetAwesomeUrlBean setAwesomeUrl;
        private AtrUrlBean atrUrl;
        private YoutubeRemarketingUrlBean youtubeRemarketingUrl;

        public VideostatsPlaybackUrlBean getVideostatsPlaybackUrl() {
            return videostatsPlaybackUrl;
        }

        public void setVideostatsPlaybackUrl(VideostatsPlaybackUrlBean videostatsPlaybackUrl) {
            this.videostatsPlaybackUrl = videostatsPlaybackUrl;
        }

        public VideostatsDelayplayUrlBean getVideostatsDelayplayUrl() {
            return videostatsDelayplayUrl;
        }

        public void setVideostatsDelayplayUrl(VideostatsDelayplayUrlBean videostatsDelayplayUrl) {
            this.videostatsDelayplayUrl = videostatsDelayplayUrl;
        }

        public VideostatsWatchtimeUrlBean getVideostatsWatchtimeUrl() {
            return videostatsWatchtimeUrl;
        }

        public void setVideostatsWatchtimeUrl(VideostatsWatchtimeUrlBean videostatsWatchtimeUrl) {
            this.videostatsWatchtimeUrl = videostatsWatchtimeUrl;
        }

        public PtrackingUrlBean getPtrackingUrl() {
            return ptrackingUrl;
        }

        public void setPtrackingUrl(PtrackingUrlBean ptrackingUrl) {
            this.ptrackingUrl = ptrackingUrl;
        }

        public QoeUrlBean getQoeUrl() {
            return qoeUrl;
        }

        public void setQoeUrl(QoeUrlBean qoeUrl) {
            this.qoeUrl = qoeUrl;
        }

        public SetAwesomeUrlBean getSetAwesomeUrl() {
            return setAwesomeUrl;
        }

        public void setSetAwesomeUrl(SetAwesomeUrlBean setAwesomeUrl) {
            this.setAwesomeUrl = setAwesomeUrl;
        }

        public AtrUrlBean getAtrUrl() {
            return atrUrl;
        }

        public void setAtrUrl(AtrUrlBean atrUrl) {
            this.atrUrl = atrUrl;
        }

        public YoutubeRemarketingUrlBean getYoutubeRemarketingUrl() {
            return youtubeRemarketingUrl;
        }

        public void setYoutubeRemarketingUrl(YoutubeRemarketingUrlBean youtubeRemarketingUrl) {
            this.youtubeRemarketingUrl = youtubeRemarketingUrl;
        }

        public static class VideostatsPlaybackUrlBean {

            private String baseUrl;

            public String getBaseUrl() {
                return baseUrl;
            }

            public void setBaseUrl(String baseUrl) {
                this.baseUrl = baseUrl;
            }
        }

        public static class VideostatsDelayplayUrlBean {

            private String baseUrl;
            private int elapsedMediaTimeSeconds;

            public String getBaseUrl() {
                return baseUrl;
            }

            public void setBaseUrl(String baseUrl) {
                this.baseUrl = baseUrl;
            }

            public int getElapsedMediaTimeSeconds() {
                return elapsedMediaTimeSeconds;
            }

            public void setElapsedMediaTimeSeconds(int elapsedMediaTimeSeconds) {
                this.elapsedMediaTimeSeconds = elapsedMediaTimeSeconds;
            }
        }

        public static class VideostatsWatchtimeUrlBean {

            private String baseUrl;

            public String getBaseUrl() {
                return baseUrl;
            }

            public void setBaseUrl(String baseUrl) {
                this.baseUrl = baseUrl;
            }
        }

        public static class PtrackingUrlBean {

            private String baseUrl;

            public String getBaseUrl() {
                return baseUrl;
            }

            public void setBaseUrl(String baseUrl) {
                this.baseUrl = baseUrl;
            }
        }

        public static class QoeUrlBean {

            private String baseUrl;

            public String getBaseUrl() {
                return baseUrl;
            }

            public void setBaseUrl(String baseUrl) {
                this.baseUrl = baseUrl;
            }
        }

        public static class SetAwesomeUrlBean {

            private String baseUrl;
            private int elapsedMediaTimeSeconds;

            public String getBaseUrl() {
                return baseUrl;
            }

            public void setBaseUrl(String baseUrl) {
                this.baseUrl = baseUrl;
            }

            public int getElapsedMediaTimeSeconds() {
                return elapsedMediaTimeSeconds;
            }

            public void setElapsedMediaTimeSeconds(int elapsedMediaTimeSeconds) {
                this.elapsedMediaTimeSeconds = elapsedMediaTimeSeconds;
            }
        }

        public static class AtrUrlBean {

            private String baseUrl;
            private int elapsedMediaTimeSeconds;

            public String getBaseUrl() {
                return baseUrl;
            }

            public void setBaseUrl(String baseUrl) {
                this.baseUrl = baseUrl;
            }

            public int getElapsedMediaTimeSeconds() {
                return elapsedMediaTimeSeconds;
            }

            public void setElapsedMediaTimeSeconds(int elapsedMediaTimeSeconds) {
                this.elapsedMediaTimeSeconds = elapsedMediaTimeSeconds;
            }
        }

        public static class YoutubeRemarketingUrlBean {

            private String baseUrl;
            private int elapsedMediaTimeSeconds;

            public String getBaseUrl() {
                return baseUrl;
            }

            public void setBaseUrl(String baseUrl) {
                this.baseUrl = baseUrl;
            }

            public int getElapsedMediaTimeSeconds() {
                return elapsedMediaTimeSeconds;
            }

            public void setElapsedMediaTimeSeconds(int elapsedMediaTimeSeconds) {
                this.elapsedMediaTimeSeconds = elapsedMediaTimeSeconds;
            }
        }
    }

    public static class VideoDetailsBean {

        private String videoId;
        private String title;
        private String lengthSeconds;
        private boolean isLive;
        private String channelId;
        private boolean isOwnerViewing;
        private String shortDescription;
        private boolean isCrawlable;
        private boolean isLiveDvrEnabled;
        private ThumbnailBean thumbnail;
        private boolean useCipher;
        private int liveChunkReadahead;
        private double averageRating;
        private boolean allowRatings;
        private String viewCount;
        private String author;
        private boolean isLiveDefaultBroadcast;
        private boolean isLowLatencyLiveStream;
        private boolean isPrivate;
        private boolean isUnpluggedCorpus;
        private String latencyClass;
        private boolean isLiveContent;
        private List<String> keywords;

        public String getVideoId() {
            return videoId;
        }

        public void setVideoId(String videoId) {
            this.videoId = videoId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getLengthSeconds() {
            return lengthSeconds;
        }

        public void setLengthSeconds(String lengthSeconds) {
            this.lengthSeconds = lengthSeconds;
        }

        public boolean isIsLive() {
            return isLive;
        }

        public void setIsLive(boolean isLive) {
            this.isLive = isLive;
        }

        public String getChannelId() {
            return channelId;
        }

        public void setChannelId(String channelId) {
            this.channelId = channelId;
        }

        public boolean isIsOwnerViewing() {
            return isOwnerViewing;
        }

        public void setIsOwnerViewing(boolean isOwnerViewing) {
            this.isOwnerViewing = isOwnerViewing;
        }

        public String getShortDescription() {
            return shortDescription;
        }

        public void setShortDescription(String shortDescription) {
            this.shortDescription = shortDescription;
        }

        public boolean isIsCrawlable() {
            return isCrawlable;
        }

        public void setIsCrawlable(boolean isCrawlable) {
            this.isCrawlable = isCrawlable;
        }

        public boolean isIsLiveDvrEnabled() {
            return isLiveDvrEnabled;
        }

        public void setIsLiveDvrEnabled(boolean isLiveDvrEnabled) {
            this.isLiveDvrEnabled = isLiveDvrEnabled;
        }

        public ThumbnailBean getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(ThumbnailBean thumbnail) {
            this.thumbnail = thumbnail;
        }

        public boolean isUseCipher() {
            return useCipher;
        }

        public void setUseCipher(boolean useCipher) {
            this.useCipher = useCipher;
        }

        public int getLiveChunkReadahead() {
            return liveChunkReadahead;
        }

        public void setLiveChunkReadahead(int liveChunkReadahead) {
            this.liveChunkReadahead = liveChunkReadahead;
        }

        public double getAverageRating() {
            return averageRating;
        }

        public void setAverageRating(double averageRating) {
            this.averageRating = averageRating;
        }

        public boolean isAllowRatings() {
            return allowRatings;
        }

        public void setAllowRatings(boolean allowRatings) {
            this.allowRatings = allowRatings;
        }

        public String getViewCount() {
            return viewCount;
        }

        public void setViewCount(String viewCount) {
            this.viewCount = viewCount;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public boolean isIsLiveDefaultBroadcast() {
            return isLiveDefaultBroadcast;
        }

        public void setIsLiveDefaultBroadcast(boolean isLiveDefaultBroadcast) {
            this.isLiveDefaultBroadcast = isLiveDefaultBroadcast;
        }

        public boolean isIsLowLatencyLiveStream() {
            return isLowLatencyLiveStream;
        }

        public void setIsLowLatencyLiveStream(boolean isLowLatencyLiveStream) {
            this.isLowLatencyLiveStream = isLowLatencyLiveStream;
        }

        public boolean isIsPrivate() {
            return isPrivate;
        }

        public void setIsPrivate(boolean isPrivate) {
            this.isPrivate = isPrivate;
        }

        public boolean isIsUnpluggedCorpus() {
            return isUnpluggedCorpus;
        }

        public void setIsUnpluggedCorpus(boolean isUnpluggedCorpus) {
            this.isUnpluggedCorpus = isUnpluggedCorpus;
        }

        public String getLatencyClass() {
            return latencyClass;
        }

        public void setLatencyClass(String latencyClass) {
            this.latencyClass = latencyClass;
        }

        public boolean isIsLiveContent() {
            return isLiveContent;
        }

        public void setIsLiveContent(boolean isLiveContent) {
            this.isLiveContent = isLiveContent;
        }

        public List<String> getKeywords() {
            return keywords;
        }

        public void setKeywords(List<String> keywords) {
            this.keywords = keywords;
        }

        public static class ThumbnailBean {
            private List<ThumbnailsBean> thumbnails;

            public List<ThumbnailsBean> getThumbnails() {
                return thumbnails;
            }

            public void setThumbnails(List<ThumbnailsBean> thumbnails) {
                this.thumbnails = thumbnails;
            }

            public static class ThumbnailsBean {

                private String url;
                private int width;
                private int height;

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }

                public int getWidth() {
                    return width;
                }

                public void setWidth(int width) {
                    this.width = width;
                }

                public int getHeight() {
                    return height;
                }

                public void setHeight(int height) {
                    this.height = height;
                }
            }
        }
    }

    public static class PlayerConfigBean {

        private StreamSelectionConfigBean streamSelectionConfig;
        private LivePlayerConfigBean livePlayerConfig;
        private MediaCommonConfigBean mediaCommonConfig;

        public StreamSelectionConfigBean getStreamSelectionConfig() {
            return streamSelectionConfig;
        }

        public void setStreamSelectionConfig(StreamSelectionConfigBean streamSelectionConfig) {
            this.streamSelectionConfig = streamSelectionConfig;
        }

        public LivePlayerConfigBean getLivePlayerConfig() {
            return livePlayerConfig;
        }

        public void setLivePlayerConfig(LivePlayerConfigBean livePlayerConfig) {
            this.livePlayerConfig = livePlayerConfig;
        }

        public MediaCommonConfigBean getMediaCommonConfig() {
            return mediaCommonConfig;
        }

        public void setMediaCommonConfig(MediaCommonConfigBean mediaCommonConfig) {
            this.mediaCommonConfig = mediaCommonConfig;
        }

        public static class StreamSelectionConfigBean {

            private String maxBitrate;

            public String getMaxBitrate() {
                return maxBitrate;
            }

            public void setMaxBitrate(String maxBitrate) {
                this.maxBitrate = maxBitrate;
            }
        }

        public static class LivePlayerConfigBean {

            private double liveReadaheadSeconds;
            private boolean hasSubfragmentedFmp4;

            public double getLiveReadaheadSeconds() {
                return liveReadaheadSeconds;
            }

            public void setLiveReadaheadSeconds(double liveReadaheadSeconds) {
                this.liveReadaheadSeconds = liveReadaheadSeconds;
            }

            public boolean isHasSubfragmentedFmp4() {
                return hasSubfragmentedFmp4;
            }

            public void setHasSubfragmentedFmp4(boolean hasSubfragmentedFmp4) {
                this.hasSubfragmentedFmp4 = hasSubfragmentedFmp4;
            }
        }

        public static class MediaCommonConfigBean {

            private DynamicReadaheadConfigBean dynamicReadaheadConfig;

            public DynamicReadaheadConfigBean getDynamicReadaheadConfig() {
                return dynamicReadaheadConfig;
            }

            public void setDynamicReadaheadConfig(DynamicReadaheadConfigBean dynamicReadaheadConfig) {
                this.dynamicReadaheadConfig = dynamicReadaheadConfig;
            }

            public static class DynamicReadaheadConfigBean {

                private int maxReadAheadMediaTimeMs;
                private int minReadAheadMediaTimeMs;
                private int readAheadGrowthRateMs;

                public int getMaxReadAheadMediaTimeMs() {
                    return maxReadAheadMediaTimeMs;
                }

                public void setMaxReadAheadMediaTimeMs(int maxReadAheadMediaTimeMs) {
                    this.maxReadAheadMediaTimeMs = maxReadAheadMediaTimeMs;
                }

                public int getMinReadAheadMediaTimeMs() {
                    return minReadAheadMediaTimeMs;
                }

                public void setMinReadAheadMediaTimeMs(int minReadAheadMediaTimeMs) {
                    this.minReadAheadMediaTimeMs = minReadAheadMediaTimeMs;
                }

                public int getReadAheadGrowthRateMs() {
                    return readAheadGrowthRateMs;
                }

                public void setReadAheadGrowthRateMs(int readAheadGrowthRateMs) {
                    this.readAheadGrowthRateMs = readAheadGrowthRateMs;
                }
            }
        }
    }

    public static class StoryboardsBean {

        private PlayerLiveStoryboardSpecRendererBean playerLiveStoryboardSpecRenderer;

        public PlayerLiveStoryboardSpecRendererBean getPlayerLiveStoryboardSpecRenderer() {
            return playerLiveStoryboardSpecRenderer;
        }

        public void setPlayerLiveStoryboardSpecRenderer(PlayerLiveStoryboardSpecRendererBean playerLiveStoryboardSpecRenderer) {
            this.playerLiveStoryboardSpecRenderer = playerLiveStoryboardSpecRenderer;
        }

        public static class PlayerLiveStoryboardSpecRendererBean {

            private String spec;

            public String getSpec() {
                return spec;
            }

            public void setSpec(String spec) {
                this.spec = spec;
            }
        }
    }

    public static class AttestationBean {

        private PlayerAttestationRendererBean playerAttestationRenderer;

        public PlayerAttestationRendererBean getPlayerAttestationRenderer() {
            return playerAttestationRenderer;
        }

        public void setPlayerAttestationRenderer(PlayerAttestationRendererBean playerAttestationRenderer) {
            this.playerAttestationRenderer = playerAttestationRenderer;
        }

        public static class PlayerAttestationRendererBean {

            private String challenge;

            public String getChallenge() {
                return challenge;
            }

            public void setChallenge(String challenge) {
                this.challenge = challenge;
            }
        }
    }

    public static class AdSafetyReasonBean {

        private ApmUserPreferenceBean apmUserPreference;
        private boolean isEmbed;
        private boolean isRemarketingEnabled;
        private boolean isFocEnabled;

        public ApmUserPreferenceBean getApmUserPreference() {
            return apmUserPreference;
        }

        public void setApmUserPreference(ApmUserPreferenceBean apmUserPreference) {
            this.apmUserPreference = apmUserPreference;
        }

        public boolean isIsEmbed() {
            return isEmbed;
        }

        public void setIsEmbed(boolean isEmbed) {
            this.isEmbed = isEmbed;
        }

        public boolean isIsRemarketingEnabled() {
            return isRemarketingEnabled;
        }

        public void setIsRemarketingEnabled(boolean isRemarketingEnabled) {
            this.isRemarketingEnabled = isRemarketingEnabled;
        }

        public boolean isIsFocEnabled() {
            return isFocEnabled;
        }

        public void setIsFocEnabled(boolean isFocEnabled) {
            this.isFocEnabled = isFocEnabled;
        }

        public static class ApmUserPreferenceBean {
        }
    }

    public static class AnnotationsBean {

        private PlayerAnnotationsUrlsRendererBean playerAnnotationsUrlsRenderer;

        public PlayerAnnotationsUrlsRendererBean getPlayerAnnotationsUrlsRenderer() {
            return playerAnnotationsUrlsRenderer;
        }

        public void setPlayerAnnotationsUrlsRenderer(PlayerAnnotationsUrlsRendererBean playerAnnotationsUrlsRenderer) {
            this.playerAnnotationsUrlsRenderer = playerAnnotationsUrlsRenderer;
        }

        public static class PlayerAnnotationsUrlsRendererBean {

            private String invideoUrl;
            private String loadPolicy;
            private boolean allowInPlaceSwitch;

            public String getInvideoUrl() {
                return invideoUrl;
            }

            public void setInvideoUrl(String invideoUrl) {
                this.invideoUrl = invideoUrl;
            }

            public String getLoadPolicy() {
                return loadPolicy;
            }

            public void setLoadPolicy(String loadPolicy) {
                this.loadPolicy = loadPolicy;
            }

            public boolean isAllowInPlaceSwitch() {
                return allowInPlaceSwitch;
            }

            public void setAllowInPlaceSwitch(boolean allowInPlaceSwitch) {
                this.allowInPlaceSwitch = allowInPlaceSwitch;
            }
        }
    }
}
