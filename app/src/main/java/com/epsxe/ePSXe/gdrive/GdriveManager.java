package com.epsxe.ePSXe.gdrive;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.epsxe.ePSXe.FileChooser;
import com.epsxe.ePSXe.R;
import com.epsxe.ePSXe.ePSXePreferences;
import com.epsxe.ePSXe.ePSXeReadPreferences;
import com.epsxe.ePSXe.util.DeviceUtil;
import com.epsxe.ePSXe.util.DialogUtil;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataBuffer;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public class GdriveManager extends ListActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final String ACCESS_TOKEN = "ACCESS_TOKEN";
    private static final String ACCOUNT_PREFS_NAME = "prefs";
    private static final String APP_KEY = "";
    private static final int REQUEST_CODE_RESOLUTION = 3;
    private static final String TAG = "gdrive";
    private static final String USER_ID = "USER_ID";
    GdriveArrayAdapter adapter;
    private Button button_download;
    private Button button_upload;
    private GoogleApiClient client;
    private GoogleApiClient mGoogleApiClient;
    private ePSXeReadPreferences mePSXeReadPreferences;
    private AlertDialog pluginAlert;
    private ProgressDialog progressDialog;
    private boolean mLoggedIn = false;
    private String code = "";
    private String psexe = "";
    private String fromActivity = "FileChooser";
    List<OptionGdrive> dir = new ArrayList();
    private int hlebios = 0;
    private final ResultCallback<DriveApi.DriveContentsResult> driveContentsCallback = new ResultCallback<DriveApi.DriveContentsResult>() { // from class: com.epsxe.ePSXe.gdrive.GdriveManager.3
        @Override // com.google.android.gms.common.api.ResultCallback
        public void onResult(@NonNull DriveApi.DriveContentsResult result) {
            if (!result.getStatus().isSuccess()) {
                Log.w(GdriveManager.TAG, "Error while opening the file contents");
            } else {
                Log.i(GdriveManager.TAG, "File contents opened");
            }
        }
    };

    @Override // android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int currentOrientation = getResources().getConfiguration().orientation;
        if (currentOrientation == 2) {
            setRequestedOrientation(6);
        } else {
            setRequestedOrientation(7);
        }
        if (this.mePSXeReadPreferences == null) {
            this.mePSXeReadPreferences = new ePSXeReadPreferences(this);
        }
        this.hlebios = this.mePSXeReadPreferences.getBiosHle();
        if (this.hlebios == 2) {
            File bios = new File(this.mePSXeReadPreferences.getBios());
            if (bios.exists() && bios.length() == 524288) {
                this.hlebios = 0;
            } else {
                this.hlebios = 1;
            }
        }
        String param = getIntent().getStringExtra("com.epsxe.ePSXe.psexe");
        if (param != null && param.length() > 0) {
            this.code = param;
        }
        String param2 = getIntent().getStringExtra("com.epsxe.ePSXe.activity");
        if (param2 != null && param2.length() > 0) {
            this.fromActivity = param2;
        }
        this.client = new GoogleApiClient.Builder(this).build();
        int osVersion = Integer.parseInt(Build.VERSION.SDK);
        if (osVersion < 11) {
            setContentView(R.layout.list_viewsdropbox);
            add_quickButtons(this.adapter);
        } else {
            setContentView(R.layout.list_viewsdropboxv11);
        }
        if (this.fromActivity.equals("ePSXePreferences")) {
            memcardsLoad(0, this.mePSXeReadPreferences.getMemcard1(), this.dir);
            memcardsLoad(1, this.mePSXeReadPreferences.getMemcard2(), this.dir);
        } else {
            this.psexe = this.code.substring(0, 4) + "_" + this.code.substring(5, 8) + "." + this.code.substring(8, 10);
            savestatesLoad(this.dir);
            gamememcardsLoad(this.dir);
        }
        this.adapter = new GdriveArrayAdapter(this, R.layout.file_viewdropbox, this.dir);
        setListAdapter(this.adapter);
    }

    @Override // com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "GoogleApiClient connection failed: " + result.toString());
        if (!result.hasResolution()) {
            GoogleApiAvailability.getInstance().getErrorDialog(this, result.getErrorCode(), 0).show();
            return;
        }
        try {
            result.startResolutionForResult(this, 3);
        } catch (IntentSender.SendIntentException e) {
            Log.e(TAG, "Exception while starting resolution activity", e);
        }
    }

    @Override // com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks
    public void onConnected(Bundle connectionHint) {
        Log.i(TAG, "API client connected.");
        Drive.DriveApi.requestSync(this.mGoogleApiClient).setResultCallback(new ResultCallback<Status>() { // from class: com.epsxe.ePSXe.gdrive.GdriveManager.1
            @Override // com.google.android.gms.common.api.ResultCallback
            public void onResult(@NonNull Status status) {
                GdriveManager.this.listFilesFromDrive();
            }
        });
    }

    @Override // com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks
    public void onConnectionSuspended(int cause) {
    }

    void savestatesLoad(List<OptionGdrive> mdir) {
        File extStore = ContextCompat.getDataDir(this);
        for (int i = 0; i < 5; i++) {
            String sname = this.hlebios == 1 ? extStore.getAbsolutePath() + "/epsxe/sstates/" + this.psexe + "HLE.00" + i : extStore.getAbsolutePath() + "/epsxe/sstates/" + this.psexe + ".00" + i;
            File ff = new File(sname);
            if (ff.exists()) {
                Long lastModified = Long.valueOf(ff.lastModified());
                Date date = new Date(lastModified.longValue());
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                String sdate = sdf.format(date);
                if (this.hlebios == 1) {
                    mdir.add(new OptionGdrive(sname, this.psexe + "HLE.00" + i, sdate, "Unknown"));
                } else {
                    mdir.add(new OptionGdrive(sname, this.psexe + ".00" + i, sdate, "Unknown"));
                }
            } else if (this.hlebios == 1) {
                mdir.add(new OptionGdrive(sname, this.psexe + "HLE.00" + i, "Not found", "Unknown"));
            } else {
                mdir.add(new OptionGdrive(sname, this.psexe + ".00" + i, "Not found", "Unknown"));
            }
        }
    }

    void gamememcardsLoad(List<OptionGdrive> mdir) {
        File extStore = ContextCompat.getDataDir(this);
        for (int i = 0; i < 2; i++) {
            String sname = extStore.getAbsolutePath() + "/epsxe/memcards/games/" + this.psexe + "-0" + i + ".mcr";
            File ff = new File(sname);
            if (ff.exists()) {
                Long lastModified = Long.valueOf(ff.lastModified());
                Date date = new Date(lastModified.longValue());
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                String sdate = sdf.format(date);
                mdir.add(new OptionGdrive(sname, this.psexe + "-0" + i + ".mcr", sdate, "Unknown"));
            } else {
                mdir.add(new OptionGdrive(sname, this.psexe + "-0" + i + ".mcr", "Not found", "Unknown"));
            }
        }
    }

    void memcardsLoad(int n, String sname, List<OptionGdrive> mdir) {
        String name = sname;
        if (name.equals("default")) {
            name = ContextCompat.getDataDir(this) + "/epsxe/memcards/epsxe00" + n + ".mcr";
        }
        int index = name.lastIndexOf("/");
        String fileName = name.substring(index + 1);
        File ff = new File(name);
        if (ff.exists()) {
            Long lastModified = Long.valueOf(ff.lastModified());
            Date date = new Date(lastModified.longValue());
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            String sdate = sdf.format(date);
            mdir.add(new OptionGdrive(name, fileName, sdate, "Unknown"));
            return;
        }
        mdir.add(new OptionGdrive(name, fileName, "Not found", "Unknown"));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void listFilesFromDrive() {
        Drive.DriveApi.getAppFolder(this.mGoogleApiClient).listChildren(this.mGoogleApiClient).setResultCallback(new ResultCallback<DriveApi.MetadataBufferResult>() { // from class: com.epsxe.ePSXe.gdrive.GdriveManager.2
            @Override // com.google.android.gms.common.api.ResultCallback
            public void onResult(@NonNull DriveApi.MetadataBufferResult result) {
                if (!result.getStatus().isSuccess()) {
                    Log.v(GdriveManager.TAG, "Request failed");
                    return;
                }
                MetadataBuffer metadataBuffer = result.getMetadataBuffer();
                Log.i(GdriveManager.TAG, "elementos = " + metadataBuffer.getCount());
                if (metadataBuffer.getCount() > 0) {
                    DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                    for (OptionGdrive o : GdriveManager.this.dir) {
                        boolean found = false;
                        Iterator<Metadata> metadataIterator = result.getMetadataBuffer().iterator();
                        while (true) {
                            if (!metadataIterator.hasNext()) {
                                break;
                            }
                            Metadata element = metadataIterator.next();
                            Log.i(GdriveManager.TAG, element.getTitle());
                            String cDate = df.format(element.getModifiedDate());
                            Log.i(GdriveManager.TAG, cDate);
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
                } else {
                    Iterator<OptionGdrive> it = GdriveManager.this.dir.iterator();
                    while (it.hasNext()) {
                        it.next().setRemote("Not found");
                    }
                }
                GdriveManager.this.adapter.notifyDataSetChanged();
            }
        });
    }

    @Override // android.app.Activity
    protected void onResume() {
        super.onResume();
        if (this.mGoogleApiClient == null) {
            this.mGoogleApiClient = new GoogleApiClient.Builder(this).addApi(Drive.API).addScope(Drive.SCOPE_APPFOLDER).addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();
        }
        this.mGoogleApiClient.connect();
    }

    @Override // android.app.Activity
    protected void onPause() {
        if (this.mGoogleApiClient != null) {
            this.mGoogleApiClient.disconnect();
        }
        super.onPause();
    }

    @Override // android.app.ListActivity
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        ViewGroup row = (ViewGroup) v;
        CheckBox check = (CheckBox) row.findViewById(R.id.checkbox);
        check.toggle();
    }

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        int osVersion = Integer.parseInt(Build.VERSION.SDK);
        if (osVersion < 11) {
            return super.onCreateOptionsMenu(menu);
        }
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dropbox_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override // android.app.Activity
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_download /* 2131034121 */:
                action_download(this.adapter);
                return true;
            case R.id.action_upload /* 2131034129 */:
                action_upload(this.adapter);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String[] get_selected_files() {
        List<String> myList = new ArrayList<>();
        try {
            ListView l = getListView();
            int count = l.getCount();
            for (int i = 0; i < count; i++) {
                ViewGroup row = (ViewGroup) l.getChildAt(i);
                CheckBox check = (CheckBox) row.findViewById(R.id.checkbox);
                if (check.isChecked()) {
                    TextView text = (TextView) row.findViewById(R.id.TextView01);
                    String name = text.getText().toString();
                    for (OptionGdrive o : this.dir) {
                        if (o.getName().equals(name)) {
                            myList.add(o.getFile());
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.e("epsxegdrive", "unable to get selected files");
        }
        return (String[]) myList.toArray(new String[myList.size()]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void action_upload(final GdriveArrayAdapter adapter) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Upload Selected Files");
        alertDialog.setMessage("Do you want to upload to Google Drive?");
        alertDialog.setButton2("Yes", new DialogInterface.OnClickListener() { // from class: com.epsxe.ePSXe.gdrive.GdriveManager.4
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                String[] arr = GdriveManager.this.get_selected_files();
                if (arr.length > 0) {
                    GdriveUploadTask upload = new GdriveUploadTask(GdriveManager.this, GdriveManager.this.mGoogleApiClient, "/" + GdriveManager.this.code + "/", arr, GdriveManager.this.dir, adapter);
                    upload.execute(new Void[0]);
                }
            }
        });
        alertDialog.setButton("No", new DialogInterface.OnClickListener() { // from class: com.epsxe.ePSXe.gdrive.GdriveManager.5
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertDialog.setIcon(android.R.drawable.ic_menu_directions);
        alertDialog.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void action_download(final GdriveArrayAdapter adapter) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Download Selected Files");
        alertDialog.setMessage("Do you want to download from Google Drive?");
        alertDialog.setButton2("Yes", new DialogInterface.OnClickListener() { // from class: com.epsxe.ePSXe.gdrive.GdriveManager.6
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                String[] arr = GdriveManager.this.get_selected_files();
                if (arr.length > 0) {
                    GdriveDownloadTask download = new GdriveDownloadTask(GdriveManager.this, GdriveManager.this.mGoogleApiClient, "/" + GdriveManager.this.code + "/", arr, GdriveManager.this.dir, adapter);
                    download.execute(new Void[0]);
                }
            }
        });
        alertDialog.setButton("No", new DialogInterface.OnClickListener() { // from class: com.epsxe.ePSXe.gdrive.GdriveManager.7
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertDialog.setIcon(android.R.drawable.ic_menu_directions);
        alertDialog.show();
    }

    private void add_quickButtons(final GdriveArrayAdapter adapter) {
        this.button_upload = (Button) findViewById(R.id.button1);
        this.button_upload.setOnClickListener(new View.OnClickListener() { // from class: com.epsxe.ePSXe.gdrive.GdriveManager.8
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                GdriveManager.this.action_upload(adapter);
            }
        });
        this.button_download = (Button) findViewById(R.id.button2);
        this.button_download.setOnClickListener(new View.OnClickListener() { // from class: com.epsxe.ePSXe.gdrive.GdriveManager.9
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                GdriveManager.this.action_download(adapter);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doback() {
        Intent myIntent;
        Toast.makeText(this, R.string.file_back, 0).show();
        if (this.fromActivity.equals("ePSXePreferences")) {
            myIntent = new Intent(this, (Class<?>) ePSXePreferences.class);
            myIntent.putExtra("com.epsxe.ePSXe.screen", "memcardpreferences");
        } else {
            myIntent = new Intent(this, (Class<?>) FileChooser.class);
            myIntent.putExtra("com.epsxe.ePSXe.fcMode", "SELECT_GDRIVE");
        }
        startActivity(myIntent);
        setRequestedOrientation(4);
        finish();
    }

    private void alertdialog_doback(Context mCont) {
        final GdriveArrayAdapter mAdapter = this.adapter;
        String[] items = {"Upload to Gdrive", "Download from Gdrive", "Back"};
        ListView gListView = new ListView(mCont);
        ArrayAdapter<String> ladapter = new ArrayAdapter<>(mCont, android.R.layout.simple_list_item_1, items);
        gListView.setAdapter((ListAdapter) ladapter);
        gListView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.epsxe.ePSXe.gdrive.GdriveManager.10
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> listView, View itemView, int position, long itemId) {
                switch (position) {
                    case 0:
                        GdriveManager.this.action_upload(mAdapter);
                        break;
                    case 1:
                        GdriveManager.this.action_download(mAdapter);
                        break;
                    case 2:
                        GdriveManager.this.doback();
                        break;
                }
                DialogUtil.closeDialog(GdriveManager.this.pluginAlert);
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(mCont);
        builder.setView(gListView);
        this.pluginAlert = builder.create();
        this.pluginAlert.show();
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4 && event.getRepeatCount() == 0) {
            boolean isAndroidTV = DeviceUtil.isAndroidTV(this);
            if (isAndroidTV) {
                alertdialog_doback(this);
            } else {
                doback();
            }
        }
        return super.onKeyDown(keyCode, event);
    }



    @Override // android.app.Activity
    public void onStart() {
        super.onStart();
        this.client.connect();
    }

    @Override // android.app.Activity
    public void onStop() {
        super.onStop();
        this.client.disconnect();
    }
}
