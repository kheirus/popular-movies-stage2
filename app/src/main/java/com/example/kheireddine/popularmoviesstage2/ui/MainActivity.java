package com.example.kheireddine.popularmoviesstage2.ui;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.kheireddine.popularmoviesstage2.api.IMovieDBRestAPI;
import com.example.kheireddine.popularmoviesstage2.api.MovieDBServiceAPI;


/**
 * This is a base activity that all other activities have to extend
 * */
public class MainActivity extends AppCompatActivity {
    protected Context mContext;

    protected IMovieDBRestAPI mdbAPI;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getBaseContext();
        mdbAPI = MovieDBServiceAPI.createService(IMovieDBRestAPI.class);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home :
                finish();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }

    }

    // Toolbar with title and home button
    protected void setToolBar(String title, boolean homeUp, boolean showHomeUp) {
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(homeUp);
        getSupportActionBar().setDisplayShowHomeEnabled(showHomeUp);
    }

    // Toolbar without home button
    protected void setToolBar(String title) {
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
    }
}
