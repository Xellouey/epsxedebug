package com.epsxe.ePSXe;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import com.google.android.vending.licensing.AESObfuscator;
import com.google.android.vending.licensing.LicenseChecker;
import com.google.android.vending.licensing.LicenseCheckerCallback;
import com.google.android.vending.licensing.ServerManagedPolicy;

/* loaded from: classes.dex */
public abstract class LicenseCheckActivity extends Activity {
    static final String BASE64_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAolDgQtljobElYOFJ9+j0jSYNA/j0txHxa9D5pVASwNcVL6z4VTdqDhEK3fdeyr0acwXvmFI1msFhNcfPdjET0VBYztw3iKrfKuZuo7Y9ML+gHpSQXM2uQubhALLKhIfx3yXoDBprOzjpUon+Y/E6Pe/P+e+XvC8jwm+uJafnIFWqjIuKn+9SMBZu4ZghkVuGI/JYW2oJMArZe1WsrcXChPF3tAnaXyKIVm4kxvad5YGwnidSWTYIPS+8EPnk9cj+dFPgGAOAjKm9wtCZpfl91qqzfnIbyzzgtYy2KozpL2O8O7Q4BbNsMzEgilaIcfgwEI2N8WmbaYaKScnP7cHqOQIDAQAB";
    LicenseChecker mChecker;
    Handler mHandler;
    LicenseCheckerCallback mLicenseCheckerCallback;
    SharedPreferences prefs;
    static boolean licensed = true;
    static boolean licensedstop = true;
    static boolean didCheck = false;
    static boolean checkingLicense = false;
    private static final byte[] SALT = {37, 43, -82, -12, 11, -112, -94, 1, 88, 74, 103, 102, -76, -3, -117, 99, 67, -3, 126, -17};

    /* JADX INFO: Access modifiers changed from: private */
    public void displayResult(String result) {
        this.mHandler.post(new Runnable() { // from class: com.epsxe.ePSXe.LicenseCheckActivity.1
            @Override // java.lang.Runnable
            public void run() {
                LicenseCheckActivity.this.setProgressBarIndeterminateVisibility(false);
            }
        });
    }

    protected void doCheck() {
        didCheck = false;
        checkingLicense = true;
        setProgressBarIndeterminateVisibility(true);
        this.mChecker.checkAccess(this.mLicenseCheckerCallback);
    }

    protected void checkLicense() {
        Log.i("LICENSE", "checkLicense");
        this.mHandler = new Handler();
        String deviceId = Settings.Secure.getString(getContentResolver(), "android_id");
        this.mLicenseCheckerCallback = new MyLicenseCheckerCallback();
        this.mChecker = new LicenseChecker(this, new ServerManagedPolicy(this, new AESObfuscator(SALT, getPackageName(), deviceId)), BASE64_PUBLIC_KEY);
        doCheck();
    }

    protected boolean getLicenseResult() {
        return licensedstop;
    }

    protected class MyLicenseCheckerCallback implements LicenseCheckerCallback {
        protected MyLicenseCheckerCallback() {
        }

        @Override // com.google.android.vending.licensing.LicenseCheckerCallback
        public void allow(int reason) {
            Log.i("LICENSE", "allow");
            if (!LicenseCheckActivity.this.isFinishing()) {
                LicenseCheckActivity.this.displayResult(LicenseCheckActivity.this.getString(R.string.allow));
                LicenseCheckActivity.licensed = true;
                LicenseCheckActivity.licensedstop = true;
                LicenseCheckActivity.checkingLicense = false;
                LicenseCheckActivity.didCheck = true;
            }
        }

        @Override // com.google.android.vending.licensing.LicenseCheckerCallback
        public void dontAllow(int reason) {
            Log.i("LICENSE", "dontAllow");
            if (!LicenseCheckActivity.this.isFinishing() && reason != 291) {
                LicenseCheckActivity.this.displayResult(LicenseCheckActivity.this.getString(R.string.dont_allow));
                LicenseCheckActivity.licensed = false;
                LicenseCheckActivity.checkingLicense = false;
                LicenseCheckActivity.didCheck = true;
                LicenseCheckActivity.licensedstop = false;
            }
        }

        @Override // com.google.android.vending.licensing.LicenseCheckerCallback
        public void applicationError(int errorCode) {
            Log.i("LICENSE", "error: " + errorCode);
            if (!LicenseCheckActivity.this.isFinishing()) {
                LicenseCheckActivity.licensed = false;
                String.format(LicenseCheckActivity.this.getString(R.string.application_error), Integer.valueOf(errorCode));
                LicenseCheckActivity.checkingLicense = false;
                LicenseCheckActivity.didCheck = true;
                LicenseCheckActivity.licensedstop = false;
            }
        }
    }

    @Override // android.app.Activity
    protected Dialog onCreateDialog(int id) {
        return new AlertDialog.Builder(this).setTitle(R.string.unlicensed_dialog_title).setMessage(R.string.unlicensed_dialog_body).setPositiveButton(R.string.buy_button, new DialogInterface.OnClickListener() { // from class: com.epsxe.ePSXe.LicenseCheckActivity.4
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                Intent marketIntent = new Intent("android.intent.action.VIEW", Uri.parse("http://market.android.com/details?id=" + LicenseCheckActivity.this.getPackageName()));
                LicenseCheckActivity.this.startActivity(marketIntent);
                LicenseCheckActivity.this.finish();
            }
        }).setNegativeButton(R.string.quit_button, new DialogInterface.OnClickListener() { // from class: com.epsxe.ePSXe.LicenseCheckActivity.3
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                LicenseCheckActivity.this.finish();
            }
        }).setCancelable(false).setOnKeyListener(new DialogInterface.OnKeyListener() { // from class: com.epsxe.ePSXe.LicenseCheckActivity.2
            @Override // android.content.DialogInterface.OnKeyListener
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                Log.i("License", "Key Listener");
                LicenseCheckActivity.this.finish();
                return true;
            }
        }).create();
    }

    @Override // android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        if (this.mChecker != null) {
            Log.i("LIcense", "distroy checker");
            this.mChecker.onDestroy();
        }
    }
}
