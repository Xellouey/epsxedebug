package com.epsxe.ePSXe.gdrive;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import com.epsxe.ePSXe.util.DialogUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataBuffer;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public class GdriveDownloadTask extends AsyncTask<Void, Long, Boolean> {
    private static final String TAG = "gdrive";
    private GdriveArrayAdapter adapter;
    private List<OptionGdrive> dir;
    private MetadataBuffer dirent;
    private boolean mCanceled;
    private Context mContext;
    private final ProgressDialog mDialog;
    private String mErrorMsg;
    private String[] mFiles;
    private FileOutputStream mFos;
    private GoogleApiClient mGoogleApiClient;
    private String mPath;
    List<MetaGdrive> dFiles = new ArrayList();
    private long mFileLenTotal = 0;
    private long mFileLenCurrent = 0;

    public GdriveDownloadTask(Context context, GoogleApiClient api, String gdrivePath, String[] files, List<OptionGdrive> ldir, GdriveArrayAdapter ladapter) {
        this.dir = new ArrayList();
        this.mContext = context.getApplicationContext();
        this.dir = ldir;
        this.adapter = ladapter;
        this.mGoogleApiClient = api;
        this.mPath = gdrivePath;
        this.mFiles = files;
        this.mDialog = new ProgressDialog(context);
        this.mDialog.setMessage("Downloading Files");
        this.mDialog.setMax(100);
        this.mDialog.setProgressStyle(1);
        this.mDialog.setProgress(0);
        this.mDialog.setButton(-1, "Cancel", new DialogInterface.OnClickListener() { // from class: com.epsxe.ePSXe.gdrive.GdriveDownloadTask.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                GdriveDownloadTask.this.mCanceled = true;
                GdriveDownloadTask.this.mErrorMsg = "Canceled";
                if (GdriveDownloadTask.this.mFos != null) {
                    try {
                        GdriveDownloadTask.this.mFos.close();
                    } catch (IOException e) {
                    }
                }
            }
        });
        this.mDialog.show();
    }

    private boolean getFileList() {
        if (this.dirent == null) {
            return false;
        }
        if (this.dirent.getCount() > 0) {
            Iterator<Metadata> metadataIterator = this.dirent.iterator();
            while (metadataIterator.hasNext()) {
                Metadata element = metadataIterator.next();
                for (int i = 0; i < this.mFiles.length; i++) {
                    int index = this.mFiles[i].lastIndexOf("/");
                    String fileName = this.mFiles[i].substring(index + 1);
                    if (element.getTitle().equals(fileName)) {
                        this.dFiles.add(new MetaGdrive(this.mFiles[i], element));
                        this.mFileLenTotal += element.getFileSize();
                    } else if (element.getTitle().equals(fileName + ".pic")) {
                        this.dFiles.add(new MetaGdrive(this.mFiles[i] + ".pic", element));
                        this.mFileLenTotal += element.getFileSize();
                    }
                }
            }
        }
        return true;
    }

    private Boolean GetMetadata() {
        boolean z;
        try {
            DriveApi.MetadataBufferResult result = Drive.DriveApi.getAppFolder(this.mGoogleApiClient).listChildren(this.mGoogleApiClient).await();
            if (!result.getStatus().isSuccess()) {
                z = false;
            } else {
                this.dirent = result.getMetadataBuffer();
                z = true;
            }
            return z;
        } catch (Exception e) {
            return false;
        }
    }

    private void RefreshMetadata() {
        if (this.dirent != null && this.dirent.getCount() > 0) {
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            for (OptionGdrive o : this.dir) {
                boolean found = false;
                Iterator<Metadata> metadataIterator = this.dirent.iterator();
                while (true) {
                    if (!metadataIterator.hasNext()) {
                        break;
                    }
                    Metadata element = metadataIterator.next();
                    df.format(element.getModifiedDate());
                    if (o.getName().equals(element.getTitle())) {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                        String sdate = sdf.format(element.getModifiedDate());
                        o.setRemote(sdate);
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    o.setRemote("Not found");
                }
            }
        }
    }

    private void refreshData() {
        for (int i = 0; i < this.dFiles.size(); i++) {
            try {
                MetaGdrive metagdrive = this.dFiles.get(i);
                String fullfileName = metagdrive.getFilename();
                Iterator<OptionGdrive> it = this.dir.iterator();
                while (true) {
                    if (it.hasNext()) {
                        OptionGdrive o = it.next();
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

    private void saveToFile(DriveContents driveContents, String filename) {
        try {
            byte[] bytes = new byte[8192];
            File localFile = new File(filename);
            FileOutputStream outputStream = new FileOutputStream(localFile, false);
            InputStream inputStream = driveContents.getInputStream();
            while (true) {
                int read = inputStream.read(bytes);
                if (read != -1) {
                    outputStream.write(bytes, 0, read);
                } else {
                    return;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void downloadFromDrive(DriveFile driveFile, String filename) {
        DriveFile.DownloadProgressListener listener = new DriveFile.DownloadProgressListener() { // from class: com.epsxe.ePSXe.gdrive.GdriveDownloadTask.2
            @Override // com.google.android.gms.drive.DriveFile.DownloadProgressListener
            public void onProgress(long bytesDownloaded, long bytesExpected) {
                GdriveDownloadTask.this.publishProgress(Long.valueOf(bytesDownloaded));
            }
        };
        DriveApi.DriveContentsResult result = driveFile.open(this.mGoogleApiClient, DriveFile.MODE_READ_ONLY, listener).await();
        if (!result.getStatus().isSuccess()) {
            Log.w(TAG, "Error while opening the file contents");
            return;
        }
        Log.i(TAG, "File contents opened");
        DriveContents driveContents = result.getDriveContents();
        saveToFile(driveContents, filename);
        driveContents.discard(this.mGoogleApiClient);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public Boolean doInBackground(Void... params) {
        if (!this.mCanceled && GetMetadata().booleanValue() && getFileList() && !this.mCanceled) {
            for (int i = 0; i < this.dFiles.size(); i++) {
                MetaGdrive metagdrive = this.dFiles.get(i);
                String fullfileName = metagdrive.getFilename();
                Metadata metadata = metagdrive.getMetadata();
                int index = fullfileName.lastIndexOf("/");
                fullfileName.substring(index + 1);
                downloadFromDrive(metadata.getDriveId().asDriveFile(), fullfileName);
                this.mFileLenCurrent += metadata.getFileSize();
                publishProgress(0L);
                if (this.mCanceled) {
                    return false;
                }
            }
            return GetMetadata().booleanValue();
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
