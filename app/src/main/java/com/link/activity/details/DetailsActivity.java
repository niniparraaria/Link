package com.link.activity.details;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.link.R;
import com.link.Utils.KeyboardUtils;
import com.link.activity.base.BaseActivity;
import com.link.data.DataManager;
import com.link.data.album.source.AlbumsRepository;
import com.link.data.album.source.cache.AlbumsCacheDataSource;
import com.link.data.album.source.remote.AlbumRemoteDataSource;
import com.link.data.album.source.remote.model.AlbumImages;
import com.link.data.album.source.remote.services.AlbumsApi;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DetailsActivity extends BaseActivity<DetailsPresenter> implements DetailsView {

    private static final String KEY_PRODUCT = "albumId";

    @BindView(R.id.detail_images)
    RecyclerView images;

    @BindView(R.id.error_layout)
    LinearLayout errorLayout;

    @BindView(R.id.error_message)
    TextView errorMessage;

    @BindView(R.id.loading)
    LinearLayout loading;

    AlbumDetailAdapter detailAdapter;
    LinearLayoutManager manager;
    private final String STATE_TAG = "STATE_KEY";
    private final String VALUE_TAG = "VALUE_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        manager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false);
        detailAdapter = new AlbumDetailAdapter(DetailsActivity.this);
        images.setLayoutManager(manager);
        images.setAdapter(detailAdapter);
        presenter.getAlbumDetail(getAlbumFromBundle());
        if (savedInstanceState != null) {
            Parcelable savedRecyclerLayoutState = savedInstanceState.getParcelable(STATE_TAG);
            int saveValue = savedInstanceState.getInt(VALUE_TAG);
            if (savedRecyclerLayoutState != null && saveValue != 0) {
                images.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
                presenter.getAlbumDetail(saveValue);
                KeyboardUtils.hideSoftKeyboard(DetailsActivity.this);

            }
        }
    }

    public static Intent newIntent(Activity activity, int albumId) {
        Intent intent = new Intent(activity, DetailsActivity.class);
        intent.putExtra(KEY_PRODUCT, albumId);
        return intent;
    }

    private int getAlbumFromBundle() {
        return getIntent().getIntExtra(KEY_PRODUCT,0);
    }

    @NonNull
    @Override
    protected DetailsPresenter createPresenter() {
        AlbumsApi albumApi = DataManager.getInstance().getAlbumsApi();
        AlbumRemoteDataSource remoteDataSource = AlbumRemoteDataSource.getInstance(albumApi);

        AlbumsCacheDataSource cacheDataSource = AlbumsCacheDataSource.getsInstance();

        AlbumsRepository albumsRepository = DataManager.getInstance().getAlbumRepository(remoteDataSource, cacheDataSource);

        return new DetailsPresenter(this, albumsRepository);
    }

    @Override
    public void showAlbumDetails(List<AlbumImages> albums) {

        detailAdapter.setItems(albums);
        images.setVisibility(View.VISIBLE);
        errorLayout.setVisibility(View.GONE);
    }

    @Override
    public void showDataUnavailableMessage() {
        errorLayout.setVisibility(View.VISIBLE);
        errorMessage.setText(getString(R.string.server_error_lbl));
    }

    @Override
    public void showErrorMessage() {
        errorLayout.setVisibility(View.VISIBLE);
        errorMessage.setText(getString(R.string.server_error_lbl));
    }
    @Override
    public void showProgress() {
        hideProgress();
        loading.setVisibility(View.VISIBLE);
        errorLayout.setVisibility(View.GONE);
    }

    @Override
    public void hideProgress() {
        loading.setVisibility(View.GONE);
        KeyboardUtils.hideSoftKeyboard(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
         outState.putInt(VALUE_TAG, getAlbumFromBundle());
        outState.putParcelable(STATE_TAG, manager.onSaveInstanceState());
        super.onSaveInstanceState(outState);
    }

}
