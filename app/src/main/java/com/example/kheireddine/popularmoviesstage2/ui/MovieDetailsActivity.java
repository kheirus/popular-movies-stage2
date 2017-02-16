package com.example.kheireddine.popularmoviesstage2.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kheireddine.popularmoviesstage2.R;
import com.example.kheireddine.popularmoviesstage2.api.MovieDBServiceAPI;
import com.example.kheireddine.popularmoviesstage2.model.Movie;
import com.example.kheireddine.popularmoviesstage2.utils.Utils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailsActivity extends MainActivity {
    @BindView(R.id.iv_backdrop) ImageView ivBackdrop;
    @BindView(R.id.iv_poster_detail) ImageView ivPosetr;
    @BindView(R.id.tv_title_detail) TextView tvTitle;
    @BindView(R.id.tv_synopsis) TextView tvSynopsis;
    @BindView(R.id.tv_rating) TextView tvRating;
    @BindView(R.id.tv_runtime) TextView tvRuntime;

    private Movie mMovie;
    private String mMovieTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        long movieId = getIntent().getExtras().getLong(MovieListActivity.EXTRA_MOVIE_ID);
        mMovieTitle= getIntent().getExtras().getString(MovieListActivity.EXTRA_MOVIE_TITLE);
        httpGetMovieDetails(movieId);

        setToolBar(mMovieTitle,true,true);


    }



    /**************************************************************************************************
     *                                            HTTP calls
     ************************************************************************************************/
    public void httpGetMovieDetails(long movieId) {
        Call<Movie> call = mdbAPI.getMovieDetails(movieId);
        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                // retrieve the selected movie
                mMovie = response.body();
                // set the title and the year
                tvTitle.setText(mMovie.getTitle() + " (" + Utils.getYear(mMovie.getReleaseDate())+")");
                // set the synopsis
                tvSynopsis.setText(mMovie.getSynopsis());
                // set rating
                tvRating.setText(mMovie.getRating());
                // set the runtime
                tvRuntime.setText(Utils.timeToDisplay(mMovie.getRuntime()));
                // set the poster
                Picasso.with(mContext)
                        .load(MovieDBServiceAPI.API_POSTER_HEADER_LARGE +mMovie.getPoster())
                        .placeholder(R.drawable.poster_placeholder)
                        .error(R.drawable.poster_error)
                        .into(ivPosetr);
                // set the background
                Picasso.with(mContext)
                        .load(MovieDBServiceAPI.API_BACKDROP_HEADER+mMovie.getBackdrop())
                        .placeholder(R.drawable.poster_placeholder)
                        .error(R.drawable.poster_error)
                        .into(ivBackdrop);


            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                Log.d(Utils.TAG, "onFailure: "+t.getMessage());
            }
        });
    }

    public void onClickButtonBackdrop(View view) {
        Utils.showShortToastMessage(mContext,"Soon...");
    }
}
