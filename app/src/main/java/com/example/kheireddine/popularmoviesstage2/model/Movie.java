package com.example.kheireddine.popularmoviesstage2.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by kheireddine on 30/01/17.
 */


public class Movie {
    @SerializedName("id")
    private long id;
    @SerializedName("original_title")
    private String title;
    @SerializedName("poster_path")
    private String poster;
    @SerializedName("backdrop_path")
    private String backdrop;
    @SerializedName("overview")
    private String synopsis;
    @SerializedName("vote_average")
    private String rating;
    @SerializedName("release_date")
    private String releaseDate;
    @SerializedName("runtime")
    private String runtime;
    @SerializedName("videos")
    private TrailersResults trailersResults;
    @SerializedName("images")
    private Images images;


    public static class Images {
        @SerializedName("backdrops")
        List<Backdrops> backdropsList;

        public List<Backdrops> getBackdropsList() {
            return backdropsList;
        }
    }

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
}
