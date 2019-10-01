package com.link.data;


import com.link.data.album.source.AlbumsDataSource;
import com.link.data.album.source.AlbumsRepository;
import com.link.data.album.source.cache.AlbumsCacheDataSource;
import com.link.data.album.source.remote.services.AlbumsApi;
import com.link.data.album.source.remote.services.AlbumsService;

public class DataManager {

    private static DataManager sInstance;

    private DataManager() {
    }

    public static synchronized DataManager getInstance() {
        if (sInstance == null) {
            sInstance = new DataManager();
        }
        return sInstance;
    }

    public AlbumsApi getAlbumsApi() {
        return AlbumsService.getInstance().getAlbumsApi();
    }

    public AlbumsRepository getAlbumRepository(AlbumsDataSource albumRemote,
                                               AlbumsCacheDataSource albumCache) {
        return AlbumsRepository.getInstance(albumRemote,albumCache);
    }

}
