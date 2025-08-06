package com.epsxe.ePSXe.dropbox;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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

import androidx.core.content.ContextCompat;

import com.dropbox.core.DbxException;
import com.dropbox.core.android.Auth;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.epsxe.ePSXe.FileChooser;
import com.epsxe.ePSXe.R;
import com.epsxe.ePSXe.ePSXePreferences;
import com.epsxe.ePSXe.ePSXeReadPreferences;
import com.epsxe.ePSXe.util.DeviceUtil;
import com.epsxe.ePSXe.util.DialogUtil;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public class DropboxManager extends ListActivity {
    private static final String ACCESS_TOKEN = "ACCESS_TOKEN";
    private static final String ACCOUNT_PREFS_NAME = "prefs";
    private static final String APP_KEY = "cxvfhfiiayajq4v";
    private static final String TAG = "epsxe";
    private static final String USER_ID = "USER_ID";
    DropboxArrayAdapter adapter;
    private Button button_download;
    private Button button_upload;
    private ePSXeReadPreferences mePSXeReadPreferences;
    private AlertDialog pluginAlert;
    private DbxClientV2 dbxClient = null;
    private boolean mLoggedIn = false;
    private String code = "";
    private String psexe = "";
    private String fromActivity = "FileChooser";
    List<OptionDropbox> dir = new ArrayList();
    private int hlebios = 0;

    @Override // android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int currentOrientation = getResources().getConfiguration().orientation;
        if (currentOrientation == 2) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
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
        DropboxInit();
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
        this.adapter = new DropboxArrayAdapter(this, R.layout.file_viewdropbox, this.dir);
        setListAdapter(this.adapter);
    }

    void savestatesLoad(List<OptionDropbox> mdir) {
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
                    mdir.add(new OptionDropbox(sname, this.psexe + "HLE.00" + i, sdate, "Unknown"));
                } else {
                    mdir.add(new OptionDropbox(sname, this.psexe + ".00" + i, sdate, "Unknown"));
                }
            } else if (this.hlebios == 1) {
                mdir.add(new OptionDropbox(sname, this.psexe + "HLE.00" + i, "Not found", "Unknown"));
            } else {
                mdir.add(new OptionDropbox(sname, this.psexe + ".00" + i, "Not found", "Unknown"));
            }
        }
    }

    void gamememcardsLoad(List<OptionDropbox> mdir) {
        File extStore = ContextCompat.getDataDir(this);
        for (int i = 0; i < 2; i++) {
            String sname = extStore.getAbsolutePath() + "/epsxe/memcards/games/" + this.psexe + "-0" + i + ".mcr";
            File ff = new File(sname);
            if (ff.exists()) {
                Long lastModified = Long.valueOf(ff.lastModified());
                Date date = new Date(lastModified.longValue());
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                String sdate = sdf.format(date);
                mdir.add(new OptionDropbox(sname, this.psexe + "-0" + i + ".mcr", sdate, "Unknown"));
            } else {
                mdir.add(new OptionDropbox(sname, this.psexe + "-0" + i + ".mcr", "Not found", "Unknown"));
            }
        }
    }

    void memcardsLoad(int n, String sname, List<OptionDropbox> mdir) {
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
            mdir.add(new OptionDropbox(name, fileName, sdate, "Unknown"));
            return;
        }
        mdir.add(new OptionDropbox(name, fileName, "Not found", "Unknown"));
    }

    private class DropboxMetadataTask extends AsyncTask<String, String, Integer> {
        private DropboxArrayAdapter mAdapter;
        private DbxClientV2 mDbxClient;

        public DropboxMetadataTask(DropboxArrayAdapter adapter, DbxClientV2 api) {
            Log.e("DropboxMetadata", "start");
            this.mAdapter = adapter;
            this.mDbxClient = api;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public Integer doInBackground(String... params) {
            return Integer.valueOf(GetMetadata(params[0]));
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(Integer result) {
            try {
                this.mAdapter.notifyDataSetChanged();
            } catch (Exception e) {
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onProgressUpdate(String... progress) {
        }

        private int GetMetadata(String code) {
            try {
                ListFolderResult dirent = this.mDbxClient.files().listFolder("/" + code + "/");
                if (dirent == null) {
                    Iterator<OptionDropbox> it = DropboxManager.this.dir.iterator();
                    while (it.hasNext()) {
                        it.next().setRemote("Not found");
                    }
                    return 0;
                }
                List<Metadata> files = dirent.getEntries();
                for (OptionDropbox o : DropboxManager.this.dir) {
                    boolean found = false;
                    Iterator<Metadata> it2 = files.iterator();
                    while (true) {
                        if (!it2.hasNext()) {
                            break;
                        }
                        Metadata ent = it2.next();
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
                return 0;
            } catch (DbxException e) {
                Log.e("epsxedropbox", "Unable to get dropbox metadata =" + e);
                return -1;
            }
        }
    }

    @Override // android.app.Activity
    protected void onResume() {
        super.onResume();
        DropboxResume();
        this.dbxClient = DropboxClientFactory.getClient();
        if (this.dbxClient != null) {
            new DropboxMetadataTask(this.adapter, this.dbxClient).execute("/" + this.code + "/");
        }
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
                    for (OptionDropbox o : this.dir) {
                        if (o.getName().equals(name)) {
                            myList.add(o.getFile());
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.e("epsxedropbox", "unable to get selected files");
        }
        return (String[]) myList.toArray(new String[myList.size()]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void action_upload(final DropboxArrayAdapter adapter) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Upload Selected Files");
        alertDialog.setMessage("Do you want to upload to Dropbox?");
        alertDialog.setButton2("Yes", new DialogInterface.OnClickListener() { // from class: com.epsxe.ePSXe.dropbox.DropboxManager.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                String[] arr = DropboxManager.this.get_selected_files();
                if (arr.length > 0) {
                    if (DropboxClientFactory.getClient() != null) {
                        DropboxUploadTask upload = new DropboxUploadTask(DropboxManager.this, DropboxClientFactory.getClient(), "/" + DropboxManager.this.code + "/", arr, DropboxManager.this.dir, adapter);
                        upload.execute(new Void[0]);
                    } else {
                        Toast.makeText(DropboxManager.this, "Error uploading file", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        alertDialog.setButton("No", new DialogInterface.OnClickListener() { // from class: com.epsxe.ePSXe.dropbox.DropboxManager.2
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertDialog.setIcon(android.R.drawable.ic_menu_directions);
        alertDialog.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void action_download(final DropboxArrayAdapter adapter) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Download Selected Files");
        alertDialog.setMessage("Do you want to download to Dropbox?");
        alertDialog.setButton2("Yes", new DialogInterface.OnClickListener() { // from class: com.epsxe.ePSXe.dropbox.DropboxManager.3
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                String[] arr = DropboxManager.this.get_selected_files();
                if (arr.length > 0 && DropboxClientFactory.getClient() != null) {
                    DropboxDownloadTask download = new DropboxDownloadTask(DropboxManager.this, DropboxClientFactory.getClient(), "/" + DropboxManager.this.code + "/", arr, DropboxManager.this.dir, adapter);
                    download.execute(new Void[0]);
                }
            }
        });
        alertDialog.setButton("No", new DialogInterface.OnClickListener() { // from class: com.epsxe.ePSXe.dropbox.DropboxManager.4
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertDialog.setIcon(android.R.drawable.ic_menu_directions);
        alertDialog.show();
    }

    private void add_quickButtons(final DropboxArrayAdapter adapter) {
        this.button_upload = (Button) findViewById(R.id.button1);
        this.button_upload.setOnClickListener(new View.OnClickListener() { // from class: com.epsxe.ePSXe.dropbox.DropboxManager.5
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                DropboxManager.this.action_upload(adapter);
            }
        });
        this.button_download = (Button) findViewById(R.id.button2);
        this.button_download.setOnClickListener(new View.OnClickListener() { // from class: com.epsxe.ePSXe.dropbox.DropboxManager.6
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                DropboxManager.this.action_download(adapter);
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
            myIntent.putExtra("com.epsxe.ePSXe.fcMode", "SELECT_DROPBOX");
        }
        startActivity(myIntent);
        setRequestedOrientation(4);
        finish();
    }

    private void alertdialog_doback(Context mCont) {
        final DropboxArrayAdapter mAdapter = this.adapter;
        String[] items = {"Upload to Dropbox", "Download from Dropbox", "Back"};
        ListView gListView = new ListView(mCont);
        ArrayAdapter<String> ladapter = new ArrayAdapter<>(mCont, android.R.layout.simple_list_item_1, items);
        gListView.setAdapter((ListAdapter) ladapter);
        gListView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.epsxe.ePSXe.dropbox.DropboxManager.7
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> listView, View itemView, int position, long itemId) {
                switch (position) {
                    case 0:
                        DropboxManager.this.action_upload(mAdapter);
                        break;
                    case 1:
                        DropboxManager.this.action_download(mAdapter);
                        break;
                    case 2:
                        DropboxManager.this.doback();
                        break;
                }
                DialogUtil.closeDialog(DropboxManager.this.pluginAlert);
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

    void DropboxInit() {
        Auth.startOAuth2Authentication(this, APP_KEY);
    }

    void DropboxResume() {
        SharedPreferences prefs = getSharedPreferences(ACCOUNT_PREFS_NAME, 0);
        String accessToken = prefs.getString("ACCESS_TOKEN", null);
        if (accessToken == null) {
            String accessToken2 = Auth.getOAuth2Token();
            if (accessToken2 != null) {
                prefs.edit().putString("ACCESS_TOKEN", accessToken2).apply();
                initAndLoadData(accessToken2);
            }
        } else {
            initAndLoadData(accessToken);
        }
        String uid = Auth.getUid();
        String storedUid = prefs.getString(USER_ID, null);
        if (uid != null && !uid.equals(storedUid)) {
            prefs.edit().putString(USER_ID, uid).apply();
        }
    }

    private void initAndLoadData(String accessToken) {
        DropboxClientFactory.init(accessToken);
    }

    protected boolean hasToken() {
        SharedPreferences prefs = getSharedPreferences(ACCOUNT_PREFS_NAME, 0);
        String accessToken = prefs.getString("ACCESS_TOKEN", null);
        return accessToken != null;
    }

    void DropboxExit() {
    }
}
