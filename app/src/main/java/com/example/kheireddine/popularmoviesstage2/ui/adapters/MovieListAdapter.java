package com.example.kheireddine.popularmoviesstage2.ui.adapters;

import android.content.Context;
import android.database.Cursor;
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

import static com.example.kheireddine.popularmoviesstage2.data.MovieContract.FavouriteMovieEntry.*;

/**
 * Created by kheireddine on 30/01/17.
 */

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieViewHolder> {

    private Context mContext;
    private List<Movie> moviesList;
    private Cursor mCursor;
    boolean isCursor;
    final private IMovieListListener mOnClickListener;

    /**
     * The interface that receives onClick messages
     */
    public interface IMovieListListener {
        //TODO add a second parameter to know wich adapter are clicked : cursor or movielist
        // I can just add the Intent of the next activity (DetailsActivity) as a String
        void onMovieListClick(int clickMovieIndex, int type);
    }

    public MovieListAdapter(Context mContext, List<Movie> moviesList, IMovieListListener listener) {
        this.mContext = mContext;
        this.moviesList = moviesList;
        this.mOnClickListener = listener;

        isCursor = false;
    }

    public MovieListAdapter(Context mContext, Cursor cursor, IMovieListListener listener) {
        this.mContext = mContext;
        this.mCursor = cursor;
        this.mOnClickListener = listener;

        isCursor = true;
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
        if (!isCursor){
            //the data is a list of movies
            Movie mMovie = moviesList.get(position);
            poster = mMovie.getPoster();
            rating = mMovie.getRating();
            title = mMovie.getTitle();

        } else {
            // the data is a cursor
            mCursor.moveToPosition(position);
            poster = mCursor.getString(mCursor.getColumnIndex(COLUMN_POSTER));
            rating = mCursor.getString(mCursor.getColumnIndex(COLUMN_RATING));
            title = mCursor.getString(mCursor.getColumnIndex(COLUMN_TITLE));
        }

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
        if (isCursor){
            // Data is a cursor
            return mCursor.getCount();
        } else {
            // Data is a movie list
            return moviesList.size();
        }
    }


    /**
     * When data changes and a re-query occurs, this function swaps the old Cursor
     * with a newly updated Cursor (Cursor c) that is passed in.
     */
    public Cursor swapCursor(Cursor c) {
        // check if this cursor is the same as the previous cursor (mCursor)
        if (mCursor == c) {
            return null; // bc nothing has changed
        }
        Cursor temp = mCursor;
        this.mCursor = c; // new cursor value assigned

        //check if this is a valid cursor, then update the cursor
        if (c != null) {
            this.notifyDataSetChanged();
        }
        return temp;
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

            if (isCursor){
                type = Constants.MOVIE_FROM_CURSOR;
            } else {
                type = Constants.MOVIE_FROM_LIST;
            }

            int clickedPosition = getAdapterPosition();
            mOnClickListener.onMovieListClick(clickedPosition, type);
        }
    }

}
