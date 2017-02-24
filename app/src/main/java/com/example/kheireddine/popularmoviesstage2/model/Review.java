package com.example.kheireddine.popularmoviesstage2.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by kheireddine on 23/02/17.
 */

@Parcel
public class Review extends Model {
    @SerializedName("author")
    String author;
    @SerializedName("content")
    String content;

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }
}
