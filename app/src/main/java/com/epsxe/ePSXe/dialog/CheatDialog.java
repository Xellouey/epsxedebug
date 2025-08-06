package com.epsxe.ePSXe.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.epsxe.ePSXe.R;
import com.epsxe.ePSXe.jni.libepsxe;
import com.epsxe.ePSXe.task.DownloadCheatFileTask;
import com.fasterxml.jackson.core.util.MinimalPrettyPrinter;

/* loaded from: classes.dex */
public final class CheatDialog {
    public static void showDownloadCheatDialog(final Context cont, final libepsxe e) {
        TextView message = new TextView(cont);
        SpannableString s = new SpannableString(cont.getText(R.string.cheat_dialog_message1));
        SpannableString s1 = new SpannableString(cont.getText(R.string.cheat_dialog_message2));
        Linkify.addLinks(s1, 1);
        message.setText(((Object) s) + e.getCode() + ((Object) s1));
        message.setMovementMethod(LinkMovementMethod.getInstance());
        AlertDialog builder = new AlertDialog.Builder(cont).setTitle(((Object) cont.getText(R.string.nocheat_dialog_title)) + " "
                + e.getCode()).setCancelable(true).setIcon(android.R.drawable.ic_dialog_info).setNegativeButton(R.string.cheat_dialog_back, (DialogInterface.OnClickListener) null).setPositiveButton(R.string.cheat_dialog_download, new DialogInterface.OnClickListener() { // from class: com.epsxe.ePSXe.dialog.CheatDialog.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int id) {
                Log.e("epsxecheat", "Downloading cheat file...");
                Toast.makeText(cont, R.string.cheat_dialog_downloading, 1).show();
                new DownloadCheatFileTask(cont, e).execute("");
            }
        }).setView(message).create();
        builder.show();
    }

    public static void showCheatDialog(Context cont, final libepsxe e) {
        int num = e.getGSNumber() + 1;
        if (num <= 0) {
            showDownloadCheatDialog(cont, e);
            return;
        }
        CharSequence[] _options = new String[num];
        boolean[] _selections = new boolean[num];
        for (int i = 0; i < num; i++) {
            try {
                _options[i] = new String(e.getGSName(i), "utf-8");
                if (e.getGSStatus(i) == 0) {
                    _selections[i] = false;
                } else {
                    _selections[i] = true;
                }
            } catch (Exception e2) {
            }
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(cont);
        builder.setTitle(((Object) cont.getText(R.string.cheat_dialog_title)) + " " + e.getCode());
        builder.setMultiChoiceItems(_options, _selections, new DialogInterface.OnMultiChoiceClickListener() { // from class: com.epsxe.ePSXe.dialog.CheatDialog.2
            @Override // android.content.DialogInterface.OnMultiChoiceClickListener
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if (isChecked) {
                    Log.e("epsxecheat", "id on:" + which);
                    e.enableGS(which);
                } else {
                    Log.e("epsxecheat", "id off:" + which);
                    e.disableGS(which);
                }
            }
        });
        builder.setPositiveButton(R.string.cheat_dialog_apply, new DialogInterface.OnClickListener() { // from class: com.epsxe.ePSXe.dialog.CheatDialog.3
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int id) {
                Log.e("epsxecheat", "id:" + id);
            }
        });
        builder.setNegativeButton(R.string.cheat_dialog_disableall, new DialogInterface.OnClickListener() { // from class: com.epsxe.ePSXe.dialog.CheatDialog.4
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int id) {
                Log.e("epsxecheat", "clear All");
                e.disableAllGS();
            }
        });
        builder.setNeutralButton(R.string.cheat_dialog_enableall, new DialogInterface.OnClickListener() { // from class: com.epsxe.ePSXe.dialog.CheatDialog.5
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int id) {
                Log.e("epsxecheat", "set All");
                e.enableAllGS();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
