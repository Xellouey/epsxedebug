package com.epsxe.ePSXe;

import android.graphics.Bitmap;

/* loaded from: classes.dex */
public class OptionDesc implements Comparable<OptionDesc> {
    private String code;
    private String country;
    private String file;
    private Bitmap mBitmap;
    private String multitap;
    private String name;
    private String nameJP;
    private String nplayers;
    private String padtype;
    private String path;
    private int slot;
    private String text;

    public OptionDesc(String n, String j, String c, String t, String f, String x, String p, String m, String y, String e, int s, Bitmap b) {
        this.name = n;
        this.nameJP = j;
        this.code = c;
        this.text = t;
        this.file = f;
        this.country = x;
        this.path = p;
        this.slot = s;
        this.multitap = m;
        this.nplayers = y;
        this.mBitmap = b;
        this.padtype = e;
    }

    public String getName() {
        return this.name;
    }

    public String getNameJP() {
        return this.nameJP;
    }

    public String getCode() {
        return this.code;
    }

    public String getText() {
        return this.text;
    }

    public String getFile() {
        return this.file;
    }

    public String getCountry() {
        return this.country;
    }

    public String getPath() {
        return this.path;
    }

    public int getSlot() {
        return this.slot;
    }

    public String getMultitap() {
        return this.multitap;
    }

    public String getNumPlayers() {
        return this.nplayers;
    }

    public Bitmap getBitmap() {
        return this.mBitmap;
    }

    public String getPadType() {
        return this.padtype;
    }

    @Override // java.lang.Comparable
    public int compareTo(OptionDesc o) {
        if (this.name != null) {
            return this.name.toLowerCase().compareTo(o.getName().toLowerCase());
        }
        throw new IllegalArgumentException();
    }
}
