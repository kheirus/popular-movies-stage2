package com.example.kheireddine.popularmoviesstage2.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by kheireddine on 16/02/17.
 */

@Parcel
public class Trailer extends Model {
    @SerializedName("key")
    String key;
    @SerializedName("name")
    String name;

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

}
