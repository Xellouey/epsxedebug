package com.epsxe.ePSXe.util;

import android.app.AlertDialog;
import android.app.ProgressDialog;

/* loaded from: classes.dex */
public final class DialogUtil {
    public static void closeDialog(ProgressDialog dialog) {
        if (dialog != null) {
            try {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            } catch (Exception e) {
            }
        }
    }

    public static void closeDialog(AlertDialog dialog) {
        if (dialog != null) {
            try {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            } catch (Exception e) {
            }
        }
    }
}
