package com.example.kheireddine.popularmoviesstage2.utils;

/**
 * Created by kheireddine on 24/02/17.
 */

public abstract class Constants {
    /**
     * Constants for API
     */
    //TODO change to your own api_key
    public static final String API_KEY = "";

    public static final String API_BASE_URL = "http://api.themoviedb.org/3/";
    public static final String API_POSTER_HEADER_LARGE = "http://image.tmdb.org/t/p/w185";
    public static final String API_POSTER_HEADER_SMALL = "http://image.tmdb.org/t/p/w92";
    public static final String API_BACKDROP_HEADER = "http://image.tmdb.org/t/p/w780";

    public static final String YOUTUBE_URL = "https://www.youtube.com/watch?v=";

    public static final String SORT_BY_TOP_RATED = "top_rated";
    public static final String SORT_BY_POPOLARITY = "popular";
    public static final String SORT_BY_DEFAULT = SORT_BY_POPOLARITY;

    public static final String TOAST_WATCHING_TRAILER_ = "watching trailer : ";

    public static final int MOVIE_FROM_LIST = 1;
    public static final int MOVIE_FROM_CURSOR = 2;


    /**
     * Constants for extras intents
     */
    public static final String EXTRA_PARCELABLE_MOVIE ="extra_parcelable_movie";
    public static final String EXTRA_MOVIE_FROM_TYPE ="clicked_movie_from";




    /**
     * Constants for exception
     * */
    public static final String EXCEPTION_UKNOWN_URI = "Unknown uri: ";
    public static final String EXCEPTION_SQL_INSERT = "Failed to insert row into ";
    public static final String EXCEPTION_RESOLVER_QUERY = "Failed to asynchronously load data. ";

}
