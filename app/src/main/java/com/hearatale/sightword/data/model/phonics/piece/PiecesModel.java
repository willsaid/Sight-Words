package com.hearatale.sightword.data.model.phonics.piece;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PiecesModel implements Parcelable {

    @SerializedName("row3-col2")
    @Expose
    private NubModel row3Col2;

    @SerializedName("row1-col2")
    @Expose
    private NubModel row1Col2;

    @SerializedName("row1-col1")
    @Expose
    private NubModel row1Col1;

    @SerializedName("row0-col2")
    @Expose
    private NubModel row0Col2;

    @SerializedName("row2-col0")
    @Expose
    private NubModel row2Col0;

    @SerializedName("row0-col0")
    @Expose
    private NubModel row0Col0;

    @SerializedName("row1-col0")
    @Expose
    private NubModel row1Col0;

    @SerializedName("row2-col1")
    @Expose
    private NubModel row2Col1;

    @SerializedName("row3-col1")
    @Expose
    private NubModel row3Col1;

    @SerializedName("row0-col1")
    @Expose
    private NubModel row0Col1;

    @SerializedName("row3-col0")
    @Expose
    private NubModel row3Col0;

    @SerializedName("row2-col2")
    @Expose
    private NubModel row2Col2;

    public NubModel getRow3Col2() {
        return row3Col2;
    }

    public void setRow3Col2(NubModel row3Col2) {
        this.row3Col2 = row3Col2;
    }

    public NubModel getRow1Col2() {
        return row1Col2;
    }

    public void setRow1Col2(NubModel row1Col2) {
        this.row1Col2 = row1Col2;
    }

    public NubModel getRow1Col1() {
        return row1Col1;
    }

    public void setRow1Col1(NubModel row1Col1) {
        this.row1Col1 = row1Col1;
    }

    public NubModel getRow0Col2() {
        return row0Col2;
    }

    public void setRow0Col2(NubModel row0Col2) {
        this.row0Col2 = row0Col2;
    }

    public NubModel getRow2Col0() {
        return row2Col0;
    }

    public void setRow2Col0(NubModel row2Col0) {
        this.row2Col0 = row2Col0;
    }

    public NubModel getRow0Col0() {
        return row0Col0;
    }

    public void setRow0Col0(NubModel row0Col0) {
        this.row0Col0 = row0Col0;
    }

    public NubModel getRow1Col0() {
        return row1Col0;
    }

    public void setRow1Col0(NubModel row1Col0) {
        this.row1Col0 = row1Col0;
    }

    public NubModel getRow2Col1() {
        return row2Col1;
    }

    public void setRow2Col1(NubModel row2Col1) {
        this.row2Col1 = row2Col1;
    }

    public NubModel getRow3Col1() {
        return row3Col1;
    }

    public void setRow3Col1(NubModel row3Col1) {
        this.row3Col1 = row3Col1;
    }

    public NubModel getRow0Col1() {
        return row0Col1;
    }

    public void setRow0Col1(NubModel row0Col1) {
        this.row0Col1 = row0Col1;
    }

    public NubModel getRow3Col0() {
        return row3Col0;
    }

    public void setRow3Col0(NubModel row3Col0) {
        this.row3Col0 = row3Col0;
    }

    public NubModel getRow2Col2() {
        return row2Col2;
    }

    public void setRow2Col2(NubModel row2Col2) {
        this.row2Col2 = row2Col2;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.row3Col2, flags);
        dest.writeParcelable(this.row1Col2, flags);
        dest.writeParcelable(this.row1Col1, flags);
        dest.writeParcelable(this.row0Col2, flags);
        dest.writeParcelable(this.row2Col0, flags);
        dest.writeParcelable(this.row0Col0, flags);
        dest.writeParcelable(this.row1Col0, flags);
        dest.writeParcelable(this.row2Col1, flags);
        dest.writeParcelable(this.row3Col1, flags);
        dest.writeParcelable(this.row0Col1, flags);
        dest.writeParcelable(this.row3Col0, flags);
        dest.writeParcelable(this.row2Col2, flags);
    }

    public PiecesModel() {
    }

    protected PiecesModel(Parcel in) {
        this.row3Col2 = in.readParcelable(NubModel.class.getClassLoader());
        this.row1Col2 = in.readParcelable(NubModel.class.getClassLoader());
        this.row1Col1 = in.readParcelable(NubModel.class.getClassLoader());
        this.row0Col2 = in.readParcelable(NubModel.class.getClassLoader());
        this.row2Col0 = in.readParcelable(NubModel.class.getClassLoader());
        this.row0Col0 = in.readParcelable(NubModel.class.getClassLoader());
        this.row1Col0 = in.readParcelable(NubModel.class.getClassLoader());
        this.row2Col1 = in.readParcelable(NubModel.class.getClassLoader());
        this.row3Col1 = in.readParcelable(NubModel.class.getClassLoader());
        this.row0Col1 = in.readParcelable(NubModel.class.getClassLoader());
        this.row3Col0 = in.readParcelable(NubModel.class.getClassLoader());
        this.row2Col2 = in.readParcelable(NubModel.class.getClassLoader());
    }

    public static final Parcelable.Creator<PiecesModel> CREATOR = new Parcelable.Creator<PiecesModel>() {
        @Override
        public PiecesModel createFromParcel(Parcel source) {
            return new PiecesModel(source);
        }

        @Override
        public PiecesModel[] newArray(int size) {
            return new PiecesModel[size];
        }
    };
}