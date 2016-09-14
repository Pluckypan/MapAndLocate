package com.fanglin.fenhong.mapandlocate.baiduloc;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Plucky on 2015/8/14.
 * Desc:
 */
public class LocMsg implements Parcelable {
    public Double mLat;
    public Double mLng;
    public String mPoi;
    public Uri mImgUri;

    public LocMsg() {

    }

    public static final Creator<LocMsg> CREATOR = new Creator<LocMsg>() {
        @Override
        public LocMsg[] newArray(int size) {
            return new LocMsg[size];
        }

        @Override
        public LocMsg createFromParcel(Parcel in) {
            return new LocMsg(in);
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.mLat);
        dest.writeDouble(this.mLng);
        dest.writeString(this.mPoi);
        writeToParcel(dest, this.mImgUri);
    }

    public LocMsg(Parcel in) {
        this.mLat = in.readDouble();
        this.mLng = in.readDouble();
        this.mPoi = in.readString();
        this.mImgUri = (Uri) readFromParcel(in, Uri.class);
    }

    public static Parcelable readFromParcel(Parcel in, Class<?> cls) {
        int flag = in.readInt();
        return flag == 1 ? in.readParcelable(cls.getClassLoader()) : null;
    }

    public static void writeToParcel(Parcel out, Parcelable model) {
        if (model != null) {
            out.writeInt(1);
            out.writeParcelable(model, 0);
        } else {
            out.writeInt(0);
        }

    }
}
