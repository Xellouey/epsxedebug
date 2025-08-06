package com.epsxe.ePSXe.task;

import android.app.ProgressDialog;
import android.net.http.EventHandler;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import com.epsxe.ePSXe.ePSXe;
import com.epsxe.ePSXe.jni.libepsxe;
import com.epsxe.ePSXe.util.DeviceUtil;
import com.epsxe.ePSXe.util.DialogUtil;
import com.epsxe.ePSXe.util.PSXUtil;

/* loaded from: classes.dex */
public class MultiplayerServerTask extends AsyncTask<String, String, Integer> {
    private ePSXe activity;
    int cdata;
    String clientIP;
    ProgressDialog dialog;

    /* renamed from: e */
    libepsxe f193e;
    String iCode;
    String isoName;
    String md5;
    int rstatus;
    int serverMode;
    int slot;
    int status = 0;

    public MultiplayerServerTask(ePSXe act, libepsxe el, int servmod, String iName, int sl, int cd, String md) {
        this.slot = 0;
        this.cdata = 0;
        this.activity = act;
        this.f193e = el;
        this.serverMode = servmod;
        this.isoName = iName;
        this.slot = sl;
        this.cdata = cd;
        this.md5 = md;
        this.dialog = new ProgressDialog(this.activity);
        this.dialog.setTitle("Multiplayer Server");
        this.dialog.show();
        this.dialog.setCancelable(false);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public Integer doInBackground(String... params) {
        this.status = this.f193e.runServer(19999, this.serverMode);
        if (this.status < 0) {
            return -1;
        }
        publishProgress("Netplay is beta, feedback is welcome!\nServer IP: " + DeviceUtil.getLocalIpv4Address() + "\nwaiting for client...");
        Log.e("WIP", "Server IP: " + DeviceUtil.getLocalIpv4Address() + "\nwaiting for client...");
        this.clientIP = this.f193e.waitClientConnect();
        if (this.clientIP == null || this.clientIP.equals("")) {
            return -2;
        }
        this.iCode = PSXUtil.getPSXGameID(this.isoName, this.slot, 1);
        if (this.iCode == null || this.iCode.equals("")) {
            return -3;
        }
        publishProgress("connecting to client " + this.clientIP + " ...");
        Log.e("WIP", "connecting to client " + this.clientIP + " ...");
        this.status = this.f193e.runServerInputSender(this.clientIP, 20000);
        if (this.status < 0) {
            return -4;
        }
        publishProgress("sending info to client ... " + this.iCode);
        Log.e("WIP", "sending info to client ... " + this.iCode);
        this.status = this.f193e.sendGameInfo(this.iCode, this.md5.substring(0, 10), this.cdata);
        if (this.status != 0) {
            return -5;
        }
        publishProgress("waiting client to start ...");
        Log.e("WIP", "waiting client to start ...");
        this.status = this.f193e.waitClientOK();
        Log.e("WIP", "status real ..." + this.status);
        if ((this.status & 255) == 0) {
            return Integer.valueOf(this.status & 255);
        }
        this.rstatus = this.status;
        return -6;
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
            case EventHandler.ERROR_CONNECT /* -6 */:
                if ((this.rstatus & 255) != 255) {
                    if ((this.rstatus & 1) != 1) {
                        if ((this.rstatus & 2) != 2) {
                            if ((this.rstatus & 4) != 4) {
                                Toast.makeText(this.activity, "Not compatible, unknown cause, contact developers =" + (this.rstatus & 255), 1).show();
                                break;
                            } else {
                                Toast.makeText(this.activity, "PS1 Bios version is different", 1).show();
                                break;
                            }
                        } else {
                            Toast.makeText(this.activity, "ePSXe version different, no compatible", 1).show();
                            break;
                        }
                    } else {
                        Toast.makeText(this.activity, "Android device arch no compatibles", 1).show();
                        break;
                    }
                } else {
                    Toast.makeText(this.activity, "Timeout waiting client start", 1).show();
                    break;
                }
            case EventHandler.ERROR_PROXYAUTH /* -5 */:
                Toast.makeText(this.activity, "Error sending game info", 1).show();
                break;
            case EventHandler.ERROR_AUTH /* -4 */:
                Toast.makeText(this.activity, "Error connecting to client", 1).show();
                break;
            case EventHandler.ERROR_UNSUPPORTED_AUTH_SCHEME /* -3 */:
                Toast.makeText(this.activity, "Error getting game code", 1).show();
                break;
            case -2:
                Toast.makeText(this.activity, "Timeout waiting for client", 1).show();
                break;
            case -1:
                Toast.makeText(this.activity, "Error opening socket", 1).show();
                break;
            case 0:
                this.activity.runIsoServer(this.isoName, this.slot);
                break;
        }
    }
}
