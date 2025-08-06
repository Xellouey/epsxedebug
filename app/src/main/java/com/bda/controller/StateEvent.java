package com.bda.controller;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class StateEvent extends BaseEvent implements Parcelable {
    public static final int ACTION_CONNECTED = 1;
    public static final int ACTION_CONNECTING = 2;
    public static final int ACTION_DISCONNECTED = 0;
    public static final int ACTION_FALSE = 0;
    public static final int ACTION_TRUE = 1;
    public static final int ACTION_VERSION_MOGA = 0;
    public static final int ACTION_VERSION_MOGAPRO = 1;
    public static final Parcelable.Creator<StateEvent> CREATOR = new ParcelableCreator();
    public static final int STATE_CONNECTION = 1;
    public static final int STATE_CURRENT_PRODUCT_VERSION = 4;
    public static final int STATE_POWER_LOW = 2;

    @Deprecated
    public static final int STATE_SELECTED_VERSION = 4;
    public static final int STATE_SUPPORTED_PRODUCT_VERSION = 3;

    @Deprecated
    public static final int STATE_SUPPORTED_VERSION = 3;
    public static final int STATE_UNKNOWN = 0;
    final int mAction;
    final int mState;

    public StateEvent(long eventTime, int deviceId, int state, int action) {
        super(eventTime, deviceId);
        this.mState = state;
        this.mAction = action;
    }

    StateEvent(Parcel parcel) {
        super(parcel);
        this.mState = parcel.readInt();
        this.mAction = parcel.readInt();
    }

    @Override // com.bda.controller.BaseEvent, android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public final int getAction() {
        return this.mAction;
    }

    public final int getState() {
        return this.mState;
    }

    @Override // com.bda.controller.BaseEvent, android.os.Parcelable
    public void writeToParcel(Parcel parcel, int flags) {
        super.writeToParcel(parcel, flags);
        parcel.writeInt(this.mState);
        parcel.writeInt(this.mAction);
    }

    static class ParcelableCreator implements Parcelable.Creator<StateEvent> {
        ParcelableCreator() {
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public StateEvent createFromParcel(Parcel source) {
            return new StateEvent(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public StateEvent[] newArray(int size) {
            return new StateEvent[size];
        }
    }
}
