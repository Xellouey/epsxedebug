package com.epsxe.ePSXe.input;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.Toast;
import com.epsxe.ePSXe.InputList;
import com.epsxe.ePSXe.ePSXe;
import com.epsxe.ePSXe.jni.libepsxe;
import java.util.Arrays;

/* loaded from: classes.dex */
public class bluezDriver {
    private static final String BLUEZ_IME_PACKAGE = "com.hexad.bluezime";
    private static final String BLUEZ_IME_SERVICE = "com.hexad.bluezime.BluezService";
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
    public static final int KEYCODE_BUTTON_A = 96;
    public static final int KEYCODE_BUTTON_B = 97;
    public static final int KEYCODE_BUTTON_C = 98;
    public static final int KEYCODE_BUTTON_X = 99;
    public static final int KEYCODE_BUTTON_Y = 100;
    public static final int KEYCODE_BUTTON_Z = 101;
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
    public static final String SESSION_ID = "com.hexad.bluezime.sessionid";
    public static final String SESSION_NAME = "EPSXE-IME";
    private static final int STATUS_NORUNNING = 0;
    private static final int STATUS_PAUSED = 3;
    private static final int STATUS_RUNNING_BIOS = 2;
    private static final int STATUS_RUNNING_GAME = 1;

    /* renamed from: a */
    ePSXe f183a;
    String[] bluezaddr;
    String[] bluezdriver;
    int[] bluezpad;
    boolean bluezstatus;

    /* renamed from: e */
    libepsxe f184e;
    int[][] bluezxd = {new int[]{0, 0}, new int[]{0, 0}, new int[]{0, 0}, new int[]{0, 0}};
    int[][] bluezyd = {new int[]{0, 0}, new int[]{0, 0}, new int[]{0, 0}, new int[]{0, 0}};
    private boolean m_connected = false;
    private BroadcastReceiver stateCallback = new BroadcastReceiver() { // from class: com.epsxe.ePSXe.input.bluezDriver.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() != null && "EPSXE-IME".equals(intent.getStringExtra("com.hexad.bluezime.sessionid"))) {
                if (intent.getAction().equals("com.hexad.bluezime.config")) {
                    Toast.makeText(bluezDriver.this.f183a, "Bluez-IME version " + intent.getIntExtra("version", 0), 0).show();
                    return;
                }
                if (intent.getAction().equals("com.hexad.bluezime.currentstate")) {
                    bluezDriver.this.m_connected = intent.getBooleanExtra("connected", false);
                    for (int i = 0; i < 4; i++) {
                        if (bluezDriver.this.bluezpad[i] != 0) {
                            Intent serviceIntent2 = new Intent("com.hexad.bluezime.connect");
                            serviceIntent2.setClassName(bluezDriver.BLUEZ_IME_PACKAGE, bluezDriver.BLUEZ_IME_SERVICE);
                            serviceIntent2.putExtra("com.hexad.bluezime.sessionid", "EPSXE-IME");
                            serviceIntent2.putExtra("address", bluezDriver.this.bluezaddr[i]);
                            serviceIntent2.putExtra("driver", bluezDriver.this.bluezdriver[i]);
                            bluezDriver.this.f183a.startService(serviceIntent2);
                            return;
                        }
                    }
                    return;
                }
                if (intent.getAction().equals("com.hexad.bluezime.connected")) {
                    bluezDriver.this.m_connected = true;
                    return;
                }
                if (intent.getAction().equals("com.hexad.bluezime.disconnected")) {
                    bluezDriver.this.m_connected = false;
                } else if (intent.getAction().equals("com.hexad.bluezime.error")) {
                    Toast.makeText(bluezDriver.this.f183a, "Error: " + intent.getStringExtra("message"), 0).show();
                    bluezDriver.this.m_connected = false;
                }
            }
        }
    };
    private BroadcastReceiver statusMonitor = new BroadcastReceiver() { // from class: com.epsxe.ePSXe.input.bluezDriver.2
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
                            bluezDriver.this.bluezxd[0][0] = value;
                            break;
                        case 1:
                            bluezDriver.this.bluezyd[0][0] = value;
                            break;
                        case 2:
                            bluezDriver.this.bluezxd[0][1] = value;
                            break;
                        case 3:
                            bluezDriver.this.bluezyd[0][1] = value;
                            break;
                    }
                    if (bluezDriver.this.f184e != null) {
                        bluezDriver.this.f184e.setpadanalog(0, 0, bluezDriver.this.bluezxd[0][0], bluezDriver.this.bluezyd[0][0]);
                        bluezDriver.this.f184e.setpadanalog(0, 1, bluezDriver.this.bluezxd[0][1], bluezDriver.this.bluezyd[0][1]);
                        return;
                    }
                    return;
                }
                if (intent.getAction().equals("com.hexad.bluezime.keypress")) {
                    int key = intent.getIntExtra("key", 0);
                    int action = intent.getIntExtra("action", 100);
                    switch (key) {
                        case 8:
                        case 99:
                            if (action == 0) {
                                bluezDriver.this.f183a.setPushedButton(0, 128);
                                break;
                            } else {
                                bluezDriver.this.f183a.unsetPushedButton(0, 128);
                                break;
                            }
                        case 9:
                        case 97:
                            if (action == 0) {
                                bluezDriver.this.f183a.setPushedButton(0, 32);
                                break;
                            } else {
                                bluezDriver.this.f183a.unsetPushedButton(0, 32);
                                break;
                            }
                        case 19:
                            if (action == 0) {
                                bluezDriver.this.f183a.setPushedButton(0, 4096);
                                break;
                            } else {
                                bluezDriver.this.f183a.unsetPushedButton(0, 4096);
                                break;
                            }
                        case 20:
                            if (action == 0) {
                                bluezDriver.this.f183a.setPushedButton(0, 16384);
                                break;
                            } else {
                                bluezDriver.this.f183a.unsetPushedButton(0, 16384);
                                break;
                            }
                        case 21:
                            if (action == 0) {
                                bluezDriver.this.f183a.setPushedButton(0, 32768);
                                break;
                            } else {
                                bluezDriver.this.f183a.unsetPushedButton(0, 32768);
                                break;
                            }
                        case 22:
                            if (action == 0) {
                                bluezDriver.this.f183a.setPushedButton(0, 8192);
                                break;
                            } else {
                                bluezDriver.this.f183a.unsetPushedButton(0, 8192);
                                break;
                            }
                        case InputList.KEYCODE_A /* 29 */:
                        case 96:
                            if (action == 0) {
                                bluezDriver.this.f183a.setPushedButton(0, 64);
                                break;
                            } else {
                                bluezDriver.this.f183a.unsetPushedButton(0, 64);
                                break;
                            }
                        case 30:
                        case 100:
                            if (action == 0) {
                                bluezDriver.this.f183a.setPushedButton(0, 16);
                                break;
                            } else {
                                bluezDriver.this.f183a.unsetPushedButton(0, 16);
                                break;
                            }
                        case InputList.KEYCODE_MINUS /* 69 */:
                        case 109:
                            if (action == 0) {
                                bluezDriver.this.f183a.setPushedButton(0, 256);
                                break;
                            } else {
                                bluezDriver.this.f183a.unsetPushedButton(0, 256);
                                break;
                            }
                        case InputList.KEYCODE_PLUS /* 81 */:
                        case 108:
                            if (action == 0) {
                                bluezDriver.this.f183a.setPushedButton(0, 2048);
                                break;
                            } else {
                                bluezDriver.this.f183a.unsetPushedButton(0, 2048);
                                break;
                            }
                        case 102:
                            if (action == 0) {
                                bluezDriver.this.f183a.setPushedButton(0, 4);
                                break;
                            } else {
                                bluezDriver.this.f183a.unsetPushedButton(0, 4);
                                break;
                            }
                        case 103:
                            if (action == 0) {
                                bluezDriver.this.f183a.setPushedButton(0, 8);
                                break;
                            } else {
                                bluezDriver.this.f183a.unsetPushedButton(0, 8);
                                break;
                            }
                        case 104:
                            if (action == 0) {
                                bluezDriver.this.f183a.setPushedButton(0, 1);
                                break;
                            } else {
                                bluezDriver.this.f183a.unsetPushedButton(0, 1);
                                break;
                            }
                        case 105:
                            if (action == 0) {
                                bluezDriver.this.f183a.setPushedButton(0, 2);
                                break;
                            } else {
                                bluezDriver.this.f183a.unsetPushedButton(0, 2);
                                break;
                            }
                    }
                }
            }
        }
    };

    public bluezDriver(ePSXe epsxeact, libepsxe epsxelib, String[] ba, String[] bd, int[] bp) {
        this.bluezstatus = false;
        this.bluezpad = Arrays.copyOf(bp, bp.length);
        this.bluezdriver = (String[]) Arrays.copyOf(bd, bd.length);
        this.bluezaddr = (String[]) Arrays.copyOf(ba, ba.length);
        this.f184e = epsxelib;
        this.f183a = epsxeact;
        bluezStart();
        this.bluezstatus = true;
    }

    public void bluezStop() {
        if (this.bluezstatus) {
            this.f183a.unregisterReceiver(this.stateCallback);
            this.f183a.unregisterReceiver(this.statusMonitor);
            this.bluezstatus = false;
        }
    }

    private void bluezStart() {
        this.f183a.registerReceiver(this.stateCallback, new IntentFilter("com.hexad.bluezime.config"));
        this.f183a.registerReceiver(this.stateCallback, new IntentFilter("com.hexad.bluezime.currentstate"));
        this.f183a.registerReceiver(this.stateCallback, new IntentFilter("com.hexad.bluezime.connected"));
        this.f183a.registerReceiver(this.stateCallback, new IntentFilter("com.hexad.bluezime.disconnected"));
        this.f183a.registerReceiver(this.stateCallback, new IntentFilter("com.hexad.bluezime.error"));
        this.f183a.registerReceiver(this.statusMonitor, new IntentFilter("com.hexad.bluezime.directionalchange"));
        this.f183a.registerReceiver(this.statusMonitor, new IntentFilter("com.hexad.bluezime.keypress"));
        Intent serviceIntent = new Intent("com.hexad.bluezime.getconfig");
        serviceIntent.setClassName(BLUEZ_IME_PACKAGE, BLUEZ_IME_SERVICE);
        serviceIntent.putExtra("com.hexad.bluezime.sessionid", "EPSXE-IME");
        this.f183a.startService(serviceIntent);
        Intent serviceIntent2 = new Intent("com.hexad.bluezime.getstate");
        serviceIntent2.setClassName(BLUEZ_IME_PACKAGE, BLUEZ_IME_SERVICE);
        serviceIntent2.putExtra("com.hexad.bluezime.sessionid", "EPSXE-IME");
        this.f183a.startService(serviceIntent2);
    }
}
