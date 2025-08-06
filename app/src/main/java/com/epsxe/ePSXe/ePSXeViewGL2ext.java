package com.epsxe.ePSXe;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.opengl.EGL14;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.os.Build;
import android.os.Environment;
import android.os.Vibrator;

import androidx.core.content.ContextCompat;
import androidx.core.internal.view.SupportMenu;
import android.util.Log;
import android.view.MotionEvent;
import android.view.PointerIcon;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;
import com.epsxe.ePSXe.jni.libepsxe;
import com.epsxe.ePSXe.touchscreen.Gun;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Array;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;
import javax.microedition.khronos.opengles.GL10;
import okhttp3.internal.http.StatusLine;
import org.apache.http.HttpStatus;

/* loaded from: classes.dex */
class ePSXeViewGL2ext extends GLSurfaceView implements ePSXeView {
    public static final int ACTION_POINTER_INDEX_MASK = 65280;
    public static final int ACTION_POINTER_INDEX_SHIFT = 8;
    private static final int BUTTON_DOWN = 1;
    private static final int INPUT_HWBUTTONS = 1;
    private static final int INPUT_TOUCHSCREEN = 0;
    private static final int NO_BUTTON_DOWN = 0;
    private int[][] analog_values;
    int[] apadsection;
    private int biosVersionAdvise;
    private boolean biosmsg;
    private boolean bufferPreserved;
    private float buttonMag;
    int[] dpadsection;
    private int dpadskin;

    /* renamed from: e */
    private libepsxe f170e;
    private int emu_action_dynamic;
    private int emu_enable_framelimit;
    private int emu_enable_frameskip;
    private int emu_enable_frameskip_tmp;
    private int emu_enable_mme;
    private int emu_enable_printfps;
    private int emu_enable_tools;
    private int emu_enable_tv;
    private int emu_gpu_mt_mode;
    private float emu_input_alpha;
    private boolean emu_mouse;
    private int emu_opengl_options;
    private int[] emu_pad_draw_mode;
    private int emu_pad_dynamic;
    private int[] emu_pad_mode;
    private int[] emu_pad_mode_analog;
    private int emu_pad_portrait;
    private int[] emu_pad_type;
    private int emu_pad_type_selected;
    private int emu_player_mode;
    private int emu_plugin;
    private int emu_portrait_skin;
    private int emu_screen_orientation;
    private int emu_screen_ratio;
    private int emu_sound_latency;
    private int emu_split_mode;
    private int emu_ui_pause_support;
    private int emu_verbose;
    private int emu_video_filter;
    private int emu_volumen;
    private int[][] fboResTable;
    private int gProfileAdvise;
    private float glresizeX;
    private float glresizeY;
    private boolean gprofile;
    private int gpuVersion;
    private int gpuVersionAdvise;
    private Gun gun;
    int initvirtualPad;
    private boolean license;
    private Context mContext;
    private ePSXeWrapperThread mGLRunThread;
    private EmuGLThread mGLThread;
    private int mHeight;
    private int mHeightDraw;
    private int mHeightSaved;
    private SurfaceHolder mHolder;
    private String mIsoName;
    private int mIsoSlot;
    private int mWidth;
    private int mWidthDraw;
    private int mWidthSaved;
    ePSXeReadPreferences mePSXeReadPreferences;
    private int mfps;
    private int mode;
    private int mrx;
    private int mry;
    private ePSXe myActivity;
    private int osVersion;
    private int overscan_x;
    private int overscan_y;
    float[][] padOffScreenLan;
    float[] padOffScreenLan2H;
    float[] padOffScreenLan2V;
    float[] padOffScreenPor;
    private float padResize;
    int[] padScreenExtra;
    int padScreenExtraCombo;
    int padScreenExtraEnabled;
    int[] padScreenFunc;
    float[][] padScreenResize;
    int[][] padScreenStatus;
    float[][] padSizeScreenLan;
    float[] padSizeScreenLan2H;
    float[] padSizeScreenLan2V;
    float[] padSizeScreenPor;
    private String padprofile;
    private int[] psxbuttonval;
    private int quitonexit;
    private float screenResize;
    private int screenshot;
    private int serverMode;
    private String skinName;
    private int statebuttons;
    private int[] stickyButton;
    private int tainted;
    private int[] ts_vibration;
    int[][] virtualPad;
    int[] virtualPadBit;
    int[] virtualPadId;
    int[][] virtualPadPort;
    int[][] virtualPadPos;
    private int volumenAdvise;
    private boolean hidePad = false;
    private boolean isInTouch = false;
    private ePSXeView.OnTouchListener touchListener;

    @Override
    public void setHidePad(boolean hide) {
        hidePad = hide;
    }

    @Override
    public boolean isInTouch() {
        return isInTouch;
    }

    @Override
    public void setOnTouchListener(ePSXeView.OnTouchListener listener) {
        touchListener = listener;
    }

    public native void initPluginGL();

    static /* synthetic */ int access$3710(ePSXeViewGL2ext x0) {
        int i = x0.gpuVersionAdvise;
        x0.gpuVersionAdvise = i - 1;
        return i;
    }

    static /* synthetic */ int access$3810(ePSXeViewGL2ext x0) {
        int i = x0.biosVersionAdvise;
        x0.biosVersionAdvise = i - 1;
        return i;
    }

    static /* synthetic */ int access$4010(ePSXeViewGL2ext x0) {
        int i = x0.gProfileAdvise;
        x0.gProfileAdvise = i - 1;
        return i;
    }

    public ePSXeViewGL2ext(Context context, ePSXe act, int ry) {
        super(context);
        this.mfps = 60;
        this.ts_vibration = new int[]{0, 0};
        this.emu_enable_frameskip = 0;
        this.emu_enable_frameskip_tmp = 0;
        this.emu_enable_printfps = 1;
        this.emu_player_mode = 1;
        this.emu_split_mode = 0;
        this.emu_enable_mme = 1;
        this.emu_screen_orientation = 0;
        this.emu_screen_ratio = 1;
        this.emu_pad_portrait = 0;
        this.emu_pad_draw_mode = new int[]{1, 1};
        this.emu_pad_mode = new int[]{1, 1};
        this.emu_pad_mode_analog = new int[]{0, 0};
        this.emu_portrait_skin = 0;
        this.emu_input_alpha = 0.6f;
        this.mode = 0;
        this.emu_enable_framelimit = 1;
        this.emu_enable_tv = 0;
        this.overscan_x = 30;
        this.overscan_y = 4;
        this.emu_pad_type = new int[]{0, 0};
        this.emu_pad_type_selected = 0;
        this.emu_video_filter = 0;
        this.emu_sound_latency = 0;
        this.emu_enable_tools = 0;
        this.emu_opengl_options = 0;
        this.emu_gpu_mt_mode = 0;
        this.emu_volumen = 16;
        this.license = true;
        this.gprofile = false;
        this.analog_values = new int[][]{new int[]{0, 0, 0, 0}, new int[]{0, 0, 0, 0}};
        this.mIsoSlot = 0;
        this.screenshot = 0;
        this.gpuVersion = -1;
        this.gpuVersionAdvise = InputList.KEYCODE_NUMPAD_6;
        this.skinName = "/sdcard/skin.png";
        this.stickyButton = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
        this.biosmsg = false;
        this.biosVersionAdvise = InputList.KEYCODE_NUMPAD_6;
        this.gProfileAdvise = InputList.KEYCODE_NUMPAD_6;
        this.volumenAdvise = 0;
        this.tainted = 0;
        this.quitonexit = 1;
        this.mry = 0;
        this.mrx = 0;
        this.padprofile = "";
        this.emu_pad_dynamic = 0;
        this.emu_action_dynamic = 0;
        this.serverMode = 0;
        this.statebuttons = 0;
        this.psxbuttonval = new int[]{0, 0, 256, 2048, 4, 1, 8, 2, 0, 512, 1024};
        this.buttonMag = 1.7f;
        this.dpadskin = 0;
        this.emu_verbose = 1;
        this.gun = new Gun();
        this.bufferPreserved = false;
        this.emu_plugin = 5;
        this.emu_ui_pause_support = 0;
        this.glresizeX = 1.0f;
        this.glresizeY = 1.0f;
        this.emu_mouse = false;
        this.fboResTable = new int[][]{new int[]{640, 480}, new int[]{800, 600}, new int[]{1024, 768}};
        this.mWidth = 800;
        this.mHeight = 480;
        this.mWidthSaved = 0;
        this.mHeightSaved = 0;
        this.mWidthDraw = 800;
        this.mHeightDraw = 480;
        this.padResize = 1.0f;
        this.screenResize = 1.0f;
        this.padSizeScreenLan = new float[][]{new float[]{228.0f, 228.0f, 224.0f, 248.0f, 66.0f, 50.0f, 66.0f, 62.0f, 64.0f, 60.0f, 64.0f, 60.0f, 64.0f, 60.0f, 64.0f, 60.0f, 53.0f, 41.0f, 64.0f, 60.0f, 64.0f, 60.0f, 222.0f, 222.0f, 222.0f, 222.0f, 71.0f, 71.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f}, new float[]{228.0f, 228.0f, 224.0f, 248.0f, 66.0f, 50.0f, 66.0f, 62.0f, 64.0f, 60.0f, 64.0f, 60.0f, 64.0f, 60.0f, 64.0f, 60.0f, 53.0f, 41.0f, 64.0f, 60.0f, 64.0f, 60.0f, 222.0f, 222.0f, 222.0f, 222.0f, 71.0f, 71.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f}};
        this.padOffScreenLan = new float[][]{new float[]{this.padSizeScreenLan[0][0] / 2.0f, this.padSizeScreenLan[0][1] / 2.0f, 800.0f - (this.padSizeScreenLan[0][2] / 2.0f), this.padSizeScreenLan[0][3] / 2.0f, (400.0f - this.padSizeScreenLan[0][4]) - 30.0f, this.padSizeScreenLan[0][5] / 2.0f, 430.0f, this.padSizeScreenLan[0][7] / 2.0f, this.padSizeScreenLan[0][8] / 2.0f, 480.0f - (this.padSizeScreenLan[0][9] / 2.0f), (this.padSizeScreenLan[0][10] / 2.0f) + this.padSizeScreenLan[0][8], 480.0f - (this.padSizeScreenLan[0][11] / 2.0f), 800.0f - (this.padSizeScreenLan[0][12] / 2.0f), 480.0f - (this.padSizeScreenLan[0][13] / 2.0f), (800.0f - (this.padSizeScreenLan[0][14] / 2.0f)) - this.padSizeScreenLan[0][12], 480.0f - (this.padSizeScreenLan[0][15] / 2.0f), 440.0f + this.padSizeScreenLan[0][6], this.padSizeScreenLan[0][17] / 2.0f, (this.padSizeScreenLan[0][18] / 2.0f) + this.padSizeScreenLan[0][10] + this.padSizeScreenLan[0][8], 480.0f - (this.padSizeScreenLan[0][19] / 2.0f), ((800.0f - (this.padSizeScreenLan[0][20] / 2.0f)) - this.padSizeScreenLan[0][12]) - this.padSizeScreenLan[0][14], 480.0f - (this.padSizeScreenLan[0][21] / 2.0f), this.padSizeScreenLan[0][22] / 2.0f, this.padSizeScreenLan[0][23] / 2.0f, 800.0f - (this.padSizeScreenLan[0][24] / 2.0f), this.padSizeScreenLan[0][25] / 2.0f, 0.0f, 0.0f, this.padSizeScreenLan[0][28] / 2.0f, (480.0f - (this.padSizeScreenLan[0][29] / 2.0f)) - (this.padSizeScreenLan[0][10] * 2.0f), (this.padSizeScreenLan[0][30] / 2.0f) + this.padSizeScreenLan[0][28], (480.0f - (this.padSizeScreenLan[0][31] / 2.0f)) - (this.padSizeScreenLan[0][10] * 2.0f), (this.padSizeScreenLan[0][32] / 2.0f) + this.padSizeScreenLan[0][28] + this.padSizeScreenLan[0][30], (480.0f - (this.padSizeScreenLan[0][33] / 2.0f)) - (this.padSizeScreenLan[0][10] * 2.0f), 800.0f - (this.padSizeScreenLan[0][34] / 2.0f), (480.0f - (this.padSizeScreenLan[0][35] / 2.0f)) - (this.padSizeScreenLan[0][10] * 2.0f), (800.0f - (this.padSizeScreenLan[0][36] / 2.0f)) - this.padSizeScreenLan[0][34], (480.0f - (this.padSizeScreenLan[0][37] / 2.0f)) - (this.padSizeScreenLan[0][10] * 2.0f), ((800.0f - (this.padSizeScreenLan[0][38] / 2.0f)) - this.padSizeScreenLan[0][34]) - this.padSizeScreenLan[0][36], (480.0f - (this.padSizeScreenLan[0][39] / 2.0f)) - (this.padSizeScreenLan[0][10] * 2.0f)}, new float[]{400.0f - ((this.padSizeScreenLan[1][0] / 2.0f) * 0.76f), 240.0f, 800.0f - (this.padSizeScreenLan[1][2] / 2.0f), this.padSizeScreenLan[1][3] / 2.0f, (400.0f - this.padSizeScreenLan[1][4]) - 30.0f, this.padSizeScreenLan[1][5] / 2.0f, 430.0f, this.padSizeScreenLan[1][7] / 2.0f, this.padSizeScreenLan[1][8] / 2.0f, 480.0f - (this.padSizeScreenLan[1][9] / 2.0f), (this.padSizeScreenLan[1][10] / 2.0f) + this.padSizeScreenLan[1][8], 480.0f - (this.padSizeScreenLan[1][11] / 2.0f), 800.0f - (this.padSizeScreenLan[1][12] / 2.0f), 480.0f - (this.padSizeScreenLan[1][13] / 2.0f), (800.0f - (this.padSizeScreenLan[1][14] / 2.0f)) - this.padSizeScreenLan[1][12], 480.0f - (this.padSizeScreenLan[1][15] / 2.0f), 440.0f + this.padSizeScreenLan[1][6], this.padSizeScreenLan[1][17] / 2.0f, (this.padSizeScreenLan[1][18] / 2.0f) + this.padSizeScreenLan[1][10] + this.padSizeScreenLan[1][8], 480.0f - (this.padSizeScreenLan[1][19] / 2.0f), ((800.0f - (this.padSizeScreenLan[1][20] / 2.0f)) - this.padSizeScreenLan[1][12]) - this.padSizeScreenLan[1][14], 480.0f - (this.padSizeScreenLan[1][21] / 2.0f), this.padSizeScreenLan[1][22] / 2.0f, this.padSizeScreenLan[1][23] / 2.0f, 400.0f + ((this.padSizeScreenLan[1][24] / 2.0f) * 0.76f), 240.0f, 0.0f, 0.0f, this.padSizeScreenLan[1][28] / 2.0f, (480.0f - (this.padSizeScreenLan[1][29] / 2.0f)) - (this.padSizeScreenLan[1][10] * 2.0f), (this.padSizeScreenLan[1][30] / 2.0f) + this.padSizeScreenLan[1][28], (480.0f - (this.padSizeScreenLan[1][31] / 2.0f)) - (this.padSizeScreenLan[1][10] * 2.0f), (this.padSizeScreenLan[1][32] / 2.0f) + this.padSizeScreenLan[1][28] + this.padSizeScreenLan[1][30], (480.0f - (this.padSizeScreenLan[1][33] / 2.0f)) - (this.padSizeScreenLan[1][10] * 2.0f), 800.0f - (this.padSizeScreenLan[1][34] / 2.0f), (480.0f - (this.padSizeScreenLan[1][35] / 2.0f)) - (this.padSizeScreenLan[1][10] * 2.0f), (800.0f - (this.padSizeScreenLan[1][36] / 2.0f)) - this.padSizeScreenLan[1][34], (480.0f - (this.padSizeScreenLan[1][37] / 2.0f)) - (this.padSizeScreenLan[1][10] * 2.0f), ((800.0f - (this.padSizeScreenLan[1][38] / 2.0f)) - this.padSizeScreenLan[1][34]) - this.padSizeScreenLan[1][36], (480.0f - (this.padSizeScreenLan[1][39] / 2.0f)) - (this.padSizeScreenLan[1][10] * 2.0f)}};
        this.padScreenStatus = new int[][]{new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 0, 0, 0, 0, 0, 0, 0}, new int[]{0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0}};
        this.padScreenResize = new float[][]{new float[]{1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f}, new float[]{0.76f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.76f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f}};
        this.padScreenExtra = new int[]{19, 19, 19, 19, 19, 19};
        this.padScreenFunc = new int[]{0, 0, 0, 0, 0, 0};
        this.padScreenExtraEnabled = 0;
        this.padScreenExtraCombo = 0;
        this.dpadsection = new int[]{15, 15, 19, 19, 14, 14, 18, 18, 13, 17, 17, 12, 12, 16, 16, 15, 15};
        this.apadsection = new int[]{7, 7, 7, 6, 6, 6, 6, 5, 5, 5, 4, 4, 4, 4, 7, 7, 7};
        this.padSizeScreenPor = new float[]{480.0f, 400.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f};
        this.padOffScreenPor = new float[]{240.0f, 200.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 288.0f + (this.padSizeScreenPor[28] / 2.0f), 332.0f - (this.padSizeScreenPor[29] / 2.0f), 352.0f + (this.padSizeScreenPor[30] / 2.0f), 332.0f - (this.padSizeScreenPor[31] / 2.0f), 416.0f + (this.padSizeScreenPor[32] / 2.0f), 332.0f - (this.padSizeScreenPor[33] / 2.0f), 8.0f + (this.padSizeScreenPor[34] / 2.0f), 62.0f - (this.padSizeScreenPor[35] / 2.0f), 72.0f + (this.padSizeScreenPor[36] / 2.0f), 62.0f - (this.padSizeScreenPor[37] / 2.0f), 136.0f + (this.padSizeScreenPor[38] / 2.0f), 62.0f - (this.padSizeScreenPor[39] / 2.0f)};
        this.padSizeScreenLan2H = new float[]{228.0f, 114.0f, 224.0f, 124.0f, 66.0f, 25.0f, 66.0f, 31.0f, 64.0f, 30.0f, 64.0f, 30.0f, 64.0f, 30.0f, 64.0f, 30.0f};
        this.padOffScreenLan2H = new float[]{this.padSizeScreenLan2H[0] / 2.0f, this.padSizeScreenLan2H[1] / 2.0f, 800.0f - (this.padSizeScreenLan2H[2] / 2.0f), this.padSizeScreenLan2H[3] / 2.0f, (400.0f - this.padSizeScreenLan2H[4]) - 30.0f, this.padSizeScreenLan2H[5] / 2.0f, 430.0f, this.padSizeScreenLan2H[7] / 2.0f, this.padSizeScreenLan2H[8] / 2.0f, 240.0f - (this.padSizeScreenLan2H[9] / 2.0f), (this.padSizeScreenLan2H[10] / 2.0f) + this.padSizeScreenLan2H[8], 240.0f - (this.padSizeScreenLan2H[11] / 2.0f), 800.0f - (this.padSizeScreenLan2H[12] / 2.0f), 240.0f - (this.padSizeScreenLan2H[13] / 2.0f), (800.0f - (this.padSizeScreenLan2H[14] / 2.0f)) - this.padSizeScreenLan2H[12], 240.0f - (this.padSizeScreenLan2H[15] / 2.0f)};
        this.padSizeScreenLan2V = new float[]{136.0f, 190.0f, 134.0f, 206.0f, 39.0f, 33.0f, 39.0f, 51.0f, 38.0f, 50.0f, 38.0f, 50.0f, 38.0f, 50.0f, 38.0f, 50.0f};
        this.padOffScreenLan2V = new float[]{this.padSizeScreenLan2V[0] / 2.0f, this.padSizeScreenLan2V[1] / 2.0f, 480.0f - (this.padSizeScreenLan2V[2] / 2.0f), this.padSizeScreenLan2V[3] / 2.0f, (240.0f - this.padSizeScreenLan2V[4]) - 30.0f, this.padSizeScreenLan2V[5] / 2.0f, 270.0f, this.padSizeScreenLan2V[7] / 2.0f, this.padSizeScreenLan2V[8] / 2.0f, 400.0f - (this.padSizeScreenLan2V[9] / 2.0f), (this.padSizeScreenLan2V[10] / 2.0f) - this.padSizeScreenLan2V[8], 400.0f - (this.padSizeScreenLan2V[11] / 2.0f), 480.0f - (this.padSizeScreenLan2V[12] / 2.0f), 400.0f - (this.padSizeScreenLan2V[13] / 2.0f), (480.0f - (this.padSizeScreenLan2V[14] / 2.0f)) - this.padSizeScreenLan2V[12], 400.0f - (this.padSizeScreenLan2V[15] / 2.0f)};
        this.virtualPad = new int[][]{new int[]{5, 0, 0, 55, 50, 1}, new int[]{7, 0, 0, 55, 50, 2}, new int[]{4, 0, 0, 55, 50, 4}, new int[]{6, 0, 0, 55, 50, 8}, new int[]{1, 72, 0, InputList.KEYCODE_NUMPAD_8, 80, 16}, new int[]{1, InputList.KEYCODE_F10, 83, 220, InputList.KEYCODE_NUMPAD_RIGHT_PAREN, 32}, new int[]{1, 72, InputList.KEYCODE_VOLUME_MUTE, InputList.KEYCODE_NUMPAD_8, 244, 64}, new int[]{1, 0, 83, 90, InputList.KEYCODE_NUMPAD_RIGHT_PAREN, 128}, new int[]{2, 0, 0, 57, 50, 256}, new int[]{9, 0, 0, 55, 50, 512}, new int[]{10, 0, 0, 55, 50, 1024}, new int[]{3, 0, 0, 59, 50, 2048}, new int[]{0, 74, 0, InputList.KEYCODE_NUMPAD_4, 74, 4096}, new int[]{0, InputList.KEYCODE_NUMPAD_4, 74, 222, InputList.KEYCODE_NUMPAD_4, 8192}, new int[]{0, 74, InputList.KEYCODE_NUMPAD_4, InputList.KEYCODE_NUMPAD_4, 222, 16384}, new int[]{0, 0, 74, 74, InputList.KEYCODE_NUMPAD_4, 32768}, new int[]{0, 0, 0, 74, 74, 36864}, new int[]{0, InputList.KEYCODE_NUMPAD_4, 0, 222, 74, 12288}, new int[]{0, InputList.KEYCODE_NUMPAD_4, InputList.KEYCODE_NUMPAD_4, 222, 222, 24576}, new int[]{0, 0, InputList.KEYCODE_NUMPAD_4, 74, 222, 49152}, new int[]{11, 0, 0, 222, 222, 0}, new int[]{12, 0, 0, 222, 222, 0}, new int[]{8, 0, 0, 53, 41, 0}, new int[]{16, 0, 0, 63, 63, 0}, new int[]{17, 0, 0, 63, 63, 0}, new int[]{18, 0, 0, 63, 63, 0}, new int[]{19, 0, 0, 63, 63, 0}, new int[]{20, 0, 0, 63, 63, 0}, new int[]{21, 0, 0, 63, 63, 0}};
        this.virtualPadPort = new int[][]{new int[]{0, 0, 10, 68, 58, 4}, new int[]{0, HttpStatus.SC_REQUEST_URI_TOO_LONG, 10, 482, 58, 8}, new int[]{0, 70, 10, InputList.KEYCODE_F8, 58, 1}, new int[]{0, 344, 10, HttpStatus.SC_PRECONDITION_FAILED, 58, 2}, new int[]{0, 324, InputList.KEYCODE_NUMPAD_ENTER, HttpStatus.SC_PAYMENT_REQUIRED, 242, 16}, new int[]{0, HttpStatus.SC_NOT_FOUND, 224, 482, 306, 32}, new int[]{0, 324, HttpStatus.SC_MOVED_TEMPORARILY, HttpStatus.SC_PAYMENT_REQUIRED, HttpStatus.SC_NOT_ACCEPTABLE, 64}, new int[]{0, 248, 224, 326, 306, 128}, new int[]{0, InputList.KEYCODE_ZOOM_IN, 10, 230, 62, 256}, new int[]{-1, 0, 0, 0, 0, 512}, new int[]{-1, 0, 0, 0, 0, 1024}, new int[]{0, 244, 10, 316, 62, 2048}, new int[]{0, 74, 106, InputList.KEYCODE_NUMPAD_4, 180, 4096}, new int[]{0, InputList.KEYCODE_NUMPAD_4, 180, 222, 254, 8192}, new int[]{0, 74, 254, InputList.KEYCODE_NUMPAD_4, 328, 16384}, new int[]{0, 0, 180, 74, 254, 32768}, new int[]{0, 0, 106, 74, 180, 36864}, new int[]{0, InputList.KEYCODE_NUMPAD_4, 106, 222, 180, 12288}, new int[]{0, InputList.KEYCODE_NUMPAD_4, 254, 222, 328, 24576}, new int[]{0, 0, 254, 74, 328, 49152}, new int[]{-1, 0, 0, 222, 222, 0}, new int[]{-1, 0, 0, 222, 222, 0}, new int[]{-1, 0, 0, 53, 41, 0}, new int[]{16, 288, 68, 351, InputList.KEYCODE_F1, 0}, new int[]{17, 352, 68, HttpStatus.SC_UNSUPPORTED_MEDIA_TYPE, InputList.KEYCODE_F1, 0}, new int[]{18, HttpStatus.SC_REQUESTED_RANGE_NOT_SATISFIABLE, 68, 479, InputList.KEYCODE_F1, 0}, new int[]{19, 8, 338, 71, HttpStatus.SC_UNAUTHORIZED, 0}, new int[]{20, 72, 338, InputList.KEYCODE_F5, HttpStatus.SC_UNAUTHORIZED, 0}, new int[]{21, 136, 338, InputList.KEYCODE_BUTTON_12, HttpStatus.SC_UNAUTHORIZED, 0}};
        this.virtualPadPos = (int[][]) Array.newInstance((Class<?>) Integer.TYPE, 60, 6);
        this.virtualPadBit = new int[60];
        this.virtualPadId = new int[60];
        this.initvirtualPad = 0;
        this.mContext = context;
        init();
        this.mHolder = getHolder();
        this.mHolder.addCallback(this);
        this.mHolder.setType(2);
        this.mry = ry;
        this.myActivity = act;
    }

    @Override // android.opengl.GLSurfaceView, android.view.SurfaceHolder.Callback
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.e("ePSXeView", "onSurfaceChanged " + width + "x" + height);
        if (this.mry == 0) {
            this.mWidth = width;
            this.mHeight = height;
            this.mWidthDraw = width;
            this.mHeightDraw = height;
            this.screenResize = 1.0f;
        } else if (this.mrx == 0) {
            this.mWidth = width;
            this.mHeight = height;
            if (this.mry < height) {
                this.mHeightDraw = this.mry;
                this.mWidthDraw = (this.mry * width) / height;
                this.screenResize = (float)this.mHeightDraw / this.mHeight;
                this.mHolder.setFixedSize(this.mWidthDraw, this.mHeightDraw);
            } else {
                this.mWidthDraw = width;
                this.mHeightDraw = height;
                this.screenResize = 1.0f;
            }
            this.mrx = 1;
        }
        redoPads();
        this.f170e.setwh(this.mWidthDraw, this.mHeightDraw, this.emu_screen_ratio);
    }

    @Override // android.opengl.GLSurfaceView, android.view.SurfaceHolder.Callback
    public void surfaceCreated(SurfaceHolder holder) {
        Log.e("epsxegl", "create surface");
        this.osVersion = Integer.parseInt(Build.VERSION.SDK);
        this.mGLThread = new EmuGLThread(this);
        this.mGLThread.start();
        setRenderer(new ePSXeRendererGL2ext());
        this.emu_ui_pause_support = 0;
    }

    public void stopEmulation(Boolean doQuit) {
        boolean retry = true;
        this.mGLThread.requestStop();
        while (retry) {
            try {
                this.mGLThread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
        try {
            this.mGLThread.stop();
        } catch (Exception e2) {
        }
        Log.e("epsxetf", "surfaceDestroyed");
        if (doQuit.booleanValue() && this.quitonexit == 1 && this.f170e != null) {
            this.f170e.quit();
        }
    }

    @Override // android.opengl.GLSurfaceView, android.view.SurfaceHolder.Callback
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (this.emu_ui_pause_support == 0) {
            stopEmulation(true);
        }
    }

    private void init() {
        setFocusable(true);
        setFocusableInTouchMode(true);
        requestFocus();
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setquitonexit() {
        this.quitonexit = 0;
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setanalogdebug(int lx1, int ly1, int lx2, int ily2) {
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public boolean setIsoName(String name, int slot, String gpu) {
        this.mIsoName = name;
        this.mIsoSlot = slot;
        Log.e("epsxegl", "library name=" + gpu);
        try {
            System.load(gpu);
            initPluginGL();
            return true;
        } catch (UnsatisfiedLinkError e) {
            Log.e("epsxegl", "unable to load plugin=" + gpu);
            return false;
        }
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setSaveslot(int slot) {
        this.screenshot = slot;
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setePSXeLib(libepsxe epsxelib, int glVersion, int net) {
        this.f170e = epsxelib;
        setEGLContextClientVersion(2);
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setfps(int fps) {
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setlicense(boolean nowlicense) {
        this.license = nowlicense;
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setcustomgameprofile(boolean gp) {
        this.gprofile = gp;
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setbiosmsg(boolean msg) {
        this.biosmsg = msg;
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setdebugstring(String s) {
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setdebugstring2(String s) {
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setinputvibration(int vibration1, int vibration2) {
        this.ts_vibration[0] = vibration1;
        this.ts_vibration[1] = vibration2;
        Log.e("epsxeView", "InputVibrate1 = " + vibration1);
        Log.e("epsxeView", "InputVibrate2 = " + vibration2);
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setinputalpha(float alpha) {
        this.emu_input_alpha = alpha;
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setinputmag(float mag) {
        this.buttonMag = mag;
        if (mag < 0.7d || mag > 2.0d) {
        }
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setframeskip(int fs) {
        this.emu_enable_frameskip = fs;
        this.emu_enable_frameskip_tmp = fs;
        Log.e("epsxeView", "FrameSkip = " + fs);
        this.emu_enable_frameskip = this.f170e.setFrameSkip(this.emu_enable_frameskip);
        this.emu_enable_frameskip_tmp = this.emu_enable_frameskip;
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setscreenorientation(int ori) {
        Log.e("epsxeView", "Orientation = " + ori);
        if (ori == 3) {
            ori = 0;
        }
        this.emu_screen_orientation = this.f170e.setscreenorientation(ori);
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setportraitmode(int mode) {
        if (mode == 6) {
            this.emu_portrait_skin = 0;
            return;
        }
        this.emu_portrait_skin = 1;
        this.emu_pad_mode[0] = mode;
        this.padprofile = "PFP1";
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setscreenvrmode(int vrmode, int vrdistorsion) {
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setscreenratio(int rat) {
        Log.e("epsxeView", "Ratio = " + rat);
        this.emu_screen_ratio = rat;
        this.f170e.setwh(this.mWidthDraw, this.mHeightDraw, this.emu_screen_ratio);
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void updatescreenratio(int rat) {
        Log.e("epsxeView", "Ratio = " + rat);
        this.emu_screen_ratio = rat;
        this.f170e.updatewh(this.mWidthDraw, this.mHeightDraw, this.emu_screen_ratio);
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setscreendepth(int dep) {
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setscreenblackbands(int bb) {
        Log.e("epsxeView", "blackbands = " + bb);
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setvideofilter(int fil) {
        Log.e("epsxeView", "Filter = " + fil);
        this.emu_video_filter = fil;
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setgpumtmodeH(int modeH) {
        this.emu_gpu_mt_mode = modeH;
        this.f170e.setGpuMtMode(modeH);
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setgpumtmodeS(int modeS) {
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setvideodither(int fil) {
        Log.e("epsxeView", "PSX Dither = " + fil);
        this.f170e.setDithering(fil);
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setvideofilterhw(int fil) {
        Log.e("epsxeView", "PSX Filter hw = " + fil);
        this.f170e.setFilterhw(fil);
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setplayermode(int mode) {
        this.emu_player_mode = mode;
        Log.e("epsxeView", "PlayerMode = " + mode);
        this.emu_player_mode = this.f170e.setPlayerMode(this.emu_player_mode);
        if (this.emu_player_mode == 10) {
            if (this.emu_screen_orientation != 0) {
                this.emu_split_mode = 0;
            } else {
                this.emu_split_mode = 1;
            }
        }
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setsplitmode(int mode) {
        this.emu_split_mode = mode;
        Log.e("epsxeView", "SplitMode = " + mode);
        this.emu_split_mode = this.f170e.setSplitMode(mode);
        this.initvirtualPad = 0;
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setsoundlatency(int mode) {
        Log.e("epsxeView", "SoundLatency = " + mode);
        this.emu_sound_latency = this.f170e.setSoundLatency(mode);
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void toggletools() {
        this.emu_enable_tools ^= 1;
        this.emu_opengl_options = this.f170e.gpugetoptiongl();
        Log.e("epsxeView", "emu_opengl_options " + this.emu_opengl_options);
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void toggleframelimit() {
        if (this.emu_enable_framelimit == 1) {
            this.emu_enable_frameskip_tmp = this.emu_enable_frameskip;
            this.emu_enable_frameskip = 0;
            this.emu_enable_frameskip = this.f170e.setFrameSkip(this.emu_enable_frameskip);
            this.emu_enable_framelimit ^= 1;
            this.emu_enable_framelimit = this.f170e.setFrameLimit(this.emu_enable_framelimit);
            return;
        }
        this.emu_enable_frameskip = this.emu_enable_frameskip_tmp;
        this.emu_enable_frameskip = this.f170e.setFrameSkip(this.emu_enable_frameskip);
        this.emu_enable_framelimit ^= 1;
        this.emu_enable_framelimit = this.f170e.setFrameLimit(this.emu_enable_framelimit);
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setframelimit() {
        this.emu_enable_frameskip = this.emu_enable_frameskip_tmp;
        this.emu_enable_frameskip = this.f170e.setFrameSkip(this.emu_enable_frameskip);
        this.emu_enable_framelimit = 1;
        this.emu_enable_framelimit = this.f170e.setFrameLimit(this.emu_enable_framelimit);
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void unsetframelimit() {
        this.emu_enable_frameskip_tmp = this.emu_enable_frameskip;
        this.emu_enable_frameskip = 0;
        this.emu_enable_frameskip = this.f170e.setFrameSkip(this.emu_enable_frameskip);
        this.emu_enable_framelimit = 0;
        this.emu_enable_framelimit = this.f170e.setFrameLimit(this.emu_enable_framelimit);
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setshowfps(int fps) {
        this.emu_enable_printfps = fps;
        Log.e("epsxeView", "CpuShowFPS " + fps);
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setinputpaintpadmode(int mode, int mode2) {
        this.emu_pad_draw_mode[0] = mode;
        this.emu_pad_draw_mode[1] = mode2;
        Log.e("epsxeView", "PadMode " + mode);
        Log.e("epsxeView", "PadMode2 " + mode2);
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setinputskinname(String name) {
        this.skinName = name;
        Log.e("epsxeView", "skinName " + name);
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setinputpadtype(int type, int type2) {
        this.emu_pad_type[0] = type;
        this.emu_pad_type[1] = type2;
        Log.e("epsxeView", "PadType " + type);
        Log.e("epsxeView", "PadType2 " + type2);
        if (this.emu_pad_type[0] != 0) {
            this.emu_pad_type_selected = 1;
        } else {
            this.emu_pad_type_selected = 0;
        }
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setinputpadmode(int mode1, int mode2, int analog1, int analog2) {
        if (this.emu_player_mode == 1) {
            this.emu_pad_mode[0] = mode1;
            this.emu_pad_mode[1] = mode2;
            this.f170e.setpadmode(0, mode1);
            this.f170e.setpadmode(1, mode2);
            if (mode1 == 4) {
                this.emu_pad_mode_analog[0] = analog1;
                this.mode = analog1;
                this.f170e.setpadanalogmode(0, this.emu_pad_mode_analog[0]);
            } else if (mode1 == 1) {
                this.emu_pad_mode_analog[0] = 0;
                this.mode = 0;
                this.f170e.setpadanalogmode(0, this.emu_pad_mode_analog[0]);
            }
            if (mode2 == 4) {
                this.emu_pad_mode_analog[1] = analog2;
                this.f170e.setpadanalogmode(1, this.emu_pad_mode_analog[1]);
            } else if (mode2 == 1) {
                this.emu_pad_mode_analog[1] = 0;
                this.f170e.setpadanalogmode(1, this.emu_pad_mode_analog[1]);
            }
            if (mode1 == 2) {
                this.emu_pad_mode_analog[0] = 1;
                this.f170e.setpadanalogmode(0, this.emu_pad_mode_analog[0]);
                this.emu_mouse = true;
            }
            if (mode2 == 2) {
                this.emu_pad_mode_analog[1] = 1;
                this.f170e.setpadanalogmode(1, this.emu_pad_mode_analog[1]);
                this.emu_mouse = true;
            }
            if (mode1 == 4 || mode1 == 1) {
                init_motionevent_1playerLandscape();
            } else if (mode1 == 3 || mode1 == 8) {
                this.emu_pad_mode_analog[0] = 1;
                this.mode = 1;
                this.f170e.setpadanalogmode(0, this.emu_pad_mode_analog[0]);
                this.gun.initGun(this.mWidth, this.mHeight, this.emu_pad_type_selected);
            }
            Log.e("epsxeView", "PadMode " + mode1);
            Log.e("epsxeView", "PadMode2 " + mode2);
        }
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setinputpadmodeanalog(int p) {
        if (this.emu_player_mode == 1) {
            if (this.emu_pad_mode[p] == 4) {
                int[] iArr = this.emu_pad_mode_analog;
                iArr[p] = iArr[p] ^ 1;
                this.f170e.setpadanalogmode(p, this.emu_pad_mode_analog[p]);
            }
            if (p == 0) {
                this.mode = this.emu_pad_mode_analog[p];
            }
        }
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setsnaprestoring(boolean snaprestoring) {
        Log.e("epsxeView", "loadtmp_snap ");
        this.f170e.loadtmpsnap();
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setautosnaprestoring() {
        Log.e("epsxeView", "loadtmp_snap ");
        this.f170e.loadautosnap();
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void settainted(int mode) {
        this.tainted = mode;
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setdevice(int dev) {
        this.emu_enable_tv = dev;
        if (this.emu_enable_tv != 0) {
            this.overscan_x = 60;
            this.overscan_y = 27;
        }
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setinputprofile(int profile) {
        String str = this.padprofile;
        if (this.emu_screen_orientation != 1) {
            switch (profile) {
                case 0:
                    this.padprofile = "";
                    break;
                case 1:
                    this.padprofile = "PF2";
                    break;
                case 2:
                    this.padprofile = "PF3";
                    break;
                case 3:
                    this.padprofile = "PF4";
                    break;
            }
            redoPads();
        }
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setdynamicpad(int dyn) {
        if ((this.emu_pad_mode[0] == 1 || this.emu_pad_mode[0] == 4) && this.emu_screen_orientation != 1 && this.emu_portrait_skin == 0) {
            this.emu_pad_dynamic = dyn;
        }
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setdynamicaction(int dyn) {
        if ((this.emu_pad_mode[0] == 1 || this.emu_pad_mode[0] == 4) && this.emu_screen_orientation != 1 && this.emu_portrait_skin == 0) {
            this.emu_action_dynamic = dyn;
        }
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setservermode(int sm) {
        this.serverMode = sm;
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setemuvolumen(int vol) {
        if (this.emu_volumen != vol) {
            this.emu_volumen = vol;
            this.volumenAdvise = InputList.KEYCODE_NUMPAD_6;
        }
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setverbose(int ver) {
        this.emu_verbose = ver;
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setplugin(int mode) {
        this.emu_plugin = mode;
    }

    @Override // android.view.View
    public PointerIcon onResolvePointerIcon(MotionEvent event, int pointerIndex) {
        if (!this.emu_mouse) {
            return null;
        }
        Bitmap bitmapIcon = Bitmap.createBitmap(2, 2, Bitmap.Config.ARGB_8888);
        bitmapIcon.eraseColor(14737632);
        int hotSpotX = bitmapIcon.getWidth() / 2;
        int hotSpotY = bitmapIcon.getHeight() / 2;
        return PointerIcon.create(bitmapIcon, hotSpotX, hotSpotY);
    }

    public void resetDynamicPad() {
        if (this.emu_pad_dynamic == 1) {
            if (this.emu_pad_mode[this.emu_pad_type_selected] == 1) {
                this.padScreenStatus[this.mode][0] = 0;
            } else if (this.emu_pad_mode[this.emu_pad_type_selected] == 4) {
                if (this.mode == 0) {
                    this.padScreenStatus[this.mode][0] = 0;
                } else {
                    this.padScreenStatus[this.mode][11] = 0;
                }
            }
        }
        if (this.emu_action_dynamic == 1) {
            if (this.emu_pad_mode[this.emu_pad_type_selected] == 1 || this.emu_pad_mode[this.emu_pad_type_selected] == 4) {
                this.padScreenStatus[this.mode][1] = 0;
            }
        }
    }

    public void init_motionevent_2playerH(int swap) {
        for (int n = 0; n < 20; n++) {
            if (this.virtualPad[n][0] == -1) {
                this.virtualPadBit[n] = 0;
                this.virtualPadId[n] = -1;
                this.virtualPadPos[n][0] = -1;
                this.virtualPadPos[n][1] = -1;
                this.virtualPadPos[n][2] = -1;
                this.virtualPadPos[n][3] = -1;
                this.virtualPadPos[n][4] = -1;
                this.virtualPadPos[n][5] = -1;
            } else {
                int offx = ((int) this.padOffScreenLan[this.mode][this.virtualPad[n][0] * 2]) - (((int) this.padSizeScreenLan[this.mode][this.virtualPad[n][0] * 2]) / 2);
                int offy = ((int) this.padOffScreenLan[this.mode][(this.virtualPad[n][0] * 2) + 1]) - (((int) this.padSizeScreenLan[this.mode][(this.virtualPad[n][0] * 2) + 1]) / 2);
                this.virtualPadPos[n][0] = ((int) (this.virtualPad[n][1] * this.padResize)) + offx;
                this.virtualPadPos[n][1] = (((((int) (this.virtualPad[n][2] * this.padResize)) + (this.mHeight - ((int) this.padSizeScreenLan[this.mode][(this.virtualPad[n][0] * 2) + 1]))) - offy) / 2) + (this.mHeight / 2);
                this.virtualPadPos[n][2] = ((int) (this.virtualPad[n][3] * this.padResize)) + offx;
                this.virtualPadPos[n][3] = (((((int) (this.virtualPad[n][4] * this.padResize)) + (this.mHeight - ((int) this.padSizeScreenLan[this.mode][(this.virtualPad[n][0] * 2) + 1]))) - offy) / 2) + (this.mHeight / 2);
                this.virtualPadPos[n][4] = ((this.virtualPadPos[n][2] - this.virtualPadPos[n][0]) / 2) + this.virtualPadPos[n][0];
                this.virtualPadPos[n][5] = ((this.virtualPadPos[n][3] - this.virtualPadPos[n][1]) / 2) + this.virtualPadPos[n][1];
                this.virtualPadBit[n] = this.virtualPad[n][5] | 65536;
                this.virtualPadId[n] = -1;
            }
        }
        for (int n2 = 0; n2 < 20; n2++) {
            if (this.virtualPad[n2][0] == -1) {
                this.virtualPadBit[n2 + 20] = 0;
                this.virtualPadId[n2 + 20] = -1;
                this.virtualPadPos[n2 + 20][0] = -1;
                this.virtualPadPos[n2 + 20][1] = -1;
                this.virtualPadPos[n2 + 20][2] = -1;
                this.virtualPadPos[n2 + 20][3] = -1;
                this.virtualPadPos[n2 + 20][4] = -1;
                this.virtualPadPos[n2 + 20][5] = -1;
            } else {
                int offx2 = ((int) this.padOffScreenLan[this.mode][this.virtualPad[n2][0] * 2]) - (((int) this.padSizeScreenLan[this.mode][this.virtualPad[n2][0] * 2]) / 2);
                int offy2 = ((int) this.padOffScreenLan[this.mode][(this.virtualPad[n2][0] * 2) + 1]) - (((int) this.padSizeScreenLan[this.mode][(this.virtualPad[n2][0] * 2) + 1]) / 2);
                this.virtualPadPos[n2 + 20][0] = this.mWidth - (((int) (this.virtualPad[n2][3] * this.padResize)) + offx2);
                this.virtualPadPos[n2 + 20][1] = (this.mHeight / 2) - (((((int) (this.virtualPad[n2][4] * this.padResize)) + (this.mHeight - ((int) this.padSizeScreenLan[this.mode][(this.virtualPad[n2][0] * 2) + 1]))) - offy2) / 2);
                this.virtualPadPos[n2 + 20][2] = this.mWidth - (((int) (this.virtualPad[n2][1] * this.padResize)) + offx2);
                this.virtualPadPos[n2 + 20][3] = (this.mHeight / 2) - (((((int) (this.virtualPad[n2][2] * this.padResize)) + (this.mHeight - ((int) this.padSizeScreenLan[this.mode][(this.virtualPad[n2][0] * 2) + 1]))) - offy2) / 2);
                this.virtualPadPos[n2 + 20][4] = ((this.virtualPadPos[n2 + 20][2] - this.virtualPadPos[n2 + 20][0]) / 2) + this.virtualPadPos[n2 + 20][0];
                this.virtualPadPos[n2 + 20][5] = ((this.virtualPadPos[n2 + 20][3] - this.virtualPadPos[n2 + 20][1]) / 2) + this.virtualPadPos[n2 + 20][1];
                this.virtualPadBit[n2 + 20] = this.virtualPad[n2][5];
                this.virtualPadId[n2 + 20] = -1;
            }
        }
        if (swap == 1) {
            for (int n3 = 0; n3 < 40; n3++) {
                int[] iArr = this.virtualPadBit;
                iArr[n3] = iArr[n3] ^ 65536;
            }
        }
        this.initvirtualPad = 1;
    }

    public void init_motionevent_2playerV() {
        for (int n = 0; n < 20; n++) {
            if (this.virtualPad[n][0] == -1) {
                this.virtualPadBit[n] = 0;
                this.virtualPadId[n] = -1;
                this.virtualPadPos[n][0] = -1;
                this.virtualPadPos[n][1] = -1;
                this.virtualPadPos[n][2] = -1;
                this.virtualPadPos[n][3] = -1;
                this.virtualPadPos[n][4] = -1;
                this.virtualPadPos[n][5] = -1;
            } else {
                int offx = ((int) this.padOffScreenLan[this.mode][this.virtualPad[n][0] * 2]) - (((int) this.padSizeScreenLan[this.mode][this.virtualPad[n][0] * 2]) / 2);
                int offy = ((int) this.padOffScreenLan[this.mode][(this.virtualPad[n][0] * 2) + 1]) - (((int) this.padSizeScreenLan[this.mode][(this.virtualPad[n][0] * 2) + 1]) / 2);
                int tmp1 = ((int) (this.virtualPad[n][1] * this.padResize)) + offx;
                int tmp2 = (((int) (this.virtualPad[n][2] * this.padResize)) + (this.mHeight - ((int) this.padSizeScreenLan[this.mode][(this.virtualPad[n][0] * 2) + 1]))) - offy;
                int tmp3 = ((int) (this.virtualPad[n][3] * this.padResize)) + offx;
                int tmp4 = (((int) (this.virtualPad[n][4] * this.padResize)) + (this.mHeight - ((int) this.padSizeScreenLan[this.mode][(this.virtualPad[n][0] * 2) + 1]))) - offy;
                int tmp12 = (this.mHeight * tmp1) / this.mWidth;
                int tmp22 = ((this.mWidth * tmp2) / 2) / this.mHeight;
                int tmp32 = (this.mHeight * tmp3) / this.mWidth;
                int tmp42 = ((this.mWidth * tmp4) / 2) / this.mHeight;
                this.virtualPadPos[n][0] = (this.mWidth / 2) + tmp22;
                this.virtualPadPos[n][1] = this.mHeight - tmp32;
                this.virtualPadPos[n][2] = (this.mWidth / 2) + tmp42;
                this.virtualPadPos[n][3] = this.mHeight - tmp12;
                this.virtualPadPos[n][4] = ((this.virtualPadPos[n][2] - this.virtualPadPos[n][0]) / 2) + this.virtualPadPos[n][0];
                this.virtualPadPos[n][5] = ((this.virtualPadPos[n][3] - this.virtualPadPos[n][1]) / 2) + this.virtualPadPos[n][1];
                this.virtualPadBit[n] = this.virtualPad[n][5] | 65536;
                this.virtualPadId[n] = -1;
            }
        }
        for (int n2 = 0; n2 < 20; n2++) {
            if (this.virtualPad[n2][0] == -1) {
                this.virtualPadBit[n2 + 20] = 0;
                this.virtualPadId[n2 + 20] = -1;
                this.virtualPadPos[n2 + 20][0] = -1;
                this.virtualPadPos[n2 + 20][1] = -1;
                this.virtualPadPos[n2 + 20][2] = -1;
                this.virtualPadPos[n2 + 20][3] = -1;
                this.virtualPadPos[n2 + 20][4] = -1;
                this.virtualPadPos[n2 + 20][5] = -1;
            } else {
                int offx2 = ((int) this.padOffScreenLan[this.mode][this.virtualPad[n2][0] * 2]) - (((int) this.padSizeScreenLan[this.mode][this.virtualPad[n2][0] * 2]) / 2);
                int offy2 = ((int) this.padOffScreenLan[this.mode][(this.virtualPad[n2][0] * 2) + 1]) - (((int) this.padSizeScreenLan[this.mode][(this.virtualPad[n2][0] * 2) + 1]) / 2);
                int tmp13 = ((int) (this.virtualPad[n2][1] * this.padResize)) + offx2;
                int tmp23 = (((int) (this.virtualPad[n2][2] * this.padResize)) + (this.mHeight - ((int) this.padSizeScreenLan[this.mode][(this.virtualPad[n2][0] * 2) + 1]))) - offy2;
                int tmp33 = ((int) (this.virtualPad[n2][3] * this.padResize)) + offx2;
                int tmp43 = (((int) (this.virtualPad[n2][4] * this.padResize)) + (this.mHeight - ((int) this.padSizeScreenLan[this.mode][(this.virtualPad[n2][0] * 2) + 1]))) - offy2;
                int tmp14 = (this.mHeight * tmp13) / this.mWidth;
                int tmp24 = ((this.mWidth * tmp23) / 2) / this.mHeight;
                int tmp34 = (this.mHeight * tmp33) / this.mWidth;
                this.virtualPadPos[n2 + 20][0] = (this.mWidth / 2) - (((this.mWidth * tmp43) / 2) / this.mHeight);
                this.virtualPadPos[n2 + 20][1] = tmp14;
                this.virtualPadPos[n2 + 20][2] = (this.mWidth / 2) - tmp24;
                this.virtualPadPos[n2 + 20][3] = tmp34;
                this.virtualPadPos[n2 + 20][4] = ((this.virtualPadPos[n2 + 20][2] - this.virtualPadPos[n2 + 20][0]) / 2) + this.virtualPadPos[n2 + 20][0];
                this.virtualPadPos[n2 + 20][5] = ((this.virtualPadPos[n2 + 20][3] - this.virtualPadPos[n2 + 20][1]) / 2) + this.virtualPadPos[n2 + 20][1];
                this.virtualPadBit[n2 + 20] = this.virtualPad[n2][5];
                this.virtualPadId[n2 + 20] = -1;
            }
        }
        this.initvirtualPad = 1;
    }

    public void init_motionevent_1playerPortrait() {
        for (int n = 0; n < 29; n++) {
            if (n > 22 && n < 29) {
                if (this.padScreenFunc[n - 23] == 2) {
                    if (this.padScreenExtra[n - 23] == 10) {
                        this.virtualPadPort[n][5] = 48;
                    } else if (this.padScreenExtra[n - 23] == 11) {
                        this.virtualPadPort[n][5] = 144;
                    } else if (this.padScreenExtra[n - 23] == 12) {
                        this.virtualPadPort[n][5] = 192;
                    } else if (this.padScreenExtra[n - 23] == 13) {
                        this.virtualPadPort[n][5] = 96;
                    } else if (this.padScreenExtra[n - 23] == 14) {
                        this.virtualPadPort[n][5] = 80;
                    } else if (this.padScreenExtra[n - 23] == 15) {
                        this.virtualPadPort[n][5] = 160;
                    } else if (this.padScreenExtra[n - 23] == 16) {
                        this.virtualPadPort[n][5] = 12;
                    } else if (this.padScreenExtra[n - 23] == 17) {
                        this.virtualPadPort[n][5] = 3;
                    }
                    this.virtualPad[n][0] = n - 9;
                } else if (this.padScreenFunc[n - 23] == 1) {
                    this.virtualPad[n][0] = n - 9;
                } else {
                    this.virtualPad[n][0] = -1;
                }
            }
            if (this.virtualPadPort[n][0] == -1) {
                this.virtualPadBit[n] = 0;
                this.virtualPadId[n] = -1;
                this.virtualPadPos[n][0] = -1;
                this.virtualPadPos[n][1] = -1;
                this.virtualPadPos[n][2] = -1;
                this.virtualPadPos[n][3] = -1;
                this.virtualPadPos[n][4] = -1;
                this.virtualPadPos[n][5] = -1;
            } else {
                int offy = this.mHeight / 2;
                this.virtualPadPos[n][0] = ((this.virtualPadPort[n][1] * this.mWidth) / 480) + 0;
                this.virtualPadPos[n][1] = ((this.virtualPadPort[n][2] * (this.mHeight / 2)) / 400) + offy;
                this.virtualPadPos[n][2] = ((this.virtualPadPort[n][3] * this.mWidth) / 480) + 0;
                this.virtualPadPos[n][3] = ((this.virtualPadPort[n][4] * (this.mHeight / 2)) / 400) + offy;
                this.virtualPadPos[n][4] = ((this.virtualPadPos[n][2] - this.virtualPadPos[n][0]) / 2) + this.virtualPadPos[n][0];
                this.virtualPadPos[n][5] = ((this.virtualPadPos[n][3] - this.virtualPadPos[n][1]) / 2) + this.virtualPadPos[n][1];
                this.virtualPadBit[n] = this.virtualPadPort[n][5];
                if (this.emu_pad_type_selected == 0) {
                    int[] iArr = this.virtualPadBit;
                    iArr[n] = iArr[n] | 65536;
                }
                this.virtualPadId[n] = -1;
                if (n == 20) {
                    this.analog_values[0][0] = -1;
                    this.analog_values[0][1] = -1;
                }
                if (n == 21) {
                    this.analog_values[0][2] = -1;
                    this.analog_values[0][3] = -1;
                }
            }
        }
        this.initvirtualPad = 1;
    }

    public void init_motionevent_1playerLandscape() {
        for (int n = 0; n < 29; n++) {
            if (n > 22 && n < 29) {
                if (this.padScreenFunc[n - 23] == 2) {
                    if (this.padScreenExtra[n - 23] == 10) {
                        this.virtualPad[n][5] = 48;
                    } else if (this.padScreenExtra[n - 23] == 11) {
                        this.virtualPad[n][5] = 144;
                    } else if (this.padScreenExtra[n - 23] == 12) {
                        this.virtualPad[n][5] = 192;
                    } else if (this.padScreenExtra[n - 23] == 13) {
                        this.virtualPad[n][5] = 96;
                    } else if (this.padScreenExtra[n - 23] == 14) {
                        this.virtualPad[n][5] = 80;
                    } else if (this.padScreenExtra[n - 23] == 15) {
                        this.virtualPad[n][5] = 160;
                    } else if (this.padScreenExtra[n - 23] == 16) {
                        this.virtualPad[n][5] = 12;
                    } else if (this.padScreenExtra[n - 23] == 17) {
                        this.virtualPad[n][5] = 3;
                    }
                    this.virtualPad[n][0] = n - 9;
                } else if (this.padScreenFunc[n - 23] == 1) {
                    this.virtualPad[n][0] = n - 9;
                } else {
                    this.virtualPad[n][0] = -1;
                }
            }
            if (this.virtualPad[n][0] == -1 || this.padScreenStatus[this.mode][this.virtualPad[n][0]] != 1) {
                this.virtualPadBit[n] = 0;
                this.virtualPadId[n] = -1;
                this.virtualPadPos[n][0] = -1;
                this.virtualPadPos[n][1] = -1;
                this.virtualPadPos[n][2] = -1;
                this.virtualPadPos[n][3] = -1;
                this.virtualPadPos[n][4] = -1;
                this.virtualPadPos[n][5] = -1;
            } else {
                int offx = ((int) this.padOffScreenLan[this.mode][this.virtualPad[n][0] * 2]) - ((int) ((this.padSizeScreenLan[this.mode][this.virtualPad[n][0] * 2] * this.padScreenResize[this.mode][this.virtualPad[n][0]]) / 2.0f));
                int offy = ((int) this.padOffScreenLan[this.mode][(this.virtualPad[n][0] * 2) + 1]) - ((int) ((this.padSizeScreenLan[this.mode][(this.virtualPad[n][0] * 2) + 1] * this.padScreenResize[this.mode][this.virtualPad[n][0]]) / 2.0f));
                this.virtualPadPos[n][0] = ((int) (this.virtualPad[n][1] * this.padResize * this.padScreenResize[this.mode][this.virtualPad[n][0]])) + offx;
                this.virtualPadPos[n][1] = (((int) ((this.virtualPad[n][2] * this.padResize) * this.padScreenResize[this.mode][this.virtualPad[n][0]])) + (this.mHeight - ((int) (this.padSizeScreenLan[this.mode][(this.virtualPad[n][0] * 2) + 1] * this.padScreenResize[this.mode][this.virtualPad[n][0]])))) - offy;
                this.virtualPadPos[n][2] = ((int) (this.virtualPad[n][3] * this.padResize * this.padScreenResize[this.mode][this.virtualPad[n][0]])) + offx;
                this.virtualPadPos[n][3] = (((int) ((this.virtualPad[n][4] * this.padResize) * this.padScreenResize[this.mode][this.virtualPad[n][0]])) + (this.mHeight - ((int) (this.padSizeScreenLan[this.mode][(this.virtualPad[n][0] * 2) + 1] * this.padScreenResize[this.mode][this.virtualPad[n][0]])))) - offy;
                this.virtualPadPos[n][4] = ((this.virtualPadPos[n][2] - this.virtualPadPos[n][0]) / 2) + this.virtualPadPos[n][0];
                this.virtualPadPos[n][5] = ((this.virtualPadPos[n][3] - this.virtualPadPos[n][1]) / 2) + this.virtualPadPos[n][1];
                this.virtualPadBit[n] = this.virtualPad[n][5];
                if (this.emu_pad_type_selected == 0) {
                    int[] iArr = this.virtualPadBit;
                    iArr[n] = iArr[n] | 65536;
                }
                this.virtualPadId[n] = -1;
                if (n == 20) {
                    this.analog_values[0][0] = ((this.virtualPadPos[n][2] - this.virtualPadPos[n][0]) / 2) + this.virtualPadPos[n][0];
                    this.analog_values[0][1] = this.mHeight - (((this.virtualPadPos[n][3] - this.virtualPadPos[n][1]) / 2) + this.virtualPadPos[n][1]);
                }
                if (n == 21) {
                    this.analog_values[0][2] = ((this.virtualPadPos[n][2] - this.virtualPadPos[n][0]) / 2) + this.virtualPadPos[n][0];
                    this.analog_values[0][3] = this.mHeight - (((this.virtualPadPos[n][3] - this.virtualPadPos[n][1]) / 2) + this.virtualPadPos[n][1]);
                }
            }
        }
        this.initvirtualPad = 1;
    }

    private void exec_tools2(int xi, int yi) {
        float butwf = (this.padResize * 508.0f) / 9.0f;
        int butw = (int) butwf;
        int offx = (this.mWidth - ((int) (this.padResize * 508.0f))) / 2;
        if (xi <= (butw * 1) + offx) {
            this.emu_opengl_options ^= 1;
            if ((this.emu_opengl_options & 1) == 1) {
                this.f170e.gpusetoptiongl2(1, 1, 0);
                return;
            } else {
                this.f170e.gpusetoptiongl2(0, 1, 0);
                return;
            }
        }
        if (xi <= (butw * 2) + offx) {
            int val = (((this.emu_opengl_options >> 1) & 7) + 1) & 7;
            if (val > 6) {
                val = 0;
            }
            this.emu_opengl_options = (this.emu_opengl_options & (-15)) | (val << 1);
            if (val != 0) {
                this.f170e.gpusetoptiongl2(1, val << 1, 0);
                return;
            } else {
                this.f170e.gpusetoptiongl2(0, 14, 0);
                return;
            }
        }
        if (xi <= (butw * 3) + offx) {
            this.emu_opengl_options ^= 16;
            if ((this.emu_opengl_options & 16) == 16) {
                this.f170e.gpusetoptiongl2(1, 16, 0);
                return;
            } else {
                this.f170e.gpusetoptiongl2(0, 16, 0);
                return;
            }
        }
        if (xi <= (butw * 4) + offx) {
            int val2 = (((this.emu_opengl_options >> 5) & 3) + 1) & 3;
            if (val2 > 2) {
                val2 = 0;
            }
            this.emu_opengl_options = (this.emu_opengl_options & (-97)) | (val2 << 5);
            if (val2 != 0) {
                this.f170e.gpusetoptiongl2(0, val2 << 5, 0);
                return;
            } else {
                this.f170e.gpusetoptiongl2(1, 96, 0);
                return;
            }
        }
        if (xi <= (butw * 5) + offx) {
            int val3 = (((this.emu_opengl_options >> 7) & 3) + 1) & 3;
            this.emu_opengl_options = (this.emu_opengl_options & (-385)) | (val3 << 7);
            if (val3 != 0) {
                this.f170e.gpusetoptiongl2(0, val3 << 7, 0);
                return;
            } else {
                this.f170e.gpusetoptiongl2(1, 384, 0);
                return;
            }
        }
        if (xi <= (butw * 6) + offx) {
            int val4 = ((this.emu_opengl_options >> 9) & 3) + 1;
            if (val4 > 2) {
                val4 = 0;
            }
            this.emu_opengl_options = (this.emu_opengl_options & (-1537)) | (val4 << 9);
            if (val4 != 0) {
                this.f170e.gpusetoptiongl2(0, val4 << 9, 0);
                return;
            } else {
                this.f170e.gpusetoptiongl2(1, 1536, 0);
                return;
            }
        }
        if (xi <= (butw * 7) + offx) {
            this.emu_opengl_options ^= 2048;
            if ((this.emu_opengl_options & 2048) == 2048) {
                this.f170e.gpusetoptiongl2(1, 2048, 0);
                return;
            } else {
                this.f170e.gpusetoptiongl2(0, 2048, 0);
                return;
            }
        }
        if (xi <= (butw * 8) + offx) {
            this.f170e.gpusaveoptiongl2(this.emu_opengl_options);
            Toast.makeText(this.mContext, R.string.viewglext_restore, 0).show();
        } else if (xi <= (butw * 9) + offx) {
            this.emu_enable_tools ^= 1;
        }
    }

    private void exec_tools(int xi, int yi) {
        float butwf = (this.padResize * 508.0f) / 9.0f;
        int butw = (int) butwf;
        int offx = (this.mWidth - ((int) (this.padResize * 508.0f))) / 2;
        if (xi <= (butw * 1) + offx) {
            if ((this.emu_opengl_options & 4096) != 0) {
                this.f170e.gpusetoptiongl(0, 4096, 0);
            } else {
                this.f170e.gpusetoptiongl(1, 4096, 0);
            }
            this.emu_opengl_options ^= 4096;
            return;
        }
        if (xi <= (butw * 2) + offx) {
            if ((this.emu_opengl_options & 7) == 3) {
                this.f170e.gpusetoptiongl(1, 4, 0);
                this.emu_opengl_options = (this.emu_opengl_options & (-8)) | 4;
                return;
            } else if ((this.emu_opengl_options & 7) == 4) {
                this.f170e.gpusetoptiongl(0, 3, 0);
                this.emu_opengl_options &= -8;
                return;
            } else {
                this.f170e.gpusetoptiongl(1, 3, 0);
                this.emu_opengl_options = (this.emu_opengl_options & (-8)) | 3;
                return;
            }
        }
        if (xi <= (butw * 3) + offx) {
            int val = ((this.emu_opengl_options >> 3) & 7) + 1;
            if (val == 7) {
                val = 0;
            }
            this.f170e.gpusetoptiongl(1, val << 3, 0);
            this.emu_opengl_options = (this.emu_opengl_options & (-57)) | (val << 3);
            return;
        }
        if (xi <= (butw * 4) + offx) {
            if ((this.emu_opengl_options & 16384) != 0) {
                this.f170e.gpusetoptiongl(0, 16384, 0);
            } else {
                this.f170e.gpusetoptiongl(1, 16384, 0);
            }
            this.emu_opengl_options ^= 16384;
            return;
        }
        if (xi <= (butw * 5) + offx) {
            if ((this.emu_opengl_options & 32768) != 0) {
                this.f170e.gpusetoptiongl(0, 32768, 0);
            } else {
                this.f170e.gpusetoptiongl(1, 32768, 0);
            }
            this.emu_opengl_options ^= 32768;
            return;
        }
        if (xi <= (butw * 6) + offx) {
            int val2 = ((this.emu_opengl_options >> 6) & 7) + 1;
            if (val2 > 3) {
                val2 = 0;
            }
            if ((this.emu_opengl_options & 448) == 192) {
                this.f170e.gpusetoptiongl(0, InputList.KEYCODE_BUTTON_5, 0);
                this.emu_opengl_options &= -449;
                return;
            } else {
                this.f170e.gpusetoptiongl(1, val2 << 6, 0);
                this.emu_opengl_options = (this.emu_opengl_options & (-449)) | (val2 << 6);
                return;
            }
        }
        if (xi <= (butw * 7) + offx) {
            if ((this.emu_opengl_options & 8192) != 0) {
                this.f170e.gpusetoptiongl(0, 8192, 0);
            } else {
                this.f170e.gpusetoptiongl(1, 8192, 0);
            }
            this.emu_opengl_options ^= 8192;
            return;
        }
        if (xi <= (butw * 8) + offx) {
            this.f170e.gpusaveoptiongl(this.emu_opengl_options);
            Toast.makeText(this.mContext, R.string.viewglext_restore, 0).show();
        } else if (xi <= (butw * 9) + offx) {
            this.emu_enable_tools ^= 1;
        }
    }

    public void toggleStickyButton(int but, int bit) {
        int[] iArr = this.stickyButton;
        iArr[but] = iArr[but] ^ 1;
        if (this.stickyButton[but] != 0) {
            this.f170e.setPadDataDown(bit & SupportMenu.USER_MASK, 0);
        } else {
            this.f170e.setPadDataUp(bit & SupportMenu.USER_MASK, 0);
        }
    }

    public void inittouchscreenevent() {
        if (this.emu_player_mode == 1) {
            if (this.emu_screen_orientation == 1 && this.emu_portrait_skin == 0) {
                init_motionevent_1playerPortrait();
                return;
            } else {
                init_motionevent_1playerLandscape();
                return;
            }
        }
        if (this.emu_split_mode != 0) {
            if (this.emu_split_mode != 1) {
                if (this.emu_split_mode == 2) {
                    init_motionevent_2playerH(1);
                    return;
                }
                return;
            }
            init_motionevent_2playerH(0);
            return;
        }
        init_motionevent_2playerV();
    }

    public void clearAllbuttons(int ext) {
        for (int Id = 0; Id < 24; Id++) {
            if (this.virtualPadId[Id] != -1) {
                int but = this.virtualPadId[Id];
                if (but < 20 || this.emu_player_mode != 1 || ext == 0) {
                    if ((this.virtualPadBit[but] & 65536) == 65536) {
                        this.f170e.setPadDataUp(this.virtualPadBit[but] & SupportMenu.USER_MASK, 0);
                        this.statebuttons &= (this.virtualPadBit[but] ^ (-1)) & SupportMenu.USER_MASK;
                    } else {
                        this.f170e.setPadDataUp(0, this.virtualPadBit[but] & SupportMenu.USER_MASK);
                    }
                }
                this.virtualPadId[Id] = -1;
            }
        }
    }

    public int touchscreenevent(long eventTime, int action, int x, int y, float pressure, float size, int deviceId, int Id, int pads) {
        int xa;
        int ya;
        int nbuttons = this.emu_player_mode == 1 ? 20 : 40;
        int ext = 0;
        int ext2 = 0;
        int mov = 0;
        int found = 0;
        int ret = 0;
        if (this.emu_pad_mode[this.emu_pad_type_selected] == 4 && this.emu_pad_mode_analog[this.emu_pad_type_selected] == 1) {
            ext = 2;
        }
        if (this.padScreenExtraCombo == 1) {
            ext2 = ext != 0 ? 7 : 9;
        }
        if (this.initvirtualPad == 0) {
            inittouchscreenevent();
        }
        if (action == 2) {
            action = 0;
            mov = 1;
        }
        if (action == 261 || action == 5 || action == 517) {
            action = 0;
        }
        if (action == 262 || action == 6 || action == 518) {
            action = 1;
        }
        if (action == 1 && this.virtualPadId[Id] != -1) {
            int but = this.virtualPadId[Id];
            if (but < 20) {
                if ((this.virtualPadBit[but] & 65536) == 65536) {
                    this.f170e.setPadDataUp(this.virtualPadBit[but] & SupportMenu.USER_MASK, 0);
                    this.statebuttons &= (this.virtualPadBit[but] ^ (-1)) & SupportMenu.USER_MASK;
                } else {
                    this.f170e.setPadDataUp(0, this.virtualPadBit[but] & SupportMenu.USER_MASK);
                }
                if (this.emu_pad_dynamic == 1 && but > 11 && but < 20 && (this.emu_pad_mode[this.emu_pad_type_selected] == 1 || (this.emu_pad_mode[this.emu_pad_type_selected] == 4 && this.emu_pad_mode_analog[this.emu_pad_type_selected] == 0))) {
                    this.padScreenStatus[this.mode][0] = 0;
                    for (int j = 12; j < 20; j++) {
                        this.virtualPadPos[j][0] = -1;
                        this.virtualPadPos[j][2] = -1;
                    }
                }
                if (this.emu_action_dynamic == 1 && but > 3 && but < 8 && (this.emu_pad_mode[this.emu_pad_type_selected] == 1 || this.emu_pad_mode[this.emu_pad_type_selected] == 4)) {
                    this.padScreenStatus[this.mode][1] = 0;
                    for (int j2 = 4; j2 < 8; j2++) {
                        this.virtualPadPos[j2][0] = -1;
                        this.virtualPadPos[j2][2] = -1;
                    }
                }
            } else if (but < 22) {
                int pad = ((this.virtualPadBit[but] >> 16) & 1) ^ 1;
                this.f170e.setpadanalog(pad, but - 20, 0, 0);
                if (but == 20) {
                    this.analog_values[0][0] = ((this.virtualPadPos[but][2] - this.virtualPadPos[but][0]) / 2) + this.virtualPadPos[but][0];
                    this.analog_values[0][1] = this.mHeight - (((this.virtualPadPos[but][3] - this.virtualPadPos[but][1]) / 2) + this.virtualPadPos[but][1]);
                }
                if (but == 21) {
                    this.analog_values[0][2] = ((this.virtualPadPos[but][2] - this.virtualPadPos[but][0]) / 2) + this.virtualPadPos[but][0];
                    this.analog_values[0][3] = this.mHeight - (((this.virtualPadPos[but][3] - this.virtualPadPos[but][1]) / 2) + this.virtualPadPos[but][1]);
                }
                if (this.emu_pad_dynamic == 1 && but == 20) {
                    this.padScreenStatus[this.mode][11] = 0;
                    this.virtualPadPos[but][0] = -1;
                    this.virtualPadPos[but][2] = -1;
                }
            } else if (but > 22 && but < 29) {
                if ((this.virtualPadBit[but] & 65536) == 65536) {
                    this.f170e.setPadDataUp(this.virtualPadBit[but] & SupportMenu.USER_MASK, 0);
                } else {
                    this.f170e.setPadDataUp(0, this.virtualPadBit[but] & SupportMenu.USER_MASK);
                }
            }
            this.virtualPadId[Id] = -1;
        }
        if (action != 0) {
            return 0;
        }
        if (this.emu_pad_mode[this.emu_pad_type_selected] == 4 && mov == 0 && x >= this.virtualPadPos[22][0] && x <= this.virtualPadPos[22][2] && y >= (this.virtualPadPos[22][1] - 140) && y <= (this.virtualPadPos[22][3] + 140)) {
            int[] iArr = this.emu_pad_mode_analog;
            int i = this.emu_pad_type_selected;
            iArr[i] = iArr[i] ^ 1;
            this.mode = this.emu_pad_mode_analog[this.emu_pad_type_selected];
            this.f170e.setpadanalogmode(this.emu_pad_type_selected, this.emu_pad_mode_analog[this.emu_pad_type_selected]);
            clearAllbuttons(ext);
            init_motionevent_1playerLandscape();
            resetDynamicPad();

            if ((this.virtualPadBit[22] & 65536) == 65536) {
                this.f170e.setPadDataDown(this.virtualPadBit[22] & SupportMenu.USER_MASK, 0);
                this.statebuttons |= this.virtualPadBit[22] & SupportMenu.USER_MASK;
            } else {
                this.f170e.setPadDataDown(0, this.virtualPadBit[22] & SupportMenu.USER_MASK);
            }

            return ret;
        }
        if (this.emu_enable_tools == 1 && mov == 0 && x >= (this.mWidth - (508.0f * this.padResize)) / 2.0f && x <= ((this.mWidth - (508.0f * this.padResize)) / 2.0f) + (this.padResize * 508.0f) && y >= 0 && y <= 60.0f * this.padResize) {
            if (this.emu_plugin == 5) {
                exec_tools2(x, y);
            } else {
                exec_tools(x, y);
            }
        }
        if (mov == 0 && this.padScreenExtraEnabled == 1 && !this.hidePad) {
            for (int i2 = 0; i2 < 6; i2++) {
                if (this.padScreenFunc[i2] == 1 && x >= this.virtualPadPos[i2 + 23][0] && x <= this.virtualPadPos[i2 + 23][2] && y >= this.virtualPadPos[i2 + 23][1] && y <= this.virtualPadPos[i2 + 23][3]) {
                    if (this.padScreenExtra[i2] >= 0 && this.padScreenExtra[i2] < 5) {
                        this.f170e.setsslot(this.padScreenExtra[i2]);
                    } else if (this.padScreenExtra[i2] < 10) {
                        this.f170e.setsslot(this.padScreenExtra[i2] + 5);
                        setSaveslot(this.padScreenExtra[i2] + 5);
                    } else if (this.padScreenExtra[i2] == 19) {
                        if (this.myActivity != null) {
                            this.myActivity.doMenu();
                        }
                    } else if (this.padScreenExtra[i2] == 18) {
                        toggleframelimit();
                    } else if (this.padScreenExtra[i2] == 20) {
                        toggleStickyButton(0, 128);
                    } else if (this.padScreenExtra[i2] == 21) {
                        toggleStickyButton(1, 64);
                    } else if (this.padScreenExtra[i2] == 22) {
                        toggleStickyButton(2, 32);
                    } else if (this.padScreenExtra[i2] == 23) {
                        toggleStickyButton(3, 16);
                    } else if (this.padScreenExtra[i2] == 24) {
                        toggleStickyButton(4, 4);
                    } else if (this.padScreenExtra[i2] == 25) {
                        toggleStickyButton(5, 1);
                    } else if (this.padScreenExtra[i2] == 26) {
                        toggleStickyButton(6, 8);
                    } else if (this.padScreenExtra[i2] == 27) {
                        toggleStickyButton(7, 2);
                    }
                }
            }
        }
        int ind = 0;
        while (ind < nbuttons + ext + ext2) {
            if (x >= this.virtualPadPos[ind][0] && x <= this.virtualPadPos[ind][2] && y >= this.virtualPadPos[ind][1] && y <= this.virtualPadPos[ind][3] && action == 0) {
                int pressed = this.virtualPadId[Id];
                if (this.virtualPadId[Id] != -1) {
                    int ind2 = this.virtualPadId[Id];
                    if (ind2 < 20 || this.emu_player_mode != 1) {
                        if ((this.virtualPadBit[ind2] & 65536) == 65536) {
                            this.f170e.setPadDataUp(this.virtualPadBit[ind2] & SupportMenu.USER_MASK, 0);
                            this.statebuttons &= (this.virtualPadBit[ind2] ^ (-1)) & SupportMenu.USER_MASK;
                        } else {
                            this.f170e.setPadDataUp(0, this.virtualPadBit[ind2] & SupportMenu.USER_MASK);
                        }
                    } else if (ind2 < 22 && ext != 0) {
                        int pad2 = ((this.virtualPadBit[ind2] >> 16) & 1) ^ 1;
                        this.f170e.setpadanalog(pad2, ind2 - 20, 0, 0);
                        if (ind2 == 20) {
                            this.analog_values[0][0] = ((this.virtualPadPos[ind2][2] - this.virtualPadPos[ind2][0]) / 2) + this.virtualPadPos[ind2][0];
                            this.analog_values[0][1] = this.mHeight - (((this.virtualPadPos[ind2][3] - this.virtualPadPos[ind2][1]) / 2) + this.virtualPadPos[ind2][1]);
                        }
                        if (ind2 == 21) {
                            this.analog_values[0][2] = ((this.virtualPadPos[ind2][2] - this.virtualPadPos[ind2][0]) / 2) + this.virtualPadPos[ind2][0];
                            this.analog_values[0][3] = this.mHeight - (((this.virtualPadPos[ind2][3] - this.virtualPadPos[ind2][1]) / 2) + this.virtualPadPos[ind2][1]);
                        }
                    } else if (ind2 > 22 && ind2 < 29) {
                        if ((this.virtualPadBit[ind2] & 65536) == 65536) {
                            this.f170e.setPadDataUp(this.virtualPadBit[ind2] & SupportMenu.USER_MASK, 0);
                        } else {
                            this.f170e.setPadDataUp(0, this.virtualPadBit[ind2] & SupportMenu.USER_MASK);
                        }
                    }
                }
                if (ind < 20 || this.emu_player_mode != 1) {
                    if ((this.virtualPadBit[ind] & 65536) == 65536) {
                        this.f170e.setPadDataDown(this.virtualPadBit[ind] & SupportMenu.USER_MASK, 0);
                        this.statebuttons |= this.virtualPadBit[ind] & SupportMenu.USER_MASK;
                    } else {
                        this.f170e.setPadDataDown(0, this.virtualPadBit[ind] & SupportMenu.USER_MASK);
                    }
                    if (this.emu_pad_dynamic == 1 && ind > 11 && ind < 20) {
                        this.padScreenStatus[this.mode][0] = 1;
                    }
                    if (this.emu_action_dynamic == 1 && ind > 3 && ind < 8) {
                        this.padScreenStatus[this.mode][1] = 1;
                    }
                } else if (ind < 22 && ext != 0) {
                    int tx = this.virtualPadPos[ind][2] - this.virtualPadPos[ind][0];
                    int ty = this.virtualPadPos[ind][3] - this.virtualPadPos[ind][1];
                    int xa2 = ((x - this.virtualPadPos[ind][0]) * 255) / tx;
                    int ya2 = ((y - this.virtualPadPos[ind][1]) * 255) / ty;
                    int pad3 = ((this.virtualPadBit[ind] >> 16) & 1) ^ 1;
                    if (ind == 20) {
                        this.analog_values[0][0] = x;
                        this.analog_values[0][1] = this.mHeight - y;
                    }
                    if (ind == 21) {
                        this.analog_values[0][2] = x;
                        this.analog_values[0][3] = this.mHeight - y;
                    }
                    this.f170e.setpadanalog(pad3, ind - 20, xa2 - 128, ya2 - 128);
                    if (this.emu_pad_dynamic == 1 && ind == 20) {
                        this.padScreenStatus[this.mode][11] = 1;
                    }
                } else if (ind > 22 && ind < 29) {
                    if ((this.virtualPadBit[ind] & 65536) == 65536) {
                        this.f170e.setPadDataDown(this.virtualPadBit[ind] & SupportMenu.USER_MASK, 0);
                    } else {
                        this.f170e.setPadDataDown(0, this.virtualPadBit[ind] & SupportMenu.USER_MASK);
                    }
                }
                this.virtualPadId[Id] = ind;
                ret = (action == 2 && pressed == ind) ? 0 : 1;
                found = 1;
            }
            ind++;
        }
        if (this.emu_pad_dynamic == 1 && this.virtualPadId[Id] == -1) {
            if (x >= this.mWidth / 2 || this.emu_pad_mode[this.emu_pad_type_selected] != 4 || this.emu_pad_mode_analog[this.emu_pad_type_selected] != 1) {
                if (x < this.mWidth / 2 && (this.emu_pad_mode[this.emu_pad_type_selected] == 1 || (this.emu_pad_mode[this.emu_pad_type_selected] == 4 && this.emu_pad_mode_analog[this.emu_pad_type_selected] == 0))) {
                    this.padOffScreenLan[this.mode][0] = x;
                    this.padOffScreenLan[this.mode][1] = this.mHeight - y;
                    for (int n = 12; n < 20; n++) {
                        int offx = ((int) this.padOffScreenLan[this.mode][this.virtualPad[n][0] * 2]) - ((int) ((this.padSizeScreenLan[this.mode][this.virtualPad[n][0] * 2] * this.padScreenResize[this.mode][this.virtualPad[n][0]]) / 2.0f));
                        int offy = ((int) this.padOffScreenLan[this.mode][(this.virtualPad[n][0] * 2) + 1]) - ((int) ((this.padSizeScreenLan[this.mode][(this.virtualPad[n][0] * 2) + 1] * this.padScreenResize[this.mode][this.virtualPad[n][0]]) / 2.0f));
                        this.virtualPadPos[n][0] = ((int) (this.virtualPad[n][1] * this.padResize * this.padScreenResize[this.mode][this.virtualPad[n][0]])) + offx;
                        this.virtualPadPos[n][1] = (((int) ((this.virtualPad[n][2] * this.padResize) * this.padScreenResize[this.mode][this.virtualPad[n][0]])) + (this.mHeight - ((int) (this.padSizeScreenLan[this.mode][(this.virtualPad[n][0] * 2) + 1] * this.padScreenResize[this.mode][this.virtualPad[n][0]])))) - offy;
                        this.virtualPadPos[n][2] = ((int) (this.virtualPad[n][3] * this.padResize * this.padScreenResize[this.mode][this.virtualPad[n][0]])) + offx;
                        this.virtualPadPos[n][3] = (((int) ((this.virtualPad[n][4] * this.padResize) * this.padScreenResize[this.mode][this.virtualPad[n][0]])) + (this.mHeight - ((int) (this.padSizeScreenLan[this.mode][(this.virtualPad[n][0] * 2) + 1] * this.padScreenResize[this.mode][this.virtualPad[n][0]])))) - offy;
                    }
                    this.virtualPadId[Id] = 12;
                    this.padScreenStatus[this.mode][0] = 1;
                }
            } else {
                this.padOffScreenLan[this.mode][this.virtualPad[20][0] * 2] = x;
                this.padOffScreenLan[this.mode][(this.virtualPad[20][0] * 2) + 1] = this.mHeight - y;
                int offx2 = ((int) this.padOffScreenLan[this.mode][this.virtualPad[20][0] * 2]) - ((int) ((this.padSizeScreenLan[this.mode][this.virtualPad[20][0] * 2] * this.padScreenResize[this.mode][this.virtualPad[20][0]]) / 2.0f));
                int offy2 = ((int) this.padOffScreenLan[this.mode][(this.virtualPad[20][0] * 2) + 1]) - ((int) ((this.padSizeScreenLan[this.mode][(this.virtualPad[20][0] * 2) + 1] * this.padScreenResize[this.mode][this.virtualPad[20][0]]) / 2.0f));
                this.virtualPadPos[20][0] = ((int) (this.virtualPad[20][1] * this.padResize * this.padScreenResize[this.mode][this.virtualPad[20][0]])) + offx2;
                this.virtualPadPos[20][1] = (((int) ((this.virtualPad[20][2] * this.padResize) * this.padScreenResize[this.mode][this.virtualPad[20][0]])) + (this.mHeight - ((int) (this.padSizeScreenLan[this.mode][(this.virtualPad[20][0] * 2) + 1] * this.padScreenResize[this.mode][this.virtualPad[20][0]])))) - offy2;
                this.virtualPadPos[20][2] = ((int) (this.virtualPad[20][3] * this.padResize * this.padScreenResize[this.mode][this.virtualPad[20][0]])) + offx2;
                this.virtualPadPos[20][3] = (((int) ((this.virtualPad[20][4] * this.padResize) * this.padScreenResize[this.mode][this.virtualPad[20][0]])) + (this.mHeight - ((int) (this.padSizeScreenLan[this.mode][(this.virtualPad[20][0] * 2) + 1] * this.padScreenResize[this.mode][this.virtualPad[20][0]])))) - offy2;
                this.analog_values[0][0] = ((this.virtualPadPos[20][2] - this.virtualPadPos[20][0]) / 2) + this.virtualPadPos[20][0];
                this.analog_values[0][1] = this.mHeight - (((this.virtualPadPos[20][3] - this.virtualPadPos[20][1]) / 2) + this.virtualPadPos[20][1]);
                int pad4 = ((this.virtualPadBit[20] >> 16) & 1) ^ 1;
                this.analog_values[0][0] = x;
                this.analog_values[0][1] = this.mHeight - y;
                this.f170e.setpadanalog(pad4, 0, -128, -128);
                this.virtualPadId[Id] = 20;
                this.padScreenStatus[this.mode][11] = 1;
            }
        }
        if (this.emu_action_dynamic == 1 && this.virtualPadId[Id] == -1 && x > this.mWidth / 2) {
            this.padOffScreenLan[this.mode][2] = x;
            this.padOffScreenLan[this.mode][3] = this.mHeight - y;
            for (int n2 = 4; n2 < 8; n2++) {
                int offx3 = ((int) this.padOffScreenLan[this.mode][this.virtualPad[n2][0] * 2]) - ((int) ((this.padSizeScreenLan[this.mode][this.virtualPad[n2][0] * 2] * this.padScreenResize[this.mode][this.virtualPad[n2][0]]) / 2.0f));
                int offy3 = ((int) this.padOffScreenLan[this.mode][(this.virtualPad[n2][0] * 2) + 1]) - ((int) ((this.padSizeScreenLan[this.mode][(this.virtualPad[n2][0] * 2) + 1] * this.padScreenResize[this.mode][this.virtualPad[n2][0]]) / 2.0f));
                this.virtualPadPos[n2][0] = ((int) (this.virtualPad[n2][1] * this.padResize * this.padScreenResize[this.mode][this.virtualPad[n2][0]])) + offx3;
                this.virtualPadPos[n2][1] = (((int) ((this.virtualPad[n2][2] * this.padResize) * this.padScreenResize[this.mode][this.virtualPad[n2][0]])) + (this.mHeight - ((int) (this.padSizeScreenLan[this.mode][(this.virtualPad[n2][0] * 2) + 1] * this.padScreenResize[this.mode][this.virtualPad[n2][0]])))) - offy3;
                this.virtualPadPos[n2][2] = ((int) (this.virtualPad[n2][3] * this.padResize * this.padScreenResize[this.mode][this.virtualPad[n2][0]])) + offx3;
                this.virtualPadPos[n2][3] = (((int) ((this.virtualPad[n2][4] * this.padResize) * this.padScreenResize[this.mode][this.virtualPad[n2][0]])) + (this.mHeight - ((int) (this.padSizeScreenLan[this.mode][(this.virtualPad[n2][0] * 2) + 1] * this.padScreenResize[this.mode][this.virtualPad[n2][0]])))) - offy3;
            }
            this.virtualPadId[Id] = 4;
            this.padScreenStatus[this.mode][1] = 1;
        }
        if (this.emu_action_dynamic == 0 && this.virtualPadId[Id] == -1) {
            int x1 = (int) (this.padOffScreenLan[this.mode][2] - ((this.padSizeScreenLan[this.mode][2] * this.padScreenResize[this.mode][1]) / 2.0f));
            int y2 = this.mHeight - ((int) (this.padOffScreenLan[this.mode][3] - ((this.padSizeScreenLan[this.mode][3] * this.padScreenResize[this.mode][1]) / 2.0f)));
            int x2 = x1 + ((int) (this.padSizeScreenLan[this.mode][2] * this.padScreenResize[this.mode][1]));
            int y1 = y2 - ((int) (this.padSizeScreenLan[this.mode][3] * this.padScreenResize[this.mode][1]));
            if (x >= x1 && x <= x2 && y >= y1 && y <= y2) {
                int seg = ((int) Math.toDegrees(Math.atan2((this.mHeight - y) - ((int) this.padOffScreenLan[this.mode][3]), x - ((int) this.padOffScreenLan[this.mode][2])) / 22.5d)) + 8;
                if (seg > 0 && seg < 17) {
                    int ind22 = this.apadsection[seg];
                    if ((this.virtualPadBit[ind22] & 65536) == 65536) {
                        this.f170e.setPadDataDown(this.virtualPadBit[ind22] & SupportMenu.USER_MASK, 0);
                        this.statebuttons |= this.virtualPadBit[ind22] & SupportMenu.USER_MASK;
                    } else {
                        this.f170e.setPadDataDown(0, this.virtualPadBit[ind22] & SupportMenu.USER_MASK);
                    }
                    this.virtualPadId[Id] = ind22;
                    found = 1;
                }
            }
        }
        if (this.emu_pad_mode[this.emu_pad_type_selected] == 4 || this.emu_pad_mode[this.emu_pad_type_selected] == 1) {
            if (found == 0 && this.emu_pad_mode[this.emu_pad_type_selected] == 4 && this.emu_pad_mode_analog[this.emu_pad_type_selected] == 1 && (this.virtualPadId[Id] == 20 || this.virtualPadId[Id] == 21)) {
                int ind23 = this.virtualPadId[Id];
                int tx2 = this.virtualPadPos[ind23][2] - this.virtualPadPos[ind23][0];
                int ty2 = this.virtualPadPos[ind23][3] - this.virtualPadPos[ind23][1];
                int pad5 = ((this.virtualPadBit[ind23] >> 16) & 1) ^ 1;
                if (x <= this.virtualPadPos[ind23][0]) {
                    xa = 0;
                } else {
                    xa = x >= this.virtualPadPos[ind23][2] ? 255 : ((x - this.virtualPadPos[ind23][0]) * 255) / tx2;
                }
                if (y <= this.virtualPadPos[ind23][1]) {
                    ya = 0;
                } else {
                    ya = y >= this.virtualPadPos[ind23][3] ? 255 : ((y - this.virtualPadPos[ind23][1]) * 255) / ty2;
                }
                if (ind23 == 20) {
                    this.analog_values[0][0] = this.virtualPadPos[ind23][0] + ((xa * tx2) / 255);
                    this.analog_values[0][1] = this.mHeight - (this.virtualPadPos[ind23][1] + ((ya * tx2) / 255));
                }
                if (ind23 == 21) {
                    this.analog_values[0][2] = this.virtualPadPos[ind23][0] + ((xa * tx2) / 255);
                    this.analog_values[0][3] = this.mHeight - (this.virtualPadPos[ind23][1] + ((ya * tx2) / 255));
                }
                this.f170e.setpadanalog(pad5, ind23 - 20, xa - 128, ya - 128);
            }
            if (this.emu_pad_dynamic == 1 && found == 0 && this.virtualPadId[Id] > 11 && this.virtualPadId[Id] < 20 && (this.emu_pad_mode[this.emu_pad_type_selected] == 1 || (this.emu_pad_mode[this.emu_pad_type_selected] == 4 && this.emu_pad_mode_analog[this.emu_pad_type_selected] == 0))) {
                int xdiff = x - ((int) this.padOffScreenLan[this.mode][0]);
                int ydiff = (this.mHeight - y) - ((int) this.padOffScreenLan[this.mode][1]);
                int seg2 = ((int) Math.toDegrees(Math.atan2(ydiff, xdiff) / 22.5d)) + 8;
                int siz = (int) Math.sqrt((xdiff * xdiff) + (ydiff * ydiff));
                if (siz >= ((int) ((this.padSizeScreenLan[this.mode][0] * this.padScreenResize[this.mode][0]) / 12.0f)) && seg2 > 0 && seg2 < 17) {
                    int up = this.virtualPadId[Id];
                    int ind24 = this.dpadsection[seg2];
                    if ((this.virtualPadBit[up] & 65536) == 65536) {
                        this.f170e.setPadDataUp(this.virtualPadBit[up] & SupportMenu.USER_MASK, 0);
                        this.statebuttons &= (this.virtualPadBit[up] ^ (-1)) & SupportMenu.USER_MASK;
                    } else {
                        this.f170e.setPadDataUp(0, this.virtualPadBit[up] & SupportMenu.USER_MASK);
                    }
                    if ((this.virtualPadBit[ind24] & 65536) == 65536) {
                        this.f170e.setPadDataDown(this.virtualPadBit[ind24] & SupportMenu.USER_MASK, 0);
                        this.statebuttons |= this.virtualPadBit[ind24] & SupportMenu.USER_MASK;
                    } else {
                        this.f170e.setPadDataDown(0, this.virtualPadBit[ind24] & SupportMenu.USER_MASK);
                    }
                    this.virtualPadId[Id] = ind24;
                }
            }
            if (this.emu_action_dynamic == 1 && found == 0 && this.virtualPadId[Id] > 3 && this.virtualPadId[Id] < 8) {
                int xdiff2 = x - ((int) this.padOffScreenLan[this.mode][2]);
                int ydiff2 = (this.mHeight - y) - ((int) this.padOffScreenLan[this.mode][3]);
                int seg3 = ((int) Math.toDegrees(Math.atan2(ydiff2, xdiff2) / 22.5d)) + 8;
                int siz2 = (int) Math.sqrt((xdiff2 * xdiff2) + (ydiff2 * ydiff2));
                if (siz2 >= ((int) ((this.padSizeScreenLan[this.mode][2] * this.padScreenResize[this.mode][1]) / 12.0f)) && seg3 > 0 && seg3 < 17) {
                    int up2 = this.virtualPadId[Id];
                    int ind25 = this.apadsection[seg3];
                    if ((this.virtualPadBit[up2] & 65536) == 65536) {
                        this.f170e.setPadDataUp(this.virtualPadBit[up2] & SupportMenu.USER_MASK, 0);
                        this.statebuttons &= (this.virtualPadBit[up2] ^ (-1)) & SupportMenu.USER_MASK;
                    } else {
                        this.f170e.setPadDataUp(0, this.virtualPadBit[up2] & SupportMenu.USER_MASK);
                    }
                    if ((this.virtualPadBit[ind25] & 65536) == 65536) {
                        this.f170e.setPadDataDown(this.virtualPadBit[ind25] & SupportMenu.USER_MASK, 0);
                        this.statebuttons |= this.virtualPadBit[ind25] & SupportMenu.USER_MASK;
                    } else {
                        this.f170e.setPadDataDown(0, this.virtualPadBit[ind25] & SupportMenu.USER_MASK);
                    }
                    this.virtualPadId[Id] = ind25;
                }
            }
            if (this.virtualPadId[Id] == -1 && found == 0 && this.emu_action_dynamic == 0 && this.emu_pad_dynamic == 0) {
                int distance = this.mWidth * 7;
                int selected = -1;
                for (int ind3 = 0; ind3 < 20; ind3++) {
                    int w = this.virtualPadPos[ind3][2] - this.virtualPadPos[ind3][0];
                    int h = this.virtualPadPos[ind3][3] - this.virtualPadPos[ind3][1];
                    int dx = Math.max(Math.abs(x - this.virtualPadPos[ind3][4]) - (w / 2), 0);
                    int dy = Math.max(Math.abs(y - this.virtualPadPos[ind3][5]) - (h / 2), 0);
                    int val = (dx * dx) + (dy * dy);
                    if (val < distance) {
                        selected = ind3;
                        distance = val;
                    }
                }
                if (selected == 11) {
                    return ret;
                }
                if (selected != -1) {
                    if ((this.virtualPadBit[selected] & 65536) == 65536) {
                        this.f170e.setPadDataDown(this.virtualPadBit[selected] & SupportMenu.USER_MASK, 0);
                        this.statebuttons |= this.virtualPadBit[selected] & SupportMenu.USER_MASK;
                    } else {
                        this.f170e.setPadDataDown(0, this.virtualPadBit[selected] & SupportMenu.USER_MASK);
                    }
                    int pressed2 = this.virtualPadId[Id];
                    this.virtualPadId[Id] = selected;
                    if (action != 2 || pressed2 != selected) {
                        ret = 1;
                    }
                }
            }
        }
        return ret;
    }

    @Override // android.view.View
    public boolean dispatchTouchEvent(MotionEvent ev) {
        queueMotionEvent(ev);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN: // нажатие
            case MotionEvent.ACTION_MOVE: // движение
                isInTouch = true;
                break;
            case MotionEvent.ACTION_UP: // отпускание
            case MotionEvent.ACTION_CANCEL:
                isInTouch = false;
                break;
        }
        if (touchListener != null) {
            touchListener.onTouch(isInTouch);
        }
        return true;
    }

    private void dumpEvent(MotionEvent event) {
        String[] names = {"DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE", "POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?"};
        StringBuilder sb = new StringBuilder();
        int action = event.getAction();
        int actionCode = action & 255;
        sb.append("event ACTION_").append(names[actionCode]);
        if (actionCode == 5 || actionCode == 6) {
            sb.append("(pid ").append(action >> 8);
            sb.append(")");
        }
        sb.append("[");
        for (int i = 0; i < event.getPointerCount(); i++) {
            sb.append("#").append(i);
            sb.append("(pid ").append(event.getPointerId(i));
            sb.append(")=").append((int) event.getX(i));
            sb.append(",").append((int) event.getY(i));
            if (i + 1 < event.getPointerCount()) {
                sb.append(";");
            }
        }
        sb.append("]");
        Log.e("epsxepad", sb.toString());
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void queueMotionEvent(int action, int x, int y, int pointerId) {
        int actionCode = action & 255;
        int vib = 0;
        if (this.emu_pad_type[0] != 1 || this.emu_pad_type[1] != 1 || this.emu_enable_tools != 0) {
            if (actionCode == 5 || actionCode == 6 || actionCode == 1 || actionCode == 0 || actionCode == 2) {
                if (this.emu_pad_mode[0] == 3 || this.emu_pad_mode[0] == 8) {
                    vib = this.gun.touchscreeneventgun(0L, action, x, y, 0.0f, 0.0f, 0, pointerId, this.f170e, this.emu_pad_mode[0], this.mfps, this.mWidth, this.mHeight, this.emu_pad_type_selected);
                } else if (this.emu_player_mode != 1 || this.emu_pad_type[0] == 0 || this.emu_pad_type[1] == 0 || this.emu_enable_tools == 1) {
                    vib = 0 + touchscreenevent(0L, action, x, y, 0.0f, 0.0f, 0, pointerId, this.emu_pad_type[0] | (this.emu_pad_type[1] << 1));
                }
            }
            if ((this.ts_vibration[0] == 1 && vib == 1) || (this.ts_vibration[1] == 1 && vib == 2)) {
                try {
                    Vibrator v = (Vibrator) this.mContext.getSystemService("vibrator");
                    v.vibrate(35L);
                } catch (Exception e) {
                }
            }
        }
    }

    public static int getActionIndexEclair(MotionEvent event) {
        return (event.getAction() & 65280) >> 8;
    }

    private void queueMotionEvent(MotionEvent event) {
        int action = event.getAction();
        int actionCode = action & 255;
        int vib = 0;
        int i = this.osVersion >= 8 ? event.getActionIndex() : getActionIndexEclair(event);
        if (this.emu_pad_type[0] != 1 || this.emu_pad_type[1] != 1) {
            if (actionCode == 5 || actionCode == 6 || actionCode == 1 || actionCode == 0) {
                if (this.emu_pad_mode[0] == 3 || this.emu_pad_mode[0] == 8) {
                    vib = this.gun.touchscreeneventgun(event.getEventTime(), event.getAction(), (int) event.getX(i), (int) event.getY(i), event.getPressure(i), event.getSize(i), event.getDeviceId(), event.getPointerId(i), this.f170e, this.emu_pad_mode[0], this.mfps, this.mWidth, this.mHeight, this.emu_pad_type_selected);
                } else if (this.emu_player_mode != 1 || this.emu_pad_type[0] == 0 || this.emu_pad_type[1] == 0) {
                    vib = touchscreenevent(event.getEventTime(), event.getAction(), (int) event.getX(i), (int) event.getY(i), event.getPressure(i), event.getSize(i), event.getDeviceId(), event.getPointerId(i), (this.emu_pad_type[1] << 1) | this.emu_pad_type[0]);
                }
            } else if (actionCode == 2) {
                for (int ind = 0; ind < event.getPointerCount(); ind++) {
                    if (this.emu_pad_mode[0] == 3 || this.emu_pad_mode[0] == 8) {
                        vib = this.gun.touchscreeneventgun(event.getEventTime(), event.getAction(), (int) event.getX(i), (int) event.getY(i), event.getPressure(i), event.getSize(i), event.getDeviceId(), event.getPointerId(i), this.f170e, this.emu_pad_mode[0], this.mfps, this.mWidth, this.mHeight, this.emu_pad_type_selected);
                    } else if (this.emu_player_mode != 1 || this.emu_pad_type[0] == 0 || this.emu_pad_type[1] == 0) {
                        vib += touchscreenevent(event.getEventTime(), event.getAction(), (int) event.getX(ind), (int) event.getY(ind), event.getPressure(ind), event.getSize(ind), event.getDeviceId(), event.getPointerId(ind), (this.emu_pad_type[1] << 1) | this.emu_pad_type[0]);
                    }
                }
            }
            if ((this.ts_vibration[0] == 1 && vib == 1) || (this.ts_vibration[1] == 1 && vib == 2)) {
                try {
                    Vibrator v = (Vibrator) this.mContext.getSystemService("vibrator");
                    v.vibrate(35L);
                } catch (Exception e) {
                }
            }
        }
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setSaveMode(int mode, int auto) {
        Log.e("epsxe", "epsxeview saving status");
        this.f170e.setSaveMode(mode, auto);
        super.onPause();
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void onPause(int mode, int auto) {
        try {
            Log.e("epsxe", "epsxeview pause status");
            this.emu_ui_pause_support = 1;
            if (this.f170e != null) {
                this.f170e.setPauseMode(mode, auto);
            }
            super.onPause();
        } catch (Exception e) {
        }
    }

    @Override // android.opengl.GLSurfaceView, com.epsxe.ePSXe.ePSXeView
    public void onResume() {
        Log.e("epsxe", "epsxeview resume status");
        this.f170e.setResumeMode();
        super.onResume();
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void onStop() {
        Log.e("epsxe", "epsxeview stop status");
        this.f170e.setStopMode();
        stopEmulation(false);
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void onAutosave(int mode) {
        Log.e("epsxe", "epsxeview autosaving status");
        this.f170e.setSaveMode(mode, 1);
    }

    public void resetPad1Values() {
        if (this.emu_screen_orientation != 1) {
            if (this.mWidth <= 600) {
                this.padResize = 0.8f;
            } else if (this.mWidth > 600 && this.mWidth <= 800) {
                this.padResize = 1.0f;
            } else if (this.mWidth > 800 && this.mWidth <= 1280) {
                this.padResize = 1.35f;
            } else if (this.mWidth <= 1280 || this.mWidth > 1500) {
                this.padResize = 1.8f;
            } else {
                this.padResize = 1.5f;
            }
        } else if (this.emu_portrait_skin == 1) {
            this.padResize = this.mWidth / 562.0f;
        }
        if (this.mWidthDraw != 0 && this.mWidthDraw != this.mWidth) {
            this.glresizeX = (float)this.mWidthDraw / this.mWidth;
        }
        if (this.mHeightDraw != 0 && this.mHeightDraw != this.mHeight) {
            this.glresizeY = (float)this.mHeightDraw / this.mHeight;
        }
        float[][] padSizeScreenLantmp = {new float[]{228.0f, 228.0f, 224.0f, 248.0f, 66.0f, 50.0f, 66.0f, 62.0f, 64.0f, 60.0f, 64.0f, 60.0f, 64.0f, 60.0f, 64.0f, 60.0f, 53.0f, 41.0f, 64.0f, 60.0f, 64.0f, 60.0f, 222.0f, 222.0f, 222.0f, 222.0f, 71.0f, 71.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f}, new float[]{228.0f, 228.0f, 224.0f, 248.0f, 66.0f, 50.0f, 66.0f, 62.0f, 64.0f, 60.0f, 64.0f, 60.0f, 64.0f, 60.0f, 64.0f, 60.0f, 53.0f, 41.0f, 64.0f, 60.0f, 64.0f, 60.0f, 222.0f, 222.0f, 222.0f, 222.0f, 71.0f, 71.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f}};
        for (int i = 0; i < 20; i++) {
            this.padSizeScreenLan[this.mode][(i * 2) + 0] = padSizeScreenLantmp[this.mode][(i * 2) + 0] * this.padResize;
            this.padSizeScreenLan[this.mode][(i * 2) + 1] = padSizeScreenLantmp[this.mode][(i * 2) + 1] * this.padResize;
        }
        if (this.emu_screen_orientation != 1) {
            float[][] padOffScreenLantmp = {new float[]{this.padSizeScreenLan[0][0] / 2.0f, this.padSizeScreenLan[0][1] / 2.0f, this.mWidth - (this.padSizeScreenLan[0][2] / 2.0f), this.padSizeScreenLan[0][3] / 2.0f, ((this.mWidth / 2f) - this.padSizeScreenLan[0][4]) - 30.0f, this.padSizeScreenLan[0][5] / 2.0f, (this.mWidth / 2f) + 30, this.padSizeScreenLan[0][7] / 2.0f, this.padSizeScreenLan[0][8] / 2.0f, this.mHeight - (this.padSizeScreenLan[0][9] / 2.0f), (this.padSizeScreenLan[0][10] / 2.0f) + this.padSizeScreenLan[0][8], this.mHeight - (this.padSizeScreenLan[0][11] / 2.0f), this.mWidth - (this.padSizeScreenLan[0][12] / 2.0f), this.mHeight - (this.padSizeScreenLan[0][13] / 2.0f), (this.mWidth - (this.padSizeScreenLan[0][14] / 2.0f)) - this.padSizeScreenLan[0][12], this.mHeight - (this.padSizeScreenLan[0][15] / 2.0f), (this.mWidth / 2f) + 40 + this.padSizeScreenLan[0][6], this.padSizeScreenLan[0][17] / 2.0f, (this.padSizeScreenLan[0][18] / 2.0f) + this.padSizeScreenLan[0][10] + this.padSizeScreenLan[0][8], this.mHeight - (this.padSizeScreenLan[0][19] / 2.0f), ((this.mWidth - (this.padSizeScreenLan[0][20] / 2.0f)) - this.padSizeScreenLan[0][12]) - this.padSizeScreenLan[0][14], this.mHeight - (this.padSizeScreenLan[0][21] / 2.0f), this.padSizeScreenLan[0][22] / 2.0f, this.padSizeScreenLan[0][23] / 2.0f, this.mWidth - (this.padSizeScreenLan[0][24] / 2.0f), this.padSizeScreenLan[0][25] / 2.0f, 0.0f, 0.0f, this.padSizeScreenLan[0][28] / 2.0f, (this.mHeight - (this.padSizeScreenLan[0][29] / 2.0f)) - (this.padSizeScreenLan[0][10] * 2.0f), (this.padSizeScreenLan[0][30] / 2.0f) + this.padSizeScreenLan[0][28], (this.mHeight - (this.padSizeScreenLan[0][31] / 2.0f)) - (this.padSizeScreenLan[0][10] * 2.0f), (this.padSizeScreenLan[0][32] / 2.0f) + this.padSizeScreenLan[0][28] + this.padSizeScreenLan[0][30], (this.mHeight - (this.padSizeScreenLan[0][33] / 2.0f)) - (this.padSizeScreenLan[0][10] * 2.0f), this.mWidth - (this.padSizeScreenLan[0][34] / 2.0f), (this.mHeight - (this.padSizeScreenLan[0][35] / 2.0f)) - (this.padSizeScreenLan[0][10] * 2.0f), (this.mWidth - (this.padSizeScreenLan[0][36] / 2.0f)) - this.padSizeScreenLan[0][34], (this.mHeight - (this.padSizeScreenLan[0][37] / 2.0f)) - (this.padSizeScreenLan[0][10] * 2.0f), ((this.mWidth - (this.padSizeScreenLan[0][38] / 2.0f)) - this.padSizeScreenLan[0][34]) - this.padSizeScreenLan[0][36], (this.mHeight - (this.padSizeScreenLan[0][39] / 2.0f)) - (this.padSizeScreenLan[0][10] * 2.0f)}, new float[]{(this.mWidth / 2f) - ((this.padSizeScreenLan[1][0] / 2.0f) * 0.76f), this.mHeight / 2f, this.mWidth - (this.padSizeScreenLan[1][2] / 2.0f), this.padSizeScreenLan[1][3] / 2.0f, ((this.mWidth / 2f) - this.padSizeScreenLan[1][4]) - 30.0f, this.padSizeScreenLan[1][5] / 2.0f, (this.mWidth / 2f) + 30, this.padSizeScreenLan[1][7] / 2.0f, this.padSizeScreenLan[1][8] / 2.0f, this.mHeight - (this.padSizeScreenLan[1][9] / 2.0f), (this.padSizeScreenLan[1][10] / 2.0f) + this.padSizeScreenLan[1][8], this.mHeight - (this.padSizeScreenLan[1][11] / 2.0f), this.mWidth - (this.padSizeScreenLan[1][12] / 2.0f), this.mHeight - (this.padSizeScreenLan[1][13] / 2.0f), (this.mWidth - (this.padSizeScreenLan[1][14] / 2.0f)) - this.padSizeScreenLan[1][12], this.mHeight - (this.padSizeScreenLan[1][15] / 2.0f), (this.mWidth / 2f) + 40 + this.padSizeScreenLan[1][6], this.padSizeScreenLan[1][17] / 2.0f, (this.padSizeScreenLan[1][18] / 2.0f) + this.padSizeScreenLan[1][10] + this.padSizeScreenLan[1][8], this.mHeight - (this.padSizeScreenLan[1][19] / 2.0f), ((this.mWidth - (this.padSizeScreenLan[1][20] / 2.0f)) - this.padSizeScreenLan[1][12]) - this.padSizeScreenLan[1][14], this.mHeight - (this.padSizeScreenLan[1][21] / 2.0f), this.padSizeScreenLan[1][22] / 2.0f, this.padSizeScreenLan[1][23] / 2.0f, (this.mWidth / 2f) + ((this.padSizeScreenLan[1][24] / 2.0f) * 0.76f), this.mHeight / 2f, 0.0f, 0.0f, this.padSizeScreenLan[1][28] / 2.0f, (this.mHeight - (this.padSizeScreenLan[1][29] / 2.0f)) - (this.padSizeScreenLan[1][10] * 2.0f), (this.padSizeScreenLan[1][30] / 2.0f) + this.padSizeScreenLan[1][28], (this.mHeight - (this.padSizeScreenLan[1][31] / 2.0f)) - (this.padSizeScreenLan[1][10] * 2.0f), (this.padSizeScreenLan[1][32] / 2.0f) + this.padSizeScreenLan[1][28] + this.padSizeScreenLan[1][30], (this.mHeight - (this.padSizeScreenLan[1][33] / 2.0f)) - (this.padSizeScreenLan[1][10] * 2.0f), this.mWidth - (this.padSizeScreenLan[1][34] / 2.0f), (this.mHeight - (this.padSizeScreenLan[1][35] / 2.0f)) - (this.padSizeScreenLan[1][10] * 2.0f), (this.mWidth - (this.padSizeScreenLan[1][36] / 2.0f)) - this.padSizeScreenLan[1][34], (this.mHeight - (this.padSizeScreenLan[1][37] / 2.0f)) - (this.padSizeScreenLan[1][10] * 2.0f), ((this.mWidth - (this.padSizeScreenLan[1][38] / 2.0f)) - this.padSizeScreenLan[1][34]) - this.padSizeScreenLan[1][36], (this.mHeight - (this.padSizeScreenLan[1][39] / 2.0f)) - (this.padSizeScreenLan[1][10] * 2.0f)}};
            for (int i2 = 0; i2 < 20; i2++) {
                this.padOffScreenLan[this.mode][(i2 * 2) + 0] = padOffScreenLantmp[this.mode][(i2 * 2) + 0];
                this.padOffScreenLan[this.mode][(i2 * 2) + 1] = padOffScreenLantmp[this.mode][(i2 * 2) + 1];
            }
            return;
        }
        if (this.emu_portrait_skin == 1) {
            float[][] padOffScreenLantmp2 = {new float[]{this.padSizeScreenLan[0][0] / 2.0f, this.padSizeScreenLan[0][1] / 2.0f, this.mWidth - (this.padSizeScreenLan[0][2] / 2.0f), this.padSizeScreenLan[0][3] / 2.0f, ((this.mWidth / 2f) - this.padSizeScreenLan[0][4]) - 30.0f, (this.mHeight / 2f) - (this.padSizeScreenLan[0][5] / 2.0f), (this.mWidth / 2f) + 30, (this.mHeight / 2f) - (this.padSizeScreenLan[0][7] / 2.0f), this.padSizeScreenLan[0][8] / 2.0f, (this.mHeight / 2f) - (this.padSizeScreenLan[0][9] / 2.0f), (this.padSizeScreenLan[0][10] / 2.0f) + this.padSizeScreenLan[0][8], (this.mHeight / 2f) - (this.padSizeScreenLan[0][11] / 2.0f), this.mWidth - (this.padSizeScreenLan[0][12] / 2.0f), (this.mHeight / 2f) - (this.padSizeScreenLan[0][13] / 2.0f), (this.mWidth - (this.padSizeScreenLan[0][14] / 2.0f)) - this.padSizeScreenLan[0][12], (this.mHeight / 2f) - (this.padSizeScreenLan[0][15] / 2.0f), (this.mWidth / 2f) + 40 + this.padSizeScreenLan[0][6], (this.mHeight / 2f) - (this.padSizeScreenLan[0][17] / 2.0f), (this.padSizeScreenLan[0][18] / 2.0f) + this.padSizeScreenLan[0][10] + this.padSizeScreenLan[0][8], (this.mHeight / 2f) - (this.padSizeScreenLan[0][19] / 2.0f), ((this.mWidth - (this.padSizeScreenLan[0][20] / 2.0f)) - this.padSizeScreenLan[0][12]) - this.padSizeScreenLan[0][14], (this.mHeight / 2f) - (this.padSizeScreenLan[0][21] / 2.0f), this.padSizeScreenLan[0][22] / 2.0f, this.padSizeScreenLan[0][23] / 2.0f, this.mWidth - (this.padSizeScreenLan[0][24] / 2.0f), this.padSizeScreenLan[0][25] / 2.0f, 0.0f, 0.0f, this.padSizeScreenLan[0][28] / 2.0f, (this.mHeight / 3f) - (this.padSizeScreenLan[0][29] / 2.0f), (this.padSizeScreenLan[0][30] / 2.0f) + this.padSizeScreenLan[0][28], (this.mHeight / 3f) - (this.padSizeScreenLan[0][31] / 2.0f), (this.padSizeScreenLan[0][32] / 2.0f) + this.padSizeScreenLan[0][28] + this.padSizeScreenLan[0][30], (this.mHeight / 3f) - (this.padSizeScreenLan[0][33] / 2.0f), this.mWidth - (this.padSizeScreenLan[0][34] / 2.0f), (this.mHeight / 3f) - (this.padSizeScreenLan[0][35] / 2.0f), (this.mWidth - (this.padSizeScreenLan[0][36] / 2.0f)) - this.padSizeScreenLan[0][34], (this.mHeight / 3f) - (this.padSizeScreenLan[0][37] / 2.0f), ((this.mWidth - (this.padSizeScreenLan[0][38] / 2.0f)) - this.padSizeScreenLan[0][34]) - this.padSizeScreenLan[0][36], (this.mHeight / 3f) - (this.padSizeScreenLan[0][39] / 2.0f)}, new float[]{(this.mWidth / 2f) - ((this.padSizeScreenLan[1][0] / 2.0f) * 0.76f), this.mHeight / 4f, this.mWidth - (this.padSizeScreenLan[1][2] / 2.0f), this.padSizeScreenLan[1][3] / 2.0f, ((this.mWidth / 2f) - this.padSizeScreenLan[1][4]) - 30.0f, (this.mHeight / 2f) - (this.padSizeScreenLan[1][5] / 2.0f), (this.mWidth / 2f) + 30, (this.mHeight / 2f) - (this.padSizeScreenLan[1][7] / 2.0f), this.padSizeScreenLan[1][8] / 2.0f, (this.mHeight / 2f) - (this.padSizeScreenLan[1][9] / 2.0f), (this.padSizeScreenLan[1][10] / 2.0f) + this.padSizeScreenLan[0][8], (this.mHeight / 2f) - (this.padSizeScreenLan[1][11] / 2.0f), this.mWidth - (this.padSizeScreenLan[1][12] / 2.0f), (this.mHeight / 2f) - (this.padSizeScreenLan[1][13] / 2.0f), (this.mWidth - (this.padSizeScreenLan[1][14] / 2.0f)) - this.padSizeScreenLan[1][12], (this.mHeight / 2f) - (this.padSizeScreenLan[1][15] / 2.0f), (this.mWidth / 2f) + 40 + this.padSizeScreenLan[1][6], (this.mHeight / 2f) - (this.padSizeScreenLan[1][17] / 2.0f), (this.padSizeScreenLan[1][18] / 2.0f) + this.padSizeScreenLan[1][10] + this.padSizeScreenLan[1][8], (this.mHeight / 2f) - (this.padSizeScreenLan[1][19] / 2.0f), ((this.mWidth - (this.padSizeScreenLan[1][20] / 2.0f)) - this.padSizeScreenLan[1][12]) - this.padSizeScreenLan[1][14], (this.mHeight / 2f) - (this.padSizeScreenLan[1][21] / 2.0f), this.padSizeScreenLan[1][22] / 2.0f, this.padSizeScreenLan[1][23] / 2.0f, (this.mWidth / 2f) + ((this.padSizeScreenLan[1][24] / 2.0f) * 0.76f), this.mHeight / 4f, 0.0f, 0.0f, this.padSizeScreenLan[1][28] / 2.0f, (this.mHeight / 3f) - (this.padSizeScreenLan[1][29] / 2.0f), (this.padSizeScreenLan[1][30] / 2.0f) + this.padSizeScreenLan[1][28], (this.mHeight / 3f) - (this.padSizeScreenLan[1][31] / 2.0f), (this.padSizeScreenLan[1][32] / 2.0f) + this.padSizeScreenLan[1][28] + this.padSizeScreenLan[1][30], (this.mHeight / 3f) - (this.padSizeScreenLan[1][33] / 2.0f), this.mWidth - (this.padSizeScreenLan[1][34] / 2.0f), (this.mHeight / 3f) - (this.padSizeScreenLan[1][35] / 2.0f), (this.mWidth - (this.padSizeScreenLan[1][36] / 2.0f)) - this.padSizeScreenLan[1][34], (this.mHeight / 3f) - (this.padSizeScreenLan[1][37] / 2.0f), ((this.mWidth - (this.padSizeScreenLan[1][38] / 2.0f)) - this.padSizeScreenLan[1][34]) - this.padSizeScreenLan[1][36], (this.mHeight / 3f) - (this.padSizeScreenLan[1][39] / 2.0f)}};
            for (int i3 = 0; i3 < 20; i3++) {
                this.padOffScreenLan[this.mode][(i3 * 2) + 0] = padOffScreenLantmp2[this.mode][(i3 * 2) + 0];
                this.padOffScreenLan[this.mode][(i3 * 2) + 1] = padOffScreenLantmp2[this.mode][(i3 * 2) + 1];
            }
        }
    }

    public void resetPadAllValues() {
        if (this.emu_screen_orientation != 1) {
            if (this.mWidth <= 600) {
                this.padResize = 0.8f;
            } else if (this.mWidth > 600 && this.mWidth <= 800) {
                this.padResize = 1.0f;
            } else if (this.mWidth > 800 && this.mWidth <= 1280) {
                this.padResize = 1.35f;
            } else if (this.mWidth <= 1280 || this.mWidth > 1500) {
                this.padResize = 1.8f;
            } else {
                this.padResize = 1.5f;
            }
        } else if (this.emu_portrait_skin == 1) {
            this.padResize = this.mWidth / 562.0f;
        }
        float[] padSizeScreenPortmp = {this.mWidth, this.mHeight / 2f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, (this.mWidth * 64f) / 480, ((this.mHeight / 2f) * 64) / 400, (this.mWidth * 64f) / 480, ((this.mHeight / 2f) * 64) / 400, (this.mWidth * 64f) / 480, ((this.mHeight / 2f) * 64) / 400, (this.mWidth * 64f) / 480, ((this.mHeight / 2f) * 64) / 400, (this.mWidth * 64f) / 480, ((this.mHeight / 2f) * 64) / 400, (this.mWidth * 64f) / 480, ((this.mHeight / 2f) * 64) / 400};
        float[] padOffScreenPortmp = {this.mWidth / 2f, this.mHeight / 4f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, ((this.mWidth * 288f) / 480) + (padSizeScreenPortmp[28] / 2.0f), ((this.mHeight / 2f) - (((this.mHeight / 2f) * 68) / 400)) - (padSizeScreenPortmp[29] / 2.0f), ((this.mWidth * 352f) / 480) + (padSizeScreenPortmp[30] / 2.0f), ((this.mHeight / 2f) - (((this.mHeight / 2f) * 68) / 400)) - (padSizeScreenPortmp[31] / 2.0f), ((this.mWidth * HttpStatus.SC_REQUESTED_RANGE_NOT_SATISFIABLE) / 480) + (padSizeScreenPortmp[32] / 2.0f), ((this.mHeight / 2f) - (((this.mHeight / 2f) * 68) / 400)) - (padSizeScreenPortmp[33] / 2.0f), ((this.mWidth * 8f) / 480) + (padSizeScreenPortmp[34] / 2.0f), ((this.mHeight / 2f) - (((this.mHeight / 2f) * 338) / 400)) - (padSizeScreenPortmp[35] / 2.0f), ((this.mWidth * 72f) / 480) + (padSizeScreenPortmp[36] / 2.0f), ((this.mHeight / 2f) - (((this.mHeight / 2f) * 338) / 400)) - (padSizeScreenPortmp[37] / 2.0f), ((this.mWidth * 136f) / 480) + (padSizeScreenPortmp[38] / 2.0f), ((this.mHeight / 2f) - (((this.mHeight / 2f) * 338) / 400)) - (padSizeScreenPortmp[39] / 2.0f)};
        for (int i = 0; i < 1; i++) {
            this.padSizeScreenPor[(i * 2) + 0] = padSizeScreenPortmp[(i * 2) + 0];
            this.padSizeScreenPor[(i * 2) + 1] = padSizeScreenPortmp[(i * 2) + 1];
            this.padOffScreenPor[(i * 2) + 0] = padOffScreenPortmp[(i * 2) + 0];
            this.padOffScreenPor[(i * 2) + 1] = padOffScreenPortmp[(i * 2) + 1];
        }
        for (int i2 = 14; i2 < 20; i2++) {
            this.padSizeScreenPor[(i2 * 2) + 0] = padSizeScreenPortmp[(i2 * 2) + 0];
            this.padSizeScreenPor[(i2 * 2) + 1] = padSizeScreenPortmp[(i2 * 2) + 1];
            this.padOffScreenPor[(i2 * 2) + 0] = padOffScreenPortmp[(i2 * 2) + 0];
            this.padOffScreenPor[(i2 * 2) + 1] = padOffScreenPortmp[(i2 * 2) + 1];
        }
        float[] padSizeScreenLan2Htmp = {228.0f, 114.0f, 224.0f, 124.0f, 66.0f, 25.0f, 66.0f, 31.0f, 64.0f, 30.0f, 64.0f, 30.0f, 64.0f, 30.0f, 64.0f, 30.0f};
        for (int i3 = 0; i3 < 8; i3++) {
            this.padSizeScreenLan2H[(i3 * 2) + 0] = padSizeScreenLan2Htmp[(i3 * 2) + 0] * this.padResize;
            this.padSizeScreenLan2H[(i3 * 2) + 1] = padSizeScreenLan2Htmp[(i3 * 2) + 1] * this.padResize;
        }
        float[] padOffScreenLan2Htmp = {this.padSizeScreenLan2H[0] / 2.0f, this.padSizeScreenLan2H[1] / 2.0f, this.mWidth - (this.padSizeScreenLan2H[2] / 2.0f), this.padSizeScreenLan2H[3] / 2.0f, ((this.mWidth / 2f) - this.padSizeScreenLan2H[4]) - 30.0f, this.padSizeScreenLan2H[5] / 2.0f, (this.mWidth / 2f) + 30, this.padSizeScreenLan2H[7] / 2.0f, this.padSizeScreenLan2H[8] / 2.0f, (this.mHeight / 2f) - (this.padSizeScreenLan2H[9] / 2.0f), (this.padSizeScreenLan2H[10] / 2.0f) + this.padSizeScreenLan2H[8], (this.mHeight / 2f) - (this.padSizeScreenLan2H[11] / 2.0f), this.mWidth - (this.padSizeScreenLan2H[12] / 2.0f), (this.mHeight / 2f) - (this.padSizeScreenLan2H[13] / 2.0f), (this.mWidth - (this.padSizeScreenLan2H[14] / 2.0f)) - this.padSizeScreenLan2H[12], (this.mHeight / 2f) - (this.padSizeScreenLan2H[15] / 2.0f)};
        for (int i4 = 0; i4 < 8; i4++) {
            this.padOffScreenLan2H[(i4 * 2) + 0] = padOffScreenLan2Htmp[(i4 * 2) + 0];
            this.padOffScreenLan2H[(i4 * 2) + 1] = padOffScreenLan2Htmp[(i4 * 2) + 1];
        }
        float[] padSizeScreenLan2Vtmp = {(this.mHeight * 228f) / this.mWidth, ((this.mWidth / 2f) * 228) / this.mHeight, (this.mHeight * 224f) / this.mWidth, ((this.mWidth / 2f) * 248) / this.mHeight, (this.mHeight * 66f) / this.mWidth, ((this.mWidth / 2f) * 40) / this.mHeight, (this.mHeight * 66f) / this.mWidth, ((this.mWidth / 2f) * 62) / this.mHeight, (this.mHeight * 64f) / this.mWidth, ((this.mWidth / 2f) * 60) / this.mHeight, (this.mHeight * 64f) / this.mWidth, ((this.mWidth / 2f) * 60) / this.mHeight, (this.mHeight * 64f) / this.mWidth, ((this.mWidth / 2f) * 60) / this.mHeight, (this.mHeight * 64f) / this.mWidth, ((this.mWidth / 2f) * 60) / this.mHeight};
        for (int i5 = 0; i5 < 8; i5++) {
            this.padSizeScreenLan2V[(i5 * 2) + 0] = padSizeScreenLan2Vtmp[(i5 * 2) + 0] * this.padResize;
            this.padSizeScreenLan2V[(i5 * 2) + 1] = padSizeScreenLan2Vtmp[(i5 * 2) + 1] * this.padResize;
        }
        float[] padOffScreenLan2Vtmp = {this.padSizeScreenLan2V[0] / 2.0f, this.padSizeScreenLan2V[1] / 2.0f, this.mHeight - (this.padSizeScreenLan2V[2] / 2.0f), this.padSizeScreenLan2V[3] / 2.0f, ((this.mHeight / 2f) - this.padSizeScreenLan2V[4]) - 30.0f, this.padSizeScreenLan2V[5] / 2.0f, (this.mHeight / 2f) + 30, this.padSizeScreenLan2V[7] / 2.0f, this.padSizeScreenLan2V[8] / 2.0f, (this.mWidth / 2f) - (this.padSizeScreenLan2V[9] / 2.0f), (this.padSizeScreenLan2V[10] / 2.0f) - this.padSizeScreenLan2V[8], (this.mWidth / 2f) - (this.padSizeScreenLan2V[11] / 2.0f), this.mHeight - (this.padSizeScreenLan2V[12] / 2.0f), (this.mWidth / 2f) - (this.padSizeScreenLan2V[13] / 2.0f), (this.mHeight - (this.padSizeScreenLan2V[14] / 2.0f)) - this.padSizeScreenLan2V[12], (this.mWidth / 2f) - (this.padSizeScreenLan2V[15] / 2.0f)};
        for (int i6 = 0; i6 < 8; i6++) {
            this.padOffScreenLan2V[(i6 * 2) + 0] = padOffScreenLan2Vtmp[(i6 * 2) + 0];
            this.padOffScreenLan2V[(i6 * 2) + 1] = padOffScreenLan2Vtmp[(i6 * 2) + 1];
        }
        this.initvirtualPad = 0;
    }

    public void loadExtraButtons() {
        if (this.emu_player_mode == 1) {
            String cpadprofile = this.padprofile;
            if (this.mePSXeReadPreferences == null) {
                this.mePSXeReadPreferences = new ePSXeReadPreferences(this.mContext);
            }
            this.padScreenExtraCombo = 0;
            this.padScreenExtraEnabled = 0;
            if (this.emu_screen_orientation == 1) {
                this.padprofile = "";
            }
            for (int i = 0; i < 6; i++) {
                int val = this.mePSXeReadPreferences.getPadExtra(this.padprofile + "inputExtrasPref" + (i + 1));
                if (val == 19) {
                    val = -1;
                }
                if (val == 28) {
                    val = 19;
                }
                if (val == -1) {
                    this.padScreenStatus[0][i + 14] = 2;
                    this.padScreenStatus[1][i + 14] = 2;
                } else {
                    this.padScreenExtraEnabled = 1;
                    this.padScreenStatus[0][i + 14] = 1;
                    this.padScreenStatus[1][i + 14] = 1;
                }
                this.padScreenExtra[i] = val;
                if (this.padScreenExtra[i] >= 0 && this.padScreenExtra[i] < 5) {
                    this.padScreenFunc[i] = 1;
                } else if (this.padScreenExtra[i] < 10) {
                    this.padScreenFunc[i] = 1;
                } else if (this.padScreenExtra[i] < 18) {
                    this.padScreenFunc[i] = 2;
                    this.padScreenExtraCombo = 1;
                } else if (this.padScreenExtra[i] == 18) {
                    this.padScreenFunc[i] = 1;
                } else if (this.padScreenExtra[i] == 19) {
                    this.padScreenFunc[i] = 1;
                } else if (this.padScreenExtra[i] == 20) {
                    this.padScreenFunc[i] = 1;
                } else if (this.padScreenExtra[i] == 21) {
                    this.padScreenFunc[i] = 1;
                } else if (this.padScreenExtra[i] == 22) {
                    this.padScreenFunc[i] = 1;
                } else if (this.padScreenExtra[i] == 23) {
                    this.padScreenFunc[i] = 1;
                } else if (this.padScreenExtra[i] == 24) {
                    this.padScreenFunc[i] = 1;
                } else if (this.padScreenExtra[i] == 25) {
                    this.padScreenFunc[i] = 1;
                } else if (this.padScreenExtra[i] == 26) {
                    this.padScreenFunc[i] = 1;
                } else if (this.padScreenExtra[i] == 27) {
                    this.padScreenFunc[i] = 1;
                } else {
                    this.padScreenFunc[i] = 0;
                }
            }
            if (this.emu_screen_orientation == 1) {
                this.padprofile = cpadprofile;
            }
        }
    }

    public void redoPads() {
        if (this.mePSXeReadPreferences == null) {
            this.mePSXeReadPreferences = new ePSXeReadPreferences(this.mContext);
        }
        if (this.mePSXeReadPreferences.getPadStatus(this.padprofile + "Pad1Status1") != -1 && this.emu_player_mode == 1 && (this.emu_screen_orientation != 1 || this.emu_portrait_skin == 1)) {
            Log.e("epsxeviewgl", "loading pad info from preferences");
            int tmode = this.mode;
            this.mode = 0;
            resetPad1Values();
            this.mode = 1;
            resetPad1Values();
            this.mode = tmode;
            this.mWidthSaved = this.mePSXeReadPreferences.getPadWH(this.padprofile + "Pad1Width");
            this.mHeightSaved = this.mePSXeReadPreferences.getPadWH(this.padprofile + "Pad1Height");
            float resizeX = 1.0f;
            float resizeY = 1.0f;
            if (this.mWidthSaved != 0 && this.mWidthSaved != this.mWidth) {
                resizeX = (float)this.mWidth / this.mWidthSaved;
            }
            if (this.mHeightSaved != 0 && this.mHeightSaved != this.mHeight) {
                resizeY = (float)this.mHeight / this.mHeightSaved;
            }
            for (int i = 1; i < 14; i++) {
                int val = this.mePSXeReadPreferences.getPadStatus(this.padprofile + "Pad1Status" + i);
                if (val != -1) {
                    this.padScreenStatus[0][i - 1] = val;
                }
            }
            for (int i2 = 1; i2 < 14; i2++) {
                int val2 = this.mePSXeReadPreferences.getPadStatus(this.padprofile + "Pad1StatusAnalog" + i2);
                if (val2 != -1) {
                    this.padScreenStatus[1][i2 - 1] = val2;
                }
            }
            for (int i3 = 1; i3 < 14; i3++) {
                float x = this.mePSXeReadPreferences.getPadSize(this.padprofile + "Pad1SizeX" + i3);
                float y = this.mePSXeReadPreferences.getPadSize(this.padprofile + "Pad1SizeY" + i3);
                if (x != -1.0f) {
                    this.padSizeScreenLan[0][(i3 - 1) * 2] = x;
                    this.padSizeScreenLan[0][((i3 - 1) * 2) + 1] = y;
                }
            }
            for (int i4 = 14; i4 < 20; i4++) {
                float x2 = this.mePSXeReadPreferences.getPadSize(this.padprofile + "Pad1SizeX" + i4);
                float y2 = this.mePSXeReadPreferences.getPadSize(this.padprofile + "Pad1SizeY" + i4);
                if (x2 != -1.0f) {
                    this.padSizeScreenLan[0][i4 * 2] = x2;
                    this.padSizeScreenLan[0][(i4 * 2) + 1] = y2;
                }
            }
            for (int i5 = 1; i5 < 14; i5++) {
                float x3 = this.mePSXeReadPreferences.getPadSize(this.padprofile + "Pad1SizeXAnalog" + i5);
                float y3 = this.mePSXeReadPreferences.getPadSize(this.padprofile + "Pad1SizeYAnalog" + i5);
                if (x3 != -1.0f) {
                    this.padSizeScreenLan[1][(i5 - 1) * 2] = x3;
                    this.padSizeScreenLan[1][((i5 - 1) * 2) + 1] = y3;
                }
            }
            for (int i6 = 14; i6 < 20; i6++) {
                float x4 = this.mePSXeReadPreferences.getPadSize(this.padprofile + "Pad1SizeXAnalog" + i6);
                float y4 = this.mePSXeReadPreferences.getPadSize(this.padprofile + "Pad1SizeYAnalog" + i6);
                if (x4 != -1.0f) {
                    this.padSizeScreenLan[1][i6 * 2] = x4;
                    this.padSizeScreenLan[1][(i6 * 2) + 1] = y4;
                }
            }
            for (int i7 = 1; i7 < 14; i7++) {
                float x5 = this.mePSXeReadPreferences.getPadSize(this.padprofile + "Pad1PosX" + i7);
                float y5 = this.mePSXeReadPreferences.getPadSize(this.padprofile + "Pad1PosY" + i7);
                if (x5 != -1.0f) {
                    this.padOffScreenLan[0][(i7 - 1) * 2] = x5;
                    this.padOffScreenLan[0][((i7 - 1) * 2) + 1] = y5;
                }
            }
            for (int i8 = 14; i8 < 20; i8++) {
                float x6 = this.mePSXeReadPreferences.getPadSize(this.padprofile + "Pad1PosX" + i8);
                float y6 = this.mePSXeReadPreferences.getPadSize(this.padprofile + "Pad1PosY" + i8);
                if (x6 != -1.0f) {
                    this.padOffScreenLan[0][i8 * 2] = x6;
                    this.padOffScreenLan[0][(i8 * 2) + 1] = y6;
                }
            }
            for (int i9 = 1; i9 < 14; i9++) {
                float x7 = this.mePSXeReadPreferences.getPadSize(this.padprofile + "Pad1PosXAnalog" + i9);
                float y7 = this.mePSXeReadPreferences.getPadSize(this.padprofile + "Pad1PosYAnalog" + i9);
                if (x7 != -1.0f) {
                    this.padOffScreenLan[1][(i9 - 1) * 2] = x7;
                    this.padOffScreenLan[1][((i9 - 1) * 2) + 1] = y7;
                }
            }
            for (int i10 = 14; i10 < 20; i10++) {
                float x8 = this.mePSXeReadPreferences.getPadSize(this.padprofile + "Pad1PosXAnalog" + i10);
                float y8 = this.mePSXeReadPreferences.getPadSize(this.padprofile + "Pad1PosYAnalog" + i10);
                if (x8 != -1.0f) {
                    this.padOffScreenLan[1][i10 * 2] = x8;
                    this.padOffScreenLan[1][(i10 * 2) + 1] = y8;
                }
            }
            for (int i11 = 1; i11 < 14; i11++) {
                float val3 = this.mePSXeReadPreferences.getPadResize(this.padprofile + "Pad1Resize" + i11);
                if (val3 != -1.0f) {
                    this.padScreenResize[0][i11 - 1] = val3;
                }
            }
            for (int i12 = 14; i12 < 20; i12++) {
                float val4 = this.mePSXeReadPreferences.getPadResize(this.padprofile + "Pad1Resize" + i12);
                if (val4 != -1.0f) {
                    this.padScreenResize[0][i12] = val4;
                }
            }
            for (int i13 = 1; i13 < 14; i13++) {
                float val5 = this.mePSXeReadPreferences.getPadResize(this.padprofile + "Pad1ResizeAnalog" + i13);
                if (val5 != -1.0f) {
                    this.padScreenResize[1][i13 - 1] = val5;
                }
            }
            for (int i14 = 14; i14 < 20; i14++) {
                float val6 = this.mePSXeReadPreferences.getPadResize(this.padprofile + "Pad1ResizeAnalog" + i14);
                if (val6 != -1.0f) {
                    this.padScreenResize[1][i14] = val6;
                }
            }
            for (int i15 = 0; i15 < 20; i15++) {
                this.padSizeScreenLan[0][(i15 * 2) + 0] = this.padSizeScreenLan[0][(i15 * 2) + 0] * resizeX;
                this.padSizeScreenLan[0][(i15 * 2) + 1] = this.padSizeScreenLan[0][(i15 * 2) + 1] * resizeY;
                this.padSizeScreenLan[1][(i15 * 2) + 0] = this.padSizeScreenLan[1][(i15 * 2) + 0] * resizeX;
                this.padSizeScreenLan[1][(i15 * 2) + 1] = this.padSizeScreenLan[1][(i15 * 2) + 1] * resizeY;
            }
            for (int i16 = 0; i16 < 20; i16++) {
                this.padOffScreenLan[0][(i16 * 2) + 0] = this.padOffScreenLan[0][(i16 * 2) + 0] * resizeX;
                this.padOffScreenLan[0][(i16 * 2) + 1] = this.padOffScreenLan[0][(i16 * 2) + 1] * resizeY;
                this.padOffScreenLan[1][(i16 * 2) + 0] = this.padOffScreenLan[1][(i16 * 2) + 0] * resizeX;
                this.padOffScreenLan[1][(i16 * 2) + 1] = this.padOffScreenLan[1][(i16 * 2) + 1] * resizeY;
            }
            loadExtraButtons();
            if (this.mGLThread != null) {
                this.mGLThread.loadExtraButtonsTextures();
            }
        } else {
            Log.e("epsxepadeditor", "setting default pad info");
            if (this.emu_player_mode == 1 && this.emu_screen_orientation == 1 && this.emu_portrait_skin == 0) {
                loadExtraButtons();
                if (this.mGLThread != null) {
                    this.mGLThread.loadExtraButtonsTextures();
                }
            } else {
                int tmode2 = this.mode;
                loadExtraButtons();
                if (this.mGLThread != null) {
                    this.mGLThread.loadExtraButtonsTextures();
                }
                this.mode = 0;
                resetPad1Values();
                this.mode = 1;
                resetPad1Values();
                this.mode = tmode2;
            }
        }
        resetPadAllValues();
        this.initvirtualPad = 0;
        if (this.emu_player_mode == 1) {
            if (this.emu_pad_mode[0] == 1 || this.emu_pad_mode[0] == 4) {
                if (this.emu_screen_orientation == 1 && this.emu_portrait_skin == 0) {
                    init_motionevent_1playerPortrait();
                    return;
                } else {
                    init_motionevent_1playerLandscape();
                    resetDynamicPad();
                    return;
                }
            }
            if (this.emu_pad_mode[0] == 3 || this.emu_pad_mode[0] == 8) {
                this.gun.initGun(this.mWidth, this.mHeight, this.emu_pad_type_selected);
            }
        }
    }

    public void FlipGL() {
        this.mGLThread.FlipGL();
    }

    private class EmuGLThread extends Thread {
        SpriteBatch2 batchPor;
        SpriteBatch2 batchTools;
        boolean csat;
        boolean cscolor;
        boolean csnormal;
        boolean cstexture;
        boolean csvertex;
        private GLText2 glText;
        int hTexPor;
        int hTexTools;
        int hTextmp;
        private FloatBuffer mColorBuffer;
        private EGL10 mEgl;
        private EGLConfig mEglConfig;
        private EGLContext mEglContext;
        private EGLDisplay mEglDisplay;
        private EGLSurface mEglSurface;
        private FloatBuffer mFVertexBuffer;
        private GL10 mGL;
        private ShortBuffer mIndexBuffer;
        int mProgram;
        int mProgramNT;
        boolean mblend;
        int mpadAlphaLoc;
        int mpadColorLoc;
        int mpadColorNtLoc;
        int mpadFrameLoc;
        int mpadPositionLoc;
        int mpadPositionNTLoc;
        int mpadTexCoordLoc;
        boolean msci;
        boolean mtex2d;

        /* renamed from: sv */
        SurfaceView f171sv;
        TextureRegion textureRgnPor;
        TextureRegion textureRgnTools;
        int wTexPor;
        int wTexTools;
        int wTextmp;
        private boolean mDone = false;
        private int[] intVar = new int[16];
        private FloatBuffer floatBuffer = FloatBuffer.allocate(16);
        int flimit = 0;
        int mTexPor = -1;
        int mTexLan = -1;
        int mTexExtra = -1;
        SpriteBatch2[] batchLan = new SpriteBatch2[28];
        TextureRegion[] textureRgnLan = new TextureRegion[28];
        SpriteBatch2[] batchLanAction = new SpriteBatch2[4];
        TextureRegion[] textureRgnLanAction = new TextureRegion[4];
        int[] hTexLan = new int[28];
        int[] wTexLan = new int[28];
        int[] hTexLanAction = new int[4];
        int[] wTexLanAction = new int[4];
        float[] sizeActionX = new float[4];
        float[] sizeActionY = new float[4];
        float[] offActionX = new float[4];
        float[] offActionY = new float[4];
        SpriteBatch2[] batchLanDpad = new SpriteBatch2[4];
        TextureRegion[] textureRgnLanDpad = new TextureRegion[4];
        int[] hTexLanDpad = new int[4];
        int[] wTexLanDpad = new int[4];
        float[] sizeDpadX = new float[4];
        float[] sizeDpadY = new float[4];
        float[] offDpadX = new float[4];
        float[] offDpadY = new float[4];
        int mTexTools = -1;
        int[] viewport = new int[4];
        protected ShortBuffer shortBuffer = null;
        private final String sVertexShader = "attribute vec4 a_position;attribute vec2 a_texCoord;varying vec2 v_texCoord;void main(){    gl_Position = a_position;    v_texCoord = a_texCoord;}";
        private final String pFragmentShader = "precision mediump float;varying vec2 v_texCoord;uniform sampler2D Frame;uniform float padAlpha;void main(){    gl_FragColor = texture2D(Frame, v_texCoord) * padAlpha;}";
        private final String sVertexShaderNT = "attribute vec4 a_position;attribute vec4 a_color;varying vec4 v_color;void main(){    gl_Position = a_position;    v_color = a_color;}";
        private final String pFragmentShaderNT = "precision mediump float;varying vec4 v_color;void main(){    gl_FragColor = v_color;}";
        int[] padCoordsExtra = {0, 0, 64, 64, 64, 0, 128, 64, 128, 0, InputList.KEYCODE_BUTTON_5, 64, InputList.KEYCODE_BUTTON_5, 0, 256, 64, 0, 64, 64, 128, 64, 64, 128, 128, 128, 64, InputList.KEYCODE_BUTTON_5, 128, InputList.KEYCODE_BUTTON_5, 64, 256, 128, 0, 128, 64, InputList.KEYCODE_BUTTON_9, 64, 128, 128, InputList.KEYCODE_BUTTON_9, 128, 128, InputList.KEYCODE_BUTTON_5, InputList.KEYCODE_BUTTON_9, InputList.KEYCODE_BUTTON_5, 128, 256, InputList.KEYCODE_BUTTON_9, 0, InputList.KEYCODE_BUTTON_5, 64, 256, 64, InputList.KEYCODE_BUTTON_5, 128, 256, 128, InputList.KEYCODE_BUTTON_5, InputList.KEYCODE_BUTTON_5, 256, InputList.KEYCODE_BUTTON_5, InputList.KEYCODE_BUTTON_5, 256, 256, 0, 256, 64, 320, 64, 256, 128, 320, 128, 256, InputList.KEYCODE_BUTTON_7, 320, InputList.KEYCODE_BUTTON_5, 256, 256, 320, 0, 320, 64, 384, 64, 320, 128, 384, 128, 320, InputList.KEYCODE_BUTTON_9, 384, InputList.KEYCODE_BUTTON_5, 320, 256, 384, 0, 384, 64, 448, 63, 384, 127, 448, 128, 384, InputList.KEYCODE_BUTTON_9, 448, InputList.KEYCODE_BUTTON_5, 384, 256, 448, 0, 448, 64, 511, 64, 448, 128, 511, 128, 448, InputList.KEYCODE_BUTTON_9, 511, InputList.KEYCODE_BUTTON_5, 448, 256, 511, 256, 0, 320, 64, 320, 0, 384, 64, 384, 0, 448, 64, 448, 0, 511, 64};

        EmuGLThread(SurfaceView view) {
            this.f171sv = view;
        }

        private int newTextureID() {
            int[] temp = new int[1];
            GLES20.glGenTextures(1, temp, 0);
            return temp[0];
        }

        private int loadTexture(Context context, int resource) {
            int id = newTextureID();
            Matrix flip = new Matrix();
            flip.setTranslate(1.0f, -1.0f);
            flip.postScale(1.0f, 1.0f);
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inScaled = false;
            Bitmap temp = BitmapFactory.decodeResource(context.getResources(), resource, opts);
            Bitmap bmp = Bitmap.createBitmap(temp, 0, 0, temp.getWidth(), temp.getHeight(), flip, true);
            temp.recycle();
            GLES20.glBindTexture(3553, id);
            GLES20.glTexParameterf(3553, 10241, 9729.0f);
            GLES20.glTexParameterf(3553, 10240, 9729.0f);
            GLES20.glTexParameterf(3553, 10242, 10497.0f);
            GLES20.glTexParameterf(3553, 10243, 10497.0f);
            this.wTextmp = bmp.getWidth();
            this.hTextmp = bmp.getHeight();
            GLUtils.texImage2D(3553, 0, bmp, 0);
            GLES20.glBindTexture(3553, 0);
            bmp.recycle();
            return id;
        }

        private int loadTextureFromFile(Context context, String filename) {
            int id = newTextureID();
            Matrix flip = new Matrix();
            flip.setTranslate(1.0f, -1.0f);
            flip.postScale(1.0f, 1.0f);
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inScaled = false;
            Bitmap temp = BitmapFactory.decodeFile(filename);
            Bitmap bmp = Bitmap.createBitmap(temp, 0, 0, temp.getWidth(), temp.getHeight(), flip, true);
            temp.recycle();
            GLES20.glBindTexture(3553, id);
            GLES20.glTexParameterf(3553, 10241, 9729.0f);
            GLES20.glTexParameterf(3553, 10240, 9729.0f);
            GLES20.glTexParameterf(3553, 10242, 10497.0f);
            GLES20.glTexParameterf(3553, 10243, 10497.0f);
            this.wTextmp = bmp.getWidth();
            this.hTextmp = bmp.getHeight();
            GLUtils.texImage2D(3553, 0, bmp, 0);
            GLES20.glBindTexture(3553, 0);
            bmp.recycle();
            return id;
        }

        public void checkGLError(String op) {
            while (true) {
                int error = GLES20.glGetError();
                if (error != 0) {
                    Log.e("epsxegl", op + ": glError " + error);
                } else {
                    return;
                }
            }
        }

        public void saveGLState(GL10 test) {
            this.mblend = GLES20.glIsEnabled(3042);
            this.msci = GLES20.glIsEnabled(3089);
            GLES20.glGetIntegerv(32873, this.intVar, 0);
            GLES20.glGetIntegerv(32971, this.intVar, 1);
            GLES20.glGetIntegerv(32970, this.intVar, 2);
            GLES20.glGetTexParameteriv(3553, 10241, this.intVar, 3);
            GLES20.glGetTexParameteriv(3553, 10240, this.intVar, 4);
            GLES20.glGetTexParameteriv(3553, 10242, this.intVar, 5);
            GLES20.glGetTexParameteriv(3553, 10243, this.intVar, 6);
            GLES20.glGetIntegerv(35725, this.intVar, 7);
            GLES20.glGetIntegerv(2978, this.viewport, 0);
            GLES20.glGetIntegerv(3106, this.intVar, 8);
            GLES20.glClearColor(0.0f, 0.0f, 0.0f, 128.0f);
            GLES20.glViewport(0, 0, ePSXeViewGL2ext.this.mWidth, ePSXeViewGL2ext.this.mHeight);
            GLES20.glDisable(3089);
            GLES20.glBlendFunc(770, 771);
            checkGLError("saveGLState");
        }

        public void restoreGLState(GL10 test) {
            if (this.mblend) {
                GLES20.glEnable(3042);
            } else {
                GLES20.glDisable(3042);
            }
            if (this.msci) {
                GLES20.glEnable(3089);
            } else {
                GLES20.glDisable(3089);
            }
            GLES20.glBindTexture(3553, this.intVar[0]);
            GLES20.glBlendFunc(this.intVar[1], this.intVar[2]);
            GLES20.glTexParameteri(3553, 10241, this.intVar[3]);
            GLES20.glTexParameteri(3553, 10240, this.intVar[4]);
            GLES20.glTexParameteri(3553, 10242, this.intVar[5]);
            GLES20.glTexParameteri(3553, 10243, this.intVar[6]);
            GLES20.glUseProgram(this.intVar[7]);
            GLES20.glClearColor(this.intVar[8], this.intVar[9], this.intVar[10], this.intVar[11]);
            GLES20.glViewport(this.viewport[0], this.viewport[1], this.viewport[2], this.viewport[3]);
            checkGLError("restoreGLState");
        }

        public void drawFPS(GL10 test) {
            int value = ePSXeViewGL2ext.this.f170e.gpugetoptionfixesgl() & 32767;
            String vs = ePSXeViewGL2ext.this.emu_enable_tools == 1 ? String.format(" 0x%04x", Integer.valueOf(value)) : "";
            GLES20.glEnable(3042);
            this.glText.begin(1.0f, 0.1f, 0.0f, 1.0f);
            if (ePSXeViewGL2ext.this.emu_plugin == 5) {
                this.glText.draw(String.valueOf(ePSXeViewGL2ext.this.f170e.getFPS()) + "/" + ePSXeViewGL2ext.this.mfps + "*" + vs, ePSXeViewGL2ext.this.overscan_x + 0, (ePSXeViewGL2ext.this.mHeight - this.glText.getCharHeight()) - ePSXeViewGL2ext.this.overscan_y, ePSXeViewGL2ext.this.mWidth, ePSXeViewGL2ext.this.mHeight);
            } else {
                this.glText.draw(String.valueOf(ePSXeViewGL2ext.this.f170e.getFPS()) + "/" + ePSXeViewGL2ext.this.mfps + "x" + vs, ePSXeViewGL2ext.this.overscan_x + 0, (ePSXeViewGL2ext.this.mHeight - this.glText.getCharHeight()) - ePSXeViewGL2ext.this.overscan_y, ePSXeViewGL2ext.this.mWidth, ePSXeViewGL2ext.this.mHeight);
            }
            this.glText.end();
            GLES20.glDisable(3042);
        }

        public void drawGPUOutdated(GL10 test) {
            GLES20.glEnable(3042);
            this.glText.begin(1.0f, 1.0f, 1.0f, 1.0f);
            this.glText.draw("OpenGL Plugin outdated, get a new copy from google play", ePSXeViewGL2ext.this.overscan_x + 0, (ePSXeViewGL2ext.this.mHeight - this.glText.getCharHeight()) - ePSXeViewGL2ext.this.overscan_y, ePSXeViewGL2ext.this.mWidth, ePSXeViewGL2ext.this.mHeight);
            this.glText.end();
            GLES20.glDisable(3042);
        }

        public void drawPad(GL10 test) {
            try {
                if (ePSXeViewGL2ext.this.emu_player_mode != 1 || ePSXeViewGL2ext.this.emu_screen_orientation != 1 || ePSXeViewGL2ext.this.emu_portrait_skin != 0) {
                    if (ePSXeViewGL2ext.this.emu_player_mode != 1 || ePSXeViewGL2ext.this.emu_pad_draw_mode[0] == 2 || ePSXeViewGL2ext.this.emu_pad_mode[0] == 3 || ePSXeViewGL2ext.this.emu_pad_mode[0] == 8) {
                        if (ePSXeViewGL2ext.this.emu_pad_mode[0] == 3 || ePSXeViewGL2ext.this.emu_pad_mode[0] == 8) {
                            ePSXeViewGL2ext.this.gun.drawGunGl(this.mTexLan, this.mProgram, this.textureRgnLan, this.batchLan, ePSXeViewGL2ext.this.mWidth, ePSXeViewGL2ext.this.mHeight);
                            return;
                        }
                        return;
                    }
                    int st = 0;
                    int en = 8;
                    if (ePSXeViewGL2ext.this.emu_pad_mode[ePSXeViewGL2ext.this.emu_pad_type_selected] == 4) {
                        if (ePSXeViewGL2ext.this.emu_pad_mode_analog[ePSXeViewGL2ext.this.emu_pad_type_selected] == 0) {
                            st = 0;
                            en = 9;
                        } else {
                            st = 0;
                            en = 13;
                        }
                    }
                    if (!ePSXeViewGL2ext.this.hidePad) {
                        GLES20.glBindTexture(3553, this.mTexLan);
                    }
                    GLES20.glUseProgram(this.mProgram);
                    GLES20.glEnable(3042);
                    for (int i = st; i < en; i++) {
                        if (ePSXeViewGL2ext.this.padScreenStatus[ePSXeViewGL2ext.this.mode][i] == 1) {
                            if (i > 1) {
                                if (i < 11 && i != 8) {
                                    if ((ePSXeViewGL2ext.this.statebuttons & ePSXeViewGL2ext.this.psxbuttonval[i]) != 0) {
                                        this.batchLan[i].beginBatch(this.mTexLan);
                                        this.batchLan[i].drawSprite((ePSXeViewGL2ext.this.padOffScreenLan[ePSXeViewGL2ext.this.mode][(i * 2) + 0] / ePSXeViewGL2ext.this.mWidth) * ePSXeViewGL2ext.this.glresizeX, (ePSXeViewGL2ext.this.padOffScreenLan[ePSXeViewGL2ext.this.mode][(i * 2) + 1] / ePSXeViewGL2ext.this.mHeight) * ePSXeViewGL2ext.this.glresizeY, ((ePSXeViewGL2ext.this.padSizeScreenLan[ePSXeViewGL2ext.this.mode][(i * 2) + 0] * ePSXeViewGL2ext.this.padScreenResize[ePSXeViewGL2ext.this.mode][i]) / ePSXeViewGL2ext.this.mWidth) * ePSXeViewGL2ext.this.buttonMag * ePSXeViewGL2ext.this.glresizeX, ((ePSXeViewGL2ext.this.padSizeScreenLan[ePSXeViewGL2ext.this.mode][(i * 2) + 1] * ePSXeViewGL2ext.this.padScreenResize[ePSXeViewGL2ext.this.mode][i]) / ePSXeViewGL2ext.this.mHeight) * ePSXeViewGL2ext.this.buttonMag * ePSXeViewGL2ext.this.glresizeY, this.textureRgnLan[i]);
                                        this.batchLan[i].endBatch();
                                    } else {
                                        this.batchLan[i].beginBatch(this.mTexLan);
                                        this.batchLan[i].drawSprite((ePSXeViewGL2ext.this.padOffScreenLan[ePSXeViewGL2ext.this.mode][(i * 2) + 0] / ePSXeViewGL2ext.this.mWidth) * ePSXeViewGL2ext.this.glresizeX, (ePSXeViewGL2ext.this.padOffScreenLan[ePSXeViewGL2ext.this.mode][(i * 2) + 1] / ePSXeViewGL2ext.this.mHeight) * ePSXeViewGL2ext.this.glresizeY, ((ePSXeViewGL2ext.this.padSizeScreenLan[ePSXeViewGL2ext.this.mode][(i * 2) + 0] * ePSXeViewGL2ext.this.padScreenResize[ePSXeViewGL2ext.this.mode][i]) / ePSXeViewGL2ext.this.mWidth) * ePSXeViewGL2ext.this.glresizeX, ((ePSXeViewGL2ext.this.padSizeScreenLan[ePSXeViewGL2ext.this.mode][(i * 2) + 1] * ePSXeViewGL2ext.this.padScreenResize[ePSXeViewGL2ext.this.mode][i]) / ePSXeViewGL2ext.this.mHeight) * ePSXeViewGL2ext.this.glresizeY, this.textureRgnLan[i]);
                                        this.batchLan[i].endBatch();
                                    }
                                } else {
                                    this.batchLan[i].beginBatch(this.mTexLan);
                                    this.batchLan[i].drawSprite((ePSXeViewGL2ext.this.padOffScreenLan[ePSXeViewGL2ext.this.mode][(i * 2) + 0] / ePSXeViewGL2ext.this.mWidth) * ePSXeViewGL2ext.this.glresizeX, (ePSXeViewGL2ext.this.padOffScreenLan[ePSXeViewGL2ext.this.mode][(i * 2) + 1] / ePSXeViewGL2ext.this.mHeight) * ePSXeViewGL2ext.this.glresizeY, ((ePSXeViewGL2ext.this.padSizeScreenLan[ePSXeViewGL2ext.this.mode][(i * 2) + 0] * ePSXeViewGL2ext.this.padScreenResize[ePSXeViewGL2ext.this.mode][i]) / ePSXeViewGL2ext.this.mWidth) * ePSXeViewGL2ext.this.glresizeX, ((ePSXeViewGL2ext.this.padSizeScreenLan[ePSXeViewGL2ext.this.mode][(i * 2) + 1] * ePSXeViewGL2ext.this.padScreenResize[ePSXeViewGL2ext.this.mode][i]) / ePSXeViewGL2ext.this.mHeight) * ePSXeViewGL2ext.this.glresizeY, this.textureRgnLan[i]);
                                    this.batchLan[i].endBatch();
                                }
                            } else if (i == 0) {
                                if (ePSXeViewGL2ext.this.emu_pad_draw_mode[0] == 4) {
                                    this.batchLan[i].beginBatch(this.mTexLan);
                                    this.batchLan[i].drawSprite(ePSXeViewGL2ext.this.padOffScreenLan[ePSXeViewGL2ext.this.mode][(i * 2) + 0] / ePSXeViewGL2ext.this.mWidth, ePSXeViewGL2ext.this.padOffScreenLan[ePSXeViewGL2ext.this.mode][(i * 2) + 1] / ePSXeViewGL2ext.this.mHeight, (ePSXeViewGL2ext.this.padSizeScreenLan[ePSXeViewGL2ext.this.mode][(i * 2) + 0] * ePSXeViewGL2ext.this.padScreenResize[ePSXeViewGL2ext.this.mode][i]) / ePSXeViewGL2ext.this.mWidth, (ePSXeViewGL2ext.this.padSizeScreenLan[ePSXeViewGL2ext.this.mode][(i * 2) + 1] * ePSXeViewGL2ext.this.padScreenResize[ePSXeViewGL2ext.this.mode][i]) / ePSXeViewGL2ext.this.mHeight, this.textureRgnLan[i]);
                                    this.batchLan[i].endBatch();
                                }
                                if (ePSXeViewGL2ext.this.dpadskin == 1) {
                                    float sx = ePSXeViewGL2ext.this.padSizeScreenLan[ePSXeViewGL2ext.this.mode][(i * 2) + 0] * ePSXeViewGL2ext.this.padScreenResize[ePSXeViewGL2ext.this.mode][i] * ePSXeViewGL2ext.this.glresizeX;
                                    float sy = ePSXeViewGL2ext.this.padSizeScreenLan[ePSXeViewGL2ext.this.mode][(i * 2) + 1] * ePSXeViewGL2ext.this.padScreenResize[ePSXeViewGL2ext.this.mode][i] * ePSXeViewGL2ext.this.glresizeY;
                                    float ox = (ePSXeViewGL2ext.this.padOffScreenLan[ePSXeViewGL2ext.this.mode][(i * 2) + 0] * ePSXeViewGL2ext.this.glresizeX) - (sx / 2.0f);
                                    float oy = (ePSXeViewGL2ext.this.padOffScreenLan[ePSXeViewGL2ext.this.mode][(i * 2) + 1] * ePSXeViewGL2ext.this.glresizeY) - (sy / 2.0f);
                                    for (int p = 0; p < 4; p++) {
                                        if ((ePSXeViewGL2ext.this.statebuttons & (4096 << p)) == 0) {
                                            if (ePSXeViewGL2ext.this.emu_pad_draw_mode[0] != 4) {
                                                this.batchLanDpad[p].beginBatch(this.mTexLan);
                                                this.batchLanDpad[p].drawSprite(((this.offDpadX[p] * sx) + ox) / ePSXeViewGL2ext.this.mWidth, ((this.offDpadY[p] * sy) + oy) / ePSXeViewGL2ext.this.mHeight, (this.sizeDpadX[p] * sx) / ePSXeViewGL2ext.this.mWidth, (this.sizeDpadY[p] * sy) / ePSXeViewGL2ext.this.mHeight, this.textureRgnLanDpad[p]);
                                                this.batchLanDpad[p].endBatch();
                                            }
                                        } else {
                                            this.batchLanDpad[p].beginBatch(this.mTexLan);
                                            this.batchLanDpad[p].drawSprite(((this.offDpadX[p] * sx) + ox) / ePSXeViewGL2ext.this.mWidth, ((this.offDpadY[p] * sy) + oy) / ePSXeViewGL2ext.this.mHeight, ((this.sizeDpadX[p] * sx) / ePSXeViewGL2ext.this.mWidth) * ePSXeViewGL2ext.this.buttonMag, ((this.sizeDpadY[p] * sy) / ePSXeViewGL2ext.this.mHeight) * ePSXeViewGL2ext.this.buttonMag, this.textureRgnLanDpad[p]);
                                            this.batchLanDpad[p].endBatch();
                                        }
                                    }
                                } else {
                                    this.batchLan[i].beginBatch(this.mTexLan);
                                    this.batchLan[i].drawSprite(ePSXeViewGL2ext.this.padOffScreenLan[ePSXeViewGL2ext.this.mode][(i * 2) + 0] / ePSXeViewGL2ext.this.mWidth, ePSXeViewGL2ext.this.padOffScreenLan[ePSXeViewGL2ext.this.mode][(i * 2) + 1] / ePSXeViewGL2ext.this.mHeight, (ePSXeViewGL2ext.this.padSizeScreenLan[ePSXeViewGL2ext.this.mode][(i * 2) + 0] * ePSXeViewGL2ext.this.padScreenResize[ePSXeViewGL2ext.this.mode][i]) / ePSXeViewGL2ext.this.mWidth, (ePSXeViewGL2ext.this.padSizeScreenLan[ePSXeViewGL2ext.this.mode][(i * 2) + 1] * ePSXeViewGL2ext.this.padScreenResize[ePSXeViewGL2ext.this.mode][i]) / ePSXeViewGL2ext.this.mHeight, this.textureRgnLan[i]);
                                    this.batchLan[i].endBatch();
                                }
                            } else {
                                if (ePSXeViewGL2ext.this.emu_pad_draw_mode[0] == 4) {
                                    this.batchLan[i].beginBatch(this.mTexLan);
                                    this.batchLan[i].drawSprite(ePSXeViewGL2ext.this.padOffScreenLan[ePSXeViewGL2ext.this.mode][(i * 2) + 0] / ePSXeViewGL2ext.this.mWidth, ePSXeViewGL2ext.this.padOffScreenLan[ePSXeViewGL2ext.this.mode][(i * 2) + 1] / ePSXeViewGL2ext.this.mHeight, (ePSXeViewGL2ext.this.padSizeScreenLan[ePSXeViewGL2ext.this.mode][(i * 2) + 0] * ePSXeViewGL2ext.this.padScreenResize[ePSXeViewGL2ext.this.mode][i]) / ePSXeViewGL2ext.this.mWidth, (ePSXeViewGL2ext.this.padSizeScreenLan[ePSXeViewGL2ext.this.mode][(i * 2) + 1] * ePSXeViewGL2ext.this.padScreenResize[ePSXeViewGL2ext.this.mode][i]) / ePSXeViewGL2ext.this.mHeight, this.textureRgnLan[i]);
                                    this.batchLan[i].endBatch();
                                }
                                float sx2 = ePSXeViewGL2ext.this.padSizeScreenLan[ePSXeViewGL2ext.this.mode][(i * 2) + 0] * ePSXeViewGL2ext.this.padScreenResize[ePSXeViewGL2ext.this.mode][i] * ePSXeViewGL2ext.this.glresizeX;
                                float sy2 = ePSXeViewGL2ext.this.padSizeScreenLan[ePSXeViewGL2ext.this.mode][(i * 2) + 1] * ePSXeViewGL2ext.this.padScreenResize[ePSXeViewGL2ext.this.mode][i] * ePSXeViewGL2ext.this.glresizeY;
                                float ox2 = (ePSXeViewGL2ext.this.padOffScreenLan[ePSXeViewGL2ext.this.mode][(i * 2) + 0] * ePSXeViewGL2ext.this.glresizeX) - (sx2 / 2.0f);
                                float oy2 = (ePSXeViewGL2ext.this.padOffScreenLan[ePSXeViewGL2ext.this.mode][(i * 2) + 1] * ePSXeViewGL2ext.this.glresizeY) - (sy2 / 2.0f);
                                for (int p2 = 0; p2 < 4; p2++) {
                                    if ((ePSXeViewGL2ext.this.statebuttons & (16 << p2)) == 0) {
                                        if (ePSXeViewGL2ext.this.emu_pad_draw_mode[0] != 4) {
                                            this.batchLanAction[p2].beginBatch(this.mTexLan);
                                            this.batchLanAction[p2].drawSprite(((this.offActionX[p2] * sx2) + ox2) / ePSXeViewGL2ext.this.mWidth, ((this.offActionY[p2] * sy2) + oy2) / ePSXeViewGL2ext.this.mHeight, (this.sizeActionX[p2] * sx2) / ePSXeViewGL2ext.this.mWidth, (this.sizeActionY[p2] * sy2) / ePSXeViewGL2ext.this.mHeight, this.textureRgnLanAction[p2]);
                                            this.batchLanAction[p2].endBatch();
                                        }
                                    } else {
                                        this.batchLanAction[p2].beginBatch(this.mTexLan);
                                        this.batchLanAction[p2].drawSprite(((this.offActionX[p2] * sx2) + ox2) / ePSXeViewGL2ext.this.mWidth, ((this.offActionY[p2] * sy2) + oy2) / ePSXeViewGL2ext.this.mHeight, ((this.sizeActionX[p2] * sx2) / ePSXeViewGL2ext.this.mWidth) * ePSXeViewGL2ext.this.buttonMag, ((this.sizeActionY[p2] * sy2) / ePSXeViewGL2ext.this.mHeight) * ePSXeViewGL2ext.this.buttonMag, this.textureRgnLanAction[p2]);
                                        this.batchLanAction[p2].endBatch();
                                    }
                                }
                            }
                            if (i == 11 || i == 12) {
                                this.batchLan[i].beginBatch(this.mTexLan);
                                this.batchLan[i].drawSprite(((float)ePSXeViewGL2ext.this.analog_values[0][(i - 11) * 2] / ePSXeViewGL2ext.this.mWidth) * ePSXeViewGL2ext.this.screenResize, ((float)ePSXeViewGL2ext.this.analog_values[0][((i - 11) * 2) + 1] / ePSXeViewGL2ext.this.mHeight) * ePSXeViewGL2ext.this.screenResize, (((ePSXeViewGL2ext.this.padSizeScreenLan[ePSXeViewGL2ext.this.mode][26] * ePSXeViewGL2ext.this.padScreenResize[ePSXeViewGL2ext.this.mode][i]) * 2.0f) / ePSXeViewGL2ext.this.mWidth) * ePSXeViewGL2ext.this.screenResize, (((ePSXeViewGL2ext.this.padSizeScreenLan[ePSXeViewGL2ext.this.mode][27] * ePSXeViewGL2ext.this.padScreenResize[ePSXeViewGL2ext.this.mode][i]) * 2.0f) / ePSXeViewGL2ext.this.mHeight) * ePSXeViewGL2ext.this.screenResize, this.textureRgnLan[13]);
                                this.batchLan[i].endBatch();
                            }
                        }
                    }
                    GLES20.glDisable(3042);
                    GLES20.glBindTexture(3553, this.mTexExtra);
                    GLES20.glUseProgram(this.mProgram);
                    GLES20.glEnable(3042);
                    if (ePSXeViewGL2ext.this.padScreenExtraEnabled == 1 && !ePSXeViewGL2ext.this.hidePad) {
                        for (int i2 = 14; i2 < 20; i2++) {
                            if (ePSXeViewGL2ext.this.padScreenStatus[ePSXeViewGL2ext.this.mode][i2] == 1) {
                                if (ePSXeViewGL2ext.this.padScreenExtra[i2 - 14] >= 20 && ePSXeViewGL2ext.this.stickyButton[ePSXeViewGL2ext.this.padScreenExtra[i2 - 14] - 20] == 1) {
                                    int j = ePSXeViewGL2ext.this.padScreenExtra[i2 - 14];
                                    this.batchLan[i2].beginBatch(this.mTexExtra);
                                    this.batchLan[i2].drawSprite(ePSXeViewGL2ext.this.padOffScreenLan[ePSXeViewGL2ext.this.mode][(i2 * 2) + 0] / ePSXeViewGL2ext.this.mWidth, ePSXeViewGL2ext.this.padOffScreenLan[ePSXeViewGL2ext.this.mode][(i2 * 2) + 1] / ePSXeViewGL2ext.this.mHeight, (ePSXeViewGL2ext.this.padSizeScreenLan[ePSXeViewGL2ext.this.mode][(i2 * 2) + 0] * ePSXeViewGL2ext.this.padScreenResize[ePSXeViewGL2ext.this.mode][i2]) / ePSXeViewGL2ext.this.mWidth, (ePSXeViewGL2ext.this.padSizeScreenLan[ePSXeViewGL2ext.this.mode][(i2 * 2) + 1] * ePSXeViewGL2ext.this.padScreenResize[ePSXeViewGL2ext.this.mode][i2]) / ePSXeViewGL2ext.this.mHeight, this.textureRgnLan[j]);
                                    this.batchLan[i2].endBatch();
                                } else {
                                    this.batchLan[i2].beginBatch(this.mTexExtra);
                                    this.batchLan[i2].drawSprite(ePSXeViewGL2ext.this.padOffScreenLan[ePSXeViewGL2ext.this.mode][(i2 * 2) + 0] / ePSXeViewGL2ext.this.mWidth, ePSXeViewGL2ext.this.padOffScreenLan[ePSXeViewGL2ext.this.mode][(i2 * 2) + 1] / ePSXeViewGL2ext.this.mHeight, (ePSXeViewGL2ext.this.padSizeScreenLan[ePSXeViewGL2ext.this.mode][(i2 * 2) + 0] * ePSXeViewGL2ext.this.padScreenResize[ePSXeViewGL2ext.this.mode][i2]) / ePSXeViewGL2ext.this.mWidth, (ePSXeViewGL2ext.this.padSizeScreenLan[ePSXeViewGL2ext.this.mode][(i2 * 2) + 1] * ePSXeViewGL2ext.this.padScreenResize[ePSXeViewGL2ext.this.mode][i2]) / ePSXeViewGL2ext.this.mHeight, this.textureRgnLan[i2]);
                                    this.batchLan[i2].endBatch();
                                }
                            }
                        }
                    }
                    GLES20.glDisable(3042);
                    return;
                }
                GLES20.glBlendFunc(1, 0);
                GLES20.glBindTexture(3553, this.mTexPor);
                GLES20.glUseProgram(this.mProgram);
                this.batchPor.beginBatch(this.mTexPor);
                this.batchPor.drawSprite(ePSXeViewGL2ext.this.padOffScreenPor[0] / ePSXeViewGL2ext.this.mWidth, ePSXeViewGL2ext.this.padOffScreenPor[1] / ePSXeViewGL2ext.this.mHeight, ePSXeViewGL2ext.this.padSizeScreenPor[0] / ePSXeViewGL2ext.this.mWidth, ePSXeViewGL2ext.this.padSizeScreenPor[1] / ePSXeViewGL2ext.this.mHeight, this.textureRgnPor);
                this.batchPor.endBatch();
                GLES20.glBlendFunc(770, 771);
                if (ePSXeViewGL2ext.this.padScreenExtraEnabled == 1 && !ePSXeViewGL2ext.this.hidePad) {
                    GLES20.glBindTexture(3553, this.mTexExtra);
                    GLES20.glUseProgram(this.mProgram);
                    GLES20.glEnable(3042);
                    for (int i3 = 14; i3 < 20; i3++) {
                        if (ePSXeViewGL2ext.this.padScreenStatus[ePSXeViewGL2ext.this.mode][i3] == 1) {
                            if (ePSXeViewGL2ext.this.padScreenExtra[i3 - 14] >= 20 && ePSXeViewGL2ext.this.stickyButton[ePSXeViewGL2ext.this.padScreenExtra[i3 - 14] - 20] == 1) {
                                int j2 = ePSXeViewGL2ext.this.padScreenExtra[i3 - 14];
                                this.batchLan[i3].beginBatch(this.mTexExtra);
                                this.batchLan[i3].drawSprite(ePSXeViewGL2ext.this.padOffScreenPor[(i3 * 2) + 0] / ePSXeViewGL2ext.this.mWidth, ePSXeViewGL2ext.this.padOffScreenPor[(i3 * 2) + 1] / ePSXeViewGL2ext.this.mHeight, ePSXeViewGL2ext.this.padSizeScreenPor[(i3 * 2) + 0] / ePSXeViewGL2ext.this.mWidth, ePSXeViewGL2ext.this.padSizeScreenPor[(i3 * 2) + 1] / ePSXeViewGL2ext.this.mHeight, this.textureRgnLan[j2]);
                                this.batchLan[i3].endBatch();
                            } else {
                                this.batchLan[i3].beginBatch(this.mTexExtra);
                                this.batchLan[i3].drawSprite(ePSXeViewGL2ext.this.padOffScreenPor[(i3 * 2) + 0] / ePSXeViewGL2ext.this.mWidth, ePSXeViewGL2ext.this.padOffScreenPor[(i3 * 2) + 1] / ePSXeViewGL2ext.this.mHeight, ePSXeViewGL2ext.this.padSizeScreenPor[(i3 * 2) + 0] / ePSXeViewGL2ext.this.mWidth, ePSXeViewGL2ext.this.padSizeScreenPor[(i3 * 2) + 1] / ePSXeViewGL2ext.this.mHeight, this.textureRgnLan[i3]);
                                this.batchLan[i3].endBatch();
                            }
                        }
                    }
                    GLES20.glDisable(3042);
                }
            } catch (Exception e) {
            }
        }

        public void drawTools(GL10 test) {
            GLES20.glEnable(3042);
            GLES20.glBindTexture(3553, this.mTexTools);
            this.batchTools.beginBatch(this.mTexTools);
            this.batchTools.drawSprite(((ePSXeViewGL2ext.this.mWidth / 2f) * ePSXeViewGL2ext.this.screenResize) / ePSXeViewGL2ext.this.mWidth, ((ePSXeViewGL2ext.this.mHeight - ((ePSXeViewGL2ext.this.padResize * 60.0f) / 2.0f)) * ePSXeViewGL2ext.this.screenResize) / ePSXeViewGL2ext.this.mHeight, ((508.0f * ePSXeViewGL2ext.this.padResize) * ePSXeViewGL2ext.this.screenResize) / ePSXeViewGL2ext.this.mWidth, ((ePSXeViewGL2ext.this.padResize * 60.0f) * ePSXeViewGL2ext.this.screenResize) / ePSXeViewGL2ext.this.mHeight, this.textureRgnTools);
            this.batchTools.endBatch();
            GLES20.glDisable(3042);
        }

        public void drawLicense(GL10 test) {
            GLES20.glEnable(3042);
            this.glText.begin(1.0f, 1.0f, 1.0f, 1.0f);
            this.glText.draw("License not validated yet. Read the documentation.", 60.0f, 0.0f, ePSXeViewGL2ext.this.mWidth, ePSXeViewGL2ext.this.mHeight);
            this.glText.end();
            GLES20.glDisable(3042);
        }

        public void drawBiosMsg(GL10 test) {
            GLES20.glEnable(3042);
            this.glText.begin(1.0f, 1.0f, 1.0f, 1.0f);
            this.glText.draw("Using HLE Bios, compatibility and options limited. Read the documentation.", 60.0f, 0.0f, ePSXeViewGL2ext.this.mWidth, ePSXeViewGL2ext.this.mHeight);
            this.glText.end();
            GLES20.glDisable(3042);
        }

        public void drawString(GL10 test, String s) {
            GLES20.glEnable(3042);
            this.glText.begin(1.0f, 1.0f, 1.0f, 1.0f);
            this.glText.draw(s, 60.0f, 0.0f, ePSXeViewGL2ext.this.mWidth, ePSXeViewGL2ext.this.mHeight);
            this.glText.end();
            GLES20.glDisable(3042);
        }

        public void drawSelected(int v, int c) {
            float[] color = new float[4];
            if (c == 1) {
                color[0] = 0.0f;
                color[1] = 1.0f;
                color[2] = 1.0f;
                color[3] = 1.0f;
            }
            if (c == 2) {
                color[0] = 1.0f;
                color[1] = 0.0f;
                color[2] = 1.0f;
                color[3] = 1.0f;
            }
            if (c == 3) {
                color[0] = 1.0f;
                color[1] = 1.0f;
                color[2] = 0.0f;
                color[3] = 1.0f;
            }
            if (c == 4) {
                color[0] = 1.0f;
                color[1] = 0.0f;
                color[2] = 0.0f;
                color[3] = 1.0f;
            }
            if (c == 5) {
                color[0] = 0.0f;
                color[1] = 1.0f;
                color[2] = 0.0f;
                color[3] = 1.0f;
            }
            if (c == 6) {
                color[0] = 0.0f;
                color[1] = 0.0f;
                color[2] = 1.0f;
                color[3] = 1.0f;
            }
            if (c == 7) {
                color[0] = 1.0f;
                color[1] = 1.0f;
                color[2] = 1.0f;
                color[3] = 1.0f;
            }
            ByteBuffer vbb = ByteBuffer.allocateDirect(48);
            vbb.order(ByteOrder.nativeOrder());
            this.mFVertexBuffer = vbb.asFloatBuffer();
            ByteBuffer cbb = ByteBuffer.allocateDirect(64);
            cbb.order(ByteOrder.nativeOrder());
            this.mColorBuffer = cbb.asFloatBuffer();
            ByteBuffer ibb = ByteBuffer.allocateDirect(8);
            ibb.order(ByteOrder.nativeOrder());
            this.mIndexBuffer = ibb.asShortBuffer();
            float ox = (((ePSXeViewGL2ext.this.mWidth - (508.0f * ePSXeViewGL2ext.this.padResize)) / 2.0f) + (((508.0f * ePSXeViewGL2ext.this.padResize) / 9.0f) * v)) * ePSXeViewGL2ext.this.screenResize;
            float oy = (ePSXeViewGL2ext.this.mHeight - (60.0f * ePSXeViewGL2ext.this.padResize)) * ePSXeViewGL2ext.this.screenResize;
            float sw = ((508.0f * ePSXeViewGL2ext.this.padResize) / 9.0f) * ePSXeViewGL2ext.this.screenResize;
            float sh = 60.0f * ePSXeViewGL2ext.this.padResize * ePSXeViewGL2ext.this.screenResize;
            float[] coords = {(((1.0f + ox) / ePSXeViewGL2ext.this.mWidth) * 2.0f) - 1.0f, (((1.0f + oy) / ePSXeViewGL2ext.this.mHeight) * 2.0f) - 1.0f, 0.0f, ((((ox + sw) - 1.0f) / ePSXeViewGL2ext.this.mWidth) * 2.0f) - 1.0f, (((1.0f + oy) / ePSXeViewGL2ext.this.mHeight) * 2.0f) - 1.0f, 0.0f, ((((ox + sw) - 1.0f) / ePSXeViewGL2ext.this.mWidth) * 2.0f) - 1.0f, ((((oy + sh) - 1.0f) / ePSXeViewGL2ext.this.mHeight) * 2.0f) - 1.0f, 0.0f, (((1.0f + ox) / ePSXeViewGL2ext.this.mWidth) * 2.0f) - 1.0f, ((((oy + sh) - 1.0f) / ePSXeViewGL2ext.this.mHeight) * 2.0f) - 1.0f, 0.0f};
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 3; j++) {
                    this.mFVertexBuffer.put(coords[(i * 3) + j]);
                }
            }
            for (int i2 = 0; i2 < 4; i2++) {
                for (int j2 = 0; j2 < 4; j2++) {
                    this.mColorBuffer.put(color[j2]);
                }
            }
            for (int i3 = 0; i3 < 4; i3++) {
                this.mIndexBuffer.put((short) i3);
            }
            this.mFVertexBuffer.position(0);
            this.mColorBuffer.position(0);
            this.mIndexBuffer.position(0);
            GLES20.glUseProgram(this.mProgramNT);
            GLES20.glVertexAttribPointer(this.mpadPositionNTLoc, 3, 5126, false, 0, (Buffer) this.mFVertexBuffer);
            GLES20.glVertexAttribPointer(this.mpadColorNtLoc, 4, 5126, false, 0, (Buffer) this.mColorBuffer);
            GLES20.glEnableVertexAttribArray(this.mpadPositionNTLoc);
            GLES20.glEnableVertexAttribArray(this.mpadColorNtLoc);
            GLES20.glDrawElements(2, 4, 5123, this.mIndexBuffer);
        }

        public void drawSelectedTools(GL10 test) {
            int value = ePSXeViewGL2ext.this.emu_opengl_options;
            if (ePSXeViewGL2ext.this.emu_plugin == 5) {
                if ((value & 1) != 0) {
                    drawSelected(0, 1);
                }
                if ((value & 14) != 0) {
                    drawSelected(1, (value >> 1) & 7);
                }
                if ((value & 16) != 0) {
                    drawSelected(2, 1);
                }
                if ((value & 96) != 0) {
                    drawSelected(3, (value >> 5) & 3);
                }
                if ((value & 384) != 0) {
                    drawSelected(4, (value >> 7) & 3);
                }
                if ((value & 1536) != 0) {
                    drawSelected(5, (value >> 9) & 3);
                }
                if ((value & 2048) != 0) {
                    drawSelected(6, 1);
                    return;
                }
                return;
            }
            if ((value & 7) != 0) {
                drawSelected(1, value & 7);
            }
            if ((value & 56) != 0) {
                drawSelected(2, (value >> 3) & 7);
            }
            if ((value & 448) != 0) {
                drawSelected(5, (value >> 6) & 3);
            }
            if ((32768 & value) != 0) {
                drawSelected(4, 1);
            }
            if ((value & 16384) != 0) {
                drawSelected(3, 1);
            }
            if ((value & 8192) != 0) {
                drawSelected(6, 1);
            }
            if ((value & 4096) != 0) {
                drawSelected(0, 1);
            }
        }

        void saveImage(Bitmap myBitmap) {
            String root = ContextCompat.getDataDir(getContext()).toString();
            File myDir = new File(root + "/epsxe/sstates/");
            File file = new File(myDir, "ImageXX.jpg");
            if (file.exists()) {
                file.delete();
            }
            try {
                FileOutputStream out = new FileOutputStream(file);
                myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        void saveImageRaw(short[] sb) {
            String root = ContextCompat.getDataDir(getContext()).toString();
            File myDir = new File(root + "/epsxe/sstates/");
            String fname = ePSXeViewGL2ext.this.f170e.getCode() + ".00" + (ePSXeViewGL2ext.this.screenshot - 10) + ".pic";
            if (ePSXeViewGL2ext.this.biosmsg) {
                fname = ePSXeViewGL2ext.this.f170e.getCode() + "HLE.00" + (ePSXeViewGL2ext.this.screenshot - 10) + ".pic";
            }
            File file = new File(myDir, fname);
            byte[] bb = new byte[36864];
            for (int i = 0; i < 12288; i++) {
                bb[(i * 3) + 2] = (byte) ((sb[i] << 3) & 248);
                bb[(i * 3) + 1] = (byte) ((sb[i] >> 3) & 252);
                bb[(i * 3) + 0] = (byte) ((sb[i] >> 8) & 248);
            }
            if (file.exists()) {
                file.delete();
            }
            try {
                FileOutputStream out = new FileOutputStream(file);
                out.write(bb);
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void doMiniPic(GL10 test) {
            int lwidth = ePSXeViewGL2ext.this.mWidth;
            int lheight = ePSXeViewGL2ext.this.mHeight;
            if (ePSXeViewGL2ext.this.emu_screen_orientation == 1) {
                lheight = ePSXeViewGL2ext.this.mHeight / 2;
            }
            int screenshotSize = lwidth * lheight;
            ByteBuffer bb = ByteBuffer.allocateDirect(screenshotSize * 4);
            bb.order(ByteOrder.nativeOrder());
            if (ePSXeViewGL2ext.this.emu_screen_orientation == 1) {
                GLES20.glReadPixels(0, lheight, lwidth, lheight, 6408, 5121, bb);
            } else {
                GLES20.glReadPixels(0, 0, lwidth, lheight, 6408, 5121, bb);
            }
            int[] pixelsBuffer = new int[screenshotSize];
            bb.asIntBuffer().get(pixelsBuffer);
            Bitmap bitmap = Bitmap.createBitmap(lwidth, lheight, Bitmap.Config.RGB_565);
            bitmap.setPixels(pixelsBuffer, screenshotSize - lwidth, -lwidth, 0, 0, lwidth, lheight);
            Bitmap sbit = Bitmap.createScaledBitmap(bitmap, 128, 96, false);
            short[] sBuffer = new short[12288];
            ShortBuffer sb = ShortBuffer.wrap(sBuffer);
            sbit.copyPixelsToBuffer(sb);
            for (int i = 0; i < 12288; i++) {
                short v = sBuffer[i];
                sBuffer[i] = (short) (((v & 31) << 11) | (v & 2016) | ((63488 & v) >> 11));
            }
            sb.rewind();
            sbit.copyPixelsFromBuffer(sb);
            saveImageRaw(sBuffer);
            ePSXeViewGL2ext.this.screenshot = 0;
        }

        public void FlipGL() {
            if (ePSXeViewGL2ext.this.emu_enable_framelimit == 1 || this.flimit > 7) {
                this.flimit = 0;
                if (ePSXeViewGL2ext.this.screenshot > 0) {
                    doMiniPic(this.mGL);
                }
                saveGLState(this.mGL);
                if (ePSXeViewGL2ext.this.emu_player_mode == 1 && ePSXeViewGL2ext.this.emu_pad_draw_mode[0] != 2) {
                    drawPad(this.mGL);
                }
                if (ePSXeViewGL2ext.this.emu_enable_printfps == 1 && ePSXeViewGL2ext.this.emu_player_mode == 1) {
                    drawFPS(this.mGL);
                }
                if (!ePSXeViewGL2ext.this.license) {
                    drawLicense(this.mGL);
                }
                if (ePSXeViewGL2ext.this.emu_enable_tools == 1) {
                    drawTools(this.mGL);
                    drawSelectedTools(this.mGL);
                }
                if (ePSXeViewGL2ext.this.emu_gpu_mt_mode > 0 && ePSXeViewGL2ext.this.gpuVersion == -1) {
                    ePSXeViewGL2ext.this.gpuVersion = ePSXeViewGL2ext.this.f170e.getGPUVersion();
                }
                if (ePSXeViewGL2ext.this.gpuVersion < 16 && ePSXeViewGL2ext.this.gpuVersion >= 0) {
                    if (ePSXeViewGL2ext.this.emu_verbose == 1) {
                        drawGPUOutdated(this.mGL);
                    }
                    if (ePSXeViewGL2ext.this.gpuVersionAdvise > 0) {
                        ePSXeViewGL2ext.access$3710(ePSXeViewGL2ext.this);
                    } else {
                        ePSXeViewGL2ext.this.gpuVersion = 16;
                    }
                }
                if (!ePSXeViewGL2ext.this.biosmsg || ePSXeViewGL2ext.this.biosVersionAdvise <= 0) {
                    if (ePSXeViewGL2ext.this.gprofile && ePSXeViewGL2ext.this.gProfileAdvise > 0) {
                        if (ePSXeViewGL2ext.this.emu_verbose == 1) {
                            drawString(this.mGL, "Loading custom game profile...");
                        }
                        ePSXeViewGL2ext.access$4010(ePSXeViewGL2ext.this);
                    }
                } else {
                    if (ePSXeViewGL2ext.this.emu_verbose == 1) {
                        drawBiosMsg(this.mGL);
                    }
                    ePSXeViewGL2ext.access$3810(ePSXeViewGL2ext.this);
                }
                this.mEgl.eglSwapBuffers(this.mEglDisplay, this.mEglSurface);
                if (!ePSXeViewGL2ext.this.bufferPreserved) {
                    GLES20.glClear(16384);
                }
                restoreGLState(this.mGL);
            }
            this.flimit++;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            initEGL();
            initGL();
            ePSXeViewGL2ext.this.f170e.setwh(ePSXeViewGL2ext.this.mWidthDraw, ePSXeViewGL2ext.this.mHeightDraw, ePSXeViewGL2ext.this.emu_screen_ratio);
            ePSXeViewGL2ext.this.mfps = ePSXeViewGL2ext.this.f170e.loadepsxe(ePSXeViewGL2ext.this.mIsoName, ePSXeViewGL2ext.this.mIsoSlot);
            if (!ePSXeViewGL2ext.this.mIsoName.equals("___RUNBIOS___")) {
                try {
                    if (ePSXeViewGL2ext.this.mfps == -1) {
                        Toast.makeText(ePSXeViewGL2ext.this.mContext, ePSXeViewGL2ext.this.mContext.getString(R.string.viewglext_error) + " (.bin/.mdf/.img not found)!!!!", 1).show();
                        ePSXeViewGL2ext.this.mfps = 60;
                    } else if (ePSXeViewGL2ext.this.mfps > 60) {
                        Toast.makeText(ePSXeViewGL2ext.this.mContext, R.string.main_systemcnf, 1).show();
                        ePSXeViewGL2ext.this.mfps -= 100;
                    }
                } catch (Exception e) {
                }
            } else {
                ePSXeViewGL2ext.this.mfps = 60;
            }
            if (ePSXeViewGL2ext.this.emu_gpu_mt_mode > 0) {
                ePSXeViewGL2ext.this.mGLRunThread = new ePSXeWrapperThread();
                ePSXeViewGL2ext.this.mGLRunThread.setePSXeLib(ePSXeViewGL2ext.this.f170e);
                ePSXeViewGL2ext.this.mGLRunThread.start();
                ePSXeViewGL2ext.this.f170e.runwrapper(0);
                ePSXeViewGL2ext.this.mGLRunThread.exit();
            } else {
                ePSXeViewGL2ext.this.gpuVersion = ePSXeViewGL2ext.this.f170e.getGPUVersion();
                while (!this.mDone) {
                    ePSXeViewGL2ext.this.f170e.runemulatorframeGLext();
                }
                ePSXeViewGL2ext.this.f170e.runemulatorframeGLext();
                ePSXeViewGL2ext.this.f170e.runemulatorframeGLext();
                ePSXeViewGL2ext.this.f170e.runemulatorframeGLext();
            }
            cleanupGL();
            Log.e("epsxegl", "OGL: cleanup opengl done.");
        }

        public void requestStop() {
            this.mDone = true;
        }

        public void cleanupGL() {
            if (this.mEgl != null) {
                this.mEgl.eglDestroyContext(this.mEglDisplay, this.mEglContext);
                this.mEgl.eglDestroySurface(this.mEglDisplay, this.mEglSurface);
            }
        }

        private int loadShader(int type, String shaderCode) {
            int shader = GLES20.glCreateShader(type);
            GLES20.glShaderSource(shader, shaderCode);
            GLES20.glCompileShader(shader);
            return shader;
        }

        public void loadExtraButtonsTextures() {
            this.hTextmp = 512;
            this.wTextmp = 512;
            for (int i = 0; i < 6; i++) {
                int j = ePSXeViewGL2ext.this.padScreenExtra[i];
                if (j >= 0) {
                    if (j >= 24) {
                        j += 4;
                    }
                    this.wTexLan[i + 14] = this.padCoordsExtra[(j * 4) + 2] - this.padCoordsExtra[(j * 4) + 0];
                    this.hTexLan[i + 14] = this.padCoordsExtra[(j * 4) + 3] - this.padCoordsExtra[(j * 4) + 1];
                    this.textureRgnLan[i + 14] = new TextureRegion(this.wTextmp, this.hTextmp, this.padCoordsExtra[(j * 4) + 0], this.padCoordsExtra[(j * 4) + 1], this.wTexLan[i + 14], this.hTexLan[i + 14]);
                    this.batchLan[i + 14] = new SpriteBatch2(null, 1, this.mProgram, this.mpadPositionLoc, this.mpadTexCoordLoc, this.mpadFrameLoc, this.mpadAlphaLoc, ePSXeViewGL2ext.this.emu_input_alpha);
                }
            }
        }

        public void initGL() {
            GLES20.glViewport(0, 0, ePSXeViewGL2ext.this.mWidth, ePSXeViewGL2ext.this.mHeight);
            GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
            int vertexShader = loadShader(35633, "attribute vec4 a_position;attribute vec2 a_texCoord;varying vec2 v_texCoord;void main(){    gl_Position = a_position;    v_texCoord = a_texCoord;}");
            int fragmentShader = loadShader(35632, "precision mediump float;varying vec2 v_texCoord;uniform sampler2D Frame;uniform float padAlpha;void main(){    gl_FragColor = texture2D(Frame, v_texCoord) * padAlpha;}");
            int vertexShaderNT = loadShader(35633, "attribute vec4 a_position;attribute vec4 a_color;varying vec4 v_color;void main(){    gl_Position = a_position;    v_color = a_color;}");
            int fragmentShaderNT = loadShader(35632, "precision mediump float;varying vec4 v_color;void main(){    gl_FragColor = v_color;}");
            this.mProgram = GLES20.glCreateProgram();
            GLES20.glAttachShader(this.mProgram, vertexShader);
            GLES20.glAttachShader(this.mProgram, fragmentShader);
            GLES20.glLinkProgram(this.mProgram);
            this.mpadPositionLoc = GLES20.glGetAttribLocation(this.mProgram, "a_position");
            this.mpadTexCoordLoc = GLES20.glGetAttribLocation(this.mProgram, "a_texCoord");
            this.mpadFrameLoc = GLES20.glGetUniformLocation(this.mProgram, "Frame");
            this.mpadAlphaLoc = GLES20.glGetUniformLocation(this.mProgram, "padAlpha");
            this.mProgramNT = GLES20.glCreateProgram();
            GLES20.glAttachShader(this.mProgramNT, vertexShaderNT);
            GLES20.glAttachShader(this.mProgramNT, fragmentShaderNT);
            GLES20.glLinkProgram(this.mProgramNT);
            this.mpadPositionNTLoc = GLES20.glGetAttribLocation(this.mProgramNT, "a_position");
            this.mpadColorNtLoc = GLES20.glGetAttribLocation(this.mProgramNT, "a_color");
            this.glText = new GLText2(null, ePSXeViewGL2ext.this.mContext.getAssets(), this.mProgram, this.mpadPositionLoc, this.mpadTexCoordLoc, this.mpadFrameLoc, this.mpadAlphaLoc, 1.0f);
            this.glText.load("Roboto-Regular.ttf", 28, 2, 2);
            ePSXeViewGL2ext.this.f170e.setwh(ePSXeViewGL2ext.this.mWidthDraw, ePSXeViewGL2ext.this.mHeightDraw, ePSXeViewGL2ext.this.emu_screen_ratio);
            if (ePSXeViewGL2ext.this.emu_player_mode != 1 || ePSXeViewGL2ext.this.emu_screen_orientation != 1 || ePSXeViewGL2ext.this.emu_portrait_skin != 0) {
                if (ePSXeViewGL2ext.this.emu_pad_mode[0] != 3) {
                    if (ePSXeViewGL2ext.this.emu_pad_mode[0] == 8) {
                        int[] padCoords = {384, 64, 448, 256, 448, 64, 511, 256};
                        this.mTexLan = loadTexture(ePSXeViewGL2ext.this.mContext, R.drawable.extra_buttons);
                        for (int i = 0; i < 2; i++) {
                            this.wTexLan[i] = padCoords[(i * 4) + 2] - padCoords[(i * 4) + 0];
                            this.hTexLan[i] = padCoords[(i * 4) + 3] - padCoords[(i * 4) + 1];
                            this.textureRgnLan[i] = new TextureRegion(this.wTextmp, this.hTextmp, padCoords[(i * 4) + 0], padCoords[(i * 4) + 1], this.wTexLan[i], this.hTexLan[i]);
                            this.batchLan[i] = new SpriteBatch2(null, 1, this.mProgram, this.mpadPositionLoc, this.mpadTexCoordLoc, this.mpadFrameLoc, this.mpadAlphaLoc, ePSXeViewGL2ext.this.emu_input_alpha);
                        }
                    } else {
                        int[] padCoords2 = {
                                //x1,  y1,  x2,  y2
                                2,   1,   224, 225, // Dpad Up, Down, Left, Right - keys
                                1,   238, 223, 486, // Action keys
                                253, 8,   309, 52,  // Stop key
                                372, 4,   433, 56,  // Play key
                                249, 80,  307, 132, // L1 key
                                308, 80,  364, 132, // L2 key
                                308, 144, 364, 196, // R1 key
                                250, 144, 307, 196, // R2 key
                                254, 208, 297, 239, // LED1
                                365, 80,  421, 132, // L3 key
                                365, 144, 422, 196, // R3 key
                                289, 289, 511, 511, // Touch pad area
                                289, 289, 511, 511, // ------"-------
                                422, 144, 491, 215 // Some ball button
                        };
                        int[] padCoordsAction = {
                                77,  244, 149, 316, // Action - triangle
                                146, 328, 218, 400, // Action - circle
                                77,  412, 149, 484, // Action - x
                                6,   328, 78,  400  // Action - square
                        };
                        int[] padCoordsDpad = {
                                76,  7,   146, 94,  // Dpad - up
                                130, 80,  218, 149, // Dpad - right
                                76,  134, 146, 220, // Dpad - down
                                4,   80,  92,  149  // Dpad - left
                        };
                        if (ePSXeViewGL2ext.this.emu_pad_draw_mode[0] == 4) {
                            File f = new File(ePSXeViewGL2ext.this.skinName);
                            if (f.exists()) {
                                this.mTexLan = loadTextureFromFile(ePSXeViewGL2ext.this.mContext, ePSXeViewGL2ext.this.skinName);
                            } else {
                                ePSXeViewGL2ext.this.emu_pad_draw_mode[0] = 0;
                            }
                        }
                        if (ePSXeViewGL2ext.this.emu_pad_draw_mode[0] == 0) {
                            this.mTexLan = loadTexture(ePSXeViewGL2ext.this.mContext, R.drawable.pure_white_digital);
                        } else if (ePSXeViewGL2ext.this.emu_pad_draw_mode[0] == 1) {
                            this.mTexLan = loadTexture(ePSXeViewGL2ext.this.mContext, R.drawable.pure_white_analog);
                        } else if (ePSXeViewGL2ext.this.emu_pad_draw_mode[0] == 10) {
                            this.mTexLan = loadTexture(ePSXeViewGL2ext.this.mContext, R.drawable.amethyst_crystal_digital);
                        } else if (ePSXeViewGL2ext.this.emu_pad_draw_mode[0] == 11) {
                            this.mTexLan = loadTexture(ePSXeViewGL2ext.this.mContext, R.drawable.amethyst_crystal_analog);
                        } else if (ePSXeViewGL2ext.this.emu_pad_draw_mode[0] == 12) {
                            this.mTexLan = loadTexture(ePSXeViewGL2ext.this.mContext, R.drawable.binchotite_digital);
                        } else if (ePSXeViewGL2ext.this.emu_pad_draw_mode[0] == 13) {
                            this.mTexLan = loadTexture(ePSXeViewGL2ext.this.mContext, R.drawable.binchotite_analog);
                        } else if (ePSXeViewGL2ext.this.emu_pad_draw_mode[0] == 14) {
                            this.mTexLan = loadTexture(ePSXeViewGL2ext.this.mContext, R.drawable.bold_amethyst_digital);
                        } else if (ePSXeViewGL2ext.this.emu_pad_draw_mode[0] == 15) {
                            this.mTexLan = loadTexture(ePSXeViewGL2ext.this.mContext, R.drawable.bold_amethyst_analog);
                        } else if (ePSXeViewGL2ext.this.emu_pad_draw_mode[0] == 16) {
                            this.mTexLan = loadTexture(ePSXeViewGL2ext.this.mContext, R.drawable.bold_digital);
                        } else if (ePSXeViewGL2ext.this.emu_pad_draw_mode[0] == 17) {
                            this.mTexLan = loadTexture(ePSXeViewGL2ext.this.mContext, R.drawable.bold_analog);
                        } else if (ePSXeViewGL2ext.this.emu_pad_draw_mode[0] == 18) {
                            this.mTexLan = loadTexture(ePSXeViewGL2ext.this.mContext, R.drawable.bold_emerald_digital);
                        } else if (ePSXeViewGL2ext.this.emu_pad_draw_mode[0] == 19) {
                            this.mTexLan = loadTexture(ePSXeViewGL2ext.this.mContext, R.drawable.bold_emerald_analog);
                        } else if (ePSXeViewGL2ext.this.emu_pad_draw_mode[0] == 20) {
                            this.mTexLan = loadTexture(ePSXeViewGL2ext.this.mContext, R.drawable.bold_gold_digital);
                        } else if (ePSXeViewGL2ext.this.emu_pad_draw_mode[0] == 21) {
                            this.mTexLan = loadTexture(ePSXeViewGL2ext.this.mContext, R.drawable.bold_gold_analog);
                        } else if (ePSXeViewGL2ext.this.emu_pad_draw_mode[0] == 22) {
                            this.mTexLan = loadTexture(ePSXeViewGL2ext.this.mContext, R.drawable.bold_pink_digital);
                        } else if (ePSXeViewGL2ext.this.emu_pad_draw_mode[0] == 23) {
                            this.mTexLan = loadTexture(ePSXeViewGL2ext.this.mContext, R.drawable.bold_pink_analog);
                        } else if (ePSXeViewGL2ext.this.emu_pad_draw_mode[0] == 24) {
                            this.mTexLan = loadTexture(ePSXeViewGL2ext.this.mContext, R.drawable.bold_sapphire_digital);
                        } else if (ePSXeViewGL2ext.this.emu_pad_draw_mode[0] == 25) {
                            this.mTexLan = loadTexture(ePSXeViewGL2ext.this.mContext, R.drawable.bold_sapphire_analog);
                        } else if (ePSXeViewGL2ext.this.emu_pad_draw_mode[0] == 26) {
                            this.mTexLan = loadTexture(ePSXeViewGL2ext.this.mContext, R.drawable.bold_silk_digital);
                        } else if (ePSXeViewGL2ext.this.emu_pad_draw_mode[0] == 27) {
                            this.mTexLan = loadTexture(ePSXeViewGL2ext.this.mContext, R.drawable.bold_silk_analog);
                        } else if (ePSXeViewGL2ext.this.emu_pad_draw_mode[0] == 28) {
                            this.mTexLan = loadTexture(ePSXeViewGL2ext.this.mContext, R.drawable.bold_turquoise_digital);
                        } else if (ePSXeViewGL2ext.this.emu_pad_draw_mode[0] == 29) {
                            this.mTexLan = loadTexture(ePSXeViewGL2ext.this.mContext, R.drawable.bold_turquoise_analog);
                        } else if (ePSXeViewGL2ext.this.emu_pad_draw_mode[0] == 30) {
                            this.mTexLan = loadTexture(ePSXeViewGL2ext.this.mContext, R.drawable.emerald_crystal_digital);
                        } else if (ePSXeViewGL2ext.this.emu_pad_draw_mode[0] == 31) {
                            this.mTexLan = loadTexture(ePSXeViewGL2ext.this.mContext, R.drawable.emerald_crystal_analog);
                        } else if (ePSXeViewGL2ext.this.emu_pad_draw_mode[0] == 32) {
                            this.mTexLan = loadTexture(ePSXeViewGL2ext.this.mContext, R.drawable.gold_crystal_digital);
                        } else if (ePSXeViewGL2ext.this.emu_pad_draw_mode[0] == 33) {
                            this.mTexLan = loadTexture(ePSXeViewGL2ext.this.mContext, R.drawable.gold_crystal_analog);
                        } else if (ePSXeViewGL2ext.this.emu_pad_draw_mode[0] == 34) {
                            this.mTexLan = loadTexture(ePSXeViewGL2ext.this.mContext, R.drawable.negative_crystal_digital);
                        } else if (ePSXeViewGL2ext.this.emu_pad_draw_mode[0] == 35) {
                            this.mTexLan = loadTexture(ePSXeViewGL2ext.this.mContext, R.drawable.negative_crystal_analog);
                        } else if (ePSXeViewGL2ext.this.emu_pad_draw_mode[0] == 36) {
                            this.mTexLan = loadTexture(ePSXeViewGL2ext.this.mContext, R.drawable.pink_crystal_digital);
                        } else if (ePSXeViewGL2ext.this.emu_pad_draw_mode[0] == 37) {
                            this.mTexLan = loadTexture(ePSXeViewGL2ext.this.mContext, R.drawable.pink_crystal_analog);
                        } else if (ePSXeViewGL2ext.this.emu_pad_draw_mode[0] == 38) {
                            this.mTexLan = loadTexture(ePSXeViewGL2ext.this.mContext, R.drawable.sapphire_crystal_digital);
                        } else if (ePSXeViewGL2ext.this.emu_pad_draw_mode[0] == 39) {
                            this.mTexLan = loadTexture(ePSXeViewGL2ext.this.mContext, R.drawable.sapphire_crystal_analog);
                        } else if (ePSXeViewGL2ext.this.emu_pad_draw_mode[0] == 40) {
                            this.mTexLan = loadTexture(ePSXeViewGL2ext.this.mContext, R.drawable.silk_crystal_digital);
                        } else if (ePSXeViewGL2ext.this.emu_pad_draw_mode[0] == 41) {
                            this.mTexLan = loadTexture(ePSXeViewGL2ext.this.mContext, R.drawable.silk_crystal_analog);
                        } else if (ePSXeViewGL2ext.this.emu_pad_draw_mode[0] == 42) {
                            this.mTexLan = loadTexture(ePSXeViewGL2ext.this.mContext, R.drawable.snow_digital);
                        } else if (ePSXeViewGL2ext.this.emu_pad_draw_mode[0] == 43) {
                            this.mTexLan = loadTexture(ePSXeViewGL2ext.this.mContext, R.drawable.snow_analog);
                        } else if (ePSXeViewGL2ext.this.emu_pad_draw_mode[0] == 44) {
                            this.mTexLan = loadTexture(ePSXeViewGL2ext.this.mContext, R.drawable.turquoise_crystal_digital);
                        } else if (ePSXeViewGL2ext.this.emu_pad_draw_mode[0] == 45) {
                            this.mTexLan = loadTexture(ePSXeViewGL2ext.this.mContext, R.drawable.turquoise_crystal_analog);
                        } else if (ePSXeViewGL2ext.this.emu_pad_draw_mode[0] != 4) {
                            this.mTexLan = loadTexture(ePSXeViewGL2ext.this.mContext, R.drawable.pure_white_digital);
                        }
                        if ((ePSXeViewGL2ext.this.emu_pad_draw_mode[0] & 1) == 0 || (ePSXeViewGL2ext.this.emu_pad_draw_mode[0] < 10 && ePSXeViewGL2ext.this.emu_pad_draw_mode[0] != 1)) {
                            ePSXeViewGL2ext.this.dpadskin = 1;
                        }
                        for (int i2 = 0; i2 < 14; i2++) {
                            this.wTexLan[i2] = padCoords2[(i2 * 4) + 2] - padCoords2[(i2 * 4) + 0];
                            this.hTexLan[i2] = padCoords2[(i2 * 4) + 3] - padCoords2[(i2 * 4) + 1];
                            this.textureRgnLan[i2] = new TextureRegion(this.wTextmp, this.hTextmp, padCoords2[(i2 * 4) + 0], padCoords2[(i2 * 4) + 1], this.wTexLan[i2], this.hTexLan[i2]);
                            this.batchLan[i2] = new SpriteBatch2(null, 1, this.mProgram, this.mpadPositionLoc, this.mpadTexCoordLoc, this.mpadFrameLoc, this.mpadAlphaLoc, ePSXeViewGL2ext.this.emu_input_alpha);
                        }
                        for (int i3 = 0; i3 < 4; i3++) {
                            this.wTexLanAction[i3] = padCoordsAction[(i3 * 4) + 2] - padCoordsAction[(i3 * 4) + 0];
                            this.hTexLanAction[i3] = padCoordsAction[(i3 * 4) + 3] - padCoordsAction[(i3 * 4) + 1];
                            this.textureRgnLanAction[i3] = new TextureRegion(this.wTextmp, this.hTextmp, padCoordsAction[(i3 * 4) + 0], padCoordsAction[(i3 * 4) + 1], this.wTexLanAction[i3], this.hTexLanAction[i3]);
                            this.batchLanAction[i3] = new SpriteBatch2(null, 1, this.mProgram, this.mpadPositionLoc, this.mpadTexCoordLoc, this.mpadFrameLoc, this.mpadAlphaLoc, ePSXeViewGL2ext.this.emu_input_alpha);
                            this.sizeActionX[i3] = (float)((padCoordsAction[(i3 * 4) + 2] - padCoords2[4]) - (padCoordsAction[(i3 * 4) + 0] - padCoords2[4])) / (padCoords2[6] - padCoords2[4]);
                            this.sizeActionY[i3] = (float)((padCoordsAction[(i3 * 4) + 3] - padCoords2[5]) - (padCoordsAction[(i3 * 4) + 1] - padCoords2[5])) / (padCoords2[7] - padCoords2[5]);
                            this.offActionX[i3] = (float)((padCoordsAction[(i3 * 4) + 0] - padCoords2[4]) + (((padCoordsAction[(i3 * 4) + 2] - padCoords2[4]) - (padCoordsAction[(i3 * 4) + 0] - padCoords2[4])) / 2)) / (padCoords2[6] - padCoords2[4]);
                            this.offActionY[i3] = 1.0f - ((float)((padCoordsAction[(i3 * 4) + 1] - padCoords2[5]) + (((padCoordsAction[(i3 * 4) + 3] - padCoords2[5]) - (padCoordsAction[(i3 * 4) + 1] - padCoords2[5])) / 2)) / (padCoords2[7] - padCoords2[5]));
                        }
                        for (int i4 = 0; i4 < 4; i4++) {
                            this.wTexLanDpad[i4] = padCoordsDpad[(i4 * 4) + 2] - padCoordsDpad[(i4 * 4) + 0];
                            this.hTexLanDpad[i4] = padCoordsDpad[(i4 * 4) + 3] - padCoordsDpad[(i4 * 4) + 1];
                            this.textureRgnLanDpad[i4] = new TextureRegion(this.wTextmp, this.hTextmp, padCoordsDpad[(i4 * 4) + 0], padCoordsDpad[(i4 * 4) + 1], this.wTexLanDpad[i4], this.hTexLanDpad[i4]);
                            this.batchLanDpad[i4] = new SpriteBatch2(null, 1, this.mProgram, this.mpadPositionLoc, this.mpadTexCoordLoc, this.mpadFrameLoc, this.mpadAlphaLoc, ePSXeViewGL2ext.this.emu_input_alpha);
                            this.sizeDpadX[i4] = (float)((padCoordsDpad[(i4 * 4) + 2] - padCoords2[0]) - (padCoordsDpad[(i4 * 4) + 0] - padCoords2[0])) / (padCoords2[2] - padCoords2[0]);
                            this.sizeDpadY[i4] = (float)((padCoordsDpad[(i4 * 4) + 3] - padCoords2[1]) - (padCoordsDpad[(i4 * 4) + 1] - padCoords2[1])) / (padCoords2[3] - padCoords2[1]);
                            this.offDpadX[i4] = (float)((padCoordsDpad[(i4 * 4) + 0] - padCoords2[0]) + (((padCoordsDpad[(i4 * 4) + 2] - padCoords2[0]) - (padCoordsDpad[(i4 * 4) + 0] - padCoords2[0])) / 2)) / (padCoords2[2] - padCoords2[0]);
                            this.offDpadY[i4] = 1.0f - ((float)((padCoordsDpad[(i4 * 4) + 1] - padCoords2[1]) + (((padCoordsDpad[(i4 * 4) + 3] - padCoords2[1]) - (padCoordsDpad[(i4 * 4) + 1] - padCoords2[1])) / 2)) / (padCoords2[3] - padCoords2[1]));
                        }
                        ePSXeViewGL2ext.this.loadExtraButtons();
                        this.mTexExtra = loadTexture(ePSXeViewGL2ext.this.mContext, R.drawable.extra_buttons);
                        loadExtraButtonsTextures();
                        for (int i5 = 0; i5 < 4; i5++) {
                            int j = i5 + 24;
                            this.wTexLan[i5 + 20] = this.padCoordsExtra[(j * 4) + 2] - this.padCoordsExtra[(j * 4) + 0];
                            this.hTexLan[i5 + 20] = this.padCoordsExtra[(j * 4) + 3] - this.padCoordsExtra[(j * 4) + 1];
                            this.textureRgnLan[i5 + 20] = new TextureRegion(this.wTextmp, this.hTextmp, this.padCoordsExtra[(j * 4) + 0], this.padCoordsExtra[(j * 4) + 1], this.wTexLan[i5 + 20], this.hTexLan[i5 + 20]);
                            this.batchLan[i5 + 20] = new SpriteBatch2(null, 1, this.mProgram, this.mpadPositionLoc, this.mpadTexCoordLoc, this.mpadFrameLoc, this.mpadAlphaLoc, ePSXeViewGL2ext.this.emu_input_alpha);
                        }
                        for (int i6 = 0; i6 < 4; i6++) {
                            int j2 = i6 + 32;
                            this.wTexLan[i6 + 24] = this.padCoordsExtra[(j2 * 4) + 2] - this.padCoordsExtra[(j2 * 4) + 0];
                            this.hTexLan[i6 + 24] = this.padCoordsExtra[(j2 * 4) + 3] - this.padCoordsExtra[(j2 * 4) + 1];
                            this.textureRgnLan[i6 + 24] = new TextureRegion(this.wTextmp, this.hTextmp, this.padCoordsExtra[(j2 * 4) + 0], this.padCoordsExtra[(j2 * 4) + 1], this.wTexLan[i6 + 24], this.hTexLan[i6 + 24]);
                            this.batchLan[i6 + 20] = new SpriteBatch2(null, 1, this.mProgram, this.mpadPositionLoc, this.mpadTexCoordLoc, this.mpadFrameLoc, this.mpadAlphaLoc, ePSXeViewGL2ext.this.emu_input_alpha);
                        }
                    }
                } else {
                    int[] padCoords3 = {256, 64, 320, 256, 320, 64, 384, 256};
                    this.mTexLan = loadTexture(ePSXeViewGL2ext.this.mContext, R.drawable.extra_buttons);
                    for (int i7 = 0; i7 < 2; i7++) {
                        this.wTexLan[i7] = padCoords3[(i7 * 4) + 2] - padCoords3[(i7 * 4) + 0];
                        this.hTexLan[i7] = padCoords3[(i7 * 4) + 3] - padCoords3[(i7 * 4) + 1];
                        this.textureRgnLan[i7] = new TextureRegion(this.wTextmp, this.hTextmp, padCoords3[(i7 * 4) + 0], padCoords3[(i7 * 4) + 1], this.wTexLan[i7], this.hTexLan[i7]);
                        this.batchLan[i7] = new SpriteBatch2(null, 1, this.mProgram, this.mpadPositionLoc, this.mpadTexCoordLoc, this.mpadFrameLoc, this.mpadAlphaLoc, ePSXeViewGL2ext.this.emu_input_alpha);
                    }
                }
            } else {
                int[] padCoords4 = {0, 0, 480, 400};
                this.mTexPor = loadTexture(ePSXeViewGL2ext.this.mContext, R.drawable.portraitpad);
                this.wTexPor = padCoords4[2] - padCoords4[0];
                this.hTexPor = padCoords4[3] - padCoords4[1];
                this.textureRgnPor = new TextureRegion(this.wTextmp, this.hTextmp, padCoords4[0], padCoords4[1], this.wTexPor, this.hTexPor);
                this.batchPor = new SpriteBatch2(null, 1, this.mProgram, this.mpadPositionLoc, this.mpadTexCoordLoc, this.mpadFrameLoc, this.mpadAlphaLoc, 1.0f);
                ePSXeViewGL2ext.this.loadExtraButtons();
                this.mTexExtra = loadTexture(ePSXeViewGL2ext.this.mContext, R.drawable.extra_buttons);
                loadExtraButtonsTextures();
                for (int i8 = 0; i8 < 4; i8++) {
                    int j3 = i8 + 24;
                    this.wTexLan[i8 + 20] = this.padCoordsExtra[(j3 * 4) + 2] - this.padCoordsExtra[(j3 * 4) + 0];
                    this.hTexLan[i8 + 20] = this.padCoordsExtra[(j3 * 4) + 3] - this.padCoordsExtra[(j3 * 4) + 1];
                    this.textureRgnLan[i8 + 20] = new TextureRegion(this.wTextmp, this.hTextmp, this.padCoordsExtra[(j3 * 4) + 0], this.padCoordsExtra[(j3 * 4) + 1], this.wTexLan[i8 + 20], this.hTexLan[i8 + 20]);
                    this.batchLan[i8 + 20] = new SpriteBatch2(null, 1, this.mProgram, this.mpadPositionLoc, this.mpadTexCoordLoc, this.mpadFrameLoc, this.mpadAlphaLoc, ePSXeViewGL2ext.this.emu_input_alpha);
                }
                for (int i9 = 0; i9 < 4; i9++) {
                    int j4 = i9 + 32;
                    this.wTexLan[i9 + 24] = this.padCoordsExtra[(j4 * 4) + 2] - this.padCoordsExtra[(j4 * 4) + 0];
                    this.hTexLan[i9 + 24] = this.padCoordsExtra[(j4 * 4) + 3] - this.padCoordsExtra[(j4 * 4) + 1];
                    this.textureRgnLan[i9 + 24] = new TextureRegion(this.wTextmp, this.hTextmp, this.padCoordsExtra[(j4 * 4) + 0], this.padCoordsExtra[(j4 * 4) + 1], this.wTexLan[i9 + 24], this.hTexLan[i9 + 24]);
                    this.batchLan[i9 + 24] = new SpriteBatch2(null, 1, this.mProgram, this.mpadPositionLoc, this.mpadTexCoordLoc, this.mpadFrameLoc, this.mpadAlphaLoc, ePSXeViewGL2ext.this.emu_input_alpha);
                }
            }
            if (ePSXeViewGL2ext.this.emu_plugin == 5) {
                this.mTexTools = loadTexture(ePSXeViewGL2ext.this.mContext, R.drawable.toolsgl2);
            } else {
                this.mTexTools = loadTexture(ePSXeViewGL2ext.this.mContext, R.drawable.toolsgl);
            }
            this.wTexTools = 508;
            this.hTexTools = 50;
            this.textureRgnTools = new TextureRegion(this.wTextmp, this.hTextmp, 0.0f, 0.0f, this.wTexTools, this.hTexTools);
            this.batchTools = new SpriteBatch2(null, 1, this.mProgram, this.mpadPositionLoc, this.mpadTexCoordLoc, this.mpadFrameLoc, this.mpadAlphaLoc, 1.0f);
        }

        public void initEGL() {
            this.mEgl = (EGL10) EGLContext.getEGL();
            this.mEglDisplay = this.mEgl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
            if (this.mEglDisplay == EGL10.EGL_NO_DISPLAY) {
                Log.e("epsxegl", "eglGetDisplay failed " + this.mEgl.eglGetError());
                return;
            }
            int[] version = new int[2];
            if (!this.mEgl.eglInitialize(this.mEglDisplay, version)) {
                Log.e("epsxegl", "eglInitialize failed " + this.mEgl.eglGetError());
                return;
            }
            this.mEglConfig = chooseEglConfig();
            if (this.mEglConfig == null) {
                Log.e("epsxegl", "eglConfig not initialized");
                return;
            }
            int[] attrib_list = {12440, 2, 12344};
            this.mEglContext = this.mEgl.eglCreateContext(this.mEglDisplay, this.mEglConfig, EGL10.EGL_NO_CONTEXT, attrib_list);
            this.mEglSurface = this.mEgl.eglCreateWindowSurface(this.mEglDisplay, this.mEglConfig, ePSXeViewGL2ext.this.mHolder, null);
            if (this.mEglSurface == null || this.mEglSurface == EGL10.EGL_NO_SURFACE) {
                int error = this.mEgl.eglGetError();
                if (this.mEglSurface == null) {
                    Log.e("epsxegl", "createWindowSurface returned null");
                }
                if (this.mEglSurface == EGL10.EGL_NO_SURFACE) {
                    Log.e("epsxegl", "createWindowSurface no surface");
                }
                Log.e("epsxegl", "createWindowSurface returned " + error);
                if (error == 12299) {
                    Log.e("epsxegl", "createWindowSurface returned EGL_BAD_NATIVE_WINDOW.");
                    return;
                } else {
                    Log.e("epsxegl", "createWindowSurface failed " + error);
                    return;
                }
            }
            if (!this.mEgl.eglMakeCurrent(this.mEglDisplay, this.mEglSurface, this.mEglSurface, this.mEglContext)) {
                Log.e("epsxegl", "eglMakeCurrent failed " + this.mEgl.eglGetError());
            }
            initBufferPreserved();
            this.mGL = (GL10) this.mEglContext.getGL();
        }

        @TargetApi(17)
        private void initBufferPreserved() {
            try {
                if (Integer.parseInt(Build.VERSION.SDK) >= 17 && ePSXeViewGL2ext.this.bufferPreserved) {
                    if (EGL14.eglSurfaceAttrib(EGL14.eglGetCurrentDisplay(), EGL14.eglGetCurrentSurface(12377), 12435, 12436)) {
                        ePSXeViewGL2ext.this.bufferPreserved = true;
                    } else {
                        ePSXeViewGL2ext.this.bufferPreserved = false;
                    }
                }
            } catch (Exception e) {
                Log.e("epsxegl", "Buffer preserved not supported");
            }
        }

        private EGLConfig chooseEglConfig() {
            int[] configsCount = new int[1];
            EGLConfig[] configs = new EGLConfig[1];
            int[] configSpec = getConfig();
            if (!this.mEgl.eglChooseConfig(this.mEglDisplay, configSpec, configs, 1, configsCount)) {
                Log.e("epsxegl", "eglChooseConfig failed " + this.mEgl.eglGetError());
            } else if (configsCount[0] > 0) {
                return configs[0];
            }
            return null;
        }

        private int[] getConfig() {
            return new int[]{12324, 5, 12323, 6, 12322, 5, 12325, 16, 12352, 4, 12344};
        }
    }

    private class ePSXeRendererGL2ext implements GLSurfaceView.Renderer {
        private ePSXeRendererGL2ext() {
        }

        @Override // android.opengl.GLSurfaceView.Renderer
        public void onDrawFrame(GL10 gl) {
        }

        @Override // android.opengl.GLSurfaceView.Renderer
        public void onSurfaceChanged(GL10 gl, int width, int height) {
        }

        @Override // android.opengl.GLSurfaceView.Renderer
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        }
    }

    private class ePSXeWrapperThread extends Thread {

        /* renamed from: e */
        private libepsxe f172e;
        private Boolean tDone;

        private ePSXeWrapperThread() {
            this.tDone = false;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            while (!this.tDone.booleanValue()) {
                this.f172e.runemulatorframeGLext();
            }
        }

        public void exit() {
            this.tDone = true;
        }

        public void setePSXeLib(libepsxe epsxelib) {
            this.f172e = epsxelib;
        }
    }
}
