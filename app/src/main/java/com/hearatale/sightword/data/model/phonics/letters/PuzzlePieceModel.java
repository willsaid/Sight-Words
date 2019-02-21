package com.hearatale.sightword.data.model.phonics.letters;

import android.os.Parcel;
import android.os.Parcelable;

public class PuzzlePieceModel implements Parcelable {

    private String id = "";

    private int xCoord = 0;

    private int yCoord = 0;

    private int width = 0;

    private int height = 0;

    private String urlArtwork = "";


    public PuzzlePieceModel(int xCoord, int yCoord, String urlArtwork) {
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.urlArtwork = urlArtwork;
    }

    public PuzzlePieceModel(String id, int xCoord, int yCoord, int width, int height, String urlArtwork) {
        this.id = id;
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.width = width;
        this.height = height;
        this.urlArtwork = urlArtwork;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getXCoord() {
        return xCoord;
    }

    public void setXCoord(int xCoord) {
        this.xCoord = xCoord;
    }

    public int getYCoord() {
        return yCoord;
    }

    public void setYCoord(int yCoord) {
        this.yCoord = yCoord;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getUrlArtwork() {
        return urlArtwork;
    }

    public void setUrlArtwork(String urlArtwork) {
        this.urlArtwork = urlArtwork;
    }

    public PuzzlePieceModel() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeInt(this.xCoord);
        dest.writeInt(this.yCoord);
        dest.writeInt(this.width);
        dest.writeInt(this.height);
        dest.writeString(this.urlArtwork);
    }

    protected PuzzlePieceModel(Parcel in) {
        this.id = in.readString();
        this.xCoord = in.readInt();
        this.yCoord = in.readInt();
        this.width = in.readInt();
        this.height = in.readInt();
        this.urlArtwork = in.readString();
    }

    public static final Creator<PuzzlePieceModel> CREATOR = new Creator<PuzzlePieceModel>() {
        @Override
        public PuzzlePieceModel createFromParcel(Parcel source) {
            return new PuzzlePieceModel(source);
        }

        @Override
        public PuzzlePieceModel[] newArray(int size) {
            return new PuzzlePieceModel[size];
        }
    };
}
