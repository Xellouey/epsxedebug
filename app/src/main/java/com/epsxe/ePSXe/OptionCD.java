package com.epsxe.ePSXe;

/* loaded from: classes.dex */
public class OptionCD implements Comparable<OptionCD> {
    private String data;
    private String name;
    private String path;
    private int slot;

    public OptionCD(String n, String d, String p, int s) {
        this.name = n;
        this.data = d;
        this.path = p;
        this.slot = s;
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

    public int getSlot() {
        return this.slot;
    }

    @Override // java.lang.Comparable
    public int compareTo(OptionCD o) {
        if (this.name != null) {
            return this.name.toLowerCase().compareTo(o.getName().toLowerCase());
        }
        throw new IllegalArgumentException();
    }
}
