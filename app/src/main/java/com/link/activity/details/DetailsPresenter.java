package com.link.activity.details;




import com.link.activity.base.BasePresenter;
import com.link.data.album.source.AlbumsRepository;
import com.link.data.album.source.remote.AlbumRemoteDataSource;
import com.link.data.album.source.remote.model.AlbumImages;

import java.lang.ref.WeakReference;
import java.util.List;

public class DetailsPresenter extends BasePresenter<DetailsView> {

    private final AlbumsRepository albumsRepository;

    public DetailsPresenter(DetailsView view, AlbumsRepository albumsRepository) {
        super(view);
        this.albumsRepository = albumsRepository;
    }

    public void getAlbumDetail(int value) {
        albumsRepository.getAlbum(value,new AlbumDetailCallListener(view));
    }
    private static class AlbumDetailCallListener implements AlbumRemoteDataSource.LoadAlbumCallback {

        private WeakReference<DetailsView> view;

        private AlbumDetailCallListener(DetailsView view) {
            this.view = new WeakReference<>(view);
            if (view != null)
            view.showProgress();
        }

        @Override
        public void onAlbumLoaded(List<AlbumImages> album) {
            if (view.get() == null) return;
            view.get().showAlbumDetails(album);
            view.get().hideProgress();
        }


        @Override
        public void onDataNotAvailable() {
            if (view.get() == null) return;
            view.get().showDataUnavailableMessage();
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
