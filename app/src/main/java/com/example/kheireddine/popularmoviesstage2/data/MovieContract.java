package com.example.kheireddine.popularmoviesstage2.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by kheireddine on 22/02/17.
 */

public final class MovieContract {
    /* Clients consult this class to know how to access the data*/

    // The authority, which is how your code knows which Content Provider to access
    public static final String AUTHORITY = "com.example.kheireddine.popularmoviesstage2";

    // The base content URI = "content://" + <authority>
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    // Define the possible paths for accessing data in this contract
    // This is the path for the "movies" directory
    public static final String PATH_FAVOURITE_MOVIES = "favourite_movies";


    private MovieContract(){}


    /* FavouriteMovieEntry is an inner class that defines the contents of the favourite movies
     It must implements BaseColumns class, this class contains by default _ID column
     */
    public static final class FavouriteMovieEntry implements BaseColumns{

        // FavouriteMovieEntry content URI = base content URI + path
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVOURITE_MOVIES).build();

        public static final String TABLE_NAME = "favourite_movies";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_POSTER = "poster";
        public static final String COLUMN_BACKDROP = "backdrop";
        public static final String COLUMN_SYNOPSIS = "synopsis";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_RUNTIME = "runtime";

    }
}
