package com.epsxe.ePSXe;

/* loaded from: classes.dex */
public class libsevenzip {
    public native int extractFile(String str, String str2);

    public native int getProgress();

    public native int getVersion();

    public libsevenzip(String file) throws UnsatisfiedLinkError {
        System.load(file);
    }
}
