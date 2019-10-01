package com.link.data.album.source;


import com.link.data.album.source.cache.AlbumsCacheDataSource;
import com.link.data.album.source.remote.model.Album;
import com.link.data.album.source.remote.model.AlbumImages;

import java.util.List;

public class AlbumsRepository implements AlbumsDataSource {

    private final AlbumsDataSource albumRemote;
    private final AlbumsCacheDataSource albumCache;

    private static AlbumsRepository instance;

    private AlbumsRepository(AlbumsDataSource albumRemote, AlbumsCacheDataSource albumCache) {

        this.albumRemote = albumRemote;
        this.albumCache = albumCache;
    }

    public static AlbumsRepository getInstance(AlbumsDataSource albumRemote,
                                               AlbumsCacheDataSource albumCache) {
        if (instance == null) {
            instance = new AlbumsRepository(albumRemote, albumCache);
        }
        return instance;
    }

    @Override
    public void getAlbums(final LoadAlbumsCallback callback) {
        if (callback == null) return;
        albumCache.getAlbums(new LoadAlbumsCallback() {
            @Override
            public void onAlbumsLoaded(List<Album> albums) {
                callback.onAlbumsLoaded(albums);
            }

            @Override
            public void onDataNotAvailable() {
                getAlbumsFromRemoteDataSource(callback);
            }

            @Override
            public void onError() {
            }
        });

    }

    @Override
    public void getAlbum(final int value, final LoadAlbumCallback callback) {
        if (callback == null) return;

        albumCache.getAlbum(value, new LoadAlbumCallback() {
            @Override
            public void onAlbumLoaded(List<AlbumImages> album) {
                callback.onAlbumLoaded(album);
            }

            @Override
            public void onDataNotAvailable() {
                getAlbumFromRemoteDataSource(value,callback);
            }

            @Override
            public void onError() {
            }
        });

    }

    @Override
    public void saveAlbums(List<Album> albums) {
        albumCache.saveAlbums(albums);
    }

    @Override
    public void saveAlbum(List<AlbumImages> album) {
        albumCache.saveAlbum(album);

    }

    private void getAlbumsFromRemoteDataSource(final LoadAlbumsCallback callback) {
        albumRemote.getAlbums(new LoadAlbumsCallback() {
            @Override
            public void onAlbumsLoaded(List<Album> albums) {
                callback.onAlbumsLoaded(albums);
                refreshCache(albums);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }

            @Override
            public void onError() {
                callback.onError();
            }
        });
    }


    private void getAlbumFromRemoteDataSource(int value,final LoadAlbumCallback callback) {
        albumRemote.getAlbum(value, new LoadAlbumCallback() {
            @Override
            public void onAlbumLoaded(List<AlbumImages> album) {
                callback.onAlbumLoaded(album);
                refreshDetailCache(album);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }

            @Override
            public void onError() {
                callback.onError();
            }
        });
    }

    private void refreshCache(List<Album> albums) {
        albumCache.saveAlbums(albums);
    }
    private void refreshDetailCache(List<AlbumImages> album) {
        albumCache.saveAlbum(album);
    }

    public void destroyInstance() {
        instance = null;
    }
}
