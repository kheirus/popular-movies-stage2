package com.example.kheireddine.popularmoviesstage2.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.kheireddine.popularmoviesstage2.utils.Utils;

import static com.example.kheireddine.popularmoviesstage2.data.MovieContract.FavouriteMovieEntry.*;

/**
 * Created by kheireddine on 22/02/17.
 */

public class MovieDbHelper extends SQLiteOpenHelper {
    // Database filename
    private static final String DATABASE_NAME = "movie.db";
    // Database version
    private static final int DATABASE_VERSION = 1;


    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Query that create the database
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE "+
                TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
                COLUMN_ID + " LONG NOT NULL," +
                COLUMN_TITLE + " TEXT NOT NULL," +
                COLUMN_POSTER + " TEXT NOT NULL," +
                COLUMN_RATING + " TEXT NOT NULL," +
                COLUMN_BACKDROP + " TEXT NOT NULL," +
                COLUMN_SYNOPSIS + " TEXT NOT NULL," +
                COLUMN_RELEASE_DATE + " TEXT NOT NULL," +
                COLUMN_RUNTIME + " TEXT NOT NULL" +
                ");";

        // Execute the query
        db.execSQL(SQL_CREATE_MOVIE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        final String SQL_DROP_MOVIE_TABLE= "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(SQL_DROP_MOVIE_TABLE);
        onCreate(db);
    }
}
