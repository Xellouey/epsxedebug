package com.epsxe.ePSXe;

import android.graphics.Bitmap;

/* loaded from: classes.dex */
public class OptionSstate implements Comparable<OptionSstate> {
    private String data;
    private String date;
    private Bitmap mBitmap;
    private String name;
    private String path;
    private String slot;

    public OptionSstate(String n, String d, String p, String t, String s, Bitmap b) {
        this.name = n;
        this.data = d;
        this.path = p;
        this.date = t;
        this.slot = s;
        this.mBitmap = b;
    }

    public String getName() {
        return this.name;
    }

    public String getData() {
        return this.data;
    }

    public String getPath() {
        return this.path;
    }

    public String getDate() {
        return this.date;
    }

    public String getSlot() {
        return this.slot;
    }

    public Bitmap getBitmap() {
        return this.mBitmap;
    }

    @Override // java.lang.Comparable
    public int compareTo(OptionSstate o) {
        if (this.name != null) {
            return this.name.toLowerCase().compareTo(o.getName().toLowerCase());
        }
        throw new IllegalArgumentException();
    }
}
