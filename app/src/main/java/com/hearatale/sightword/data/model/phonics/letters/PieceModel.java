package com.hearatale.sightword.data.model.phonics.letters;

import android.os.Parcel;
import android.os.Parcelable;

public class PieceModel implements Parcelable{
    private int row;
    private int column;

    public PieceModel() {
    }

    public PieceModel(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.row);
        dest.writeInt(this.column);
    }

    protected PieceModel(Parcel in) {
        this.row = in.readInt();
        this.column = in.readInt();
    }

    public static final Creator<PieceModel> CREATOR = new Creator<PieceModel>() {
        @Override
        public PieceModel createFromParcel(Parcel source) {
            return new PieceModel(source);
        }

        @Override
        public PieceModel[] newArray(int size) {
            return new PieceModel[size];
        }
    };
}
