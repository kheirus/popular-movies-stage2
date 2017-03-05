package com.example.kheireddine.popularmoviesstage2.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kheireddine.popularmoviesstage2.R;
import com.example.kheireddine.popularmoviesstage2.model.Movie;
import com.example.kheireddine.popularmoviesstage2.ui.activities.MainActivity;
import com.example.kheireddine.popularmoviesstage2.ui.activities.MovieDetailsActivity;
import com.example.kheireddine.popularmoviesstage2.ui.fragments.MovieDetailsFragment;
import com.example.kheireddine.popularmoviesstage2.utils.Constants;
import com.example.kheireddine.popularmoviesstage2.utils.Utils;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.kheireddine.popularmoviesstage2.utils.Constants.EXTRA_MOVIE_FROM_TYPE;
import static com.example.kheireddine.popularmoviesstage2.utils.Constants.EXTRA_PARCELABLE_MOVIE;
import static com.example.kheireddine.popularmoviesstage2.utils.Constants.MOVIE_FROM_CURSOR;
import static com.example.kheireddine.popularmoviesstage2.utils.Constants.MOVIE_FROM_LIST;

/**
 * Created by kheireddine on 30/01/17.
 */

public class MovieGridAdapter extends RecyclerView.Adapter<MovieGridAdapter.MovieViewHolder> {

    private Context mContext;
    private List<Movie> moviesList;
    private boolean isFavouriteMovie;
    private boolean mTwoPane;

    public MovieGridAdapter(Context mContext, List<Movie> moviesList, boolean twoPane) {
        this.mContext = mContext;
        this.moviesList = moviesList;
        mTwoPane = twoPane;

    }


    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);

        View view = inflater.inflate(R.layout.movie_item, parent, false);
        MovieViewHolder viewHolder = new MovieViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        String title, rating, poster;

        //the data is a list of movies
        Movie mMovie = moviesList.get(position);
        if (mMovie.isFavourite()){
            isFavouriteMovie = true;
        }
        poster = mMovie.getPoster();
        rating = mMovie.getRating();
        title = mMovie.getTitle();

        Picasso.with(mContext)
                .load(Constants.API_POSTER_HEADER_LARGE +poster)
                .placeholder(R.drawable.poster_placeholder)
                .error(R.drawable.poster_error)
                .into(holder.ivPoser);

        holder.tvRatingMovieItem.setText(rating);
        holder.tvTitle.setText(title);


    }

    @Override
    public int getItemCount() {
        if (moviesList !=null)
            return moviesList.size();
        else
            return 0;
    }


    /**
     * Cache of the children views for a list movie
     */
    class MovieViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{

        @BindView(R.id.iv_poster) ImageView ivPoser;
        @BindView(R.id.tv_title) TextView tvTitle;
        @BindView(R.id.tv_rating_movie_item) TextView tvRatingMovieItem;

        public MovieViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }


        /**
         * Click on a movie
         */
        @Override
        public void onClick(View v) {
            int clickMovieIndex = getAdapterPosition();
            if (clickMovieIndex != RecyclerView.NO_POSITION) {

                Movie mMovieClicked = moviesList.get(clickMovieIndex);

                int movieFromType;
                int type;

                if (isFavouriteMovie){
                    type = Constants.MOVIE_FROM_CURSOR;
                } else {
                    type = Constants.MOVIE_FROM_LIST;
                }

                switch (type) {
                    case MOVIE_FROM_LIST:
                        movieFromType = MOVIE_FROM_LIST;
                        break;
                    case MOVIE_FROM_CURSOR:
                        movieFromType = MOVIE_FROM_CURSOR;
                        break;

                    default:
                        return;
                }
                if (mTwoPane){
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(EXTRA_PARCELABLE_MOVIE, Parcels.wrap(mMovieClicked));
                    bundle.putInt(EXTRA_MOVIE_FROM_TYPE,movieFromType);
                    addDetailFragmentForTwoPane(bundle);
                }
                else {
                    Log.d(Utils.TAG, "Intent called ");
                    Intent movieDetailsIntent = new Intent(mContext, MovieDetailsActivity.class);
                    movieDetailsIntent.putExtra(EXTRA_PARCELABLE_MOVIE, Parcels.wrap(mMovieClicked));
                    movieDetailsIntent.putExtra(EXTRA_MOVIE_FROM_TYPE, movieFromType);
                    mContext.startActivity(movieDetailsIntent);
                }
            }
        }

        public void addDetailFragmentForTwoPane(Bundle bundle) {
            MovieDetailsFragment detailFragment = MovieDetailsFragment.create(bundle);
            FragmentManager fragmentManager = ((MainActivity) mContext).getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fl_details, detailFragment)
                    .commit();
        }
    }

}
