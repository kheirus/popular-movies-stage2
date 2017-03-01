package com.example.kheireddine.popularmoviesstage2.ui;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.content.ContextCompat;
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
import com.example.kheireddine.popularmoviesstage2.data.MovieContract;
import com.example.kheireddine.popularmoviesstage2.model.Movie;
import com.example.kheireddine.popularmoviesstage2.model.ReviewsResults;
import com.example.kheireddine.popularmoviesstage2.model.Trailer;
import com.example.kheireddine.popularmoviesstage2.model.TrailersResults;
import com.example.kheireddine.popularmoviesstage2.ui.adapters.ReviewListAdapter;
import com.example.kheireddine.popularmoviesstage2.ui.adapters.TrailerListAdapter;
import com.example.kheireddine.popularmoviesstage2.utils.Constants;
import com.example.kheireddine.popularmoviesstage2.utils.Utils;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import static com.example.kheireddine.popularmoviesstage2.data.MovieContract.FavouriteMovieEntry.*;
import static com.example.kheireddine.popularmoviesstage2.utils.Constants.*;


public class MovieDetailsActivity extends MainActivity implements
        TrailerListAdapter.ITrailerListListener, ReviewListAdapter.IReviewListListener{

    @BindView(R.id.iv_backdrop) ImageView ivBackdrop;
    @BindView(R.id.iv_poster_detail) ImageView ivPosetr;
    @BindView(R.id.tv_title_detail) TextView tvTitle;
    @BindView(R.id.tv_synopsis) TextView tvSynopsis;
    @BindView(R.id.tv_rating) TextView tvRating;
    @BindView(R.id.tv_runtime) TextView tvRuntime;
    @BindView(R.id.rv_trailer_list) RecyclerView rvTrailerList;
    @BindView(R.id.rv_reviews_list) RecyclerView rvReviewList;
    @BindView(R.id.iv_hiden_heart) ImageView ivHidenHeart;
    @BindView(R.id.tv_reviews_count) TextView tvReviewCount;
    @BindView(R.id.tv_reviews_fix) TextView tvReviewFix;

    private Movie mMovie;
    private TrailerListAdapter mTrailerAdapter;
    private ReviewListAdapter mReviewAdapter;
    private int movieFromType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        mMovie = Parcels.unwrap(getIntent().getExtras().getParcelable(EXTRA_PARCELABLE_MOVIE));
        movieFromType = getIntent().getExtras().getInt(EXTRA_MOVIE_FROM_TYPE);
        Log.d(Utils.TAG, "Movie type = "+movieFromType);
        setViewMovie();


        setToolBar(mMovie.getTitle(),true,true);
        setTrailerLayoutManager();
        setReviewLayoutManager();

        // fetch other details of the movie (trailers, images, reviews...)
        if (movieFromType==MOVIE_FROM_LIST ||
                (movieFromType==MOVIE_FROM_CURSOR && Utils.isOnline(this))){
            /*  We do the http request only when the movie was selected from list of movies (that comes from internet)
             *  OR
             *  When the movie was selected from movies comes from database BUT the internet is available to requesting ThMDB API
             * */
            httpGetMovieTrailers();
            httpGetMovieImages();
            httpGetMovieReviews();
        }


    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    private void setViewMovie(){
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
                .load(API_POSTER_HEADER_LARGE +mMovie.getPoster())
                .placeholder(R.drawable.poster_placeholder)
                .error(R.drawable.poster_error)
                .into(ivPosetr);
        // set the background
        Picasso.with(mContext)
                .load(API_BACKDROP_HEADER+mMovie.getBackdrop())
                .placeholder(R.drawable.poster_placeholder)
                .error(R.drawable.poster_error)
                .into(ivBackdrop);

    }

    private void setTrailerLayoutManager() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager (mContext,LinearLayoutManager.HORIZONTAL,false);
        rvTrailerList.setLayoutManager(linearLayoutManager);
        rvTrailerList.setHasFixedSize(true);
    }

    private void setReviewLayoutManager() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager (mContext,LinearLayoutManager.HORIZONTAL,false);
        rvReviewList.setLayoutManager(linearLayoutManager);
        rvReviewList.setHasFixedSize(true);
    }

    private void setTrailerRecyclerAdapter(RecyclerView recyclerView) {
        mTrailerAdapter = new TrailerListAdapter(mContext, mMovie, this);
        recyclerView.setAdapter(mTrailerAdapter);
    }

    private void setReviewRecyclerAdapter(RecyclerView recyclerView){
        mReviewAdapter = new ReviewListAdapter(mContext, mMovie ,this);
        recyclerView.setAdapter(mReviewAdapter);
    }

    // Open youtube application to watch trailer
    //TODO You should use an Intent to open a youtube link in either the native app or a web browser of choice.
    @Override
    public void onTrailerListClick(int clickTrailerIndex) {
        Trailer mTrailerClicked = mMovie.getTrailersResults().getTrailers().get(clickTrailerIndex);
        Utils.showLongToastMessage(this, TOAST_WATCHING_TRAILER_ + mTrailerClicked.getName());
        Intent playYoutubeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE_URL+mTrailerClicked.getKey()));
        startActivity(playYoutubeIntent);
    }

    // Click on a review
    @Override
    public void onReviewListClick(int clickReviewIndex) {
        Intent reviewsIntent = new Intent(this, ReviewsActivity.class);
        reviewsIntent.putExtra(EXTRA_PARCELABLE_MOVIE, Parcels.wrap(mMovie));
        startActivity(reviewsIntent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out );

    }

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
                    //CHECKED
                    item.setIcon(ContextCompat.getDrawable(mContext,R.drawable.ic_favorite_fill));
                    item.setChecked(false);
                    final Animation animScale = AnimationUtils.loadAnimation(mContext, R.anim.anim_scale);
                    ivHidenHeart.startAnimation(animScale);
                    addToFavourite();
                    break;
                }

                else {
                    //UNCHECKED
                    item.setIcon(ContextCompat.getDrawable(mContext,R.drawable.ic_favorite));
                    item.setChecked(true);
                    removeFromFavourite();
                    break;
                }
        }
        return super.onOptionsItemSelected(item);
    }

    /** Adding movie to favourite movies */
    private void addToFavourite () {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_TITLE, mMovie.getTitle());
        contentValues.put(COLUMN_RATING, mMovie.getRating());
        contentValues.put(COLUMN_POSTER, mMovie.getPoster());

        Uri uri = getContentResolver().insert(CONTENT_URI, contentValues);

        if (uri != null) {
            Log.d(Utils.TAG, "addToFavourite: " + uri);
        }

        // TODO : add favourite movie in shared preferences as boolean true matched with its id, that allows me to retrieve the value
        // of the button if it is checked or not
    }

    private void removeFromFavourite (){
        // TODO
    }

    /**************************************************************************************************
     *                                            HTTP calls
     ************************************************************************************************/
    private void httpGetMovieTrailers(){
        Call<TrailersResults> call = mdbAPI.getMovieTrailers(mMovie.getId());
        call.enqueue(new Callback<TrailersResults>() {
            @Override
            public void onResponse(Call<TrailersResults> call, Response<TrailersResults> response) {
                // set trailers
                TrailersResults trailersResults= response.body();
                mMovie.setTrailersResults(trailersResults);
                setTrailerRecyclerAdapter(rvTrailerList);
            }

            @Override
            public void onFailure(Call<TrailersResults> call, Throwable t) {
                Log.e(Utils.TAG, "onFailure: "+t.getMessage());
            }
        });
    }

    private void httpGetMovieImages(){
        Call<Movie.Images> call = mdbAPI.getMovieImages(mMovie.getId());
        call.enqueue(new Callback<Movie.Images>() {
            @Override
            public void onResponse(Call<Movie.Images> call, Response<Movie.Images> response) {
                // set trailers images
                Movie.Images images= response.body();
                mMovie.setImages(images);
                setTrailerRecyclerAdapter(rvTrailerList);
            }

            @Override
            public void onFailure(Call<Movie.Images> call, Throwable t) {
                Log.e(Utils.TAG, "onFailure: "+t.getMessage());
            }
        });
    }

    private void httpGetMovieReviews(){
        Call<ReviewsResults> call = mdbAPI.getMovieReviews(mMovie.getId());
        call.enqueue(new Callback<ReviewsResults>() {
            @Override
            public void onResponse(Call<ReviewsResults> call, Response<ReviewsResults> response) {
                // set reviews
                ReviewsResults reviewsResults= response.body();
                mMovie.setReviewsResults(reviewsResults);
                tvReviewCount.setText(String.valueOf("("+mMovie.getReviewsResults().getTotalReviews())+")");
                setReviewRecyclerAdapter(rvReviewList);
            }

            @Override
            public void onFailure(Call<ReviewsResults> call, Throwable t) {
                Log.e(Utils.TAG, "onFailure: "+t.getMessage());
            }
        });
    }


}
