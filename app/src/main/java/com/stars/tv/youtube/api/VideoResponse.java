package com.stars.tv.youtube.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VideoResponse {
    @SerializedName("kind")
    String kind;
    @SerializedName("items")
    List<Items> items;

    public String getKind() {
        return kind;
    }

    public List<Items> getItems() {
        return items;
    }

    public class Items{
        @SerializedName("kind")
        String kind;
        @SerializedName("id")
        String id;
        @SerializedName("snippet")
        private Snippet snippet;
        @SerializedName("contentDetails")
        private ContentDetails contentDetails;
        @SerializedName("statistics")
        private Statistics statistics;

        public Snippet getSnippet() {
            return snippet;
        }

        public String getId() {
            return id;
        }

        public ContentDetails getContentDetails() {
            return contentDetails;
        }

        public Statistics getStatistics() {
            return statistics;
        }

        public class Snippet{
            @SerializedName("title")
            String title;
            @SerializedName("publishedAt")
            String publishedAt;
            @SerializedName("description")
            String description;
            @SerializedName("channelTitle")
            String channelTitle;
            @SerializedName("channelId")
            String channelId;

            public String getPublishedAt() {
                return publishedAt;
            }

            public String getTitle() {
                return title;
            }

            public String getDescription() {
                return description;
            }

            public String getChannelTitle() {
                return channelTitle;
            }
        }

        public class ContentDetails{
            @SerializedName("duration")
            String duration;

            public String getDuration() {
                return duration;
            }
        }

        public class Statistics{
            @SerializedName("viewCount")
            int viewCount;
            @SerializedName("likeCount")
            int likeCount;
            @SerializedName("dislikeCount")
            int dislikeCount;

            public int getViewCount() {
                return viewCount;
            }

            public int getLikeCount() {
                return likeCount;
            }

            public int getDislikeCount() {
                return dislikeCount;
            }
        }
    }
}
