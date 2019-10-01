package com.link.activity.main;

import com.link.data.album.source.AlbumsDataSource;
import com.link.data.album.source.AlbumsRepository;
import com.link.data.album.source.remote.model.Album;

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

@RunWith(MockitoJUnitRunner.class)
public class MainPresenterTest {
    @Mock
    private MainView view;
    @Mock
    private AlbumsRepository albumsRepository;

    @Captor
    private ArgumentCaptor<AlbumsDataSource.LoadAlbumsCallback> loadAlbumsCallback;

    private MainPresenter presenter;

    @Before
    public void setUp() {
        presenter = new MainPresenter(view, albumsRepository);
    }

    @Test
    public void showAlbums_WhenGetAlbumsCallSuccess() {
        presenter.getAllAlbum();
        List<Album> albums = Arrays.asList(new Album(), new Album());
        Mockito.verify(albumsRepository).getAlbums(loadAlbumsCallback.capture());
        loadAlbumsCallback.getValue().onAlbumsLoaded(albums);
        Mockito.verify(view).showAlbums(ArgumentMatchers.<Album>anyList());
    }

    @Test
    public void showNoAlbums_WhenGetAlbumsCallSuccessAndMovieListIsEmpty() {

        presenter.getAllAlbum();

        Mockito.verify(albumsRepository).getAlbums(loadAlbumsCallback.capture());
        loadAlbumsCallback.getValue().onDataNotAvailable();

        Mockito.verify(view).showThereIsNoAlbums();
    }

    @Test
    public void showErrorMessage_WhenGetAlbumsCallFailed() {
        presenter.getAllAlbum();

        Mockito.verify(albumsRepository).getAlbums(loadAlbumsCallback.capture());
        loadAlbumsCallback.getValue().onError();

        Mockito.verify(view).showErrorMessage();
    }
}