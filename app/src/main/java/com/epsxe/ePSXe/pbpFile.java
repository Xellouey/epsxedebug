package com.epsxe.ePSXe;

import android.util.Log;
import java.io.RandomAccessFile;

/* loaded from: classes.dex */
public class pbpFile {
    public static final int PBP_PSAR = 36;
    public static final int PBP_PSTITLEIMG = 512;
    String[] nameFiles;
    int numFiles;

    static String[] append(String[] arr, String element) {
        int N = arr.length;
        String[] nameFilestmp = new String[N + 1];
        System.arraycopy(arr, 0, nameFilestmp, 0, N);
        nameFilestmp[N] = element;
        return nameFilestmp;
    }

    private long getLong(byte[] v) {
        long n = (v[0] & 255) + ((v[1] & 255) * 256) + ((v[2] & 255) * 256 * 256) + ((v[3] & 255) * 256 * 256 * 256);
        return n;
    }

    public pbpFile(String file, String filename) {
        this.numFiles = 0;
        this.nameFiles = new String[]{""};
        byte[] pbptype = new byte[12];
        byte[] tmp = new byte[4];
        try {
            RandomAccessFile mFile = new RandomAccessFile(file, "r");
            mFile.seek(36L);
            mFile.read(tmp);
            long psar = getLong(tmp);
            mFile.seek(psar);
            mFile.read(pbptype);
            String str = new String(pbptype);
            if (str.equals("PSTITLEIMG00")) {
                Log.e("pbpfile", "multi");
                for (int i = 0; i < 6; i++) {
                    byte[] name = new byte[256];
                    mFile.seek(512 + psar + (i * 4));
                    mFile.read(tmp);
                    long off = getLong(tmp);
                    if (off == 0) {
                        break;
                    }
                    mFile.seek(off + psar + 4652);
                    mFile.read(name);
                    String fname = new String(name);
                    this.nameFiles = append(this.nameFiles, fname.substring(0, fname.indexOf(0)) + " (CD" + (i + 1) + ")");
                    this.numFiles++;
                }
            } else if (str.equals("PSISOIMG0000")) {
                this.nameFiles = append(this.nameFiles, filename);
                this.numFiles++;
            } else {
                this.numFiles = 0;
                mFile.close();
                return;
            }
            for (int j = 0; j < this.nameFiles.length; j++) {
                Log.e("pbpfile", "--> " + this.nameFiles[j]);
            }
            mFile.close();
        } catch (Exception e) {
            this.nameFiles = null;
            this.nameFiles = append(this.nameFiles, filename);
            this.numFiles = 1;
        }
    }

    public int getNumFiles() {
        return this.numFiles;
    }

    public String getFileName(int num) {
        if (num > this.numFiles) {
            return "";
        }
        String blank = this.nameFiles[num];
        return blank;
    }
}
