package com.epsxe.ePSXe.gdrive;

import com.google.android.gms.drive.Metadata;

/* loaded from: classes.dex */
public class MetaGdrive implements Comparable<MetaGdrive> {
    private String filename;
    private Metadata metadata;

    public MetaGdrive(String f, Metadata m) {
        this.filename = f;
        this.metadata = m;
    }

    public String getFilename() {
        return this.filename;
    }

    public Metadata getMetadata() {
        return this.metadata;
    }

    @Override // java.lang.Comparable
    public int compareTo(MetaGdrive o) {
        if (this.filename != null) {
            return this.filename.toLowerCase().compareTo(o.getFilename().toLowerCase());
        }
        throw new IllegalArgumentException();
    }
}
