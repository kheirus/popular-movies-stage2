package com.example.kheireddine.popularmoviesstage2.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kheireddine.popularmoviesstage2.R;
import com.example.kheireddine.popularmoviesstage2.model.Movie;
import com.example.kheireddine.popularmoviesstage2.model.Review;
import com.example.kheireddine.popularmoviesstage2.model.ReviewsResults;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kheireddine on 23/02/17.
 */

public class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.ReviewViewHolder> {
    private Context mContext;
    private Movie mMovie;
    final private IReviewListListener mOnClickListener;

    /**
     * The interface that receives onClick messages
     */
    public interface IReviewListListener {
        void onReviewListClick(int clickReviewIndex);
    }

    public ReviewListAdapter(Context mContext, Movie movie, IReviewListListener listener) {
        this.mContext = mContext;
        this.mMovie = movie;
        this.mOnClickListener = listener;
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context mContext = parent.getContext();
        int layourIdForReviewItem = R.layout.review_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);

        View view = inflater.inflate(layourIdForReviewItem, parent, false);
        ReviewViewHolder viewHolder = new ReviewViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        ReviewsResults reviewsResults = mMovie.getReviewsResults();
        Review mReview = reviewsResults.getReviews().get(position);

        holder.tvReviewAuthor.setText(mReview.getAuthor()+" :");
        holder.tvReviewContent.setText(mReview.getContent());
    }

    @Override
    public int getItemCount() {
        if (mMovie.getReviewsResults() == null){
            return 0;
        }
        return mMovie.getReviewsResults().getReviews().size();
    }




    protected class ReviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.tv_review_author) TextView tvReviewAuthor;
        @BindView(R.id.tv_review_content) TextView tvReviewContent;
        public ReviewViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // If we're within the ReveiwsActivity (all reviews displayed) we don't need to click at all.
            if (mOnClickListener == null){
                // do nothing
            }
            else {
                int clickPosition = getAdapterPosition();
                mOnClickListener.onReviewListClick(clickPosition);
            }

        }
    }

}
