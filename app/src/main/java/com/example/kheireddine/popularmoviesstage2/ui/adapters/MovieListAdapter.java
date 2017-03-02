package com.example.kheireddine.popularmoviesstage2.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kheireddine.popularmoviesstage2.R;
import com.example.kheireddine.popularmoviesstage2.model.Movie;
import com.example.kheireddine.popularmoviesstage2.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kheireddine on 30/01/17.
 */

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieViewHolder> {

    private Context mContext;
    private List<Movie> moviesList;
    final private IMovieListListener mOnClickListener;
    boolean isFavouriteMovie;

    /**
     * The interface that receives onClick messages
     */
    public interface IMovieListListener {
        // I can just add the Intent of the next activity (DetailsActivity) as a String
        void onMovieListClick(int clickMovieIndex, int type);
    }

    public MovieListAdapter(Context mContext, List<Movie> moviesList, IMovieListListener listener) {
        this.mContext = mContext;
        this.moviesList = moviesList;
        this.mOnClickListener = listener;

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

        @Override
        public void onClick(View v) {
            int type;

            if (isFavouriteMovie){
                type = Constants.MOVIE_FROM_CURSOR;
            } else {
                type = Constants.MOVIE_FROM_LIST;
            }

            int clickedPosition = getAdapterPosition();
            mOnClickListener.onMovieListClick(clickedPosition, type);
        }
    }

}
