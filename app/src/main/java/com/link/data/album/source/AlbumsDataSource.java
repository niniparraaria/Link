package com.link.data.album.source;


import com.link.data.album.source.remote.model.Album;
import com.link.data.album.source.remote.model.AlbumImages;

import java.util.List;

public interface AlbumsDataSource {

    interface LoadAlbumsCallback {
        void onAlbumsLoaded(List<Album> albums);
        void onDataNotAvailable();
        void onError();
    }
    interface LoadAlbumCallback {
        void onAlbumLoaded(List<AlbumImages> album);
        void onDataNotAvailable();
        void onError();
    }

    void getAlbums(LoadAlbumsCallback callback);
    void getAlbum(int value, LoadAlbumCallback callback);

    void saveAlbums(List<Album> albums);
    void saveAlbum(List<AlbumImages> album);
}
