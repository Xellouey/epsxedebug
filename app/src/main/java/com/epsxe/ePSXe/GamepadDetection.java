package com.epsxe.ePSXe;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import androidx.core.internal.view.SupportMenu;
import androidx.core.view.InputDeviceCompat;
import android.util.Log;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.epsxe.ePSXe.util.DialogUtil;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public class GamepadDetection {
    private ePSXe act;
    private ePSXeView mePSXeView;
    int osVersion;
    String[] supportedGamepads = {"Broadcom Bluetooth HID", "OUYA Game Controller", "NVIDIA Controller", "NVIDIA Controller", "NVIDIA Controller", "NVidia Virtual Mouse", "Microsoft X-Box 360 pad", "Generic X-Box pad", "Generic X-Box pad", "Generic X-Box pad", "Sony PLAYSTATION(R)3 Controller", "NYKO PLAYPAD", "Bluetooth Gamepad", "Moga Pro HID", "Moga Pro 2 HID", "Moga Pro 2 HID", "Moga 2 HID", "WikiPad Controller", "Samsung Game Pad E", "ADC Joystick", "ipega media gamepad controller", "Dualshock3", "Sixaxis", "Gasia,Co", "ipega media gamepad controller", "Sony Computer Entertainment Wireless Controller", "Wireless Controller", "PG-9028", "jxdkey_driver", "AXUS Gamepad", "BETOP USB GAMEPAD", "Logitech Logitech Dual Action", "Zeemote", "Performance Designed Products Wireless Controller for PS3", "GS controller", "GS gamepad", "DragonRise", "PLAYSTATION(R)3", "Logitech Logitech Cordless RumblePad 2", "USB Gamepad", "Mad Catz C.T.R.L.R", "8Bitdo NES30 Pro", "小米蓝牙手柄", "Gamesir-G4s", "Gamesir-G4s", "Sony Computer Entertainment Wireless Controller", "FeiZhi Wee 2T"};
    Integer[] supportedGamepadsId = {-1, -2, -1, -2, 2389, 29200, 2389, 29187, 2389, 29204, -1, -2, 1118, 654, 1133, 49693, 1133, 49694, 1133, 49695, 1356, 616, 7545, 9, 2652, 34050, 8406, 3501, 8406, 25201, -1, -2, 8406, 35301, -1, -2, -1, -2, -1, -2, 1452, 556, -1, -2, -1, -2, -1, -2, 6473, 1026, 1356, 1476, 1356, 1476, 6473, 1026, -1, -2, 2821, 17664, 3727, 2, 1133, 49686, 4152, 5138, 3695, 532, -1, -2, -1, -2, Integer.valueOf(InputList.KEYCODE_BREAK), 6, -1, -2, 1133, 49689, Integer.valueOf(InputList.KEYCODE_BREAK), 17, 1848, 21094, 14368, 9, Integer.valueOf(InputList.KEYCODE_AXIS_TOOL_MINOR), 12612, 1452, 557, Integer.valueOf(SupportMenu.USER_MASK), 1135, 1356, 2508, 6421, 64};
    String[] supportedGamepadsExt = {"Other", "Nyko Playpad Pro", "Ipega Gamepad", "Moga HID"};
    String[] gamepadData = {"Broadcom Bluetooth HID", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "OUYA Game Controller", "1", "0", "1", "11", "14", "17", "18", "-1", "-1", "102", "103", "100", "97", "96", "99", "106", "-1", "-1", "107", "19", "22", "20", "21", "-1", "4", "2", "", "NVIDIA Game Controller v1.03", "0", "0", "1", "11", "14", "17", "18", "-1", "-1", "102", "103", "100", "97", "96", "99", "106", "-1", "-1", "108", "19", "22", "20", "21", "107", "4", "2", "", "NVIDIA Game Controller v1.01", "0", "0", "1", "11", "14", "17", "18", "-1", "-1", "102", "103", "100", "97", "96", "99", "106", "-1", "-1", "108", "19", "22", "20", "21", "107", "4", "2", "", "NVIDIA Game Controller v1.04", "0", "0", "1", "11", "14", "17", "18", "-1", "-1", "102", "103", "100", "97", "96", "99", "106", "-1", "-1", "108", "19", "22", "20", "21", "107", "4", "2", "", "NVidia Virtual Mouse", "0", "0", "1", "11", "14", "23", "22", "-1", "-1", "102", "103", "100", "97", "96", "99", "106", "-1", "-1", "108", "19", "22", "20", "21", "107", "4", "2", "", "Microsoft X-Box 360 Controller", "1", "0", "1", "11", "14", "17", "18", "-1", "-1", "102", "103", "100", "97", "96", "99", "4", "106", "107", "108", "19", "22", "20", "21", "-1", "4", "2", "", "Logitech Gamepad F310", "1", "0", "1", "12", "13", "11", "14", "-1", "-1", "102", "103", "100", "97", "96", "99", "109", "106", "107", "108", "19", "22", "20", "-21", "-1", "4", "2", "", "Logitech Gamepad F510", "1", "0", "1", "12", "13", "11", "14", "-1", "-1", "102", "103", "100", "97", "96", "99", "109", "106", "107", "108", "19", "22", "20", "-21", "-1", "4", "2", "", "Logitech Gamepad F710", "1", "0", "1", "12", "13", "11", "14", "-1", "-1", "102", "103", "100", "97", "96", "99", "109", "106", "107", "108", "19", "22", "20", "-21", "-1", "4", "2", "", "Sony PS3 Sixaxis Controller", "0", "0", "1", "11", "14", "17", "18", "104", "105", "102", "103", "100", "97", "96", "99", "109", "106", "107", "108", "19", "22", "20", "21", "-1", "4", "2", "", "NYKO Playpad Pro", "1", "0", "1", "11", "14", "23", "22", "-1", "-1", "102", "103", "100", "97", "96", "99", "4", "-1", "-1", "108", "19", "22", "20", "21", "-1", "4", "2", "", "IPega Gamepad 9017", "1", "0", "1", "11", "14", "48", "48", "-1", "-1", "192", "193", "191", "190", "189", "188", "196", "-1", "-1", "197", "19", "22", "20", "21", "-1", "4", "2", "", "Moga Pro HID", "1", "0", "1", "11", "14", "23", "22", "-1", "-1", "102", "103", "100", "97", "96", "99", "4", "-1", "-1", "108", "19", "22", "20", "21", "-1", "4", "2", "", "Moga Pro 2 HID", "1", "0", "1", "11", "14", "23", "22", "-1", "-1", "102", "103", "100", "97", "96", "99", "4", "-1", "-1", "108", "19", "22", "20", "21", "-1", "4", "2", "", "Moga Pro 2 HID", "1", "0", "1", "11", "14", "23", "22", "10023", "10022", "102", "103", "100", "97", "96", "99", "4", "-1", "-1", "108", "19", "22", "20", "21", "-1", "4", "2", "", "Moga 2 HID", "1", "0", "1", "11", "14", "23", "22", "-1", "-1", "102", "103", "100", "97", "96", "99", "4", "-1", "-1", "108", "19", "22", "20", "21", "-1", "4", "2", "", "WikiPad Controller", "1", "0", "1", "11", "14", "17", "18", "-1", "-1", "102", "103", "100", "97", "96", "99", "4", "-1", "-1", "108", "19", "22", "20", "21", "-1", "4", "2", "", "Samsung Game Pad EI-GP20 Gamepad", "1", "0", "1", "12", "13", "48", "48", "-1", "-1", "102", "103", "100", "97", "99", "96", "109", "-1", "-1", "108", "19", "22", "20", "21", "-1", "4", "2", "", "JXD s7800B Gamepad", "1", "0", "1", "11", "14", "48", "48", "102", "103", "104", "105", "99", "96", "97", "100", "109", "-1", "-1", "108", "19", "22", "20", "21", "-1", "4", "2", "", "Ipega Gamepad 9025", "1", "0", "1", "11", "14", "48", "48", "102", "103", "104", "105", "100", "97", "96", "99", "109", "106", "107", "108", "19", "22", "20", "21", "88", "4", "2", "", "Sony PS3 Sixaxis Controller", "0", "0", "1", "11", "14", "17", "18", "104", "105", "102", "103", "100", "97", "96", "99", "109", "106", "107", "108", "19", "22", "20", "21", "-1", "4", "2", "", "Sony PS3 Sixaxis Controller", "0", "0", "1", "11", "14", "17", "18", "104", "105", "102", "103", "100", "97", "96", "99", "109", "106", "107", "108", "19", "22", "20", "21", "-1", "4", "2", "", "Sony PS3 Sixaxis Controller", "0", "0", "1", "11", "14", "17", "18", "104", "105", "102", "103", "100", "97", "96", "99", "109", "106", "107", "108", "19", "22", "20", "21", "-1", "4", "2", "", "Ipega Gamepad 9025", "1", "0", "1", "11", "14", "48", "48", "102", "103", "104", "105", "100", "97", "96", "99", "109", "106", "107", "108", "19", "22", "20", "21", "88", "4", "2", "", "Sony PS4 - Dualshock Controler", "1", "0", "1", "11", "14", "17", "18", "102", "103", "100", "101", "99", "98", "97", "96", "104", "109", "108", "105", "19", "22", "20", "21", "3001", "4", "2", "", "Sony PS4 - Dualshock Controler Pf2", "1", "0", "1", "11", "14", "17", "18", "104", "105", "102", "103", "100", "97", "96", "99", "109", "106", "107", "108", "19", "22", "20", "21", "3001", "4", "2", "", "Ipega Gamepad 9028", "1", "0", "1", "11", "14", "48", "48", "102", "103", "104", "105", "100", "97", "96", "99", "109", "106", "107", "108", "19", "22", "20", "21", "88", "4", "2", "", "JXD s7800B Gamepad", "1", "0", "1", "11", "14", "17", "18", "104", "105", "102", "103", "100", "97", "96", "99", "109", "-1", "-1", "108", "19", "22", "20", "21", "-1", "4", "2", "", "Asus Gamepad", "1", "0", "1", "11", "14", "17", "18", "103", "-1", "102", "-1", "100", "97", "96", "99", "106", "-1", "-1", "107", "19", "22", "20", "21", "-1", "4", "2", "", "BETOP USB GAMEPAD", "1", "0", "1", "11", "12", "48", "48", "192", "193", "194", "195", "188", "189", "190", "191", "196", "-1", "-1", "197", "-1", "-1", "-1", "-1", "-1", "1", "2", "", "Logitech Gamepad Dual Action", "1", "0", "1", "11", "14", "17", "18", "104", "105", "102", "103", "97", "100", "99", "96", "109", "106", "107", "108", "19", "22", "20", "21", "-1", "4", "2", "", "Zeemote: SteelSeries FREE", "1", "48", "48", "48", "48", "48", "48", "-1", "-1", "102", "103", "100", "97", "96", "99", "110", "-1", "-1", "108", "19", "22", "20", "21", "-1", "1", "2", "", "Wireless Controller for PS3", "1", "0", "1", "11", "14", "17", "18", "102", "103", "100", "101", "99", "98", "97", "96", "104", "109", "108", "105", "19", "22", "20", "21", "3001", "4", "2", "", "Red samurai - Keyboard mode", "1", "48", "48", "48", "48", "48", "48", "13", "15", "12", "14", "11", "9", "8", "10", "111", "17", "16", "66", "19", "22", "20", "21", "51", "4", "2", "", "Red samurai - Gamepad mode", "1", "0", "1", "11", "14", "48", "48", "104", "105", "102", "103", "100", "97", "96", "99", "109", "106", "107", "108", 
    "19", "22", "20", "21", "-1", "4", "2", "", "DragonRise Generic USB driver", "1", "0", "1", "11", "14", "48", "48", "104", "105", "102", "103", "100", "97", "96", "99", "106", "-1", "-1", "108", "19", "22", "20", "21", "-1", "1", "2", "", "GPD G7A Gamepad", "0", "0", "1", "11", "14", "17", "18", "10017", "10018", "102", "103", "100", "97", "96", "99", "109", "106", "107", "108", "19", "22", "20", "21", "101", "4", "2", "", "Logitech Cordless RumblePad 2", "1", "0", "1", "12", "13", "48", "48", "104", "105", "102", "103", "100", "97", "96", "99", "4", "106", "107", "108", "19", "22", "20", "21", "-1", "4", "2", "", "TTX tech gamepad", "1", "0", "1", "12", "14", "17", "18", "294", "295", "292", "293", "288", "96", "97", "291", "109", "-1", "-1", "108", "19", "22", "20", "21", "-1", "4", "2", "", "Mad Catz C.T.R.L.R", "1", "0", "1", "12", "14", "23", "22", "10023", "10022", "102", "103", "100", "97", "96", "99", "4", "106", "107", "108", "19", "22", "20", "21", "-1", "4", "2", "", "8Bitdo NES30 Pro", "1", "0", "1", "11", "14", "48", "48", "104", "105", "102", "103", "99", "96", "97", "100", "109", "106", "107", "108", "19", "22", "20", "21", "-1", "4", "2", "", "Xiomi Gamepad", "1", "0", "1", "11", "14", "48", "48", "104", "105", "102", "103", "100", "97", "96", "99", "109", "106", "107", "108", "19", "22", "20", "21", "-1", "4", "2", "", "Gamesir-G4s", "1", "0", "1", "11", "14", "48", "48", "104", "105", "102", "103", "100", "97", "96", "99", "109", "106", "107", "108", "19", "22", "20", "21", "-1", "4", "2", "", "Gamesir-G4s", "1", "0", "1", "11", "14", "48", "48", "104", "105", "102", "103", "100", "97", "96", "99", "109", "106", "107", "108", "19", "22", "20", "21", "-1", "4", "2", "", "Sony PS4 - Dualshock Controler USB", "1", "0", "1", "11", "14", "48", "48", "102", "103", "100", "101", "99", "98", "97", "96", "104", "106", "107", "105", "19", "22", "20", "21", "-1", "4", "2", "", "FeiZhi Wee 2T", "1", "0", "1", "11", "14", "48", "48", "104", "105", "102", "103", "100", "97", "96", "99", "109", "-1", "-1", "108", "19", "22", "20", "21", "-1", "4", "2", ""};
    String[] gamepadDataExt = {"Other", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "Nyko Playpad Pro", "1", "0", "1", "11", "14", "17", "18", "-1", "-1", "102", "103", "100", "97", "96", "99", "4", "-1", "-1", "108", "19", "22", "20", "21", "-1", "4", "2", "", "Ipega 9017", "1", "0", "1", "11", "14", "48", "48", "-1", "-1", "192", "193", "191", "190", "189", "188", "196", "-1", "-1", "197", "19", "22", "20", "21", "-1", "4", "2", "", "Moga HID", "1", "0", "1", "11", "14", "23", "22", "-1", "-1", "102", "103", "100", "97", "96", "99", "4", "-1", "-1", "108", "19", "22", "20", "21", "-1", "4", "2", "", "GS controller", "1", "48", "48", "48", "48", "48", "48", "13", "15", "12", "14", "11", "9", "8", "10", "111", "17", "16", "66", "19", "22", "20", "21", "51", "4", "2", "", "GS gamepad", "1", "0", "1", "11", "14", "48", "48", "104", "105", "102", "103", "100", "97", "96", "99", "109", "106", "107", "108", "19", "22", "20", "21", "-1", "4", "2", ""};
    String[] returnString = new String[28];
    private int emu_gamepad_autodetect_blocked = 0;
    private List<Integer> analogpadidIgnore = new ArrayList();

    public GamepadDetection(ePSXe a, ePSXeView eview) {
        this.osVersion = 0;
        this.osVersion = Integer.parseInt(Build.VERSION.SDK);
        this.act = a;
        this.mePSXeView = eview;
    }

    public int MatchGamepad(String name, int vendor, int product) {
        if (Integer.parseInt(Build.VERSION.SDK) >= 19) {
            Log.e("epsxe", "vendor:product" + vendor + ":" + product);
            for (int i = 0; i < this.supportedGamepadsId.length; i += 2) {
                if (this.supportedGamepadsId[i].intValue() == vendor && this.supportedGamepadsId[i + 1].intValue() == product) {
                    if (vendor == 1356 && product == 1476 && name.startsWith("Wireless Controller")) {
                        return (i + 2) / 2;
                    }
                    return i / 2;
                }
            }
        }
        for (int i2 = 0; i2 < this.supportedGamepads.length; i2++) {
            if (name.contains(this.supportedGamepads[i2])) {
                return i2;
            }
        }
        return -1;
    }

    public String[] getGamepadListExt() {
        return this.supportedGamepadsExt;
    }

    public String[] getGamepadSettings(int prof, int ext) {
        if (ext != -1) {
            for (int j = 0; j < 28; j++) {
                this.returnString[j] = this.gamepadDataExt[(ext * 28) + j];
            }
        } else {
            for (int j2 = 0; j2 < 28; j2++) {
                this.returnString[j2] = this.gamepadData[(prof * 28) + j2];
            }
        }
        return this.returnString;
    }

    public String getGamepadLabel(int prof, int ext) {
        return ext != -1 ? this.gamepadDataExt[ext * 28] : this.gamepadData[prof * 28];
    }

    public String getGamepadName(int prof, int ext) {
        return ext != -1 ? this.supportedGamepadsExt[ext] : this.supportedGamepads[prof];
    }

    public void saveGamepadPref(SharedPreferences.Editor editor, String name, String value, int player, int prof, int ext) {
        editor.putString("analog" + player + "padidPref", value);
        editor.putString("analog" + player + "paddescPref", name);
        if (ext != -1) {
            for (int i = 0; i < 28; i++) {
                this.gamepadData[i] = this.gamepadDataExt[i];
            }
        }
        try {
            editor.putString("analog" + player + "rangePref", this.gamepadData[(prof * 28) + 1]);
            editor.putString("analog" + player + "leftxPref", this.gamepadData[(prof * 28) + 2]);
            editor.putString("analog" + player + "leftyPref", this.gamepadData[(prof * 28) + 3]);
            editor.putString("analog" + player + "rightxPref", this.gamepadData[(prof * 28) + 4]);
            editor.putString("analog" + player + "rightyPref", this.gamepadData[(prof * 28) + 5]);
            editor.putString("analog" + player + "l2Pref", this.gamepadData[(prof * 28) + 6]);
            editor.putString("analog" + player + "r2Pref", this.gamepadData[(prof * 28) + 7]);
            editor.putInt("P" + player + "L2", Integer.parseInt(this.gamepadData[(prof * 28) + 8]));
            editor.putInt("P" + player + "R2", Integer.parseInt(this.gamepadData[(prof * 28) + 9]));
            editor.putInt("P" + player + "L1", Integer.parseInt(this.gamepadData[(prof * 28) + 10]));
            editor.putInt("P" + player + "R1", Integer.parseInt(this.gamepadData[(prof * 28) + 11]));
            editor.putInt("P" + player + "Triangle", Integer.parseInt(this.gamepadData[(prof * 28) + 12]));
            editor.putInt("P" + player + "Circle", Integer.parseInt(this.gamepadData[(prof * 28) + 13]));
            editor.putInt("P" + player + "X", Integer.parseInt(this.gamepadData[(prof * 28) + 14]));
            editor.putInt("P" + player + "Square", Integer.parseInt(this.gamepadData[(prof * 28) + 15]));
            editor.putInt("P" + player + "Select", Integer.parseInt(this.gamepadData[(prof * 28) + 16]));
            editor.putInt("P" + player + "L3", Integer.parseInt(this.gamepadData[(prof * 28) + 17]));
            editor.putInt("P" + player + "R3", Integer.parseInt(this.gamepadData[(prof * 28) + 18]));
            editor.putInt("P" + player + "Start", Integer.parseInt(this.gamepadData[(prof * 28) + 19]));
            editor.putInt("P" + player + "Up", Integer.parseInt(this.gamepadData[(prof * 28) + 20]));
            editor.putInt("P" + player + "Right", Integer.parseInt(this.gamepadData[(prof * 28) + 21]));
            editor.putInt("P" + player + "Down", Integer.parseInt(this.gamepadData[(prof * 28) + 22]));
            editor.putInt("P" + player + "Left", Integer.parseInt(this.gamepadData[(prof * 28) + 23]));
            editor.putInt("P" + player + "Mode", Integer.parseInt(this.gamepadData[(prof * 28) + 24]));
            if (player == 1) {
                editor.putString("inputPadModePref", this.gamepadData[(prof * 28) + 25]);
            } else if (player == 2) {
                editor.putString("inputPad2ModePref", this.gamepadData[(prof * 28) + 25]);
            }
            editor.putString("inputPaintPadModePref", this.gamepadData[(prof * 28) + 26]);
        } catch (Exception e) {
            Log.e("epsxe", "error saving gamepad config profile=" + prof + " name=" + name);
        }
    }

    public Boolean isIgnoreId(int id) {
        Iterator<Integer> it = this.analogpadidIgnore.iterator();
        while (it.hasNext()) {
            int v = it.next().intValue();
            if (v == id) {
                return true;
            }
        }
        return false;
    }

    public void addIgnoreId(int id) {
        if (!isIgnoreId(id).booleanValue()) {
            this.analogpadidIgnore.add(Integer.valueOf(id));
        }
    }

    public void FindGamepad(InputDevice device, KeyEvent event, Context mContext, int[] analogpadid) {
        int sources = device.getSources();
        int id = event.getDeviceId();
        if (!isIgnoreId(id).booleanValue()) {
            if ((16777232 & sources) == 16777232 || (sources & InputDeviceCompat.SOURCE_GAMEPAD) == 1025 || ((sources & 257) == 257 && device.getName().startsWith("GS "))) {
                if (analogpadid[0] == -1) {
                    if (id != analogpadid[1] && id != analogpadid[2] && id != analogpadid[3]) {
                        this.emu_gamepad_autodetect_blocked = 1;
                        autodetectGamepad(device, id, 1, mContext, analogpadid);
                        return;
                    }
                    return;
                }
                if (analogpadid[1] == -1 && id != analogpadid[0] && id != analogpadid[2] && id != analogpadid[3]) {
                    this.emu_gamepad_autodetect_blocked = 1;
                    autodetectGamepad(device, id, 2, mContext, analogpadid);
                }
            }
        }
    }

    public Boolean isdetectedGamepad(InputDevice device, KeyEvent event, int[] analogpadid, int mogapad) {
        int sources = device.getSources();
        String name = device.getName();
        int id = event.getDeviceId();
        int productId = -1;
        int vendorId = -1;
        if (isIgnoreId(id).booleanValue()) {
            return false;
        }
        if (this.osVersion >= 19) {
            vendorId = device.getVendorId();
            productId = device.getProductId();
        }
        if ((16777232 & sources) == 16777232 || (sources & InputDeviceCompat.SOURCE_GAMEPAD) == 1025 || ((sources & 257) == 257 && device.getName().startsWith("GS "))) {
            if (analogpadid[0] == -1) {
                if (id != analogpadid[1] && id != analogpadid[2] && id != analogpadid[3]) {
                    return mogapad == -1 && MatchGamepad(name, vendorId, productId) > 0;
                }
            } else if (analogpadid[1] == -1 && id != analogpadid[0] && id != analogpadid[2] && id != analogpadid[3]) {
                return mogapad == -1 && MatchGamepad(name, vendorId, productId) > 0;
            }
        }
        return false;
    }

    private void autodetectGamepad(InputDevice device, int id, int player, Context mContext, int[] analogpadid) {
        String name = device.getName();
        String descriptor = getDescriptor(device.toString(), name, id);
        int productId = -1;
        int vendorId = -1;
        if (this.osVersion >= 19) {
            vendorId = device.getVendorId();
            productId = device.getProductId();
        }
        int match = MatchGamepad(name, vendorId, productId);
        if (match != -1) {
            if (match != 0) {
                alertdialog_automapgamepad(device, getGamepadLabel(match, -1), getGamepadName(match, -1), id, descriptor, player, match, -1, vendorId, productId, mContext, analogpadid);
            } else {
                alertdialog_automapgamepadext(device, getGamepadLabel(match, -1), id, descriptor, player, match, mContext, analogpadid);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void saveGamepadPref(Context mContext, String name, String value, int p, int prof, int ext, int vid, int pid) {
        SharedPreferences sharedPreferences = ePSXeApplication.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (vid != -1) {
            editor.putString("analog" + p + "padvpIdPref", vid + ":" + pid);
        }
        saveGamepadPref(editor, name, value, p, prof, ext);
        editor.commit();
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

    /* JADX INFO: Access modifiers changed from: private */
    public void alertdialog_automapgamepad(InputDevice dev, String label, final String n, final int i, final String d, final int pl, final int ix, final int ex, final int pid, final int vid, final Context mCont, final int[] apadid) {
        LayoutInflater adbInflater = LayoutInflater.from(mCont);
        AlertDialog alertDialog = new AlertDialog.Builder(mCont).create();
        alertDialog.setTitle(R.string.main_gamepadtitle);
        alertDialog.setMessage(this.act.getString(R.string.main_gamepadmsg1) + label + "\n" + this.act.getString(R.string.main_gamepadmsg2) + pl + "?");
        alertDialog.setCancelable(false);
        View mLayout = adbInflater.inflate(R.layout.checkbox, (ViewGroup) null);
        final CheckBox dontshow = (CheckBox) mLayout.findViewById(R.id.dontshow);
        dontshow.setChecked(true);
        alertDialog.setView(mLayout);
        alertDialog.setButton(this.act.getString(R.string.main_gamepadyes), new DialogInterface.OnClickListener() { // from class: com.epsxe.ePSXe.GamepadDetection.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                if (apadid[pl] == -1) {
                    GamepadDetection.this.saveGamepadPref(mCont, n, d, pl, ix, ex, vid, pid);
                    if (GamepadDetection.this.act != null) {
                        GamepadDetection.this.act.initGamepad(i, n, d, pl, ix, ex);
                    }
                    if (dontshow.isChecked() && GamepadDetection.this.mePSXeView != null) {
                        GamepadDetection.this.mePSXeView.setinputpaintpadmode(2, 2);
                    }
                    GamepadDetection.this.emu_gamepad_autodetect_blocked = 0;
                }
            }
        });
        alertDialog.setButton3(this.act.getString(R.string.main_gamepadyesone), new DialogInterface.OnClickListener() { // from class: com.epsxe.ePSXe.GamepadDetection.2
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                if (apadid[pl] == -1) {
                    if (dontshow.isChecked() && GamepadDetection.this.mePSXeView != null) {
                        GamepadDetection.this.mePSXeView.setinputpaintpadmode(2, 2);
                    }
                    if (GamepadDetection.this.act != null) {
                        GamepadDetection.this.act.initGamepad(i, n, d, pl, ix, ex);
                    }
                    GamepadDetection.this.emu_gamepad_autodetect_blocked = 0;
                }
            }
        });
        alertDialog.setButton2(this.act.getString(R.string.main_gamepadno), new DialogInterface.OnClickListener() { // from class: com.epsxe.ePSXe.GamepadDetection.3
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                if (apadid[pl] == -1) {
                    GamepadDetection.this.addIgnoreId(i);
                    GamepadDetection.this.emu_gamepad_autodetect_blocked = 0;
                }
            }
        });
        alertDialog.setIcon(android.R.drawable.ic_menu_directions);
        alertDialog.show();
    }

    private void alertdialog_automapgamepadext(final InputDevice dev, String l, final int i, final String d, final int pl, int idx, final Context mCont, final int[] apadid) {
        ListView gListView = new ListView(mCont);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(mCont, android.R.layout.simple_list_item_1, getGamepadListExt());
        gListView.setAdapter((ListAdapter) adapter);
        AlertDialog.Builder builder = new AlertDialog.Builder(mCont);
        builder.setTitle(R.string.main_gamepadtypetitle);
        builder.setView(gListView);
        builder.setCancelable(false);
        final AlertDialog lAlert = builder.create();
        lAlert.show();
        gListView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.epsxe.ePSXe.GamepadDetection.4
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> listView, View itemView, int position, long itemId) {
                if (apadid[pl] == -1) {
                    if (position == 0) {
                        GamepadDetection.this.addIgnoreId(i);
                        GamepadDetection.this.emu_gamepad_autodetect_blocked = 0;
                        DialogUtil.closeDialog(lAlert);
                        return;
                    } else {
                        DialogUtil.closeDialog(lAlert);
                        GamepadDetection.this.alertdialog_automapgamepad(dev, GamepadDetection.this.getGamepadLabel(0, position), GamepadDetection.this.getGamepadName(0, position), i, d, pl, 0, position, -1, -1, mCont, apadid);
                        return;
                    }
                }
                DialogUtil.closeDialog(lAlert);
            }
        });
    }
}
