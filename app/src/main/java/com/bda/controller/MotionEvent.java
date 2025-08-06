package com.bda.controller;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseArray;

/* loaded from: classes.dex */
public final class MotionEvent extends BaseEvent implements Parcelable {
    public static final int AXIS_LTRIGGER = 17;
    public static final int AXIS_RTRIGGER = 18;
    public static final int AXIS_RZ = 14;
    public static final int AXIS_X = 0;
    public static final int AXIS_Y = 1;
    public static final int AXIS_Z = 11;
    public static final Parcelable.Creator<MotionEvent> CREATOR = new ParcelableCreator();
    final SparseArray<Float> mAxis;
    final SparseArray<Float> mPrecision;

    public MotionEvent(long eventTime, int deviceId, float x, float y, float z, float rz, float xPrecision, float yPrecision) {
        super(eventTime, deviceId);
        this.mAxis = new SparseArray<>(4);
        this.mAxis.put(0, Float.valueOf(x));
        this.mAxis.put(1, Float.valueOf(y));
        this.mAxis.put(11, Float.valueOf(z));
        this.mAxis.put(14, Float.valueOf(rz));
        this.mPrecision = new SparseArray<>(2);
        this.mPrecision.put(0, Float.valueOf(xPrecision));
        this.mPrecision.put(1, Float.valueOf(yPrecision));
    }

    public MotionEvent(long eventTime, int deviceId, int[] axisKey, float[] axisValue, int[] precisionKey, float[] precisionValue) {
        super(eventTime, deviceId);
        int axis = axisKey.length;
        this.mAxis = new SparseArray<>(axis);
        for (int index = 0; index < axis; index++) {
            this.mAxis.put(axisKey[index], Float.valueOf(axisValue[index]));
        }
        int precision = precisionKey.length;
        this.mPrecision = new SparseArray<>(precision);
        for (int index2 = 0; index2 < precision; index2++) {
            this.mPrecision.put(precisionKey[index2], Float.valueOf(precisionValue[index2]));
        }
    }

    MotionEvent(Parcel parcel) {
        super(parcel);
        int axis = parcel.readInt();
        this.mAxis = new SparseArray<>(axis);
        for (int index = 0; index < axis; index++) {
            int key = parcel.readInt();
            float value = parcel.readFloat();
            this.mAxis.put(key, Float.valueOf(value));
        }
        int precision = parcel.readInt();
        this.mPrecision = new SparseArray<>(precision);
        for (int index2 = 0; index2 < axis; index2++) {
            int key2 = parcel.readInt();
            float value2 = parcel.readFloat();
            this.mPrecision.put(key2, Float.valueOf(value2));
        }
    }

    @Override // com.bda.controller.BaseEvent, android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public final int findPointerIndex(int pointerId) {
        return -1;
    }

    public final float getAxisValue(int axis) {
        return getAxisValue(axis, 0);
    }

    public final float getAxisValue(int axis, int pointerIndex) {
        if (pointerIndex == 0) {
            return this.mAxis.get(axis, Float.valueOf(0.0f)).floatValue();
        }
        return 0.0f;
    }

    public final int getPointerCount() {
        return 1;
    }

    public final int getPointerId(int pointerIndex) {
        return 0;
    }

    public final float getRawX() {
        return getX();
    }

    public final float getRawY() {
        return getY();
    }

    public final float getX() {
        return getAxisValue(0, 0);
    }

    public final float getX(int pointerIndex) {
        return getAxisValue(0, pointerIndex);
    }

    public final float getXPrecision() {
        return this.mPrecision.get(0, Float.valueOf(0.0f)).floatValue();
    }

    public final float getY() {
        return getAxisValue(1, 0);
    }

    public final float getY(int pointerIndex) {
        return getAxisValue(1, pointerIndex);
    }

    public final float getYPrecision() {
        return this.mPrecision.get(1, Float.valueOf(0.0f)).floatValue();
    }

    @Override // com.bda.controller.BaseEvent, android.os.Parcelable
    public void writeToParcel(Parcel parcel, int flags) {
        super.writeToParcel(parcel, flags);
        int axis = this.mAxis.size();
        parcel.writeInt(axis);
        for (int index = 0; index < axis; index++) {
            parcel.writeInt(this.mAxis.keyAt(index));
            parcel.writeFloat(this.mAxis.valueAt(index).floatValue());
        }
        int precision = this.mPrecision.size();
        parcel.writeInt(precision);
        for (int index2 = 0; index2 < precision; index2++) {
            parcel.writeInt(this.mPrecision.keyAt(index2));
            parcel.writeFloat(this.mPrecision.valueAt(index2).floatValue());
        }
    }

    static class ParcelableCreator implements Parcelable.Creator<MotionEvent> {
        ParcelableCreator() {
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public MotionEvent createFromParcel(Parcel source) {
            return new MotionEvent(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public MotionEvent[] newArray(int size) {
            return new MotionEvent[size];
        }
    }
}
