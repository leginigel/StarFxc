package com.stars.tv.youtube.api;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "CompleteSuggestion")
public class CompleteSuggestion {

    @Element(name = "suggestion")
    private Suggestion suggestion;


    public Suggestion getSuggestion() {
        return suggestion;
    }

}
