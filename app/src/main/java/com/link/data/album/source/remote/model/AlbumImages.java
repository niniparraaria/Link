package com.link.data.album.source.remote.model;

import android.os.Parcel;
import android.os.Parcelable;

public class AlbumImages implements Parcelable{

    private int id;
    private int albumId;
    private String title;
    private String url;
    private String thumbnailUrl;

    public AlbumImages() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAlbumId() {
        return albumId;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public static Creator<AlbumImages> getCREATOR() {
        return CREATOR;
    }

    public static final Creator<AlbumImages> CREATOR = new Creator<AlbumImages>() {
        @Override
        public AlbumImages createFromParcel(Parcel in) {
            return new AlbumImages(in);
        }

        @Override
        public AlbumImages[] newArray(int size) {
            return new AlbumImages[size];
        }
    };

    protected AlbumImages(Parcel in) {
        url = in.readString();
        id = in.readInt();
        albumId = in.readInt();
        title = in.readString();
        thumbnailUrl = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeInt(id);
        parcel.writeInt(albumId);
        parcel.writeString(url);
        parcel.writeString(thumbnailUrl);
    }

}
