package com.link.data.album.source.remote;

import android.util.Log;


import com.link.data.album.source.AlbumsDataSource;
import com.link.data.album.source.remote.model.Album;
import com.link.data.album.source.remote.model.AlbumImages;
import com.link.data.album.source.remote.services.AlbumsApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlbumRemoteDataSource implements AlbumsDataSource {

    private static AlbumRemoteDataSource instance;

    private final AlbumsApi albumsApi;

    private AlbumRemoteDataSource(AlbumsApi albumsApi) {
        this.albumsApi = albumsApi;
    }

    public static AlbumRemoteDataSource getInstance(AlbumsApi albumsApi) {
        if (instance == null) {
            instance = new AlbumRemoteDataSource(albumsApi);
        }
        return instance;
    }


    @Override
    public void getAlbums(final LoadAlbumsCallback callback) {
        albumsApi.getAlbums().enqueue(new Callback<ArrayList<Album>>() {

            @Override
            public void onResponse(Call<ArrayList<Album>> call, Response<ArrayList<Album>> response) {
                Log.e("res", response.raw().toString());
                List<Album> albums = response.body();
                if (albums != null && !albums.isEmpty()) {
                    callback.onAlbumsLoaded(albums);
                } else {
                    callback.onDataNotAvailable();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Album>> call, Throwable t) {
                Log.e("t",t.getMessage(),t);
                callback.onError();
            }
        });
    }

    @Override
    public void getAlbum(int value, final LoadAlbumCallback callback) {
        albumsApi.getAlbumDetail(value).enqueue(new Callback<ArrayList<AlbumImages>>() {
            @Override
            public void onResponse(Call<ArrayList<AlbumImages>> call, Response<ArrayList<AlbumImages>> response) {
                Log.e("res", response.raw().toString());
                List<AlbumImages> albumImages = response.body();
                if (albumImages != null) {
                    callback.onAlbumLoaded(albumImages);
                } else {
                    callback.onDataNotAvailable();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<AlbumImages>> call, Throwable t) {
                callback.onError();
            }
        });
    }

    @Override
    public void saveAlbums(List<Album> albums) {
    }

    @Override
    public void saveAlbum(List<AlbumImages> albumImages) {

    }
}
