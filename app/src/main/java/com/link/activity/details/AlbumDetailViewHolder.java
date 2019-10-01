package com.link.activity.details;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.link.R;
import com.link.data.album.source.remote.model.Album;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AlbumDetailViewHolder extends RecyclerView.ViewHolder{

    @BindView(R.id.detail_item_image)
    ImageView image;

    @BindView(R.id.detail_item_constraint)
    ConstraintLayout constraintLayout;

    ConstraintSet constraintSet;
    RequestOptions requestOptions;

    @BindView(R.id.detail_item_title)
    TextView title;

    @BindView(R.id.detail_item_id)
    TextView id;


    public AlbumDetailViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }

    public void setImage(String imageUrl, Context context) {
        constraintSet = new ConstraintSet();
        requestOptions = new RequestOptions().placeholder(R.drawable.placeholder);
        String posterRatio = String.format("%s:%s", 600, 600);
        Glide.with(context)
                .setDefaultRequestOptions(requestOptions)
                .load(imageUrl)
                .into(image);

        constraintSet.clone(constraintLayout);
        constraintSet.setDimensionRatio(image.getId(), posterRatio);
        constraintSet.applyTo(constraintLayout);
        Glide.with(itemView.getContext()).load(imageUrl).into(image);
    }

    public void setId(String id) {
        this.id.setText(id);
    }


}
