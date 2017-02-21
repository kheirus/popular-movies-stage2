package com.example.kheireddine.popularmoviesstage2.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kheireddine.popularmoviesstage2.R;
import com.example.kheireddine.popularmoviesstage2.api.MovieDBServiceAPI;
import com.example.kheireddine.popularmoviesstage2.model.Movie;
import com.example.kheireddine.popularmoviesstage2.model.Trailer;
import com.example.kheireddine.popularmoviesstage2.model.TrailersResults;
import com.example.kheireddine.popularmoviesstage2.ui.adapters.TrailerListAdapter;
import com.example.kheireddine.popularmoviesstage2.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailsActivity extends MainActivity implements TrailerListAdapter.ITrailerListListener{
    @BindView(R.id.iv_backdrop) ImageView ivBackdrop;
    @BindView(R.id.iv_poster_detail) ImageView ivPosetr;
    @BindView(R.id.tv_title_detail) TextView tvTitle;
    @BindView(R.id.tv_synopsis) TextView tvSynopsis;
    @BindView(R.id.tv_rating) TextView tvRating;
    @BindView(R.id.tv_runtime) TextView tvRuntime;
    @BindView(R.id.rv_trailer_list) RecyclerView rvTrailerList;
    @BindView(R.id.iv_hiden_heart) ImageView ivHidenHeart;

    private Movie mMovie;
    private String mMovieTitle;
    private TrailerListAdapter mAdapter;
    private List<Trailer> mTrailersList;
    private StringBuilder mParamsForApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        long movieId = getIntent().getExtras().getLong(MovieListActivity.EXTRA_MOVIE_ID);
        mMovieTitle= getIntent().getExtras().getString(MovieListActivity.EXTRA_MOVIE_TITLE);
        httpGetMovieDetails(movieId);

        setToolBar(mMovieTitle,true,true);
        setTrailerLayoutManager();

    }

    public void setTrailerLayoutManager() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager (mContext,LinearLayoutManager.HORIZONTAL,false);
        rvTrailerList.setLayoutManager(linearLayoutManager);
        rvTrailerList.setHasFixedSize(true);
    }

    private void setTrailerRecyclerAdapter(RecyclerView recyclerView) {
        mAdapter = new TrailerListAdapter(mContext, mTrailersList, mMovie, this);
        recyclerView.setAdapter(mAdapter);
    }


    // Open youtube application to watch trailer
    //TODO You should use an Intent to open a youtube link in either the native app or a web browser of choice.
    @Override
    public void onTrailerListClick(int clickTrailerIndex) {
        Utils.showLongToastMessage(this,"watching trailer : "+mTrailersList.get(clickTrailerIndex).getName());
        Trailer mTrailerClicked = mTrailersList.get(clickTrailerIndex);
        Intent playYoutubeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(MovieDBServiceAPI.YOUTUBE_URL+mTrailerClicked.getKey()));
        startActivity(playYoutubeIntent);
    }

//    public void onclickFavouriteButton(View view) {
////        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
////            view.setBackground(getDrawable(R.drawable.ic_favorite_fill));
////        }
//
//    }

    /**
     * Create a Menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.details_movie_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_favourite:

                if(item.isChecked()){
                    item.setIcon(ContextCompat.getDrawable(mContext,R.drawable.ic_favorite_fill));
                    item.setChecked(false);
                    final Animation animScale = AnimationUtils.loadAnimation(mContext, R.anim.anim_scale);
                    ivHidenHeart.startAnimation(animScale);
                    addToFavourite();
                    break;
                }

                else {
                    item.setIcon(ContextCompat.getDrawable(mContext,R.drawable.ic_favorite));
                    item.setChecked(true);
                    removeFromFavourite();
                    break;
                }




        }
        return super.onOptionsItemSelected(item);
    }

    private void addToFavourite (){
        // TODO
    }

    private void removeFromFavourite (){
        // TODO
    }

    /**************************************************************************************************
     *                                            HTTP calls
     ************************************************************************************************/
    public void httpGetMovieDetails(long movieId) {
        //append_to_response to api
        mParamsForApi = new StringBuilder();
        mParamsForApi.append(getString(R.string.api_append_videos));
        mParamsForApi.append(",");
        mParamsForApi.append(getString(R.string.api_append_reviews));
        mParamsForApi.append(",");
        mParamsForApi.append(getString(R.string.api_append_images));



        Call<Movie> call = mdbAPI.getMovieDetails(movieId,mParamsForApi.toString());
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
                // set trailers
                TrailersResults trailersResults= mMovie.getTrailersResults();
                mTrailersList = trailersResults.getmTrailerResults();
                setTrailerRecyclerAdapter(rvTrailerList);

            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                Log.d(Utils.TAG, "onFailure: "+t.getMessage());
            }
        });
    }



}
