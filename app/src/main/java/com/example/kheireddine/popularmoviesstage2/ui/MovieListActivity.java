package com.example.kheireddine.popularmoviesstage2.ui;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.kheireddine.popularmoviesstage2.R;
import com.example.kheireddine.popularmoviesstage2.data.MovieContract;
import com.example.kheireddine.popularmoviesstage2.model.Movie;
import com.example.kheireddine.popularmoviesstage2.model.MoviesResults;
import com.example.kheireddine.popularmoviesstage2.ui.adapters.MovieListAdapter;
import com.example.kheireddine.popularmoviesstage2.utils.Constants;
import com.example.kheireddine.popularmoviesstage2.utils.Utils;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieListActivity extends MainActivity
        implements MovieListAdapter.IMovieListListener, LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.rv_movies_list) RecyclerView rvMovieList;
    private List<Movie> mMoviesList;
    private MovieListAdapter mAdapter;
    private String SORT_BY = Constants.SORT_BY_DEFAULT;
    private static final int TITLE_MOVIE_DEFAULT = R.string.toolbar_pop_movies;

    // Refers to a unique loader
    private static final int MOVIE_LOADER_ID = 0;



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
                httpGetMovies(SORT_BY);
            }


            // invalid API_KEY
            else{
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

    private void setRecyclerAdapter(RecyclerView recyclerView, List<Movie> movieList) {
        mAdapter = new MovieListAdapter(mContext, movieList, this);
        recyclerView.setAdapter(mAdapter);
    }

    private void setRecyclerAdapter(RecyclerView recyclerView, Cursor cursor) {
        mAdapter = new MovieListAdapter(mContext, cursor, this);
        recyclerView.setAdapter(mAdapter);
    }


    /**
     * Click on a movie
     */
    @Override
    public void onMovieListClick(int clickMovieIndex) {
        Movie mMovieClicked = mMoviesList.get(clickMovieIndex);
        Intent movieDetailsIntent = new Intent(MovieListActivity.this, MovieDetailsActivity.class);
        movieDetailsIntent.putExtra(Constants.EXTRA_PARCELABLE_MOVIE, Parcels.wrap(mMovieClicked));

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

            case R.id.item_sort_by_favourite:
                item.setChecked(true);
                setToolBar(getString(R.string.toolbar_favourite_movies));
                dbGetFavouriteMovies();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }

    }

    /**************************************************************************************************
     *                                            HTTP calls
     ************************************************************************************************/
    private void httpGetMovies(String sortBy) {
        Call<MoviesResults> call = mdbAPI.getPopluarMovies(sortBy);
        call.enqueue(new Callback<MoviesResults>() {
            @Override
            public void onResponse(Call<MoviesResults> call, Response<MoviesResults> response) {
                if (response.isSuccessful()) {
                    mMoviesList = response.body().getmMoviesResults();
                    if (mMoviesList.size() != 0) {
                        setRecyclerAdapter(rvMovieList,mMoviesList);
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


    /**************************************************************************************************
     *                                            DATABASE calls
     ************************************************************************************************/
    private void dbGetFavouriteMovies() {
        getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, null, MovieListActivity.this);
        Log.d(Utils.TAG, "dbGetFavouriteMovies: ");
    }


    // Loader
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(this) {

            Cursor mMovieData = null;

            @Override
            protected void onStartLoading() {
                if (mMovieData !=null){
                    deliverResult(mMovieData);
                } else{
                    forceLoad();
                }
            }

            @Override
            public Cursor loadInBackground() {
                Log.d(Utils.TAG, " loadInBackground: ");

                try{
                    Cursor cursor = getContentResolver().query(MovieContract.FavouriteMovieEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            MovieContract.FavouriteMovieEntry._ID);

                    return cursor;

                }catch (Exception e){

                    Log.e(Utils.TAG, Constants.EXCEPTION_RESOLVER_QUERY);
                    e.printStackTrace();
                    return null;
                }

            }

            public void deliverResult(Cursor data) {
                mMovieData = data;
                super.deliverResult(data);
            }

        };

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
        setRecyclerAdapter(rvMovieList, data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }


}

