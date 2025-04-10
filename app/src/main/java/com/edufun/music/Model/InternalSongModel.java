package com.edufun.music.Model;

import java.io.Serializable;

public class InternalSongModel implements Serializable {
    String path,title,duration,imageIcon;

    public InternalSongModel(String path, String title, String duration,String imageIcon) {
        this.path = path;
        this.title = title;
        this.duration = duration;
        this.imageIcon = imageIcon;
    }

    public InternalSongModel() {
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getImageIcon() {
        return imageIcon;
    }

    public void setImageIcon(String imageIcon) {
        this.imageIcon = imageIcon;
    }
}
