package com.stars.tv.youtube.api;

import org.simpleframework.xml.Attribute;

public class Suggestion {
    @Attribute(name = "data", required = false)
    private String data;

    public String getData() {
        return data;
    }
}
