package com.link.data.album.source.remote.services;

import com.link.Utils.Endpoints;
import com.link.data.album.source.remote.model.Album;
import com.link.data.album.source.remote.model.AlbumImages;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AlbumsApi {
    @GET(Endpoints.ALBUMS)
    Call<ArrayList<Album>> getAlbums();

    @GET(Endpoints.DETAIL_URL)
    Call<ArrayList<AlbumImages>> getAlbumDetail(@Query("albumId") int value);
}
