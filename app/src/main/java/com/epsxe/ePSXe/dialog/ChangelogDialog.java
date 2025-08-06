package com.epsxe.ePSXe.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Log;

import com.epsxe.ePSXe.R;
import com.epsxe.ePSXe.ePSXeApplication;
import com.epsxe.ePSXe.util.DeviceUtil;

/* loaded from: classes.dex */
public final class ChangelogDialog {
    public static void ChangelogDialog(Context mContext) {
        DeviceUtil.getAppVersion(mContext);
        AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
        alertDialog.setTitle("What's New");
        alertDialog.setMessage(Html.fromHtml("<p><b><p>ePSXe v2.0.9, released on 08.02.2019</b></p><p><br>&#149;<li>Update Android SDK support to Oreo<br>&#149;<li>Support to movement in Resident Evil Survivor Namco Gun mode<br>\n&#149;<li>Added Google Drive support<br>\n&#149;<li>Fixed fullscreen mode in 18:9 devices<br>\n&#149;<li>Added OTG Mouse support<br>\n&#149;<li>Fixed sound volume sweep (DW7 and DQ4)<br>\n&#149;<li>Fixed shortcuts on Android 8+<br>\n&#149;<li>Added barrel distorsion effect to VR mode<br>\n&#149;<li>Fixed touchscreen editor in low resolution phones<br>\n&#149;<li>Reworked the autosave option<br>\n&#149;<li>Fixed touchscreen skin when using low res modes in opengl<br>\n&#149;<li>Improved gamepad skin fit when changing the screen resolution<br>\n&#149;<li>Fixed touchscreen skin in portrait mode<br>\n&#149;<li>Support to disable the confirm prompt dialog on resume<br>\n&#149;<li>Support to disable confirm dialog on exit<br>\n&#149;<li>Added buttons to volume up/down<br>\n&#149;<li>Added a turbo button support (combo with other button)<br>\n&#149;<li>Added fast-forward button support<br>\n&#149;<li>Improved netplay feature (less desyncs)<br>\n&#149;<li>Fixed some crashes on gamelist scanning (buggy big CUE files)<br>\n&#149;<li>More misc fixes</p>"));
        alertDialog.setButton(mContext.getString(R.string.dialog_action_ok), new DialogInterface.OnClickListener() { // from class: com.epsxe.ePSXe.dialog.ChangelogDialog.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertDialog.setIcon(R.drawable.icon);
        alertDialog.show();
    }

    public static void showChangelogDialog(Context c, int version) {
        try {
            PackageInfo packageInfo = c.getPackageManager().getPackageInfo(c.getPackageName(), 0);
            int versionCode = packageInfo.versionCode;
            if (version < versionCode) {
                SharedPreferences sharedPreferences = ePSXeApplication.getDefaultSharedPreferences(c);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("changelog", "" + versionCode);
                editor.commit();
                ChangelogDialog(c);
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.w("epsxe", "Unable to get version code. Will not show changelog");
        }
    }
}
