package com.epsxe.ePSXe;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import okhttp3.internal.http.StatusLine;
import org.apache.http.HttpStatus;

/* loaded from: classes.dex */
public class ePSXePadEditor extends Activity {
    private ePSXePadEditorGL mePSXePadEditorGL;
    private ePSXeReadPreferences mePSXeReadPreferences;
    private String padprofile = "";
    private int emu_screen_orientation = 0;
    private int dialog_selected = 0;

    /* JADX INFO: Access modifiers changed from: private */
    public void setImmersive() {
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(5894);
        }
    }

    @Override // android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String params = getIntent().getStringExtra("com.epsxe.ePSXe.padprofile");
        if (params != null && params.length() > 0) {
            this.padprofile = params;
        }
        getWindow().setFlags(1024, 1024);
        requestWindowFeature(1);
        getWindow().setFlags(128, 128);
        if (this.mePSXePadEditorGL == null) {
            this.mePSXePadEditorGL = new ePSXePadEditorGL(getApplication());
            this.mePSXePadEditorGL.setRenderer();
        }
        if (this.padprofile.startsWith("PFP")) {
            this.emu_screen_orientation = 1;
        }
        if (this.emu_screen_orientation == 1) {
            setRequestedOrientation(1);
        } else {
            setRequestedOrientation(0);
        }
        setContentView(this.mePSXePadEditorGL);
        setImmersive();
    }

    private void alertdialog_quitEditPad() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(R.string.padedit_back);
        alertDialog.setMessage(getString(R.string.padedit_savewant));
        alertDialog.setButton(getString(R.string.padedit_savecontinue), new DialogInterface.OnClickListener() { // from class: com.epsxe.ePSXe.ePSXePadEditor.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                ePSXePadEditor.this.setImmersive();
            }
        });
        alertDialog.setButton3(getString(R.string.padedit_saveno), new DialogInterface.OnClickListener() { // from class: com.epsxe.ePSXe.ePSXePadEditor.2
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                Intent myIntent = new Intent(ePSXePadEditor.this, (Class<?>) ePSXePreferences.class);
                myIntent.putExtra("com.epsxe.ePSXe.screen", "inputpreferences.virtualPad");
                ePSXePadEditor.this.startActivity(myIntent);
                ePSXePadEditor.this.finish();
            }
        });
        alertDialog.setButton2(getString(R.string.padedit_saveyes), new DialogInterface.OnClickListener() { // from class: com.epsxe.ePSXe.ePSXePadEditor.3
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                ePSXePadEditor.this.mePSXePadEditorGL.savePreferences();
                Intent myIntent = new Intent(ePSXePadEditor.this, (Class<?>) ePSXePreferences.class);
                myIntent.putExtra("com.epsxe.ePSXe.screen", "inputpreferences.virtualPad");
                ePSXePadEditor.this.startActivity(myIntent);
                ePSXePadEditor.this.finish();
            }
        });
        alertDialog.setIcon(R.drawable.icon);
        alertDialog.show();
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4 || event.getRepeatCount() != 0) {
            return super.onKeyDown(keyCode, event);
        }
        alertdialog_quitEditPad();
        return true;
    }

    class ePSXePadEditorGL extends GLSurfaceView {
        private static final int BUTTON_DOWN = 1;
        private static final int INPUT_HWBUTTONS = 1;
        private static final int INPUT_TOUCHSCREEN = 0;
        private static final int NO_BUTTON_DOWN = 0;
        int initvirtualPad;
        private Context mContext;
        private int mHeight;
        private int mWidth;
        ePSXeReadPreferences mePSXeReadPreferences;
        long millis;
        int mode;
        int mxi;
        int myi;
        private int nButtons;
        float[][] padOffScreenLan;
        private float padResize;
        int[] padScreenExtra;
        float[][] padScreenResize;
        float[][] padScreenResizeInit;
        int padScreenSelected;
        int[][] padScreenStatus;
        int[][] padScreenStatusInit;
        float[][] padSizeScreenLan;
        int[] virtualPadId;
        int[][] virtualPadPos;

        public ePSXePadEditorGL(Context context) {
            super(context);
            this.mWidth = 800;
            this.mHeight = 480;
            this.nButtons = 20;
            this.padResize = 1.0f;
            this.padSizeScreenLan = new float[][]{new float[]{400.0f, 60.0f, 228.0f, 228.0f, 224.0f, 248.0f, 66.0f, 50.0f, 66.0f, 62.0f, 64.0f, 60.0f, 64.0f, 60.0f, 64.0f, 60.0f, 64.0f, 60.0f, 53.0f, 41.0f, 64.0f, 60.0f, 64.0f, 60.0f, 222.0f, 222.0f, 222.0f, 222.0f, 60.0f, 60.0f, 60.0f, 60.0f, 60.0f, 60.0f, 60.0f, 60.0f, 60.0f, 60.0f, 60.0f, 60.0f}, new float[]{400.0f, 60.0f, 228.0f, 228.0f, 224.0f, 248.0f, 66.0f, 50.0f, 66.0f, 62.0f, 64.0f, 60.0f, 64.0f, 60.0f, 64.0f, 60.0f, 64.0f, 60.0f, 53.0f, 41.0f, 64.0f, 60.0f, 64.0f, 60.0f, 222.0f, 222.0f, 222.0f, 222.0f, 60.0f, 60.0f, 60.0f, 60.0f, 60.0f, 60.0f, 60.0f, 60.0f, 60.0f, 60.0f, 60.0f, 60.0f}};
            this.padOffScreenLan = new float[][]{new float[]{400.0f, 455.0f, this.padSizeScreenLan[0][2] / 2.0f, this.padSizeScreenLan[0][3] / 2.0f, 800.0f - (this.padSizeScreenLan[0][4] / 2.0f), this.padSizeScreenLan[0][5] / 2.0f, (400.0f - this.padSizeScreenLan[0][6]) - 30.0f, this.padSizeScreenLan[0][7] / 2.0f, 430.0f, this.padSizeScreenLan[0][9] / 2.0f, this.padSizeScreenLan[0][10] / 2.0f, 480.0f - (this.padSizeScreenLan[0][11] / 2.0f), (this.padSizeScreenLan[0][12] / 2.0f) + this.padSizeScreenLan[0][10], 480.0f - (this.padSizeScreenLan[0][13] / 2.0f), 800.0f - (this.padSizeScreenLan[0][14] / 2.0f), 480.0f - (this.padSizeScreenLan[0][15] / 2.0f), (800.0f - (this.padSizeScreenLan[0][16] / 2.0f)) - this.padSizeScreenLan[0][14], 480.0f - (this.padSizeScreenLan[0][17] / 2.0f), 440.0f + this.padSizeScreenLan[0][8], this.padSizeScreenLan[0][19] / 2.0f, (this.padSizeScreenLan[0][20] / 2.0f) + this.padSizeScreenLan[0][12] + this.padSizeScreenLan[0][10], 480.0f - (this.padSizeScreenLan[0][21] / 2.0f), ((800.0f - (this.padSizeScreenLan[0][22] / 2.0f)) - this.padSizeScreenLan[0][14]) - this.padSizeScreenLan[0][16], 480.0f - (this.padSizeScreenLan[0][23] / 2.0f), this.padSizeScreenLan[0][24] / 2.0f, this.padSizeScreenLan[0][25] / 2.0f, 800.0f - (this.padSizeScreenLan[0][26] / 2.0f), this.padSizeScreenLan[0][27] / 2.0f, this.padSizeScreenLan[0][28] / 2.0f, (480.0f - (this.padSizeScreenLan[0][29] / 2.0f)) - (this.padSizeScreenLan[0][11] * 2.0f), (this.padSizeScreenLan[0][30] / 2.0f) + this.padSizeScreenLan[0][28], (480.0f - (this.padSizeScreenLan[0][31] / 2.0f)) - (this.padSizeScreenLan[0][11] * 2.0f), (this.padSizeScreenLan[0][32] / 2.0f) + this.padSizeScreenLan[0][28] + this.padSizeScreenLan[0][30], (480.0f - (this.padSizeScreenLan[0][33] / 2.0f)) - (this.padSizeScreenLan[0][11] * 2.0f), 800.0f - (this.padSizeScreenLan[0][34] / 2.0f), (480.0f - (this.padSizeScreenLan[0][35] / 2.0f)) - (this.padSizeScreenLan[0][11] * 2.0f), (800.0f - (this.padSizeScreenLan[0][36] / 2.0f)) - this.padSizeScreenLan[0][34], (480.0f - (this.padSizeScreenLan[0][37] / 2.0f)) - (this.padSizeScreenLan[0][11] * 2.0f), ((800.0f - (this.padSizeScreenLan[0][38] / 2.0f)) - this.padSizeScreenLan[0][34]) - this.padSizeScreenLan[0][36], (480.0f - (this.padSizeScreenLan[0][39] / 2.0f)) - (this.padSizeScreenLan[0][11] * 2.0f)}, new float[]{400.0f, 455.0f, 400.0f - ((this.padSizeScreenLan[1][2] / 2.0f) * 0.76f), 240.0f, 800.0f - (this.padSizeScreenLan[1][4] / 2.0f), this.padSizeScreenLan[1][5] / 2.0f, (400.0f - this.padSizeScreenLan[1][6]) - 30.0f, this.padSizeScreenLan[1][7] / 2.0f, 430.0f, this.padSizeScreenLan[1][9] / 2.0f, this.padSizeScreenLan[1][10] / 2.0f, 480.0f - (this.padSizeScreenLan[1][11] / 2.0f), (this.padSizeScreenLan[1][12] / 2.0f) + this.padSizeScreenLan[1][10], 480.0f - (this.padSizeScreenLan[1][13] / 2.0f), 800.0f - (this.padSizeScreenLan[1][14] / 2.0f), 480.0f - (this.padSizeScreenLan[1][15] / 2.0f), (800.0f - (this.padSizeScreenLan[1][16] / 2.0f)) - this.padSizeScreenLan[1][14], 480.0f - (this.padSizeScreenLan[1][17] / 2.0f), 440.0f + this.padSizeScreenLan[1][8], this.padSizeScreenLan[1][19] / 2.0f, (this.padSizeScreenLan[1][20] / 2.0f) + this.padSizeScreenLan[1][12] + this.padSizeScreenLan[1][10], 480.0f - (this.padSizeScreenLan[1][21] / 2.0f), ((800.0f - (this.padSizeScreenLan[1][22] / 2.0f)) - this.padSizeScreenLan[1][14]) - this.padSizeScreenLan[1][16], 480.0f - (this.padSizeScreenLan[1][23] / 2.0f), this.padSizeScreenLan[1][24] / 2.0f, this.padSizeScreenLan[1][25] / 2.0f, 400.0f + ((this.padSizeScreenLan[1][26] / 2.0f) * 0.76f), 240.0f, this.padSizeScreenLan[1][28] / 2.0f, (480.0f - (this.padSizeScreenLan[1][29] / 2.0f)) - (this.padSizeScreenLan[1][11] * 2.0f), (this.padSizeScreenLan[1][30] / 2.0f) + this.padSizeScreenLan[1][28], (480.0f - (this.padSizeScreenLan[1][31] / 2.0f)) - (this.padSizeScreenLan[1][11] * 2.0f), (this.padSizeScreenLan[1][32] / 2.0f) + this.padSizeScreenLan[1][28] + this.padSizeScreenLan[1][30], (480.0f - (this.padSizeScreenLan[1][33] / 2.0f)) - (this.padSizeScreenLan[1][11] * 2.0f), 800.0f - (this.padSizeScreenLan[1][34] / 2.0f), (480.0f - (this.padSizeScreenLan[1][35] / 2.0f)) - (this.padSizeScreenLan[1][11] * 2.0f), (800.0f - (this.padSizeScreenLan[1][36] / 2.0f)) - this.padSizeScreenLan[1][34], (480.0f - (this.padSizeScreenLan[1][37] / 2.0f)) - (this.padSizeScreenLan[1][11] * 2.0f), ((800.0f - (this.padSizeScreenLan[1][38] / 2.0f)) - this.padSizeScreenLan[1][34]) - this.padSizeScreenLan[1][36], (480.0f - (this.padSizeScreenLan[1][39] / 2.0f)) - (this.padSizeScreenLan[1][11] * 2.0f)}};
            this.padScreenStatusInit = new int[][]{new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 4, 4, 2, 2, 2, 2, 2, 2}, new int[]{1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 0, 2, 2, 2, 2, 2, 2}};
            this.padScreenStatus = new int[][]{new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 4, 4, 0, 0, 0, 0, 0, 0}, new int[]{1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0}};
            this.padScreenResizeInit = new float[][]{new float[]{1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f}, new float[]{1.0f, 0.76f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.76f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f}};
            this.padScreenResize = new float[][]{new float[]{1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f}, new float[]{1.0f, 0.76f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.76f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f}};
            this.padScreenExtra = new int[]{-1, -1, -1, -1, -1, -1};
            this.padScreenSelected = -1;
            this.millis = 0L;
            this.mode = 0;
            this.virtualPadPos = (int[][]) Array.newInstance((Class<?>) Integer.TYPE, this.nButtons, 4);
            this.virtualPadId = new int[this.nButtons];
            this.initvirtualPad = 0;
            this.mContext = context;
            init();
        }

        private void init() {
            setFocusable(true);
            setFocusableInTouchMode(true);
            requestFocus();
        }

        public void loadExtraButtons() {
            if (this.mePSXeReadPreferences == null) {
                this.mePSXeReadPreferences = new ePSXeReadPreferences(this.mContext);
            }
            for (int i = 0; i < 6; i++) {
                int val = this.mePSXeReadPreferences.getPadExtra(ePSXePadEditor.this.padprofile + "inputExtrasPref" + (i + 1));
                if (val == 19) {
                    val = -1;
                }
                if (val == 28) {
                    val = 19;
                }
                this.padScreenExtra[i] = val;
                if (val == -1) {
                    this.padScreenStatus[0][i + 14] = 2;
                    this.padScreenStatus[1][i + 14] = 2;
                } else {
                    this.padScreenStatus[0][i + 14] = 1;
                    this.padScreenStatus[1][i + 14] = 1;
                }
            }
        }

        public void setRenderer() {
            setRenderer(new ePSXePadEditorRenderer());
        }

        public void savePreferences() {
            SharedPreferences sharedPreferences = ePSXeApplication.getDefaultSharedPreferences(this.mContext);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            Log.e("epsxepadeditor", "saving preferences");
            editor.putInt(ePSXePadEditor.this.padprofile + "Pad1Width", this.mWidth);
            editor.putInt(ePSXePadEditor.this.padprofile + "Pad1Height", this.mHeight);
            if (this.mode == 0) {
                for (int i = 0; i < this.nButtons; i++) {
                    editor.putInt(ePSXePadEditor.this.padprofile + "Pad1Status" + i, this.padScreenStatus[0][i]);
                }
                for (int i2 = 0; i2 < this.nButtons; i2++) {
                    editor.putFloat(ePSXePadEditor.this.padprofile + "Pad1SizeX" + i2, this.padSizeScreenLan[0][i2 * 2]);
                    editor.putFloat(ePSXePadEditor.this.padprofile + "Pad1SizeY" + i2, this.padSizeScreenLan[0][(i2 * 2) + 1]);
                }
                for (int i3 = 0; i3 < this.nButtons; i3++) {
                    editor.putFloat(ePSXePadEditor.this.padprofile + "Pad1PosX" + i3, this.padOffScreenLan[0][i3 * 2]);
                    editor.putFloat(ePSXePadEditor.this.padprofile + "Pad1PosY" + i3, this.padOffScreenLan[0][(i3 * 2) + 1]);
                }
                for (int i4 = 0; i4 < this.nButtons; i4++) {
                    editor.putFloat(ePSXePadEditor.this.padprofile + "Pad1Resize" + i4, this.padScreenResize[0][i4]);
                }
            } else {
                for (int i5 = 0; i5 < this.nButtons; i5++) {
                    editor.putInt(ePSXePadEditor.this.padprofile + "Pad1StatusAnalog" + i5, this.padScreenStatus[1][i5]);
                }
                for (int i6 = 0; i6 < this.nButtons; i6++) {
                    editor.putFloat(ePSXePadEditor.this.padprofile + "Pad1SizeXAnalog" + i6, this.padSizeScreenLan[1][i6 * 2]);
                    editor.putFloat(ePSXePadEditor.this.padprofile + "Pad1SizeYAnalog" + i6, this.padSizeScreenLan[1][(i6 * 2) + 1]);
                }
                for (int i7 = 0; i7 < this.nButtons; i7++) {
                    editor.putFloat(ePSXePadEditor.this.padprofile + "Pad1PosXAnalog" + i7, this.padOffScreenLan[1][i7 * 2]);
                    editor.putFloat(ePSXePadEditor.this.padprofile + "Pad1PosYAnalog" + i7, this.padOffScreenLan[1][(i7 * 2) + 1]);
                }
                for (int i8 = 0; i8 < this.nButtons; i8++) {
                    editor.putFloat(ePSXePadEditor.this.padprofile + "Pad1ResizeAnalog" + i8, this.padScreenResize[1][i8]);
                }
            }
            editor.commit();
        }

        private void alertdialog_restoreDefault() {
            ePSXePadEditor.this.dialog_selected = 1;
            AlertDialog alertDialog = new AlertDialog.Builder(ePSXePadEditor.this).create();
            alertDialog.setTitle(R.string.padedit_restore);
            alertDialog.setCancelable(false);
            alertDialog.setMessage(ePSXePadEditor.this.getString(R.string.padedit_restorewant));
            alertDialog.setButton(ePSXePadEditor.this.getString(R.string.padedit_restoreno), new DialogInterface.OnClickListener() { // from class: com.epsxe.ePSXe.ePSXePadEditor.ePSXePadEditorGL.1
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialog, int which) {
                    ePSXePadEditor.this.dialog_selected = 0;
                    ePSXePadEditor.this.setImmersive();
                }
            });
            alertDialog.setButton2(ePSXePadEditor.this.getString(R.string.padedit_restoreyes), new DialogInterface.OnClickListener() { // from class: com.epsxe.ePSXe.ePSXePadEditor.ePSXePadEditorGL.2
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialog, int which) {
                    ePSXePadEditor.this.dialog_selected = 0;
                    ePSXePadEditorGL.this.resetPadValues();
                    ePSXePadEditor.this.setImmersive();
                }
            });
            alertDialog.setIcon(R.drawable.icon);
            alertDialog.show();
        }

        private void alertdialog_saveEditPad() {
            ePSXePadEditor.this.dialog_selected = 1;
            AlertDialog alertDialog = new AlertDialog.Builder(ePSXePadEditor.this).create();
            alertDialog.setTitle(R.string.padedit_save);
            alertDialog.setCancelable(false);
            alertDialog.setMessage(ePSXePadEditor.this.getString(R.string.padedit_savewant));
            alertDialog.setButton(ePSXePadEditor.this.getString(R.string.padedit_savecontinue), new DialogInterface.OnClickListener() { // from class: com.epsxe.ePSXe.ePSXePadEditor.ePSXePadEditorGL.3
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialog, int which) {
                    ePSXePadEditor.this.dialog_selected = 0;
                    ePSXePadEditor.this.setImmersive();
                }
            });
            alertDialog.setButton3(ePSXePadEditor.this.getString(R.string.padedit_saveno), new DialogInterface.OnClickListener() { // from class: com.epsxe.ePSXe.ePSXePadEditor.ePSXePadEditorGL.4
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialog, int which) {
                    ePSXePadEditor.this.dialog_selected = 0;
                    Intent myIntent = new Intent(ePSXePadEditor.this, (Class<?>) ePSXePreferences.class);
                    myIntent.putExtra("com.epsxe.ePSXe.screen", "inputpreferences.virtualPad");
                    ePSXePadEditor.this.startActivity(myIntent);
                    ePSXePadEditor.this.finish();
                }
            });
            alertDialog.setButton2(ePSXePadEditor.this.getString(R.string.padedit_saveyes), new DialogInterface.OnClickListener() { // from class: com.epsxe.ePSXe.ePSXePadEditor.ePSXePadEditorGL.5
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialog, int which) {
                    ePSXePadEditor.this.dialog_selected = 0;
                    ePSXePadEditor.this.mePSXePadEditorGL.savePreferences();
                    Intent myIntent = new Intent(ePSXePadEditor.this, (Class<?>) ePSXePreferences.class);
                    myIntent.putExtra("com.epsxe.ePSXe.screen", "inputpreferences.virtualPad");
                    ePSXePadEditor.this.startActivity(myIntent);
                    ePSXePadEditor.this.finish();
                }
            });
            alertDialog.setIcon(R.drawable.icon);
            alertDialog.show();
        }

        private void alertdialog_swapMode() {
            ePSXePadEditor.this.dialog_selected = 1;
            AlertDialog alertDialog = new AlertDialog.Builder(ePSXePadEditor.this).create();
            alertDialog.setTitle(R.string.padedit_swap);
            alertDialog.setCancelable(false);
            alertDialog.setMessage(ePSXePadEditor.this.getString(R.string.padedit_swapwant));
            alertDialog.setButton(ePSXePadEditor.this.getString(R.string.padedit_swapno), new DialogInterface.OnClickListener() { // from class: com.epsxe.ePSXe.ePSXePadEditor.ePSXePadEditorGL.6
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialog, int which) {
                    ePSXePadEditor.this.dialog_selected = 0;
                    ePSXePadEditor.this.setImmersive();
                }
            });
            alertDialog.setButton2(ePSXePadEditor.this.getString(R.string.padedit_swapyes), new DialogInterface.OnClickListener() { // from class: com.epsxe.ePSXe.ePSXePadEditor.ePSXePadEditorGL.7
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialog, int which) {
                    ePSXePadEditor.this.dialog_selected = 0;
                    ePSXePadEditor.this.setImmersive();
                    ePSXePadEditorGL.this.swapMode();
                }
            });
            alertDialog.setIcon(R.drawable.icon);
            alertDialog.show();
        }

        public void updateClip(int ind) {
            if (((int) this.padOffScreenLan[this.mode][ind * 2]) - ((((int) this.padSizeScreenLan[this.mode][ind * 2]) * this.padScreenResize[this.mode][ind]) / 2.0f) < 0.0f) {
                this.padOffScreenLan[this.mode][ind * 2] = (((int) this.padSizeScreenLan[this.mode][ind * 2]) * this.padScreenResize[this.mode][ind]) / 2.0f;
            }
            if (((int) this.padOffScreenLan[this.mode][(ind * 2) + 1]) - ((((int) this.padSizeScreenLan[this.mode][(ind * 2) + 1]) * this.padScreenResize[this.mode][ind]) / 2.0f) < 0.0f) {
                this.padOffScreenLan[this.mode][(ind * 2) + 1] = (((int) this.padSizeScreenLan[this.mode][(ind * 2) + 1]) * this.padScreenResize[this.mode][ind]) / 2.0f;
            }
            if (((int) this.padOffScreenLan[this.mode][ind * 2]) + ((((int) this.padSizeScreenLan[this.mode][ind * 2]) * this.padScreenResize[this.mode][ind]) / 2.0f) > this.mWidth) {
                this.padOffScreenLan[this.mode][ind * 2] = this.mWidth - ((((int) this.padSizeScreenLan[this.mode][ind * 2]) * this.padScreenResize[this.mode][ind]) / 2.0f);
            }
            if (((int) this.padOffScreenLan[this.mode][(ind * 2) + 1]) + ((((int) this.padSizeScreenLan[this.mode][(ind * 2) + 1]) * this.padScreenResize[this.mode][ind]) / 2.0f) > this.mHeight) {
                this.padOffScreenLan[this.mode][(ind * 2) + 1] = this.mHeight - ((((int) this.padSizeScreenLan[this.mode][(ind * 2) + 1]) * this.padScreenResize[this.mode][ind]) / 2.0f);
            }
        }

        public void updateTouchpadDetection(int ind) {
            int offx = ((int) this.padOffScreenLan[this.mode][ind * 2]) - ((int) ((this.padSizeScreenLan[this.mode][ind * 2] * this.padScreenResize[this.mode][ind]) / 2.0f));
            int offy = ((int) this.padOffScreenLan[this.mode][(ind * 2) + 1]) - ((int) ((this.padSizeScreenLan[this.mode][(ind * 2) + 1] * this.padScreenResize[this.mode][ind]) / 2.0f));
            this.virtualPadPos[ind][0] = offx;
            this.virtualPadPos[ind][1] = (this.mHeight - ((int) (this.padSizeScreenLan[this.mode][(ind * 2) + 1] * this.padScreenResize[this.mode][ind]))) - offy;
            this.virtualPadPos[ind][2] = ((int) (this.padSizeScreenLan[this.mode][ind * 2] * this.padScreenResize[this.mode][ind])) + offx;
            this.virtualPadPos[ind][3] = ((this.mHeight - ((int) (this.padSizeScreenLan[this.mode][(ind * 2) + 1] * this.padScreenResize[this.mode][ind]))) - offy) + ((int) (this.padSizeScreenLan[this.mode][(ind * 2) + 1] * this.padScreenResize[this.mode][ind]));
        }

        public void init_motionevent() {
            for (int n = 0; n < this.nButtons; n++) {
                updateTouchpadDetection(n);
                this.virtualPadId[n] = -1;
            }
            this.initvirtualPad = 1;
        }

        public void touchscreenevent(long eventTime, int action, float x, float y, float pressure, float size, int deviceId, int Id) {
            int xi = (int) x;
            int yi = (int) y;
            int sel = 0;
            if (this.initvirtualPad == 0) {
                init_motionevent();
            }
            if (action == 2 && this.virtualPadId[Id] != -1) {
                int ind = this.virtualPadId[Id];
                float[] fArr = this.padOffScreenLan[this.mode];
                int i = ind * 2;
                fArr[i] = fArr[i] + (xi - this.mxi);
                float[] fArr2 = this.padOffScreenLan[this.mode];
                int i2 = (ind * 2) + 1;
                fArr2[i2] = fArr2[i2] + ((yi - this.myi) * (-1));
                updateClip(ind);
                updateTouchpadDetection(ind);
                this.mxi = xi;
                this.myi = yi;
                return;
            }
            if (action == 2 && this.virtualPadId[Id] == -1) {
                action = 0;
            }
            if (action == 261 || action == 5 || action == 517) {
                action = 0;
            }
            if (action == 262 || action == 6 || action == 518) {
                action = 1;
            }
            if (action == 1 && this.virtualPadId[Id] != -1) {
                if (this.padScreenSelected == 0) {
                    this.padScreenSelected = -1;
                }
                this.virtualPadId[Id] = -1;
                this.mxi = -1;
                this.myi = -1;
            }
            if (action == 0) {
                int ind2 = 0;
                while (true) {
                    if (ind2 >= this.nButtons) {
                        break;
                    }
                    if (xi < this.virtualPadPos[ind2][0] || xi > this.virtualPadPos[ind2][2] || yi < this.virtualPadPos[ind2][1] || yi > this.virtualPadPos[ind2][3]) {
                        ind2++;
                    } else if (ind2 == 0) {
                        float butwf = this.padSizeScreenLan[this.mode][ind2] / 6.0f;
                        int butw = (int) butwf;
                        int offx = ((int) this.padOffScreenLan[this.mode][ind2 * 2]) - (((int) this.padSizeScreenLan[this.mode][ind2 * 2]) / 2);
                        int butaction = -1;
                        if (xi <= (butw * 1) + offx) {
                            butaction = 1;
                        } else if (xi <= (butw * 2) + offx) {
                            butaction = 2;
                        } else if (xi <= (butw * 3) + offx) {
                            butaction = 3;
                        } else if (xi <= (butw * 4) + offx) {
                            butaction = 4;
                        } else if (xi <= (butw * 5) + offx) {
                            butaction = 5;
                        } else if (xi <= (butw * 6) + offx) {
                            butaction = 6;
                        }
                        if (butaction >= 4) {
                            sel = 1;
                            if (butaction == 4) {
                                if (ePSXePadEditor.this.dialog_selected == 0) {
                                    alertdialog_restoreDefault();
                                }
                            } else if (butaction == 5) {
                                if (ePSXePadEditor.this.dialog_selected == 0) {
                                    alertdialog_swapMode();
                                }
                            } else if (butaction == 6 && ePSXePadEditor.this.dialog_selected == 0) {
                                alertdialog_saveEditPad();
                            }
                        } else if (this.padScreenSelected <= 0) {
                            this.padScreenSelected = ind2;
                            this.virtualPadId[Id] = ind2;
                            Log.e("epsxepadeditor", "button(" + ind2 + "): ON ");
                            sel = 1;
                            this.mxi = xi;
                            this.myi = yi;
                        } else if (butaction >= 1 && butaction <= 3) {
                            if (butaction == 1) {
                                float[] fArr3 = this.padScreenResize[this.mode];
                                int i3 = this.padScreenSelected;
                                fArr3[i3] = fArr3[i3] - 0.03f;
                                updateClip(this.padScreenSelected);
                                updateTouchpadDetection(this.padScreenSelected);
                            } else if (butaction == 2) {
                                float[] fArr4 = this.padScreenResize[this.mode];
                                int i4 = this.padScreenSelected;
                                fArr4[i4] = fArr4[i4] + 0.03f;
                                updateClip(this.padScreenSelected);
                                updateTouchpadDetection(this.padScreenSelected);
                            } else if (this.padScreenSelected < 14 && this.padScreenStatusInit[this.mode][this.padScreenSelected] != 4 && this.millis + 300 < eventTime) {
                                this.millis = eventTime;
                                int[] iArr = this.padScreenStatus[this.mode];
                                int i5 = this.padScreenSelected;
                                iArr[i5] = iArr[i5] + 1;
                                if (this.padScreenStatus[this.mode][this.padScreenSelected] > 2) {
                                    this.padScreenStatus[this.mode][this.padScreenSelected] = 0;
                                }
                            }
                            sel = 1;
                        }
                        break;
                    } else {
                        this.padScreenSelected = ind2;
                        this.virtualPadId[Id] = ind2;
                        sel = 1;
                        this.mxi = xi;
                        this.myi = yi;
                        break;
                    }
                }
                if (sel == 0) {
                    this.padScreenSelected = -1;
                    this.mxi = -1;
                    this.myi = -1;
                }
            }
        }

        @Override // android.view.View
        public boolean dispatchTouchEvent(MotionEvent ev) {
            queueMotionEvent(ev);
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

        private void queueMotionEvent(MotionEvent event) {
            int action = event.getAction();
            int actionCode = action & 255;
            int i = event.getActionIndex();
            dumpEvent(event);
            if (actionCode == 5 || actionCode == 6 || actionCode == 1 || actionCode == 0) {
                touchscreenevent(event.getEventTime(), event.getAction(), event.getX(i), event.getY(i), event.getPressure(i), event.getSize(i), event.getDeviceId(), event.getPointerId(i));
            } else if (actionCode == 2) {
                for (int ind = 0; ind < event.getPointerCount(); ind++) {
                    touchscreenevent(event.getEventTime(), event.getAction(), event.getX(ind), event.getY(ind), event.getPressure(ind), event.getSize(ind), event.getDeviceId(), event.getPointerId(ind));
                }
            }
        }

        @Override // android.opengl.GLSurfaceView
        public void onPause() {
            Log.e("epsxe", "epsxepadEditor saving status");
        }

        public void resetPadValues() {
            if (ePSXePadEditor.this.emu_screen_orientation != 1) {
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
            } else {
                this.padResize = this.mWidth / 562.0f;
            }
            float[][] padSizeScreenLantmp = {new float[]{400.0f, 60.0f, 228.0f, 228.0f, 224.0f, 248.0f, 66.0f, 50.0f, 66.0f, 62.0f, 64.0f, 60.0f, 64.0f, 60.0f, 64.0f, 60.0f, 64.0f, 60.0f, 53.0f, 41.0f, 64.0f, 60.0f, 64.0f, 60.0f, 222.0f, 222.0f, 222.0f, 222.0f, 60.0f, 60.0f, 60.0f, 60.0f, 60.0f, 60.0f, 60.0f, 60.0f, 60.0f, 60.0f, 60.0f, 60.0f}, new float[]{400.0f, 60.0f, 228.0f, 228.0f, 224.0f, 248.0f, 66.0f, 50.0f, 66.0f, 62.0f, 64.0f, 60.0f, 64.0f, 60.0f, 64.0f, 60.0f, 64.0f, 60.0f, 53.0f, 41.0f, 64.0f, 60.0f, 64.0f, 60.0f, 222.0f, 222.0f, 222.0f, 222.0f, 60.0f, 60.0f, 60.0f, 60.0f, 60.0f, 60.0f, 60.0f, 60.0f, 60.0f, 60.0f, 60.0f, 60.0f}};
            if (ePSXePadEditor.this.emu_screen_orientation != 1) {
                if (this.mWidth > 1500) {
                    this.padSizeScreenLan[this.mode][0] = padSizeScreenLantmp[this.mode][0] * 2.0f;
                    this.padSizeScreenLan[this.mode][1] = padSizeScreenLantmp[this.mode][1] * 2.0f;
                }
            } else {
                this.padSizeScreenLan[this.mode][0] = (padSizeScreenLantmp[this.mode][0] * this.mWidth) / padSizeScreenLantmp[this.mode][0];
                this.padSizeScreenLan[this.mode][1] = (padSizeScreenLantmp[this.mode][1] * this.mWidth) / padSizeScreenLantmp[this.mode][0];
            }
            for (int i = 1; i < this.nButtons; i++) {
                this.padSizeScreenLan[this.mode][(i * 2) + 0] = padSizeScreenLantmp[this.mode][(i * 2) + 0] * this.padResize;
                this.padSizeScreenLan[this.mode][(i * 2) + 1] = padSizeScreenLantmp[this.mode][(i * 2) + 1] * this.padResize;
            }
            if (ePSXePadEditor.this.emu_screen_orientation != 1) {
                float[][] padOffScreenLantmp = {new float[]{(float)this.mWidth / 2, this.mHeight - (this.padSizeScreenLan[0][1] / 2.0f), this.padSizeScreenLan[0][2] / 2.0f, this.padSizeScreenLan[0][3] / 2.0f, this.mWidth - (this.padSizeScreenLan[0][4] / 2.0f), this.padSizeScreenLan[0][5] / 2.0f, (((float)this.mWidth / 2) - this.padSizeScreenLan[0][6]) - 30.0f, this.padSizeScreenLan[0][7] / 2.0f, ((float)this.mWidth / 2) + 30, this.padSizeScreenLan[0][9] / 2.0f, this.padSizeScreenLan[0][10] / 2.0f, this.mHeight - (this.padSizeScreenLan[0][11] / 2.0f), (this.padSizeScreenLan[0][12] / 2.0f) + this.padSizeScreenLan[0][10], this.mHeight - (this.padSizeScreenLan[0][13] / 2.0f), this.mWidth - (this.padSizeScreenLan[0][14] / 2.0f), this.mHeight - (this.padSizeScreenLan[0][15] / 2.0f), (this.mWidth - (this.padSizeScreenLan[0][16] / 2.0f)) - this.padSizeScreenLan[0][14], this.mHeight - (this.padSizeScreenLan[0][17] / 2.0f), ((float)this.mWidth / 2) + 40 + this.padSizeScreenLan[0][8], this.padSizeScreenLan[0][19] / 2.0f, (this.padSizeScreenLan[0][20] / 2.0f) + this.padSizeScreenLan[0][12] + this.padSizeScreenLan[0][10], this.mHeight - (this.padSizeScreenLan[0][21] / 2.0f), ((this.mWidth - (this.padSizeScreenLan[0][22] / 2.0f)) - this.padSizeScreenLan[0][14]) - this.padSizeScreenLan[0][16], this.mHeight - (this.padSizeScreenLan[0][23] / 2.0f), this.padSizeScreenLan[0][24] / 2.0f, this.padSizeScreenLan[0][25] / 2.0f, this.mWidth - (this.padSizeScreenLan[0][26] / 2.0f), this.padSizeScreenLan[0][27] / 2.0f, this.padSizeScreenLan[0][28] / 2.0f, (this.mHeight - (this.padSizeScreenLan[0][29] / 2.0f)) - (this.padSizeScreenLan[0][11] * 2.0f), (this.padSizeScreenLan[0][30] / 2.0f) + this.padSizeScreenLan[0][28], (this.mHeight - (this.padSizeScreenLan[0][31] / 2.0f)) - (this.padSizeScreenLan[0][11] * 2.0f), (this.padSizeScreenLan[0][32] / 2.0f) + this.padSizeScreenLan[0][28] + this.padSizeScreenLan[0][30], (this.mHeight - (this.padSizeScreenLan[0][33] / 2.0f)) - (this.padSizeScreenLan[0][11] * 2.0f), this.mWidth - (this.padSizeScreenLan[0][34] / 2.0f), (this.mHeight - (this.padSizeScreenLan[0][35] / 2.0f)) - (this.padSizeScreenLan[0][11] * 2.0f), (this.mWidth - (this.padSizeScreenLan[0][36] / 2.0f)) - this.padSizeScreenLan[0][34], (this.mHeight - (this.padSizeScreenLan[0][37] / 2.0f)) - (this.padSizeScreenLan[0][11] * 2.0f), ((this.mWidth - (this.padSizeScreenLan[0][38] / 2.0f)) - this.padSizeScreenLan[0][34]) - this.padSizeScreenLan[0][36], (this.mHeight - (this.padSizeScreenLan[0][39] / 2.0f)) - (this.padSizeScreenLan[0][11] * 2.0f)}, new float[]{(float)this.mWidth / 2, this.mHeight - (this.padSizeScreenLan[1][1] / 2.0f), ((float)this.mWidth / 2) - ((this.padSizeScreenLan[1][2] / 2.0f) * 0.76f), (float)this.mHeight / 2, this.mWidth - (this.padSizeScreenLan[1][4] / 2.0f), this.padSizeScreenLan[1][5] / 2.0f, (((float)this.mWidth / 2) - this.padSizeScreenLan[1][6]) - 30.0f, this.padSizeScreenLan[1][7] / 2.0f, ((float)this.mWidth / 2) + 30, this.padSizeScreenLan[1][9] / 2.0f, this.padSizeScreenLan[1][10] / 2.0f, this.mHeight - (this.padSizeScreenLan[1][11] / 2.0f), (this.padSizeScreenLan[1][12] / 2.0f) + this.padSizeScreenLan[1][10], this.mHeight - (this.padSizeScreenLan[1][13] / 2.0f), this.mWidth - (this.padSizeScreenLan[1][14] / 2.0f), this.mHeight - (this.padSizeScreenLan[1][15] / 2.0f), (this.mWidth - (this.padSizeScreenLan[1][16] / 2.0f)) - this.padSizeScreenLan[1][14], this.mHeight - (this.padSizeScreenLan[1][17] / 2.0f), ((float)this.mWidth / 2) + 40 + this.padSizeScreenLan[1][8], this.padSizeScreenLan[1][19] / 2.0f, (this.padSizeScreenLan[1][20] / 2.0f) + this.padSizeScreenLan[1][12] + this.padSizeScreenLan[1][10], this.mHeight - (this.padSizeScreenLan[1][21] / 2.0f), ((this.mWidth - (this.padSizeScreenLan[1][22] / 2.0f)) - this.padSizeScreenLan[1][14]) - this.padSizeScreenLan[1][16], this.mHeight - (this.padSizeScreenLan[1][23] / 2.0f), this.padSizeScreenLan[1][24] / 2.0f, this.padSizeScreenLan[1][25] / 2.0f, ((float)this.mWidth / 2) + ((this.padSizeScreenLan[1][26] / 2.0f) * 0.76f), (float)this.mHeight / 2, this.padSizeScreenLan[1][28] / 2.0f, (this.mHeight - (this.padSizeScreenLan[1][29] / 2.0f)) - (this.padSizeScreenLan[1][11] * 2.0f), (this.padSizeScreenLan[1][30] / 2.0f) + this.padSizeScreenLan[1][28], (this.mHeight - (this.padSizeScreenLan[1][31] / 2.0f)) - (this.padSizeScreenLan[1][11] * 2.0f), (this.padSizeScreenLan[1][32] / 2.0f) + this.padSizeScreenLan[1][28] + this.padSizeScreenLan[1][30], (this.mHeight - (this.padSizeScreenLan[1][33] / 2.0f)) - (this.padSizeScreenLan[1][11] * 2.0f), this.mWidth - (this.padSizeScreenLan[1][34] / 2.0f), (this.mHeight - (this.padSizeScreenLan[1][35] / 2.0f)) - (this.padSizeScreenLan[1][11] * 2.0f), (this.mWidth - (this.padSizeScreenLan[1][36] / 2.0f)) - this.padSizeScreenLan[1][34], (this.mHeight - (this.padSizeScreenLan[1][37] / 2.0f)) - (this.padSizeScreenLan[1][11] * 2.0f), ((this.mWidth - (this.padSizeScreenLan[1][38] / 2.0f)) - this.padSizeScreenLan[1][34]) - this.padSizeScreenLan[1][36], (this.mHeight - (this.padSizeScreenLan[1][39] / 2.0f)) - (this.padSizeScreenLan[1][11] * 2.0f)}};
                for (int i2 = 0; i2 < this.nButtons; i2++) {
                    this.padOffScreenLan[this.mode][(i2 * 2) + 0] = padOffScreenLantmp[this.mode][(i2 * 2) + 0];
                    this.padOffScreenLan[this.mode][(i2 * 2) + 1] = padOffScreenLantmp[this.mode][(i2 * 2) + 1];
                }
            } else {
                float[][] padOffScreenLantmp2 = {new float[]{(float)this.mWidth / 2, this.mHeight - (this.padSizeScreenLan[0][1] / 2.0f), this.padSizeScreenLan[0][2] / 2.0f, this.padSizeScreenLan[0][3] / 2.0f, this.mWidth - (this.padSizeScreenLan[0][4] / 2.0f), this.padSizeScreenLan[0][5] / 2.0f, (((float)this.mWidth / 2) - this.padSizeScreenLan[0][6]) - 30.0f, ((float)this.mHeight / 2) - (this.padSizeScreenLan[0][7] / 2.0f), ((float)this.mWidth / 2) + 30, ((float)this.mHeight / 2) - (this.padSizeScreenLan[0][9] / 2.0f), this.padSizeScreenLan[0][10] / 2.0f, ((float)this.mHeight / 2) - (this.padSizeScreenLan[0][11] / 2.0f), (this.padSizeScreenLan[0][12] / 2.0f) + this.padSizeScreenLan[0][10], ((float)this.mHeight / 2) - (this.padSizeScreenLan[0][13] / 2.0f), this.mWidth - (this.padSizeScreenLan[0][14] / 2.0f), ((float)this.mHeight / 2) - (this.padSizeScreenLan[0][15] / 2.0f), (this.mWidth - (this.padSizeScreenLan[0][16] / 2.0f)) - this.padSizeScreenLan[0][14], ((float)this.mHeight / 2) - (this.padSizeScreenLan[0][17] / 2.0f), ((float)this.mWidth / 2) + 40 + this.padSizeScreenLan[0][8], ((float)this.mHeight / 2) - (this.padSizeScreenLan[0][19] / 2.0f), (this.padSizeScreenLan[0][20] / 2.0f) + this.padSizeScreenLan[0][12] + this.padSizeScreenLan[0][10], ((float)this.mHeight / 2) - (this.padSizeScreenLan[0][21] / 2.0f), ((this.mWidth - (this.padSizeScreenLan[0][22] / 2.0f)) - this.padSizeScreenLan[0][14]) - this.padSizeScreenLan[0][16], ((float)this.mHeight / 2) - (this.padSizeScreenLan[0][23] / 2.0f), this.padSizeScreenLan[0][24] / 2.0f, this.padSizeScreenLan[0][25] / 2.0f, this.mWidth - (this.padSizeScreenLan[0][26] / 2.0f), this.padSizeScreenLan[0][27] / 2.0f, this.padSizeScreenLan[0][28] / 2.0f, ((float)this.mHeight / 3) - (this.padSizeScreenLan[0][29] / 2.0f), (this.padSizeScreenLan[0][30] / 2.0f) + this.padSizeScreenLan[0][28], ((float)this.mHeight / 3) - (this.padSizeScreenLan[0][31] / 2.0f), (this.padSizeScreenLan[0][32] / 2.0f) + this.padSizeScreenLan[0][28] + this.padSizeScreenLan[0][30], ((float)this.mHeight / 3) - (this.padSizeScreenLan[0][33] / 2.0f), this.mWidth - (this.padSizeScreenLan[0][34] / 2.0f), ((float)this.mHeight / 3) - (this.padSizeScreenLan[0][35] / 2.0f), (this.mWidth - (this.padSizeScreenLan[0][36] / 2.0f)) - this.padSizeScreenLan[0][34], ((float)this.mHeight / 3) - (this.padSizeScreenLan[0][37] / 2.0f), ((this.mWidth - (this.padSizeScreenLan[0][38] / 2.0f)) - this.padSizeScreenLan[0][34]) - this.padSizeScreenLan[0][36], ((float)this.mHeight / 3) - (this.padSizeScreenLan[0][39] / 2.0f)}, new float[]{(float)this.mWidth / 2, this.mHeight - (this.padSizeScreenLan[1][1] / 2.0f), ((float)this.mWidth / 2) - ((this.padSizeScreenLan[1][2] / 2.0f) * 0.76f), (float)this.mHeight / 4, this.mWidth - (this.padSizeScreenLan[1][4] / 2.0f), this.padSizeScreenLan[1][5] / 2.0f, (((float)this.mWidth / 2) - this.padSizeScreenLan[1][6]) - 30.0f, ((float)this.mHeight / 2) - (this.padSizeScreenLan[1][7] / 2.0f), ((float)this.mWidth / 2) + 30, ((float)this.mHeight / 2) - (this.padSizeScreenLan[1][9] / 2.0f), this.padSizeScreenLan[1][10] / 2.0f, ((float)this.mHeight / 2) - (this.padSizeScreenLan[1][11] / 2.0f), (this.padSizeScreenLan[1][12] / 2.0f) + this.padSizeScreenLan[0][10], ((float)this.mHeight / 2) - (this.padSizeScreenLan[1][13] / 2.0f), this.mWidth - (this.padSizeScreenLan[1][14] / 2.0f), ((float)this.mHeight / 2) - (this.padSizeScreenLan[1][15] / 2.0f), (this.mWidth - (this.padSizeScreenLan[1][16] / 2.0f)) - this.padSizeScreenLan[1][14], ((float)this.mHeight / 2) - (this.padSizeScreenLan[1][17] / 2.0f), ((float)this.mWidth / 2) + 40 + this.padSizeScreenLan[1][8], ((float)this.mHeight / 2) - (this.padSizeScreenLan[1][19] / 2.0f), (this.padSizeScreenLan[1][20] / 2.0f) + this.padSizeScreenLan[1][12] + this.padSizeScreenLan[1][10], ((float)this.mHeight / 2) - (this.padSizeScreenLan[1][21] / 2.0f), ((this.mWidth - (this.padSizeScreenLan[1][22] / 2.0f)) - this.padSizeScreenLan[1][14]) - this.padSizeScreenLan[1][16], ((float)this.mHeight / 2) - (this.padSizeScreenLan[1][23] / 2.0f), this.padSizeScreenLan[1][24] / 2.0f, this.padSizeScreenLan[1][25] / 2.0f, ((float)this.mWidth / 2) + ((this.padSizeScreenLan[1][26] / 2.0f) * 0.76f), (float)this.mHeight / 4, this.padSizeScreenLan[1][28] / 2.0f, ((float)this.mHeight / 3) - (this.padSizeScreenLan[1][29] / 2.0f), (this.padSizeScreenLan[1][30] / 2.0f) + this.padSizeScreenLan[1][28], ((float)this.mHeight / 3) - (this.padSizeScreenLan[1][31] / 2.0f), (this.padSizeScreenLan[1][32] / 2.0f) + this.padSizeScreenLan[1][28] + this.padSizeScreenLan[1][30], ((float)this.mHeight / 3) - (this.padSizeScreenLan[1][33] / 2.0f), this.mWidth - (this.padSizeScreenLan[1][34] / 2.0f), ((float)this.mHeight / 3) - (this.padSizeScreenLan[1][35] / 2.0f), (this.mWidth - (this.padSizeScreenLan[1][36] / 2.0f)) - this.padSizeScreenLan[1][34], ((float)this.mHeight / 3) - (this.padSizeScreenLan[1][37] / 2.0f), ((this.mWidth - (this.padSizeScreenLan[1][38] / 2.0f)) - this.padSizeScreenLan[1][34]) - this.padSizeScreenLan[1][36], ((float)this.mHeight / 3) - (this.padSizeScreenLan[1][39] / 2.0f)}};
                for (int i3 = 0; i3 < 20; i3++) {
                    this.padOffScreenLan[this.mode][(i3 * 2) + 0] = padOffScreenLantmp2[this.mode][(i3 * 2) + 0];
                    this.padOffScreenLan[this.mode][(i3 * 2) + 1] = padOffScreenLantmp2[this.mode][(i3 * 2) + 1];
                }
            }
            for (int i4 = 0; i4 < 20; i4++) {
                this.padScreenStatus[this.mode][i4] = this.padScreenStatusInit[this.mode][i4];
                this.padScreenResize[this.mode][i4] = this.padScreenResizeInit[this.mode][i4];
            }
            loadExtraButtons();
            this.initvirtualPad = 0;
        }

        public void swapMode() {
            ePSXePadEditor.this.mePSXePadEditorGL.savePreferences();
            this.mode ^= 1;
            init_motionevent();
        }

        private class ePSXePadEditorRenderer implements GLSurfaceView.Renderer {
            SpriteBatch[] batchLan;
            private GLText glText;
            int[] hTexLan;
            int hTextmp;
            private FloatBuffer mFVertexBuffer;
            private ShortBuffer mIndexBuffer;
            private FloatBuffer mTexBuffer;
            int mTexExtra;
            int mTexLan;
            int mTexTools;
            protected ShortBuffer shortBuffer;
            TextureRegion[] textureRgnLan;
            int[] wTexLan;
            int wTextmp;

            private ePSXePadEditorRenderer() {
                this.mTexTools = -1;
                this.mTexLan = -1;
                this.mTexExtra = -1;
                this.batchLan = new SpriteBatch[ePSXePadEditorGL.this.nButtons];
                this.textureRgnLan = new TextureRegion[ePSXePadEditorGL.this.nButtons];
                this.hTexLan = new int[ePSXePadEditorGL.this.nButtons];
                this.wTexLan = new int[ePSXePadEditorGL.this.nButtons];
                this.shortBuffer = null;
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
                return id;
            }

            public void drawDisabled(GL10 gl, int v, boolean disabled) {
                float color = disabled ? 0.0f : 1.0f;
                gl.glColor4f(1.0f, color, 0.0f, 1.0f);
                ByteBuffer vbb = ByteBuffer.allocateDirect(48);
                vbb.order(ByteOrder.nativeOrder());
                this.mFVertexBuffer = vbb.asFloatBuffer();
                ByteBuffer ibb = ByteBuffer.allocateDirect(8);
                ibb.order(ByteOrder.nativeOrder());
                this.mIndexBuffer = ibb.asShortBuffer();
                float ox = ePSXePadEditorGL.this.padOffScreenLan[ePSXePadEditorGL.this.mode][(v * 2) + 0] - ((ePSXePadEditorGL.this.padSizeScreenLan[ePSXePadEditorGL.this.mode][(v * 2) + 0] * ePSXePadEditorGL.this.padScreenResize[ePSXePadEditorGL.this.mode][v]) / 2.0f);
                float oy = ePSXePadEditorGL.this.padOffScreenLan[ePSXePadEditorGL.this.mode][(v * 2) + 1] - ((ePSXePadEditorGL.this.padSizeScreenLan[ePSXePadEditorGL.this.mode][(v * 2) + 1] * ePSXePadEditorGL.this.padScreenResize[ePSXePadEditorGL.this.mode][v]) / 2.0f);
                float sw = ePSXePadEditorGL.this.padSizeScreenLan[ePSXePadEditorGL.this.mode][(v * 2) + 0] * ePSXePadEditorGL.this.padScreenResize[ePSXePadEditorGL.this.mode][v];
                float sh = ePSXePadEditorGL.this.padSizeScreenLan[ePSXePadEditorGL.this.mode][(v * 2) + 1] * ePSXePadEditorGL.this.padScreenResize[ePSXePadEditorGL.this.mode][v];
                float[] coords = {(ox + sw) - 1.0f, 1.0f + oy, 0.0f, (ox + sw) - 1.0f, (int) ((oy + sh) - 1.0f), 0.0f, 1.0f + ox, 1.0f + oy, 0.0f, 1.0f + ox, (int) ((oy + sh) - 1.0f), 0.0f};
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 3; j++) {
                        this.mFVertexBuffer.put(coords[(i * 3) + j]);
                    }
                }
                for (int i2 = 0; i2 < 4; i2++) {
                    this.mIndexBuffer.put((short) i2);
                }
                this.mFVertexBuffer.position(0);
                this.mIndexBuffer.position(0);
                gl.glDisable(3553);
                gl.glEnableClientState(32884);
                gl.glVertexPointer(3, 5126, 0, this.mFVertexBuffer);
                gl.glDrawElements(2, 4, 5123, this.mIndexBuffer);
                this.mIndexBuffer.put((short) 2);
                this.mIndexBuffer.put((short) 0);
                this.mIndexBuffer.put((short) 1);
                this.mIndexBuffer.put((short) 3);
                this.mIndexBuffer.position(0);
                gl.glDrawElements(2, 4, 5123, this.mIndexBuffer);
                gl.glDisableClientState(32884);
                gl.glEnable(3553);
                gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            }

            public void drawSelected(GL10 gl, int v) {
                gl.glColor4f(0.0f, 1.0f, 0.0f, 1.0f);
                ByteBuffer vbb = ByteBuffer.allocateDirect(48);
                vbb.order(ByteOrder.nativeOrder());
                this.mFVertexBuffer = vbb.asFloatBuffer();
                ByteBuffer ibb = ByteBuffer.allocateDirect(8);
                ibb.order(ByteOrder.nativeOrder());
                this.mIndexBuffer = ibb.asShortBuffer();
                float ox = ePSXePadEditorGL.this.padOffScreenLan[ePSXePadEditorGL.this.mode][(v * 2) + 0] - ((ePSXePadEditorGL.this.padSizeScreenLan[ePSXePadEditorGL.this.mode][(v * 2) + 0] * ePSXePadEditorGL.this.padScreenResize[ePSXePadEditorGL.this.mode][v]) / 2.0f);
                float oy = ePSXePadEditorGL.this.padOffScreenLan[ePSXePadEditorGL.this.mode][(v * 2) + 1] - ((ePSXePadEditorGL.this.padSizeScreenLan[ePSXePadEditorGL.this.mode][(v * 2) + 1] * ePSXePadEditorGL.this.padScreenResize[ePSXePadEditorGL.this.mode][v]) / 2.0f);
                float sw = ePSXePadEditorGL.this.padSizeScreenLan[ePSXePadEditorGL.this.mode][(v * 2) + 0] * ePSXePadEditorGL.this.padScreenResize[ePSXePadEditorGL.this.mode][v];
                float sh = ePSXePadEditorGL.this.padSizeScreenLan[ePSXePadEditorGL.this.mode][(v * 2) + 1] * ePSXePadEditorGL.this.padScreenResize[ePSXePadEditorGL.this.mode][v];
                float[] coords = {1.0f + ox, 1.0f + oy, 0.0f, (ox + sw) - 1.0f, (int) (1.0f + oy), 0.0f, (ox + sw) - 1.0f, (oy + sh) - 1.0f, 0.0f, 1.0f + ox, (int) ((oy + sh) - 1.0f), 0.0f};
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 3; j++) {
                        this.mFVertexBuffer.put(coords[(i * 3) + j]);
                    }
                }
                for (int i2 = 0; i2 < 4; i2++) {
                    this.mIndexBuffer.put((short) i2);
                }
                this.mFVertexBuffer.position(0);
                this.mIndexBuffer.position(0);
                gl.glDisable(3553);
                gl.glEnableClientState(32884);
                gl.glVertexPointer(3, 5126, 0, this.mFVertexBuffer);
                gl.glDrawElements(2, 4, 5123, this.mIndexBuffer);
                gl.glDisableClientState(32884);
                gl.glEnable(3553);
                gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            }

            public void drawTextMode(GL10 gl, int mode) {
                String prof = ePSXePadEditor.this.padprofile;
                if (ePSXePadEditor.this.padprofile.length() < 1) {
                    prof = "Default";
                }
                gl.glEnable(3553);
                gl.glEnable(3042);
                gl.glBlendFunc(770, 771);
                this.glText.begin(1.0f, 1.0f, 1.0f, 1.0f);
                if (mode == 0) {
                    this.glText.draw("Profile: " + prof + " Mode: Digital", (ePSXePadEditorGL.this.mWidth - ((ePSXePadEditorGL.this.mHeight * 4f) / 3)) / 2, ePSXePadEditorGL.this.mHeight - 142);
                } else {
                    this.glText.draw("Profile: " + prof + "Mode: Analogic", (ePSXePadEditorGL.this.mWidth - ((ePSXePadEditorGL.this.mHeight * 4f) / 3)) / 2, ePSXePadEditorGL.this.mHeight - 142);
                }
                this.glText.end();
                gl.glDisable(3042);
                gl.glDisable(3553);
            }

            public void drawButtonInfo(GL10 gl, int mode) {
                String statString;
                float posx = Math.round(ePSXePadEditorGL.this.padOffScreenLan[mode][ePSXePadEditorGL.this.padScreenSelected * 2]);
                float posy = Math.round(ePSXePadEditorGL.this.padOffScreenLan[mode][(ePSXePadEditorGL.this.padScreenSelected * 2) + 1]);
                float res = ePSXePadEditorGL.this.padScreenResize[mode][ePSXePadEditorGL.this.padScreenSelected];
                int stat = ePSXePadEditorGL.this.padScreenStatus[mode][ePSXePadEditorGL.this.padScreenSelected];
                if (stat == 0) {
                    statString = "off";
                } else {
                    statString = stat == 1 ? "on" : "hid";
                }
                gl.glEnable(3553);
                gl.glEnable(3042);
                gl.glBlendFunc(770, 771);
                this.glText.begin(1.0f, 1.0f, 1.0f, 1.0f);
                this.glText.draw("X=" + posx + " Y=" + posy + " Stat=" + statString + " Resize=" + res, (ePSXePadEditorGL.this.mWidth - ((ePSXePadEditorGL.this.mHeight * 4f) / 3)) / 2, ePSXePadEditorGL.this.mHeight - 172);
                this.glText.end();
                gl.glDisable(3042);
                gl.glDisable(3553);
            }

            public void drawBackground(GL10 gl) {
                float w = (ePSXePadEditorGL.this.mWidth - ((ePSXePadEditorGL.this.mHeight * 4f) / 3)) / 2;
                float sw = (ePSXePadEditorGL.this.mHeight * 4f) / 3;
                int size = ((ePSXePadEditorGL.this.mHeight * 2) / 20) + 4 + (((int) (2.0f * sw)) / 20);
                if (size < (((ePSXePadEditorGL.this.mHeight * 2) / 2) / 20) + 4 + ((ePSXePadEditorGL.this.mWidth * 2) / 20)) {
                    size = (((ePSXePadEditorGL.this.mHeight * 2) / 2) / 20) + 4 + ((ePSXePadEditorGL.this.mWidth * 2) / 20);
                }
                if (size < 256) {
                    size = 256;
                }
                gl.glColor4f(0.2f, 0.2f, 0.2f, 1.0f);
                ByteBuffer vbb = ByteBuffer.allocateDirect(size * 3 * 4);
                vbb.order(ByteOrder.nativeOrder());
                FloatBuffer mFVertexBuffer = vbb.asFloatBuffer();
                ByteBuffer ibb = ByteBuffer.allocateDirect(size * 2);
                ibb.order(ByteOrder.nativeOrder());
                ShortBuffer mIndexBuffer = ibb.asShortBuffer();
                if (ePSXePadEditor.this.emu_screen_orientation == 0) {
                    float[] coords = {sw + w, 1.0f, 0.0f, sw + w, (int) (ePSXePadEditorGL.this.mHeight - 1.0f), 0.0f, w, 1.0f, 0.0f, w, (int) (ePSXePadEditorGL.this.mHeight - 1.0f), 0.0f};
                    for (int i = 0; i < 4; i++) {
                        for (int j = 0; j < 3; j++) {
                            mFVertexBuffer.put(coords[(i * 3) + j]);
                        }
                    }
                } else {
                    float[] coords2 = {ePSXePadEditorGL.this.mWidth - 1, ((float)ePSXePadEditorGL.this.mHeight / 2) + 1, 0.0f, ePSXePadEditorGL.this.mWidth - 1, (int) (ePSXePadEditorGL.this.mHeight - 1.0f), 0.0f, 1.0f, ((float)ePSXePadEditorGL.this.mHeight / 2) + 1, 0.0f, 1.0f, (int) (ePSXePadEditorGL.this.mHeight - 1.0f), 0.0f};
                    for (int i2 = 0; i2 < 4; i2++) {
                        for (int j2 = 0; j2 < 3; j2++) {
                            mFVertexBuffer.put(coords2[(i2 * 3) + j2]);
                        }
                    }
                }
                for (int i3 = 0; i3 < 4; i3++) {
                    mIndexBuffer.put((short) i3);
                }
                mFVertexBuffer.position(0);
                mIndexBuffer.position(0);
                gl.glDisable(3553);
                gl.glEnableClientState(32884);
                gl.glVertexPointer(3, 5126, 0, mFVertexBuffer);
                gl.glDrawElements(2, 4, 5123, mIndexBuffer);
                mIndexBuffer.put((short) 2);
                mIndexBuffer.put((short) 0);
                mIndexBuffer.put((short) 1);
                mIndexBuffer.put((short) 3);
                mIndexBuffer.position(0);
                gl.glDrawElements(2, 4, 5123, mIndexBuffer);
                mFVertexBuffer.position(0);
                mIndexBuffer.position(0);
                if (ePSXePadEditor.this.emu_screen_orientation == 0) {
                    int y = 1;
                    while (y < ePSXePadEditorGL.this.mHeight - 1) {
                        float[] mcoords = {sw + w, y, 0.0f, w, y, 0.0f};
                        for (int j3 = 0; j3 < 6; j3++) {
                            mFVertexBuffer.put(mcoords[j3]);
                        }
                        y += ePSXePadEditorGL.this.mHeight / 20;
                    }
                    for (int x = (int) w; x < ((int) (sw + w)); x += (int) (sw / 20.0f)) {
                        float[] mcoords2 = {x, 1.0f, 0.0f, x, ePSXePadEditorGL.this.mHeight - 1, 0.0f};
                        for (int j4 = 0; j4 < 6; j4++) {
                            mFVertexBuffer.put(mcoords2[j4]);
                        }
                    }
                    for (int i4 = 0; i4 < ((ePSXePadEditorGL.this.mHeight * 2) / 20) + (((int) (2.0f * sw)) / 20); i4++) {
                        mIndexBuffer.put((short) i4);
                    }
                    mFVertexBuffer.position(0);
                    mIndexBuffer.position(0);
                    gl.glVertexPointer(3, 5126, 0, mFVertexBuffer);
                    gl.glDrawElements(1, ((ePSXePadEditorGL.this.mHeight * 2) / 20) + (((int) (2.0f * sw)) / 20), 5123, mIndexBuffer);
                } else {
                    int y2 = 1;
                    while (y2 < (ePSXePadEditorGL.this.mHeight / 2) - 1) {
                        float[] mcoords3 = {1.0f, y2, 0.0f, ePSXePadEditorGL.this.mWidth - 1, y2, 0.0f};
                        for (int j5 = 0; j5 < 6; j5++) {
                            mFVertexBuffer.put(mcoords3[j5]);
                        }
                        y2 += (ePSXePadEditorGL.this.mHeight / 2) / 20;
                    }
                    int x2 = 1;
                    while (x2 < ePSXePadEditorGL.this.mWidth - 1) {
                        float[] mcoords4 = {x2, 1.0f, 0.0f, x2, ((float)ePSXePadEditorGL.this.mHeight / 2) - 1, 0.0f};
                        for (int j6 = 0; j6 < 6; j6++) {
                            mFVertexBuffer.put(mcoords4[j6]);
                        }
                        x2 += ePSXePadEditorGL.this.mWidth / 20;
                    }
                    for (int i5 = 0; i5 < (((ePSXePadEditorGL.this.mHeight * 2) / 2) / 20) + ((ePSXePadEditorGL.this.mWidth * 2) / 20); i5++) {
                        mIndexBuffer.put((short) i5);
                    }
                    mFVertexBuffer.position(0);
                    mIndexBuffer.position(0);
                    gl.glVertexPointer(3, 5126, 0, mFVertexBuffer);
                    gl.glDrawElements(1, (((ePSXePadEditorGL.this.mHeight * 2) / 2) / 20) + ((ePSXePadEditorGL.this.mWidth * 2) / 20), 5123, mIndexBuffer);
                }
                gl.glDisableClientState(32884);
                gl.glEnable(3553);
                gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            }

            @Override // android.opengl.GLSurfaceView.Renderer
            public void onDrawFrame(GL10 gl) {
                try {
                    gl.glClear(16384);
                    drawBackground(gl);
                    drawTextMode(gl, ePSXePadEditorGL.this.mode);
                    if (ePSXePadEditorGL.this.padScreenSelected != -1) {
                        drawButtonInfo(gl, ePSXePadEditorGL.this.mode);
                    }
                    gl.glEnable(3553);
                    gl.glEnable(3042);
                    gl.glBlendFunc(770, 771);
                    gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                    for (int i = 0; i < ePSXePadEditorGL.this.nButtons; i++) {
                        if ((ePSXePadEditorGL.this.padScreenStatusInit[ePSXePadEditorGL.this.mode][i] & 4) != 4 && (i < 14 || ePSXePadEditorGL.this.padScreenStatus[ePSXePadEditorGL.this.mode][i] == 1)) {
                            if (i < 1) {
                                this.batchLan[i].beginBatch(this.mTexTools);
                            } else if (i < 14) {
                                this.batchLan[i].beginBatch(this.mTexLan);
                            } else {
                                this.batchLan[i].beginBatch(this.mTexExtra);
                            }
                            this.batchLan[i].drawSprite(ePSXePadEditorGL.this.padOffScreenLan[ePSXePadEditorGL.this.mode][(i * 2) + 0], ePSXePadEditorGL.this.padOffScreenLan[ePSXePadEditorGL.this.mode][(i * 2) + 1], ePSXePadEditorGL.this.padSizeScreenLan[ePSXePadEditorGL.this.mode][(i * 2) + 0] * ePSXePadEditorGL.this.padScreenResize[ePSXePadEditorGL.this.mode][i], ePSXePadEditorGL.this.padSizeScreenLan[ePSXePadEditorGL.this.mode][(i * 2) + 1] * ePSXePadEditorGL.this.padScreenResize[ePSXePadEditorGL.this.mode][i], this.textureRgnLan[i]);
                            this.batchLan[i].endBatch();
                            if (i > 0) {
                                if (ePSXePadEditorGL.this.padScreenStatus[ePSXePadEditorGL.this.mode][i] == 0) {
                                    drawDisabled(gl, i, false);
                                } else if (ePSXePadEditorGL.this.padScreenStatus[ePSXePadEditorGL.this.mode][i] == 2) {
                                    drawDisabled(gl, i, true);
                                }
                                if (ePSXePadEditorGL.this.padScreenSelected == i) {
                                    drawSelected(gl, i);
                                }
                            }
                        }
                    }
                    gl.glDisable(3042);
                    gl.glDisable(3553);
                } catch (Exception e) {
                }
            }

            @Override // android.opengl.GLSurfaceView.Renderer
            public void onSurfaceChanged(GL10 gl, int width, int height) {
                Log.e("ePSXeView", "onSurfaceChanged");
                ePSXePadEditorGL.this.mWidth = width;
                ePSXePadEditorGL.this.mHeight = height;
                gl.glViewport(0, 0, width, height);
                gl.glMatrixMode(5889);
                gl.glLoadIdentity();
                gl.glOrthof(0.0f, width, 0.0f, height, 1.0f, -1.0f);
                if (ePSXePadEditorGL.this.mePSXeReadPreferences == null) {
                    ePSXePadEditorGL.this.mePSXeReadPreferences = new ePSXeReadPreferences(ePSXePadEditorGL.this.mContext);
                }
                if (ePSXePadEditorGL.this.mePSXeReadPreferences.getPadStatus(ePSXePadEditor.this.padprofile + "Pad1Status0") != -1) {
                    Log.e("epsxepadeditor", "loading pad info from preferences");
                    int tmode = ePSXePadEditorGL.this.mode;
                    ePSXePadEditorGL.this.mode = 0;
                    ePSXePadEditorGL.this.resetPadValues();
                    ePSXePadEditorGL.this.mode = 1;
                    ePSXePadEditorGL.this.resetPadValues();
                    ePSXePadEditorGL.this.mode = tmode;
                    for (int i = 1; i < 14; i++) {
                        int val = ePSXePadEditorGL.this.mePSXeReadPreferences.getPadStatus(ePSXePadEditor.this.padprofile + "Pad1Status" + i);
                        if (val != -1) {
                            ePSXePadEditorGL.this.padScreenStatus[0][i] = val;
                        }
                    }
                    for (int i2 = 1; i2 < 14; i2++) {
                        int val2 = ePSXePadEditorGL.this.mePSXeReadPreferences.getPadStatus(ePSXePadEditor.this.padprofile + "Pad1StatusAnalog" + i2);
                        if (val2 != -1) {
                            ePSXePadEditorGL.this.padScreenStatus[1][i2] = val2;
                        }
                    }
                    for (int i3 = 1; i3 < ePSXePadEditorGL.this.nButtons; i3++) {
                        float x = ePSXePadEditorGL.this.mePSXeReadPreferences.getPadSize(ePSXePadEditor.this.padprofile + "Pad1SizeX" + i3);
                        float y = ePSXePadEditorGL.this.mePSXeReadPreferences.getPadSize(ePSXePadEditor.this.padprofile + "Pad1SizeY" + i3);
                        if (x != -1.0f) {
                            ePSXePadEditorGL.this.padSizeScreenLan[0][i3 * 2] = x;
                            ePSXePadEditorGL.this.padSizeScreenLan[0][(i3 * 2) + 1] = y;
                        }
                    }
                    for (int i4 = 1; i4 < ePSXePadEditorGL.this.nButtons; i4++) {
                        float x2 = ePSXePadEditorGL.this.mePSXeReadPreferences.getPadSize(ePSXePadEditor.this.padprofile + "Pad1SizeXAnalog" + i4);
                        float y2 = ePSXePadEditorGL.this.mePSXeReadPreferences.getPadSize(ePSXePadEditor.this.padprofile + "Pad1SizeYAnalog" + i4);
                        if (x2 != -1.0f) {
                            ePSXePadEditorGL.this.padSizeScreenLan[1][i4 * 2] = x2;
                            ePSXePadEditorGL.this.padSizeScreenLan[1][(i4 * 2) + 1] = y2;
                        }
                    }
                    for (int i5 = 1; i5 < ePSXePadEditorGL.this.nButtons; i5++) {
                        float x3 = ePSXePadEditorGL.this.mePSXeReadPreferences.getPadSize(ePSXePadEditor.this.padprofile + "Pad1PosX" + i5);
                        float y3 = ePSXePadEditorGL.this.mePSXeReadPreferences.getPadSize(ePSXePadEditor.this.padprofile + "Pad1PosY" + i5);
                        if (x3 != -1.0f) {
                            ePSXePadEditorGL.this.padOffScreenLan[0][i5 * 2] = x3;
                            ePSXePadEditorGL.this.padOffScreenLan[0][(i5 * 2) + 1] = y3;
                        }
                    }
                    for (int i6 = 1; i6 < ePSXePadEditorGL.this.nButtons; i6++) {
                        float x4 = ePSXePadEditorGL.this.mePSXeReadPreferences.getPadSize(ePSXePadEditor.this.padprofile + "Pad1PosXAnalog" + i6);
                        float y4 = ePSXePadEditorGL.this.mePSXeReadPreferences.getPadSize(ePSXePadEditor.this.padprofile + "Pad1PosYAnalog" + i6);
                        if (x4 != -1.0f) {
                            ePSXePadEditorGL.this.padOffScreenLan[1][i6 * 2] = x4;
                            ePSXePadEditorGL.this.padOffScreenLan[1][(i6 * 2) + 1] = y4;
                        }
                        Log.e("values", "i " + i6 + " x " + x4);
                        Log.e("values", "i " + i6 + " y " + y4);
                    }
                    for (int i7 = 1; i7 < ePSXePadEditorGL.this.nButtons; i7++) {
                        float val3 = ePSXePadEditorGL.this.mePSXeReadPreferences.getPadResize(ePSXePadEditor.this.padprofile + "Pad1Resize" + i7);
                        if (val3 != -1.0f) {
                            ePSXePadEditorGL.this.padScreenResize[0][i7] = val3;
                        }
                    }
                    for (int i8 = 1; i8 < ePSXePadEditorGL.this.nButtons; i8++) {
                        float val4 = ePSXePadEditorGL.this.mePSXeReadPreferences.getPadResize(ePSXePadEditor.this.padprofile + "Pad1ResizeAnalog" + i8);
                        Log.e("values", "i " + i8 + " val " + val4);
                        if (val4 != -1.0f) {
                            ePSXePadEditorGL.this.padScreenResize[1][i8] = val4;
                        }
                    }
                } else {
                    Log.e("epsxepadeditor", "setting default pad info");
                    int tmode2 = ePSXePadEditorGL.this.mode;
                    ePSXePadEditorGL.this.mode = 0;
                    ePSXePadEditorGL.this.resetPadValues();
                    ePSXePadEditorGL.this.mode = 1;
                    ePSXePadEditorGL.this.resetPadValues();
                    ePSXePadEditorGL.this.mode = tmode2;
                }
                ePSXePadEditorGL.this.initvirtualPad = 0;
            }

            @Override // android.opengl.GLSurfaceView.Renderer
            public void onSurfaceCreated(GL10 gl, EGLConfig config) {
                this.glText = new GLText(gl, ePSXePadEditorGL.this.mContext.getAssets());
                this.glText.load("Roboto-Regular.ttf", 28, 2, 2);
                int[] padCoords = {0, 0, 341, 50, 2, 1, 224, 225, 1, 238, 223, 486, 253, 8, 309, 52, 372, 4, 433, 56, 249, 80, 307, InputList.KEYCODE_F2, StatusLine.HTTP_PERM_REDIRECT, 80, 364, InputList.KEYCODE_F2, StatusLine.HTTP_PERM_REDIRECT, InputList.KEYCODE_NUMPAD_0, 364, InputList.KEYCODE_BUTTON_9, 250, InputList.KEYCODE_NUMPAD_0, 307, InputList.KEYCODE_BUTTON_9, 254, 208, 297, 239, 365, 80, 421, InputList.KEYCODE_F2, 365, InputList.KEYCODE_NUMPAD_0, HttpStatus.SC_UNPROCESSABLE_ENTITY, InputList.KEYCODE_BUTTON_9, 289, 289, 511, 511, 289, 289, 511, 511};
                int[] padCoordsExtra = {0, 0, 64, 64, 64, 0, 128, 64, 128, 0, InputList.KEYCODE_BUTTON_5, 64, InputList.KEYCODE_BUTTON_5, 0, 256, 64, 0, 64, 64, 128, 64, 64, 128, 128, 128, 64, InputList.KEYCODE_BUTTON_5, 128, InputList.KEYCODE_BUTTON_5, 64, 256, 128, 0, 128, 64, InputList.KEYCODE_BUTTON_9, 64, 128, 128, InputList.KEYCODE_BUTTON_9, 128, 128, InputList.KEYCODE_BUTTON_5, InputList.KEYCODE_BUTTON_9, InputList.KEYCODE_BUTTON_5, 128, 256, InputList.KEYCODE_BUTTON_9, 0, InputList.KEYCODE_BUTTON_5, 64, 256, 64, InputList.KEYCODE_BUTTON_5, 128, 256, 128, InputList.KEYCODE_BUTTON_5, InputList.KEYCODE_BUTTON_5, 256, InputList.KEYCODE_BUTTON_5, InputList.KEYCODE_BUTTON_5, 256, 256, 0, 256, 64, 320, 64, 256, 128, 320, 128, 256, InputList.KEYCODE_BUTTON_9, 320, InputList.KEYCODE_BUTTON_5, 256, 256, 320, 0, 320, 64, 384, 64, 320, 128, 384, 128, 320, InputList.KEYCODE_BUTTON_9, 384, InputList.KEYCODE_BUTTON_5, 320, 256, 384, 0, 384, 64, 448, 63, 384, 127, 448, 128, 384, InputList.KEYCODE_BUTTON_9, 448, InputList.KEYCODE_BUTTON_5, 384, 256, 448, 0, 448, 64, 511, 64, 448, 128, 511, 128, 448, InputList.KEYCODE_BUTTON_9, 511, InputList.KEYCODE_BUTTON_5, 448, 256, 511};
                this.mTexTools = loadTexture(gl, ePSXePadEditorGL.this.mContext, R.drawable.tools);
                for (int i = 0; i < 1; i++) {
                    this.wTexLan[i] = padCoords[(i * 4) + 2] - padCoords[(i * 4) + 0];
                    this.hTexLan[i] = padCoords[(i * 4) + 3] - padCoords[(i * 4) + 1];
                    this.textureRgnLan[i] = new TextureRegion(this.wTextmp, this.hTextmp, padCoords[(i * 4) + 0], padCoords[(i * 4) + 1], this.wTexLan[i], this.hTexLan[i]);
                    this.batchLan[i] = new SpriteBatch(gl, 1);
                }
                this.mTexLan = loadTexture(gl, ePSXePadEditorGL.this.mContext, R.drawable.pure_white_digital);
                for (int i2 = 1; i2 < ePSXePadEditorGL.this.nButtons - 6; i2++) {
                    this.wTexLan[i2] = padCoords[(i2 * 4) + 2] - padCoords[(i2 * 4) + 0];
                    this.hTexLan[i2] = padCoords[(i2 * 4) + 3] - padCoords[(i2 * 4) + 1];
                    this.textureRgnLan[i2] = new TextureRegion(this.wTextmp, this.hTextmp, padCoords[(i2 * 4) + 0], padCoords[(i2 * 4) + 1], this.wTexLan[i2], this.hTexLan[i2]);
                    this.batchLan[i2] = new SpriteBatch(gl, 1);
                }
                ePSXePadEditorGL.this.loadExtraButtons();
                this.mTexExtra = loadTexture(gl, ePSXePadEditorGL.this.mContext, R.drawable.extra_buttons);
                for (int i3 = 0; i3 < 6; i3++) {
                    int j = ePSXePadEditorGL.this.padScreenExtra[i3];
                    if (j >= 0) {
                        if (j >= 24) {
                            j += 4;
                        }
                        this.wTexLan[i3 + 14] = padCoordsExtra[(j * 4) + 2] - padCoordsExtra[(j * 4) + 0];
                        this.hTexLan[i3 + 14] = padCoordsExtra[(j * 4) + 3] - padCoordsExtra[(j * 4) + 1];
                        this.textureRgnLan[i3 + 14] = new TextureRegion(this.wTextmp, this.hTextmp, padCoordsExtra[(j * 4) + 0], padCoordsExtra[(j * 4) + 1], this.wTexLan[i3 + 14], this.hTexLan[i3 + 14]);
                        this.batchLan[i3 + 14] = new SpriteBatch(gl, 1);
                    }
                }
            }
        }
    }
}
