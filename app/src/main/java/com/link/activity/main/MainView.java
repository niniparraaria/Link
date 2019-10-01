package com.link.activity.main;



import com.link.activity.base.BaseView;
import com.link.data.album.source.remote.model.Album;

import java.util.List;


public interface MainView extends BaseView {
    void showAlbums(List<Album> albums);

    void showAlbumsSearch(List<Album> albums);

    void showErrorMessage();

    void showThereIsNoAlbums();

    void showProgress();

    void hideProgress();

    void onAlbumClicked(Album album);

}
