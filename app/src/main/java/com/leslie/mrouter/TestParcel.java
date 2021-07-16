package com.leslie.mrouter;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 作者：xjzhao
 * 时间：2021-07-15 23:47
 */
public class TestParcel implements Parcelable {
    private int i;
    private String n;

    public TestParcel(int i, String n) {
        this.i = i;
        this.n = n;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.i);
        dest.writeString(this.n);
    }

    public TestParcel() {
    }

    protected TestParcel(Parcel in) {
        this.i = in.readInt();
        this.n = in.readString();
    }

    public static final Creator<TestParcel> CREATOR = new Creator<TestParcel>() {
        @Override
        public TestParcel createFromParcel(Parcel source) {
            return new TestParcel(source);
        }

        @Override
        public TestParcel[] newArray(int size) {
            return new TestParcel[size];
        }
    };

    @Override
    public String toString() {
        return "TestParcel{" +
                "i=" + i +
                ", n='" + n + '\'' +
                '}';
    }
}
