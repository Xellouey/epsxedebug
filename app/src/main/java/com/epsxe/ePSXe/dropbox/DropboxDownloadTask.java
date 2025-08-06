package com.epsxe.ePSXe.dropbox;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.widget.Toast;
import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.epsxe.ePSXe.util.DialogUtil;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public class DropboxDownloadTask extends AsyncTask<Void, Long, Boolean> {
    private DropboxArrayAdapter adapter;
    private List<OptionDropbox> dir;
    private boolean mCanceled;
    private Context mContext;
    private DbxClientV2 mDbxClient;
    private final ProgressDialog mDialog;
    private String mErrorMsg;
    private String[] mFiles;
    private FileOutputStream mFos;
    private String mPath;
    List<MetaDropbox> dFiles = new ArrayList();
    private long mFileLenTotal = 0;
    private long mFileLenCurrent = 0;

    public DropboxDownloadTask(Context context, DbxClientV2 api, String dropboxPath, String[] files, List<OptionDropbox> ldir, DropboxArrayAdapter ladapter) {
        this.dir = new ArrayList();
        this.mContext = context.getApplicationContext();
        this.dir = ldir;
        this.adapter = ladapter;
        this.mDbxClient = api;
        this.mPath = dropboxPath;
        this.mFiles = files;
        this.mDialog = new ProgressDialog(context);
        this.mDialog.setMessage("Downloading Files");
        this.mDialog.setMax(100);
        this.mDialog.setProgressStyle(1);
        this.mDialog.setProgress(0);
        this.mDialog.setButton(-1, "Cancel", new DialogInterface.OnClickListener() { // from class: com.epsxe.ePSXe.dropbox.DropboxDownloadTask.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                DropboxDownloadTask.this.mCanceled = true;
                DropboxDownloadTask.this.mErrorMsg = "Canceled";
                if (DropboxDownloadTask.this.mFos != null) {
                    try {
                        DropboxDownloadTask.this.mFos.close();
                    } catch (IOException e) {
                    }
                }
            }
        });
        this.mDialog.show();
    }

    private boolean getFileList() {
        try {
            ListFolderResult folder = this.mDbxClient != null ? this.mDbxClient.files().listFolder(this.mPath) : null;
            if (folder == null) {
                this.mErrorMsg = "File or empty directory";
                return false;
            }
            List<Metadata> files = folder.getEntries();
            for (Metadata ent : files) {
                for (int i = 0; i < this.mFiles.length; i++) {
                    int index = this.mFiles[i].lastIndexOf("/");
                    String fileName = this.mFiles[i].substring(index + 1);
                    if (ent.getName().equals(fileName)) {
                        if (ent instanceof FileMetadata) {
                            this.dFiles.add(new MetaDropbox(this.mFiles[i], (FileMetadata) ent));
                            this.mFileLenTotal += ((FileMetadata) ent).getSize();
                        }
                    } else if (ent.getName().equals(fileName + ".pic") && (ent instanceof FileMetadata)) {
                        this.dFiles.add(new MetaDropbox(this.mFiles[i] + ".pic", (FileMetadata) ent));
                        this.mFileLenTotal += ((FileMetadata) ent).getSize();
                    }
                }
            }
            return true;
        } catch (DbxException e) {
            this.mErrorMsg = "Unknown error.  Try again.";
            return false;
        }
    }

    private void refreshData() {
        for (int i = 0; i < this.dFiles.size(); i++) {
            try {
                MetaDropbox metadropbox = this.dFiles.get(i);
                String fullfileName = metadropbox.getFilename();
                Iterator<OptionDropbox> it = this.dir.iterator();
                while (true) {
                    if (it.hasNext()) {
                        OptionDropbox o = it.next();
                        if (o.getFile().equals(fullfileName)) {
                            File ff = new File(fullfileName);
                            if (ff.exists()) {
                                Long lastModified = Long.valueOf(ff.lastModified());
                                Date date = new Date(lastModified.longValue());
                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                                String sdate = sdf.format(date);
                                o.setLocal(sdate);
                            } else {
                                o.setLocal("Not found");
                            }
                        }
                    }
                }
            } catch (Exception e) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public Boolean doInBackground(Void... params) {
        if (!this.mCanceled && getFileList() && !this.mCanceled) {
            for (int i = 0; i < this.dFiles.size(); i++) {
                try {
                    MetaDropbox metadropbox = this.dFiles.get(i);
                    String fullfileName = metadropbox.getFilename();
                    FileMetadata metadata = metadropbox.getMetadata();
                    int index = fullfileName.lastIndexOf("/");
                    fullfileName.substring(index + 1);
                    try {
                        this.mFos = new FileOutputStream(fullfileName);
                        if (this.mDbxClient != null) {
                            this.mDbxClient.files().download(metadata.getPathLower(), metadata.getRev()).download(this.mFos);
                        }
                        this.mFileLenCurrent += metadata.getSize();
                        if (this.mCanceled) {
                            return false;
                        }
                    } catch (IOException e) {
                        this.mErrorMsg = "Couldn't create a local file to store the image";
                        return false;
                    }
                } catch (DbxException e2) {
                    this.mErrorMsg = "Dropbox error.  Try again.";
                    return false;
                }
            }
            return true;
        }
        return false;
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
        if (!result.booleanValue()) {
            showToast(this.mErrorMsg);
        } else {
            refreshData();
            this.adapter.notifyDataSetChanged();
        }
    }

    private void showToast(String msg) {
        Toast error = Toast.makeText(this.mContext, msg, 1);
        error.show();
    }
}
