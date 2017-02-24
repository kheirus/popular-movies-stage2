package com.example.kheireddine.popularmoviesstage2.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.kheireddine.popularmoviesstage2.R;
import com.example.kheireddine.popularmoviesstage2.model.Movie;
import com.example.kheireddine.popularmoviesstage2.model.MoviesResults;
import com.example.kheireddine.popularmoviesstage2.ui.adapters.MovieListAdapter;
import com.example.kheireddine.popularmoviesstage2.utils.Constants;
import com.example.kheireddine.popularmoviesstage2.utils.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieListActivity extends MainActivity
        implements MovieListAdapter.IMovieListListener {

    @BindView(R.id.rv_movies_list)
    RecyclerView rvMovieList;
    private List<Movie> mMoviesList;
    private MovieListAdapter mAdapter;
    private String SORT_BY = Constants.SORT_BY_DEFAULT;
    private final static int NB_CELL = 2;
    private static final int TITLE_MOVIE_DEFAULT = R.string.toolbar_pop_movies;
    public static String EXTRA_MOVIE_ID = "extra_movie_id";
    public static String EXTRA_MOVIE_TITLE = "extra_movie_title";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setToolBar(getString(TITLE_MOVIE_DEFAULT));
        setLayoutManager();

        /** Check network and api_key */
        if (Utils.isOnline(mContext)) {
            if (Utils.isValidApiKey()){
                Log.d(Utils.TAG, "api valide ");
                httpGetMovies(SORT_BY);
            }


                // invalid API_KEY
            else{
                Log.d(Utils.TAG, "api invalide");
                Utils.showDialog(MovieListActivity.this, getString(R.string.dialog_error_api_key_title), getString(R.string.dialog_error_api_key_message));
            }


        }
        // No network
        else
            Utils.showDialog(MovieListActivity.this, getString(R.string.dialog_error_network_title), getString(R.string.dialog_error_network_message));

    }


    public void setLayoutManager() {
        //StaggeredGridLayoutManager sglm = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        //
        int nbCell = Utils.calculateNoOfColumns(mContext);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, nbCell);
        rvMovieList.setLayoutManager(gridLayoutManager);
        rvMovieList.setHasFixedSize(true);
    }

    private void setRecyclerAdapter(RecyclerView recyclerView) {
        mAdapter = new MovieListAdapter(mContext, mMoviesList, this);
        recyclerView.setAdapter(mAdapter);
    }


    /**
     * Click on a movie
     */
    @Override
    public void onMovieListClick(int clickMovieIndex) {
        Movie mMovieClicked = mMoviesList.get(clickMovieIndex);
        Intent movieDetailsIntent = new Intent(MovieListActivity.this, MovieDetailsActivity.class);
        movieDetailsIntent.putExtra(EXTRA_MOVIE_ID, mMovieClicked.getId());
        movieDetailsIntent.putExtra(EXTRA_MOVIE_TITLE, mMovieClicked.getTitle());
        startActivity(movieDetailsIntent);
    }


    /**
     * Menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_sort_by_popularity:
                SORT_BY = Constants.SORT_BY_POPOLARITY;
                item.setChecked(true);
                setToolBar(getString(R.string.toolbar_pop_movies));
                httpGetMovies(SORT_BY);
                return true;

            case R.id.item_sort_by_top_rated:
                SORT_BY = Constants.SORT_BY_TOP_RATED;
                item.setChecked(true);
                setToolBar(getString(R.string.toolbar_top_movies));
                httpGetMovies(SORT_BY);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }

    }

    /**************************************************************************************************
     *                                            HTTP calls
     ************************************************************************************************/
    public void httpGetMovies(String sortBy) {
        Call<MoviesResults> call = mdbAPI.getPopluarMovies(sortBy);
        call.enqueue(new Callback<MoviesResults>() {
            @Override
            public void onResponse(Call<MoviesResults> call, Response<MoviesResults> response) {
                if (response.isSuccessful()) {
                    mMoviesList = response.body().getmMoviesResults();
                    if (mMoviesList.size() != 0) {
                        setRecyclerAdapter(rvMovieList);
                    } else {
                        //TODO empty list error
                    }
                } else {
                    //TODO http response error
                }
            }

            @Override
            public void onFailure(Call<MoviesResults> call, Throwable t) {
                Utils.showLongToastMessage(mContext, "Error fetching movies :" + t.getMessage());
            }
        });

    }

}

