/**
 * Midterm <br>
 * Filename: Favorite.java<br>
 * Students in Group: Aneesh Garg
 */
package com.thetechdork.rottentomatoes.model;

/**
 * 
 * @author Aneesh Garg
 * @since Oct 31, 2013
 */
public class Favorite {
    private String  id;
    private boolean isFavourite;
    private Integer count;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean isFavourite) {
        this.isFavourite = isFavourite;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "Favorite [id=" + id + ", isFavourite=" + isFavourite + ", count=" + count + "]";
    }
}
