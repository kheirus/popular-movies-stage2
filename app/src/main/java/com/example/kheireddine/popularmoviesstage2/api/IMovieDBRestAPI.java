package com.example.kheireddine.popularmoviesstage2.api;

import com.example.kheireddine.popularmoviesstage2.model.Movie;
import com.example.kheireddine.popularmoviesstage2.model.MoviesResults;
import com.example.kheireddine.popularmoviesstage2.model.ReviewsResults;
import com.example.kheireddine.popularmoviesstage2.model.TrailersResults;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by kheireddine on 30/01/17.
 */

public interface IMovieDBRestAPI {
    //http://api.themoviedb.org/3/movie/popular?api_key=999999999999999999
    @GET("movie/{sort_by}")
    Call<MoviesResults> getPopluarMovies(@Path("sort_by") String sortBy);

    //http://api.themoviedb.org/3/movie/123?api_key=99999999999999999999
//    @GET("movie/{movie_id}")
//    Call<Movie> getMovieDetails(@Path("movie_id")long movieId,
//                                @Query("append_to_response") String appendToResponseList);

    @GET("movie/{id}/videos")
    Call<TrailersResults> getMovieTrailers(@Path("id") long movieId);

    @GET("movie/{id}/reviews")
    Call<ReviewsResults> getMovieReviews(@Path("id") long movieId);

    @GET("movie/{id}/images")
    Call<Movie.Images> getMovieImages(@Path("id") long movieId);


}
