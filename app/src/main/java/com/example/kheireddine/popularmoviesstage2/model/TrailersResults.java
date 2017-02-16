package com.example.kheireddine.popularmoviesstage2.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by kheireddine on 16/02/17.
 */

public class TrailersResults {
    @SerializedName("results")
    private List<Trailer> mTrailerResults;

    public List<Trailer> getmTrailerResults() {
        return mTrailerResults;
    }
}