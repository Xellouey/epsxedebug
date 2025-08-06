package com.epsxe.ePSXe.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.epsxe.ePSXe.R;
import com.epsxe.ePSXe.jni.libepsxe;

/* loaded from: classes.dex */
public final class ResetDialog {
    public static void showResetDialog(Context mContext, final libepsxe epsxe) {
        AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
        alertDialog.setTitle(R.string.main_reset_title);
        alertDialog.setMessage(mContext.getString(R.string.main_wantresetgame));
        alertDialog.setButton(mContext.getString(R.string.main_noresetgame), new DialogInterface.OnClickListener() { // from class: com.epsxe.ePSXe.dialog.ResetDialog.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertDialog.setButton2(mContext.getString(R.string.main_yesresetgame), new DialogInterface.OnClickListener() { // from class: com.epsxe.ePSXe.dialog.ResetDialog.2
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                epsxe.reset();
            }
        });
        alertDialog.setIcon(R.drawable.icon);
        alertDialog.show();
    }
}
