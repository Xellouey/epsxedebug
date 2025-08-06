package com.epsxe.ePSXe.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.preference.PreferenceManager;

import com.epsxe.ePSXe.R;
import com.epsxe.ePSXe.ePSXeApplication;

/* loaded from: classes.dex */
public final class ResetPreferencesDialog {
    public static void showResetPreferendesDialog(final Context mContext) {
        AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
        alertDialog.setTitle("Reset preferences");
        alertDialog.setMessage("Do you really want to reset the ePSXe preferences to default values?");
        alertDialog.setButton(mContext.getString(R.string.dialog_action_cancel), new DialogInterface.OnClickListener() { // from class: com.epsxe.ePSXe.dialog.ResetPreferencesDialog.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertDialog.setButton2(mContext.getString(R.string.dialog_action_ok), new DialogInterface.OnClickListener() { // from class: com.epsxe.ePSXe.dialog.ResetPreferencesDialog.2
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                ePSXeApplication.getDefaultSharedPreferences(mContext).edit().clear().commit();
            }
        });
        alertDialog.setIcon(R.drawable.icon);
        alertDialog.show();
    }
}
