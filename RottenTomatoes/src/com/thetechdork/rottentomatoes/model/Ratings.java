/**
 * Midterm <br>
 * Filename: Ratings.java<br>
 * Students in Group: Aneesh Garg
 */
package com.thetechdork.rottentomatoes.model;

/**
 * 
 * @author Aneesh Garg
 * @since Oct 31, 2013
 */
public class Ratings {
    private String criticRating;
    private String criticScore;
    private String audienceRating;
    private String audienceScore;

    public String getCriticRating() {
        return criticRating;
    }

    public void setCriticRating(String criticRating) {
        this.criticRating = criticRating;
    }

    public String getCriticScore() {
        return criticScore;
    }

    public void setCriticScore(String criticScore) {
        this.criticScore = criticScore;
    }

    public String getAudienceRating() {
        return audienceRating;
    }

    public void setAudienceRating(String audienceRating) {
        this.audienceRating = audienceRating;
    }

    public String getAudienceScore() {
        return audienceScore;
    }

    public void setAudienceScore(String audienceScore) {
        this.audienceScore = audienceScore;
    }

    @Override
    public String toString() {
        return "Ratings [criticRating=" + criticRating + ", criticScore=" + criticScore + ", audienceRating="
                + audienceRating + ", audienceScore=" + audienceScore + "]";
    }

}
