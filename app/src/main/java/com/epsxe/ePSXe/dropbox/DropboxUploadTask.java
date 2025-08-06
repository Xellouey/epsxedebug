package com.epsxe.ePSXe.dropbox;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.files.WriteMode;
import com.epsxe.ePSXe.util.DialogUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public class DropboxUploadTask extends AsyncTask<Void, Long, Boolean> {
    private DropboxArrayAdapter adapter;
    private List<OptionDropbox> dir;
    private ListFolderResult dirent;
    private Context mContext;
    private DbxClientV2 mDbxClient;
    private final ProgressDialog mDialog;
    private String mErrorMsg;
    private String mPath;
    List<String> mFiles = new ArrayList();
    private long mFileLenCurrent = 0;
    private long mFileLenTotal = 0;

    public DropboxUploadTask(Context context, DbxClientV2 api, String dropboxPath, String[] files, List<OptionDropbox> ldir, DropboxArrayAdapter ladapter) {
        this.dir = new ArrayList();
        this.mContext = context.getApplicationContext();
        this.dir = ldir;
        this.adapter = ladapter;
        for (int i = 0; i < files.length; i++) {
            File f1 = new File(files[i]);
            if (f1.exists()) {
                this.mFiles.add(files[i]);
                this.mFileLenTotal += f1.length();
                File f2 = new File(files[i] + ".pic");
                if (f2.exists()) {
                    this.mFiles.add(files[i] + ".pic");
                    this.mFileLenTotal += f2.length();
                }
            }
        }
        this.mDbxClient = api;
        this.mPath = dropboxPath;
        this.mDialog = new ProgressDialog(context);
        this.mDialog.setMax(100);
        this.mDialog.setMessage("Uploading " + this.mFiles.size() + " Files");
        this.mDialog.setProgressStyle(1);
        this.mDialog.setProgress(0);
        this.mDialog.setButton(-1, "Cancel", new DialogInterface.OnClickListener() { // from class: com.epsxe.ePSXe.dropbox.DropboxUploadTask.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        this.mDialog.show();
    }

    private int GetMetadata() {
        try {
            if (this.mDbxClient != null) {
                this.dirent = this.mDbxClient.files().listFolder(this.mPath);
            }
            return 0;
        } catch (DbxException e) {
            Log.e("epsxedropbox", "Unable to get dropbox metadata =" + e);
            return -1;
        }
    }

    private void RefreshMetadata() {
        if (this.dirent != null) {
            try {
                List<Metadata> files = this.dirent.getEntries();
                for (OptionDropbox o : this.dir) {
                    boolean found = false;
                    Iterator<Metadata> it = files.iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            break;
                        }
                        Metadata ent = it.next();
                        if (o.getName().equals(ent.getName())) {
                            if (ent instanceof FileMetadata) {
                                Date date = ((FileMetadata) ent).getServerModified();
                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                                String sdate = sdf.format(date);
                                o.setRemote(sdate);
                                found = true;
                            }
                        }
                    }
                    if (!found) {
                        o.setRemote("Not found");
                    }
                }
            } catch (Exception e) {
                Log.e("epsxedropbox", "Unable to get dropbox metadata =" + e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public Boolean doInBackground(Void... params) {
        for (int i = 0; i < this.mFiles.size(); i++) {
            File mFile = new File(this.mFiles.get(i));
            try {
                FileInputStream fis = new FileInputStream(mFile);
                String path = this.mPath + mFile.getName();
                if (this.mDbxClient != null) {
                    this.mDbxClient.files().uploadBuilder(path).withMode(WriteMode.OVERWRITE).uploadAndFinish(fis);
                }
                this.mFileLenCurrent += mFile.length();
            } catch (DbxException e) {
                this.mErrorMsg = "Dropbox error.  Try again.";
                return false;
            } catch (IOException e2) {
                this.mErrorMsg = "Dropbox error.  Try again.";
                return false;
            }
        }
        GetMetadata();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public void onProgressUpdate(Long... progress) {
        int percent = (int) (((100.0d * (progress[0].longValue() + this.mFileLenCurrent)) / this.mFileLenTotal) + 0.5d);
        this.mDialog.setProgress(percent);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public void onPostExecute(Boolean result) {
        DialogUtil.closeDialog(this.mDialog);
        try {
            if (result.booleanValue()) {
                RefreshMetadata();
                this.adapter.notifyDataSetChanged();
                showToast("State successfully uploaded");
            } else {
                showToast(this.mErrorMsg);
            }
        } catch (Exception e) {
        }
    }

    private void showToast(String msg) {
        Toast error = Toast.makeText(this.mContext, msg, 1);
        error.show();
    }
}
