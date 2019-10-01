package com.link.activity.main;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.link.R;
import com.link.data.album.source.remote.model.Album;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AlbumViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    @BindView(R.id.main_item_title)
    TextView title;

    @BindView(R.id.main_item_id)
    TextView id;

    private MainPresenter presenter;


    public AlbumViewHolder(MainPresenter presenter, View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.presenter = presenter;
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }

    public void setId(String id) {
        this.id.setText(id);
    }

    public void setOnClickListener(Album album) {
        itemView.setTag(album);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        presenter.onAlbumClicked((Album) view.getTag());
    }
}
