package com.epsxe.ePSXe;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.os.Build;
import android.os.Vibrator;

import androidx.core.internal.view.SupportMenu;

import android.util.Log;
import android.view.MotionEvent;
import android.view.PointerIcon;

import com.epsxe.ePSXe.jni.libepsxe;
import com.epsxe.ePSXe.touchscreen.Gun;

import java.io.File;
import java.lang.reflect.Array;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import org.apache.http.HttpStatus;

/* loaded from: classes.dex */
class ePSXeViewGL extends GLSurfaceView implements ePSXeView {

    private boolean useDiagonal = false; // FALSE ЕСЛИ ДИАГОНАЛЬНАЯ АНИМАЦИЯ НЕ ТРЕБУЕТСЯ, TRUE ЕСЛИ ТРЕБУЕТСЯ
    private int lastTouchX = -1;
    private int lastTouchY = -1;
    private int animationButtonIndex = -1;
    private boolean isDpadTouchActive = false;
    int[] virtualPadId;
    private int[] activeTouchIds;
    private int[] touchButtonIds;
    private static final int MAX_TOUCHES = 10;
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
    private float buttonMag;
    private String debugString;
    private String debugString2;
    int[] dpadsection;
    private int dpadskin;

    /* renamed from: e */
    private libepsxe f165e;
    private int emu_action_dynamic;
    private int emu_enable_framelimit;
    private int emu_enable_frameskip;
    private int emu_enable_frameskip_tmp;
    private int emu_enable_printfps;
    private int emu_enable_tv;
    private int emu_gpu_blit_mode;
    private int emu_gpu_soft_mt_mode;
    private float emu_input_alpha;
    private boolean emu_mouse;
    private int[] emu_pad_draw_mode;
    private int emu_pad_dynamic;
    private int[] emu_pad_mode;
    private int[] emu_pad_mode_analog;
    private int[] emu_pad_type;
    private int emu_pad_type_selected;
    private int emu_player_mode;
    private int emu_portrait_skin;
    private int emu_screen_orientation;
    private int emu_screen_ratio;
    private int emu_screen_vrdistorsion;
    private int emu_screen_vrmode;
    private int emu_sound_latency;
    private int emu_split_mode;
    private int emu_verbose;
    private int emu_video_filter;
    private int emu_volumen;
    private int gProfileAdvise;
    private boolean gprofile;
    private Gun gun;
    int initvirtualPad;
    private boolean license;
    private Context mContext;
    private int mHeight;
    private int mHeightSaved;
    private int mWidth;
    private int mWidthSaved;
    ePSXeReadPreferences mePSXeReadPreferences;
    private int mfps;
    private int mode;
    private ePSXe myActivity;
    private int onPauseMode;
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
    private boolean redoPads;
    private int serverMode;
    private String skinName;
    private int statebuttons;
    private int[] stickyButton;
    private int tainted;
    private int[] ts_vibration;
    int[][] virtualPad;
    int[] virtualPadBit;
    int[][] virtualPadPort;
    int[][] virtualPadPos;
    private int volumenAdvise;
    private boolean hidePad = false;
    private boolean isInTouch = false;
    private ePSXeView.OnTouchListener touchListener;

    public int getStatebuttons() {
        return statebuttons;
    }

    public static void setStatebuttons(int statebuttons) {
        statebuttons = statebuttons;
    }

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

    /* renamed from: x1 */
    private int f166x1;

    /* renamed from: x2 */
    private int f167x2;

    /* renamed from: y1 */
    private int f168y1;

    /* renamed from: y2 */
    private int f169y2;

    static /* synthetic */ int access$4010(ePSXeViewGL x0) {
        int i = x0.biosVersionAdvise;
        x0.biosVersionAdvise = i - 1;
        return i;
    }

    static /* synthetic */ int access$4310(ePSXeViewGL x0) {
        int i = x0.gProfileAdvise;
        x0.gProfileAdvise = i - 1;
        return i;
    }

    static /* synthetic */ int access$4410(ePSXeViewGL x0) {
        int i = x0.volumenAdvise;
        x0.volumenAdvise = i - 1;
        return i;
    }

    public ePSXeViewGL(Context context, ePSXe act, int ry) {
        super(context);
        this.mfps = 60;
        this.ts_vibration = new int[] { 0, 0 };
        this.emu_enable_frameskip = 0;
        this.emu_enable_frameskip_tmp = 0;
        this.emu_enable_printfps = 1;
        this.emu_player_mode = 1;
        this.emu_split_mode = 0;
        this.emu_screen_orientation = 0;
        this.emu_screen_ratio = 0;
        this.emu_screen_vrmode = 0;
        this.emu_screen_vrdistorsion = 1;
        this.emu_pad_draw_mode = new int[] { 1, 1 };
        this.emu_pad_mode = new int[] { 1, 1 };
        this.emu_pad_mode_analog = new int[] { 0, 0 };
        this.emu_portrait_skin = 0;
        this.emu_input_alpha = 0.6f;
        this.emu_gpu_blit_mode = 16;
        this.mode = 0;
        this.emu_enable_framelimit = 1;
        this.emu_enable_tv = 1;
        this.overscan_x = 30;
        this.overscan_y = 4;
        this.emu_pad_type = new int[] { 0, 0 };
        this.emu_pad_type_selected = 0;
        this.emu_video_filter = 0;
        this.emu_sound_latency = 0;
        this.emu_gpu_soft_mt_mode = 0;
        this.emu_volumen = 16;
        this.license = true;
        this.gprofile = false;
        this.analog_values = new int[][] { new int[] { 0, 0, 0, 0 }, new int[] { 0, 0, 0, 0 } };
        this.f166x1 = 0;
        this.f168y1 = 0;
        this.f167x2 = 0;
        this.f169y2 = 0;
        this.debugString = "";
        this.debugString2 = "";
        this.skinName = "/sdcard/skin.png";
        this.stickyButton = new int[] { 0, 0, 0, 0, 0, 0, 0, 0 };
        this.serverMode = 0;
        this.biosmsg = false;
        this.biosVersionAdvise = InputList.KEYCODE_NUMPAD_6;
        this.gProfileAdvise = InputList.KEYCODE_NUMPAD_6;
        this.volumenAdvise = 0;
        this.tainted = 0;
        this.padprofile = "";
        this.redoPads = false;
        this.emu_pad_dynamic = 0;
        this.emu_action_dynamic = 0;
        this.statebuttons = 0;
        this.psxbuttonval = new int[] { 0, 0, 256, 2048, 4, 1, 8, 2, 0, 512, 1024 };
        this.buttonMag = 1.7f;
        this.dpadskin = 0;
        this.onPauseMode = 0;
        this.emu_verbose = 1;
        this.gun = new Gun();
        this.emu_mouse = false;
        this.mWidth = 800;
        this.mHeight = 480;
        this.mWidthSaved = 0;
        this.mHeightSaved = 0;
        this.padResize = 1.0f;
        this.padSizeScreenLan = new float[][] {
                new float[] { 228.0f, 228.0f, 224.0f, 248.0f, 66.0f, 50.0f, 66.0f, 62.0f, 64.0f, 60.0f, 64.0f, 60.0f,
                        64.0f, 60.0f, 64.0f, 60.0f, 53.0f, 41.0f, 64.0f, 60.0f, 64.0f, 60.0f, 222.0f, 222.0f, 222.0f,
                        222.0f, 71.0f, 71.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f,
                        64.0f, 64.0f },
                new float[] { 228.0f, 228.0f, 224.0f, 248.0f, 66.0f, 50.0f, 66.0f, 62.0f, 64.0f, 60.0f, 64.0f, 60.0f,
                        64.0f, 60.0f, 64.0f, 60.0f, 53.0f, 41.0f, 64.0f, 60.0f, 64.0f, 60.0f, 222.0f, 222.0f, 222.0f,
                        222.0f, 71.0f, 71.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f,
                        64.0f, 64.0f } };
        this.padOffScreenLan = new float[][] {
                new float[] { this.padSizeScreenLan[0][0] / 2.0f, this.padSizeScreenLan[0][1] / 2.0f,
                        800.0f - (this.padSizeScreenLan[0][2] / 2.0f), this.padSizeScreenLan[0][3] / 2.0f,
                        (400.0f - this.padSizeScreenLan[0][4]) - 30.0f, this.padSizeScreenLan[0][5] / 2.0f, 430.0f,
                        this.padSizeScreenLan[0][7] / 2.0f, this.padSizeScreenLan[0][8] / 2.0f,
                        480.0f - (this.padSizeScreenLan[0][9] / 2.0f),
                        (this.padSizeScreenLan[0][10] / 2.0f) + this.padSizeScreenLan[0][8],
                        480.0f - (this.padSizeScreenLan[0][11] / 2.0f), 800.0f - (this.padSizeScreenLan[0][12] / 2.0f),
                        480.0f - (this.padSizeScreenLan[0][13] / 2.0f),
                        (800.0f - (this.padSizeScreenLan[0][14] / 2.0f)) - this.padSizeScreenLan[0][12],
                        480.0f - (this.padSizeScreenLan[0][15] / 2.0f), 440.0f + this.padSizeScreenLan[0][6],
                        this.padSizeScreenLan[0][17] / 2.0f,
                        (this.padSizeScreenLan[0][18] / 2.0f)
                                + this.padSizeScreenLan[0][10] + this.padSizeScreenLan[0][8],
                        480.0f - (this.padSizeScreenLan[0][19] / 2.0f),
                        ((800.0f - (this.padSizeScreenLan[0][20] / 2.0f)) - this.padSizeScreenLan[0][12])
                                - this.padSizeScreenLan[0][14],
                        480.0f - (this.padSizeScreenLan[0][21] / 2.0f), this.padSizeScreenLan[0][22] / 2.0f,
                        this.padSizeScreenLan[0][23] / 2.0f, 800.0f - (this.padSizeScreenLan[0][24] / 2.0f),
                        this.padSizeScreenLan[0][25] / 2.0f, 0.0f, 0.0f, this.padSizeScreenLan[0][28] / 2.0f,
                        (480.0f - (this.padSizeScreenLan[0][29] / 2.0f)) - (this.padSizeScreenLan[0][10] * 2.0f),
                        (this.padSizeScreenLan[0][30] / 2.0f) + this.padSizeScreenLan[0][28],
                        (480.0f - (this.padSizeScreenLan[0][31] / 2.0f)) - (this.padSizeScreenLan[0][10] * 2.0f),
                        (this.padSizeScreenLan[0][32] / 2.0f) + this.padSizeScreenLan[0][28]
                                + this.padSizeScreenLan[0][30],
                        (480.0f - (this.padSizeScreenLan[0][33] / 2.0f)) - (this.padSizeScreenLan[0][10] * 2.0f),
                        800.0f - (this.padSizeScreenLan[0][34] / 2.0f),
                        (480.0f - (this.padSizeScreenLan[0][35] / 2.0f)) - (this.padSizeScreenLan[0][10] * 2.0f),
                        (800.0f - (this.padSizeScreenLan[0][36] / 2.0f)) - this.padSizeScreenLan[0][34],
                        (480.0f - (this.padSizeScreenLan[0][37] / 2.0f)) - (this.padSizeScreenLan[0][10] * 2.0f),
                        ((800.0f - (this.padSizeScreenLan[0][38] / 2.0f)) - this.padSizeScreenLan[0][34])
                                - this.padSizeScreenLan[0][36],
                        (480.0f - (this.padSizeScreenLan[0][39] / 2.0f)) - (this.padSizeScreenLan[0][10] * 2.0f) },
                new float[] { 400.0f - ((this.padSizeScreenLan[1][0] / 2.0f) * 0.76f), 240.0f,
                        800.0f - (this.padSizeScreenLan[1][2] / 2.0f), this.padSizeScreenLan[1][3] / 2.0f,
                        (400.0f - this.padSizeScreenLan[1][4]) - 30.0f, this.padSizeScreenLan[1][5] / 2.0f, 430.0f,
                        this.padSizeScreenLan[1][7] / 2.0f, this.padSizeScreenLan[1][8] / 2.0f,
                        480.0f - (this.padSizeScreenLan[1][9] / 2.0f),
                        (this.padSizeScreenLan[1][10] / 2.0f) + this.padSizeScreenLan[1][8],
                        480.0f - (this.padSizeScreenLan[1][11] / 2.0f), 800.0f - (this.padSizeScreenLan[1][12] / 2.0f),
                        480.0f - (this.padSizeScreenLan[1][13] / 2.0f),
                        (800.0f - (this.padSizeScreenLan[1][14] / 2.0f)) - this.padSizeScreenLan[1][12],
                        480.0f - (this.padSizeScreenLan[1][15] / 2.0f), 440.0f + this.padSizeScreenLan[1][6],
                        this.padSizeScreenLan[1][17] / 2.0f,
                        (this.padSizeScreenLan[1][18] / 2.0f)
                                + this.padSizeScreenLan[1][10] + this.padSizeScreenLan[1][8],
                        480.0f - (this.padSizeScreenLan[1][19] / 2.0f),
                        ((800.0f - (this.padSizeScreenLan[1][20] / 2.0f)) - this.padSizeScreenLan[1][12])
                                - this.padSizeScreenLan[1][14],
                        480.0f - (this.padSizeScreenLan[1][21] / 2.0f), this.padSizeScreenLan[1][22] / 2.0f,
                        this.padSizeScreenLan[1][23] / 2.0f, 400.0f + ((this.padSizeScreenLan[1][24] / 2.0f) * 0.76f),
                        240.0f, 0.0f, 0.0f, this.padSizeScreenLan[1][28] / 2.0f,
                        (480.0f - (this.padSizeScreenLan[1][29] / 2.0f)) - (this.padSizeScreenLan[1][10] * 2.0f),
                        (this.padSizeScreenLan[1][30] / 2.0f) + this.padSizeScreenLan[1][28],
                        (480.0f - (this.padSizeScreenLan[1][31] / 2.0f)) - (this.padSizeScreenLan[1][10] * 2.0f),
                        (this.padSizeScreenLan[1][32] / 2.0f) + this.padSizeScreenLan[1][28]
                                + this.padSizeScreenLan[1][30],
                        (480.0f - (this.padSizeScreenLan[1][33] / 2.0f)) - (this.padSizeScreenLan[1][10] * 2.0f),
                        800.0f - (this.padSizeScreenLan[1][34] / 2.0f),
                        (480.0f - (this.padSizeScreenLan[1][35] / 2.0f)) - (this.padSizeScreenLan[1][10] * 2.0f),
                        (800.0f - (this.padSizeScreenLan[1][36] / 2.0f)) - this.padSizeScreenLan[1][34],
                        (480.0f - (this.padSizeScreenLan[1][37] / 2.0f)) - (this.padSizeScreenLan[1][10] * 2.0f),
                        ((800.0f - (this.padSizeScreenLan[1][38] / 2.0f)) - this.padSizeScreenLan[1][34])
                                - this.padSizeScreenLan[1][36],
                        (480.0f - (this.padSizeScreenLan[1][39] / 2.0f)) - (this.padSizeScreenLan[1][10] * 2.0f) } };
        this.padScreenStatus = new int[][] { new int[] { 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 0, 0, 0, 0, 0, 0, 0 },
                new int[] { 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0 } };
        this.padScreenFunc = new int[] { 0, 0, 0, 0, 0, 0 };
        this.padScreenExtra = new int[] { 19, 19, 19, 19, 19, 19 };
        this.padScreenResize = new float[][] {
                new float[] { 0.7f, 0.7f, 0.7f, 0.7f, 0.7f, 0.7f, 0.7f, 0.7f, 0.7f, 0.7f, 0.7f, 0.7f, 0.7f, 0.7f, 0.7f,
                        0.7f, 0.7f, 0.7f, 0.7f, 0.7f },
                new float[] { 0.532f, 0.7f, 0.7f, 0.7f, 0.7f, 0.7f, 0.7f, 0.7f, 0.7f, 0.7f, 0.7f, 0.7f, 0.532f, 0.7f,
                        0.7f, 0.7f, 0.7f, 0.7f, 0.7f, 0.7f } };
        this.padScreenExtraCombo = 0;
        this.dpadsection = new int[] { 15, 15, 19, 19, 14, 14, 18, 18, 13, 17, 17, 12, 12, 16, 16, 15, 15 };
        this.apadsection = new int[] { 7, 7, 7, 6, 6, 6, 6, 5, 5, 5, 4, 4, 4, 4, 7, 7, 7 };
        this.padSizeScreenPor = new float[] { 480.0f, 400.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
                0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
                64.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f };
        this.padOffScreenPor = new float[] { 240.0f, 200.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
                0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
                288.0f + (this.padSizeScreenPor[28] / 2.0f), 332.0f - (this.padSizeScreenPor[29] / 2.0f),
                352.0f + (this.padSizeScreenPor[30] / 2.0f), 332.0f - (this.padSizeScreenPor[31] / 2.0f),
                416.0f + (this.padSizeScreenPor[32] / 2.0f), 332.0f - (this.padSizeScreenPor[33] / 2.0f),
                8.0f + (this.padSizeScreenPor[34] / 2.0f), 62.0f - (this.padSizeScreenPor[35] / 2.0f),
                72.0f + (this.padSizeScreenPor[36] / 2.0f), 62.0f - (this.padSizeScreenPor[37] / 2.0f),
                136.0f + (this.padSizeScreenPor[38] / 2.0f), 62.0f - (this.padSizeScreenPor[39] / 2.0f) };
        this.padSizeScreenLan2H = new float[] { 228.0f, 114.0f, 224.0f, 124.0f, 66.0f, 25.0f, 66.0f, 31.0f, 64.0f,
                30.0f, 64.0f, 30.0f, 64.0f, 30.0f, 64.0f, 30.0f };
        this.padOffScreenLan2H = new float[] { this.padSizeScreenLan2H[0] / 2.0f, this.padSizeScreenLan2H[1] / 2.0f,
                800.0f - (this.padSizeScreenLan2H[2] / 2.0f), this.padSizeScreenLan2H[3] / 2.0f,
                (400.0f - this.padSizeScreenLan2H[4]) - 30.0f, this.padSizeScreenLan2H[5] / 2.0f, 430.0f,
                this.padSizeScreenLan2H[7] / 2.0f, this.padSizeScreenLan2H[8] / 2.0f,
                240.0f - (this.padSizeScreenLan2H[9] / 2.0f),
                (this.padSizeScreenLan2H[10] / 2.0f) + this.padSizeScreenLan2H[8],
                240.0f - (this.padSizeScreenLan2H[11] / 2.0f), 800.0f - (this.padSizeScreenLan2H[12] / 2.0f),
                240.0f - (this.padSizeScreenLan2H[13] / 2.0f),
                (800.0f - (this.padSizeScreenLan2H[14] / 2.0f)) - this.padSizeScreenLan2H[12],
                240.0f - (this.padSizeScreenLan2H[15] / 2.0f) };
        this.padSizeScreenLan2V = new float[] { 136.0f, 190.0f, 134.0f, 206.0f, 39.0f, 33.0f, 39.0f, 51.0f, 38.0f,
                50.0f, 38.0f, 50.0f, 38.0f, 50.0f, 38.0f, 50.0f };
        this.padOffScreenLan2V = new float[] { this.padSizeScreenLan2V[0] / 2.0f, this.padSizeScreenLan2V[1] / 2.0f,
                480.0f - (this.padSizeScreenLan2V[2] / 2.0f), this.padSizeScreenLan2V[3] / 2.0f,
                (240.0f - this.padSizeScreenLan2V[4]) - 30.0f, this.padSizeScreenLan2V[5] / 2.0f, 270.0f,
                this.padSizeScreenLan2V[7] / 2.0f, this.padSizeScreenLan2V[8] / 2.0f,
                400.0f - (this.padSizeScreenLan2V[9] / 2.0f),
                (this.padSizeScreenLan2V[10] / 2.0f) + this.padSizeScreenLan2V[8],
                400.0f - (this.padSizeScreenLan2V[11] / 2.0f), 480.0f - (this.padSizeScreenLan2V[12] / 2.0f),
                400.0f - (this.padSizeScreenLan2V[13] / 2.0f),
                (480.0f - (this.padSizeScreenLan2V[14] / 2.0f)) - this.padSizeScreenLan2V[12],
                400.0f - (this.padSizeScreenLan2V[15] / 2.0f) };
        this.virtualPad = new int[][] { new int[] { 5, 0, 0, 55, 50, 1 }, new int[] { 7, 0, 0, 55, 50, 2 },
                new int[] { 4, 0, 0, 55, 50, 4 }, new int[] { 6, 0, 0, 55, 50, 8 },
                new int[] { 1, 72, 0, InputList.KEYCODE_NUMPAD_8, 80, 16 },
                new int[] { 1, InputList.KEYCODE_F10, 83, 220, InputList.KEYCODE_NUMPAD_RIGHT_PAREN, 32 },
                new int[] { 1, 72, InputList.KEYCODE_VOLUME_MUTE, InputList.KEYCODE_NUMPAD_8, 244, 64 },
                new int[] { 1, 0, 83, 90, InputList.KEYCODE_NUMPAD_RIGHT_PAREN, 128 },
                new int[] { 2, 0, 0, 57, 50, 256 }, new int[] { 9, 0, 0, 55, 50, 512 },
                new int[] { 10, 0, 0, 55, 50, 1024 }, new int[] { 3, 0, 0, 59, 50, 2048 },
                new int[] { 0, 74, 0, InputList.KEYCODE_NUMPAD_4, 74, 4096 },
                new int[] { 0, InputList.KEYCODE_NUMPAD_4, 74, 222, InputList.KEYCODE_NUMPAD_4, 8192 },
                new int[] { 0, 74, InputList.KEYCODE_NUMPAD_4, InputList.KEYCODE_NUMPAD_4, 222, 16384 },
                new int[] { 0, 0, 74, 74, InputList.KEYCODE_NUMPAD_4, 32768 }, new int[] { 0, 0, 0, 74, 74, 36864 },
                new int[] { 0, InputList.KEYCODE_NUMPAD_4, 0, 222, 74, 12288 },
                new int[] { 0, InputList.KEYCODE_NUMPAD_4, InputList.KEYCODE_NUMPAD_4, 222, 222, 24576 },
                new int[] { 0, 0, InputList.KEYCODE_NUMPAD_4, 74, 222, 49152 }, new int[] { 11, 0, 0, 222, 222, 0 },
                new int[] { 12, 0, 0, 222, 222, 0 }, new int[] { 8, 0, 0, 53, 41, 0 },
                new int[] { 16, 0, 0, 63, 63, 0 }, new int[] { 17, 0, 0, 63, 63, 0 }, new int[] { 18, 0, 0, 63, 63, 0 },
                new int[] { 19, 0, 0, 63, 63, 0 }, new int[] { 20, 0, 0, 63, 63, 0 },
                new int[] { 21, 0, 0, 63, 63, 0 } };
        this.virtualPadPort = new int[][] { new int[] { 0, 0, 10, 68, 58, 4 },
                new int[] { 0, HttpStatus.SC_REQUEST_URI_TOO_LONG, 10, 482, 58, 8 },
                new int[] { 0, 70, 10, InputList.KEYCODE_F8, 58, 1 },
                new int[] { 0, 344, 10, HttpStatus.SC_PRECONDITION_FAILED, 58, 2 },
                new int[] { 0, 324, InputList.KEYCODE_NUMPAD_ENTER, HttpStatus.SC_PAYMENT_REQUIRED, 242, 16 },
                new int[] { 0, HttpStatus.SC_NOT_FOUND, 224, 482, 306, 32 },
                new int[] { 0, 324, HttpStatus.SC_MOVED_TEMPORARILY, HttpStatus.SC_PAYMENT_REQUIRED,
                        HttpStatus.SC_NOT_ACCEPTABLE, 64 },
                new int[] { 0, 248, 224, 326, 306, 128 }, new int[] { 0, InputList.KEYCODE_ZOOM_IN, 10, 230, 62, 256 },
                new int[] { -1, 0, 0, 0, 0, 512 }, new int[] { -1, 0, 0, 0, 0, 1024 },
                new int[] { 0, 244, 10, 316, 62, 2048 },
                new int[] { 0, 74, 106, InputList.KEYCODE_NUMPAD_4, 180, 4096 },
                new int[] { 0, InputList.KEYCODE_NUMPAD_4, 180, 222, 254, 8192 },
                new int[] { 0, 74, 254, InputList.KEYCODE_NUMPAD_4, 328, 16384 },
                new int[] { 0, 0, 180, 74, 254, 32768 }, new int[] { 0, 0, 106, 74, 180, 36864 },
                new int[] { 0, InputList.KEYCODE_NUMPAD_4, 106, 222, 180, 12288 },
                new int[] { 0, InputList.KEYCODE_NUMPAD_4, 254, 222, 328, 24576 },
                new int[] { 0, 0, 254, 74, 328, 49152 }, new int[] { -1, 0, 0, 222, 222, 0 },
                new int[] { -1, 0, 0, 222, 222, 0 }, new int[] { -1, 0, 0, 53, 41, 0 },
                new int[] { 16, 288, 68, 351, InputList.KEYCODE_F1, 0 },
                new int[] { 17, 352, 68, HttpStatus.SC_UNSUPPORTED_MEDIA_TYPE, InputList.KEYCODE_F1, 0 },
                new int[] { 18, HttpStatus.SC_REQUESTED_RANGE_NOT_SATISFIABLE, 68, 479, InputList.KEYCODE_F1, 0 },
                new int[] { 19, 8, 338, 71, HttpStatus.SC_UNAUTHORIZED, 0 },
                new int[] { 20, 72, 338, InputList.KEYCODE_F5, HttpStatus.SC_UNAUTHORIZED, 0 },
                new int[] { 21, 136, 338, InputList.KEYCODE_BUTTON_12, HttpStatus.SC_UNAUTHORIZED, 0 } };
        this.virtualPadPos = (int[][]) Array.newInstance((Class<?>) Integer.TYPE, 60, 6);
        this.virtualPadBit = new int[60];
        this.virtualPadId = new int[60];
        this.activeTouchIds = new int[MAX_TOUCHES];
        this.touchButtonIds = new int[MAX_TOUCHES];
        this.initvirtualPad = 0;
        this.mContext = context;
        this.myActivity = act;
        this.onPauseMode = 0;
        init();
    }

    private void init() {
        for (int i = 0; i < MAX_TOUCHES; i++) {
            this.activeTouchIds[i] = -1;
            this.touchButtonIds[i] = -1;
        }
        setFocusable(true);
        setFocusableInTouchMode(true);
        requestFocus();
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setquitonexit() {
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setanalogdebug(int lx1, int ly1, int lx2, int ly2) {
        this.f166x1 = lx1;
        this.f168y1 = ly1;
        this.f167x2 = lx2;
        this.f169y2 = ly2;
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setdebugstring(String s) {
        this.debugString = s;
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setdebugstring2(String s) {
        this.debugString2 = s;
    }

    /*
     * JADX WARN: Type inference failed for: r0v10, types:
     * [com.epsxe.ePSXe.ePSXeViewGL$2]
     */
    /*
     * JADX WARN: Type inference failed for: r0v11, types:
     * [com.epsxe.ePSXe.ePSXeViewGL$1]
     */
    @Override // com.epsxe.ePSXe.ePSXeView
    public void setePSXeLib(libepsxe epsxelib, int glVersion, int net) {
        this.osVersion = Integer.parseInt(Build.VERSION.SDK);
        Log.e("ePSXeViewGL", "set gpu renderer == " + glVersion);
        this.f165e = epsxelib;
        this.f165e.gpusetopenglmode(glVersion);
        this.f165e.setscreendepth(this.emu_gpu_blit_mode);
        this.f165e.setGpuSoftMtMode(this.emu_gpu_soft_mt_mode);
        this.serverMode = net;
        if (this.emu_gpu_soft_mt_mode > 0) {
            new Thread() { // from class: com.epsxe.ePSXe.ePSXeViewGL.1
                @Override // java.lang.Thread, java.lang.Runnable
                public void run() {
                    ePSXeViewGL.this.f165e.runwrapper(1);
                }
            }.start();
        }
        if (net != 2) {
            new Thread() { // from class: com.epsxe.ePSXe.ePSXeViewGL.2
                @Override // java.lang.Thread, java.lang.Runnable
                public void run() {
                    ePSXeViewGL.this.f165e.openglstartthread(ePSXeViewGL.this.emu_gpu_soft_mt_mode);
                }
            }.start();
        }
        if (glVersion != 1) {
            setEGLContextClientVersion(2);
            setRenderer(new ePSXeRenderer2());
        } else {
            setRenderer(new ePSXeRenderer());
        }
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setfps(int fps) {
        this.mfps = fps;
        Log.e("ePSXeView", "fps = " + fps);
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
        this.emu_enable_frameskip = this.f165e.setFrameSkip(this.emu_enable_frameskip);
        this.emu_enable_frameskip_tmp = this.emu_enable_frameskip;
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setscreenorientation(int ori) {
        Log.e("epsxeView", "Orientation = " + ori);
        this.emu_screen_orientation = this.f165e.setscreenorientation(ori);
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
        this.emu_screen_vrmode = vrmode;
        this.emu_screen_vrdistorsion = vrdistorsion;
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setscreenratio(int rat) {
        Log.e("epsxeView", "Ratio = " + rat);
        this.emu_screen_ratio = rat;
        this.f165e.openglresize(this.mWidth, this.mHeight, this.emu_player_mode, this.emu_split_mode,
                this.emu_screen_ratio, this.emu_screen_orientation, this.emu_screen_vrmode,
                this.emu_screen_vrdistorsion);
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void updatescreenratio(int rat) {
        setscreenratio(rat);
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setscreendepth(int dep) {
        this.emu_gpu_blit_mode = dep;
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setscreenblackbands(int bb) {
        Log.e("epsxeView", "blackbands = " + bb);
        this.f165e.setblackbands(bb);
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setvideofilter(int fil) {
        Log.e("epsxeView", "Filter = " + fil);
        this.emu_video_filter = fil;
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setgpumtmodeH(int modeH) {
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setgpumtmodeS(int modeS) {
        this.emu_gpu_soft_mt_mode = modeS;
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setvideodither(int fil) {
        Log.e("epsxeView", "PSX Dither = " + fil);
        this.f165e.setDithering(fil);
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setvideofilterhw(int fil) {
        Log.e("epsxeView", "PSX Filter hw = " + fil);
        this.f165e.setFilterhw(fil);
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setplayermode(int pmode) {
        this.emu_player_mode = pmode;
        Log.e("epsxeView", "PlayerMode = " + pmode);
        this.emu_player_mode = this.f165e.setPlayerMode(this.emu_player_mode);
        if (this.emu_player_mode == 10) {
            if (this.emu_screen_orientation != 0) {
                this.emu_split_mode = 0;
            } else {
                this.emu_split_mode = 1;
            }
        }
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setsplitmode(int spmode) {
        this.emu_split_mode = spmode;
        Log.e("epsxeView", "SplitMode = " + spmode);
        this.emu_split_mode = this.f165e.setSplitMode(spmode);
        this.initvirtualPad = 0;
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setsoundlatency(int lmode) {
        Log.e("epsxeView", "SoundLatency = " + lmode);
        this.emu_sound_latency = this.f165e.setSoundLatency(lmode);
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void toggleframelimit() {
        if (this.emu_enable_framelimit == 1) {
            this.emu_enable_frameskip_tmp = this.emu_enable_frameskip;
            this.emu_enable_frameskip = 0;
            this.emu_enable_frameskip = this.f165e.setFrameSkip(this.emu_enable_frameskip);
            this.emu_enable_framelimit ^= 1;
            this.emu_enable_framelimit = this.f165e.setFrameLimit(this.emu_enable_framelimit);
            return;
        }
        this.emu_enable_frameskip = this.emu_enable_frameskip_tmp;
        this.emu_enable_frameskip = this.f165e.setFrameSkip(this.emu_enable_frameskip);
        this.emu_enable_framelimit ^= 1;
        this.emu_enable_framelimit = this.f165e.setFrameLimit(this.emu_enable_framelimit);
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setframelimit() {
        this.emu_enable_frameskip = this.emu_enable_frameskip_tmp;
        this.emu_enable_frameskip = this.f165e.setFrameSkip(this.emu_enable_frameskip);
        this.emu_enable_framelimit = 1;
        this.emu_enable_framelimit = this.f165e.setFrameLimit(this.emu_enable_framelimit);
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void unsetframelimit() {
        this.emu_enable_frameskip_tmp = this.emu_enable_frameskip;
        this.emu_enable_frameskip = 0;
        this.emu_enable_frameskip = this.f165e.setFrameSkip(this.emu_enable_frameskip);
        this.emu_enable_framelimit = 0;
        this.emu_enable_framelimit = this.f165e.setFrameLimit(this.emu_enable_framelimit);
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setshowfps(int fps) {
        this.emu_enable_printfps = fps;
        Log.e("epsxeView", "CpuShowFPS " + fps);
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setinputpaintpadmode(int mode1, int mode2) {
        this.emu_pad_draw_mode[0] = mode1;
        this.emu_pad_draw_mode[1] = mode2;
        Log.e("epsxeView", "PadPaintMode " + mode1);
        Log.e("epsxeView", "PadPaintMode2 " + mode2);
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
            this.f165e.setpadmode(0, mode1);
            this.f165e.setpadmode(1, mode2);
            if (mode1 == 4) {
                this.emu_pad_mode_analog[0] = analog1;
                this.mode = analog1;
                this.f165e.setpadanalogmode(0, this.emu_pad_mode_analog[0]);
            } else if (mode1 == 1) {
                this.emu_pad_mode_analog[0] = 0;
                this.mode = 0;
                this.f165e.setpadanalogmode(0, this.emu_pad_mode_analog[0]);
            }
            if (mode2 == 4) {
                this.emu_pad_mode_analog[1] = analog2;
                this.f165e.setpadanalogmode(1, this.emu_pad_mode_analog[1]);
            } else if (mode2 == 1) {
                this.emu_pad_mode_analog[1] = 0;
                this.f165e.setpadanalogmode(1, this.emu_pad_mode_analog[1]);
            }
            if (mode1 == 2) {
                this.emu_pad_mode_analog[0] = 1;
                this.f165e.setpadanalogmode(0, this.emu_pad_mode_analog[0]);
                this.emu_mouse = true;
            }
            if (mode2 == 2) {
                this.emu_pad_mode_analog[1] = 1;
                this.f165e.setpadanalogmode(1, this.emu_pad_mode_analog[1]);
                this.emu_mouse = true;
            }
            if (this.serverMode == 4) {
                mode2 = mode1;
                mode1 = mode2;
                this.emu_pad_mode[0] = mode1;
                this.emu_pad_mode[1] = mode2;
                if (mode1 == 4) {
                    this.emu_pad_mode_analog[0] = analog1;
                    this.mode = analog1;
                } else if (mode1 == 1) {
                    this.emu_pad_mode_analog[0] = 0;
                    this.mode = 0;
                }
                if (mode2 == 4) {
                    this.emu_pad_mode_analog[1] = analog2;
                } else if (mode2 == 1) {
                    this.emu_pad_mode_analog[1] = 0;
                }
            }
            if (mode1 == 4 || mode1 == 1) {
                init_motionevent_1playerLandscape();
            } else if (mode1 == 3 || mode1 == 8) {
                this.emu_pad_mode_analog[0] = 1;
                this.mode = 1;
                this.f165e.setpadanalogmode(0, this.emu_pad_mode_analog[0]);
                this.gun.initGun(this.mWidth, this.mHeight, this.emu_pad_type_selected);
            }
            Log.e("epsxeView", "PadMode " + mode1 + " Analog " + analog1);
            Log.e("epsxeView", "PadMode2 " + mode2 + " Analog " + analog2);
        }
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setinputpadmodeanalog(int p) {
        if (this.emu_player_mode == 1) {
            if (this.emu_pad_mode[p] == 4) {
                int[] iArr = this.emu_pad_mode_analog;
                iArr[p] = iArr[p] ^ 1;
                this.f165e.setpadanalogmode(p, this.emu_pad_mode_analog[p]);
            }
            if (p == 0) {
                this.mode = this.emu_pad_mode_analog[p];
            }
        }
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setsnaprestoring(boolean snaprestoring) {
        Log.e("epsxeView", "loadtmp_snap ");
        this.f165e.loadtmpsnap();
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setautosnaprestoring() {
        Log.e("epsxeView", "loadauto_snap ");
        this.f165e.loadautosnap();
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void settainted(int mode) {
        this.tainted = mode;
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public boolean setIsoName(String name, int slot, String gpu) {
        return true;
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setSaveslot(int slot) {
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void toggletools() {
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
        String oldprofile = this.padprofile;
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
            if (!oldprofile.equals(this.padprofile)) {
                this.redoPads = true;
            }
        }
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setdynamicpad(int dyn) {
        if ((this.emu_pad_mode[0] == 1 || this.emu_pad_mode[0] == 4) && this.emu_screen_orientation != 1
                && this.emu_portrait_skin == 0) {
            this.emu_pad_dynamic = dyn;
        }
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setdynamicaction(int dyn) {
        if ((this.emu_pad_mode[0] == 1 || this.emu_pad_mode[0] == 4) && this.emu_screen_orientation != 1
                && this.emu_portrait_skin == 0) {
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
            if (this.emu_pad_mode[this.emu_pad_type_selected] == 1
                    || this.emu_pad_mode[this.emu_pad_type_selected] == 4) {
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
                int offx = ((int) this.padOffScreenLan[this.mode][this.virtualPad[n][0] * 2])
                        - (((int) this.padSizeScreenLan[this.mode][this.virtualPad[n][0] * 2]) / 2);
                int offy = ((int) this.padOffScreenLan[this.mode][(this.virtualPad[n][0] * 2) + 1])
                        - (((int) this.padSizeScreenLan[this.mode][(this.virtualPad[n][0] * 2) + 1]) / 2);
                this.virtualPadPos[n][0] = ((int) (this.virtualPad[n][1] * this.padResize)) + offx;
                this.virtualPadPos[n][1] = (((((int) (this.virtualPad[n][2] * this.padResize))
                        + (this.mHeight - ((int) this.padSizeScreenLan[this.mode][(this.virtualPad[n][0] * 2) + 1])))
                        - offy) / 2) + (this.mHeight / 2);
                this.virtualPadPos[n][2] = ((int) (this.virtualPad[n][3] * this.padResize)) + offx;
                this.virtualPadPos[n][3] = (((((int) (this.virtualPad[n][4] * this.padResize))
                        + (this.mHeight - ((int) this.padSizeScreenLan[this.mode][(this.virtualPad[n][0] * 2) + 1])))
                        - offy) / 2) + (this.mHeight / 2);
                this.virtualPadPos[n][4] = ((this.virtualPadPos[n][2] - this.virtualPadPos[n][0]) / 2)
                        + this.virtualPadPos[n][0];
                this.virtualPadPos[n][5] = ((this.virtualPadPos[n][3] - this.virtualPadPos[n][1]) / 2)
                        + this.virtualPadPos[n][1];
                this.virtualPadBit[n] = this.virtualPad[n][5] | 65536;
                this.virtualPadId[n] = -1;

                // Смещение для левого стика первого игрока в 2-player режиме
                if (this.virtualPad[n][0] == 11 && this.virtualPadPos[n][0] < 100) {
                    this.virtualPadPos[n][0] += 90;
                    this.virtualPadPos[n][1] -= 100;
                    this.virtualPadPos[n][2] += 90;
                    this.virtualPadPos[n][3] -= 100;
                    this.virtualPadPos[n][4] += 90;
                    this.virtualPadPos[n][5] -= 100;

                    Log.d("TOUCH_INIT", String.format(
                            "2PLAYER LEFT STICK SHIFTED n=%d pos=[%d,%d]",
                            n, this.virtualPadPos[n][0], this.virtualPadPos[n][1]));
                }
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
                int offx2 = ((int) this.padOffScreenLan[this.mode][this.virtualPad[n2][0] * 2])
                        - (((int) this.padSizeScreenLan[this.mode][this.virtualPad[n2][0] * 2]) / 2);
                int offy2 = ((int) this.padOffScreenLan[this.mode][(this.virtualPad[n2][0] * 2) + 1])
                        - (((int) this.padSizeScreenLan[this.mode][(this.virtualPad[n2][0] * 2) + 1]) / 2);
                this.virtualPadPos[n2 + 20][0] = this.mWidth
                        - (((int) (this.virtualPad[n2][3] * this.padResize)) + offx2);
                this.virtualPadPos[n2 + 20][1] = (this.mHeight / 2)
                        - (((((int) (this.virtualPad[n2][4] * this.padResize)) + (this.mHeight
                                - ((int) this.padSizeScreenLan[this.mode][(this.virtualPad[n2][0] * 2) + 1]))) - offy2)
                                / 2);
                this.virtualPadPos[n2 + 20][2] = this.mWidth
                        - (((int) (this.virtualPad[n2][1] * this.padResize)) + offx2);
                this.virtualPadPos[n2 + 20][3] = (this.mHeight / 2)
                        - (((((int) (this.virtualPad[n2][2] * this.padResize)) + (this.mHeight
                                - ((int) this.padSizeScreenLan[this.mode][(this.virtualPad[n2][0] * 2) + 1]))) - offy2)
                                / 2);
                this.virtualPadPos[n2 + 20][4] = ((this.virtualPadPos[n2 + 20][2] - this.virtualPadPos[n2 + 20][0]) / 2)
                        + this.virtualPadPos[n2 + 20][0];
                this.virtualPadPos[n2 + 20][5] = ((this.virtualPadPos[n2 + 20][3] - this.virtualPadPos[n2 + 20][1]) / 2)
                        + this.virtualPadPos[n2 + 20][1];
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
            }
            int offx = ((int) this.padOffScreenLan[this.mode][this.virtualPad[n][0] * 2])
                    - (((int) this.padSizeScreenLan[this.mode][this.virtualPad[n][0] * 2]) / 2);
            int offy = ((int) this.padOffScreenLan[this.mode][(this.virtualPad[n][0] * 2) + 1])
                    - (((int) this.padSizeScreenLan[this.mode][(this.virtualPad[n][0] * 2) + 1]) / 2);
            int tmp1 = ((int) (this.virtualPad[n][1] * this.padResize)) + offx;
            int tmp2 = (((int) (this.virtualPad[n][2] * this.padResize))
                    + (this.mHeight - ((int) this.padSizeScreenLan[this.mode][(this.virtualPad[n][0] * 2) + 1])))
                    - offy;
            int tmp3 = ((int) (this.virtualPad[n][3] * this.padResize)) + offx;
            int tmp4 = (((int) (this.virtualPad[n][4] * this.padResize))
                    + (this.mHeight - ((int) this.padSizeScreenLan[this.mode][(this.virtualPad[n][0] * 2) + 1])))
                    - offy;
            int tmp12 = (this.mHeight * tmp1) / this.mWidth;
            int tmp22 = ((this.mWidth * tmp2) / 2) / this.mHeight;
            int tmp32 = (this.mHeight * tmp3) / this.mWidth;
            int tmp42 = ((this.mWidth * tmp4) / 2) / this.mHeight;
            this.virtualPadPos[n][0] = (this.mWidth / 2) + tmp22;
            this.virtualPadPos[n][1] = this.mHeight - tmp32;
            this.virtualPadPos[n][2] = (this.mWidth / 2) + tmp42;
            this.virtualPadPos[n][3] = this.mHeight - tmp12;
            this.virtualPadPos[n][4] = ((this.virtualPadPos[n][2] - this.virtualPadPos[n][0]) / 2)
                    + this.virtualPadPos[n][0];
            this.virtualPadPos[n][5] = ((this.virtualPadPos[n][3] - this.virtualPadPos[n][1]) / 2)
                    + this.virtualPadPos[n][1];
            this.virtualPadBit[n] = this.virtualPad[n][5] | 65536;
            this.virtualPadId[n] = -1;
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
                int offx2 = ((int) this.padOffScreenLan[this.mode][this.virtualPad[n2][0] * 2])
                        - (((int) this.padSizeScreenLan[this.mode][this.virtualPad[n2][0] * 2]) / 2);
                int offy2 = ((int) this.padOffScreenLan[this.mode][(this.virtualPad[n2][0] * 2) + 1])
                        - (((int) this.padSizeScreenLan[this.mode][(this.virtualPad[n2][0] * 2) + 1]) / 2);
                int tmp13 = ((int) (this.virtualPad[n2][1] * this.padResize)) + offx2;
                int tmp23 = (((int) (this.virtualPad[n2][2] * this.padResize))
                        + (this.mHeight - ((int) this.padSizeScreenLan[this.mode][(this.virtualPad[n2][0] * 2) + 1])))
                        - offy2;
                int tmp33 = ((int) (this.virtualPad[n2][3] * this.padResize)) + offx2;
                int tmp43 = (((int) (this.virtualPad[n2][4] * this.padResize))
                        + (this.mHeight - ((int) this.padSizeScreenLan[this.mode][(this.virtualPad[n2][0] * 2) + 1])))
                        - offy2;
                int tmp14 = (this.mHeight * tmp13) / this.mWidth;
                int tmp24 = ((this.mWidth * tmp23) / 2) / this.mHeight;
                int tmp34 = (this.mHeight * tmp33) / this.mWidth;
                this.virtualPadPos[n2 + 20][0] = (this.mWidth / 2) - (((this.mWidth * tmp43) / 2) / this.mHeight);
                this.virtualPadPos[n2 + 20][1] = tmp14;
                this.virtualPadPos[n2 + 20][2] = (this.mWidth / 2) - tmp24;
                this.virtualPadPos[n2 + 20][3] = tmp34;
                this.virtualPadPos[n2 + 20][4] = ((this.virtualPadPos[n2 + 20][2] - this.virtualPadPos[n2 + 20][0]) / 2)
                        + this.virtualPadPos[n2 + 20][0];
                this.virtualPadPos[n2 + 20][5] = ((this.virtualPadPos[n2 + 20][3] - this.virtualPadPos[n2 + 20][1]) / 2)
                        + this.virtualPadPos[n2 + 20][1];
                this.virtualPadBit[n2 + 20] = this.virtualPad[n2][5];
                this.virtualPadId[n2 + 20] = -1;
            }
        }
        this.initvirtualPad = 1;
    }

    public void init_motionevent_1playerPortrait() {
        for (int n = 0; n < 29; n++) {
            if (n > 22) {
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
                this.virtualPadPos[n][4] = ((this.virtualPadPos[n][2] - this.virtualPadPos[n][0]) / 2)
                        + this.virtualPadPos[n][0];
                this.virtualPadPos[n][5] = ((this.virtualPadPos[n][3] - this.virtualPadPos[n][1]) / 2)
                        + this.virtualPadPos[n][1];
                this.virtualPadBit[n] = this.virtualPadPort[n][5];
                if (this.emu_pad_type_selected == 0) {
                    int[] iArr = this.virtualPadBit;
                    iArr[n] = iArr[n] | 65536;
                }
                this.virtualPadId[n] = -1;
                if (n == 20) {
                    // Применяем смещение для портретного режима (проверяем по virtualPadPort)
                    if (this.virtualPadPort[n][0] == 11 && this.virtualPadPos[n][0] < 100) {
                        this.virtualPadPos[n][0] += 90; // смещение вправо
                        this.virtualPadPos[n][1] -= 100; // смещение вверх
                        this.virtualPadPos[n][2] += 90; // смещение вправо
                        this.virtualPadPos[n][3] -= 100; // смещение вверх
                        this.virtualPadPos[n][4] += 90; // центр X
                        this.virtualPadPos[n][5] -= 100; // центр Y

                        Log.d("TOUCH_INIT", String.format(
                                "PORTRAIT LEFT STICK SHIFTED n=%d pos=[%d,%d]",
                                n, this.virtualPadPos[n][0], this.virtualPadPos[n][1]));
                    }

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
            if (n > 22) {
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
                this.virtualPadBit[n] = this.virtualPad[n][5];
                this.virtualPadId[n] = -1;
                this.virtualPadPos[n][0] = -1;
                this.virtualPadPos[n][1] = -1;
                this.virtualPadPos[n][2] = -1;
                this.virtualPadPos[n][3] = -1;
                this.virtualPadPos[n][4] = -1;
                this.virtualPadPos[n][5] = -1;
                if (this.emu_pad_type_selected == 0) {
                    int[] iArr = this.virtualPadBit;
                    iArr[n] = iArr[n] | 65536;
                }
            } else {
                // Исправленное вычисление позиций для ландшафтного режима
                int buttonIndex = this.virtualPad[n][0];

                // Получаем размеры и позицию кнопки
                float buttonWidth = this.padSizeScreenLan[this.mode][buttonIndex * 2]
                        * this.padScreenResize[this.mode][buttonIndex];
                float buttonHeight = this.padSizeScreenLan[this.mode][(buttonIndex * 2) + 1]
                        * this.padScreenResize[this.mode][buttonIndex];

                // Центр кнопки на экране (OpenGL координаты)
                float centerX = this.padOffScreenLan[this.mode][buttonIndex * 2];
                float centerY = this.padOffScreenLan[this.mode][(buttonIndex * 2) + 1];

                // Для D-pad (индекс 0) и Action buttons (индекс 1) используем специальную
                // логику
                if (buttonIndex == 0 || buttonIndex == 1) {
                    // Для D-pad и Action buttons используем исходную логику с коррекцией для
                    // правильного центрирования
                    int offx = ((int) centerX) - ((int) (buttonWidth / 2.0f));
                    int offy = ((int) centerY) - ((int) (buttonHeight / 2.0f));

                    // Инвертируем Y координату для touch событий (Android координаты начинаются
                    // сверху)
                    float touchCenterY = this.mHeight - centerY;
                    int touchOffy = ((int) touchCenterY) - ((int) (buttonHeight / 2.0f));

                    this.virtualPadPos[n][0] = ((int) (this.virtualPad[n][1] * this.padResize
                            * this.padScreenResize[this.mode][buttonIndex])) + offx;
                    this.virtualPadPos[n][1] = ((int) (this.virtualPad[n][2] * this.padResize
                            * this.padScreenResize[this.mode][buttonIndex])) + touchOffy;
                    this.virtualPadPos[n][2] = ((int) (this.virtualPad[n][3] * this.padResize
                            * this.padScreenResize[this.mode][buttonIndex])) + offx;
                    this.virtualPadPos[n][3] = ((int) (this.virtualPad[n][4] * this.padResize
                            * this.padScreenResize[this.mode][buttonIndex])) + touchOffy;
                    this.virtualPadPos[n][4] = ((this.virtualPadPos[n][2] - this.virtualPadPos[n][0]) / 2)
                            + this.virtualPadPos[n][0];
                    this.virtualPadPos[n][5] = ((this.virtualPadPos[n][3] - this.virtualPadPos[n][1]) / 2)
                            + this.virtualPadPos[n][1];
                } else if (buttonIndex == 1) {
                    // Специальная обработка для Action buttons (треугольник, круг, квадрат, икс)
                    int offx = ((int) centerX) - ((int) (buttonWidth / 2.0f));
                    int offy = ((int) centerY) - ((int) (buttonHeight / 2.0f));

                    // Инвертируем Y координату для touch событий (Android координаты начинаются
                    // сверху)
                    float touchCenterY = this.mHeight - centerY;
                    int touchOffy = ((int) touchCenterY) - ((int) (buttonHeight / 2.0f));

                    this.virtualPadPos[n][0] = ((int) (this.virtualPad[n][1] * this.padResize
                            * this.padScreenResize[this.mode][buttonIndex])) + offx;
                    this.virtualPadPos[n][1] = ((int) (this.virtualPad[n][2] * this.padResize
                            * this.padScreenResize[this.mode][buttonIndex])) + touchOffy;
                    this.virtualPadPos[n][2] = ((int) (this.virtualPad[n][3] * this.padResize
                            * this.padScreenResize[this.mode][buttonIndex])) + offx;
                    this.virtualPadPos[n][3] = ((int) (this.virtualPad[n][4] * this.padResize
                            * this.padScreenResize[this.mode][buttonIndex])) + touchOffy;
                    this.virtualPadPos[n][4] = ((this.virtualPadPos[n][2] - this.virtualPadPos[n][0]) / 2)
                            + this.virtualPadPos[n][0];
                    this.virtualPadPos[n][5] = ((this.virtualPadPos[n][3] - this.virtualPadPos[n][1]) / 2)
                            + this.virtualPadPos[n][1];
                } else {
                    // Для остальных кнопок используем исправленную логику для правильного
                    // совпадения touch и визуальных областей
                    // Инвертируем Y координату для touch событий (Android координаты начинаются
                    // сверху)
                    float touchCenterY = this.mHeight - centerY;

                    // Вычисляем границы touch области с учетом центрирования и инверсии Y
                    this.virtualPadPos[n][0] = (int) (centerX - (buttonWidth / 2.0f));
                    this.virtualPadPos[n][1] = (int) (touchCenterY - (buttonHeight / 2.0f));
                    this.virtualPadPos[n][2] = (int) (centerX + (buttonWidth / 2.0f));
                    this.virtualPadPos[n][3] = (int) (touchCenterY + (buttonHeight / 2.0f));

                    // Центр touch области должен совпадать с центром визуальной кнопки
                    this.virtualPadPos[n][4] = (int) centerX;
                    this.virtualPadPos[n][5] = (int) touchCenterY;
                }

                this.virtualPadBit[n] = this.virtualPad[n][5];
                if (this.emu_pad_type_selected == 0) {
                    int[] iArr2 = this.virtualPadBit;
                    iArr2[n] = iArr2[n] | 65536;
                }
                this.virtualPadId[n] = -1;

                // Специальная обработка для аналоговых стиков
                if (n == 20) {
                    if (buttonIndex == 0 || buttonIndex == 1) {
                        this.analog_values[0][0] = ((this.virtualPadPos[n][2] - this.virtualPadPos[n][0]) / 2)
                                + this.virtualPadPos[n][0];
                        this.analog_values[0][1] = this.mHeight
                                - (((this.virtualPadPos[n][3] - this.virtualPadPos[n][1]) / 2)
                                        + this.virtualPadPos[n][1]);
                    } else {
                        this.analog_values[0][0] = (int) centerX;
                        this.analog_values[0][1] = (int) centerY; // Для отрисовки используем OpenGL координаты
                    }
                }
                if (n == 21) {
                    if (buttonIndex == 0 || buttonIndex == 1) {
                        this.analog_values[0][2] = ((this.virtualPadPos[n][2] - this.virtualPadPos[n][0]) / 2)
                                + this.virtualPadPos[n][0];
                        this.analog_values[0][3] = this.mHeight
                                - (((this.virtualPadPos[n][3] - this.virtualPadPos[n][1]) / 2)
                                        + this.virtualPadPos[n][1]);
                    } else {
                        this.analog_values[0][2] = (int) centerX;
                        this.analog_values[0][3] = (int) centerY; // Для отрисовки используем OpenGL координаты
                    }
                }

                // Специальная обработка для отдельных кнопок D-pad (индексы 12-15)
                if (n >= 12 && n <= 15 && buttonIndex == 0) {
                    // Для отдельных кнопок D-pad используем правильные touch координаты
                    // Получаем центр основного D-pad
                    float dpadCenterX = this.padOffScreenLan[this.mode][0];
                    float dpadCenterY = this.padOffScreenLan[this.mode][1];
                    float dpadWidth = this.padSizeScreenLan[this.mode][0] * this.padScreenResize[this.mode][0];
                    float dpadHeight = this.padSizeScreenLan[this.mode][1] * this.padScreenResize[this.mode][0];

                    // Вычисляем позицию каждой кнопки D-pad относительно центра
                    // Учитываем что в OpenGL Y растет вверх, а в Android touch - вниз
                    float buttonOffsetX = 0, buttonOffsetY = 0;
                    switch (n) {
                        case 12: // Up - в OpenGL координатах это +Y, но для touch нужно -Y
                            buttonOffsetX = 0;
                            buttonOffsetY = dpadHeight * 0.25f; // Инвертируем для touch координат
                            break;
                        case 13: // Right
                            buttonOffsetX = dpadWidth * 0.25f;
                            buttonOffsetY = 0;
                            break;
                        case 14: // Down - в OpenGL координатах это -Y, но для touch нужно +Y
                            buttonOffsetX = 0;
                            buttonOffsetY = -dpadHeight * 0.25f; // Инвертируем для touch координат
                            break;
                        case 15: // Left
                            buttonOffsetX = -dpadWidth * 0.25f;
                            buttonOffsetY = 0;
                            break;
                    }

                    float buttonCenterX = dpadCenterX + buttonOffsetX;
                    float buttonCenterY = dpadCenterY + buttonOffsetY;
                    float touchCenterY = this.mHeight - buttonCenterY;

                    // Размер каждой кнопки D-pad
                    float dpadButtonWidth = dpadWidth * 0.4f;
                    float dpadButtonHeight = dpadHeight * 0.4f;

                    this.virtualPadPos[n][0] = (int) (buttonCenterX - (dpadButtonWidth / 2.0f));
                    this.virtualPadPos[n][1] = (int) (touchCenterY - (dpadButtonHeight / 2.0f));
                    this.virtualPadPos[n][2] = (int) (buttonCenterX + (dpadButtonWidth / 2.0f));
                    this.virtualPadPos[n][3] = (int) (touchCenterY + (dpadButtonHeight / 2.0f));
                    this.virtualPadPos[n][4] = (int) buttonCenterX;
                    this.virtualPadPos[n][5] = (int) touchCenterY;
                }

                // Специальная обработка для отдельных Action кнопок (индексы 4-7: треугольник,
                // круг, икс, квадрат)
                if (n >= 4 && n <= 7 && buttonIndex == 1) {
                    // Получаем центр основного Action блока
                    float actionCenterX = this.padOffScreenLan[this.mode][2];
                    float actionCenterY = this.padOffScreenLan[this.mode][3];
                    float actionWidth = this.padSizeScreenLan[this.mode][2] * this.padScreenResize[this.mode][1];
                    float actionHeight = this.padSizeScreenLan[this.mode][3] * this.padScreenResize[this.mode][1];

                    // Вычисляем позицию каждой Action кнопки относительно центра
                    float buttonOffsetX = 0, buttonOffsetY = 0;
                    switch (n) {
                        case 4: // Triangle (треугольник) - вверху
                            buttonOffsetX = 0;
                            buttonOffsetY = actionHeight * 0.25f; // Инвертируем для touch координат
                            break;
                        case 5: // Circle (круг) - справа
                            buttonOffsetX = actionWidth * 0.25f;
                            buttonOffsetY = 0;
                            break;
                        case 6: // X (икс) - внизу
                            buttonOffsetX = 0;
                            buttonOffsetY = -actionHeight * 0.25f; // Инвертируем для touch координат
                            break;
                        case 7: // Square (квадрат) - слева
                            buttonOffsetX = -actionWidth * 0.25f;
                            buttonOffsetY = 0;
                            break;
                    }

                    float buttonCenterX = actionCenterX + buttonOffsetX;
                    float buttonCenterY = actionCenterY + buttonOffsetY;
                    float touchCenterY = this.mHeight - buttonCenterY;

                    // Размер каждой Action кнопки
                    float actionButtonWidth = actionWidth * 0.4f;
                    float actionButtonHeight = actionHeight * 0.4f;

                    this.virtualPadPos[n][0] = (int) (buttonCenterX - (actionButtonWidth / 2.0f));
                    this.virtualPadPos[n][1] = (int) (touchCenterY - (actionButtonHeight / 2.0f));
                    this.virtualPadPos[n][2] = (int) (buttonCenterX + (actionButtonWidth / 2.0f));
                    this.virtualPadPos[n][3] = (int) (touchCenterY + (actionButtonHeight / 2.0f));
                    this.virtualPadPos[n][4] = (int) buttonCenterX;
                    this.virtualPadPos[n][5] = (int) touchCenterY;
                }
            }
        }
        this.initvirtualPad = 1;
    }

    public void toggleStickyButton(int but, int bit) {
        int[] iArr = this.stickyButton;
        iArr[but] = iArr[but] ^ 1;
        if (this.stickyButton[but] != 0) {
            this.f165e.setPadDataDown(bit & SupportMenu.USER_MASK, 0);
        } else {
            this.f165e.setPadDataUp(bit & SupportMenu.USER_MASK, 0);
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
                        this.f165e.setPadDataUp(this.virtualPadBit[but] & SupportMenu.USER_MASK, 0);
                        this.statebuttons &= (this.virtualPadBit[but] ^ (-1)) & SupportMenu.USER_MASK;
                    } else {
                        this.f165e.setPadDataUp(0, this.virtualPadBit[but] & SupportMenu.USER_MASK);
                    }
                }
                this.virtualPadId[Id] = -1;
            }
        }
        for (int i = 0; i < MAX_TOUCHES; i++) {
            this.activeTouchIds[i] = -1;
            this.touchButtonIds[i] = -1;
        }
    }

    private int analogStickPointerId = -1;
    private int analogStickIndex = -1;

    public int touchscreenevent(long eventTime, int action, int xi, int yi, float pressure, float size, int deviceId,
            int Id) {
        int ret = 0;
        int xa;
        int ya;
        int nbuttons = this.emu_player_mode == 1 ? 20 : 40;
        int ext = 0;
        int ext2 = 0;
        int mov = 0;
        int found = 0;
        if (analogStickPointerId != -1 && Id == analogStickPointerId && analogStickIndex != -1) {
            if (action == MotionEvent.ACTION_MOVE) {

                int centerX = (this.virtualPadPos[analogStickIndex][0] + this.virtualPadPos[analogStickIndex][2]) / 2;
                int centerY = (this.virtualPadPos[analogStickIndex][1] + this.virtualPadPos[analogStickIndex][3]) / 2;
                int radius = (this.virtualPadPos[analogStickIndex][2] - this.virtualPadPos[analogStickIndex][0]) / 2;
                int dx = xi - centerX;
                int dy = yi - centerY;
                double dist = Math.sqrt(dx * dx + dy * dy);
                if (dist > radius) {
                    double scale = radius / dist;
                    dx = (int) (dx * scale);
                    dy = (int) (dy * scale);
                }
                int limitedX = centerX + dx;
                int limitedY = centerY + dy;
                int tx = this.virtualPadPos[analogStickIndex][2] - this.virtualPadPos[analogStickIndex][0];
                int ty = this.virtualPadPos[analogStickIndex][3] - this.virtualPadPos[analogStickIndex][1];
                int xa2 = ((limitedX - this.virtualPadPos[analogStickIndex][0]) * 255) / tx;
                int ya2 = ((limitedY - this.virtualPadPos[analogStickIndex][1]) * 255) / ty;
                int pad3 = ((this.virtualPadBit[analogStickIndex] >> 16) & 1) ^ 1;
                if (analogStickIndex == 20) {
                    this.analog_values[0][0] = limitedX;
                    this.analog_values[0][1] = this.mHeight - limitedY;
                }
                if (analogStickIndex == 21) {
                    this.analog_values[0][2] = limitedX;
                    this.analog_values[0][3] = this.mHeight - limitedY;
                }
                this.f165e.setpadanalog(pad3, analogStickIndex - 20, xa2 - 128, ya2 - 128);
                this.virtualPadId[Id] = analogStickIndex;
                return 1;
            }
            if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_POINTER_UP) {
                int pad = ((this.virtualPadBit[analogStickIndex] >> 16) & 1) ^ 1;
                this.f165e.setpadanalog(pad, analogStickIndex - 20, 0, 0);

                int centerX = (this.virtualPadPos[analogStickIndex][0] + this.virtualPadPos[analogStickIndex][2]) / 2;
                int centerY = (this.virtualPadPos[analogStickIndex][1] + this.virtualPadPos[analogStickIndex][3]) / 2;
                if (analogStickIndex == 20) {
                    this.analog_values[0][0] = centerX;
                    this.analog_values[0][1] = this.mHeight - centerY;
                }
                if (analogStickIndex == 21) {
                    this.analog_values[0][2] = centerX;
                    this.analog_values[0][3] = this.mHeight - centerY;
                }
                analogStickPointerId = -1;
                analogStickIndex = -1;
                this.virtualPadId[Id] = -1;
                return 1;
            }

            return 1;
        }

        if (analogStickPointerId == -1
                && (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_POINTER_DOWN)) {
            for (int analogIdx = 20; analogIdx <= 21; analogIdx++) {

                if (xi >= this.virtualPadPos[analogIdx][0] && xi <= this.virtualPadPos[analogIdx][2]
                        && yi >= this.virtualPadPos[analogIdx][1] && yi <= this.virtualPadPos[analogIdx][3]) {
                    analogStickPointerId = Id;
                    analogStickIndex = analogIdx;

                    int centerX = (this.virtualPadPos[analogStickIndex][0] + this.virtualPadPos[analogStickIndex][2])
                            / 2;
                    int centerY = (this.virtualPadPos[analogStickIndex][1] + this.virtualPadPos[analogStickIndex][3])
                            / 2;
                    int radius = (this.virtualPadPos[analogStickIndex][2] - this.virtualPadPos[analogStickIndex][0])
                            / 2;
                    int dx = xi - centerX;
                    int dy = yi - centerY;
                    double dist = Math.sqrt(dx * dx + dy * dy);
                    if (dist > radius) {
                        double scale = radius / dist;
                        dx = (int) (dx * scale);
                        dy = (int) (dy * scale);
                    }
                    int limitedX = centerX + dx;
                    int limitedY = centerY + dy;
                    int tx = this.virtualPadPos[analogStickIndex][2] - this.virtualPadPos[analogStickIndex][0];
                    int ty = this.virtualPadPos[analogStickIndex][3] - this.virtualPadPos[analogStickIndex][1];
                    int xa2 = ((limitedX - this.virtualPadPos[analogStickIndex][0]) * 255) / tx;
                    int ya2 = ((limitedY - this.virtualPadPos[analogStickIndex][1]) * 255) / ty;
                    int pad3 = ((this.virtualPadBit[analogStickIndex] >> 16) & 1) ^ 1;
                    if (analogStickIndex == 20) {
                        this.analog_values[0][0] = limitedX;
                        this.analog_values[0][1] = this.mHeight - limitedY;
                    }
                    if (analogStickIndex == 21) {
                        this.analog_values[0][2] = limitedX;
                        this.analog_values[0][3] = this.mHeight - limitedY;
                    }
                    this.f165e.setpadanalog(pad3, analogStickIndex - 20, xa2 - 128, ya2 - 128);
                    this.virtualPadId[Id] = analogStickIndex;
                    return 1;
                }
            }
        }
        if (action == MotionEvent.ACTION_DOWN) {
            this.lastTouchX = xi;
            this.lastTouchY = yi;
            if (useDiagonal) {
                this.animationButtonIndex = AnimateButton.handleDpadDynamic(
                        xi, yi, virtualPadId, dpadsection, virtualPadBit,
                        statebuttons, padOffScreenLan,
                        padSizeScreenLan, padScreenResize,
                        mode, emu_pad_mode,
                        emu_pad_mode_analog, emu_pad_type_selected,
                        mHeight, f165e);
            }
            if (!useDiagonal) {
                this.animationButtonIndex = AnimateButton.getAnimatedDpadButton(xi, yi, virtualPadPos);
            }
            this.isDpadTouchActive = (this.animationButtonIndex != -1);
        } else if (action == MotionEvent.ACTION_MOVE) {
            this.lastTouchX = xi;
            this.lastTouchY = yi;

        } else if (action == MotionEvent.ACTION_UP) {
            this.isDpadTouchActive = false;
            this.animationButtonIndex = -1;
        }
        if (this.emu_pad_mode[this.emu_pad_type_selected] == 4
                && this.emu_pad_mode_analog[this.emu_pad_type_selected] == 1) {
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
            if (but < 20 || this.emu_player_mode != 1 || ext == 0) {
                if ((this.virtualPadBit[but] & 65536) == 65536) {
                    this.f165e.setPadDataUp(this.virtualPadBit[but] & SupportMenu.USER_MASK, 0);
                    this.statebuttons &= (~this.virtualPadBit[but]) & SupportMenu.USER_MASK;
                } else {
                    this.f165e.setPadDataUp(0, this.virtualPadBit[but] & SupportMenu.USER_MASK);
                }
                if (this.emu_pad_dynamic == 1 && but > 11 && but < 20
                        && (this.emu_pad_mode[this.emu_pad_type_selected] == 1
                                || (this.emu_pad_mode[this.emu_pad_type_selected] == 4
                                        && this.emu_pad_mode_analog[this.emu_pad_type_selected] == 0))) {
                    this.padScreenStatus[this.mode][0] = 0;
                    for (int j = 12; j < 20; j++) {
                        this.virtualPadPos[j][0] = -1;
                        this.virtualPadPos[j][2] = -1;
                    }
                }
                if (this.emu_action_dynamic == 1 && but > 3 && but < 8
                        && (this.emu_pad_mode[this.emu_pad_type_selected] == 1
                                || this.emu_pad_mode[this.emu_pad_type_selected] == 4)) {
                    this.padScreenStatus[this.mode][1] = 0;
                    for (int j2 = 4; j2 < 8; j2++) {
                        this.virtualPadPos[j2][0] = -1;
                        this.virtualPadPos[j2][2] = -1;
                    }
                }
            } else if (but < 22) {
                int pad = ((this.virtualPadBit[but] >> 16) & 1) ^ 1;
                this.f165e.setpadanalog(pad, but - 20, 0, 0);
                if (but == 20) {
                    this.analog_values[0][0] = ((this.virtualPadPos[but][2] - this.virtualPadPos[but][0]) / 2)
                            + this.virtualPadPos[but][0];
                    this.analog_values[0][1] = this.mHeight
                            - (((this.virtualPadPos[but][3] - this.virtualPadPos[but][1]) / 2)
                                    + this.virtualPadPos[but][1]);
                }
                if (but == 21) {
                    this.analog_values[0][2] = ((this.virtualPadPos[but][2] - this.virtualPadPos[but][0]) / 2)
                            + this.virtualPadPos[but][0];
                    this.analog_values[0][3] = this.mHeight
                            - (((this.virtualPadPos[but][3] - this.virtualPadPos[but][1]) / 2)
                                    + this.virtualPadPos[but][1]);
                }
                if (this.emu_pad_dynamic == 1 && but == 20) {
                    this.padScreenStatus[this.mode][11] = 0;
                    this.virtualPadPos[but][0] = -1;
                    this.virtualPadPos[but][2] = -1;
                }
            } else if (but > 22 && but < 29) {
                if ((this.virtualPadBit[but] & 65536) == 65536) {
                    this.f165e.setPadDataUp(this.virtualPadBit[but] & SupportMenu.USER_MASK, 0);
                } else {
                    this.f165e.setPadDataUp(0, this.virtualPadBit[but] & SupportMenu.USER_MASK);
                }
            }

            for (int i = 0; i < MAX_TOUCHES; i++) {
                if (this.activeTouchIds[i] == Id) {
                    this.activeTouchIds[i] = -1;
                    this.touchButtonIds[i] = -1;
                    break;
                }
            }

            this.virtualPadId[Id] = -1;
        }
        if (action != 0) {
            return 0;
        }
        if (this.emu_pad_mode[this.emu_pad_type_selected] == 4 && mov == 0 && xi >= this.virtualPadPos[22][0]
                && xi <= this.virtualPadPos[22][2] && yi >= (this.virtualPadPos[22][1] - 140)
                && yi <= (this.virtualPadPos[22][3] + 140)) {
            int[] iArr = this.emu_pad_mode_analog;
            int i = this.emu_pad_type_selected;
            iArr[i] = iArr[i] ^ 1;
            this.mode = this.emu_pad_mode_analog[this.emu_pad_type_selected];
            this.f165e.setpadanalogmode(this.emu_pad_type_selected,
                    this.emu_pad_mode_analog[this.emu_pad_type_selected]);
            clearAllbuttons(ext);
            init_motionevent_1playerLandscape();
            resetDynamicPad();

            if ((this.virtualPadBit[22] & 65536) == 65536) {
                this.f165e.setPadDataDown(this.virtualPadBit[22] & SupportMenu.USER_MASK, 0);
                this.statebuttons |= this.virtualPadBit[22] & SupportMenu.USER_MASK;
            } else {
                this.f165e.setPadDataDown(0, this.virtualPadBit[22] & SupportMenu.USER_MASK);
            }

            return ret;
        }
        if (mov == 0 && this.padScreenExtraEnabled == 1 && !this.hidePad) {
            for (int i2 = 0; i2 < 6; i2++) {
                if (this.padScreenFunc[i2] == 1 && xi >= this.virtualPadPos[i2 + 23][0]
                        && xi <= this.virtualPadPos[i2 + 23][2] && yi >= this.virtualPadPos[i2 + 23][1]
                        && yi <= this.virtualPadPos[i2 + 23][3]) {
                    if (this.padScreenExtra[i2] >= 0 && this.padScreenExtra[i2] < 5) {
                        this.f165e.setsslot(this.padScreenExtra[i2]);
                    } else if (this.padScreenExtra[i2] < 10) {
                        this.f165e.setsslot(this.padScreenExtra[i2] + 5);
                        setSaveslot(this.padScreenExtra[i2] + 5);
                    } else if (this.padScreenExtra[i2] == 18) {
                        toggleframelimit();
                    } else if (this.padScreenExtra[i2] == 19) {
                        if (this.myActivity != null) {
                            this.myActivity.doMenu();
                        }
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
            if (xi >= this.virtualPadPos[ind][0] && xi <= this.virtualPadPos[ind][2] && yi >= this.virtualPadPos[ind][1]
                    && yi <= this.virtualPadPos[ind][3] && action == 0) {
                int pressed = this.virtualPadId[Id];
                if (this.virtualPadId[Id] != -1) {
                    int ind2 = this.virtualPadId[Id];

                    // Упрощенная логика: всегда отпускаем кнопку при отпускании пальца
                    if (ind2 < 20 || this.emu_player_mode != 1) {
                        if ((this.virtualPadBit[ind2] & 65536) == 65536) {
                            this.f165e.setPadDataUp(this.virtualPadBit[ind2] & SupportMenu.USER_MASK, 0);
                            this.statebuttons &= (~this.virtualPadBit[ind2]) & SupportMenu.USER_MASK;
                        } else {
                            this.f165e.setPadDataUp(0, this.virtualPadBit[ind2] & SupportMenu.USER_MASK);
                        }
                    } else if (ind2 < 22 && ext != 0) {
                        int pad2 = ((this.virtualPadBit[ind2] >> 16) & 1) ^ 1;
                        this.f165e.setpadanalog(pad2, ind2 - 20, 0, 0);
                        if (ind2 == 20) {
                            this.analog_values[0][0] = ((this.virtualPadPos[ind2][2] - this.virtualPadPos[ind2][0]) / 2)
                                    + this.virtualPadPos[ind2][0];
                            this.analog_values[0][1] = this.mHeight
                                    - (((this.virtualPadPos[ind2][3] - this.virtualPadPos[ind2][1]) / 2)
                                            + this.virtualPadPos[ind2][1]);
                        }
                        if (ind2 == 21) {
                            this.analog_values[0][2] = ((this.virtualPadPos[ind2][2] - this.virtualPadPos[ind2][0]) / 2)
                                    + this.virtualPadPos[ind2][0];
                            this.analog_values[0][3] = this.mHeight
                                    - (((this.virtualPadPos[ind2][3] - this.virtualPadPos[ind2][1]) / 2)
                                            + this.virtualPadPos[ind2][1]);
                        }
                    } else if (ind2 > 22 && ind2 < 29) {
                        if ((this.virtualPadBit[ind2] & 65536) == 65536) {
                            this.f165e.setPadDataUp(this.virtualPadBit[ind2] & SupportMenu.USER_MASK, 0);
                        } else {
                            this.f165e.setPadDataUp(0, this.virtualPadBit[ind2] & SupportMenu.USER_MASK);
                        }
                    }
                }
                if (ind < 20 || this.emu_player_mode != 1) {
                    if ((this.virtualPadBit[ind] & 65536) == 65536) {
                        this.f165e.setPadDataDown(this.virtualPadBit[ind] & SupportMenu.USER_MASK, 0);
                        this.statebuttons |= this.virtualPadBit[ind] & SupportMenu.USER_MASK;
                    } else {
                        this.f165e.setPadDataDown(0, this.virtualPadBit[ind] & SupportMenu.USER_MASK);
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
                    int xa2 = ((xi - this.virtualPadPos[ind][0]) * 255) / tx;
                    int ya2 = ((yi - this.virtualPadPos[ind][1]) * 255) / ty;
                    int pad3 = ((this.virtualPadBit[ind] >> 16) & 1) ^ 1;
                    if (ind == 20) {
                        this.analog_values[0][0] = xi;
                        this.analog_values[0][1] = this.mHeight - yi;
                    }
                    if (ind == 21) {
                        this.analog_values[0][2] = xi;
                        this.analog_values[0][3] = this.mHeight - yi;
                    }
                    this.f165e.setpadanalog(pad3, ind - 20, xa2 - 128, ya2 - 128);
                    if (this.emu_pad_dynamic == 1 && ind == 20) {
                        this.padScreenStatus[this.mode][11] = 1;
                    }
                } else if (ind > 22 && ind < 29) {
                    if ((this.virtualPadBit[ind] & 65536) == 65536) {
                        this.f165e.setPadDataDown(this.virtualPadBit[ind] & SupportMenu.USER_MASK, 0);
                    } else {
                        this.f165e.setPadDataDown(0, this.virtualPadBit[ind] & SupportMenu.USER_MASK);
                    }
                }
                this.virtualPadId[Id] = ind;
                for (int i = 0; i < MAX_TOUCHES; i++) {
                    if (this.activeTouchIds[i] == -1) {
                        this.activeTouchIds[i] = Id;
                        this.touchButtonIds[i] = ind;
                        break;
                    }
                }
                ret = (action == 2 && pressed == ind) ? 0 : 1;
                found = 1;
            }
            ind++;
        }
        if (this.emu_pad_dynamic == 1 && this.virtualPadId[Id] == -1) {
            if (xi < this.mWidth / 2 && this.emu_pad_mode[this.emu_pad_type_selected] == 4
                    && this.emu_pad_mode_analog[this.emu_pad_type_selected] == 1) {
                this.padOffScreenLan[this.mode][this.virtualPad[20][0] * 2] = xi;
                this.padOffScreenLan[this.mode][(this.virtualPad[20][0] * 2) + 1] = this.mHeight - yi;
                int offx = ((int) this.padOffScreenLan[this.mode][this.virtualPad[20][0] * 2])
                        - ((int) ((this.padSizeScreenLan[this.mode][this.virtualPad[20][0] * 2]
                                * this.padScreenResize[this.mode][this.virtualPad[20][0]]) / 2.0f));
                int offy = ((int) this.padOffScreenLan[this.mode][(this.virtualPad[20][0] * 2) + 1])
                        - ((int) ((this.padSizeScreenLan[this.mode][(this.virtualPad[20][0] * 2) + 1]
                                * this.padScreenResize[this.mode][this.virtualPad[20][0]]) / 2.0f));
                this.virtualPadPos[20][0] = ((int) (this.virtualPad[20][1] * this.padResize
                        * this.padScreenResize[this.mode][this.virtualPad[20][0]])) + offx;
                this.virtualPadPos[20][1] = (((int) ((this.virtualPad[20][2] * this.padResize)
                        * this.padScreenResize[this.mode][this.virtualPad[20][0]]))
                        + (this.mHeight - ((int) (this.padSizeScreenLan[this.mode][(this.virtualPad[20][0] * 2) + 1]
                                * this.padScreenResize[this.mode][this.virtualPad[20][0]]))))
                        - offy;
                this.virtualPadPos[20][2] = ((int) (this.virtualPad[20][3] * this.padResize
                        * this.padScreenResize[this.mode][this.virtualPad[20][0]])) + offx;
                this.virtualPadPos[20][3] = (((int) ((this.virtualPad[20][4] * this.padResize)
                        * this.padScreenResize[this.mode][this.virtualPad[20][0]]))
                        + (this.mHeight - ((int) (this.padSizeScreenLan[this.mode][(this.virtualPad[20][0] * 2) + 1]
                                * this.padScreenResize[this.mode][this.virtualPad[20][0]]))))
                        - offy;
                this.analog_values[0][0] = ((this.virtualPadPos[20][2] - this.virtualPadPos[20][0]) / 2)
                        + this.virtualPadPos[20][0];
                this.analog_values[0][1] = this.mHeight
                        - (((this.virtualPadPos[20][3] - this.virtualPadPos[20][1]) / 2) + this.virtualPadPos[20][1]);
                int pad4 = ((this.virtualPadBit[20] >> 16) & 1) ^ 1;
                this.analog_values[0][0] = xi;
                this.analog_values[0][1] = this.mHeight - yi;
                this.f165e.setpadanalog(pad4, 0, -128, -128);
                this.virtualPadId[Id] = 20;
                this.padScreenStatus[this.mode][11] = 1;
            } else if (xi < this.mWidth / 2 && (this.emu_pad_mode[this.emu_pad_type_selected] == 1
                    || (this.emu_pad_mode[this.emu_pad_type_selected] == 4
                            && this.emu_pad_mode_analog[this.emu_pad_type_selected] == 0))) {
                this.padOffScreenLan[this.mode][0] = xi;
                this.padOffScreenLan[this.mode][1] = this.mHeight - yi;
                for (int n = 12; n < 20; n++) {
                    int offx2 = ((int) this.padOffScreenLan[this.mode][this.virtualPad[n][0] * 2])
                            - ((int) ((this.padSizeScreenLan[this.mode][this.virtualPad[n][0] * 2]
                                    * this.padScreenResize[this.mode][this.virtualPad[n][0]]) / 2.0f));
                    int offy2 = ((int) this.padOffScreenLan[this.mode][(this.virtualPad[n][0] * 2) + 1])
                            - ((int) ((this.padSizeScreenLan[this.mode][(this.virtualPad[n][0] * 2) + 1]
                                    * this.padScreenResize[this.mode][this.virtualPad[n][0]]) / 2.0f));
                    this.virtualPadPos[n][0] = ((int) (this.virtualPad[n][1] * this.padResize
                            * this.padScreenResize[this.mode][this.virtualPad[n][0]])) + offx2;
                    this.virtualPadPos[n][1] = (((int) ((this.virtualPad[n][2] * this.padResize)
                            * this.padScreenResize[this.mode][this.virtualPad[n][0]]))
                            + (this.mHeight - ((int) (this.padSizeScreenLan[this.mode][(this.virtualPad[n][0] * 2) + 1]
                                    * this.padScreenResize[this.mode][this.virtualPad[n][0]]))))
                            - offy2;
                    this.virtualPadPos[n][2] = ((int) (this.virtualPad[n][3] * this.padResize
                            * this.padScreenResize[this.mode][this.virtualPad[n][0]])) + offx2;
                    this.virtualPadPos[n][3] = (((int) ((this.virtualPad[n][4] * this.padResize)
                            * this.padScreenResize[this.mode][this.virtualPad[n][0]]))
                            + (this.mHeight - ((int) (this.padSizeScreenLan[this.mode][(this.virtualPad[n][0] * 2) + 1]
                                    * this.padScreenResize[this.mode][this.virtualPad[n][0]]))))
                            - offy2;
                }
                this.virtualPadId[Id] = 12;
                this.padScreenStatus[this.mode][0] = 1;
            }
        }
        if (this.emu_action_dynamic == 1 && this.virtualPadId[Id] == -1 && xi > this.mWidth / 2) {
            this.padOffScreenLan[this.mode][2] = xi;
            this.padOffScreenLan[this.mode][3] = this.mHeight - yi;
            for (int n2 = 4; n2 < 8; n2++) {
                int offx3 = ((int) this.padOffScreenLan[this.mode][this.virtualPad[n2][0] * 2])
                        - ((int) ((this.padSizeScreenLan[this.mode][this.virtualPad[n2][0] * 2]
                                * this.padScreenResize[this.mode][this.virtualPad[n2][0]]) / 2.0f));
                int offy3 = ((int) this.padOffScreenLan[this.mode][(this.virtualPad[n2][0] * 2) + 1])
                        - ((int) ((this.padSizeScreenLan[this.mode][(this.virtualPad[n2][0] * 2) + 1]
                                * this.padScreenResize[this.mode][this.virtualPad[n2][0]]) / 2.0f));
                this.virtualPadPos[n2][0] = ((int) (this.virtualPad[n2][1] * this.padResize
                        * this.padScreenResize[this.mode][this.virtualPad[n2][0]])) + offx3;
                this.virtualPadPos[n2][1] = (((int) ((this.virtualPad[n2][2] * this.padResize)
                        * this.padScreenResize[this.mode][this.virtualPad[n2][0]]))
                        + (this.mHeight - ((int) (this.padSizeScreenLan[this.mode][(this.virtualPad[n2][0] * 2) + 1]
                                * this.padScreenResize[this.mode][this.virtualPad[n2][0]]))))
                        - offy3;
                this.virtualPadPos[n2][2] = ((int) (this.virtualPad[n2][3] * this.padResize
                        * this.padScreenResize[this.mode][this.virtualPad[n2][0]])) + offx3;
                this.virtualPadPos[n2][3] = (((int) ((this.virtualPad[n2][4] * this.padResize)
                        * this.padScreenResize[this.mode][this.virtualPad[n2][0]]))
                        + (this.mHeight - ((int) (this.padSizeScreenLan[this.mode][(this.virtualPad[n2][0] * 2) + 1]
                                * this.padScreenResize[this.mode][this.virtualPad[n2][0]]))))
                        - offy3;
            }
            this.virtualPadId[Id] = 4;
            this.padScreenStatus[this.mode][1] = 1;
        }
        if (this.emu_action_dynamic == 0 && this.virtualPadId[Id] == -1) {
            int x1 = (int) (this.padOffScreenLan[this.mode][2]
                    - ((this.padSizeScreenLan[this.mode][2] * this.padScreenResize[this.mode][1]) / 2.0f));
            int y2 = this.mHeight - ((int) (this.padOffScreenLan[this.mode][3]
                    - ((this.padSizeScreenLan[this.mode][3] * this.padScreenResize[this.mode][1]) / 2.0f)));
            int x2 = x1 + ((int) (this.padSizeScreenLan[this.mode][2] * this.padScreenResize[this.mode][1]));
            int y1 = y2 - ((int) (this.padSizeScreenLan[this.mode][3] * this.padScreenResize[this.mode][1]));
            if (xi >= x1 && xi <= x2 && yi >= y1 && yi <= y2) {
                int seg = ((int) Math
                        .toDegrees(Math.atan2((this.mHeight - yi) - ((int) this.padOffScreenLan[this.mode][3]),
                                xi - ((int) this.padOffScreenLan[this.mode][2])) / 22.5d))
                        + 8;
                if (seg > 0 && seg < 17) {
                    int ind22 = this.apadsection[seg];
                    if ((this.virtualPadBit[ind22] & 65536) == 65536) {
                        this.f165e.setPadDataDown(this.virtualPadBit[ind22] & SupportMenu.USER_MASK, 0);
                        this.statebuttons |= this.virtualPadBit[ind22] & SupportMenu.USER_MASK;
                    } else {
                        this.f165e.setPadDataDown(0, this.virtualPadBit[ind22] & SupportMenu.USER_MASK);
                    }
                    this.virtualPadId[Id] = ind22;
                    found = 1;
                }
            }
        }
        if (this.emu_pad_mode[this.emu_pad_type_selected] == 4 || this.emu_pad_mode[this.emu_pad_type_selected] == 1) {
            if (found == 0 && this.emu_pad_mode[this.emu_pad_type_selected] == 4
                    && this.emu_pad_mode_analog[this.emu_pad_type_selected] == 1
                    && (this.virtualPadId[Id] == 20 || this.virtualPadId[Id] == 21)) {
                int ind23 = this.virtualPadId[Id];
                int tx2 = this.virtualPadPos[ind23][2] - this.virtualPadPos[ind23][0];
                int ty2 = this.virtualPadPos[ind23][3] - this.virtualPadPos[ind23][1];
                int pad5 = ((this.virtualPadBit[ind23] >> 16) & 1) ^ 1;
                if (xi <= this.virtualPadPos[ind23][0]) {
                    xa = 0;
                } else {
                    xa = xi >= this.virtualPadPos[ind23][2] ? 255 : ((xi - this.virtualPadPos[ind23][0]) * 255) / tx2;
                }
                if (yi <= this.virtualPadPos[ind23][1]) {
                    ya = 0;
                } else {
                    ya = yi >= this.virtualPadPos[ind23][3] ? 255 : ((yi - this.virtualPadPos[ind23][1]) * 255) / ty2;
                }
                if (ind23 == 20) {
                    this.analog_values[0][0] = this.virtualPadPos[ind23][0] + ((xa * tx2) / 255);
                    this.analog_values[0][1] = this.mHeight - (this.virtualPadPos[ind23][1] + ((ya * tx2) / 255));
                }
                if (ind23 == 21) {
                    this.analog_values[0][2] = this.virtualPadPos[ind23][0] + ((xa * tx2) / 255);
                    this.analog_values[0][3] = this.mHeight - (this.virtualPadPos[ind23][1] + ((ya * tx2) / 255));
                }
                this.f165e.setpadanalog(pad5, ind23 - 20, xa - 128, ya - 128);
            }
            if (this.emu_pad_dynamic == 1 && found == 0 && this.virtualPadId[Id] > 11 && this.virtualPadId[Id] < 20
                    && (this.emu_pad_mode[this.emu_pad_type_selected] == 1
                            || (this.emu_pad_mode[this.emu_pad_type_selected] == 4
                                    && this.emu_pad_mode_analog[this.emu_pad_type_selected] == 0))) {
                int xdiff = xi - ((int) this.padOffScreenLan[this.mode][0]);
                int ydiff = (this.mHeight - yi) - ((int) this.padOffScreenLan[this.mode][1]);
                int seg2 = ((int) Math.toDegrees(Math.atan2(ydiff, xdiff) / 22.5d)) + 8;
                int siz = (int) Math.sqrt((xdiff * xdiff) + (ydiff * ydiff));
                if (siz >= ((int) ((this.padSizeScreenLan[this.mode][0] * this.padScreenResize[this.mode][0]) / 12.0f))
                        && seg2 > 0 && seg2 < 17) {
                    int up = this.virtualPadId[Id];
                    int ind24 = this.dpadsection[seg2];
                    if ((this.virtualPadBit[up] & 65536) == 65536) {
                        this.f165e.setPadDataUp(this.virtualPadBit[up] & SupportMenu.USER_MASK, 0);
                        this.statebuttons &= (this.virtualPadBit[up] ^ (-1)) & SupportMenu.USER_MASK;
                    } else {
                        this.f165e.setPadDataUp(0, this.virtualPadBit[up] & SupportMenu.USER_MASK);
                    }
                    if ((this.virtualPadBit[ind24] & 65536) == 65536) {
                        this.f165e.setPadDataDown(this.virtualPadBit[ind24] & SupportMenu.USER_MASK, 0);
                        this.statebuttons |= this.virtualPadBit[ind24] & SupportMenu.USER_MASK;
                    } else {
                        this.f165e.setPadDataDown(0, this.virtualPadBit[ind24] & SupportMenu.USER_MASK);
                    }
                    this.virtualPadId[Id] = ind24;
                }
            }
            if (this.emu_action_dynamic == 1 && found == 0 && this.virtualPadId[Id] > 3 && this.virtualPadId[Id] < 8) {
                int xdiff2 = xi - ((int) this.padOffScreenLan[this.mode][2]);
                int ydiff2 = (this.mHeight - yi) - ((int) this.padOffScreenLan[this.mode][3]);
                int seg3 = ((int) Math.toDegrees(Math.atan2(ydiff2, xdiff2) / 22.5d)) + 8;
                int siz2 = (int) Math.sqrt((xdiff2 * xdiff2) + (ydiff2 * ydiff2));
                if (siz2 >= ((int) ((this.padSizeScreenLan[this.mode][2] * this.padScreenResize[this.mode][1]) / 12.0f))
                        && seg3 > 0 && seg3 < 17) {
                    int up2 = this.virtualPadId[Id];
                    int ind25 = this.apadsection[seg3];
                    if ((this.virtualPadBit[up2] & 65536) == 65536) {
                        this.f165e.setPadDataUp(this.virtualPadBit[up2] & SupportMenu.USER_MASK, 0);
                    } else {
                        this.f165e.setPadDataUp(0, this.virtualPadBit[up2] & SupportMenu.USER_MASK);
                    }
                    if ((this.virtualPadBit[ind25] & 65536) == 65536) {
                        this.f165e.setPadDataDown(this.virtualPadBit[ind25] & SupportMenu.USER_MASK, 0);
                    } else {
                        this.f165e.setPadDataDown(0, this.virtualPadBit[ind25] & SupportMenu.USER_MASK);
                    }
                    this.virtualPadId[Id] = ind25;
                }
            }
            if (this.virtualPadId[Id] == -1 && found == 0 && this.emu_action_dynamic == 0
                    && this.emu_pad_dynamic == 0) {
                int distance = this.mWidth * 7;
                int selected = -1;
                for (int ind3 = 0; ind3 < 20; ind3++) {
                    int w = this.virtualPadPos[ind3][2] - this.virtualPadPos[ind3][0];
                    int h = this.virtualPadPos[ind3][3] - this.virtualPadPos[ind3][1];
                    int dx = Math.max(Math.abs(xi - this.virtualPadPos[ind3][4]) - (w / 2), 0);
                    int dy = Math.max(Math.abs(yi - this.virtualPadPos[ind3][5]) - (h / 2), 0);
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
                        this.f165e.setPadDataDown(this.virtualPadBit[selected] & SupportMenu.USER_MASK, 0);
                        this.statebuttons |= this.virtualPadBit[selected] & SupportMenu.USER_MASK;
                    } else {
                        this.f165e.setPadDataDown(0, this.virtualPadBit[selected] & SupportMenu.USER_MASK);
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
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                isInTouch = true;
                break;
            case MotionEvent.ACTION_UP:
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
        String[] names = { "DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE", "POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?" };
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
        if (this.emu_pad_type[0] != 1 || this.emu_pad_type[1] != 1) {
            if (actionCode == 5 || actionCode == 6 || actionCode == 1 || actionCode == 0 || actionCode == 2) {
                vib = (this.emu_pad_mode[0] == 3 || this.emu_pad_mode[0] == 8)
                        ? this.gun.touchscreeneventgun(0L, action, x, y, 0.0f, 0.0f, 0, pointerId, this.f165e,
                                this.emu_pad_mode[0], this.mfps, this.mWidth, this.mHeight, this.emu_pad_type_selected)
                        : 0 + touchscreenevent(0L, action, x, y, 0.0f, 0.0f, 0, pointerId);
            }
            if ((this.ts_vibration[0] == 1 && vib == 1) || (this.ts_vibration[1] == 1 && vib == 2)) {
                try {
                    Vibrator v = (Vibrator) this.mContext.getSystemService("vibrator");
                    if (v != null) {
                        v.vibrate(35L);
                    }
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
        dumpEvent(event);
        int i = this.osVersion >= 8 ? event.getActionIndex() : getActionIndexEclair(event);
        if (this.emu_pad_type[0] != 1 || this.emu_pad_type[1] != 1) {
            if (actionCode == MotionEvent.ACTION_POINTER_DOWN
                    || actionCode == MotionEvent.ACTION_POINTER_UP
                    || actionCode == MotionEvent.ACTION_UP
                    || actionCode == MotionEvent.ACTION_DOWN) {

                if (this.emu_pad_mode[0] == 3 || this.emu_pad_mode[0] == 8) {
                    vib = this.gun.touchscreeneventgun(event.getEventTime(), event.getAction(), (int) event.getX(i),
                            (int) event.getY(i), event.getPressure(i), event.getSize(i), event.getDeviceId(),
                            event.getPointerId(i), this.f165e, this.emu_pad_mode[0], this.mfps, this.mWidth,
                            this.mHeight, this.emu_pad_type_selected);
                } else {
                    vib = touchscreenevent(event.getEventTime(), event.getAction(), (int) event.getX(i),
                            (int) event.getY(i), event.getPressure(i), event.getSize(i), event.getDeviceId(),
                            event.getPointerId(i));
                }
            } else if (actionCode == MotionEvent.ACTION_MOVE) {
                for (int ind = 0; ind < event.getPointerCount(); ind++) {
                    if (this.emu_pad_mode[0] == 3 || this.emu_pad_mode[0] == 8) {
                        vib = this.gun.touchscreeneventgun(event.getEventTime(), event.getAction(), (int) event.getX(i),
                                (int) event.getY(i), event.getPressure(i), event.getSize(i), event.getDeviceId(),
                                event.getPointerId(i), this.f165e, this.emu_pad_mode[0], this.mfps, this.mWidth,
                                this.mHeight, this.emu_pad_type_selected);
                    } else {
                        touchscreenevent(event.getEventTime(), event.getAction(), (int) event.getX(ind),
                                (int) event.getY(ind), event.getPressure(ind), event.getSize(ind), event.getDeviceId(),
                                event.getPointerId(ind));
                    }
                }
            }
            if ((this.ts_vibration[0] == 1 && vib == 1) || (this.ts_vibration[1] == 1 && vib == 2)) {
                try {
                    Vibrator v = (Vibrator) this.mContext.getSystemService("vibrator");
                    if (v != null) {
                        v.vibrate(35L);
                    }
                } catch (Exception e) {
                }
            }
        }
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setSaveMode(int mode, int auto) {
        Log.e("epsxe", "epsxeview saving status");
        this.f165e.setSaveMode(mode, auto);
        this.onPauseMode = 1;
        super.onPause();
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void onPause(int mode, int auto) {
        try {
            Log.e("epsxelf", "epsxeview pause status");
            if (this.f165e != null) {
                this.f165e.setPauseMode(mode, auto);
            }
            this.onPauseMode = 1;
            super.onPause();
        } catch (Exception e) {
        }
    }

    @Override // android.opengl.GLSurfaceView, com.epsxe.ePSXe.ePSXeView
    public void onResume() {
        Log.e("epsxelf", "epsxeview resume status");
        this.f165e.setResumeMode();
        this.onPauseMode = 0;
        super.onResume();
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void onStop() {
        Log.e("epsxelf", "epsxeview stop status");
        this.f165e.setStopMode();
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void onAutosave(int mode) {
        Log.e("epsxe", "epsxeview autosaving status");
        this.f165e.setSaveMode(mode, 1);
    }

    @Override // android.view.View
    @TargetApi(24)
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

    /**
     * Вычисляет оптимальный масштаб для виртуальных кнопок в ландшафтном режиме
     */
    public void calculateLandscapePadScale() {
        // Базовые размеры для расчета (оригинальные размеры ePSXe)
        final float BASE_WIDTH = 800.0f;
        final float BASE_HEIGHT = 480.0f;

        // Вычисляем коэффициенты масштабирования по осям
        float scaleX = (float) this.mWidth / BASE_WIDTH;
        float scaleY = (float) this.mHeight / BASE_HEIGHT;

        // Используем минимальный коэффициент для сохранения пропорций
        float baseScale = Math.min(scaleX, scaleY);

        // УМЕНЬШАЕМ кнопки в 2 раза - идеальный размер
        baseScale = baseScale / 2.0f;

        // Ограничиваем масштаб разумными пределами
        if (baseScale > 1.0f) {
            baseScale = 1.0f;
        } else if (baseScale < 0.3f) {
            baseScale = 0.3f;
        }

        // Применяем базовый масштаб
        this.padResize = baseScale;

        // Логирование для отладки
        Log.d("PadScaling", String.format(
                "Screen: %dx%d, ScaleX: %.2f, ScaleY: %.2f, FinalScale: %.2f (уменьшено в 3 раза)",
                this.mWidth, this.mHeight,
                scaleX, scaleY, this.padResize));
    }

    private class ePSXeRenderer2 implements GLSurfaceView.Renderer {

        SpriteBatch2[] batchLan;
        SpriteBatch2[] batchLanAction;
        SpriteBatch2[] batchLanDpad;
        SpriteBatch2 batchPor;
        private GLText2 glText;
        int[] hTexLan;
        int[] hTexLanAction;
        int[] hTexLanDpad;
        int hTexPor;
        int hTextmp;
        int mProgram;
        int mTexExtra;
        int mTexLan;
        int mTexPor;
        int mpadAlphaLoc;
        int mpadFrameLoc;
        int mpadPositionLoc;
        int mpadTexCoordLoc;
        float[] offActionX;
        float[] offActionY;
        float[] offDpadX;
        float[] offDpadY;
        private final String pFragmentShader;
        private final String pFragmentShaderOld;
        int[] padCoordsExtra;
        private final String sVertexShader;
        float[] sizeActionX;
        float[] sizeActionY;
        float[] sizeDpadX;
        float[] sizeDpadY;
        TextureRegion[] textureRgnLan;
        TextureRegion[] textureRgnLanAction;
        TextureRegion[] textureRgnLanDpad;
        TextureRegion textureRgnPor;
        int[] wTexLan;
        int[] wTexLanAction;
        int[] wTexLanDpad;
        int wTexPor;
        int wTextmp;

        private ePSXeRenderer2() {
            this.mTexPor = -1;
            this.mTexLan = -1;
            this.mTexExtra = -1;
            this.batchLan = new SpriteBatch2[28];
            this.textureRgnLan = new TextureRegion[28];
            this.hTexLan = new int[28];
            this.wTexLan = new int[28];
            this.batchLanAction = new SpriteBatch2[4];
            this.textureRgnLanAction = new TextureRegion[4];
            this.hTexLanAction = new int[4];
            this.wTexLanAction = new int[4];
            this.sizeActionX = new float[4];
            this.sizeActionY = new float[4];
            this.offActionX = new float[4];
            this.offActionY = new float[4];
            this.batchLanDpad = new SpriteBatch2[4];
            this.textureRgnLanDpad = new TextureRegion[4];
            this.hTexLanDpad = new int[4];
            this.wTexLanDpad = new int[4];
            this.sizeDpadX = new float[4];
            this.sizeDpadY = new float[4];
            this.offDpadX = new float[4];
            this.offDpadY = new float[4];
            this.sVertexShader = "attribute vec4 a_position;attribute vec2 a_texCoord;varying vec2 v_texCoord;void main(){    gl_Position = a_position;    v_texCoord = a_texCoord;}";
            this.pFragmentShaderOld = "precision mediump float;varying vec2 v_texCoord;uniform sampler2D Frame;uniform float padAlpha;void main(){    gl_FragColor = texture2D(Frame, v_texCoord);    gl_FragColor.a = gl_FragColor.a * padAlpha;}";
            this.pFragmentShader = "precision mediump float;varying vec2 v_texCoord;uniform sampler2D Frame;uniform float padAlpha;void main(){    gl_FragColor = texture2D(Frame, v_texCoord) * padAlpha;}";
            this.padCoordsExtra = new int[] { 0, 0, 64, 64, 64, 0, 128, 64, 128, 0, InputList.KEYCODE_BUTTON_5, 64,
                    InputList.KEYCODE_BUTTON_5, 0, 256, 64, 0, 64, 64, 128, 64, 64, 128, 128, 128, 64,
                    InputList.KEYCODE_BUTTON_5, 128, InputList.KEYCODE_BUTTON_5, 64, 256, 128, 0, 128, 64,
                    InputList.KEYCODE_BUTTON_9, 64, 128, 128, InputList.KEYCODE_BUTTON_9, 128, 128,
                    InputList.KEYCODE_BUTTON_5, InputList.KEYCODE_BUTTON_9, InputList.KEYCODE_BUTTON_5, 128, 256,
                    InputList.KEYCODE_BUTTON_9, 0, InputList.KEYCODE_BUTTON_5, 64, 256, 64, InputList.KEYCODE_BUTTON_5,
                    128, 256, 128, InputList.KEYCODE_BUTTON_5, InputList.KEYCODE_BUTTON_5, 256,
                    InputList.KEYCODE_BUTTON_5, InputList.KEYCODE_BUTTON_5, 256, 256, 0, 256, 64, 320, 64, 256, 128,
                    320, 128, 256, InputList.KEYCODE_BUTTON_7, 320, InputList.KEYCODE_BUTTON_5, 256, 256, 320, 0, 320,
                    64, 384, 64, 320, 128, 384, 128, 320, InputList.KEYCODE_BUTTON_9, 384, InputList.KEYCODE_BUTTON_5,
                    320, 256, 384, 0, 384, 64, 448, 63, 384, 126, 448, 128, 384, InputList.KEYCODE_BUTTON_9, 448,
                    InputList.KEYCODE_BUTTON_5, 384, 256, 448, 0, 448, 64, 511, 64, 448, 128, 511, 128, 448,
                    InputList.KEYCODE_BUTTON_9, 511, InputList.KEYCODE_BUTTON_5, 448, 256, 511, 256, 0, 320, 64, 320, 0,
                    384, 64, 384, 0, 448, 64, 448, 0, 511, 64 };
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

        public void drawFPS(GL10 gl) {
            GLES20.glEnable(3042);
            this.glText.begin(1.0f, 1.0f, 1.0f, 1.0f);
            if (ePSXeViewGL.this.tainted == 1) {
                this.glText.draw(String.valueOf(ePSXeViewGL.this.f165e.getFPS()) + "/" + ePSXeViewGL.this.mfps + "*",
                        ePSXeViewGL.this.overscan_x + 0,
                        (ePSXeViewGL.this.mHeight - this.glText.getCharHeight()) - ePSXeViewGL.this.overscan_y,
                        ePSXeViewGL.this.mWidth, ePSXeViewGL.this.mHeight);
            } else {
                this.glText.draw(String.valueOf(ePSXeViewGL.this.f165e.getFPS()) + "/" + ePSXeViewGL.this.mfps,
                        ePSXeViewGL.this.overscan_x + 0,
                        (ePSXeViewGL.this.mHeight - this.glText.getCharHeight()) - ePSXeViewGL.this.overscan_y,
                        ePSXeViewGL.this.mWidth, ePSXeViewGL.this.mHeight);
            }
            this.glText.end();
            GLES20.glDisable(3042);
        }

        public void drawVolumen(GL10 gl) {
            GLES20.glEnable(3042);
            this.glText.begin(1.0f, 1.0f, 1.0f, 1.0f);
            this.glText.draw(String.valueOf(ePSXeViewGL.this.emu_volumen) + "/16",
                    ePSXeViewGL.this.mWidth - (this.glText.getCharHeight() * 6.0f),
                    (ePSXeViewGL.this.mHeight - this.glText.getCharHeight()) - ePSXeViewGL.this.overscan_y,
                    ePSXeViewGL.this.mWidth, ePSXeViewGL.this.mHeight);
            this.glText.end();
            GLES20.glDisable(3042);
        }

        public void drawAnalogValues(GL10 gl) {
            this.glText.begin(1.0f, 1.0f, 1.0f, 1.0f);
            this.glText.draw(
                    "lx " + ePSXeViewGL.this.f166x1 + " ly " + ePSXeViewGL.this.f168y1 + " rx "
                            + ePSXeViewGL.this.f167x2 + " ry " + ePSXeViewGL.this.f169y2,
                    0.0f, ePSXeViewGL.this.mHeight - (this.glText.getCharHeight() * 3.0f), ePSXeViewGL.this.mWidth,
                    ePSXeViewGL.this.mHeight);
            this.glText.end();
        }

        public void drawDebugString(GL10 gl) {
            this.glText.begin(1.0f, 1.0f, 1.0f, 1.0f);
            this.glText.draw(ePSXeViewGL.this.debugString, 0.0f,
                    ePSXeViewGL.this.mHeight - (this.glText.getCharHeight() * 3.0f), ePSXeViewGL.this.mWidth,
                    ePSXeViewGL.this.mHeight);
            this.glText.end();
        }

        public void drawDebugString2(GL10 gl) {
            this.glText.begin(1.0f, 1.0f, 1.0f, 1.0f);
            this.glText.draw(ePSXeViewGL.this.debugString2, 0.0f,
                    ePSXeViewGL.this.mHeight - (this.glText.getCharHeight() * 4.0f), ePSXeViewGL.this.mWidth,
                    ePSXeViewGL.this.mHeight);
            this.glText.end();
        }

        private void drawLicense(GL10 gl) {
            GLES20.glEnable(3042);
            this.glText.begin(1.0f, 1.0f, 1.0f, 1.0f);
            this.glText.draw("License not validated yet. Read the documentation.", 60.0f, 0.0f, ePSXeViewGL.this.mWidth,
                    ePSXeViewGL.this.mHeight);
            this.glText.end();
            GLES20.glDisable(3042);
        }

        private void drawBiosMsg(GL10 gl) {
            GLES20.glEnable(3042);
            this.glText.begin(1.0f, 1.0f, 1.0f, 1.0f);
            this.glText.draw("Using HLE Bios, compatibility and options limited. Read the documentation.", 60.0f, 0.0f,
                    ePSXeViewGL.this.mWidth, ePSXeViewGL.this.mHeight);
            this.glText.end();
            GLES20.glDisable(3042);
        }

        private void drawString(GL10 gl, String s) {
            GLES20.glEnable(3042);
            this.glText.begin(1.0f, 1.0f, 1.0f, 1.0f);
            this.glText.draw(s, 60.0f, 0.0f, ePSXeViewGL.this.mWidth, ePSXeViewGL.this.mHeight);
            this.glText.end();
            GLES20.glDisable(3042);
        }

        public void drawDeviceName(GL10 gl) {
            this.glText.begin(1.0f, 1.0f, 1.0f, 1.0f);
            this.glText.draw(Build.DEVICE, 60.0f, 0.0f, ePSXeViewGL.this.mWidth, ePSXeViewGL.this.mHeight);
            this.glText.end();
        }

        @Override // android.opengl.GLSurfaceView.Renderer
        public void onDrawFrame(GL10 gl) {
            if (ePSXeViewGL.this.onPauseMode != 1) {
                ePSXeViewGL.this.f165e.openglrender(ePSXeViewGL.this.emu_player_mode,
                        ePSXeViewGL.this.emu_screen_orientation, ePSXeViewGL.this.emu_split_mode,
                        ePSXeViewGL.this.emu_screen_ratio, ePSXeViewGL.this.emu_screen_vrmode);
                if (ePSXeViewGL.this.onPauseMode != 1) {
                    if (ePSXeViewGL.this.redoPads) {
                        redoPads(gl);
                        ePSXeViewGL.this.redoPads = false;
                    }
                    try {
                        if (ePSXeViewGL.this.emu_player_mode != 1 || ePSXeViewGL.this.emu_screen_orientation != 1
                                || ePSXeViewGL.this.emu_portrait_skin != 0) {
                            if (ePSXeViewGL.this.emu_player_mode != 1 || ePSXeViewGL.this.emu_pad_draw_mode[0] == 2
                                    || ePSXeViewGL.this.emu_pad_mode[0] == 3 || ePSXeViewGL.this.emu_pad_mode[0] == 8) {
                                if (ePSXeViewGL.this.emu_pad_mode[0] == 3 || ePSXeViewGL.this.emu_pad_mode[0] == 8) {
                                    ePSXeViewGL.this.gun.drawGunGl(this.mTexLan, this.mProgram, this.textureRgnLan,
                                            this.batchLan, ePSXeViewGL.this.mWidth, ePSXeViewGL.this.mHeight);
                                }
                            } else {
                                int st = 0;
                                int en = 8;
                                if (ePSXeViewGL.this.emu_pad_mode[ePSXeViewGL.this.emu_pad_type_selected] == 4) {
                                    if (ePSXeViewGL.this.emu_pad_mode_analog[ePSXeViewGL.this.emu_pad_type_selected] == 0) {
                                        st = 0;
                                        en = 9;
                                    } else {
                                        st = 0;
                                        en = 13;
                                    }
                                }
                                if (!ePSXeViewGL.this.hidePad) {
                                    GLES20.glBindTexture(3553, this.mTexLan);
                                }
                                GLES20.glUseProgram(this.mProgram);
                                GLES20.glEnable(3042);
                                for (int i = st; i < en; i++) {
                                    if (ePSXeViewGL.this.padScreenStatus[ePSXeViewGL.this.mode][i] == 1) {
                                        if (i > 1) {
                                            if (i < 11 && i != 8) {
                                                if ((ePSXeViewGL.this.statebuttons
                                                        & ePSXeViewGL.this.psxbuttonval[i]) != 0) {
                                                    this.batchLan[i].beginBatch(this.mTexLan);
                                                    this.batchLan[i].drawSprite(
                                                            ePSXeViewGL.this.padOffScreenLan[ePSXeViewGL.this.mode][(i
                                                                    * 2) + 0] / ePSXeViewGL.this.mWidth,
                                                            ePSXeViewGL.this.padOffScreenLan[ePSXeViewGL.this.mode][(i
                                                                    * 2) + 1] / ePSXeViewGL.this.mHeight,
                                                            ((ePSXeViewGL.this.padSizeScreenLan[ePSXeViewGL.this.mode][(i
                                                                    * 2) + 0]
                                                                    * ePSXeViewGL.this.padScreenResize[ePSXeViewGL.this.mode][i])
                                                                    / ePSXeViewGL.this.mWidth)
                                                                    * ePSXeViewGL.this.buttonMag,
                                                            ((ePSXeViewGL.this.padSizeScreenLan[ePSXeViewGL.this.mode][(i
                                                                    * 2) + 1]
                                                                    * ePSXeViewGL.this.padScreenResize[ePSXeViewGL.this.mode][i])
                                                                    / ePSXeViewGL.this.mHeight)
                                                                    * ePSXeViewGL.this.buttonMag,
                                                            this.textureRgnLan[i]);
                                                    this.batchLan[i].endBatch();
                                                } else {
                                                    this.batchLan[i].beginBatch(this.mTexLan);
                                                    this.batchLan[i].drawSprite(
                                                            ePSXeViewGL.this.padOffScreenLan[ePSXeViewGL.this.mode][(i
                                                                    * 2) + 0] / ePSXeViewGL.this.mWidth,
                                                            ePSXeViewGL.this.padOffScreenLan[ePSXeViewGL.this.mode][(i
                                                                    * 2) + 1] / ePSXeViewGL.this.mHeight,
                                                            (ePSXeViewGL.this.padSizeScreenLan[ePSXeViewGL.this.mode][(i
                                                                    * 2) + 0]
                                                                    * ePSXeViewGL.this.padScreenResize[ePSXeViewGL.this.mode][i])
                                                                    / ePSXeViewGL.this.mWidth,
                                                            (ePSXeViewGL.this.padSizeScreenLan[ePSXeViewGL.this.mode][(i
                                                                    * 2) + 1]
                                                                    * ePSXeViewGL.this.padScreenResize[ePSXeViewGL.this.mode][i])
                                                                    / ePSXeViewGL.this.mHeight,
                                                            this.textureRgnLan[i]);
                                                    this.batchLan[i].endBatch();
                                                }
                                            } else {
                                                this.batchLan[i].beginBatch(this.mTexLan);
                                                this.batchLan[i].drawSprite(
                                                        ePSXeViewGL.this.padOffScreenLan[ePSXeViewGL.this.mode][(i * 2)
                                                                + 0] / ePSXeViewGL.this.mWidth,
                                                        ePSXeViewGL.this.padOffScreenLan[ePSXeViewGL.this.mode][(i * 2)
                                                                + 1] / ePSXeViewGL.this.mHeight,
                                                        (ePSXeViewGL.this.padSizeScreenLan[ePSXeViewGL.this.mode][(i
                                                                * 2) + 0]
                                                                * ePSXeViewGL.this.padScreenResize[ePSXeViewGL.this.mode][i])
                                                                / ePSXeViewGL.this.mWidth,
                                                        (ePSXeViewGL.this.padSizeScreenLan[ePSXeViewGL.this.mode][(i
                                                                * 2) + 1]
                                                                * ePSXeViewGL.this.padScreenResize[ePSXeViewGL.this.mode][i])
                                                                / ePSXeViewGL.this.mHeight,
                                                        this.textureRgnLan[i]);
                                                this.batchLan[i].endBatch();
                                            }
                                        } else if (i == 0) {
                                            if (ePSXeViewGL.this.emu_pad_draw_mode[0] == 4) {
                                                this.batchLan[i].beginBatch(this.mTexLan);
                                                this.batchLan[i].drawSprite(
                                                        ePSXeViewGL.this.padOffScreenLan[ePSXeViewGL.this.mode][(i * 2)
                                                                + 0] / ePSXeViewGL.this.mWidth,
                                                        ePSXeViewGL.this.padOffScreenLan[ePSXeViewGL.this.mode][(i * 2)
                                                                + 1] / ePSXeViewGL.this.mHeight,
                                                        (ePSXeViewGL.this.padSizeScreenLan[ePSXeViewGL.this.mode][(i
                                                                * 2) + 0]
                                                                * ePSXeViewGL.this.padScreenResize[ePSXeViewGL.this.mode][i])
                                                                / ePSXeViewGL.this.mWidth,
                                                        (ePSXeViewGL.this.padSizeScreenLan[ePSXeViewGL.this.mode][(i
                                                                * 2) + 1]
                                                                * ePSXeViewGL.this.padScreenResize[ePSXeViewGL.this.mode][i])
                                                                / ePSXeViewGL.this.mHeight,
                                                        this.textureRgnLan[i]);
                                                this.batchLan[i].endBatch();
                                            }
                                            if (ePSXeViewGL.this.dpadskin == 1) {
                                                float sx = ePSXeViewGL.this.padSizeScreenLan[ePSXeViewGL.this.mode][(i
                                                        * 2) + 0]
                                                        * ePSXeViewGL.this.padScreenResize[ePSXeViewGL.this.mode][i];
                                                float sy = ePSXeViewGL.this.padSizeScreenLan[ePSXeViewGL.this.mode][(i
                                                        * 2) + 1]
                                                        * ePSXeViewGL.this.padScreenResize[ePSXeViewGL.this.mode][i];
                                                float ox = ePSXeViewGL.this.padOffScreenLan[ePSXeViewGL.this.mode][(i
                                                        * 2) + 0] - (sx / 2.0f);
                                                float oy = ePSXeViewGL.this.padOffScreenLan[ePSXeViewGL.this.mode][(i
                                                        * 2) + 1] - (sy / 2.0f);
                                                for (int p = 0; p < 4; p++) {
                                                    if (ePSXeViewGL.this.isDpadTouchActive
                                                            && ePSXeViewGL.this.animationButtonIndex != -1) {
                                                        int buttonMapping = -1;
                                                        if (ePSXeViewGL.this.animationButtonIndex == 12) {
                                                            buttonMapping = 0;
                                                        } else if (ePSXeViewGL.this.animationButtonIndex == 13) {
                                                            buttonMapping = 1;
                                                        } else if (ePSXeViewGL.this.animationButtonIndex == 14) {
                                                            buttonMapping = 2;
                                                        } else if (ePSXeViewGL.this.animationButtonIndex == 15) {
                                                            buttonMapping = 3;
                                                        }

                                                        if (buttonMapping == p) {
                                                            this.batchLanDpad[p].beginBatch(this.mTexLan);
                                                            this.batchLanDpad[p].drawSprite(
                                                                    ((this.offDpadX[p] * sx) + ox)
                                                                            / ePSXeViewGL.this.mWidth,
                                                                    ((this.offDpadY[p] * sy) + oy)
                                                                            / ePSXeViewGL.this.mHeight,
                                                                    ((this.sizeDpadX[p] * sx) / ePSXeViewGL.this.mWidth)
                                                                            * ePSXeViewGL.this.buttonMag,
                                                                    ((this.sizeDpadY[p] * sy)
                                                                            / ePSXeViewGL.this.mHeight)
                                                                            * ePSXeViewGL.this.buttonMag,
                                                                    this.textureRgnLanDpad[p]);
                                                            this.batchLanDpad[p].endBatch();
                                                        } else {
                                                            this.batchLanDpad[p].beginBatch(this.mTexLan);
                                                            this.batchLanDpad[p].drawSprite(
                                                                    ((this.offDpadX[p] * sx) + ox)
                                                                            / ePSXeViewGL.this.mWidth,
                                                                    ((this.offDpadY[p] * sy) + oy)
                                                                            / ePSXeViewGL.this.mHeight,
                                                                    (this.sizeDpadX[p] * sx) / ePSXeViewGL.this.mWidth,
                                                                    (this.sizeDpadY[p] * sy) / ePSXeViewGL.this.mHeight,
                                                                    this.textureRgnLanDpad[p]);
                                                            this.batchLanDpad[p].endBatch();
                                                        }
                                                    } else {
                                                        if ((ePSXeViewGL.this.statebuttons & (4096 << p)) == 0) {
                                                            if (ePSXeViewGL.this.emu_pad_draw_mode[0] != 4) {
                                                                this.batchLanDpad[p].beginBatch(this.mTexLan);
                                                                this.batchLanDpad[p].drawSprite(
                                                                        ((this.offDpadX[p] * sx) + ox)
                                                                                / ePSXeViewGL.this.mWidth,
                                                                        ((this.offDpadY[p] * sy) + oy)
                                                                                / ePSXeViewGL.this.mHeight,
                                                                        (this.sizeDpadX[p] * sx)
                                                                                / ePSXeViewGL.this.mWidth,
                                                                        (this.sizeDpadY[p] * sy)
                                                                                / ePSXeViewGL.this.mHeight,
                                                                        this.textureRgnLanDpad[p]);
                                                                this.batchLanDpad[p].endBatch();
                                                            }
                                                        } else {
                                                            this.batchLanDpad[p].beginBatch(this.mTexLan);
                                                            this.batchLanDpad[p].drawSprite(
                                                                    ((this.offDpadX[p] * sx) + ox)
                                                                            / ePSXeViewGL.this.mWidth,
                                                                    ((this.offDpadY[p] * sy) + oy)
                                                                            / ePSXeViewGL.this.mHeight,
                                                                    ((this.sizeDpadX[p] * sx) / ePSXeViewGL.this.mWidth)
                                                                            * ePSXeViewGL.this.buttonMag,
                                                                    ((this.sizeDpadY[p] * sy)
                                                                            / ePSXeViewGL.this.mHeight)
                                                                            * ePSXeViewGL.this.buttonMag,
                                                                    this.textureRgnLanDpad[p]);
                                                            this.batchLanDpad[p].endBatch();
                                                        }
                                                    }
                                                }
                                            } else {
                                                this.batchLan[i].beginBatch(this.mTexLan);
                                                this.batchLan[i].drawSprite(
                                                        ePSXeViewGL.this.padOffScreenLan[ePSXeViewGL.this.mode][(i * 2)
                                                                + 0] / ePSXeViewGL.this.mWidth,
                                                        ePSXeViewGL.this.padOffScreenLan[ePSXeViewGL.this.mode][(i * 2)
                                                                + 1] / ePSXeViewGL.this.mHeight,
                                                        (ePSXeViewGL.this.padSizeScreenLan[ePSXeViewGL.this.mode][(i
                                                                * 2) + 0]
                                                                * ePSXeViewGL.this.padScreenResize[ePSXeViewGL.this.mode][i])
                                                                / ePSXeViewGL.this.mWidth,
                                                        (ePSXeViewGL.this.padSizeScreenLan[ePSXeViewGL.this.mode][(i
                                                                * 2) + 1]
                                                                * ePSXeViewGL.this.padScreenResize[ePSXeViewGL.this.mode][i])
                                                                / ePSXeViewGL.this.mHeight,
                                                        this.textureRgnLan[i]);
                                                this.batchLan[i].endBatch();
                                            }
                                        } else {
                                            if (ePSXeViewGL.this.emu_pad_draw_mode[0] == 4) {
                                                this.batchLan[i].beginBatch(this.mTexLan);
                                                this.batchLan[i].drawSprite(
                                                        ePSXeViewGL.this.padOffScreenLan[ePSXeViewGL.this.mode][(i * 2)
                                                                + 0] / ePSXeViewGL.this.mWidth,
                                                        ePSXeViewGL.this.padOffScreenLan[ePSXeViewGL.this.mode][(i * 2)
                                                                + 1] / ePSXeViewGL.this.mHeight,
                                                        (ePSXeViewGL.this.padSizeScreenLan[ePSXeViewGL.this.mode][(i
                                                                * 2) + 0]
                                                                * ePSXeViewGL.this.padScreenResize[ePSXeViewGL.this.mode][i])
                                                                / ePSXeViewGL.this.mWidth,
                                                        (ePSXeViewGL.this.padSizeScreenLan[ePSXeViewGL.this.mode][(i
                                                                * 2) + 1]
                                                                * ePSXeViewGL.this.padScreenResize[ePSXeViewGL.this.mode][i])
                                                                / ePSXeViewGL.this.mHeight,
                                                        this.textureRgnLan[i]);
                                                this.batchLan[i].endBatch();
                                            }
                                            float sx2 = ePSXeViewGL.this.padSizeScreenLan[ePSXeViewGL.this.mode][(i * 2)
                                                    + 0] * ePSXeViewGL.this.padScreenResize[ePSXeViewGL.this.mode][i];
                                            float sy2 = ePSXeViewGL.this.padSizeScreenLan[ePSXeViewGL.this.mode][(i * 2)
                                                    + 1] * ePSXeViewGL.this.padScreenResize[ePSXeViewGL.this.mode][i];
                                            // Исправляем координаты для правильного центрирования action buttons
                                            float centerX = ePSXeViewGL.this.padOffScreenLan[ePSXeViewGL.this.mode][(i
                                                    * 2) + 0];
                                            float centerY = ePSXeViewGL.this.padOffScreenLan[ePSXeViewGL.this.mode][(i
                                                    * 2) + 1];
                                            float ox2 = centerX - (sx2 / 2.0f);
                                            float oy2 = centerY - (sy2 / 2.0f);

                                            for (int p2 = 0; p2 < 4; p2++) {
                                                // Вычисляем правильные позиции для каждой action кнопки
                                                float buttonCenterX = (this.offActionX[p2] * sx2) + ox2;
                                                float buttonCenterY = (this.offActionY[p2] * sy2) + oy2;
                                                float buttonWidth = this.sizeActionX[p2] * sx2;
                                                float buttonHeight = this.sizeActionY[p2] * sy2;

                                                if ((ePSXeViewGL.this.statebuttons & (16 << p2)) == 0) {
                                                    if (ePSXeViewGL.this.emu_pad_draw_mode[0] != 4) {
                                                        this.batchLanAction[p2].beginBatch(this.mTexLan);
                                                        this.batchLanAction[p2].drawSprite(
                                                                buttonCenterX / ePSXeViewGL.this.mWidth,
                                                                buttonCenterY / ePSXeViewGL.this.mHeight,
                                                                buttonWidth / ePSXeViewGL.this.mWidth,
                                                                buttonHeight / ePSXeViewGL.this.mHeight,
                                                                this.textureRgnLanAction[p2]);
                                                        this.batchLanAction[p2].endBatch();
                                                    }
                                                } else {
                                                    this.batchLanAction[p2].beginBatch(this.mTexLan);
                                                    this.batchLanAction[p2].drawSprite(
                                                            buttonCenterX / ePSXeViewGL.this.mWidth,
                                                            buttonCenterY / ePSXeViewGL.this.mHeight,
                                                            (buttonWidth * ePSXeViewGL.this.buttonMag)
                                                                    / ePSXeViewGL.this.mWidth,
                                                            (buttonHeight * ePSXeViewGL.this.buttonMag)
                                                                    / ePSXeViewGL.this.mHeight,
                                                            this.textureRgnLanAction[p2]);
                                                    this.batchLanAction[p2].endBatch();
                                                }
                                            }
                                        }
                                        if (i == 11) {
                                            // 1) Центр круга левого стика (виртуальные пиксели):
                                            float cx_px = (virtualPadPos[20][0] + virtualPadPos[20][2]) * 0.5f;
                                            float cy_px = (virtualPadPos[20][1] + virtualPadPos[20][3]) * 0.5f;

                                            // 2) Размер круга стика в пикселях — по тому же, что вы рисовали в
                                            // touch-коде
                                            // padSizeScreenLan[mode][26] и [27] — «половина» вашего nub,
                                            // умноженная padScreenResize; *2 превращает в полный диаметр.
                                            float w_px = padSizeScreenLan[mode][26] * padScreenResize[mode][11] * 2.0f;
                                            float h_px = padSizeScreenLan[mode][27] * padScreenResize[mode][11] * 2.0f;

                                            // 3) Нормализуем центр и размеры в [0..1] для drawSprite
                                            float x_n = (cx_px - w_px * 0.5f) / (float) mWidth;
                                            float y_n = (cy_px - h_px * 0.5f) / (float) mHeight;
                                            float w_n = w_px / (float) mWidth;
                                            float h_n = h_px / (float) mHeight;

                                            // 4) Логируем, чтобы убедиться, что все в [0..1]
                                            Log.d("DRAW_NUB", String.format(
                                                    "i=11 x_n=%.3f y_n=%.3f w_n=%.3f h_n=%.3f",
                                                    x_n, y_n, w_n, h_n));

                                            // 5) Рисуем nub строго по центру круга
                                            batchLan[i].beginBatch(mTexLan);
                                            batchLan[i].drawSprite(
                                                    x_n, y_n, // левый-нижний угол (нормализовано)
                                                    w_n, h_n, // размеры (нормализовано)
                                                    textureRgnLan[i]);
                                            batchLan[i].endBatch();

                                            // 6) Прекращаем старую логику для i==11
                                            continue;
                                        } else if (i == 12) {
                                            this.batchLan[i].beginBatch(this.mTexLan);
                                            this.batchLan[i].drawSprite(
                                                    (float) ePSXeViewGL.this.analog_values[0][(i - 11) * 2]
                                                            / ePSXeViewGL.this.mWidth,
                                                    (float) ePSXeViewGL.this.analog_values[0][((i - 11) * 2) + 1]
                                                            / ePSXeViewGL.this.mHeight,
                                                    ((ePSXeViewGL.this.padSizeScreenLan[ePSXeViewGL.this.mode][26]
                                                            * ePSXeViewGL.this.padScreenResize[ePSXeViewGL.this.mode][i])
                                                            * 2.0f) / ePSXeViewGL.this.mWidth,
                                                    ((ePSXeViewGL.this.padSizeScreenLan[ePSXeViewGL.this.mode][27]
                                                            * ePSXeViewGL.this.padScreenResize[ePSXeViewGL.this.mode][i])
                                                            * 2.0f) / ePSXeViewGL.this.mHeight,
                                                    this.textureRgnLan[13]);
                                            this.batchLan[i].endBatch();
                                        }
                                    }
                                }
                                GLES20.glDisable(3042);
                                GLES20.glBindTexture(3553, this.mTexExtra);
                                GLES20.glUseProgram(this.mProgram);
                                GLES20.glEnable(3042);
                                if (ePSXeViewGL.this.padScreenExtraEnabled == 1 && !ePSXeViewGL.this.hidePad) {
                                    for (int i2 = 14; i2 < 20; i2++) {
                                        if (ePSXeViewGL.this.padScreenStatus[ePSXeViewGL.this.mode][i2] == 1) {
                                            if (ePSXeViewGL.this.padScreenExtra[i2 - 14] >= 20
                                                    && ePSXeViewGL.this.stickyButton[ePSXeViewGL.this.padScreenExtra[i2
                                                            - 14] - 20] == 1) {
                                                int j = ePSXeViewGL.this.padScreenExtra[i2 - 14];
                                                this.batchLan[i2].beginBatch(this.mTexExtra);
                                                this.batchLan[i2].drawSprite(
                                                        ePSXeViewGL.this.padOffScreenLan[ePSXeViewGL.this.mode][(i2 * 2)
                                                                + 0] / ePSXeViewGL.this.mWidth,
                                                        ePSXeViewGL.this.padOffScreenLan[ePSXeViewGL.this.mode][(i2 * 2)
                                                                + 1] / ePSXeViewGL.this.mHeight,
                                                        (ePSXeViewGL.this.padSizeScreenLan[ePSXeViewGL.this.mode][(i2
                                                                * 2) + 0]
                                                                * ePSXeViewGL.this.padScreenResize[ePSXeViewGL.this.mode][i2])
                                                                / ePSXeViewGL.this.mWidth,
                                                        (ePSXeViewGL.this.padSizeScreenLan[ePSXeViewGL.this.mode][(i2
                                                                * 2) + 1]
                                                                * ePSXeViewGL.this.padScreenResize[ePSXeViewGL.this.mode][i2])
                                                                / ePSXeViewGL.this.mHeight,
                                                        this.textureRgnLan[j]);
                                                this.batchLan[i2].endBatch();
                                            } else {
                                                this.batchLan[i2].beginBatch(this.mTexExtra);
                                                this.batchLan[i2].drawSprite(
                                                        ePSXeViewGL.this.padOffScreenLan[ePSXeViewGL.this.mode][(i2 * 2)
                                                                + 0] / ePSXeViewGL.this.mWidth,
                                                        ePSXeViewGL.this.padOffScreenLan[ePSXeViewGL.this.mode][(i2 * 2)
                                                                + 1] / ePSXeViewGL.this.mHeight,
                                                        (ePSXeViewGL.this.padSizeScreenLan[ePSXeViewGL.this.mode][(i2
                                                                * 2) + 0]
                                                                * ePSXeViewGL.this.padScreenResize[ePSXeViewGL.this.mode][i2])
                                                                / ePSXeViewGL.this.mWidth,
                                                        (ePSXeViewGL.this.padSizeScreenLan[ePSXeViewGL.this.mode][(i2
                                                                * 2) + 1]
                                                                * ePSXeViewGL.this.padScreenResize[ePSXeViewGL.this.mode][i2])
                                                                / ePSXeViewGL.this.mHeight,
                                                        this.textureRgnLan[i2]);
                                                this.batchLan[i2].endBatch();
                                            }
                                        }
                                    }
                                }
                                GLES20.glDisable(3042);
                            }
                            if (ePSXeViewGL.this.emu_player_mode != 10 || ePSXeViewGL.this.emu_split_mode != 0
                                    || ePSXeViewGL.this.emu_pad_draw_mode[0] == 2) {
                                if ((ePSXeViewGL.this.emu_player_mode == 10
                                        && ePSXeViewGL.this.emu_pad_draw_mode[0] != 2
                                        && ePSXeViewGL.this.emu_split_mode == 1)
                                        || ePSXeViewGL.this.emu_split_mode == 2) {
                                    if (!ePSXeViewGL.this.hidePad) {
                                        GLES20.glBindTexture(3553, this.mTexLan);
                                    }
                                    GLES20.glUseProgram(this.mProgram);
                                    for (int i3 = 0; i3 < 8; i3++) {
                                        this.batchLan[i3].beginBatch(this.mTexLan);
                                        this.batchLan[i3].drawSprite(
                                                ePSXeViewGL.this.padOffScreenLan2H[(i3 * 2) + 0]
                                                        / ePSXeViewGL.this.mWidth,
                                                ePSXeViewGL.this.padOffScreenLan2H[(i3 * 2) + 1]
                                                        / ePSXeViewGL.this.mHeight,
                                                ePSXeViewGL.this.padSizeScreenLan2H[(i3 * 2) + 0]
                                                        / ePSXeViewGL.this.mWidth,
                                                ePSXeViewGL.this.padSizeScreenLan2H[(i3 * 2) + 1]
                                                        / ePSXeViewGL.this.mHeight,
                                                this.textureRgnLan[i3]);
                                        this.batchLan[i3].endBatch();
                                    }
                                    for (int i4 = 0; i4 < 8; i4++) {
                                        this.batchLan[i4].beginBatch(this.mTexLan);
                                        this.batchLan[i4].drawSpriteSwap(
                                                (ePSXeViewGL.this.mWidth
                                                        - ePSXeViewGL.this.padOffScreenLan2H[(i4 * 2) + 0])
                                                        / ePSXeViewGL.this.mWidth,
                                                ((((float) ePSXeViewGL.this.mHeight / 2)
                                                        - ePSXeViewGL.this.padOffScreenLan2H[(i4 * 2) + 1])
                                                        + ((float) ePSXeViewGL.this.mHeight / 2))
                                                        / ePSXeViewGL.this.mHeight,
                                                ePSXeViewGL.this.padSizeScreenLan2H[(i4 * 2) + 0]
                                                        / ePSXeViewGL.this.mWidth,
                                                ePSXeViewGL.this.padSizeScreenLan2H[(i4 * 2) + 1]
                                                        / ePSXeViewGL.this.mHeight,
                                                this.textureRgnLan[i4]);
                                        this.batchLan[i4].endBatch();
                                    }
                                }
                            } else {
                                if (!ePSXeViewGL.this.hidePad) {
                                    GLES20.glBindTexture(3553, this.mTexLan);
                                }
                                GLES20.glUseProgram(this.mProgram);
                                for (int i5 = 0; i5 < 8; i5++) {
                                    this.batchLan[i5].beginBatch(this.mTexLan);
                                    this.batchLan[i5].drawSpriteRotate90(
                                            (ePSXeViewGL.this.mWidth - ePSXeViewGL.this.padOffScreenLan2V[(i5 * 2) + 1])
                                                    / ePSXeViewGL.this.mWidth,
                                            ePSXeViewGL.this.padOffScreenLan2V[(i5 * 2) + 0] / ePSXeViewGL.this.mHeight,
                                            ePSXeViewGL.this.padSizeScreenLan2V[(i5 * 2) + 1] / ePSXeViewGL.this.mWidth,
                                            ePSXeViewGL.this.padSizeScreenLan2V[(i5 * 2) + 0]
                                                    / ePSXeViewGL.this.mHeight,
                                            this.textureRgnLan[i5]);
                                    this.batchLan[i5].endBatch();
                                }
                                for (int i6 = 0; i6 < 8; i6++) {
                                    this.batchLan[i6].beginBatch(this.mTexLan);
                                    this.batchLan[i6].drawSpriteRotate270(
                                            ePSXeViewGL.this.padOffScreenLan2V[(i6 * 2) + 1] / ePSXeViewGL.this.mWidth,
                                            (ePSXeViewGL.this.mHeight
                                                    - ePSXeViewGL.this.padOffScreenLan2V[(i6 * 2) + 0])
                                                    / ePSXeViewGL.this.mHeight,
                                            ePSXeViewGL.this.padSizeScreenLan2V[(i6 * 2) + 1] / ePSXeViewGL.this.mWidth,
                                            ePSXeViewGL.this.padSizeScreenLan2V[(i6 * 2) + 0]
                                                    / ePSXeViewGL.this.mHeight,
                                            this.textureRgnLan[i6]);
                                    this.batchLan[i6].endBatch();
                                }
                            }
                        } else {
                            GLES20.glBindTexture(3553, this.mTexPor);
                            GLES20.glUseProgram(this.mProgram);
                            this.batchPor.beginBatch(this.mTexPor);
                            this.batchPor.drawSprite(ePSXeViewGL.this.padOffScreenPor[0] / ePSXeViewGL.this.mWidth,
                                    ePSXeViewGL.this.padOffScreenPor[1] / ePSXeViewGL.this.mHeight,
                                    ePSXeViewGL.this.padSizeScreenPor[0] / ePSXeViewGL.this.mWidth,
                                    ePSXeViewGL.this.padSizeScreenPor[1] / ePSXeViewGL.this.mHeight,
                                    this.textureRgnPor);
                            this.batchPor.endBatch();
                            if (ePSXeViewGL.this.padScreenExtraEnabled == 1 && !ePSXeViewGL.this.hidePad) {
                                GLES20.glBindTexture(3553, this.mTexExtra);
                                GLES20.glUseProgram(this.mProgram);
                                GLES20.glEnable(3042);
                                for (int i7 = 14; i7 < 20; i7++) {
                                    if (ePSXeViewGL.this.padScreenStatus[ePSXeViewGL.this.mode][i7] == 1) {
                                        if (ePSXeViewGL.this.padScreenExtra[i7 - 14] >= 20
                                                && ePSXeViewGL.this.stickyButton[ePSXeViewGL.this.padScreenExtra[i7
                                                        - 14] - 20] == 1) {
                                            int j2 = ePSXeViewGL.this.padScreenExtra[i7 - 14];
                                            this.batchLan[i7].beginBatch(this.mTexExtra);
                                            this.batchLan[i7].drawSprite(
                                                    ePSXeViewGL.this.padOffScreenPor[(i7 * 2) + 0]
                                                            / ePSXeViewGL.this.mWidth,
                                                    ePSXeViewGL.this.padOffScreenPor[(i7 * 2) + 1]
                                                            / ePSXeViewGL.this.mHeight,
                                                    ePSXeViewGL.this.padSizeScreenPor[(i7 * 2) + 0]
                                                            / ePSXeViewGL.this.mWidth,
                                                    ePSXeViewGL.this.padSizeScreenPor[(i7 * 2) + 1]
                                                            / ePSXeViewGL.this.mHeight,
                                                    this.textureRgnLan[j2]);
                                            this.batchLan[i7].endBatch();
                                        } else {
                                            this.batchLan[i7].beginBatch(this.mTexExtra);
                                            this.batchLan[i7].drawSprite(
                                                    ePSXeViewGL.this.padOffScreenPor[(i7 * 2) + 0]
                                                            / ePSXeViewGL.this.mWidth,
                                                    ePSXeViewGL.this.padOffScreenPor[(i7 * 2) + 1]
                                                            / ePSXeViewGL.this.mHeight,
                                                    ePSXeViewGL.this.padSizeScreenPor[(i7 * 2) + 0]
                                                            / ePSXeViewGL.this.mWidth,
                                                    ePSXeViewGL.this.padSizeScreenPor[(i7 * 2) + 1]
                                                            / ePSXeViewGL.this.mHeight,
                                                    this.textureRgnLan[i7]);
                                            this.batchLan[i7].endBatch();
                                        }
                                    }
                                }
                                GLES20.glDisable(3042);
                            }
                        }
                    } catch (Exception e) {
                    }
                    if (ePSXeViewGL.this.emu_enable_printfps == 1 && ePSXeViewGL.this.emu_player_mode == 1) {
                        drawFPS(gl);
                    }
                    if (!ePSXeViewGL.this.license) {
                        drawLicense(gl);
                    }
                    if (!ePSXeViewGL.this.biosmsg || ePSXeViewGL.this.biosVersionAdvise <= 0) {
                        if (!ePSXeViewGL.this.gprofile || ePSXeViewGL.this.gProfileAdvise <= 0) {
                            if (ePSXeViewGL.this.volumenAdvise > 0) {
                                drawVolumen(gl);
                                ePSXeViewGL.access$4410(ePSXeViewGL.this);
                                return;
                            }
                            return;
                        }
                        if (ePSXeViewGL.this.emu_verbose == 1) {
                            drawString(gl, "Loading custom game profile...");
                        }
                        ePSXeViewGL.access$4310(ePSXeViewGL.this);
                        return;
                    }
                    if (ePSXeViewGL.this.emu_verbose == 1) {
                        drawBiosMsg(gl);
                    }
                    ePSXeViewGL.access$4010(ePSXeViewGL.this);
                }
            }
        }

        private void resetPad1Values() {
            // Вычисляем базовое масштабирование только один раз
            calculateBasePadResize();
            applyBaseSizesToPads();
        }

        private void calculateBasePadResize() {
            if (ePSXeViewGL.this.emu_screen_orientation != 1) {
                // Ландшафтный режим - улучшенное масштабирование
                calculateLandscapePadScale();
            } else if (ePSXeViewGL.this.emu_portrait_skin == 1) {
                // Портретный режим - используем существующую логику
                ePSXeViewGL.this.padResize = ePSXeViewGL.this.mWidth / 562.0f;
            } else {
                // Портретный режим без скина
                ePSXeViewGL.this.padResize = 1.0f;
            }
        }

        private void applyBaseSizesToPads() {
            float[][] padSizeScreenLantmp = {
                    new float[] { 228.0f, 228.0f, 224.0f, 248.0f, 66.0f, 50.0f, 66.0f, 62.0f, 64.0f, 60.0f, 64.0f,
                            60.0f, 64.0f, 60.0f, 64.0f, 60.0f, 53.0f, 41.0f, 64.0f, 60.0f, 64.0f, 60.0f, 222.0f, 222.0f,
                            222.0f, 222.0f, 71.0f, 71.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f,
                            64.0f, 64.0f, 64.0f },
                    new float[] { 228.0f, 228.0f, 224.0f, 248.0f, 66.0f, 50.0f, 66.0f, 62.0f, 64.0f, 60.0f, 64.0f,
                            60.0f, 64.0f, 60.0f, 64.0f, 60.0f, 53.0f, 41.0f, 64.0f, 60.0f, 64.0f, 60.0f, 222.0f, 222.0f,
                            222.0f, 222.0f, 71.0f, 71.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f,
                            64.0f, 64.0f, 64.0f } };
            for (int i = 0; i < 20; i++) {
                ePSXeViewGL.this.padSizeScreenLan[ePSXeViewGL.this.mode][(i * 2)
                        + 0] = padSizeScreenLantmp[ePSXeViewGL.this.mode][(i * 2) + 0] * ePSXeViewGL.this.padResize;
                ePSXeViewGL.this.padSizeScreenLan[ePSXeViewGL.this.mode][(i * 2)
                        + 1] = padSizeScreenLantmp[ePSXeViewGL.this.mode][(i * 2) + 1] * ePSXeViewGL.this.padResize;
            }
            if (ePSXeViewGL.this.emu_screen_orientation == 1) {
                if (ePSXeViewGL.this.emu_portrait_skin == 1) {
                    float[][] padOffScreenLantmp = { new float[] { ePSXeViewGL.this.padSizeScreenLan[0][0] / 2.0f,
                            ePSXeViewGL.this.padSizeScreenLan[0][1] / 2.0f,
                            ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[0][2] / 2.0f),
                            ePSXeViewGL.this.padSizeScreenLan[0][3] / 2.0f,
                            (((float) ePSXeViewGL.this.mWidth / 2) - ePSXeViewGL.this.padSizeScreenLan[0][4]) - 30.0f,
                            ((float) ePSXeViewGL.this.mHeight / 2) - (ePSXeViewGL.this.padSizeScreenLan[0][5] / 2.0f),
                            ((float) ePSXeViewGL.this.mWidth / 2) + 30,
                            ((float) ePSXeViewGL.this.mHeight / 2) - (ePSXeViewGL.this.padSizeScreenLan[0][7] / 2.0f),
                            ePSXeViewGL.this.padSizeScreenLan[0][8] / 2.0f,
                            ((float) ePSXeViewGL.this.mHeight / 2) - (ePSXeViewGL.this.padSizeScreenLan[0][9] / 2.0f),
                            (ePSXeViewGL.this.padSizeScreenLan[0][10] / 2.0f) + ePSXeViewGL.this.padSizeScreenLan[0][8],
                            ((float) ePSXeViewGL.this.mHeight / 2) - (ePSXeViewGL.this.padSizeScreenLan[0][11] / 2.0f),
                            ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[0][12] / 2.0f),
                            ((float) ePSXeViewGL.this.mHeight / 2) - (ePSXeViewGL.this.padSizeScreenLan[0][13] / 2.0f),
                            (ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[0][14] / 2.0f))
                                    - ePSXeViewGL.this.padSizeScreenLan[0][12],
                            ((float) ePSXeViewGL.this.mHeight / 2) - (ePSXeViewGL.this.padSizeScreenLan[0][15] / 2.0f),
                            ((float) ePSXeViewGL.this.mWidth / 2) + 40 + ePSXeViewGL.this.padSizeScreenLan[0][6],
                            ((float) ePSXeViewGL.this.mHeight / 2) - (ePSXeViewGL.this.padSizeScreenLan[0][17] / 2.0f),
                            (ePSXeViewGL.this.padSizeScreenLan[0][18] / 2.0f) + ePSXeViewGL.this.padSizeScreenLan[0][10]
                                    + ePSXeViewGL.this.padSizeScreenLan[0][8],
                            ((float) ePSXeViewGL.this.mHeight / 2) - (ePSXeViewGL.this.padSizeScreenLan[0][19] / 2.0f),
                            ((ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[0][20] / 2.0f))
                                    - ePSXeViewGL.this.padSizeScreenLan[0][12])
                                    - ePSXeViewGL.this.padSizeScreenLan[0][14],
                            ((float) ePSXeViewGL.this.mHeight / 2) - (ePSXeViewGL.this.padSizeScreenLan[0][21] / 2.0f),
                            ePSXeViewGL.this.padSizeScreenLan[0][22] / 2.0f,
                            ePSXeViewGL.this.padSizeScreenLan[0][23] / 2.0f,
                            ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[0][24] / 2.0f),
                            ePSXeViewGL.this.padSizeScreenLan[0][25] / 2.0f, 0.0f, 0.0f,
                            ePSXeViewGL.this.padSizeScreenLan[0][28] / 2.0f,
                            ((float) ePSXeViewGL.this.mHeight / 3) - (ePSXeViewGL.this.padSizeScreenLan[0][29] / 2.0f),
                            (ePSXeViewGL.this.padSizeScreenLan[0][30] / 2.0f)
                                    + ePSXeViewGL.this.padSizeScreenLan[0][28],
                            ((float) ePSXeViewGL.this.mHeight / 3) - (ePSXeViewGL.this.padSizeScreenLan[0][31] / 2.0f),
                            (ePSXeViewGL.this.padSizeScreenLan[0][32] / 2.0f) + ePSXeViewGL.this.padSizeScreenLan[0][28]
                                    + ePSXeViewGL.this.padSizeScreenLan[0][30],
                            ((float) ePSXeViewGL.this.mHeight / 3) - (ePSXeViewGL.this.padSizeScreenLan[0][33] / 2.0f),
                            ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[0][34] / 2.0f),
                            ((float) ePSXeViewGL.this.mHeight / 3) - (ePSXeViewGL.this.padSizeScreenLan[0][35] / 2.0f),
                            (ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[0][36] / 2.0f))
                                    - ePSXeViewGL.this.padSizeScreenLan[0][34],
                            ((float) ePSXeViewGL.this.mHeight / 3) - (ePSXeViewGL.this.padSizeScreenLan[0][37] / 2.0f),
                            ((ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[0][38] / 2.0f))
                                    - ePSXeViewGL.this.padSizeScreenLan[0][34])
                                    - ePSXeViewGL.this.padSizeScreenLan[0][36],
                            ((float) ePSXeViewGL.this.mHeight / 3)
                                    - (ePSXeViewGL.this.padSizeScreenLan[0][39] / 2.0f) },
                            new float[] {
                                    ((float) ePSXeViewGL.this.mWidth / 2)
                                            - ((ePSXeViewGL.this.padSizeScreenLan[1][0] / 2.0f) * 0.76f),
                                    (float) ePSXeViewGL.this.mHeight / 4,
                                    ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[1][2] / 2.0f),
                                    ePSXeViewGL.this.padSizeScreenLan[1][3] / 2.0f,
                                    (((float) ePSXeViewGL.this.mWidth / 2) - ePSXeViewGL.this.padSizeScreenLan[1][4])
                                            - 30.0f,
                                    ((float) ePSXeViewGL.this.mHeight / 2)
                                            - (ePSXeViewGL.this.padSizeScreenLan[1][5] / 2.0f),
                                    ((float) ePSXeViewGL.this.mWidth / 2) + 30,
                                    ((float) ePSXeViewGL.this.mHeight / 2)
                                            - (ePSXeViewGL.this.padSizeScreenLan[1][7] / 2.0f),
                                    ePSXeViewGL.this.padSizeScreenLan[1][8] / 2.0f,
                                    ((float) ePSXeViewGL.this.mHeight / 2)
                                            - (ePSXeViewGL.this.padSizeScreenLan[1][9] / 2.0f),
                                    (ePSXeViewGL.this.padSizeScreenLan[1][10] / 2.0f)
                                            + ePSXeViewGL.this.padSizeScreenLan[0][8],
                                    ((float) ePSXeViewGL.this.mHeight / 2)
                                            - (ePSXeViewGL.this.padSizeScreenLan[1][11] / 2.0f),
                                    ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[1][12] / 2.0f),
                                    ((float) ePSXeViewGL.this.mHeight / 2)
                                            - (ePSXeViewGL.this.padSizeScreenLan[1][13] / 2.0f),
                                    (ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[1][14] / 2.0f))
                                            - ePSXeViewGL.this.padSizeScreenLan[1][12],
                                    ((float) ePSXeViewGL.this.mHeight / 2)
                                            - (ePSXeViewGL.this.padSizeScreenLan[1][15] / 2.0f),
                                    ((float) ePSXeViewGL.this.mWidth / 2) + 40
                                            + ePSXeViewGL.this.padSizeScreenLan[1][6],
                                    ((float) ePSXeViewGL.this.mHeight / 2)
                                            - (ePSXeViewGL.this.padSizeScreenLan[1][17] / 2.0f),
                                    (ePSXeViewGL.this.padSizeScreenLan[1][18] / 2.0f)
                                            + ePSXeViewGL.this.padSizeScreenLan[1][10]
                                            + ePSXeViewGL.this.padSizeScreenLan[1][8],
                                    ((float) ePSXeViewGL.this.mHeight / 2)
                                            - (ePSXeViewGL.this.padSizeScreenLan[1][19] / 2.0f),
                                    ((ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[1][20] / 2.0f))
                                            - ePSXeViewGL.this.padSizeScreenLan[1][12])
                                            - ePSXeViewGL.this.padSizeScreenLan[1][14],
                                    ((float) ePSXeViewGL.this.mHeight / 2)
                                            - (ePSXeViewGL.this.padSizeScreenLan[1][21] / 2.0f),
                                    ePSXeViewGL.this.padSizeScreenLan[1][22] / 2.0f,
                                    ePSXeViewGL.this.padSizeScreenLan[1][23] / 2.0f,
                                    ((float) ePSXeViewGL.this.mWidth / 2)
                                            + ((ePSXeViewGL.this.padSizeScreenLan[1][24] / 2.0f) * 0.76f),
                                    (float) ePSXeViewGL.this.mHeight / 4, 0.0f, 0.0f,
                                    ePSXeViewGL.this.padSizeScreenLan[1][28] / 2.0f,
                                    ((float) ePSXeViewGL.this.mHeight / 3)
                                            - (ePSXeViewGL.this.padSizeScreenLan[1][29] / 2.0f),
                                    (ePSXeViewGL.this.padSizeScreenLan[1][30] / 2.0f)
                                            + ePSXeViewGL.this.padSizeScreenLan[1][28],
                                    ((float) ePSXeViewGL.this.mHeight / 3)
                                            - (ePSXeViewGL.this.padSizeScreenLan[1][31] / 2.0f),
                                    (ePSXeViewGL.this.padSizeScreenLan[1][32] / 2.0f)
                                            + ePSXeViewGL.this.padSizeScreenLan[1][28]
                                            + ePSXeViewGL.this.padSizeScreenLan[1][30],
                                    ((float) ePSXeViewGL.this.mHeight / 3)
                                            - (ePSXeViewGL.this.padSizeScreenLan[1][33] / 2.0f),
                                    ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[1][34] / 2.0f),
                                    ((float) ePSXeViewGL.this.mHeight / 3)
                                            - (ePSXeViewGL.this.padSizeScreenLan[1][35] / 2.0f),
                                    (ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[1][36] / 2.0f))
                                            - ePSXeViewGL.this.padSizeScreenLan[1][34],
                                    ((float) ePSXeViewGL.this.mHeight / 3)
                                            - (ePSXeViewGL.this.padSizeScreenLan[1][37] / 2.0f),
                                    ((ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[1][38] / 2.0f))
                                            - ePSXeViewGL.this.padSizeScreenLan[1][34])
                                            - ePSXeViewGL.this.padSizeScreenLan[1][36],
                                    ((float) ePSXeViewGL.this.mHeight / 3)
                                            - (ePSXeViewGL.this.padSizeScreenLan[1][39] / 2.0f) } };
                    for (int i2 = 0; i2 < 20; i2++) {
                        ePSXeViewGL.this.padOffScreenLan[ePSXeViewGL.this.mode][(i2 * 2)
                                + 0] = padOffScreenLantmp[ePSXeViewGL.this.mode][(i2 * 2) + 0];
                        ePSXeViewGL.this.padOffScreenLan[ePSXeViewGL.this.mode][(i2 * 2)
                                + 1] = padOffScreenLantmp[ePSXeViewGL.this.mode][(i2 * 2) + 1];
                    }
                    return;
                }
                return;
            }
            float[][] padOffScreenLantmp2 = {
                    new float[] { ePSXeViewGL.this.padSizeScreenLan[0][0] / 2.0f,
                            ePSXeViewGL.this.padSizeScreenLan[0][1] / 2.0f,
                            ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[0][2] / 2.0f),
                            ePSXeViewGL.this.padSizeScreenLan[0][3] / 2.0f,
                            (((float) ePSXeViewGL.this.mWidth / 2) - ePSXeViewGL.this.padSizeScreenLan[0][4]) - 30.0f,
                            ePSXeViewGL.this.padSizeScreenLan[0][5] / 2.0f, ((float) ePSXeViewGL.this.mWidth / 2) + 30,
                            ePSXeViewGL.this.padSizeScreenLan[0][7] / 2.0f,
                            ePSXeViewGL.this.padSizeScreenLan[0][8] / 2.0f,
                            ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan[0][9] / 2.0f),
                            (ePSXeViewGL.this.padSizeScreenLan[0][10] / 2.0f) + ePSXeViewGL.this.padSizeScreenLan[0][8],
                            ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan[0][11] / 2.0f),
                            ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[0][12] / 2.0f),
                            ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan[0][13] / 2.0f),
                            (ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[0][14] / 2.0f))
                                    - ePSXeViewGL.this.padSizeScreenLan[0][12],
                            ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan[0][15] / 2.0f),
                            ((float) ePSXeViewGL.this.mWidth / 2) + 40 + ePSXeViewGL.this.padSizeScreenLan[0][6],
                            ePSXeViewGL.this.padSizeScreenLan[0][17] / 2.0f,
                            (ePSXeViewGL.this.padSizeScreenLan[0][18] / 2.0f) + ePSXeViewGL.this.padSizeScreenLan[0][10]
                                    + ePSXeViewGL.this.padSizeScreenLan[0][8],
                            ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan[0][19] / 2.0f),
                            ((ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[0][20] / 2.0f))
                                    - ePSXeViewGL.this.padSizeScreenLan[0][12])
                                    - ePSXeViewGL.this.padSizeScreenLan[0][14],
                            ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan[0][21] / 2.0f),
                            ePSXeViewGL.this.padSizeScreenLan[0][22] / 2.0f,
                            ePSXeViewGL.this.padSizeScreenLan[0][23] / 2.0f,
                            ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[0][24] / 2.0f),
                            ePSXeViewGL.this.padSizeScreenLan[0][25] / 2.0f, 0.0f, 0.0f,
                            ePSXeViewGL.this.padSizeScreenLan[0][28] / 2.0f,
                            (ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan[0][29] / 2.0f))
                                    - (ePSXeViewGL.this.padSizeScreenLan[0][10] * 2.0f),
                            (ePSXeViewGL.this.padSizeScreenLan[0][30] / 2.0f)
                                    + ePSXeViewGL.this.padSizeScreenLan[0][28],
                            (ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan[0][31] / 2.0f))
                                    - (ePSXeViewGL.this.padSizeScreenLan[0][10] * 2.0f),
                            (ePSXeViewGL.this.padSizeScreenLan[0][32] / 2.0f) + ePSXeViewGL.this.padSizeScreenLan[0][28]
                                    + ePSXeViewGL.this.padSizeScreenLan[0][30],
                            (ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan[0][33] / 2.0f))
                                    - (ePSXeViewGL.this.padSizeScreenLan[0][10] * 2.0f),
                            ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[0][34] / 2.0f),
                            (ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan[0][35] / 2.0f))
                                    - (ePSXeViewGL.this.padSizeScreenLan[0][10] * 2.0f),
                            (ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[0][36] / 2.0f))
                                    - ePSXeViewGL.this.padSizeScreenLan[0][34],
                            (ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan[0][37] / 2.0f))
                                    - (ePSXeViewGL.this.padSizeScreenLan[0][10] * 2.0f),
                            ((ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[0][38] / 2.0f))
                                    - ePSXeViewGL.this.padSizeScreenLan[0][34])
                                    - ePSXeViewGL.this.padSizeScreenLan[0][36],
                            (ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan[0][39] / 2.0f))
                                    - (ePSXeViewGL.this.padSizeScreenLan[0][10] * 2.0f) },
                    new float[] {
                            ((float) ePSXeViewGL.this.mWidth / 2)
                                    - ((ePSXeViewGL.this.padSizeScreenLan[1][0] / 2.0f) * 0.76f),
                            (float) ePSXeViewGL.this.mHeight / 2,
                            ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[1][2] / 2.0f),
                            ePSXeViewGL.this.padSizeScreenLan[1][3] / 2.0f,
                            (((float) ePSXeViewGL.this.mWidth / 2) - ePSXeViewGL.this.padSizeScreenLan[1][4]) - 30.0f,
                            ePSXeViewGL.this.padSizeScreenLan[1][5] / 2.0f, ((float) ePSXeViewGL.this.mWidth / 2) + 30,
                            ePSXeViewGL.this.padSizeScreenLan[1][7] / 2.0f,
                            ePSXeViewGL.this.padSizeScreenLan[1][8] / 2.0f,
                            ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan[1][9] / 2.0f),
                            (ePSXeViewGL.this.padSizeScreenLan[1][10] / 2.0f) + ePSXeViewGL.this.padSizeScreenLan[1][8],
                            ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan[1][11] / 2.0f),
                            ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[1][12] / 2.0f),
                            ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan[1][13] / 2.0f),
                            (ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[1][14] / 2.0f))
                                    - ePSXeViewGL.this.padSizeScreenLan[1][12],
                            ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan[1][15] / 2.0f),
                            ((float) ePSXeViewGL.this.mWidth / 2) + 40 + ePSXeViewGL.this.padSizeScreenLan[1][6],
                            ePSXeViewGL.this.padSizeScreenLan[1][17] / 2.0f,
                            (ePSXeViewGL.this.padSizeScreenLan[1][18] / 2.0f) + ePSXeViewGL.this.padSizeScreenLan[1][10]
                                    + ePSXeViewGL.this.padSizeScreenLan[1][8],
                            ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan[1][19] / 2.0f),
                            ((ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[1][20] / 2.0f))
                                    - ePSXeViewGL.this.padSizeScreenLan[1][12])
                                    - ePSXeViewGL.this.padSizeScreenLan[1][14],
                            ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan[1][21] / 2.0f),
                            ePSXeViewGL.this.padSizeScreenLan[1][22] / 2.0f,
                            ePSXeViewGL.this.padSizeScreenLan[1][23] / 2.0f,
                            ((float) ePSXeViewGL.this.mWidth / 2)
                                    + ((ePSXeViewGL.this.padSizeScreenLan[1][24] / 2.0f) * 0.76f),
                            (float) ePSXeViewGL.this.mHeight / 2, 0.0f, 0.0f,
                            ePSXeViewGL.this.padSizeScreenLan[1][28] / 2.0f,
                            (ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan[1][29] / 2.0f))
                                    - (ePSXeViewGL.this.padSizeScreenLan[1][10] * 2.0f),
                            (ePSXeViewGL.this.padSizeScreenLan[1][30] / 2.0f)
                                    + ePSXeViewGL.this.padSizeScreenLan[1][28],
                            (ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan[1][31] / 2.0f))
                                    - (ePSXeViewGL.this.padSizeScreenLan[1][10] * 2.0f),
                            (ePSXeViewGL.this.padSizeScreenLan[1][32] / 2.0f) + ePSXeViewGL.this.padSizeScreenLan[1][28]
                                    + ePSXeViewGL.this.padSizeScreenLan[1][30],
                            (ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan[1][33] / 2.0f))
                                    - (ePSXeViewGL.this.padSizeScreenLan[1][10] * 2.0f),
                            ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[1][34] / 2.0f),
                            (ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan[1][35] / 2.0f))
                                    - (ePSXeViewGL.this.padSizeScreenLan[1][10] * 2.0f),
                            (ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[1][36] / 2.0f))
                                    - ePSXeViewGL.this.padSizeScreenLan[1][34],
                            (ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan[1][37] / 2.0f))
                                    - (ePSXeViewGL.this.padSizeScreenLan[1][10] * 2.0f),
                            ((ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[1][38] / 2.0f))
                                    - ePSXeViewGL.this.padSizeScreenLan[1][34])
                                    - ePSXeViewGL.this.padSizeScreenLan[1][36],
                            (ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan[1][39] / 2.0f))
                                    - (ePSXeViewGL.this.padSizeScreenLan[1][10] * 2.0f) } };
            for (int i3 = 0; i3 < 20; i3++) {
                ePSXeViewGL.this.padOffScreenLan[ePSXeViewGL.this.mode][(i3 * 2)
                        + 0] = padOffScreenLantmp2[ePSXeViewGL.this.mode][(i3 * 2) + 0];
                ePSXeViewGL.this.padOffScreenLan[ePSXeViewGL.this.mode][(i3 * 2)
                        + 1] = padOffScreenLantmp2[ePSXeViewGL.this.mode][(i3 * 2) + 1];
            }
        }

        public void resetPadAllValues() {
            // Используем единую функцию для вычисления базового масштабирования
            calculateBasePadResize();
            float[] padSizeScreenPortmp = { ePSXeViewGL.this.mWidth, (float) ePSXeViewGL.this.mHeight / 2, 0.0f, 0.0f,
                    0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
                    0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, (ePSXeViewGL.this.mWidth * 64f) / 480,
                    (((float) ePSXeViewGL.this.mHeight / 2) * 64) / 400, (ePSXeViewGL.this.mWidth * 64f) / 480,
                    (((float) ePSXeViewGL.this.mHeight / 2) * 64) / 400, (ePSXeViewGL.this.mWidth * 64f) / 480,
                    (((float) ePSXeViewGL.this.mHeight / 2) * 64) / 400, (ePSXeViewGL.this.mWidth * 64f) / 480,
                    (((float) ePSXeViewGL.this.mHeight / 2) * 64) / 400, (ePSXeViewGL.this.mWidth * 64f) / 480,
                    (((float) ePSXeViewGL.this.mHeight / 2) * 64) / 400, (ePSXeViewGL.this.mWidth * 64f) / 480,
                    (((float) ePSXeViewGL.this.mHeight / 2) * 64) / 400 };
            float[] padOffScreenPortmp = { (float) ePSXeViewGL.this.mWidth / 2, (float) ePSXeViewGL.this.mHeight / 4,
                    0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
                    0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
                    ((ePSXeViewGL.this.mWidth * 288f) / 480) + (padSizeScreenPortmp[28] / 2.0f),
                    (((float) ePSXeViewGL.this.mHeight / 2) - ((((float) ePSXeViewGL.this.mHeight / 2) * 68) / 400))
                            - (padSizeScreenPortmp[29] / 2.0f),
                    ((ePSXeViewGL.this.mWidth * 352f) / 480) + (padSizeScreenPortmp[30] / 2.0f),
                    (((float) ePSXeViewGL.this.mHeight / 2) - ((((float) ePSXeViewGL.this.mHeight / 2) * 68) / 400))
                            - (padSizeScreenPortmp[31] / 2.0f),
                    ((ePSXeViewGL.this.mWidth * HttpStatus.SC_REQUESTED_RANGE_NOT_SATISFIABLE) / 480)
                            + (padSizeScreenPortmp[32] / 2.0f),
                    (((float) ePSXeViewGL.this.mHeight / 2) - ((((float) ePSXeViewGL.this.mHeight / 2) * 68) / 400))
                            - (padSizeScreenPortmp[33] / 2.0f),
                    ((ePSXeViewGL.this.mWidth * 8f) / 480) + (padSizeScreenPortmp[34] / 2.0f),
                    (((float) ePSXeViewGL.this.mHeight / 2) - ((((float) ePSXeViewGL.this.mHeight / 2) * 338) / 400))
                            - (padSizeScreenPortmp[35] / 2.0f),
                    ((ePSXeViewGL.this.mWidth * 72f) / 480) + (padSizeScreenPortmp[36] / 2.0f),
                    (((float) ePSXeViewGL.this.mHeight / 2) - ((((float) ePSXeViewGL.this.mHeight / 2) * 338) / 400))
                            - (padSizeScreenPortmp[37] / 2.0f),
                    ((ePSXeViewGL.this.mWidth * 136f) / 480) + (padSizeScreenPortmp[38] / 2.0f),
                    (((float) ePSXeViewGL.this.mHeight / 2) - ((((float) ePSXeViewGL.this.mHeight / 2) * 338) / 400))
                            - (padSizeScreenPortmp[39] / 2.0f) };
            for (int i = 0; i < 1; i++) {
                ePSXeViewGL.this.padSizeScreenPor[(i * 2) + 0] = padSizeScreenPortmp[(i * 2) + 0];
                ePSXeViewGL.this.padSizeScreenPor[(i * 2) + 1] = padSizeScreenPortmp[(i * 2) + 1];
                ePSXeViewGL.this.padOffScreenPor[(i * 2) + 0] = padOffScreenPortmp[(i * 2) + 0];
                ePSXeViewGL.this.padOffScreenPor[(i * 2) + 1] = padOffScreenPortmp[(i * 2) + 1];
            }
            for (int i2 = 14; i2 < 20; i2++) {
                ePSXeViewGL.this.padSizeScreenPor[(i2 * 2) + 0] = padSizeScreenPortmp[(i2 * 2) + 0];
                ePSXeViewGL.this.padSizeScreenPor[(i2 * 2) + 1] = padSizeScreenPortmp[(i2 * 2) + 1];
                ePSXeViewGL.this.padOffScreenPor[(i2 * 2) + 0] = padOffScreenPortmp[(i2 * 2) + 0];
                ePSXeViewGL.this.padOffScreenPor[(i2 * 2) + 1] = padOffScreenPortmp[(i2 * 2) + 1];
            }
            float[] padSizeScreenLan2Htmp = { 228.0f, 114.0f, 224.0f, 124.0f, 66.0f, 25.0f, 66.0f, 31.0f, 64.0f, 30.0f,
                    64.0f, 30.0f, 64.0f, 30.0f, 64.0f, 30.0f };
            for (int i3 = 0; i3 < 8; i3++) {
                ePSXeViewGL.this.padSizeScreenLan2H[(i3 * 2) + 0] = padSizeScreenLan2Htmp[(i3 * 2) + 0]
                        * ePSXeViewGL.this.padResize;
                ePSXeViewGL.this.padSizeScreenLan2H[(i3 * 2) + 1] = padSizeScreenLan2Htmp[(i3 * 2) + 1]
                        * ePSXeViewGL.this.padResize;
            }
            float[] padOffScreenLan2Htmp = { ePSXeViewGL.this.padSizeScreenLan2H[0] / 2.0f,
                    ePSXeViewGL.this.padSizeScreenLan2H[1] / 2.0f,
                    ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan2H[2] / 2.0f),
                    ePSXeViewGL.this.padSizeScreenLan2H[3] / 2.0f,
                    (((float) ePSXeViewGL.this.mWidth / 2) - ePSXeViewGL.this.padSizeScreenLan2H[4]) - 30.0f,
                    ePSXeViewGL.this.padSizeScreenLan2H[5] / 2.0f, ((float) ePSXeViewGL.this.mWidth / 2) + 30,
                    ePSXeViewGL.this.padSizeScreenLan2H[7] / 2.0f, ePSXeViewGL.this.padSizeScreenLan2H[8] / 2.0f,
                    ((float) ePSXeViewGL.this.mHeight / 2) - (ePSXeViewGL.this.padSizeScreenLan2H[9] / 2.0f),
                    (ePSXeViewGL.this.padSizeScreenLan2H[10] / 2.0f) + ePSXeViewGL.this.padSizeScreenLan2H[8],
                    ((float) ePSXeViewGL.this.mHeight / 2) - (ePSXeViewGL.this.padSizeScreenLan2H[11] / 2.0f),
                    ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan2H[12] / 2.0f),
                    ((float) ePSXeViewGL.this.mHeight / 2) - (ePSXeViewGL.this.padSizeScreenLan2H[13] / 2.0f),
                    (ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan2H[14] / 2.0f))
                            - ePSXeViewGL.this.padSizeScreenLan2H[12],
                    ((float) ePSXeViewGL.this.mHeight / 2) - (ePSXeViewGL.this.padSizeScreenLan2H[15] / 2.0f) };
            for (int i4 = 0; i4 < 8; i4++) {
                ePSXeViewGL.this.padOffScreenLan2H[(i4 * 2) + 0] = padOffScreenLan2Htmp[(i4 * 2) + 0];
                ePSXeViewGL.this.padOffScreenLan2H[(i4 * 2) + 1] = padOffScreenLan2Htmp[(i4 * 2) + 1];
            }
            float[] padSizeScreenLan2Vtmp = { (ePSXeViewGL.this.mHeight * 228f) / ePSXeViewGL.this.mWidth,
                    (((float) ePSXeViewGL.this.mWidth / 2) * 228) / ePSXeViewGL.this.mHeight,
                    (ePSXeViewGL.this.mHeight * 224f) / ePSXeViewGL.this.mWidth,
                    (((float) ePSXeViewGL.this.mWidth / 2) * 248) / ePSXeViewGL.this.mHeight,
                    (ePSXeViewGL.this.mHeight * 66f) / ePSXeViewGL.this.mWidth,
                    (((float) ePSXeViewGL.this.mWidth / 2) * 40) / ePSXeViewGL.this.mHeight,
                    (ePSXeViewGL.this.mHeight * 66f) / ePSXeViewGL.this.mWidth,
                    (((float) ePSXeViewGL.this.mWidth / 2) * 62) / ePSXeViewGL.this.mHeight,
                    (ePSXeViewGL.this.mHeight * 64f) / ePSXeViewGL.this.mWidth,
                    (((float) ePSXeViewGL.this.mWidth / 2) * 60) / ePSXeViewGL.this.mHeight,
                    (ePSXeViewGL.this.mHeight * 64f) / ePSXeViewGL.this.mWidth,
                    (((float) ePSXeViewGL.this.mWidth / 2) * 60) / ePSXeViewGL.this.mHeight,
                    (ePSXeViewGL.this.mHeight * 64f) / ePSXeViewGL.this.mWidth,
                    (((float) ePSXeViewGL.this.mWidth / 2) * 60) / ePSXeViewGL.this.mHeight,
                    (ePSXeViewGL.this.mHeight * 64f) / ePSXeViewGL.this.mWidth,
                    (((float) ePSXeViewGL.this.mWidth / 2) * 60) / ePSXeViewGL.this.mHeight };
            for (int i5 = 0; i5 < 8; i5++) {
                ePSXeViewGL.this.padSizeScreenLan2V[(i5 * 2) + 0] = padSizeScreenLan2Vtmp[(i5 * 2) + 0]
                        * ePSXeViewGL.this.padResize;
                ePSXeViewGL.this.padSizeScreenLan2V[(i5 * 2) + 1] = padSizeScreenLan2Vtmp[(i5 * 2) + 1]
                        * ePSXeViewGL.this.padResize;
            }
            float[] padOffScreenLan2Vtmp = { ePSXeViewGL.this.padSizeScreenLan2V[0] / 2.0f,
                    ePSXeViewGL.this.padSizeScreenLan2V[1] / 2.0f,
                    ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan2V[2] / 2.0f),
                    ePSXeViewGL.this.padSizeScreenLan2V[3] / 2.0f,
                    (((float) ePSXeViewGL.this.mHeight / 2) - ePSXeViewGL.this.padSizeScreenLan2V[4]) - 30.0f,
                    ePSXeViewGL.this.padSizeScreenLan2V[5] / 2.0f, ((float) ePSXeViewGL.this.mHeight / 2) + 30,
                    ePSXeViewGL.this.padSizeScreenLan2V[7] / 2.0f, ePSXeViewGL.this.padSizeScreenLan2V[8] / 2.0f,
                    ((float) ePSXeViewGL.this.mWidth / 2) - (ePSXeViewGL.this.padSizeScreenLan2V[9] / 2.0f),
                    (ePSXeViewGL.this.padSizeScreenLan2V[10] / 2.0f) + ePSXeViewGL.this.padSizeScreenLan2V[8],
                    ((float) ePSXeViewGL.this.mWidth / 2) - (ePSXeViewGL.this.padSizeScreenLan2V[11] / 2.0f),
                    ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan2V[12] / 2.0f),
                    ((float) ePSXeViewGL.this.mWidth / 2) - (ePSXeViewGL.this.padSizeScreenLan2V[13] / 2.0f),
                    (ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan2V[14] / 2.0f))
                            - ePSXeViewGL.this.padSizeScreenLan2V[12],
                    ((float) ePSXeViewGL.this.mWidth / 2) - (ePSXeViewGL.this.padSizeScreenLan2V[15] / 2.0f) };
            for (int i6 = 0; i6 < 8; i6++) {
                ePSXeViewGL.this.padOffScreenLan2V[(i6 * 2) + 0] = padOffScreenLan2Vtmp[(i6 * 2) + 0];
                ePSXeViewGL.this.padOffScreenLan2V[(i6 * 2) + 1] = padOffScreenLan2Vtmp[(i6 * 2) + 1];
            }
            ePSXeViewGL.this.initvirtualPad = 0;
        }

        /**
         * Устанавливает базовые размеры кнопок без применения padResize
         * Используется при загрузке сохраненных настроек
         */
        private void setBasePadSizesWithoutResize() {
            float[][] padSizeScreenLantmp = {
                    new float[] { 228.0f, 228.0f, 224.0f, 248.0f, 66.0f, 50.0f, 66.0f, 62.0f, 64.0f, 60.0f, 64.0f,
                            60.0f, 64.0f, 60.0f, 64.0f, 60.0f, 53.0f, 41.0f, 64.0f, 60.0f, 64.0f, 60.0f, 222.0f, 222.0f,
                            222.0f, 222.0f, 71.0f, 71.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f,
                            64.0f, 64.0f, 64.0f },
                    new float[] { 228.0f, 228.0f, 224.0f, 248.0f, 66.0f, 50.0f, 66.0f, 62.0f, 64.0f, 60.0f, 64.0f,
                            60.0f, 64.0f, 60.0f, 64.0f, 60.0f, 53.0f, 41.0f, 64.0f, 60.0f, 64.0f, 60.0f, 222.0f, 222.0f,
                            222.0f, 222.0f, 71.0f, 71.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f,
                            64.0f, 64.0f, 64.0f }
            };

            // Применяем базовые размеры БЕЗ padResize
            for (int i = 0; i < 20; i++) {
                ePSXeViewGL.this.padSizeScreenLan[ePSXeViewGL.this.mode][(i * 2)
                        + 0] = padSizeScreenLantmp[ePSXeViewGL.this.mode][(i * 2) + 0];
                ePSXeViewGL.this.padSizeScreenLan[ePSXeViewGL.this.mode][(i * 2)
                        + 1] = padSizeScreenLantmp[ePSXeViewGL.this.mode][(i * 2) + 1];
            }

            // Устанавливаем базовые позиции
            setBasePadPositions();
        }

        /**
         * Устанавливает базовые позиции кнопок
         */
        private void setBasePadPositions() {
            if (ePSXeViewGL.this.emu_screen_orientation == 1) {
                if (ePSXeViewGL.this.emu_portrait_skin == 1) {
                    setPortraitPadPositions();
                }
                return;
            }
            setLandscapePadPositions();
        }

        /**
         * Устанавливает позиции для портретного режима
         */
        private void setPortraitPadPositions() {
            float[][] padOffScreenLantmp = {
                    new float[] { ePSXeViewGL.this.padSizeScreenLan[0][0] / 2.0f,
                            ePSXeViewGL.this.padSizeScreenLan[0][1] / 2.0f,
                            ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[0][2] / 2.0f),
                            ePSXeViewGL.this.padSizeScreenLan[0][3] / 2.0f,
                            (((float) ePSXeViewGL.this.mWidth / 2) - ePSXeViewGL.this.padSizeScreenLan[0][4]) - 30.0f,
                            ((float) ePSXeViewGL.this.mHeight / 2) - (ePSXeViewGL.this.padSizeScreenLan[0][5] / 2.0f),
                            ((float) ePSXeViewGL.this.mWidth / 2) + 30,
                            ((float) ePSXeViewGL.this.mHeight / 2) - (ePSXeViewGL.this.padSizeScreenLan[0][7] / 2.0f),
                            ePSXeViewGL.this.padSizeScreenLan[0][8] / 2.0f,
                            ((float) ePSXeViewGL.this.mHeight / 2) - (ePSXeViewGL.this.padSizeScreenLan[0][9] / 2.0f),
                            (ePSXeViewGL.this.padSizeScreenLan[0][10] / 2.0f) + ePSXeViewGL.this.padSizeScreenLan[0][8],
                            ((float) ePSXeViewGL.this.mHeight / 2) - (ePSXeViewGL.this.padSizeScreenLan[0][11] / 2.0f),
                            ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[0][12] / 2.0f),
                            ((float) ePSXeViewGL.this.mHeight / 2) - (ePSXeViewGL.this.padSizeScreenLan[0][13] / 2.0f),
                            (ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[0][14] / 2.0f))
                                    - ePSXeViewGL.this.padSizeScreenLan[0][12],
                            ((float) ePSXeViewGL.this.mHeight / 2) - (ePSXeViewGL.this.padSizeScreenLan[0][15] / 2.0f),
                            ((float) ePSXeViewGL.this.mWidth / 2) + 40 + ePSXeViewGL.this.padSizeScreenLan[0][6],
                            ((float) ePSXeViewGL.this.mHeight / 2) - (ePSXeViewGL.this.padSizeScreenLan[0][17] / 2.0f),
                            (ePSXeViewGL.this.padSizeScreenLan[0][18] / 2.0f) + ePSXeViewGL.this.padSizeScreenLan[0][10]
                                    + ePSXeViewGL.this.padSizeScreenLan[0][8],
                            ((float) ePSXeViewGL.this.mHeight / 2) - (ePSXeViewGL.this.padSizeScreenLan[0][19] / 2.0f),
                            ((ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[0][20] / 2.0f))
                                    - ePSXeViewGL.this.padSizeScreenLan[0][12])
                                    - ePSXeViewGL.this.padSizeScreenLan[0][14],
                            ((float) ePSXeViewGL.this.mHeight / 2) - (ePSXeViewGL.this.padSizeScreenLan[0][21] / 2.0f),
                            ePSXeViewGL.this.padSizeScreenLan[0][22] / 2.0f,
                            ePSXeViewGL.this.padSizeScreenLan[0][23] / 2.0f,
                            ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[0][24] / 2.0f),
                            ePSXeViewGL.this.padSizeScreenLan[0][25] / 2.0f, 0.0f, 0.0f,
                            ePSXeViewGL.this.padSizeScreenLan[0][28] / 2.0f,
                            ((float) ePSXeViewGL.this.mHeight / 3) - (ePSXeViewGL.this.padSizeScreenLan[0][29] / 2.0f),
                            (ePSXeViewGL.this.padSizeScreenLan[0][30] / 2.0f)
                                    + ePSXeViewGL.this.padSizeScreenLan[0][28],
                            ((float) ePSXeViewGL.this.mHeight / 3) - (ePSXeViewGL.this.padSizeScreenLan[0][31] / 2.0f),
                            (ePSXeViewGL.this.padSizeScreenLan[0][32] / 2.0f) + ePSXeViewGL.this.padSizeScreenLan[0][28]
                                    + ePSXeViewGL.this.padSizeScreenLan[0][30],
                            ((float) ePSXeViewGL.this.mHeight / 3) - (ePSXeViewGL.this.padSizeScreenLan[0][33] / 2.0f),
                            ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[0][34] / 2.0f),
                            ((float) ePSXeViewGL.this.mHeight / 3) - (ePSXeViewGL.this.padSizeScreenLan[0][35] / 2.0f),
                            (ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[0][36] / 2.0f))
                                    - ePSXeViewGL.this.padSizeScreenLan[0][34],
                            ((float) ePSXeViewGL.this.mHeight / 3) - (ePSXeViewGL.this.padSizeScreenLan[0][37] / 2.0f),
                            ((ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[0][38] / 2.0f))
                                    - ePSXeViewGL.this.padSizeScreenLan[0][34])
                                    - ePSXeViewGL.this.padSizeScreenLan[0][36],
                            ((float) ePSXeViewGL.this.mHeight / 3)
                                    - (ePSXeViewGL.this.padSizeScreenLan[0][39] / 2.0f) },
                    new float[] {
                            ((float) ePSXeViewGL.this.mWidth / 2)
                                    - ((ePSXeViewGL.this.padSizeScreenLan[1][0] / 2.0f) * 0.76f),
                            (float) ePSXeViewGL.this.mHeight / 4,
                            ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[1][2] / 2.0f),
                            ePSXeViewGL.this.padSizeScreenLan[1][3] / 2.0f,
                            (((float) ePSXeViewGL.this.mWidth / 2) - ePSXeViewGL.this.padSizeScreenLan[1][4]) - 30.0f,
                            ((float) ePSXeViewGL.this.mHeight / 2) - (ePSXeViewGL.this.padSizeScreenLan[1][5] / 2.0f),
                            ((float) ePSXeViewGL.this.mWidth / 2) + 30,
                            ((float) ePSXeViewGL.this.mHeight / 2) - (ePSXeViewGL.this.padSizeScreenLan[1][7] / 2.0f),
                            ePSXeViewGL.this.padSizeScreenLan[1][8] / 2.0f,
                            ((float) ePSXeViewGL.this.mHeight / 2) - (ePSXeViewGL.this.padSizeScreenLan[1][9] / 2.0f),
                            (ePSXeViewGL.this.padSizeScreenLan[1][10] / 2.0f) + ePSXeViewGL.this.padSizeScreenLan[0][8],
                            ((float) ePSXeViewGL.this.mHeight / 2) - (ePSXeViewGL.this.padSizeScreenLan[1][11] / 2.0f),
                            ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[1][12] / 2.0f),
                            ((float) ePSXeViewGL.this.mHeight / 2) - (ePSXeViewGL.this.padSizeScreenLan[1][13] / 2.0f),
                            (ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[1][14] / 2.0f))
                                    - ePSXeViewGL.this.padSizeScreenLan[1][12],
                            ((float) ePSXeViewGL.this.mHeight / 2) - (ePSXeViewGL.this.padSizeScreenLan[1][15] / 2.0f),
                            ((float) ePSXeViewGL.this.mWidth / 2) + 40 + ePSXeViewGL.this.padSizeScreenLan[1][6],
                            ((float) ePSXeViewGL.this.mHeight / 2) - (ePSXeViewGL.this.padSizeScreenLan[1][17] / 2.0f),
                            (ePSXeViewGL.this.padSizeScreenLan[1][18] / 2.0f) + ePSXeViewGL.this.padSizeScreenLan[1][10]
                                    + ePSXeViewGL.this.padSizeScreenLan[1][8],
                            ((float) ePSXeViewGL.this.mHeight / 2) - (ePSXeViewGL.this.padSizeScreenLan[1][19] / 2.0f),
                            ((ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[1][20] / 2.0f))
                                    - ePSXeViewGL.this.padSizeScreenLan[1][12])
                                    - ePSXeViewGL.this.padSizeScreenLan[1][14],
                            ((float) ePSXeViewGL.this.mHeight / 2) - (ePSXeViewGL.this.padSizeScreenLan[1][21] / 2.0f),
                            ePSXeViewGL.this.padSizeScreenLan[1][22] / 2.0f,
                            ePSXeViewGL.this.padSizeScreenLan[1][23] / 2.0f,
                            ((float) ePSXeViewGL.this.mWidth / 2)
                                    + ((ePSXeViewGL.this.padSizeScreenLan[1][24] / 2.0f) * 0.76f),
                            (float) ePSXeViewGL.this.mHeight / 4, 0.0f, 0.0f,
                            ePSXeViewGL.this.padSizeScreenLan[1][28] / 2.0f,
                            ((float) ePSXeViewGL.this.mHeight / 3) - (ePSXeViewGL.this.padSizeScreenLan[1][29] / 2.0f),
                            (ePSXeViewGL.this.padSizeScreenLan[1][30] / 2.0f)
                                    + ePSXeViewGL.this.padSizeScreenLan[1][28],
                            ((float) ePSXeViewGL.this.mHeight / 3) - (ePSXeViewGL.this.padSizeScreenLan[1][31] / 2.0f),
                            (ePSXeViewGL.this.padSizeScreenLan[1][32] / 2.0f) + ePSXeViewGL.this.padSizeScreenLan[1][28]
                                    + ePSXeViewGL.this.padSizeScreenLan[1][30],
                            ((float) ePSXeViewGL.this.mHeight / 3) - (ePSXeViewGL.this.padSizeScreenLan[1][33] / 2.0f),
                            ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[1][34] / 2.0f),
                            ((float) ePSXeViewGL.this.mHeight / 3) - (ePSXeViewGL.this.padSizeScreenLan[1][35] / 2.0f),
                            (ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[1][36] / 2.0f))
                                    - ePSXeViewGL.this.padSizeScreenLan[1][34],
                            ((float) ePSXeViewGL.this.mHeight / 3) - (ePSXeViewGL.this.padSizeScreenLan[1][37] / 2.0f),
                            ((ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[1][38] / 2.0f))
                                    - ePSXeViewGL.this.padSizeScreenLan[1][34])
                                    - ePSXeViewGL.this.padSizeScreenLan[1][36],
                            ((float) ePSXeViewGL.this.mHeight / 3) - (ePSXeViewGL.this.padSizeScreenLan[1][39] / 2.0f) }
            };
            for (int i = 0; i < 20; i++) {
                ePSXeViewGL.this.padOffScreenLan[ePSXeViewGL.this.mode][(i * 2)
                        + 0] = padOffScreenLantmp[ePSXeViewGL.this.mode][(i * 2) + 0];
                ePSXeViewGL.this.padOffScreenLan[ePSXeViewGL.this.mode][(i * 2)
                        + 1] = padOffScreenLantmp[ePSXeViewGL.this.mode][(i * 2) + 1];
            }
        }

        /**
         * Устанавливает позиции для ландшафтного режима
         */
        private void setLandscapePadPositions() {
            float[][] padOffScreenLantmp = {
                    new float[] { ePSXeViewGL.this.padSizeScreenLan[0][0] / 2.0f,
                            ePSXeViewGL.this.padSizeScreenLan[0][1] / 2.0f,
                            ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[0][2] / 2.0f),
                            ePSXeViewGL.this.padSizeScreenLan[0][3] / 2.0f,
                            (((float) ePSXeViewGL.this.mWidth / 2) - ePSXeViewGL.this.padSizeScreenLan[0][4]) - 30.0f,
                            ePSXeViewGL.this.padSizeScreenLan[0][5] / 2.0f, ((float) ePSXeViewGL.this.mWidth / 2) + 30,
                            ePSXeViewGL.this.padSizeScreenLan[0][7] / 2.0f,
                            ePSXeViewGL.this.padSizeScreenLan[0][8] / 2.0f,
                            ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan[0][9] / 2.0f),
                            (ePSXeViewGL.this.padSizeScreenLan[0][10] / 2.0f) + ePSXeViewGL.this.padSizeScreenLan[0][8],
                            ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan[0][11] / 2.0f),
                            ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[0][12] / 2.0f),
                            ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan[0][13] / 2.0f),
                            (ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[0][14] / 2.0f))
                                    - ePSXeViewGL.this.padSizeScreenLan[0][12],
                            ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan[0][15] / 2.0f),
                            ((float) ePSXeViewGL.this.mWidth / 2) + 40 + ePSXeViewGL.this.padSizeScreenLan[0][6],
                            ePSXeViewGL.this.padSizeScreenLan[0][17] / 2.0f,
                            (ePSXeViewGL.this.padSizeScreenLan[0][18] / 2.0f) + ePSXeViewGL.this.padSizeScreenLan[0][10]
                                    + ePSXeViewGL.this.padSizeScreenLan[0][8],
                            ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan[0][19] / 2.0f),
                            ((ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[0][20] / 2.0f))
                                    - ePSXeViewGL.this.padSizeScreenLan[0][12])
                                    - ePSXeViewGL.this.padSizeScreenLan[0][14],
                            ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan[0][21] / 2.0f),
                            ePSXeViewGL.this.padSizeScreenLan[0][22] / 2.0f,
                            ePSXeViewGL.this.padSizeScreenLan[0][23] / 2.0f,
                            ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[0][24] / 2.0f),
                            ePSXeViewGL.this.padSizeScreenLan[0][25] / 2.0f, 0.0f, 0.0f,
                            ePSXeViewGL.this.padSizeScreenLan[0][28] / 2.0f,
                            (ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan[0][29] / 2.0f))
                                    - (ePSXeViewGL.this.padSizeScreenLan[0][10] * 2.0f),
                            (ePSXeViewGL.this.padSizeScreenLan[0][30] / 2.0f)
                                    + ePSXeViewGL.this.padSizeScreenLan[0][28],
                            (ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan[0][31] / 2.0f))
                                    - (ePSXeViewGL.this.padSizeScreenLan[0][10] * 2.0f),
                            (ePSXeViewGL.this.padSizeScreenLan[0][32] / 2.0f) + ePSXeViewGL.this.padSizeScreenLan[0][28]
                                    + ePSXeViewGL.this.padSizeScreenLan[0][30],
                            (ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan[0][33] / 2.0f))
                                    - (ePSXeViewGL.this.padSizeScreenLan[0][10] * 2.0f),
                            ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[0][34] / 2.0f),
                            (ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan[0][35] / 2.0f))
                                    - (ePSXeViewGL.this.padSizeScreenLan[0][10] * 2.0f),
                            (ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[0][36] / 2.0f))
                                    - ePSXeViewGL.this.padSizeScreenLan[0][34],
                            (ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan[0][37] / 2.0f))
                                    - (ePSXeViewGL.this.padSizeScreenLan[0][10] * 2.0f),
                            ((ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[0][38] / 2.0f))
                                    - ePSXeViewGL.this.padSizeScreenLan[0][34])
                                    - ePSXeViewGL.this.padSizeScreenLan[0][36],
                            (ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan[0][39] / 2.0f))
                                    - (ePSXeViewGL.this.padSizeScreenLan[0][10] * 2.0f) },
                    new float[] {
                            ((float) ePSXeViewGL.this.mWidth / 2)
                                    - ((ePSXeViewGL.this.padSizeScreenLan[1][0] / 2.0f) * 0.76f),
                            (float) ePSXeViewGL.this.mHeight / 2,
                            ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[1][2] / 2.0f),
                            ePSXeViewGL.this.padSizeScreenLan[1][3] / 2.0f,
                            (((float) ePSXeViewGL.this.mWidth / 2) - ePSXeViewGL.this.padSizeScreenLan[1][4]) - 30.0f,
                            ePSXeViewGL.this.padSizeScreenLan[1][5] / 2.0f, ((float) ePSXeViewGL.this.mWidth / 2) + 30,
                            ePSXeViewGL.this.padSizeScreenLan[1][7] / 2.0f,
                            ePSXeViewGL.this.padSizeScreenLan[1][8] / 2.0f,
                            ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan[1][9] / 2.0f),
                            (ePSXeViewGL.this.padSizeScreenLan[1][10] / 2.0f) + ePSXeViewGL.this.padSizeScreenLan[1][8],
                            ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan[1][11] / 2.0f),
                            ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[1][12] / 2.0f),
                            ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan[1][13] / 2.0f),
                            (ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[1][14] / 2.0f))
                                    - ePSXeViewGL.this.padSizeScreenLan[1][12],
                            ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan[1][15] / 2.0f),
                            ((float) ePSXeViewGL.this.mWidth / 2) + 40 + ePSXeViewGL.this.padSizeScreenLan[1][6],
                            ePSXeViewGL.this.padSizeScreenLan[1][17] / 2.0f,
                            (ePSXeViewGL.this.padSizeScreenLan[1][18] / 2.0f) + ePSXeViewGL.this.padSizeScreenLan[1][10]
                                    + ePSXeViewGL.this.padSizeScreenLan[1][8],
                            ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan[1][19] / 2.0f),
                            ((ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[1][20] / 2.0f))
                                    - ePSXeViewGL.this.padSizeScreenLan[1][12])
                                    - ePSXeViewGL.this.padSizeScreenLan[1][14],
                            ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan[1][21] / 2.0f),
                            ePSXeViewGL.this.padSizeScreenLan[1][22] / 2.0f,
                            ePSXeViewGL.this.padSizeScreenLan[1][23] / 2.0f,
                            ((float) ePSXeViewGL.this.mWidth / 2)
                                    + ((ePSXeViewGL.this.padSizeScreenLan[1][24] / 2.0f) * 0.76f),
                            (float) ePSXeViewGL.this.mHeight / 2, 0.0f, 0.0f,
                            ePSXeViewGL.this.padSizeScreenLan[1][28] / 2.0f,
                            (ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan[1][29] / 2.0f))
                                    - (ePSXeViewGL.this.padSizeScreenLan[1][10] * 2.0f),
                            (ePSXeViewGL.this.padSizeScreenLan[1][30] / 2.0f)
                                    + ePSXeViewGL.this.padSizeScreenLan[1][28],
                            (ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan[1][31] / 2.0f))
                                    - (ePSXeViewGL.this.padSizeScreenLan[1][10] * 2.0f),
                            (ePSXeViewGL.this.padSizeScreenLan[1][32] / 2.0f) + ePSXeViewGL.this.padSizeScreenLan[1][28]
                                    + ePSXeViewGL.this.padSizeScreenLan[1][30],
                            (ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan[1][33] / 2.0f))
                                    - (ePSXeViewGL.this.padSizeScreenLan[1][10] * 2.0f),
                            ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[1][34] / 2.0f),
                            (ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan[1][35] / 2.0f))
                                    - (ePSXeViewGL.this.padSizeScreenLan[1][10] * 2.0f),
                            (ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[1][36] / 2.0f))
                                    - ePSXeViewGL.this.padSizeScreenLan[1][34],
                            (ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan[1][37] / 2.0f))
                                    - (ePSXeViewGL.this.padSizeScreenLan[1][10] * 2.0f),
                            ((ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[1][38] / 2.0f))
                                    - ePSXeViewGL.this.padSizeScreenLan[1][34])
                                    - ePSXeViewGL.this.padSizeScreenLan[1][36],
                            (ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan[1][39] / 2.0f))
                                    - (ePSXeViewGL.this.padSizeScreenLan[1][10] * 2.0f) }
            };
            for (int i = 0; i < 20; i++) {
                ePSXeViewGL.this.padOffScreenLan[ePSXeViewGL.this.mode][(i * 2)
                        + 0] = padOffScreenLantmp[ePSXeViewGL.this.mode][(i * 2) + 0];
                ePSXeViewGL.this.padOffScreenLan[ePSXeViewGL.this.mode][(i * 2)
                        + 1] = padOffScreenLantmp[ePSXeViewGL.this.mode][(i * 2) + 1];
            }
        }

        /**
         * Применяет финальное масштабирование к размерам и позициям после загрузки из
         * XML
         */
        private void applyFinalScalingToPads() {
            // Применяем padResize к размерам
            for (int i = 0; i < 20; i++) {
                ePSXeViewGL.this.padSizeScreenLan[0][(i * 2) + 0] *= ePSXeViewGL.this.padResize;
                ePSXeViewGL.this.padSizeScreenLan[0][(i * 2) + 1] *= ePSXeViewGL.this.padResize;
                ePSXeViewGL.this.padSizeScreenLan[1][(i * 2) + 0] *= ePSXeViewGL.this.padResize;
                ePSXeViewGL.this.padSizeScreenLan[1][(i * 2) + 1] *= ePSXeViewGL.this.padResize;
            }

            // Применяем padResize к позициям
            for (int i = 0; i < 20; i++) {
                ePSXeViewGL.this.padOffScreenLan[0][(i * 2) + 0] *= ePSXeViewGL.this.padResize;
                ePSXeViewGL.this.padOffScreenLan[0][(i * 2) + 1] *= ePSXeViewGL.this.padResize;
                ePSXeViewGL.this.padOffScreenLan[1][(i * 2) + 0] *= ePSXeViewGL.this.padResize;
                ePSXeViewGL.this.padOffScreenLan[1][(i * 2) + 1] *= ePSXeViewGL.this.padResize;
            }
        }

        public void loadExtraButtons() {
            if (ePSXeViewGL.this.emu_player_mode == 1) {
                String cpadprofile = ePSXeViewGL.this.padprofile;
                if (ePSXeViewGL.this.mePSXeReadPreferences == null) {
                    ePSXeViewGL.this.mePSXeReadPreferences = new ePSXeReadPreferences(ePSXeViewGL.this.mContext);
                }
                ePSXeViewGL.this.padScreenExtraCombo = 0;
                ePSXeViewGL.this.padScreenExtraEnabled = 0;
                if (ePSXeViewGL.this.emu_screen_orientation == 1) {
                    ePSXeViewGL.this.padprofile = "";
                }
                for (int i = 0; i < 6; i++) {
                    int val = ePSXeViewGL.this.mePSXeReadPreferences
                            .getPadExtra(ePSXeViewGL.this.padprofile + "inputExtrasPref" + (i + 1));
                    if (val == 19) {
                        val = -1;
                    }
                    if (val == 28) {
                        val = 19;
                    }
                    if (val == -1) {
                        ePSXeViewGL.this.padScreenStatus[0][i + 14] = 2;
                        ePSXeViewGL.this.padScreenStatus[1][i + 14] = 2;
                    } else {
                        ePSXeViewGL.this.padScreenExtraEnabled = 1;
                        ePSXeViewGL.this.padScreenStatus[0][i + 14] = 1;
                        ePSXeViewGL.this.padScreenStatus[1][i + 14] = 1;
                    }
                    ePSXeViewGL.this.padScreenExtra[i] = val;
                    if (ePSXeViewGL.this.padScreenExtra[i] >= 0 && ePSXeViewGL.this.padScreenExtra[i] < 5) {
                        ePSXeViewGL.this.padScreenFunc[i] = 1;
                    } else if (ePSXeViewGL.this.padScreenExtra[i] < 10) {
                        ePSXeViewGL.this.padScreenFunc[i] = 1;
                    } else if (ePSXeViewGL.this.padScreenExtra[i] < 18) {
                        ePSXeViewGL.this.padScreenFunc[i] = 2;
                        ePSXeViewGL.this.padScreenExtraCombo = 1;
                    } else if (ePSXeViewGL.this.padScreenExtra[i] == 18) {
                        ePSXeViewGL.this.padScreenFunc[i] = 1;
                    } else if (ePSXeViewGL.this.padScreenExtra[i] == 19) {
                        ePSXeViewGL.this.padScreenFunc[i] = 1;
                    } else if (ePSXeViewGL.this.padScreenExtra[i] == 20) {
                        ePSXeViewGL.this.padScreenFunc[i] = 1;
                    } else if (ePSXeViewGL.this.padScreenExtra[i] == 21) {
                        ePSXeViewGL.this.padScreenFunc[i] = 1;
                    } else if (ePSXeViewGL.this.padScreenExtra[i] == 22) {
                        ePSXeViewGL.this.padScreenFunc[i] = 1;
                    } else if (ePSXeViewGL.this.padScreenExtra[i] == 23) {
                        ePSXeViewGL.this.padScreenFunc[i] = 1;
                    } else if (ePSXeViewGL.this.padScreenExtra[i] == 24) {
                        ePSXeViewGL.this.padScreenFunc[i] = 1;
                    } else if (ePSXeViewGL.this.padScreenExtra[i] == 25) {
                        ePSXeViewGL.this.padScreenFunc[i] = 1;
                    } else if (ePSXeViewGL.this.padScreenExtra[i] == 26) {
                        ePSXeViewGL.this.padScreenFunc[i] = 1;
                    } else if (ePSXeViewGL.this.padScreenExtra[i] == 27) {
                        ePSXeViewGL.this.padScreenFunc[i] = 1;
                    } else {
                        ePSXeViewGL.this.padScreenFunc[i] = 0;
                    }
                }
                if (ePSXeViewGL.this.emu_screen_orientation == 1) {
                    ePSXeViewGL.this.padprofile = cpadprofile;
                }
            }
        }

        public void redoPads(GL10 gl) {
            if (ePSXeViewGL.this.mePSXeReadPreferences == null) {
                ePSXeViewGL.this.mePSXeReadPreferences = new ePSXeReadPreferences(ePSXeViewGL.this.mContext);
            }
            if (ePSXeViewGL.this.mePSXeReadPreferences.getPadStatus(ePSXeViewGL.this.padprofile + "Pad1Status1") != -1
                    && ePSXeViewGL.this.emu_player_mode == 1
                    && (ePSXeViewGL.this.emu_screen_orientation != 1 || ePSXeViewGL.this.emu_portrait_skin == 1)) {
                Log.e("epsxeviewgl", "loading pad info from preferences");

                // Сначала устанавливаем базовые размеры без масштабирования
                int tmode = ePSXeViewGL.this.mode;
                ePSXeViewGL.this.mode = 0;
                setBasePadSizesWithoutResize();
                ePSXeViewGL.this.mode = 1;
                setBasePadSizesWithoutResize();
                ePSXeViewGL.this.mode = tmode;

                // Получаем сохраненные размеры экрана
                ePSXeViewGL.this.mWidthSaved = ePSXeViewGL.this.mePSXeReadPreferences
                        .getPadWH(ePSXeViewGL.this.padprofile + "Pad1Width");
                ePSXeViewGL.this.mHeightSaved = ePSXeViewGL.this.mePSXeReadPreferences
                        .getPadWH(ePSXeViewGL.this.padprofile + "Pad1Height");

                // Вычисляем коэффициенты масштабирования для изменения разрешения
                float resizeX = 1.0f;
                float resizeY = 1.0f;
                if (ePSXeViewGL.this.mWidthSaved != 0 && ePSXeViewGL.this.mWidthSaved != ePSXeViewGL.this.mWidth) {
                    resizeX = (float) ePSXeViewGL.this.mWidth / ePSXeViewGL.this.mWidthSaved;
                }
                if (ePSXeViewGL.this.mHeightSaved != 0 && ePSXeViewGL.this.mHeightSaved != ePSXeViewGL.this.mHeight) {
                    resizeY = (float) ePSXeViewGL.this.mHeight / ePSXeViewGL.this.mHeightSaved;
                }
                for (int i = 1; i < 14; i++) {
                    int val = ePSXeViewGL.this.mePSXeReadPreferences
                            .getPadStatus(ePSXeViewGL.this.padprofile + "Pad1Status" + i);
                    if (val != -1) {
                        ePSXeViewGL.this.padScreenStatus[0][i - 1] = val;
                    }
                }
                for (int i2 = 1; i2 < 14; i2++) {
                    int val2 = ePSXeViewGL.this.mePSXeReadPreferences
                            .getPadStatus(ePSXeViewGL.this.padprofile + "Pad1StatusAnalog" + i2);
                    if (val2 != -1) {
                        ePSXeViewGL.this.padScreenStatus[1][i2 - 1] = val2;
                    }
                }
                for (int i3 = 1; i3 < 14; i3++) {
                    float x = ePSXeViewGL.this.mePSXeReadPreferences
                            .getPadSize(ePSXeViewGL.this.padprofile + "Pad1SizeX" + i3);
                    float y = ePSXeViewGL.this.mePSXeReadPreferences
                            .getPadSize(ePSXeViewGL.this.padprofile + "Pad1SizeY" + i3);
                    if (x != -1.0f) {
                        ePSXeViewGL.this.padSizeScreenLan[0][(i3 - 1) * 2] = x;
                        ePSXeViewGL.this.padSizeScreenLan[0][((i3 - 1) * 2) + 1] = y;
                    }
                }
                for (int i4 = 14; i4 < 20; i4++) {
                    float x2 = ePSXeViewGL.this.mePSXeReadPreferences
                            .getPadSize(ePSXeViewGL.this.padprofile + "Pad1SizeX" + i4);
                    float y2 = ePSXeViewGL.this.mePSXeReadPreferences
                            .getPadSize(ePSXeViewGL.this.padprofile + "Pad1SizeY" + i4);
                    if (x2 != -1.0f) {
                        ePSXeViewGL.this.padSizeScreenLan[0][i4 * 2] = x2;
                        ePSXeViewGL.this.padSizeScreenLan[0][(i4 * 2) + 1] = y2;
                    }
                }
                for (int i5 = 1; i5 < 14; i5++) {
                    float x3 = ePSXeViewGL.this.mePSXeReadPreferences
                            .getPadSize(ePSXeViewGL.this.padprofile + "Pad1SizeXAnalog" + i5);
                    float y3 = ePSXeViewGL.this.mePSXeReadPreferences
                            .getPadSize(ePSXeViewGL.this.padprofile + "Pad1SizeYAnalog" + i5);
                    if (x3 != -1.0f) {
                        ePSXeViewGL.this.padSizeScreenLan[1][(i5 - 1) * 2] = x3;
                        ePSXeViewGL.this.padSizeScreenLan[1][((i5 - 1) * 2) + 1] = y3;
                    }
                }
                for (int i6 = 14; i6 < 20; i6++) {
                    float x4 = ePSXeViewGL.this.mePSXeReadPreferences
                            .getPadSize(ePSXeViewGL.this.padprofile + "Pad1SizeXAnalog" + i6);
                    float y4 = ePSXeViewGL.this.mePSXeReadPreferences
                            .getPadSize(ePSXeViewGL.this.padprofile + "Pad1SizeYAnalog" + i6);
                    if (x4 != -1.0f) {
                        ePSXeViewGL.this.padSizeScreenLan[1][i6 * 2] = x4;
                        ePSXeViewGL.this.padSizeScreenLan[1][(i6 * 2) + 1] = y4;
                    }
                }
                for (int i7 = 1; i7 < 14; i7++) {
                    float x5 = ePSXeViewGL.this.mePSXeReadPreferences
                            .getPadSize(ePSXeViewGL.this.padprofile + "Pad1PosX" + i7);
                    float y5 = ePSXeViewGL.this.mePSXeReadPreferences
                            .getPadSize(ePSXeViewGL.this.padprofile + "Pad1PosY" + i7);
                    if (x5 != -1.0f) {
                        ePSXeViewGL.this.padOffScreenLan[0][(i7 - 1) * 2] = x5;
                        ePSXeViewGL.this.padOffScreenLan[0][((i7 - 1) * 2) + 1] = y5;
                    }
                }
                for (int i8 = 14; i8 < 20; i8++) {
                    float x6 = ePSXeViewGL.this.mePSXeReadPreferences
                            .getPadSize(ePSXeViewGL.this.padprofile + "Pad1PosX" + i8);
                    float y6 = ePSXeViewGL.this.mePSXeReadPreferences
                            .getPadSize(ePSXeViewGL.this.padprofile + "Pad1PosY" + i8);
                    if (x6 != -1.0f) {
                        ePSXeViewGL.this.padOffScreenLan[0][i8 * 2] = x6;
                        ePSXeViewGL.this.padOffScreenLan[0][(i8 * 2) + 1] = y6;
                    }
                }
                for (int i9 = 1; i9 < 14; i9++) {
                    float x7 = ePSXeViewGL.this.mePSXeReadPreferences
                            .getPadSize(ePSXeViewGL.this.padprofile + "Pad1PosXAnalog" + i9);
                    float y7 = ePSXeViewGL.this.mePSXeReadPreferences
                            .getPadSize(ePSXeViewGL.this.padprofile + "Pad1PosYAnalog" + i9);
                    if (x7 != -1.0f) {
                        ePSXeViewGL.this.padOffScreenLan[1][(i9 - 1) * 2] = x7;
                        ePSXeViewGL.this.padOffScreenLan[1][((i9 - 1) * 2) + 1] = y7;
                    }
                }
                for (int i10 = 14; i10 < 20; i10++) {
                    float x8 = ePSXeViewGL.this.mePSXeReadPreferences
                            .getPadSize(ePSXeViewGL.this.padprofile + "Pad1PosXAnalog" + i10);
                    float y8 = ePSXeViewGL.this.mePSXeReadPreferences
                            .getPadSize(ePSXeViewGL.this.padprofile + "Pad1PosYAnalog" + i10);
                    if (x8 != -1.0f) {
                        ePSXeViewGL.this.padOffScreenLan[1][i10 * 2] = x8;
                        ePSXeViewGL.this.padOffScreenLan[1][(i10 * 2) + 1] = y8;
                    }
                }
                for (int i11 = 1; i11 < 14; i11++) {
                    float val3 = ePSXeViewGL.this.mePSXeReadPreferences
                            .getPadResize(ePSXeViewGL.this.padprofile + "Pad1Resize" + i11);
                    if (val3 != -1.0f) {
                        ePSXeViewGL.this.padScreenResize[0][i11 - 1] = val3;
                    }
                }
                for (int i12 = 14; i12 < 20; i12++) {
                    float val4 = ePSXeViewGL.this.mePSXeReadPreferences
                            .getPadResize(ePSXeViewGL.this.padprofile + "Pad1Resize" + i12);
                    if (val4 != -1.0f) {
                        ePSXeViewGL.this.padScreenResize[0][i12] = val4;
                    }
                }
                for (int i13 = 1; i13 < 14; i13++) {
                    float val5 = ePSXeViewGL.this.mePSXeReadPreferences
                            .getPadResize(ePSXeViewGL.this.padprofile + "Pad1ResizeAnalog" + i13);
                    if (val5 != -1.0f) {
                        ePSXeViewGL.this.padScreenResize[1][i13 - 1] = val5;
                    }
                }
                for (int i14 = 14; i14 < 20; i14++) {
                    float val6 = ePSXeViewGL.this.mePSXeReadPreferences
                            .getPadResize(ePSXeViewGL.this.padprofile + "Pad1ResizeAnalog" + i14);
                    if (val6 != -1.0f) {
                        ePSXeViewGL.this.padScreenResize[1][i14] = val6;
                    }
                }
                // Применяем коррекцию разрешения к размерам и позициям
                for (int i15 = 0; i15 < 20; i15++) {
                    ePSXeViewGL.this.padSizeScreenLan[0][(i15 * 2)
                            + 0] = ePSXeViewGL.this.padSizeScreenLan[0][(i15 * 2) + 0] * resizeX;
                    ePSXeViewGL.this.padSizeScreenLan[0][(i15 * 2)
                            + 1] = ePSXeViewGL.this.padSizeScreenLan[0][(i15 * 2) + 1] * resizeY;
                    ePSXeViewGL.this.padSizeScreenLan[1][(i15 * 2)
                            + 0] = ePSXeViewGL.this.padSizeScreenLan[1][(i15 * 2) + 0] * resizeX;
                    ePSXeViewGL.this.padSizeScreenLan[1][(i15 * 2)
                            + 1] = ePSXeViewGL.this.padSizeScreenLan[1][(i15 * 2) + 1] * resizeY;
                }
                for (int i16 = 0; i16 < 20; i16++) {
                    ePSXeViewGL.this.padOffScreenLan[0][(i16 * 2)
                            + 0] = ePSXeViewGL.this.padOffScreenLan[0][(i16 * 2) + 0] * resizeX;
                    ePSXeViewGL.this.padOffScreenLan[0][(i16 * 2)
                            + 1] = ePSXeViewGL.this.padOffScreenLan[0][(i16 * 2) + 1] * resizeY;
                    ePSXeViewGL.this.padOffScreenLan[1][(i16 * 2)
                            + 0] = ePSXeViewGL.this.padOffScreenLan[1][(i16 * 2) + 0] * resizeX;
                    ePSXeViewGL.this.padOffScreenLan[1][(i16 * 2)
                            + 1] = ePSXeViewGL.this.padOffScreenLan[1][(i16 * 2) + 1] * resizeY;
                }

                // Теперь применяем базовое масштабирование для текущего разрешения
                calculateBasePadResize();
                applyFinalScalingToPads();
                loadExtraButtons();
                loadExtraButtonsTextures(gl);
            } else {
                Log.e("epsxepadeditor", "setting default pad info");
                if (ePSXeViewGL.this.emu_player_mode != 1 || ePSXeViewGL.this.emu_screen_orientation != 1
                        || ePSXeViewGL.this.emu_portrait_skin != 0) {
                    int tmode2 = ePSXeViewGL.this.mode;
                    ePSXeViewGL.this.mode = 0;
                    resetPad1Values();
                    ePSXeViewGL.this.mode = 1;
                    resetPad1Values();
                    ePSXeViewGL.this.mode = tmode2;
                } else {
                    loadExtraButtons();
                    loadExtraButtonsTextures(gl);
                }
            }
            resetPadAllValues();
            ePSXeViewGL.this.initvirtualPad = 0;
            if (ePSXeViewGL.this.emu_player_mode == 1) {
                if (ePSXeViewGL.this.emu_pad_mode[0] == 1 || ePSXeViewGL.this.emu_pad_mode[0] == 4) {
                    if (ePSXeViewGL.this.emu_screen_orientation == 1 && ePSXeViewGL.this.emu_portrait_skin == 0) {
                        ePSXeViewGL.this.init_motionevent_1playerPortrait();
                        return;
                    } else {
                        ePSXeViewGL.this.init_motionevent_1playerLandscape();
                        ePSXeViewGL.this.resetDynamicPad();
                        return;
                    }
                }
                if (ePSXeViewGL.this.emu_pad_mode[0] == 3 || ePSXeViewGL.this.emu_pad_mode[0] == 8) {
                    ePSXeViewGL.this.gun.initGun(ePSXeViewGL.this.mWidth, ePSXeViewGL.this.mHeight,
                            ePSXeViewGL.this.emu_pad_type_selected);
                }
            }
        }

        @Override // android.opengl.GLSurfaceView.Renderer
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            Log.e("ePSXeRenderer2", "onSurfaceChanged");
            if (ePSXeViewGL.this.onPauseMode != 1) {
                ePSXeViewGL.this.mWidth = width;
                ePSXeViewGL.this.mHeight = height;
                ePSXeViewGL.this.f165e.openglresize(width, height, ePSXeViewGL.this.emu_player_mode,
                        ePSXeViewGL.this.emu_split_mode, ePSXeViewGL.this.emu_screen_ratio,
                        ePSXeViewGL.this.emu_screen_orientation, ePSXeViewGL.this.emu_screen_vrmode,
                        ePSXeViewGL.this.emu_screen_vrdistorsion);
                redoPads(gl);
            }
        }

        public int loadShader(int type, String shaderCode) {
            int shader = GLES20.glCreateShader(type);
            GLES20.glShaderSource(shader, shaderCode);
            GLES20.glCompileShader(shader);
            return shader;
        }

        public void loadExtraButtonsTextures(GL10 gl) {
            this.hTextmp = 512;
            this.wTextmp = 512;
            for (int i = 0; i < 6; i++) {
                int j = ePSXeViewGL.this.padScreenExtra[i];
                if (j >= 0) {
                    if (j >= 24) {
                        j += 4;
                    }
                    this.wTexLan[i + 14] = this.padCoordsExtra[(j * 4) + 2] - this.padCoordsExtra[(j * 4) + 0];
                    this.hTexLan[i + 14] = this.padCoordsExtra[(j * 4) + 3] - this.padCoordsExtra[(j * 4) + 1];
                    this.textureRgnLan[i + 14] = new TextureRegion(this.wTextmp, this.hTextmp,
                            this.padCoordsExtra[(j * 4) + 0], this.padCoordsExtra[(j * 4) + 1], this.wTexLan[i + 14],
                            this.hTexLan[i + 14]);
                    this.batchLan[i + 14] = new SpriteBatch2(gl, 1, this.mProgram, this.mpadPositionLoc,
                            this.mpadTexCoordLoc, this.mpadFrameLoc, this.mpadAlphaLoc,
                            ePSXeViewGL.this.emu_input_alpha);
                }
            }
        }

        @Override // android.opengl.GLSurfaceView.Renderer
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            Log.e("ePSXeRenderer2", "onSurfaceCreated");
            ePSXeViewGL.this.f165e.openglinit(ePSXeViewGL.this.emu_screen_vrmode,
                    ePSXeViewGL.this.emu_screen_vrdistorsion);
            int vertexShader = loadShader(35633,
                    "attribute vec4 a_position;attribute vec2 a_texCoord;varying vec2 v_texCoord;void main(){    gl_Position = a_position;    v_texCoord = a_texCoord;}");
            int fragmentShader = loadShader(35632,
                    "precision mediump float;varying vec2 v_texCoord;uniform sampler2D Frame;uniform float padAlpha;void main(){    gl_FragColor = texture2D(Frame, v_texCoord) * padAlpha;}");
            this.mProgram = GLES20.glCreateProgram();
            GLES20.glAttachShader(this.mProgram, vertexShader);
            GLES20.glAttachShader(this.mProgram, fragmentShader);
            GLES20.glLinkProgram(this.mProgram);
            this.mpadPositionLoc = GLES20.glGetAttribLocation(this.mProgram, "a_position");
            this.mpadTexCoordLoc = GLES20.glGetAttribLocation(this.mProgram, "a_texCoord");
            this.mpadFrameLoc = GLES20.glGetUniformLocation(this.mProgram, "Frame");
            this.mpadAlphaLoc = GLES20.glGetUniformLocation(this.mProgram, "padAlpha");
            this.glText = new GLText2(gl, ePSXeViewGL.this.mContext.getAssets(), this.mProgram, this.mpadPositionLoc,
                    this.mpadTexCoordLoc, this.mpadFrameLoc, this.mpadAlphaLoc, 1.0f);
            this.glText.load("Roboto-Regular.ttf", 28, 2, 2);
            if (ePSXeViewGL.this.emu_player_mode != 1 || ePSXeViewGL.this.emu_screen_orientation != 1
                    || ePSXeViewGL.this.emu_portrait_skin != 0) {
                if (ePSXeViewGL.this.emu_pad_mode[0] != 3) {
                    if (ePSXeViewGL.this.emu_pad_mode[0] == 8) {
                        int[] padCoords = { 384, 64, 448, 256, 448, 64, 511, 256 };
                        this.mTexLan = loadTexture(ePSXeViewGL.this.mContext, R.drawable.extra_buttons);
                        for (int i = 0; i < 2; i++) {
                            this.wTexLan[i] = padCoords[(i * 4) + 2] - padCoords[(i * 4) + 0];
                            this.hTexLan[i] = padCoords[(i * 4) + 3] - padCoords[(i * 4) + 1];
                            this.textureRgnLan[i] = new TextureRegion(this.wTextmp, this.hTextmp,
                                    padCoords[(i * 4) + 0], padCoords[(i * 4) + 1], this.wTexLan[i], this.hTexLan[i]);
                            this.batchLan[i] = new SpriteBatch2(gl, 1, this.mProgram, this.mpadPositionLoc,
                                    this.mpadTexCoordLoc, this.mpadFrameLoc, this.mpadAlphaLoc,
                                    ePSXeViewGL.this.emu_input_alpha);
                        }
                        return;
                    }
                    int[] padCoords2 = {
                            // x1, y1, x2, y2
                            2, 1, 224, 225, // Dpad Up, Down, Left, Right - keys
                            1, 238, 223, 486, // Action keys
                            253, 8, 309, 52, // Stop key
                            372, 4, 433, 56, // Play key
                            249, 80, 307, 132, // L1 key
                            308, 80, 364, 132, // L2 key
                            308, 144, 364, 196, // R1 key
                            250, 144, 307, 196, // R2 key
                            254, 208, 297, 239, // LED1
                            365, 80, 421, 132, // L3 key
                            365, 144, 422, 196, // R3 key
                            289, 289, 511, 511, // Touch pad area
                            289, 289, 511, 511, // ------"-------
                            422, 144, 491, 215 // Some ball button
                    };
                    int[] padCoordsAction = {
                            77, 244, 149, 316, // Action - triangle
                            146, 328, 218, 400, // Action - circle
                            77, 412, 149, 484, // Action - x
                            6, 328, 78, 400 // Action - square
                    };
                    int[] padCoordsDpad = {
                            76, 7, 146, 94, // Dpad - up
                            130, 80, 218, 149, // Dpad - right
                            76, 134, 146, 220, // Dpad - down
                            4, 80, 92, 149 // Dpad - left
                    };
                    if (ePSXeViewGL.this.emu_pad_draw_mode[0] == 4) {
                        File f = new File(ePSXeViewGL.this.skinName);
                        if (f.exists()) {
                            this.mTexLan = loadTextureFromFile(ePSXeViewGL.this.mContext, ePSXeViewGL.this.skinName);
                        } else {
                            ePSXeViewGL.this.emu_pad_draw_mode[0] = 0;
                        }
                    }
                    if (ePSXeViewGL.this.emu_pad_draw_mode[0] == 0) {
                        this.mTexLan = loadTexture(ePSXeViewGL.this.mContext, R.drawable.pure_white_digital);
                    } else if (ePSXeViewGL.this.emu_pad_draw_mode[0] == 1) {
                        this.mTexLan = loadTexture(ePSXeViewGL.this.mContext, R.drawable.pure_white_analog);
                    } else if (ePSXeViewGL.this.emu_pad_draw_mode[0] == 10) {
                        this.mTexLan = loadTexture(ePSXeViewGL.this.mContext, R.drawable.amethyst_crystal_digital);
                    } else if (ePSXeViewGL.this.emu_pad_draw_mode[0] == 11) {
                        this.mTexLan = loadTexture(ePSXeViewGL.this.mContext, R.drawable.amethyst_crystal_analog);
                    } else if (ePSXeViewGL.this.emu_pad_draw_mode[0] == 12) {
                        this.mTexLan = loadTexture(ePSXeViewGL.this.mContext, R.drawable.binchotite_digital);
                    } else if (ePSXeViewGL.this.emu_pad_draw_mode[0] == 13) {
                        this.mTexLan = loadTexture(ePSXeViewGL.this.mContext, R.drawable.binchotite_analog);
                    } else if (ePSXeViewGL.this.emu_pad_draw_mode[0] == 14) {
                        this.mTexLan = loadTexture(ePSXeViewGL.this.mContext, R.drawable.bold_amethyst_digital);
                    } else if (ePSXeViewGL.this.emu_pad_draw_mode[0] == 15) {
                        this.mTexLan = loadTexture(ePSXeViewGL.this.mContext, R.drawable.bold_amethyst_analog);
                    } else if (ePSXeViewGL.this.emu_pad_draw_mode[0] == 16) {
                        this.mTexLan = loadTexture(ePSXeViewGL.this.mContext, R.drawable.bold_digital);
                    } else if (ePSXeViewGL.this.emu_pad_draw_mode[0] == 17) {
                        this.mTexLan = loadTexture(ePSXeViewGL.this.mContext, R.drawable.bold_analog);
                    } else if (ePSXeViewGL.this.emu_pad_draw_mode[0] == 18) {
                        this.mTexLan = loadTexture(ePSXeViewGL.this.mContext, R.drawable.bold_emerald_digital);
                    } else if (ePSXeViewGL.this.emu_pad_draw_mode[0] == 19) {
                        this.mTexLan = loadTexture(ePSXeViewGL.this.mContext, R.drawable.bold_emerald_analog);
                    } else if (ePSXeViewGL.this.emu_pad_draw_mode[0] == 20) {
                        this.mTexLan = loadTexture(ePSXeViewGL.this.mContext, R.drawable.bold_gold_digital);
                    } else if (ePSXeViewGL.this.emu_pad_draw_mode[0] == 21) {
                        this.mTexLan = loadTexture(ePSXeViewGL.this.mContext, R.drawable.bold_gold_analog);
                    } else if (ePSXeViewGL.this.emu_pad_draw_mode[0] == 22) {
                        this.mTexLan = loadTexture(ePSXeViewGL.this.mContext, R.drawable.bold_pink_digital);
                    } else if (ePSXeViewGL.this.emu_pad_draw_mode[0] == 23) {
                        this.mTexLan = loadTexture(ePSXeViewGL.this.mContext, R.drawable.bold_pink_analog);
                    } else if (ePSXeViewGL.this.emu_pad_draw_mode[0] == 24) {
                        this.mTexLan = loadTexture(ePSXeViewGL.this.mContext, R.drawable.bold_sapphire_digital);
                    } else if (ePSXeViewGL.this.emu_pad_draw_mode[0] == 25) {
                        this.mTexLan = loadTexture(ePSXeViewGL.this.mContext, R.drawable.bold_sapphire_analog);
                    } else if (ePSXeViewGL.this.emu_pad_draw_mode[0] == 26) {
                        this.mTexLan = loadTexture(ePSXeViewGL.this.mContext, R.drawable.bold_silk_digital);
                    } else if (ePSXeViewGL.this.emu_pad_draw_mode[0] == 27) {
                        this.mTexLan = loadTexture(ePSXeViewGL.this.mContext, R.drawable.bold_silk_analog);
                    } else if (ePSXeViewGL.this.emu_pad_draw_mode[0] == 28) {
                        this.mTexLan = loadTexture(ePSXeViewGL.this.mContext, R.drawable.bold_turquoise_digital);
                    } else if (ePSXeViewGL.this.emu_pad_draw_mode[0] == 29) {
                        this.mTexLan = loadTexture(ePSXeViewGL.this.mContext, R.drawable.bold_turquoise_analog);
                    } else if (ePSXeViewGL.this.emu_pad_draw_mode[0] == 30) {
                        this.mTexLan = loadTexture(ePSXeViewGL.this.mContext, R.drawable.emerald_crystal_digital);
                    } else if (ePSXeViewGL.this.emu_pad_draw_mode[0] == 31) {
                        this.mTexLan = loadTexture(ePSXeViewGL.this.mContext, R.drawable.emerald_crystal_analog);
                    } else if (ePSXeViewGL.this.emu_pad_draw_mode[0] == 32) {
                        this.mTexLan = loadTexture(ePSXeViewGL.this.mContext, R.drawable.gold_crystal_digital);
                    } else if (ePSXeViewGL.this.emu_pad_draw_mode[0] == 33) {
                        this.mTexLan = loadTexture(ePSXeViewGL.this.mContext, R.drawable.gold_crystal_analog);
                    } else if (ePSXeViewGL.this.emu_pad_draw_mode[0] == 34) {
                        this.mTexLan = loadTexture(ePSXeViewGL.this.mContext, R.drawable.negative_crystal_digital);
                    } else if (ePSXeViewGL.this.emu_pad_draw_mode[0] == 35) {
                        this.mTexLan = loadTexture(ePSXeViewGL.this.mContext, R.drawable.negative_crystal_analog);
                    } else if (ePSXeViewGL.this.emu_pad_draw_mode[0] == 36) {
                        this.mTexLan = loadTexture(ePSXeViewGL.this.mContext, R.drawable.pink_crystal_digital);
                    } else if (ePSXeViewGL.this.emu_pad_draw_mode[0] == 37) {
                        this.mTexLan = loadTexture(ePSXeViewGL.this.mContext, R.drawable.pink_crystal_analog);
                    } else if (ePSXeViewGL.this.emu_pad_draw_mode[0] == 38) {
                        this.mTexLan = loadTexture(ePSXeViewGL.this.mContext, R.drawable.sapphire_crystal_digital);
                    } else if (ePSXeViewGL.this.emu_pad_draw_mode[0] == 39) {
                        this.mTexLan = loadTexture(ePSXeViewGL.this.mContext, R.drawable.sapphire_crystal_analog);
                    } else if (ePSXeViewGL.this.emu_pad_draw_mode[0] == 40) {
                        this.mTexLan = loadTexture(ePSXeViewGL.this.mContext, R.drawable.silk_crystal_digital);
                    } else if (ePSXeViewGL.this.emu_pad_draw_mode[0] == 41) {
                        this.mTexLan = loadTexture(ePSXeViewGL.this.mContext, R.drawable.silk_crystal_analog);
                    } else if (ePSXeViewGL.this.emu_pad_draw_mode[0] == 42) {
                        this.mTexLan = loadTexture(ePSXeViewGL.this.mContext, R.drawable.snow_digital);
                    } else if (ePSXeViewGL.this.emu_pad_draw_mode[0] == 43) {
                        this.mTexLan = loadTexture(ePSXeViewGL.this.mContext, R.drawable.snow_analog);
                    } else if (ePSXeViewGL.this.emu_pad_draw_mode[0] == 44) {
                        this.mTexLan = loadTexture(ePSXeViewGL.this.mContext, R.drawable.turquoise_crystal_digital);
                    } else if (ePSXeViewGL.this.emu_pad_draw_mode[0] == 45) {
                        this.mTexLan = loadTexture(ePSXeViewGL.this.mContext, R.drawable.turquoise_crystal_analog);
                    } else if (ePSXeViewGL.this.emu_pad_draw_mode[0] != 4) {
                        this.mTexLan = loadTexture(ePSXeViewGL.this.mContext, R.drawable.pure_white_digital);
                    }
                    if ((ePSXeViewGL.this.emu_pad_draw_mode[0] & 1) == 0 || (ePSXeViewGL.this.emu_pad_draw_mode[0] < 10
                            && ePSXeViewGL.this.emu_pad_draw_mode[0] != 1)) {
                        ePSXeViewGL.this.dpadskin = 1;
                    }
                    for (int i2 = 0; i2 < 14; i2++) {
                        this.wTexLan[i2] = padCoords2[(i2 * 4) + 2] - padCoords2[(i2 * 4) + 0];
                        this.hTexLan[i2] = padCoords2[(i2 * 4) + 3] - padCoords2[(i2 * 4) + 1];
                        this.textureRgnLan[i2] = new TextureRegion(this.wTextmp, this.hTextmp, padCoords2[(i2 * 4) + 0],
                                padCoords2[(i2 * 4) + 1], this.wTexLan[i2], this.hTexLan[i2]);
                        this.batchLan[i2] = new SpriteBatch2(gl, 1, this.mProgram, this.mpadPositionLoc,
                                this.mpadTexCoordLoc, this.mpadFrameLoc, this.mpadAlphaLoc,
                                ePSXeViewGL.this.emu_input_alpha);
                    }
                    for (int i3 = 0; i3 < 4; i3++) {
                        this.wTexLanAction[i3] = padCoordsAction[(i3 * 4) + 2] - padCoordsAction[(i3 * 4) + 0];
                        this.hTexLanAction[i3] = padCoordsAction[(i3 * 4) + 3] - padCoordsAction[(i3 * 4) + 1];
                        this.textureRgnLanAction[i3] = new TextureRegion(this.wTextmp, this.hTextmp,
                                padCoordsAction[(i3 * 4) + 0], padCoordsAction[(i3 * 4) + 1], this.wTexLanAction[i3],
                                this.hTexLanAction[i3]);
                        this.batchLanAction[i3] = new SpriteBatch2(gl, 1, this.mProgram, this.mpadPositionLoc,
                                this.mpadTexCoordLoc, this.mpadFrameLoc, this.mpadAlphaLoc,
                                ePSXeViewGL.this.emu_input_alpha);
                        this.sizeActionX[i3] = (float) ((padCoordsAction[(i3 * 4) + 2] - padCoords2[4])
                                - (padCoordsAction[(i3 * 4) + 0] - padCoords2[4])) / (padCoords2[6] - padCoords2[4]);
                        this.sizeActionY[i3] = (float) ((padCoordsAction[(i3 * 4) + 3] - padCoords2[5])
                                - (padCoordsAction[(i3 * 4) + 1] - padCoords2[5])) / (padCoords2[7] - padCoords2[5]);
                        this.offActionX[i3] = (float) ((padCoordsAction[(i3 * 4) + 0] - padCoords2[4])
                                + (((padCoordsAction[(i3 * 4) + 2] - padCoords2[4])
                                        - (padCoordsAction[(i3 * 4) + 0] - padCoords2[4])) / 2))
                                / (padCoords2[6] - padCoords2[4]);
                        this.offActionY[i3] = 1.0f - ((float) ((padCoordsAction[(i3 * 4) + 1] - padCoords2[5])
                                + (((padCoordsAction[(i3 * 4) + 3] - padCoords2[5])
                                        - (padCoordsAction[(i3 * 4) + 1] - padCoords2[5])) / 2))
                                / (padCoords2[7] - padCoords2[5]));
                    }
                    for (int i4 = 0; i4 < 4; i4++) {
                        this.wTexLanDpad[i4] = padCoordsDpad[(i4 * 4) + 2] - padCoordsDpad[(i4 * 4) + 0];
                        this.hTexLanDpad[i4] = padCoordsDpad[(i4 * 4) + 3] - padCoordsDpad[(i4 * 4) + 1];
                        this.textureRgnLanDpad[i4] = new TextureRegion(this.wTextmp, this.hTextmp,
                                padCoordsDpad[(i4 * 4) + 0], padCoordsDpad[(i4 * 4) + 1], this.wTexLanDpad[i4],
                                this.hTexLanDpad[i4]);
                        this.batchLanDpad[i4] = new SpriteBatch2(gl, 1, this.mProgram, this.mpadPositionLoc,
                                this.mpadTexCoordLoc, this.mpadFrameLoc, this.mpadAlphaLoc,
                                ePSXeViewGL.this.emu_input_alpha);
                        this.sizeDpadX[i4] = (float) ((padCoordsDpad[(i4 * 4) + 2] - padCoords2[0])
                                - (padCoordsDpad[(i4 * 4) + 0] - padCoords2[0])) / (padCoords2[2] - padCoords2[0]);
                        this.sizeDpadY[i4] = (float) ((padCoordsDpad[(i4 * 4) + 3] - padCoords2[1])
                                - (padCoordsDpad[(i4 * 4) + 1] - padCoords2[1])) / (padCoords2[3] - padCoords2[1]);
                        this.offDpadX[i4] = (float) ((padCoordsDpad[(i4 * 4) + 0] - padCoords2[0])
                                + (((padCoordsDpad[(i4 * 4) + 2] - padCoords2[0])
                                        - (padCoordsDpad[(i4 * 4) + 0] - padCoords2[0])) / 2))
                                / (padCoords2[2] - padCoords2[0]);
                        this.offDpadY[i4] = 1.0f - ((float) ((padCoordsDpad[(i4 * 4) + 1] - padCoords2[1])
                                + (((padCoordsDpad[(i4 * 4) + 3] - padCoords2[1])
                                        - (padCoordsDpad[(i4 * 4) + 1] - padCoords2[1])) / 2))
                                / (padCoords2[3] - padCoords2[1]));
                    }
                    loadExtraButtons();
                    this.mTexExtra = loadTexture(ePSXeViewGL.this.mContext, R.drawable.extra_buttons);
                    for (int i5 = 0; i5 < 6; i5++) {
                        int j = ePSXeViewGL.this.padScreenExtra[i5];
                        if (j >= 0) {
                            if (j >= 24) {
                                j += 4;
                            }
                            this.wTexLan[i5 + 14] = this.padCoordsExtra[(j * 4) + 2] - this.padCoordsExtra[(j * 4) + 0];
                            this.hTexLan[i5 + 14] = this.padCoordsExtra[(j * 4) + 3] - this.padCoordsExtra[(j * 4) + 1];
                            this.textureRgnLan[i5 + 14] = new TextureRegion(this.wTextmp, this.hTextmp,
                                    this.padCoordsExtra[(j * 4) + 0], this.padCoordsExtra[(j * 4) + 1],
                                    this.wTexLan[i5 + 14], this.hTexLan[i5 + 14]);
                            this.batchLan[i5 + 14] = new SpriteBatch2(gl, 1, this.mProgram, this.mpadPositionLoc,
                                    this.mpadTexCoordLoc, this.mpadFrameLoc, this.mpadAlphaLoc,
                                    ePSXeViewGL.this.emu_input_alpha);
                        }
                    }
                    for (int i6 = 0; i6 < 4; i6++) {
                        int j2 = i6 + 24;
                        this.wTexLan[i6 + 20] = this.padCoordsExtra[(j2 * 4) + 2] - this.padCoordsExtra[(j2 * 4) + 0];
                        this.hTexLan[i6 + 20] = this.padCoordsExtra[(j2 * 4) + 3] - this.padCoordsExtra[(j2 * 4) + 1];
                        this.textureRgnLan[i6 + 20] = new TextureRegion(this.wTextmp, this.hTextmp,
                                this.padCoordsExtra[(j2 * 4) + 0], this.padCoordsExtra[(j2 * 4) + 1],
                                this.wTexLan[i6 + 20], this.hTexLan[i6 + 20]);
                        this.batchLan[i6 + 20] = new SpriteBatch2(gl, 1, this.mProgram, this.mpadPositionLoc,
                                this.mpadTexCoordLoc, this.mpadFrameLoc, this.mpadAlphaLoc,
                                ePSXeViewGL.this.emu_input_alpha);
                    }
                    for (int i7 = 0; i7 < 4; i7++) {
                        int j3 = i7 + 32;
                        this.wTexLan[i7 + 24] = this.padCoordsExtra[(j3 * 4) + 2] - this.padCoordsExtra[(j3 * 4) + 0];
                        this.hTexLan[i7 + 24] = this.padCoordsExtra[(j3 * 4) + 3] - this.padCoordsExtra[(j3 * 4) + 1];
                        this.textureRgnLan[i7 + 24] = new TextureRegion(this.wTextmp, this.hTextmp,
                                this.padCoordsExtra[(j3 * 4) + 0], this.padCoordsExtra[(j3 * 4) + 1],
                                this.wTexLan[i7 + 24], this.hTexLan[i7 + 24]);
                        this.batchLan[i7 + 24] = new SpriteBatch2(gl, 1, this.mProgram, this.mpadPositionLoc,
                                this.mpadTexCoordLoc, this.mpadFrameLoc, this.mpadAlphaLoc,
                                ePSXeViewGL.this.emu_input_alpha);
                    }
                    return;
                }
                int[] padCoords3 = {
                        256, 64, 320, 256,
                        320, 64, 384, 256
                };
                this.mTexLan = loadTexture(ePSXeViewGL.this.mContext, R.drawable.extra_buttons);
                for (int i8 = 0; i8 < 2; i8++) {
                    this.wTexLan[i8] = padCoords3[(i8 * 4) + 2] - padCoords3[(i8 * 4) + 0];
                    this.hTexLan[i8] = padCoords3[(i8 * 4) + 3] - padCoords3[(i8 * 4) + 1];
                    this.textureRgnLan[i8] = new TextureRegion(this.wTextmp, this.hTextmp, padCoords3[(i8 * 4) + 0],
                            padCoords3[(i8 * 4) + 1], this.wTexLan[i8], this.hTexLan[i8]);
                    this.batchLan[i8] = new SpriteBatch2(gl, 1, this.mProgram, this.mpadPositionLoc,
                            this.mpadTexCoordLoc, this.mpadFrameLoc, this.mpadAlphaLoc,
                            ePSXeViewGL.this.emu_input_alpha);
                }
                return;
            }
            int[] padCoords4 = { 0, 0, 480, 400 };
            this.mTexPor = loadTexture(ePSXeViewGL.this.mContext, R.drawable.portraitpad);
            this.wTexPor = padCoords4[2] - padCoords4[0];
            this.hTexPor = padCoords4[3] - padCoords4[1];
            this.textureRgnPor = new TextureRegion(this.wTextmp, this.hTextmp, padCoords4[0], padCoords4[1],
                    this.wTexPor, this.hTexPor);
            this.batchPor = new SpriteBatch2(gl, 1, this.mProgram, this.mpadPositionLoc, this.mpadTexCoordLoc,
                    this.mpadFrameLoc, this.mpadAlphaLoc, 1.0f);
            loadExtraButtons();
            this.mTexExtra = loadTexture(ePSXeViewGL.this.mContext, R.drawable.extra_buttons);
            loadExtraButtonsTextures(gl);
            for (int i9 = 0; i9 < 4; i9++) {
                int j4 = i9 + 24;
                this.wTexLan[i9 + 20] = this.padCoordsExtra[(j4 * 4) + 2] - this.padCoordsExtra[(j4 * 4) + 0];
                this.hTexLan[i9 + 20] = this.padCoordsExtra[(j4 * 4) + 3] - this.padCoordsExtra[(j4 * 4) + 1];
                this.textureRgnLan[i9 + 20] = new TextureRegion(this.wTextmp, this.hTextmp,
                        this.padCoordsExtra[(j4 * 4) + 0], this.padCoordsExtra[(j4 * 4) + 1], this.wTexLan[i9 + 20],
                        this.hTexLan[i9 + 20]);
                this.batchLan[i9 + 20] = new SpriteBatch2(gl, 1, this.mProgram, this.mpadPositionLoc,
                        this.mpadTexCoordLoc, this.mpadFrameLoc, this.mpadAlphaLoc, ePSXeViewGL.this.emu_input_alpha);
            }
            for (int i10 = 0; i10 < 4; i10++) {
                int j5 = i10 + 32;
                this.wTexLan[i10 + 24] = this.padCoordsExtra[(j5 * 4) + 2] - this.padCoordsExtra[(j5 * 4) + 0];
                this.hTexLan[i10 + 24] = this.padCoordsExtra[(j5 * 4) + 3] - this.padCoordsExtra[(j5 * 4) + 1];
                this.textureRgnLan[i10 + 24] = new TextureRegion(this.wTextmp, this.hTextmp,
                        this.padCoordsExtra[(j5 * 4) + 0], this.padCoordsExtra[(j5 * 4) + 1], this.wTexLan[i10 + 24],
                        this.hTexLan[i10 + 24]);
                this.batchLan[i10 + 24] = new SpriteBatch2(gl, 1, this.mProgram, this.mpadPositionLoc,
                        this.mpadTexCoordLoc, this.mpadFrameLoc, this.mpadAlphaLoc, ePSXeViewGL.this.emu_input_alpha);
            }
        }
    }

    private class ePSXeRenderer implements GLSurfaceView.Renderer {

        SpriteBatch[] batchLan;
        SpriteBatch[] batchLanAction;
        SpriteBatch[] batchLanDpad;
        SpriteBatch batchPor;
        private GLText glText;
        int[] hTexLan;
        int[] hTexLanAction;
        int[] hTexLanDpad;
        int hTexPor;
        int hTextmp;
        int mTexExtra;
        int mTexLan;
        int mTexPor;
        float[] offActionX;
        float[] offActionY;
        float[] offDpadX;
        float[] offDpadY;
        int[] padCoordsExtra;
        float[] sizeActionX;
        float[] sizeActionY;
        float[] sizeDpadX;
        float[] sizeDpadY;
        TextureRegion[] textureRgnLan;
        TextureRegion[] textureRgnLanAction;
        TextureRegion[] textureRgnLanDpad;
        TextureRegion textureRgnPor;
        int[] wTexLan;
        int[] wTexLanAction;
        int[] wTexLanDpad;
        int wTexPor;
        int wTextmp;

        private ePSXeRenderer() {
            this.mTexPor = -1;
            this.mTexLan = -1;
            this.mTexExtra = -1;
            this.batchLan = new SpriteBatch[28];
            this.textureRgnLan = new TextureRegion[28];
            this.hTexLan = new int[28];
            this.wTexLan = new int[28];
            this.batchLanAction = new SpriteBatch[4];
            this.textureRgnLanAction = new TextureRegion[4];
            this.hTexLanAction = new int[4];
            this.wTexLanAction = new int[4];
            this.sizeActionX = new float[4];
            this.sizeActionY = new float[4];
            this.offActionX = new float[4];
            this.offActionY = new float[4];
            this.batchLanDpad = new SpriteBatch[4];
            this.textureRgnLanDpad = new TextureRegion[4];
            this.hTexLanDpad = new int[4];
            this.wTexLanDpad = new int[4];
            this.sizeDpadX = new float[4];
            this.sizeDpadY = new float[4];
            this.offDpadX = new float[4];
            this.offDpadY = new float[4];
            this.padCoordsExtra = new int[] { 0, 0, 64, 64, 64, 0, 128, 64, 128, 0, InputList.KEYCODE_BUTTON_5, 64,
                    InputList.KEYCODE_BUTTON_5, 0, 256, 64, 0, 64, 64, 128, 64, 64, 128, 128, 128, 64,
                    InputList.KEYCODE_BUTTON_5, 128, InputList.KEYCODE_BUTTON_5, 64, 256, 128, 0, 128, 64,
                    InputList.KEYCODE_BUTTON_9, 64, 128, 128, InputList.KEYCODE_BUTTON_9, 128, 128,
                    InputList.KEYCODE_BUTTON_5, InputList.KEYCODE_BUTTON_9, InputList.KEYCODE_BUTTON_5, 128, 256,
                    InputList.KEYCODE_BUTTON_9, 0, InputList.KEYCODE_BUTTON_5, 64, 256, 64, InputList.KEYCODE_BUTTON_5,
                    128, 256, 128, InputList.KEYCODE_BUTTON_5, InputList.KEYCODE_BUTTON_5, 256,
                    InputList.KEYCODE_BUTTON_5, InputList.KEYCODE_BUTTON_5, 256, 256, 0, 256, 64, 320, 64, 256, 128,
                    320, 128, 256, InputList.KEYCODE_BUTTON_7, 320, InputList.KEYCODE_BUTTON_5, 256, 256, 320, 0, 320,
                    64, 384, 64, 320, 128, 384, 128, 320, InputList.KEYCODE_BUTTON_9, 384, InputList.KEYCODE_BUTTON_5,
                    320, 256, 384, 0, 384, 64, 448, 63, 384, 126, 448, 128, 384, InputList.KEYCODE_BUTTON_9, 448,
                    InputList.KEYCODE_BUTTON_5, 384, 256, 448, 0, 448, 64, 511, 64, 448, 128, 511, 128, 448,
                    InputList.KEYCODE_BUTTON_9, 511, InputList.KEYCODE_BUTTON_5, 448, 256, 511, 256, 0, 320, 64, 320, 0,
                    384, 64, 384, 0, 448, 64, 448, 0, 511, 64 };
        }

        private int newTextureID(GL10 gl) {
            int[] temp = new int[1];
            gl.glGenTextures(1, temp, 0);
            return temp[0];
        }

        private int loadTexture(GL10 gl, Context context, int resource) {
            int id = newTextureID(gl);
            Matrix flip = new Matrix();
            flip.setTranslate(1.0f, -1.0f);
            flip.postScale(1.0f, 1.0f);
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inScaled = false;
            Bitmap temp = BitmapFactory.decodeResource(context.getResources(), resource, opts);
            Bitmap bmp = Bitmap.createBitmap(temp, 0, 0, temp.getWidth(), temp.getHeight(), flip, true);
            temp.recycle();
            gl.glBindTexture(3553, id);
            gl.glTexParameterf(3553, 10241, 9729.0f);
            gl.glTexParameterf(3553, 10240, 9729.0f);
            gl.glTexParameterf(3553, 10242, 10497.0f);
            gl.glTexParameterf(3553, 10243, 10497.0f);
            this.wTextmp = bmp.getWidth();
            this.hTextmp = bmp.getHeight();
            GLUtils.texImage2D(3553, 0, bmp, 0);
            gl.glBindTexture(3553, 0);
            bmp.recycle();
            return id;
        }

        private int loadTextureFromFile(GL10 gl, Context context, String filename) {
            int id = newTextureID(gl);
            Matrix flip = new Matrix();
            flip.setTranslate(1.0f, -1.0f);
            flip.postScale(1.0f, 1.0f);
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inScaled = false;
            Bitmap temp = BitmapFactory.decodeFile(filename);
            Bitmap bmp = Bitmap.createBitmap(temp, 0, 0, temp.getWidth(), temp.getHeight(), flip, true);
            temp.recycle();
            gl.glBindTexture(3553, id);
            gl.glTexParameterf(3553, 10241, 9729.0f);
            gl.glTexParameterf(3553, 10240, 9729.0f);
            gl.glTexParameterf(3553, 10242, 10497.0f);
            gl.glTexParameterf(3553, 10243, 10497.0f);
            this.wTextmp = bmp.getWidth();
            this.hTextmp = bmp.getHeight();
            GLUtils.texImage2D(3553, 0, bmp, 0);
            gl.glBindTexture(3553, 0);
            bmp.recycle();
            return id;
        }

        public void drawFPS(GL10 gl) {
            gl.glEnable(3553);
            gl.glEnable(3042);
            gl.glBlendFunc(770, 771);
            this.glText.begin(1.0f, 0.8f, 0.8f, 0.8f);
            if (ePSXeViewGL.this.tainted == 1) {
                this.glText.draw(String.valueOf(ePSXeViewGL.this.f165e.getFPS()) + "/" + ePSXeViewGL.this.mfps + "*",
                        ePSXeViewGL.this.overscan_x + 0,
                        (ePSXeViewGL.this.mHeight - this.glText.getCharHeight()) - ePSXeViewGL.this.overscan_y);
            } else {
                this.glText.draw(String.valueOf(ePSXeViewGL.this.f165e.getFPS()) + "/" + ePSXeViewGL.this.mfps,
                        ePSXeViewGL.this.overscan_x + 0,
                        (ePSXeViewGL.this.mHeight - this.glText.getCharHeight()) - ePSXeViewGL.this.overscan_y);
            }
            this.glText.end();
            gl.glDisable(3042);
            gl.glDisable(3553);
        }

        public void drawVolumen(GL10 gl) {
            gl.glEnable(3553);
            gl.glEnable(3042);
            gl.glBlendFunc(770, 771);
            this.glText.begin(1.0f, 0.8f, 0.8f, 0.8f);
            if (ePSXeViewGL.this.tainted == 1) {
                this.glText.draw(String.valueOf(ePSXeViewGL.this.f165e.getFPS()) + "/" + ePSXeViewGL.this.mfps + "*",
                        ePSXeViewGL.this.overscan_x + 0,
                        (ePSXeViewGL.this.mHeight - this.glText.getCharHeight()) - ePSXeViewGL.this.overscan_y);
            } else {
                this.glText.draw(String.valueOf(ePSXeViewGL.this.emu_volumen) + "/16",
                        ePSXeViewGL.this.mWidth - (this.glText.getCharHeight() * 6.0f),
                        (ePSXeViewGL.this.mHeight - this.glText.getCharHeight()) - ePSXeViewGL.this.overscan_y);
            }
            this.glText.end();
            gl.glDisable(3042);
            gl.glDisable(3553);
        }

        public void drawAnalogValues(GL10 gl) {
            gl.glEnable(3553);
            gl.glEnable(3042);
            gl.glBlendFunc(770, 771);
            this.glText.begin(1.0f, 1.0f, 1.0f, 1.0f);
            this.glText.draw(
                    "lx " + ePSXeViewGL.this.f166x1 + " ly " + ePSXeViewGL.this.f168y1 + " rx "
                            + ePSXeViewGL.this.f167x2 + " ry " + ePSXeViewGL.this.f169y2,
                    0.0f, ePSXeViewGL.this.mHeight - (this.glText.getCharHeight() * 3.0f));
            this.glText.end();
            gl.glDisable(3042);
            gl.glDisable(3553);
        }

        public void drawDebugString(GL10 gl) {
            gl.glEnable(3553);
            gl.glEnable(3042);
            gl.glBlendFunc(770, 771);
            this.glText.begin(1.0f, 1.0f, 1.0f, 1.0f);
            this.glText.draw(ePSXeViewGL.this.debugString, 0.0f,
                    ePSXeViewGL.this.mHeight - (this.glText.getCharHeight() * 3.0f));
            this.glText.end();
            gl.glDisable(3042);
            gl.glDisable(3553);
        }

        public void drawDebugString2(GL10 gl) {
            gl.glEnable(3553);
            gl.glEnable(3042);
            gl.glBlendFunc(770, 771);
            this.glText.begin(1.0f, 1.0f, 1.0f, 1.0f);
            this.glText.draw(ePSXeViewGL.this.debugString2, 0.0f,
                    ePSXeViewGL.this.mHeight - (this.glText.getCharHeight() * 4.0f));
            this.glText.end();
            gl.glDisable(3042);
            gl.glDisable(3553);
        }

        public void drawLicense(GL10 gl) {
            gl.glEnable(3553);
            gl.glEnable(3042);
            gl.glBlendFunc(770, 771);
            this.glText.begin(1.0f, 1.0f, 1.0f, 1.0f);
            this.glText.draw("License not validated yet. Read the documentation.", 60.0f, 0.0f);
            this.glText.end();
            gl.glDisable(3042);
            gl.glDisable(3553);
        }

        public void drawBiosMsg(GL10 gl) {
            gl.glEnable(3553);
            gl.glEnable(3042);
            gl.glBlendFunc(770, 771);
            this.glText.begin(1.0f, 1.0f, 1.0f, 1.0f);
            this.glText.draw("Using HLE Bios, compatibility and options limited. Read the documentation.", 60.0f, 0.0f);
            this.glText.end();
            gl.glDisable(3042);
            gl.glDisable(3553);
        }

        public void drawString(GL10 gl, String s) {
            gl.glEnable(3553);
            gl.glEnable(3042);
            gl.glBlendFunc(770, 771);
            this.glText.begin(1.0f, 1.0f, 1.0f, 1.0f);
            this.glText.draw(s, 60.0f, 0.0f);
            this.glText.end();
            gl.glDisable(3042);
            gl.glDisable(3553);
        }

        public void drawDeviceName(GL10 gl) {
            gl.glEnable(3553);
            gl.glEnable(3042);
            gl.glBlendFunc(770, 771);
            this.glText.begin(1.0f, 1.0f, 1.0f, 1.0f);
            this.glText.draw(Build.DEVICE, 60.0f, 0.0f);
            this.glText.end();
            gl.glDisable(3042);
            gl.glDisable(3553);
        }

        @Override // android.opengl.GLSurfaceView.Renderer
        public void onDrawFrame(GL10 gl) {
            if (ePSXeViewGL.this.onPauseMode != 1) {
                gl.glClear(16384);
                if (ePSXeViewGL.this.redoPads) {
                    redoPads(gl);
                    ePSXeViewGL.this.redoPads = false;
                }
                if (ePSXeViewGL.this.serverMode != 2) {
                    ePSXeViewGL.this.f165e.openglrender(ePSXeViewGL.this.emu_player_mode,
                            ePSXeViewGL.this.emu_screen_orientation, ePSXeViewGL.this.emu_split_mode,
                            ePSXeViewGL.this.emu_screen_ratio, ePSXeViewGL.this.emu_screen_vrmode);
                }
                if (ePSXeViewGL.this.onPauseMode != 1) {
                    if (ePSXeViewGL.this.emu_screen_vrmode == 0) {
                        if (ePSXeViewGL.this.emu_player_mode != 1 || ePSXeViewGL.this.emu_screen_orientation != 1
                                || ePSXeViewGL.this.emu_portrait_skin != 0) {
                            if (ePSXeViewGL.this.emu_player_mode != 1 || ePSXeViewGL.this.emu_pad_draw_mode[0] == 2
                                    || ePSXeViewGL.this.emu_pad_mode[0] == 3 || ePSXeViewGL.this.emu_pad_mode[0] == 8) {
                                if (ePSXeViewGL.this.emu_pad_mode[0] == 3 || ePSXeViewGL.this.emu_pad_mode[0] == 8) {
                                    ePSXeViewGL.this.gun.drawGunGl(gl, this.mTexLan, this.textureRgnLan, this.batchLan,
                                            ePSXeViewGL.this.mWidth, ePSXeViewGL.this.mHeight,
                                            ePSXeViewGL.this.emu_input_alpha);
                                }
                            } else {
                                int st = 0;
                                int en = 8;
                                gl.glEnable(3553);
                                gl.glEnable(3042);
                                gl.glBlendFunc(770, 771);
                                gl.glColor4f(1.0f, 1.0f, 1.0f, ePSXeViewGL.this.emu_input_alpha);
                                if (ePSXeViewGL.this.emu_pad_mode[ePSXeViewGL.this.emu_pad_type_selected] == 4) {
                                    if (ePSXeViewGL.this.emu_pad_mode_analog[ePSXeViewGL.this.emu_pad_type_selected] == 0) {
                                        st = 0;
                                        en = 9;
                                    } else {
                                        st = 0;
                                        en = 13;
                                    }
                                }
                                if (!ePSXeViewGL.this.hidePad) {
                                    gl.glBindTexture(3553, this.mTexLan);
                                }
                                for (int i = st; i < en; i++) {
                                    if (ePSXeViewGL.this.padScreenStatus[ePSXeViewGL.this.mode][i] == 1) {
                                        if (i > 1) {
                                            if (i < 11 && i != 8) {
                                                if ((ePSXeViewGL.this.statebuttons
                                                        & ePSXeViewGL.this.psxbuttonval[i]) != 0) {
                                                    this.batchLan[i].beginBatch();
                                                    // Исправленная отрисовка нажатых кнопок для OpenGL 1.0 - используем
                                                    // оригинальные координаты с увеличением
                                                    this.batchLan[i].drawSprite(
                                                            ePSXeViewGL.this.padOffScreenLan[ePSXeViewGL.this.mode][(i
                                                                    * 2) + 0],
                                                            ePSXeViewGL.this.padOffScreenLan[ePSXeViewGL.this.mode][(i
                                                                    * 2) + 1],
                                                            ePSXeViewGL.this.padSizeScreenLan[ePSXeViewGL.this.mode][(i
                                                                    * 2) + 0]
                                                                    * ePSXeViewGL.this.padScreenResize[ePSXeViewGL.this.mode][i]
                                                                    * ePSXeViewGL.this.buttonMag,
                                                            ePSXeViewGL.this.padSizeScreenLan[ePSXeViewGL.this.mode][(i
                                                                    * 2) + 1]
                                                                    * ePSXeViewGL.this.padScreenResize[ePSXeViewGL.this.mode][i]
                                                                    * ePSXeViewGL.this.buttonMag,
                                                            this.textureRgnLan[i]);
                                                    this.batchLan[i].endBatch();
                                                } else {
                                                    this.batchLan[i].beginBatch();
                                                    // Исправленная отрисовка кнопок для OpenGL 1.0 - используем
                                                    // оригинальные координаты
                                                    this.batchLan[i].drawSprite(
                                                            ePSXeViewGL.this.padOffScreenLan[ePSXeViewGL.this.mode][(i
                                                                    * 2) + 0],
                                                            ePSXeViewGL.this.padOffScreenLan[ePSXeViewGL.this.mode][(i
                                                                    * 2) + 1],
                                                            ePSXeViewGL.this.padSizeScreenLan[ePSXeViewGL.this.mode][(i
                                                                    * 2) + 0]
                                                                    * ePSXeViewGL.this.padScreenResize[ePSXeViewGL.this.mode][i],
                                                            ePSXeViewGL.this.padSizeScreenLan[ePSXeViewGL.this.mode][(i
                                                                    * 2) + 1]
                                                                    * ePSXeViewGL.this.padScreenResize[ePSXeViewGL.this.mode][i],
                                                            this.textureRgnLan[i]);
                                                    this.batchLan[i].endBatch();
                                                }
                                            } else {
                                                this.batchLan[i].beginBatch();
                                                this.batchLan[i].drawSprite(
                                                        ePSXeViewGL.this.padOffScreenLan[ePSXeViewGL.this.mode][(i * 2)
                                                                + 0],
                                                        ePSXeViewGL.this.padOffScreenLan[ePSXeViewGL.this.mode][(i * 2)
                                                                + 1],
                                                        ePSXeViewGL.this.padSizeScreenLan[ePSXeViewGL.this.mode][(i * 2)
                                                                + 0]
                                                                * ePSXeViewGL.this.padScreenResize[ePSXeViewGL.this.mode][i],
                                                        ePSXeViewGL.this.padSizeScreenLan[ePSXeViewGL.this.mode][(i * 2)
                                                                + 1]
                                                                * ePSXeViewGL.this.padScreenResize[ePSXeViewGL.this.mode][i],
                                                        this.textureRgnLan[i]);
                                                this.batchLan[i].endBatch();
                                            }
                                        } else if (i == 0) {
                                            // Drawing DPAD - исправленная отрисовка для правильного совпадения с touch
                                            // областями
                                            if (ePSXeViewGL.this.emu_pad_draw_mode[0] == 4) {
                                                this.batchLan[i].beginBatch();
                                                this.batchLan[i].drawSprite(
                                                        ePSXeViewGL.this.padOffScreenLan[ePSXeViewGL.this.mode][(i * 2)
                                                                + 0],
                                                        ePSXeViewGL.this.padOffScreenLan[ePSXeViewGL.this.mode][(i * 2)
                                                                + 1],
                                                        ePSXeViewGL.this.padSizeScreenLan[ePSXeViewGL.this.mode][(i * 2)
                                                                + 0]
                                                                * ePSXeViewGL.this.padScreenResize[ePSXeViewGL.this.mode][i],
                                                        ePSXeViewGL.this.padSizeScreenLan[ePSXeViewGL.this.mode][(i * 2)
                                                                + 1]
                                                                * ePSXeViewGL.this.padScreenResize[ePSXeViewGL.this.mode][i],
                                                        this.textureRgnLan[i]);
                                                this.batchLan[i].endBatch();
                                            }
                                            if (ePSXeViewGL.this.dpadskin == 1) {
                                                float sx = ePSXeViewGL.this.padSizeScreenLan[ePSXeViewGL.this.mode][(i
                                                        * 2) + 0]
                                                        * ePSXeViewGL.this.padScreenResize[ePSXeViewGL.this.mode][i];
                                                float sy = ePSXeViewGL.this.padSizeScreenLan[ePSXeViewGL.this.mode][(i
                                                        * 2) + 1]
                                                        * ePSXeViewGL.this.padScreenResize[ePSXeViewGL.this.mode][i];
                                                // Исправляем координаты для правильного центрирования d-pad
                                                float centerX = ePSXeViewGL.this.padOffScreenLan[ePSXeViewGL.this.mode][(i
                                                        * 2) + 0];
                                                float centerY = ePSXeViewGL.this.padOffScreenLan[ePSXeViewGL.this.mode][(i
                                                        * 2) + 1];
                                                float ox = centerX - (sx / 2.0f);
                                                float oy = centerY - (sy / 2.0f);

                                                for (int p = 0; p < 4; p++) {
                                                    // Вычисляем правильные позиции для каждой кнопки d-pad
                                                    // Используем исходную логику с правильными смещениями
                                                    float buttonCenterX = (this.offDpadX[p] * sx) + ox;
                                                    float buttonCenterY = (this.offDpadY[p] * sy) + oy;
                                                    float buttonWidth = this.sizeDpadX[p] * sx;
                                                    float buttonHeight = this.sizeDpadY[p] * sy;

                                                    if (ePSXeViewGL.this.isDpadTouchActive
                                                            && ePSXeViewGL.this.animationButtonIndex != -1) {
                                                        int buttonMapping = -1;
                                                        if (ePSXeViewGL.this.animationButtonIndex == 12) {
                                                            buttonMapping = 0;
                                                        } else if (ePSXeViewGL.this.animationButtonIndex == 13) {
                                                            buttonMapping = 1;
                                                        } else if (ePSXeViewGL.this.animationButtonIndex == 14) {
                                                            buttonMapping = 2;
                                                        } else if (ePSXeViewGL.this.animationButtonIndex == 15) {
                                                            buttonMapping = 3;
                                                        }

                                                        if (buttonMapping == p) {
                                                            this.batchLanDpad[p].beginBatch();
                                                            this.batchLanDpad[p].drawSprite(
                                                                    buttonCenterX, buttonCenterY,
                                                                    buttonWidth * ePSXeViewGL.this.buttonMag,
                                                                    buttonHeight * ePSXeViewGL.this.buttonMag,
                                                                    this.textureRgnLanDpad[p]);
                                                            this.batchLanDpad[p].endBatch();
                                                        } else {
                                                            this.batchLanDpad[p].beginBatch();
                                                            this.batchLanDpad[p].drawSprite(
                                                                    buttonCenterX, buttonCenterY,
                                                                    buttonWidth, buttonHeight,
                                                                    this.textureRgnLanDpad[p]);
                                                            this.batchLanDpad[p].endBatch();
                                                        }
                                                    } else {
                                                        if ((ePSXeViewGL.this.statebuttons & (4096 << p)) == 0) {
                                                            if (ePSXeViewGL.this.emu_pad_draw_mode[0] != 4) {
                                                                this.batchLanDpad[p].beginBatch();
                                                                this.batchLanDpad[p].drawSprite(
                                                                        buttonCenterX, buttonCenterY,
                                                                        buttonWidth, buttonHeight,
                                                                        this.textureRgnLanDpad[p]);
                                                                this.batchLanDpad[p].endBatch();
                                                            }
                                                        } else {
                                                            this.batchLanDpad[p].beginBatch();
                                                            this.batchLanDpad[p].drawSprite(
                                                                    buttonCenterX, buttonCenterY,
                                                                    buttonWidth * ePSXeViewGL.this.buttonMag,
                                                                    buttonHeight * ePSXeViewGL.this.buttonMag,
                                                                    this.textureRgnLanDpad[p]);
                                                            this.batchLanDpad[p].endBatch();
                                                        }
                                                    }
                                                }
                                            } else {
                                                this.batchLan[i].beginBatch();
                                                this.batchLan[i].drawSprite(
                                                        ePSXeViewGL.this.padOffScreenLan[ePSXeViewGL.this.mode][(i * 2)
                                                                + 0],
                                                        ePSXeViewGL.this.padOffScreenLan[ePSXeViewGL.this.mode][(i * 2)
                                                                + 1],
                                                        ePSXeViewGL.this.padSizeScreenLan[ePSXeViewGL.this.mode][(i * 2)
                                                                + 0]
                                                                * ePSXeViewGL.this.padScreenResize[ePSXeViewGL.this.mode][i],
                                                        ePSXeViewGL.this.padSizeScreenLan[ePSXeViewGL.this.mode][(i * 2)
                                                                + 1]
                                                                * ePSXeViewGL.this.padScreenResize[ePSXeViewGL.this.mode][i],
                                                        this.textureRgnLan[i]);
                                                this.batchLan[i].endBatch();
                                            }
                                        } else {
                                            // Drawing ACTION KEYS
                                            if (ePSXeViewGL.this.emu_pad_draw_mode[0] == 4) {
                                                this.batchLan[i].beginBatch();
                                                this.batchLan[i].drawSprite(
                                                        ePSXeViewGL.this.padOffScreenLan[ePSXeViewGL.this.mode][(i * 2)
                                                                + 0],
                                                        ePSXeViewGL.this.padOffScreenLan[ePSXeViewGL.this.mode][(i * 2)
                                                                + 1],
                                                        ePSXeViewGL.this.padSizeScreenLan[ePSXeViewGL.this.mode][(i * 2)
                                                                + 0]
                                                                * ePSXeViewGL.this.padScreenResize[ePSXeViewGL.this.mode][i],
                                                        ePSXeViewGL.this.padSizeScreenLan[ePSXeViewGL.this.mode][(i * 2)
                                                                + 1]
                                                                * ePSXeViewGL.this.padScreenResize[ePSXeViewGL.this.mode][i],
                                                        this.textureRgnLan[i]);
                                                this.batchLan[i].endBatch();
                                            }
                                            float sx2 = ePSXeViewGL.this.padSizeScreenLan[ePSXeViewGL.this.mode][(i * 2)
                                                    + 0] * ePSXeViewGL.this.padScreenResize[ePSXeViewGL.this.mode][i];
                                            float sy2 = ePSXeViewGL.this.padSizeScreenLan[ePSXeViewGL.this.mode][(i * 2)
                                                    + 1] * ePSXeViewGL.this.padScreenResize[ePSXeViewGL.this.mode][i];
                                            // Исправляем координаты для правильного центрирования action buttons
                                            float centerX = ePSXeViewGL.this.padOffScreenLan[ePSXeViewGL.this.mode][(i
                                                    * 2) + 0];
                                            float centerY = ePSXeViewGL.this.padOffScreenLan[ePSXeViewGL.this.mode][(i
                                                    * 2) + 1];
                                            float ox2 = centerX - (sx2 / 2.0f);
                                            float oy2 = centerY - (sy2 / 2.0f);

                                            for (int p2 = 0; p2 < 4; p2++) {
                                                // Вычисляем правильные позиции для каждой action кнопки
                                                float buttonCenterX = (this.offActionX[p2] * sx2) + ox2;
                                                float buttonCenterY = (this.offActionY[p2] * sy2) + oy2;
                                                float buttonWidth = this.sizeActionX[p2] * sx2;
                                                float buttonHeight = this.sizeActionY[p2] * sy2;

                                                if ((ePSXeViewGL.this.statebuttons & (16 << p2)) == 0) {
                                                    if (ePSXeViewGL.this.emu_pad_draw_mode[0] != 4) {
                                                        this.batchLanAction[p2].beginBatch();
                                                        this.batchLanAction[p2].drawSprite(buttonCenterX, buttonCenterY,
                                                                buttonWidth, buttonHeight,
                                                                this.textureRgnLanAction[p2]);
                                                        this.batchLanAction[p2].endBatch();
                                                    }
                                                } else {
                                                    this.batchLanAction[p2].beginBatch();
                                                    this.batchLanAction[p2].drawSprite(buttonCenterX, buttonCenterY,
                                                            buttonWidth * ePSXeViewGL.this.buttonMag,
                                                            buttonHeight * ePSXeViewGL.this.buttonMag,
                                                            this.textureRgnLanAction[p2]);
                                                    this.batchLanAction[p2].endBatch();
                                                }
                                            }
                                        }
                                        if (i == 11 || i == 12) {
                                            this.batchLan[i].beginBatch();
                                            // Исправленная отрисовка аналоговых стиков для OpenGL 1.0 - используем
                                            // оригинальные координаты
                                            this.batchLan[i].drawSprite(
                                                    (float) ePSXeViewGL.this.analog_values[0][(i - 11) * 2],
                                                    (float) ePSXeViewGL.this.analog_values[0][((i - 11) * 2) + 1],
                                                    ePSXeViewGL.this.padSizeScreenLan[ePSXeViewGL.this.mode][26]
                                                            * ePSXeViewGL.this.padScreenResize[ePSXeViewGL.this.mode][i]
                                                            * 2.0f,
                                                    ePSXeViewGL.this.padSizeScreenLan[ePSXeViewGL.this.mode][27]
                                                            * ePSXeViewGL.this.padScreenResize[ePSXeViewGL.this.mode][i]
                                                            * 2.0f,
                                                    this.textureRgnLan[13]);
                                            this.batchLan[i].endBatch();
                                        }
                                    }
                                }
                                if (ePSXeViewGL.this.padScreenExtraEnabled == 1 && !ePSXeViewGL.this.hidePad) {
                                    gl.glBindTexture(3553, this.mTexExtra);
                                    for (int i2 = 14; i2 < 20; i2++) {
                                        if ((ePSXeViewGL.this.padScreenStatus[ePSXeViewGL.this.mode][i2] & 2) != 2) {
                                            if (ePSXeViewGL.this.padScreenExtra[i2 - 14] >= 20
                                                    && ePSXeViewGL.this.stickyButton[ePSXeViewGL.this.padScreenExtra[i2
                                                            - 14] - 20] == 1) {
                                                int j = ePSXeViewGL.this.padScreenExtra[i2 - 14];
                                                this.batchLan[i2].beginBatch();
                                                this.batchLan[i2].drawSprite(
                                                        ePSXeViewGL.this.padOffScreenLan[ePSXeViewGL.this.mode][(i2 * 2)
                                                                + 0],
                                                        ePSXeViewGL.this.padOffScreenLan[ePSXeViewGL.this.mode][(i2 * 2)
                                                                + 1],
                                                        ePSXeViewGL.this.padSizeScreenLan[ePSXeViewGL.this.mode][(i2
                                                                * 2) + 0]
                                                                * ePSXeViewGL.this.padScreenResize[ePSXeViewGL.this.mode][i2],
                                                        ePSXeViewGL.this.padSizeScreenLan[ePSXeViewGL.this.mode][(i2
                                                                * 2) + 1]
                                                                * ePSXeViewGL.this.padScreenResize[ePSXeViewGL.this.mode][i2],
                                                        this.textureRgnLan[j]);
                                                this.batchLan[i2].endBatch();
                                            } else {
                                                this.batchLan[i2].beginBatch();
                                                this.batchLan[i2].drawSprite(
                                                        ePSXeViewGL.this.padOffScreenLan[ePSXeViewGL.this.mode][(i2 * 2)
                                                                + 0],
                                                        ePSXeViewGL.this.padOffScreenLan[ePSXeViewGL.this.mode][(i2 * 2)
                                                                + 1],
                                                        ePSXeViewGL.this.padSizeScreenLan[ePSXeViewGL.this.mode][(i2
                                                                * 2) + 0]
                                                                * ePSXeViewGL.this.padScreenResize[ePSXeViewGL.this.mode][i2],
                                                        ePSXeViewGL.this.padSizeScreenLan[ePSXeViewGL.this.mode][(i2
                                                                * 2) + 1]
                                                                * ePSXeViewGL.this.padScreenResize[ePSXeViewGL.this.mode][i2],
                                                        this.textureRgnLan[i2]);
                                                this.batchLan[i2].endBatch();
                                            }
                                        }
                                    }
                                }
                                gl.glDisable(3042);
                                gl.glDisable(3553);
                            }
                            if (ePSXeViewGL.this.emu_player_mode != 10 || ePSXeViewGL.this.emu_split_mode != 0
                                    || ePSXeViewGL.this.emu_pad_draw_mode[0] == 2) {
                                if ((ePSXeViewGL.this.emu_player_mode == 10
                                        && ePSXeViewGL.this.emu_pad_draw_mode[0] != 2
                                        && ePSXeViewGL.this.emu_split_mode == 1)
                                        || ePSXeViewGL.this.emu_split_mode == 2) {
                                    gl.glEnable(3553);
                                    gl.glEnable(3042);
                                    gl.glBlendFunc(770, 771);
                                    gl.glColor4f(1.0f, 1.0f, 1.0f, ePSXeViewGL.this.emu_input_alpha);
                                    if (!ePSXeViewGL.this.hidePad) {
                                        gl.glBindTexture(3553, this.mTexLan);
                                    }
                                    for (int i3 = 0; i3 < 8; i3++) {
                                        this.batchLan[i3].beginBatch();
                                        this.batchLan[i3].drawSprite(ePSXeViewGL.this.padOffScreenLan2H[(i3 * 2) + 0],
                                                ePSXeViewGL.this.padOffScreenLan2H[(i3 * 2) + 1],
                                                ePSXeViewGL.this.padSizeScreenLan2H[(i3 * 2) + 0],
                                                ePSXeViewGL.this.padSizeScreenLan2H[(i3 * 2) + 1],
                                                this.textureRgnLan[i3]);
                                        this.batchLan[i3].endBatch();
                                    }
                                    gl.glDisable(3042);
                                    gl.glDisable(3553);
                                    gl.glEnable(3553);
                                    gl.glEnable(3042);
                                    gl.glBlendFunc(770, 771);
                                    gl.glColor4f(1.0f, 1.0f, 1.0f, ePSXeViewGL.this.emu_input_alpha);
                                    if (!ePSXeViewGL.this.hidePad) {
                                        gl.glBindTexture(3553, this.mTexLan);
                                    }
                                    for (int i4 = 0; i4 < 8; i4++) {
                                        this.batchLan[i4].beginBatch();
                                        this.batchLan[i4].drawSpriteSwap(
                                                ePSXeViewGL.this.mWidth
                                                        - ePSXeViewGL.this.padOffScreenLan2H[(i4 * 2) + 0],
                                                (((float) ePSXeViewGL.this.mHeight / 2)
                                                        - ePSXeViewGL.this.padOffScreenLan2H[(i4 * 2) + 1])
                                                        + ((float) ePSXeViewGL.this.mHeight / 2),
                                                ePSXeViewGL.this.padSizeScreenLan2H[(i4 * 2) + 0],
                                                ePSXeViewGL.this.padSizeScreenLan2H[(i4 * 2) + 1],
                                                this.textureRgnLan[i4]);
                                        this.batchLan[i4].endBatch();
                                    }
                                    gl.glDisable(3042);
                                    gl.glDisable(3553);
                                }
                            } else {
                                gl.glPushMatrix();
                                gl.glEnable(3553);
                                gl.glEnable(3042);
                                gl.glBlendFunc(770, 771);
                                gl.glColor4f(1.0f, 1.0f, 1.0f, ePSXeViewGL.this.emu_input_alpha);
                                gl.glTranslatef(ePSXeViewGL.this.mWidth, 0.0f, 0.0f);
                                gl.glRotatef(90.0f, 0.0f, 0.0f, 1.0f);
                                if (!ePSXeViewGL.this.hidePad) {
                                    gl.glBindTexture(3553, this.mTexLan);
                                }
                                for (int i5 = 0; i5 < 8; i5++) {
                                    this.batchLan[i5].beginBatch();
                                    this.batchLan[i5].drawSprite(ePSXeViewGL.this.padOffScreenLan2V[(i5 * 2) + 0],
                                            ePSXeViewGL.this.padOffScreenLan2V[(i5 * 2) + 1],
                                            ePSXeViewGL.this.padSizeScreenLan2V[(i5 * 2) + 0],
                                            ePSXeViewGL.this.padSizeScreenLan2V[(i5 * 2) + 1], this.textureRgnLan[i5]);
                                    this.batchLan[i5].endBatch();
                                }
                                gl.glDisable(3042);
                                gl.glDisable(3553);
                                gl.glPopMatrix();
                                gl.glPushMatrix();
                                gl.glEnable(3553);
                                gl.glEnable(3042);
                                gl.glBlendFunc(770, 771);
                                gl.glColor4f(1.0f, 1.0f, 1.0f, ePSXeViewGL.this.emu_input_alpha);
                                gl.glTranslatef(0.0f, ePSXeViewGL.this.mHeight, 0.0f);
                                gl.glRotatef(-90.0f, 0.0f, 0.0f, 1.0f);
                                if (!ePSXeViewGL.this.hidePad) {
                                    gl.glBindTexture(3553, this.mTexLan);
                                }
                                for (int i6 = 0; i6 < 8; i6++) {
                                    this.batchLan[i6].beginBatch();
                                    this.batchLan[i6].drawSprite(ePSXeViewGL.this.padOffScreenLan2V[(i6 * 2) + 0],
                                            ePSXeViewGL.this.padOffScreenLan2V[(i6 * 2) + 1],
                                            ePSXeViewGL.this.padSizeScreenLan2V[(i6 * 2) + 0],
                                            ePSXeViewGL.this.padSizeScreenLan2V[(i6 * 2) + 1], this.textureRgnLan[i6]);
                                    this.batchLan[i6].endBatch();
                                }
                                gl.glDisable(3042);
                                gl.glDisable(3553);
                                gl.glPopMatrix();
                            }
                        } else {
                            gl.glEnable(3553);
                            gl.glEnableClientState(32884);
                            this.batchPor.beginBatch(this.mTexPor);
                            this.batchPor.drawSprite(ePSXeViewGL.this.padOffScreenPor[0],
                                    ePSXeViewGL.this.padOffScreenPor[1], ePSXeViewGL.this.padSizeScreenPor[0],
                                    ePSXeViewGL.this.padSizeScreenPor[1], this.textureRgnPor);
                            this.batchPor.endBatch();
                            gl.glDisableClientState(32884);
                            if (ePSXeViewGL.this.padScreenExtraEnabled == 1 && !ePSXeViewGL.this.hidePad) {
                                gl.glEnable(3042);
                                gl.glBlendFunc(770, 771);
                                gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                                for (int i7 = 14; i7 < 20; i7++) {
                                    if ((ePSXeViewGL.this.padScreenStatus[ePSXeViewGL.this.mode][i7] & 2) != 2) {
                                        if (ePSXeViewGL.this.padScreenExtra[i7 - 14] >= 20
                                                && ePSXeViewGL.this.stickyButton[ePSXeViewGL.this.padScreenExtra[i7
                                                        - 14] - 20] == 1) {
                                            int j2 = ePSXeViewGL.this.padScreenExtra[i7 - 14];
                                            this.batchLan[i7].beginBatch(this.mTexExtra);
                                            this.batchLan[i7].drawSprite(ePSXeViewGL.this.padOffScreenPor[(i7 * 2) + 0],
                                                    ePSXeViewGL.this.padOffScreenPor[(i7 * 2) + 1],
                                                    ePSXeViewGL.this.padSizeScreenPor[(i7 * 2) + 0],
                                                    ePSXeViewGL.this.padSizeScreenPor[(i7 * 2) + 1],
                                                    this.textureRgnLan[j2]);
                                            this.batchLan[i7].endBatch();
                                        } else {
                                            this.batchLan[i7].beginBatch(this.mTexExtra);
                                            this.batchLan[i7].drawSprite(ePSXeViewGL.this.padOffScreenPor[(i7 * 2) + 0],
                                                    ePSXeViewGL.this.padOffScreenPor[(i7 * 2) + 1],
                                                    ePSXeViewGL.this.padSizeScreenPor[(i7 * 2) + 0],
                                                    ePSXeViewGL.this.padSizeScreenPor[(i7 * 2) + 1],
                                                    this.textureRgnLan[i7]);
                                            this.batchLan[i7].endBatch();
                                        }
                                    }
                                }
                                gl.glDisable(3042);
                            }
                            gl.glDisable(3553);
                        }
                    }
                    if (ePSXeViewGL.this.emu_enable_printfps == 1 && ePSXeViewGL.this.emu_player_mode == 1) {
                        drawFPS(gl);
                    }
                    if (!ePSXeViewGL.this.license) {
                        drawLicense(gl);
                    }
                    if (!ePSXeViewGL.this.biosmsg || ePSXeViewGL.this.biosVersionAdvise <= 0) {
                        if (!ePSXeViewGL.this.gprofile || ePSXeViewGL.this.gProfileAdvise <= 0) {
                            if (ePSXeViewGL.this.volumenAdvise > 0) {
                                drawVolumen(gl);
                                ePSXeViewGL.access$4410(ePSXeViewGL.this);
                                return;
                            }
                            return;
                        }
                        if (ePSXeViewGL.this.emu_verbose == 1) {
                            drawString(gl, "Loading custom game profile...");
                        }
                        ePSXeViewGL.access$4310(ePSXeViewGL.this);
                        return;
                    }
                    if (ePSXeViewGL.this.emu_verbose == 1) {
                        drawBiosMsg(gl);
                    }
                    ePSXeViewGL.access$4010(ePSXeViewGL.this);
                }
            }
        }

        public void resetPad1Values() {
            // Используем единую функцию для вычисления базового масштабирования
            calculateBasePadResize();
            applyBaseSizesToPads();
        }

        private void calculateBasePadResize() {
            if (ePSXeViewGL.this.emu_screen_orientation != 1) {
                // Ландшафтный режим - простое масштабирование с уменьшением в 3 раза
                float baseWidth = 800.0f;
                float scaleX = (float) ePSXeViewGL.this.mWidth / baseWidth;
                float scaleY = (float) ePSXeViewGL.this.mHeight / 480.0f;
                ePSXeViewGL.this.padResize = Math.min(scaleX, scaleY);

                // УМЕНЬШАЕМ кнопки в 2 раза - идеальный размер
                ePSXeViewGL.this.padResize = ePSXeViewGL.this.padResize / 2.0f;

                // Ограничиваем разумными пределами
                if (ePSXeViewGL.this.padResize > 1.0f) {
                    ePSXeViewGL.this.padResize = 1.0f;
                } else if (ePSXeViewGL.this.padResize < 0.3f) {
                    ePSXeViewGL.this.padResize = 0.3f;
                }
            } else if (ePSXeViewGL.this.emu_portrait_skin == 1) {
                // Портретный режим - также уменьшаем в 2 раза
                ePSXeViewGL.this.padResize = (ePSXeViewGL.this.mWidth / 562.0f) / 2.0f;
            } else {
                // Портретный режим без скина - уменьшаем в 2 раза
                ePSXeViewGL.this.padResize = 1.0f / 2.0f;
            }
        }

        private void applyBaseSizesToPads() {
            float[][] padSizeScreenLantmp = {
                    new float[] { 228.0f, 228.0f, 224.0f, 248.0f, 66.0f, 50.0f, 66.0f, 62.0f, 64.0f, 60.0f, 64.0f,
                            60.0f, 64.0f, 60.0f, 64.0f, 60.0f, 53.0f, 41.0f, 64.0f, 60.0f, 64.0f, 60.0f, 222.0f, 222.0f,
                            222.0f, 222.0f, 71.0f, 71.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f,
                            64.0f, 64.0f, 64.0f },
                    new float[] { 228.0f, 228.0f, 224.0f, 248.0f, 66.0f, 50.0f, 66.0f, 62.0f, 64.0f, 60.0f, 64.0f,
                            60.0f, 64.0f, 60.0f, 64.0f, 60.0f, 53.0f, 41.0f, 64.0f, 60.0f, 64.0f, 60.0f, 222.0f, 222.0f,
                            222.0f, 222.0f, 71.0f, 71.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f,
                            64.0f, 64.0f, 64.0f } };
            for (int i = 0; i < 20; i++) {
                ePSXeViewGL.this.padSizeScreenLan[ePSXeViewGL.this.mode][(i * 2)
                        + 0] = padSizeScreenLantmp[ePSXeViewGL.this.mode][(i * 2) + 0] * ePSXeViewGL.this.padResize;
                ePSXeViewGL.this.padSizeScreenLan[ePSXeViewGL.this.mode][(i * 2)
                        + 1] = padSizeScreenLantmp[ePSXeViewGL.this.mode][(i * 2) + 1] * ePSXeViewGL.this.padResize;
            }
            if (ePSXeViewGL.this.emu_screen_orientation == 1) {
                if (ePSXeViewGL.this.emu_portrait_skin == 1) {
                    float[][] padOffScreenLantmp = { new float[] { ePSXeViewGL.this.padSizeScreenLan[0][0] / 2.0f,
                            ePSXeViewGL.this.padSizeScreenLan[0][1] / 2.0f,
                            ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[0][2] / 2.0f),
                            ePSXeViewGL.this.padSizeScreenLan[0][3] / 2.0f,
                            (((float) ePSXeViewGL.this.mWidth / 2) - ePSXeViewGL.this.padSizeScreenLan[0][4]) - 30.0f,
                            ((float) ePSXeViewGL.this.mHeight / 2) - (ePSXeViewGL.this.padSizeScreenLan[0][5] / 2.0f),
                            ((float) ePSXeViewGL.this.mWidth / 2) + 30,
                            ((float) ePSXeViewGL.this.mHeight / 2) - (ePSXeViewGL.this.padSizeScreenLan[0][7] / 2.0f),
                            ePSXeViewGL.this.padSizeScreenLan[0][8] / 2.0f,
                            ((float) ePSXeViewGL.this.mHeight / 2) - (ePSXeViewGL.this.padSizeScreenLan[0][9] / 2.0f),
                            (ePSXeViewGL.this.padSizeScreenLan[0][10] / 2.0f) + ePSXeViewGL.this.padSizeScreenLan[0][8],
                            ((float) ePSXeViewGL.this.mHeight / 2) - (ePSXeViewGL.this.padSizeScreenLan[0][11] / 2.0f),
                            ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[0][12] / 2.0f),
                            ((float) ePSXeViewGL.this.mHeight / 2) - (ePSXeViewGL.this.padSizeScreenLan[0][13] / 2.0f),
                            (ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[0][14] / 2.0f))
                                    - ePSXeViewGL.this.padSizeScreenLan[0][12],
                            ((float) ePSXeViewGL.this.mHeight / 2) - (ePSXeViewGL.this.padSizeScreenLan[0][15] / 2.0f),
                            ((float) ePSXeViewGL.this.mWidth / 2) + 40 + ePSXeViewGL.this.padSizeScreenLan[0][6],
                            ((float) ePSXeViewGL.this.mHeight / 2) - (ePSXeViewGL.this.padSizeScreenLan[0][17] / 2.0f),
                            (ePSXeViewGL.this.padSizeScreenLan[0][18] / 2.0f) + ePSXeViewGL.this.padSizeScreenLan[0][10]
                                    + ePSXeViewGL.this.padSizeScreenLan[0][8],
                            ((float) ePSXeViewGL.this.mHeight / 2) - (ePSXeViewGL.this.padSizeScreenLan[0][19] / 2.0f),
                            ((ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[0][20] / 2.0f))
                                    - ePSXeViewGL.this.padSizeScreenLan[0][12])
                                    - ePSXeViewGL.this.padSizeScreenLan[0][14],
                            ((float) ePSXeViewGL.this.mHeight / 2) - (ePSXeViewGL.this.padSizeScreenLan[0][21] / 2.0f),
                            ePSXeViewGL.this.padSizeScreenLan[0][22] / 2.0f,
                            ePSXeViewGL.this.padSizeScreenLan[0][23] / 2.0f,
                            ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[0][24] / 2.0f),
                            ePSXeViewGL.this.padSizeScreenLan[0][25] / 2.0f, 0.0f, 0.0f,
                            ePSXeViewGL.this.padSizeScreenLan[0][28] / 2.0f,
                            ((float) ePSXeViewGL.this.mHeight / 3) - (ePSXeViewGL.this.padSizeScreenLan[0][29] / 2.0f),
                            (ePSXeViewGL.this.padSizeScreenLan[0][30] / 2.0f)
                                    + ePSXeViewGL.this.padSizeScreenLan[0][28],
                            ((float) ePSXeViewGL.this.mHeight / 3) - (ePSXeViewGL.this.padSizeScreenLan[0][31] / 2.0f),
                            (ePSXeViewGL.this.padSizeScreenLan[0][32] / 2.0f) + ePSXeViewGL.this.padSizeScreenLan[0][28]
                                    + ePSXeViewGL.this.padSizeScreenLan[0][30],
                            ((float) ePSXeViewGL.this.mHeight / 3) - (ePSXeViewGL.this.padSizeScreenLan[0][33] / 2.0f),
                            ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[0][34] / 2.0f),
                            ((float) ePSXeViewGL.this.mHeight / 3) - (ePSXeViewGL.this.padSizeScreenLan[0][35] / 2.0f),
                            (ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[0][36] / 2.0f))
                                    - ePSXeViewGL.this.padSizeScreenLan[0][34],
                            ((float) ePSXeViewGL.this.mHeight / 3) - (ePSXeViewGL.this.padSizeScreenLan[0][37] / 2.0f),
                            ((ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[0][38] / 2.0f))
                                    - ePSXeViewGL.this.padSizeScreenLan[0][34])
                                    - ePSXeViewGL.this.padSizeScreenLan[0][36],
                            ((float) ePSXeViewGL.this.mHeight / 3)
                                    - (ePSXeViewGL.this.padSizeScreenLan[0][39] / 2.0f) },
                            new float[] {
                                    ((float) ePSXeViewGL.this.mWidth / 2)
                                            - ((ePSXeViewGL.this.padSizeScreenLan[1][0] / 2.0f) * 0.76f),
                                    (float) ePSXeViewGL.this.mHeight / 4,
                                    ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[1][2] / 2.0f),
                                    ePSXeViewGL.this.padSizeScreenLan[1][3] / 2.0f,
                                    (((float) ePSXeViewGL.this.mWidth / 2) - ePSXeViewGL.this.padSizeScreenLan[1][4])
                                            - 30.0f,
                                    ((float) ePSXeViewGL.this.mHeight / 2)
                                            - (ePSXeViewGL.this.padSizeScreenLan[1][5] / 2.0f),
                                    ((float) ePSXeViewGL.this.mWidth / 2) + 30,
                                    ((float) ePSXeViewGL.this.mHeight / 2)
                                            - (ePSXeViewGL.this.padSizeScreenLan[1][7] / 2.0f),
                                    ePSXeViewGL.this.padSizeScreenLan[1][8] / 2.0f,
                                    ((float) ePSXeViewGL.this.mHeight / 2)
                                            - (ePSXeViewGL.this.padSizeScreenLan[1][9] / 2.0f),
                                    (ePSXeViewGL.this.padSizeScreenLan[1][10] / 2.0f)
                                            + ePSXeViewGL.this.padSizeScreenLan[0][8],
                                    ((float) ePSXeViewGL.this.mHeight / 2)
                                            - (ePSXeViewGL.this.padSizeScreenLan[1][11] / 2.0f),
                                    ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[1][12] / 2.0f),
                                    ((float) ePSXeViewGL.this.mHeight / 2)
                                            - (ePSXeViewGL.this.padSizeScreenLan[1][13] / 2.0f),
                                    (ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[1][14] / 2.0f))
                                            - ePSXeViewGL.this.padSizeScreenLan[1][12],
                                    ((float) ePSXeViewGL.this.mHeight / 2)
                                            - (ePSXeViewGL.this.padSizeScreenLan[1][15] / 2.0f),
                                    ((float) ePSXeViewGL.this.mWidth / 2) + 40
                                            + ePSXeViewGL.this.padSizeScreenLan[1][6],
                                    ((float) ePSXeViewGL.this.mHeight / 2)
                                            - (ePSXeViewGL.this.padSizeScreenLan[1][17] / 2.0f),
                                    (ePSXeViewGL.this.padSizeScreenLan[1][18] / 2.0f)
                                            + ePSXeViewGL.this.padSizeScreenLan[1][10]
                                            + ePSXeViewGL.this.padSizeScreenLan[1][8],
                                    ((float) ePSXeViewGL.this.mHeight / 2)
                                            - (ePSXeViewGL.this.padSizeScreenLan[1][19] / 2.0f),
                                    ((ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[1][20] / 2.0f))
                                            - ePSXeViewGL.this.padSizeScreenLan[1][12])
                                            - ePSXeViewGL.this.padSizeScreenLan[1][14],
                                    ((float) ePSXeViewGL.this.mHeight / 2)
                                            - (ePSXeViewGL.this.padSizeScreenLan[1][21] / 2.0f),
                                    ePSXeViewGL.this.padSizeScreenLan[1][22] / 2.0f,
                                    ePSXeViewGL.this.padSizeScreenLan[1][23] / 2.0f,
                                    ((float) ePSXeViewGL.this.mWidth / 2)
                                            + ((ePSXeViewGL.this.padSizeScreenLan[1][24] / 2.0f) * 0.76f),
                                    (float) ePSXeViewGL.this.mHeight / 4, 0.0f, 0.0f,
                                    ePSXeViewGL.this.padSizeScreenLan[1][28] / 2.0f,
                                    ((float) ePSXeViewGL.this.mHeight / 3)
                                            - (ePSXeViewGL.this.padSizeScreenLan[1][29] / 2.0f),
                                    (ePSXeViewGL.this.padSizeScreenLan[1][30] / 2.0f)
                                            + ePSXeViewGL.this.padSizeScreenLan[1][28],
                                    ((float) ePSXeViewGL.this.mHeight / 3)
                                            - (ePSXeViewGL.this.padSizeScreenLan[1][31] / 2.0f),
                                    (ePSXeViewGL.this.padSizeScreenLan[1][32] / 2.0f)
                                            + ePSXeViewGL.this.padSizeScreenLan[1][28]
                                            + ePSXeViewGL.this.padSizeScreenLan[1][30],
                                    ((float) ePSXeViewGL.this.mHeight / 3)
                                            - (ePSXeViewGL.this.padSizeScreenLan[1][33] / 2.0f),
                                    ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[1][34] / 2.0f),
                                    ((float) ePSXeViewGL.this.mHeight / 3)
                                            - (ePSXeViewGL.this.padSizeScreenLan[1][35] / 2.0f),
                                    (ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[1][36] / 2.0f))
                                            - ePSXeViewGL.this.padSizeScreenLan[1][34],
                                    ((float) ePSXeViewGL.this.mHeight / 3)
                                            - (ePSXeViewGL.this.padSizeScreenLan[1][37] / 2.0f),
                                    ((ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[1][38] / 2.0f))
                                            - ePSXeViewGL.this.padSizeScreenLan[1][34])
                                            - ePSXeViewGL.this.padSizeScreenLan[1][36],
                                    ((float) ePSXeViewGL.this.mHeight / 3)
                                            - (ePSXeViewGL.this.padSizeScreenLan[1][39] / 2.0f) } };
                    for (int i2 = 0; i2 < 20; i2++) {
                        ePSXeViewGL.this.padOffScreenLan[ePSXeViewGL.this.mode][(i2 * 2)
                                + 0] = padOffScreenLantmp[ePSXeViewGL.this.mode][(i2 * 2) + 0];
                        ePSXeViewGL.this.padOffScreenLan[ePSXeViewGL.this.mode][(i2 * 2)
                                + 1] = padOffScreenLantmp[ePSXeViewGL.this.mode][(i2 * 2) + 1];
                    }
                    return;
                }
                return;
            }
            float[][] padOffScreenLantmp2 = {
                    new float[] { ePSXeViewGL.this.padSizeScreenLan[0][0] / 2.0f,
                            ePSXeViewGL.this.padSizeScreenLan[0][1] / 2.0f,
                            ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[0][2] / 2.0f),
                            ePSXeViewGL.this.padSizeScreenLan[0][3] / 2.0f,
                            (((float) ePSXeViewGL.this.mWidth / 2) - ePSXeViewGL.this.padSizeScreenLan[0][4]) - 30.0f,
                            ePSXeViewGL.this.padSizeScreenLan[0][5] / 2.0f, ((float) ePSXeViewGL.this.mWidth / 2) + 30,
                            ePSXeViewGL.this.padSizeScreenLan[0][7] / 2.0f,
                            ePSXeViewGL.this.padSizeScreenLan[0][8] / 2.0f,
                            ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan[0][9] / 2.0f),
                            (ePSXeViewGL.this.padSizeScreenLan[0][10] / 2.0f) + ePSXeViewGL.this.padSizeScreenLan[0][8],
                            ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan[0][11] / 2.0f),
                            ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[0][12] / 2.0f),
                            ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan[0][13] / 2.0f),
                            (ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[0][14] / 2.0f))
                                    - ePSXeViewGL.this.padSizeScreenLan[0][12],
                            ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan[0][15] / 2.0f),
                            ((float) ePSXeViewGL.this.mWidth / 2) + 40 + ePSXeViewGL.this.padSizeScreenLan[0][6],
                            ePSXeViewGL.this.padSizeScreenLan[0][17] / 2.0f,
                            (ePSXeViewGL.this.padSizeScreenLan[0][18] / 2.0f) + ePSXeViewGL.this.padSizeScreenLan[0][10]
                                    + ePSXeViewGL.this.padSizeScreenLan[0][8],
                            ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan[0][19] / 2.0f),
                            ((ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[0][20] / 2.0f))
                                    - ePSXeViewGL.this.padSizeScreenLan[0][12])
                                    - ePSXeViewGL.this.padSizeScreenLan[0][14],
                            ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan[0][21] / 2.0f),
                            ePSXeViewGL.this.padSizeScreenLan[0][22] / 2.0f,
                            ePSXeViewGL.this.padSizeScreenLan[0][23] / 2.0f,
                            ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[0][24] / 2.0f),
                            ePSXeViewGL.this.padSizeScreenLan[0][25] / 2.0f, 0.0f, 0.0f,
                            ePSXeViewGL.this.padSizeScreenLan[0][28] / 2.0f,
                            (ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan[0][29] / 2.0f))
                                    - (ePSXeViewGL.this.padSizeScreenLan[0][10] * 2.0f),
                            (ePSXeViewGL.this.padSizeScreenLan[0][30] / 2.0f)
                                    + ePSXeViewGL.this.padSizeScreenLan[0][28],
                            (ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan[0][31] / 2.0f))
                                    - (ePSXeViewGL.this.padSizeScreenLan[0][10] * 2.0f),
                            (ePSXeViewGL.this.padSizeScreenLan[0][32] / 2.0f) + ePSXeViewGL.this.padSizeScreenLan[0][28]
                                    + ePSXeViewGL.this.padSizeScreenLan[0][30],
                            (ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan[0][33] / 2.0f))
                                    - (ePSXeViewGL.this.padSizeScreenLan[0][10] * 2.0f),
                            ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[0][34] / 2.0f),
                            (ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan[0][35] / 2.0f))
                                    - (ePSXeViewGL.this.padSizeScreenLan[0][10] * 2.0f),
                            (ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[0][36] / 2.0f))
                                    - ePSXeViewGL.this.padSizeScreenLan[0][34],
                            (ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan[0][37] / 2.0f))
                                    - (ePSXeViewGL.this.padSizeScreenLan[0][10] * 2.0f),
                            ((ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[0][38] / 2.0f))
                                    - ePSXeViewGL.this.padSizeScreenLan[0][34])
                                    - ePSXeViewGL.this.padSizeScreenLan[0][36],
                            (ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan[0][39] / 2.0f))
                                    - (ePSXeViewGL.this.padSizeScreenLan[0][10] * 2.0f) },
                    new float[] {
                            ((float) ePSXeViewGL.this.mWidth / 2)
                                    - ((ePSXeViewGL.this.padSizeScreenLan[1][0] / 2.0f) * 0.76f),
                            (float) ePSXeViewGL.this.mHeight / 2,
                            ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[1][2] / 2.0f),
                            ePSXeViewGL.this.padSizeScreenLan[1][3] / 2.0f,
                            (((float) ePSXeViewGL.this.mWidth / 2) - ePSXeViewGL.this.padSizeScreenLan[1][4]) - 30.0f,
                            ePSXeViewGL.this.padSizeScreenLan[1][5] / 2.0f, ((float) ePSXeViewGL.this.mWidth / 2) + 30,
                            ePSXeViewGL.this.padSizeScreenLan[1][7] / 2.0f,
                            ePSXeViewGL.this.padSizeScreenLan[1][8] / 2.0f,
                            ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan[1][9] / 2.0f),
                            (ePSXeViewGL.this.padSizeScreenLan[1][10] / 2.0f) + ePSXeViewGL.this.padSizeScreenLan[1][8],
                            ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan[1][11] / 2.0f),
                            ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[1][12] / 2.0f),
                            ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan[1][13] / 2.0f),
                            (ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[1][14] / 2.0f))
                                    - ePSXeViewGL.this.padSizeScreenLan[1][12],
                            ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan[1][15] / 2.0f),
                            ((float) ePSXeViewGL.this.mWidth / 2) + 40 + ePSXeViewGL.this.padSizeScreenLan[1][6],
                            ePSXeViewGL.this.padSizeScreenLan[1][17] / 2.0f,
                            (ePSXeViewGL.this.padSizeScreenLan[1][18] / 2.0f) + ePSXeViewGL.this.padSizeScreenLan[1][10]
                                    + ePSXeViewGL.this.padSizeScreenLan[1][8],
                            ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan[1][19] / 2.0f),
                            ((ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[1][20] / 2.0f))
                                    - ePSXeViewGL.this.padSizeScreenLan[1][12])
                                    - ePSXeViewGL.this.padSizeScreenLan[1][14],
                            ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan[1][21] / 2.0f),
                            ePSXeViewGL.this.padSizeScreenLan[1][22] / 2.0f,
                            ePSXeViewGL.this.padSizeScreenLan[1][23] / 2.0f,
                            ((float) ePSXeViewGL.this.mWidth / 2)
                                    + ((ePSXeViewGL.this.padSizeScreenLan[1][24] / 2.0f) * 0.76f),
                            (float) ePSXeViewGL.this.mHeight / 2, 0.0f, 0.0f,
                            ePSXeViewGL.this.padSizeScreenLan[1][28] / 2.0f,
                            (ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan[1][29] / 2.0f))
                                    - (ePSXeViewGL.this.padSizeScreenLan[1][10] * 2.0f),
                            (ePSXeViewGL.this.padSizeScreenLan[1][30] / 2.0f)
                                    + ePSXeViewGL.this.padSizeScreenLan[1][28],
                            (ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan[1][31] / 2.0f))
                                    - (ePSXeViewGL.this.padSizeScreenLan[1][10] * 2.0f),
                            (ePSXeViewGL.this.padSizeScreenLan[1][32] / 2.0f) + ePSXeViewGL.this.padSizeScreenLan[1][28]
                                    + ePSXeViewGL.this.padSizeScreenLan[1][30],
                            (ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan[1][33] / 2.0f))
                                    - (ePSXeViewGL.this.padSizeScreenLan[1][10] * 2.0f),
                            ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[1][34] / 2.0f),
                            (ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan[1][35] / 2.0f))
                                    - (ePSXeViewGL.this.padSizeScreenLan[1][10] * 2.0f),
                            (ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[1][36] / 2.0f))
                                    - ePSXeViewGL.this.padSizeScreenLan[1][34],
                            (ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan[1][37] / 2.0f))
                                    - (ePSXeViewGL.this.padSizeScreenLan[1][10] * 2.0f),
                            ((ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[1][38] / 2.0f))
                                    - ePSXeViewGL.this.padSizeScreenLan[1][34])
                                    - ePSXeViewGL.this.padSizeScreenLan[1][36],
                            (ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan[1][39] / 2.0f))
                                    - (ePSXeViewGL.this.padSizeScreenLan[1][10] * 2.0f) } };
            for (int i3 = 0; i3 < 20; i3++) {
                ePSXeViewGL.this.padOffScreenLan[ePSXeViewGL.this.mode][(i3 * 2)
                        + 0] = padOffScreenLantmp2[ePSXeViewGL.this.mode][(i3 * 2) + 0];
                ePSXeViewGL.this.padOffScreenLan[ePSXeViewGL.this.mode][(i3 * 2)
                        + 1] = padOffScreenLantmp2[ePSXeViewGL.this.mode][(i3 * 2) + 1];
            }
        }

        public void resetPadAllValues() {
            if (ePSXeViewGL.this.emu_screen_orientation != 1) {
                float baseWidth = 800.0f;
                ePSXeViewGL.this.padResize = ePSXeViewGL.this.mWidth / baseWidth;
                
                // УМЕНЬШАЕМ кнопки в 2 раза - идеальный размер
                ePSXeViewGL.this.padResize = ePSXeViewGL.this.padResize / 2.0f;
                
                if (ePSXeViewGL.this.padResize < 0.3f) {
                    ePSXeViewGL.this.padResize = 0.3f;
                } else if (ePSXeViewGL.this.padResize > 1.0f) {
                    ePSXeViewGL.this.padResize = 1.0f;
                }
            } else if (ePSXeViewGL.this.emu_portrait_skin == 1) {
                ePSXeViewGL.this.padResize = (ePSXeViewGL.this.mWidth / 562.0f) / 2.0f;
            }
            float[] padSizeScreenPortmp = { ePSXeViewGL.this.mWidth, ePSXeViewGL.this.mHeight / 2f, 0.0f, 0.0f, 0.0f,
                    0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
                    0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, (ePSXeViewGL.this.mWidth * 64f) / 480,
                    ((ePSXeViewGL.this.mHeight / 2f) * 64) / 400, (ePSXeViewGL.this.mWidth * 64f) / 480,
                    ((ePSXeViewGL.this.mHeight / 2f) * 64) / 400, (ePSXeViewGL.this.mWidth * 64f) / 480,
                    ((ePSXeViewGL.this.mHeight / 2f) * 64) / 400, (ePSXeViewGL.this.mWidth * 64f) / 480,
                    ((ePSXeViewGL.this.mHeight / 2f) * 64) / 400, (ePSXeViewGL.this.mWidth * 64f) / 480,
                    ((ePSXeViewGL.this.mHeight / 2f) * 64) / 400, (ePSXeViewGL.this.mWidth * 64f) / 480,
                    ((ePSXeViewGL.this.mHeight / 2f) * 64) / 400 };
            float[] padOffScreenPortmp = { ePSXeViewGL.this.mWidth / 2f, ePSXeViewGL.this.mHeight / 4f, 0.0f, 0.0f,
                    0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
                    0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
                    ((ePSXeViewGL.this.mWidth * 288f) / 480) + (padSizeScreenPortmp[28] / 2.0f),
                    ((ePSXeViewGL.this.mHeight / 2f) - (((ePSXeViewGL.this.mHeight / 2f) * 68) / 400))
                            - (padSizeScreenPortmp[29] / 2.0f),
                    ((ePSXeViewGL.this.mWidth * 352f) / 480) + (padSizeScreenPortmp[30] / 2.0f),
                    ((ePSXeViewGL.this.mHeight / 2f) - (((ePSXeViewGL.this.mHeight / 2f) * 68) / 400))
                            - (padSizeScreenPortmp[31] / 2.0f),
                    ((ePSXeViewGL.this.mWidth * HttpStatus.SC_REQUESTED_RANGE_NOT_SATISFIABLE) / 480)
                            + (padSizeScreenPortmp[32] / 2.0f),
                    ((ePSXeViewGL.this.mHeight / 2f) - (((ePSXeViewGL.this.mHeight / 2f) * 68) / 400))
                            - (padSizeScreenPortmp[33] / 2.0f),
                    ((ePSXeViewGL.this.mWidth * 8f) / 480) + (padSizeScreenPortmp[34] / 2.0f),
                    ((ePSXeViewGL.this.mHeight / 2f) - (((ePSXeViewGL.this.mHeight / 2f) * 338) / 400))
                            - (padSizeScreenPortmp[35] / 2.0f),
                    ((ePSXeViewGL.this.mWidth * 72f) / 480) + (padSizeScreenPortmp[36] / 2.0f),
                    ((ePSXeViewGL.this.mHeight / 2f) - (((ePSXeViewGL.this.mHeight / 2f) * 338) / 400))
                            - (padSizeScreenPortmp[37] / 2.0f),
                    ((ePSXeViewGL.this.mWidth * 136f) / 480) + (padSizeScreenPortmp[38] / 2.0f),
                    ((ePSXeViewGL.this.mHeight / 2f) - (((ePSXeViewGL.this.mHeight / 2f) * 338) / 400))
                            - (padSizeScreenPortmp[39] / 2.0f) };
            for (int i = 0; i < 1; i++) {
                ePSXeViewGL.this.padSizeScreenPor[i * 2] = padSizeScreenPortmp[i * 2];
                ePSXeViewGL.this.padSizeScreenPor[(i * 2) + 1] = padSizeScreenPortmp[(i * 2) + 1];
                ePSXeViewGL.this.padOffScreenPor[i * 2] = padOffScreenPortmp[i * 2];
                ePSXeViewGL.this.padOffScreenPor[(i * 2) + 1] = padOffScreenPortmp[(i * 2) + 1];
            }
            for (int i2 = 14; i2 < 20; i2++) {
                ePSXeViewGL.this.padSizeScreenPor[i2 * 2] = padSizeScreenPortmp[i2 * 2];
                ePSXeViewGL.this.padSizeScreenPor[(i2 * 2) + 1] = padSizeScreenPortmp[(i2 * 2) + 1];
                ePSXeViewGL.this.padOffScreenPor[i2 * 2] = padOffScreenPortmp[i2 * 2];
                ePSXeViewGL.this.padOffScreenPor[(i2 * 2) + 1] = padOffScreenPortmp[(i2 * 2) + 1];
            }
            float[] padSizeScreenLan2Htmp = { 228.0f, 114.0f, 224.0f, 124.0f, 66.0f, 25.0f, 66.0f, 31.0f, 64.0f, 30.0f,
                    64.0f, 30.0f, 64.0f, 30.0f, 64.0f, 30.0f };
            for (int i3 = 0; i3 < 8; i3++) {
                ePSXeViewGL.this.padSizeScreenLan2H[i3 * 2] = padSizeScreenLan2Htmp[i3 * 2]
                        * ePSXeViewGL.this.padResize;
                ePSXeViewGL.this.padSizeScreenLan2H[(i3 * 2) + 1] = padSizeScreenLan2Htmp[(i3 * 2) + 1]
                        * ePSXeViewGL.this.padResize;
            }
            float[] padOffScreenLan2Htmp = { ePSXeViewGL.this.padSizeScreenLan2H[0] / 2.0f,
                    ePSXeViewGL.this.padSizeScreenLan2H[1] / 2.0f,
                    ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan2H[2] / 2.0f),
                    ePSXeViewGL.this.padSizeScreenLan2H[3] / 2.0f,
                    ((ePSXeViewGL.this.mWidth / 2f) - ePSXeViewGL.this.padSizeScreenLan2H[4]) - 30.0f,
                    ePSXeViewGL.this.padSizeScreenLan2H[5] / 2.0f, (ePSXeViewGL.this.mWidth / 2f) + 30,
                    ePSXeViewGL.this.padSizeScreenLan2H[7] / 2.0f, ePSXeViewGL.this.padSizeScreenLan2H[8] / 2.0f,
                    (ePSXeViewGL.this.mHeight / 2f) - (ePSXeViewGL.this.padSizeScreenLan2H[9] / 2.0f),
                    (ePSXeViewGL.this.padSizeScreenLan2H[10] / 2.0f) + ePSXeViewGL.this.padSizeScreenLan2H[8],
                    (ePSXeViewGL.this.mHeight / 2f) - (ePSXeViewGL.this.padSizeScreenLan2H[11] / 2.0f),
                    ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan2H[12] / 2.0f),
                    (ePSXeViewGL.this.mHeight / 2f) - (ePSXeViewGL.this.padSizeScreenLan2H[13] / 2.0f),
                    (ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan2H[14] / 2.0f))
                            - ePSXeViewGL.this.padSizeScreenLan2H[12],
                    (ePSXeViewGL.this.mHeight / 2f) - (ePSXeViewGL.this.padSizeScreenLan2H[15] / 2.0f) };
            for (int i4 = 0; i4 < 8; i4++) {
                ePSXeViewGL.this.padOffScreenLan2H[i4 * 2] = padOffScreenLan2Htmp[i4 * 2];
                ePSXeViewGL.this.padOffScreenLan2H[(i4 * 2) + 1] = padOffScreenLan2Htmp[(i4 * 2) + 1];
            }
            float[] padSizeScreenLan2Vtmp = { (ePSXeViewGL.this.mHeight * 228f) / ePSXeViewGL.this.mWidth,
                    ((ePSXeViewGL.this.mWidth / 2f) * 228) / ePSXeViewGL.this.mHeight,
                    (ePSXeViewGL.this.mHeight * 224f) / ePSXeViewGL.this.mWidth,
                    ((ePSXeViewGL.this.mWidth / 2f) * 248) / ePSXeViewGL.this.mHeight,
                    (ePSXeViewGL.this.mHeight * 66f) / ePSXeViewGL.this.mWidth,
                    ((ePSXeViewGL.this.mWidth / 2f) * 40) / ePSXeViewGL.this.mHeight,
                    (ePSXeViewGL.this.mHeight * 66f) / ePSXeViewGL.this.mWidth,
                    ((ePSXeViewGL.this.mWidth / 2f) * 62) / ePSXeViewGL.this.mHeight,
                    (ePSXeViewGL.this.mHeight * 64f) / ePSXeViewGL.this.mWidth,
                    ((ePSXeViewGL.this.mWidth / 2f) * 60) / ePSXeViewGL.this.mHeight,
                    (ePSXeViewGL.this.mHeight * 64f) / ePSXeViewGL.this.mWidth,
                    ((ePSXeViewGL.this.mWidth / 2f) * 60) / ePSXeViewGL.this.mHeight,
                    (ePSXeViewGL.this.mHeight * 64f) / ePSXeViewGL.this.mWidth,
                    ((ePSXeViewGL.this.mWidth / 2f) * 60) / ePSXeViewGL.this.mHeight,
                    (ePSXeViewGL.this.mHeight * 64f) / ePSXeViewGL.this.mWidth,
                    ((ePSXeViewGL.this.mWidth / 2f) * 60) / ePSXeViewGL.this.mHeight };
            for (int i5 = 0; i5 < 8; i5++) {
                ePSXeViewGL.this.padSizeScreenLan2V[i5 * 2] = padSizeScreenLan2Vtmp[i5 * 2]
                        * ePSXeViewGL.this.padResize;
                ePSXeViewGL.this.padSizeScreenLan2V[(i5 * 2) + 1] = padSizeScreenLan2Vtmp[(i5 * 2) + 1]
                        * ePSXeViewGL.this.padResize;
            }
            float[] padOffScreenLan2Vtmp = { ePSXeViewGL.this.padSizeScreenLan2V[0] / 2.0f,
                    ePSXeViewGL.this.padSizeScreenLan2V[1] / 2.0f,
                    ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan2V[2] / 2.0f),
                    ePSXeViewGL.this.padSizeScreenLan2V[3] / 2.0f,
                    ((ePSXeViewGL.this.mHeight / 2f) - ePSXeViewGL.this.padSizeScreenLan2V[4]) - 30.0f,
                    ePSXeViewGL.this.padSizeScreenLan2V[5] / 2.0f, (ePSXeViewGL.this.mHeight / 2f) + 30,
                    ePSXeViewGL.this.padSizeScreenLan2V[7] / 2.0f, ePSXeViewGL.this.padSizeScreenLan2V[8] / 2.0f,
                    (ePSXeViewGL.this.mWidth / 2f) - (ePSXeViewGL.this.padSizeScreenLan2V[9] / 2.0f),
                    (ePSXeViewGL.this.padSizeScreenLan2V[10] / 2.0f) + ePSXeViewGL.this.padSizeScreenLan2V[8],
                    (ePSXeViewGL.this.mWidth / 2f) - (ePSXeViewGL.this.padSizeScreenLan2V[11] / 2.0f),
                    ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan2V[12] / 2.0f),
                    (ePSXeViewGL.this.mWidth / 2f) - (ePSXeViewGL.this.padSizeScreenLan2V[13] / 2.0f),
                    (ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan2V[14] / 2.0f))
                            - ePSXeViewGL.this.padSizeScreenLan2V[12],
                    (ePSXeViewGL.this.mWidth / 2f) - (ePSXeViewGL.this.padSizeScreenLan2V[15] / 2.0f) };
            for (int i6 = 0; i6 < 8; i6++) {
                ePSXeViewGL.this.padOffScreenLan2V[i6 * 2] = padOffScreenLan2Vtmp[i6 * 2];
                ePSXeViewGL.this.padOffScreenLan2V[(i6 * 2) + 1] = padOffScreenLan2Vtmp[(i6 * 2) + 1];
            }
            ePSXeViewGL.this.initvirtualPad = 0;
        }

        /**
         * Устанавливает базовые размеры кнопок без применения padResize
         * Используется при загрузке сохраненных настроек
         */
        private void setBasePadSizesWithoutResize() {
            float[][] padSizeScreenLantmp = {
                    new float[] { 228.0f, 228.0f, 224.0f, 248.0f, 66.0f, 50.0f, 66.0f, 62.0f, 64.0f, 60.0f, 64.0f,
                            60.0f, 64.0f, 60.0f, 64.0f, 60.0f, 53.0f, 41.0f, 64.0f, 60.0f, 64.0f, 60.0f, 222.0f, 222.0f,
                            222.0f, 222.0f, 71.0f, 71.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f,
                            64.0f, 64.0f, 64.0f },
                    new float[] { 228.0f, 228.0f, 224.0f, 248.0f, 66.0f, 50.0f, 66.0f, 62.0f, 64.0f, 60.0f, 64.0f,
                            60.0f, 64.0f, 60.0f, 64.0f, 60.0f, 53.0f, 41.0f, 64.0f, 60.0f, 64.0f, 60.0f, 222.0f, 222.0f,
                            222.0f, 222.0f, 71.0f, 71.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f, 64.0f,
                            64.0f, 64.0f, 64.0f }
            };

            // Применяем базовые размеры БЕЗ padResize
            for (int i = 0; i < 20; i++) {
                ePSXeViewGL.this.padSizeScreenLan[ePSXeViewGL.this.mode][(i * 2)
                        + 0] = padSizeScreenLantmp[ePSXeViewGL.this.mode][(i * 2) + 0];
                ePSXeViewGL.this.padSizeScreenLan[ePSXeViewGL.this.mode][(i * 2)
                        + 1] = padSizeScreenLantmp[ePSXeViewGL.this.mode][(i * 2) + 1];
            }

            // Устанавливаем базовые позиции
            setBasePadPositions();
        }

        /**
         * Устанавливает базовые позиции кнопок
         */
        private void setBasePadPositions() {
            if (ePSXeViewGL.this.emu_screen_orientation == 1) {
                if (ePSXeViewGL.this.emu_portrait_skin == 1) {
                    setPortraitPadPositions();
                }
                return;
            }
            setLandscapePadPositions();
        }

        /**
         * Устанавливает позиции для портретного режима
         */
        private void setPortraitPadPositions() {
            float[][] padOffScreenLantmp = {
                    new float[] { ePSXeViewGL.this.padSizeScreenLan[0][0] / 2.0f,
                            ePSXeViewGL.this.padSizeScreenLan[0][1] / 2.0f,
                            ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[0][2] / 2.0f),
                            ePSXeViewGL.this.padSizeScreenLan[0][3] / 2.0f,
                            (((float) ePSXeViewGL.this.mWidth / 2) - ePSXeViewGL.this.padSizeScreenLan[0][4]) - 30.0f,
                            ((float) ePSXeViewGL.this.mHeight / 2) - (ePSXeViewGL.this.padSizeScreenLan[0][5] / 2.0f),
                            ((float) ePSXeViewGL.this.mWidth / 2) + 30,
                            ((float) ePSXeViewGL.this.mHeight / 2) - (ePSXeViewGL.this.padSizeScreenLan[0][7] / 2.0f),
                            ePSXeViewGL.this.padSizeScreenLan[0][8] / 2.0f,
                            ((float) ePSXeViewGL.this.mHeight / 2) - (ePSXeViewGL.this.padSizeScreenLan[0][9] / 2.0f),
                            (ePSXeViewGL.this.padSizeScreenLan[0][10] / 2.0f) + ePSXeViewGL.this.padSizeScreenLan[0][8],
                            ((float) ePSXeViewGL.this.mHeight / 2) - (ePSXeViewGL.this.padSizeScreenLan[0][11] / 2.0f),
                            ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[0][12] / 2.0f),
                            ((float) ePSXeViewGL.this.mHeight / 2) - (ePSXeViewGL.this.padSizeScreenLan[0][13] / 2.0f),
                            (ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[0][14] / 2.0f))
                                    - ePSXeViewGL.this.padSizeScreenLan[0][12],
                            ((float) ePSXeViewGL.this.mHeight / 2) - (ePSXeViewGL.this.padSizeScreenLan[0][15] / 2.0f),
                            ((float) ePSXeViewGL.this.mWidth / 2) + 40 + ePSXeViewGL.this.padSizeScreenLan[0][6],
                            ((float) ePSXeViewGL.this.mHeight / 2) - (ePSXeViewGL.this.padSizeScreenLan[0][17] / 2.0f),
                            (ePSXeViewGL.this.padSizeScreenLan[0][18] / 2.0f) + ePSXeViewGL.this.padSizeScreenLan[0][10]
                                    + ePSXeViewGL.this.padSizeScreenLan[0][8],
                            ((float) ePSXeViewGL.this.mHeight / 2) - (ePSXeViewGL.this.padSizeScreenLan[0][19] / 2.0f),
                            ((ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[0][20] / 2.0f))
                                    - ePSXeViewGL.this.padSizeScreenLan[0][12])
                                    - ePSXeViewGL.this.padSizeScreenLan[0][14],
                            ((float) ePSXeViewGL.this.mHeight / 2) - (ePSXeViewGL.this.padSizeScreenLan[0][21] / 2.0f),
                            ePSXeViewGL.this.padSizeScreenLan[0][22] / 2.0f,
                            ePSXeViewGL.this.padSizeScreenLan[0][23] / 2.0f,
                            ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[0][24] / 2.0f),
                            ePSXeViewGL.this.padSizeScreenLan[0][25] / 2.0f, 0.0f, 0.0f,
                            ePSXeViewGL.this.padSizeScreenLan[0][28] / 2.0f,
                            ((float) ePSXeViewGL.this.mHeight / 3) - (ePSXeViewGL.this.padSizeScreenLan[0][29] / 2.0f),
                            (ePSXeViewGL.this.padSizeScreenLan[0][30] / 2.0f)
                                    + ePSXeViewGL.this.padSizeScreenLan[0][28],
                            ((float) ePSXeViewGL.this.mHeight / 3) - (ePSXeViewGL.this.padSizeScreenLan[0][31] / 2.0f),
                            (ePSXeViewGL.this.padSizeScreenLan[0][32] / 2.0f) + ePSXeViewGL.this.padSizeScreenLan[0][28]
                                    + ePSXeViewGL.this.padSizeScreenLan[0][30],
                            ((float) ePSXeViewGL.this.mHeight / 3) - (ePSXeViewGL.this.padSizeScreenLan[0][33] / 2.0f),
                            ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[0][34] / 2.0f),
                            ((float) ePSXeViewGL.this.mHeight / 3) - (ePSXeViewGL.this.padSizeScreenLan[0][35] / 2.0f),
                            (ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[0][36] / 2.0f))
                                    - ePSXeViewGL.this.padSizeScreenLan[0][34],
                            ((float) ePSXeViewGL.this.mHeight / 3) - (ePSXeViewGL.this.padSizeScreenLan[0][37] / 2.0f),
                            ((ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[0][38] / 2.0f))
                                    - ePSXeViewGL.this.padSizeScreenLan[0][34])
                                    - ePSXeViewGL.this.padSizeScreenLan[0][36],
                            ((float) ePSXeViewGL.this.mHeight / 3)
                                    - (ePSXeViewGL.this.padSizeScreenLan[0][39] / 2.0f) },
                    new float[] {
                            ((float) ePSXeViewGL.this.mWidth / 2)
                                    - ((ePSXeViewGL.this.padSizeScreenLan[1][0] / 2.0f) * 0.76f),
                            (float) ePSXeViewGL.this.mHeight / 4,
                            ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[1][2] / 2.0f),
                            ePSXeViewGL.this.padSizeScreenLan[1][3] / 2.0f,
                            (((float) ePSXeViewGL.this.mWidth / 2) - ePSXeViewGL.this.padSizeScreenLan[1][4]) - 30.0f,
                            ((float) ePSXeViewGL.this.mHeight / 2) - (ePSXeViewGL.this.padSizeScreenLan[1][5] / 2.0f),
                            ((float) ePSXeViewGL.this.mWidth / 2) + 30,
                            ((float) ePSXeViewGL.this.mHeight / 2) - (ePSXeViewGL.this.padSizeScreenLan[1][7] / 2.0f),
                            ePSXeViewGL.this.padSizeScreenLan[1][8] / 2.0f,
                            ((float) ePSXeViewGL.this.mHeight / 2) - (ePSXeViewGL.this.padSizeScreenLan[1][9] / 2.0f),
                            (ePSXeViewGL.this.padSizeScreenLan[1][10] / 2.0f) + ePSXeViewGL.this.padSizeScreenLan[0][8],
                            ((float) ePSXeViewGL.this.mHeight / 2) - (ePSXeViewGL.this.padSizeScreenLan[1][11] / 2.0f),
                            ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[1][12] / 2.0f),
                            ((float) ePSXeViewGL.this.mHeight / 2) - (ePSXeViewGL.this.padSizeScreenLan[1][13] / 2.0f),
                            (ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[1][14] / 2.0f))
                                    - ePSXeViewGL.this.padSizeScreenLan[1][12],
                            ((float) ePSXeViewGL.this.mHeight / 2) - (ePSXeViewGL.this.padSizeScreenLan[1][15] / 2.0f),
                            ((float) ePSXeViewGL.this.mWidth / 2) + 40 + ePSXeViewGL.this.padSizeScreenLan[1][6],
                            ((float) ePSXeViewGL.this.mHeight / 2) - (ePSXeViewGL.this.padSizeScreenLan[1][17] / 2.0f),
                            (ePSXeViewGL.this.padSizeScreenLan[1][18] / 2.0f) + ePSXeViewGL.this.padSizeScreenLan[1][10]
                                    + ePSXeViewGL.this.padSizeScreenLan[1][8],
                            ((float) ePSXeViewGL.this.mHeight / 2) - (ePSXeViewGL.this.padSizeScreenLan[1][19] / 2.0f),
                            ((ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[1][20] / 2.0f))
                                    - ePSXeViewGL.this.padSizeScreenLan[1][12])
                                    - ePSXeViewGL.this.padSizeScreenLan[1][14],
                            ((float) ePSXeViewGL.this.mHeight / 2) - (ePSXeViewGL.this.padSizeScreenLan[1][21] / 2.0f),
                            ePSXeViewGL.this.padSizeScreenLan[1][22] / 2.0f,
                            ePSXeViewGL.this.padSizeScreenLan[1][23] / 2.0f,
                            ((float) ePSXeViewGL.this.mWidth / 2)
                                    + ((ePSXeViewGL.this.padSizeScreenLan[1][24] / 2.0f) * 0.76f),
                            (float) ePSXeViewGL.this.mHeight / 4, 0.0f, 0.0f,
                            ePSXeViewGL.this.padSizeScreenLan[1][28] / 2.0f,
                            ((float) ePSXeViewGL.this.mHeight / 3) - (ePSXeViewGL.this.padSizeScreenLan[1][29] / 2.0f),
                            (ePSXeViewGL.this.padSizeScreenLan[1][30] / 2.0f)
                                    + ePSXeViewGL.this.padSizeScreenLan[1][28],
                            ((float) ePSXeViewGL.this.mHeight / 3) - (ePSXeViewGL.this.padSizeScreenLan[1][31] / 2.0f),
                            (ePSXeViewGL.this.padSizeScreenLan[1][32] / 2.0f) + ePSXeViewGL.this.padSizeScreenLan[1][28]
                                    + ePSXeViewGL.this.padSizeScreenLan[1][30],
                            ((float) ePSXeViewGL.this.mHeight / 3) - (ePSXeViewGL.this.padSizeScreenLan[1][33] / 2.0f),
                            ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[1][34] / 2.0f),
                            ((float) ePSXeViewGL.this.mHeight / 3) - (ePSXeViewGL.this.padSizeScreenLan[1][35] / 2.0f),
                            (ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[1][36] / 2.0f))
                                    - ePSXeViewGL.this.padSizeScreenLan[1][34],
                            ((float) ePSXeViewGL.this.mHeight / 3) - (ePSXeViewGL.this.padSizeScreenLan[1][37] / 2.0f),
                            ((ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[1][38] / 2.0f))
                                    - ePSXeViewGL.this.padSizeScreenLan[1][34])
                                    - ePSXeViewGL.this.padSizeScreenLan[1][36],
                            ((float) ePSXeViewGL.this.mHeight / 3) - (ePSXeViewGL.this.padSizeScreenLan[1][39] / 2.0f) }
            };
            for (int i = 0; i < 20; i++) {
                ePSXeViewGL.this.padOffScreenLan[ePSXeViewGL.this.mode][(i * 2)
                        + 0] = padOffScreenLantmp[ePSXeViewGL.this.mode][(i * 2) + 0];
                ePSXeViewGL.this.padOffScreenLan[ePSXeViewGL.this.mode][(i * 2)
                        + 1] = padOffScreenLantmp[ePSXeViewGL.this.mode][(i * 2) + 1];
            }
        }

        /**
         * Устанавливает позиции для ландшафтного режима
         */
        private void setLandscapePadPositions() {
            float[][] padOffScreenLantmp = {
                    new float[] { ePSXeViewGL.this.padSizeScreenLan[0][0] / 2.0f,
                            ePSXeViewGL.this.padSizeScreenLan[0][1] / 2.0f,
                            ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[0][2] / 2.0f),
                            ePSXeViewGL.this.padSizeScreenLan[0][3] / 2.0f,
                            (((float) ePSXeViewGL.this.mWidth / 2) - ePSXeViewGL.this.padSizeScreenLan[0][4]) - 30.0f,
                            ePSXeViewGL.this.padSizeScreenLan[0][5] / 2.0f, ((float) ePSXeViewGL.this.mWidth / 2) + 30,
                            ePSXeViewGL.this.padSizeScreenLan[0][7] / 2.0f,
                            ePSXeViewGL.this.padSizeScreenLan[0][8] / 2.0f,
                            ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan[0][9] / 2.0f),
                            (ePSXeViewGL.this.padSizeScreenLan[0][10] / 2.0f) + ePSXeViewGL.this.padSizeScreenLan[0][8],
                            ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan[0][11] / 2.0f),
                            ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[0][12] / 2.0f),
                            ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan[0][13] / 2.0f),
                            (ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[0][14] / 2.0f))
                                    - ePSXeViewGL.this.padSizeScreenLan[0][12],
                            ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan[0][15] / 2.0f),
                            ((float) ePSXeViewGL.this.mWidth / 2) + 40 + ePSXeViewGL.this.padSizeScreenLan[0][6],
                            ePSXeViewGL.this.padSizeScreenLan[0][17] / 2.0f,
                            (ePSXeViewGL.this.padSizeScreenLan[0][18] / 2.0f) + ePSXeViewGL.this.padSizeScreenLan[0][10]
                                    + ePSXeViewGL.this.padSizeScreenLan[0][8],
                            ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan[0][19] / 2.0f),
                            ((ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[0][20] / 2.0f))
                                    - ePSXeViewGL.this.padSizeScreenLan[0][12])
                                    - ePSXeViewGL.this.padSizeScreenLan[0][14],
                            ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan[0][21] / 2.0f),
                            ePSXeViewGL.this.padSizeScreenLan[0][22] / 2.0f,
                            ePSXeViewGL.this.padSizeScreenLan[0][23] / 2.0f,
                            ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[0][24] / 2.0f),
                            ePSXeViewGL.this.padSizeScreenLan[0][25] / 2.0f, 0.0f, 0.0f,
                            ePSXeViewGL.this.padSizeScreenLan[0][28] / 2.0f,
                            (ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan[0][29] / 2.0f))
                                    - (ePSXeViewGL.this.padSizeScreenLan[0][10] * 2.0f),
                            (ePSXeViewGL.this.padSizeScreenLan[0][30] / 2.0f)
                                    + ePSXeViewGL.this.padSizeScreenLan[0][28],
                            (ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan[0][31] / 2.0f))
                                    - (ePSXeViewGL.this.padSizeScreenLan[0][10] * 2.0f),
                            (ePSXeViewGL.this.padSizeScreenLan[0][32] / 2.0f) + ePSXeViewGL.this.padSizeScreenLan[0][28]
                                    + ePSXeViewGL.this.padSizeScreenLan[0][30],
                            (ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan[0][33] / 2.0f))
                                    - (ePSXeViewGL.this.padSizeScreenLan[0][10] * 2.0f),
                            ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[0][34] / 2.0f),
                            (ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan[0][35] / 2.0f))
                                    - (ePSXeViewGL.this.padSizeScreenLan[0][10] * 2.0f),
                            (ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[0][36] / 2.0f))
                                    - ePSXeViewGL.this.padSizeScreenLan[0][34],
                            (ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan[0][37] / 2.0f))
                                    - (ePSXeViewGL.this.padSizeScreenLan[0][10] * 2.0f),
                            ((ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[0][38] / 2.0f))
                                    - ePSXeViewGL.this.padSizeScreenLan[0][34])
                                    - ePSXeViewGL.this.padSizeScreenLan[0][36],
                            (ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan[0][39] / 2.0f))
                                    - (ePSXeViewGL.this.padSizeScreenLan[0][10] * 2.0f) },
                    new float[] {
                            ((float) ePSXeViewGL.this.mWidth / 2)
                                    - ((ePSXeViewGL.this.padSizeScreenLan[1][0] / 2.0f) * 0.76f),
                            (float) ePSXeViewGL.this.mHeight / 2,
                            ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[1][2] / 2.0f),
                            ePSXeViewGL.this.padSizeScreenLan[1][3] / 2.0f,
                            (((float) ePSXeViewGL.this.mWidth / 2) - ePSXeViewGL.this.padSizeScreenLan[1][4]) - 30.0f,
                            ePSXeViewGL.this.padSizeScreenLan[1][5] / 2.0f, ((float) ePSXeViewGL.this.mWidth / 2) + 30,
                            ePSXeViewGL.this.padSizeScreenLan[1][7] / 2.0f,
                            ePSXeViewGL.this.padSizeScreenLan[1][8] / 2.0f,
                            ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan[1][9] / 2.0f),
                            (ePSXeViewGL.this.padSizeScreenLan[1][10] / 2.0f) + ePSXeViewGL.this.padSizeScreenLan[1][8],
                            ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan[1][11] / 2.0f),
                            ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[1][12] / 2.0f),
                            ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan[1][13] / 2.0f),
                            (ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[1][14] / 2.0f))
                                    - ePSXeViewGL.this.padSizeScreenLan[1][12],
                            ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan[1][15] / 2.0f),
                            ((float) ePSXeViewGL.this.mWidth / 2) + 40 + ePSXeViewGL.this.padSizeScreenLan[1][6],
                            ePSXeViewGL.this.padSizeScreenLan[1][17] / 2.0f,
                            (ePSXeViewGL.this.padSizeScreenLan[1][18] / 2.0f) + ePSXeViewGL.this.padSizeScreenLan[1][10]
                                    + ePSXeViewGL.this.padSizeScreenLan[1][8],
                            ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan[1][19] / 2.0f),
                            ((ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[1][20] / 2.0f))
                                    - ePSXeViewGL.this.padSizeScreenLan[1][12])
                                    - ePSXeViewGL.this.padSizeScreenLan[1][14],
                            ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan[1][21] / 2.0f),
                            ePSXeViewGL.this.padSizeScreenLan[1][22] / 2.0f,
                            ePSXeViewGL.this.padSizeScreenLan[1][23] / 2.0f,
                            ((float) ePSXeViewGL.this.mWidth / 2)
                                    + ((ePSXeViewGL.this.padSizeScreenLan[1][24] / 2.0f) * 0.76f),
                            (float) ePSXeViewGL.this.mHeight / 2, 0.0f, 0.0f,
                            ePSXeViewGL.this.padSizeScreenLan[1][28] / 2.0f,
                            (ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan[1][29] / 2.0f))
                                    - (ePSXeViewGL.this.padSizeScreenLan[1][10] * 2.0f),
                            (ePSXeViewGL.this.padSizeScreenLan[1][30] / 2.0f)
                                    + ePSXeViewGL.this.padSizeScreenLan[1][28],
                            (ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan[1][31] / 2.0f))
                                    - (ePSXeViewGL.this.padSizeScreenLan[1][10] * 2.0f),
                            (ePSXeViewGL.this.padSizeScreenLan[1][32] / 2.0f) + ePSXeViewGL.this.padSizeScreenLan[1][28]
                                    + ePSXeViewGL.this.padSizeScreenLan[1][30],
                            (ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan[1][33] / 2.0f))
                                    - (ePSXeViewGL.this.padSizeScreenLan[1][10] * 2.0f),
                            ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[1][34] / 2.0f),
                            (ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan[1][35] / 2.0f))
                                    - (ePSXeViewGL.this.padSizeScreenLan[1][10] * 2.0f),
                            (ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[1][36] / 2.0f))
                                    - ePSXeViewGL.this.padSizeScreenLan[1][34],
                            (ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan[1][37] / 2.0f))
                                    - (ePSXeViewGL.this.padSizeScreenLan[1][10] * 2.0f),
                            ((ePSXeViewGL.this.mWidth - (ePSXeViewGL.this.padSizeScreenLan[1][38] / 2.0f))
                                    - ePSXeViewGL.this.padSizeScreenLan[1][34])
                                    - ePSXeViewGL.this.padSizeScreenLan[1][36],
                            (ePSXeViewGL.this.mHeight - (ePSXeViewGL.this.padSizeScreenLan[1][39] / 2.0f))
                                    - (ePSXeViewGL.this.padSizeScreenLan[1][10] * 2.0f) }
            };
            for (int i = 0; i < 20; i++) {
                ePSXeViewGL.this.padOffScreenLan[ePSXeViewGL.this.mode][(i * 2)
                        + 0] = padOffScreenLantmp[ePSXeViewGL.this.mode][(i * 2) + 0];
                ePSXeViewGL.this.padOffScreenLan[ePSXeViewGL.this.mode][(i * 2)
                        + 1] = padOffScreenLantmp[ePSXeViewGL.this.mode][(i * 2) + 1];
            }
        }

        /**
         * Применяет финальное масштабирование к размерам и позициям после загрузки из
         * XML
         */
        private void applyFinalScalingToPads() {
            // Применяем padResize к размерам
            for (int i = 0; i < 20; i++) {
                ePSXeViewGL.this.padSizeScreenLan[0][(i * 2) + 0] *= ePSXeViewGL.this.padResize;
                ePSXeViewGL.this.padSizeScreenLan[0][(i * 2) + 1] *= ePSXeViewGL.this.padResize;
                ePSXeViewGL.this.padSizeScreenLan[1][(i * 2) + 0] *= ePSXeViewGL.this.padResize;
                ePSXeViewGL.this.padSizeScreenLan[1][(i * 2) + 1] *= ePSXeViewGL.this.padResize;
            }

            // Применяем padResize к позициям
            for (int i = 0; i < 20; i++) {
                ePSXeViewGL.this.padOffScreenLan[0][(i * 2) + 0] *= ePSXeViewGL.this.padResize;
                ePSXeViewGL.this.padOffScreenLan[0][(i * 2) + 1] *= ePSXeViewGL.this.padResize;
                ePSXeViewGL.this.padOffScreenLan[1][(i * 2) + 0] *= ePSXeViewGL.this.padResize;
                ePSXeViewGL.this.padOffScreenLan[1][(i * 2) + 1] *= ePSXeViewGL.this.padResize;
            }
        }

        private void loadExtraButtons() {
            if (ePSXeViewGL.this.emu_player_mode == 1) {
                String cpadprofile = ePSXeViewGL.this.padprofile;
                if (ePSXeViewGL.this.mePSXeReadPreferences == null) {
                    ePSXeViewGL.this.mePSXeReadPreferences = new ePSXeReadPreferences(ePSXeViewGL.this.mContext);
                }
                ePSXeViewGL.this.padScreenExtraCombo = 0;
                ePSXeViewGL.this.padScreenExtraEnabled = 0;
                if (ePSXeViewGL.this.emu_screen_orientation == 1) {
                    ePSXeViewGL.this.padprofile = "";
                }
                for (int i = 0; i < 6; i++) {
                    int val = ePSXeViewGL.this.mePSXeReadPreferences
                            .getPadExtra(ePSXeViewGL.this.padprofile + "inputExtrasPref" + (i + 1));
                    if (val == 19) {
                        val = -1;
                    }
                    if (val == 28) {
                        val = 19;
                    }
                    if (val == -1) {
                        ePSXeViewGL.this.padScreenStatus[0][i + 14] = 2;
                        ePSXeViewGL.this.padScreenStatus[1][i + 14] = 2;
                    } else {
                        ePSXeViewGL.this.padScreenExtraEnabled = 1;
                        ePSXeViewGL.this.padScreenStatus[0][i + 14] = 1;
                        ePSXeViewGL.this.padScreenStatus[1][i + 14] = 1;
                    }
                    ePSXeViewGL.this.padScreenExtra[i] = val;
                    if (ePSXeViewGL.this.padScreenExtra[i] >= 0 && ePSXeViewGL.this.padScreenExtra[i] < 5) {
                        ePSXeViewGL.this.padScreenFunc[i] = 1;
                    } else if (ePSXeViewGL.this.padScreenExtra[i] < 10) {
                        ePSXeViewGL.this.padScreenFunc[i] = 1;
                    } else if (ePSXeViewGL.this.padScreenExtra[i] < 18) {
                        ePSXeViewGL.this.padScreenFunc[i] = 2;
                        ePSXeViewGL.this.padScreenExtraCombo = 1;
                    } else if (ePSXeViewGL.this.padScreenExtra[i] == 18) {
                        ePSXeViewGL.this.padScreenFunc[i] = 1;
                    } else if (ePSXeViewGL.this.padScreenExtra[i] == 19) {
                        ePSXeViewGL.this.padScreenFunc[i] = 1;
                    } else if (ePSXeViewGL.this.padScreenExtra[i] == 20) {
                        ePSXeViewGL.this.padScreenFunc[i] = 1;
                    } else if (ePSXeViewGL.this.padScreenExtra[i] == 21) {
                        ePSXeViewGL.this.padScreenFunc[i] = 1;
                    } else if (ePSXeViewGL.this.padScreenExtra[i] == 22) {
                        ePSXeViewGL.this.padScreenFunc[i] = 1;
                    } else if (ePSXeViewGL.this.padScreenExtra[i] == 23) {
                        ePSXeViewGL.this.padScreenFunc[i] = 1;
                    } else if (ePSXeViewGL.this.padScreenExtra[i] == 24) {
                        ePSXeViewGL.this.padScreenFunc[i] = 1;
                    } else if (ePSXeViewGL.this.padScreenExtra[i] == 25) {
                        ePSXeViewGL.this.padScreenFunc[i] = 1;
                    } else if (ePSXeViewGL.this.padScreenExtra[i] == 26) {
                        ePSXeViewGL.this.padScreenFunc[i] = 1;
                    } else if (ePSXeViewGL.this.padScreenExtra[i] == 27) {
                        ePSXeViewGL.this.padScreenFunc[i] = 1;
                    } else {
                        ePSXeViewGL.this.padScreenFunc[i] = 0;
                    }
                }
                if (ePSXeViewGL.this.emu_screen_orientation == 1) {
                    ePSXeViewGL.this.padprofile = cpadprofile;
                }
            }
        }

        @Override // android.opengl.GLSurfaceView.Renderer
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            Log.e("ePSXeView", "onSurfaceChanged");
            if (ePSXeViewGL.this.onPauseMode != 1) {
                ePSXeViewGL.this.mWidth = width;
                ePSXeViewGL.this.mHeight = height;
                ePSXeViewGL.this.f165e.openglresize(width, height, ePSXeViewGL.this.emu_player_mode,
                        ePSXeViewGL.this.emu_split_mode, ePSXeViewGL.this.emu_screen_ratio,
                        ePSXeViewGL.this.emu_screen_orientation, ePSXeViewGL.this.emu_screen_vrmode,
                        ePSXeViewGL.this.emu_screen_vrdistorsion);
                gl.glViewport(0, 0, width, height);
                gl.glMatrixMode(5889);
                gl.glLoadIdentity();
                gl.glOrthof(0.0f, width, 0.0f, height, 1.0f, -1.0f);
                redoPads(gl);
            }
        }

        private void redoPads(GL10 gl) {
            if (ePSXeViewGL.this.mePSXeReadPreferences == null) {
                ePSXeViewGL.this.mePSXeReadPreferences = new ePSXeReadPreferences(ePSXeViewGL.this.mContext);
            }
            if (ePSXeViewGL.this.mePSXeReadPreferences.getPadStatus(ePSXeViewGL.this.padprofile + "Pad1Status1") != -1
                    && ePSXeViewGL.this.emu_player_mode == 1
                    && (ePSXeViewGL.this.emu_screen_orientation != 1 || ePSXeViewGL.this.emu_portrait_skin == 1)) {
                Log.e("epsxeviewgl", "loading pad info from preferences");

                // Сначала устанавливаем базовые размеры без масштабирования
                int tmode = ePSXeViewGL.this.mode;
                ePSXeViewGL.this.mode = 0;
                setBasePadSizesWithoutResize();
                ePSXeViewGL.this.mode = 1;
                setBasePadSizesWithoutResize();
                ePSXeViewGL.this.mode = tmode;

                // Получаем сохраненные размеры экрана
                ePSXeViewGL.this.mWidthSaved = ePSXeViewGL.this.mePSXeReadPreferences
                        .getPadWH(ePSXeViewGL.this.padprofile + "Pad1Width");
                ePSXeViewGL.this.mHeightSaved = ePSXeViewGL.this.mePSXeReadPreferences
                        .getPadWH(ePSXeViewGL.this.padprofile + "Pad1Height");

                // Вычисляем коэффициенты масштабирования для изменения разрешения
                float resizeX = 1.0f;
                float resizeY = 1.0f;
                if (ePSXeViewGL.this.mWidthSaved != 0 && ePSXeViewGL.this.mWidthSaved != ePSXeViewGL.this.mWidth) {
                    resizeX = (float) ePSXeViewGL.this.mWidth / ePSXeViewGL.this.mWidthSaved;
                }
                if (ePSXeViewGL.this.mHeightSaved != 0 && ePSXeViewGL.this.mHeightSaved != ePSXeViewGL.this.mHeight) {
                    resizeY = (float) ePSXeViewGL.this.mHeight / ePSXeViewGL.this.mHeightSaved;
                }
                for (int i = 1; i < 14; i++) {
                    int val = ePSXeViewGL.this.mePSXeReadPreferences
                            .getPadStatus(ePSXeViewGL.this.padprofile + "Pad1Status" + i);
                    if (val != -1) {
                        ePSXeViewGL.this.padScreenStatus[0][i - 1] = val;
                    }
                }
                for (int i2 = 14; i2 < 20; i2++) {
                    int val2 = ePSXeViewGL.this.mePSXeReadPreferences
                            .getPadStatus(ePSXeViewGL.this.padprofile + "Pad1Status" + i2);
                    if (val2 != -1) {
                        ePSXeViewGL.this.padScreenStatus[0][i2] = val2;
                    }
                }
                for (int i3 = 1; i3 < 14; i3++) {
                    int val3 = ePSXeViewGL.this.mePSXeReadPreferences
                            .getPadStatus(ePSXeViewGL.this.padprofile + "Pad1StatusAnalog" + i3);
                    if (val3 != -1) {
                        ePSXeViewGL.this.padScreenStatus[1][i3 - 1] = val3;
                    }
                }
                for (int i4 = 14; i4 < 20; i4++) {
                    int val4 = ePSXeViewGL.this.mePSXeReadPreferences
                            .getPadStatus(ePSXeViewGL.this.padprofile + "Pad1StatusAnalog" + i4);
                    if (val4 != -1) {
                        ePSXeViewGL.this.padScreenStatus[1][i4] = val4;
                    }
                }
                for (int i5 = 1; i5 < 14; i5++) {
                    float x = ePSXeViewGL.this.mePSXeReadPreferences
                            .getPadSize(ePSXeViewGL.this.padprofile + "Pad1SizeX" + i5);
                    float y = ePSXeViewGL.this.mePSXeReadPreferences
                            .getPadSize(ePSXeViewGL.this.padprofile + "Pad1SizeY" + i5);
                    if (x != -1.0f) {
                        ePSXeViewGL.this.padSizeScreenLan[0][(i5 - 1) * 2] = x;
                        ePSXeViewGL.this.padSizeScreenLan[0][((i5 - 1) * 2) + 1] = y;
                    }
                }
                for (int i6 = 14; i6 < 20; i6++) {
                    float x2 = ePSXeViewGL.this.mePSXeReadPreferences
                            .getPadSize(ePSXeViewGL.this.padprofile + "Pad1SizeX" + i6);
                    float y2 = ePSXeViewGL.this.mePSXeReadPreferences
                            .getPadSize(ePSXeViewGL.this.padprofile + "Pad1SizeY" + i6);
                    if (x2 != -1.0f) {
                        ePSXeViewGL.this.padSizeScreenLan[0][i6 * 2] = x2;
                        ePSXeViewGL.this.padSizeScreenLan[0][(i6 * 2) + 1] = y2;
                    }
                }
                for (int i7 = 1; i7 < 14; i7++) {
                    float x3 = ePSXeViewGL.this.mePSXeReadPreferences
                            .getPadSize(ePSXeViewGL.this.padprofile + "Pad1SizeXAnalog" + i7);
                    float y3 = ePSXeViewGL.this.mePSXeReadPreferences
                            .getPadSize(ePSXeViewGL.this.padprofile + "Pad1SizeYAnalog" + i7);
                    if (x3 != -1.0f) {
                        ePSXeViewGL.this.padSizeScreenLan[1][(i7 - 1) * 2] = x3;
                        ePSXeViewGL.this.padSizeScreenLan[1][((i7 - 1) * 2) + 1] = y3;
                    }
                }
                for (int i8 = 14; i8 < 20; i8++) {
                    float x4 = ePSXeViewGL.this.mePSXeReadPreferences
                            .getPadSize(ePSXeViewGL.this.padprofile + "Pad1SizeXAnalog" + i8);
                    float y4 = ePSXeViewGL.this.mePSXeReadPreferences
                            .getPadSize(ePSXeViewGL.this.padprofile + "Pad1SizeYAnalog" + i8);
                    if (x4 != -1.0f) {
                        ePSXeViewGL.this.padSizeScreenLan[1][i8 * 2] = x4;
                        ePSXeViewGL.this.padSizeScreenLan[1][(i8 * 2) + 1] = y4;
                    }
                }
                for (int i9 = 1; i9 < 14; i9++) {
                    float x5 = ePSXeViewGL.this.mePSXeReadPreferences
                            .getPadSize(ePSXeViewGL.this.padprofile + "Pad1PosX" + i9);
                    float y5 = ePSXeViewGL.this.mePSXeReadPreferences
                            .getPadSize(ePSXeViewGL.this.padprofile + "Pad1PosY" + i9);
                    if (x5 != -1.0f) {
                        ePSXeViewGL.this.padOffScreenLan[0][(i9 - 1) * 2] = x5;
                        ePSXeViewGL.this.padOffScreenLan[0][((i9 - 1) * 2) + 1] = y5;
                    }
                }
                for (int i10 = 14; i10 < 20; i10++) {
                    float x6 = ePSXeViewGL.this.mePSXeReadPreferences
                            .getPadSize(ePSXeViewGL.this.padprofile + "Pad1PosX" + i10);
                    float y6 = ePSXeViewGL.this.mePSXeReadPreferences
                            .getPadSize(ePSXeViewGL.this.padprofile + "Pad1PosY" + i10);
                    if (x6 != -1.0f) {
                        ePSXeViewGL.this.padOffScreenLan[0][i10 * 2] = x6;
                        ePSXeViewGL.this.padOffScreenLan[0][(i10 * 2) + 1] = y6;
                    }
                }
                for (int i11 = 1; i11 < 14; i11++) {
                    float x7 = ePSXeViewGL.this.mePSXeReadPreferences
                            .getPadSize(ePSXeViewGL.this.padprofile + "Pad1PosXAnalog" + i11);
                    float y7 = ePSXeViewGL.this.mePSXeReadPreferences
                            .getPadSize(ePSXeViewGL.this.padprofile + "Pad1PosYAnalog" + i11);
                    if (x7 != -1.0f) {
                        ePSXeViewGL.this.padOffScreenLan[1][(i11 - 1) * 2] = x7;
                        ePSXeViewGL.this.padOffScreenLan[1][((i11 - 1) * 2) + 1] = y7;
                    }
                }
                for (int i12 = 14; i12 < 20; i12++) {
                    float x8 = ePSXeViewGL.this.mePSXeReadPreferences
                            .getPadSize(ePSXeViewGL.this.padprofile + "Pad1PosXAnalog" + i12);
                    float y8 = ePSXeViewGL.this.mePSXeReadPreferences
                            .getPadSize(ePSXeViewGL.this.padprofile + "Pad1PosYAnalog" + i12);
                    if (x8 != -1.0f) {
                        ePSXeViewGL.this.padOffScreenLan[1][i12 * 2] = x8;
                        ePSXeViewGL.this.padOffScreenLan[1][(i12 * 2) + 1] = y8;
                    }
                }
                for (int i13 = 1; i13 < 14; i13++) {
                    float val5 = ePSXeViewGL.this.mePSXeReadPreferences
                            .getPadResize(ePSXeViewGL.this.padprofile + "Pad1Resize" + i13);
                    if (val5 != -1.0f) {
                        ePSXeViewGL.this.padScreenResize[0][i13 - 1] = val5;
                    }
                }
                for (int i14 = 14; i14 < 20; i14++) {
                    float val6 = ePSXeViewGL.this.mePSXeReadPreferences
                            .getPadResize(ePSXeViewGL.this.padprofile + "Pad1Resize" + i14);
                    if (val6 != -1.0f) {
                        ePSXeViewGL.this.padScreenResize[0][i14] = val6;
                    }
                }
                for (int i15 = 1; i15 < 14; i15++) {
                    float val7 = ePSXeViewGL.this.mePSXeReadPreferences
                            .getPadResize(ePSXeViewGL.this.padprofile + "Pad1ResizeAnalog" + i15);
                    if (val7 != -1.0f) {
                        ePSXeViewGL.this.padScreenResize[1][i15 - 1] = val7;
                    }
                }
                for (int i16 = 14; i16 < 20; i16++) {
                    float val8 = ePSXeViewGL.this.mePSXeReadPreferences
                            .getPadResize(ePSXeViewGL.this.padprofile + "Pad1ResizeAnalog" + i16);
                    if (val8 != -1.0f) {
                        ePSXeViewGL.this.padScreenResize[1][i16] = val8;
                    }
                }
                // Применяем коррекцию разрешения к размерам и позициям
                for (int i17 = 0; i17 < 20; i17++) {
                    ePSXeViewGL.this.padSizeScreenLan[0][i17 * 2] = ePSXeViewGL.this.padSizeScreenLan[0][i17 * 2]
                            * resizeX;
                    ePSXeViewGL.this.padSizeScreenLan[0][(i17 * 2)
                            + 1] = ePSXeViewGL.this.padSizeScreenLan[0][(i17 * 2) + 1] * resizeY;
                    ePSXeViewGL.this.padSizeScreenLan[1][i17 * 2] = ePSXeViewGL.this.padSizeScreenLan[1][i17 * 2]
                            * resizeX;
                    ePSXeViewGL.this.padSizeScreenLan[1][(i17 * 2)
                            + 1] = ePSXeViewGL.this.padSizeScreenLan[1][(i17 * 2) + 1] * resizeY;
                }
                for (int i18 = 0; i18 < 20; i18++) {
                    ePSXeViewGL.this.padOffScreenLan[0][i18 * 2] = ePSXeViewGL.this.padOffScreenLan[0][i18 * 2]
                            * resizeX;
                    ePSXeViewGL.this.padOffScreenLan[0][(i18 * 2)
                            + 1] = ePSXeViewGL.this.padOffScreenLan[0][(i18 * 2) + 1] * resizeY;
                    ePSXeViewGL.this.padOffScreenLan[1][i18 * 2] = ePSXeViewGL.this.padOffScreenLan[1][i18 * 2]
                            * resizeX;
                    ePSXeViewGL.this.padOffScreenLan[1][(i18 * 2)
                            + 1] = ePSXeViewGL.this.padOffScreenLan[1][(i18 * 2) + 1] * resizeY;
                }

                // Теперь применяем базовое масштабирование для текущего разрешения
                calculateBasePadResize();
                applyFinalScalingToPads();
                loadExtraButtons();
                loadExtraButtonsTextures(gl);
            } else {
                Log.e("epsxepadeditor", "setting default pad info");
                if (ePSXeViewGL.this.emu_player_mode != 1 || ePSXeViewGL.this.emu_screen_orientation != 1
                        || ePSXeViewGL.this.emu_portrait_skin != 0) {
                    int tmode2 = ePSXeViewGL.this.mode;
                    ePSXeViewGL.this.mode = 0;
                    resetPad1Values();
                    ePSXeViewGL.this.mode = 1;
                    resetPad1Values();
                    ePSXeViewGL.this.mode = tmode2;
                } else {
                    loadExtraButtons();
                    loadExtraButtonsTextures(gl);
                }
            }
            resetPadAllValues();
            ePSXeViewGL.this.initvirtualPad = 0;
            if (ePSXeViewGL.this.emu_player_mode == 1) {
                if (ePSXeViewGL.this.emu_pad_mode[0] == 1 || ePSXeViewGL.this.emu_pad_mode[0] == 4) {
                    if (ePSXeViewGL.this.emu_screen_orientation == 1 && ePSXeViewGL.this.emu_portrait_skin == 0) {
                        ePSXeViewGL.this.init_motionevent_1playerPortrait();
                        return;
                    } else {
                        ePSXeViewGL.this.init_motionevent_1playerLandscape();
                        ePSXeViewGL.this.resetDynamicPad();
                        return;
                    }
                }
                if (ePSXeViewGL.this.emu_pad_mode[0] == 3 || ePSXeViewGL.this.emu_pad_mode[0] == 8) {
                    ePSXeViewGL.this.gun.initGun(ePSXeViewGL.this.mWidth, ePSXeViewGL.this.mHeight,
                            ePSXeViewGL.this.emu_pad_type_selected);
                }
            }
        }

        public void loadExtraButtonsTextures(GL10 gl) {
            for (int i = 0; i < 6; i++) {
                int j = ePSXeViewGL.this.padScreenExtra[i];
                if (j >= 0) {
                    if (j >= 24) {
                        j += 4;
                    }
                    this.wTexLan[i + 14] = this.padCoordsExtra[(j * 4) + 2] - this.padCoordsExtra[j * 4];
                    this.hTexLan[i + 14] = this.padCoordsExtra[(j * 4) + 3] - this.padCoordsExtra[(j * 4) + 1];
                    this.textureRgnLan[i + 14] = new TextureRegion(this.wTextmp, this.hTextmp,
                            this.padCoordsExtra[j * 4], this.padCoordsExtra[(j * 4) + 1], this.wTexLan[i + 14],
                            this.hTexLan[i + 14]);
                    this.batchLan[i + 14] = new SpriteBatch(gl, 1);
                }
            }
        }

        @Override // android.opengl.GLSurfaceView.Renderer
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            Log.e("ePSXeRenderer", "onSurfaceCreated");
            this.glText = new GLText(gl, ePSXeViewGL.this.mContext.getAssets());
            this.glText.load("Roboto-Regular.ttf", 28, 2, 2);
            if (ePSXeViewGL.this.emu_player_mode != 1 || ePSXeViewGL.this.emu_screen_orientation != 1
                    || ePSXeViewGL.this.emu_portrait_skin != 0) {
                if (ePSXeViewGL.this.emu_pad_mode[0] != 3) {
                    if (ePSXeViewGL.this.emu_pad_mode[0] == 8) {
                        int[] padCoords = { 384, 64, 448, 256, 448, 64, 511, 256 };
                        this.mTexLan = loadTexture(gl, ePSXeViewGL.this.mContext, R.drawable.extra_buttons);
                        for (int i = 0; i < 2; i++) {
                            this.wTexLan[i] = padCoords[(i * 4) + 2] - padCoords[i * 4];
                            this.hTexLan[i] = padCoords[(i * 4) + 3] - padCoords[(i * 4) + 1];
                            this.textureRgnLan[i] = new TextureRegion(this.wTextmp, this.hTextmp, padCoords[i * 4],
                                    padCoords[(i * 4) + 1], this.wTexLan[i], this.hTexLan[i]);
                            this.batchLan[i] = new SpriteBatch(gl, 1);
                        }
                        return;
                    }
                    int[] padCoords2 = {
                            // x1, y1, x2, y2
                            2, 1, 224, 225, // Dpad Up, Down, Left, Right - keys
                            1, 238, 223, 486, // Action keys
                            253, 8, 309, 52, // Stop key
                            372, 4, 433, 56, // Play key
                            249, 80, 307, 132, // L1 key
                            308, 80, 364, 132, // L2 key
                            308, 144, 364, 196, // R1 key
                            250, 144, 307, 196, // R2 key
                            254, 208, 297, 239, // LED1
                            365, 80, 421, 132, // L3 key
                            365, 144, 422, 196, // R3 key
                            289, 289, 511, 511, // Touch pad area
                            289, 289, 511, 511, // ------"-------
                            422, 144, 491, 215 // Some ball button
                    };
                    int[] padCoordsAction = {
                            77, 244, 149, 316, // Action - triangle
                            146, 328, 218, 400, // Action - circle
                            77, 412, 149, 484, // Action - x
                            6, 328, 78, 400 // Action - square
                    };
                    int[] padCoordsDpad = {
                            76, 7, 146, 94, // Dpad - up
                            130, 80, 218, 149, // Dpad - right
                            76, 134, 146, 220, // Dpad - down
                            4, 80, 92, 149 // Dpad - left
                    };
                    if (ePSXeViewGL.this.emu_pad_draw_mode[0] == 4) {
                        File f = new File(ePSXeViewGL.this.skinName);
                        if (f.exists()) {
                            this.mTexLan = loadTextureFromFile(gl, ePSXeViewGL.this.mContext,
                                    ePSXeViewGL.this.skinName);
                        } else {
                            ePSXeViewGL.this.emu_pad_draw_mode[0] = 0;
                        }
                    }
                    switch (ePSXeViewGL.this.emu_pad_draw_mode[0]) {
                        case 0:
                            this.mTexLan = loadTexture(gl, ePSXeViewGL.this.mContext, R.drawable.pure_white_digital);
                            break;
                        case 1:
                            this.mTexLan = loadTexture(gl, ePSXeViewGL.this.mContext, R.drawable.pure_white_analog);
                            break;
                        case 10:
                            this.mTexLan = loadTexture(gl, ePSXeViewGL.this.mContext,
                                    R.drawable.amethyst_crystal_digital);
                            break;
                        case 11:
                            this.mTexLan = loadTexture(gl, ePSXeViewGL.this.mContext,
                                    R.drawable.amethyst_crystal_analog);
                            break;
                        case 12:
                            this.mTexLan = loadTexture(gl, ePSXeViewGL.this.mContext, R.drawable.binchotite_digital);
                            break;
                        case 13:
                            this.mTexLan = loadTexture(gl, ePSXeViewGL.this.mContext, R.drawable.binchotite_analog);
                            break;
                        case 14:
                            this.mTexLan = loadTexture(gl, ePSXeViewGL.this.mContext, R.drawable.bold_amethyst_digital);
                            break;
                        case 15:
                            this.mTexLan = loadTexture(gl, ePSXeViewGL.this.mContext, R.drawable.bold_amethyst_analog);
                            break;
                        case 16:
                            this.mTexLan = loadTexture(gl, ePSXeViewGL.this.mContext, R.drawable.bold_digital);
                            break;
                        case 17:
                            this.mTexLan = loadTexture(gl, ePSXeViewGL.this.mContext, R.drawable.bold_analog);
                            break;
                        case 18:
                            this.mTexLan = loadTexture(gl, ePSXeViewGL.this.mContext, R.drawable.bold_emerald_digital);
                            break;
                        case 19:
                            this.mTexLan = loadTexture(gl, ePSXeViewGL.this.mContext, R.drawable.bold_emerald_analog);
                            break;
                        case 20:
                            this.mTexLan = loadTexture(gl, ePSXeViewGL.this.mContext, R.drawable.bold_gold_digital);
                            break;
                        case 21:
                            this.mTexLan = loadTexture(gl, ePSXeViewGL.this.mContext, R.drawable.bold_gold_analog);
                            break;
                        case 22:
                            this.mTexLan = loadTexture(gl, ePSXeViewGL.this.mContext, R.drawable.bold_pink_digital);
                            break;
                        case 23:
                            this.mTexLan = loadTexture(gl, ePSXeViewGL.this.mContext, R.drawable.bold_pink_analog);
                            break;
                        case 24:
                            this.mTexLan = loadTexture(gl, ePSXeViewGL.this.mContext, R.drawable.bold_sapphire_digital);
                            break;
                        case 25:
                            this.mTexLan = loadTexture(gl, ePSXeViewGL.this.mContext, R.drawable.bold_sapphire_analog);
                            break;
                        case 26:
                            this.mTexLan = loadTexture(gl, ePSXeViewGL.this.mContext, R.drawable.bold_silk_digital);
                            break;
                        case 27:
                            this.mTexLan = loadTexture(gl, ePSXeViewGL.this.mContext, R.drawable.bold_silk_analog);
                            break;
                        case 28:
                            this.mTexLan = loadTexture(gl, ePSXeViewGL.this.mContext,
                                    R.drawable.bold_turquoise_digital);
                            break;
                        case 29:
                            this.mTexLan = loadTexture(gl, ePSXeViewGL.this.mContext, R.drawable.bold_turquoise_analog);
                            break;
                        case 30:
                            this.mTexLan = loadTexture(gl, ePSXeViewGL.this.mContext,
                                    R.drawable.emerald_crystal_digital);
                            break;
                        case 31:
                            this.mTexLan = loadTexture(gl, ePSXeViewGL.this.mContext,
                                    R.drawable.emerald_crystal_analog);
                            break;
                        case 32:
                            this.mTexLan = loadTexture(gl, ePSXeViewGL.this.mContext, R.drawable.gold_crystal_digital);
                            break;
                        case 33:
                            this.mTexLan = loadTexture(gl, ePSXeViewGL.this.mContext, R.drawable.gold_crystal_analog);
                            break;
                        case 34:
                            this.mTexLan = loadTexture(gl, ePSXeViewGL.this.mContext,
                                    R.drawable.negative_crystal_digital);
                            break;
                        case 35:
                            this.mTexLan = loadTexture(gl, ePSXeViewGL.this.mContext,
                                    R.drawable.negative_crystal_analog);
                            break;
                        case 36:
                            this.mTexLan = loadTexture(gl, ePSXeViewGL.this.mContext, R.drawable.pink_crystal_digital);
                            break;
                        case 37:
                            this.mTexLan = loadTexture(gl, ePSXeViewGL.this.mContext, R.drawable.pink_crystal_analog);
                            break;
                        case 38:
                            this.mTexLan = loadTexture(gl, ePSXeViewGL.this.mContext,
                                    R.drawable.sapphire_crystal_digital);
                            break;
                        case 39:
                            this.mTexLan = loadTexture(gl, ePSXeViewGL.this.mContext,
                                    R.drawable.sapphire_crystal_analog);
                            break;
                        case 40:
                            this.mTexLan = loadTexture(gl, ePSXeViewGL.this.mContext, R.drawable.silk_crystal_digital);
                            break;
                        case 41:
                            this.mTexLan = loadTexture(gl, ePSXeViewGL.this.mContext, R.drawable.silk_crystal_analog);
                            break;
                        case 42:
                            this.mTexLan = loadTexture(gl, ePSXeViewGL.this.mContext, R.drawable.snow_digital);
                            break;
                        case 43:
                            this.mTexLan = loadTexture(gl, ePSXeViewGL.this.mContext, R.drawable.snow_analog);
                            break;
                        case 44:
                            this.mTexLan = loadTexture(gl, ePSXeViewGL.this.mContext,
                                    R.drawable.turquoise_crystal_digital);
                            break;
                        case 45:
                            this.mTexLan = loadTexture(gl, ePSXeViewGL.this.mContext,
                                    R.drawable.turquoise_crystal_analog);
                            break;
                        default:
                            if (ePSXeViewGL.this.emu_pad_draw_mode[0] != 4) {
                                this.mTexLan = loadTexture(gl, ePSXeViewGL.this.mContext,
                                        R.drawable.pure_white_digital);
                            }
                    }
                    if ((ePSXeViewGL.this.emu_pad_draw_mode[0] & 1) == 0 || (ePSXeViewGL.this.emu_pad_draw_mode[0] < 10
                            && ePSXeViewGL.this.emu_pad_draw_mode[0] != 1)) {
                        ePSXeViewGL.this.dpadskin = 1;
                    }
                    for (int i2 = 0; i2 < 14; i2++) {
                        this.wTexLan[i2] = padCoords2[(i2 * 4) + 2] - padCoords2[i2 * 4]; // calculate width
                        this.hTexLan[i2] = padCoords2[(i2 * 4) + 3] - padCoords2[(i2 * 4) + 1]; // calculate height
                        this.textureRgnLan[i2] = new TextureRegion(this.wTextmp, this.hTextmp, padCoords2[i2 * 4],
                                padCoords2[(i2 * 4) + 1], this.wTexLan[i2], this.hTexLan[i2]);
                        this.batchLan[i2] = new SpriteBatch(gl, 1);
                    }
                    for (int i3 = 0; i3 < 4; i3++) {
                        this.wTexLanAction[i3] = padCoordsAction[(i3 * 4) + 2] - padCoordsAction[i3 * 4];
                        this.hTexLanAction[i3] = padCoordsAction[(i3 * 4) + 3] - padCoordsAction[(i3 * 4) + 1];
                        this.textureRgnLanAction[i3] = new TextureRegion(this.wTextmp, this.hTextmp,
                                padCoordsAction[i3 * 4], padCoordsAction[(i3 * 4) + 1], this.wTexLanAction[i3],
                                this.hTexLanAction[i3]);
                        this.batchLanAction[i3] = new SpriteBatch(gl, 1);
                        this.sizeActionX[i3] = (float) (padCoordsAction[(i3 * 4) + 2] - padCoordsAction[i3 * 4])
                                / (padCoords2[6] - padCoords2[4]); // width percent to action block width
                        this.sizeActionY[i3] = (float) (padCoordsAction[(i3 * 4) + 3] - padCoordsAction[(i3 * 4) + 1])
                                / (padCoords2[7] - padCoords2[5]); // height percent to action block height
                        this.offActionX[i3] = (float) ((padCoordsAction[i3 * 4] - padCoords2[4])
                                + ((padCoordsAction[(i3 * 4) + 2] - padCoordsAction[i3 * 4]) / 2))
                                / (padCoords2[6] - padCoords2[4]);
                        this.offActionY[i3] = 1.0f - ((float) ((padCoordsAction[(i3 * 4) + 1] - padCoords2[5])
                                + ((padCoordsAction[(i3 * 4) + 3] - padCoordsAction[(i3 * 4) + 1]) / 2))
                                / (padCoords2[7] - padCoords2[5]));
                    }
                    for (int i4 = 0; i4 < 4; i4++) {
                        this.wTexLanDpad[i4] = padCoordsDpad[(i4 * 4) + 2] - padCoordsDpad[i4 * 4];
                        this.hTexLanDpad[i4] = padCoordsDpad[(i4 * 4) + 3] - padCoordsDpad[(i4 * 4) + 1];
                        this.textureRgnLanDpad[i4] = new TextureRegion(this.wTextmp, this.hTextmp,
                                padCoordsDpad[i4 * 4], padCoordsDpad[(i4 * 4) + 1], this.wTexLanDpad[i4],
                                this.hTexLanDpad[i4]);
                        this.batchLanDpad[i4] = new SpriteBatch(gl, 1);
                        this.sizeDpadX[i4] = (float) ((padCoordsDpad[(i4 * 4) + 2] - padCoords2[0])
                                - (padCoordsDpad[i4 * 4] - padCoords2[0])) / (padCoords2[2] - padCoords2[0]);
                        this.sizeDpadY[i4] = (float) ((padCoordsDpad[(i4 * 4) + 3] - padCoords2[1])
                                - (padCoordsDpad[(i4 * 4) + 1] - padCoords2[1])) / (padCoords2[3] - padCoords2[1]);
                        this.offDpadX[i4] = (float) ((padCoordsDpad[i4 * 4] - padCoords2[0])
                                + (((padCoordsDpad[(i4 * 4) + 2] - padCoords2[0])
                                        - (padCoordsDpad[i4 * 4] - padCoords2[0])) / 2))
                                / (padCoords2[2] - padCoords2[0]);
                        this.offDpadY[i4] = 1.0f - ((float) ((padCoordsDpad[(i4 * 4) + 1] - padCoords2[1])
                                + (((padCoordsDpad[(i4 * 4) + 3] - padCoords2[1])
                                        - (padCoordsDpad[(i4 * 4) + 1] - padCoords2[1])) / 2))
                                / (padCoords2[3] - padCoords2[1]));
                    }
                    loadExtraButtons();
                    this.mTexExtra = loadTexture(gl, ePSXeViewGL.this.mContext, R.drawable.extra_buttons);
                    loadExtraButtonsTextures(gl);
                    for (int i5 = 0; i5 < 4; i5++) {
                        int j = i5 + 24;
                        this.wTexLan[i5 + 20] = this.padCoordsExtra[(j * 4) + 2] - this.padCoordsExtra[j * 4];
                        this.hTexLan[i5 + 20] = this.padCoordsExtra[(j * 4) + 3] - this.padCoordsExtra[(j * 4) + 1];
                        this.textureRgnLan[i5 + 20] = new TextureRegion(this.wTextmp, this.hTextmp,
                                this.padCoordsExtra[j * 4], this.padCoordsExtra[(j * 4) + 1], this.wTexLan[i5 + 20],
                                this.hTexLan[i5 + 20]);
                        this.batchLan[i5 + 20] = new SpriteBatch(gl, 1);
                    }
                    for (int i6 = 0; i6 < 4; i6++) {
                        int j2 = i6 + 32;
                        this.wTexLan[i6 + 24] = this.padCoordsExtra[(j2 * 4) + 2] - this.padCoordsExtra[j2 * 4];
                        this.hTexLan[i6 + 24] = this.padCoordsExtra[(j2 * 4) + 3] - this.padCoordsExtra[(j2 * 4) + 1];
                        this.textureRgnLan[i6 + 24] = new TextureRegion(this.wTextmp, this.hTextmp,
                                this.padCoordsExtra[j2 * 4], this.padCoordsExtra[(j2 * 4) + 1], this.wTexLan[i6 + 24],
                                this.hTexLan[i6 + 24]);
                        this.batchLan[i6 + 24] = new SpriteBatch(gl, 1);
                    }
                    return;
                }
                int[] padCoords3 = {
                        256, 64, 320, 256,
                        320, 64, 384, 256
                };
                this.mTexLan = loadTexture(gl, ePSXeViewGL.this.mContext, R.drawable.extra_buttons);
                for (int i7 = 0; i7 < 2; i7++) {
                    this.wTexLan[i7] = padCoords3[(i7 * 4) + 2] - padCoords3[i7 * 4];
                    this.hTexLan[i7] = padCoords3[(i7 * 4) + 3] - padCoords3[(i7 * 4) + 1];
                    this.textureRgnLan[i7] = new TextureRegion(this.wTextmp, this.hTextmp, padCoords3[i7 * 4],
                            padCoords3[(i7 * 4) + 1], this.wTexLan[i7], this.hTexLan[i7]);
                    this.batchLan[i7] = new SpriteBatch(gl, 1);
                }
                return;
            }
            int[] padCoords4 = { 0, 0, 480, 400 };
            this.mTexPor = loadTexture(gl, ePSXeViewGL.this.mContext, R.drawable.portraitpad);
            this.wTexPor = padCoords4[2] - padCoords4[0];
            this.hTexPor = padCoords4[3] - padCoords4[1];
            this.textureRgnPor = new TextureRegion(this.wTextmp, this.hTextmp, padCoords4[0], padCoords4[1],
                    this.wTexPor, this.hTexPor);
            this.batchPor = new SpriteBatch(gl, 1);
            loadExtraButtons();
            this.mTexExtra = loadTexture(gl, ePSXeViewGL.this.mContext, R.drawable.extra_buttons);
            loadExtraButtonsTextures(gl);
            for (int i8 = 0; i8 < 4; i8++) {
                int j3 = i8 + 24;
                this.wTexLan[i8 + 20] = this.padCoordsExtra[(j3 * 4) + 2] - this.padCoordsExtra[j3 * 4];
                this.hTexLan[i8 + 20] = this.padCoordsExtra[(j3 * 4) + 3] - this.padCoordsExtra[(j3 * 4) + 1];
                this.textureRgnLan[i8 + 20] = new TextureRegion(this.wTextmp, this.hTextmp, this.padCoordsExtra[j3 * 4],
                        this.padCoordsExtra[(j3 * 4) + 1], this.wTexLan[i8 + 20], this.hTexLan[i8 + 20]);
                this.batchLan[i8 + 20] = new SpriteBatch(gl, 1);
            }
            for (int i9 = 0; i9 < 4; i9++) {
                int j4 = i9 + 32;
                this.wTexLan[i9 + 24] = this.padCoordsExtra[(j4 * 4) + 2] - this.padCoordsExtra[j4 * 4];
                this.hTexLan[i9 + 24] = this.padCoordsExtra[(j4 * 4) + 3] - this.padCoordsExtra[(j4 * 4) + 1];
                this.textureRgnLan[i9 + 24] = new TextureRegion(this.wTextmp, this.hTextmp, this.padCoordsExtra[j4 * 4],
                        this.padCoordsExtra[(j4 * 4) + 1], this.wTexLan[i9 + 24], this.hTexLan[i9 + 24]);
                this.batchLan[i9 + 24] = new SpriteBatch(gl, 1);
            }
        }
    }
}
