package com.stars.tv.youtube.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchResponse {
    @SerializedName("kind")
    private String kind;
    @SerializedName("etag")
    private String etag;
    @SerializedName("pageInfo")
    private PageInfo pageInfo;
    @SerializedName("items")
    private List<Items> items;

    public String getKind() {
        return kind;
    }

    public String getEtag() {
        return etag;
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public List<Items> getItems() {
        return items;
    }

    class PageInfo {
        String totalResults, resultsPerPage;

        public PageInfo(String totalResults, String resultsPerPage) {
            this.totalResults = totalResults;
            this.resultsPerPage = resultsPerPage;
        }

        public String getTotalResults() {
            return totalResults;
        }

        public String getResultsPerPage() {
            return resultsPerPage;
        }
    }

    public class Items{
        @SerializedName("kind")
        private String kind;
        @SerializedName("etag")
        private String etag;
        @SerializedName("id")
        private ID id;
        @SerializedName("snippet")
        private Snippet snippet;

        public String getKind() {
            return kind;
        }

        public String getEtag() {
            return etag;
        }

        public ID getId() {
            return id;
        }

        public Snippet getSnippet() {
            return snippet;
        }

        public class ID{
            @SerializedName("kind")
            private String kind;
            @SerializedName("videoId")
            private String videoId;
            @SerializedName("channelId")
            private String channelId;
            @SerializedName("playlistId")
            private String playlistId;

            public String getKind() {
                return kind;
            }

            public String getVideoId() {
                return videoId;
            }

            public String getChannelId() {
                return channelId;
            }

            public String getPlaylistId() {
                return playlistId;
            }
        }

        public class Snippet{
            @SerializedName("title")
            String title;
            @SerializedName("publishedAt")
            String publishedAt;
            @SerializedName("description")
            String description;
            @SerializedName("channelTitle")
            private String channelTitle;

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
    }
}
