package com.example.kheireddine.popularmoviesstage2.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by kheireddine on 16/02/17.
 */

@Parcel
public class TrailersResults {
    @SerializedName("results")
    List<Trailer> trailers;

    public List<Trailer> getTrailers() {
        return trailers;
    }
}