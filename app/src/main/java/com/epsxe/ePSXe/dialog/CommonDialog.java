package com.epsxe.ePSXe.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.epsxe.ePSXe.R;

/* loaded from: classes.dex */
public final class CommonDialog {
    public static void showIsoErrorDialog(String isoName, Context mContext) {
        AlertDialog builder = new AlertDialog.Builder(mContext)
                .setTitle("Error running game")
                .setCancelable(true)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton(R.string.dialog_action_ok, (DialogInterface.OnClickListener) null)
                .setMessage("Error loading " + isoName + ", possible errors: \n 1) missing data rom img/iso/bin \n 2) .7z/.ape format NOT supported!!!")
                .create();
        builder.show();
    }
}
