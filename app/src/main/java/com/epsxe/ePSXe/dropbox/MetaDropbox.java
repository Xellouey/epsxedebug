package com.epsxe.ePSXe.dropbox;

import com.dropbox.core.v2.files.FileMetadata;

/* loaded from: classes.dex */
public class MetaDropbox implements Comparable<MetaDropbox> {
    private String filename;
    private FileMetadata metadata;

    public MetaDropbox(String f, FileMetadata m) {
        this.filename = f;
        this.metadata = m;
    }

    public String getFilename() {
        return this.filename;
    }

    public FileMetadata getMetadata() {
        return this.metadata;
    }

    @Override // java.lang.Comparable
    public int compareTo(MetaDropbox o) {
        if (this.filename != null) {
            return this.filename.toLowerCase().compareTo(o.getFilename().toLowerCase());
        }
        throw new IllegalArgumentException();
    }
}
