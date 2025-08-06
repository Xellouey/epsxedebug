package com.epsxe.ePSXe.task;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;

import androidx.core.content.ContextCompat;
import androidx.core.internal.view.SupportMenu;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.widget.Toast;

import com.epsxe.ePSXe.InputList;
import com.epsxe.ePSXe.R;
import com.epsxe.ePSXe.ePSXe;
import com.epsxe.ePSXe.ePSXeNative;
import com.epsxe.ePSXe.util.DialogUtil;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.util.Arrays;

/* loaded from: classes.dex */
public class IndexECMTask extends AsyncTask<String, Integer, Integer> {
    private Activity activity;
    private Context context;
    ProgressDialog dialog;
    int emu_xperiaplay;
    String name;
    int serverMode;
    String slot;

    public IndexECMTask(Activity a, Context ctx, int sm, int xp) {
        this.emu_xperiaplay = 0;
        this.serverMode = 0;
        Log.e("IndexECMTask", "start");
        this.context = ctx;
        this.activity = a;
        this.emu_xperiaplay = xp;
        this.serverMode = sm;
        this.dialog = new ProgressDialog(this.context);
        this.dialog.setTitle(R.string.file_games_ecm);
        this.dialog.setProgressStyle(1);
        this.dialog.setMax(100);
        this.dialog.setProgress(0);
        this.dialog.setCancelable(false);
        this.dialog.show();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public Integer doInBackground(String... params) {
        this.name = params[1];
        this.slot = params[2];
        Log.e("IndexECMTask", "pre-index " + params[0]);
        int res = makeIndexECM(params[0]);
        return Integer.valueOf(res);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public void onPostExecute(Integer result) {
        Log.e("IndexECMTask", "end");
        if (result.intValue() == 0) {
            Intent myIntent = this.emu_xperiaplay == 1 ? new Intent(this.activity, (Class<?>) ePSXeNative.class) : new Intent(this.activity, (Class<?>) ePSXe.class);
            myIntent.putExtra("com.epsxe.ePSXe.isoName", this.name);
            myIntent.putExtra("com.epsxe.ePSXe.isoSlot", this.slot);
            myIntent.putExtra("com.epsxe.ePSXe.servermode", "" + this.serverMode);
            DialogUtil.closeDialog(this.dialog);
            this.activity.startActivity(myIntent);
            this.activity.finish();
            return;
        }
        DialogUtil.closeDialog(this.dialog);
        Toast.makeText(this.context, this.activity.getString(R.string.file_games_ecm_err), 0).show();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public void onProgressUpdate(Integer... progress) {
        this.dialog.setProgress(progress[0].intValue());
    }

    private int getSectorSize(String file) {
        byte[] header = new byte[InputList.KEYCODE_FORWARD_DEL];
        int size = -1;
        try {
            RandomAccessFile mFile = new RandomAccessFile(file, "r");
            mFile.seek(0L);
            mFile.read(header, 0, InputList.KEYCODE_FORWARD_DEL);
            int nentries = header[98] & 255;
            for (int ind = 0; ind < nentries; ind++) {
                byte[] block = new byte[80];
                mFile.read(block, 0, 80);
                if ((block[4] & 255) < 100) {
                    int ssize = (block[16] & 255) + ((block[17] & 255) * 256);
                    if (size == -1) {
                        size = ssize;
                    }
                    if (ssize != size) {
                        return -1;
                    }
                }
            }
            return size;
        } catch (Exception e) {
            return -1;
        }
    }

    private int makeIndexECM(String file) {
        byte[] bArr = new byte[2448];
        byte[] type = new byte[4];
        byte[] ecmtype = {69, 67, 77, 0};
        int sectorsize = 2352;
        int nsector = 0;
        int done = 1;
        int progress = 0;
        Log.e("epsxefolder", "ECM Indexing");
        if (file.toLowerCase().endsWith(".mdf.ecm")) {
            int res = getSectorSize(file.substring(0, file.lastIndexOf(46) - 1) + "s");
            if (res != -1) {
                sectorsize = res;
                if (sectorsize < 2048) {
                    sectorsize = 2352;
                }
            }
        }
        int pos = sectorsize;
        try {
            RandomAccessFile mFile = new RandomAccessFile(file, "r");
            mFile.seek(0L);
            mFile.read(type, 0, 4);
            if (!Arrays.equals(ecmtype, type)) {
                Log.e("epsxefolder", "Not ECM file");
                return -1;
            }
            long fsize = mFile.length() / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID;
            long fend = mFile.length();
            if (fsize == 0) {
                fsize = 1;
            }
            int fpos = 0 + 4;
            int[] ecmidx = new int[720000];
            mFile.seek(4L);
            while (true) {
                byte[] rtmp = {0};
                if (progress < ((int) (((fpos / 1024) * 100) / fsize))) {
                    progress = (int) (((fpos / 1024) * 100) / fsize);
                    publishProgress(Integer.valueOf(progress));
                }
                mFile.seek(fpos);
                mFile.read(rtmp);
                fpos++;
                int tmp = rtmp[0] & 255;
                int val = (tmp >> 2) & 31;
                int mtype = tmp & 3;
                for (int ind = 0; (tmp & 128) != 0 && ind < 4; ind++) {
                    mFile.read(rtmp);
                    fpos++;
                    tmp = rtmp[0] & 255;
                    val |= (tmp & 127) << ((ind * 7) + 5);
                }
                if (val == -1) {
                    Log.e("epsxefolder", "ECM exit:" + fpos);
                    return ISOSaveIdxECM(ecmidx, file, nsector);
                }
                if (fpos > fend) {
                    return -1;
                }
                if (done != 0) {
                    pos = 0;
                    ecmidx[nsector * 2] = fpos - 0;
                    ecmidx[(nsector * 2) + 1] = (mtype << 30) | (1073741823 & val);
                    nsector++;
                    done = 0;
                }
                int val2 = val + 1;
                if (mtype == 0) {
                    while (val2 > 0) {
                        int r = val2;
                        if (done != 0) {
                            pos = 0;
                            ecmidx[nsector * 2] = fpos - 0;
                            if (val2 > 0) {
                                ecmidx[(nsector * 2) + 1] = ((val2 - 1) & 1073741823) | 0;
                            } else {
                                ecmidx[(nsector * 2) + 1] = 0;
                            }
                            nsector++;
                            done = 0;
                        }
                        if (r >= sectorsize) {
                            r = sectorsize;
                        }
                        if (r + pos >= sectorsize) {
                            r = sectorsize - pos;
                        }
                        fpos += r;
                        val2 -= r;
                        pos += r;
                        if (pos == sectorsize) {
                            done = 1;
                        }
                    }
                } else {
                    while (val2 > 0) {
                        if (done != 0) {
                            pos -= sectorsize;
                            ecmidx[nsector * 2] = fpos;
                            if (val2 > 0) {
                                ecmidx[(nsector * 2) + 1] = (mtype << 30) | (val2 - 1);
                            } else {
                                ecmidx[(nsector * 2) + 1] = mtype << 30;
                            }
                            nsector++;
                            done = 0;
                        }
                        switch (mtype) {
                            case 1:
                                fpos = fpos + 3 + 2048;
                                pos += 2352;
                                break;
                            case 2:
                                if (pos > 16) {
                                    ecmidx[nsector * 2] = fpos;
                                    if (val2 > 0) {
                                        ecmidx[(nsector * 2) + 1] = (mtype << 30) | ((sectorsize - pos) << 16) | ((val2 - 1) & SupportMenu.USER_MASK);
                                    } else {
                                        ecmidx[(nsector * 2) + 1] = (mtype << 30) | ((sectorsize - pos) << 16);
                                    }
                                    nsector++;
                                    fpos += 2052;
                                    pos += 2336 - sectorsize;
                                    break;
                                } else {
                                    fpos += 2052;
                                    pos += 2336;
                                    break;
                                }
                            case 3:
                                fpos += 2328;
                                pos += 2336;
                                break;
                        }
                        if (pos >= sectorsize) {
                            done = 1;
                        }
                        val2--;
                    }
                }
            }
        } catch (Exception e) {
            return -1;
        }
    }

    private int ISOSaveIdxECM(int[] ints, String nfile, int nsector) {
        String fname;
        RandomAccessFile out;
        RandomAccessFile out2 = null;
        try {
            fname = ContextCompat.getDataDir(context) + "/epsxe/idx/" + nfile.replaceAll("/", "_").replaceAll("\\.", "_");
            Log.e("epsxefolder", "ECM idx file to save: " + fname);
            out = new RandomAccessFile(fname, "rw");
        } catch (IOException e) {
            e = e;
            return -1;
        }
        try {
            FileChannel file = out.getChannel();
            ByteBuffer buf = file.map(FileChannel.MapMode.READ_WRITE, 0L, nsector * 2 * 4);
            buf.order(ByteOrder.LITTLE_ENDIAN);
            for (int i = 0; i < nsector * 2; i++) {
                buf.putInt(ints[i]);
            }
            file.close();
            if (out != null) {
                out.close();
            }
            Log.e("epsxefolder", "ECM idx file saved: " + fname);
            return 0;
        } catch (IOException e2) {
            IOException e = e2;
            out2 = out;
            Log.e("epsxefolder", "ECM idx file save ERROR " + e);
            if (out2 != null) {
                try {
                    out2.close();
                } catch (IOException e3) {
                    return -1;
                }
            }
            return -1;
        }
    }
}
