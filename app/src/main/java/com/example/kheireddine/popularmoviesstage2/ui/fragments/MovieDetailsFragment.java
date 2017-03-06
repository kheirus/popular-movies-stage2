package com.example.kheireddine.popularmoviesstage2.ui.fragments;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.kheireddine.popularmoviesstage2.ui.activities.ReviewsActivity;
import com.example.kheireddine.popularmoviesstage2.ui.adapters.ReviewListAdapter;
import com.example.kheireddine.popularmoviesstage2.ui.adapters.TrailerListAdapter;
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
import static com.example.kheireddine.popularmoviesstage2.utils.Constants.API_BACKDROP_HEADER;
import static com.example.kheireddine.popularmoviesstage2.utils.Constants.API_POSTER_HEADER_LARGE;
import static com.example.kheireddine.popularmoviesstage2.utils.Constants.EXTRA_MOVIE_FROM_TYPE;
import static com.example.kheireddine.popularmoviesstage2.utils.Constants.EXTRA_PARCELABLE_MOVIE;
import static com.example.kheireddine.popularmoviesstage2.utils.Constants.MOVIE_FROM_CURSOR;
import static com.example.kheireddine.popularmoviesstage2.utils.Constants.MOVIE_FROM_LIST;
import static com.example.kheireddine.popularmoviesstage2.utils.Constants.TOAST_WATCHING_TRAILER_;
import static com.example.kheireddine.popularmoviesstage2.utils.Constants.YOUTUBE_URL;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieDetailsFragment extends Fragment implements
        TrailerListAdapter.ITrailerListListener, ReviewListAdapter.IReviewListListener {

    @BindView(R.id.iv_backdrop)
    ImageView ivBackdrop;
    @BindView(R.id.iv_poster_detail) ImageView ivPosetr;
    @BindView(R.id.tv_title_detail)
    TextView tvTitle;
    @BindView(R.id.tv_synopsis) TextView tvSynopsis;
    @BindView(R.id.tv_rating) TextView tvRating;
    @BindView(R.id.tv_runtime) TextView tvRuntime;
    @BindView(R.id.rv_trailer_list)
    RecyclerView rvTrailerList;
    @BindView(R.id.rv_reviews_list) RecyclerView rvReviewList;
    @BindView(R.id.iv_hiden_heart) ImageView ivHidenHeart;
    @BindView(R.id.tv_reviews_count) TextView tvReviewCount;
    @BindView(R.id.tv_reviews_fix) TextView tvReviewFix;
    @BindView(R.id.fab_favourite) FloatingActionButton fabFavourite;

    private Movie mMovie;
    private TrailerListAdapter mTrailerAdapter;
    private ReviewListAdapter mReviewAdapter;
    private int movieFromType;
    private StringBuilder mParamsForApi;
    private static boolean isFavBtnChecked;
    private Context mContext;
    private ITheMovieDbRestAPI mdbAPI;


    public static MovieDetailsFragment create(Bundle args){
        MovieDetailsFragment fragment = new MovieDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public MovieDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie_details, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);
        mContext = getActivity();
        mdbAPI = TheMovieDbServiceAPI.createService(ITheMovieDbRestAPI.class);
        setHasOptionsMenu(true);

        if (getArguments() !=null ){
            Bundle arg = getArguments();
            mMovie = Parcels.unwrap(arg.getParcelable(EXTRA_PARCELABLE_MOVIE));
            movieFromType = arg.getInt(EXTRA_MOVIE_FROM_TYPE);
        }

        // getting value of button favourite (checked or unchecked)
        isFavBtnChecked = DbUtils.getStateChecking(mContext, mMovie);

        setViewMovie();
        setToolBar(mMovie.getTitle(),true,true);
        setTrailerLayoutManager();
        setReviewLayoutManager();

        // fetch other details of the movie (trailers, images, reviews...)
        if (movieFromType==MOVIE_FROM_LIST ||
                (movieFromType==MOVIE_FROM_CURSOR && Utils.isOnline(getActivity()))){
            /*  We do the http request only when the movie was selected from list of movies (that comes from internet)
             *  OR
             *  When the movie was selected from movies comes from database BUT the internet is available to requesting ThMDB API
             * */
            httpGetMovieDetails(mMovie.getId());
        }


    }


    private void setViewMovie(){
        // set the title and the year
        tvTitle.setText(mMovie.getTitle() + " (" + Utils.getYear(mMovie.getReleaseDate())+")");
        // set the synopsis
        tvSynopsis.setText(mMovie.getSynopsis());
        // set rating
        tvRating.setText(mMovie.getRating());
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

        Log.d(Utils.TAG, "isFavbtnchecked " + isFavBtnChecked);
        if (isFavBtnChecked){
            fabFavourite.setImageResource(R.drawable.ic_favorite_fill);
        } else {
            fabFavourite.setImageResource(R.drawable.ic_favorite_empty);
        }

        fabFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFavBtnChecked){
                    //CHECKED
                    fabFavourite.setImageResource(R.drawable.ic_favorite_empty);
                    isFavBtnChecked = false;
                    removeFromFavourite();
                } else {
                    //UNCHECKED
                    fabFavourite.setImageResource(R.drawable.ic_favorite_fill);
                    final Animation animScale = AnimationUtils.loadAnimation(mContext, R.anim.anim_scale);
                    ivHidenHeart.startAnimation(animScale);
                    isFavBtnChecked = true;
                    addToFavourite();
                }

            }
        });
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
        Utils.showLongToastMessage(getActivity(), TOAST_WATCHING_TRAILER_ + mTrailerClicked.getName());
        Intent playYoutubeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE_URL+mTrailerClicked.getKey()));
        startActivity(playYoutubeIntent);
    }

    // Click on a review
    @Override
    public void onReviewListClick(int clickReviewIndex) {
        Intent reviewsIntent = new Intent(getActivity(), ReviewsActivity.class);
        reviewsIntent.putExtra(EXTRA_PARCELABLE_MOVIE, Parcels.wrap(mMovie));
        startActivity(reviewsIntent);
        getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home :
                getActivity().finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Toolbar with title and home button
    protected void setToolBar(String title, boolean homeUp, boolean showHomeUp) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(title);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(homeUp);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(showHomeUp);
    }


    /** Adding movie to favourite movies */
    private void addToFavourite () {
        DbUtils.insertMovie(mContext, mMovie);
    }

    private void removeFromFavourite (){
        DbUtils.deleteMovie(mContext, mMovie);
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

                // set trailers
                TrailersResults trailersResults= mMovie.getTrailersResults();
                mMovie.setTrailersResults(trailersResults);
                setTrailerRecyclerAdapter(rvTrailerList);

                //set reviews
                ReviewsResults reviewsResults= mMovie.getReviewsResults();
                mMovie.setReviewsResults(reviewsResults);
                tvReviewCount.setText(String.valueOf("("+mMovie.getReviewsResults().getTotalReviews())+")");
                setReviewRecyclerAdapter(rvReviewList);

                //set runtime
                tvRuntime.setText(Utils.timeToDisplay(mMovie.getRuntime()));
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                Log.d(Utils.TAG, "onFailure: "+t.getMessage());
            }
        });
    }

}
