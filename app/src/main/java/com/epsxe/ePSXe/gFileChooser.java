package com.epsxe.ePSXe;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.util.Log;
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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.epsxe.ePSXe.jni.libdetect;
import com.epsxe.ePSXe.jni.libgamedetect;
import com.epsxe.ePSXe.task.IndexECMTask;
import com.epsxe.ePSXe.util.CacheUtil;
import com.epsxe.ePSXe.util.DeviceUtil;
import com.epsxe.ePSXe.util.DialogUtil;
import com.epsxe.ePSXe.util.FileUtil;
import com.epsxe.ePSXe.util.ReportUtil;
import com.epsxe.ePSXe.util.ShortcutUtil;
import com.fasterxml.jackson.core.util.MinimalPrettyPrinter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import org.apache.http.cookie.ClientCookie;

/* loaded from: classes.dex */
public class gFileChooser extends Activity {
    private static final String QUOTATION_MARK = "\"";

    /* renamed from: a */
    private libdetect f177a;
    private GridViewAdapter adapterdesc;
    private Button button_browse_files;
    private Button button_game_list;
    private Button button_reload;
    private File currentDir;

    /* renamed from: d */
    private libgamedetect f178d;
    private GameInfo gameinfo;
    private GridView gridView;
    private AlertDialog reloadAlert;
    private String fcMode = "SELECT_ISO";
    private int browserMode = 2;
    private int reloadMode = 0;
    private int notfound = 0;
    private int tracescan = 0;
    private List<File> gFolders = new ArrayList();
    private int emu_xperiaplay = 0;
    private String sdcardname = "";
    private String locale = "en";
    private int serverMode = 0;
    private int version = Build.VERSION.SDK_INT;

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        int osVersion = Integer.parseInt(Build.VERSION.SDK);
        if (osVersion < 11 || !this.fcMode.equals("SELECT_ISO")) {
            return super.onCreateOptionsMenu(menu);
        }
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.filechooser_actions_grid, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override // android.app.Activity
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
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
            default:
                return super.onOptionsItemSelected(item);
            case R.id.action_rescan /* 2131034128 */:
                action_rescan();
                return true;
        }
    }

    @Override // android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String param = getIntent().getStringExtra("com.epsxe.ePSXe.fcMode");
        if (param != null && param.length() > 0) {
            this.fcMode = param;
        }
        this.locale = Locale.getDefault().getLanguage();
        DeviceUtil.setLocale(this);
        Log.e("epsxefolder", "Mode " + this.fcMode);
        String param2 = getIntent().getStringExtra("com.epsxe.ePSXe.isoPath");
        if (param2 != null && param2.length() > 0) {
            this.currentDir = new File(param2);
        } else {
            File extStore = ContextCompat.getDataDir(this);
            this.currentDir = new File(extStore.getAbsolutePath());
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
        String param5 = getIntent().getStringExtra("com.epsxe.ePSXe.xperiaplay");
        if (param5 != null && param5.length() > 0 && param5.contains("1")) {
            this.emu_xperiaplay = 1;
        } else {
            this.emu_xperiaplay = 0;
        }
        String param6 = getIntent().getStringExtra("com.epsxe.ePSXe.servermode");
        if (param6 != null && param6.length() > 0) {
            this.serverMode = Integer.parseInt(param6);
        } else {
            this.serverMode = 0;
        }
        File extStore2 = ContextCompat.getDataDir(this);
        this.f178d = new libgamedetect();
        this.f178d.setSdCardPath(extStore2.getAbsolutePath());
        this.gameinfo = new GameInfo(this, this.f178d);
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
        alertDialog.setButton(getString(R.string.file_crash_retry), new DialogInterface.OnClickListener() { // from class: com.epsxe.ePSXe.gFileChooser.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                gFileChooser.this.tracescan = 1;
                CacheUtil.deletewipscan();
                CacheUtil.deletetracescan();
                gFileChooser.this.scanForGames();
            }
        });
        alertDialog.setButton2(getString(R.string.file_crash_disable), new DialogInterface.OnClickListener() { // from class: com.epsxe.ePSXe.gFileChooser.2
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                CacheUtil.deletewipscan();
                Intent myIntent = new Intent(gFileChooser.this, (Class<?>) FileChooser.class);
                myIntent.putExtra("com.epsxe.ePSXe.fcMode", "SELECT_ISO");
                myIntent.putExtra("com.epsxe.ePSXe.isoPath", gFileChooser.this.currentDir.getAbsolutePath());
                myIntent.putExtra("com.epsxe.ePSXe.browserMode", "0");
                myIntent.putExtra("com.epsxe.ePSXe.reloadMode", "0");
                gFileChooser.this.startActivity(myIntent);
                gFileChooser.this.finish();
            }
        });
        File f = new File(ContextCompat.getDataDir(this), "epsxe/info/tracescan.txt");
        if (f.exists()) {
            alertDialog.setButton3(getString(R.string.file_crash_report), new DialogInterface.OnClickListener() { // from class: com.epsxe.ePSXe.gFileChooser.3
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialog, int which) {
                    ReportUtil.sendScanningCrashEmail(gFileChooser.this, "epsxe scanning crash", "attached scanning tracelog");
                    gFileChooser.this.browserMode = 0;
                    gFileChooser.this.reloadMode = 0;
                    gFileChooser.this.scanForGames();
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
        alertDialog.setButton(getString(R.string.file_shortcutlarge), new DialogInterface.OnClickListener() { // from class: com.epsxe.ePSXe.gFileChooser.4
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                ShortcutUtil scUtil = new ShortcutUtil();
                if (Build.VERSION.SDK_INT >= 26) {
                    scUtil.addShortcut26(gFileChooser.this.getApplicationContext(), ePSXe.class, path, slot, input.getText().toString(), code, 128, padtype);
                } else {
                    scUtil.addShortcut(gFileChooser.this.getApplicationContext(), ePSXe.class, path, slot, input.getText().toString(), code, 128, padtype);
                }
            }
        });
        alertDialog.setButton2(getString(R.string.file_shortcutcancel), new DialogInterface.OnClickListener() { // from class: com.epsxe.ePSXe.gFileChooser.5
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertDialog.setButton3(getString(R.string.file_shortcutsmall), new DialogInterface.OnClickListener() { // from class: com.epsxe.ePSXe.gFileChooser.6
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                ShortcutUtil scUtil = new ShortcutUtil();
                if (Build.VERSION.SDK_INT >= 26) {
                    scUtil.addShortcut26(gFileChooser.this.getApplicationContext(), ePSXe.class, path, slot, input.getText().toString(), code, 64, padtype);
                } else {
                    scUtil.addShortcut(gFileChooser.this.getApplicationContext(), ePSXe.class, path, slot, input.getText().toString(), code, 64, padtype);
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
                if (checkGfolders(gFileChooser.this.gFolders)) {
                    publishProgress("saving folders...");
                    gFileChooser.this.saveFolders(gFileChooser.this.gFolders);
                    publishProgress("saving folders done");
                }
            } else {
                for (int i = 0; i < params.length; i++) {
                    File f = new File(params[i]);
                    if (f.exists()) {
                        findFolders(params[i], 0);
                    }
                }
                gFileChooser.this.saveFolders(gFileChooser.this.gFolders);
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
                gFileChooser.this.generateList();
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
                    if (!f.getName().equals("DCIM") && !f.getName().equals("Camera") && !f.getName().equals("asec") && !f.getName().equals(ClientCookie.SECURE_ATTR) && !f.getName().equals("dev") && !f.getName().equals("obb") && !f.getName().equals("\\.lfs")) {
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
                                            if (!gFileChooser.this.gFolders.contains(a) && ((!f.getCanonicalPath().startsWith("/storage/emulated/0") && !f.getCanonicalPath().startsWith("/storage/emulated/1")) || gFileChooser.this.version > 22)) {
                                                gFileChooser.this.gFolders.add(a);
                                            }
                                            added = 1;
                                        }
                                    } else if (FileUtil.acceptPSX(file.getName())) {
                                        File a2 = new File(f.getCanonicalPath());
                                        if (!gFileChooser.this.gFolders.contains(a2) && ((!f.getCanonicalPath().startsWith("/storage/emulated/0") && !f.getCanonicalPath().startsWith("/storage/emulated/1")) || gFileChooser.this.version > 22)) {
                                            gFileChooser.this.gFolders.add(a2);
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
        printGrid();
        if (this.browserMode == 1 || this.browserMode == 2) {
            if (this.gFolders.size() != 0) {
                fillDesc(this.gFolders);
            } else {
                this.gFolders.add(this.currentDir);
                fillDesc(this.gFolders);
            }
        }
        int osVersion = Integer.parseInt(Build.VERSION.SDK);
        if (osVersion < 11) {
            add_quickButtons();
        }
        this.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.epsxe.ePSXe.gFileChooser.7
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                OptionDesc o = gFileChooser.this.adapterdesc.getItem(position);
                gFileChooser.this.onFileClick(o.getName(), o.getPath(), o.getSlot(), o.getPadType());
            }
        });
        if (!DeviceUtil.isAndroidTV(this)) {
            this.gridView.setLongClickable(true);
            this.gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() { // from class: com.epsxe.ePSXe.gFileChooser.8
                @Override // android.widget.AdapterView.OnItemLongClickListener
                public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id) {
                    if (gFileChooser.this.browserMode == 1 || gFileChooser.this.browserMode == 2) {
                        OptionDesc o = gFileChooser.this.adapterdesc.getItem(position);
                        if (gFileChooser.this.fcMode.equals("SELECT_ISO")) {
                            gFileChooser.this.alertdialog_shortcut(o.getPath(), o.getSlot(), o.getName(), o.getCode(), o.getPadType());
                        }
                    }
                    return true;
                }
            });
        }
        this.gridView.setSelection(0);
        this.gridView.requestFocusFromTouch();
        this.gridView.setSelection(0);
    }

    private void printGrid() {
        int osVersion = Integer.parseInt(Build.VERSION.SDK);
        if (osVersion >= 11) {
            setContentView(R.layout.grid_viewv11);
        } else {
            setContentView(R.layout.grid_view2);
        }
    }

    private void fillDesc(List<File> dirs) {
        ((WindowManager) getSystemService("window")).getDefaultDisplay();
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
        Log.e("filechooser", "Number of games found: " + fls.size());
        if (fls.size() > 0) {
            Collections.sort(fls);
            CacheUtil.saveCache(fls, dirs);
            this.gridView = (GridView) findViewById(R.id.gridView);
            this.adapterdesc = new GridViewAdapter(this, this, R.layout.row_grid, fls);
            this.gridView.setAdapter((ListAdapter) this.adapterdesc);
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

    /* JADX INFO: Access modifiers changed from: private */
    public void onFileClick(String name, String path, int slot, String padtype) {
        if (this.fcMode.equals("SELECT_ISO")) {
            String index = this.f178d.getECMToIndex(path);
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
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doback() {
        Toast.makeText(this, R.string.file_back, 0).show();
        Intent myIntent = new Intent(this, (Class<?>) ePSXe.class);
        startActivity(myIntent);
        finish();
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4 && event.getRepeatCount() == 0) {
            if (DeviceUtil.isAndroidTV(this) && this.fcMode.equals("SELECT_ISO")) {
                alertdialog_reload_tv(this);
                return true;
            }
            doback();
        }
        return super.onKeyDown(keyCode, event);
    }

    public class GridViewAdapter extends ArrayAdapter<OptionDesc> {
        private Activity activity;

        /* renamed from: c */
        private Context f179c;

        /* renamed from: id */
        private int f180id;
        public ImageLoader imageLoader;
        private List<OptionDesc> items;

        public GridViewAdapter(Activity a, Context context, int textViewResourceId, List<OptionDesc> objects) {
            super(context, textViewResourceId, objects);
            this.f179c = context;
            this.activity = a;
            this.f180id = textViewResourceId;
            this.items = objects;
            this.imageLoader = new ImageLoader(this.activity.getApplicationContext());
        }

        @Override // android.widget.ArrayAdapter, android.widget.Adapter
        public OptionDesc getItem(int i) {
            return this.items.get(i);
        }

        @Override // android.widget.ArrayAdapter, android.widget.Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) this.f179c.getSystemService("layout_inflater");
                v = vi.inflate(this.f180id, parent, false);
            }
            OptionDesc o = this.items.get(position);
            if (o != null) {
                TextView t1 = (TextView) v.findViewById(R.id.text);
                ImageView v1 = (ImageView) v.findViewById(R.id.image);
                if (t1 != null) {
                    String nameJP = o.getNameJP();
                    if (!gFileChooser.this.locale.equals("ja") || nameJP.length() <= 2) {
                        t1.setText(o.getName());
                    } else {
                        t1.setText(nameJP);
                    }
                }
                this.imageLoader.DisplayImage(o.getCode(), v1);
            }
            return v;
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

    private void action_gamelist() {
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

    /* JADX INFO: Access modifiers changed from: private */
    public void action_gamedetails() {
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

    private void add_quickButtons() {
        this.button_game_list = (Button) findViewById(R.id.button1);
        this.button_game_list.setOnClickListener(new View.OnClickListener() { // from class: com.epsxe.ePSXe.gFileChooser.9
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                gFileChooser.this.action_gamedetails();
            }
        });
        this.button_browse_files = (Button) findViewById(R.id.button2);
        this.button_browse_files.setOnClickListener(new View.OnClickListener() { // from class: com.epsxe.ePSXe.gFileChooser.10
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                gFileChooser.this.action_filebrowser();
            }
        });
        this.button_reload = (Button) findViewById(R.id.button3);
        this.button_reload.setOnClickListener(new View.OnClickListener() { // from class: com.epsxe.ePSXe.gFileChooser.11
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                gFileChooser.this.action_rescan();
            }
        });
    }

    private void alertdialog_reload() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(R.string.file_rescansdcard);
        alertDialog.setMessage(getString(R.string.file_rescansdcardask));
        alertDialog.setButton(getString(R.string.file_rescansdcardsdcard), new DialogInterface.OnClickListener() { // from class: com.epsxe.ePSXe.gFileChooser.12
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                if (gFileChooser.this.browserMode == 2) {
                    gFileChooser.this.browserMode = 1;
                }
                Intent myIntent = new Intent(gFileChooser.this, (Class<?>) gFileChooser.class);
                myIntent.putExtra("com.epsxe.ePSXe.fcMode", "SELECT_ISO");
                myIntent.putExtra("com.epsxe.ePSXe.isoPath", gFileChooser.this.currentDir.getAbsolutePath());
                myIntent.putExtra("com.epsxe.ePSXe.browserMode", "2");
                myIntent.putExtra("com.epsxe.ePSXe.reloadMode", "1");
                gFileChooser.this.startActivity(myIntent);
                gFileChooser.this.finish();
            }
        });
        alertDialog.setButton2(getString(R.string.file_rescansdcardall), new DialogInterface.OnClickListener() { // from class: com.epsxe.ePSXe.gFileChooser.13
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                if (gFileChooser.this.browserMode == 2) {
                    gFileChooser.this.browserMode = 1;
                }
                Intent myIntent = new Intent(gFileChooser.this, (Class<?>) gFileChooser.class);
                myIntent.putExtra("com.epsxe.ePSXe.fcMode", "SELECT_ISO");
                myIntent.putExtra("com.epsxe.ePSXe.isoPath", gFileChooser.this.currentDir.getAbsolutePath());
                myIntent.putExtra("com.epsxe.ePSXe.browserMode", "2");
                myIntent.putExtra("com.epsxe.ePSXe.reloadMode", "2");
                gFileChooser.this.startActivity(myIntent);
                gFileChooser.this.finish();
            }
        });
        alertDialog.setButton3(getString(R.string.file_rescansdcardexternal), new DialogInterface.OnClickListener() { // from class: com.epsxe.ePSXe.gFileChooser.14
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                if (gFileChooser.this.browserMode == 2) {
                    gFileChooser.this.browserMode = 1;
                }
                Intent myIntent = new Intent(gFileChooser.this, (Class<?>) gFileChooser.class);
                myIntent.putExtra("com.epsxe.ePSXe.fcMode", "SELECT_ISO");
                myIntent.putExtra("com.epsxe.ePSXe.isoPath", gFileChooser.this.currentDir.getAbsolutePath());
                myIntent.putExtra("com.epsxe.ePSXe.browserMode", "2");
                myIntent.putExtra("com.epsxe.ePSXe.reloadMode", "3");
                gFileChooser.this.startActivity(myIntent);
                gFileChooser.this.finish();
            }
        });
        alertDialog.setIcon(android.R.drawable.ic_menu_directions);
        alertDialog.show();
    }

    private void alertdialog_reload_tv(Context mCont) {
        String[] items = {"Rescan ALL", "Rescan External", "Rescan SDCARD", "Back"};
        ListView gListView = new ListView(mCont);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(mCont, android.R.layout.simple_list_item_1, items);
        gListView.setAdapter((ListAdapter) adapter);
        gListView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.epsxe.ePSXe.gFileChooser.15
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> listView, View itemView, int position, long itemId) {
                switch (position) {
                    case 0:
                        Intent myIntent = new Intent(gFileChooser.this, (Class<?>) gFileChooser.class);
                        myIntent.putExtra("com.epsxe.ePSXe.fcMode", "SELECT_ISO");
                        myIntent.putExtra("com.epsxe.ePSXe.isoPath", gFileChooser.this.currentDir.getAbsolutePath());
                        myIntent.putExtra("com.epsxe.ePSXe.browserMode", "1");
                        myIntent.putExtra("com.epsxe.ePSXe.reloadMode", "2");
                        gFileChooser.this.startActivity(myIntent);
                        gFileChooser.this.finish();
                        break;
                    case 1:
                        Intent myIntent2 = new Intent(gFileChooser.this, (Class<?>) gFileChooser.class);
                        myIntent2.putExtra("com.epsxe.ePSXe.fcMode", "SELECT_ISO");
                        myIntent2.putExtra("com.epsxe.ePSXe.isoPath", gFileChooser.this.currentDir.getAbsolutePath());
                        myIntent2.putExtra("com.epsxe.ePSXe.browserMode", "1");
                        myIntent2.putExtra("com.epsxe.ePSXe.reloadMode", "3");
                        gFileChooser.this.startActivity(myIntent2);
                        gFileChooser.this.finish();
                        break;
                    case 2:
                        Intent myIntent3 = new Intent(gFileChooser.this, (Class<?>) gFileChooser.class);
                        myIntent3.putExtra("com.epsxe.ePSXe.fcMode", "SELECT_ISO");
                        myIntent3.putExtra("com.epsxe.ePSXe.isoPath", gFileChooser.this.currentDir.getAbsolutePath());
                        myIntent3.putExtra("com.epsxe.ePSXe.browserMode", "1");
                        myIntent3.putExtra("com.epsxe.ePSXe.reloadMode", "1");
                        gFileChooser.this.startActivity(myIntent3);
                        gFileChooser.this.finish();
                        break;
                    case 3:
                        gFileChooser.this.doback();
                        break;
                }
                DialogUtil.closeDialog(gFileChooser.this.reloadAlert);
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(mCont);
        builder.setView(gListView);
        this.reloadAlert = builder.create();
        this.reloadAlert.show();
    }
}
