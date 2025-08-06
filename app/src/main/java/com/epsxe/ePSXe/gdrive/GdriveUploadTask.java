package com.epsxe.ePSXe.gdrive;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import com.epsxe.ePSXe.util.DialogUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataBuffer;
import com.google.android.gms.drive.MetadataChangeSet;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public class GdriveUploadTask extends AsyncTask<Void, Long, Boolean> {
    private static final String TAG = "gdrive";
    private GdriveArrayAdapter adapter;
    private List<OptionGdrive> dir;
    private MetadataBuffer dirent;
    private Context mContext;
    private final ProgressDialog mDialog;
    private String mErrorMsg;
    private GoogleApiClient mGoogleApiClient;
    private String mPath;
    List<String> mFiles = new ArrayList();
    private long mFileLenCurrent = 0;
    private long mFileLenTotal = 0;

    public GdriveUploadTask(Context context, GoogleApiClient api, String gdrivePath, String[] files, List<OptionGdrive> ldir, GdriveArrayAdapter ladapter) {
        this.dir = new ArrayList();
        this.mContext = context.getApplicationContext();
        this.mGoogleApiClient = api;
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
        this.mPath = gdrivePath;
        this.mDialog = new ProgressDialog(context);
        this.mDialog.setMax(100);
        this.mDialog.setMessage("Uploading " + this.mFiles.size() + " Files");
        this.mDialog.setProgressStyle(1);
        this.mDialog.setProgress(0);
        this.mDialog.setButton(-1, "Cancel", new DialogInterface.OnClickListener() { // from class: com.epsxe.ePSXe.gdrive.GdriveUploadTask.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        this.mDialog.show();
    }

    private Boolean GetMetadata() {
        boolean z;
        try {
            DriveApi.MetadataBufferResult result = Drive.DriveApi.getAppFolder(this.mGoogleApiClient).listChildren(this.mGoogleApiClient).await();
            if (!result.getStatus().isSuccess()) {
                this.dirent = null;
                z = false;
            } else {
                this.dirent = result.getMetadataBuffer();
                z = true;
            }
            return z;
        } catch (Exception e) {
            this.dirent = null;
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

    private Boolean deleteFromDrive(DriveFile driveFile, String name) {
        com.google.android.gms.common.api.Status status = driveFile.delete(this.mGoogleApiClient).await();
        if (!status.isSuccess()) {
            return false;
        }
        Log.i(TAG, "Delete duplicated file=" + name);
        return true;
    }

    private Boolean DeleteDuplicateEntry(String name) {
        if (this.dirent == null) {
            return true;
        }
        if (this.dirent.getCount() > 0) {
            Iterator<Metadata> metadataIterator = this.dirent.iterator();
            while (metadataIterator.hasNext()) {
                Metadata element = metadataIterator.next();
                Log.i(TAG, element.getTitle());
                if (name.equals(element.getTitle())) {
                    return deleteFromDrive(element.getDriveId().asDriveFile(), name);
                }
            }
        }
        return true;
    }

    private void saveFileToOutputStream(String path, OutputStream outputStream) {
        try {
            File localFile = new File(path);
            byte[] buf = new byte[8192];
            InputStream is = new FileInputStream(localFile);
            while (true) {
                int c = is.read(buf, 0, buf.length);
                if (c > 0) {
                    outputStream.write(buf, 0, c);
                    outputStream.flush();
                } else {
                    return;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Boolean uploadFileToDrive(String path, String name) {
        DriveApi.DriveContentsResult result = Drive.DriveApi.newDriveContents(this.mGoogleApiClient).await();
        if (!result.getStatus().isSuccess()) {
            return false;
        }
        OutputStream outputStream = result.getDriveContents().getOutputStream();
        saveFileToOutputStream(path, outputStream);
        MetadataChangeSet changeSet = new MetadataChangeSet.Builder().setTitle(name).setMimeType("application/octet-stream").build();
        Drive.DriveApi.getAppFolder(this.mGoogleApiClient).createFile(this.mGoogleApiClient, changeSet, result.getDriveContents()).await();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public Boolean doInBackground(Void... params) {
        if (!GetMetadata().booleanValue()) {
            return false;
        }
        for (int i = 0; i < this.mFiles.size(); i++) {
            File mFile = new File(this.mFiles.get(i));
            Log.e(TAG, "Upload file Path=" + mFile.getPath());
            if (DeleteDuplicateEntry(mFile.getName()).booleanValue() && uploadFileToDrive(mFile.getPath(), mFile.getName()).booleanValue()) {
                Log.e(TAG, "Done");
                this.mFileLenCurrent += mFile.length();
                publishProgress(0L);
            }
            return false;
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
