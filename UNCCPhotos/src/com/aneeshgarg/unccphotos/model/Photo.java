/**
 * Assignment #: 5<br>
 * Filename: Photo.java<br>
 * Students in Group: Aneesh Garg
 */
package com.aneeshgarg.unccphotos.model;

import java.io.Serializable;

public class Photo implements Serializable, Comparable<Photo> {

    private static final long serialVersionUID = 1L;
    private String            title;
    private int               views;
    private String            url_m;
    private String            id;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public String getUrl_m() {
        return url_m;
    }

    public void setUrl_m(String url_m) {
        this.url_m = url_m;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override 
    public int compareTo(Photo another) {
        return Integer.valueOf(this.views).compareTo(another.views);
    }

    @Override
    public String toString() {
        return "Photo [title=" + title + ", views=" + views + ", url_m=" + url_m + ", id=" + id + "]";
    }

}
