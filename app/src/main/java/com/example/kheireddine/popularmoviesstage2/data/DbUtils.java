package com.example.kheireddine.popularmoviesstage2.data;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;;

import com.example.kheireddine.popularmoviesstage2.model.Movie;
import com.example.kheireddine.popularmoviesstage2.utils.Constants;
import com.example.kheireddine.popularmoviesstage2.utils.Utils;

import static android.content.Context.MODE_PRIVATE;
import static com.example.kheireddine.popularmoviesstage2.data.MovieContract.FavouriteMovieEntry.*;

/**
 * Created by kheireddine on 03/03/17.
 */

public class DbUtils {
    private static final String SHARED_PREFERENCES_FILE_NAME = "favourite_checking";
    private static final String SP_KEY_ITEM_MENU = "item_menu_selected";

    public final static boolean BTN_CHECKED = true;
    public final static boolean BTN_UNCHECKED = false;
    /**
     * TODO : do it on a detached thread
     * Store value of favourite button as a shared preferences
     * @param context Context of the Activity
     * @param movie Movie we want to add to favourite
     * @param isChecked state of the favourite button
     * */
    public static void setStateChecking(Context context, Movie movie, boolean isChecked){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE_NAME,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(movie.getTitle(), isChecked);
        editor.commit();
    }

    /**
     * TODO : do it on a detached thread
     * Getting value of favourite button
     * @param context Context of the Activity
     * @param movie Movie we want to add to check
     * @return state of the selected movie (checked or unchecked)
     * */
    public static boolean getStateChecking(Context context, Movie movie){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE_NAME,MODE_PRIVATE);
        boolean isChecked = sharedPreferences.getBoolean(movie.getTitle(), false);
        return isChecked;
    }

    /**
     * Setting value of selected item menu
     * @param context Context of the Activity
     * @param sortBy value of the param send to get movie http request
     * */
    public static void setItemMenuSelected(Context context, String sortBy){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE_NAME,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SP_KEY_ITEM_MENU, sortBy);
        editor.commit();
    }


    /**
     * Getting value of selected item menu
     * @param context Context of the Activity
     * @return value of the selected item menu
     * */
    public static String getItemMenuSelected(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE_NAME,MODE_PRIVATE);
        String itemSelected = sharedPreferences.getString(SP_KEY_ITEM_MENU, Constants.SORT_BY_DEFAULT);
        return itemSelected;
    }

    /**
     * Insert a Movie as a favourite in the databe
     * @param context Context of the Activity
     * @param movie Movie we want to add to favourite
     * */
    public static void insertMovie(Context context, Movie movie){
        String stringRuntime;
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ID, movie.getId());
        contentValues.put(COLUMN_TITLE, movie.getTitle());
        contentValues.put(COLUMN_RATING, movie.getRating());
        contentValues.put(COLUMN_POSTER, movie.getPoster());
        contentValues.put(COLUMN_BACKDROP, movie.getBackdrop());
        contentValues.put(COLUMN_RELEASE_DATE, movie.getReleaseDate());
        contentValues.put(COLUMN_SYNOPSIS, movie.getSynopsis());
        // Whether the network is off, we haven't the runtime value so it's null
        if (movie.getRuntime() == null){
            stringRuntime = " ";
        }
        else {
            stringRuntime = movie.getRuntime();
        }
        contentValues.put(COLUMN_RUNTIME, stringRuntime);

        context.getContentResolver().insert(CONTENT_URI, contentValues);
        // add the movie as a favourite in our SharedPreferences file
        setStateChecking(context, movie, BTN_CHECKED);
    }



    public static void deleteMovie(Context context, Movie movie){
        String stringId = String.valueOf(movie.getId());
        Uri uri = CONTENT_URI.buildUpon().appendPath(stringId).build();

        context.getContentResolver().delete(uri, null, null);

        // delete the movie from SharedPreferences file
        setStateChecking(context, movie, false);
    }

}
