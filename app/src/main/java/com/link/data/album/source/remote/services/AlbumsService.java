package com.link.data.album.source.remote.services;

import com.link.Utils.Endpoints;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AlbumsService {

    private AlbumsApi mAlbumsApi;

    private static AlbumsService singleton;

    private AlbumsService() {
        Retrofit mRetrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(Endpoints.BASE_URL).build();
        mAlbumsApi = mRetrofit.create(AlbumsApi.class);
    }

    public static AlbumsService getInstance() {
        if (singleton == null) {
            singleton = new AlbumsService();
        }
        return singleton;
    }

    public AlbumsApi getAlbumsApi() {
        return mAlbumsApi;
    }
}
