package com.example.kheireddine.popularmoviesstage2.api;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by kheireddine on 30/01/17.
 */
public class MovieDBServiceAPI {
    public static final String API_BASE_URL = "http://api.themoviedb.org/3/";
    public static final String API_POSTER_HEADER_LARGE = "http://image.tmdb.org/t/p/w185";
    public static final String API_POSTER_HEADER_SMALL = "http://image.tmdb.org/t/p/w92";
    public static final String API_BACKDROP_HEADER = "http://image.tmdb.org/t/p/w780";
    public static final String YOUTUBE_URL = "https://www.youtube.com/watch?v=";



    public static final String SORT_BY_TOP_RATED = "top_rated";
    public static final String SORT_BY_POPOLARITY = "popular";
    public static final String SORT_BY_DEFAULT = SORT_BY_POPOLARITY;

    //TODO change YOUR_API_KEY to your own api_key
    public static final String API_KEY = "";


    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    private static Retrofit retrofit;

    public static <S> S createService(Class<S> serviceClass) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.addInterceptor(logging).addInterceptor(new AuthInterceptor());


        // Creation of retrofit object
        retrofit =  new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();


        return retrofit.create(serviceClass);
    }

    //To add "api_key" parameter at the end of each request
    private static class AuthInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            HttpUrl url = chain.request().url()
                    .newBuilder()
                    .addQueryParameter("api_key", API_KEY)
                    .build();
            Request request = chain.request().newBuilder().url(url).build();
            return chain.proceed(request);
        }
    }
}