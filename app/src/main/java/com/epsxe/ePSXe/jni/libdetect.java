package com.epsxe.ePSXe.jni;

/* loaded from: classes.dex */
public class libdetect {
    public native int getCpuCount();

    public native int is64bits();

    public native int isNeon();

    public native int isX86();

    static {
        System.loadLibrary("detect");
    }
}
