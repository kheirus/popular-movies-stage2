package com.example.kheireddine.popularmoviesstage2.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by kheireddine on 30/01/17.
 */

@Parcel
public class Movie{
    @SerializedName("id")
    long id;
    @SerializedName("original_title")
    String title;
    @SerializedName("poster_path")
    String poster;
    @SerializedName("backdrop_path")
    String backdrop;
    @SerializedName("overview")
    String synopsis;
    @SerializedName("vote_average")
    String rating;
    @SerializedName("release_date")
    String releaseDate;
    @SerializedName("runtime")
    String runtime;
    @SerializedName("videos")
    TrailersResults trailersResults;
    @SerializedName("reviews")
    ReviewsResults reviewsResults;
    @SerializedName("images")
    Images images;

    boolean isFavourite;

    @Parcel
    public static class Images {
        @SerializedName("backdrops")
        List<Backdrops> backdropsList;

        public List<Backdrops> getBackdropsList() {
            return backdropsList;
        }
    }

    @Parcel
    public static class Backdrops{
        @SerializedName("file_path")
        String path;

        public String getPath() {
            return path;
        }
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPoster() {
        return poster;
    }

    public String getBackdrop() {
        return backdrop;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public String getRating() {
        return rating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getRuntime() {
        return runtime;
    }

    public TrailersResults getTrailersResults() {
        return trailersResults;
    }

    public Images getImages() {
        return images;
    }

    public ReviewsResults getReviewsResults() {
        return reviewsResults;
    }

    public void setTrailersResults(TrailersResults trailersResults) {
        this.trailersResults = trailersResults;
    }

    public void setReviewsResults(ReviewsResults reviewsResults) {
        this.reviewsResults = reviewsResults;
    }

    public void setImages(Images images) {
        this.images = images;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public void setBackdrop(String backdrop) {
        this.backdrop = backdrop;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }
}
