package com.link.activity.details;


import com.link.activity.base.BaseView;
import com.link.data.album.source.remote.model.Album;
import com.link.data.album.source.remote.model.AlbumImages;

import java.util.List;

public interface DetailsView extends BaseView {

    void showAlbumDetails(List<AlbumImages> album);

    void showDataUnavailableMessage();

    void showErrorMessage();

    void showProgress();

    void hideProgress();



}
