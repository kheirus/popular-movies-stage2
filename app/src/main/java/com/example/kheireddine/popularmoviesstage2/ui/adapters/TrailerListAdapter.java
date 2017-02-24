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
import com.example.kheireddine.popularmoviesstage2.model.Trailer;
import com.example.kheireddine.popularmoviesstage2.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kheireddine on 16/02/17.
 */

public class TrailerListAdapter extends RecyclerView.Adapter<TrailerListAdapter.TrailerViewHolder> {
    private Context mContext;
    private List<Trailer> trailers;
    private Movie mMovie;
    final private ITrailerListListener mOnClickListener;

    /**
     * The interface that receives onClick messages
     */
    public interface ITrailerListListener {
        void onTrailerListClick(int clickTrailerIndex);
    }

    public TrailerListAdapter(Context mContext, List<Trailer> trailers, Movie movie, ITrailerListListener listener) {
        this.mContext = mContext;
        this.trailers = trailers;
        this.mOnClickListener = listener;
        this.mMovie = movie;
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context mContext = parent.getContext();
        int layourIdForTrailerItem = R.layout.trailer_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);

        View view = inflater.inflate(layourIdForTrailerItem, parent, false);
        TrailerViewHolder viewHolder = new TrailerViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
        // TODO handle case when there is trailers > backdrops
        Trailer mTrailer = trailers.get(position);
        String mBackdrop = mMovie.getImages().getBackdropsList().get(position).getPath();

        Picasso.with(mContext)
                .load(Constants.API_BACKDROP_HEADER +mBackdrop)
                .placeholder(R.drawable.poster_placeholder)
                .error(R.drawable.poster_error)
                .into(holder.ivTrailerBackdrop);
        holder.tvTrailerTitle.setText(mTrailer.getName());
        holder.itemView.setTag(mTrailer.getKey());
    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }




    class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.iv_trailer_backdrop) ImageView ivTrailerBackdrop;
        @BindView(R.id.tv_trailer_title) TextView tvTrailerTitle;
        public TrailerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int clickPosition = getAdapterPosition();
            mOnClickListener.onTrailerListClick(clickPosition);
        }
    }

}
