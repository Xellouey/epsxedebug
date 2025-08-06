package com.epsxe.ePSXe;

import android.content.Context;
import android.os.Environment;

import androidx.core.content.ContextCompat;

import java.io.File;

/* loaded from: classes.dex */
public class FileCache {
    private File cacheDir;

    public FileCache(Context context) {
        this.cacheDir = new File(ContextCompat.getDataDir(context), "epsxe/info");
        if (!this.cacheDir.exists()) {
            this.cacheDir.mkdirs();
        }
    }

    public File getFile(String filename, String filehash) {
        File f = new File(this.cacheDir, filehash);
        if (!f.exists()) {
            File fi = new File(this.cacheDir, filename);
            return fi;
        }
        return f;
    }

    public void clear() {
        File[] files = this.cacheDir.listFiles();
        if (files != null) {
            for (File f : files) {
                f.delete();
            }
        }
    }
}
