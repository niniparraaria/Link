package com.link.activity.main;



import com.link.activity.base.BasePresenter;
import com.link.data.album.source.AlbumsDataSource;
import com.link.data.album.source.AlbumsRepository;
import com.link.data.album.source.remote.model.Album;

import java.lang.ref.WeakReference;
import java.util.List;


public class MainPresenter extends BasePresenter<MainView> {

    private final AlbumsRepository albumsRepository;

    public MainPresenter(MainView view, AlbumsRepository albumsRepository) {
        super(view);
        this.albumsRepository = albumsRepository;
    }

    public void getAllAlbum() {
        albumsRepository.getAlbums(new AlbumCallListener(view));
    }

    public void onAlbumClicked(Album album) {
        view.onAlbumClicked(album);
    }

    public void getAlbumSearch(List<Album> albums) {
        if (albums.size() > 0) {
            view.showAlbumsSearch(albums);
        }else{
            view.showThereIsNoAlbums();
        }
    }

    private static class AlbumCallListener implements AlbumsDataSource.LoadAlbumsCallback {

        private WeakReference<MainView> view;

        private AlbumCallListener(MainView view) {
            this.view = new WeakReference<>(view);
            if (view != null)
                view.showProgress();

        }

        @Override
        public void onAlbumsLoaded(List<Album> albums) {
            if (view.get() == null) return;
            view.get().showAlbums(albums);
            view.get().hideProgress();
        }

        @Override
        public void onDataNotAvailable() {
            if (view.get() == null) return;
            view.get().showThereIsNoAlbums();
            view.get().hideProgress();

        }

        @Override
        public void onError() {
            if (view.get() == null) return;
            view.get().showErrorMessage();
            view.get().hideProgress();

        }
    }
}
