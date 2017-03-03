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

import static com.example.kheireddine.popularmoviesstage2.data.MovieContract.FavouriteMovieEntry.*;
import static com.example.kheireddine.popularmoviesstage2.utils.Constants.*;

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
    private static boolean isFavSorting;


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


    @Override
    protected void onResume() {
        super.onResume();

        // re-queries for all movies from db
        if (isFavSorting)
            getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
    }

    public void setLayoutManager() {
        //StaggeredGridLayoutManager sglm = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        int nbCell = Utils.calculateNoOfColumns(mContext);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, nbCell);
        rvMovieList.setLayoutManager(gridLayoutManager);
        rvMovieList.setHasFixedSize(true);
    }

    // Adapter with a list of movie as a data
    private void setRecyclerAdapter(RecyclerView recyclerView, List<Movie> movieList) {
        mAdapter = new MovieListAdapter(mContext, movieList, this);
        recyclerView.setAdapter(mAdapter);
    }


    /**
     * Click on a movie
     */
    @Override
    public void onMovieListClick(int clickMovieIndex, int type) {
        //TODO BUG BUG BUG when favourite movie is selected the position is false
        Movie mMovieClicked = mMoviesList.get(clickMovieIndex);
        Intent movieDetailsIntent = new Intent(MovieListActivity.this, MovieDetailsActivity.class);
        int movieFromType;

        switch (type){
            case MOVIE_FROM_LIST:
                movieFromType = MOVIE_FROM_LIST;
                break;
            case MOVIE_FROM_CURSOR:
                movieFromType = MOVIE_FROM_CURSOR;
                break;

            default:
                return;
        }

        movieDetailsIntent.putExtra(Constants.EXTRA_PARCELABLE_MOVIE, Parcels.wrap(mMovieClicked));
        movieDetailsIntent.putExtra(EXTRA_MOVIE_FROM_TYPE, movieFromType);
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
                isFavSorting = false;
                return true;

            case R.id.item_sort_by_top_rated:
                SORT_BY = Constants.SORT_BY_TOP_RATED;
                item.setChecked(true);
                setToolBar(getString(R.string.toolbar_top_movies));
                httpGetMovies(SORT_BY);
                isFavSorting = false;
                return true;

            case R.id.item_sort_by_favourite:
                item.setChecked(true);
                setToolBar(getString(R.string.toolbar_favourite_movies));
                dbGetFavouriteMovies();
                isFavSorting = true;
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
    }


    // Loader that fetch movies from database due to ContentProvider
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
        //TODO create function that cast the cursor on list of movie

        mMoviesList = cursorToListMovies(data);
        setRecyclerAdapter(rvMovieList, mMoviesList);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }


    /**
     * Method that fill a list of movies with data retrieved from a cursor
     * @param cursor Data retrieved from database
     * @return List of movies
     * */
    public List<Movie> cursorToListMovies(Cursor cursor){
        List<Movie> listMovies = new ArrayList<>();
        while (cursor.moveToNext()){
            Movie movie = new Movie();
            movie.setFavourite(true);
            movie.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_ID)));
            movie.setPoster(cursor.getString(cursor.getColumnIndex(COLUMN_POSTER)));
            movie.setBackdrop(cursor.getString(cursor.getColumnIndex(COLUMN_BACKDROP)));
            movie.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
            movie.setRating(cursor.getString(cursor.getColumnIndex(COLUMN_RATING)));
            movie.setReleaseDate(cursor.getString(cursor.getColumnIndex(COLUMN_RELEASE_DATE)));
            movie.setRuntime(cursor.getString(cursor.getColumnIndex(COLUMN_RUNTIME)));
            movie.setSynopsis(cursor.getString(cursor.getColumnIndex(COLUMN_SYNOPSIS)));

            listMovies.add(movie);
        }
        return listMovies;
    }

}

