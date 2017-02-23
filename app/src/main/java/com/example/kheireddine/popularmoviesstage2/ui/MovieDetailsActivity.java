package com.example.kheireddine.popularmoviesstage2.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.example.kheireddine.popularmoviesstage2.model.Review;
import com.example.kheireddine.popularmoviesstage2.model.ReviewResults;
import com.example.kheireddine.popularmoviesstage2.model.Trailer;
import com.example.kheireddine.popularmoviesstage2.model.TrailersResults;
import com.example.kheireddine.popularmoviesstage2.ui.adapters.ReviewListAdapter;
import com.example.kheireddine.popularmoviesstage2.ui.adapters.TrailerListAdapter;
import com.example.kheireddine.popularmoviesstage2.utils.Utils;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    private String mMovieTitle;
    private TrailerListAdapter mTrailerAdapter;
    private ReviewListAdapter mReviewAdapter;
    private List<Trailer> mTrailersList;
    private List<Review> mReviewList;
    private StringBuilder mParamsForApi;
    public static final String EXTRA_PARCELABLE_MOVIE ="extra_parcelable_movie";

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
        setReviewLayoutManager();

    }

    public void setTrailerLayoutManager() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager (mContext,LinearLayoutManager.HORIZONTAL,false);
        rvTrailerList.setLayoutManager(linearLayoutManager);
        rvTrailerList.setHasFixedSize(true);
    }

    public void setReviewLayoutManager() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager (mContext,LinearLayoutManager.HORIZONTAL,false);
        rvReviewList.setLayoutManager(linearLayoutManager);
        rvReviewList.setHasFixedSize(true);
    }

    private void setTrailerRecyclerAdapter(RecyclerView recyclerView) {
        mTrailerAdapter = new TrailerListAdapter(mContext, mTrailersList, mMovie, this);
        recyclerView.setAdapter(mTrailerAdapter);
    }

    private void setReviewRecyclerAdapter(RecyclerView recyclerView){
        mReviewAdapter = new ReviewListAdapter(mContext, mReviewList,this);
        recyclerView.setAdapter(mReviewAdapter);
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

    // Click on a review
    @Override
    public void onReviewListClick(int clickReviewIndex) {
        Log.d("pm", "onReviewListClick: "+mReviewList.get(clickReviewIndex).getAuthor());
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

                //set reviews
                ReviewResults reviewResults = mMovie.getReviewResults();
                mReviewList = reviewResults.getReviews();
                setReviewRecyclerAdapter(rvReviewList);
                int totalReviews = mMovie.getReviewResults().getTotalReviews();
                if (totalReviews==0){
                    tvReviewCount.setVisibility(View.GONE);
                    tvReviewFix.setVisibility(View.GONE);
                }
                else {
                    tvReviewCount.setText("("+totalReviews+")");
                }


            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                Log.d(Utils.TAG, "onFailure: "+t.getMessage());
            }
        });
    }



}
