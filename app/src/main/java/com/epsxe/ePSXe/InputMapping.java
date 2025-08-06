package com.epsxe.ePSXe;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import com.fasterxml.jackson.core.util.MinimalPrettyPrinter;

/* loaded from: classes.dex */
public class InputMapping extends Activity {
    protected String buttonName;
    private Context mContext;
    protected InputDevice mDevice;
    protected int mId;
    protected int mKeyval;
    private int osVersion;
    protected int player = 1;
    protected int mode = 1;
    protected int done = 0;
    Handler mHandler = new Handler();
    private Runnable mLaunchTask = new Runnable() { // from class: com.epsxe.ePSXe.InputMapping.5
        @Override // java.lang.Runnable
        public void run() {
            if (InputMapping.this.done == 0) {
                InputMapping.this.saveHat(InputMapping.this.mDevice, InputMapping.this.mKeyval, InputMapping.this.mId);
            }
        }
    };

    @Override // android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.osVersion = Integer.parseInt(Build.VERSION.SDK);
        this.buttonName = getIntent().getStringExtra("com.epsxe.ePSXe.button");
        if (this.buttonName.substring(0, 1).equals("P")) {
            this.player = Integer.parseInt(this.buttonName.substring(1, 2));
            this.mode = 0;
        }
        this.mContext = this;
        Log.e("epsxe", "--->buttonName " + this.buttonName + " player " + this.player);
        setTitle(getString(R.string.inputmap_map) + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + this.buttonName);
        final Button cancelButton = new Button(this) { // from class: com.epsxe.ePSXe.InputMapping.1
            {
                setText(R.string.inputmap_cancel);
                setOnClickListener(new View.OnClickListener() { // from class: com.epsxe.ePSXe.InputMapping.1.1
                    @Override // android.view.View.OnClickListener
                    public void onClick(View v) {
                        InputMapping.this.setResult(0, new Intent());
                        InputMapping.this.finish();
                    }
                });
            }
        };
        final Button unmapButton = new Button(this) { // from class: com.epsxe.ePSXe.InputMapping.2
            {
                setText(R.string.inputmap_unmap);
                setOnClickListener(new View.OnClickListener() { // from class: com.epsxe.ePSXe.InputMapping.2.1
                    @Override // android.view.View.OnClickListener
                    public void onClick(View v) {
                        SharedPreferences sharedPreferences = ePSXeApplication.getDefaultSharedPreferences(v.getContext());
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt(InputMapping.this.buttonName, -1);
                        editor.commit();
                        InputMapping.this.setResult(-1, new Intent());
                        InputMapping.this.finish();
                    }
                });
            }
        };
        final View primaryView = new View(this) { // from class: com.epsxe.ePSXe.InputMapping.3
            {
                setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
                setFocusable(true);
                setFocusableInTouchMode(true);
                requestFocus();
            }

            @Override // android.view.View, android.view.KeyEvent.Callback
            public boolean onKeyDown(int keyCode, KeyEvent event) {
                InputDevice device;
                Log.e("epsxe", "keycode " + keyCode);
                int keyval = keyCode | (event.isAltPressed() ? 65536 : 0);
                int sources = 0;
                if (InputMapping.this.osVersion >= 12 && (device = event.getDevice()) != null) {
                    sources = device.getSources();
                }
                if (keyval == 0 && event.getScanCode() != 0) {
                    keyval = event.getScanCode();
                }
                if ((keyval == 4 || keyval == 82) && (sources & 16778513) != 16778513) {
                    return false;
                }
                SharedPreferences sharedPreferences = ePSXeApplication.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt(InputMapping.this.buttonName, keyval);
                if (InputMapping.this.buttonName.contains("L2")) {
                    editor.putString("analog" + InputMapping.this.player + "l2PrefbuttonName", "48");
                } else if (InputMapping.this.buttonName.contains("R2")) {
                    editor.putString("analog" + InputMapping.this.player + "r2PrefbuttonName", "48");
                }
                editor.commit();
                InputMapping.this.setResult(-1, new Intent());
                InputMapping.this.done = 1;
                InputMapping.this.finish();
                return true;
            }
        };
        LinearLayout parentContainer = new LinearLayout(this) { // from class: com.epsxe.ePSXe.InputMapping.4
            {
                setOrientation(1);
                addView(cancelButton);
                addView(unmapButton);
                addView(primaryView);
            }
        };
        setContentView(parentContainer, new ViewGroup.LayoutParams(-2, -2));
    }

    private String getDescriptor(String s, String name, int id) {
        int in = s.indexOf("Descriptor:");
        if (in != -1) {
            String descriptor = s.substring(in + 12, s.indexOf(10, in + 12));
            Log.i("Gamepad", "[" + descriptor + "]");
            return descriptor;
        }
        return "###" + name + "###" + id + "###";
    }

    private void saveTrigger(InputDevice device, int keyval, int id) {
        String name = device.getName();
        String descriptor = getDescriptor(device.toString(), name, id);
        SharedPreferences sharedPreferences = ePSXeApplication.getDefaultSharedPreferences(this.mContext);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(this.buttonName, keyval + 10000);
        if (this.buttonName.contains("L2")) {
            editor.putString("analog" + this.player + "l2Pref", "" + keyval);
        } else if (this.buttonName.contains("R2")) {
            editor.putString("analog" + this.player + "r2Pref", "" + keyval);
        }
        editor.putString("analog" + this.player + "padidPref", descriptor);
        editor.putString("analog" + this.player + "paddescPref", name);
        if (this.osVersion >= 19) {
            editor.putString("analog" + this.player + "padvpIdPref", device.getVendorId() + ":" + device.getProductId());
        }
        editor.commit();
        setResult(-1, new Intent());
        finish();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void saveHat(InputDevice device, int keyval, int id) {
        String name = device.getName();
        String descriptor = getDescriptor(device.toString(), name, id);
        SharedPreferences sharedPreferences = ePSXeApplication.getDefaultSharedPreferences(this.mContext);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(this.buttonName, keyval + 10000);
        if (keyval < 15) {
            editor.putString("analog" + this.player + "hatPref", "1");
        }
        editor.putString("analog" + this.player + "padidPref", descriptor);
        editor.putString("analog" + this.player + "paddescPref", name);
        if (this.osVersion >= 19) {
            editor.putString("analog" + this.player + "padvpIdPref", device.getVendorId() + ":" + device.getProductId());
        }
        editor.commit();
        setResult(-1, new Intent());
        finish();
    }

    @Override // android.app.Activity
    public boolean onGenericMotionEvent(MotionEvent event) {
        InputDevice device = event.getDevice();
        if (device == null) {
            return false;
        }
        int eventDevice = device.getId();
        if (this.buttonName == null || this.mode == 1 || this.done == 1) {
            return false;
        }
        if (this.buttonName.contains("L2") || this.buttonName.contains("R2")) {
            int[] axis = {11, 14, 17, 18, 22, 23};
            for (int j : axis) {
                if (((int) (event.getAxisValue(j) * 128.0f)) > 40) {
                    saveTrigger(device, j, eventDevice);
                    return true;
                }
            }
        }
        if (!this.buttonName.contains("Up") && !this.buttonName.contains("Right") && !this.buttonName.contains("Down") && !this.buttonName.contains("Left")) {
            return false;
        }
        int[] axis2 = {0, 1, 15, 16};
        for (int j2 : axis2) {
            int val = (int) (event.getAxisValue(j2) * 128.0f);
            if (val > 40 || val < -40) {
                this.mDevice = device;
                this.mKeyval = j2;
                this.mId = eventDevice;
                this.mHandler.postDelayed(this.mLaunchTask, 2000L);
                return false;
            }
        }
        return false;
    }
}
