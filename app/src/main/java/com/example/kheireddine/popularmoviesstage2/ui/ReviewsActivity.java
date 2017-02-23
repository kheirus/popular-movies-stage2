package com.example.kheireddine.popularmoviesstage2.ui;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kheireddine.popularmoviesstage2.R;
import com.example.kheireddine.popularmoviesstage2.model.Movie;
import com.example.kheireddine.popularmoviesstage2.model.Review;
import com.example.kheireddine.popularmoviesstage2.model.ReviewResults;
import com.example.kheireddine.popularmoviesstage2.ui.adapters.ReviewListAdapter;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.kheireddine.popularmoviesstage2.ui.MovieDetailsActivity.EXTRA_PARCELABLE_MOVIE;

public class ReviewsActivity extends AppCompatActivity implements ReviewListAdapter.IReviewListListener {

    @BindView(R.id.rv_reviews_activity) RecyclerView rvReviews;
    private List<Review> mReviewList;
    private ReviewsActivityAdapter mReviewAdapter;
    private Movie mMovie;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);
        ButterKnife.bind(this);

        setReviewLayoutManager();
        mMovie = Parcels.unwrap(getIntent().getExtras().getParcelable(EXTRA_PARCELABLE_MOVIE));
        ReviewResults reviewResults = mMovie.getReviewResults();
        mReviewList = reviewResults.getReviews();
        setReviewRecyclerAdapter(rvReviews);

    }

    public void setReviewLayoutManager() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager (this,LinearLayoutManager.VERTICAL,false);
        rvReviews.setLayoutManager(linearLayoutManager);
        rvReviews.setHasFixedSize(true);
    }

    private void setReviewRecyclerAdapter(RecyclerView recyclerView){
        mReviewAdapter = new ReviewsActivityAdapter(this, mReviewList);
        recyclerView.setAdapter(mReviewAdapter);
    }

    @Override
    public void onReviewListClick(int clickReviewIndex) {

    }


    private class ReviewsActivityAdapter extends ReviewListAdapter{

        public ReviewsActivityAdapter(Context mContext, List<Review> Reviews) {
            super(mContext, Reviews,null);
        }

        @Override
        public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context mContext = parent.getContext();
            int layourIdForReviewItem = R.layout.review_activity_item;
            LayoutInflater inflater = LayoutInflater.from(mContext);

            View view = inflater.inflate(layourIdForReviewItem, parent, false);
            ReviewViewHolder viewHolder = new ReviewViewHolder(view);

            return viewHolder;


        }
    }

}

