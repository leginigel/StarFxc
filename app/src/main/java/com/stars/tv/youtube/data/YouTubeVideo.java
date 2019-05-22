package com.stars.tv.youtube.data;

public class YouTubeVideo {

    private String id;

    private String title;

    private String channel;

    private int number_views;

    private String time;

    private String duration;

    public YouTubeVideo(String id, String title, String channel, int number_views, String time, String duration) {
        this.id = id;
        this.title = title;
        this.channel = channel;
        this.number_views = number_views;
        this.time = time;
        this.duration = duration;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getChannel() {
        return channel;
    }

    public int getNumber_views() {
        return number_views;
    }

    public String getTime() {
        return time;
    }

    public String getDuration() {
        return duration;
    }
}
