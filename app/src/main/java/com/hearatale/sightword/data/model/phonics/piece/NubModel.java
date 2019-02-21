package com.hearatale.sightword.data.model.phonics.piece;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NubModel implements Parcelable {

    @SerializedName("rightNub")
    @Expose
    private String rightNub;
    @SerializedName("bottomNub")
    @Expose
    private String bottomNub;
    @SerializedName("topNub")
    @Expose
    private String topNub;
    @SerializedName("leftNub")
    @Expose
    private String leftNub;

    boolean isCompleted;

    public String getRightNub() {
        return rightNub;
    }

    public void setRightNub(String rightNub) {
        this.rightNub = rightNub;
    }

    public String getBottomNub() {
        return bottomNub;
    }

    public void setBottomNub(String bottomNub) {
        this.bottomNub = bottomNub;
    }

    public String getTopNub() {
        return topNub;
    }

    public void setTopNub(String topNub) {
        this.topNub = topNub;
    }

    public String getLeftNub() {
        return leftNub;
    }

    public void setLeftNub(String leftNub) {
        this.leftNub = leftNub;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.rightNub);
        dest.writeString(this.bottomNub);
        dest.writeString(this.topNub);
        dest.writeString(this.leftNub);
    }

    public NubModel() {
    }

    protected NubModel(Parcel in) {
        this.rightNub = in.readString();
        this.bottomNub = in.readString();
        this.topNub = in.readString();
        this.leftNub = in.readString();
    }

    public static final Parcelable.Creator<NubModel> CREATOR = new Parcelable.Creator<NubModel>() {
        @Override
        public NubModel createFromParcel(Parcel source) {
            return new NubModel(source);
        }

        @Override
        public NubModel[] newArray(int size) {
            return new NubModel[size];
        }
    };
}