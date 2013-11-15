/**
 * Assignment #: 5<br>
 * Filename: Image.java<br>
 * Students in Group: Aneesh Garg
 */
package com.aneeshgarg.inclass4.utility;

public class Image implements Comparable<Image>{
    private String  url;
    private Integer height;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    @Override
    public int compareTo(Image another) {
        return this.height.compareTo(another.height);
    }

    @Override
    public String toString() {
        return "Image [url=" + url + ", height=" + height + "]";
    }
}
