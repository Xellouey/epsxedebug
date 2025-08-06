package com.epsxe.ePSXe.util;

import android.util.Log;
import com.epsxe.ePSXe.InputList;
import com.epsxe.ePSXe.jni.libgamedetect;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/* loaded from: classes.dex */
public final class PSXUtil {
    public static String getPSXGameID(String isoName, int slot, int oformat) {
        try {
            libgamedetect d = new libgamedetect();
            String code = d.getCodeSlot(isoName, slot, oformat).toUpperCase();
            return code.equals("ECM") ? "" : code;
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean isIsoExtension(String name) {
        return name.toLowerCase().endsWith(".cue") || name.toLowerCase().endsWith(".ccd") || name.toLowerCase().endsWith(".bin") || name.toLowerCase().endsWith(".img") || name.toLowerCase().endsWith(".iso") || name.toLowerCase().endsWith(".mds") || name.toLowerCase().endsWith(".mdf") || name.toLowerCase().endsWith(".cdi") || name.toLowerCase().endsWith(".nrg") || name.toLowerCase().endsWith(".pbp") || name.toLowerCase().endsWith(".ecm");
    }

    public static boolean create_memcard(String sdcardname) {
        if (!sdcardname.equals("")) {
            if (!sdcardname.toLowerCase().endsWith(".mcr")) {
                sdcardname = sdcardname + ".mcr";
            }
            File f = new File(sdcardname);
            if (!f.exists()) {
                try {
                    FileOutputStream s = new FileOutputStream(sdcardname);
                    byte[] buf = new byte[131072];
                    for (int i = 0; i < 131072; i++) {
                        buf[i] = 0;
                    }
                    buf[0] = 77;
                    buf[1] = 67;
                    buf[127] = 14;
                    for (int i2 = 0; i2 < 15; i2++) {
                        buf[(i2 * 128) + 128] = -96;
                        buf[(i2 * 128) + 136] = -1;
                        buf[(i2 * 128) + InputList.KEYCODE_F7] = -1;
                        buf[(i2 * 128) + 255] = -96;
                    }
                    for (int i3 = 0; i3 < 20; i3++) {
                        buf[(i3 * 128) + 2048] = -1;
                        buf[(i3 * 128) + 2049] = -1;
                        buf[(i3 * 128) + 2050] = -1;
                        buf[(i3 * 128) + 2051] = -1;
                        buf[(i3 * 128) + 2056] = -1;
                        buf[(i3 * 128) + 2057] = -1;
                    }
                    try {
                        s.write(buf);
                        s.flush();
                        s.close();
                        return true;
                    } catch (IOException e) {
                        Log.e("epsxefolder", "IOException");
                        return false;
                    }
                } catch (FileNotFoundException e2) {
                    Log.e("epsxefolder", "FileNotFound");
                    return false;
                }
            }
        }
        return false;
    }
}
