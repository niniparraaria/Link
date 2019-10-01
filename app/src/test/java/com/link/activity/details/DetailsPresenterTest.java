package com.link.activity.details;

import android.util.Log;

import com.link.data.album.source.AlbumsDataSource;
import com.link.data.album.source.AlbumsRepository;
import com.link.data.album.source.remote.model.AlbumImages;

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

@RunWith(MockitoJUnitRunner.class)
public class DetailsPresenterTest {

    @Mock
    private DetailsView view;

    @Mock
    private AlbumsRepository albumsRepository;

    @Captor
    private ArgumentCaptor<AlbumsDataSource.LoadAlbumCallback> loadAlbumCallback;

    private DetailsPresenter presenter;

    @Before
    public void setUp() {
        presenter = new DetailsPresenter(view,albumsRepository);
    }

    @Test
    public void showDataUnavailableMessage_WhenAlbumDataNotExist() {
        presenter.getAlbumDetail(1);
        Mockito.verify(albumsRepository).getAlbum(anyInt(), loadAlbumCallback.capture());
        loadAlbumCallback.getValue().onDataNotAvailable();
        Mockito.verify(view).showDataUnavailableMessage();
    }

    @Test
    public void showAlbumDetails_WhenGetMoveDataSuccess() {
        presenter.getAlbumDetail(1);
        List<AlbumImages> albums = Arrays.asList(new AlbumImages(), new AlbumImages());
        Mockito.verify(albumsRepository).getAlbum(anyInt(), loadAlbumCallback.capture());
        loadAlbumCallback.getValue().onAlbumLoaded(albums);
        Mockito.verify(view).showAlbumDetails(ArgumentMatchers.<AlbumImages>anyList());
    }

    @Test
    public void showErrorMessage_WhenGetAlbumCallFailed() {
        presenter.getAlbumDetail(1);

        Mockito.verify(albumsRepository).getAlbum(anyInt(),loadAlbumCallback.capture());
        loadAlbumCallback.getValue().onError();

        Mockito.verify(view).showErrorMessage();
    }
}