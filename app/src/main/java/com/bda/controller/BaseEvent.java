package com.bda.controller;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
class BaseEvent implements Parcelable {
    public static final Parcelable.Creator<BaseEvent> CREATOR = new ParcelableCreator();
    final int mControllerId;
    final long mEventTime;

    public BaseEvent(long eventTime, int deviceId) {
        this.mEventTime = eventTime;
        this.mControllerId = deviceId;
    }

    BaseEvent(Parcel parcel) {
        this.mEventTime = parcel.readLong();
        this.mControllerId = parcel.readInt();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public final int getControllerId() {
        return this.mControllerId;
    }

    public final long getEventTime() {
        return this.mEventTime;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeLong(this.mEventTime);
        parcel.writeInt(this.mControllerId);
    }

    static class ParcelableCreator implements Parcelable.Creator<BaseEvent> {
        ParcelableCreator() {
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public BaseEvent createFromParcel(Parcel source) {
            return new BaseEvent(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public BaseEvent[] newArray(int size) {
            return new BaseEvent[size];
        }
    }
}
