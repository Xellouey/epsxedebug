package com.epsxe.ePSXe;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.content.Intent;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.app.ProgressDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Looper;
import android.os.Vibrator;
import androidx.core.content.ContextCompat;
import androidx.core.view.InputDeviceCompat;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.epsxe.ePSXe.dialog.AboutDialog;
import com.epsxe.ePSXe.dialog.ChangediscDialog;
import com.epsxe.ePSXe.dialog.CheatDialog;
import com.epsxe.ePSXe.dialog.CommonDialog;
import com.epsxe.ePSXe.dialog.GetIPAddressDialog;
import com.epsxe.ePSXe.dialog.ResetDialog;
import com.epsxe.ePSXe.dialog.ResetPreferencesDialog;
import com.epsxe.ePSXe.dialog.SstateDialog;
import com.epsxe.ePSXe.input.bluezDriver;
import com.epsxe.ePSXe.input.mogaDriver;
import com.epsxe.ePSXe.jni.libdetect;
import com.epsxe.ePSXe.jni.libepsxe;
import com.epsxe.ePSXe.task.MultiplayerServerTask;
import com.epsxe.ePSXe.task.ScanBiosTask;
import com.epsxe.ePSXe.util.DeviceUtil;
import com.epsxe.ePSXe.util.DialogUtil;
import com.epsxe.ePSXe.util.FileUtil;
import com.epsxe.ePSXe.util.MathUtil;
import com.epsxe.ePSXe.util.PSXUtil;
import com.epsxe.ePSXe.util.ReportUtil;
import com.fasterxml.jackson.core.util.MinimalPrettyPrinter;
import com.google.android.gms.drive.DriveFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

/* loaded from: classes.dex */
public class ePSXe extends LicenseCheckActivity implements SettingsDialogFragment.ResumeEmuCallback, SensorEventListener {

    private static final boolean SHOW_SAVE_STATE_IN_MENU = false;
    private static final boolean SHOW_LOAD_STATE_IN_MENU = false;

    private static final int REQ_PREFS = 0xCAFE;
    private boolean glReallyPaused = false;
    private View mainLayout;

    private String previousDiscPath = null;
    private int previousDiscSlot = 0;
    private static final int ABOUT_ID = 7;
    private static final int BLACKBANDS_ID = 104;
    private static final int CHANGEDISC_ID = 100;
    private static final int CHEAT_ID = 15;
    public static final int CPUOVERCLOCK_DISABLED = 10;
    public static final int CPUOVERCLOCK_X1_5 = 15;
    public static final int CPUOVERCLOCK_X2 = 20;
    public static final int CPUOVERCLOCK_X2_5 = 25;
    public static final int CPUOVERCLOCK_X3 = 30;
    private static final int DITHERING_ID = 106;
    private static final int FAQ_ID = 17;
    private static final int FILTERING_ID = 105;
    private static final int FRAMELIMIT_ID = 14;
    private static final int FRAMESKIP_ID = 101;
    private static final int GAMEPADINFO_ID = 21;
    private static final int GPUACCURATE_ID = 107;
    private static final int HELP_ID = 9;
    private static final int LOADSTATE_ID = 4;
    private static final int MOREOPTIONS_ID = 23;
    public static final int NETMODE_CLIENT = 4;
    public static final int NETMODE_DISABLED = 0;
    public static final int NETMODE_PADCLIENT = 2;
    public static final int NETMODE_PADSERVER = 1;
    public static final int NETMODE_SERVER = 3;
    public static final int ORIENTATION_HALFLAND = 3;
    public static final int ORIENTATION_LAND = 0;
    public static final int ORIENTATION_PORT = 1;
    public static final int ORIENTATION_REVLAND = 2;
    public static final int PADMODE_DIGITAL = 1;
    public static final int PADMODE_DISCONNECTED = 0;
    public static final int PADMODE_DUALSHOCK = 4;
    public static final int PADMODE_KONAMIGUN = 8;
    public static final int PADMODE_MOUSE = 2;
    public static final int PADMODE_NAMCOGUN = 3;
    private static final int PLAYER1MODE_ID = 110;
    private static final int PLAYER2MODE_ID = 111;
    public static final int PLAYERMODE_MULTITAP1 = 2;
    public static final int PLAYERMODE_MULTITAP2 = 3;
    public static final int PLAYERMODE_NORMAL = 1;
    public static final int PLAYERMODE_SPLITSCR = 10;
    private static final int PREFERENCES_GROUP_ID = 0;
    private static final int PREFERENCES_ID = 6;
    private static final int PREFINFO_ID = 22;
    private static final int QUIT_ID = 8;

    private static final int RESUME_GAME_ID = 24;
    public static final int RENDERER_GL2 = 3;
    public static final int RENDERER_GLEXT = 2;
    public static final int RENDERER_GLEXT2 = 4;
    public static final int RENDERER_HW = 1;
    public static final int RENDERER_PETEGL = 5;
    public static final int RENDERER_SW = 0;
    private static final int RUNBIOS_ID = 2;
    private static final int RUNCLIENT_ID = 19;
    private static final int RUNGAME_ID = 1;
    private static final int RUNSERVER_ID = 18;
    private static final int SAVESTATE_ID = 5;
    private static final int SCREENRATIO_ID = 103;
    private static final int SHOWFPS_ID = 102;
    public static final int SOUNDLATENCY_LOW = 2;
    public static final int SOUNDLATENCY_LOWEST = 4;
    public static final int SOUNDLATENCY_NORMAL = 0;
    public static final int SOUNDLATENCY_VERYLOW = 3;
    private static final int SPLITH1_ID = 11;
    private static final int SPLITH2_ID = 12;
    private static final int SPLITV_ID = 10;
    private static final int SPULATENCY_ID = 109;
    public static final int STATUS_NORUNNING = 0;
    public static final int STATUS_PAUSED = 3;
    public static final int STATUS_RUNNING_BIOS = 2;
    public static final int STATUS_RUNNING_GAME = 1;
    private static final int SUBPIXEL_ID = 108;
    private static final int TOOLSGL_ID = 16;
    private static final int TOUCHSCREEN_ID = 112;
    private cdArrayAdapter chtadapter;
    private File currentCHTDir;

    /* renamed from: d */
    private libdetect f152d;

    /* renamed from: e */
    private libepsxe f153e;
    private boolean userRequestedExit = false;
    private String gpuPluginName;
    private AlertDialog mchtAlert;
    private ePSXeReadPreferences mePSXeReadPreferences;
    private ePSXeViewGL mePSXeView2;
    private AlertDialog men2Alert;
    private AlertDialog menAlert;
    private AlertDialog menuaudioAlert;
    private AlertDialog menucheatAlert;
    private AlertDialog menuinputAlert;
    private AlertDialog menuogl2Alert;
    private AlertDialog menuoglAlert;
    private AlertDialog menuprofileAlert;
    private AlertDialog menuvideoAlert;
    private int osVersion;
    private int emuStatus = 0;
    private int emuStatusPrev = 0;
    private IsoFileSelected mIsoName = new IsoFileSelected("", 0);
    private Boolean snapRestoring = false;
    private ePSXeSound mePSXeSound = null;
    private int fps = 60;
    private Class ePSXeViewType = null;
    private ePSXeView mePSXeView = null;
    private String currentPath = "";
    private String sdCardPath = "/sdcard";
    private int emu_screen_vrmode = 0;
    private int emu_screen_vrdistorsion = 1;
    private int emu_screen_orientation = 0;
    private int emu_player_mode = 1;
    private int[] emu_pad_type = {1, 1};
    private int emu_enable_neon = 0;
    private int emu_enable_x86 = 0;
    private int emu_enable_64bits = 0;
    private int emu_enable_cores = 1;
    private int emu_renderer = 1;
    private int emu_enable_check = 0;
    private int emu_autosave = 0;
    private int emu_browser_mode = 1;
    private int emu_gui = 1;
    private int emu_xperiaplay = 0;
    private int emu_padType = 0;
    private int emu_ignore_bogus_keyup = 0;
    private int emu_exit_mapto_menu = 0;
    private int emu_gamepad_autodetect = 0;
    private int emu_enable_gamefaq = 0;
    private int emu_ui_menu_mode = 2;
    private int emu_ui_resume_dialog = 1;
    private int emu_ui_show_msg = 1;
    private int emu_ui_show_rate_dialog = 1;
    private int emu_ui_exit_confirm_dialog = 1;
    private int emu_ui_pause_support = 0;
    private boolean emu_androidtv = false;
    private int[][] keycodes = (int[][]) Array.newInstance((Class<?>) Integer.TYPE, 4, 21);
    private int[] keycodesextra = new int[16];
    private int[] padScreenExtra = {19, 19, 19, 19, 19, 19};
    private int[] pushedButtons = {0, 0, 0, 0};
    private String[] analogpadidString = {"none", "none", "none", "none"};
    private String[] analogpaddescString = {"virtual", "none", "none", "none"};
    private int[] analogpadid = {-1, -1, -1, -1};
    private int[] analogrange = {0, 0, 0, 0};
    private int[] analogleftx = {0, 0, 0, 0};
    private int[] analoglefty = {1, 1, 1, 1};
    private int[] analogrightx = {11, 11, 11, 11};
    private int[] analogrighty = {14, 14, 14, 14};
    private float[] analogleftdz = {0.0f, 0.0f, 0.0f, 0.0f};
    private float[] analogrightdz = {0.0f, 0.0f, 0.0f, 0.0f};
    private int[] analogleftdzi = {0, 0, 0, 0};
    private int[] analogrightdzi = {0, 0, 0, 0};
    private int[] analogl2 = {48, 48, 48, 48};
    private int[] analogr2 = {48, 48, 48, 48};
    private int[] analogl2range = {0, 0, 0, 0};
    private int[] analogr2range = {0, 0, 0, 0};
    private int[] analoghatx = {0, 0, 0, 0};
    private int[] analoghaty = {0, 0, 0, 0};
    private int[] analoghatdef = {0, 0, 0, 0};
    private int[] analogdpadfromanalog = {0, 0, 0, 0};
    private boolean emu_mouse = false;
    private float emu_mouse_sen = 1.4f;
    private int gamepadmatch = 1;
    private int serverMode = 0;
    private int multiplayerStep = 0;
    private int fakeBiosMsg = 0;
    private String ipServer = "192.168.1.1";
    private int hlebiosrunning = 0;
    private int emu_acc_mode = 0;
    private int tainted = 0;
    private boolean gprofile = false;
    private boolean isSwitchingToPreferences = false;
    private boolean emuReallyPausedForPrefs = false;
    private int[] emu_vibration_state = {0, 0, 0, 0, 0, 0, 0, 0};
    private long stime = 0;
    private int emu_auxvol = 16;
    private boolean enableChangeOrientation = false;
    private int emu_ogl2_res = 0;
    private int emu_ogl2_fbo = 1;
    boolean bluezenabled = false;
    bluezDriver bluezInput = null;
    String[] bluezaddr = {"", "", "", ""};
    String[] bluezdriver = {"", "", "", ""};
    int[] bluezpad = {0, 0, 0, 0};
    int mogapad = -1;
    mogaDriver mogaInput = null;
    private float lastX = 0.0f;
    private float lastY = 0.0f;
    private float lastZ = 0.0f;
    private float accJitterMargin = 0.1f;
    private float accDZ = 0.2f;
    private int emu_menu2_frameskip = 0;
    private int emu_menu2_showfps = 0;
    private int emu_menu2_soundlatency = 0;
    private int emu_menu2_input1type = 0;
    private int emu_menu2_input2type = 0;
    private int emu_menu2_input3type = 0;
    private int emu_menu2_input4type = 0;
    private int emu_menu2_input1mode = 0;
    private int emu_menu2_input2mode = 0;
    private int emu_menu2_input3mode = 0;
    private int emu_menu2_input4mode = 0;
    private int emu_menu2_inputprofile = 0;
    private int emu_menu2_dynamicpad = 0;
    private int emu_menu2_dynamicaction = 0;
    private int emu_menu2_touchscreen = 0;
    private int emu_menu2_screenratio = 0;
    private int emu_menu2_subpixelprecision = 0;
    private int emu_menu2_blackbands = 0;
    private int emu_menu2_dither = 0;
    private int emu_menu2_brightnessprofile = 0;
    private int emu_menu2_soundqa = 0;
    private int emu_menu2_screenvibrate = 0;
    private int emu_menu2_scanlines = 0;
    private int emu_menu2_scanlinestrans = 0;
    private int emu_menu2_scanlinesthickVal = 2;
    private int emu_menu2_scanlinesthick = 0;
    private int emu_menu2_customfps = 0;
    private int emu_menu2_autofirefreq = 0;
    private int emu_menu2_autofirebutton = 0;
    private int emu_menu2_autofireenabled = 0;
    private int emu_menu2_overclock = 10;
    private int emu_menu2_iresolution = 1;
    private int emu_menu2_depth = 16;
    private int emu_menu2_gpumtmodeS = 0;
    private int emu_menu2_gpumtmodeH = 0;
    private int emu_menu2_videofilter = 1;
    private int emu_menu2_overscantop = 2;
    private int emu_menu2_overscanbottom = 0;
    private int emu_menu2_iresolution_plugin = 0;
    private int emu_menu2_texmode_plugin = 2;
    private int emu_menu2_gpu2dfilter = 0;
    private int emu_menu2_gpublitskip = 0;
    private int emu_menu2_bootmode = 0;
    private int emu_menu2_interpreter = 0;
    private int emu_menu2_dmachaincore = 0;
    private int emu_menu2_mcrfilemode = 0;
    private int emu_menu2_mcrmemcardmode = 0;
    private String emu_menu2_mcr1 = "";
    private String emu_menu2_mcr2 = "";
    private int emu_menu2_input1type_net = 0;
    private int emu_menu2_input2type_net = 0;
    private int emu_menu2_gpumtmodeS_net = 0;
    Handler handlerErr = new Handler();
    private Runnable runnableErr = new Runnable() { // from class: com.epsxe.ePSXe.ePSXe.1
        @Override // java.lang.Runnable
        public void run() {
            if (ePSXe.this.emuStatus == 1 || ePSXe.this.emuStatus == 2) {
                ePSXe.this.alertdialog_quitGame_error();
            }
        }
    };
    int wvscrollx = 0;
    int wvscrolly = 0;
    int wvscrollrestored = 0;
    Handler mHandler = new Handler();
    private Runnable mLaunchTask = new Runnable() { // from class: com.epsxe.ePSXe.ePSXe.6
        @Override // java.lang.Runnable
        public void run() {
            ePSXe.this.mePSXeSound.exit();
            ePSXe.this.emuStatus = 0;
            if (ePSXe.this.emu_gui == 1) {
                Intent myIntent = new Intent(ePSXe.this, (Class<?>) ePSXe.class);
                myIntent.setFlags(DriveFile.MODE_READ_ONLY);
                ePSXe.this.startActivity(myIntent);
            }
            ePSXe.this.f153e.quit();
            ePSXe.this.finish();
        }
    };
    private int onDragX = -1;
    private int onDragY = -1;
    private boolean onDrag = false;
    ArrayAdapter<String> gladapter = null;
    ArrayList<String> gllist = new ArrayList<>();
    int emu_opengl_options = 0;
    ArrayAdapter<String> gladapter2 = null;
    ArrayList<String> gllist2 = new ArrayList<>();
    int emu_opengl_options2 = 0;
    ArrayAdapter<String> menuvideoadapter = null;
    ArrayList<String> menuvideolist = new ArrayList<>();
    ArrayAdapter<String> menuaudioadapter = null;
    ArrayList<String> menuaudiolist = new ArrayList<>();
    ArrayAdapter<String> menuinputadapter = null;
    ArrayList<String> menuinputlist = new ArrayList<>();
    ArrayAdapter<String> menucheatadapter = null;
    ArrayList<String> menucheatlist = new ArrayList<>();
    ArrayAdapter<String> menuprofileadapter = null;
    ArrayList<String> menuprofilelist = new ArrayList<>();
    ArrayAdapter<String> menuogladapter = null;
    ArrayList<String> menuogllist = new ArrayList<>();
    ArrayAdapter<String> menuogl2adapter = null;
    ArrayList<String> menuogl2list = new ArrayList<>();
    ArrayAdapter<String> men2adapter = null;
    ArrayList<String> men2list = new ArrayList<>();
    private GamepadDetection gpd = null;

    private final Handler hidePadHandler = new Handler(Looper.getMainLooper());
    private final Runnable hidePadRunnable = new Runnable() {
        @Override
        public void run() {
            Log.e("epsxelf", "onHidePad");
            if (mePSXeView != null && BuildConfig.ENABLE_PAD_TIMEOUT) {
                mePSXeView.setHidePad(!mePSXeView.isInTouch());
            }
        }
    };

    private void runHidePadHandler(Runnable r) {
        hidePadHandler.removeCallbacks(hidePadRunnable);
        hidePadHandler.postDelayed(r, BuildConfig.PAD_TIMEOUT_MILLIS);
    }

    private boolean hasExactlyTwoDiscsForGame() {
        File currentFile = new File(mIsoName.getmIsoName());
        String fileName = currentFile.getName();
        String ext = fileName.substring(fileName.lastIndexOf('.')).toLowerCase();
        String prefix = fileName.substring(0, 2).toLowerCase();
        File dir = currentFile.getParentFile();
        int count = 0;
        if (dir != null) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File f : files) {
                    String fname = f.getName().toLowerCase();
                    if (fname.startsWith(prefix) && fname.endsWith(ext)) {
                        count++;
                    }
                }
            }
        }
        return count == 2;
    }

    private boolean hasExactlyOneDiscsForGame() {
        File currentFile = new File(mIsoName.getmIsoName());
        String fileName = currentFile.getName();
        String ext = fileName.substring(fileName.lastIndexOf('.')).toLowerCase();
        String prefix = fileName.substring(0, 2).toLowerCase();
        File dir = currentFile.getParentFile();
        int count = 0;
        if (dir != null) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File f : files) {
                    String fname = f.getName().toLowerCase();
                    if (fname.startsWith(prefix) && fname.endsWith(ext)) {
                        count++;
                    }
                }
            }
        }
        return count == 1;
    }

    native int RegisterThis();

    public native void initVibration();

    static /* synthetic */ int access$3108(ePSXe x0) {
        int i = x0.emu_menu2_dither;
        x0.emu_menu2_dither = i + 1;
        return i;
    }

    static /* synthetic */ int access$3308(ePSXe x0) {
        int i = x0.emu_menu2_brightnessprofile;
        x0.emu_menu2_brightnessprofile = i + 1;
        return i;
    }

    static /* synthetic */ int access$3608(ePSXe x0) {
        int i = x0.emu_menu2_scanlinesthick;
        x0.emu_menu2_scanlinesthick = i + 1;
        return i;
    }

    static /* synthetic */ int access$5308(ePSXe x0) {
        int i = x0.emu_menu2_inputprofile;
        x0.emu_menu2_inputprofile = i + 1;
        return i;
    }

    public void runIso(String isoName, int slot) {
        Log.d("ePSXeDebug", "=== ePSXe.runIso() STARTED ===");
        Log.d("ePSXeDebug", "ePSXe: isoName = " + isoName);
        Log.d("ePSXeDebug", "ePSXe: slot = " + slot);

        try {
            this.enableChangeOrientation = false;
            if (this.mePSXeReadPreferences == null) {
                this.mePSXeReadPreferences = new ePSXeReadPreferences(this);
                Log.d("ePSXeDebug", "ePSXe: ePSXeReadPreferences initialized");
            }
        } catch (Exception e) {
            Log.e("ePSXeDebug", "ePSXe: ERROR in runIso initialization", e);
            throw e;
        }
        if (this.multiplayerStep == 0) {
            if (isoName.equals("___RUNBIOS___")) {
                this.currentPath = this.sdCardPath;
            } else {
                File f = new File(isoName);
                this.currentPath = f.getParent();
            }
            Log.e("epsxe", "currentpath " + this.currentPath);
            Log.e("epsxe", "sdCardPath " + this.sdCardPath);
            if (this.mePSXeReadPreferences.getCpuMME() == 0) {
                this.emu_enable_neon = 0;
            }
            if (this.emu_enable_64bits == 1) {
                this.emu_enable_neon = 1;
            }
            if (this.emu_enable_x86 == 0) {
                this.f153e = new libepsxe(this.emu_enable_neon);
            } else {
                this.f153e = new libepsxe(1);
            }
            this.f153e.resetdefaultvalues();
            this.tainted = 0;
            this.f153e.setSdCardPath(this.sdCardPath);
            String bios = this.mePSXeReadPreferences.getBios();
            Log.e("epsxe", "mePSXeReadPreferences.getBios " + bios);
            this.f153e.setBios(bios);
            this.hlebiosrunning = this.mePSXeReadPreferences.getBiosHle();
            if (this.hlebiosrunning == 2) {
                this.hlebiosrunning = 0;
            }
            if (this.hlebiosrunning == 1 && isoName.equals("___RUNBIOS___")) {
                this.hlebiosrunning = 0;
            }
            this.f153e.setBiosHle(this.hlebiosrunning);
            this.emu_menu2_gpumtmodeS = this.mePSXeReadPreferences.getGpuSoftMtPref();
            this.emu_menu2_gpumtmodeS_net = this.emu_menu2_gpumtmodeS;
            this.emu_menu2_soundqa = this.mePSXeReadPreferences.getSoundQA();
            this.emu_menu2_soundlatency = this.mePSXeReadPreferences.getSoundLatency();
            this.emu_menu2_input1type = this.mePSXeReadPreferences.getInputPadMode();
            this.emu_menu2_input2type = this.mePSXeReadPreferences.getInputPad2Mode();
            this.emu_menu2_iresolution = this.mePSXeReadPreferences.getGpuIresolution();
            if (this.serverMode == 3 || this.serverMode == 4) {
                this.emu_menu2_input1type_net = 1;
                this.emu_menu2_input2type_net = 1;
                this.emu_menu2_input1type = this.emu_menu2_input1type_net;
                this.emu_menu2_input2type = this.emu_menu2_input2type_net;
                this.emu_menu2_soundlatency = 0;
                this.emu_menu2_iresolution = 1;
                String biosMD5 = "0000000000000000";
                int cdata = this.emu_enable_x86 | ((this.emu_menu2_soundqa == 2 ? 1 : 0) << 1) | ((this.hlebiosrunning == 0 ? 0 : 1) << 2) | ((this.emu_menu2_gpumtmodeS == 0 ? 0 : 1) << 3) | ((this.emu_menu2_input1type_net & 15) << 4) | ((this.emu_menu2_input2type_net & 15) << 8);
                try {
                    File fbios = new File(bios);
                    if (fbios.exists()) {
                        biosMD5 = FileUtil.getMD5fromFile(fbios.getAbsolutePath());
                    }
                } catch (Exception e) {
                }
                if (this.serverMode == 3) {
                    new MultiplayerServerTask(this, this.f153e, this.serverMode, isoName, slot, cdata, biosMD5).execute("");
                    return;
                } else if (this.serverMode == 4) {
                    GetIPAddressDialog.showGetIPAddressDialog(this, this.f153e, this.serverMode, this.mePSXeReadPreferences.getNetplayServer(), cdata, biosMD5);
                    return;
                }
            }
        }
        Log.e("epsxe", "getVideoRenderer " + this.mePSXeReadPreferences.getVideoRenderer());
        this.emu_renderer = this.mePSXeReadPreferences.getVideoRenderer();
        if ((this.emu_renderer == 4 || this.emu_renderer == 5) && this.osVersion < 17) {
            this.emu_renderer = 1;
        }
        this.emu_ogl2_res = this.mePSXeReadPreferences.getGpuGl2xResPref(this.emu_renderer);
        this.emu_ogl2_fbo = this.mePSXeReadPreferences.getGpuGl2FboPref();
        this.emu_screen_orientation = this.mePSXeReadPreferences.getScreenOrientation();
        this.emu_screen_vrmode = this.mePSXeReadPreferences.getScreenVrmode();
        this.emu_screen_vrdistorsion = this.mePSXeReadPreferences.getScreenVrdistorsion();
        this.emu_menu2_screenratio = this.mePSXeReadPreferences.getScreenRatio();
        this.emu_menu2_depth = this.mePSXeReadPreferences.getScreenDepth(this.emu_androidtv);
        this.emu_menu2_gpumtmodeH = this.mePSXeReadPreferences.getGpuMtPref(this.emu_androidtv);
        this.emu_menu2_videofilter = this.mePSXeReadPreferences.getVideoFilterhw();
        this.emu_menu2_subpixelprecision = this.mePSXeReadPreferences.getGpuperspectivecorrection();
        this.emu_menu2_scanlinestrans = this.mePSXeReadPreferences.getGpuscanlinestrans();
        this.emu_menu2_scanlinesthick = this.mePSXeReadPreferences.getGpuscanlinesthick();
        this.emu_menu2_overscantop = this.mePSXeReadPreferences.getGpuOverscantop();
        this.emu_menu2_overscanbottom = this.mePSXeReadPreferences.getGpuOverscanbottom();
        this.emu_menu2_brightnessprofile = this.mePSXeReadPreferences.getGpuBrighttnessprofile();
        this.emu_menu2_blackbands = this.mePSXeReadPreferences.getScreenBlackbands();
        this.emu_menu2_iresolution_plugin = this.mePSXeReadPreferences.getGpureduceres();
        this.emu_menu2_texmode_plugin = this.mePSXeReadPreferences.getGpuNamePref();
        this.emu_menu2_dither = this.mePSXeReadPreferences.getVideoDither();
        this.emu_menu2_gpu2dfilter = this.mePSXeReadPreferences.getGpu2dfilter();
        this.emu_menu2_gpublitskip = this.mePSXeReadPreferences.getGpublitskip();
        if (this.emu_menu2_gpu2dfilter > 1) {
            this.emu_menu2_iresolution = 1;
        }
        if (this.emu_renderer == 0) {
            this.emu_menu2_iresolution = 1;
        }
        this.emu_menu2_dynamicpad = this.mePSXeReadPreferences.getInputDynamicpad();
        this.emu_menu2_dynamicaction = this.mePSXeReadPreferences.getInputDynamicaction();
        this.emu_menu2_autofirefreq = this.mePSXeReadPreferences.getInputAutofirefreq();
        this.emu_menu2_autofirebutton = this.mePSXeReadPreferences.getInputAutofirebutton();
        this.emu_menu2_input3type = this.mePSXeReadPreferences.getInputPad3Mode();
        this.emu_menu2_input4type = this.mePSXeReadPreferences.getInputPad4Mode();
        this.emu_acc_mode = this.mePSXeReadPreferences.getInputPadAcc();
        this.accDZ = this.mePSXeReadPreferences.getInputPadAccdz() / 10.0f;
        this.emu_menu2_frameskip = this.mePSXeReadPreferences.getCpuFrameSkip();
        this.emu_menu2_customfps = this.mePSXeReadPreferences.getCpucustomfps();
        this.emu_player_mode = this.mePSXeReadPreferences.getInputPlayerMode();
        this.emu_autosave = this.mePSXeReadPreferences.getMiscAutosave();
        this.emu_menu2_bootmode = this.mePSXeReadPreferences.getmiscbootmode();
        this.emu_menu2_interpreter = this.mePSXeReadPreferences.getDebugInterpreter();
        this.emu_menu2_dmachaincore = this.mePSXeReadPreferences.getGpuDmachaincore();
        this.emu_menu2_overclock = this.mePSXeReadPreferences.getCpuOverclock();
        this.emu_menu2_mcr1 = this.mePSXeReadPreferences.getMemcard1();
        this.emu_menu2_mcr2 = this.mePSXeReadPreferences.getMemcard2();
        this.emu_menu2_mcrfilemode = this.mePSXeReadPreferences.getMemcardFileMode();
        this.emu_menu2_mcrmemcardmode = this.mePSXeReadPreferences.getMemcardMode();
        this.emu_ui_exit_confirm_dialog = this.mePSXeReadPreferences.getUiconfirmexitdialog();
        this.emu_ui_show_msg = this.mePSXeReadPreferences.getUishowmsg();
        this.emu_ui_show_rate_dialog = this.mePSXeReadPreferences.getUishowratedialog();
        this.emu_ui_pause_support = this.mePSXeReadPreferences.getUipausesupportdialog();
        if (this.emu_renderer == 0) {
            this.emu_ui_pause_support = 0;
        }
        loadGameProfile(isoName, slot);
        if (this.serverMode >= 2) {
            this.emu_renderer = 1;
        }
        if (this.serverMode == 1) {
            Toast.makeText(this, "Server IP: " + DeviceUtil.getLocalIpv4Address(), 1).show();
        }
        if (this.serverMode == 3 || this.serverMode == 4) {
            this.emu_menu2_frameskip = 0;
            this.emu_menu2_customfps = 0;
            this.emu_player_mode = 1;
            this.emu_autosave = 0;
            this.emu_menu2_bootmode = 0;
            this.emu_menu2_interpreter = 1;
            this.emu_menu2_dmachaincore = 1;
            this.emu_menu2_mcrmemcardmode = 0;
            this.emu_menu2_mcrfilemode = 255;
            this.emu_menu2_overclock = 10;
            this.emu_menu2_gpublitskip = 0;
            this.emu_acc_mode = 0;
            this.emu_menu2_screenratio = 1;
        }
        if (this.serverMode == 4) {
            this.emu_menu2_input1type = this.emu_menu2_input1type_net;
            this.emu_menu2_input2type = this.emu_menu2_input2type_net;
            this.emu_menu2_gpumtmodeS = this.emu_menu2_gpumtmodeS_net;
        }
        this.f153e.setBootMode(this.emu_menu2_bootmode);
        this.f153e.setCpuMaxFreq(DeviceUtil.getCPUFrequencyMax());
        this.f153e.setInterpreter(this.emu_menu2_interpreter);
        this.f153e.setDmachaincore(this.emu_menu2_dmachaincore);
        this.f153e.setgpublitskip(this.emu_menu2_gpublitskip);
        if (this.emu_menu2_overclock != 10) {
            this.f153e.setcpuoverclocking(this.emu_menu2_overclock);
        }
        this.f153e.setgpuiresolution(this.emu_menu2_iresolution);
        this.f153e.setGteaccurateH(this.emu_menu2_subpixelprecision);
        this.f153e.setgpu2dfilter(this.emu_menu2_gpu2dfilter);
        this.f153e.setgpubrightnessprofile(this.emu_menu2_brightnessprofile);
        initShaders();
        initScanlines();
        initMemcards();
        initVRMode();
        initGPUOpenGLPlugin();
        if (initGPUSoftPlugin(isoName, slot)) {
            getWindow().setFlags(128, 128);
            if (this.mePSXeView == null) {
                createEpsxeView(this.emu_renderer);
                if (this.hlebiosrunning == 1) {
                    this.mePSXeView.setbiosmsg(true);
                }
                if (this.serverMode == 1) {
                    this.f153e.runServer(19999, this.serverMode);
                } else if (this.serverMode == 2) {
                    this.f153e.runClient(this.ipServer, 19999, 1, this.serverMode);
                }
                this.mePSXeView.setplugin(this.emu_renderer);
                this.mePSXeView.setservermode(this.serverMode);
                this.mePSXeView.setcustomgameprofile(this.gprofile);
                this.mePSXeView.setgpumtmodeS(this.emu_menu2_gpumtmodeS);
                this.mePSXeView.setscreendepth(this.emu_menu2_depth);
                this.f153e.setcustomfps(this.emu_menu2_customfps);
                this.mePSXeView.setePSXeLib(this.f153e, this.emu_renderer, this.serverMode);
                if (!this.mePSXeView.setIsoName(this.mIsoName.getmIsoName(), slot, this.gpuPluginName)) {
                    Toast.makeText(this, "Error! GPU plugin is corrupted or wrong arch, please download again the plugin", 1).show();
                    return;
                }
                this.mePSXeView.setfps(this.fps);
                if (this.emu_androidtv) {
                    this.mePSXeView.setdevice(2);
                }
                this.mePSXeView.setscreenvrmode(this.emu_screen_vrmode, this.emu_screen_vrdistorsion);
                this.mePSXeView.setverbose(this.emu_ui_show_msg);
                this.emu_menu2_screenvibrate = this.mePSXeReadPreferences.getInputVibrate();
                this.mePSXeView.setinputvibration(this.emu_menu2_screenvibrate, this.mePSXeReadPreferences.getInputVibrate2());
                this.mePSXeView.setinputalpha(this.mePSXeReadPreferences.getInputAlpha());
                this.mePSXeView.setinputmag(this.mePSXeReadPreferences.getInputMag());
                this.emu_ignore_bogus_keyup = this.mePSXeReadPreferences.getInputKeyboard();
                this.mePSXeView.setframeskip(this.emu_menu2_frameskip);
                this.emu_menu2_showfps = this.mePSXeReadPreferences.getCpuShowFPS();
                this.mePSXeView.setshowfps(this.emu_menu2_showfps);
                if (this.emu_screen_orientation == 1) {
                    this.emu_menu2_touchscreen = this.mePSXeReadPreferences.getInputPaintPadPorMode(this.emu_androidtv);
                } else {
                    this.emu_menu2_touchscreen = this.mePSXeReadPreferences.getInputPaintPadMode(this.emu_androidtv);
                }
//                this.mePSXeView.setinputpaintpadmode(this.emu_menu2_touchscreen, this.mePSXeReadPreferences.getInputPaintPadMode2());
                this.mePSXeView.setinputpaintpadmode(0, this.mePSXeReadPreferences.getInputPaintPadMode2());
                this.mePSXeView.setinputskinname(this.mePSXeReadPreferences.getSkin());
                if (this.emu_screen_orientation == 2) {
                    this.mePSXeView.setscreenorientation(0);
                } else {
                    this.mePSXeView.setscreenorientation(this.emu_screen_orientation);
                }
                if (this.emu_screen_orientation == 1) {
                    this.mePSXeView.setportraitmode(this.mePSXeReadPreferences.getInputPaintPadPorMode(this.emu_androidtv));
                }
                if (this.emu_menu2_screenratio == 2) {
                    this.f153e.setWidescreen(1);
                    this.mePSXeView.updatescreenratio(0);
                } else {
                    this.f153e.setWidescreen(0);
                    this.mePSXeView.updatescreenratio(this.emu_menu2_screenratio);
                }
                this.mePSXeView.setscreenblackbands(this.emu_menu2_blackbands);
                if (this.emu_player_mode == 10) {
                    this.f153e.setPadModeMultitap(1);
                } else {
                    this.f153e.setPadModeMultitap(this.emu_player_mode);
                }
                if (this.emu_player_mode == 2 || this.emu_player_mode == 3) {
                    this.emu_player_mode = 1;
                }
                this.mePSXeView.setplayermode(this.emu_player_mode);
                this.mePSXeView.setvideodither(this.emu_menu2_dither);
                this.mePSXeView.setgpumtmodeH(this.emu_menu2_gpumtmodeH);
                this.mePSXeView.setvideofilterhw(this.emu_menu2_videofilter);
                if (this.emu_menu2_customfps != 0) {
                    this.emu_menu2_soundlatency = 0;
                }
                this.mePSXeView.setsoundlatency(this.emu_menu2_soundlatency);
                this.f153e.setVibration(0, this.mePSXeReadPreferences.getInputVibrationPSX());
                this.f153e.setVibration(1, this.mePSXeReadPreferences.getInputVibrationPSX2());
                if (this.mePSXeReadPreferences.getInputDetectGamepad() == 1) {
                    this.emu_gamepad_autodetect = 6;
                } else {
                    this.emu_gamepad_autodetect = 0;
                }
                initPSXPadMode(isoName);
                initAutoFire();
                this.mePSXeView.setdynamicpad(this.emu_menu2_dynamicpad);
                this.mePSXeView.setdynamicaction(this.emu_menu2_dynamicaction);
                this.f153e.setgpuoverscantop(this.emu_menu2_overscantop);
                this.f153e.setgpuoverscanbottom(this.emu_menu2_overscanbottom);
                this.emu_ui_menu_mode = this.mePSXeReadPreferences.getMiscUimenu();
                initFAQMode(isoName);
                readHwButtons();
                initJoysticks();
                initBluez();
                initMoga();
                if (this.emu_pad_type[0] == 1 && this.emu_pad_type[1] == 1) {
                    this.emu_pad_type[0] = 0;
                }
                this.mePSXeView.setinputpadtype(this.emu_pad_type[0], this.emu_pad_type[1]);
                resumeFromHomeOrAutosave(isoName);
                if (!isoName.equals("___RUNBIOS___")) {
                    this.mePSXeReadPreferences.setIsoPath(this.currentPath);
                }
            }
            Log.d("ePSXeDebug", "ePSXe: Initializing sound...");
            initSound();
            Log.d("ePSXeDebug", "ePSXe: Checking license...");
            mcheckLicense();
            Log.d("ePSXeDebug", "ePSXe: Setting screen orientation...");
            initScreenOrientation();
            Log.d("ePSXeDebug", "ePSXe: Setting content view...");
            setContentView();
            Log.d("ePSXeDebug", "ePSXe: Setting immersion mode...");
            DeviceUtil.setInmmersionMode(this);
            Log.d("ePSXeDebug", "ePSXe: Initializing vibration...");
            initVibration();
            Log.d("ePSXeDebug", "ePSXe: Initializing accelerator...");
            initAccelerator();
            this.stime = System.currentTimeMillis() / 1000;
            this.emuStatus = 1;
            this.emuStatusPrev = 1;
            if (this.mIsoName.getmIsoName().equals("___RUNBIOS___")) {
                this.emuStatus = 2;
                this.emuStatusPrev = 2;
            }
            Log.d("ePSXeDebug", "ePSXe: Game initialization completed, emuStatus = " + this.emuStatus);
            if (this.emu_ui_show_msg == 1) {
                Toast.makeText(this, "Player 1: " + this.analogpaddescString[0] + "\nPlayer 2: " + this.analogpaddescString[1], 1).show();
            }
        }
    }

    public void runIso(String isoName, int slot, String ipAddress) {
        this.ipServer = ipAddress;
        runIso(isoName, slot);
    }

    public void runIsoClient(String isoName, int slot, int status) {
        this.emu_menu2_input1type_net = (status >> 8) & 15;
        this.emu_menu2_input2type_net = (status >> 12) & 15;
        this.hlebiosrunning = (status >> 16) & 1;
        this.emu_menu2_gpumtmodeS_net = (status >> 17) & 1;
        this.emu_menu2_soundqa = ((status >> 18) & 1) + 1;
        this.multiplayerStep = 1;
        runIso(isoName, slot);
    }

    public void runIsoServer(String isoName, int slot) {
        this.multiplayerStep = 1;
        runIso(isoName, slot);
    }

    private void createEpsxeView(int renderer) {

        try {
            if (renderer == 0) {
                ePSXeViewType = ePSXeViewSoft.class;
            } else if (renderer == 1 || renderer == 3) {
                ePSXeViewType = ePSXeViewGL.class;
            } else if (renderer == 2) {
                ePSXeViewType = ePSXeViewGLext.class;
            } else if (renderer == 4) {
                ePSXeViewType = ePSXeViewGL2ext.class;
            } else {
                if (renderer == 5) {
                    ePSXeViewType = ePSXeViewGL2ext.class;
                }
                Constructor[] c = this.ePSXeViewType.getConstructors();
                if (this.emu_screen_orientation == 1) {
                    this.emu_menu2_iresolution_plugin = 0;
                }
                this.mePSXeView = (ePSXeView) c[0].newInstance(getApplication(), this, Integer.valueOf(this.emu_menu2_iresolution_plugin));
            }
            Constructor[] c2 = this.ePSXeViewType.getConstructors();
            if (this.emu_screen_orientation == 1) {
            }
            this.mePSXeView = (ePSXeView) c2[0].newInstance(getApplication(), this, Integer.valueOf(this.emu_menu2_iresolution_plugin));
        } catch (Exception e2) {
            finish();
        }
        this.mePSXeView.setOnTouchListener(isInTouch -> {
            if (isInTouch) {
                mePSXeView.setHidePad(false);
            }
            hidePadHandler.removeCallbacks(hidePadRunnable);
            runHidePadHandler(hidePadRunnable);
        });
    }

    private void readHwButtons() {
        for (int p = 0; p < 4; p++) {
            for (int i = 0; i < 21; i++) {
                this.keycodes[p][i] = this.mePSXeReadPreferences.getButtonKeycode(p, i);
                Log.e("epsxekey", "keycode[" + p + "][" + i + "] = " + this.keycodes[p][i]);
            }
        }
        for (int i2 = 0; i2 < this.keycodesextra.length; i2++) {
            this.keycodesextra[i2] = this.mePSXeReadPreferences.getButtonKeycodeextra(i2);
        }
        for (int i3 = 0; i3 < 6; i3++) {
            int val = this.mePSXeReadPreferences.getPadExtra("inputExtrasPref" + (i3 + 1));
            if (val != 19) {
                this.padScreenExtra[i3] = val;
            }
        }
    }

    private void resumeFromHomeOrAutosave(String isoName) {
        if (this.emu_autosave == 1 && !isoName.equals("___RUNBIOS___")) {
            this.mePSXeView.setautosnaprestoring();
        } else if (this.snapRestoring.booleanValue()) {
            this.mePSXeView.setsnaprestoring(true);
            this.snapRestoring = false;
        }
    }

    public void loadGameProfile(String isoName, int slot) {
        String pcode;
        if (!isoName.equals("___RUNBIOS___") && this.serverMode != 3 && this.serverMode != 4 && (pcode = PSXUtil.getPSXGameID(isoName, slot, 0)) != null && pcode.length() > 0) {
            this.gprofile = loadGameProfile(pcode);
        }
    }

    private boolean initGPUSoftPlugin(String isoName, int slot) {
        if (this.emu_renderer != 0 && this.emu_renderer != 1 && this.emu_renderer != 3) {
            return true;
        }
        this.f153e.setGpu("GPUCORE");
        if (this.emu_renderer == 1 || this.emu_renderer == 3) {
            this.f153e.setGpuSoftMtMode(this.emu_menu2_gpumtmodeS);
        }
        this.fps = this.f153e.loadepsxe(isoName, slot);
        if (!isoName.equals("___RUNBIOS___")) {
            if (this.fps == -1) {
                CommonDialog.showIsoErrorDialog(isoName, this);
                return false;
            }
            if (this.fps <= 60) {
                return true;
            }
            Toast.makeText(this, R.string.main_systemcnf, 1).show();
            this.fps -= 100;
            return true;
        }
        this.fps = 60;
        return true;
    }

    private void initGPUOpenGLPlugin() {
        String pathlibFolder;
        String pathlibFolderAlt;
        String pathlibFolderAlt2;
        String pathlibFolderAlt3;
        if (this.emu_renderer == 5) {
            File extStore = ContextCompat.getDataDir(this);
            String filenameDebug = extStore.getAbsolutePath() + "/epsxe/plugins/liboglplugin2.so";
            File fileDebug = new File(filenameDebug);
            if (fileDebug.exists()) {
                String gpufiletmp = getFilesDir() + "/libopenglextTMP.so";
                File gpuTmp = new File(getFilesDir(), "/libopenglextTMP.so");
                if (gpuTmp.exists()) {
                    gpuTmp.delete();
                }
                FileUtil.copyFile(fileDebug, gpuTmp);
                if (gpuTmp.exists()) {
                    this.f153e.setfbosettings(this.emu_renderer, this.emu_ogl2_res, this.emu_ogl2_fbo);
                    this.f153e.setPluginMode(this.emu_menu2_texmode_plugin);
                    this.f153e.setGpu(gpufiletmp);
                    this.gpuPluginName = gpufiletmp;
                    return;
                }
                this.emu_renderer = 1;
                Toast.makeText(this, R.string.main_gpunotfound, 1).show();
                return;
            }
            try {
                pathlibFolderAlt3 = getPackageManager().getPackageInfo(BuildConfig.APPLICATION_ID, 0).applicationInfo.nativeLibraryDir;
            } catch (Exception e) {
                pathlibFolderAlt3 = "";
            }
            String plugin2 = pathlibFolderAlt3 + "/liboglplugin2.so";
            File ff2 = new File(plugin2);
            if (ff2.exists()) {
                this.f153e.setfbosettings(this.emu_renderer, this.emu_ogl2_res, this.emu_ogl2_fbo);
                this.f153e.setPluginMode(this.emu_menu2_texmode_plugin);
                this.f153e.setGpu(plugin2);
                this.gpuPluginName = plugin2;
                return;
            }
            this.emu_renderer = 1;
            Toast.makeText(this, R.string.main_gpunotfound, 1).show();
            return;
        }
        if (this.emu_renderer == 4) {
            File extStore2 = ContextCompat.getDataDir(this);
            String filenameDebug2 = extStore2.getAbsolutePath() + "/epsxe/plugins/libogl2extplugin.so";
            File fileDebug2 = new File(filenameDebug2);
            if (fileDebug2.exists()) {
                String gpufiletmp2 = getFilesDir() + "/libopenglextTMP.so";
                File gpuTmp2 = new File(getFilesDir(), "/libopenglextTMP.so");
                if (gpuTmp2.exists()) {
                    gpuTmp2.delete();
                }
                FileUtil.copyFile(fileDebug2, gpuTmp2);
                if (gpuTmp2.exists()) {
                    this.f153e.setfbosettings(this.emu_renderer, this.emu_ogl2_res, this.emu_ogl2_fbo);
                    this.f153e.setPluginMode(this.emu_menu2_texmode_plugin);
                    this.f153e.setGpu(gpufiletmp2);
                    this.gpuPluginName = gpufiletmp2;
                    return;
                }
                this.emu_renderer = 1;
                Toast.makeText(this, R.string.main_gpunotfound, 1).show();
                return;
            }
            try {
                pathlibFolderAlt2 = getPackageManager().getPackageInfo(BuildConfig.APPLICATION_ID, 0).applicationInfo.nativeLibraryDir;
            } catch (Exception e2) {
                pathlibFolderAlt2 = "";
            }
            String plugin22 = pathlibFolderAlt2 + "/libogl2extplugin.so";
            File ff22 = new File(plugin22);
            if (ff22.exists()) {
                this.f153e.setfbosettings(this.emu_renderer, this.emu_ogl2_res, this.emu_ogl2_fbo);
                this.f153e.setPluginMode(this.emu_menu2_texmode_plugin);
                this.f153e.setGpu(plugin22);
                this.gpuPluginName = plugin22;
                return;
            }
            this.emu_renderer = 1;
            Toast.makeText(this, R.string.main_gpunotfound, 1).show();
            return;
        }
        if (this.emu_renderer == 2) {
            try {
                pathlibFolder = getPackageManager().getPackageInfo("com.epsxe.opengl", 0).applicationInfo.dataDir + "/lib";
            } catch (Exception e3) {
                pathlibFolder = "";
            }
            String plugin = pathlibFolder + "/libopenglplugin.so";
            File f = new File(plugin);
            if (f.exists()) {
                this.f153e.setPluginMode(this.emu_menu2_texmode_plugin);
                this.f153e.setGpu(plugin);
                this.gpuPluginName = plugin;
                return;
            }
            try {
                pathlibFolderAlt = getPackageManager().getPackageInfo("com.epsxe.opengl", 0).applicationInfo.nativeLibraryDir;
            } catch (Exception e4) {
                pathlibFolderAlt = "";
            }
            String plugin23 = pathlibFolderAlt + "/libopenglplugin.so";
            File ff23 = new File(plugin23);
            if (ff23.exists()) {
                this.f153e.setPluginMode(this.emu_menu2_texmode_plugin);
                this.f153e.setGpu(plugin23);
                this.gpuPluginName = plugin23;
                return;
            }
            String gpufile = this.mePSXeReadPreferences.getGpu();
            File f2 = new File(gpufile);
            if (f2.exists()) {
                String gpufiletmp3 = getFilesDir() + "/libopenglextTMP.so";
                File gpuTmp3 = new File(getFilesDir(), "/libopenglextTMP.so");
                if (gpuTmp3.exists()) {
                    gpuTmp3.delete();
                }
                FileUtil.copyFile(f2, gpuTmp3);
                if (gpuTmp3.exists()) {
                    this.f153e.setPluginMode(this.emu_menu2_texmode_plugin);
                    this.f153e.setGpu(gpufiletmp3);
                    this.gpuPluginName = gpufiletmp3;
                    return;
                } else {
                    this.emu_renderer = 1;
                    Toast.makeText(this, R.string.main_gpunotfound, 1).show();
                    return;
                }
            }
            this.emu_renderer = 1;
            Toast.makeText(this, R.string.main_gpunotfound, 1).show();
        }
    }

    private void initShaders() {
        if (this.emu_renderer == 3) {
            String sname = this.mePSXeReadPreferences.getGpushader();
            if (!sname.equals("FXAA") && !sname.equals("NONE") && !sname.equals("CRT1") && !sname.equals("CRT2") && !sname.equals("XBR")) {
                File fsh = new File(sname);
                if (fsh.exists()) {
                    this.f153e.setGpuShader(sname);
                    return;
                }
                return;
            }
            this.f153e.setGpuShader(sname);
        }
    }

    private void initScanlines() {
        if (this.emu_menu2_scanlinesthick == 0) {
            this.emu_menu2_scanlines = 0;
        } else {
            this.emu_menu2_scanlines = 1;
            this.emu_menu2_scanlinestrans = (int) (this.emu_menu2_scanlinestrans * 2.55d);
        }
        this.emu_menu2_scanlinesthickVal = this.emu_menu2_scanlinesthick;
        this.f153e.setscanlines(this.emu_menu2_scanlines, this.emu_menu2_scanlinestrans, this.emu_menu2_scanlinesthickVal);
    }

    private void initMemcards() {
        if (this.emu_menu2_mcrfilemode > 1 && this.serverMode < 3) {
            this.emu_menu2_mcrfilemode = 0;
        }
        Log.e("epsxe", "memcard1 " + this.emu_menu2_mcr1);
        Log.e("epsxe", "memcard2 " + this.emu_menu2_mcr2);
        Log.e("epsxe", "memcardmode " + this.emu_menu2_mcrfilemode);
        if (this.emu_menu2_mcr1.equals("default")) {
            this.f153e.setMemcard1("NULL");
        } else {
            this.f153e.setMemcard1(this.emu_menu2_mcr1);
        }
        if (this.emu_menu2_mcr2.equals("default")) {
            this.f153e.setMemcard2("NULL");
        } else {
            this.f153e.setMemcard2(this.emu_menu2_mcr2);
        }
        this.f153e.setMemcardMode(this.emu_menu2_mcrmemcardmode);
        if (this.hlebiosrunning != 1 || this.serverMode >= 3) {
            this.f153e.setMemcardFileMode(this.emu_menu2_mcrfilemode);
        } else {
            this.f153e.setMemcardFileMode(1);
        }
    }

    private void initVRMode() {
        if (this.emu_screen_vrmode != 0) {
            if (this.emu_screen_orientation == 1) {
                this.emu_screen_orientation = 0;
            }
            if (this.emu_screen_vrdistorsion == 1) {
                this.emu_renderer = 3;
            } else {
                this.emu_renderer = 1;
            }
            this.emu_menu2_screenratio = 1;
        }
    }

    private void initFAQMode(String isoName) {
        if (!isoName.equals("___RUNBIOS___")) {
            File extStore = ContextCompat.getDataDir(this);
            File fileTmp = new File(extStore.getAbsolutePath() + "/epsxe/faqs/" + this.f153e.getCode() + ".txt");
            if (fileTmp.exists()) {
                this.emu_enable_gamefaq = 1;
            }
        }
    }

    private void initAutoFire() {
        if (this.emu_menu2_autofirefreq != 0) {
            this.f153e.enableautofire(0, 0, this.emu_menu2_autofirebutton, this.emu_menu2_autofirefreq, 1);
            this.emu_menu2_autofireenabled = 1;
        }
    }

    private void initPSXPadMode(String isoName) {
        int mode1 = this.emu_menu2_input1type;
        int mode2 = this.emu_menu2_input2type;
        int mode3 = this.emu_menu2_input3type;
        int mode4 = this.emu_menu2_input4type;
        int analog1 = 1;
        int analog2 = 1;
        int analog3 = 1;
        int analog4 = 1;
        if (this.emu_padType != 0) {

            if (mode1 == 3 && (this.emu_padType & 64) == 0) {
                mode1 = 1;
            }
            if (mode1 == 8 && (this.emu_padType & 32) == 0) {
                mode1 = 1;
            }
            if (mode1 == 2 && (this.emu_padType & 16) == 0) {
                mode1 = 1;
            }
        }

        if (mode1 == 2 || mode2 == 2) {
            if (Build.VERSION.SDK_INT >= 24) {
                this.emu_mouse = true;
                this.emu_mouse_sen = this.mePSXeReadPreferences.getInputmousesen();
                this.emu_mouse_sen /= 100.0f;
                if (mode1 == 2) {
                    this.emu_menu2_touchscreen = 2;
                    this.mePSXeView.setinputpaintpadmode(0, this.mePSXeReadPreferences.getInputPaintPadMode2());
                }
            } else {
                if (mode1 == 2) {
                    mode1 = 1;
                }
                if (mode2 == 2) {
                    mode2 = 1;
                }
            }
        }
        if ((mode1 == 3 || mode1 == 8) && this.emu_screen_orientation == 1) {
            mode1 = 1;
        }
        this.emu_menu2_input1type = mode1;
        this.emu_menu2_input2type = mode2;
        this.emu_menu2_input3type = mode3;
        this.emu_menu2_input4type = mode4;
        this.emu_menu2_input1mode = analog1;
        this.emu_menu2_input2mode = analog2;
        this.emu_menu2_input3mode = analog3;
        this.emu_menu2_input4mode = analog4;
        this.mePSXeView.setinputpadmode(mode1, mode2, analog1, analog2);
        this.f153e.setpadmode(2, mode3);
        this.f153e.setpadanalogmode(2, analog3);
        this.f153e.setpadmode(3, mode4);
        this.f153e.setpadanalogmode(3, analog4);
    }

    private void setContentView() {
        if (this.emu_renderer == 0) {
            setContentView((ePSXeViewSoft) this.mePSXeView);
            return;
        }
        if (this.emu_renderer == 1 || this.emu_renderer == 3) {
            setContentView((ePSXeViewGL) this.mePSXeView);
        } else if (this.emu_renderer == 4 || this.emu_renderer == 5) {
            setContentView((ePSXeViewGL2ext) this.mePSXeView);
        } else {
            setContentView((ePSXeViewGLext) this.mePSXeView);
        }
    }

    private void initAccelerator() {
        SensorManager sensorManager;
        if (this.emu_acc_mode != 0 && (sensorManager = (SensorManager) getSystemService("sensor")) != null) {
            Sensor accelerometer = sensorManager.getDefaultSensor(1);
            sensorManager.registerListener(this, accelerometer, 1);
        }
    }

    private void initSound() {
        if (this.mePSXeSound == null) {
            this.mePSXeSound = new ePSXeSound();
            this.mePSXeSound.setePSXeLib(this.f153e);
            this.mePSXeSound.setsoundqa(this.emu_menu2_soundqa);
            this.mePSXeSound.setsoundlatency(this.emu_menu2_soundlatency);
            this.mePSXeSound.start();
        }
    }

    private void initScreenOrientation() {
        if (this.emu_screen_orientation == 0 || this.emu_screen_orientation == 3 || this.emu_player_mode == 10) {
            setRequestedOrientation(0);
        } else if (this.emu_screen_orientation == 1) {
            setRequestedOrientation(1);
        } else if (this.emu_screen_orientation == 2) {
            setRequestedOrientation(8);
        }
    }

    private void mcheckLicense() {
        if (this.emu_enable_check != 1 || getLicenseResult()) {
            return;
        }
        this.mePSXeView.setlicense(false);
    }

    protected boolean getLicenseResult() {
        // Stub implementation - always return true for now
        // This would normally check license validation
        return true;
    }

    protected void checkLicense() {
        // Stub implementation - license check would go here
        // For now, just assume license is valid
    }

    public void initJoysticks() {
        String label;
        if (this.osVersion >= 12) {
            int[] deviceIds = InputDevice.getDeviceIds();
            String[] tmpId = new String[4];
            tmpId[0] = "none";
            tmpId[1] = "none";
            tmpId[2] = "none";
            tmpId[3] = "none";
            for (int i = 0; i < 4; i++) {
                tmpId[i] = this.mePSXeReadPreferences.getPadAnalogPadID(i + 1);
            }
            if (!tmpId[0].equals("none") && !tmpId[0].equals("virtual") && tmpId[1].equals("none") && tmpId[2].equals("none") && tmpId[3].equals("none")) {
                this.gamepadmatch = 0;
            }
            for (int i2 = 0; i2 < 4; i2++) {
                this.analogpadidString[i2] = this.mePSXeReadPreferences.getPadAnalogPadID(i2 + 1);
                Log.e("epsxekey", "getPadAnalogPadID(" + (i2 + 1) + ") = " + this.analogpadidString[i2]);
                this.analogpaddescString[i2] = this.mePSXeReadPreferences.getPadAnalogPadDesc(i2 + 1);
                if (!this.analogpadidString[i2].equals("none")) {
                    if (this.analogpadidString[i2].equals("virtual")) {
                        if (i2 == 0) {
                            this.emu_pad_type[0] = 0;
                        } else if (i2 == 1 && this.emu_pad_type[0] != 0) {
                            this.emu_pad_type[1] = 0;
                        }
                    } else {
                        this.analogrange[i2] = this.mePSXeReadPreferences.getPadAnalogRange(i2 + 1);
                        this.analogleftx[i2] = this.mePSXeReadPreferences.getPadAnalogLeftx(i2 + 1);
                        this.analoglefty[i2] = this.mePSXeReadPreferences.getPadAnalogLefty(i2 + 1);
                        this.analogrightx[i2] = this.mePSXeReadPreferences.getPadAnalogRightx(i2 + 1);
                        this.analogrighty[i2] = this.mePSXeReadPreferences.getPadAnalogRighty(i2 + 1);
                        this.analogl2[i2] = this.mePSXeReadPreferences.getPadAnalogL2(i2 + 1);
                        this.analogr2[i2] = this.mePSXeReadPreferences.getPadAnalogR2(i2 + 1);
                        this.analoghatdef[i2] = this.mePSXeReadPreferences.getPadAnalogHat(i2 + 1);
                        if (this.analogl2[i2] == 17 || this.analogl2[i2] == 18 || this.analogl2[i2] == 19 || this.analogl2[i2] == 22 || this.analogl2[i2] == 23) {
                            this.analogl2range[i2] = 0;
                        } else {
                            this.analogl2range[i2] = 1;
                        }
                        if (this.analogr2[i2] == 17 || this.analogr2[i2] == 18 || this.analogr2[i2] == 19 || this.analogr2[i2] == 22 || this.analogr2[i2] == 23) {
                            this.analogr2range[i2] = 0;
                        } else {
                            this.analogr2range[i2] = 1;
                        }
                        this.analogleftdzi[i2] = this.mePSXeReadPreferences.getPadAnalogLeftdz(i2 + 1);
                        this.analogrightdzi[i2] = this.mePSXeReadPreferences.getPadAnalogRightdz(i2 + 1);
                        this.analogleftdz[i2] = this.analogleftdzi[i2] / 100.0f;
                        this.analogrightdz[i2] = this.analogrightdzi[i2] / 100.0f;
                        int in = this.analogpadidString[i2].indexOf("###");
                        if (in != -1) {
                            label = this.analogpadidString[i2].substring(3, this.analogpadidString[i2].indexOf("###", 4));
                        } else {
                            label = "" + this.analogpadidString[i2];
                        }
                        if (this.mePSXeReadPreferences.getPadAnalogProfile(i2 + 1).equals("moga")) {
                            this.analogdpadfromanalog[i2] = 1;
                        }
                        if (label.equals("bluez")) {
                            this.bluezdriver[i2] = this.mePSXeReadPreferences.getPadAnalogProfile(i2 + 1);
                            if (!this.bluezdriver[i2].equals("0")) {
                                this.bluezenabled = true;
                                this.bluezaddr[i2] = this.analogpadidString[i2].substring(11, this.analogpadidString[i2].indexOf("###", 11));
                                this.bluezpad[i2] = 1;
                                Log.e("epsxe", "init bluez: addr " + this.bluezaddr[i2] + " driver " + this.bluezdriver[i2]);
                            }
                        } else if (label.equals("moganative")) {
                            this.mogapad = i2;
                            Log.e("epsxe", "init moga native");
                        } else {
                            int j = 0;
                            while (true) {
                                if (j >= deviceIds.length) {
                                    break;
                                }
                                InputDevice device = InputDevice.getDevice(deviceIds[j]);
                                if (device != null) {
                                    int sources = device.getSources();
                                    if ((16777232 & sources) == 16777232 || (sources & InputDeviceCompat.SOURCE_GAMEPAD) == 1025 || ((sources & 257) == 257 && device.getName().startsWith("GS "))) {
                                        String s = device.toString();
                                        String n = device.getName();
                                        if (n != null && n.contains("NVIDIA Controller")) {
                                            this.emu_exit_mapto_menu = 1;
                                        }
                                        int val = s.indexOf(label);
                                        if (val != -1) {
                                            Boolean used = false;
                                            for (int k = 0; k < i2; k++) {
                                                if (deviceIds[j] == this.analogpadid[k]) {
                                                    used = true;
                                                }
                                            }
                                            if (!used.booleanValue()) {
                                                this.analogpadid[i2] = deviceIds[j];
                                                Log.e("epsxekey", "joystick(" + i2 + ") found id=" + this.analogpadid[i2]);
                                                break;
                                            }
                                        } else {
                                            continue;
                                        }
                                    }
                                }
                                j++;
                            }
                            if (this.analogpadid[i2] == -1) {
                                Log.e("epsxekey", "joystick" + i2 + " not found " + label);
                            }
                        }
                    }
                }
            }
        }
    }

    void initBluez() {
        if (this.bluezenabled) {
            this.bluezInput = new bluezDriver(this, this.f153e, this.bluezaddr, this.bluezdriver, this.bluezpad);
        }
    }

    void initMoga() {
        if (this.mogapad != -1) {
            this.mogaInput = new mogaDriver(this, this.f153e, this.mogapad);
        }
    }

    private void showOptionsMenuDialog(Context cont, int emu_ui_menu_mode) {
        if ((Build.VERSION.SDK_INT >= 11 && Build.VERSION.SDK_INT <= 13) || DeviceUtil.isKindleFire() || this.emu_androidtv || DeviceUtil.isMenuDialog(emu_ui_menu_mode)) {
            alertdialog_menu(this);
        } else {
            openOptionsMenu();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int check_bios(int mode) {
        try {
            if (this.mePSXeReadPreferences == null) {
                this.mePSXeReadPreferences = new ePSXeReadPreferences(this);
            }
            int hlebios = this.mePSXeReadPreferences.getBiosHle();
            Log.e("epsxe", "getBios " + this.mePSXeReadPreferences.getBios() + " hle: " + hlebios);
            boolean bFound = FileUtil.isFileBios(this.mePSXeReadPreferences.getBios());
            if (mode == 0 && hlebios == 1) {
                bFound = true;
            }
            if (!bFound) {
                new ScanBiosTask(this, hlebios, mode).execute(Environment.getExternalStorageDirectory().getAbsolutePath());
                return -1;
            }
            this.currentPath = this.mePSXeReadPreferences.getIsoPath();
            Log.e("epsxe", "currentPath from save " + this.mePSXeReadPreferences.getIsoPath());
            return 0;
        } catch (Exception e) {
            return -1;
        }
    }

    private boolean checkrestoreResumeGame(String cIsoName) {
        File f = new File(ContextCompat.getDataDir(this), "epsxe/sstates/savetmp");
        StringBuilder text = new StringBuilder();
        StringBuilder slot = new StringBuilder();
        StringBuilder padtype = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            this.mIsoName.setmIsoSlot(0);
            String line = br.readLine();
            if (line != null) {
                text.append(line);
            }
            String line2 = br.readLine();
            if (line2 != null) {
                slot.append(line2);
                try {
                    this.mIsoName.setmIsoSlot(Integer.parseInt(slot.toString()));
                } catch (Exception e) {
                    this.mIsoName.setmIsoSlot(0);
                }
            }
            String line3 = br.readLine();
            if (line3 != null) {
                padtype.append(line3);
                this.emu_padType = Integer.parseInt(padtype.toString());
            }
            String IsoName2 = text.toString();
            f.delete();
            this.snapRestoring = false;
            return cIsoName.equals(IsoName2);
        } catch (IOException e2) {
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void restoreResumeGame(String cIsoName) {
        File f = new File(ContextCompat.getDataDir(this), "epsxe/sstates/savetmp");
        StringBuilder text = new StringBuilder();
        StringBuilder slot = new StringBuilder();
        StringBuilder padtype = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            this.mIsoName.setmIsoSlot(0);
            String line = br.readLine();
            if (line != null) {
                text.append(line);
            }
            String line2 = br.readLine();
            if (line2 != null) {
                slot.append(line2);
                try {
                    this.mIsoName.setmIsoSlot(Integer.parseInt(slot.toString()));
                } catch (Exception e) {
                    this.mIsoName.setmIsoSlot(0);
                }
            }
            String line3 = br.readLine();
            if (line3 != null) {
                padtype.append(line3);
                this.emu_padType = Integer.parseInt(padtype.toString());
            }
            String IsoName2 = text.toString();
            if (this.emu_gui == 0) {
                if (cIsoName.equals(IsoName2)) {
                    f.delete();
                    this.mePSXeView.setsnaprestoring(true);
                    this.snapRestoring = false;
                    return;
                }
                f.delete();
                return;
            }
            f.delete();
            if (check_bios(0) != -1) {
                File isoName = new File(IsoName2);
                if (isoName.exists()) {
                    Log.e("epsxe", "restoring " + IsoName2);
                    Intent myIntent = new Intent(this, (Class<?>) FileChooser.class);
                    myIntent.putExtra("com.epsxe.ePSXe.fcMode", "EXEC_ISO");
                    myIntent.putExtra("com.epsxe.ePSXe.isoName", IsoName2);
                    myIntent.putExtra("com.epsxe.ePSXe.isoSlot", "" + this.mIsoName.getmIsoSlot());
                    myIntent.putExtra("com.epsxe.ePSXe.xperiaplay", "" + this.emu_xperiaplay);
                    myIntent.putExtra("com.epsxe.ePSXe.padType", "" + this.emu_padType);
                    startActivity(myIntent);
                    finish();
                }
            }
        } catch (IOException e2) {
        }
    }

    private void alertdialog_restoreGame(final String IsoName) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(R.string.main_restoregame);
        alertDialog.setMessage(getString(R.string.main_wantquitrestore));
        alertDialog.setCancelable(false);
        alertDialog.setButton(getString(R.string.net_cancel), new DialogInterface.OnClickListener() { // from class: com.epsxe.ePSXe.ePSXe.2
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                File f = new File(ContextCompat.getDataDir(ePSXe.this), "epsxe/sstates/savetmp");
                f.delete();
            }
        });
        alertDialog.setButton2(getString(R.string.main_yesquitrestore), new DialogInterface.OnClickListener() { // from class: com.epsxe.ePSXe.ePSXe.3
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                ePSXe.this.restoreResumeGame(IsoName);
            }
        });
        alertDialog.setIcon(R.drawable.icon);
        alertDialog.show();
    }

    private boolean check_savetmp_snap(String IsoName) {
        File f = new File(ContextCompat.getDataDir(this), "epsxe/sstates/savetmp");
        if (f.exists()) {
            if (this.emu_gui == 1) {
                if (this.emu_ui_resume_dialog == 0) {
                    restoreResumeGame(IsoName);
                    return true;
                }
                alertdialog_restoreGame(IsoName);
                return true;
            }
            if (checkrestoreResumeGame(IsoName)) {
                if (this.emu_autosave == 0 && this.mePSXeView != null) {
                    this.mePSXeView.setsnaprestoring(true);
                }
                return false;
            }
        }
        return false;
    }

    private void savetmp_snapshot(String isoName, int slot, int padType) {
        try {
            File root = new File(ContextCompat.getDataDir(this), "epsxe/sstates/");
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, "savetmp");
            FileWriter writer = new FileWriter(gpxfile);
            writer.append((CharSequence) isoName);
            writer.append((CharSequence) ("\n" + slot));
            writer.append((CharSequence) ("\n" + padType));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initSdCard() {
        boolean status = true;
        File extStore = ContextCompat.getDataDir(this);
        String mainDir = extStore.getAbsolutePath() + "/epsxe";
        this.sdCardPath = extStore.getPath();
        if (this.currentPath.equals("")) {
            this.currentPath = this.sdCardPath;
        }
        File f = new File(mainDir);
        if (!f.exists()) {
            status = new File(mainDir).mkdir();
        }
        if (status) {
            File m = new File(mainDir + "/memcards");
            if (!m.exists()) {
                new File(mainDir + "/memcards").mkdir();
            }
            File m2 = new File(mainDir + "/memcards/games");
            if (!m2.exists()) {
                new File(mainDir + "/memcards/games").mkdir();
            }
            File m3 = new File(mainDir + "/memcards/net");
            if (!m3.exists()) {
                new File(mainDir + "/memcards/net").mkdir();
            }
            File s = new File(mainDir + "/sstates");
            if (!s.exists()) {
                new File(mainDir + "/sstates").mkdir();
            }
            File b = new File(mainDir + "/bios");
            if (!b.exists()) {
                new File(mainDir + "/bios").mkdir();
            }
            File i = new File(mainDir + "/isos");
            if (!i.exists()) {
                new File(mainDir + "/isos").mkdir();
            }
            File c = new File(mainDir + "/cheats");
            if (!c.exists()) {
                new File(mainDir + "/cheats").mkdir();
            }
            File o = new File(mainDir + "/config");
            if (!o.exists()) {
                new File(mainDir + "/config").mkdir();
            }
            File x = new File(mainDir + "/idx");
            if (!x.exists()) {
                new File(mainDir + "/idx").mkdir();
            }
            File p = new File(mainDir + "/patches");
            if (!p.exists()) {
                new File(mainDir + "/patches").mkdir();
            }
            File u = new File(mainDir + "/plugins");
            if (!u.exists()) {
                new File(mainDir + "/plugins").mkdir();
            }
            File j = new File(mainDir + "/faqs");
            if (!j.exists()) {
                new File(mainDir + "/faqs").mkdir();
            }
            File h = new File(mainDir + "/shaders");
            if (!h.exists()) {
                new File(mainDir + "/shaders").mkdir();
            }
        }
    }

    @TargetApi(14)
    private void setMenuButtonMode(int osVersion) {
        if (osVersion >= 11) {
            this.emu_exit_mapto_menu = 1;
            if ((osVersion <= 10 || (osVersion >= 14 && ViewConfiguration.get(this).hasPermanentMenuKey())) && osVersion < 21) {
                this.emu_exit_mapto_menu = 0;
            }
        }
        if (this.emu_androidtv) {
            this.emu_exit_mapto_menu = 1;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isStoragePermissionGranted() {
//        if (Build.VERSION.SDK_INT >= 23) {
//            if (checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
//                Log.v("epsxe", "Permission is granted");
//                return true;
//            }
//            Log.v("epsxe", "Permission is revoked");
//            requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 1);
//            return false;
//        }
//        Log.v("epsxe", "Permission is granted");
        return true;
    }

    @Override // android.app.Activity
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == 0) {
            Log.v("epsxe", "Permission: " + permissions[0] + "was " + grantResults[0]);
        }
    }

    @Override // android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        Log.d("ePSXeDebug", "=== ePSXe.onCreate() STARTED ===");
        super.onCreate(savedInstanceState);
        Log.e("epsxelf", "onCreate");

        this.emuStatus = 0;
        this.emuStatusPrev = 0;
        this.emu_androidtv = DeviceUtil.isAndroidTV(this);
        if (this.mePSXeReadPreferences == null) {
            this.mePSXeReadPreferences = new ePSXeReadPreferences(this);
        }
        this.osVersion = Integer.parseInt(Build.VERSION.SDK);
        initSdCard();
        this.f152d = new libdetect();
        this.emu_enable_neon = this.f152d.isNeon();
        Log.e("epsxe", "neon detected: " + this.emu_enable_neon);
        this.emu_enable_x86 = this.f152d.isX86();
        Log.e("epsxe", "x86 detected: " + this.emu_enable_x86);
        this.emu_enable_64bits = this.f152d.is64bits();
        Log.e("epsxe", "64bits detected: " + this.emu_enable_64bits);
        this.emu_enable_cores = this.f152d.getCpuCount();
        Log.e("epsxe", "cores detected: " + this.emu_enable_cores);
        if (this.osVersion >= 21) {
            Log.e("epsxe", "ABIs=" + Arrays.toString(Build.SUPPORTED_ABIS));
        }
        setMenuButtonMode(this.osVersion);
        DeviceUtil.setUILanguage(getBaseContext(), this.mePSXeReadPreferences.getMiscUILanguage());
        getWindow().setFlags(1024, 1024);
        requestWindowFeature(1);
        if (this.emu_enable_check == 1) {
            checkLicense();
        }
        if (this.mePSXeReadPreferences.getPadAnalogPadID(1).contains("xperiaplay")) {
            this.emu_xperiaplay = 1;
        }
        String param = getIntent().getStringExtra("com.epsxe.ePSXe.isoName");
        String param2 = getIntent().getStringExtra("com.epsxe.ePSXe.snapRestore");
        String param3 = getIntent().getStringExtra("com.epsxe.ePSXe.isoSlot");
        String param4 = getIntent().getStringExtra("com.epsxe.ePSXe.gui");
        String param5 = getIntent().getStringExtra("com.epsxe.ePSXe.padType");
        String param6 = getIntent().getStringExtra("com.epsxe.ePSXe.servermode");

        Log.d("ePSXeDebug", "ePSXe: Intent extras received:");
        Log.d("ePSXeDebug", "ePSXe: - isoName: " + param);
        Log.d("ePSXeDebug", "ePSXe: - snapRestore: " + param2);
        Log.d("ePSXeDebug", "ePSXe: - isoSlot: " + param3);
        Log.d("ePSXeDebug", "ePSXe: - gui: " + param4);
        Log.d("ePSXeDebug", "ePSXe: - padType: " + param5);
        Log.d("ePSXeDebug", "ePSXe: - servermode: " + param6);
        if (this.emuStatus == 0) {
            int slot = 0;
            if (param != null && param.length() > 0) {
                this.mIsoName.setmIsoName(param);
            }
            if (param2 != null && param2.length() > 0) {
                this.snapRestoring = true;
            }
            if (param3 != null && param3.length() > 0) {
                slot = Integer.parseInt(param3);
                this.mIsoName.setmIsoSlot(slot);
            }
            if (param4 != null && param4.length() > 0) {
                this.emu_gui = Integer.parseInt(param4);
            }
            if (param5 != null && param5.length() > 0) {
                this.emu_padType = Integer.parseInt(param5);
            }
            if (param6 != null && param6.length() > 0) {
                this.serverMode = Integer.parseInt(param6);
            }
            if (true) {
                // Fix pad type
                this.emu_padType = 1;
                // Get access to permissions
                ePSXe.this.isStoragePermissionGranted();
                // Switch to simulated bios
//                SharedPreferences sharedPreferences = ePSXeApplication.getDefaultSharedPreferences(this);
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.putString("biosHlePref", "1");
//                editor.commit();
                // Run game
                String gameFileName = getCacheDir() + "/Game/Strider (USA).cue";
                this.mIsoName.setmIsoName(gameFileName);
                this.snapRestoring = false;
                slot = 0;
                this.mIsoName.setmIsoSlot(slot);
                this.emu_gui = 0;
                this.serverMode = 0;
                runIso(gameFileName, slot);
            } else if (this.mIsoName.getmIsoName().length() > 0) {
                if (this.mIsoName.getmIsoName().equals("___NETWORK___") && this.serverMode == 2) {
                    GetIPAddressDialog.showGetIPAddressDialog(this, this.f153e, this.serverMode, this.mePSXeReadPreferences.getNetplayServer(), 0, "");
                    return;
                }
                runIso(this.mIsoName.getmIsoName(), slot);
            } else {
                setContentView(R.layout.mainmenu);
                ListView m_listview = (ListView) findViewById(R.id.list);
                Loadmenulist(m_listview);
                this.enableChangeOrientation = true;
            }
            this.emu_ui_resume_dialog = 0; //this.mePSXeReadPreferences.getUiresumedialog();
            if (check_savetmp_snap(this.mIsoName.getmIsoName()) || this.emuStatus == 0) {
            }
        }
    }

    private void quitEmulation() {
        if (this.emuStatus == 1) {
            this.mePSXeView.setSaveMode(DeviceUtil.getDevicesWorkaround(this.emu_renderer, this.emu_menu2_gpumtmodeS), this.emu_autosave);
            this.mePSXeSound.exit();
            this.emuStatus = 3;
            savetmp_snapshot(this.mIsoName.getmIsoName(), this.mIsoName.getmIsoSlot(), this.emu_padType);
            finish();
            return;
        }
        if (this.emuStatus == 2) {
            this.mePSXeSound.exit();
            this.emuStatus = 3;
            this.f153e.quit();
            finish();
        }
    }

    private void pauseEmulation() {
        if (mePSXeView != null) {
            mePSXeView.onPause(DeviceUtil.getDevicesWorkaround(this.emu_renderer, this.emu_menu2_gpumtmodeS), this.emu_autosave);
            glReallyPaused = true;
        }
    }

    private void resumeEmulation() {
        if (glReallyPaused && mePSXeView != null) {
            mePSXeView.onResume();
            glReallyPaused = false;
        }
    }

    private void stopEmulation() {
        if (this.emuStatus == 3) {
            if (this.emuStatusPrev == 1) {
                Log.e("epsxelf", "onStop emulation ePSXe Activity running game");
                this.mePSXeView.onStop();
                this.mePSXeSound.exit();
                this.emuStatus = 0;
                Log.e("epsxelf", "onStop previous store saveTmp file");
                savetmp_snapshot(this.mIsoName.getmIsoName(), this.mIsoName.getmIsoSlot(), this.emu_padType);
                Log.e("epsxelf", "onStop previous finish");
                finish();
                return;
            }
            if (this.emuStatusPrev == 2) {
                Log.e("epsxelf", "onStop emulation ePSXe Activity running bios");
                this.emuStatus = 0;
                this.mePSXeSound.exit();
                this.f153e.quit();
                finish();
            }
        }
    }

    @Override
    protected void onPause() {
        Log.e("epsxelf", "onPause");
        super.onPause();
        if (this.mogaInput != null) {
            this.mogaInput.onPause();
        }
        hidePadHandler.removeCallbacks(hidePadRunnable);
        if (this.emu_ui_pause_support != 0) {
            pauseEmulation();
        } else {
            quitEmulation();
        }
    }

    private void restartToPreferences() {
        Intent mainMenuIntent = new Intent(getApplicationContext(), ePSXe.class);
        mainMenuIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        Intent prefsIntent = new Intent(getApplicationContext(), ePSXePreferences.class);

        startActivities(new Intent[]{mainMenuIntent, prefsIntent});

        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    @Override
    protected void onResume() {
        Log.e("epsxelf", "onResume");
        super.onResume();
        if (this.mogaInput != null) {
            this.mogaInput.onResume();
        }
        runHidePadHandler(hidePadRunnable);
        if (this.emu_ui_pause_support == 1) {
            resumeEmulation();
        }
    }

    @Override // android.app.Activity
    protected void onStop() {
        Log.e("epsxelf", "onStop");
        super.onStop();
        if (this.emu_ui_pause_support == 1) {
            stopEmulation();
        }
    }

    @Override // com.epsxe.ePSXe.LicenseCheckActivity, android.app.Activity
    protected void onDestroy() {
        Log.e("epsxelf", "onDestroy");

        if (hidePadHandler != null) {
            hidePadHandler.removeCallbacks(hidePadRunnable);
        }
        if (mHandler != null) {
            mHandler.removeCallbacks(mLaunchTask);
        }
        if (handlerErr != null) {
            handlerErr.removeCallbacks(runnableErr);
        }

        if (this.mePSXeView != null) {
            this.mePSXeView.onStop();
            this.mePSXeView = null;
        }

        if (this.mePSXeSound != null) {
            this.mePSXeSound.exit();
            this.mePSXeSound = null;
        }

        if (this.f153e != null) {
            this.f153e.quit();
            this.f153e = null;
        }

        if (this.bluezenabled && this.bluezInput != null) {
            this.bluezenabled = false;
            this.bluezInput.bluezStop();
            this.bluezInput = null;
        }

        if (this.mogapad != -1) {
            this.mogapad = -1;
            if (this.mogaInput != null) {
                this.mogaInput.mogaStop();
                this.mogaInput = null;
            }
        }

        super.onDestroy();
    }

    @Override // android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration newConfig) {
        Log.e("epsxe", "onconfigurationchanged");
        super.onConfigurationChanged(newConfig);
        if (this.enableChangeOrientation) {
            if (this.mePSXeReadPreferences != null) {
                DeviceUtil.setUILanguage(getBaseContext(), this.mePSXeReadPreferences.getMiscUILanguage());
            }
            setContentView(R.layout.mainmenu);
            ListView m_listview = (ListView) findViewById(R.id.list);
            Loadmenulist(m_listview);
        }
    }

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, 1, 0, R.string.menu_rungame).setIcon(android.R.drawable.ic_menu_add);
        menu.add(0, 2, 0, R.string.menu_runbios).setIcon(android.R.drawable.ic_menu_directions);
        menu.add(0, 23, 0, R.string.menu_moreoptions).setIcon(android.R.drawable.ic_menu_add);
        menu.add(0, 4, 0, R.string.menu_loadstate).setIcon(android.R.drawable.ic_menu_set_as);
        menu.add(0, 5, 0, R.string.menu_savestate).setIcon(android.R.drawable.ic_menu_save);
        menu.add(0, 14, 0, R.string.menu_framelimit).setIcon(android.R.drawable.ic_menu_add);
        menu.add(0, 14, 0, R.string.menu_framelimit).setIcon(android.R.drawable.ic_menu_add);
        menu.add(0, 6, 0, R.string.menu_preferences).setIcon(android.R.drawable.ic_menu_preferences);
        menu.add(0, 9, 0, R.string.menu_help).setIcon(android.R.drawable.ic_menu_help);
        menu.add(0, 7, 0, R.string.menu_about).setIcon(android.R.drawable.ic_menu_info_details);
        menu.add(0, 8, 0, R.string.menu_quit).setIcon(android.R.drawable.ic_menu_revert);
        return true;
    }

    @Override // android.app.Activity
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (this.emu_enable_check == 1) {
            getLicenseResult();
        }
        menu.clear();
        if (this.emuStatus == 0) {
            menu.add(0, 18, 0, R.string.menu_runserver).setIcon(android.R.drawable.ic_menu_add);
            menu.add(0, 19, 0, R.string.menu_runclient).setIcon(android.R.drawable.ic_menu_directions);
            menu.add(0, 21, 0, R.string.menu_gamepadinfo).setIcon(android.R.drawable.ic_menu_manage);
            menu.add(0, 22, 0, R.string.menu_prefinfo).setIcon(android.R.drawable.ic_menu_manage);
            menu.add(0, 8, 0, R.string.menu_quit).setIcon(android.R.drawable.ic_menu_revert);
        } else if (this.emuStatus == 1) {
            menu.add(0, 23, 0, R.string.menu_moreoptions).setIcon(android.R.drawable.ic_menu_add);
            menu.add(0, 4, 0, R.string.menu_loadstate).setIcon(android.R.drawable.ic_menu_set_as);
            menu.add(0, 5, 0, R.string.menu_savestate).setIcon(android.R.drawable.ic_menu_save);
            menu.add(0, 14, 0, R.string.menu_framelimit).setIcon(android.R.drawable.ic_menu_add);
            menu.add(0, 15, 0, R.string.menu_cheat).setIcon(android.R.drawable.ic_menu_add);
            if (this.f153e != null) {
                File extStore = ContextCompat.getDataDir(this);
                File fileTmp = new File(extStore.getAbsolutePath() + "/epsxe/faqs/" + this.f153e.getCode() + ".txt");
                if (fileTmp.exists()) {
                    menu.add(0, 17, 0, R.string.menu_faq).setIcon(android.R.drawable.ic_menu_info_details);
                    this.emu_enable_gamefaq = 1;
                }
            }
            if (this.emu_renderer == 2 || this.emu_renderer == 4 || this.emu_renderer == 5) {
                menu.add(0, 16, 0, R.string.menu_tools).setIcon(android.R.drawable.ic_menu_preferences);
            }
            if (this.emu_player_mode == 10) {
                menu.add(0, 10, 0, R.string.menu_splitv).setIcon(android.R.drawable.ic_menu_rotate);
                menu.add(0, 11, 0, R.string.menu_splith1).setIcon(android.R.drawable.ic_menu_rotate);
                menu.add(0, 12, 0, R.string.menu_splith2).setIcon(android.R.drawable.ic_menu_rotate);
            }
            menu.add(0, 8, 0, R.string.menu_quit).setIcon(android.R.drawable.ic_menu_revert);
        } else if (this.emuStatus == 2) {
            menu.add(0, 14, 0, R.string.menu_framelimit).setIcon(android.R.drawable.ic_menu_add);
            if (this.emu_renderer == 2 || this.emu_renderer == 4 || this.emu_renderer == 5) {
                menu.add(0, 16, 0, R.string.menu_tools).setIcon(android.R.drawable.ic_menu_preferences);
            }
            menu.add(0, 8, 0, R.string.menu_quit).setIcon(android.R.drawable.ic_menu_revert);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    private void forceQuitApp() {
        userRequestedExit = true;

        try {
            quitEmulation();
        } catch (Throwable t) {
            Log.e("epsxelf", "quitEmulation failed during forceQuit", t);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAndRemoveTask();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            finishAffinity(); // API 16+
        } else {
            finish();
        }

        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    private void alertdialog_gamefaq() {
        File extStore = ContextCompat.getDataDir(this);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        if (this.wvscrollrestored == 0) {
            if (this.mePSXeReadPreferences != null) {
                this.wvscrollx = this.mePSXeReadPreferences.getFaqPosX(this.f153e.getCode());
                this.wvscrolly = this.mePSXeReadPreferences.getFaqPosY(this.f153e.getCode());
            }
            this.wvscrollrestored = 1;
        }
        final WebView wv = new WebView(this);
        WebSettings webSettings = wv.getSettings();
        webSettings.setTextSize(WebSettings.TextSize.SMALLEST);
        wv.scrollTo(this.wvscrollx, this.wvscrolly);
        wv.loadUrl("file://" + extStore.getAbsolutePath() + "/epsxe/faqs/" + this.f153e.getCode() + ".txt");
        wv.setWebViewClient(new WebViewClient() { // from class: com.epsxe.ePSXe.ePSXe.4
            @Override // android.webkit.WebViewClient
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override // android.webkit.WebViewClient
            public void onPageFinished(WebView view, String url) {
                jumpTo();
            }

            private void jumpTo() {
                wv.postDelayed(new Runnable() { // from class: com.epsxe.ePSXe.ePSXe.4.1
                    @Override // java.lang.Runnable
                    public void run() {
                        wv.scrollTo(ePSXe.this.wvscrollx, ePSXe.this.wvscrolly);
                    }
                }, 3000L);
            }
        });
        alert.setView(wv);
        alert.setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: com.epsxe.ePSXe.ePSXe.5
            @Override // android.content.DialogInterface.OnCancelListener
            public void onCancel(DialogInterface dialog) {
                ePSXe.this.wvscrollx = wv.getScrollX();
                ePSXe.this.wvscrolly = wv.getScrollY();
                if (ePSXe.this.mePSXeReadPreferences != null) {
                    ePSXe.this.mePSXeReadPreferences.setFaqPos(ePSXe.this.f153e.getCode(), ePSXe.this.wvscrollx, ePSXe.this.wvscrolly);
                }
            }
        });
        alert.show();
    }

    public void alertdialog_quitGame_rate() {
        CharSequence[] items = {getString(R.string.main_noquitgame), getString(R.string.main_yesquitgame), getString(R.string.main_yesquitandrate)};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Do you want to quit game?");
        builder.setInverseBackgroundForced(true);
        builder.setIcon(R.drawable.icon);
        builder.setItems(items, new DialogInterface.OnClickListener() { // from class: com.epsxe.ePSXe.ePSXe.7
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                if (which != 0) {
                    if (which == 1) {
                        if (ePSXe.this.emu_autosave == 1) {
                            ePSXe.this.f153e.setsslot(15);
                            ePSXe.this.mePSXeView.setSaveslot(15);
                            ePSXe.this.mHandler.postDelayed(ePSXe.this.mLaunchTask, 1000L);
                            return;
                        }
                        ePSXe.this.mePSXeSound.exit();
                        ePSXe.this.emuStatus = 0;
                        if (ePSXe.this.emu_gui != 1) {
                            ePSXe.this.f153e.setSaveMode(1, 0);
                        } else {
                            Intent myIntent = new Intent(ePSXe.this, (Class<?>) ePSXe.class);
                            myIntent.setFlags(DriveFile.MODE_READ_ONLY);
                            ePSXe.this.startActivity(myIntent);
                            ePSXe.this.mePSXeView.setSaveMode(DeviceUtil.getDevicesWorkaround(ePSXe.this.emu_renderer, ePSXe.this.emu_menu2_gpumtmodeS), ePSXe.this.emu_autosave);
                        }
                        if (DeviceUtil.getDevicesWorkaround(ePSXe.this.emu_renderer, ePSXe.this.emu_menu2_gpumtmodeS) == 3 || ePSXe.this.emu_renderer == 2 || ePSXe.this.emu_renderer == 4 || ePSXe.this.emu_renderer == 5) {
                            ePSXe.this.f153e.quit();
                        }
                        ePSXe.this.finish();
                        return;
                    }
                    if (which == 2) {
                        if (ePSXe.this.emu_autosave == 1) {
                            ePSXe.this.f153e.setsslot(15);
                            ePSXe.this.mePSXeView.setSaveslot(15);
                            ePSXe.this.mHandler.postDelayed(ePSXe.this.mLaunchTask, 1000L);
                            return;
                        }
                        ePSXe.this.mePSXeSound.exit();
                        ePSXe.this.emuStatus = 0;
                        if (ePSXe.this.emu_gui != 1) {
                            ePSXe.this.f153e.setSaveMode(1, 0);
                        } else {
                            Uri uri = Uri.parse("market://details?id=com.epsxe.ePSXe");
                            Intent goToMarket = new Intent("android.intent.action.VIEW", uri);
                            goToMarket.addFlags(1208483840);
                            try {
                                ePSXe.this.startActivity(goToMarket);
                            } catch (ActivityNotFoundException e) {
                                ePSXe.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://play.google.com/store/apps/details?id=com.epsxe.ePSXe")));
                            }
                            ePSXe.this.mePSXeView.setSaveMode(DeviceUtil.getDevicesWorkaround(ePSXe.this.emu_renderer, ePSXe.this.emu_menu2_gpumtmodeS), ePSXe.this.emu_autosave);
                        }
                        if (DeviceUtil.getDevicesWorkaround(ePSXe.this.emu_renderer, ePSXe.this.emu_menu2_gpumtmodeS) == 3 || ePSXe.this.emu_renderer == 2 || ePSXe.this.emu_renderer == 4 || ePSXe.this.emu_renderer == 5) {
                            ePSXe.this.f153e.quit();
                        }
                        ePSXe.this.finish();
                    }
                }
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void quitGame_norate() {
        if (this.emu_autosave == 1) {
            this.f153e.setsslot(15);
            this.mePSXeView.setSaveslot(15);
            this.mHandler.postDelayed(this.mLaunchTask, 1000L);
            return;
        }

        if (this.mePSXeSound != null) {
            this.mePSXeSound.exit();
            this.mePSXeSound = null;
        }

        this.emuStatus = 0;

        if (this.emu_gui == 1) {
            Intent myIntent = new Intent(this, (Class<?>) ePSXe.class);
            myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(myIntent);
            if (this.mePSXeView != null) {
                this.mePSXeView.setSaveMode(DeviceUtil.getDevicesWorkaround(this.emu_renderer, this.emu_menu2_gpumtmodeS), this.emu_autosave);
            }
        } else {
            if (this.f153e != null) {
                this.f153e.setSaveMode(1, 0);
            }
        }

        if (this.f153e != null && (DeviceUtil.getDevicesWorkaround(this.emu_renderer, this.emu_menu2_gpumtmodeS) == 3 || this.emu_renderer == 2 || this.emu_renderer == 4 || this.emu_renderer == 5)) {
            this.f153e.quit();
            this.f153e = null;
        }
    }

    private void alertdialog_quitGame_norate() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.main_back);

        String message = getString(R.string.main_wantquitgame);
        if (this.f153e != null && this.f153e.getCode() != null) {
            message += " (" + this.f153e.getCode() + ")?\n" + this.f153e.getGameInfo();
        }
        builder.setMessage(message);

        builder.setNegativeButton(getString(R.string.main_noquitgame), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setPositiveButton(getString(R.string.main_yesquitgame), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                forceQuitApp();
            }
        });

        builder.setIcon(R.drawable.icon);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void alertdialog_quitGame() {
        forceQuitApp();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void quitGame_error() {
        this.mePSXeSound.exit();
        this.emuStatus = 0;
        if (this.emu_gui == 1) {
            Intent myIntent = new Intent(this, (Class<?>) ePSXe.class);
            myIntent.setFlags(DriveFile.MODE_READ_ONLY);
            startActivity(myIntent);
            this.mePSXeView.setSaveMode(DeviceUtil.getDevicesWorkaround(this.emu_renderer, this.emu_menu2_gpumtmodeS), 0);
        } else {
            this.f153e.setSaveMode(1, 0);
        }
        this.f153e.quit();
        finish();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void alertdialog_quitGame_error() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(R.string.main_err_tit);
        alertDialog.setMessage(getString(R.string.main_err_msg) + ": " + this.f153e.getError());
        alertDialog.setButton2(getString(R.string.main_err_quit), new DialogInterface.OnClickListener() { // from class: com.epsxe.ePSXe.ePSXe.10
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                ePSXe.this.quitGame_error();
            }
        });
        alertDialog.setIcon(R.drawable.icon);
        alertDialog.show();
    }

    @Override // android.app.Activity
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                if (this.emuStatus == 0 && isStoragePermissionGranted() && check_bios(0) != -1) {
                    Log.e("epsxe", "getMiscBrowsermode " + this.mePSXeReadPreferences.getMiscBrowsermode(this.emu_androidtv));
                    this.emu_browser_mode = this.mePSXeReadPreferences.getMiscBrowsermode(this.emu_androidtv);
                    Intent myIntent = this.emu_browser_mode == 2 ? new Intent(this, (Class<?>) gFileChooser.class) : new Intent(this, (Class<?>) FileChooser.class);
                    myIntent.putExtra("com.epsxe.ePSXe.fcMode", "SELECT_ISO");
                    myIntent.putExtra("com.epsxe.ePSXe.isoPath", this.currentPath);
                    myIntent.putExtra("com.epsxe.ePSXe.xperiaplay", "" + this.emu_xperiaplay);
                    myIntent.putExtra("com.epsxe.ePSXe.browserMode", "" + this.emu_browser_mode);
                    startActivity(myIntent);
                    finish();
                    break;
                }
                break;
            case 2:
                if (this.emuStatus == 0 && isStoragePermissionGranted() && check_bios(1) != -1) {
                    Intent myIntent2 = new Intent(this, (Class<?>) FileChooser.class);
                    myIntent2.putExtra("com.epsxe.ePSXe.fcMode", "RUN_BIOS");
                    myIntent2.putExtra("com.epsxe.ePSXe.isoPath", this.currentPath);
                    startActivity(myIntent2);
                    finish();
                    break;
                }
                break;
            case 4:
                if (this.emuStatus == 1) {
                    showSstateDialog(this, this.f153e, 0, this.sdCardPath, this.hlebiosrunning);
                    break;
                }
                break;
            case 5:
                if (this.emuStatus == 1) {
                    showSstateDialog(this, this.f153e, 1, this.sdCardPath, this.hlebiosrunning);
                    break;
                }
                break;
            case 6:
                if (this.emuStatus == 0 && isStoragePermissionGranted()) {
                    startActivity(new Intent(this, (Class<?>) ePSXePreferences.class));
                    finish();
                    break;
                }
                break;
            case 7:
                AboutDialog.showAboutDialog(this);
                break;
            case 8:
                if (this.emuStatus == 0) {
                    finish();
                    break;
                } else if (this.emuStatus == 1 || this.emuStatus == 2) {
                    alertdialog_quitGame();
                    break;
                }
                break;
            case 9:
                AboutDialog.showHelpDialog(this, this.mePSXeReadPreferences, this.emu_enable_x86, this.emu_enable_cores, this.emu_enable_64bits);
                break;
            case 10:
                if (this.emuStatus == 1) {
                    this.mePSXeView.setsplitmode(0);
                    break;
                }
                break;
            case 11:
                if (this.emuStatus == 1) {
                    this.mePSXeView.setsplitmode(1);
                    break;
                }
                break;
            case 12:
                if (this.emuStatus == 1) {
                    this.mePSXeView.setsplitmode(2);
                    break;
                }
                break;
            case 14:
                if (this.emuStatus == 1 || this.emuStatus == 2) {
                    this.mePSXeView.toggleframelimit();
                    break;
                }
                break;
            case 15:
                if (this.emuStatus == 1) {
                    CheatDialog.showCheatDialog(this, this.f153e);
                    break;
                }
                break;
            case 16:
                if (this.emuStatus == 1 || this.emuStatus == 2) {
                    if (this.emu_renderer == 2 || this.emu_renderer == 4) {
                        if (!this.emu_androidtv) {
                            this.mePSXeView.toggletools();
                            break;
                        } else {
                            alertdialog_gltoolbar(this);
                            break;
                        }
                    } else if (this.emu_renderer == 5) {
                        if (!this.emu_androidtv) {
                            this.mePSXeView.toggletools();
                            break;
                        } else {
                            alertdialog_gltoolbar2(this);
                            break;
                        }
                    }
                }
                break;
            case 17:
                if (this.emuStatus == 1) {
                    alertdialog_gamefaq();
                    break;
                }
                break;
            case 18:
                if (this.emuStatus == 0 && check_bios(0) != -1) {
                    Log.e("epsxe", "getMiscBrowsermode " + this.mePSXeReadPreferences.getMiscBrowsermode(this.emu_androidtv));
                    this.emu_browser_mode = this.mePSXeReadPreferences.getMiscBrowsermode(this.emu_androidtv);
                    Intent myIntent3 = new Intent(this, (Class<?>) FileChooser.class);
                    myIntent3.putExtra("com.epsxe.ePSXe.fcMode", "SELECT_ISO");
                    myIntent3.putExtra("com.epsxe.ePSXe.isoPath", this.currentPath);
                    myIntent3.putExtra("com.epsxe.ePSXe.xperiaplay", "" + this.emu_xperiaplay);
                    myIntent3.putExtra("com.epsxe.ePSXe.browserMode", "" + this.emu_browser_mode);
                    myIntent3.putExtra("com.epsxe.ePSXe.servermode", "1");
                    startActivity(myIntent3);
                    finish();
                    break;
                }
                break;
            case 19:
                if (this.emuStatus == 0 && check_bios(0) != -1) {
                    Log.e("epsxe", "getMiscBrowsermode " + this.mePSXeReadPreferences.getMiscBrowsermode(this.emu_androidtv));
                    this.emu_browser_mode = this.mePSXeReadPreferences.getMiscBrowsermode(this.emu_androidtv);
                    Intent myIntent4 = new Intent(this, (Class<?>) FileChooser.class);
                    myIntent4.putExtra("com.epsxe.ePSXe.fcMode", "SELECT_ISO");
                    myIntent4.putExtra("com.epsxe.ePSXe.isoPath", this.currentPath);
                    myIntent4.putExtra("com.epsxe.ePSXe.xperiaplay", "" + this.emu_xperiaplay);
                    myIntent4.putExtra("com.epsxe.ePSXe.browserMode", "" + this.emu_browser_mode);
                    myIntent4.putExtra("com.epsxe.ePSXe.servermode", "2");
                    startActivity(myIntent4);
                    finish();
                    break;
                }
                break;
            case 21:
                if (this.emuStatus == 0) {
                    ReportUtil.showReportGamepadDialog(this, this.mePSXeReadPreferences);
                    break;
                }
                break;
            case 22:
                if (this.emuStatus == 0) {
                    ReportUtil.showReportFullPreferencesDialog(this, this.mePSXeReadPreferences, this.emu_enable_x86, this.emu_enable_cores, this.emu_enable_64bits);
                    break;
                }
                break;
            case 23:
                if (this.emuStatus == 1) {
                    alertdialog_menu2(this);
                    break;
                }
                break;
            case 100:
                if (this.emuStatus == 1) {
                    ChangediscDialog.showChangediscDialog(this, this.f153e, this.currentPath, this.mIsoName);
                    break;
                }
                break;
        }

        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void alertdialog_multiplayer(final Context mCont) {
        String[] items = {getString(R.string.menu_multiplayer_server), getString(R.string.menu_multiplayer_client)};
        ListView gListView = new ListView(mCont);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(mCont, android.R.layout.simple_list_item_1, items);
        gListView.setAdapter((ListAdapter) adapter);
        AlertDialog.Builder builder = new AlertDialog.Builder(mCont);
        builder.setView(gListView);
        final AlertDialog multiAlert = builder.create();
        multiAlert.show();
        gListView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.epsxe.ePSXe.ePSXe.11
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> listView, View itemView, int position, long itemId) {
                switch (position) {
                    case 0:
                        if (ePSXe.this.check_bios(0) != -1) {
                            Log.e("epsxe", "getMiscBrowsermode " + ePSXe.this.mePSXeReadPreferences.getMiscBrowsermode(DeviceUtil.isAndroidTV(mCont)));
                            ePSXe.this.emu_browser_mode = ePSXe.this.mePSXeReadPreferences.getMiscBrowsermode(DeviceUtil.isAndroidTV(mCont));
                            Intent myIntent = new Intent(ePSXe.this, (Class<?>) FileChooser.class);
                            myIntent.putExtra("com.epsxe.ePSXe.fcMode", "SELECT_ISO");
                            myIntent.putExtra("com.epsxe.ePSXe.isoPath", ePSXe.this.currentPath);
                            myIntent.putExtra("com.epsxe.ePSXe.xperiaplay", "" + ePSXe.this.emu_xperiaplay);
                            myIntent.putExtra("com.epsxe.ePSXe.browserMode", "" + ePSXe.this.emu_browser_mode);
                            myIntent.putExtra("com.epsxe.ePSXe.servermode", "3");
                            ePSXe.this.startActivity(myIntent);
                            ePSXe.this.finish();
                            break;
                        } else {
                            return;
                        }
                    case 1:
                        if (ePSXe.this.check_bios(0) != -1) {
                            Log.e("epsxe", "getMiscBrowsermode " + ePSXe.this.mePSXeReadPreferences.getMiscBrowsermode(DeviceUtil.isAndroidTV(mCont)));
                            ePSXe.this.emu_browser_mode = ePSXe.this.mePSXeReadPreferences.getMiscBrowsermode(DeviceUtil.isAndroidTV(mCont));
                            Intent myIntent2 = new Intent(ePSXe.this, (Class<?>) FileChooser.class);
                            myIntent2.putExtra("com.epsxe.ePSXe.fcMode", "SELECT_ISO");
                            myIntent2.putExtra("com.epsxe.ePSXe.isoPath", ePSXe.this.currentPath);
                            myIntent2.putExtra("com.epsxe.ePSXe.xperiaplay", "" + ePSXe.this.emu_xperiaplay);
                            myIntent2.putExtra("com.epsxe.ePSXe.browserMode", "" + ePSXe.this.emu_browser_mode);
                            myIntent2.putExtra("com.epsxe.ePSXe.servermode", "4");
                            ePSXe.this.startActivity(myIntent2);
                            ePSXe.this.finish();
                            break;
                        } else {
                            return;
                        }
                }
                DialogUtil.closeDialog(multiAlert);
            }
        });
    }

    private void Loadmenulist(ListView m_listview) {
        String[] items = {getString(R.string.fbutton_run_game), getString(R.string.fbutton_run_bios), getString(R.string.fbutton_multiplayer), getString(R.string.fbutton_preferences), getString(R.string.fbutton_help), getString(R.string.fbutton_quit)};
        List<menuOption> fls = new ArrayList<>();
        for (int ind = 0; ind < 6; ind++) {
            fls.add(new menuOption(items[ind]));
        }
        menuArrayAdapter adapter = new menuArrayAdapter(this, R.layout.file_simple, fls);
        m_listview.setAdapter((ListAdapter) adapter);
        m_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.epsxe.ePSXe.ePSXe.12
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> listView, View itemView, int position, long itemId) {
                switch (position) {
                    case 0:
                        if (ePSXe.this.emuStatus == 0 && ePSXe.this.isStoragePermissionGranted() && ePSXe.this.check_bios(0) != -1) {
                            Log.e("epsxe", "getMiscBrowsermode " + ePSXe.this.mePSXeReadPreferences.getMiscBrowsermode(ePSXe.this.emu_androidtv));
                            ePSXe.this.emu_browser_mode = ePSXe.this.mePSXeReadPreferences.getMiscBrowsermode(ePSXe.this.emu_androidtv);
                            Intent myIntent = ePSXe.this.emu_browser_mode == 2 ? new Intent(ePSXe.this, (Class<?>) gFileChooser.class) : new Intent(ePSXe.this, (Class<?>) FileChooser.class);
                            myIntent.putExtra("com.epsxe.ePSXe.fcMode", "SELECT_ISO");
                            myIntent.putExtra("com.epsxe.ePSXe.isoPath", ePSXe.this.currentPath);
                            myIntent.putExtra("com.epsxe.ePSXe.xperiaplay", "" + ePSXe.this.emu_xperiaplay);
                            myIntent.putExtra("com.epsxe.ePSXe.browserMode", "" + ePSXe.this.emu_browser_mode);
                            ePSXe.this.startActivity(myIntent);
                            ePSXe.this.finish();
                            break;
                        }
                        break;
                    case 1:
                        if (ePSXe.this.emuStatus == 0 && ePSXe.this.isStoragePermissionGranted() && ePSXe.this.check_bios(1) != -1) {
                            Intent myIntent2 = new Intent(ePSXe.this, (Class<?>) FileChooser.class);
                            myIntent2.putExtra("com.epsxe.ePSXe.fcMode", "RUN_BIOS");
                            myIntent2.putExtra("com.epsxe.ePSXe.isoPath", ePSXe.this.currentPath);
                            ePSXe.this.startActivity(myIntent2);
                            ePSXe.this.finish();
                            break;
                        }
                        break;
                    case 2:
                        ePSXe.this.alertdialog_multiplayer(ePSXe.this);
                        break;
                    case 3:
                        ePSXe.this.startActivity(new Intent(ePSXe.this, (Class<?>) ePSXePreferences.class));
                        ePSXe.this.finish();
                        break;
                    case 4:
                        AboutDialog.showHelpDialog(ePSXe.this, ePSXe.this.mePSXeReadPreferences, ePSXe.this.emu_enable_x86, ePSXe.this.emu_enable_cores, ePSXe.this.emu_enable_64bits);
                        break;
                    case 5:
                        forceQuitApp();
                        break;
                }
            }
        });
    }

    private class menuOption implements Comparable<menuOption> {

        private String name;

        public menuOption(String n) {
            this.name = n;
        }

        public String getName() {
            return this.name;
        }

        @Override // java.lang.Comparable
        public int compareTo(menuOption o) {
            if (this.name != null) {
                return this.name.toLowerCase().compareTo(o.getName().toLowerCase());
            }
            throw new IllegalArgumentException();
        }
    }

    public class menuArrayAdapter extends ArrayAdapter<menuOption> {

        /* renamed from: c */
        private Context f154c;

        /* renamed from: id */
        private int f155id;
        private List<menuOption> items;

        public menuArrayAdapter(Context context, int textViewResourceId, List<menuOption> objects) {
            super(context, textViewResourceId, objects);
            this.f154c = context;
            this.f155id = textViewResourceId;
            this.items = objects;
        }

        @Override // android.widget.ArrayAdapter, android.widget.Adapter
        public menuOption getItem(int i) {
            return this.items.get(i);
        }

        @Override // android.widget.ArrayAdapter, android.widget.Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView t1;
            LayoutInflater vi;
            View v = convertView;
            if (v == null && (vi = (LayoutInflater) this.f154c.getSystemService("layout_inflater")) != null) {
                v = vi.inflate(this.f155id, (ViewGroup) null);
            }
            if (v != null) {
                menuOption o = this.items.get(position);
                if (o != null && (t1 = (TextView) v.findViewById(R.id.TextView10)) != null) {
                    t1.setText(o.getName());
                }
                if ((position & 1) == 1) {
                    v.setBackgroundDrawable(ePSXe.this.getResources().getDrawable(R.drawable.normaldark2));
                } else {
                    v.setBackgroundDrawable(ePSXe.this.getResources().getDrawable(R.drawable.normallight2));
                }
            }
            return v;
        }
    }

    public boolean OnNativeMotion(int action, int x, int y, int source, int device_id) {
        return true;
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (this.emu_mouse && this.onDrag) {
            int port = this.emu_menu2_input1type == 2 ? 0 : 4;
            int action = ev.getAction();
            int actionCode = action & 255;
            if (actionCode == 0) {
                this.onDragX = (int) ev.getX(0);
                this.onDragY = (int) ev.getY(0);
            }
            if (actionCode == 2 && this.onDragX != -1) {
                int cx = (int) ev.getX(0);
                int cy = (int) ev.getY(0);
                int x = (int) ((cx - this.onDragX) * this.emu_mouse_sen);
                int y = (int) ((cy - this.onDragY) * this.emu_mouse_sen);
                if (x != 0 || y != 0) {
                    this.f153e.setMouseData(port, 0, x, y, 0);
                }
                this.onDragX = cx;
                this.onDragY = cy;
            }
            if (actionCode == 1) {
                this.onDragX = -1;
                this.onDragY = -1;
            }
            return true;
        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean onMouseEmulationButton(int action, int button) {
        int port = this.emu_menu2_input1type == 2 ? 0 : 4;
        switch (action) {
            case 0:
                this.f153e.setMouseData(port, 1, 0, 0, button);
                break;
            case 1:
                this.f153e.setMouseData(port, 2, 0, 0, button);
                break;
        }

        return true;
    }

    @TargetApi(24)
    private boolean onMouseEmulation(MotionEvent event, int eventDevice) {
        int action = event.getActionMasked();
        int port = this.emu_menu2_input1type == 2 ? 0 : 4;
        switch (action) {
            case 2:
            case 7:
                int x = (int) (event.getAxisValue(27) * this.emu_mouse_sen);
                int y = (int) (event.getAxisValue(28) * this.emu_mouse_sen);
                if (x != 0 || y != 0) {
                    this.f153e.setMouseData(port, 0, x, y, 0);
                    break;
                }
            case 9:
                this.onDrag = false;
                break;
            case 10:
                this.onDrag = true;
                break;
            case 11:
                this.f153e.setMouseData(port, 1, 0, 0, event.getActionButton());
                break;
            case 12:
                this.f153e.setMouseData(port, 2, 0, 0, event.getActionButton());
                break;
        }
        return super.onGenericMotionEvent(event);
    }

    @Override // android.app.Activity
    @TargetApi(12)
    public boolean onGenericMotionEvent(MotionEvent event) {
        int x1;
        int y1;
        int x2;
        int y2;
        int hatx;
        int haty;
        InputDevice device = event.getDevice();
        if (device == null) {
            return false;
        }
        int mdev = -1;
        int eventDevice = device.getId();
        if (this.emu_mouse && event.getSource() == 8194) {
            return onMouseEmulation(event, eventDevice);
        }
        int i = 0;
        while (true) {
            if (i >= 4) {
                break;
            }
            if (eventDevice == this.analogpadid[i]) {
                mdev = i;
                break;
            }
            i++;
        }
        if (mdev == -1 && this.gamepadmatch == 1) {
            return false;
        }
        if (this.gamepadmatch == 0) {
            mdev = 0;
        }
        if (this.analogrange[mdev] == 1) {
            float lx = event.getAxisValue(this.analogleftx[mdev]);
            float ly = event.getAxisValue(this.analoglefty[mdev]);
            float rx = event.getAxisValue(this.analogrightx[mdev]);
            float ry = event.getAxisValue(this.analogrighty[mdev]);
            if (this.analogleftdzi[mdev] != 0) {
                lx = Math.abs(lx) < this.analogleftdz[mdev] ? 0.0f : (Math.signum(lx) * (Math.abs(lx) - this.analogleftdz[mdev])) / (1.0f - this.analogleftdz[mdev]);
                ly = Math.abs(ly) < this.analogleftdz[mdev] ? 0.0f : (Math.signum(ly) * (Math.abs(ly) - this.analogleftdz[mdev])) / (1.0f - this.analogleftdz[mdev]);
            }
            if (this.analogrightdzi[mdev] != 0) {
                rx = Math.abs(rx) < this.analogrightdz[mdev] ? 0.0f : (Math.signum(rx) * (Math.abs(rx) - this.analogrightdz[mdev])) / (1.0f - this.analogrightdz[mdev]);
                ry = Math.abs(ry) < this.analogrightdz[mdev] ? 0.0f : (Math.signum(ry) * (Math.abs(ry) - this.analogrightdz[mdev])) / (1.0f - this.analogrightdz[mdev]);
            }
            x1 = (int) (128.0f * lx);
            y1 = (int) (128.0f * ly);
            x2 = (int) (128.0f * rx);
            y2 = (int) (128.0f * ry);
            float ratio1 = MathUtil.GetRatio(x1, y1);
            float ratio2 = MathUtil.GetRatio(x2, y2);
            if (x1 != 0 || y1 != 0) {
                int tmpX = (int) (x1 * ratio1);
                int tmpY = (int) (y1 * ratio1);
                if (tmpX < -127) {
                    tmpX = -127;
                } else if (tmpX > 127) {
                    tmpX = 127;
                }
                if (tmpY < -127) {
                    tmpY = -127;
                } else if (tmpY > 127) {
                    tmpY = 127;
                }
                x1 = tmpX;
                y1 = tmpY;
            }
            if (x2 != 0 || y2 != 0) {
                int tmpX2 = (int) (x2 * ratio2);
                int tmpY2 = (int) (y2 * ratio2);
                if (tmpX2 < -127) {
                    tmpX2 = -127;
                } else if (tmpX2 > 127) {
                    tmpX2 = 127;
                }
                if (tmpY2 < -127) {
                    tmpY2 = -127;
                } else if (tmpY2 > 127) {
                    tmpY2 = 127;
                }
                x2 = tmpX2;
                y2 = tmpY2;
            }
        } else {
            float lx2 = event.getAxisValue(this.analogleftx[mdev]);
            float ly2 = event.getAxisValue(this.analoglefty[mdev]);
            float rx2 = event.getAxisValue(this.analogrightx[mdev]);
            float ry2 = event.getAxisValue(this.analogrighty[mdev]);
            if (this.analogleftdzi[mdev] != 0) {
                lx2 = Math.abs(lx2) < this.analogleftdz[mdev] ? 0.0f : (Math.signum(lx2) * (Math.abs(lx2) - this.analogleftdz[mdev])) / (1.0f - this.analogleftdz[mdev]);
                ly2 = Math.abs(ly2) < this.analogleftdz[mdev] ? 0.0f : (Math.signum(ly2) * (Math.abs(ly2) - this.analogleftdz[mdev])) / (1.0f - this.analogleftdz[mdev]);
            }
            if (this.analogrightdzi[mdev] != 0) {
                rx2 = Math.abs(rx2) < this.analogrightdz[mdev] ? 0.0f : (Math.signum(rx2) * (Math.abs(rx2) - this.analogrightdz[mdev])) / (1.0f - this.analogrightdz[mdev]);
                ry2 = Math.abs(ry2) < this.analogrightdz[mdev] ? 0.0f : (Math.signum(ry2) * (Math.abs(ry2) - this.analogrightdz[mdev])) / (1.0f - this.analogrightdz[mdev]);
            }
            x1 = (int) (127.0f * lx2);
            y1 = (int) (127.0f * ly2);
            x2 = (int) (127.0f * rx2);
            y2 = (int) (127.0f * ry2);
        }
        this.f153e.setpadanalog(mdev, 0, x1, y1);
        this.f153e.setpadanalog(mdev, 1, x2, y2);
        int l2 = (int) (event.getAxisValue(this.analogl2[mdev]) * 127.0f);
        int r2 = (int) (event.getAxisValue(this.analogr2[mdev]) * 127.0f);
        int tmp = this.pushedButtons[mdev];
        if (this.analoghatdef[mdev] == 0) {
            hatx = (int) event.getAxisValue(15);
            haty = (int) event.getAxisValue(16);
        } else {
            hatx = (int) event.getAxisValue(0);
            haty = (int) event.getAxisValue(1);
        }
        if (this.analoghatx[mdev] != hatx) {
            if (hatx == -1) {
                int[] iArr = this.pushedButtons;
                iArr[mdev] = iArr[mdev] & (-8193);
                int[] iArr2 = this.pushedButtons;
                iArr2[mdev] = iArr2[mdev] | 32768;
            } else if (hatx == 1) {
                int[] iArr3 = this.pushedButtons;
                iArr3[mdev] = iArr3[mdev] | 8192;
                int[] iArr4 = this.pushedButtons;
                iArr4[mdev] = iArr4[mdev] & (-32769);
            } else {
                int[] iArr5 = this.pushedButtons;
                iArr5[mdev] = iArr5[mdev] & (-8193);
                int[] iArr6 = this.pushedButtons;
                iArr6[mdev] = iArr6[mdev] & (-32769);
            }
            this.analoghatx[mdev] = hatx;
        }
        if (this.analoghaty[mdev] != haty) {
            if (haty == -1) {
                int[] iArr7 = this.pushedButtons;
                iArr7[mdev] = iArr7[mdev] | 4096;
                int[] iArr8 = this.pushedButtons;
                iArr8[mdev] = iArr8[mdev] & (-16385);
            } else if (haty == 1) {
                int[] iArr9 = this.pushedButtons;
                iArr9[mdev] = iArr9[mdev] & (-4097);
                int[] iArr10 = this.pushedButtons;
                iArr10[mdev] = iArr10[mdev] | 16384;
            } else {
                int[] iArr11 = this.pushedButtons;
                iArr11[mdev] = iArr11[mdev] & (-4097);
                int[] iArr12 = this.pushedButtons;
                iArr12[mdev] = iArr12[mdev] & (-16385);
            }
            this.analoghaty[mdev] = haty;
        }
        if (this.analogl2[mdev] < 48 && this.analogr2[mdev] < 48) {
            if (this.analogl2range[mdev] == 0) {
                if (l2 > 20) {
                    int[] iArr13 = this.pushedButtons;
                    iArr13[mdev] = iArr13[mdev] | 1;
                } else {
                    int[] iArr14 = this.pushedButtons;
                    iArr14[mdev] = iArr14[mdev] & (-2);
                }
            } else if (l2 > -102) {
                int[] iArr15 = this.pushedButtons;
                iArr15[mdev] = iArr15[mdev] | 1;
            } else {
                int[] iArr16 = this.pushedButtons;
                iArr16[mdev] = iArr16[mdev] & (-2);
            }
            if (this.analogr2range[mdev] == 0) {
                if (r2 > 20) {
                    int[] iArr17 = this.pushedButtons;
                    iArr17[mdev] = iArr17[mdev] | 2;
                } else {
                    int[] iArr18 = this.pushedButtons;
                    iArr18[mdev] = iArr18[mdev] & (-3);
                }
            } else if (r2 > -102) {
                int[] iArr19 = this.pushedButtons;
                iArr19[mdev] = iArr19[mdev] | 2;
            } else {
                int[] iArr20 = this.pushedButtons;
                iArr20[mdev] = iArr20[mdev] & (-3);
            }
        }
        if (tmp != this.pushedButtons[mdev]) {
            this.f153e.setPadDataMultitap(this.pushedButtons[0], this.pushedButtons[1], this.pushedButtons[2], this.pushedButtons[3]);
        }
        return this.analogdpadfromanalog[mdev] != 1;
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        InputDevice device;
        int sources = 0;
        if (this.osVersion >= 12 && (device = event.getDevice()) != null) {
            sources = device.getSources();
        }
        int eventDevice = event.getDeviceId();
        int mdev = -1;
        if (this.emu_ignore_bogus_keyup == 1 && event.getEventTime() == event.getDownTime() && eventDevice == -1) {
            return true;
        }
        int i = 0;
        while (true) {
            if (i >= 4) {
                break;
            }
            if (eventDevice != -1 && eventDevice == this.analogpadid[i]) {
                mdev = i;
                break;
            }
            i++;
        }
        if (keyCode == 0 && event.getScanCode() != 0) {
            keyCode = event.getScanCode();
        }
        int keyval = keyCode | (event.isAltPressed() ? 65536 : 0);
        if ((this.emuStatus == 1 || this.emuStatus == 2) && ((keyval != 4 && keyval != 82) || (16778513 & sources) == 16778513)) {
            for (int p = 0; p < 4; p++) {
                if (mdev == -1 || mdev == p || (this.gamepadmatch == 0 && p == 0)) {
                    for (int k = 0; k < 20; k++) {
                        if ((((event.isAltPressed() || keyCode == 57 || keyCode == 58) ? 65536 : 0) | keyCode) == this.keycodes[p][k]) {
                            int[] iArr = this.pushedButtons;
                            iArr[p] = iArr[p] & ((1 << k) ^ (-1));
                            this.f153e.setPadDataMultitap(this.pushedButtons[0], this.pushedButtons[1], this.pushedButtons[2], this.pushedButtons[3]);
                            return true;
                        }
                    }
                }
            }
            if (keyval == this.keycodesextra[12]) {
                this.mePSXeView.setframelimit();
                return true;
            }
            if (keyval == this.keycodesextra[13]) {
                this.f153e.disableautofire(0, 0);
                return true;
            }
        }
        return (keyval == 4 && this.emu_mouse && event.getSource() == 8194) ? onMouseEmulationButton(1, 2) : super.onKeyUp(keyCode, event);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateData(ArrayAdapter<String> adapter, ArrayList<String> list, int pos, String value) {
        list.set(pos, value);
        adapter.notifyDataSetChanged();
    }

    private void alertdialog_gltoolbar(final Context mCont) {
        this.emu_opengl_options = this.f153e.gpugetoptiongl();
        String[] items = new String[9];
        items[0] = "Frameskip=" + ((this.emu_opengl_options & 4096) != 0 ? 1 : 0);
        items[1] = "Offscreen Drawing=" + ((this.emu_opengl_options & 7) != 0 ? 1 : 0);
        items[2] = "Filter=" + ((this.emu_opengl_options & 56) != 0 ? (this.emu_opengl_options >> 3) & 7 : 0);
        items[3] = "Advanced Mask=" + ((this.emu_opengl_options & 16384) != 0 ? 1 : 0);
        items[4] = "Alpha Blending=" + ((this.emu_opengl_options & 32768) != 0 ? 1 : 0);
        items[5] = "Framebuffer Texture=" + ((this.emu_opengl_options & 448) != 0 ? 1 : 0);
        items[6] = "Mask Bit=" + ((this.emu_opengl_options & 8192) == 0 ? 0 : 1);
        items[7] = "Save";
        items[8] = "Exit";
        ListView gListView = new ListView(mCont);
        if (this.gladapter == null) {
            this.gllist.addAll(Arrays.asList(items));
            this.gladapter = new ArrayAdapter<>(mCont, android.R.layout.simple_list_item_1, this.gllist);
        }
        gListView.setAdapter((ListAdapter) this.gladapter);
        AlertDialog.Builder builder = new AlertDialog.Builder(mCont);
        builder.setView(gListView);
        final AlertDialog GLToolBarAlert = builder.create();
        GLToolBarAlert.show();
        gListView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.epsxe.ePSXe.ePSXe.13
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> listView, View itemView, int position, long itemId) {
                switch (position) {
                    case 0:
                        if ((ePSXe.this.emu_opengl_options & 4096) != 0) {
                            ePSXe.this.f153e.gpusetoptiongl(0, 4096, 0);
                        } else {
                            ePSXe.this.f153e.gpusetoptiongl(1, 4096, 0);
                        }
                        ePSXe.this.emu_opengl_options ^= 4096;
                        ePSXe.this.updateData(ePSXe.this.gladapter, ePSXe.this.gllist, position, "Frameskip=" + ((ePSXe.this.emu_opengl_options & 4096) == 0 ? 0 : 1));
                        break;
                    case 1:
                        if ((ePSXe.this.emu_opengl_options & 7) == 3) {
                            ePSXe.this.f153e.gpusetoptiongl(1, 4, 0);
                            ePSXe.this.emu_opengl_options = (ePSXe.this.emu_opengl_options & (-8)) | 4;
                        } else if ((ePSXe.this.emu_opengl_options & 7) == 4) {
                            ePSXe.this.f153e.gpusetoptiongl(0, 3, 0);
                            ePSXe.this.emu_opengl_options &= -8;
                        } else {
                            ePSXe.this.f153e.gpusetoptiongl(1, 3, 0);
                            ePSXe.this.emu_opengl_options = (ePSXe.this.emu_opengl_options & (-8)) | 3;
                        }
                        ePSXe.this.updateData(ePSXe.this.gladapter, ePSXe.this.gllist, position, "Offscreen Drawing=" + (ePSXe.this.emu_opengl_options & 7));
                        break;
                    case 2:
                        int val = ((ePSXe.this.emu_opengl_options >> 3) & 7) + 1;
                        if (val == 7) {
                            val = 0;
                        }
                        if ((val & 56) != 0) {
                            ePSXe.this.f153e.gpusetoptiongl(0, 32, 0);
                            ePSXe.this.emu_opengl_options &= -57;
                        } else {
                            ePSXe.this.f153e.gpusetoptiongl(1, val << 3, 0);
                            ePSXe.this.emu_opengl_options = (ePSXe.this.emu_opengl_options & (-57)) | (val << 3);
                        }
                        ePSXe.this.updateData(ePSXe.this.gladapter, ePSXe.this.gllist, position, "Filter=" + ((ePSXe.this.emu_opengl_options & 56) != 0 ? (ePSXe.this.emu_opengl_options >> 3) & 7 : 0));
                        break;
                    case 3:
                        if ((ePSXe.this.emu_opengl_options & 16384) != 0) {
                            ePSXe.this.f153e.gpusetoptiongl(0, 16384, 0);
                        } else {
                            ePSXe.this.f153e.gpusetoptiongl(1, 16384, 0);
                        }
                        ePSXe.this.emu_opengl_options ^= 16384;
                        ePSXe.this.updateData(ePSXe.this.gladapter, ePSXe.this.gllist, position, "Advanced Mask=" + ((ePSXe.this.emu_opengl_options & 16384) == 0 ? 0 : 1));
                        break;
                    case 4:
                        if ((ePSXe.this.emu_opengl_options & 32768) != 0) {
                            ePSXe.this.f153e.gpusetoptiongl(0, 32768, 0);
                        } else {
                            ePSXe.this.f153e.gpusetoptiongl(1, 32768, 0);
                        }
                        ePSXe.this.emu_opengl_options ^= 32768;
                        ePSXe.this.updateData(ePSXe.this.gladapter, ePSXe.this.gllist, position, "Alpha Blending=" + ((ePSXe.this.emu_opengl_options & 32768) == 0 ? 0 : 1));
                        break;
                    case 5:
                        int val2 = ((ePSXe.this.emu_opengl_options >> 6) & 7) + 1;
                        if (val2 > 3) {
                            val2 = 0;
                        }
                        if ((ePSXe.this.emu_opengl_options & 448) == 192) {
                            ePSXe.this.f153e.gpusetoptiongl(0, InputList.KEYCODE_BUTTON_5, 0);
                            ePSXe.this.emu_opengl_options &= -449;
                        } else {
                            ePSXe.this.f153e.gpusetoptiongl(1, val2 << 6, 0);
                            ePSXe.this.emu_opengl_options = (ePSXe.this.emu_opengl_options & (-449)) | (val2 << 6);
                        }
                        ePSXe.this.updateData(ePSXe.this.gladapter, ePSXe.this.gllist, position, "Framebuffer Texture=" + ((ePSXe.this.emu_opengl_options & 448) == 0 ? 0 : 1));
                        break;
                    case 6:
                        if ((ePSXe.this.emu_opengl_options & 8192) != 0) {
                            ePSXe.this.f153e.gpusetoptiongl(0, 8192, 0);
                        } else {
                            ePSXe.this.f153e.gpusetoptiongl(1, 8192, 0);
                        }
                        ePSXe.this.emu_opengl_options ^= 8192;
                        ePSXe.this.updateData(ePSXe.this.gladapter, ePSXe.this.gllist, position, "Mask Bit=" + ((ePSXe.this.emu_opengl_options & 8192) == 0 ? 0 : 1));
                        break;
                    case 7:
                        ePSXe.this.f153e.gpusaveoptiongl(ePSXe.this.emu_opengl_options);
                        Toast.makeText(mCont, R.string.viewglext_restore, 0).show();
                        break;
                    case 8:
                        DialogUtil.closeDialog(GLToolBarAlert);
                        break;
                }
            }
        });
    }

    private void alertdialog_gltoolbar2(final Context mCont) {
        this.emu_opengl_options2 = this.f153e.gpugetoptiongl();
        String[] items = new String[9];
        items[0] = "Frameskip=" + ((this.emu_opengl_options2 & 1) != 0 ? 1 : 0);
        items[1] = "Filter=" + ((this.emu_opengl_options2 & 14) != 0 ? (this.emu_opengl_options2 >> 1) & 7 : 0);
        items[2] = "Mdec Filter=" + ((this.emu_opengl_options2 & 16) != 0 ? 1 : 0);
        items[3] = "Offscreen Drawing=" + ((this.emu_opengl_options2 & 96) != 0 ? (this.emu_opengl_options2 >> 5) & 3 : 0);
        items[4] = "Framebuffer Texture=" + ((this.emu_opengl_options2 & 384) != 0 ? (this.emu_opengl_options2 >> 7) & 3 : 0);
        items[5] = "Framebuffer Upload=" + ((this.emu_opengl_options2 & 1536) != 0 ? (this.emu_opengl_options2 >> 9) & 3 : 0);
        items[6] = "Mask Bit=" + ((this.emu_opengl_options2 & 2048) == 0 ? 0 : 1);
        items[7] = "Save";
        items[8] = "Exit";
        ListView gListView = new ListView(mCont);
        if (this.gladapter2 == null) {
            this.gllist2.addAll(Arrays.asList(items));
            this.gladapter2 = new ArrayAdapter<>(mCont, android.R.layout.simple_list_item_1, this.gllist2);
        }
        gListView.setAdapter((ListAdapter) this.gladapter2);
        AlertDialog.Builder builder = new AlertDialog.Builder(mCont);
        builder.setView(gListView);
        final AlertDialog GLToolBarAlert = builder.create();
        GLToolBarAlert.show();
        gListView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.epsxe.ePSXe.ePSXe.14
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> listView, View itemView, int position, long itemId) {
                switch (position) {
                    case 0:
                        if ((ePSXe.this.emu_opengl_options2 & 1) != 0) {
                            ePSXe.this.f153e.gpusetoptiongl(0, 1, 0);
                        } else {
                            ePSXe.this.f153e.gpusetoptiongl(1, 1, 0);
                        }
                        ePSXe.this.emu_opengl_options2 ^= 1;
                        ePSXe.this.updateData(ePSXe.this.gladapter2, ePSXe.this.gllist2, position, "Frameskip=" + ((ePSXe.this.emu_opengl_options2 & 1) == 0 ? 0 : 1));
                        break;
                    case 1:
                        int val = (((ePSXe.this.emu_opengl_options2 >> 1) & 7) + 1) & 7;
                        if (val > 6) {
                            val = 0;
                        }
                        ePSXe.this.emu_opengl_options2 = (ePSXe.this.emu_opengl_options2 & (-15)) | (val << 1);
                        if (val != 0) {
                            ePSXe.this.f153e.gpusetoptiongl2(1, val << 1, 0);
                        } else {
                            ePSXe.this.f153e.gpusetoptiongl2(0, 14, 0);
                        }
                        ePSXe.this.updateData(ePSXe.this.gladapter2, ePSXe.this.gllist2, position, "Filter=" + ((ePSXe.this.emu_opengl_options2 & 14) != 0 ? (ePSXe.this.emu_opengl_options2 >> 1) & 7 : 0));
                        break;
                    case 2:
                        ePSXe.this.emu_opengl_options2 ^= 16;
                        if ((ePSXe.this.emu_opengl_options2 & 16) == 16) {
                            ePSXe.this.f153e.gpusetoptiongl2(1, 16, 0);
                        } else {
                            ePSXe.this.f153e.gpusetoptiongl2(0, 16, 0);
                        }
                        ePSXe.this.updateData(ePSXe.this.gladapter2, ePSXe.this.gllist2, position, "Mdec Filter=" + ((ePSXe.this.emu_opengl_options2 & 16) == 0 ? 0 : 1));
                        break;
                    case 3:
                        int val2 = (((ePSXe.this.emu_opengl_options2 >> 5) & 3) + 1) & 3;
                        if (val2 > 2) {
                            val2 = 0;
                        }
                        ePSXe.this.emu_opengl_options2 = (ePSXe.this.emu_opengl_options2 & (-97)) | (val2 << 5);
                        if (val2 != 0) {
                            ePSXe.this.f153e.gpusetoptiongl2(0, val2 << 5, 0);
                        } else {
                            ePSXe.this.f153e.gpusetoptiongl2(1, 96, 0);
                        }
                        ePSXe.this.updateData(ePSXe.this.gladapter2, ePSXe.this.gllist2, position, "Offscreen Drawing=" + ((ePSXe.this.emu_opengl_options2 & 96) != 0 ? (ePSXe.this.emu_opengl_options2 >> 5) & 3 : 0));
                        break;
                    case 4:
                        int val3 = (((ePSXe.this.emu_opengl_options2 >> 7) & 3) + 1) & 3;
                        ePSXe.this.emu_opengl_options2 = (ePSXe.this.emu_opengl_options2 & (-385)) | (val3 << 7);
                        if (val3 != 0) {
                            ePSXe.this.f153e.gpusetoptiongl2(0, val3 << 7, 0);
                        } else {
                            ePSXe.this.f153e.gpusetoptiongl2(1, 384, 0);
                        }
                        ePSXe.this.updateData(ePSXe.this.gladapter2, ePSXe.this.gllist2, position, "Framebuffer Texture=" + ((ePSXe.this.emu_opengl_options2 & 384) != 0 ? (ePSXe.this.emu_opengl_options2 >> 7) & 3 : 0));
                        break;
                    case 5:
                        int val4 = ((ePSXe.this.emu_opengl_options2 >> 9) & 3) + 1;
                        if (val4 > 2) {
                            val4 = 0;
                        }
                        ePSXe.this.emu_opengl_options2 = (ePSXe.this.emu_opengl_options2 & (-1537)) | (val4 << 9);
                        if (val4 != 0) {
                            ePSXe.this.f153e.gpusetoptiongl2(0, val4 << 9, 0);
                        } else {
                            ePSXe.this.f153e.gpusetoptiongl2(1, 1536, 0);
                        }
                        ePSXe.this.updateData(ePSXe.this.gladapter2, ePSXe.this.gllist2, position, "Framebuffer Upload=" + ((ePSXe.this.emu_opengl_options2 & 1536) != 0 ? (ePSXe.this.emu_opengl_options2 >> 9) & 3 : 0));
                        break;
                    case 6:
                        if ((ePSXe.this.emu_opengl_options2 & 2048) != 0) {
                            ePSXe.this.f153e.gpusetoptiongl(0, 2048, 0);
                        } else {
                            ePSXe.this.f153e.gpusetoptiongl(1, 2048, 0);
                        }
                        ePSXe.this.emu_opengl_options2 ^= 2048;
                        ePSXe.this.updateData(ePSXe.this.gladapter2, ePSXe.this.gllist2, position, "Mask Bit=Mask Bit=" + ((ePSXe.this.emu_opengl_options2 & 2048) == 0 ? 0 : 1));
                        break;
                    case 7:
                        ePSXe.this.f153e.gpusaveoptiongl2(ePSXe.this.emu_opengl_options2);
                        Toast.makeText(mCont, R.string.viewglext_restore, 0).show();
                        break;
                    case 8:
                        DialogUtil.closeDialog(GLToolBarAlert);
                        break;
                }
            }
        });
    }

    private int string2int(String val, int def) {
        try {
            int def2 = Integer.parseInt(val);
            return def2;
        } catch (Exception e) {
            return def;
        }
    }

    private void saveVideoGameprefs(String code) {
        try {
            Properties props = new Properties();
            OutputStream output = new FileOutputStream(ContextCompat.getDataDir(this).getAbsolutePath() + "/epsxe/config/" + code + ".video.txt");
            props.setProperty("videoRenderer", "" + this.emu_renderer);
            props.setProperty("cpuFrameskip", "" + this.emu_menu2_frameskip);
            props.setProperty("videoScreenOrientation", "" + this.emu_screen_orientation);
            props.setProperty("videoScreenRatio", "" + this.emu_menu2_screenratio);
            props.setProperty("videoSubPixelPrecision", "" + this.emu_menu2_subpixelprecision);
            props.setProperty("videoBlackBands", "" + this.emu_menu2_blackbands);
            props.setProperty("videoDither", "" + this.emu_menu2_dither);
            props.setProperty("videoBrightnessProfile", "" + this.emu_menu2_brightnessprofile);
            props.setProperty("videoScanlinesTrans", "" + this.emu_menu2_scanlinestrans);
            props.setProperty("videoScanlinesThick", "" + this.emu_menu2_scanlinesthick);
            props.setProperty("videoIResolution", "" + this.emu_menu2_iresolution);
            props.setProperty("videoMtModeS", "" + this.emu_menu2_gpumtmodeS);
            props.setProperty("videoDepth", "" + this.emu_menu2_depth);
            props.setProperty("videoMtModeH", "" + this.emu_menu2_gpumtmodeH);
            props.setProperty("videoFilter", "" + this.emu_menu2_videofilter);
            props.setProperty("videoOverscanTop", "" + this.emu_menu2_overscantop);
            props.setProperty("videoOverscanBottom", "" + this.emu_menu2_overscanbottom);
            props.setProperty("videoIResolutionPlugin", "" + this.emu_menu2_iresolution_plugin);
            props.setProperty("videoTexmodePlugin", "" + this.emu_menu2_texmode_plugin);
            props.setProperty("video2dFilter", "" + this.emu_menu2_gpu2dfilter);
            props.setProperty("videoDmaChainCore", "" + this.emu_menu2_dmachaincore);
            props.setProperty("videoFboRes", "" + this.emu_ogl2_res);
            props.setProperty("videoFbo", "" + this.emu_ogl2_fbo);
            props.store(output, (String) null);
        } catch (Exception e) {
        }
    }

    private boolean loadVideoGameprefs(String code) {
        try {
            Properties props = new Properties();
            InputStream inputStream = new FileInputStream(ContextCompat.getDataDir(this).getAbsolutePath() + "/epsxe/config/" + code + ".video.txt");
            props.load(inputStream);
            this.emu_renderer = string2int(props.getProperty("videoRenderer"), this.emu_renderer);
            this.emu_menu2_frameskip = string2int(props.getProperty("cpuFrameskip"), this.emu_menu2_frameskip);
            this.emu_screen_orientation = string2int(props.getProperty("videoScreenOrientation"), this.emu_screen_orientation);
            this.emu_menu2_screenratio = string2int(props.getProperty("videoScreenRatio"), this.emu_menu2_screenratio);
            this.emu_menu2_subpixelprecision = string2int(props.getProperty("videoSubPixelPrecision"), this.emu_menu2_subpixelprecision);
            this.emu_menu2_blackbands = string2int(props.getProperty("videoBlackBands"), this.emu_menu2_blackbands);
            this.emu_menu2_dither = string2int(props.getProperty("videoDither"), this.emu_menu2_dither);
            this.emu_menu2_brightnessprofile = string2int(props.getProperty("videoBrightnessProfile"), this.emu_menu2_brightnessprofile);
            this.emu_menu2_scanlinestrans = string2int(props.getProperty("videoScanlinesTrans"), this.emu_menu2_scanlinestrans);
            this.emu_menu2_scanlinesthick = string2int(props.getProperty("videoScanlinesThick"), this.emu_menu2_scanlinesthick);
            this.emu_menu2_iresolution = string2int(props.getProperty("videoIResolution"), this.emu_menu2_iresolution);
            this.emu_menu2_gpumtmodeS = string2int(props.getProperty("videoMtModeS"), this.emu_menu2_gpumtmodeS);
            this.emu_menu2_depth = string2int(props.getProperty("videoDepth"), this.emu_menu2_depth);
            this.emu_menu2_gpumtmodeH = string2int(props.getProperty("videoMtModeH"), this.emu_menu2_gpumtmodeH);
            this.emu_menu2_videofilter = string2int(props.getProperty("videoFilter"), this.emu_menu2_videofilter);
            this.emu_menu2_overscantop = string2int(props.getProperty("videoOverscanTop"), this.emu_menu2_overscantop);
            this.emu_menu2_overscanbottom = string2int(props.getProperty("videoOverscanBottom"), this.emu_menu2_overscanbottom);
            this.emu_menu2_iresolution_plugin = string2int(props.getProperty("videoIResolutionPlugin"), this.emu_menu2_iresolution_plugin);
            this.emu_menu2_texmode_plugin = string2int(props.getProperty("videoTexmodePlugin"), this.emu_menu2_texmode_plugin);
            this.emu_menu2_gpu2dfilter = string2int(props.getProperty("video2dFilter"), this.emu_menu2_gpu2dfilter);
            this.emu_menu2_dmachaincore = string2int(props.getProperty("videoDmaChainCore"), this.emu_menu2_dmachaincore);
            this.emu_ogl2_res = string2int(props.getProperty("videoFboRes"), this.emu_ogl2_res);
            this.emu_ogl2_fbo = string2int(props.getProperty("videoFbo"), this.emu_ogl2_fbo);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void saveInputGameprefs(String code) {
        try {
            Properties props = new Properties();
            OutputStream output = new FileOutputStream(ContextCompat.getDataDir(this).getAbsolutePath() + "/epsxe/config/" + code + ".input.txt");
            props.setProperty("inputType", "" + this.emu_menu2_input1type);
            props.setProperty("inputAnalogType", "" + this.emu_menu2_input1mode);
            props.setProperty("inputProfile", "" + this.emu_menu2_inputprofile);
            props.setProperty("inputAutofireEnabled", "" + this.emu_menu2_autofireenabled);
            props.setProperty("inputAutofireFreq", "" + this.emu_menu2_autofirefreq);
            props.setProperty("inputAutofireButton", "" + this.emu_menu2_autofirebutton);
            props.setProperty("inputDynamicPad", "" + this.emu_menu2_dynamicpad);
            props.setProperty("inputDynamicAction", "" + this.emu_menu2_dynamicaction);
            for (int i = 0; i < this.keycodesextra.length; i++) {
                int v = this.keycodes[0][i];
                props.setProperty("inputP1But" + i, "" + v);
            }
            for (int i2 = 0; i2 < this.keycodesextra.length; i2++) {
                int v2 = this.keycodesextra[i2];
                props.setProperty("inputP1ButExtra" + i2, "" + v2);
            }
            props.store(output, (String) null);
        } catch (Exception e) {
        }
    }

    private boolean loadInputGameprefs(String code) {
        try {
            Properties props = new Properties();
            InputStream inputStream = new FileInputStream(ContextCompat.getDataDir(this).getAbsolutePath() + "/epsxe/config/" + code + ".input.txt");
            props.load(inputStream);
            this.emu_menu2_input1type = string2int(props.getProperty("inputType"), this.emu_menu2_input1type);
            this.emu_menu2_input1mode = string2int(props.getProperty("inputAnalogType"), this.emu_menu2_input1mode);
            this.emu_menu2_inputprofile = string2int(props.getProperty("inputProfile"), this.emu_menu2_inputprofile);
            this.emu_menu2_autofireenabled = string2int(props.getProperty("inputAutofireEnabled"), this.emu_menu2_autofireenabled);
            this.emu_menu2_autofirefreq = string2int(props.getProperty("inputAutofireFreq"), this.emu_menu2_autofirefreq);
            this.emu_menu2_autofirebutton = string2int(props.getProperty("inputAutofireButton"), this.emu_menu2_autofirebutton);
            this.emu_menu2_dynamicpad = string2int(props.getProperty("inputDynamicPad"), this.emu_menu2_dynamicpad);
            this.emu_menu2_dynamicaction = string2int(props.getProperty("inputDynamicAction"), this.emu_menu2_dynamicaction);
            if (this.emu_menu2_input1mode > 1) {
                this.emu_menu2_input1mode = 0;
            }
            if (this.emu_menu2_input2mode > 1) {
                this.emu_menu2_input2mode = 0;
            }
            this.mePSXeView.setinputpadmode(this.emu_menu2_input1type, this.emu_menu2_input2type, this.emu_menu2_input1mode, this.emu_menu2_input2mode);
            this.mePSXeView.setinputprofile(this.emu_menu2_inputprofile);
            for (int i = 0; i < this.keycodesextra.length; i++) {
                this.keycodes[0][i] = string2int(props.getProperty("inputP1But" + i), this.keycodes[0][i]);
            }
            for (int i2 = 0; i2 < this.keycodesextra.length; i2++) {
                this.keycodesextra[i2] = string2int(props.getProperty("inputP1ButExtra" + i2), this.keycodesextra[i2]);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void saveSoundGameprefs(String code) {
        try {
            Properties props = new Properties();
            OutputStream output = new FileOutputStream(ContextCompat.getDataDir(this).getAbsolutePath() + "/epsxe/config/" + code + ".sound.txt");
            props.setProperty("soundQA", "" + this.emu_menu2_soundqa);
            props.setProperty("soundLatency", "" + this.emu_menu2_soundlatency);
            props.store(output, (String) null);
        } catch (Exception e) {
        }
    }

    private boolean loadSoundGameprefs(String code) {
        try {
            Properties props = new Properties();
            InputStream inputStream = new FileInputStream(ContextCompat.getDataDir(this).getAbsolutePath() + "/epsxe/config/" + code + ".sound.txt");
            props.load(inputStream);
            this.emu_menu2_soundqa = string2int(props.getProperty("soundQA"), this.emu_menu2_soundqa);
            this.emu_menu2_soundlatency = string2int(props.getProperty("soundLatency"), this.emu_menu2_soundlatency);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean loadGameProfile(String code) {
        loadInputGameprefs(code);
        loadVideoGameprefs(code);
        boolean loaded = loadSoundGameprefs(code);
        return loaded;
    }

    public void saveGameProfile(String code) {
        saveInputGameprefs(code);
        saveVideoGameprefs(code);
        saveSoundGameprefs(code);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void clearGameProfile(String code) {
        try {
            File tmpi = new File(ContextCompat.getDataDir(this).getAbsolutePath() + "/epsxe/config/" + code + ".input.txt");
            if (tmpi.exists()) {
                tmpi.delete();
            }
            File tmps = new File(ContextCompat.getDataDir(this).getAbsolutePath() + "/epsxe/config/" + code + ".sound.txt");
            if (tmps.exists()) {
                tmps.delete();
            }
            File tmpv = new File(ContextCompat.getDataDir(this).getAbsolutePath() + "/epsxe/config/" + code + ".video.txt");
            if (tmpv.exists()) {
                tmpv.delete();
            }
        } catch (Exception e) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void alertdialog_menuvideo(final Context mCont) {
        String str;
        String str2;
        String str3;
        String str4;
        String str5;
        String str6;
        String str7;
        String[] items = new String[9];
        items[0] = getString(R.string.menuvideo_frameskip) + "=" + (this.emu_menu2_frameskip == 1 ? "Enabled" : "Disabled");
        items[1] = getString(R.string.menuvideo_showfps) + "=" + (this.emu_menu2_showfps == 1 ? "Enabled" : "Disabled");
        StringBuilder append = new StringBuilder().append(getString(R.string.menuvideo_screenratio)).append("=");
        if (this.emu_menu2_screenratio != 0) {
            str = this.emu_menu2_screenratio == 2 ? "Widescreen" : "4:3";
        } else {
            str = "Stretch";
        }
        items[2] = append.append(str).toString();
        StringBuilder append2 = new StringBuilder().append(getString(R.string.menuvideo_subpixelprecision)).append("=");
        if (this.emu_renderer != 0) {
            str2 = this.emu_menu2_subpixelprecision == 1 ? "Enabled" : "Disabled";
        } else {
            str2 = "N/A";
        }
        items[3] = append2.append(str2).toString();
        StringBuilder append3 = new StringBuilder().append(getString(R.string.menuvideo_blackbands)).append("=");
        if (this.emu_renderer == 2 || this.emu_renderer == 4 || this.emu_renderer == 5) {
            str3 = "N/A";
        } else {
            str3 = this.emu_menu2_blackbands == 1 ? "Remove bands" : "Real PSX";
        }
        items[4] = append3.append(str3).toString();
        StringBuilder append4 = new StringBuilder().append(getString(R.string.menuvideo_dither)).append("=");
        if (this.emu_renderer == 2 || this.emu_renderer == 4 || this.emu_renderer == 5) {
            str4 = "N/A";
        } else if (this.emu_menu2_dither != 0) {
            str4 = (this.emu_menu2_dither == 1 || this.emu_menu2_dither == 2) ? "Enabled" : "Always Enabled";
        } else {
            str4 = "Disabled";
        }
        items[5] = append4.append(str4).toString();
        StringBuilder append5 = new StringBuilder().append(getString(R.string.menuvideo_brightnessprofile)).append("=");
        if (this.emu_renderer == 2 || this.emu_renderer == 4 || this.emu_renderer == 5) {
            str5 = "N/A";
        } else if (this.emu_menu2_brightnessprofile == 0) {
            str5 = "Default";
        } else if (this.emu_menu2_brightnessprofile != 1) {
            str5 = this.emu_menu2_brightnessprofile != 2 ? "high" : "medium";
        } else {
            str5 = "low";
        }
        items[6] = append5.append(str5).toString();
        StringBuilder append6 = new StringBuilder().append(getString(R.string.menuvideo_scanlinestrans)).append("=");
        if (this.emu_menu2_scanlinestrans == 255) {
            str6 = "Disabled";
        } else {
            str6 = this.emu_menu2_scanlinestrans >= 204 ? "80%" : this.emu_menu2_scanlinestrans >= 153 ? "60%" : this.emu_menu2_scanlinestrans >= 102 ? "40%" : this.emu_menu2_scanlinestrans >= 51 ? "20%" : "Full Black";
        }
        items[7] = append6.append(str6).toString();
        StringBuilder append7 = new StringBuilder().append(getString(R.string.menuvideo_scanlinesthick)).append("=");
        if (this.emu_menu2_scanlinesthick == 0) {
            str7 = "Default";
        } else {
            str7 = this.emu_menu2_scanlinesthick == 1 ? "1-line" : this.emu_menu2_scanlinesthick == 2 ? "2-lines" : this.emu_menu2_scanlinesthick == 3 ? "3-lines" : "4-lines";
        }
        items[8] = append7.append(str7).toString();
        ListView gListView = new ListView(mCont);
        if (this.menuvideoadapter == null) {
            this.menuvideolist.addAll(Arrays.asList(items));
            this.menuvideoadapter = new ArrayAdapter<>(mCont, android.R.layout.simple_list_item_1, this.menuvideolist);
        }
        gListView.setAdapter((ListAdapter) this.menuvideoadapter);
        gListView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.epsxe.ePSXe.ePSXe.15
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> listView, View itemView, int position, long itemId) {
                String str8;
                String str9;
                String str10;
                String str11;
                String str12;
                String str13;
                String str14;
                switch (position) {
                    case 0:
                        if (ePSXe.this.emuStatus == 1) {
                            ePSXe.this.emu_menu2_frameskip ^= 1;
                            ePSXe.this.mePSXeView.setframeskip(ePSXe.this.emu_menu2_frameskip);
                            ePSXe.this.updateData(ePSXe.this.menuvideoadapter, ePSXe.this.menuvideolist, position, ePSXe.this.getString(R.string.menuvideo_frameskip) + "=" + (ePSXe.this.emu_menu2_frameskip == 1 ? "Enabled" : "Disabled"));
                            break;
                        }
                        break;
                    case 1:
                        if (ePSXe.this.emuStatus == 1) {
                            ePSXe.this.emu_menu2_showfps ^= 1;
                            ePSXe.this.mePSXeView.setshowfps(ePSXe.this.emu_menu2_showfps);
                            ePSXe.this.updateData(ePSXe.this.menuvideoadapter, ePSXe.this.menuvideolist, position, ePSXe.this.getString(R.string.menuvideo_showfps) + "=" + (ePSXe.this.emu_menu2_showfps == 1 ? "Enabled" : "Disabled"));
                            break;
                        }
                        break;
                    case 2:
                        if (ePSXe.this.emuStatus == 1) {
                            if (ePSXe.this.emu_menu2_screenratio == 0) {
                                ePSXe.this.emu_menu2_screenratio = 1;
                            } else if (ePSXe.this.emu_menu2_screenratio == 1) {
                                ePSXe.this.emu_menu2_screenratio = 2;
                            } else {
                                ePSXe.this.emu_menu2_screenratio = 0;
                            }
                            if (ePSXe.this.emu_menu2_screenratio == 2) {
                                ePSXe.this.f153e.setWidescreen(1);
                                ePSXe.this.mePSXeView.updatescreenratio(0);
                            } else {
                                ePSXe.this.f153e.setWidescreen(0);
                                ePSXe.this.mePSXeView.updatescreenratio(ePSXe.this.emu_menu2_screenratio);
                            }
                            ePSXe epsxe = ePSXe.this;
                            ArrayAdapter<String> arrayAdapter = ePSXe.this.menuvideoadapter;
                            ArrayList<String> arrayList = ePSXe.this.menuvideolist;
                            StringBuilder append8 = new StringBuilder().append(ePSXe.this.getString(R.string.menuvideo_screenratio)).append("=");
                            if (ePSXe.this.emu_menu2_screenratio != 0) {
                                str14 = ePSXe.this.emu_menu2_screenratio == 2 ? "Widescreen" : "4:3";
                            } else {
                                str14 = "Stretch";
                            }
                            epsxe.updateData(arrayAdapter, arrayList, position, append8.append(str14).toString());
                            break;
                        }
                        break;
                    case 3:
                        if (ePSXe.this.emuStatus == 1) {
                            if (ePSXe.this.emu_renderer == 3 || ePSXe.this.emu_renderer == 1) {
                                ePSXe.this.emu_menu2_subpixelprecision ^= 1;
                            }
                            ePSXe.this.f153e.updategteaccuracy(ePSXe.this.emu_menu2_subpixelprecision);
                            ePSXe epsxe2 = ePSXe.this;
                            ArrayAdapter<String> arrayAdapter2 = ePSXe.this.menuvideoadapter;
                            ArrayList<String> arrayList2 = ePSXe.this.menuvideolist;
                            StringBuilder append9 = new StringBuilder().append(ePSXe.this.getString(R.string.menuvideo_subpixelprecision)).append("=");
                            if (ePSXe.this.emu_renderer != 0) {
                                str13 = ePSXe.this.emu_menu2_subpixelprecision == 1 ? "Enabled" : "Disabled";
                            } else {
                                str13 = "N/A";
                            }
                            epsxe2.updateData(arrayAdapter2, arrayList2, position, append9.append(str13).toString());
                            break;
                        }
                        break;
                    case 4:
                        if (ePSXe.this.emuStatus == 1) {
                            ePSXe.this.emu_menu2_blackbands ^= 1;
                            ePSXe.this.mePSXeView.setscreenblackbands(ePSXe.this.emu_menu2_blackbands);
                            ePSXe epsxe3 = ePSXe.this;
                            ArrayAdapter<String> arrayAdapter3 = ePSXe.this.menuvideoadapter;
                            ArrayList<String> arrayList3 = ePSXe.this.menuvideolist;
                            StringBuilder append10 = new StringBuilder().append(ePSXe.this.getString(R.string.menuvideo_blackbands)).append("=");
                            if (ePSXe.this.emu_renderer == 2 || ePSXe.this.emu_renderer == 4 || ePSXe.this.emu_renderer == 5) {
                                str12 = "N/A";
                            } else {
                                str12 = ePSXe.this.emu_menu2_blackbands == 1 ? "Remove bands" : "Real PSX";
                            }
                            epsxe3.updateData(arrayAdapter3, arrayList3, position, append10.append(str12).toString());
                            break;
                        }
                        break;
                    case 5:
                        if (ePSXe.this.emuStatus == 1) {
                            ePSXe.access$3108(ePSXe.this);
                            if (ePSXe.this.emu_menu2_dither > 3) {
                                ePSXe.this.emu_menu2_dither = 0;
                            }
                            if (ePSXe.this.emu_menu2_dither == 2 && ePSXe.this.emu_enable_neon == 0) {
                                ePSXe.this.emu_menu2_dither = 0;
                            }
                            ePSXe.this.mePSXeView.setvideodither(ePSXe.this.emu_menu2_dither);
                            ePSXe epsxe4 = ePSXe.this;
                            ArrayAdapter<String> arrayAdapter4 = ePSXe.this.menuvideoadapter;
                            ArrayList<String> arrayList4 = ePSXe.this.menuvideolist;
                            StringBuilder append11 = new StringBuilder().append(ePSXe.this.getString(R.string.menuvideo_dither)).append("=");
                            if (ePSXe.this.emu_renderer == 2 || ePSXe.this.emu_renderer == 4 || ePSXe.this.emu_renderer == 5) {
                                str11 = "N/A";
                            } else if (ePSXe.this.emu_menu2_dither != 0) {
                                str11 = (ePSXe.this.emu_menu2_dither == 1 || ePSXe.this.emu_menu2_dither == 2) ? "Enabled" : "Always Enabled";
                            } else {
                                str11 = "Disabled";
                            }
                            epsxe4.updateData(arrayAdapter4, arrayList4, position, append11.append(str11).toString());
                            break;
                        }
                        break;
                    case 6:
                        if (ePSXe.this.emuStatus == 1) {
                            ePSXe.access$3308(ePSXe.this);
                            if (ePSXe.this.emu_menu2_brightnessprofile > 3) {
                                ePSXe.this.emu_menu2_brightnessprofile = 0;
                            }
                            ePSXe.this.f153e.setgpubrightnessprofile(ePSXe.this.emu_menu2_brightnessprofile);
                            ePSXe epsxe5 = ePSXe.this;
                            ArrayAdapter<String> arrayAdapter5 = ePSXe.this.menuvideoadapter;
                            ArrayList<String> arrayList5 = ePSXe.this.menuvideolist;
                            StringBuilder append12 = new StringBuilder().append(ePSXe.this.getString(R.string.menuvideo_brightnessprofile)).append("=");
                            if (ePSXe.this.emu_renderer == 2 || ePSXe.this.emu_renderer == 4 || ePSXe.this.emu_renderer == 5) {
                                str10 = "N/A";
                            } else if (ePSXe.this.emu_menu2_brightnessprofile == 0) {
                                str10 = "Default";
                            } else if (ePSXe.this.emu_menu2_brightnessprofile != 1) {
                                str10 = ePSXe.this.emu_menu2_brightnessprofile != 2 ? "high" : "medium";
                            } else {
                                str10 = "low";
                            }
                            epsxe5.updateData(arrayAdapter5, arrayList5, position, append12.append(str10).toString());
                            break;
                        }
                        break;
                    case 7:
                        if (ePSXe.this.emuStatus == 1) {
                            if (ePSXe.this.emu_menu2_scanlinestrans == 255) {
                                ePSXe.this.emu_menu2_scanlinestrans = 204;
                            } else if (ePSXe.this.emu_menu2_scanlinestrans >= 204) {
                                ePSXe.this.emu_menu2_scanlinestrans = InputList.KEYCODE_NUMPAD_9;
                            } else if (ePSXe.this.emu_menu2_scanlinestrans >= 153) {
                                ePSXe.this.emu_menu2_scanlinestrans = 102;
                            } else if (ePSXe.this.emu_menu2_scanlinestrans >= 102) {
                                ePSXe.this.emu_menu2_scanlinestrans = 51;
                            } else if (ePSXe.this.emu_menu2_scanlinestrans >= 51) {
                                ePSXe.this.emu_menu2_scanlinestrans = 0;
                            } else if (ePSXe.this.emu_menu2_scanlinestrans >= 0) {
                                ePSXe.this.emu_menu2_scanlinestrans = 255;
                            }
                            if (ePSXe.this.emu_menu2_scanlinestrans == 255) {
                                ePSXe.this.emu_menu2_scanlines = 0;
                            } else {
                                ePSXe.this.emu_menu2_scanlines = 1;
                            }
                            if (ePSXe.this.emu_menu2_scanlinesthick != 0) {
                                ePSXe.this.emu_menu2_scanlinesthickVal = ePSXe.this.emu_menu2_scanlinesthick;
                            } else {
                                DisplayMetrics metrics = ePSXe.this.getResources().getDisplayMetrics();
                                if (metrics.densityDpi < 320) {
                                    ePSXe.this.emu_menu2_scanlinesthickVal = 1;
                                } else if (metrics.densityDpi < 640) {
                                    ePSXe.this.emu_menu2_scanlinesthickVal = 2;
                                } else {
                                    ePSXe.this.emu_menu2_scanlinesthickVal = 3;
                                }
                            }
                            ePSXe.this.f153e.updatescanlines(ePSXe.this.emu_menu2_scanlines, ePSXe.this.emu_menu2_scanlinestrans, ePSXe.this.emu_menu2_scanlinesthickVal);
                            ePSXe epsxe6 = ePSXe.this;
                            ArrayAdapter<String> arrayAdapter6 = ePSXe.this.menuvideoadapter;
                            ArrayList<String> arrayList6 = ePSXe.this.menuvideolist;
                            StringBuilder append13 = new StringBuilder().append(ePSXe.this.getString(R.string.menuvideo_scanlinestrans)).append("=");
                            if (ePSXe.this.emu_menu2_scanlinestrans == 255) {
                                str9 = "Disabled";
                            } else if (ePSXe.this.emu_menu2_scanlinestrans >= 204) {
                                str9 = "80%";
                            } else if (ePSXe.this.emu_menu2_scanlinestrans >= 153) {
                                str9 = "60%";
                            } else if (ePSXe.this.emu_menu2_scanlinestrans >= 102) {
                                str9 = "40%";
                            } else {
                                str9 = ePSXe.this.emu_menu2_scanlinestrans >= 51 ? "20%" : "Full Black";
                            }
                            epsxe6.updateData(arrayAdapter6, arrayList6, position, append13.append(str9).toString());
                            break;
                        }
                        break;
                    case 8:
                        if (ePSXe.this.emuStatus == 1) {
                            ePSXe.access$3608(ePSXe.this);
                            if (ePSXe.this.emu_menu2_scanlinesthick == 5) {
                                ePSXe.this.emu_menu2_scanlinesthick = 0;
                            }
                            if (ePSXe.this.emu_menu2_scanlinestrans == 255) {
                                ePSXe.this.emu_menu2_scanlines = 0;
                            } else {
                                ePSXe.this.emu_menu2_scanlines = 1;
                            }
                            if (ePSXe.this.emu_menu2_scanlinesthick != 0) {
                                ePSXe.this.emu_menu2_scanlinesthickVal = ePSXe.this.emu_menu2_scanlinesthick;
                            } else {
                                DisplayMetrics metrics2 = ePSXe.this.getResources().getDisplayMetrics();
                                if (metrics2.densityDpi < 320) {
                                    ePSXe.this.emu_menu2_scanlinesthickVal = 1;
                                } else if (metrics2.densityDpi < 640) {
                                    ePSXe.this.emu_menu2_scanlinesthickVal = 2;
                                } else {
                                    ePSXe.this.emu_menu2_scanlinesthickVal = 3;
                                }
                            }
                            ePSXe.this.f153e.updatescanlines(ePSXe.this.emu_menu2_scanlines, ePSXe.this.emu_menu2_scanlinestrans, ePSXe.this.emu_menu2_scanlinesthickVal);
                            ePSXe epsxe7 = ePSXe.this;
                            ArrayAdapter<String> arrayAdapter7 = ePSXe.this.menuvideoadapter;
                            ArrayList<String> arrayList7 = ePSXe.this.menuvideolist;
                            StringBuilder append14 = new StringBuilder().append(ePSXe.this.getString(R.string.menuvideo_scanlinesthick)).append("=");
                            if (ePSXe.this.emu_menu2_scanlinesthick == 0) {
                                str8 = "Default";
                            } else if (ePSXe.this.emu_menu2_scanlinesthick == 1) {
                                str8 = "1-line";
                            } else if (ePSXe.this.emu_menu2_scanlinesthick == 2) {
                                str8 = "2-lines";
                            } else {
                                str8 = ePSXe.this.emu_menu2_scanlinesthick == 3 ? "3-lines" : "4-lines";
                            }
                            epsxe7.updateData(arrayAdapter7, arrayList7, position, append14.append(str8).toString());
                            break;
                        }
                        break;
                }
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(mCont).setOnKeyListener(new DialogInterface.OnKeyListener() { // from class: com.epsxe.ePSXe.ePSXe.16
            @Override // android.content.DialogInterface.OnKeyListener
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode != 4 || event.getAction() != 1 || event.isCanceled()) {
                    return false;
                }
                ePSXe.this.alertdialog_menu2(mCont);
                DialogUtil.closeDialog(ePSXe.this.menuvideoAlert);
                return true;
            }
        });
        builder.setView(gListView);
        this.menuvideoAlert = builder.create();
        this.menuvideoAlert.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void alertdialog_menuaudio(final Context mCont) {
        String str;
        String str2;
        String[] items = new String[2];
        StringBuilder append = new StringBuilder().append(getString(R.string.menuaudio_soundlatency)).append("=");
        if (this.emu_menu2_soundlatency == 0) {
            str = "Normal";
        } else if (this.emu_menu2_soundlatency == 2) {
            str = "Low";
        } else {
            str = this.emu_menu2_soundlatency == 3 ? "Very Low" : "Lowest Experimental";
        }
        items[0] = append.append(str).toString();
        StringBuilder append2 = new StringBuilder().append(getString(R.string.menuaudio_soundqa)).append("=");
        if (this.emu_menu2_soundqa != 0) {
            str2 = this.emu_menu2_soundqa == 2 ? "Full Effects" : "Less Effects";
        } else {
            str2 = "No Sound";
        }
        items[1] = append2.append(str2).toString();
        ListView gListView = new ListView(mCont);
        if (this.menuaudioadapter == null) {
            this.menuaudiolist.addAll(Arrays.asList(items));
            this.menuaudioadapter = new ArrayAdapter<>(mCont, android.R.layout.simple_list_item_1, this.menuaudiolist);
        }
        gListView.setAdapter((ListAdapter) this.menuaudioadapter);
        gListView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.epsxe.ePSXe.ePSXe.17
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> listView, View itemView, int position, long itemId) {
                String str3;
                String str4;
                switch (position) {
                    case 0:
                        if (ePSXe.this.emuStatus == 1) {
                            if (ePSXe.this.emu_menu2_soundlatency == 0) {
                                ePSXe.this.emu_menu2_soundlatency = 2;
                            } else if (ePSXe.this.emu_menu2_soundlatency == 2) {
                                ePSXe.this.emu_menu2_soundlatency = 3;
                            } else {
                                ePSXe.this.emu_menu2_soundlatency = 0;
                            }
                            ePSXe.this.mePSXeSound.setsoundlatency(ePSXe.this.emu_menu2_soundlatency);
                            ePSXe epsxe = ePSXe.this;
                            ArrayAdapter<String> arrayAdapter = ePSXe.this.menuaudioadapter;
                            ArrayList<String> arrayList = ePSXe.this.menuaudiolist;
                            StringBuilder append3 = new StringBuilder().append(ePSXe.this.getString(R.string.menuaudio_soundlatency)).append("=");
                            if (ePSXe.this.emu_menu2_soundlatency == 0) {
                                str4 = "Normal";
                            } else if (ePSXe.this.emu_menu2_soundlatency == 2) {
                                str4 = "Low";
                            } else {
                                str4 = ePSXe.this.emu_menu2_soundlatency == 3 ? "Very Low" : "Lowest Experimental";
                            }
                            epsxe.updateData(arrayAdapter, arrayList, position, append3.append(str4).toString());
                            break;
                        }
                        break;
                    case 1:
                        if (ePSXe.this.emuStatus == 1) {
                            if (ePSXe.this.emu_menu2_soundqa == 0) {
                                ePSXe.this.emu_menu2_soundqa = 1;
                            } else if (ePSXe.this.emu_menu2_soundqa == 1) {
                                ePSXe.this.emu_menu2_soundqa = 2;
                            } else {
                                ePSXe.this.emu_menu2_soundqa = 0;
                            }
                            ePSXe.this.mePSXeSound.setsoundqa(ePSXe.this.emu_menu2_soundqa);
                            ePSXe epsxe2 = ePSXe.this;
                            ArrayAdapter<String> arrayAdapter2 = ePSXe.this.menuaudioadapter;
                            ArrayList<String> arrayList2 = ePSXe.this.menuaudiolist;
                            StringBuilder append4 = new StringBuilder().append(ePSXe.this.getString(R.string.menuaudio_soundqa)).append("=");
                            if (ePSXe.this.emu_menu2_soundqa != 0) {
                                str3 = ePSXe.this.emu_menu2_soundqa == 2 ? "Full Effects" : "Less Effects";
                            } else {
                                str3 = "No Sound";
                            }
                            epsxe2.updateData(arrayAdapter2, arrayList2, position, append4.append(str3).toString());
                            break;
                        }
                        break;
                }
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(mCont).setOnKeyListener(new DialogInterface.OnKeyListener() { // from class: com.epsxe.ePSXe.ePSXe.18
            @Override // android.content.DialogInterface.OnKeyListener
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode != 4 || event.getAction() != 1 || event.isCanceled()) {
                    return false;
                }
                ePSXe.this.alertdialog_menu2(mCont);
                DialogUtil.closeDialog(ePSXe.this.menuaudioAlert);
                return true;
            }
        });
        builder.setView(gListView);
        this.menuaudioAlert = builder.create();
        this.menuaudioAlert.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void alertdialog_menuinput(final Context mCont) {
        String str;
        String str2;
        String[] items = new String[6];
        StringBuilder append = new StringBuilder().append(getString(R.string.menuinput_input1mode)).append("=");
        if (this.emu_menu2_input1type == 4) {
            str = this.emu_menu2_input1mode == 0 ? "Digital" : "Analog";
        } else {
            str = "Only Digital";
        }
        items[0] = append.append(str).toString();
        StringBuilder append2 = new StringBuilder().append(getString(R.string.menuinput_input2mode)).append("=");
        if (this.emu_menu2_input2type == 4) {
            str2 = this.emu_menu2_input2mode == 0 ? "Digital" : "Analog";
        } else {
            str2 = "Only Digital";
        }
        items[1] = append2.append(str2).toString();
        items[2] = getString(R.string.menuinput_touchscreen) + "=" + (this.emu_menu2_touchscreen == 0 ? "Show" : "Hidden");
        items[3] = getString(R.string.menuinput_screenvibrate) + "=" + (this.emu_menu2_screenvibrate == 0 ? "Disabled" : "Enabled");
        items[4] = getString(R.string.menuinput_autofire) + "=" + (this.emu_menu2_autofireenabled == 0 ? "Disabled" : "Enabled");
        items[5] = getString(R.string.menuinput_profile) + "=" + (this.emu_menu2_inputprofile == 0 ? "default" : Integer.valueOf(this.emu_menu2_inputprofile + 1));
        ListView gListView = new ListView(mCont);
        if (this.menuinputadapter == null) {
            this.menuinputlist.addAll(Arrays.asList(items));
            this.menuinputadapter = new ArrayAdapter<>(mCont, android.R.layout.simple_list_item_1, this.menuinputlist);
        }
        gListView.setAdapter((ListAdapter) this.menuinputadapter);
        gListView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.epsxe.ePSXe.ePSXe.19
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> listView, View itemView, int position, long itemId) {
                String str3;
                String str4;
                switch (position) {
                    case 0:
                        if (ePSXe.this.emuStatus == 1 && ePSXe.this.emu_menu2_input1type == 4) {
                            ePSXe.this.emu_menu2_input1mode ^= 1;
                            ePSXe.this.mePSXeView.setinputpadmode(ePSXe.this.emu_menu2_input1type, ePSXe.this.emu_menu2_input2type, ePSXe.this.emu_menu2_input1mode, ePSXe.this.emu_menu2_input2mode);
                            ePSXe epsxe = ePSXe.this;
                            ArrayAdapter<String> arrayAdapter = ePSXe.this.menuinputadapter;
                            ArrayList<String> arrayList = ePSXe.this.menuinputlist;
                            StringBuilder append3 = new StringBuilder().append(ePSXe.this.getString(R.string.menuinput_input1mode)).append("=");
                            if (ePSXe.this.emu_menu2_input1type == 4) {
                                str4 = ePSXe.this.emu_menu2_input1mode == 0 ? "Digital" : "Analog";
                            } else {
                                str4 = "Only Digital";
                            }
                            epsxe.updateData(arrayAdapter, arrayList, position, append3.append(str4).toString());
                            break;
                        }
                        break;
                    case 1:
                        if (ePSXe.this.emuStatus == 1 && ePSXe.this.emu_menu2_input2type == 4) {
                            ePSXe.this.emu_menu2_input2mode ^= 1;
                            ePSXe.this.mePSXeView.setinputpadmode(ePSXe.this.emu_menu2_input1type, ePSXe.this.emu_menu2_input2type, ePSXe.this.emu_menu2_input1mode, ePSXe.this.emu_menu2_input2mode);
                            ePSXe epsxe2 = ePSXe.this;
                            ArrayAdapter<String> arrayAdapter2 = ePSXe.this.menuinputadapter;
                            ArrayList<String> arrayList2 = ePSXe.this.menuinputlist;
                            StringBuilder append4 = new StringBuilder().append(ePSXe.this.getString(R.string.menuinput_input2mode)).append("=");
                            if (ePSXe.this.emu_menu2_input2type == 4) {
                                str3 = ePSXe.this.emu_menu2_input2mode == 0 ? "Digital" : "Analog";
                            } else {
                                str3 = "Only Digital";
                            }
                            epsxe2.updateData(arrayAdapter2, arrayList2, position, append4.append(str3).toString());
                            break;
                        }
                        break;
                    case 2:
                        if (ePSXe.this.emuStatus == 1) {
                            ePSXe.this.emu_menu2_touchscreen ^= 2;
//                            ePSXe.this.mePSXeView.setinputpaintpadmode(ePSXe.this.emu_menu2_touchscreen, ePSXe.this.mePSXeReadPreferences.getInputPaintPadMode2());
                            ePSXe.this.mePSXeView.setinputpaintpadmode(0, ePSXe.this.mePSXeReadPreferences.getInputPaintPadMode2());
                            ePSXe.this.updateData(ePSXe.this.menuinputadapter, ePSXe.this.menuinputlist, position, ePSXe.this.getString(R.string.menuinput_touchscreen) + "=" + (ePSXe.this.emu_menu2_touchscreen == 0 ? "Show" : "Hidden"));
                            break;
                        }
                        break;
                    case 3:
                        if (ePSXe.this.emuStatus == 1) {
                            ePSXe.this.emu_menu2_screenvibrate ^= 1;
                            ePSXe.this.mePSXeView.setinputvibration(ePSXe.this.emu_menu2_screenvibrate, ePSXe.this.mePSXeReadPreferences.getInputVibrate2());
                            ePSXe.this.updateData(ePSXe.this.menuinputadapter, ePSXe.this.menuinputlist, position, ePSXe.this.getString(R.string.menuinput_screenvibrate) + "=" + (ePSXe.this.emu_menu2_screenvibrate == 0 ? "Disabled" : "Enabled"));
                            break;
                        }
                        break;
                    case 4:
                        if (ePSXe.this.emuStatus == 1) {
                            if (ePSXe.this.emu_menu2_autofirefreq != 0) {
                                ePSXe.this.emu_menu2_autofireenabled ^= 1;
                                if (ePSXe.this.emu_menu2_autofireenabled != 0) {
                                    ePSXe.this.f153e.enableautofire(0, 0, ePSXe.this.emu_menu2_autofirebutton, ePSXe.this.emu_menu2_autofirefreq, 1);
                                } else {
                                    ePSXe.this.f153e.disableautofire(0, 0);
                                }
                            } else if (ePSXe.this.emu_ui_show_msg == 1) {
                                Toast.makeText(mCont, "Autofire button not configured on preferences!", 1).show();
                            }
                            ePSXe.this.updateData(ePSXe.this.menuinputadapter, ePSXe.this.menuinputlist, position, ePSXe.this.getString(R.string.menuinput_autofire) + "=" + (ePSXe.this.emu_menu2_autofireenabled == 0 ? "Disabled" : "Enabled"));
                            break;
                        }
                        break;
                    case 5:
                        if (ePSXe.this.emuStatus == 1) {
                            ePSXe.access$5308(ePSXe.this);
                            if (ePSXe.this.emu_menu2_inputprofile > 3) {
                                ePSXe.this.emu_menu2_inputprofile = 0;
                            }
                            ePSXe.this.mePSXeView.setinputprofile(ePSXe.this.emu_menu2_inputprofile);
                            ePSXe.this.updateData(ePSXe.this.menuinputadapter, ePSXe.this.menuinputlist, position, ePSXe.this.getString(R.string.menuinput_profile) + "=" + (ePSXe.this.emu_menu2_inputprofile == 0 ? "default" : Integer.valueOf(ePSXe.this.emu_menu2_inputprofile + 1)));
                            break;
                        }
                        break;
                }
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(mCont).setOnKeyListener(new DialogInterface.OnKeyListener() { // from class: com.epsxe.ePSXe.ePSXe.20
            @Override // android.content.DialogInterface.OnKeyListener
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode != 4 || event.getAction() != 1 || event.isCanceled()) {
                    return false;
                }
                ePSXe.this.alertdialog_menu2(mCont);
                DialogUtil.closeDialog(ePSXe.this.menuinputAlert);
                return true;
            }
        });
        builder.setView(gListView);
        this.menuinputAlert = builder.create();
        this.menuinputAlert.show();
    }

    public void CreateCHTDialog(final Context mContext) {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        final ListView mListView = new ListView(this);
        this.currentCHTDir = new File(path);
        Log.e("epsxe", MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + path);
        fillCHT(this.currentCHTDir);
        mListView.setAdapter((ListAdapter) this.chtadapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.epsxe.ePSXe.ePSXe.21
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> listView, View itemView, int position, long itemId) {
                OptionCD o = ePSXe.this.chtadapter.getItem(position);
                if (o.getData().equalsIgnoreCase("folder") || o.getData().equalsIgnoreCase("parent directory")) {
                    File currentDir = new File(o.getPath());
                    Log.e("epsxe", "changepath " + o.getPath());
                    ePSXe.this.fillCHT(currentDir);
                    mListView.setAdapter((ListAdapter) ePSXe.this.chtadapter);
                    return;
                }
                if (!o.getPath().equalsIgnoreCase("folder")) {
                    FileUtil.copyFile(new File(o.getPath()), new File(ContextCompat.getDataDir(ePSXe.this).getAbsolutePath() + "/epsxe/cheats/" + ePSXe.this.f153e.getCode() + ".txt"));
                    ePSXe.this.f153e.reloadAllGS();
                    ePSXe.this.alertdialog_menucheat(mContext);
                    DialogUtil.closeDialog(ePSXe.this.mchtAlert);
                }
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setView(mListView);
        this.mchtAlert = builder.create();
        this.mchtAlert.show();
    }

    private boolean acceptCHT(String name) {
        return name.toLowerCase().endsWith(".txt") || name.toLowerCase().endsWith(".cht");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void fillCHT(File f) {
        File[] dirs = f.listFiles();
        setTitle(R.string.main_selectiso);
        ArrayList arrayList = new ArrayList();
        List<OptionCD> dir = new ArrayList<>();
        try {
            for (File ff : dirs) {
                if (ff.isDirectory()) {
                    dir.add(new OptionCD(ff.getName(), "Folder", ff.getAbsolutePath(), 0));
                } else if (acceptCHT(ff.getName())) {
                    arrayList.add(new OptionCD(ff.getName(), getString(R.string.main_filesize) + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + ff.length() + " Bytes", ff.getAbsolutePath(), 0));
                }
            }
        } catch (Exception e) {
        }
        Collections.sort(dir);
        Collections.sort(arrayList);
        dir.addAll(arrayList);
        if (!f.getAbsolutePath().equalsIgnoreCase("/")) {
            dir.add(0, new OptionCD("..", "Parent Directory", f.getParent(), 0));
        }
        this.chtadapter = new cdArrayAdapter(this, R.layout.file_view, dir);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void alertdialog_editCheats(final Context mContext) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(R.string.cheatedit_title);
        alert.setMessage(getString(R.string.cheatedit_msg));
        String cheats = FileUtil.readFileToString(ContextCompat.getDataDir(this).getAbsolutePath() + "/epsxe/cheats/" + this.f153e.getCode() + ".txt");
        final EditText input = new EditText(this);
        input.setInputType(655537);
        input.setText(cheats);
        alert.setView(input);
        alert.setPositiveButton(getString(R.string.file_save), new DialogInterface.OnClickListener() { // from class: com.epsxe.ePSXe.ePSXe.22
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int whichButton) {
                FileUtil.writeStringToFile(ContextCompat.getDataDir(ePSXe.this).getAbsolutePath() + "/epsxe/cheats/" + ePSXe.this.f153e.getCode() + ".txt", input.getText().toString());
                ePSXe.this.f153e.reloadAllGS();
                ePSXe.this.alertdialog_menucheat(mContext);
            }
        });
        alert.setNegativeButton(getString(R.string.file_cancel), new DialogInterface.OnClickListener() { // from class: com.epsxe.ePSXe.ePSXe.23
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int whichButton) {
                ePSXe.this.alertdialog_menucheat(mContext);
            }
        });
        alert.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void alertdialog_menucheat(final Context mCont) {
        String[] items = {getString(R.string.menucheat_enabledisable), getString(R.string.menucheat_addedit), getString(R.string.menucheat_loadfromfile)};
        ListView gListView = new ListView(mCont);
        if (this.menucheatadapter == null) {
            this.menucheatlist.addAll(Arrays.asList(items));
            this.menucheatadapter = new ArrayAdapter<>(mCont, android.R.layout.simple_list_item_1, this.menucheatlist);
        }
        gListView.setAdapter((ListAdapter) this.menucheatadapter);
        gListView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.epsxe.ePSXe.ePSXe.24
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> listView, View itemView, int position, long itemId) {
                switch (position) {
                    case 0:
                        if (ePSXe.this.emuStatus == 1) {
                            CheatDialog.showCheatDialog(mCont, ePSXe.this.f153e);
                            DialogUtil.closeDialog(ePSXe.this.menucheatAlert);
                            break;
                        }
                        break;
                    case 1:
                        if (ePSXe.this.emuStatus == 1) {
                            ePSXe.this.alertdialog_editCheats(mCont);
                            DialogUtil.closeDialog(ePSXe.this.menucheatAlert);
                            break;
                        }
                        break;
                    case 2:
                        if (ePSXe.this.emuStatus == 1) {
                            ePSXe.this.CreateCHTDialog(mCont);
                            DialogUtil.closeDialog(ePSXe.this.menucheatAlert);
                            break;
                        }
                        break;
                }
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(mCont).setOnKeyListener(new DialogInterface.OnKeyListener() { // from class: com.epsxe.ePSXe.ePSXe.25
            @Override // android.content.DialogInterface.OnKeyListener
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode != 4 || event.getAction() != 1 || event.isCanceled()) {
                    return false;
                }
                ePSXe.this.alertdialog_menu2(mCont);
                DialogUtil.closeDialog(ePSXe.this.menucheatAlert);
                return true;
            }
        });
        builder.setView(gListView);
        this.menucheatAlert = builder.create();
        this.menucheatAlert.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void alertdialog_menuprofile(final Context mCont) {
        String[] items = {getString(R.string.menuprofile_saveprofile), getString(R.string.menuprofile_clearprofile)};
        ListView gListView = new ListView(mCont);
        if (this.menuprofileadapter == null) {
            this.menuprofilelist.addAll(Arrays.asList(items));
            this.menuprofileadapter = new ArrayAdapter<>(mCont, android.R.layout.simple_list_item_1, this.menuprofilelist);
        }
        gListView.setAdapter((ListAdapter) this.menuprofileadapter);
        gListView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.epsxe.ePSXe.ePSXe.26
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> listView, View itemView, int position, long itemId) {
                switch (position) {
                    case 0:
                        if (ePSXe.this.emuStatus == 1) {
                            ePSXe.this.saveGameProfile(ePSXe.this.f153e.getCode());
                            Toast.makeText(mCont, "Saved game profile!", 1).show();
                            break;
                        }
                        break;
                    case 1:
                        if (ePSXe.this.emuStatus == 1) {
                            ePSXe.this.clearGameProfile(ePSXe.this.f153e.getCode());
                            Toast.makeText(mCont, "Cleared the game profile!", 1).show();
                            break;
                        }
                        break;
                }
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(mCont).setOnKeyListener(new DialogInterface.OnKeyListener() { // from class: com.epsxe.ePSXe.ePSXe.27
            @Override // android.content.DialogInterface.OnKeyListener
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode != 4 || event.getAction() != 1 || event.isCanceled()) {
                    return false;
                }
                ePSXe.this.alertdialog_menu2(mCont);
                DialogUtil.closeDialog(ePSXe.this.menuprofileAlert);
                return true;
            }
        });
        builder.setView(gListView);
        this.menuprofileAlert = builder.create();
        this.menuprofileAlert.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setoglfixesValue(int value) {
        if (value == 0) {
            this.f153e.gpusetoptiongl(1, 0, value);
        } else {
            this.f153e.gpusetoptiongl(0, 0, value);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void alertdialog_menuogl(final Context mCont) {
        int value = 0;
        if (this.emuStatus == 1) {
            value = this.f153e.gpugetoptionfixesgl();
        }
        String[] items = new String[12];
        items[0] = "Adjust framebuffer access=" + ((value & 1) == 0 ? "Disabled" : "Enabled");
        items[1] = "Use old texture filtering=" + ((value & 2) == 0 ? "Disabled" : "Enabled");
        items[2] = "Ignore black brighness color=" + ((value & 4) == 0 ? "Disabled" : "Enabled");
        items[3] = "Swap front/back detection=" + ((value & 8) == 0 ? "Disabled" : "Enabled");
        items[4] = "Disable coord check=" + ((value & 16) == 0 ? "Disabled" : "Enabled");
        items[5] = "Remove blue glitches=" + ((value & 32) == 0 ? "Disabled" : "Enabled");
        items[6] = "G4 Polygon cache=" + ((value & 512) == 0 ? "Disabled" : "Enabled");
        items[7] = "Lazy upload detection=" + ((value & 1024) == 0 ? "Disabled" : "Enabled");
        items[8] = "Odd/even bit hack=" + ((value & 2048) == 0 ? "Disabled" : "Enabled");
        items[9] = "Expand screen width=" + ((value & 4096) == 0 ? "Disabled" : "Enabled");
        items[10] = "Special upload detection=" + ((value & 8192) == 0 ? "Disabled" : "Enabled");
        items[11] = "Mixed software FB access=" + ((value & 16384) == 0 ? "Disabled" : "Enabled");
        ListView gListView = new ListView(mCont);
        if (this.menuogladapter == null) {
            this.menuogllist.addAll(Arrays.asList(items));
            this.menuogladapter = new ArrayAdapter<>(mCont, android.R.layout.simple_list_item_1, this.menuogllist);
        }
        gListView.setAdapter((ListAdapter) this.menuogladapter);
        gListView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.epsxe.ePSXe.ePSXe.28
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> listView, View itemView, int position, long itemId) {
                int value2 = 0;
                if (ePSXe.this.emuStatus == 1) {
                    value2 = ePSXe.this.f153e.gpugetoptionfixesgl();
                }
                switch (position) {
                    case 0:
                        if (ePSXe.this.emuStatus == 1) {
                            int value3 = value2 ^ 1;
                            ePSXe.this.setoglfixesValue(value3);
                            ePSXe.this.updateData(ePSXe.this.menuogladapter, ePSXe.this.menuogllist, position, "Adjust framebuffer access=" + ((value3 & 1) == 0 ? "Disabled" : "Enabled"));
                            break;
                        }
                        break;
                    case 1:
                        if (ePSXe.this.emuStatus == 1) {
                            int value4 = value2 ^ 2;
                            ePSXe.this.setoglfixesValue(value4);
                            ePSXe.this.updateData(ePSXe.this.menuogladapter, ePSXe.this.menuogllist, position, "Use old texture filtering=" + ((value4 & 2) == 0 ? "Disabled" : "Enabled"));
                            break;
                        }
                        break;
                    case 2:
                        if (ePSXe.this.emuStatus == 1) {
                            int value5 = value2 ^ 4;
                            ePSXe.this.setoglfixesValue(value5);
                            ePSXe.this.updateData(ePSXe.this.menuogladapter, ePSXe.this.menuogllist, position, "Ignore black brighness color=" + ((value5 & 4) == 0 ? "Disabled" : "Enabled"));
                            break;
                        }
                        break;
                    case 3:
                        if (ePSXe.this.emuStatus == 1) {
                            int value6 = value2 ^ 8;
                            ePSXe.this.setoglfixesValue(value6);
                            ePSXe.this.updateData(ePSXe.this.menuogladapter, ePSXe.this.menuogllist, position, "Swap front/back detection=" + ((value6 & 8) == 0 ? "Disabled" : "Enabled"));
                            break;
                        }
                        break;
                    case 4:
                        if (ePSXe.this.emuStatus == 1) {
                            int value7 = value2 ^ 16;
                            ePSXe.this.setoglfixesValue(value7);
                            ePSXe.this.updateData(ePSXe.this.menuogladapter, ePSXe.this.menuogllist, position, "Disable coord check=" + ((value7 & 16) == 0 ? "Disabled" : "Enabled"));
                            break;
                        }
                        break;
                    case 5:
                        if (ePSXe.this.emuStatus == 1) {
                            int value8 = value2 ^ 32;
                            ePSXe.this.setoglfixesValue(value8);
                            ePSXe.this.updateData(ePSXe.this.menuogladapter, ePSXe.this.menuogllist, position, "Remove blue glitches=" + ((value8 & 32) == 0 ? "Disabled" : "Enabled"));
                            break;
                        }
                        break;
                    case 6:
                        if (ePSXe.this.emuStatus == 1) {
                            int value9 = value2 ^ 512;
                            ePSXe.this.setoglfixesValue(value9);
                            ePSXe.this.updateData(ePSXe.this.menuogladapter, ePSXe.this.menuogllist, position, "G4 Polygon cache=" + ((value9 & 512) == 0 ? "Disabled" : "Enabled"));
                            break;
                        }
                        break;
                    case 7:
                        if (ePSXe.this.emuStatus == 1) {
                            int value10 = value2 ^ 1024;
                            ePSXe.this.setoglfixesValue(value10);
                            ePSXe.this.updateData(ePSXe.this.menuogladapter, ePSXe.this.menuogllist, position, "Lazy upload detection=" + ((value10 & 1024) == 0 ? "Disabled" : "Enabled"));
                            break;
                        }
                        break;
                    case 8:
                        if (ePSXe.this.emuStatus == 1) {
                            int value11 = value2 ^ 2048;
                            ePSXe.this.setoglfixesValue(value11);
                            ePSXe.this.updateData(ePSXe.this.menuogladapter, ePSXe.this.menuogllist, position, "Odd/even bit hack=" + ((value11 & 2048) == 0 ? "Disabled" : "Enabled"));
                            break;
                        }
                        break;
                    case 9:
                        if (ePSXe.this.emuStatus == 1) {
                            int value12 = value2 ^ 4096;
                            ePSXe.this.setoglfixesValue(value12);
                            ePSXe.this.updateData(ePSXe.this.menuogladapter, ePSXe.this.menuogllist, position, "Expand screen width=" + ((value12 & 4096) == 0 ? "Disabled" : "Enabled"));
                            break;
                        }
                        break;
                    case 10:
                        if (ePSXe.this.emuStatus == 1) {
                            int value13 = value2 ^ 8192;
                            ePSXe.this.setoglfixesValue(value13);
                            ePSXe.this.updateData(ePSXe.this.menuogladapter, ePSXe.this.menuogllist, position, "Special upload detection=" + ((value13 & 8192) == 0 ? "Disabled" : "Enabled"));
                            break;
                        }
                        break;
                    case 11:
                        if (ePSXe.this.emuStatus == 1) {
                            int value14 = value2 ^ 16384;
                            ePSXe.this.setoglfixesValue(value14);
                            ePSXe.this.updateData(ePSXe.this.menuogladapter, ePSXe.this.menuogllist, position, "Mixed software FB access=" + ((value14 & 16384) == 0 ? "Disabled" : "Enabled"));
                            break;
                        }
                        break;
                }
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(mCont).setOnKeyListener(new DialogInterface.OnKeyListener() { // from class: com.epsxe.ePSXe.ePSXe.29
            @Override // android.content.DialogInterface.OnKeyListener
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode != 4 || event.getAction() != 1 || event.isCanceled()) {
                    return false;
                }
                ePSXe.this.alertdialog_menu2(mCont);
                DialogUtil.closeDialog(ePSXe.this.menuoglAlert);
                return true;
            }
        });
        builder.setView(gListView);
        this.menuoglAlert = builder.create();
        this.menuoglAlert.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setogl2fixesValue(int value) {
        if (value == 0) {
            this.f153e.gpusetoptiongl2(1, 0, value);
        } else {
            this.f153e.gpusetoptiongl2(0, 0, value);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void alertdialog_menuogl2(final Context mCont) {
        int value = 0;
        if (this.emuStatus == 1) {
            value = this.f153e.gpugetoptionfixesgl();
        }
        String[] items = new String[3];
        items[0] = "Fake Low Compatibility Frameread=" + ((value & 1) == 0 ? "Disabled" : "Enabled");
        items[1] = "Ignore Small Framebuffer moves=" + ((value & 2) == 0 ? "Disabled" : "Enabled");
        items[2] = "Ignore black brighness color=" + ((value & 4) == 0 ? "Disabled" : "Enabled");
        ListView gListView = new ListView(mCont);
        if (this.menuogl2adapter == null) {
            this.menuogl2list.addAll(Arrays.asList(items));
            this.menuogl2adapter = new ArrayAdapter<>(mCont, android.R.layout.simple_list_item_1, this.menuogl2list);
        }
        gListView.setAdapter((ListAdapter) this.menuogl2adapter);
        gListView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.epsxe.ePSXe.ePSXe.30
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> listView, View itemView, int position, long itemId) {
                int value2 = 0;
                if (ePSXe.this.emuStatus == 1) {
                    value2 = ePSXe.this.f153e.gpugetoptionfixesgl();
                }
                switch (position) {
                    case 0:
                        if (ePSXe.this.emuStatus == 1) {
                            int value3 = value2 ^ 1;
                            ePSXe.this.setogl2fixesValue(value3);
                            ePSXe.this.updateData(ePSXe.this.menuogl2adapter, ePSXe.this.menuogl2list, position, "Ignore Small Framebuffer moves=" + ((value3 & 2) == 0 ? "Disabled" : "Enabled"));
                            break;
                        }
                        break;
                    case 1:
                        if (ePSXe.this.emuStatus == 1) {
                            int value4 = value2 ^ 2;
                            ePSXe.this.setogl2fixesValue(value4);
                            ePSXe.this.updateData(ePSXe.this.menuogl2adapter, ePSXe.this.menuogl2list, position, "Ignore Small Framebuffer moves=" + ((value4 & 2) == 0 ? "Disabled" : "Enabled"));
                            break;
                        }
                        break;
                    case 2:
                        if (ePSXe.this.emuStatus == 1) {
                            int value5 = value2 ^ 4;
                            ePSXe.this.setogl2fixesValue(value5);
                            ePSXe.this.updateData(ePSXe.this.menuogl2adapter, ePSXe.this.menuogl2list, position, "Ignore black brighness color=" + ((value5 & 4) == 0 ? "Disabled" : "Enabled"));
                            break;
                        }
                        break;
                }
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(mCont).setOnKeyListener(new DialogInterface.OnKeyListener() { // from class: com.epsxe.ePSXe.ePSXe.31
            @Override // android.content.DialogInterface.OnKeyListener
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode != 4 || event.getAction() != 1 || event.isCanceled()) {
                    return false;
                }
                ePSXe.this.alertdialog_menu2(mCont);
                DialogUtil.closeDialog(ePSXe.this.menuogl2Alert);
                return true;
            }
        });
        builder.setView(gListView);
        this.menuogl2Alert = builder.create();
        this.menuogl2Alert.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void alertdialog_menu2(final Context mCont) {
        String[] items = {getString(R.string.menu_changedisc), getString(R.string.menu2_reset), getString(R.string.menu2_video), getString(R.string.menu2_audio), getString(R.string.menu2_input), getString(R.string.menu2_cheats), getString(R.string.menu2_profile)};
        String[] itemsgl = {getString(R.string.menu_changedisc), getString(R.string.menu2_reset), getString(R.string.menu2_video), getString(R.string.menu2_audio), getString(R.string.menu2_input), getString(R.string.menu2_cheats), getString(R.string.menu2_profile), getString(R.string.menu2_oglfixes)};
        ListView gListView = new ListView(mCont);
        if (this.men2adapter == null) {
            if (this.emu_renderer == 2 || this.emu_renderer == 4 || this.emu_renderer == 5) {
                this.men2list.addAll(Arrays.asList(itemsgl));
            } else {
                this.men2list.addAll(Arrays.asList(items));
            }
            this.men2adapter = new ArrayAdapter<>(mCont, android.R.layout.simple_list_item_1, this.men2list);
        }
        gListView.setAdapter((ListAdapter) this.men2adapter);
        gListView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.epsxe.ePSXe.ePSXe.32
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> listView, View itemView, int position, long itemId) {
                switch (position) {
                    case 0:
                        if (ePSXe.this.emuStatus == 1) {
                            ChangediscDialog.showChangediscDialog(mCont, ePSXe.this.f153e, ePSXe.this.currentPath, ePSXe.this.mIsoName);
                            DialogUtil.closeDialog(ePSXe.this.men2Alert);
                            break;
                        }
                        break;
                    case 1:
                        if (ePSXe.this.emuStatus == 1) {
                            ResetDialog.showResetDialog(mCont, ePSXe.this.f153e);
                            DialogUtil.closeDialog(ePSXe.this.men2Alert);
                            break;
                        }
                        break;
                    case 2:
                        if (ePSXe.this.emuStatus == 1) {
                            ePSXe.this.alertdialog_menuvideo(mCont);
                            DialogUtil.closeDialog(ePSXe.this.men2Alert);
                            break;
                        }
                        break;
                    case 3:
                        if (ePSXe.this.emuStatus == 1) {
                            ePSXe.this.alertdialog_menuaudio(mCont);
                            DialogUtil.closeDialog(ePSXe.this.men2Alert);
                            break;
                        }
                        break;
                    case 4:
                        if (ePSXe.this.emuStatus == 1) {
                            ePSXe.this.alertdialog_menuinput(mCont);
                            DialogUtil.closeDialog(ePSXe.this.men2Alert);
                            break;
                        }
                        break;
                    case 5:
                        if (ePSXe.this.emuStatus == 1) {
                            ePSXe.this.alertdialog_menucheat(mCont);
                            DialogUtil.closeDialog(ePSXe.this.men2Alert);
                            break;
                        }
                        break;
                    case 6:
                        if (ePSXe.this.emuStatus == 1) {
                            ePSXe.this.alertdialog_menuprofile(mCont);
                            DialogUtil.closeDialog(ePSXe.this.men2Alert);
                            break;
                        }
                        break;
                    case 7:
                        if (ePSXe.this.emuStatus == 1) {
                            if (ePSXe.this.emu_renderer == 5) {
                                ePSXe.this.alertdialog_menuogl2(mCont);
                            } else {
                                ePSXe.this.alertdialog_menuogl(mCont);
                            }
                            DialogUtil.closeDialog(ePSXe.this.men2Alert);
                            break;
                        }
                        break;
                }
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(mCont).setOnKeyListener(new DialogInterface.OnKeyListener() { // from class: com.epsxe.ePSXe.ePSXe.33
            @Override // android.content.DialogInterface.OnKeyListener
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode != 4 || event.getAction() != 1 || event.isCanceled()) {
                    return false;
                }
                DialogUtil.closeDialog(ePSXe.this.men2Alert);
                return true;
            }
        });
        builder.setView(gListView);
        this.men2Alert = builder.create();
        this.men2Alert.show();
    }

    public void menu_notrunning(int position, Context mContext) {
        switch (position) {
            case 0:
                if (check_bios(0) != -1) {
                    Log.e("epsxe", "getMiscBrowsermode " + this.mePSXeReadPreferences.getMiscBrowsermode(this.emu_androidtv));
                    this.emu_browser_mode = this.mePSXeReadPreferences.getMiscBrowsermode(this.emu_androidtv);
                    Intent myIntent = new Intent(this, (Class<?>) FileChooser.class);
                    myIntent.putExtra("com.epsxe.ePSXe.fcMode", "SELECT_ISO");
                    myIntent.putExtra("com.epsxe.ePSXe.isoPath", this.currentPath);
                    myIntent.putExtra("com.epsxe.ePSXe.xperiaplay", "" + this.emu_xperiaplay);
                    myIntent.putExtra("com.epsxe.ePSXe.browserMode", "" + this.emu_browser_mode);
                    myIntent.putExtra("com.epsxe.ePSXe.servermode", "1");
                    startActivity(myIntent);
                    finish();
                    break;
                }
                break;
            case 1:
                if (check_bios(0) != -1) {
                    Log.e("epsxe", "getMiscBrowsermode " + this.mePSXeReadPreferences.getMiscBrowsermode(this.emu_androidtv));
                    this.emu_browser_mode = this.mePSXeReadPreferences.getMiscBrowsermode(this.emu_androidtv);
                    Intent myIntent2 = new Intent(this, (Class<?>) FileChooser.class);
                    myIntent2.putExtra("com.epsxe.ePSXe.fcMode", "SELECT_ISO");
                    myIntent2.putExtra("com.epsxe.ePSXe.isoPath", this.currentPath);
                    myIntent2.putExtra("com.epsxe.ePSXe.xperiaplay", "" + this.emu_xperiaplay);
                    myIntent2.putExtra("com.epsxe.ePSXe.browserMode", "" + this.emu_browser_mode);
                    myIntent2.putExtra("com.epsxe.ePSXe.servermode", "2");
                    startActivity(myIntent2);
                    finish();
                    break;
                }
                break;
            case 2:
                ReportUtil.showReportGamepadDialog(mContext, this.mePSXeReadPreferences);
                break;
            case 3:
                ReportUtil.showReportFullPreferencesDialog(mContext, this.mePSXeReadPreferences, this.emu_enable_x86, this.emu_enable_cores, this.emu_enable_64bits);
                break;
            case 4:
                ResetPreferencesDialog.showResetPreferendesDialog(mContext);
                break;
            case 5:
                startActivity(new Intent(this, (Class<?>) ePSXeTerms.class));
                finish();
                break;
            case 6:
                finish();
                break;
        }
    }

    public void menu_rungamenet(int position, Context mContext) {
        switch (position) {
            case 0:
                this.mePSXeView.toggleframelimit();
                break;
            case 1:
                alertdialog_quitGame();
                break;
        }
    }

    public void menu_rungamenetserver(int position, Context mContext) {
        switch (position) {
            case 0:
                this.mePSXeView.toggleframelimit();
                break;
            case 1:
                showSstateDialog(this, this.f153e, 0, this.sdCardPath, this.hlebiosrunning);
                break;
            case 2:
                showSstateDialog(this, this.f153e, 1, this.sdCardPath, this.hlebiosrunning);
                break;
            case 3:
                alertdialog_quitGame();
                break;
        }
    }

    public void menu_runbios(int position, Context mContext) {
        switch (position) {
            case 0:
                this.mePSXeView.toggleframelimit();
                break;
            case 1:
                if (this.emu_renderer == 2 || this.emu_renderer == 4) {
                    if (!DeviceUtil.isAndroidTV(mContext)) {
                        this.mePSXeView.toggletools();
                        break;
                    } else {
                        alertdialog_gltoolbar(mContext);
                        break;
                    }
                } else if (this.emu_renderer == 5) {
                    if (!DeviceUtil.isAndroidTV(mContext)) {
                        this.mePSXeView.toggletools();
                        break;
                    } else {
                        alertdialog_gltoolbar2(mContext);
                        break;
                    }
                } else {
                    alertdialog_quitGame();
                    break;
                }
//                break;
            case 2:
                alertdialog_quitGame();
                break;
        }
    }

    public void menu_rungame(int position, Context mContext) {
        switch (position) {
//            case 0:
//                alertdialog_menu2(mContext);
//                break;
            case 0:
                break;
            case 1:
                showSstateDialog(this, this.f153e, 0, this.sdCardPath, this.hlebiosrunning);
                break;
            case 2:
                showSstateDialog(this, this.f153e, 1, this.sdCardPath, this.hlebiosrunning);
                break;
//            case 3:
//                CheatDialog.showCheatDialog(mContext, this.f153e);
//                break;
//            case 4:
//                this.mePSXeView.toggleframelimit();
//                break;
            case 3:
                if (BuildConfig.DEBUG) {
                    startActivity(new Intent(this, ePSXePreferences.class));
                    stopEmulation();
                    finish();
                    return;
                }
                if (this.emu_enable_gamefaq != 1) {
                    if (this.emu_renderer == 2 || this.emu_renderer == 4 || this.emu_renderer == 5) {
                        if (this.emu_renderer == 2 || this.emu_renderer == 4) {
                            if (!DeviceUtil.isAndroidTV(mContext)) {
                                this.mePSXeView.toggletools();
                                break;
                            } else {
                                alertdialog_gltoolbar(mContext);
                                break;
                            }
                        } else if (this.emu_renderer == 5) {
                            if (!DeviceUtil.isAndroidTV(mContext)) {
                                this.mePSXeView.toggletools();
                                break;
                            } else {
                                alertdialog_gltoolbar2(mContext);
                                break;
                            }
                        }
                    } else {
                        alertdialog_quitGame();
                        break;
                    }
                } else {
                    alertdialog_gamefaq();
                    break;
                }
                break;
            case 4:
                if (this.emu_enable_gamefaq == 1 && (this.emu_renderer == 2 || this.emu_renderer == 4 || this.emu_renderer == 5)) {
                    if (this.emu_renderer == 2 || this.emu_renderer == 4) {
                        if (!DeviceUtil.isAndroidTV(mContext)) {
                            this.mePSXeView.toggletools();
                            break;
                        } else {
                            alertdialog_gltoolbar(mContext);
                            break;
                        }
                    } else if (this.emu_renderer == 5) {
                        if (!DeviceUtil.isAndroidTV(mContext)) {
                            this.mePSXeView.toggletools();
                            break;
                        } else {
                            alertdialog_gltoolbar2(mContext);
                            break;
                        }
                    }
                } else {
                    alertdialog_quitGame();
                    break;
                }
                break;
            case 5:
                alertdialog_quitGame_norate();
                break;
        }
    }

    private int getDiscNumber(String fileName) {
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("(disc|disk|cd)[ _-]?(\\d+)", java.util.regex.Pattern.CASE_INSENSITIVE);
        java.util.regex.Matcher matcher = pattern.matcher(fileName);
        if (matcher.find()) {
            try {
                return Integer.parseInt(Objects.requireNonNull(matcher.group(2)));
            } catch (Exception ignore) {
            }
        }
        return -1;
    }

    private void alertdialog_menu(final Context mCont) {
        boolean oneDisk = hasExactlyOneDiscsForGame();
        ArrayList<String> menuItems = new ArrayList<>();

        if (emuStatus == 1) {
            menuItems.add(getString(R.string.menu_resume_game));
            if (SHOW_LOAD_STATE_IN_MENU) {
                menuItems.add(getString(R.string.menu_loadstate));
            }
            if (SHOW_SAVE_STATE_IN_MENU) {
                menuItems.add(getString(R.string.menu_savestate));
            }
            if (BuildConfig.DEBUG) {
                menuItems.add(getString(R.string.fbutton_preferences));
            }
            if (!oneDisk) {
                menuItems.add(getString(R.string.change_disk));
            }
            if (emu_enable_gamefaq == 1) {
                menuItems.add(getString(R.string.menu_faq));
            }
            if (emu_renderer == 2 || emu_renderer == 4 || emu_renderer == 5) {
                menuItems.add(getString(R.string.menu_tools));
            }
            menuItems.add(getString(R.string.fbutton_quit));
        } else if (emuStatus == 2) {
            menuItems.add(getString(R.string.menu_framelimit));
            if (emu_renderer == 2 || emu_renderer == 4 || emu_renderer == 5) {
                menuItems.add(getString(R.string.menu_tools));
            }
            menuItems.add(getString(R.string.fbutton_quit));
        } else {
            menuItems.add(getString(R.string.menu_runserver));
            menuItems.add(getString(R.string.menu_runclient));
            menuItems.add(getString(R.string.menu_gamepadinfo));
            menuItems.add(getString(R.string.menu_prefinfo));
            menuItems.add("Reset preferences");
            menuItems.add("Privacy Policy");
            menuItems.add(getString(R.string.fbutton_quit));
        }

        if (serverMode == 3) {
            menuItems.clear();
            menuItems.add(getString(R.string.menu_framelimit));
            if (SHOW_LOAD_STATE_IN_MENU) {
                menuItems.add(getString(R.string.menu_loadstate));
            }
            if (SHOW_SAVE_STATE_IN_MENU) {
                menuItems.add(getString(R.string.menu_savestate));
            }
            menuItems.add(getString(R.string.fbutton_quit));
        } else if (serverMode == 4) {
            menuItems.clear();
            menuItems.add(getString(R.string.menu_framelimit));
            menuItems.add(getString(R.string.fbutton_quit));
        }

        ListView gListView = new ListView(mCont);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(mCont, android.R.layout.simple_list_item_1, menuItems);
        gListView.setAdapter(adapter);

        final AlertDialog menuDialog = new AlertDialog.Builder(mCont).setView(gListView).create();

        gListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selected = adapter.getItem(position);
                if (selected == null) {
                    menuDialog.dismiss();
                    return;
                }

                if (selected.equals(getString(R.string.menu_resume_game))) {
                    // No action needed, just close the dialog
                } else if (selected.equals(getString(R.string.fbutton_quit))) {
                    forceQuitApp();
                } else if (selected.equals(getString(R.string.menu_loadstate))) {
                    showSstateDialog(mCont, f153e, 0, sdCardPath, hlebiosrunning);
                } else if (selected.equals(getString(R.string.menu_savestate))) {
                    showSstateDialog(mCont, f153e, 1, sdCardPath, hlebiosrunning);
                } else if (selected.equals(getString(R.string.fbutton_preferences))) {
                    menuDialog.dismiss();
                    restartToPreferences();
                    return;
                } else if (selected.equals(getString(R.string.change_disk))) {
                    if (hasExactlyTwoDiscsForGame()) {
                        ChangediscDialog.autoChangeToNextDiscIfTwo(mCont, f153e, currentPath, mIsoName);
                        int num = getDiscNumber(mIsoName.getmIsoName());
                        if (num > 0) {
                            Toast.makeText(mCont, String.valueOf(num), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        previousDiscPath = mIsoName.getmIsoName();
                        previousDiscSlot = mIsoName.getmIsoSlot();
                        ChangediscDialog.showChangediscDialog(mCont, f153e, currentPath, mIsoName);
                    }
                } else if (selected.equals(getString(R.string.menu_framelimit))) {
                    mePSXeView.toggleframelimit();
                } else if (selected.equals(getString(R.string.menu_tools))) {
                    alertdialog_menuogl(mCont);
                } else if (selected.equals(getString(R.string.menu_faq))) {
                    alertdialog_gamefaq();
                } else if (selected.equals(getString(R.string.menu_runserver))) {
                    serverMode = 1;
                    runIso("___RUNBIOS___", 0);
                } else if (selected.equals(getString(R.string.menu_runclient))) {
                    serverMode = 2;
                    runIso("___RUNBIOS___", 0);
                } else if (selected.equals(getString(R.string.menu_gamepadinfo))) {
                    ReportUtil.showReportGamepadDialog(ePSXe.this, mePSXeReadPreferences);
                } else if (selected.equals(getString(R.string.menu_prefinfo))) {
                    AboutDialog.showAboutDialog(ePSXe.this);
                } else if (selected.equals("Reset preferences")) {
                    ResetPreferencesDialog.showResetPreferendesDialog(ePSXe.this);
                } else if (selected.equals("Privacy Policy")) {
                    try {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.epsxe.com/privacy.php"));
                        startActivity(browserIntent);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(ePSXe.this, "Cannot open browser", Toast.LENGTH_SHORT).show();
                    }
                }

                menuDialog.dismiss();
            }
        });

        this.menAlert = menuDialog;
        this.menAlert.show();
    }

    @Override // android.app.Activity
    public void openOptionsMenu() {
        if (Build.VERSION.SDK_INT >= 11) {
            Configuration config = getResources().getConfiguration();
            if ((config.screenLayout & 15) > 3) {
                int originalScreenLayout = config.screenLayout;
                config.screenLayout = 3;
                super.openOptionsMenu();
                config.screenLayout = originalScreenLayout;
                return;
            }
            super.openOptionsMenu();
            return;
        }
        super.openOptionsMenu();
    }

    public void doMenu() {
        showOptionsMenuDialog(this, this.emu_ui_menu_mode);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        InputDevice device;
        int sources = 0;
        if (this.osVersion >= 12 && (device = event.getDevice()) != null) {
            sources = device.getSources();
            if ((this.emuStatus == 1 || this.emuStatus == 2) && this.emu_gamepad_autodetect > 0) {
                this.emu_gamepad_autodetect--;
                if (this.gpd == null) {
                    this.gpd = new GamepadDetection(this, this.mePSXeView);
                }
                if (this.gpd != null && this.gpd.isdetectedGamepad(device, event, this.analogpadid, this.mogapad).booleanValue()) {
                    this.gpd.FindGamepad(device, event, this, this.analogpadid);
                    return true;
                }
            }
        }
        int eventDevice = event.getDeviceId();
        int mdev = -1;
        int i = 0;
        while (true) {
            if (i >= 4) {
                break;
            }
            if (eventDevice != -1 && eventDevice == this.analogpadid[i]) {
                mdev = i;
                break;
            }
            i++;
        }
        if (keyCode == 0 && event.getScanCode() != 0) {
            keyCode = event.getScanCode();
        }
        int keyval = keyCode | (event.isAltPressed() ? 65536 : 0);

        if (keyval == 4) {
            if (emuStatus == 1 || emuStatus == 2) {
                alertdialog_menu(this);
            } else {
                forceQuitApp();
            }
            return true;
        }

        if (this.emuStatus == 1 || this.emuStatus == 2) {
            if ((keyval != 82) || (16778513 & sources) == 16778513) {
                for (int p = 0; p < 4; p++) {
                    if (mdev == -1 || mdev == p || this.gamepadmatch == 0) {
                        for (int k = 0; k < 20; k++) {
                            if (keyval == this.keycodes[p][k]) {
                                int[] iArr = this.pushedButtons;
                                iArr[p] = iArr[p] | (1 << k);
                                this.f153e.setPadDataMultitap(this.pushedButtons[0], this.pushedButtons[1], this.pushedButtons[2], this.pushedButtons[3]);
                                return true;
                            }
                        }
                        if ((((event.isAltPressed() || keyCode == 57 || keyCode == 58) ? 65536 : 0) | keyCode) == this.keycodes[p][20]) {
                            this.mePSXeView.setinputpadmodeanalog(p);
                            return true;
                        }
                    }
                }
                for (int k2 = 0; k2 < this.keycodesextra.length; k2++) {
                    if (keyval == this.keycodesextra[k2]) {
                        if (k2 < 5) {
                            this.f153e.setsslot(k2);
                        } else if (k2 < 10) {
                            this.f153e.setsslot(k2 + 5);
                            this.mePSXeView.setSaveslot(k2 + 5);
                        } else if (k2 < 11) {
                            this.mePSXeView.toggleframelimit();
                        } else if (k2 < 12) {
                            showOptionsMenuDialog(this, this.emu_ui_menu_mode);
                        } else if (k2 < 13) {
                            this.mePSXeView.unsetframelimit();
                        } else if (k2 < 14) {
                            int freq = this.emu_menu2_autofirefreq;
                            if (freq == 0) {
                                freq = 3;
                            }
                            this.f153e.enableautofire(0, 0, this.emu_menu2_autofirebutton, freq, 2);
                        } else if (k2 < 15) {
                            this.emu_auxvol++;
                            if (this.emu_auxvol > 16) {
                                this.emu_auxvol = 16;
                            }
                            this.f153e.setAuxVol(this.emu_auxvol);
                            this.mePSXeView.setemuvolumen(this.emu_auxvol);
                        } else if (k2 < 16) {
                            this.emu_auxvol--;
                            if (this.emu_auxvol < 0) {
                                this.emu_auxvol = 0;
                            }
                            this.f153e.setAuxVol(this.emu_auxvol);
                            this.mePSXeView.setemuvolumen(this.emu_auxvol);
                        }
                        return true;
                    }
                }
            }
        } else if (this.emu_exit_mapto_menu == 1 && keyval == 82) { // 82 = MENU
            showOptionsMenuDialog(this, this.emu_ui_menu_mode);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    public void doVibration(int player, int mode, int motor, int value) {
        InputDevice device;
        Vibrator v;
        if (this.analogpadid[player] != -1 && mode == 1) {
            if (Build.VERSION.SDK_INT >= 16 && (device = InputDevice.getDevice(this.analogpadid[player])) != null && (v = device.getVibrator()) != null && v.hasVibrator()) {
                if (motor != 0 || value != 1) {
                    if (motor != 1 || value <= 20) {
                        return;
                    }
                    v.vibrate((value >> 2) + 35);
                    return;
                }
                v.vibrate(35L);
                return;
            }
            return;
        }
        Vibrator v2 = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (v2 != null) {
            if (value != 0) {
                if (this.emu_vibration_state[player] > 0) {
                    this.emu_vibration_state[player] = value - 20;
                    if (this.emu_vibration_state[player] <= 0) {
                        v2.cancel();
                        return;
                    }
                    return;
                }
                if (motor != 0 || value != 1) {
                    if (motor != 1 || value <= 20) {
                        return;
                    }
                    this.emu_vibration_state[player] = (value >> 2) + 35;
                    v2.vibrate((value >> 2) + 35);
                    return;
                }
                this.emu_vibration_state[player] = 35;
                v2.vibrate(35L);
                return;
            }
            this.emu_vibration_state[player] = 0;
            v2.cancel();
        }
    }

    public void doError(int err) {
        this.handlerErr.post(this.runnableErr);
    }

    public void setPushedButton(int pad, int v) {
        int[] iArr = this.pushedButtons;
        iArr[pad] = iArr[pad] | v;
        this.f153e.setPadDataMultitap(this.pushedButtons[0], this.pushedButtons[1], this.pushedButtons[2], this.pushedButtons[3]);
    }

    public void unsetPushedButton(int pad, int v) {
        int[] iArr = this.pushedButtons;
        iArr[pad] = iArr[pad] & (v ^ (-1));
        this.f153e.setPadDataMultitap(this.pushedButtons[0], this.pushedButtons[1], this.pushedButtons[2], this.pushedButtons[3]);
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            DeviceUtil.setInmmersionMode(this);
        }
    }

    @Override // android.hardware.SensorEventListener
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override // android.hardware.SensorEventListener
    public void onSensorChanged(SensorEvent event) {
        if (this.emu_acc_mode != 0) {
            Sensor mySensor = event.sensor;
            if (mySensor.getType() == 1) {
                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];
                boolean change = false;
                if (Math.abs(x) < this.accDZ) {
                    x = 0.0f;
                    this.lastX = 0.0f;
                }
                if (Math.abs(y) < this.accDZ) {
                    y = 0.0f;
                    this.lastY = 0.0f;
                }
                if (Math.abs(z) < this.accDZ) {
                    z = 0.0f;
                    this.lastZ = 0.0f;
                }
                if (Math.abs(this.lastX - x) > this.accJitterMargin) {
                    this.lastX = x;
                    change = true;
                }
                if (Math.abs(this.lastY - y) > this.accJitterMargin) {
                    this.lastY = y;
                    change = true;
                }
                if (Math.abs(this.lastZ - z) > this.accJitterMargin) {
                    this.lastZ = z;
                    change = true;
                }
                if (change) {
                    if (this.lastX > 5.0f) {
                        this.lastX = 5.0f;
                    } else if (this.lastX < -5.0f) {
                        this.lastX = -5.0f;
                    }
                    if (this.lastY > 5.0f) {
                        this.lastY = 5.0f;
                    } else if (this.lastY < -5.0f) {
                        this.lastY = -5.0f;
                    }
                    if (this.lastZ > 5.0f) {
                        this.lastZ = 5.0f;
                    } else if (this.lastZ < -5.0f) {
                        this.lastZ = -5.0f;
                    }
                    int val1 = (this.emu_screen_orientation != 0 || this.emu_acc_mode >= 7) ? Math.round(((-128.0f) * this.lastX) / 5.0f) : Math.round((128.0f * this.lastY) / 5.0f);
                    int val2 = (this.emu_screen_orientation != 0 || this.emu_acc_mode >= 7) ? Math.round(((-128.0f) * this.lastY) / 5.0f) : Math.round((128.0f * this.lastX) / 5.0f);
                    int val3 = (this.emu_screen_orientation != 0 || this.emu_acc_mode >= 7) ? Math.round(((-128.0f) * this.lastZ) / 5.0f) : Math.round(((-128.0f) * this.lastZ) / 5.0f);
                    if (val1 > 127) {
                        val1 = 127;
                    }
                    if (val1 < -128) {
                        val1 = -128;
                    }
                    if (val2 > 127) {
                        val2 = 127;
                    }
                    if (val2 < -128) {
                    }
                    if (val3 > 127) {
                        val3 = 127;
                    }
                    if (val3 < -128) {
                        val3 = -128;
                    }
                    if (this.emu_acc_mode != 1) {
                        if (this.emu_acc_mode != 2) {
                            if (this.emu_acc_mode != 3) {
                                if (this.emu_acc_mode != 4 && this.emu_acc_mode != 7) {
                                    if (this.emu_acc_mode != 5 && this.emu_acc_mode != 8) {
                                        if (this.emu_acc_mode == 6 || this.emu_acc_mode == 9) {
                                            this.f153e.setpadanalog(0, 0, val3, val1);
                                            return;
                                        }
                                        return;
                                    }
                                    this.f153e.setpadanalog(0, 0, 0, val1);
                                    return;
                                }
                                this.f153e.setpadanalog(0, 0, val3, 0);
                                return;
                            }
                            this.f153e.setpadanalog(0, 0, val1, val3);
                            return;
                        }
                        this.f153e.setpadanalog(0, 0, 0, val3);
                        return;
                    }
                    this.f153e.setpadanalog(0, 0, val1, 0);
                }
            }
        }
    }

    public void showSstateDialog(final Context cont, final libepsxe e, final int mycmd, String sdCardPath, int hlebios) {
        String currentDir = sdCardPath + "/epsxe/sstates";
        String gameCode = e.getCode();
        if (hlebios == 1) {
            gameCode = gameCode + "HLE";
        }
        Log.e("sstate", "gameCode " + gameCode);
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(24576);
        final sstateArrayAdapter adapter = new sstateArrayAdapter(cont, R.layout.sstate_view, SstateDialog.fillSS(cont, currentDir, gameCode, byteBuffer));
        ListView sstateListView = new ListView(cont);
        sstateListView.setAdapter((ListAdapter) adapter);
        AlertDialog.Builder builder = new AlertDialog.Builder(cont);
        builder.setView(sstateListView);
        final AlertDialog sAlert = builder.create();
        sAlert.show();
        sstateListView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.epsxe.ePSXe.ePSXe.35
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OptionSstate o = adapter.getItem(position);
                if (o != null) {
                    Log.e("sstate", "state: " + o.getPath());
                    int slot = Integer.parseInt(o.getSlot());
                    Log.e("sstate", "slot: " + slot);
                    if (slot != -1) {
                        if (mycmd == 0) {
                            e.setsslot(slot);
                            ePSXe.this.tainted = e.isTainted();
                        } else {
                            e.setsslot(slot + 10);
                            ePSXe.this.mePSXeView.setSaveslot(slot + 10);
                        }
                    }
                }
                DialogUtil.closeDialog(sAlert);
            }
        });
        sstateListView.setLongClickable(true);
        sstateListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() { // from class: com.epsxe.ePSXe.ePSXe.36
            @Override // android.widget.AdapterView.OnItemLongClickListener
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                OptionSstate o = adapter.getItem(position);
                if (o != null) {
                    Log.e("sstate", "state: " + o.getPath());
                    int slot = Integer.parseInt(o.getSlot());
                    Log.e("sstate", "slot: " + slot);
                    File fileTmp = new File(o.getPath());
                    if (fileTmp.exists()) {
                        SstateDialog.showDeleteSstateDialog(cont, o.getPath());
                    }
                }
                DialogUtil.closeDialog(sAlert);
                return true;
            }
        });
    }

    public void initGamepad(int id, String name, String value, int player, int prof, int ext) {
        int i = player - 1;
        this.analogpadid[i] = id;
        this.analogpadidString[i] = value;
        this.analogpaddescString[i] = name;
        if (player != 1) {
            this.gamepadmatch = 1;
        }
        String[] gamepadData = this.gpd.getGamepadSettings(prof, ext);
        this.analogrange[i] = Integer.parseInt(gamepadData[1]);
        this.analogleftx[i] = Integer.parseInt(gamepadData[2]);
        this.analoglefty[i] = Integer.parseInt(gamepadData[3]);
        this.analogrightx[i] = Integer.parseInt(gamepadData[4]);
        this.analogrighty[i] = Integer.parseInt(gamepadData[5]);
        this.analogl2[i] = Integer.parseInt(gamepadData[6]);
        this.analogr2[i] = Integer.parseInt(gamepadData[7]);
        if (this.analogl2[i] == 17 || this.analogl2[i] == 18 || this.analogl2[i] == 19 || this.analogl2[i] == 22 || this.analogl2[i] == 23) {
            this.analogl2range[i] = 0;
        } else {
            this.analogl2range[i] = 1;
        }
        if (this.analogr2[i] == 17 || this.analogr2[i] == 18 || this.analogr2[i] == 19 || this.analogr2[i] == 22 || this.analogr2[i] == 23) {
            this.analogr2range[i] = 0;
        } else {
            this.analogr2range[i] = 1;
        }
        for (int j = 0; j < 16; j++) {
            this.keycodes[i][j] = Integer.parseInt(gamepadData[j + 8]);
            Log.e("epsxekey", "keycode[" + i + "][" + j + "] = " + this.keycodes[i][j]);
        }
        this.keycodes[i][19] = Integer.parseInt(gamepadData[24]);
    }

    @Override
    public void onSettingsOpened() {
        pauseEmulation();
    }

    @Override
    public void onSettingsClosed() {
        resumeEmulation();
    }
}
