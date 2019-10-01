package com.link.activity.details;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.link.R;
import com.link.data.album.source.remote.model.Album;
import com.link.data.album.source.remote.model.AlbumImages;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AlbumDetailAdapter extends RecyclerView.Adapter<AlbumDetailViewHolder> {
    private List<AlbumImages> mItems;
    Context context;

    public AlbumDetailAdapter(Context context) {
        this.context = context;
        mItems = new ArrayList<>();
    }

    public void setItems(List<AlbumImages> items) {
        mItems = items;
        notifyDataSetChanged();
    }

    @Override
    public AlbumDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_item, parent, false);
        return new AlbumDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AlbumDetailViewHolder holder, int position) {
        AlbumImages album = getItem(position);

        holder.setImage(album.getUrl(),context);
        holder.setTitle(album.getTitle());
        holder.setId(context.getString(R.string.view_id_format,album.getId()));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    private AlbumImages getItem(int position) {
        return mItems.get(position);
    }

}

