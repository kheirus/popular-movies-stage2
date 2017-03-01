package com.example.kheireddine.popularmoviesstage2.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

// static imports
import static com.example.kheireddine.popularmoviesstage2.data.MovieContract.FavouriteMovieEntry.*;
import static com.example.kheireddine.popularmoviesstage2.utils.Constants.*;

/**
 * Created by kheireddine on 01/03/17.
 */

public class MovieContentProvider extends ContentProvider {
    // Define final integer constants for the directory of tasks and a single item.
    // It's convention to use 100, 200, 300, etc for directories,
    // and related ints (101, 102, ..) for items in that directory.
    public static final int FAVOURITE_MOVIES = 100;
    public static final int FAVOURITE_MOVIE_WITH_ID = 101;

    // Static variable for the Uri marcher
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);


    /** Method that associates URI's with their int match*/
    static {
        sUriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_FAVOURITE_MOVIES, FAVOURITE_MOVIES);
        sUriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_FAVOURITE_MOVIES +"/#", FAVOURITE_MOVIE_WITH_ID);
    }


    private MovieDbHelper mMovieDbHelper;


    @Override
    public boolean onCreate() {
        Context context = getContext();
        mMovieDbHelper = new MovieDbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        // Get access to the database
        final SQLiteDatabase db = mMovieDbHelper.getReadableDatabase();

        int match = sUriMatcher.match(uri);
        Cursor returnCursor;

        switch (match){
            case FAVOURITE_MOVIES:
                returnCursor = db.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            default:
                throw new UnsupportedOperationException(EXCEPTION_UKNOWN_URI + uri);

        }

        returnCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return returnCursor;

    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // Get access to the database
        final SQLiteDatabase db = mMovieDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        Uri returnUri;

        switch (match){
            case FAVOURITE_MOVIES:
                long id = db.insert(TABLE_NAME, null, values);
                if ( id > 0 ) {
                    //success
                    returnUri = ContentUris.withAppendedId(CONTENT_URI, id);

                } else {
                    throw new android.database.SQLException(EXCEPTION_SQL_INSERT + uri);
                }
                break;

            default:
                throw new UnsupportedOperationException(EXCEPTION_UKNOWN_URI + uri);
        }
        // Notify the resolver if the uri has been changed
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }
}
