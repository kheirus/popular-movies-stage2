package com.example.kheireddine.popularmoviesstage2.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kheireddine.popularmoviesstage2.R;
import com.example.kheireddine.popularmoviesstage2.api.ITheMovieDbRestAPI;
import com.example.kheireddine.popularmoviesstage2.api.TheMovieDbServiceAPI;
import com.example.kheireddine.popularmoviesstage2.data.DbUtils;
import com.example.kheireddine.popularmoviesstage2.model.Movie;
import com.example.kheireddine.popularmoviesstage2.model.ReviewsResults;
import com.example.kheireddine.popularmoviesstage2.model.Trailer;
import com.example.kheireddine.popularmoviesstage2.model.TrailersResults;
import com.example.kheireddine.popularmoviesstage2.ui.adapters.ReviewListAdapter;
import com.example.kheireddine.popularmoviesstage2.ui.adapters.TrailerListAdapter;
import com.example.kheireddine.popularmoviesstage2.ui.fragments.MovieDetailsFragment;
import com.example.kheireddine.popularmoviesstage2.utils.Utils;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import static com.example.kheireddine.popularmoviesstage2.data.DbUtils.BTN_CHECKED;
import static com.example.kheireddine.popularmoviesstage2.data.DbUtils.BTN_UNCHECKED;
import static com.example.kheireddine.popularmoviesstage2.utils.Constants.*;


public class MovieDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        addDetailsFragment();

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }


    public void addDetailsFragment() {
        if (!isFinishing()) {
            // In case this activity was started with special instructions from an Intent,
            // pass the Intent's extras to the fragment as arguments
            MovieDetailsFragment detailFragment = MovieDetailsFragment.create(getIntent().getExtras());

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fl_details, detailFragment)
                    .commit();
        }
    }

}
