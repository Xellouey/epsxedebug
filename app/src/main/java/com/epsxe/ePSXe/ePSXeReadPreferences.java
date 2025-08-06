package com.epsxe.ePSXe;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.File;

/* loaded from: classes.dex */
public class ePSXeReadPreferences {
    public static final String PREF_FILE_NAME = "ePSXePreferences";
    private Context context;
    private SharedPreferences prefs;
    public static final String[][] ButtonNames = {new String[]{"P1L2", "P1R2", "P1L1", "P1R1", "P1Triangle", "P1Circle", "P1X", "P1Square", "P1Select", "P1L3", "P1R3", "P1Start", "P1Up", "P1Right", "P1Down", "P1Left", "P1LeftUp", "P1UpRight", "P1RightDown", "P1DownLeft", "P1Mode"}, new String[]{"P2L2", "P2R2", "P2L1", "P2R1", "P2Triangle", "P2Circle", "P2X", "P2Square", "P2Select", "P2L3", "P2R3", "P2Start", "P2Up", "P2Right", "P2Down", "P2Left", "P2LeftUp", "P2UpRight", "P2RightDown", "P2DownLeft", "P2Mode"}, new String[]{"P3L2", "P3R2", "P3L1", "P3R1", "P3Triangle", "P3Circle", "P3X", "P3Square", "P3Select", "P3L3", "P3R3", "P3Start", "P3Up", "P3Right", "P3Down", "P3Left", "P3LeftUp", "P3UpRight", "P3RightDown", "P3DownLeft", "P3Mode"}, new String[]{"P4L2", "P4R2", "P4L1", "P4R1", "P4Triangle", "P4Circle", "P4X", "P4Square", "P4Select", "P4L3", "P4R3", "P4Start", "P4Up", "P4Right", "P4Down", "P4Left", "P4LeftUp", "P4UpRight", "P4RightDown", "P4DownLeft", "P4Mode"}};
    public static final String[] ButtonExtraNames = {"LoadState1", "LoadState2", "LoadState3", "LoadState4", "LoadState5", "SaveState1", "SaveState2", "SaveState3", "SaveState4", "SaveState5", "ToggleFramelimit", "Menu", "FastForward - Hold pressed", "Turbo - Hold pressed + button", "VolumeUp", "VolumeDown"};

    public ePSXeReadPreferences(Context context) {
        this.context = context;
        this.prefs = ePSXeApplication.getDefaultSharedPreferences(context);
    }

    public String getBios() {
        File biosFile = new File(context.getCacheDir() + "/bios/" + "Sony PSone BIOS (U)(v4.5)(2000-05-25)[SCPH-101].bin");
        return this.prefs.getString("biosPref", biosFile.getPath());
    }

    public int getBiosHle() {
        try {
            return Integer.parseInt(this.prefs.getString("biosHlePref", "0"));
        } catch (Exception e) {
            return 0;
        }
    }

    public String getGpu() {
        return this.prefs.getString("gpuPref", "/sdcard/libopenglplugin.so");
    }

    public String getSkin() {
        return this.prefs.getString("skinPref", "/sdcard/skin.png");
    }

    public int getCpuFrameSkip() {
        try {
            return Integer.parseInt(this.prefs.getString("cpuFrameSkipPref", "0"));
        } catch (Exception e) {
            return 0;
        }
    }

    public int getSoundQA() {
        try {
            return Integer.parseInt(this.prefs.getString("soundQAPref", "1"));
        } catch (Exception e) {
            return 1;
        }
    }

    public int getInputVibrate() {
        if (true) return 1;
        try {
            return Integer.parseInt(this.prefs.getString("inputVibratePref", "0"));
        } catch (Exception e) {
            return 0;
        }
    }

    public float getInputAlpha() {
        if (true) return 0.7f;
        try {
            return Integer.parseInt(this.prefs.getString("inputAlphaPref", "40")) / 100.0f;
        } catch (Exception e) {
            return 0.6f;
        }
    }

    public float getInputMag() {
        try {
            return Integer.parseInt(this.prefs.getString("inputMagPref", "170")) / 100.0f;
        } catch (ClassCastException e) {
            return this.prefs.getInt("inputMagPref", InputList.KEYCODE_TV) / 100.0f;
        } catch (Exception e2) {
            return 1.0f;
        }
    }

    public int getInputKeyboard() {
        try {
            return Integer.parseInt(this.prefs.getString("inputKeyboardPref", "0"));
        } catch (Exception e) {
            return 0;
        }
    }

    public int getInputVibrate2() {
        if (true) return 1;
        try {
            return Integer.parseInt(this.prefs.getString("inputVibratePref2", "0"));
        } catch (Exception e) {
            return 0;
        }
    }

    public int getInputPlayerMode() {
        try {
            return Integer.parseInt(this.prefs.getString("inputPlayerModePref", "1"));
        } catch (Exception e) {
            return 1;
        }
    }

    public int getCpuShowFPS() {
        try {
            return Integer.parseInt(this.prefs.getString("cpuShowFPSPref", "0"));
        } catch (ClassCastException e) {
            return this.prefs.getBoolean("cpuShowFPSPref", false) ? 1 : 0;
        } catch (Exception e2) {
            return 0;
        }
    }

    public int getInputPaintPadMode(boolean tv) {
        if (tv) {
            return 2;
        }
        try {
            return Integer.parseInt(this.prefs.getString("inputPaintPadModePref", "0"));
        } catch (Exception e) {
            return 0;
        }
    }

    public int getInputPaintPadMode2() {
        try {
            return Integer.parseInt(this.prefs.getString("inputPaintPadModePref2", "0"));
        } catch (Exception e) {
            return 0;
        }
    }

    public int getInputPaintPadPorMode(boolean tv) {
        if (tv) {
            return 2;
        }
        try {
            return Integer.parseInt(this.prefs.getString("inputPaintPadPorModePref", "6"));
        } catch (Exception e) {
            return 6;
        }
    }

    public int getInputPadType() {
        try {
            return Integer.parseInt(this.prefs.getString("inputPadTypePref", "0"));
        } catch (Exception e) {
            return 0;
        }
    }

    public int getInputPadType2() {
        try {
            return Integer.parseInt(this.prefs.getString("inputPadTypePref2", "0"));
        } catch (Exception e) {
            return 0;
        }
    }

    public int getInputPadAcc() {
        try {
            return Integer.parseInt(this.prefs.getString("inputPadAccPref", "0"));
        } catch (Exception e) {
            return 0;
        }
    }

    public int getInputPadAccdz() {
        try {
            return Integer.parseInt(this.prefs.getString("inputPadAccdzPref", "5"));
        } catch (ClassCastException e) {
            return this.prefs.getInt("inputPadAccdzPref", 5);
        } catch (Exception e2) {
            return 5;
        }
    }

    public int getInputPadMode() {
        try {
            return Integer.parseInt(this.prefs.getString("inputPadModePref", "1"));
        } catch (Exception e) {
            return 1;
        }
    }

    public int getInputPad2Mode() {
        try {
            return Integer.parseInt(this.prefs.getString("inputPad2ModePref", "1"));
        } catch (Exception e) {
            return 1;
        }
    }

    public int getInputPad3Mode() {
        try {
            return Integer.parseInt(this.prefs.getString("inputPad3ModePref", "1"));
        } catch (Exception e) {
            return 1;
        }
    }

    public int getInputPad4Mode() {
        try {
            return Integer.parseInt(this.prefs.getString("inputPad4ModePref", "1"));
        } catch (Exception e) {
            return 1;
        }
    }

    public int getCpuMME() {
        try {
            return Integer.parseInt(this.prefs.getString("cpuMMEPref", "1"));
        } catch (Exception e) {
            return 1;
        }
    }

    public int getScreenOrientation() {
        try {
            return Integer.parseInt(this.prefs.getString("screenOrientationPref", "0"));
        } catch (Exception e) {
            return 0;
        }
    }

    public int getScreenRatio() {
        try {
            return Integer.parseInt(this.prefs.getString("screenRatioPref", "1"));
        } catch (Exception e) {
            return 1;
        }
    }

    public int getScreenDepth(boolean tv) {
        if (true) return 32;
        if (tv) {
            try {
                return Integer.parseInt(this.prefs.getString("screenDepthPref", "32"));
            } catch (Exception e) {
                return 32;
            }
        }
        try {
            return Integer.parseInt(this.prefs.getString("screenDepthPref", "16"));
        } catch (Exception e2) {
            return 16;
        }
    }

    public int getScreenBlackbands() {
        try {
            return Integer.parseInt(this.prefs.getString("screenBlackbandsPref", "0"));
        } catch (Exception e) {
            return 0;
        }
    }

    public int getVideoFilter() {
        try {
            return Integer.parseInt(this.prefs.getString("videoFilterPref", "0"));
        } catch (Exception e) {
            return 0;
        }
    }

    public int getVideoRenderer() {
        try {
            return Integer.parseInt(this.prefs.getString("videoRendererPref", "1"));
        } catch (Exception e) {
            return 1;
        }
    }

    public int getVideoDither() {
        try {
            return Integer.parseInt(this.prefs.getString("videoDitherFinalPref", "1"));
        } catch (Exception e) {
            return 1;
        }
    }

    public int getGpuNamePref() {
        try {
            return Integer.parseInt(this.prefs.getString("gpuNamePref", "2"));
        } catch (Exception e) {
            return 2;
        }
    }

    public int getGpuMtPref(boolean tv) {
        if (tv) {
            try {
                return Integer.parseInt(this.prefs.getString("gpuMtPref", "1"));
            } catch (Exception e) {
                return 0;
            }
        }
        try {
            return Integer.parseInt(this.prefs.getString("gpuMtPref", "1"));
        } catch (Exception e2) {
            return 1;
        }
    }

    public int getGpuSoftMtPref() {
        try {
            return Integer.parseInt(this.prefs.getString("gpuSoftMtPref", "0"));
        } catch (Exception e) {
            return 0;
        }
    }

    public int getGpuAccurateMode() {
        try {
            return Integer.parseInt(this.prefs.getString("gpuAccurateModeNew", "1"));
        } catch (Exception e) {
            return 1;
        }
    }

    public int getVideoFilterhw() {
        try {
            return Integer.parseInt(this.prefs.getString("videoFilterhwPref", "1"));
        } catch (Exception e) {
            return 1;
        }
    }

    public int getSoundLatency() {
        try {
            return Integer.parseInt(this.prefs.getString("soundLatencyPref", "0"));
        } catch (Exception e) {
            return 0;
        }
    }

    public void setIsoPath(String dir) {
        SharedPreferences.Editor editor = this.prefs.edit();
        editor.putString("isoPath", dir);
        editor.commit();
    }

    public String getIsoPath() {
        return this.prefs.getString("isoPath", "/");
    }

    public int getButtonKeycode(int player, int index) {
        return this.prefs.getInt(ButtonNames[player][index], -1);
    }

    public int getButtonKeycode(int player, String name) {
        int p = player + 1;
        String buttonName = "P" + p + name;
        for (int i = 0; i < ButtonNames[player].length; i++) {
            if (buttonName.equalsIgnoreCase(ButtonNames[player][i])) {
                return this.prefs.getInt(ButtonNames[player][i], -1);
            }
        }
        Log.e("ePSXeReadPreferences", "Couldn't find button player " + player + " name " + name);
        return -1;
    }

    public int getButtonKeycodeextra(int key) {
        return this.prefs.getInt(ButtonExtraNames[key], -1);
    }

    public int getPadStatus(String name) {
        return this.prefs.getInt(name, -1);
    }

    public int getPadWH(String name) {
        return this.prefs.getInt(name, 0);
    }

    public int getPadExtra(String name) {
        try {
            return Integer.parseInt(this.prefs.getString(name, "19"));
        } catch (Exception e) {
            return 19;
        }
    }

    public float getPadSize(String name) {
        return this.prefs.getFloat(name, -1.0f);
    }

    public float getPadPos(String name) {
        return this.prefs.getFloat(name, -1.0f);
    }

    public float getPadResize(String name) {
        return this.prefs.getFloat(name, -1.0f);
    }

    public String getPadAnalogPadID(int p) {
        return this.prefs.getString("analog" + p + "padidPref", "none");
    }

    public String getPadAnalogProfile(int p) {
        return this.prefs.getString("analog" + p + "profilePref", "icade");
    }

    public String getPadAnalogPadDesc(int p) {
        return p > 1 ? this.prefs.getString("analog" + p + "paddescPref", "none") : this.prefs.getString("analog" + p + "paddescPref", "default");
    }

    public String getPadAnalogPadvpIdDesc(int p) {
        return this.prefs.getString("analog" + p + "padvpIdPref", "0000:0000");
    }

    public int getPadAnalogRange(int p) {
        try {
            return Integer.parseInt(this.prefs.getString("analog" + p + "rangePref", "0"));
        } catch (Exception e) {
            return 0;
        }
    }

    public int getPadAnalogLeftx(int p) {
        try {
            return Integer.parseInt(this.prefs.getString("analog" + p + "leftxPref", "48"));
        } catch (Exception e) {
            return 48;
        }
    }

    public int getPadAnalogLefty(int p) {
        try {
            return Integer.parseInt(this.prefs.getString("analog" + p + "leftyPref", "48"));
        } catch (Exception e) {
            return 48;
        }
    }

    public int getPadAnalogRightx(int p) {
        try {
            return Integer.parseInt(this.prefs.getString("analog" + p + "rightxPref", "48"));
        } catch (Exception e) {
            return 48;
        }
    }

    public int getPadAnalogRighty(int p) {
        try {
            return Integer.parseInt(this.prefs.getString("analog" + p + "rightyPref", "48"));
        } catch (Exception e) {
            return 48;
        }
    }

    public int getPadAnalogLeftdz(int p) {
        try {
            return Integer.parseInt(this.prefs.getString("analog" + p + "leftdzPref", "0"));
        } catch (ClassCastException e) {
            return this.prefs.getInt("analog" + p + "leftdzPref", 0);
        } catch (Exception e2) {
            return 0;
        }
    }

    public int getPadAnalogRightdz(int p) {
        try {
            return Integer.parseInt(this.prefs.getString("analog" + p + "rightdzPref", "0"));
        } catch (ClassCastException e) {
            return this.prefs.getInt("analog" + p + "rightdzPref", 0);
        } catch (Exception e2) {
            return 0;
        }
    }

    public int getPadAnalogL2(int p) {
        try {
            return Integer.parseInt(this.prefs.getString("analog" + p + "l2Pref", "48"));
        } catch (Exception e) {
            return 48;
        }
    }

    public int getPadAnalogR2(int p) {
        try {
            return Integer.parseInt(this.prefs.getString("analog" + p + "r2Pref", "48"));
        } catch (Exception e) {
            return 48;
        }
    }

    public int getPadAnalogHat(int p) {
        try {
            return Integer.parseInt(this.prefs.getString("analog" + p + "hatPref", "0"));
        } catch (Exception e) {
            return 0;
        }
    }

    public int getMiscAutosave() {
        try {
            return Integer.parseInt(this.prefs.getString("cpuShowFPSPref", "0"));
        } catch (ClassCastException e) {
            return this.prefs.getBoolean("miscAutosavePref", false) ? 1 : 0;
        } catch (Exception e2) {
            return 0;
        }
    }

    public int getMiscBrowsermode(boolean tv) {
        if (tv) {
            try {
                return Integer.parseInt(this.prefs.getString("miscBrowsermodePref", "2"));
            } catch (Exception e) {
                return 2;
            }
        }
        try {
            return Integer.parseInt(this.prefs.getString("miscBrowsermodePref", "1"));
        } catch (Exception e2) {
            return 1;
        }
    }

    public String getMemcard1() {
        return this.prefs.getString("mcr1Pref", "default");
    }

    public String getMemcard2() {
        return this.prefs.getString("mcr2Pref", "default");
    }

    public int getMemcardMode() {
        try {
            return Integer.parseInt(this.prefs.getString("mcrStatus", "0"));
        } catch (Exception e) {
            return 0;
        }
    }

    public int getMemcardFileMode() {
        try {
            return Integer.parseInt(this.prefs.getString("mcrFileMode", "0"));
        } catch (Exception e) {
            return 0;
        }
    }

    public int getmiscbootmode() {
        try {
            return Integer.parseInt(this.prefs.getString("miscbootmodePref", "0"));
        } catch (ClassCastException e) {
            return this.prefs.getBoolean("miscbootmodePref", false) ? 1 : 0;
        } catch (Exception e2) {
            return 0;
        }
    }

    public int getInputBluez() {
        try {
            return Integer.parseInt(this.prefs.getString("inputBluezPref", "0"));
        } catch (Exception e) {
            return 0;
        }
    }

    public int getInputDetectGamepad() {
        try {
            return Integer.parseInt(this.prefs.getString("inputDetectGamepadPref", "1"));
        } catch (Exception e) {
            return 1;
        }
    }

    public int getInputVibrationPSX() {
        try {
            return Integer.parseInt(this.prefs.getString("inputVibrationPSX1Pref", "0"));
        } catch (Exception e) {
            return 0;
        }
    }

    public int getInputVibrationPSX2() {
        try {
            return Integer.parseInt(this.prefs.getString("inputVibrationPSX2Pref", "0"));
        } catch (Exception e) {
            return 0;
        }
    }

    public int getMiscUILanguage() {
        try {
            return Integer.parseInt(this.prefs.getString("miscuilanguagePref", "0"));
        } catch (Exception e) {
            return 0;
        }
    }

    public int getDebugInterpreter() {
        try {
            return Integer.parseInt(this.prefs.getString("debugInterpreterPref", "1"));
        } catch (Exception e) {
            return 1;
        }
    }

    public int getGpuDmachaincore() {
        try {
            return Integer.parseInt(this.prefs.getString("gpuDmachaincorePref", "1"));
        } catch (Exception e) {
            return 1;
        }
    }

    public int getScreenWidescreen() {
        try {
            return Integer.parseInt(this.prefs.getString("screenWidescreenPref", "0"));
        } catch (Exception e) {
            return 0;
        }
    }

    public int getGpuperspectivecorrection() {
        try {
            return Integer.parseInt(this.prefs.getString("gpuPerspectiveCorrectionPref", "0"));
        } catch (Exception e) {
            return 0;
        }
    }

    public int getGpureduceres() {
        try {
            return Integer.parseInt(this.prefs.getString("gpuReduceResPref", "0"));
        } catch (Exception e) {
            return 0;
        }
    }

    public int getGpuscanlinestrans() {
        try {
            int oldtrans = Integer.parseInt(this.prefs.getString("gpuScanlinesTransPref", "0"));
            return (int) (oldtrans / 2.55d);
        } catch (ClassCastException e) {
            return this.prefs.getInt("gpuScanlinesTransPref", 0);
        } catch (Exception e2) {
            return 0;
        }
    }

    public int getGpuscanlinesthick() {
        try {
            return Integer.parseInt(this.prefs.getString("gpuScanlinesThickPref", "0"));
        } catch (ClassCastException e) {
            return this.prefs.getInt("gpuScanlinesThickPref", 0);
        } catch (Exception e2) {
            return 0;
        }
    }

    public int getCpucustomfps() {
        try {
            return Integer.parseInt(this.prefs.getString("cpuCustomFpsPref", "0"));
        } catch (ClassCastException e) {
            return this.prefs.getInt("cpuCustomFpsPref", 0);
        } catch (Exception e2) {
            return 0;
        }
    }

    public int getScreenVrmode() {
        try {
            return Integer.parseInt(this.prefs.getString("screenVrmodePref", "0"));
        } catch (Exception e) {
            return 0;
        }
    }

    public int getInputAutofirefreq() {
        try {
            return Integer.parseInt(this.prefs.getString("inputAutofirefreq11Pref", "0"));
        } catch (Exception e) {
            return 0;
        }
    }

    public int getInputAutofirebutton() {
        try {
            return Integer.parseInt(this.prefs.getString("inputAutofirebutton11Pref", "0"));
        } catch (Exception e) {
            return 0;
        }
    }

    public void setFaqPos(int x, int y) {
        SharedPreferences.Editor editor = this.prefs.edit();
        editor.putString("faqX", "" + x);
        editor.putString("faqY", "" + y);
        editor.commit();
    }

    public int getFaqPosX() {
        try {
            return Integer.parseInt(this.prefs.getString("faqX", "0"));
        } catch (Exception e) {
            return 0;
        }
    }

    public int getFaqPosY() {
        try {
            return Integer.parseInt(this.prefs.getString("faqY", "0"));
        } catch (Exception e) {
            return 0;
        }
    }

    public void setFaqPos(String code, int x, int y) {
        if (code == "") {
            setFaqPos(x, y);
            return;
        }
        SharedPreferences.Editor editor = this.prefs.edit();
        editor.putString(code + "-faqX", "" + x);
        editor.putString(code + "-faqY", "" + y);
        editor.commit();
    }

    public int getFaqPosX(String code) {
        try {
            return Integer.parseInt(this.prefs.getString(code + "-faqX", "0"));
        } catch (Exception e) {
            return getFaqPosX();
        }
    }

    public int getFaqPosY(String code) {
        try {
            return Integer.parseInt(this.prefs.getString(code + "-faqY", "0"));
        } catch (Exception e) {
            return getFaqPosY();
        }
    }

    public int getGpuOverscantop() {
        try {
            return Integer.parseInt(this.prefs.getString("gpuOverscantopPref", "2"));
        } catch (ClassCastException e) {
            return this.prefs.getInt("gpuOverscantopPref", 2);
        } catch (Exception e2) {
            return 2;
        }
    }

    public int getGpuOverscanbottom() {
        try {
            return Integer.parseInt(this.prefs.getString("gpuOverscanbottomPref", "0"));
        } catch (ClassCastException e) {
            return this.prefs.getInt("gpuOverscanbottomPref", 0);
        } catch (Exception e2) {
            return 0;
        }
    }

    public int getGpuBrighttnessprofile() {
        try {
            return Integer.parseInt(this.prefs.getString("gpuBrightnessprofilePref", "0"));
        } catch (Exception e) {
            return 0;
        }
    }

    public int getInputDynamicpad() {
        try {
            return Integer.parseInt(this.prefs.getString("inputDynamicpadPref", "0"));
        } catch (Exception e) {
            return 0;
        }
    }

    public int getInputDynamicaction() {
        try {
            return Integer.parseInt(this.prefs.getString("inputDynamicactionPref", "0"));
        } catch (Exception e) {
            return 0;
        }
    }

    public int getMiscUimenu() {
        try {
            return Integer.parseInt(this.prefs.getString("miscuimenuPref", "2"));
        } catch (Exception e) {
            return 2;
        }
    }

    public int getUiresumedialog() {
        try {
            return this.prefs.getBoolean("uiresumedialogPref", true) ? 1 : 0;
        } catch (Exception e) {
            return 1;
        }
    }

    public int getUishowmsg() {
        try {
            return this.prefs.getBoolean("uishowmsgPref", false) ? 0 : 0;
        } catch (Exception e) {
            return 0;
        }
    }

    public int getUiconfirmexitdialog() {
        try {
            return this.prefs.getBoolean("uiconfirmexitdialogPref", true) ? 1 : 0;
        } catch (Exception e) {
            return 1;
        }
    }

    public int getUishowratedialog() {
        try {
            return this.prefs.getBoolean("uishowratedialogPref", true) ? 1 : 0;
        } catch (Exception e) {
            return 1;
        }
    }

    public int getUipausesupportdialog() {
        try {
            return this.prefs.getBoolean("uipausesupportdialogPref", false) ? 1 : 0;
        } catch (Exception e) {
            return 1;
        }
    }

    public int getChangelogVersion() {
        try {
            return Integer.parseInt(this.prefs.getString("changelog", "0"));
        } catch (Exception e) {
            return 0;
        }
    }

    public int getGpuIresolution() {
        try {
            return Integer.parseInt(this.prefs.getString("gpuIresolutioPref", "1"));
        } catch (Exception e) {
            return 1;
        }
    }

    public String getNetplayServer() {
        try {
            return this.prefs.getString("netplayserver", "");
        } catch (Exception e) {
            return "";
        }
    }

    public int getInputlistfilter() {
        try {
            return Integer.parseInt(this.prefs.getString("inputFilterPref", "1"));
        } catch (Exception e) {
            return 1;
        }
    }

    public int getGpu2dfilter() {
        try {
            return Integer.parseInt(this.prefs.getString("gpu2DFilterPref", "0"));
        } catch (Exception e) {
            return 0;
        }
    }

    public String getGpushader() {
        return this.prefs.getString("gpushaderPref", "FXAA");
    }

    public int getCpuOverclock() {
        try {
            return Integer.parseInt(this.prefs.getString("cpuoverclockPref", "10"));
        } catch (Exception e) {
            return 10;
        }
    }

    public int getGpublitskip() {
        try {
            return Integer.parseInt(this.prefs.getString("gpublitskipPref", "0"));
        } catch (Exception e) {
            return 0;
        }
    }

    public int getInputmousesen() {
        try {
            return Integer.parseInt(this.prefs.getString("inputMouseSenPref", "140"));
        } catch (ClassCastException e) {
            return this.prefs.getInt("inputMouseSenPref", InputList.KEYCODE_F10);
        } catch (Exception e2) {
            return InputList.KEYCODE_F10;
        }
    }

    public int getScreenVrdistorsion() {
        try {
            return this.prefs.getBoolean("screenVrdistorsionPref", true) ? 1 : 0;
        } catch (Exception e) {
            return 1;
        }
    }

    public int getGpuGl2xResPref(int mode) {
        if (mode == 4) {
            try {
                return Integer.parseInt(this.prefs.getString("gpuGl2xResPref", "1"));
            } catch (Exception e) {
                return 1;
            }
        }
        try {
            return Integer.parseInt(this.prefs.getString("gpuGl2xResPref", "1"));
        } catch (Exception e2) {
            return 1;
        }
    }

    public int getGpuGl2FboPref() {
        try {
            return Integer.parseInt(this.prefs.getString("gpuGl2FboPref", "1"));
        } catch (Exception e) {
            return 1;
        }
    }
}
