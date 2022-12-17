package br.com.aluraflix.domain.entity;

import java.util.UUID;

public class Video {

    private UUID id;
    private String title;
    private String description;
    private String url;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return String.format("id=%s, title=%s, description=%s, url=%s", this.id, this.title, this.description, this.url);
    }
}