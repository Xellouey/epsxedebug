package com.epsxe.ePSXe.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.support.v4.media.session.PlaybackStateCompat;

import com.epsxe.ePSXe.OptionSstate;
import com.epsxe.ePSXe.R;
import com.epsxe.ePSXe.util.FileUtil;
import com.fasterxml.jackson.core.util.MinimalPrettyPrinter;
import java.io.File;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/* loaded from: classes.dex */
public final class SstateDialog {
    public static List<OptionSstate> fillSS(Context cont, String currentDir, String gameCode, ByteBuffer byteBuffer) {
        List<OptionSstate> fls = new ArrayList<>();
        for (int ind = 0; ind < 5; ind++) {
            try {
                File ff = new File(currentDir + "/" + gameCode + ".00" + ind);
                if (ff.exists()) {
                    Long lastModified = Long.valueOf(ff.lastModified());
                    Date date = new Date(lastModified.longValue());
                    File ffpic = new File(ff.getAbsolutePath() + ".pic");
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                    String sdate = sdf.format(date);
                    Bitmap tBitmap = Bitmap.createBitmap(128, 96, Bitmap.Config.RGB_565);
                    if (ffpic.exists()) {
                        byteBuffer.clear();
                        byteBuffer.put(FileUtil.getBytesFromFile(ffpic));
                        byteBuffer.flip();
                        tBitmap.copyPixelsFromBuffer(byteBuffer);
                        fls.add(new OptionSstate(ff.getName(), cont.getString(R.string.main_filesize) + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + (ff.length() / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID) + " Kb", ff.getAbsolutePath(), "" + sdate, "" + ind, tBitmap));
                    } else {
                        fls.add(new OptionSstate(ff.getName(), cont.getString(R.string.main_filesize) + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + (ff.length() / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID) + " Kb", ff.getAbsolutePath(), "" + sdate, "" + ind, tBitmap));
                    }
                } else {
                    fls.add(new OptionSstate(cont.getString(R.string.main_slotfree), "", "", "", "" + ind, Bitmap.createBitmap(128, 96, Bitmap.Config.RGB_565)));
                }
            } catch (Exception e) {
            }
        }
        return fls;
    }

    public static void showDeleteSstateDialog(Context cont, final String dfile) {
        AlertDialog alertDialog = new AlertDialog.Builder(cont).create();
        alertDialog.setTitle(R.string.main_dstate);
        alertDialog.setMessage(cont.getString(R.string.main_dstatewant));
        alertDialog.setButton(cont.getString(R.string.main_dstateno), new DialogInterface.OnClickListener() { // from class: com.epsxe.ePSXe.dialog.SstateDialog.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertDialog.setButton2(cont.getString(R.string.main_dstateyes), new DialogInterface.OnClickListener() { // from class: com.epsxe.ePSXe.dialog.SstateDialog.2
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                File fileTmp = new File(dfile);
                if (fileTmp.exists()) {
                    fileTmp.delete();
                }
            }
        });
        alertDialog.setIcon(R.drawable.icon);
        alertDialog.show();
    }
}
