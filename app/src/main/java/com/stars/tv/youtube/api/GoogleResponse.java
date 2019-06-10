package com.stars.tv.youtube.api;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "toplevel", strict = false)
public class GoogleResponse {
    @ElementList(inline = true, required = false)
    private List<CompleteSuggestion> CompleteSuggestion;

    public List<CompleteSuggestion> getCompleteSuggestion() {
        return CompleteSuggestion;
    }

}
