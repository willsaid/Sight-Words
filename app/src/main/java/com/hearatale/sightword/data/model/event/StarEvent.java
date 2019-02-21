package com.hearatale.sightword.data.model.event;

import android.os.Parcel;

import com.hearatale.sightword.data.model.base.BaseModel;

public class StarEvent extends BaseModel {
    private String text;
    private int count;

    public StarEvent() {
    }

    public StarEvent(String text, int count) {
        this.text = text;
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.text);
        dest.writeInt(this.count);
    }

    protected StarEvent(Parcel in) {
        this.text = in.readString();
        this.count = in.readInt();
    }

    public static final Creator<StarEvent> CREATOR = new Creator<StarEvent>() {
        @Override
        public StarEvent createFromParcel(Parcel source) {
            return new StarEvent(source);
        }

        @Override
        public StarEvent[] newArray(int size) {
            return new StarEvent[size];
        }
    };
}
