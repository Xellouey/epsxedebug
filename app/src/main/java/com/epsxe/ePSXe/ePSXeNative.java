package com.epsxe.ePSXe;

import android.app.AlertDialog;
import android.app.NativeActivity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v4.media.session.PlaybackStateCompat;

import androidx.core.content.ContextCompat;
import androidx.core.view.InputDeviceCompat;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.bda.controller.Controller;
import com.bda.controller.ControllerListener;
import com.bda.controller.StateEvent;
import com.epsxe.ePSXe.jni.libdetect;
import com.epsxe.ePSXe.jni.libepsxe;
import com.fasterxml.jackson.core.util.MinimalPrettyPrinter;
import com.google.android.gms.drive.DriveFile;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.cookie.ClientCookie;

/* loaded from: classes.dex */
public class ePSXeNative extends NativeActivity {
    private static final int ABOUT_ID = 7;
    private static final String BLUEZ_IME_PACKAGE = "com.hexad.bluezime";
    private static final String BLUEZ_IME_SERVICE = "com.hexad.bluezime.BluezService";
    private static final int CHANGEDISC_ID = 3;
    private static final int CHEAT_ID = 15;
    public static final int CPUOVERCLOCK_DISABLED = 10;
    public static final int CPUOVERCLOCK_X1_5 = 15;
    public static final int CPUOVERCLOCK_X2 = 20;
    public static final int CPUOVERCLOCK_X2_5 = 25;
    public static final int CPUOVERCLOCK_X3 = 30;
    public static final String EVENT_CONNECTED = "com.hexad.bluezime.connected";
    public static final String EVENT_CONNECTED_ADDRESS = "address";
    public static final String EVENT_DIRECTIONALCHANGE = "com.hexad.bluezime.directionalchange";
    public static final String EVENT_DIRECTIONALCHANGE_DIRECTION = "direction";
    public static final String EVENT_DIRECTIONALCHANGE_VALUE = "value";
    public static final String EVENT_DISCONNECTED = "com.hexad.bluezime.disconnected";
    public static final String EVENT_DISCONNECTED_ADDRESS = "address";
    public static final String EVENT_ERROR = "com.hexad.bluezime.error";
    public static final String EVENT_ERROR_FULL = "stacktrace";
    public static final String EVENT_ERROR_SHORT = "message";
    public static final String EVENT_KEYPRESS = "com.hexad.bluezime.keypress";
    public static final String EVENT_KEYPRESS_ACTION = "action";
    public static final String EVENT_KEYPRESS_KEY = "key";
    public static final String EVENT_REPORTSTATE = "com.hexad.bluezime.currentstate";
    public static final String EVENT_REPORTSTATE_CONNECTED = "connected";
    public static final String EVENT_REPORTSTATE_DEVICENAME = "devicename";
    public static final String EVENT_REPORTSTATE_DISPLAYNAME = "displayname";
    public static final String EVENT_REPORTSTATE_DRIVERNAME = "drivername";
    public static final String EVENT_REPORT_CONFIG = "com.hexad.bluezime.config";
    public static final String EVENT_REPORT_CONFIG_DRIVER_DISPLAYNAMES = "driverdisplaynames";
    public static final String EVENT_REPORT_CONFIG_DRIVER_NAMES = "drivernames";
    public static final String EVENT_REPORT_CONFIG_VERSION = "version";
    private static final int FRAMELIMIT_ID = 14;
    private static final int HELP_ID = 9;
    public static final int KEYCODE_BUTTON_A = 96;
    public static final int KEYCODE_BUTTON_B = 97;
    public static final int KEYCODE_BUTTON_C = 98;
    public static final int KEYCODE_BUTTON_X = 99;
    public static final int KEYCODE_BUTTON_Y = 100;
    public static final int KEYCODE_BUTTON_Z = 101;
    private static final int LOADSTATE_ID = 4;
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
    public static final int PLAYERMODE_MULTITAP1 = 2;
    public static final int PLAYERMODE_MULTITAP2 = 3;
    public static final int PLAYERMODE_NORMAL = 1;
    public static final int PLAYERMODE_SPLITSCR = 10;
    private static final int PREFERENCES_GROUP_ID = 0;
    private static final int PREFERENCES_ID = 6;
    private static final int QUIT_ID = 8;
    public static final int RENDERER_GL2 = 3;
    public static final int RENDERER_GLEXT = 2;
    public static final int RENDERER_GLEXT2 = 4;
    public static final int RENDERER_HW = 1;
    public static final int RENDERER_PETEGL = 5;
    public static final int RENDERER_SW = 0;
    public static final String REQUEST_CONFIG = "com.hexad.bluezime.getconfig";
    public static final String REQUEST_CONNECT = "com.hexad.bluezime.connect";
    public static final String REQUEST_CONNECT_ADDRESS = "address";
    public static final String REQUEST_CONNECT_DRIVER = "driver";
    public static final String REQUEST_DISCONNECT = "com.hexad.bluezime.disconnect";
    public static final String REQUEST_FEATURECHANGE = "com.hexad.bluezime.featurechange";
    public static final String REQUEST_FEATURECHANGE_ACCELEROMETER = "accelerometer";
    public static final String REQUEST_FEATURECHANGE_LEDID = "ledid";
    public static final String REQUEST_FEATURECHANGE_RUMBLE = "rumble";
    public static final String REQUEST_STATE = "com.hexad.bluezime.getstate";
    private static final int RUNBIOS_ID = 2;
    private static final int RUNGAME_ID = 1;
    private static final int SAVESTATE_ID = 5;
    public static final String SESSION_ID = "com.hexad.bluezime.sessionid";
    public static final String SESSION_NAME = "EPSXE-IME";
    public static final int SOUNDLATENCY_LOW = 2;
    public static final int SOUNDLATENCY_LOWEST = 4;
    public static final int SOUNDLATENCY_NORMAL = 0;
    public static final int SOUNDLATENCY_VERYLOW = 3;
    private static final int SPLITH1_ID = 11;
    private static final int SPLITH2_ID = 12;
    private static final int SPLITV_ID = 10;
    public static final int STATUS_NORUNNING = 0;
    public static final int STATUS_PAUSED = 3;
    public static final int STATUS_RUNNING_BIOS = 2;
    public static final int STATUS_RUNNING_GAME = 1;
    private static final int TOOLSGL_ID = 16;
    private sstateArrayAdapter adapter;
    private Button button_help;
    private Button button_preferences;
    private Button button_quit;
    private Button button_run_bios;
    private Button button_run_game;
    private ByteBuffer byteBuffer;
    private cdArrayAdapter cdadapter;
    private File currentCDDir;
    private File currentDir;

    /* renamed from: d */
    private libdetect f156d;

    /* renamed from: e */
    private libepsxe f157e;
    private String gpuPluginName;
    private AlertDialog mAlert;
    private ListView mListView;
    private ePSXeReadPreferences mePSXeReadPreferences;
    private ePSXeSound mePSXeSound;
    private ePSXeViewGL mePSXeView2;
    private AlertDialog sAlert;
    private ListView sstateListView;
    private int emuStatus = 0;
    private String mIsoName = "";
    private int mIsoSlot = 0;
    private Boolean snapRestoring = false;
    private int fps = 60;
    private Class ePSXeViewType = null;
    private ePSXeView mePSXeView = null;
    private String currentPath = "";
    private String sdCardPath = "/sdcard";
    private int emu_screen_vrmode = 0;
    private int emu_screen_vrdistorsion = 1;
    private int emu_screen_ratio = 0;
    private int emu_screen_orientation = 0;
    private int emu_player_mode = 1;
    private int[] emu_pad_type = {0, 0};
    private int emu_enable_neon = 0;
    private int emu_enable_x86 = 0;
    private int emu_renderer = 1;
    private int emu_enable_check = 0;
    private int emu_autosave = 0;
    private int emu_autoload_disable = 0;
    private int emu_browser_mode = 1;
    private int emu_gui = 1;
    private int emu_padType = 0;
    private int emu_ui_resume_dialog = 1;
    private int[][] keycodes = (int[][]) Array.newInstance((Class<?>) Integer.TYPE, 4, 21);
    private int[] keycodesextra = new int[16];
    private int[] pushedButtons = {0, 0, 0, 0};
    private String[] analogpadidString = {"none", "none", "none", "none"};
    private String[] analogpaddescString = {"virtual", "none", "none", "none"};
    private int[] analogpadid = {-1, -1, -1, -1};
    private int[] analogrange = {0, 0, 0, 0};
    private int[] analogleftx = {0, 0, 0, 0};
    private int[] analoglefty = {1, 1, 1, 1};
    private int[] analogrightx = {11, 11, 11, 11};
    private int[] analogrighty = {14, 14, 14, 14};
    private int[] analogl2 = {48, 48, 48, 48};
    private int[] analogr2 = {48, 48, 48, 48};
    private int[] analogl2range = {0, 0, 0, 0};
    private int[] analogr2range = {0, 0, 0, 0};
    private int[] analoghatx = {0, 0, 0, 0};
    private int[] analoghaty = {0, 0, 0, 0};
    private int[] analogdpadfromanalog = {0, 0, 0, 0};
    private int hlebiosrunning = 0;
    private int emu_menu2_autofirefreq = 0;
    private int emu_menu2_autofirebutton = 0;
    private int emu_menu2_autofireenabled = 0;
    private int emu_menu2_dynamicpad = 0;
    private int emu_menu2_dynamicaction = 0;
    private int emu_menu2_brightnessprofile = 0;
    private int emu_menu2_iresolution = 1;
    private int emu_menu2_touchscreen = 0;
    Handler handlerErr = new Handler();
    private Runnable runnableErr = new Runnable() { // from class: com.epsxe.ePSXe.ePSXeNative.1
        @Override // java.lang.Runnable
        public void run() {
            if (ePSXeNative.this.emuStatus == 1 || ePSXeNative.this.emuStatus == 2) {
                ePSXeNative.this.alertdialog_quitGame_error();
            }
        }
    };
    Handler mHandlerAL = new Handler();
    private Runnable mLaunchTaskAL = new Runnable() { // from class: com.epsxe.ePSXe.ePSXeNative.2
        @Override // java.lang.Runnable
        public void run() {
            ePSXeNative.this.CreateSstateDialog(ePSXeNative.this.f157e.getCode(), 0, 1);
        }
    };
    Handler mHandler = new Handler();
    private Runnable mLaunchTask = new Runnable() { // from class: com.epsxe.ePSXe.ePSXeNative.11
        @Override // java.lang.Runnable
        public void run() {
            ePSXeNative.this.mePSXeSound.exit();
            ePSXeNative.this.emuStatus = 0;
            if (ePSXeNative.this.emu_gui == 1) {
                Intent myIntent = new Intent(ePSXeNative.this, (Class<?>) ePSXe.class);
                myIntent.setFlags(DriveFile.MODE_READ_ONLY);
                ePSXeNative.this.startActivity(myIntent);
            }
            ePSXeNative.this.f157e.quit();
            ePSXeNative.this.finish();
        }
    };
    private String gameCode = "SLUS_000.00";
    private int sscmd = 0;
    String[][] bioslist = {new String[]{"SCPH-1000/DTL-H1000 (Version 1.0 J)", "239665b1a3dade1b5a52c06338011044"}, new String[]{"SCPH-1001/DTL-H1201/DTL-H3001 (Version 2.2 12/04/95 A)", "924e392ed05558ffdb115408c263dccf"}, new String[]{"DTL-H1001 (Version 2.0 05/07/95 A)", "dc2b9bf8da62ec93e868cfd29f0d067d"}, new String[]{"DTL-H1002/SCPH-1002 (Version 2.0 05/10/95 E)", "54847e693405ffeb0359c6287434cbef"}, new String[]{"SCPH-3000/DTL-H1000H (Version 1.1 01/22/95)", "849515939161e62f6b866f6853006780"}, new String[]{"SCPH-3500 (Version 2.1 07/17/95 J)", "cba733ceeff5aef5c32254f1d617fa62"}, new String[]{"DTL-H1100 (Version 2.2 03/06/96 D)", "ca5cfc321f916756e3f0effbfaeba13b"}, new String[]{"DTL-H1101 (Version 2.1 07/17/95 A)", "da27e8b6dab242d8f91a9b25d80c63b8"}, new String[]{"SCPH-1002/DTL-H1102 (Version 2.1 07/17/95 E)", "417b34706319da7cf001e76e40136c23"}, new String[]{"SCPH-5000/DTL-H1200 (Version 2.2 12/04/95 J)", "57a06303dfa9cf9351222dfcbb4a29d9"}, new String[]{"SCPH-1002/DTL-H1202/DTL-H3002 (Version 2.2 12/04/95 E)", "e2110b8a2b97a8e0b857a45d32f7e187"}, new String[]{"SCPH-5500 (Version 3.0 09/09/96 J)", "8dd7d5296a650fac7319bce665a6a53c"}, new String[]{"SCPH-5501/SCPH-7003 (Version 3.0 11/18/96 A)", "490f666e1afb15b7362b406ed1cea246"}, new String[]{"SCPH-5502/SCPH-5552 (Version 3.0 01/06/97 E)", "32736f17079d0b2b7024407c39bd3050"}, new String[]{"SCPH-7000/SCPH-9000 (Version 4.0 08/18/97 J)", "8e4c14f567745eff2f0408c8129f72a6"}, new String[]{"SCPH-7001/SCPH-7501/SCPH-7503/SCPH-9001 (Version 4.1 12/16/97 A)", "1e68c231d0896b7eadcad1d7d8e76129"}, new String[]{"SCPH-7002/SCPH-7502/SCPH-9002 (Version 4.1 12/16/97 E)", "b9d9a0286c33dc6b7237bb13cd46fdee"}, new String[]{"SCPH-100 (Version 4.3 03/11/00 J)", "8abc1b549a4a80954addc48ef02c4521"}, new String[]{"SCPH-102 (Version 4.4 03/24/00 E)", "b10f5e0e3d9eb60e5159690680b1e774"}, new String[]{"SCPH-101 (Version 4.5 05/25/00 A)", "6e3735ff4c7dc899ee98981385f6f3d0"}, new String[]{"SCPH-102 (Version 4.5 05/25/00 E)", "de93caec13d1a141a40a79f5c86168d6"}};
    String biosnamefound = "";
    Controller mController = null;
    int mogapad = -1;
    final MogaControllerListener mListener = new MogaControllerListener();
    private BroadcastReceiver stateCallback = new BroadcastReceiver() { // from class: com.epsxe.ePSXe.ePSXeNative.25
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() != null && "EPSXE-IME".equals(intent.getStringExtra("com.hexad.bluezime.sessionid"))) {
                if (intent.getAction().equals("com.hexad.bluezime.config")) {
                    Toast.makeText(ePSXeNative.this, "Bluez-IME version " + intent.getIntExtra("version", 0), 0).show();
                    return;
                }
                if (intent.getAction().equals("com.hexad.bluezime.currentstate")) {
                    ePSXeNative.this.m_connected = intent.getBooleanExtra("connected", false);
                    for (int i = 0; i < 4; i++) {
                        if (ePSXeNative.this.bluezpad[i] != 0) {
                            Log.e("epsxenative", "init bluez pad ----> connect " + ePSXeNative.this.bluezaddr[i] + " driver " + ePSXeNative.this.bluezdriver[i]);
                            Intent serviceIntent2 = new Intent("com.hexad.bluezime.connect");
                            serviceIntent2.setClassName(ePSXeNative.BLUEZ_IME_PACKAGE, ePSXeNative.BLUEZ_IME_SERVICE);
                            serviceIntent2.putExtra("com.hexad.bluezime.sessionid", "EPSXE-IME");
                            serviceIntent2.putExtra("address", ePSXeNative.this.bluezaddr[i]);
                            serviceIntent2.putExtra("driver", ePSXeNative.this.bluezdriver[i]);
                            ePSXeNative.this.startService(serviceIntent2);
                            return;
                        }
                    }
                    return;
                }
                if (intent.getAction().equals("com.hexad.bluezime.connected")) {
                    ePSXeNative.this.m_connected = true;
                    return;
                }
                if (intent.getAction().equals("com.hexad.bluezime.disconnected")) {
                    ePSXeNative.this.m_connected = false;
                } else if (intent.getAction().equals("com.hexad.bluezime.error")) {
                    Toast.makeText(ePSXeNative.this, "Error: " + intent.getStringExtra("message"), 0).show();
                    ePSXeNative.this.m_connected = false;
                }
            }
        }
    };
    private BroadcastReceiver statusMonitor = new BroadcastReceiver() { // from class: com.epsxe.ePSXe.ePSXeNative.26
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() != null && "EPSXE-IME".equals(intent.getStringExtra("com.hexad.bluezime.sessionid"))) {
                if (intent.getAction().equals("com.hexad.bluezime.directionalchange")) {
                    int value = intent.getIntExtra("value", 0);
                    int direction = intent.getIntExtra("direction", 100);
                    if (value < -127) {
                        value = -127;
                    } else if (value > 127) {
                        value = 127;
                    }
                    switch (direction) {
                        case 0:
                            ePSXeNative.this.bluezxd[0][0] = value;
                            break;
                        case 1:
                            ePSXeNative.this.bluezyd[0][0] = value;
                            break;
                        case 2:
                            ePSXeNative.this.bluezxd[0][1] = value;
                            break;
                        case 3:
                            ePSXeNative.this.bluezyd[0][1] = value;
                            break;
                    }
                    if (ePSXeNative.this.emuStatus == 1) {
                        ePSXeNative.this.f157e.setpadanalog(0, 0, ePSXeNative.this.bluezxd[0][0], ePSXeNative.this.bluezyd[0][0]);
                        ePSXeNative.this.f157e.setpadanalog(0, 1, ePSXeNative.this.bluezxd[0][1], ePSXeNative.this.bluezyd[0][1]);
                        return;
                    }
                    return;
                }
                if (intent.getAction().equals("com.hexad.bluezime.keypress")) {
                    int key = intent.getIntExtra("key", 0);
                    int action = intent.getIntExtra("action", 100);
                    switch (key) {
                        case 19:
                            if (action == 0) {
                                int[] iArr = ePSXeNative.this.pushedButtons;
                                iArr[0] = iArr[0] | 4096;
                                break;
                            } else {
                                int[] iArr2 = ePSXeNative.this.pushedButtons;
                                iArr2[0] = iArr2[0] & (-4097);
                                break;
                            }
                        case 20:
                            if (action == 0) {
                                int[] iArr3 = ePSXeNative.this.pushedButtons;
                                iArr3[0] = iArr3[0] | 16384;
                                break;
                            } else {
                                int[] iArr4 = ePSXeNative.this.pushedButtons;
                                iArr4[0] = iArr4[0] & (-16385);
                                break;
                            }
                        case 21:
                            if (action == 0) {
                                int[] iArr5 = ePSXeNative.this.pushedButtons;
                                iArr5[0] = iArr5[0] | 32768;
                                break;
                            } else {
                                int[] iArr6 = ePSXeNative.this.pushedButtons;
                                iArr6[0] = iArr6[0] & (-32769);
                                break;
                            }
                        case 22:
                            if (action == 0) {
                                int[] iArr7 = ePSXeNative.this.pushedButtons;
                                iArr7[0] = iArr7[0] | 8192;
                                break;
                            } else {
                                int[] iArr8 = ePSXeNative.this.pushedButtons;
                                iArr8[0] = iArr8[0] & (-8193);
                                break;
                            }
                        case 96:
                            if (action == 0) {
                                int[] iArr9 = ePSXeNative.this.pushedButtons;
                                iArr9[0] = iArr9[0] | 64;
                                break;
                            } else {
                                int[] iArr10 = ePSXeNative.this.pushedButtons;
                                iArr10[0] = iArr10[0] & (-65);
                                break;
                            }
                        case 97:
                            if (action == 0) {
                                int[] iArr11 = ePSXeNative.this.pushedButtons;
                                iArr11[0] = iArr11[0] | 32;
                                break;
                            } else {
                                int[] iArr12 = ePSXeNative.this.pushedButtons;
                                iArr12[0] = iArr12[0] & (-33);
                                break;
                            }
                        case 99:
                            if (action == 0) {
                                int[] iArr13 = ePSXeNative.this.pushedButtons;
                                iArr13[0] = iArr13[0] | 128;
                                break;
                            } else {
                                int[] iArr14 = ePSXeNative.this.pushedButtons;
                                iArr14[0] = iArr14[0] & (-129);
                                break;
                            }
                        case 100:
                            if (action == 0) {
                                int[] iArr15 = ePSXeNative.this.pushedButtons;
                                iArr15[0] = iArr15[0] | 16;
                                break;
                            } else {
                                int[] iArr16 = ePSXeNative.this.pushedButtons;
                                iArr16[0] = iArr16[0] & (-17);
                                break;
                            }
                        case 102:
                            if (action == 0) {
                                int[] iArr17 = ePSXeNative.this.pushedButtons;
                                iArr17[0] = iArr17[0] | 4;
                                break;
                            } else {
                                int[] iArr18 = ePSXeNative.this.pushedButtons;
                                iArr18[0] = iArr18[0] & (-5);
                                break;
                            }
                        case 103:
                            if (action == 0) {
                                int[] iArr19 = ePSXeNative.this.pushedButtons;
                                iArr19[0] = iArr19[0] | 8;
                                break;
                            } else {
                                int[] iArr20 = ePSXeNative.this.pushedButtons;
                                iArr20[0] = iArr20[0] & (-9);
                                break;
                            }
                        case 104:
                            if (action == 0) {
                                int[] iArr21 = ePSXeNative.this.pushedButtons;
                                iArr21[0] = iArr21[0] | 1;
                                break;
                            } else {
                                int[] iArr22 = ePSXeNative.this.pushedButtons;
                                iArr22[0] = iArr22[0] & (-2);
                                break;
                            }
                        case 105:
                            if (action == 0) {
                                int[] iArr23 = ePSXeNative.this.pushedButtons;
                                iArr23[0] = iArr23[0] | 2;
                                break;
                            } else {
                                int[] iArr24 = ePSXeNative.this.pushedButtons;
                                iArr24[0] = iArr24[0] & (-3);
                                break;
                            }
                        case 108:
                            if (action == 0) {
                                int[] iArr25 = ePSXeNative.this.pushedButtons;
                                iArr25[0] = iArr25[0] | 2048;
                                break;
                            } else {
                                int[] iArr26 = ePSXeNative.this.pushedButtons;
                                iArr26[0] = iArr26[0] & (-2049);
                                break;
                            }
                        case 109:
                            if (action == 0) {
                                int[] iArr27 = ePSXeNative.this.pushedButtons;
                                iArr27[0] = iArr27[0] | 256;
                                break;
                            } else {
                                int[] iArr28 = ePSXeNative.this.pushedButtons;
                                iArr28[0] = iArr28[0] & (-257);
                                break;
                            }
                    }
                    if (ePSXeNative.this.emuStatus == 1) {
                        ePSXeNative.this.f157e.setPadDataMultitap(ePSXeNative.this.pushedButtons[0], ePSXeNative.this.pushedButtons[1], ePSXeNative.this.pushedButtons[2], ePSXeNative.this.pushedButtons[3]);
                    }
                }
            }
        }
    };
    int[][] bluezxd = {new int[]{0, 0}, new int[]{0, 0}, new int[]{0, 0}, new int[]{0, 0}};
    int[][] bluezyd = {new int[]{0, 0}, new int[]{0, 0}, new int[]{0, 0}, new int[]{0, 0}};
    String[] bluezaddr = {"", "", "", ""};
    String[] bluezdriver = {"", "", "", ""};
    int[] bluezpad = {0, 0, 0, 0};
    int bluezenabled = 0;
    private boolean m_connected = false;
    int[] virtualPadId = {-1, -1, -1, -1, -1, -1, -1, -1};
    int[][] virtualPadPos = {new int[]{0, 0, 360, 360}, new int[]{606, 0, 966, 360}};

    /* renamed from: xd */
    int[] f158xd = {0, 0};

    /* renamed from: yd */
    int[] f159yd = {0, 0};

    native int RegisterThis();

    public native byte[] getGSName(int i);

    public native void initVibration();

    public void IsoErrDialog(String isoName) {
        AlertDialog builder = new AlertDialog.Builder(this).setTitle("Error running game").setCancelable(true).setIcon(android.R.drawable.ic_dialog_info).setPositiveButton(R.string.dialog_action_ok, (DialogInterface.OnClickListener) null).setMessage("Error loading " + isoName + ", possible errors: \n 1) missing data rom img/iso/bin \n 2) .7z/.ape format NOT supported!!!").create();
        builder.show();
    }

    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:173:0x04d8 -> B:69:0x042c). Please report as a decompilation issue!!! */
    public void runIso(String isoName, int slot) {
        String pathlibFolder;
        getWindow().takeSurface(null);
        RegisterThis();
        if (this.mePSXeReadPreferences == null) {
            this.mePSXeReadPreferences = new ePSXeReadPreferences(this);
        }
        if (isoName.equals("___RUNBIOS___")) {
            this.currentPath = this.sdCardPath;
        } else {
            File f = new File(isoName);
            this.currentPath = f.getParent();
        }
        Log.e("epsxenative", "currentpath " + this.currentPath);
        if (this.mePSXeReadPreferences.getCpuMME() == 0) {
            this.emu_enable_neon = 0;
        }
        if (this.emu_enable_x86 == 0) {
            this.f157e = new libepsxe(this.emu_enable_neon);
        } else {
            this.f157e = new libepsxe(1);
        }
        String bios = this.mePSXeReadPreferences.getBios();
        Log.e("epsxenative", "mePSXeReadPreferences.getBios " + bios);
        Log.e("epsxenative", "sdCardPath " + this.sdCardPath);
        this.f157e.setBios(bios);
        int hlebios = this.mePSXeReadPreferences.getBiosHle();
        if (hlebios == 2) {
            hlebios = 0;
        }
        if (hlebios == 1 && isoName.equals("___RUNBIOS___")) {
            hlebios = 0;
        }
        this.f157e.setBiosHle(hlebios);
        this.hlebiosrunning = hlebios;
        this.f157e.setSdCardPath(this.sdCardPath);
        this.f157e.setBootMode(this.mePSXeReadPreferences.getmiscbootmode());
        this.f157e.setInterpreter(this.mePSXeReadPreferences.getDebugInterpreter());
        this.f157e.setGteaccurateH(this.mePSXeReadPreferences.getGpuperspectivecorrection());
        this.emu_menu2_iresolution = this.mePSXeReadPreferences.getGpuIresolution();
        String mcr1 = this.mePSXeReadPreferences.getMemcard1();
        String mcr2 = this.mePSXeReadPreferences.getMemcard2();
        int mcrfilemode = this.mePSXeReadPreferences.getMemcardFileMode();
        if (mcrfilemode > 1) {
            mcrfilemode = 0;
        }
        Log.e("epsxe", "memcard1 " + mcr1);
        Log.e("epsxe", "memcard2 " + mcr2);
        Log.e("epsxe", "memcardmode " + this.mePSXeReadPreferences.getMemcardMode());
        if (mcr1.equals("default")) {
            this.f157e.setMemcard1("NULL");
        } else {
            this.f157e.setMemcard1(mcr1);
        }
        if (mcr2.equals("default")) {
            this.f157e.setMemcard2("NULL");
        } else {
            this.f157e.setMemcard2(mcr2);
        }
        this.f157e.setMemcardMode(this.mePSXeReadPreferences.getMemcardMode());
        if (hlebios == 1) {
            this.f157e.setMemcardFileMode(1);
        } else {
            this.f157e.setMemcardFileMode(mcrfilemode);
        }
        Log.e("epsxenative", "getVideoRenderer " + this.mePSXeReadPreferences.getVideoRenderer());
        this.emu_renderer = this.mePSXeReadPreferences.getVideoRenderer();
        if ((this.emu_renderer == 4 || this.emu_renderer == 5) && Integer.parseInt(Build.VERSION.SDK) < 17) {
            this.emu_renderer = 1;
        }
        this.emu_screen_orientation = this.mePSXeReadPreferences.getScreenOrientation();
        this.emu_screen_vrmode = this.mePSXeReadPreferences.getScreenVrmode();
        this.emu_screen_vrdistorsion = this.mePSXeReadPreferences.getScreenVrdistorsion();
        this.emu_screen_ratio = this.mePSXeReadPreferences.getScreenRatio();
        if (this.emu_renderer == 0) {
            this.emu_menu2_iresolution = 1;
        }
        this.f157e.setgpuiresolution(this.emu_menu2_iresolution);
        initVRMode();
        if (this.emu_renderer == 2) {
            try {
                pathlibFolder = getPackageManager().getPackageInfo("com.epsxe.opengl", 0).applicationInfo.dataDir + "/lib";
            } catch (Exception e) {
                pathlibFolder = "";
            }
            String str = pathlibFolder + "/libopenglplugin.so";
            File f2 = new File(pathlibFolder);
            if (f2.exists()) {
                this.f157e.setPluginMode(this.mePSXeReadPreferences.getGpuNamePref());
                this.f157e.setGpu(pathlibFolder);
                this.gpuPluginName = pathlibFolder;
            } else {
                String gpufile = this.mePSXeReadPreferences.getGpu();
                File f22 = new File(gpufile);
                if (f22.exists()) {
                    String gpufiletmp = getFilesDir() + "/libopenglextTMP.so";
                    File gpuTmp = new File(getFilesDir(), "/libopenglextTMP.so");
                    if (gpuTmp.exists()) {
                        gpuTmp.delete();
                    }
                    copyFile(f22, gpuTmp);
                    if (gpuTmp.exists()) {
                        this.f157e.setPluginMode(this.mePSXeReadPreferences.getGpuNamePref());
                        this.f157e.setGpu(gpufiletmp);
                        this.gpuPluginName = gpufiletmp;
                    } else {
                        this.emu_renderer = 1;
                        Toast.makeText(this, R.string.main_gpunotfound, 1).show();
                    }
                } else {
                    this.emu_renderer = 1;
                    Toast.makeText(this, R.string.main_gpunotfound, 1).show();
                }
            }
        }
        if (this.emu_renderer == 0 || this.emu_renderer == 1 || this.emu_renderer == 3) {
            this.f157e.setGpu("GPUCORE");
            this.fps = this.f157e.loadepsxe(isoName, slot);
            if (!isoName.equals("___RUNBIOS___")) {
                if (this.fps == -1) {
                    IsoErrDialog(isoName);
                    return;
                } else if (this.fps > 60) {
                    Toast.makeText(this, R.string.main_systemcnf, 1).show();
                    this.fps -= 100;
                }
            } else {
                this.fps = 60;
            }
        }
        getWindow().setFlags(128, 128);
        if (this.mePSXeView == null) {
            try {
                if (this.emu_renderer == 0) {
                    this.ePSXeViewType = Class.forName("com.epsxe.ePSXe.ePSXeViewSoft");
                } else if (this.emu_renderer == 1 || this.emu_renderer == 3) {
                    this.ePSXeViewType = Class.forName("com.epsxe.ePSXe.ePSXeViewGL");
                } else if (this.emu_renderer == 2) {
                    this.ePSXeViewType = Class.forName("com.epsxe.ePSXe.ePSXeViewGLext");
                }
            } catch (ClassNotFoundException e2) {
                finish();
            }
            try {
                Constructor[] c = this.ePSXeViewType.getConstructors();
                this.mePSXeView = (ePSXeView) c[0].newInstance(getApplication(), null, 0);
            } catch (Exception e3) {
                finish();
            }
            if (hlebios == 1) {
                this.mePSXeView.setbiosmsg(true);
            }
            this.mePSXeView.setscreendepth(this.mePSXeReadPreferences.getScreenDepth(false));
            this.f157e.setcustomfps(this.mePSXeReadPreferences.getCpucustomfps());
            this.mePSXeView.setePSXeLib(this.f157e, this.emu_renderer, 0);
            if (!this.mePSXeView.setIsoName(this.mIsoName, slot, this.gpuPluginName)) {
                Toast.makeText(this, "Error! GPU plugin is corrupted or wrong arch, please download again the plugin", 1).show();
                return;
            }
            this.mePSXeView.setfps(this.fps);
            this.mePSXeView.setscreenvrmode(this.emu_screen_vrmode, this.emu_screen_vrdistorsion);
            this.mePSXeView.setinputvibration(this.mePSXeReadPreferences.getInputVibrate(), this.mePSXeReadPreferences.getInputVibrate2());
            this.mePSXeView.setinputalpha(this.mePSXeReadPreferences.getInputAlpha());
            this.mePSXeView.setframeskip(this.mePSXeReadPreferences.getCpuFrameSkip());
            this.mePSXeView.setshowfps(this.mePSXeReadPreferences.getCpuShowFPS());
            if (this.emu_screen_orientation == 1) {
                this.emu_menu2_touchscreen = this.mePSXeReadPreferences.getInputPaintPadPorMode(false);
            } else {
                this.emu_menu2_touchscreen = this.mePSXeReadPreferences.getInputPaintPadMode(false);
            }
//            this.mePSXeView.setinputpaintpadmode(this.emu_menu2_touchscreen, this.mePSXeReadPreferences.getInputPaintPadMode2());
            this.mePSXeView.setinputpaintpadmode(0, this.mePSXeReadPreferences.getInputPaintPadMode2());
            this.mePSXeView.setinputskinname(this.mePSXeReadPreferences.getSkin());
            this.mePSXeView.setinputpadtype(this.mePSXeReadPreferences.getInputPadType(), this.mePSXeReadPreferences.getInputPadType2());
            if (this.emu_screen_orientation == 2) {
                this.mePSXeView.setscreenorientation(0);
            } else {
                this.mePSXeView.setscreenorientation(this.emu_screen_orientation);
            }
            if (this.emu_screen_orientation == 1) {
                this.mePSXeView.setportraitmode(this.mePSXeReadPreferences.getInputPaintPadPorMode(false));
            }
            if (this.emu_screen_ratio == 2) {
                this.f157e.setWidescreen(1);
                this.mePSXeView.setscreenratio(0);
            } else {
                this.f157e.setWidescreen(0);
                this.mePSXeView.setscreenratio(this.emu_screen_ratio);
            }
            this.mePSXeView.setscreenblackbands(this.mePSXeReadPreferences.getScreenBlackbands());
            this.emu_player_mode = this.mePSXeReadPreferences.getInputPlayerMode();
            if (this.emu_player_mode == 10) {
                this.f157e.setPadModeMultitap(1);
            }
            if (this.emu_player_mode == 2 || this.emu_player_mode == 3) {
                this.emu_player_mode = 1;
            }
            this.mePSXeView.setplayermode(this.emu_player_mode);
            this.mePSXeView.setvideodither(this.mePSXeReadPreferences.getVideoDither());
            this.mePSXeView.setvideofilterhw(this.mePSXeReadPreferences.getVideoFilterhw());
            this.f157e.setVibration(0, this.mePSXeReadPreferences.getInputVibrationPSX());
            this.f157e.setVibration(1, this.mePSXeReadPreferences.getInputVibrationPSX2());
            this.f157e.setDmachaincore(this.mePSXeReadPreferences.getGpuDmachaincore());
            this.mePSXeView.setsoundlatency(this.mePSXeReadPreferences.getSoundLatency());
            int mode1 = this.mePSXeReadPreferences.getInputPadMode();
            int mode2 = this.mePSXeReadPreferences.getInputPad2Mode();
            int analog1 = 1;
            int analog2 = 1;
            if (this.emu_padType != 0) {
                if (mode1 == 4 && (this.emu_padType & 6) == 0) {
                    analog1 = 0;
                }
                if (mode2 == 4 && (this.emu_padType & 6) == 0) {
                    analog2 = 0;
                }
                if (mode1 == 3 && (this.emu_padType & 64) == 0) {
                    mode1 = 1;
                }
            }
            if (isoName.equals("___RUNBIOS___") && mode1 == 4) {
                analog1 = 0;
            }
            if (this.emu_screen_orientation == 1 && mode1 == 4) {
                analog1 = 0;
            }
            this.mePSXeView.setinputpadmode(mode1, mode2, analog1, analog2);
            this.emu_menu2_autofirefreq = this.mePSXeReadPreferences.getInputAutofirefreq();
            this.emu_menu2_autofirebutton = this.mePSXeReadPreferences.getInputAutofirebutton();
            if (this.emu_menu2_autofirefreq != 0) {
                this.f157e.enableautofire(0, 0, this.emu_menu2_autofirebutton, this.emu_menu2_autofirefreq, 1);
                this.emu_menu2_autofireenabled = 1;
            }
            this.emu_menu2_brightnessprofile = this.mePSXeReadPreferences.getGpuBrighttnessprofile();
            this.emu_menu2_dynamicpad = this.mePSXeReadPreferences.getInputDynamicpad();
            this.emu_menu2_dynamicaction = this.mePSXeReadPreferences.getInputDynamicaction();
            this.mePSXeView.setdynamicpad(this.emu_menu2_dynamicpad);
            this.mePSXeView.setdynamicaction(this.emu_menu2_dynamicaction);
            this.f157e.setgpubrightnessprofile(this.emu_menu2_brightnessprofile);
            this.f157e.setgpuoverscantop(this.mePSXeReadPreferences.getGpuOverscantop());
            this.f157e.setgpuoverscanbottom(this.mePSXeReadPreferences.getGpuOverscanbottom());
            this.emu_ui_resume_dialog = this.mePSXeReadPreferences.getUiresumedialog();
            this.emu_autosave = this.mePSXeReadPreferences.getMiscAutosave();
            if (this.mePSXeReadPreferences.getCpuMME() == 0) {
            }
            for (int p = 0; p < 4; p++) {
                for (int i = 0; i < 21; i++) {
                    this.keycodes[p][i] = this.mePSXeReadPreferences.getButtonKeycode(p, i);
                    Log.e("epsxekey", "keycode[" + p + "][" + i + "] = " + this.keycodes[p][i]);
                }
            }
            for (int i2 = 0; i2 < this.keycodesextra.length; i2++) {
                this.keycodesextra[i2] = this.mePSXeReadPreferences.getButtonKeycodeextra(i2);
            }
            initJoysticks();
            if (this.emu_pad_type[0] == 1 && this.emu_pad_type[1] == 1) {
                this.emu_pad_type[0] = 0;
            }
            this.mePSXeView.setinputpadtype(this.emu_pad_type[0], this.emu_pad_type[1]);
            if (this.bluezenabled == 1) {
                bluezStart();
            }
            if (this.mogapad != -1) {
                mogaStart();
            }
            if (this.snapRestoring.booleanValue()) {
                this.mePSXeView.setsnaprestoring(true);
                this.snapRestoring = false;
            }
            if (!isoName.equals("___RUNBIOS___")) {
                this.mePSXeReadPreferences.setIsoPath(this.currentPath);
            }
        }
        initSound();
        initScreenOrientation();
        setContentView();
        initVibration();
        this.emuStatus = 1;
        if (this.mIsoName.equals("___RUNBIOS___")) {
            this.emuStatus = 2;
        }
        if (this.emu_autosave == 1 && this.emuStatus == 1 && this.emu_autoload_disable == 0) {
            if (this.emu_renderer == 2) {
                this.mHandlerAL.postDelayed(this.mLaunchTaskAL, 2000L);
            } else {
                CreateSstateDialog(this.f157e.getCode(), 0, 1);
            }
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
            this.emu_screen_ratio = 1;
        }
    }

    private void setContentView() {
        if (this.emu_renderer == 0) {
            setContentView((ePSXeViewSoft) this.mePSXeView);
        } else if (this.emu_renderer == 1 || this.emu_renderer == 3) {
            setContentView((ePSXeViewGL) this.mePSXeView);
        } else {
            setContentView((ePSXeViewGLext) this.mePSXeView);
        }
    }

    private void initSound() {
        if (this.mePSXeSound == null) {
            this.mePSXeSound = new ePSXeSound();
            this.mePSXeSound.setePSXeLib(this.f157e);
            Log.e("epsxenative", "getSoundQA " + this.mePSXeReadPreferences.getSoundQA());
            this.mePSXeSound.setsoundqa(this.mePSXeReadPreferences.getSoundQA());
            this.mePSXeSound.setsoundlatency(this.mePSXeReadPreferences.getSoundLatency());
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

    public void initJoysticks() {
        String label;
        int osVersion = Integer.parseInt(Build.VERSION.SDK);
        if (osVersion >= 12) {
            int[] deviceIds = InputDevice.getDeviceIds();
            for (int i = 0; i < 4; i++) {
                this.analogpadidString[i] = this.mePSXeReadPreferences.getPadAnalogPadID(i + 1);
                Log.e("epsxekey", "getPadAnalogPadID(" + (i + 1) + ") = " + this.analogpadidString[i]);
                this.analogrange[i] = this.mePSXeReadPreferences.getPadAnalogRange(i + 1);
                this.analogleftx[i] = this.mePSXeReadPreferences.getPadAnalogLeftx(i + 1);
                this.analoglefty[i] = this.mePSXeReadPreferences.getPadAnalogLefty(i + 1);
                this.analogrightx[i] = this.mePSXeReadPreferences.getPadAnalogRightx(i + 1);
                this.analogrighty[i] = this.mePSXeReadPreferences.getPadAnalogRighty(i + 1);
                this.analogl2[i] = this.mePSXeReadPreferences.getPadAnalogL2(i + 1);
                this.analogr2[i] = this.mePSXeReadPreferences.getPadAnalogR2(i + 1);
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
                if (!this.analogpadidString[i].equals("none")) {
                    int in = this.analogpadidString[i].indexOf("###");
                    if (in != -1) {
                        label = this.analogpadidString[i].substring(3, this.analogpadidString[i].indexOf("###", 4));
                    } else {
                        label = "" + this.analogpadidString[i];
                    }
                    if (this.mePSXeReadPreferences.getPadAnalogProfile(i + 1).equals("moga")) {
                        this.analogdpadfromanalog[i] = 1;
                    }
                    if (label.equals("bluez")) {
                        this.bluezdriver[i] = this.mePSXeReadPreferences.getPadAnalogProfile(i + 1);
                        if (!this.bluezdriver[i].equals("0")) {
                            this.bluezenabled = 1;
                            this.bluezaddr[i] = this.analogpadidString[i].substring(11, this.analogpadidString[i].indexOf("###", 11));
                            this.bluezpad[i] = 1;
                            Log.e("epsxe", "init bluez: addr " + this.bluezaddr[i] + " driver " + this.bluezdriver[i]);
                        }
                    } else if (label.equals("moganative")) {
                        this.mogapad = i;
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
                                if ((16777232 & sources) == 16777232 || (sources & InputDeviceCompat.SOURCE_GAMEPAD) == 1025) {
                                    String s = device.toString();
                                    int val = s.indexOf(label);
                                    if (val != -1) {
                                        Boolean used = false;
                                        for (int k = 0; k < i; k++) {
                                            if (deviceIds[j] == this.analogpadid[k]) {
                                                used = true;
                                            }
                                        }
                                        if (!used.booleanValue()) {
                                            this.analogpadid[i] = deviceIds[j];
                                            Log.e("epsxekey", "joystick(" + i + ") found id=" + this.analogpadid[i]);
                                            break;
                                        }
                                    } else {
                                        continue;
                                    }
                                }
                            }
                            j++;
                        }
                        if (this.analogpadid[i] == -1) {
                            Log.e("epsxekey", "joystick" + i + " not found " + label);
                        }
                    }
                }
            }
        }
    }

    public void BiosDialog() {
        TextView message = new TextView(this);
        SpannableString s = new SpannableString(getText(R.string.bios_dialog_message));
        Linkify.addLinks(s, 1);
        message.setText(s);
        message.setMovementMethod(LinkMovementMethod.getInstance());
        AlertDialog builder = new AlertDialog.Builder(this)
                .setTitle(R.string.bios_dialog_title)
                .setCancelable(true)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton(getString(R.string.dialog_action_ok), (DialogInterface.OnClickListener) null)
                .setView(message)
                .create();
        builder.show();
    }

    public void BiosFoundDialog(String BiosName) {
        TextView message = new TextView(this);
        SpannableString s = new SpannableString(getString(R.string.main_psxbios) + BiosName + "\n" + ((Object) getText(R.string.bios_found_dialog_message)));
        Linkify.addLinks(s, 1);
        message.setText(s);
        message.setMovementMethod(LinkMovementMethod.getInstance());
        AlertDialog builder = new AlertDialog.Builder(this).setTitle(R.string.bios_found_dialog_title).setCancelable(true).setIcon(android.R.drawable.ic_dialog_info).setPositiveButton(getString(R.string.dialog_action_ok), (DialogInterface.OnClickListener) null).setView(message).create();
        builder.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int check_bios(int v) {
        boolean bFound = true;
        try {
            this.mePSXeReadPreferences = new ePSXeReadPreferences(this);
            int hlebios = this.mePSXeReadPreferences.getBiosHle();
            Log.e("epsxenative", "getBios " + this.mePSXeReadPreferences.getBios() + " hle: " + hlebios);
            File f = new File(this.mePSXeReadPreferences.getBios());
            if (!f.exists()) {
                bFound = false;
            } else if (f.length() != 524288) {
                bFound = false;
            }
            if (v == 0 && hlebios == 1) {
                bFound = true;
            }
            if (!bFound) {
                new ScansdcardBiosTask(this).execute(Environment.getExternalStorageDirectory().getAbsolutePath());
                Log.e("bios", "check_bios not found pre dialog");
                Log.e("bios", "check_bios not found post dialog");
                return -1;
            }
            this.currentPath = this.mePSXeReadPreferences.getIsoPath();
            Log.e("epsxenative", "currentPath from save " + this.mePSXeReadPreferences.getIsoPath());
            return 0;
        } catch (Exception e) {
            return -1;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void restoreResumeGame() {
        File f = new File(ContextCompat.getDataDir(this), "epsxe/sstates/savetmp");
        StringBuilder text = new StringBuilder();
        StringBuilder slot = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            this.mIsoSlot = 0;
            String line = br.readLine();
            if (line != null) {
                text.append(line);
            }
            String line2 = br.readLine();
            if (line2 != null) {
                slot.append(line2);
                this.mIsoSlot = Integer.parseInt(slot.toString());
            }
            this.mIsoName = text.toString();
            f.delete();
            if (check_bios(0) != -1) {
                File isoName = new File(this.mIsoName);
                if (isoName.exists()) {
                    Log.e("epsxenative", "restoring " + this.mIsoName);
                    Intent myIntent = new Intent(this, (Class<?>) FileChooser.class);
                    myIntent.putExtra("com.epsxe.ePSXe.fcMode", "EXEC_ISO");
                    myIntent.putExtra("com.epsxe.ePSXe.isoName", this.mIsoName);
                    myIntent.putExtra("com.epsxe.ePSXe.isoSlot", "" + this.mIsoSlot);
                    startActivity(myIntent);
                    finish();
                }
            }
        } catch (IOException e) {
        }
    }

    private void alertdialog_restoreGame() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(R.string.main_restoregame);
        alertDialog.setMessage(getString(R.string.main_wantquitrestore));
        alertDialog.setButton(getString(R.string.main_noquitrestore), new DialogInterface.OnClickListener() { // from class: com.epsxe.ePSXe.ePSXeNative.3
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                File f = new File(ContextCompat.getDataDir(ePSXeNative.this), "epsxe/sstates/savetmp");
                f.delete();
            }
        });
        alertDialog.setButton2(getString(R.string.main_yesquitrestore), new DialogInterface.OnClickListener() { // from class: com.epsxe.ePSXe.ePSXeNative.4
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                ePSXeNative.this.restoreResumeGame();
            }
        });
        alertDialog.setIcon(R.drawable.icon);
        alertDialog.show();
    }

    private void check_savetmp_snap() {
        File f = new File(ContextCompat.getDataDir(this), "epsxe/sstates/savetmp");
        if (f.exists()) {
            if (this.emu_ui_resume_dialog != 0) {
                alertdialog_restoreGame();
            } else {
                restoreResumeGame();
            }
        }
    }

    private void savetmp_snapshot(String isoName, int slot) {
        try {
            File root = new File(ContextCompat.getDataDir(this), "epsxe/sstates/");
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, "savetmp");
            FileWriter writer = new FileWriter(gpxfile);
            writer.append((CharSequence) isoName);
            writer.append((CharSequence) ("\n" + slot));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void add_quickButtons() {
        this.button_run_game = (Button) findViewById(R.id.button_run_game);
        this.button_run_game.setOnClickListener(new View.OnClickListener() { // from class: com.epsxe.ePSXe.ePSXeNative.5
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (ePSXeNative.this.emuStatus == 0 && ePSXeNative.this.check_bios(0) != -1) {
                    Log.e("epsxenative", "getMiscBrowsermode " + ePSXeNative.this.mePSXeReadPreferences.getMiscBrowsermode(false));
                    ePSXeNative.this.emu_browser_mode = ePSXeNative.this.mePSXeReadPreferences.getMiscBrowsermode(false);
                    Intent myIntent = new Intent(ePSXeNative.this, (Class<?>) FileChooser.class);
                    myIntent.putExtra("com.epsxe.ePSXe.fcMode", "SELECT_ISO");
                    myIntent.putExtra("com.epsxe.ePSXe.isoPath", ePSXeNative.this.currentPath);
                    myIntent.putExtra("com.epsxe.ePSXe.browserMode", "" + ePSXeNative.this.emu_browser_mode);
                    ePSXeNative.this.startActivity(myIntent);
                    ePSXeNative.this.finish();
                }
            }
        });
        this.button_run_bios = (Button) findViewById(R.id.button_run_bios);
        this.button_run_bios.setOnClickListener(new View.OnClickListener() { // from class: com.epsxe.ePSXe.ePSXeNative.6
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (ePSXeNative.this.emuStatus == 0 && ePSXeNative.this.check_bios(1) != -1) {
                    Intent myIntent = new Intent(ePSXeNative.this, (Class<?>) FileChooser.class);
                    myIntent.putExtra("com.epsxe.ePSXe.fcMode", "RUN_BIOS");
                    myIntent.putExtra("com.epsxe.ePSXe.isoPath", ePSXeNative.this.currentPath);
                    ePSXeNative.this.startActivity(myIntent);
                    ePSXeNative.this.finish();
                }
            }
        });
        this.button_preferences = (Button) findViewById(R.id.button_preferences);
        this.button_preferences.setOnClickListener(new View.OnClickListener() { // from class: com.epsxe.ePSXe.ePSXeNative.7
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                Intent myIntent = new Intent(ePSXeNative.this, (Class<?>) ePSXePreferences.class);
                ePSXeNative.this.startActivity(myIntent);
                ePSXeNative.this.finish();
            }
        });
        this.button_help = (Button) findViewById(R.id.button_help);
        this.button_help.setOnClickListener(new View.OnClickListener() { // from class: com.epsxe.ePSXe.ePSXeNative.8
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                Intent myIntent = new Intent(ePSXeNative.this, (Class<?>) ePSXeHelp.class);
                ePSXeNative.this.startActivity(myIntent);
                ePSXeNative.this.finish();
            }
        });
        this.button_quit = (Button) findViewById(R.id.button_quit);
        this.button_quit.setOnClickListener(new View.OnClickListener() { // from class: com.epsxe.ePSXe.ePSXeNative.9
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                ePSXeNative.this.finish();
            }
        });
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
        }
    }

    public void initLogs() {
        try {
            File filename = new File(ContextCompat.getDataDir(this) + "/epsxe/epsxe.log");
            filename.createNewFile();
            String cmd = "logcat -d -f " + filename.getAbsolutePath();
            Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override // android.app.NativeActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        initSdCard();
        this.f156d = new libdetect();
        this.emu_enable_neon = this.f156d.isNeon();
        Log.e("epsxenative", "neon detected: " + this.emu_enable_neon);
        this.emu_enable_x86 = this.f156d.isX86();
        Log.e("epsxenative", "x86 detected: " + this.emu_enable_x86);
        requestWindowFeature(1);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(1024, 1024);
        if (this.mePSXeReadPreferences == null) {
            this.mePSXeReadPreferences = new ePSXeReadPreferences(this);
        }
        String param = getIntent().getStringExtra("com.epsxe.ePSXe.isoName");
        String param2 = getIntent().getStringExtra("com.epsxe.ePSXe.snapRestore");
        String param3 = getIntent().getStringExtra("com.epsxe.ePSXe.isoSlot");
        String param4 = getIntent().getStringExtra("com.epsxe.ePSXe.gui");
        String param5 = getIntent().getStringExtra("com.epsxe.ePSXe.padType");
        if (this.emuStatus == 0) {
            int slot = 0;
            if (param != null && param.length() > 0) {
                this.mIsoName = param;
            }
            this.emu_autoload_disable = 0;
            if (param2 != null && param2.length() > 0) {
                this.snapRestoring = true;
                this.emu_autoload_disable = 1;
            }
            if (param3 != null && param3.length() > 0) {
                slot = Integer.parseInt(param3);
                this.mIsoSlot = slot;
            }
            if (param4 != null && param4.length() > 0) {
                this.emu_gui = Integer.parseInt(param4);
            }
            if (param5 != null && param5.length() > 0) {
                this.emu_padType = Integer.parseInt(param5);
            }
            if (this.mIsoName.length() > 0) {
                runIso(this.mIsoName, slot);
            }
            //check_savetmp_snap();
        }
    }

    @Override // android.app.NativeActivity, android.app.Activity
    protected void onPause() {
        super.onPause();
        if (this.mController != null) {
            this.mController.onPause();
        }
        if (this.emuStatus == 1) {
            this.mePSXeView.setSaveMode(3, this.emu_autosave);
            this.mePSXeSound.exit();
            this.emuStatus = 3;
            savetmp_snapshot(this.mIsoName, this.mIsoSlot);
            finish();
            return;
        }
        if (this.emuStatus == 2) {
            this.mePSXeSound.exit();
            this.emuStatus = 3;
            this.f157e.quit();
            finish();
        }
    }

    @Override // android.app.NativeActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
        if (this.mController != null) {
            this.mController.onResume();
        }
    }

    @Override // android.app.NativeActivity, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        if (this.mePSXeView != null) {
        }
        if (this.bluezenabled == 1) {
            bluezStop();
        }
        if (this.mogapad != -1) {
            mogaStop();
        }
    }

    @Override // android.app.NativeActivity, android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration newConfig) {
        Log.e("epsxenative", "onconfigurationchanged");
        super.onConfigurationChanged(newConfig);
    }

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, 1, 0, R.string.menu_rungame).setIcon(android.R.drawable.ic_menu_add);
        menu.add(0, 2, 0, R.string.menu_runbios).setIcon(android.R.drawable.ic_menu_directions);
        menu.add(0, 3, 0, R.string.menu_changedisc).setIcon(android.R.drawable.ic_menu_add);
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
        menu.clear();
        if (this.emuStatus == 0) {
            menu.add(0, 1, 0, R.string.menu_rungame).setIcon(android.R.drawable.ic_menu_add);
            menu.add(0, 2, 0, R.string.menu_runbios).setIcon(android.R.drawable.ic_menu_directions);
            menu.add(0, 6, 0, R.string.menu_preferences).setIcon(android.R.drawable.ic_menu_preferences);
            menu.add(0, 9, 0, R.string.menu_help).setIcon(android.R.drawable.ic_menu_help);
            menu.add(0, 7, 0, R.string.menu_about).setIcon(android.R.drawable.ic_menu_info_details);
            menu.add(0, 8, 0, R.string.menu_quit).setIcon(android.R.drawable.ic_menu_revert);
        } else if (this.emuStatus == 1) {
            if (1 != 0) {
                menu.add(0, 3, 0, R.string.menu_changedisc).setIcon(android.R.drawable.ic_menu_add);
                menu.add(0, 4, 0, R.string.menu_loadstate).setIcon(android.R.drawable.ic_menu_set_as);
                menu.add(0, 5, 0, R.string.menu_savestate).setIcon(android.R.drawable.ic_menu_save);
                menu.add(0, 14, 0, R.string.menu_framelimit).setIcon(android.R.drawable.ic_menu_add);
                menu.add(0, 15, 0, R.string.menu_cheat).setIcon(android.R.drawable.ic_menu_add);
                if (this.emu_renderer == 2) {
                    menu.add(0, 16, 0, R.string.menu_tools).setIcon(android.R.drawable.ic_menu_preferences);
                }
                if (this.emu_player_mode == 10) {
                    menu.add(0, 10, 0, R.string.menu_splitv).setIcon(android.R.drawable.ic_menu_rotate);
                    menu.add(0, 11, 0, R.string.menu_splith1).setIcon(android.R.drawable.ic_menu_rotate);
                    menu.add(0, 12, 0, R.string.menu_splith2).setIcon(android.R.drawable.ic_menu_rotate);
                }
            }
            menu.add(0, 8, 0, R.string.menu_quit).setIcon(android.R.drawable.ic_menu_revert);
        } else if (this.emuStatus == 2) {
            if (1 != 0) {
                menu.add(0, 14, 0, R.string.menu_framelimit).setIcon(android.R.drawable.ic_menu_add);
                if (this.emu_renderer == 2) {
                    menu.add(0, 16, 0, R.string.menu_tools).setIcon(android.R.drawable.ic_menu_preferences);
                }
            }
            menu.add(0, 8, 0, R.string.menu_quit).setIcon(android.R.drawable.ic_menu_revert);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    private void alertdialog_about() {
        String version = "X.X.X";
        try {
            version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("ePSXe", e.getMessage());
        }
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(getString(R.string.main_aboutepsxe));
        alertDialog.setMessage("ePSXe (Enhanced PSX Emulator) v." + version + "\n[http://www.epsxe.com]\n\n____/ ePSXe Team \\____\ncalb, Galtor and _Demo_\n\nSpecial thanks! Roor, Expert, Doomed, Vood, Fennec, Jean-Ioup Gailly/Mark Adler, Zsknight, Zilmar, Jabo, Alex7/Burutter, 1964 team, Fpse team, Duddie, Tratax, i4get, Psychojak, Shunt, Willy, Shalma, Tikalat, David Muriel, Goi and Jose.\n\n_____| PSEmuPro Plugin coders Team |_____\nPete, Lewpy, Kazzuya, JNS, Null2, Iori, Andy, Nickk, Barrett, Knack, linuzappz, Adrenaline,Nik, Segu, e}i{ ...\n\n___\\ ePSXe Betatesters Team /___\nGladiator, Pts, CDBuRnOuT, GreenImp, Wormie, squall-leonheart, sxamiga, emumaniac, crualfoxhound and Bnu.\n\n_| ePSXe Webmaster/Betatester/Help file |__\nBobbi, _Demo_\n\n___\\ ePSXe Translations Team /___ Nekokabu, Chow Chi Hoi, Ultra Taber, Thomas, Luca, Jesse,Duo Jeong, Marco Freire \n\n___\\ ePSXe Android Art /__\n Robert Typek, Javier ~ X1673 \n");
        alertDialog.setButton(getString(R.string.dialog_action_ok), new DialogInterface.OnClickListener() { // from class: com.epsxe.ePSXe.ePSXeNative.10
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertDialog.setIcon(R.drawable.icon);
        alertDialog.show();
    }

    private void alertdialog_quitGame() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(R.string.main_back);
        alertDialog.setMessage(getString(R.string.main_wantquitgame) + "(" + this.f157e.getCode() + ")?\n" + this.f157e.getGameInfo());
        alertDialog.setButton(getString(R.string.main_noquitgame), new DialogInterface.OnClickListener() { // from class: com.epsxe.ePSXe.ePSXeNative.12
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertDialog.setButton2(getString(R.string.main_yesquitgame), new DialogInterface.OnClickListener() { // from class: com.epsxe.ePSXe.ePSXeNative.13
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                if (ePSXeNative.this.emu_autosave == 1) {
                    ePSXeNative.this.f157e.setsslot(15);
                    ePSXeNative.this.mePSXeView.setSaveslot(15);
                    ePSXeNative.this.mHandler.postDelayed(ePSXeNative.this.mLaunchTask, 1000L);
                    return;
                }
                ePSXeNative.this.mePSXeSound.exit();
                ePSXeNative.this.emuStatus = 0;
                if (ePSXeNative.this.emu_gui == 1) {
                    Intent myIntent = new Intent(ePSXeNative.this, (Class<?>) ePSXe.class);
                    myIntent.setFlags(DriveFile.MODE_READ_ONLY);
                    ePSXeNative.this.startActivity(myIntent);
                }
                ePSXeNative.this.f157e.quit();
                ePSXeNative.this.finish();
            }
        });
        alertDialog.setIcon(R.drawable.icon);
        alertDialog.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void quitGame_error() {
        this.mePSXeSound.exit();
        this.emuStatus = 0;
        if (this.emu_gui == 1) {
            Intent myIntent = new Intent(this, (Class<?>) ePSXe.class);
            myIntent.setFlags(DriveFile.MODE_READ_ONLY);
            startActivity(myIntent);
        } else {
            this.f157e.setSaveMode(1, 0);
        }
        this.f157e.quit();
        finish();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void alertdialog_quitGame_error() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(R.string.main_err_tit);
        alertDialog.setMessage(getString(R.string.main_err_msg) + ": " + this.f157e.getError());
        alertDialog.setButton2(getString(R.string.main_err_quit), new DialogInterface.OnClickListener() { // from class: com.epsxe.ePSXe.ePSXeNative.14
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                ePSXeNative.this.quitGame_error();
            }
        });
        alertDialog.setIcon(R.drawable.icon);
        alertDialog.show();
    }

    @Override // android.app.Activity
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                if (this.emuStatus == 0 && check_bios(0) != -1) {
                    Intent myIntent = new Intent(this, (Class<?>) FileChooser.class);
                    myIntent.putExtra("com.epsxe.ePSXe.fcMode", "SELECT_ISO");
                    myIntent.putExtra("com.epsxe.ePSXe.isoPath", this.currentPath);
                    startActivity(myIntent);
                    finish();
                    break;
                }
                break;
            case 2:
                if (this.emuStatus == 0 && check_bios(1) != -1) {
                    Intent myIntent2 = new Intent(this, (Class<?>) FileChooser.class);
                    myIntent2.putExtra("com.epsxe.ePSXe.fcMode", "RUN_BIOS");
                    myIntent2.putExtra("com.epsxe.ePSXe.isoPath", this.currentPath);
                    startActivity(myIntent2);
                    finish();
                    break;
                }
                break;
            case 3:
                if (this.emuStatus == 1) {
                    CreateCDDialog(this.currentPath);
                    break;
                }
                break;
            case 4:
                if (this.emuStatus == 1) {
                    CreateSstateDialog(this.f157e.getCode(), 0, 0);
                    break;
                }
                break;
            case 5:
                if (this.emuStatus == 1) {
                    CreateSstateDialog(this.f157e.getCode(), 1, 0);
                    break;
                }
                break;
            case 6:
                Intent myIntent3 = new Intent(this, (Class<?>) ePSXePreferences.class);
                startActivity(myIntent3);
                finish();
                break;
            case 7:
                alertdialog_about();
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
                Intent myIntent4 = new Intent(this, (Class<?>) ePSXeHelp.class);
                startActivity(myIntent4);
                finish();
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
                    CreatecheatDialog();
                    break;
                }
                break;
            case 16:
                if (this.emuStatus == 1 || this.emuStatus == 2) {
                    this.mePSXeView.toggletools();
                    break;
                }
                break;
        }

        return true;
    }

    public float GetRadiusf(float x, float y) {
        return (float) Math.sqrt((x * x) + (y * y));
    }

    public float GetRatio(int x, int y) {
        int A1;
        int B1;
        int C1;
        int A2;
        int B2;
        int C2;
        if (x >= 0 && y >= 0) {
            A1 = 0 - y;
            B1 = x + 0;
            C1 = (A1 * x) + (B1 * y);
            if (Math.abs(y) >= Math.abs(x)) {
                A2 = 0;
                B2 = 128;
                C2 = 0 + 16384;
            } else {
                A2 = -128;
                B2 = 0;
                C2 = (-16384) + 0;
            }
        } else if (x <= 0 && y >= 0) {
            A1 = 0 - y;
            B1 = x + 0;
            C1 = (A1 * x) + (B1 * y);
            if (Math.abs(y) >= Math.abs(x)) {
                A2 = 0;
                B2 = 128;
                C2 = 0 + 16384;
            } else {
                A2 = -128;
                B2 = 0;
                C2 = 16384 + 0;
            }
        } else if (x <= 0 && y <= 0) {
            A1 = 0 - y;
            B1 = x + 0;
            C1 = (A1 * x) + (B1 * y);
            if (Math.abs(y) >= Math.abs(x)) {
                A2 = 0;
                B2 = -128;
                C2 = 0 + 16384;
            } else {
                A2 = 128;
                B2 = 0;
                C2 = (-16384) + 0;
            }
        } else {
            A1 = 0 - y;
            B1 = x + 0;
            C1 = (A1 * x) + (B1 * y);
            if (Math.abs(y) >= Math.abs(x)) {
                A2 = 0;
                B2 = -128;
                C2 = 0 + 16384;
            } else {
                A2 = 128;
                B2 = 0;
                C2 = 16384 + 0;
            }
        }
        float det = (A1 * B2) - (A2 * B1);
        float f = ((B2 * C1) - (B1 * C2)) / det;
        float f2 = ((A1 * C2) - (A2 * C1)) / det;
        float rad = GetRadiusf(x, y);
        float ratio = rad / 128.0f;
        return ratio;
    }

    @Override // android.app.Activity
    public boolean onGenericMotionEvent(MotionEvent event) {
        InputDevice device = event.getDevice();
        int mdev = -1;
        int eventDevice = device.getId();
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
        if (mdev == -1) {
            return false;
        }
        int x1 = (int) (event.getAxisValue(this.analogleftx[mdev]) * 127.0f);
        int y1 = (int) (event.getAxisValue(this.analoglefty[mdev]) * 127.0f);
        int x2 = (int) (event.getAxisValue(this.analogrightx[mdev]) * 127.0f);
        int y2 = (int) (event.getAxisValue(this.analogrighty[mdev]) * 127.0f);
        if (this.analogrange[mdev] == 1) {
            float ratio1 = GetRatio(x1, y1);
            float ratio2 = GetRatio(x2, y2);
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
        }
        this.f157e.setpadanalog(mdev, 0, x1, y1);
        this.f157e.setpadanalog(mdev, 1, x2, y2);
        int l2 = (int) (event.getAxisValue(this.analogl2[mdev]) * 127.0f);
        int r2 = (int) (event.getAxisValue(this.analogr2[mdev]) * 127.0f);
        int tmp = this.pushedButtons[mdev];
        int hatx = (int) event.getAxisValue(15);
        int haty = (int) event.getAxisValue(16);
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
                iArr7[mdev] = iArr7[mdev] & (-4097);
                int[] iArr8 = this.pushedButtons;
                iArr8[mdev] = iArr8[mdev] | 16384;
            } else if (haty == 1) {
                int[] iArr9 = this.pushedButtons;
                iArr9[mdev] = iArr9[mdev] | 4096;
                int[] iArr10 = this.pushedButtons;
                iArr10[mdev] = iArr10[mdev] & (-16385);
            } else {
                int[] iArr11 = this.pushedButtons;
                iArr11[mdev] = iArr11[mdev] & (-4097);
                int[] iArr12 = this.pushedButtons;
                iArr12[mdev] = iArr12[mdev] & (-16385);
            }
            this.analoghaty[mdev] = haty;
        }
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
        if (tmp != this.pushedButtons[mdev]) {
            this.f157e.setPadDataMultitap(this.pushedButtons[0], this.pushedButtons[1], this.pushedButtons[2], this.pushedButtons[3]);
        }
        return this.analogdpadfromanalog[mdev] != 1;
    }

    public void doMenu() {
        openOptionsMenu();
    }

    private boolean doKeyUp(int keyCode, boolean meta, int mdev) {
        for (int p = 0; p < 2; p++) {
            if (mdev == -1 || mdev == p) {
                for (int k = 0; k < 20; k++) {
                    if ((((meta || keyCode == 57 || keyCode == 58) ? 65536 : 0) | keyCode) == this.keycodes[p][k]) {
                        int[] iArr = this.pushedButtons;
                        iArr[p] = iArr[p] & ((1 << k) ^ (-1));
                        this.f157e.setPadDataMultitap(this.pushedButtons[0], this.pushedButtons[1], this.pushedButtons[2], this.pushedButtons[3]);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        int eventDevice = event.getDeviceId();
        int mdev = -1;
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
        if ((this.emuStatus == 1 || this.emuStatus == 2) && doKeyUp(keyCode, event.isAltPressed(), mdev)) {
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    private boolean doKeyDown(int keyCode, boolean meta, int mdev) {
        for (int p = 0; p < 2; p++) {
            if (mdev == -1 || mdev == p) {
                for (int k = 0; k < 20; k++) {
                    if (((meta ? 65536 : 0) | keyCode) == this.keycodes[p][k]) {
                        int[] iArr = this.pushedButtons;
                        iArr[p] = iArr[p] | (1 << k);
                        this.f157e.setPadDataMultitap(this.pushedButtons[0], this.pushedButtons[1], this.pushedButtons[2], this.pushedButtons[3]);
                        return true;
                    }
                }
                if ((((meta || keyCode == 57 || keyCode == 58) ? 65536 : 0) | keyCode) == this.keycodes[p][20]) {
                    this.mePSXeView.setinputpadmodeanalog(p);
                    return true;
                }
            }
        }
        for (int k2 = 0; k2 < this.keycodesextra.length; k2++) {
            if (((meta ? 65536 : 0) | keyCode) == this.keycodesextra[k2]) {
                if (k2 < 5) {
                    this.f157e.setsslot(k2);
                } else if (k2 < 10) {
                    this.f157e.setsslot(k2 + 5);
                    this.mePSXeView.setSaveslot(k2 + 5);
                } else if (k2 < 11) {
                    this.mePSXeView.toggleframelimit();
                } else if (k2 < 12) {
                    openOptionsMenu();
                }
                return true;
            }
        }
        return keyCode == 4;
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        int eventDevice = event.getDeviceId();
        int mdev = -1;
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
        if ((this.emuStatus == 1 || this.emuStatus == 2) && doKeyDown(keyCode, event.isAltPressed(), mdev)) {
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
        Vibrator v2 = (Vibrator) getSystemService("vibrator");
        if (v2 != null) {
            if (motor != 0 || value != 1) {
                if (motor != 1 || value <= 20) {
                    return;
                }
                v2.vibrate((value >> 2) + 35);
                return;
            }
            v2.vibrate(35L);
        }
    }

    public void doError(int err) {
        this.handlerErr.post(this.runnableErr);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void alertdialog_deleteState(final String dfile) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(R.string.main_dstate);
        alertDialog.setMessage(getString(R.string.main_dstatewant));
        alertDialog.setButton(getString(R.string.main_dstateno), new DialogInterface.OnClickListener() { // from class: com.epsxe.ePSXe.ePSXeNative.15
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertDialog.setButton2(getString(R.string.main_dstateyes), new DialogInterface.OnClickListener() { // from class: com.epsxe.ePSXe.ePSXeNative.16
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                File fileTmp = new File(dfile);
                if (fileTmp.exists()) {
                    fileTmp.delete();
                }
            }
        });
        alertDialog.setIcon(R.drawable.icon);
        alertDialog.show();
    }

    public void CreateSstateDialog(String code, int mycmd, int autoload) {
        this.currentDir = new File(this.sdCardPath + "/epsxe/sstates");
        this.gameCode = code;
        if (this.hlebiosrunning == 1) {
            this.gameCode += "HLE";
        }
        this.sscmd = mycmd;
        if (this.byteBuffer == null) {
            this.byteBuffer = ByteBuffer.allocateDirect(24576);
        }
        Log.e("sstate", "gameCode " + this.gameCode);
        if (autoload == 0) {
            fill(this.currentDir);
        } else {
            File ff = new File(this.currentDir + "/" + this.gameCode + ".005");
            if (ff.exists()) {
                fill_autoload(this.currentDir);
            } else {
                return;
            }
        }
        this.sstateListView = new ListView(this);
        this.sstateListView.setAdapter((ListAdapter) this.adapter);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(this.sstateListView);
        if (autoload == 1) {
            builder.setCancelable(false);
        }
        this.sstateListView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.epsxe.ePSXe.ePSXeNative.17
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Option o = ePSXeNative.this.adapter.getItem(position);
                Log.e("sstate", "state: " + o.getPath());
                int slot = Integer.parseInt(o.getSlot());
                Log.e("sstate", "slot: " + slot);
                if (slot != -1) {
                    if (ePSXeNative.this.sscmd == 0) {
                        ePSXeNative.this.f157e.setsslot(slot);
                    } else {
                        ePSXeNative.this.f157e.setsslot(slot + 10);
                        ePSXeNative.this.mePSXeView.setSaveslot(slot + 10);
                    }
                }
                ePSXeNative.this.sAlert.dismiss();
            }
        });
        this.sstateListView.setLongClickable(true);
        this.sstateListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() { // from class: com.epsxe.ePSXe.ePSXeNative.18
            @Override // android.widget.AdapterView.OnItemLongClickListener
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Option o = ePSXeNative.this.adapter.getItem(position);
                Log.e("sstate", "state: " + o.getPath());
                int slot = Integer.parseInt(o.getSlot());
                Log.e("sstate", "slot: " + slot);
                File fileTmp = new File(o.getPath());
                if (fileTmp.exists()) {
                    ePSXeNative.this.alertdialog_deleteState(o.getPath());
                }
                ePSXeNative.this.sAlert.dismiss();
                return true;
            }
        });
        this.sAlert = builder.create();
        this.sAlert.show();
    }

    public static byte[] getBytesFromFile(File file) throws IOException {
        int numRead;
        InputStream is = new FileInputStream(file);
        long length = file.length();
        if (length > 2147483647L) {
        }
        byte[] bytes = new byte[(int) length];
        byte[] res = new byte[24576];
        int offset = 0;
        while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }
        is.close();
        int offres = 0;
        for (int ind = 0; ind < ((int) length) / 3; ind++) {
            res[offres] = (byte) (((bytes[(ind * 3) + 2] & 248) >> 3) | ((bytes[(ind * 3) + 1] & 28) << 3));
            res[offres + 1] = (byte) (((bytes[(ind * 3) + 1] & 224) >> 5) | (bytes[ind * 3] & 248));
            offres += 2;
        }
        return res;
    }

    private void fill_autoload(File f) {
        f.listFiles();
        setTitle(R.string.main_selectslot);
        List<Option> fls = new ArrayList<>();
        try {
            fls.add(new Option("--- NEW GAME ---", "", "", "", "-1", Bitmap.createBitmap(128, 96, Bitmap.Config.RGB_565)));
            File ff = new File(this.currentDir + "/" + this.gameCode + ".005");
            if (ff.exists()) {
                Long lastModified = Long.valueOf(ff.lastModified());
                Date date = new Date(lastModified.longValue());
                File ffpic = new File(ff.getAbsolutePath() + ".pic");
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                String sdate = sdf.format(date);
                Bitmap tBitmap = Bitmap.createBitmap(128, 96, Bitmap.Config.RGB_565);
                if (ffpic.exists()) {
                    this.byteBuffer.clear();
                    this.byteBuffer.put(getBytesFromFile(ffpic));
                    this.byteBuffer.flip();
                    tBitmap.copyPixelsFromBuffer(this.byteBuffer);
                    fls.add(new Option(ff.getName(), getString(R.string.main_filesize) + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + (ff.length() / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID) + " Kb", ff.getAbsolutePath(), "" + sdate, "5", tBitmap));
                } else {
                    fls.add(new Option(ff.getName(), getString(R.string.main_filesize) + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + (ff.length() / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID) + " Kb", ff.getAbsolutePath(), "" + sdate, "5", tBitmap));
                }
            }
        } catch (Exception e) {
        }
        this.adapter = new sstateArrayAdapter(this, R.layout.sstate_view, fls);
    }

    private void fill(File f) {
        f.listFiles();
        setTitle(R.string.main_selectslot);
        List<Option> fls = new ArrayList<>();
        for (int ind = 0; ind < 5; ind++) {
            try {
                File ff = new File(this.currentDir + "/" + this.gameCode + ".00" + ind);
                if (ff.exists()) {
                    Long lastModified = Long.valueOf(ff.lastModified());
                    Date date = new Date(lastModified.longValue());
                    File ffpic = new File(ff.getAbsolutePath() + ".pic");
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                    String sdate = sdf.format(date);
                    Bitmap tBitmap = Bitmap.createBitmap(128, 96, Bitmap.Config.RGB_565);
                    if (ffpic.exists()) {
                        this.byteBuffer.clear();
                        this.byteBuffer.put(getBytesFromFile(ffpic));
                        this.byteBuffer.flip();
                        tBitmap.copyPixelsFromBuffer(this.byteBuffer);
                        fls.add(new Option(ff.getName(), getString(R.string.main_filesize) + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + (ff.length() / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID) + " Kb", ff.getAbsolutePath(), "" + sdate, "" + ind, tBitmap));
                    } else {
                        fls.add(new Option(ff.getName(), getString(R.string.main_filesize) + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + (ff.length() / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID) + " Kb", ff.getAbsolutePath(), "" + sdate, "" + ind, tBitmap));
                    }
                } else {
                    fls.add(new Option(getString(R.string.main_slotfree), "", "", "", "" + ind, Bitmap.createBitmap(128, 96, Bitmap.Config.RGB_565)));
                }
            } catch (Exception e) {
            }
        }
        this.adapter = new sstateArrayAdapter(this, R.layout.sstate_view, fls);
    }

    private class Option implements Comparable<Option> {
        private String data;
        private String date;
        private Bitmap mBitmap;
        private String name;
        private String path;
        private String slot;

        public Option(String n, String d, String p, String t, String s, Bitmap b) {
            this.name = n;
            this.data = d;
            this.path = p;
            this.date = t;
            this.slot = s;
            this.mBitmap = b;
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

        public String getDate() {
            return this.date;
        }

        public String getSlot() {
            return this.slot;
        }

        public Bitmap getBitmap() {
            return this.mBitmap;
        }

        @Override // java.lang.Comparable
        public int compareTo(Option o) {
            if (this.name != null) {
                return this.name.toLowerCase().compareTo(o.getName().toLowerCase());
            }
            throw new IllegalArgumentException();
        }
    }

    public class sstateArrayAdapter extends ArrayAdapter<Option> {

        /* renamed from: c */
        private Context f162c;

        /* renamed from: id */
        private int f163id;
        private List<Option> items;

        public sstateArrayAdapter(Context context, int textViewResourceId, List<Option> objects) {
            super(context, textViewResourceId, objects);
            this.f162c = context;
            this.f163id = textViewResourceId;
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
                LayoutInflater vi = (LayoutInflater) this.f162c.getSystemService("layout_inflater");
                v = vi.inflate(this.f163id, (ViewGroup) null);
            }
            Option o = this.items.get(position);
            if (o != null) {
                TextView t1 = (TextView) v.findViewById(R.id.TextView01);
                TextView t2 = (TextView) v.findViewById(R.id.TextView02);
                TextView t3 = (TextView) v.findViewById(R.id.TextView03);
                TextView t4 = (TextView) v.findViewById(R.id.TextView04);
                ImageView v1 = (ImageView) v.findViewById(R.id.image);
                if (t1 != null) {
                    t1.setText(o.getName());
                }
                if (t2 != null) {
                    int slot = Integer.parseInt(o.getSlot());
                    if (slot == -1) {
                        t2.setText("");
                    } else if (slot == 5) {
                        t2.setText(ePSXeNative.this.getString(R.string.main_autosave));
                    } else {
                        t2.setText(ePSXeNative.this.getString(R.string.main_slotnumber) + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + slot);
                    }
                }
                if (t3 != null) {
                    t3.setText(o.getData());
                }
                if (t4 != null) {
                    t4.setText(o.getDate());
                }
                if (v1 != null && o.getBitmap() != null) {
                    v1.setImageBitmap(o.getBitmap());
                }
            }
            return v;
        }
    }

    public void CreateCDDialog(String path) {
        this.currentCDDir = new File(path);
        Log.e("changedisc", MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + path);
        fillCD(this.currentCDDir);
        this.mListView = new ListView(this);
        this.mListView.setAdapter((ListAdapter) this.cdadapter);
        this.mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.epsxe.ePSXe.ePSXeNative.19
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> listView, View itemView, int position, long itemId) {
                OptionCD o = ePSXeNative.this.cdadapter.getItem(position);
                if (o.getData().equalsIgnoreCase("folder") || o.getData().equalsIgnoreCase("parent directory")) {
                    File currentDir = new File(o.getPath());
                    Log.e("cd", "changepath " + o.getPath());
                    ePSXeNative.this.fillCD(currentDir);
                    ePSXeNative.this.mListView.setAdapter((ListAdapter) ePSXeNative.this.cdadapter);
                    return;
                }
                if (!o.getPath().equalsIgnoreCase("folder")) {
                    Log.e("cd", "state: " + o.getPath());
                    Log.e("cd", "which: " + position);
                    ePSXeNative.this.mAlert.dismiss();
                }
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(this.mListView);
        this.mAlert = builder.create();
        this.mAlert.show();
    }

    private boolean acceptCD(String name) {
        return name.toLowerCase().endsWith(".cue") || name.toLowerCase().endsWith(".ccd") || name.toLowerCase().endsWith(".bin") || name.toLowerCase().endsWith(".img") || name.toLowerCase().endsWith(".iso") || name.toLowerCase().endsWith(".mds") || name.toLowerCase().endsWith(".mdf") || name.toLowerCase().endsWith(".cdi") || name.toLowerCase().endsWith(".nrg") || name.toLowerCase().endsWith(".pbp") || name.toLowerCase().endsWith(".ecm");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void fillCD(File f) {
        File[] dirs = f.listFiles();
        setTitle(R.string.main_selectiso);
        ArrayList arrayList = new ArrayList();
        List<OptionCD> dir = new ArrayList<>();
        try {
            for (File ff : dirs) {
                if (ff.isDirectory()) {
                    dir.add(new OptionCD(ff.getName(), "Folder", ff.getAbsolutePath(), 0));
                } else {
                    Log.e("folder", "File " + ff.getName());
                    if (acceptCD(ff.getName())) {
                        long msize = ff.length() / 1048576;
                        if (msize > 2) {
                            if (ff.getName().toLowerCase().endsWith(".pbp")) {
                                pbpFile mfile = new pbpFile(ff.getAbsolutePath(), ff.getName());
                                int n = mfile.getNumFiles();
                                for (int i = 0; i < n; i++) {
                                    arrayList.add(new OptionCD(mfile.getFileName(i + 1), getString(R.string.main_filesize) + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + (msize / n) + " Mbytes", ff.getAbsolutePath(), i));
                                }
                            } else {
                                arrayList.add(new OptionCD(ff.getName(), getString(R.string.main_filesize) + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + msize + " Mbytes", ff.getAbsolutePath(), 0));
                            }
                        } else {
                            arrayList.add(new OptionCD(ff.getName(), getString(R.string.main_filesize) + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + ff.length() + " Bytes", ff.getAbsolutePath(), 0));
                        }
                    }
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
        this.cdadapter = new cdArrayAdapter(this, R.layout.file_view, dir);
    }

    private class OptionCD implements Comparable<OptionCD> {
        private String data;
        private String name;
        private String path;
        private int slot;

        public OptionCD(String n, String d, String p, int s) {
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
        public int compareTo(OptionCD o) {
            if (this.name != null) {
                return this.name.toLowerCase().compareTo(o.getName().toLowerCase());
            }
            throw new IllegalArgumentException();
        }
    }

    public class cdArrayAdapter extends ArrayAdapter<OptionCD> {

        /* renamed from: c */
        private Context f160c;

        /* renamed from: id */
        private int f161id;
        private List<OptionCD> items;

        public cdArrayAdapter(Context context, int textViewResourceId, List<OptionCD> objects) {
            super(context, textViewResourceId, objects);
            this.f160c = context;
            this.f161id = textViewResourceId;
            this.items = objects;
        }

        @Override // android.widget.ArrayAdapter, android.widget.Adapter
        public OptionCD getItem(int i) {
            return this.items.get(i);
        }

        @Override // android.widget.ArrayAdapter, android.widget.Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) this.f160c.getSystemService("layout_inflater");
                v = vi.inflate(this.f161id, (ViewGroup) null);
            }
            OptionCD o = this.items.get(position);
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

    /* JADX INFO: Access modifiers changed from: private */
    public String DownloadCheatcodesFile() {
        try {
            String country = this.f157e.getCode().substring(0, 4);
            URL url = new URL("http://epsxe.com/cheats/" + country + "/" + this.f157e.getCode() + ".txt");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(HttpGet.METHOD_NAME);
            urlConnection.setDoOutput(true);
            urlConnection.connect();
            File SDCardRoot = ContextCompat.getDataDir(this);
            File file = new File(SDCardRoot, "epsxe/cheats/" + this.f157e.getCode() + ".txt");
            FileOutputStream fileOutput = new FileOutputStream(file);
            InputStream inputStream = urlConnection.getInputStream();
            urlConnection.getContentLength();
            int downloadedSize = 0;
            byte[] buffer = new byte[1024];
            while (true) {
                int bufferLength = inputStream.read(buffer);
                if (bufferLength > 0) {
                    fileOutput.write(buffer, 0, bufferLength);
                    downloadedSize += bufferLength;
                } else {
                    fileOutput.close();
                    this.f157e.reloadAllGS();
                    return getString(R.string.cheat_dialog_downloaded);
                }
            }
        } catch (FileNotFoundException e) {
            return getString(R.string.cheat_dialog_notdownloaded);
        } catch (MalformedURLException e2) {
            e2.printStackTrace();
            return "Error malformed URL!";
        } catch (IOException e3) {
            e3.printStackTrace();
            return getString(R.string.cheat_dialog_notdownloadednow);
        }
    }

    private class DownloadFilesTask extends AsyncTask<String, Integer, String> {
        private Context context;

        public DownloadFilesTask(Context context) {
            this.context = context;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public String doInBackground(String... params) {
            return ePSXeNative.this.DownloadCheatcodesFile();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onProgressUpdate(Integer... progress) {
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(String result) {
            Toast.makeText(this.context, result, 1).show();
        }
    }

    public void nocheatDialog() {
        TextView message = new TextView(this);
        SpannableString s = new SpannableString(getText(R.string.cheat_dialog_message1));
        SpannableString s1 = new SpannableString(getText(R.string.cheat_dialog_message2));
        Linkify.addLinks(s1, 1);
        message.setText(((Object) s) + this.f157e.getCode() + ((Object) s1));
        message.setMovementMethod(LinkMovementMethod.getInstance());
        AlertDialog builder = new AlertDialog.Builder(this).setTitle(((Object) getText(R.string.nocheat_dialog_title)) + " " + this.f157e.getCode()).setCancelable(true).setIcon(android.R.drawable.ic_dialog_info).setNegativeButton(getString(R.string.cheat_dialog_back), (DialogInterface.OnClickListener) null).setPositiveButton(getString(R.string.cheat_dialog_download), new DialogInterface.OnClickListener() { // from class: com.epsxe.ePSXe.ePSXeNative.20
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int id) {
                Log.e("epsxecheat", "Downloading cheat file...");
                Toast.makeText(ePSXeNative.this, R.string.cheat_dialog_downloading, 1).show();
                ePSXeNative.this.new DownloadFilesTask(ePSXeNative.this).execute("");
            }
        }).setView(message).create();
        builder.show();
    }

    public void CreatecheatDialog() {
        int num = this.f157e.getGSNumber() + 1;
        if (num > 0) {
            CharSequence[] _options = new String[num];
            boolean[] _selections = new boolean[num];
            for (int i = 0; i < num; i++) {
                try {
                    _options[i] = new String(this.f157e.getGSName(i), "utf-8");
                    if (this.f157e.getGSStatus(i) == 0) {
                        _selections[i] = false;
                    } else {
                        _selections[i] = true;
                    }
                } catch (Exception e) {
                }
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(((Object) getText(R.string.cheat_dialog_title)) + " " + this.f157e.getCode());
            builder.setMultiChoiceItems(_options, _selections, new DialogInterface.OnMultiChoiceClickListener() { // from class: com.epsxe.ePSXe.ePSXeNative.21
                @Override // android.content.DialogInterface.OnMultiChoiceClickListener
                public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                    if (isChecked) {
                        Log.e("epsxecheat", "id on:" + which);
                        ePSXeNative.this.f157e.enableGS(which);
                    } else {
                        Log.e("epsxecheat", "id off:" + which);
                        ePSXeNative.this.f157e.disableGS(which);
                    }
                }
            });
            builder.setPositiveButton(getString(R.string.cheat_dialog_apply), new DialogInterface.OnClickListener() { // from class: com.epsxe.ePSXe.ePSXeNative.22
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialog, int id) {
                    Log.e("epsxecheat", "id:" + id);
                }
            });
            builder.setNegativeButton(getString(R.string.cheat_dialog_disableall), new DialogInterface.OnClickListener() { // from class: com.epsxe.ePSXe.ePSXeNative.23
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialog, int id) {
                    Log.e("epsxecheat", "clear All");
                    ePSXeNative.this.f157e.disableAllGS();
                }
            });
            builder.setNeutralButton(getString(R.string.cheat_dialog_enableall), new DialogInterface.OnClickListener() { // from class: com.epsxe.ePSXe.ePSXeNative.24
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialog, int id) {
                    Log.e("epsxecheat", "set All");
                    ePSXeNative.this.f157e.enableAllGS();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
            return;
        }
        nocheatDialog();
    }

    public static boolean copyFile(File source, File dest) {
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            BufferedInputStream bis2 = new BufferedInputStream(new FileInputStream(source));
            try {
                BufferedOutputStream bos2 = new BufferedOutputStream(new FileOutputStream(dest, false));
                try {
                    byte[] buf = new byte[1024];
                    bis2.read(buf);
                    do {
                        bos2.write(buf);
                    } while (bis2.read(buf) != -1);
                    if (bis2 != null) {
                        try {
                            bis2.close();
                        } catch (IOException e) {
                            return false;
                        }
                    }
                    if (bos2 != null) {
                        bos2.close();
                    }
                    return true;
                } catch (IOException e2) {
                    bos = bos2;
                    bis = bis2;
                    if (bis != null) {
                        try {
                            bis.close();
                        } catch (IOException e3) {
                            return false;
                        }
                    }
                    if (bos == null) {
                        return false;
                    }
                    bos.close();
                    return false;
                } catch (Throwable th) {
                    th = th;
                    bos = bos2;
                    bis = bis2;
                    if (bis != null) {
                        try {
                            bis.close();
                        } catch (IOException e4) {
                            return false;
                        }
                    }
                    if (bos != null) {
                        bos.close();
                    }
                    throw th;
                }
            } catch (IOException e5) {
                bis = bis2;
            } catch (Throwable th2) {
                Throwable th = th2;
                bis = bis2;
            }
        } catch (IOException e6) {
        } catch (Throwable th3) {
            Throwable th = th3;
        }

        return true;
    }

    public static byte[] createChecksum(String filename) throws Exception {
        int numRead;
        InputStream fis = new FileInputStream(filename);
        byte[] buffer = new byte[1024];
        MessageDigest complete = MessageDigest.getInstance("MD5");
        do {
            numRead = fis.read(buffer);
            if (numRead > 0) {
                complete.update(buffer, 0, numRead);
            }
        } while (numRead != -1);
        fis.close();
        return complete.digest();
    }

    public static String getMD5Checksum(String filename) throws Exception {
        byte[] b = createChecksum(filename);
        String result = "";
        for (byte b2 : b) {
            result = result + Integer.toString((b2 & 255) + 256, 16).substring(1);
        }
        return result;
    }

    public int findBios(String path, int level) {
        if (level > 12) {
            return -1;
        }
        try {
            File f = new File(path);
            File[] fileList = f.listFiles();
            if (!f.getName().equals("DCIM") && !f.getName().equals("Camera") && !f.getName().equals("asec") && !f.getName().equals(ClientCookie.SECURE_ATTR)) {
                Log.e("findBios", "DirScan: [" + f.getAbsoluteFile() + "]" + f.getName());
                for (File file : fileList) {
                    try {
                        if (file.isDirectory()) {
                            int ret = findBios(file.getAbsolutePath(), level + 1);
                            if (ret != -1) {
                                return ret;
                            }
                        } else if (file.getName().toLowerCase().endsWith(".bin")) {
                            long msize = file.length();
                            if (msize == 524288) {
                                String md5sum = getMD5Checksum(file.getAbsolutePath());
                                Log.e("findBios", "Bios found: " + md5sum);
                                for (int n = 0; n < 21; n++) {
                                    if (this.bioslist[n][1].equals(md5sum)) {
                                        this.biosnamefound = file.getAbsolutePath();
                                        return n;
                                    }
                                }
                            } else {
                                continue;
                            }
                        } else {
                            continue;
                        }
                    } catch (Exception e) {
                        Log.e("findBios", "" + e.getMessage());
                        return -1;
                    }
                }
                return -1;
            }
            return -1;
        } catch (Exception e2) {
            Log.e("findBios", "" + e2.getMessage());
            return -1;
        }
    }

    private class ScansdcardBiosTask extends AsyncTask<String, Integer, Integer> {
        private Context context;
        ProgressDialog dialog;

        public ScansdcardBiosTask(Context ctx) {
            this.context = ctx;
            this.dialog = new ProgressDialog(this.context);
            this.dialog.setTitle(R.string.bios_finding);
            this.dialog.show();
            this.dialog.setCancelable(false);
            Log.e("ScansdcardBiosTask", "start scan bios");
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public Integer doInBackground(String... params) {
            Log.e("ScansdcardBiosTask", "start scan bios 2");
            return Integer.valueOf(ePSXeNative.this.findBios(params[0], 0));
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(Integer result) {
            this.dialog.dismiss();
            if (result.intValue() == -1) {
                ePSXeReadPreferences lePSXeReadPreferences = new ePSXeReadPreferences(this.context);
                int hlebios = lePSXeReadPreferences.getBiosHle();
                if (hlebios == 2) {
                    SharedPreferences sharedPreferences = ePSXeApplication.getDefaultSharedPreferences(this.context);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("biosHlePref", "1");
                    editor.commit();
                    return;
                }
                if (hlebios != 1) {
                    ePSXeNative.this.BiosDialog();
                    return;
                }
                return;
            }
            SharedPreferences sharedPreferences2 = ePSXeApplication.getDefaultSharedPreferences(this.context);
            SharedPreferences.Editor editor2 = sharedPreferences2.edit();
            editor2.putString("biosPref", ePSXeNative.this.biosnamefound);
            editor2.commit();
            ePSXeNative.this.BiosFoundDialog(ePSXeNative.this.bioslist[result.intValue()][0]);
        }
    }

    class MogaControllerListener implements ControllerListener {
        MogaControllerListener() {
        }

        @Override // com.bda.controller.ControllerListener
        public void onKeyEvent(com.bda.controller.KeyEvent event) {
            if (ePSXeNative.this.mogapad != -1) {
                if (ePSXeNative.this.emuStatus == 1 || ePSXeNative.this.emuStatus == 2) {
                    int npad = ePSXeNative.this.mogapad;
                    int action = event.getAction();
                    switch (event.getKeyCode()) {
                        case 19:
                            if (action == 0) {
                                int[] iArr = ePSXeNative.this.pushedButtons;
                                iArr[npad] = iArr[npad] | 4096;
                                break;
                            } else {
                                int[] iArr2 = ePSXeNative.this.pushedButtons;
                                iArr2[npad] = iArr2[npad] & (-4097);
                                break;
                            }
                        case 20:
                            if (action == 0) {
                                int[] iArr3 = ePSXeNative.this.pushedButtons;
                                iArr3[npad] = iArr3[npad] | 16384;
                                break;
                            } else {
                                int[] iArr4 = ePSXeNative.this.pushedButtons;
                                iArr4[npad] = iArr4[npad] & (-16385);
                                break;
                            }
                        case 21:
                            if (action == 0) {
                                int[] iArr5 = ePSXeNative.this.pushedButtons;
                                iArr5[npad] = iArr5[npad] | 32768;
                                break;
                            } else {
                                int[] iArr6 = ePSXeNative.this.pushedButtons;
                                iArr6[npad] = iArr6[npad] & (-32769);
                                break;
                            }
                        case 22:
                            if (action == 0) {
                                int[] iArr7 = ePSXeNative.this.pushedButtons;
                                iArr7[npad] = iArr7[npad] | 8192;
                                break;
                            } else {
                                int[] iArr8 = ePSXeNative.this.pushedButtons;
                                iArr8[npad] = iArr8[npad] & (-8193);
                                break;
                            }
                        case 96:
                            if (action == 0) {
                                int[] iArr9 = ePSXeNative.this.pushedButtons;
                                iArr9[npad] = iArr9[npad] | 64;
                                break;
                            } else {
                                int[] iArr10 = ePSXeNative.this.pushedButtons;
                                iArr10[npad] = iArr10[npad] & (-65);
                                break;
                            }
                        case 97:
                            if (action == 0) {
                                int[] iArr11 = ePSXeNative.this.pushedButtons;
                                iArr11[npad] = iArr11[npad] | 32;
                                break;
                            } else {
                                int[] iArr12 = ePSXeNative.this.pushedButtons;
                                iArr12[npad] = iArr12[npad] & (-33);
                                break;
                            }
                        case 99:
                            if (action == 0) {
                                int[] iArr13 = ePSXeNative.this.pushedButtons;
                                iArr13[npad] = iArr13[npad] | 128;
                                break;
                            } else {
                                int[] iArr14 = ePSXeNative.this.pushedButtons;
                                iArr14[npad] = iArr14[npad] & (-129);
                                break;
                            }
                        case 100:
                            if (action == 0) {
                                int[] iArr15 = ePSXeNative.this.pushedButtons;
                                iArr15[npad] = iArr15[npad] | 16;
                                break;
                            } else {
                                int[] iArr16 = ePSXeNative.this.pushedButtons;
                                iArr16[npad] = iArr16[npad] & (-17);
                                break;
                            }
                        case 102:
                            if (action == 0) {
                                int[] iArr17 = ePSXeNative.this.pushedButtons;
                                iArr17[npad] = iArr17[npad] | 4;
                                break;
                            } else {
                                int[] iArr18 = ePSXeNative.this.pushedButtons;
                                iArr18[npad] = iArr18[npad] & (-5);
                                break;
                            }
                        case 103:
                            if (action == 0) {
                                int[] iArr19 = ePSXeNative.this.pushedButtons;
                                iArr19[npad] = iArr19[npad] | 8;
                                break;
                            } else {
                                int[] iArr20 = ePSXeNative.this.pushedButtons;
                                iArr20[npad] = iArr20[npad] & (-9);
                                break;
                            }
                        case 104:
                            if (action == 0) {
                                int[] iArr21 = ePSXeNative.this.pushedButtons;
                                iArr21[npad] = iArr21[npad] | 1;
                                break;
                            } else {
                                int[] iArr22 = ePSXeNative.this.pushedButtons;
                                iArr22[npad] = iArr22[npad] & (-2);
                                break;
                            }
                        case 105:
                            if (action == 0) {
                                int[] iArr23 = ePSXeNative.this.pushedButtons;
                                iArr23[npad] = iArr23[npad] | 2;
                                break;
                            } else {
                                int[] iArr24 = ePSXeNative.this.pushedButtons;
                                iArr24[npad] = iArr24[npad] & (-3);
                                break;
                            }
                        case 106:
                            if (action == 0) {
                                int[] iArr25 = ePSXeNative.this.pushedButtons;
                                iArr25[npad] = iArr25[npad] | 512;
                                break;
                            } else {
                                int[] iArr26 = ePSXeNative.this.pushedButtons;
                                iArr26[npad] = iArr26[npad] & (-513);
                                break;
                            }
                        case 107:
                            if (action == 0) {
                                int[] iArr27 = ePSXeNative.this.pushedButtons;
                                iArr27[npad] = iArr27[npad] | 1024;
                                break;
                            } else {
                                int[] iArr28 = ePSXeNative.this.pushedButtons;
                                iArr28[npad] = iArr28[npad] & (-1025);
                                break;
                            }
                        case 108:
                            if (action == 0) {
                                int[] iArr29 = ePSXeNative.this.pushedButtons;
                                iArr29[npad] = iArr29[npad] | 2048;
                                break;
                            } else {
                                int[] iArr30 = ePSXeNative.this.pushedButtons;
                                iArr30[npad] = iArr30[npad] & (-2049);
                                break;
                            }
                        case 109:
                            if (action == 0) {
                                int[] iArr31 = ePSXeNative.this.pushedButtons;
                                iArr31[npad] = iArr31[npad] | 256;
                                break;
                            } else {
                                int[] iArr32 = ePSXeNative.this.pushedButtons;
                                iArr32[npad] = iArr32[npad] & (-257);
                                break;
                            }
                    }
                    ePSXeNative.this.f157e.setPadDataMultitap(ePSXeNative.this.pushedButtons[0], ePSXeNative.this.pushedButtons[1], ePSXeNative.this.pushedButtons[2], ePSXeNative.this.pushedButtons[3]);
                }
            }
        }

        @Override // com.bda.controller.ControllerListener
        public void onMotionEvent(com.bda.controller.MotionEvent event) {
            if (ePSXeNative.this.mogapad != -1) {
                if (ePSXeNative.this.emuStatus == 1 || ePSXeNative.this.emuStatus == 2) {
                    int x1 = (int) (event.getAxisValue(0) * 127.0f);
                    int y1 = (int) (event.getAxisValue(1) * 127.0f);
                    int x2 = (int) (event.getAxisValue(11) * 127.0f);
                    int y2 = (int) (event.getAxisValue(14) * 127.0f);
                    ePSXeNative.this.f157e.setpadanalogMoga(ePSXeNative.this.mogapad, 0, x1, y1);
                    ePSXeNative.this.f157e.setpadanalogMoga(ePSXeNative.this.mogapad, 1, x2, y2);
                }
            }
        }

        /* JADX WARN: Failed to find 'out' block for switch in B:11:0x001e. Please report as an issue. */
        @Override // com.bda.controller.ControllerListener
        public void onStateEvent(StateEvent event) {
            if (ePSXeNative.this.mogapad != -1) {
                if (ePSXeNative.this.emuStatus == 1 || ePSXeNative.this.emuStatus == 2) {
                    switch (event.getState()) {
                    }
                }
            }
        }
    }

    private void mogaStop() {
        if (this.mController != null) {
            this.mController.exit();
        }
    }

    private void mogaStart() {
        this.mController = Controller.getInstance(this);
        if (this.mController != null) {
            Log.e("moga", "mogaStart");
            GamepadMoga.init(this.mController, this);
            this.mController.setListener(this.mListener, null);
            return;
        }
        this.mogapad = -1;
    }

    private void bluezStop() {
        unregisterReceiver(this.stateCallback);
        unregisterReceiver(this.statusMonitor);
    }

    private void bluezStart() {
        registerReceiver(this.stateCallback, new IntentFilter("com.hexad.bluezime.config"));
        registerReceiver(this.stateCallback, new IntentFilter("com.hexad.bluezime.currentstate"));
        registerReceiver(this.stateCallback, new IntentFilter("com.hexad.bluezime.connected"));
        registerReceiver(this.stateCallback, new IntentFilter("com.hexad.bluezime.disconnected"));
        registerReceiver(this.stateCallback, new IntentFilter("com.hexad.bluezime.error"));
        registerReceiver(this.statusMonitor, new IntentFilter("com.hexad.bluezime.directionalchange"));
        registerReceiver(this.statusMonitor, new IntentFilter("com.hexad.bluezime.keypress"));
        Intent serviceIntent = new Intent("com.hexad.bluezime.getconfig");
        serviceIntent.setClassName(BLUEZ_IME_PACKAGE, BLUEZ_IME_SERVICE);
        serviceIntent.putExtra("com.hexad.bluezime.sessionid", "EPSXE-IME");
        startService(serviceIntent);
        Intent serviceIntent2 = new Intent("com.hexad.bluezime.getstate");
        serviceIntent2.setClassName(BLUEZ_IME_PACKAGE, BLUEZ_IME_SERVICE);
        serviceIntent2.putExtra("com.hexad.bluezime.sessionid", "EPSXE-IME");
        startService(serviceIntent2);
    }

    public void OnNativeMotion(int action, int x, int y, int source, int device_id, int pointer_id) {
        if (source == 4098) {
            if (this.mePSXeView != null) {
                this.mePSXeView.queueMotionEvent(action, x, y, pointer_id);
                return;
            }
            return;
        }
        if (source == 1048584) {
            if (action == 2) {
                action = 0;
            }
            if (action == 261 || action == 5 || action == 517) {
                action = 0;
            }
            if (action == 262 || action == 6 || action == 518) {
                action = 1;
            }
            if (action == 1 && this.virtualPadId[pointer_id] != -1) {
                int st = this.virtualPadId[pointer_id];
                this.f157e.setpadanalogXP(0, st, 0, 0);
                this.virtualPadId[pointer_id] = -1;
                this.f158xd[st] = 0;
                this.f159yd[st] = 0;
                return;
            }
            for (int ind = 0; ind < 2; ind++) {
                if (x >= this.virtualPadPos[ind][0] && x <= this.virtualPadPos[ind][2] && y >= this.virtualPadPos[ind][1] && y <= this.virtualPadPos[ind][3] && action == 0) {
                    if (this.virtualPadId[pointer_id] != -1) {
                        int ind2 = this.virtualPadId[pointer_id];
                        this.f157e.setpadanalogXP(0, ind2, 0, 0);
                        this.f158xd[ind2] = 0;
                        this.f159yd[ind2] = 0;
                    }
                    int xa = (((x - this.virtualPadPos[ind][0]) * 255) / 360) - 127;
                    int ya = (((360 - (y - this.virtualPadPos[ind][1])) * 255) / 360) - 127;
                    float ratio1 = GetRatio(xa, ya);
                    int tmpX = (((int) (xa * ratio1)) * 220) / 100;
                    int tmpY = (((int) (ya * ratio1)) * 220) / 100;
                    if (tmpX <= -127) {
                        tmpX = -128;
                    } else if (tmpX > 127) {
                        tmpX = 127;
                    }
                    if (tmpY <= -127) {
                        tmpY = -128;
                    } else if (tmpY > 127) {
                        tmpY = 127;
                    }
                    this.f157e.setpadanalogXP(0, ind, tmpX, tmpY);
                    this.f158xd[ind] = tmpX;
                    this.f159yd[ind] = tmpY;
                    this.virtualPadId[pointer_id] = ind;
                    return;
                }
            }
        }
    }

    public boolean OnNativeKey(int action, int keycode, int meta) {
        boolean ret = false;
        if (keycode == 82 || keycode == 24 || keycode == 25) {
            return false;
        }
        if (this.emuStatus == 1 || this.emuStatus == 2) {
            boolean alt = (meta & 2) == 2;
            if (action == 0) {
                if (doKeyDown(keycode, alt, -1)) {
                    ret = true;
                }
            } else if (action == 1 && doKeyUp(keycode, alt, -1)) {
                ret = true;
            }
        }
        return ret;
    }

    static {
        System.loadLibrary("xperia");
    }
}
