package com.epsxe.ePSXe.task;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import com.epsxe.ePSXe.libsevenzip;
import com.epsxe.ePSXe.util.DialogUtil;
import java.io.File;

/* loaded from: classes.dex */
public class DecodeSevenZipTask extends AsyncTask<String, Integer, Integer> {
    private Activity activity;
    private Context context;

    /* renamed from: d */
    private libsevenzip f189d;
    ProgressDialog dialog;
    int error;

    /* renamed from: in */
    String f190in;
    private int is64bits;
    String out;

    private void deletelibrary(String path) {
        try {
            File f = new File(path);
            if (f.exists()) {
                f.delete();
            }
        } catch (Exception e) {
            Log.e("DecodeSevenZipTask", "error deleting library");
        }
    }

    public DecodeSevenZipTask(Activity a, Context ctx, String path, int is64bits) {
        this.is64bits = 0;
        this.error = 0;
        Log.e("DecodeSevenZipTask", "start");
        this.context = ctx;
        this.activity = a;
        this.is64bits = is64bits;
        int currentOrientation = this.activity.getResources().getConfiguration().orientation;
        try {
            this.f189d = new libsevenzip(path);
            if (currentOrientation == 2) {
                this.activity.setRequestedOrientation(6);
            } else {
                this.activity.setRequestedOrientation(7);
            }
            this.dialog = new ProgressDialog(this.context);
            this.dialog.setTitle("Extracting file");
            this.dialog.setProgressStyle(1);
            this.dialog.setMax(100);
            this.dialog.setProgress(0);
            this.dialog.setCancelable(false);
            this.dialog.show();
        } catch (UnsatisfiedLinkError e) {
            Log.e("DecodeSevenZipTask", "unable to load the uncompress library");
            deletelibrary(path);
            this.error = -1;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public Integer doInBackground(String... params) {
        this.f190in = params[0];
        this.out = params[1];
        Log.e("DecodeSevenZipTask", "params " + params[0]);
        if (this.error != 0) {
            return -1;
        }
        int res = decodeSevenZip(this.f190in, this.out);
        return Integer.valueOf(res);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public void onPostExecute(Integer result) {
        Log.e("Decode7zTask", "end=" + result);
        if (result.intValue() == 0) {
            DialogUtil.closeDialog(this.dialog);
            Toast.makeText(this.context, "Extracted done. Rescan to refresh the gamelist.", 0).show();
        } else {
            if (result.intValue() == -1) {
                if (this.is64bits == 1) {
                    Toast.makeText(this.context, "Please update epsxe sevenzip to the beta version!!!", 1).show();
                    return;
                } else {
                    Toast.makeText(this.context, "Uncompress library corrupted, please download again", 1).show();
                    return;
                }
            }
            DialogUtil.closeDialog(this.dialog);
            Toast.makeText(this.context, "Extracting error", 1).show();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public void onProgressUpdate(Integer... progress) {
        this.dialog.setProgress(progress[0].intValue());
    }

    /* JADX WARN: Type inference failed for: r1v0, types: [com.epsxe.ePSXe.task.DecodeSevenZipTask$1] */
    private int decodeSevenZip(String inFile, String outPath) {
        new Thread() { // from class: com.epsxe.ePSXe.task.DecodeSevenZipTask.1
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                int val;
                do {
                    try {
                        val = DecodeSevenZipTask.this.f189d.getProgress();
                        DecodeSevenZipTask.this.publishProgress(Integer.valueOf(val));
                        sleep(200L);
                    } catch (Exception e) {
                        return;
                    }
                } while (val <= 99);
            }
        }.start();
        int res = this.f189d.extractFile(inFile, "-o" + outPath);
        this.activity.setRequestedOrientation(4);
        return res;
    }
}
