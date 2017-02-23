package com.example.kheireddine.popularmoviesstage2.data;

import android.provider.BaseColumns;

/**
 * Created by kheireddine on 22/02/17.
 */

public final class MovieContract {

    private MovieContract(){}

    public static final class MovieEntry implements BaseColumns{
        public static final String TABLE_NAME = "favourite_movie";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_POSTER = "poster";
        public static final String COLUMN_BACKDROP = "backdrop";
        public static final String COLUMN_SYNOPSIS = "synopsis";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_RUNTIME = "runtime";
        public static final String COLUMN_VIDEOS = "trailers";
        public static final String COLUMN_IMAGES = "images";
    }
}
