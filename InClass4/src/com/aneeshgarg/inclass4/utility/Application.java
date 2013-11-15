/**
 * Assignment #: 4<br>
 * Filename: AppInfo.java<br>
 * Students in Group: Aneesh Garg
 */
package com.aneeshgarg.inclass4.utility;

import java.util.ArrayList;
import java.util.List;

public class Application implements Comparable<Application> {

    private String      name;
    private String      displayPrice;
    private Double      price;
    private List<Image> imageList = new ArrayList<Image>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayPrice() {
        return displayPrice;
    }

    public void setDisplayPrice(String displayPrice) {
        this.displayPrice = displayPrice;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public List<Image> getImageList() {
        return imageList;
    }

    public void setImageList(List<Image> imageList) {
        this.imageList = imageList;
    }

    @Override
    public int compareTo(Application another) {
        return this.price.compareTo(another.price);
    }

    @Override
    public String toString() {
        return "Application [name=" + name + ", displayPrice=" + displayPrice + ", price=" + price + ", imageList="
                + imageList + "]";
    }
}
