package com.example.kheireddine.popularmoviesstage2.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by kheireddine on 23/02/17.
 */

@Parcel
public class ReviewResults extends Model {
    @SerializedName("results")
    List<Review> reviews;
    @SerializedName("total_results")
    int totalReviews;

    public List<Review> getReviews() {
        return reviews;
    }

    public int getTotalReviews() {
        return totalReviews;
    }
}
