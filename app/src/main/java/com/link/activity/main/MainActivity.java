package com.link.activity.main;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.link.R;
import com.link.Utils.KeyboardUtils;
import com.link.activity.base.BaseActivity;
import com.link.activity.details.DetailsActivity;
import com.link.data.DataManager;
import com.link.data.album.source.AlbumsRepository;
import com.link.data.album.source.cache.AlbumsCacheDataSource;
import com.link.data.album.source.remote.AlbumRemoteDataSource;
import com.link.data.album.source.remote.model.Album;
import com.link.data.album.source.remote.services.AlbumsApi;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends BaseActivity<MainPresenter> implements MainView {

    AlbumAdapter albumAdapter;

    @BindView(R.id.main_list)
    RecyclerView recyclerView;

    @BindView(R.id.main_search)
    TextInputEditText search;

    @BindView(R.id.empty_layout)
    LinearLayout emptyLayout;

    @BindView(R.id.error_layout)
    LinearLayout errorLayout;

    @BindView(R.id.error_message)
    TextView errorMessage;

    @BindView(R.id.loading)
    LinearLayout loading;

    LinearLayoutManager manager;
    private final String SEARCH_TAG = "SEARCH_KEY";
    private final String STATE_TAG = "STATE_KEY";
    private Parcelable savedRecyclerLayoutState;
    private String saveSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        manager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false);
        albumAdapter = new AlbumAdapter(MainActivity.this,presenter);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(albumAdapter);
        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    albumAdapter.getFilter().filter(search.getText().toString());
                    KeyboardUtils.hideSoftKeyboard(MainActivity.this);
                }
                return false;
            }
        });
        if (savedInstanceState != null) {
            savedRecyclerLayoutState = savedInstanceState.getParcelable(STATE_TAG);
            saveSearch = savedInstanceState.getString(SEARCH_TAG);
            if (savedRecyclerLayoutState != null){
                if (saveSearch != null && !saveSearch.equals("")) {
                    recyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
                    albumAdapter.getFilter().filter(search.getText().toString());
                    KeyboardUtils.hideSoftKeyboard(MainActivity.this);
                }else {
                    recyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
                    presenter.getAllAlbum();
                    KeyboardUtils.hideSoftKeyboard(MainActivity.this);
                }
            }
        }
        presenter.getAllAlbum();

    }

    @NonNull
    @Override
    protected MainPresenter createPresenter() {

        AlbumsApi albumApi = DataManager.getInstance().getAlbumsApi();
        AlbumRemoteDataSource remoteDataSource = AlbumRemoteDataSource.getInstance(albumApi);

        AlbumsCacheDataSource cacheDataSource = AlbumsCacheDataSource.getsInstance();

        AlbumsRepository albumsRepository = DataManager.getInstance().getAlbumRepository(remoteDataSource, cacheDataSource);

        return new MainPresenter(this, albumsRepository);
    }


    @Override
    public void showAlbums(List<Album> albums) {
        albumAdapter.setItemsInitial(albums);
        recyclerView.setVisibility(View.VISIBLE);
        emptyLayout.setVisibility(View.GONE);
        errorLayout.setVisibility(View.GONE);

    }
    @Override
    public void showAlbumsSearch(List<Album> albums) {
        albumAdapter.setItems(albums);
        recyclerView.setVisibility(View.VISIBLE);
        emptyLayout.setVisibility(View.GONE);
        errorLayout.setVisibility(View.GONE);

    }

    @Override
    public void showErrorMessage() {
        recyclerView.setVisibility(View.GONE);
        emptyLayout.setVisibility(View.GONE);
        errorLayout.setVisibility(View.VISIBLE);
        errorMessage.setText(getString(R.string.server_error_lbl));
    }

    @Override
    public void showThereIsNoAlbums() {
        recyclerView.setVisibility(View.GONE);
        emptyLayout.setVisibility(View.GONE);
        errorLayout.setVisibility(View.VISIBLE);
        errorMessage.setText(getString(R.string.search_error_lbl));
    }

    @Override
    public void showProgress() {
        hideProgress();
        loading.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        emptyLayout.setVisibility(View.GONE);
        errorLayout.setVisibility(View.GONE);
    }

    @Override
    public void hideProgress() {
        loading.setVisibility(View.GONE);
        KeyboardUtils.hideSoftKeyboard(this);
    }

    @Override
    public void onAlbumClicked(Album album) {
        startActivity(DetailsActivity.newIntent(this, album.getId()));

    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (!search.getText().toString().equals("")) {
            outState.putString(SEARCH_TAG, search.getText().toString());
        }
        outState.putParcelable(STATE_TAG, manager.onSaveInstanceState());
        super.onSaveInstanceState(outState);
    }
}
