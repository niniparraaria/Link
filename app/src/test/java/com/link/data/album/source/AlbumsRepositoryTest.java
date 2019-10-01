package com.link.data.album.source;

import com.link.data.album.source.cache.AlbumsCacheDataSource;
import com.link.data.album.source.remote.AlbumRemoteDataSource;
import com.link.data.album.source.remote.model.Album;

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

import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
public class AlbumsRepositoryTest {
    @Mock
    AlbumsCacheDataSource albumsCache;

    @Mock
    AlbumRemoteDataSource albumRemote;


    @Captor
    ArgumentCaptor<AlbumsDataSource.LoadAlbumsCallback> loadAlbumsCallbackCapture;

    @Mock
    AlbumsDataSource.LoadAlbumsCallback loadAlbumsCallback;



    private static List<Album> AlbumList = Arrays.asList(new Album(), new Album());

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
        AlbumsRepository.getAlbums(loadAlbumsCallback);
        Mockito.verify(albumsCache).getAlbums((AlbumsDataSource.LoadAlbumsCallback) Mockito.any());
    }

    @Test
    public void getAlbums_requestDataFromCacheDataSource() {
        AlbumsRepository.getAlbums(loadAlbumsCallback);

        setAlbumsAvailable(albumsCache);

        Mockito.verify(loadAlbumsCallback).onAlbumsLoaded(AlbumList);
    }

    @Test
    public void getAlbums_requestDataFromCacheAfterFirstApiCall() {

        AlbumsRepository.getAlbums(loadAlbumsCallback);

        setAlbumsNotAvailable(albumsCache);
        setAlbumsAvailable(albumRemote);

        Mockito.verify(albumsCache).saveAlbums(Mockito.<Album>anyList());

        AlbumsRepository.getAlbums(loadAlbumsCallback);

        Mockito.verify(albumsCache, times(2)).getAlbums((AlbumsDataSource.LoadAlbumsCallback) Mockito.any());
        Mockito.verify(albumRemote, times(1)).getAlbums((AlbumsDataSource.LoadAlbumsCallback) Mockito.any());
    }

    @Test
    public void getAlbums_verifyThatDataSavedInCacheAfterApiCall() {
        AlbumsRepository.getAlbums(loadAlbumsCallback);

        setAlbumsNotAvailable(albumsCache);
        setAlbumsAvailable(albumRemote);
        Mockito.verify(albumsCache).saveAlbums(ArgumentMatchers.<Album>anyList());
    }


    @Test
    public void getAlbums_requestDataFromRemoteDataSource() {
        AlbumsRepository.getAlbums(loadAlbumsCallback);

        setAlbumsNotAvailable(albumsCache);
        setAlbumsAvailable(albumRemote);

        Mockito.verify(loadAlbumsCallback).onAlbumsLoaded(AlbumList);
    }

    @Test
    public void getAlbums_requestDataFromRemoteDataSourceNotAvailable() {
        AlbumsRepository.getAlbums(loadAlbumsCallback);

        setAlbumsNotAvailable(albumsCache);
        setAlbumsNotAvailable(albumRemote);

        Mockito.verify(loadAlbumsCallback).onDataNotAvailable();
    }

    @Test
    public void getAlbums_requestDataFromRemoteDataSourceReturnError() {
        AlbumsRepository.getAlbums(loadAlbumsCallback);

        setAlbumsNotAvailable(albumsCache);
        setAlbumsErrorResponse(albumRemote);

        Mockito.verify(loadAlbumsCallback).onError();
    }

    private void setAlbumsErrorResponse(AlbumsDataSource dataSource) {
        Mockito.verify(dataSource).getAlbums(loadAlbumsCallbackCapture.capture());
        loadAlbumsCallbackCapture.getValue().onError();
    }

    private void setAlbumsNotAvailable(AlbumsDataSource dataSource) {
        Mockito.verify(dataSource).getAlbums(loadAlbumsCallbackCapture.capture());
        loadAlbumsCallbackCapture.getValue().onDataNotAvailable();
    }

    private void setAlbumsAvailable(AlbumsDataSource dataSource) {
        Mockito.verify(dataSource).getAlbums(loadAlbumsCallbackCapture.capture());
        loadAlbumsCallbackCapture.getValue().onAlbumsLoaded(AlbumList);
    }
}