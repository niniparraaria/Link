package com.link.data.album.source;

import com.link.data.album.source.cache.AlbumsCacheDataSource;
import com.link.data.album.source.remote.AlbumRemoteDataSource;
import com.link.data.album.source.remote.model.Album;
import com.link.data.album.source.remote.model.AlbumImages;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
public class AlbumDetailRepositoryTest {
    @Mock
    AlbumsCacheDataSource albumsCache;

    @Mock
    AlbumRemoteDataSource albumRemote;


    @Captor
    ArgumentCaptor<AlbumsDataSource.LoadAlbumCallback> loadAlbumCallbackCapture;

    @Mock
    AlbumsDataSource.LoadAlbumCallback loadAlbumCallback;



    private static List<AlbumImages> AlbumList = Arrays.asList(new AlbumImages(), new AlbumImages());

    private AlbumsRepository AlbumsRepository;

    @Before
    public void setUp() {
        AlbumsRepository = AlbumsRepository.getInstance(albumRemote, albumsCache);
    }

    @After
    public void destroy() {
        AlbumsRepository.destroyInstance();
    }

    @Test
    public void getAlbums_checkCacheDataSourceEveryRequest() {
        AlbumsRepository.getAlbum(1,loadAlbumCallback);
        Mockito.verify(albumsCache).getAlbum(anyInt(), (AlbumsDataSource.LoadAlbumCallback) Mockito.any());
    }

    @Test
    public void getAlbum_requestDataFromCacheDataSource() {
        AlbumsRepository.getAlbum(1,loadAlbumCallback);

        setAlbumAvailable(albumsCache);

        Mockito.verify(loadAlbumCallback).onAlbumLoaded(AlbumList);
    }

    @Test
    public void getAlbums_requestDataFromCacheAfterFirstApiCall() {

        AlbumsRepository.getAlbum(1, loadAlbumCallback);

        setAlbumNotAvailable(albumsCache);
        setAlbumAvailable(albumRemote);

        Mockito.verify(albumsCache).saveAlbum(Mockito.<AlbumImages>anyList());

        AlbumsRepository.getAlbum(1,loadAlbumCallback);

        Mockito.verify(albumsCache, times(2)).getAlbum(anyInt(),
                (AlbumsDataSource.LoadAlbumCallback) Mockito.any());
        Mockito.verify(albumRemote, times(1)).getAlbum(anyInt(),
                (AlbumsDataSource.LoadAlbumCallback) Mockito.any());
    }

    @Test
    public void getAlbums_verifyThatDataSavedInCacheAfterApiCall() {
        AlbumsRepository.getAlbum(1,loadAlbumCallback);
        setAlbumNotAvailable(albumsCache);
        setAlbumAvailable(albumRemote);
        Mockito.verify(albumsCache).saveAlbum(ArgumentMatchers.<AlbumImages>anyList());
    }


    @Test
    public void getAlbums_requestDataFromRemoteDataSource() {
        AlbumsRepository.getAlbum(1,loadAlbumCallback);

        setAlbumNotAvailable(albumsCache);
        setAlbumAvailable(albumRemote);

        Mockito.verify(loadAlbumCallback).onAlbumLoaded(AlbumList);
    }

    @Test
    public void getAlbums_requestDataFromRemoteDataSourceNotAvailable() {
        AlbumsRepository.getAlbum(1,loadAlbumCallback);

        setAlbumNotAvailable(albumsCache);
        setAlbumNotAvailable(albumRemote);

        Mockito.verify(loadAlbumCallback).onDataNotAvailable();
    }

    @Test
    public void getAlbums_requestDataFromRemoteDataSourceReturnError() {
        AlbumsRepository.getAlbum(1,loadAlbumCallback);

        setAlbumNotAvailable(albumsCache);
        setAlbumErrorResponse(albumRemote);

        Mockito.verify(loadAlbumCallback).onError();
    }

    private void setAlbumErrorResponse(AlbumsDataSource dataSource) {
        Mockito.verify(dataSource).getAlbum(anyInt(),loadAlbumCallbackCapture.capture());
        loadAlbumCallbackCapture.getValue().onError();
    }

    private void setAlbumNotAvailable(AlbumsDataSource dataSource) {
        Mockito.verify(dataSource).getAlbum(anyInt(),loadAlbumCallbackCapture.capture());
        loadAlbumCallbackCapture.getValue().onDataNotAvailable();
    }

    private void setAlbumAvailable(AlbumsDataSource dataSource) {
        Mockito.verify(dataSource).getAlbum(anyInt(),loadAlbumCallbackCapture.capture());
        loadAlbumCallbackCapture.getValue().onAlbumLoaded(AlbumList);
    }
}