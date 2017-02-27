package com.example.kheireddine.popularmoviesstage2.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by kheireddine on 30/01/17.
 */

@Parcel
public class MoviesResults {
    @SerializedName("results")
    List<Movie> mMoviesResults;

    public List<Movie> getmMoviesResults() {
        return mMoviesResults;
    }
}
