package com.epsxe.ePSXe;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.core.view.InputDeviceCompat;
import android.util.Log;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.TextView;
import java.util.HashMap;

/* loaded from: classes.dex */
public class GamepadTest extends Activity {
    private static final int BACK_ID = 1;
    private static final int PREFERENCES_GROUP_ID = 0;
    private ePSXeReadPreferences mePSXeReadPreferences;
    String GamepadInfo = "";
    String ConfigInfo = "";
    HashMap<Integer, Float> axis = new HashMap<>();

    /* renamed from: p */
    int f131p = 1;

    private int getLabel(int i, int max) {
        if (i < 0 || i >= max) {
            return 48;
        }
        return i;
    }

    @Override // android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        this.f131p = getIntent().getIntExtra("com.epsxe.ePSXe.player", 1);
        Log.e("epsxe", "parameter player" + this.f131p);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gamepadtest);
        TextView tv = (TextView) getWindow().getDecorView().findViewById(R.id.configinfo);
        this.mePSXeReadPreferences = new ePSXeReadPreferences(this);
        String[] labels = getResources().getStringArray(R.array.listanalogaxis);
        this.ConfigInfo = "Player " + this.f131p + " Gamepad=" + this.mePSXeReadPreferences.getPadAnalogPadDesc(this.f131p) + "\n";
        this.ConfigInfo += "Left Stick X=" + labels[getLabel(this.mePSXeReadPreferences.getPadAnalogLeftx(this.f131p), labels.length)] + "\n";
        this.ConfigInfo += "Left Stick Y=" + labels[getLabel(this.mePSXeReadPreferences.getPadAnalogLefty(this.f131p), labels.length)] + "\n";
        this.ConfigInfo += "Right Stick X=" + labels[getLabel(this.mePSXeReadPreferences.getPadAnalogRightx(this.f131p), labels.length)] + "\n";
        this.ConfigInfo += "Right Stick Y=" + labels[getLabel(this.mePSXeReadPreferences.getPadAnalogRighty(this.f131p), labels.length)] + "\n";
        this.ConfigInfo += "L2 Trigger=" + labels[getLabel(this.mePSXeReadPreferences.getPadAnalogL2(this.f131p), labels.length)] + "\n";
        this.ConfigInfo += "R2 Trigger=" + labels[getLabel(this.mePSXeReadPreferences.getPadAnalogR2(this.f131p), labels.length)] + "\n";
        tv.setText(this.ConfigInfo);
        getWindow().getDecorView().invalidate();
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.e("epsxe", "parameter keyCode" + keyCode);
        if (keyCode == 4 && event.getRepeatCount() == 0) {
            Log.e("epsxe", "me voy a preferences");
            Intent myIntent = new Intent(this, (Class<?>) ePSXePreferences.class);
            myIntent.putExtra("com.epsxe.ePSXe.screen", "inputpreferences.controller" + this.f131p);
            startActivity(myIntent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, 1, 0, R.string.file_unsuportedback).setIcon(android.R.drawable.ic_menu_revert);
        return true;
    }

    @Override // android.app.Activity
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        menu.add(0, 1, 0, R.string.file_unsuportedback).setIcon(android.R.drawable.ic_menu_revert);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override // android.app.Activity
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                Intent myIntent = new Intent(this, (Class<?>) ePSXePreferences.class);
                myIntent.putExtra("com.epsxe.ePSXe.screen", "inputpreferences.controller" + this.f131p);
                startActivity(myIntent);
                finish();
                break;
        }
        return true;
    }

    private void updateView() {
        String[] labels = getResources().getStringArray(R.array.listanalogaxis);
        String text = this.GamepadInfo + "\n";
        for (Integer key : this.axis.keySet()) {
            if (key.intValue() < labels.length) {
                text = text + labels[key.intValue()] + "=" + this.axis.get(key) + "\n";
            } else {
                text = text + key + "=" + this.axis.get(key) + "\n";
            }
        }
        TextView tv = (TextView) getWindow().getDecorView().findViewById(R.id.gamepadinfo);
        tv.setText(text);
        getWindow().getDecorView().invalidate();
    }

    @Override // android.app.Activity
    @TargetApi(14)
    public boolean onGenericMotionEvent(MotionEvent event) {
        if ((event.getSource() & InputDeviceCompat.SOURCE_JOYSTICK) == 16777232) {
            boolean update = false;
            InputDevice device = event.getDevice();
            if (device != null) {
                this.GamepadInfo = device.getName();
                for (int i = 0; i < 48; i++) {
                    float v = event.getAxisValue(i);
                    if (this.axis.containsKey(Integer.valueOf(i))) {
                        update = true;
                        this.axis.put(Integer.valueOf(i), Float.valueOf(v));
                    } else if (v != 0.0f) {
                        this.axis.put(Integer.valueOf(i), Float.valueOf(v));
                        update = true;
                    }
                }
                if (update) {
                    updateView();
                }
            }
        }
        return false;
    }
}
