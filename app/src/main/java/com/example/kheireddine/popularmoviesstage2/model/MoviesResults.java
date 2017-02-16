package com.example.kheireddine.popularmoviesstage2.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by kheireddine on 30/01/17.
 */

public class MoviesResults {
    @SerializedName("results")
    private List<Movie> mMoviesResults;

    public List<Movie> getmMoviesResults() {
        return mMoviesResults;
    }
}
