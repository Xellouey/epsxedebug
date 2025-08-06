package com.epsxe.ePSXe.task;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.epsxe.ePSXe.R;
import com.epsxe.ePSXe.ePSXePreferences;
import com.epsxe.ePSXe.util.DialogUtil;
import com.google.android.gms.common.api.CommonStatusCodes;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.apache.http.client.methods.HttpGet;

/* loaded from: classes.dex */
public class DownloadShaderPluginTask extends AsyncTask<Integer, TaskProgress, Integer> {
    private WeakReference<Activity> activity;
    private WeakReference<Context> context;
    private ProgressDialog dialog;

    public DownloadShaderPluginTask(Context ctx, Activity act) {
        Log.e("DownloadShaderTask", "start");
        this.context = new WeakReference<>(ctx);
        this.activity = new WeakReference<>(act);
        try {
            this.dialog = new ProgressDialog(this.context.get());
            this.dialog.setTitle(R.string.file_shadertitle);
            this.dialog.setProgressStyle(1);
            this.dialog.setMax(100);
            this.dialog.setProgress(0);
            this.dialog.setCancelable(false);
            this.dialog.setMessage("Downloading index...");
            this.dialog.show();
        } catch (Exception e) {
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public Integer doInBackground(Integer... params) {
        Log.e("DownloadShaderTask", "doInBackground");
        try {
            File SDCardRoot = ContextCompat.getDataDir(context.get());
            File file = new File(SDCardRoot, "/epsxe/shaders/shaders.ini");
            Log.e("DownloadShaderTask", "file http://epsxe.com/files/shaders/shaders.ini");
            if (DownloadFile("http://epsxe.com/files/shaders/shaders.ini", file, "Downloading index...") == 0 && file.exists()) {
                BufferedReader br = new BufferedReader(new FileReader(file));
                while (true) {
                    String line = br.readLine();
                    if (line == null) {
                        break;
                    }
                    String[] split = line.split("#");
                    if (split.length == 3) {
                        File sh = new File(SDCardRoot, "epsxe/shaders/" + split[0]);
                        File sv = new File(SDCardRoot, "epsxe/shaders/" + split[0] + "/gpuCore.slv");
                        File sf = new File(SDCardRoot, "epsxe/shaders/" + split[0] + "/gpuCore.slf");
                        File si = new File(SDCardRoot, "epsxe/shaders/" + split[0] + "/gpuCore.ini");
                        String msg = "Downloading shader " + split[0] + " version=" + split[1] + "...";
                        if (!sh.exists()) {
                            sh.mkdirs();
                        }
                        publishProgress(new TaskProgress(0, msg));
                        if (DownloadFile("http://epsxe.com/files/shaders/" + split[2] + "/gpuCore.slv", sv, msg) == 0) {
                            publishProgress(new TaskProgress(0, msg));
                            if (DownloadFile("http://epsxe.com/files/shaders/" + split[2] + "/gpuCore.slf", sf, msg) == 0) {
                                publishProgress(new TaskProgress(0, msg));
                                DownloadFile("http://epsxe.com/files/shaders/" + split[2] + "/gpuCore.ini", si, msg);
                            }
                        }
                    }
                }
                br.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public void onPostExecute(Integer result) {
        Log.e("DownloadShadersTask", "end");
        if (result.intValue() == 0) {
            if (this.context.get() != null) {
                Intent myIntent = new Intent(this.context.get(), (Class<?>) ePSXePreferences.class);
                this.context.get().startActivity(myIntent);
                DialogUtil.closeDialog(this.dialog);
                if (this.activity.get() != null) {
                    this.activity.get().finish();
                    return;
                }
                return;
            }
            return;
        }
        DialogUtil.closeDialog(this.dialog);
    }

    @Override // android.os.AsyncTask
    public void onProgressUpdate(TaskProgress... progress) {
        try {
            this.dialog.setProgress(progress[0].percentage);
        } catch (Exception e) {
        }
        try {
            this.dialog.setMessage(progress[0].message);
        } catch (Exception e2) {
        }
    }

    private int DownloadFile(String mUrl, File file, String nfile) {
        for (int retries = 0; retries < 5; retries++) {
            try {
                URL url = new URL(mUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod(HttpGet.METHOD_NAME);
                urlConnection.setDoOutput(false);
                urlConnection.setConnectTimeout(10000);
                urlConnection.setReadTimeout(5000);
                urlConnection.connect();
                FileOutputStream fileOutput = new FileOutputStream(file);
                InputStream inputStream = urlConnection.getInputStream();
                int totalSize = urlConnection.getContentLength();
                int downloadedSize = 0;
                if (totalSize == 0) {
                    totalSize = 1;
                }
                byte[] buffer = new byte[1024];
                while (true) {
                    int bufferLength = inputStream.read(buffer);
                    if (bufferLength > 0) {
                        fileOutput.write(buffer, 0, bufferLength);
                        downloadedSize += bufferLength;
                        publishProgress(new TaskProgress((downloadedSize * 100) / totalSize, nfile));
                    } else {
                        urlConnection.disconnect();
                        fileOutput.close();
                        return 0;
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return -1;
            } catch (MalformedURLException e2) {
                e2.printStackTrace();
                return -1;
            } catch (IOException e3) {
            }
        }
        return -1;
    }
}
