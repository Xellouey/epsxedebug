package com.epsxe.ePSXe.task;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.epsxe.ePSXe.R;
import com.epsxe.ePSXe.jni.libepsxe;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.apache.http.client.methods.HttpGet;

/* loaded from: classes.dex */
public class DownloadCheatFileTask extends AsyncTask<String, Integer, String> {
    private Context context;

    /* renamed from: e */
    private libepsxe f191e;

    public DownloadCheatFileTask(Context context, libepsxe e) {
        this.context = context;
        this.f191e = e;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public String doInBackground(String... params) {
        return DownloadCheatcodesFile();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public void onProgressUpdate(Integer... progress) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public void onPostExecute(String result) {
        Toast.makeText(this.context, result, 1).show();
    }

    private String DownloadCheatcodesFile() {
        try {
            String country = this.f191e.getCode().substring(0, 4);
            URL url = new URL("http://epsxe.com/cheats/" + country + "/" + this.f191e.getCode() + ".txt");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(HttpGet.METHOD_NAME);
            urlConnection.setDoOutput(true);
            urlConnection.connect();
            File SDCardRoot = ContextCompat.getDataDir(context);
            File file = new File(SDCardRoot, "epsxe/cheats/" + this.f191e.getCode() + ".txt");
            FileOutputStream fileOutput = new FileOutputStream(file);
            InputStream inputStream = urlConnection.getInputStream();
            urlConnection.getContentLength();
            int downloadedSize = 0;
            byte[] buffer = new byte[1024];
            while (true) {
                int bufferLength = inputStream.read(buffer);
                if (bufferLength > 0) {
                    fileOutput.write(buffer, 0, bufferLength);
                    downloadedSize += bufferLength;
                } else {
                    fileOutput.close();
                    this.f191e.reloadAllGS();
                    return this.context.getString(R.string.cheat_dialog_downloaded);
                }
            }
        } catch (FileNotFoundException e) {
            return this.context.getString(R.string.cheat_dialog_notdownloaded);
        } catch (MalformedURLException e2) {
            e2.printStackTrace();
            return "Error malformed URL!";
        } catch (IOException e3) {
            e3.printStackTrace();
            return this.context.getString(R.string.cheat_dialog_notdownloadednow);
        }
    }
}
