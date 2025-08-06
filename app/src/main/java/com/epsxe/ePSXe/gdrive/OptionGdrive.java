package com.epsxe.ePSXe.gdrive;

/* loaded from: classes.dex */
public class OptionGdrive implements Comparable<OptionGdrive> {
    private String file;
    private String local;
    private String name;
    private String remote;

    public OptionGdrive(String f, String n, String l, String r) {
        this.file = f;
        this.name = n;
        this.local = l;
        this.remote = r;
    }

    public String getFile() {
        return this.file;
    }

    public String getName() {
        return this.name;
    }

    public String getLocal() {
        return this.local;
    }

    public String getRemote() {
        return this.remote;
    }

    public void setRemote(String r) {
        this.remote = r;
    }

    public void setLocal(String l) {
        this.local = l;
    }

    @Override // java.lang.Comparable
    public int compareTo(OptionGdrive o) {
        if (this.name != null) {
            return this.name.toLowerCase().compareTo(o.getName().toLowerCase());
        }
        throw new IllegalArgumentException();
    }
}
