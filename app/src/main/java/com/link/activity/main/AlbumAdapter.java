package com.link.activity.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.link.R;
import com.link.data.album.source.remote.model.Album;

import java.util.ArrayList;
import java.util.List;


public class AlbumAdapter extends RecyclerView.Adapter<AlbumViewHolder> implements Filterable {

    private List<Album> mItems;
    private List<Album> mItemsFiltered;
    private MainPresenter mPresenter;
    Context context;

    public AlbumAdapter(Context context, MainPresenter presenter) {
        this.context = context;
        mPresenter = presenter;
        mItems = new ArrayList<>();
        mItemsFiltered = new ArrayList<>();
    }

    public void setItems(List<Album> items) {
        mItemsFiltered = items;
        notifyDataSetChanged();
    }
    public void setItemsInitial(List<Album> items) {
        mItems = items;
        mItemsFiltered = items;
        notifyDataSetChanged();
    }
    @Override
    public AlbumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item, parent, false);
        return new AlbumViewHolder(mPresenter,view);
    }

    @Override
    public void onBindViewHolder(AlbumViewHolder holder, int position) {
        Album album = getItem(position);

        holder.setOnClickListener(album);
        holder.setTitle(album.getTitle());
        holder.setId(context.getString(R.string.view_id_format,album.getId()));
    }

    @Override
    public int getItemCount() {
        return mItemsFiltered.size();
    }

    private Album getItem(int position) {
        return mItemsFiltered.get(position);
    }
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    Log.e("e", mItems.size()+"");

                    mItemsFiltered = mItems;
                } else {
                    List<Album> filteredList = new ArrayList<>();
                    for (Album row : mItems) {
                        if (row.getTitle().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    mItemsFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mItemsFiltered;
                return filterResults;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mItemsFiltered = (ArrayList<Album>) filterResults.values;
                Log.e("e", mItemsFiltered.size()+"");
                mPresenter.getAlbumSearch(mItemsFiltered);
            }
        };
    }
}
