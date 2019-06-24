package com.stars.tv.bean;

import java.util.List;

public class YTM3U8Bean {

    /**
     * playabilityStatus : {"status":"OK","playableInEmbed":true,"liveStreamability":{"liveStreamabilityRenderer":{"videoId":"ibz7k3DjIeA","pollDelayMs":"7500"}}}
     * streamingData : {"expiresInSeconds":"21540","dashManifestUrl":"https://manifest.googlevideo.com/api/manifest/dash/expire/1561109372/ei/HE8MXZTYLbmXs8IPztmx2Aw/ip/59.124.136.10/id/ibz7k3DjIeA.0/source/yt_live_broadcast/requiressl/yes/as/fmp4_audio_clear%2Cwebm_audio_clear%2Cwebm2_audio_clear%2Cfmp4_sd_hd_clear%2Cwebm2_sd_hd_clear/pacing/0/itag/0/playlist_type/LIVE/sparams/expire%2Cei%2Cip%2Cid%2Csource%2Crequiressl%2Cas%2Citag%2Cplaylist_type/sig/ALgxI2wwRgIhAPWoDAcpYdpG1vdW_LHbrMA9d2ok_RKIUTuJdn7CVTExAiEA-MqxmT8xgL2-nRNS1wUqBhdvLvMPs4moAp1vLLw82XQ%3D","hlsManifestUrl":"https://manifest.googlevideo.com/api/manifest/hls_variant/expire/1561109372/ei/HE8MXZTYLbmXs8IPztmx2Aw/ip/59.124.136.10/id/ibz7k3DjIeA.0/source/yt_live_broadcast/requiressl/yes/hfr/1/playlist_duration/30/manifest_duration/30/maudio/1/go/1/keepalive/yes/dover/11/itag/0/playlist_type/DVR/sparams/expire%2Cei%2Cip%2Cid%2Csource%2Crequiressl%2Chfr%2Cplaylist_duration%2Cmanifest_duration%2Cmaudio%2Cgo%2Citag%2Cplaylist_type/sig/ALgxI2wwRgIhAN3c11rRAQtH5jY442cmfQPoMyHMg9WLIDVpG7us0Ws6AiEA1xABT4-CL4N9qlZ_cqIjMd96SesHWu2H3WInLZPrPQQ%3D/file/index.m3u8","probeUrl":"https://r5---sn-n4v7sn7s.googlevideo.com/videogoodput?id=o-AG4Z-ED702kq-j3_OjhSDwKEp7CpckRfyIUGVAAWLIlH&source=goodput&range=0-4999&expire=1561091372&ip=59.124.136.10&ms=pm&mm=35&pl=24&nh=IgpwcjAyLnBhbzAzKgkxMjcuMC4wLjE&sparams=id,source,range,expire,ip,ms,mm,pl,nh&signature=2E595D3F1EFFB24731E7EA537612E601A54B738B.0A2B6A6FCE1BAEE81E36CF6F27B173E825B9CCD0&key=cms1"}
     * playbackTracking : {"videostatsPlaybackUrl":{"baseUrl":"https://s.youtube.com/api/stats/playback?cl=253883325&docid=ibz7k3DjIeA&ei=HE8MXZTYLbmXs8IPztmx2Aw&fexp=23811378%2C23822435%2C23820266%2C23736684%2C23818213%2C23822161%2C23805410%2C23814135%2C23804281%2C23817543%2C23811453%2C23811427%2C23818122%2C23817114%2C23819595%2C1714243%2C23818585%2C23757411%2C23817149%2C23804908%2C9488571%2C23780337&live=live&ns=yt&plid=AAWLzRIrtioe5DMS&delay=5&el=embedded&len=0&of=1elpPrSFyAjPnmLc4xeyWw&vm=CAEQARgE"},"videostatsDelayplayUrl":{"baseUrl":"https://s.youtube.com/api/stats/delayplay?cl=253883325&docid=ibz7k3DjIeA&ei=HE8MXZTYLbmXs8IPztmx2Aw&fexp=23811378%2C23822435%2C23820266%2C23736684%2C23818213%2C23822161%2C23805410%2C23814135%2C23804281%2C23817543%2C23811453%2C23811427%2C23818122%2C23817114%2C23819595%2C1714243%2C23818585%2C23757411%2C23817149%2C23804908%2C9488571%2C23780337&live=live&ns=yt&plid=AAWLzRIrtioe5DMS&delay=5&el=embedded&len=0&of=1elpPrSFyAjPnmLc4xeyWw&vm=CAEQARgE","elapsedMediaTimeSeconds":5},"videostatsWatchtimeUrl":{"baseUrl":"https://s.youtube.com/api/stats/watchtime?cl=253883325&docid=ibz7k3DjIeA&ei=HE8MXZTYLbmXs8IPztmx2Aw&fexp=23811378%2C23822435%2C23820266%2C23736684%2C23818213%2C23822161%2C23805410%2C23814135%2C23804281%2C23817543%2C23811453%2C23811427%2C23818122%2C23817114%2C23819595%2C1714243%2C23818585%2C23757411%2C23817149%2C23804908%2C9488571%2C23780337&live=live&ns=yt&plid=AAWLzRIrtioe5DMS&el=embedded&len=0&of=1elpPrSFyAjPnmLc4xeyWw&vm=CAEQARgE"},"ptrackingUrl":{"baseUrl":"https://www.youtube.com/ptracking?ei=HE8MXZTYLbmXs8IPztmx2Aw&oid=9BqkSXQzn-DvCnA2lS07Rw&plid=AAWLzRIrtioe5DMS&pltype=contentlive&ptchn=uzqko_GKcj9922M1gUo__w&ptk=youtube_single&video_id=ibz7k3DjIeA"},"qoeUrl":{"baseUrl":"https://s.youtube.com/api/stats/qoe?cl=253883325&docid=ibz7k3DjIeA&ei=HE8MXZTYLbmXs8IPztmx2Aw&event=streamingstats&fexp=23811378%2C23822435%2C23820266%2C23736684%2C23818213%2C23822161%2C23805410%2C23814135%2C23804281%2C23817543%2C23811453%2C23811427%2C23818122%2C23817114%2C23819595%2C1714243%2C23818585%2C23757411%2C23817149%2C23804908%2C9488571%2C23780337&live=live&ns=yt&plid=AAWLzRIrtioe5DMS"},"setAwesomeUrl":{"baseUrl":"https://www.youtube.com/set_awesome?ei=HE8MXZTYLbmXs8IPztmx2Aw&plid=AAWLzRIrtioe5DMS&video_id=ibz7k3DjIeA","elapsedMediaTimeSeconds":0},"atrUrl":{"baseUrl":"https://s.youtube.com/api/stats/atr?docid=ibz7k3DjIeA&ei=HE8MXZTYLbmXs8IPztmx2Aw&len=0&ns=yt&plid=AAWLzRIrtioe5DMS&ver=2","elapsedMediaTimeSeconds":5},"youtubeRemarketingUrl":{"baseUrl":"https://www.youtube.com/pagead/viewthroughconversion/962985656/?backend=innertube&cname=1&cver=2_20190619&foc_id=uzqko_GKcj9922M1gUo__w&label=followon_view&ptype=no_rmkt&random=754623177","elapsedMediaTimeSeconds":0}}
     * videoDetails : {"videoId":"ibz7k3DjIeA","title":"EBC 東森財經新聞 24小時線上直播｜Taiwan EBC Financial News 24h live｜台湾 EBC 金融ニュース24 時間オンライン放送｜대만 뉴스 생방송","lengthSeconds":"0","isLive":true,"keywords":["賺大錢","金價","股價","台股","A股","房市","基金","看盤","外匯","金融","銀行","投資","理財","小資","黃金","紅盤","金管會","證交所","證交稅","房市大跌","稅金","美元","人民幣","新台幣","石油","原油","房貸","信貸","定存","利率","央行"],"channelId":"UCuzqko_GKcj9922M1gUo__w","isOwnerViewing":false,"shortDescription":"鎖定財經新聞直播，請上東森財經官網 https://fnc.ebc.net.tw/\n\n【57金錢爆 - 播出完整版】 https://goo.gl/D7fK9T\n【57爆新聞 - 播出完整版】https://goo.gl/UGAWB2\n【57夢想街之全能事務所 - 播出完整版】http://pse.ee/5S9TP\n【夢想街57號 - 播出完整版】https://goo.gl/V51C3c\n\n【東森財經新聞YouTube】 https://www.youtube.com/user/57ETFN\n【東森財經新聞粉絲團】https://www.facebook.com/ebcmoney\n\n備註：您收看的畫面會比電視播出延遲30秒。","isCrawlable":true,"isLiveDvrEnabled":false,"thumbnail":{"thumbnails":[{"url":"https://i.ytimg.com/vi/ibz7k3DjIeA/hqdefault_live.jpg?sqp=CIycsegF-oaymwEYCKgBEF5IVfKriqkDCwgBFQAAiEIYAXAB&rs=AOn4CLCuogGgd3-hu1Cwh47byBVXDtIcWA","width":168,"height":94},{"url":"https://i.ytimg.com/vi/ibz7k3DjIeA/hqdefault_live.jpg?sqp=CIycsegF-oaymwEYCMQBEG5IVfKriqkDCwgBFQAAiEIYAXAB&rs=AOn4CLAXUIiMDoJs_ISFIsrukSJOuaLT_g","width":196,"height":110},{"url":"https://i.ytimg.com/vi/ibz7k3DjIeA/hqdefault_live.jpg?sqp=CIycsegF-oaymwEZCPYBEIoBSFXyq4qpAwsIARUAAIhCGAFwAQ==&rs=AOn4CLCRCdtLJXb0MJ0uaeUXen8LmuU-Kw","width":246,"height":138},{"url":"https://i.ytimg.com/vi/ibz7k3DjIeA/hqdefault_live.jpg?sqp=CIycsegF-oaymwEZCNACELwBSFXyq4qpAwsIARUAAIhCGAFwAQ==&rs=AOn4CLCgzI-ylUih-3sOhaiJRTTE4ozDiA","width":336,"height":188}]},"useCipher":false,"liveChunkReadahead":3,"averageRating":4.0174756,"allowRatings":true,"viewCount":"631381","author":"５７東森財經新聞台","isLiveDefaultBroadcast":true,"isLowLatencyLiveStream":true,"isPrivate":false,"isUnpluggedCorpus":false,"latencyClass":"MDE_STREAM_OPTIMIZATIONS_RENDERER_LATENCY_LOW","isLiveContent":true}
     * annotations : [{"playerAnnotationsUrlsRenderer":{"invideoUrl":"//www.youtube.com/annotations_invideo?cap_hist=1&video_id=ibz7k3DjIeA&client=1","loadPolicy":"ALWAYS","allowInPlaceSwitch":true}}]
     * playerConfig : {"streamSelectionConfig":{"maxBitrate":"7730000"},"livePlayerConfig":{"liveReadaheadSeconds":4.800000000000001,"hasSubfragmentedFmp4":true},"mediaCommonConfig":{"dynamicReadaheadConfig":{"maxReadAheadMediaTimeMs":120000,"minReadAheadMediaTimeMs":15000,"readAheadGrowthRateMs":1000}}}
     * storyboards : {"playerLiveStoryboardSpecRenderer":{"spec":"https://i.ytimg.com/sb/ibz7k3DjIeA/storyboard_live_60_3x3_b0/M$M.jpg?rs=AOn4CLDdMJhV6LVUM2Mw_ya-Fxaj-Ob9_w#106#60#3#3"}}
     * trackingParams : CAEQu2kiEwiU1qGR0fniAhW5y0wCHc5sDMso6NQB
     * attestation : {"playerAttestationRenderer":{"challenge":"a=4&b=_osbWMosr-XpbJSn3e29dXRSQ8w&c=1561087772&d=1&e=ibz7k3DjIeA&c3a=15&c1a=1&c6a=1&hh=W1ZlYaGaK7XPhnIB5vhcgdY3qpIGiHXyhpH59DvZS5s"}}
     * adSafetyReason : {"apmUserPreference":{},"isEmbed":true,"isRemarketingEnabled":true,"isFocEnabled":true}
     */

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
        /**
         * status : OK
         * playableInEmbed : true
         * liveStreamability : {"liveStreamabilityRenderer":{"videoId":"ibz7k3DjIeA","pollDelayMs":"7500"}}
         */

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
            /**
             * liveStreamabilityRenderer : {"videoId":"ibz7k3DjIeA","pollDelayMs":"7500"}
             */

            private LiveStreamabilityRendererBean liveStreamabilityRenderer;

            public LiveStreamabilityRendererBean getLiveStreamabilityRenderer() {
                return liveStreamabilityRenderer;
            }

            public void setLiveStreamabilityRenderer(LiveStreamabilityRendererBean liveStreamabilityRenderer) {
                this.liveStreamabilityRenderer = liveStreamabilityRenderer;
            }

            public static class LiveStreamabilityRendererBean {
                /**
                 * videoId : ibz7k3DjIeA
                 * pollDelayMs : 7500
                 */

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
        /**
         * expiresInSeconds : 21540
         * dashManifestUrl : https://manifest.googlevideo.com/api/manifest/dash/expire/1561109372/ei/HE8MXZTYLbmXs8IPztmx2Aw/ip/59.124.136.10/id/ibz7k3DjIeA.0/source/yt_live_broadcast/requiressl/yes/as/fmp4_audio_clear%2Cwebm_audio_clear%2Cwebm2_audio_clear%2Cfmp4_sd_hd_clear%2Cwebm2_sd_hd_clear/pacing/0/itag/0/playlist_type/LIVE/sparams/expire%2Cei%2Cip%2Cid%2Csource%2Crequiressl%2Cas%2Citag%2Cplaylist_type/sig/ALgxI2wwRgIhAPWoDAcpYdpG1vdW_LHbrMA9d2ok_RKIUTuJdn7CVTExAiEA-MqxmT8xgL2-nRNS1wUqBhdvLvMPs4moAp1vLLw82XQ%3D
         * hlsManifestUrl : https://manifest.googlevideo.com/api/manifest/hls_variant/expire/1561109372/ei/HE8MXZTYLbmXs8IPztmx2Aw/ip/59.124.136.10/id/ibz7k3DjIeA.0/source/yt_live_broadcast/requiressl/yes/hfr/1/playlist_duration/30/manifest_duration/30/maudio/1/go/1/keepalive/yes/dover/11/itag/0/playlist_type/DVR/sparams/expire%2Cei%2Cip%2Cid%2Csource%2Crequiressl%2Chfr%2Cplaylist_duration%2Cmanifest_duration%2Cmaudio%2Cgo%2Citag%2Cplaylist_type/sig/ALgxI2wwRgIhAN3c11rRAQtH5jY442cmfQPoMyHMg9WLIDVpG7us0Ws6AiEA1xABT4-CL4N9qlZ_cqIjMd96SesHWu2H3WInLZPrPQQ%3D/file/index.m3u8
         * probeUrl : https://r5---sn-n4v7sn7s.googlevideo.com/videogoodput?id=o-AG4Z-ED702kq-j3_OjhSDwKEp7CpckRfyIUGVAAWLIlH&source=goodput&range=0-4999&expire=1561091372&ip=59.124.136.10&ms=pm&mm=35&pl=24&nh=IgpwcjAyLnBhbzAzKgkxMjcuMC4wLjE&sparams=id,source,range,expire,ip,ms,mm,pl,nh&signature=2E595D3F1EFFB24731E7EA537612E601A54B738B.0A2B6A6FCE1BAEE81E36CF6F27B173E825B9CCD0&key=cms1
         */

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
        /**
         * videostatsPlaybackUrl : {"baseUrl":"https://s.youtube.com/api/stats/playback?cl=253883325&docid=ibz7k3DjIeA&ei=HE8MXZTYLbmXs8IPztmx2Aw&fexp=23811378%2C23822435%2C23820266%2C23736684%2C23818213%2C23822161%2C23805410%2C23814135%2C23804281%2C23817543%2C23811453%2C23811427%2C23818122%2C23817114%2C23819595%2C1714243%2C23818585%2C23757411%2C23817149%2C23804908%2C9488571%2C23780337&live=live&ns=yt&plid=AAWLzRIrtioe5DMS&delay=5&el=embedded&len=0&of=1elpPrSFyAjPnmLc4xeyWw&vm=CAEQARgE"}
         * videostatsDelayplayUrl : {"baseUrl":"https://s.youtube.com/api/stats/delayplay?cl=253883325&docid=ibz7k3DjIeA&ei=HE8MXZTYLbmXs8IPztmx2Aw&fexp=23811378%2C23822435%2C23820266%2C23736684%2C23818213%2C23822161%2C23805410%2C23814135%2C23804281%2C23817543%2C23811453%2C23811427%2C23818122%2C23817114%2C23819595%2C1714243%2C23818585%2C23757411%2C23817149%2C23804908%2C9488571%2C23780337&live=live&ns=yt&plid=AAWLzRIrtioe5DMS&delay=5&el=embedded&len=0&of=1elpPrSFyAjPnmLc4xeyWw&vm=CAEQARgE","elapsedMediaTimeSeconds":5}
         * videostatsWatchtimeUrl : {"baseUrl":"https://s.youtube.com/api/stats/watchtime?cl=253883325&docid=ibz7k3DjIeA&ei=HE8MXZTYLbmXs8IPztmx2Aw&fexp=23811378%2C23822435%2C23820266%2C23736684%2C23818213%2C23822161%2C23805410%2C23814135%2C23804281%2C23817543%2C23811453%2C23811427%2C23818122%2C23817114%2C23819595%2C1714243%2C23818585%2C23757411%2C23817149%2C23804908%2C9488571%2C23780337&live=live&ns=yt&plid=AAWLzRIrtioe5DMS&el=embedded&len=0&of=1elpPrSFyAjPnmLc4xeyWw&vm=CAEQARgE"}
         * ptrackingUrl : {"baseUrl":"https://www.youtube.com/ptracking?ei=HE8MXZTYLbmXs8IPztmx2Aw&oid=9BqkSXQzn-DvCnA2lS07Rw&plid=AAWLzRIrtioe5DMS&pltype=contentlive&ptchn=uzqko_GKcj9922M1gUo__w&ptk=youtube_single&video_id=ibz7k3DjIeA"}
         * qoeUrl : {"baseUrl":"https://s.youtube.com/api/stats/qoe?cl=253883325&docid=ibz7k3DjIeA&ei=HE8MXZTYLbmXs8IPztmx2Aw&event=streamingstats&fexp=23811378%2C23822435%2C23820266%2C23736684%2C23818213%2C23822161%2C23805410%2C23814135%2C23804281%2C23817543%2C23811453%2C23811427%2C23818122%2C23817114%2C23819595%2C1714243%2C23818585%2C23757411%2C23817149%2C23804908%2C9488571%2C23780337&live=live&ns=yt&plid=AAWLzRIrtioe5DMS"}
         * setAwesomeUrl : {"baseUrl":"https://www.youtube.com/set_awesome?ei=HE8MXZTYLbmXs8IPztmx2Aw&plid=AAWLzRIrtioe5DMS&video_id=ibz7k3DjIeA","elapsedMediaTimeSeconds":0}
         * atrUrl : {"baseUrl":"https://s.youtube.com/api/stats/atr?docid=ibz7k3DjIeA&ei=HE8MXZTYLbmXs8IPztmx2Aw&len=0&ns=yt&plid=AAWLzRIrtioe5DMS&ver=2","elapsedMediaTimeSeconds":5}
         * youtubeRemarketingUrl : {"baseUrl":"https://www.youtube.com/pagead/viewthroughconversion/962985656/?backend=innertube&cname=1&cver=2_20190619&foc_id=uzqko_GKcj9922M1gUo__w&label=followon_view&ptype=no_rmkt&random=754623177","elapsedMediaTimeSeconds":0}
         */

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
            /**
             * baseUrl : https://s.youtube.com/api/stats/playback?cl=253883325&docid=ibz7k3DjIeA&ei=HE8MXZTYLbmXs8IPztmx2Aw&fexp=23811378%2C23822435%2C23820266%2C23736684%2C23818213%2C23822161%2C23805410%2C23814135%2C23804281%2C23817543%2C23811453%2C23811427%2C23818122%2C23817114%2C23819595%2C1714243%2C23818585%2C23757411%2C23817149%2C23804908%2C9488571%2C23780337&live=live&ns=yt&plid=AAWLzRIrtioe5DMS&delay=5&el=embedded&len=0&of=1elpPrSFyAjPnmLc4xeyWw&vm=CAEQARgE
             */

            private String baseUrl;

            public String getBaseUrl() {
                return baseUrl;
            }

            public void setBaseUrl(String baseUrl) {
                this.baseUrl = baseUrl;
            }
        }

        public static class VideostatsDelayplayUrlBean {
            /**
             * baseUrl : https://s.youtube.com/api/stats/delayplay?cl=253883325&docid=ibz7k3DjIeA&ei=HE8MXZTYLbmXs8IPztmx2Aw&fexp=23811378%2C23822435%2C23820266%2C23736684%2C23818213%2C23822161%2C23805410%2C23814135%2C23804281%2C23817543%2C23811453%2C23811427%2C23818122%2C23817114%2C23819595%2C1714243%2C23818585%2C23757411%2C23817149%2C23804908%2C9488571%2C23780337&live=live&ns=yt&plid=AAWLzRIrtioe5DMS&delay=5&el=embedded&len=0&of=1elpPrSFyAjPnmLc4xeyWw&vm=CAEQARgE
             * elapsedMediaTimeSeconds : 5
             */

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
            /**
             * baseUrl : https://s.youtube.com/api/stats/watchtime?cl=253883325&docid=ibz7k3DjIeA&ei=HE8MXZTYLbmXs8IPztmx2Aw&fexp=23811378%2C23822435%2C23820266%2C23736684%2C23818213%2C23822161%2C23805410%2C23814135%2C23804281%2C23817543%2C23811453%2C23811427%2C23818122%2C23817114%2C23819595%2C1714243%2C23818585%2C23757411%2C23817149%2C23804908%2C9488571%2C23780337&live=live&ns=yt&plid=AAWLzRIrtioe5DMS&el=embedded&len=0&of=1elpPrSFyAjPnmLc4xeyWw&vm=CAEQARgE
             */

            private String baseUrl;

            public String getBaseUrl() {
                return baseUrl;
            }

            public void setBaseUrl(String baseUrl) {
                this.baseUrl = baseUrl;
            }
        }

        public static class PtrackingUrlBean {
            /**
             * baseUrl : https://www.youtube.com/ptracking?ei=HE8MXZTYLbmXs8IPztmx2Aw&oid=9BqkSXQzn-DvCnA2lS07Rw&plid=AAWLzRIrtioe5DMS&pltype=contentlive&ptchn=uzqko_GKcj9922M1gUo__w&ptk=youtube_single&video_id=ibz7k3DjIeA
             */

            private String baseUrl;

            public String getBaseUrl() {
                return baseUrl;
            }

            public void setBaseUrl(String baseUrl) {
                this.baseUrl = baseUrl;
            }
        }

        public static class QoeUrlBean {
            /**
             * baseUrl : https://s.youtube.com/api/stats/qoe?cl=253883325&docid=ibz7k3DjIeA&ei=HE8MXZTYLbmXs8IPztmx2Aw&event=streamingstats&fexp=23811378%2C23822435%2C23820266%2C23736684%2C23818213%2C23822161%2C23805410%2C23814135%2C23804281%2C23817543%2C23811453%2C23811427%2C23818122%2C23817114%2C23819595%2C1714243%2C23818585%2C23757411%2C23817149%2C23804908%2C9488571%2C23780337&live=live&ns=yt&plid=AAWLzRIrtioe5DMS
             */

            private String baseUrl;

            public String getBaseUrl() {
                return baseUrl;
            }

            public void setBaseUrl(String baseUrl) {
                this.baseUrl = baseUrl;
            }
        }

        public static class SetAwesomeUrlBean {
            /**
             * baseUrl : https://www.youtube.com/set_awesome?ei=HE8MXZTYLbmXs8IPztmx2Aw&plid=AAWLzRIrtioe5DMS&video_id=ibz7k3DjIeA
             * elapsedMediaTimeSeconds : 0
             */

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
            /**
             * baseUrl : https://s.youtube.com/api/stats/atr?docid=ibz7k3DjIeA&ei=HE8MXZTYLbmXs8IPztmx2Aw&len=0&ns=yt&plid=AAWLzRIrtioe5DMS&ver=2
             * elapsedMediaTimeSeconds : 5
             */

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
            /**
             * baseUrl : https://www.youtube.com/pagead/viewthroughconversion/962985656/?backend=innertube&cname=1&cver=2_20190619&foc_id=uzqko_GKcj9922M1gUo__w&label=followon_view&ptype=no_rmkt&random=754623177
             * elapsedMediaTimeSeconds : 0
             */

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
        /**
         * videoId : ibz7k3DjIeA
         * title : EBC 東森財經新聞 24小時線上直播｜Taiwan EBC Financial News 24h live｜台湾 EBC 金融ニュース24 時間オンライン放送｜대만 뉴스 생방송
         * lengthSeconds : 0
         * isLive : true
         * keywords : ["賺大錢","金價","股價","台股","A股","房市","基金","看盤","外匯","金融","銀行","投資","理財","小資","黃金","紅盤","金管會","證交所","證交稅","房市大跌","稅金","美元","人民幣","新台幣","石油","原油","房貸","信貸","定存","利率","央行"]
         * channelId : UCuzqko_GKcj9922M1gUo__w
         * isOwnerViewing : false
         * shortDescription : 鎖定財經新聞直播，請上東森財經官網 https://fnc.ebc.net.tw/

         【57金錢爆 - 播出完整版】 https://goo.gl/D7fK9T
         【57爆新聞 - 播出完整版】https://goo.gl/UGAWB2
         【57夢想街之全能事務所 - 播出完整版】http://pse.ee/5S9TP
         【夢想街57號 - 播出完整版】https://goo.gl/V51C3c

         【東森財經新聞YouTube】 https://www.youtube.com/user/57ETFN
         【東森財經新聞粉絲團】https://www.facebook.com/ebcmoney

         備註：您收看的畫面會比電視播出延遲30秒。
         * isCrawlable : true
         * isLiveDvrEnabled : false
         * thumbnail : {"thumbnails":[{"url":"https://i.ytimg.com/vi/ibz7k3DjIeA/hqdefault_live.jpg?sqp=CIycsegF-oaymwEYCKgBEF5IVfKriqkDCwgBFQAAiEIYAXAB&rs=AOn4CLCuogGgd3-hu1Cwh47byBVXDtIcWA","width":168,"height":94},{"url":"https://i.ytimg.com/vi/ibz7k3DjIeA/hqdefault_live.jpg?sqp=CIycsegF-oaymwEYCMQBEG5IVfKriqkDCwgBFQAAiEIYAXAB&rs=AOn4CLAXUIiMDoJs_ISFIsrukSJOuaLT_g","width":196,"height":110},{"url":"https://i.ytimg.com/vi/ibz7k3DjIeA/hqdefault_live.jpg?sqp=CIycsegF-oaymwEZCPYBEIoBSFXyq4qpAwsIARUAAIhCGAFwAQ==&rs=AOn4CLCRCdtLJXb0MJ0uaeUXen8LmuU-Kw","width":246,"height":138},{"url":"https://i.ytimg.com/vi/ibz7k3DjIeA/hqdefault_live.jpg?sqp=CIycsegF-oaymwEZCNACELwBSFXyq4qpAwsIARUAAIhCGAFwAQ==&rs=AOn4CLCgzI-ylUih-3sOhaiJRTTE4ozDiA","width":336,"height":188}]}
         * useCipher : false
         * liveChunkReadahead : 3
         * averageRating : 4.0174756
         * allowRatings : true
         * viewCount : 631381
         * author : ５７東森財經新聞台
         * isLiveDefaultBroadcast : true
         * isLowLatencyLiveStream : true
         * isPrivate : false
         * isUnpluggedCorpus : false
         * latencyClass : MDE_STREAM_OPTIMIZATIONS_RENDERER_LATENCY_LOW
         * isLiveContent : true
         */

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
                /**
                 * url : https://i.ytimg.com/vi/ibz7k3DjIeA/hqdefault_live.jpg?sqp=CIycsegF-oaymwEYCKgBEF5IVfKriqkDCwgBFQAAiEIYAXAB&rs=AOn4CLCuogGgd3-hu1Cwh47byBVXDtIcWA
                 * width : 168
                 * height : 94
                 */

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
        /**
         * streamSelectionConfig : {"maxBitrate":"7730000"}
         * livePlayerConfig : {"liveReadaheadSeconds":4.800000000000001,"hasSubfragmentedFmp4":true}
         * mediaCommonConfig : {"dynamicReadaheadConfig":{"maxReadAheadMediaTimeMs":120000,"minReadAheadMediaTimeMs":15000,"readAheadGrowthRateMs":1000}}
         */

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
            /**
             * maxBitrate : 7730000
             */

            private String maxBitrate;

            public String getMaxBitrate() {
                return maxBitrate;
            }

            public void setMaxBitrate(String maxBitrate) {
                this.maxBitrate = maxBitrate;
            }
        }

        public static class LivePlayerConfigBean {
            /**
             * liveReadaheadSeconds : 4.800000000000001
             * hasSubfragmentedFmp4 : true
             */

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
            /**
             * dynamicReadaheadConfig : {"maxReadAheadMediaTimeMs":120000,"minReadAheadMediaTimeMs":15000,"readAheadGrowthRateMs":1000}
             */

            private DynamicReadaheadConfigBean dynamicReadaheadConfig;

            public DynamicReadaheadConfigBean getDynamicReadaheadConfig() {
                return dynamicReadaheadConfig;
            }

            public void setDynamicReadaheadConfig(DynamicReadaheadConfigBean dynamicReadaheadConfig) {
                this.dynamicReadaheadConfig = dynamicReadaheadConfig;
            }

            public static class DynamicReadaheadConfigBean {
                /**
                 * maxReadAheadMediaTimeMs : 120000
                 * minReadAheadMediaTimeMs : 15000
                 * readAheadGrowthRateMs : 1000
                 */

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
        /**
         * playerLiveStoryboardSpecRenderer : {"spec":"https://i.ytimg.com/sb/ibz7k3DjIeA/storyboard_live_60_3x3_b0/M$M.jpg?rs=AOn4CLDdMJhV6LVUM2Mw_ya-Fxaj-Ob9_w#106#60#3#3"}
         */

        private PlayerLiveStoryboardSpecRendererBean playerLiveStoryboardSpecRenderer;

        public PlayerLiveStoryboardSpecRendererBean getPlayerLiveStoryboardSpecRenderer() {
            return playerLiveStoryboardSpecRenderer;
        }

        public void setPlayerLiveStoryboardSpecRenderer(PlayerLiveStoryboardSpecRendererBean playerLiveStoryboardSpecRenderer) {
            this.playerLiveStoryboardSpecRenderer = playerLiveStoryboardSpecRenderer;
        }

        public static class PlayerLiveStoryboardSpecRendererBean {
            /**
             * spec : https://i.ytimg.com/sb/ibz7k3DjIeA/storyboard_live_60_3x3_b0/M$M.jpg?rs=AOn4CLDdMJhV6LVUM2Mw_ya-Fxaj-Ob9_w#106#60#3#3
             */

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
        /**
         * playerAttestationRenderer : {"challenge":"a=4&b=_osbWMosr-XpbJSn3e29dXRSQ8w&c=1561087772&d=1&e=ibz7k3DjIeA&c3a=15&c1a=1&c6a=1&hh=W1ZlYaGaK7XPhnIB5vhcgdY3qpIGiHXyhpH59DvZS5s"}
         */

        private PlayerAttestationRendererBean playerAttestationRenderer;

        public PlayerAttestationRendererBean getPlayerAttestationRenderer() {
            return playerAttestationRenderer;
        }

        public void setPlayerAttestationRenderer(PlayerAttestationRendererBean playerAttestationRenderer) {
            this.playerAttestationRenderer = playerAttestationRenderer;
        }

        public static class PlayerAttestationRendererBean {
            /**
             * challenge : a=4&b=_osbWMosr-XpbJSn3e29dXRSQ8w&c=1561087772&d=1&e=ibz7k3DjIeA&c3a=15&c1a=1&c6a=1&hh=W1ZlYaGaK7XPhnIB5vhcgdY3qpIGiHXyhpH59DvZS5s
             */

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
        /**
         * apmUserPreference : {}
         * isEmbed : true
         * isRemarketingEnabled : true
         * isFocEnabled : true
         */

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
        /**
         * playerAnnotationsUrlsRenderer : {"invideoUrl":"//www.youtube.com/annotations_invideo?cap_hist=1&video_id=ibz7k3DjIeA&client=1","loadPolicy":"ALWAYS","allowInPlaceSwitch":true}
         */

        private PlayerAnnotationsUrlsRendererBean playerAnnotationsUrlsRenderer;

        public PlayerAnnotationsUrlsRendererBean getPlayerAnnotationsUrlsRenderer() {
            return playerAnnotationsUrlsRenderer;
        }

        public void setPlayerAnnotationsUrlsRenderer(PlayerAnnotationsUrlsRendererBean playerAnnotationsUrlsRenderer) {
            this.playerAnnotationsUrlsRenderer = playerAnnotationsUrlsRenderer;
        }

        public static class PlayerAnnotationsUrlsRendererBean {
            /**
             * invideoUrl : //www.youtube.com/annotations_invideo?cap_hist=1&video_id=ibz7k3DjIeA&client=1
             * loadPolicy : ALWAYS
             * allowInPlaceSwitch : true
             */

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
