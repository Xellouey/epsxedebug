package com.epsxe.ePSXe;

/* loaded from: classes.dex */
public class gamePathInfo {
    private String path;
    private int slot;

    public gamePathInfo(int s, String p) {
        this.slot = s;
        this.path = p;
    }

    public String getPath() {
        return this.path;
    }

    public int getSlot() {
        return this.slot;
    }
}
