package com.example.kheireddine.popularmoviesstage2.ui.fragments;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.kheireddine.popularmoviesstage2.R;
import com.example.kheireddine.popularmoviesstage2.api.ITheMovieDbRestAPI;
import com.example.kheireddine.popularmoviesstage2.api.TheMovieDbServiceAPI;
import com.example.kheireddine.popularmoviesstage2.data.MovieContract;
import com.example.kheireddine.popularmoviesstage2.model.Movie;
import com.example.kheireddine.popularmoviesstage2.model.MoviesResults;
import com.example.kheireddine.popularmoviesstage2.ui.activities.MovieDetailsActivity;
import com.example.kheireddine.popularmoviesstage2.ui.adapters.MovieGridAdapter;
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

import static com.example.kheireddine.popularmoviesstage2.data.MovieContract.FavouriteMovieEntry.COLUMN_BACKDROP;
import static com.example.kheireddine.popularmoviesstage2.data.MovieContract.FavouriteMovieEntry.COLUMN_ID;
import static com.example.kheireddine.popularmoviesstage2.data.MovieContract.FavouriteMovieEntry.COLUMN_POSTER;
import static com.example.kheireddine.popularmoviesstage2.data.MovieContract.FavouriteMovieEntry.COLUMN_RATING;
import static com.example.kheireddine.popularmoviesstage2.data.MovieContract.FavouriteMovieEntry.COLUMN_RELEASE_DATE;
import static com.example.kheireddine.popularmoviesstage2.data.MovieContract.FavouriteMovieEntry.COLUMN_RUNTIME;
import static com.example.kheireddine.popularmoviesstage2.data.MovieContract.FavouriteMovieEntry.COLUMN_SYNOPSIS;
import static com.example.kheireddine.popularmoviesstage2.data.MovieContract.FavouriteMovieEntry.COLUMN_TITLE;
import static com.example.kheireddine.popularmoviesstage2.utils.Constants.EXTRA_MOVIE_FROM_TYPE;
import static com.example.kheireddine.popularmoviesstage2.utils.Constants.EXTRA_PARCELABLE_MOVIE;
import static com.example.kheireddine.popularmoviesstage2.utils.Constants.MOVIE_FROM_CURSOR;
import static com.example.kheireddine.popularmoviesstage2.utils.Constants.MOVIE_FROM_LIST;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    @BindView(R.id.rv_movies_list)
    RecyclerView rvMovieList;
    private List<Movie> mMoviesList;
    private MovieGridAdapter mAdapter;
    private String SORT_BY = Constants.SORT_BY_DEFAULT;
    private Context mContext;
    public ITheMovieDbRestAPI mdbAPI;


    private static final int TITLE_MOVIE_DEFAULT = R.string.toolbar_pop_movies;

    private int itemMenuSelected = -1;
    private MenuItem menuItem;
    private boolean mTwoPane;


    // Refers to a unique loader
    private static final int MOVIE_LOADER_ID = 1;
    private static boolean isFavSorting;


    public static MainFragment create(){
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_main, container, false);
        if (getActivity().findViewById(R.id.fl_details) != null) {
            mTwoPane = true;
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        // re-queries for all movies from db
        if (isFavSorting)
            getActivity().getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mdbAPI = TheMovieDbServiceAPI.createService(ITheMovieDbRestAPI.class);
        setHasOptionsMenu(true);

        mContext = getContext();
        ButterKnife.bind(this, view);
        setToolBar(getString(TITLE_MOVIE_DEFAULT));
        setLayoutManager();

        /** Check network and api_key */
        if (Utils.isOnline(mContext)) {
            if (Utils.isValidApiKey()){
                httpGetMovies(SORT_BY);
            }

            // invalid API_KEY
            else{
                Utils.showDialog(getActivity(), getString(R.string.dialog_error_api_key_title), getString(R.string.dialog_error_api_key_message));
            }

        }
        // No network
        else
            Utils.showDialog(getActivity(), getString(R.string.dialog_error_network_title), getString(R.string.dialog_error_network_message));
    }



//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        //outState.putParcelableArrayList(STATE_MOVIE_LIST, (ArrayList<? extends Parcelable>) mMoviesList);
//        //outState.putSerializable(STATE_MOVIE_LIST, (Serializable) mMoviesList);
//        outState.putInt(STATE_MENU_SELECTED,itemMenuSelected);
//        super.onSaveInstanceState(outState);
//    }
//
//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        //mMoviesList = savedInstanceState.getParcelableArrayList(STATE_MOVIE_LIST);
//        //mMoviesList = (List<Movie>) savedInstanceState.getSerializable(STATE_MOVIE_LIST);
//        itemMenuSelected = savedInstanceState.getInt(STATE_MENU_SELECTED);
//
//        super.onRestoreInstanceState(savedInstanceState);
//    }

    public void setLayoutManager() {
        //StaggeredGridLayoutManager sglm = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        int nbCell;

        if (mTwoPane)
            nbCell = 2;
        else
            nbCell= Utils.calculateNoOfColumns(mContext);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, nbCell);
        rvMovieList.setLayoutManager(gridLayoutManager);
        rvMovieList.setHasFixedSize(true);
    }

    // Adapter with a list of movie as a data
    private void setRecyclerAdapter(RecyclerView recyclerView, List<Movie> movieList) {
        mAdapter = new MovieGridAdapter(mContext, movieList, mTwoPane);
        recyclerView.setAdapter(mAdapter);
    }


    /**
     * Menu
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);

        if (itemMenuSelected == -1){
            return;
        }

        switch (itemMenuSelected){
            case R.id.item_sort_by_popularity:
                menuItem = menu.findItem(R.id.item_sort_by_popularity);
                menuItem.setChecked(true);
                break;
            case R.id.item_sort_by_top_rated:
                menuItem = menu.findItem(R.id.item_sort_by_top_rated);
                menuItem.setChecked(true);
                break;

            case R.id.item_sort_by_favourite:
                menuItem = menu.findItem(R.id.item_sort_by_favourite);
                menuItem.setChecked(true);
                break;
        }
        return;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.item_sort_by_popularity:
                SORT_BY = Constants.SORT_BY_POPOLARITY;
                item.setChecked(true);
                setToolBar(getString(R.string.toolbar_pop_movies));
                httpGetMovies(SORT_BY);
                isFavSorting = false;
                itemMenuSelected = id;
                return true;

            case R.id.item_sort_by_top_rated:
                SORT_BY = Constants.SORT_BY_TOP_RATED;
                item.setChecked(true);
                setToolBar(getString(R.string.toolbar_top_movies));
                httpGetMovies(SORT_BY);
                isFavSorting = false;
                itemMenuSelected = id;
                return true;

            case R.id.item_sort_by_favourite:
                item.setChecked(true);
                setToolBar(getString(R.string.toolbar_favourite_movies));
                dbGetFavouriteMovies();
                isFavSorting = true;
                itemMenuSelected = id;
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }

    }

    public void setToolBar(String title) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(title);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(false);
    }

    public void addDetailFragmentForTwoPane(Bundle bundle) {
        MovieDetailsFragment detailFragment = MovieDetailsFragment.create(bundle);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fl_details, detailFragment)
                .commit();
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
                        if (mTwoPane){
                            Bundle bundle = new Bundle();
                            bundle.putParcelable(EXTRA_PARCELABLE_MOVIE, Parcels.wrap(mMoviesList.get(0)));
                            addDetailFragmentForTwoPane(bundle);
                        }
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
        getActivity().getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
    }


    // Loader that fetch movies from database due to ContentProvider
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(mContext) {

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
                    Cursor cursor = getActivity().getContentResolver().query(MovieContract.FavouriteMovieEntry.CONTENT_URI,
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
