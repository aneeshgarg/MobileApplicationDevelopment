/**
 * Midterm <br>
 * Filename: Movie.java<br>
 * Students in Group: Aneesh Garg
 */
package com.thetechdork.rottentomatoes.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 
 * @author Aneesh Garg
 * @since Oct 31, 2013
 */
public class Movie implements Serializable {

    private static final long serialVersionUID = 5849487311654104640L;

    private String            id;
    private String            title;
    private Integer           year;
    private Integer           runtime;
    private String            mpaaRating;
    private Date              releaseDate;
    private String            criticsRating;
    private Integer           criticsScore;
    private String            audienceRating;
    private Integer           audienceScore;
    private String            thumbnailURL;
    private String            detailedURL;
    private String            alternateURL;
    private String            genre;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getRuntime() {
        return runtime;
    }

    public void setRuntime(Integer runtime) {
        this.runtime = runtime;
    }

    public String getMpaaRating() {
        return mpaaRating;
    }

    public void setMpaaRating(String mpaaRating) {
        this.mpaaRating = mpaaRating;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getCriticsRating() {
        return criticsRating;
    }

    public void setCriticsRating(String criticsRating) {
        this.criticsRating = criticsRating;
    }

    public Integer getCriticsScore() {
        return criticsScore;
    }

    public void setCriticsScore(Integer criticsScore) {
        this.criticsScore = criticsScore;
    }

    public String getAudienceRating() {
        return audienceRating;
    }

    public void setAudienceRating(String audienceRating) {
        this.audienceRating = audienceRating;
    }

    public Integer getAudienceScore() {
        return audienceScore;
    }

    public void setAudienceScore(Integer audienceScore) {
        this.audienceScore = audienceScore;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

    public String getDetailedURL() {
        return detailedURL;
    }

    public void setDetailedURL(String detailedURL) {
        this.detailedURL = detailedURL;
    }

    public String getAlternateURL() {
        return alternateURL;
    }

    public void setAlternateURL(String alternateURL) {
        this.alternateURL = alternateURL;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(List<String> genreList) {
        StringBuilder sb = new StringBuilder();
        int count = 0;
        for (String genre : genreList) {
            count++;
            sb.append(genre);
            if (count != genreList.size())
                sb.append(",");
        }
        this.genre = sb.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Movie other = (Movie) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Movie [id=" + id + ", title=" + title + ", year=" + year + ", runtime=" + runtime + ", mpaaRating="
                + mpaaRating + ", releaseDate=" + releaseDate + ", criticsRating=" + criticsRating + ", criticsScore="
                + criticsScore + ", audienceRating=" + audienceRating + ", audienceScore=" + audienceScore
                + ", thumbnailURL=" + thumbnailURL + ", detailedURL=" + detailedURL + ", alternateURL=" + alternateURL
                + ", genre=" + genre + "]";
    }

}
