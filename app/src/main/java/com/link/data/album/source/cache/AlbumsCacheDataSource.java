package com.link.data.album.source.cache;

import android.util.ArrayMap;

import com.link.data.album.source.AlbumsDataSource;
import com.link.data.album.source.remote.model.Album;
import com.link.data.album.source.remote.model.AlbumImages;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class AlbumsCacheDataSource implements AlbumsDataSource {

    private static AlbumsCacheDataSource sInstance;

    private final ArrayMap<String, Album> cachedAlbums = new ArrayMap<>();
    private ArrayMap<String, AlbumImages> cachedAlbum = new ArrayMap<>();

    public static AlbumsCacheDataSource getsInstance() {
        if (sInstance == null) {
            sInstance = new AlbumsCacheDataSource();
        }
        return sInstance;
    }

    @Override
    public void getAlbums(LoadAlbumsCallback callback) {

        if (cachedAlbums.size() > 0) {
            List<Album> albumList = new ArrayList<>();
            for (int i = 0; i < cachedAlbums.size(); i++) {
                String key = cachedAlbums.keyAt(i);
                albumList.add(cachedAlbums.get(key));
            }
            if (albumList.size() > 0) {
                callback.onAlbumsLoaded(albumList);
            }else{
                callback.onDataNotAvailable();
            }

        } else {
            callback.onDataNotAvailable();
        }

    }



    @Override
    public void getAlbum(int value, LoadAlbumCallback callback) {
        if (cachedAlbum.size() > 0) {
            List<AlbumImages> albumList = new ArrayList<>();
            for (int i = 0; i < cachedAlbum.size(); i++) {
                String key = cachedAlbum.keyAt(i);
                if (Objects.requireNonNull(cachedAlbum.get(key)).getId() == value) {
                    albumList.add(cachedAlbum.get(key));
                }
            }
            if (albumList.size() > 0) {
                callback.onAlbumLoaded(albumList);
            }else{
                callback.onDataNotAvailable();
            }

        } else {
            callback.onDataNotAvailable();
        }
    }

    @Override
    public void saveAlbums(List<Album> albumsList) {
        cachedAlbums.clear();
        for (int i=0; i<albumsList.size(); i++) {
            cachedAlbums.put(String.valueOf(albumsList.get(i).getId()), albumsList.get(i));
        }
    }
    @Override
    public void saveAlbum(List<AlbumImages> albumsList) {
        for (int i=0; i<albumsList.size(); i++) {
            cachedAlbum.put(String.valueOf(albumsList.get(i).getId()), albumsList.get(i));
        }
    }
}
