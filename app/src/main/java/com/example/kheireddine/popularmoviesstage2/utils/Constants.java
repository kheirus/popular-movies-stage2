package com.example.kheireddine.popularmoviesstage2.utils;

/**
 * Created by kheireddine on 24/02/17.
 */

public abstract class Constants {
    /**
     * Constants for API
     */
    //TODO to change to your own api_key
    public static final String API_KEY = "46fb89b499cf5ac7aa47c78bc5902a50";
    public static final String API_BASE_URL = "http://api.themoviedb.org/3/";
    public static final String API_POSTER_HEADER_LARGE = "http://image.tmdb.org/t/p/w185";
    public static final String API_POSTER_HEADER_SMALL = "http://image.tmdb.org/t/p/w92";
    public static final String API_BACKDROP_HEADER = "http://image.tmdb.org/t/p/w780";
    public static final String YOUTUBE_URL = "https://www.youtube.com/watch?v=";
    public static final String SORT_BY_TOP_RATED = "top_rated";
    public static final String SORT_BY_POPOLARITY = "popular";
    public static final String SORT_BY_DEFAULT = SORT_BY_POPOLARITY;

    /**
     * Constants for extras intents
     */
    public static final String EXTRA_PARCELABLE_MOVIE ="extra_parcelable_movie";

    public enum ExtraMovieDetails{
        TRAILERS("trailers"),
        IMAGES("images"),
        REVIEWS("reviews");

        private String type = "";
        ExtraMovieDetails(String type) {
            this.type = type;
        }

    }
}
