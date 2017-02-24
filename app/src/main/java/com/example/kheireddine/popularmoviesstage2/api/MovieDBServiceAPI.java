package com.example.kheireddine.popularmoviesstage2.api;

import com.example.kheireddine.popularmoviesstage2.utils.Constants;

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

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    private static Retrofit retrofit;

    public static <S> S createService(Class<S> serviceClass) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.addInterceptor(logging).addInterceptor(new AuthInterceptor());


        // Creation of retrofit object
        retrofit =  new Retrofit.Builder()
                .baseUrl(Constants.API_BASE_URL)
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
                    .addQueryParameter("api_key", Constants.API_KEY)
                    .build();
            Request request = chain.request().newBuilder().url(url).build();
            return chain.proceed(request);
        }
    }
}