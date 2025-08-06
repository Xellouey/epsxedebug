package com.epsxe.ePSXe.jni;

/* loaded from: classes.dex */
public class libgamedetect {
    public native String getCode(String str, int i);

    public native String getCodeSlot(String str, int i, int i2);

    public native String getECMToIndex(String str);

    public native void makeIndexECM(String str);

    public native int setSdCardPath(String str);

    static {
        System.loadLibrary("gamedetect");
    }
}
