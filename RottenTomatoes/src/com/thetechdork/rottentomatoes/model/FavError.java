/**
 * Midterm <br>
 * Filename: Error.java<br>
 * Students in Group: Aneesh Garg
 */
package com.thetechdork.rottentomatoes.model;

/**
 * 
 * @author Aneesh Garg
 * @since Oct 31, 2013
 */
public class FavError {

    private Integer id;
    private String  errorMessage;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return "Error [id=" + id + ", errorMessage=" + errorMessage + "]";
    }

}
