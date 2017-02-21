package com.example.kheireddine.popularmoviesstage2.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kheireddine.popularmoviesstage2.R;
import com.example.kheireddine.popularmoviesstage2.api.MovieDBServiceAPI;
import com.example.kheireddine.popularmoviesstage2.model.Movie;
import com.example.kheireddine.popularmoviesstage2.utils.Utils;
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

    /**
     * The interface that receives onClick messages
     */
    public interface IMovieListListener {
        void onMovieListClick(int clickMovieIndex);
    }

    public MovieListAdapter(Context mContext, List<Movie> moviesList, IMovieListListener listener) {
        this.mContext = mContext;
        this.moviesList = moviesList;
        this.mOnClickListener = listener;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context mContext = parent.getContext();
        int layoutIdForMovieItem = R.layout.movie_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);

        View view = inflater.inflate(layoutIdForMovieItem,parent,false);
        MovieViewHolder viewHolder = new MovieViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        Movie mMovie = moviesList.get(position);
        Log.d(Utils.TAG, "poster =  "+mMovie.getPoster());
        Picasso.with(mContext)
                .load(MovieDBServiceAPI.API_POSTER_HEADER_LARGE +mMovie.getPoster())
                .placeholder(R.drawable.poster_placeholder)
                .error(R.drawable.poster_error)
                .into(holder.ivPoser);

        holder.tvRatingMovieItem.setText(mMovie.getRating());
        holder.tvTitle.setText(mMovie.getTitle());
        holder.itemView.setTag(mMovie);

    }

    @Override
    public int getItemCount() {
        return moviesList.size();
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
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onMovieListClick(clickedPosition);
        }
    }

}
