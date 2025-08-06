package com.epsxe.ePSXe.task;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.net.http.EventHandler;
import android.os.AsyncTask;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.epsxe.ePSXe.ePSXe;
import com.epsxe.ePSXe.ePSXeApplication;
import com.epsxe.ePSXe.gamePathInfo;
import com.epsxe.ePSXe.jni.libepsxe;
import com.epsxe.ePSXe.util.DialogUtil;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/* loaded from: classes.dex */
public class MultiplayerClientTask extends AsyncTask<String, String, Integer> {
    public ePSXe activity;
    int cdata;
    ProgressDialog dialog;

    /* renamed from: e */
    libepsxe f192e;
    String md5;
    String serverIP;
    int serverMode;
    String isoName = "";
    String iCode = "";
    int slot = 0;
    int status = 0;
    int setup = 0;

    public MultiplayerClientTask(ePSXe act, libepsxe el, int servmod, String srvIP, int cd, String md5) {
        this.cdata = 0;
        this.activity = act;
        this.serverMode = servmod;
        this.f192e = el;
        this.serverIP = srvIP;
        this.cdata = cd;
        this.md5 = md5;
        this.dialog = new ProgressDialog(this.activity);
        this.dialog.setTitle("Multiplayer Client");
        this.dialog.show();
        this.dialog.setCancelable(false);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public Integer doInBackground(String... params) {
        publishProgress("Opening client conection...");
        Log.e("WIP", "Opening client conection...");
        this.status = this.f192e.runClientInputReceiver(20000);
        if (this.status < 0) {
            return -1;
        }
        publishProgress("Conecting to server " + this.serverIP + " ...");
        Log.e("WIP", "Conecting to server " + this.serverIP + " ...");
        this.status = this.f192e.runClient(this.serverIP, 19999, 1, this.serverMode);
        if (this.status < 0) {
            return -2;
        }
        publishProgress("Connected!. Waiting game info...");
        Log.e("WIP", "Connected!. Waiting game info...");
        this.iCode = this.f192e.waitGameInfo();
        if (this.iCode == null || this.iCode.equals("")) {
            return -3;
        }
        publishProgress("Got game info. Searching the game + " + this.iCode);
        Log.e("WIP", "Got game info. Searching the game + " + this.iCode);
        gamePathInfo gi = findGameFromCode(this.iCode);
        if (gi != null) {
            this.isoName = gi.getPath();
            this.slot = gi.getSlot();
        }
        if (this.isoName == null || this.isoName.equals("")) {
            return -4;
        }
        publishProgress("loading game and starting game ... " + this.isoName);
        Log.e("WIP", "loading game and starting game ... " + this.isoName);
        this.status = this.f192e.sendClientOK(this.md5.substring(0, 10), this.cdata);
        if ((this.status & 255) != 0) {
            return -5;
        }
        this.setup = this.status;
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public void onProgressUpdate(String... values) {
        this.dialog.setMessage(values[0]);
    }

    public void doProgress(String value) {
        publishProgress(value);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public void onPostExecute(Integer status) {
        Log.e("WIP", "onPostExecute status=" + status);
        DialogUtil.closeDialog(this.dialog);
        switch (status.intValue()) {
            case EventHandler.ERROR_PROXYAUTH /* -5 */:
                Toast.makeText(this.activity, "ePSXe version, bios, or device arch not compatible", 1).show();
                break;
            case EventHandler.ERROR_AUTH /* -4 */:
                Toast.makeText(this.activity, "Game not found in local storage", 1).show();
                break;
            case EventHandler.ERROR_UNSUPPORTED_AUTH_SCHEME /* -3 */:
                Toast.makeText(this.activity, "Game code not received", 1).show();
                break;
            case -2:
                Toast.makeText(this.activity, "Error connecting to server", 1).show();
                break;
            case -1:
                Toast.makeText(this.activity, "Error opening socket", 1).show();
                break;
            case 0:
                SharedPreferences sharedPreferences = ePSXeApplication.getDefaultSharedPreferences(this.activity);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("netplayserver", this.serverIP);
                editor.commit();
                this.activity.runIsoClient(this.isoName, this.slot, this.setup);
                break;
        }
    }

    private gamePathInfo findGameFromCode(String mcode) {
        String code = null;
        String path = null;
        int slot = 0;
        try {
            File f = new File(ContextCompat.getDataDir(activity), "epsxe/info/gamelistv2");
            if (f != null) {
                BufferedReader r = new BufferedReader(new FileReader(f));
                new StringBuilder();
                do {
                    String line = r.readLine();
                    if (line != null) {
                        String[] tokens = line.split(";");
                        code = tokens[2];
                        path = tokens[6];
                        slot = Integer.parseInt(tokens[10]);
                    }
                } while (code == null || !code.equals(mcode));
                return new gamePathInfo(slot, path);
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}
