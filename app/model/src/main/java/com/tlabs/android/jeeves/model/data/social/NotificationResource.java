package com.tlabs.android.jeeves.model.data.social;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

class NotificationResource {

    private static final NotificationResource resources;
    static {
        final ObjectMapper mapper = new ObjectMapper();
        try {
            resources = mapper.readValue(
                    NotificationResource.class.getResourceAsStream("/notifications.json"),
                    NotificationResource.class);
        }
        catch (IOException e) {
            throw new IllegalStateException(e);
        }

    }

    public static class Title {

        private long id;
        private long group;
        private String text;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public long getGroup() {
            return group;
        }

        public void setGroup(long group) {
            this.group = group;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    public static class Group {

        private long id;
        private String text;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    @JsonProperty
    private List<Title> titles;

    @JsonProperty
    private List<Group> groups;

    public List<Title> getTitles() {
        return titles;
    }

    public void setTitles(List<Title> titles) {
        this.titles = titles;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public static Title getTitle(final String key) {
        return getTitle(Long.parseLong(key));
    }

    public static Title getTitle(final long key) {
        for (Title e: resources.titles) {
            if (e.id == key) {
                return e;
            }
        }
        return null;
    }

    public static List<Group> getGroups() {
        return resources.groups;
    }
}
