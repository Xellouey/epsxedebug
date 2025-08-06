package com.epsxe.ePSXe;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.epsxe.ePSXe.dropbox.DropboxManager;
import com.epsxe.ePSXe.gdrive.GdriveManager;
import com.epsxe.ePSXe.jni.libdetect;
import com.epsxe.ePSXe.jni.libgamedetect;
import com.epsxe.ePSXe.task.DecodeSevenZipTask;
import com.epsxe.ePSXe.task.DownloadShaderPluginTask;
import com.epsxe.ePSXe.task.IndexECMTask;
import com.epsxe.ePSXe.util.CacheUtil;
import com.epsxe.ePSXe.util.DeviceUtil;
import com.epsxe.ePSXe.util.DialogUtil;
import com.epsxe.ePSXe.util.FileUtil;
import com.epsxe.ePSXe.util.PSXUtil;
import com.epsxe.ePSXe.util.ReportUtil;
import com.epsxe.ePSXe.util.ShortcutUtil;
import com.fasterxml.jackson.core.util.MinimalPrettyPrinter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Scanner;
import org.apache.http.cookie.ClientCookie;

/* loaded from: classes.dex */
public class FileChooser extends ListActivity {
    private static final String QUOTATION_MARK = "\"";

    /* renamed from: a */
    private libdetect f119a;
    private FileArrayAdapter adapter;
    private FileDescArrayAdapter adapterdesc;
    private Button button_browse_files;
    private Button button_game_list;
    private Button button_reload;
    private File currentDir;

    /* renamed from: d */
    private libgamedetect f120d;
    private GameInfo gameinfo;
    private String libFolder;
    private AlertDialog mcdAlert;
    private ePSXeReadPreferences mePSXeReadPreferences;
    private AlertDialog pluginAlert;
    private AlertDialog reloadAlert;
    private AlertDialog shaderAlert;
    private String fcMode = "SELECT_ISO";
    private int browserMode = 1;
    private int reloadMode = 0;
    private int notfound = 0;
    private int tracescan = 0;
    private List<File> gFolders = new ArrayList();
    private int emu_xperiaplay = 0;
    private String sdcardname = "";
    private String locale = "en";
    private int serverMode = 0;
    private int version = Build.VERSION.SDK_INT;
    private int is64bits = 0;

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        int osVersion = Integer.parseInt(Build.VERSION.SDK);
        if (osVersion >= 11) {
            if (this.fcMode.equals("SELECT_ISO")) {
                MenuInflater inflater = getMenuInflater();
                if (this.notfound == 0) {
                    inflater.inflate(R.menu.filechooser_actions, menu);
                } else {
                    inflater.inflate(R.menu.filechooser_actions_notfound, menu);
                }
                return super.onCreateOptionsMenu(menu);
            }
            if (this.fcMode.equals("SELECT_GPU")) {
                MenuInflater inflater2 = getMenuInflater();
                inflater2.inflate(R.menu.filechooser_gpu_actions, menu);
                return super.onCreateOptionsMenu(menu);
            }
            if (this.fcMode.equals("SELECT_SHADER")) {
                MenuInflater inflater3 = getMenuInflater();
                inflater3.inflate(R.menu.filechooser_shader_actions, menu);
                return super.onCreateOptionsMenu(menu);
            }
            if (this.fcMode.equals("SELECT_MCR1") || this.fcMode.equals("SELECT_MCR2")) {
                MenuInflater inflater4 = getMenuInflater();
                inflater4.inflate(R.menu.filechooser_memcard_actions, menu);
                return super.onCreateOptionsMenu(menu);
            }
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override // android.app.Activity
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_addmemcard /* 2131034120 */:
                getsdcardname();
                return true;
            case R.id.action_download /* 2131034121 */:
            default:
                return super.onOptionsItemSelected(item);
            case R.id.action_downloadgpu /* 2131034122 */:
                alertdialog_downloadgpu();
                return true;
            case R.id.action_downloadshader /* 2131034123 */:
                alertdialog_downloadshader(this.f119a.isX86(), this);
                return true;
            case R.id.action_filebrowser /* 2131034124 */:
                action_filebrowser();
                return true;
            case R.id.action_gamedetails /* 2131034125 */:
                action_gamedetails();
                return true;
            case R.id.action_gamelist /* 2131034126 */:
                action_gamelist();
                return true;
            case R.id.action_help /* 2131034127 */:
                action_help();
                return true;
            case R.id.action_rescan /* 2131034128 */:
                action_rescan();
                return true;
        }
    }

    @Override // android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (this.mePSXeReadPreferences == null) {
            this.mePSXeReadPreferences = new ePSXeReadPreferences(this);
        }
        this.libFolder = getFilesDir().getAbsolutePath();
        String param = getIntent().getStringExtra("com.epsxe.ePSXe.fcMode");
        if (param != null && param.length() > 0) {
            this.fcMode = param;
        }
        this.locale = Locale.getDefault().getLanguage();
        DeviceUtil.setLocale(this);
        Log.e("epsxefolder", "Mode " + this.fcMode);
        if (this.fcMode.equals("RUN_BIOS")) {
            Log.e("epsxefolder", "folder RUN_BIOS");
            Intent myIntent = new Intent(this, (Class<?>) ePSXe.class);
            myIntent.putExtra("com.epsxe.ePSXe.isoName", "___RUNBIOS___");
            startActivity(myIntent);
            finish();
            return;
        }
        if (this.fcMode.equals("EXEC_ISO")) {
            this.f120d = new libgamedetect();
            String index = this.f120d.getECMToIndex(getIntent().getStringExtra("com.epsxe.ePSXe.isoName"));
            if (!index.equals("OK")) {
                new IndexECMTask(this, this, this.serverMode, this.emu_xperiaplay).execute(index, getIntent().getStringExtra("com.epsxe.ePSXe.isoName"), getIntent().getStringExtra("com.epsxe.ePSXe.isoSlot"));
                return;
            }
            Log.e("epsxefolder", "folder EXEC_ISO");
            String xp = getIntent().getStringExtra("com.epsxe.ePSXe.xperiaplay");
            Intent myIntent2 = (xp == null || !xp.contains("1")) ? new Intent(this, (Class<?>) ePSXe.class) : new Intent(this, (Class<?>) ePSXeNative.class);
            myIntent2.putExtra("com.epsxe.ePSXe.isoName", getIntent().getStringExtra("com.epsxe.ePSXe.isoName"));
            myIntent2.putExtra("com.epsxe.ePSXe.isoSlot", getIntent().getStringExtra("com.epsxe.ePSXe.isoSlot"));
            myIntent2.putExtra("com.epsxe.ePSXe.padType", getIntent().getStringExtra("com.epsxe.ePSXe.padType"));
            myIntent2.putExtra("com.epsxe.ePSXe.snapRestore", "yes");
            startActivity(myIntent2);
            finish();
            return;
        }
        if (this.fcMode.equals("EXEC_GUI")) {
            Log.e("epsxefolder", "folder EXEC_GUI");
            startActivity(new Intent(this, (Class<?>) ePSXe.class));
            finish();
            return;
        }
        String param2 = getIntent().getStringExtra("com.epsxe.ePSXe.isoPath");
        if (param2 != null && !param2.equals("/") && param2.length() > 0) {
            this.currentDir = new File(param2);
        } else {
            File extStore = ContextCompat.getDataDir(this);
            if (this.fcMode.equals("SELECT_MCR1") || this.fcMode.equals("SELECT_MCR2")) {
                this.currentDir = new File(extStore.getAbsolutePath() + "/epsxe/memcards");
            } else if (this.fcMode.equals("SELECT_GPU")) {
                this.currentDir = new File(extStore.getAbsolutePath() + "/epsxe/plugins");
            } else if (this.fcMode.equals("SELECT_SHADER")) {
                this.currentDir = new File(extStore.getAbsolutePath() + "/epsxe/shaders");
            } else {
                this.currentDir = new File(extStore.getAbsolutePath());
            }
        }
        Log.e("folder", "CurrentDir " + this.currentDir);
        String param3 = getIntent().getStringExtra("com.epsxe.ePSXe.browserMode");
        if (param3 != null && param3.length() > 0) {
            this.browserMode = Integer.parseInt(param3);
        } else {
            this.browserMode = 1;
        }
        String param4 = getIntent().getStringExtra("com.epsxe.ePSXe.reloadMode");
        if (param4 != null && param4.length() > 0) {
            this.reloadMode = Integer.parseInt(param4);
        } else {
            this.reloadMode = 4;
        }
        if (this.mePSXeReadPreferences.getPadAnalogPadID(1).contains("xperiaplay")) {
            this.emu_xperiaplay = 1;
        }
        String param6 = getIntent().getStringExtra("com.epsxe.ePSXe.servermode");
        if (param6 != null && param6.length() > 0) {
            this.serverMode = Integer.parseInt(param6);
        } else {
            this.serverMode = 0;
        }
        String param7 = getIntent().getStringExtra("com.epsxe.ePSXe.notfound");
        if (param7 != null && param7.length() > 0) {
            this.notfound = Integer.parseInt(param7);
        } else {
            this.notfound = 0;
        }
        if (this.serverMode == 2 || this.serverMode == 4) {
            Log.e("epsxefolder", "folder RUN_CLIENT");
            String xp2 = getIntent().getStringExtra("com.epsxe.ePSXe.xperiaplay");
            Intent myIntent3 = (xp2 == null || !xp2.contains("1")) ? new Intent(this, (Class<?>) ePSXe.class) : new Intent(this, (Class<?>) ePSXeNative.class);
            myIntent3.putExtra("com.epsxe.ePSXe.isoName", "___NETWORK___");
            myIntent3.putExtra("com.epsxe.ePSXe.isoSlot", getIntent().getStringExtra("com.epsxe.ePSXe.isoSlot"));
            myIntent3.putExtra("com.epsxe.ePSXe.padType", getIntent().getStringExtra("com.epsxe.ePSXe.padType"));
            myIntent3.putExtra("com.epsxe.ePSXe.servermode", "" + this.serverMode);
            startActivity(myIntent3);
            finish();
            return;
        }
        File extStore2 = ContextCompat.getDataDir(this);
        if (this.fcMode.equals("SELECT_BIOS") || this.fcMode.equals("SELECT_SKIN") || this.fcMode.equals("SELECT_MCR1") || this.fcMode.equals("SELECT_MCR2") || this.fcMode.equals("SELECT_GPU") || this.fcMode.equals("SELECT_SHADER")) {
            this.f119a = new libdetect();
            this.browserMode = 0;
            this.reloadMode = 0;
            this.is64bits = this.f119a.is64bits();
        } else {
            this.f119a = new libdetect();
            this.is64bits = this.f119a.is64bits();
            this.f120d = new libgamedetect();
            this.f120d.setSdCardPath(extStore2.getAbsolutePath());
            this.gameinfo = new GameInfo(this, this.f120d);
        }
        Log.e("epsxefolder", "Browser Mode " + this.browserMode);
        Log.e("epsxefolder", "Reload Mode " + this.reloadMode);
        getWindow().setFlags(128, 128);
        if (this.browserMode == 1 || this.browserMode == 2) {
            File f = new File(ContextCompat.getDataDir(this), "epsxe/info/wipscanning");
            if (f == null || !f.exists()) {
                scanForGames();
                return;
            } else {
                alertdialog_crash();
                return;
            }
        }
        generateList();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void scanForGames() {
        File extStore = ContextCompat.getDataDir(this);
        switch (this.reloadMode) {
            case 0:
                loadFolders(this.gFolders);
                new ScansdcardGamesTask(this).execute("gFolders");
                break;
            case 1:
                CacheUtil.deleteCache();
                new ScansdcardGamesTask(this).execute(extStore.getAbsolutePath());
                break;
            case 2:
                CacheUtil.deleteCache();
                new ScansdcardGamesTask(this).execute("/mnt", "/storage", "/Removable", "/network", extStore.getAbsolutePath());
                break;
            case 3:
                CacheUtil.deleteCache();
                File rem = new File("/Removable");
                if (!rem.exists()) {
                    File sto = new File("/storage");
                    if (!sto.exists()) {
                        new ScansdcardGamesTask(this).execute("/mnt");
                        break;
                    } else {
                        new ScansdcardGamesTask(this).execute("/network", "/storage");
                        break;
                    }
                } else {
                    new ScansdcardGamesTask(this).execute("/network", "/Removable");
                    break;
                }
            case 4:
                if (loadFolders(this.gFolders) == 0) {
                    new ScansdcardGamesTask(this).execute("/mnt", "/storage", "/Removable", extStore.getAbsolutePath());
                    break;
                } else {
                    new ScansdcardGamesTask(this).execute("gFolders");
                    break;
                }
            default:
                generateList();
                break;
        }
    }

    private void alertdialog_crash() {
        TextView message = new TextView(this);
        SpannableString s = new SpannableString(getText(R.string.file_crash_msg));
        message.setText(s);
        message.setMovementMethod(LinkMovementMethod.getInstance());
        message.setTextAppearance(this, android.R.style.TextAppearance_Medium);
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(R.string.file_crash_title);
        alertDialog.setView(message);
        alertDialog.setCancelable(false);
        alertDialog.setButton(getString(R.string.file_crash_retry), new DialogInterface.OnClickListener() { // from class: com.epsxe.ePSXe.FileChooser.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                FileChooser.this.tracescan = 1;
                CacheUtil.deletewipscan();
                CacheUtil.deletetracescan();
                FileChooser.this.scanForGames();
            }
        });
        alertDialog.setButton2(getString(R.string.file_crash_disable), new DialogInterface.OnClickListener() { // from class: com.epsxe.ePSXe.FileChooser.2
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                FileChooser.this.browserMode = 0;
                FileChooser.this.reloadMode = 0;
                CacheUtil.deletewipscan();
                FileChooser.this.generateList();
            }
        });
        File f = new File(ContextCompat.getDataDir(this), "epsxe/info/tracescan.txt");
        if (f.exists()) {
            alertDialog.setButton3(getString(R.string.file_crash_report), new DialogInterface.OnClickListener() { // from class: com.epsxe.ePSXe.FileChooser.3
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialog, int which) {
                    ReportUtil.sendScanningCrashEmail(FileChooser.this, "epsxe scanning crash", "attached scanning tracelog");
                    FileChooser.this.browserMode = 0;
                    FileChooser.this.reloadMode = 0;
                    FileChooser.this.generateList();
                }
            });
        }
        alertDialog.setIcon(android.R.drawable.ic_menu_directions);
        alertDialog.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void alertdialog_shortcut(final String path, final int slot, String name, final String code, final String padtype) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(R.string.file_createshortcut);
        alertDialog.setMessage(getString(R.string.file_wantcreateshortcut));
        final EditText input = new EditText(this);
        alertDialog.setView(input);
        input.setText(name);
        alertDialog.setButton(getString(R.string.file_shortcutlarge), new DialogInterface.OnClickListener() { // from class: com.epsxe.ePSXe.FileChooser.4
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                ShortcutUtil scUtil = new ShortcutUtil();
                if (Build.VERSION.SDK_INT >= 26) {
                    scUtil.addShortcut26(FileChooser.this.getApplicationContext(), ePSXe.class, path, slot, input.getText().toString(), code, 128, padtype);
                } else {
                    scUtil.addShortcut(FileChooser.this.getApplicationContext(), ePSXe.class, path, slot, input.getText().toString(), code, 128, padtype);
                }
            }
        });
        alertDialog.setButton2(getString(R.string.file_shortcutcancel), new DialogInterface.OnClickListener() { // from class: com.epsxe.ePSXe.FileChooser.5
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertDialog.setButton3(getString(R.string.file_shortcutsmall), new DialogInterface.OnClickListener() { // from class: com.epsxe.ePSXe.FileChooser.6
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                ShortcutUtil scUtil = new ShortcutUtil();
                if (Build.VERSION.SDK_INT >= 26) {
                    scUtil.addShortcut26(FileChooser.this.getApplicationContext(), ePSXe.class, path, slot, input.getText().toString(), code, 64, padtype);
                } else {
                    scUtil.addShortcut(FileChooser.this.getApplicationContext(), ePSXe.class, path, slot, input.getText().toString(), code, 64, padtype);
                }
            }
        });
        alertDialog.setIcon(android.R.drawable.ic_menu_directions);
        alertDialog.show();
    }

    private class ScansdcardGamesTask extends AsyncTask<String, String, Integer> {
        private Context context;
        ProgressDialog dialog;

        public ScansdcardGamesTask(Context ctx) {
            this.context = ctx;
            this.dialog = new ProgressDialog(this.context);
            this.dialog.setTitle(R.string.file_games_scanning);
            this.dialog.show();
            this.dialog.setCancelable(false);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public Integer doInBackground(String... params) {
            publishProgress("scanning folders...");
            if (params[0].equals("gFolders")) {
                if (checkGfolders(FileChooser.this.gFolders)) {
                    publishProgress("saving folders...");
                    FileChooser.this.saveFolders(FileChooser.this.gFolders);
                    publishProgress("saving folders done");
                }
            } else {
                for (int i = 0; i < params.length; i++) {
                    File f = new File(params[i]);
                    if (f.exists()) {
                        findFolders(params[i], 0);
                    }
                }
                FileChooser.this.saveFolders(FileChooser.this.gFolders);
            }
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
        public void onPostExecute(Integer result) {
            try {
                FileChooser.this.generateList();
            } catch (Exception e) {
            }
            DialogUtil.closeDialog(this.dialog);
        }

        private boolean checkGfolders(List<File> dirs) {
            return false;
        }

        protected void findFolders(String path, int level) {
            int added = 0;
            if (level <= 16) {
                try {
                    File f = new File(path);
                    File[] fileList = f.listFiles();
                    if (!f.getName().equals("DCIM") && !f.getName().equals("Camera") && !f.getName().equals("asec") && !f.getName().equals(ClientCookie.SECURE_ATTR) && !f.getName().equals("dev") && !f.getName().equals("obb") && !f.getName().equals("\\.lfs") && !f.getAbsolutePath().contains("/Android/data")) {
                        publishProgress(f.getAbsolutePath());
                        for (File file : fileList) {
                            try {
                                if (file.isDirectory()) {
                                    findFolders(file.getAbsolutePath(), level + 1);
                                } else if (added == 0) {
                                    if (FileUtil.isRom(file.getName())) {
                                        long msize = file.length() / 1048576;
                                        if (msize > 6) {
                                            File a = new File(f.getCanonicalPath());
                                            if (!FileChooser.this.gFolders.contains(a) && ((!f.getCanonicalPath().startsWith("/storage/emulated/0") && !f.getCanonicalPath().startsWith("/storage/emulated/1")) || FileChooser.this.version > 22)) {
                                                FileChooser.this.gFolders.add(a);
                                            }
                                            added = 1;
                                        }
                                    } else if (FileUtil.acceptPSX(file.getName())) {
                                        File a2 = new File(f.getCanonicalPath());
                                        if (!FileChooser.this.gFolders.contains(a2) && ((!f.getCanonicalPath().startsWith("/storage/emulated/0") && !f.getCanonicalPath().startsWith("/storage/emulated/1")) || FileChooser.this.version > 22)) {
                                            FileChooser.this.gFolders.add(a2);
                                        }
                                        added = 1;
                                    }
                                }
                            } catch (Exception e) {
                                Log.e("findFolders", "" + e.getMessage());
                            }
                        }
                    }
                } catch (Exception e2) {
                    Log.e("findFoldersG", "" + e2.getMessage());
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void generateList() {
        if (this.browserMode == 1 || this.browserMode == 2) {
            if (this.gFolders.size() != 0) {
                fillDesc(this.gFolders);
            } else {
                this.gFolders.add(this.currentDir);
                fillDesc(this.gFolders);
            }
        } else if (this.fcMode.equals("SELECT_SHADER")) {
            fillSimpleShader(this.currentDir);
        } else {
            fillSimple(this.currentDir);
        }
        if (this.fcMode.equals("SELECT_MCR1") || this.fcMode.equals("SELECT_MCR2")) {
            int osVersion = Integer.parseInt(Build.VERSION.SDK);
            if (osVersion < 11) {
                setContentView(R.layout.list_viewsd);
                add_quickButtonsSD();
            } else {
                setContentView(R.layout.list_viewsdv11);
            }
        } else if (this.fcMode.equals("SELECT_GPU")) {
            int osVersion2 = Integer.parseInt(Build.VERSION.SDK);
            if (osVersion2 < 11) {
                setContentView(R.layout.list_viewsg);
                add_quickButtonsGPU(this, this.f119a.isX86());
            } else {
                setContentView(R.layout.list_viewsdv11);
            }
        } else if (this.fcMode.equals("SELECT_SHADER")) {
            int osVersion3 = Integer.parseInt(Build.VERSION.SDK);
            if (osVersion3 < 11) {
                setContentView(R.layout.list_viewsg);
                add_quickButtonsSHADER(this, this.f119a.isX86());
            } else {
                setContentView(R.layout.list_viewsdv11);
            }
        } else {
            printList();
            int osVersion4 = Integer.parseInt(Build.VERSION.SDK);
            if (osVersion4 < 11 && !this.fcMode.equals("SELECT_DROPBOX") && !this.fcMode.equals("SELECT_GDRIVE")) {
                add_quickButtons();
            }
        }
        if (!DeviceUtil.isAndroidTV(this)) {
            getListView().setLongClickable(true);
            getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() { // from class: com.epsxe.ePSXe.FileChooser.7
                @Override // android.widget.AdapterView.OnItemLongClickListener
                public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id) {
                    if (FileChooser.this.browserMode == 1 || FileChooser.this.browserMode == 2) {
                        OptionDesc o = FileChooser.this.adapterdesc.getItem(position);
                        if (FileChooser.this.fcMode.equals("SELECT_ISO")) {
                            FileChooser.this.alertdialog_shortcut(o.getPath(), o.getSlot(), o.getName(), o.getCode(), o.getPadType());
                        }
                    }
                    return true;
                }
            });
        }
    }

    private void printList() {
        int osVersion = Integer.parseInt(Build.VERSION.SDK);
        if (osVersion >= 11 || this.fcMode.equals("SELECT_DROPBOX") || this.fcMode.equals("SELECT_GDRIVE")) {
            setContentView(R.layout.list_viewv11);
        } else {
            setContentView(R.layout.list_view2);
        }
    }

    private void fillDesc(List<File> dirs) {
        Display d = ((WindowManager) getSystemService("window")).getDefaultDisplay();
        boolean lIcon = d.getWidth() > 1700 || d.getHeight() > 1700;
        setTitle(R.string.file_listgames);
        List<OptionDesc> fls = CacheUtil.restoreCache(new ArrayList<>(), dirs);
        if (fls.size() <= 0) {
            CacheUtil.createwipscan();
            for (File f : dirs) {
                List<File> ifiles = new ArrayList<>();
                List<File> files = new ArrayList<>();
                File[] lfiles = f.listFiles();
                try {
                    for (File ff : lfiles) {
                        if (!ff.isDirectory()) {
                            if (FileUtil.isFullRom(ff.getName())) {
                                fls = this.gameinfo.addFile(ff, fls, this.tracescan);
                            } else if (FileUtil.isPBP(ff.getName())) {
                                pbpFile mfile = new pbpFile(ff.getAbsolutePath(), ff.getName());
                                int n = mfile.getNumFiles();
                                if (n == 1) {
                                    this.gameinfo.addFile(ff, fls, this.tracescan);
                                } else {
                                    for (int i = 0; i < n; i++) {
                                        fls = this.gameinfo.addFile(ff, fls, mfile.getFileName(i + 1), i, this.tracescan);
                                    }
                                }
                            } else if (FileUtil.isRom(ff.getName())) {
                                long msize = ff.length() / 1048576;
                                if (msize > 6) {
                                    files.add(ff);
                                }
                            } else if (FileUtil.isIndex(ff.getName())) {
                                int add = 1;
                                Iterator<File> it = ifiles.iterator();
                                while (true) {
                                    if (!it.hasNext()) {
                                        break;
                                    }
                                    File ii = it.next();
                                    if (ii.getName().length() > 4 && ff.getName().length() > 4) {
                                        String iname = ii.getName().substring(0, ii.getName().length() - 4);
                                        String fname = ff.getName().substring(0, ff.getName().length() - 4);
                                        if (iname.toLowerCase().equals(fname.toLowerCase())) {
                                            add = 0;
                                            break;
                                        }
                                    }
                                }
                                if (add == 1) {
                                    ifiles.add(ff);
                                    fls = this.gameinfo.addFile(ff, fls, this.tracescan);
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                }
                for (File ii2 : ifiles) {
                    if (ii2.getName().length() > 4) {
                        String iname2 = ii2.getName().substring(0, ii2.getName().length() - 4);
                        Iterator<File> it2 = files.iterator();
                        while (true) {
                            if (!it2.hasNext()) {
                                break;
                            }
                            File rr = it2.next();
                            if (rr.getName().length() > 4) {
                                String rname = rr.getName().substring(0, rr.getName().length() - 4);
                                if (iname2.toLowerCase().equals(rname.toLowerCase())) {
                                    Log.e("listview", "removing " + rr.getName());
                                    files.remove(rr);
                                    break;
                                }
                            }
                            if (rr.getName().length() > 8) {
                                String rname2 = rr.getName().substring(0, rr.getName().length() - 8);
                                if (iname2.toLowerCase().equals(rname2.toLowerCase())) {
                                    Log.e("listview", "removing " + rr.getName());
                                    files.remove(rr);
                                    break;
                                }
                            }
                        }
                    }
                    if (FileUtil.isCUE(ii2.getName()) && ii2.length() < 32768) {
                        for (File rr2 : files) {
                            if (rr2.getName().toLowerCase().equals(ii2.getName().toLowerCase())) {
                                Log.e("listview", "removing " + rr2.getName());
                                files.remove(rr2);
                                break;
                            }
                        }
                        try {
                            BufferedReader reader = new BufferedReader(new FileReader(ii2));
                            while (true) {
                                String line = reader.readLine();
                                if (line == null) {
                                    break;
                                }
                                Scanner tok = new Scanner(line);
                                if (tok.hasNext()) {
                                    String token = tok.next();
                                    if ("FILE".equalsIgnoreCase(token)) {
                                        String nextToken = tok.next();
                                        if (nextToken.startsWith(QUOTATION_MARK)) {
                                            if (nextToken.endsWith(QUOTATION_MARK)) {
                                                nextToken = nextToken.substring(1, nextToken.length() - 1);
                                            } else {
                                                nextToken = nextToken.substring(1) + tok.useDelimiter(QUOTATION_MARK).next();
                                            }
                                        }
                                        for (File rr3 : files) {
                                            if (rr3.getName().toLowerCase().equals(nextToken.toLowerCase()) || rr3.getName().toLowerCase().equals(nextToken.concat(".ecm").toLowerCase())) {
                                                files.remove(rr3);
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                            reader.close();
                        } catch (Exception e2) {
                        }
                    }
                }
                Iterator<File> it3 = files.iterator();
                while (it3.hasNext()) {
                    fls = this.gameinfo.addFile(it3.next(), fls, this.tracescan);
                }
            }
            CacheUtil.deletewipscan();
            CacheUtil.deletetracescan();
        }
        if (this.fcMode.equals("SELECT_DROPBOX") || this.fcMode.equals("SELECT_GDRIVE")) {
            File fs = new File(ContextCompat.getDataDir(this).getAbsolutePath() + "/epsxe/sstates/");
            File[] sfiles = fs.listFiles();
            try {
                for (File ff2 : sfiles) {
                    if (ff2.getName().startsWith("SL")) {
                        String psexe = ff2.getName().substring(0, 11);
                        String code = psexe.substring(0, 4) + "-" + psexe.substring(5, 8) + psexe.substring(9, 11);
                        boolean found = false;
                        Iterator<OptionDesc> it4 = fls.iterator();
                        while (true) {
                            if (!it4.hasNext()) {
                                break;
                            }
                            OptionDesc o = it4.next();
                            if (o.getCode().equals(code)) {
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            this.gameinfo.addFileDropbox(code, fls);
                        }
                    }
                }
            } catch (Exception e3) {
                Log.e("FileChooser", "e = " + e3);
            }
        }
        Log.e("filechooser", "Number of games found: " + fls.size());
        if (fls.size() > 0) {
            Collections.sort(fls);
            if (!this.fcMode.equals("SELECT_DROPBOX") && !this.fcMode.equals("SELECT_GDRIVE")) {
                CacheUtil.saveCache(fls, dirs);
            }
            if (lIcon) {
                this.adapterdesc = new FileDescArrayAdapter(this, this, R.layout.list_rowl, fls);
            } else {
                this.adapterdesc = new FileDescArrayAdapter(this, this, R.layout.list_row, fls);
            }
            setListAdapter(this.adapterdesc);
            return;
        }
        Intent myIntent = new Intent(this, (Class<?>) FileChooser.class);
        myIntent.putExtra("com.epsxe.ePSXe.fcMode", "SELECT_ISO");
        myIntent.putExtra("com.epsxe.ePSXe.isoPath", this.currentDir.getAbsolutePath());
        myIntent.putExtra("com.epsxe.ePSXe.browserMode", "0");
        myIntent.putExtra("com.epsxe.ePSXe.reloadMode", "0");
        myIntent.putExtra("com.epsxe.ePSXe.notfound", "1");
        startActivity(myIntent);
        finish();
    }

    private void fillSimpleShader(File f) {
        File[] dirs = f.listFiles();
        setTitle(getString(R.string.file_currentfolder) + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + f.getName());
        List<Option> fls = new ArrayList<>();
        fls.add(new Option("Disabled", "Version = 1, License = BSD, Copyright = ePSXe", "NONE", 0));
        fls.add(new Option("FXAA", "Version = 1, License = BSD, Copyright = Timothy Lottes", "FXAA", 0));
        fls.add(new Option("CRT-Hyllian", "Version = 1, License = Free, Copyright = Hyllian", "CRT1", 0));
        fls.add(new Option("CRT-Lottes", "Version = 1, License = Free, Copyright = Timothy Lottes", "CRT2", 0));
        fls.add(new Option("xBR-LV2-3D", "Version = 1, License = Free, Copyright = Hyllian", "XBR", 0));
        try {
            for (File ff : dirs) {
                if (ff.isDirectory()) {
                    String path = ff.getAbsolutePath() + "/gpuCore.";
                    File fv = new File(path + "slv");
                    File fg = new File(path + "slf");
                    if (fv != null && fv.exists() && fg != null && fg.exists()) {
                        File fi = new File(path + "ini");
                        if (fi != null && fi.exists()) {
                            String snam = ff.getName();
                            String sver = "N/A";
                            String slic = "N/A";
                            String scop = "N/A";
                            try {
                                Properties props = new Properties();
                                InputStream inputStream = new FileInputStream(path + "ini");
                                props.load(inputStream);
                                snam = props.getProperty("name", snam);
                                sver = props.getProperty("version", "N/A");
                                slic = props.getProperty("license", "N/A");
                                scop = props.getProperty("copyright", "N/A");
                            } catch (Exception e) {
                            }
                            fls.add(new Option(snam, "Version = " + sver + ", License = " + slic + ", Copyright = " + scop, ff.getAbsolutePath(), 0));
                        } else {
                            fls.add(new Option(ff.getName(), "Version = N/A, License = N/A, Copyright = N/A", ff.getAbsolutePath(), 0));
                        }
                    }
                }
            }
        } catch (Exception e2) {
        }
        this.adapter = new FileArrayAdapter(this, R.layout.file_viewos, fls);
        setListAdapter(this.adapter);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void fillSimple(File f) {
        String pathlibFolder;
        String pathlibFolderAlt;
        File[] dirs = f.listFiles();
        setTitle(getString(R.string.file_currentfolder) + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + f.getName());
        List<Option> dir = new ArrayList<>();
        List<Option> fls = new ArrayList<>();
        if (this.fcMode.equals("SELECT_GPU")) {
            try {
                pathlibFolder = getPackageManager().getPackageInfo("com.epsxe.opengl", 0).applicationInfo.dataDir + "/lib";
            } catch (Exception e) {
                pathlibFolder = "";
            }
            String plugin = pathlibFolder + "/libopenglplugin.so";
            File ff = new File(plugin);
            if (ff.exists()) {
                fls.add(new Option(ff.getName(), "File Size: " + ff.length() + " Bytes", ff.getAbsolutePath(), 0));
            } else {
                try {
                    pathlibFolderAlt = getPackageManager().getPackageInfo("com.epsxe.opengl", 0).applicationInfo.nativeLibraryDir;
                } catch (Exception e2) {
                    pathlibFolderAlt = "";
                }
                String plugin2 = pathlibFolderAlt + "/libopenglplugin.so";
                File ff2 = new File(plugin2);
                if (ff2.exists()) {
                    fls.add(new Option(ff.getName(), "File Size: " + ff.length() + " Bytes", ff2.getAbsolutePath(), 0));
                }
            }
        }
        try {
            int length = dirs.length;
            int i = 0;
            while (true) {
                int i2 = i;
                if (i2 >= length) {
                    break;
                }
                File ff3 = dirs[i2];
                if (ff3.isDirectory()) {
                    dir.add(new Option(ff3.getName(), "Folder", ff3.getAbsolutePath(), 0));
                } else {
                    Log.e("folder", "File " + ff3.getName());
                    if (this.fcMode.equals("SELECT_ISO")) {
                        if (FileUtil.acceptPSX(ff3.getName())) {
                            long msize = ff3.length() / 1048576;
                            if (msize > 2) {
                                if (ff3.getName().toLowerCase().endsWith(".pbp")) {
                                    pbpFile mfile = new pbpFile(ff3.getAbsolutePath(), ff3.getName());
                                    int n = mfile.getNumFiles();
                                    for (int i3 = 0; i3 < n; i3++) {
                                        fls.add(new Option(mfile.getFileName(i3 + 1), "File Size: " + (msize / n) + " Mbytes", ff3.getAbsolutePath(), i3));
                                    }
                                } else {
                                    fls.add(new Option(ff3.getName(), "File Size: " + msize + " Mbytes", ff3.getAbsolutePath(), 0));
                                }
                            } else {
                                fls.add(new Option(ff3.getName(), "File Size: " + ff3.length() + " Bytes", ff3.getAbsolutePath(), 0));
                            }
                        }
                    } else if (this.fcMode.equals("SELECT_BIOS")) {
                        long msize2 = ff3.length();
                        Log.e("folder", "File1 " + ff3.getName() + " size: " + msize2);
                        if (ff3.getName().toLowerCase().endsWith(".bin") && msize2 == 524288) {
                            Log.e("folder", "File2 " + ff3.getName());
                            fls.add(new Option(ff3.getName(), "File Size: " + ff3.length() + " Bytes", ff3.getAbsolutePath(), 0));
                        } else if (ff3.getName().toLowerCase().endsWith(".zip") && msize2 > 150000 && msize2 < 4500000 && (ff3.getName().toLowerCase().contains("scph") || ff3.getName().toLowerCase().contains("psx") || ff3.getName().toLowerCase().contains("bios"))) {
                            Log.e("folder", "File2 " + ff3.getName());
                            fls.add(new Option(ff3.getName(), "File Size: " + ff3.length() + " Bytes", ff3.getAbsolutePath(), 0));
                        }
                    } else if (this.fcMode.equals("SELECT_GPU")) {
                        Log.e("folder", "File1 " + ff3.getName());
                        long msize3 = ff3.length();
                        Log.e("folder", "File1 " + ff3.getName() + " size: " + msize3);
                        if (ff3.getName().toLowerCase().endsWith(".so")) {
                            Log.e("folder", "File2 " + ff3.getName());
                            fls.add(new Option(ff3.getName(), "File Size: " + ff3.length() + " Bytes", ff3.getAbsolutePath(), 0));
                        }
                    } else if (this.fcMode.equals("SELECT_SKIN")) {
                        Log.e("folder", "File1 " + ff3.getName());
                        long msize4 = ff3.length();
                        Log.e("folder", "File1 " + ff3.getName() + " size: " + msize4);
                        if (ff3.getName().toLowerCase().endsWith(".png")) {
                            Log.e("folder", "File2 " + ff3.getName());
                            fls.add(new Option(ff3.getName(), "File Size: " + ff3.length() + " Bytes", ff3.getAbsolutePath(), 0));
                        }
                    } else if (this.fcMode.equals("SELECT_MCR1") || this.fcMode.equals("SELECT_MCR2")) {
                        ff3.length();
                        if (FileUtil.isMemcard(ff3.getName())) {
                            fls.add(new Option(ff3.getName(), "File Size: " + ff3.length() + " Bytes", ff3.getAbsolutePath(), 0));
                        }
                    }
                }
                i = i2 + 1;
            }
        } catch (Exception e3) {
        }
        Collections.sort(dir);
        Collections.sort(fls);
        dir.addAll(fls);
        if (!f.getAbsolutePath().equalsIgnoreCase("/")) {
            dir.add(0, new Option("..", "Parent Directory", f.getParent(), 0));
        }
        this.adapter = new FileArrayAdapter(this, R.layout.file_viewos, dir);
        setListAdapter(this.adapter);
    }

    private void alertdialog_compressed(String name) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(R.string.file_unsuported);
        alertDialog.setMessage(getString(R.string.file_unsuportedcompressed));
        alertDialog.setButton(getString(R.string.file_unsuportedback), new DialogInterface.OnClickListener() { // from class: com.epsxe.ePSXe.FileChooser.8
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertDialog.setIcon(android.R.drawable.ic_menu_directions);
        alertDialog.show();
    }

    @Override // android.app.ListActivity
    protected void onListItemClick(ListView l, View v, int position, long id) {
        String pathlibFolder;
        String pathlibFolderAlt;
        super.onListItemClick(l, v, position, id);
        if (this.browserMode == 0) {
            Option o = this.adapter.getItem(position);
            if (o.getData().equalsIgnoreCase("folder") || o.getData().equalsIgnoreCase("parent directory")) {
                this.currentDir = new File(o.getPath());
                fillSimple(this.currentDir);
                return;
            }
            if (!o.getPath().equalsIgnoreCase("folder")) {
                if (FileUtil.compressed7z(o.getPath())) {
                    if (this.fcMode.equals("SELECT_ISO") || this.fcMode.equals("SELECT_BIOS")) {
                        try {
                            pathlibFolder = getPackageManager().getPackageInfo("com.epsxe.sevenzip", 0).applicationInfo.dataDir + "/lib";
                        } catch (Exception e) {
                            pathlibFolder = "";
                        }
                        try {
                            pathlibFolderAlt = getPackageManager().getPackageInfo("com.epsxe.sevenzip", 0).applicationInfo.nativeLibraryDir;
                        } catch (Exception e2) {
                            pathlibFolderAlt = "";
                        }
                        String plugin = pathlibFolder + "/libsevenzip.so";
                        File ff = new File(plugin);
                        if (ff.exists()) {
                            action_uncompress(this, this, o.getName(), o.getPath(), plugin);
                            return;
                        }
                        String plugin2 = pathlibFolderAlt + "/libsevenzip.so";
                        File ff2 = new File(plugin2);
                        if (ff2.exists()) {
                            action_uncompress(this, this, o.getName(), o.getPath(), plugin2);
                            return;
                        }
                        int x86 = this.f119a.isX86();
                        String pluginold = this.libFolder + "/libsevenzip.so";
                        if (x86 == 1) {
                            pluginold = this.libFolder + "/libsevenzip_intel.so";
                        }
                        File fff = new File(pluginold);
                        if (fff.exists()) {
                            action_uncompress(this, this, o.getName(), o.getPath(), pluginold);
                            return;
                        } else {
                            alertdialog_downloadunc();
                            return;
                        }
                    }
                    return;
                }
                if (!FileUtil.compressed(o.getPath())) {
                    onFileClick(o.getName(), o.getPath(), o.getSlot(), null);
                    return;
                } else {
                    alertdialog_compressed(o.getName());
                    return;
                }
            }
            return;
        }
        OptionDesc o2 = this.adapterdesc.getItem(position);
        if (!this.fcMode.equals("SELECT_DROPBOX")) {
            if (!this.fcMode.equals("SELECT_GDRIVE")) {
                onFileClick(o2.getName(), o2.getPath(), o2.getSlot(), o2.getPadType());
                return;
            } else {
                onFileClickGdrive(o2.getCode());
                return;
            }
        }
        onFileClickDropbox(o2.getCode());
    }

    private void action_uncompress(final Activity a, final Context c, final String name, final String path, final String plugin) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Uncompress Files");
        alertDialog.setMessage("Do you want to uncompress the game to the SDCARD?");
        if (this.fcMode.equals("SELECT_ISO")) {
            alertDialog.setButton2("Yes", new DialogInterface.OnClickListener() { // from class: com.epsxe.ePSXe.FileChooser.9
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialog, int which) {
                    String folder;
                    try {
                        String mfolder = ContextCompat.getDataDir(FileChooser.this).getAbsolutePath() + "/" + name + "/";
                        int lastPeriodPos = mfolder.lastIndexOf(46);
                        folder = lastPeriodPos > 0 ? mfolder.substring(0, lastPeriodPos) : mfolder;
                        File ffolder = new File(folder);
                        if (!ffolder.exists()) {
                            ffolder.mkdirs();
                        }
                        if (!ffolder.exists() || !ffolder.isDirectory()) {
                            folder = ContextCompat.getDataDir(FileChooser.this).getAbsolutePath();
                        }
                    } catch (Exception e) {
                        folder = ContextCompat.getDataDir(FileChooser.this).getAbsolutePath();
                    }
                    new DecodeSevenZipTask(a, c, plugin, FileChooser.this.is64bits).execute(path, folder);
                }
            });
        } else if (this.fcMode.equals("SELECT_BIOS")) {
            alertDialog.setButton2("Yes", new DialogInterface.OnClickListener() { // from class: com.epsxe.ePSXe.FileChooser.10
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialog, int which) {
                    String folder;
                    try {
                        String mfolder = ContextCompat.getDataDir(FileChooser.this).getAbsolutePath() + "/epsxe/bios/";
                        folder = mfolder;
                        File ffolder = new File(folder);
                        if (!ffolder.exists()) {
                            ffolder.mkdirs();
                        }
                        if (!ffolder.exists() || !ffolder.isDirectory()) {
                            folder = ContextCompat.getDataDir(FileChooser.this).getAbsolutePath();
                        }
                    } catch (Exception e) {
                        folder = ContextCompat.getDataDir(FileChooser.this).getAbsolutePath();
                    }
                    new DecodeSevenZipTask(a, c, plugin, FileChooser.this.is64bits).execute(path, folder);
                }
            });
        }
        alertDialog.setButton("No", new DialogInterface.OnClickListener() { // from class: com.epsxe.ePSXe.FileChooser.11
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertDialog.setIcon(android.R.drawable.ic_menu_directions);
        alertDialog.show();
    }

    private void onFileClickDropbox(String code) {
        Intent myIntent = new Intent(this, (Class<?>) DropboxManager.class);
        myIntent.putExtra("com.epsxe.ePSXe.psexe", code);
        myIntent.putExtra("com.epsxe.ePSXe.activity", "FileChooser");
        startActivity(myIntent);
        finish();
    }

    private void onFileClickGdrive(String code) {
        Intent myIntent = new Intent(this, (Class<?>) GdriveManager.class);
        myIntent.putExtra("com.epsxe.ePSXe.psexe", code);
        myIntent.putExtra("com.epsxe.ePSXe.activity", "FileChooser");
        startActivity(myIntent);
        finish();
    }

    private void onFileClick(String name, String path, int slot, String padtype) {
        String pathlibFolder;
        if (this.fcMode.equals("SELECT_ISO")) {
            String index = this.f120d.getECMToIndex(path);
            Log.e("IndexECMTask", "onFileClick " + index + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + path);
            if (!index.equals("OK")) {
                new IndexECMTask(this, this, this.serverMode, this.emu_xperiaplay).execute(index, path, "" + slot);
                return;
            }
            Toast.makeText(this, getString(R.string.file_isoselected) + name, 0).show();
            Intent myIntent = this.emu_xperiaplay == 1 ? new Intent(this, (Class<?>) ePSXeNative.class) : new Intent(this, (Class<?>) ePSXe.class);
            myIntent.putExtra("com.epsxe.ePSXe.isoName", path);
            myIntent.putExtra("com.epsxe.ePSXe.isoSlot", "" + slot);
            if (padtype != null) {
                myIntent.putExtra("com.epsxe.ePSXe.padType", padtype);
            }
            myIntent.putExtra("com.epsxe.ePSXe.servermode", "" + this.serverMode);
            startActivity(myIntent);
            finish();
            return;
        }
        if (this.fcMode.equals("SELECT_BIOS")) {
            if (FileUtil.compressed7z(path)) {
                try {
                    pathlibFolder = getPackageManager().getPackageInfo("com.epsxe.sevenzip", 0).applicationInfo.dataDir + "/lib";
                } catch (Exception e) {
                    pathlibFolder = "";
                }
                String plugin = pathlibFolder + "/libsevenzip.so";
                File ff = new File(plugin);
                if (ff.exists()) {
                    action_uncompress(this, this, name, path, plugin);
                    return;
                }
                int x86 = this.f119a.isX86();
                String pluginold = this.libFolder + "/libsevenzip.so";
                if (x86 == 1) {
                    pluginold = this.libFolder + "/libsevenzip_intel.so";
                }
                File fff = new File(pluginold);
                if (fff.exists()) {
                    action_uncompress(this, this, name, path, pluginold);
                    return;
                } else {
                    alertdialog_downloadunc();
                    return;
                }
            }
            Toast.makeText(this, getString(R.string.file_biosselected) + name, 0).show();
            Intent myIntent2 = new Intent(this, (Class<?>) ePSXePreferences.class);
            myIntent2.putExtra("com.epsxe.ePSXe.biosName", path);
            myIntent2.putExtra("com.epsxe.ePSXe.screen", "biospreferences");
            startActivity(myIntent2);
            finish();
            return;
        }
        if (this.fcMode.equals("SELECT_GPU")) {
            Toast.makeText(this, getString(R.string.file_gpuselected) + name, 0).show();
            Intent myIntent3 = new Intent(this, (Class<?>) ePSXePreferences.class);
            myIntent3.putExtra("com.epsxe.ePSXe.gpuName", path);
            myIntent3.putExtra("com.epsxe.ePSXe.screen", "videopreferences");
            startActivity(myIntent3);
            finish();
            return;
        }
        if (this.fcMode.equals("SELECT_SHADER")) {
            Toast.makeText(this, getString(R.string.file_shaderselected) + name, 0).show();
            Intent myIntent4 = new Intent(this, (Class<?>) ePSXePreferences.class);
            myIntent4.putExtra("com.epsxe.ePSXe.gpushaderName", path);
            myIntent4.putExtra("com.epsxe.ePSXe.screen", "videopreferences");
            startActivity(myIntent4);
            finish();
            return;
        }
        if (this.fcMode.equals("SELECT_SKIN")) {
            Toast.makeText(this, getString(R.string.file_ksinselected) + name, 0).show();
            Intent myIntent5 = new Intent(this, (Class<?>) ePSXePreferences.class);
            myIntent5.putExtra("com.epsxe.ePSXe.skinName", path);
            myIntent5.putExtra("com.epsxe.ePSXe.screen", "inputpreferences.virtualPad");
            startActivity(myIntent5);
            finish();
            return;
        }
        if (this.fcMode.equals("SELECT_MCR1")) {
            Toast.makeText(this, getString(R.string.file_mcr1selected) + name, 0).show();
            Intent myIntent6 = new Intent(this, (Class<?>) ePSXePreferences.class);
            myIntent6.putExtra("com.epsxe.ePSXe.mcr1Name", path);
            myIntent6.putExtra("com.epsxe.ePSXe.screen", "memcardpreferences");
            startActivity(myIntent6);
            finish();
            return;
        }
        if (this.fcMode.equals("SELECT_MCR2")) {
            Toast.makeText(this, getString(R.string.file_mcr2selected) + name, 0).show();
            Intent myIntent7 = new Intent(this, (Class<?>) ePSXePreferences.class);
            myIntent7.putExtra("com.epsxe.ePSXe.mcr2Name", path);
            myIntent7.putExtra("com.epsxe.ePSXe.screen", "memcardpreferences");
            startActivity(myIntent7);
            finish();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doback() {
        Intent myIntent;
        Toast.makeText(this, R.string.file_back, 0).show();
        if (this.fcMode.equals("SELECT_BIOS") || this.fcMode.equals("SELECT_GPU") || this.fcMode.equals("SELECT_SKIN") || this.fcMode.equals("SELECT_MCR1") || this.fcMode.equals("SELECT_MCR2") || this.fcMode.equals("SELECT_DROPBOX") || this.fcMode.equals("SELECT_GDRIVE") || this.fcMode.equals("SELECT_SHADER")) {
            myIntent = new Intent(this, (Class<?>) ePSXePreferences.class);
        } else {
            myIntent = new Intent(this, (Class<?>) ePSXe.class);
        }
        if (this.fcMode.equals("SELECT_BIOS")) {
            myIntent.putExtra("com.epsxe.ePSXe.screen", "biospreferences");
        } else if (this.fcMode.equals("SELECT_GPU") || this.fcMode.equals("SELECT_SHADER")) {
            myIntent.putExtra("com.epsxe.ePSXe.screen", "videopreferences");
        } else if (this.fcMode.equals("SELECT_MCR1") || this.fcMode.equals("SELECT_MCR2") || this.fcMode.equals("SELECT_DROPBOX") || this.fcMode.equals("SELECT_GDRIVE")) {
            myIntent.putExtra("com.epsxe.ePSXe.screen", "memcardpreferences");
        } else if (this.fcMode.equals("SELECT_SKIN")) {
            myIntent.putExtra("com.epsxe.ePSXe.screen", "inputpreferences.virtualPad");
        }
        startActivity(myIntent);
        finish();
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4 && event.getRepeatCount() == 0) {
            boolean isAndroidTV = DeviceUtil.isAndroidTV(this);
            if (isAndroidTV && this.fcMode.equals("SELECT_GPU")) {
                alertdialog_plugin(this);
                return true;
            }
            if (isAndroidTV && this.fcMode.equals("SELECT_SHADER")) {
                alertdialog_shader(this);
                return true;
            }
            if (isAndroidTV && (this.fcMode.equals("SELECT_MCR1") || this.fcMode.equals("SELECT_MCR2"))) {
                alertdialog_mcd(this);
                return true;
            }
            if (isAndroidTV && this.fcMode.equals("SELECT_ISO")) {
                alertdialog_reload_tv(this);
                return true;
            }
            doback();
        }
        return super.onKeyDown(keyCode, event);
    }

    public class Option implements Comparable<Option> {
        private String data;
        private String name;
        private String path;
        private int slot;

        public Option(String n, String d, String p, int s) {
            this.name = n;
            this.data = d;
            this.path = p;
            this.slot = s;
        }

        public String getName() {
            return this.name;
        }

        public String getData() {
            return this.data;
        }

        public String getPath() {
            return this.path;
        }

        public int getSlot() {
            return this.slot;
        }

        @Override // java.lang.Comparable
        public int compareTo(Option o) {
            if (this.name != null) {
                return this.name.toLowerCase().compareTo(o.getName().toLowerCase());
            }
            throw new IllegalArgumentException();
        }
    }

    public class FileArrayAdapter extends ArrayAdapter<Option> {

        /* renamed from: c */
        private Context f121c;

        /* renamed from: id */
        private int f122id;
        private List<Option> items;

        public FileArrayAdapter(Context context, int textViewResourceId, List<Option> objects) {
            super(context, textViewResourceId, objects);
            this.f121c = context;
            this.f122id = textViewResourceId;
            this.items = objects;
        }

        @Override // android.widget.ArrayAdapter, android.widget.Adapter
        public Option getItem(int i) {
            return this.items.get(i);
        }

        @Override // android.widget.ArrayAdapter, android.widget.Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) this.f121c.getSystemService("layout_inflater");
                v = vi.inflate(this.f122id, (ViewGroup) null);
            }
            Option o = this.items.get(position);
            if (o != null) {
                TextView t1 = (TextView) v.findViewById(R.id.TextView01);
                TextView t2 = (TextView) v.findViewById(R.id.TextView02);
                if (t1 != null) {
                    t1.setText(o.getName());
                }
                if (t2 != null) {
                    t2.setText(o.getData());
                }
            }
            return v;
        }
    }

    public class FileDescArrayAdapter extends ArrayAdapter<OptionDesc> {
        private Activity activity;

        /* renamed from: c */
        private Context f123c;

        /* renamed from: id */
        private int f124id;
        public ImageLoader imageLoader;
        private List<OptionDesc> items;

        public FileDescArrayAdapter(Activity a, Context context, int textViewResourceId, List<OptionDesc> objects) {
            super(context, textViewResourceId, objects);
            this.f123c = context;
            this.activity = a;
            this.f124id = textViewResourceId;
            this.items = objects;
            this.imageLoader = new ImageLoader(this.activity.getApplicationContext());
        }

        @Override // android.widget.ArrayAdapter, android.widget.Adapter
        public OptionDesc getItem(int i) {
            return this.items.get(i);
        }

        @Override // android.widget.ArrayAdapter, android.widget.Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            String val;
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) this.f123c.getSystemService("layout_inflater");
                v = vi.inflate(this.f124id, (ViewGroup) null);
            }
            OptionDesc o = this.items.get(position);
            if (o != null) {
                TextView t1 = (TextView) v.findViewById(R.id.TextView01);
                TextView t2 = (TextView) v.findViewById(R.id.TextView02);
                TextView t3 = (TextView) v.findViewById(R.id.TextView03);
                TextView t4 = (TextView) v.findViewById(R.id.TextView04);
                TextView t5 = (TextView) v.findViewById(R.id.TextView05);
                TextView t6 = (TextView) v.findViewById(R.id.TextView06);
                ImageView v1 = (ImageView) v.findViewById(R.id.image);
                if (t1 != null) {
                    String nameJP = o.getNameJP();
                    if (!FileChooser.this.locale.equals("ja") || nameJP.length() <= 2) {
                        t1.setText(o.getName());
                    } else {
                        t1.setText(nameJP);
                    }
                }
                if (t2 != null) {
                    t2.setText(o.getText());
                }
                String code = o.getCountry();
                if (t3 != null) {
                    t3.setText("");
                }
                if (t4 != null) {
                    t4.setText(o.getCountry());
                }
                String padtype = o.getPadType();
                val = "";
                if (padtype != null && padtype.length() > 0) {
                    int padType = Integer.parseInt(padtype);
                    val = (padType & 1) == 1 ? "Digital" : "";
                    if ((padType & 2) == 2) {
                        if (!val.equals("")) {
                            val = val + ",";
                        }
                        val = val + "Analog";
                    }
                    if ((padType & 4) == 4) {
                        if (!val.equals("")) {
                            val = val + ",";
                        }
                        val = val + "DualShock";
                    }
                    if ((padType & 16) == 16) {
                        if (!val.equals("")) {
                            val = val + ",";
                        }
                        val = val + "Mouse";
                    }
                    if ((padType & 32) == 32) {
                        if (!val.equals("")) {
                            val = val + ",";
                        }
                        val = val + "Justifier";
                    }
                    if ((padType & 64) == 64) {
                        if (!val.equals("")) {
                            val = val + ",";
                        }
                        val = val + "Gun";
                    }
                    if (o.getMultitap().equals("yes")) {
                        if (!val.equals("")) {
                            val = val + ",";
                        }
                        val = val + "Multitap";
                    }
                }
                if (val.equals("")) {
                    val = "Unknown";
                }
                if (t5 != null) {
                    t5.setText(val);
                }
                if (t6 != null) {
                    if (code.contains("/")) {
                        code.split("/");
                        t6.setText("");
                        v.setBackgroundDrawable(FileChooser.this.getResources().getDrawable(R.drawable.list_selector));
                    } else {
                        t6.setText("");
                        v.setBackgroundDrawable(FileChooser.this.getResources().getDrawable(R.drawable.list_selector));
                    }
                    if (FileChooser.this.fcMode.equals("SELECT_DROPBOX") || FileChooser.this.fcMode.equals("SELECT_GDRIVE")) {
                        if (o.getFile().equals("NONE")) {
                            t6.setText("Game not installed");
                            v.setBackgroundDrawable(FileChooser.this.getResources().getDrawable(R.drawable.list_selector));
                        } else {
                            t6.setText("");
                            v.setBackgroundDrawable(FileChooser.this.getResources().getDrawable(R.drawable.list_selector));
                        }
                    }
                }
                this.imageLoader.DisplayImage(o.getCode(), v1);
            }
            return v;
        }
    }

    public static void parsecue(File cueFile) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(cueFile));
        while (true) {
            String line = reader.readLine();
            if (line != null) {
                Scanner tok = new Scanner(line);
                if (tok.hasNext()) {
                    String token = tok.next();
                    if ("FILE".equalsIgnoreCase(token)) {
                        String nextToken = tok.next();
                        if (nextToken.startsWith(QUOTATION_MARK)) {
                            if (nextToken.endsWith(QUOTATION_MARK)) {
                                nextToken = nextToken.substring(1, nextToken.length() - 1);
                            } else {
                                nextToken = nextToken.substring(1) + tok.useDelimiter(QUOTATION_MARK).next();
                            }
                        }
                        Log.e("cuefile", "" + nextToken);
                    }
                }
            } else {
                reader.close();
                return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void saveFolders(List<File> dirs) {
        try {
            File root = new File(ContextCompat.getDataDir(this), "epsxe/config/");
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, "folders");
            FileWriter writer = new FileWriter(gpxfile);
            for (File f : dirs) {
                writer.append((CharSequence) (f.getAbsoluteFile() + "\n"));
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int loadFolders(List<File> dirs) {
        int res = 0;
        try {
            File f = new File(ContextCompat.getDataDir(this), "epsxe/config/folders");
            if (f.exists()) {
                BufferedReader br = new BufferedReader(new FileReader(f));
                while (true) {
                    String line = br.readLine();
                    if (line == null) {
                        break;
                    }
                    File e = new File(line);
                    if (f.exists()) {
                        this.gFolders.add(e);
                        res++;
                    }
                }
            }
            return res;
        } catch (IOException e2) {
            e2.printStackTrace();
            return res;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void action_gamelist() {
        if (this.fcMode.equals("SELECT_ISO")) {
            Intent myIntent = new Intent(this, (Class<?>) gFileChooser.class);
            myIntent.putExtra("com.epsxe.ePSXe.fcMode", "SELECT_ISO");
            myIntent.putExtra("com.epsxe.ePSXe.isoPath", this.currentDir.getAbsolutePath());
            myIntent.putExtra("com.epsxe.ePSXe.browserMode", "1");
            myIntent.putExtra("com.epsxe.ePSXe.reloadMode", "0");
            startActivity(myIntent);
            finish();
        }
    }

    private void action_gamedetails() {
        if (this.fcMode.equals("SELECT_ISO")) {
            Intent myIntent = new Intent(this, (Class<?>) FileChooser.class);
            myIntent.putExtra("com.epsxe.ePSXe.fcMode", "SELECT_ISO");
            myIntent.putExtra("com.epsxe.ePSXe.isoPath", this.currentDir.getAbsolutePath());
            myIntent.putExtra("com.epsxe.ePSXe.browserMode", "2");
            myIntent.putExtra("com.epsxe.ePSXe.reloadMode", "0");
            startActivity(myIntent);
            finish();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void action_filebrowser() {
        if (this.fcMode.equals("SELECT_ISO")) {
            Intent myIntent = new Intent(this, (Class<?>) FileChooser.class);
            myIntent.putExtra("com.epsxe.ePSXe.fcMode", "SELECT_ISO");
            myIntent.putExtra("com.epsxe.ePSXe.isoPath", this.currentDir.getAbsolutePath());
            myIntent.putExtra("com.epsxe.ePSXe.browserMode", "0");
            myIntent.putExtra("com.epsxe.ePSXe.reloadMode", "0");
            startActivity(myIntent);
            finish();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void action_rescan() {
        if (this.fcMode.equals("SELECT_ISO")) {
            alertdialog_reload();
        }
    }

    public void alertdialog_help() {
        TextView message = new TextView(this);
        SpannableString s = new SpannableString(getText(R.string.game_notfound_message));
        message.setText(s);
        message.setMovementMethod(LinkMovementMethod.getInstance());
        AlertDialog builder = new AlertDialog.Builder(this).setTitle(R.string.game_notfound_title).setCancelable(true).setIcon(android.R.drawable.ic_dialog_info).setPositiveButton(R.string.dialog_action_ok, (DialogInterface.OnClickListener) null).setView(message).create();
        builder.show();
    }

    private void action_help() {
        if (this.fcMode.equals("SELECT_ISO")) {
            alertdialog_help();
        }
    }

    private void add_quickButtons() {
        this.button_game_list = (Button) findViewById(R.id.button1);
        this.button_game_list.setOnClickListener(new View.OnClickListener() { // from class: com.epsxe.ePSXe.FileChooser.12
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                FileChooser.this.action_gamelist();
            }
        });
        this.button_browse_files = (Button) findViewById(R.id.button2);
        this.button_browse_files.setOnClickListener(new View.OnClickListener() { // from class: com.epsxe.ePSXe.FileChooser.13
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                FileChooser.this.action_filebrowser();
            }
        });
        this.button_reload = (Button) findViewById(R.id.button3);
        this.button_reload.setOnClickListener(new View.OnClickListener() { // from class: com.epsxe.ePSXe.FileChooser.14
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                FileChooser.this.action_rescan();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getsdcardname() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(R.string.file_creatememcard);
        alert.setMessage(getString(R.string.file_creatememcardfile));
        this.sdcardname = "";
        final EditText input = new EditText(this);
        input.setInputType(524465);
        alert.setView(input);
        alert.setPositiveButton(getString(R.string.file_ok), new DialogInterface.OnClickListener() { // from class: com.epsxe.ePSXe.FileChooser.15
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int whichButton) {
                FileChooser.this.sdcardname = input.getText().toString();
                if (PSXUtil.create_memcard(FileChooser.this.currentDir.getAbsolutePath() + "/" + FileChooser.this.sdcardname)) {
                    FileChooser.this.fillSimple(FileChooser.this.currentDir);
                }
            }
        });
        alert.setNegativeButton(getString(R.string.file_cancel), new DialogInterface.OnClickListener() { // from class: com.epsxe.ePSXe.FileChooser.16
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int whichButton) {
                FileChooser.this.sdcardname = "";
            }
        });
        alert.show();
    }

    private void add_quickButtonsSD() {
        this.button_game_list = (Button) findViewById(R.id.button1);
        this.button_game_list.setOnClickListener(new View.OnClickListener() { // from class: com.epsxe.ePSXe.FileChooser.17
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                FileChooser.this.getsdcardname();
            }
        });
    }

    private void add_quickButtonsGPU(Context c, int x) {
        this.button_game_list = (Button) findViewById(R.id.button1);
        this.button_game_list.setOnClickListener(new View.OnClickListener() { // from class: com.epsxe.ePSXe.FileChooser.18
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                FileChooser.this.alertdialog_downloadgpu();
            }
        });
    }

    private void add_quickButtonsSHADER(final Context c, final int x) {
        this.button_game_list = (Button) findViewById(R.id.button1);
        this.button_game_list.setOnClickListener(new View.OnClickListener() { // from class: com.epsxe.ePSXe.FileChooser.19
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                FileChooser.this.alertdialog_downloadshader(x, c);
            }
        });
    }

    private void alertdialog_reload() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(R.string.file_rescansdcard);
        alertDialog.setMessage(getString(R.string.file_rescansdcardask));
        alertDialog.setButton(getString(R.string.file_rescansdcardsdcard), new DialogInterface.OnClickListener() { // from class: com.epsxe.ePSXe.FileChooser.20
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                Intent myIntent = new Intent(FileChooser.this, (Class<?>) FileChooser.class);
                myIntent.putExtra("com.epsxe.ePSXe.fcMode", "SELECT_ISO");
                myIntent.putExtra("com.epsxe.ePSXe.isoPath", FileChooser.this.currentDir.getAbsolutePath());
                myIntent.putExtra("com.epsxe.ePSXe.browserMode", "1");
                myIntent.putExtra("com.epsxe.ePSXe.reloadMode", "1");
                FileChooser.this.startActivity(myIntent);
                FileChooser.this.finish();
            }
        });
        alertDialog.setButton2(getString(R.string.file_rescansdcardall), new DialogInterface.OnClickListener() { // from class: com.epsxe.ePSXe.FileChooser.21
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                Intent myIntent = new Intent(FileChooser.this, (Class<?>) FileChooser.class);
                myIntent.putExtra("com.epsxe.ePSXe.fcMode", "SELECT_ISO");
                myIntent.putExtra("com.epsxe.ePSXe.isoPath", FileChooser.this.currentDir.getAbsolutePath());
                myIntent.putExtra("com.epsxe.ePSXe.browserMode", "1");
                myIntent.putExtra("com.epsxe.ePSXe.reloadMode", "2");
                FileChooser.this.startActivity(myIntent);
                FileChooser.this.finish();
            }
        });
        alertDialog.setButton3(getString(R.string.file_rescansdcardexternal), new DialogInterface.OnClickListener() { // from class: com.epsxe.ePSXe.FileChooser.22
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                Intent myIntent = new Intent(FileChooser.this, (Class<?>) FileChooser.class);
                myIntent.putExtra("com.epsxe.ePSXe.fcMode", "SELECT_ISO");
                myIntent.putExtra("com.epsxe.ePSXe.isoPath", FileChooser.this.currentDir.getAbsolutePath());
                myIntent.putExtra("com.epsxe.ePSXe.browserMode", "1");
                myIntent.putExtra("com.epsxe.ePSXe.reloadMode", "3");
                FileChooser.this.startActivity(myIntent);
                FileChooser.this.finish();
            }
        });
        alertDialog.setIcon(android.R.drawable.ic_menu_directions);
        alertDialog.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void alertdialog_downloadgpu() {
        try {
            Intent goToMarket = new Intent("android.intent.action.VIEW").setData(Uri.parse("market://details?id=com.epsxe.opengl"));
            startActivity(goToMarket);
        } catch (Exception e) {
        }
    }

    private void alertdialog_downloadunc() {
        try {
            Intent goToMarket = new Intent("android.intent.action.VIEW").setData(Uri.parse("market://details?id=com.epsxe.sevenzip"));
            startActivity(goToMarket);
        } catch (Exception e) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void alertdialog_downloadshader(int x, final Context c) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(R.string.file_shadertitle);
        alertDialog.setMessage(getString(R.string.file_shadermsg));
        alertDialog.setButton2(getString(R.string.file_shaderyes), new DialogInterface.OnClickListener() { // from class: com.epsxe.ePSXe.FileChooser.23
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                new DownloadShaderPluginTask(c, FileChooser.this).execute(new Integer[0]);
            }
        });
        alertDialog.setButton(getString(R.string.file_shaderno), new DialogInterface.OnClickListener() { // from class: com.epsxe.ePSXe.FileChooser.24
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertDialog.setIcon(android.R.drawable.ic_menu_directions);
        alertDialog.show();
    }

    private void alertdialog_plugin(Context mCont) {
        String[] items = {"Download Plugin", "Back"};
        ListView gListView = new ListView(mCont);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(mCont, android.R.layout.simple_list_item_1, items);
        gListView.setAdapter((ListAdapter) adapter);
        gListView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.epsxe.ePSXe.FileChooser.25
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> listView, View itemView, int position, long itemId) {
                switch (position) {
                    case 0:
                        FileChooser.this.alertdialog_downloadgpu();
                        break;
                    case 1:
                        FileChooser.this.doback();
                        break;
                }
                DialogUtil.closeDialog(FileChooser.this.pluginAlert);
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(mCont);
        builder.setView(gListView);
        this.pluginAlert = builder.create();
        this.pluginAlert.show();
    }

    private void alertdialog_mcd(Context mCont) {
        String[] items = {"Create memcard", "Back"};
        ListView gListView = new ListView(mCont);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(mCont, android.R.layout.simple_list_item_1, items);
        gListView.setAdapter((ListAdapter) adapter);
        gListView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.epsxe.ePSXe.FileChooser.26
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> listView, View itemView, int position, long itemId) {
                switch (position) {
                    case 0:
                        FileChooser.this.getsdcardname();
                        break;
                    case 1:
                        FileChooser.this.doback();
                        break;
                }
                DialogUtil.closeDialog(FileChooser.this.mcdAlert);
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(mCont);
        builder.setView(gListView);
        this.mcdAlert = builder.create();
        this.mcdAlert.show();
    }

    private void alertdialog_reload_tv(Context mCont) {
        String[] items = {"Rescan ALL", "Rescan External", "Rescan SDCARD", "Back"};
        ListView gListView = new ListView(mCont);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(mCont, android.R.layout.simple_list_item_1, items);
        gListView.setAdapter((ListAdapter) adapter);
        gListView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.epsxe.ePSXe.FileChooser.27
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> listView, View itemView, int position, long itemId) {
                switch (position) {
                    case 0:
                        Intent myIntent = new Intent(FileChooser.this, (Class<?>) FileChooser.class);
                        myIntent.putExtra("com.epsxe.ePSXe.fcMode", "SELECT_ISO");
                        myIntent.putExtra("com.epsxe.ePSXe.isoPath", FileChooser.this.currentDir.getAbsolutePath());
                        myIntent.putExtra("com.epsxe.ePSXe.browserMode", "1");
                        myIntent.putExtra("com.epsxe.ePSXe.reloadMode", "2");
                        FileChooser.this.startActivity(myIntent);
                        FileChooser.this.finish();
                        break;
                    case 1:
                        Intent myIntent2 = new Intent(FileChooser.this, (Class<?>) FileChooser.class);
                        myIntent2.putExtra("com.epsxe.ePSXe.fcMode", "SELECT_ISO");
                        myIntent2.putExtra("com.epsxe.ePSXe.isoPath", FileChooser.this.currentDir.getAbsolutePath());
                        myIntent2.putExtra("com.epsxe.ePSXe.browserMode", "1");
                        myIntent2.putExtra("com.epsxe.ePSXe.reloadMode", "3");
                        FileChooser.this.startActivity(myIntent2);
                        FileChooser.this.finish();
                        break;
                    case 2:
                        Intent myIntent3 = new Intent(FileChooser.this, (Class<?>) FileChooser.class);
                        myIntent3.putExtra("com.epsxe.ePSXe.fcMode", "SELECT_ISO");
                        myIntent3.putExtra("com.epsxe.ePSXe.isoPath", FileChooser.this.currentDir.getAbsolutePath());
                        myIntent3.putExtra("com.epsxe.ePSXe.browserMode", "1");
                        myIntent3.putExtra("com.epsxe.ePSXe.reloadMode", "1");
                        FileChooser.this.startActivity(myIntent3);
                        FileChooser.this.finish();
                        break;
                    case 3:
                        FileChooser.this.doback();
                        break;
                }
                DialogUtil.closeDialog(FileChooser.this.reloadAlert);
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(mCont);
        builder.setView(gListView);
        this.reloadAlert = builder.create();
        this.reloadAlert.show();
    }

    private void alertdialog_shader(final Context mCont) {
        String[] items = {"Download Shaders", "Back"};
        ListView gListView = new ListView(mCont);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(mCont, android.R.layout.simple_list_item_1, items);
        gListView.setAdapter((ListAdapter) adapter);
        gListView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.epsxe.ePSXe.FileChooser.28
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> listView, View itemView, int position, long itemId) {
                switch (position) {
                    case 0:
                        FileChooser.this.alertdialog_downloadshader(FileChooser.this.f119a.isX86(), mCont);
                        break;
                    case 1:
                        FileChooser.this.doback();
                        break;
                }
                DialogUtil.closeDialog(FileChooser.this.shaderAlert);
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(mCont);
        builder.setView(gListView);
        this.shaderAlert = builder.create();
        this.shaderAlert.show();
    }
}
