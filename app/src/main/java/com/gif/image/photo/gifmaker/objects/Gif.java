package com.gif.image.photo.gifmaker.objects;

/**
 * Created by huand on 9/9/2017.
 */

public class Gif {
    private String path;

    private boolean isClicked;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isClicked() {
        return isClicked;
    }

    public void setClicked(boolean clicked) {
        isClicked = clicked;
    }
}
