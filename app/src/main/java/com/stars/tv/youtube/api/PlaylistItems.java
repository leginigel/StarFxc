package com.stars.tv.youtube.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlaylistItems {
    @SerializedName("kind")
    private String kind;
    @SerializedName("items")
    private List<Items> items;

    public String getKind() {
        return kind;
    }

    public List<Items> getItems() {
        return items;
    }

    public class Items{
        @SerializedName("kind")
        private String kind;
        @SerializedName("id")
        private String id;
        @SerializedName("pageInfo")
        private PageInfo pageInfo;
        @SerializedName("snippet")
        private Snippet snippet;

        public String getKind() {
            return kind;
        }

        public PageInfo getPageInfo() {
            return pageInfo;
        }

        public Snippet getSnippet() {
            return snippet;
        }

        public class PageInfo{
            @SerializedName("totalResults")
            private int totalResults;
            @SerializedName("resultsPerpage")
            private int resultsPerpage;

            public int getResultsPerpage() {
                return resultsPerpage;
            }
        }

        public class Snippet{
            @SerializedName("title")
            String title;
            @SerializedName("publishedAt")
            String publishedAt;
            @SerializedName("description")
            String description;
            @SerializedName("channelId")
            private String channelId;
            @SerializedName("channelTitle")
            private String channelTitle;
            @SerializedName("playlistId")
            private String playlistId;
            @SerializedName("position")
            private int position;
            @SerializedName("resourceId")
            private ResourceId resourceId;

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

            public ResourceId getResourceId() {
                return resourceId;
            }

            public class ResourceId{
                @SerializedName("kind")
                private String kind;
                @SerializedName("videoId")
                private String videoId;

                public String getKind() {
                    return kind;
                }

                public String getVideoId() {
                    return videoId;
                }
            }
        }
    }
}
