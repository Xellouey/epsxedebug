package com.epsxe.ePSXe.util;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.epsxe.ePSXe.R;
import com.epsxe.ePSXe.ePSXeApplication;
import com.epsxe.ePSXe.ePSXeReadPreferences;
import java.io.File;

/* loaded from: classes.dex */
public final class ReportUtil {
    public static final String[] ButtonDisplayNames = {"L2", "R2", "L1", "R1", "Triangle", "Circle", "X", "Square", "Select", "L3", "R3", "Start", "Up", "Right", "Down", "Left", "Mode"};

    public static void showReportGamepadDialog(final Context mcont, ePSXeReadPreferences mePSXeReadPreferences) {
        TextView message = new TextView(mcont);
        String padInfo = "INPUT CONFIG\nePSXe Version=" + DeviceUtil.getAppVersion(mcont) + "\n";
        String padInfo2 = (((padInfo + "Device=" + DeviceUtil.getDeviceName() + "\n") + "Android=" + Build.VERSION.SDK_INT + "\n") + "Lang=" + Resources.getSystem().getConfiguration().locale.toString() + "\n") + "Input Mode=" + mePSXeReadPreferences.getInputPlayerMode() + "\n";
        for (int j = 1; j < 5; j++) {
            padInfo2 = ((((((((((((((padInfo2 + "\nPLAYER " + j + " INFO\n") + "Selected=" + mePSXeReadPreferences.getPadAnalogPadID(j) + "\n") + "Label=" + mePSXeReadPreferences.getPadAnalogPadDesc(j) + "\n") + "VP=" + mePSXeReadPreferences.getPadAnalogPadvpIdDesc(j) + "\n") + "PSX PAD Type=" + mePSXeReadPreferences.getInputPadMode() + "\n") + "Range=" + mePSXeReadPreferences.getPadAnalogRange(j) + "\n") + "AxisLeftX=" + mePSXeReadPreferences.getPadAnalogLeftx(j) + "\n") + "AxisLeftY=" + mePSXeReadPreferences.getPadAnalogLefty(j) + "\n") + "AxisRightX=" + mePSXeReadPreferences.getPadAnalogRightx(j) + "\n") + "AxisRightY=" + mePSXeReadPreferences.getPadAnalogRighty(j) + "\n") + "AnalogL2=" + mePSXeReadPreferences.getPadAnalogL2(j) + "\n") + "AnalogR2=" + mePSXeReadPreferences.getPadAnalogR2(j) + "\n") + "AnalogHat=" + mePSXeReadPreferences.getPadAnalogHat(j) + "\n") + "AnalogLeftDZ=" + mePSXeReadPreferences.getPadAnalogLeftdz(j) + "%\n") + "AnalogRightDZ=" + mePSXeReadPreferences.getPadAnalogRightdz(j) + "%\n";
            for (int i = 0; i < 17; i++) {
                padInfo2 = padInfo2 + ButtonDisplayNames[i] + "=" + mePSXeReadPreferences.getButtonKeycode(j - 1, i) + "\n";
            }
        }
        final String padInfoSend = padInfo2;
        SpannableString s = new SpannableString(padInfo2);
        Linkify.addLinks(s, 1);
        message.setText(s);
        message.setMovementMethod(LinkMovementMethod.getInstance());
        AlertDialog builder = new AlertDialog.Builder(mcont).setTitle("Gamepads Report").setCancelable(true).setIcon(android.R.drawable.ic_dialog_info).setPositiveButton(R.string.dialog_action_email, new DialogInterface.OnClickListener() { // from class: com.epsxe.ePSXe.util.ReportUtil.2
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                DeviceUtil.sendEmail(mcont, "Gamepads Report", padInfoSend);
            }
        }).setNegativeButton(R.string.dialog_action_cancel, new DialogInterface.OnClickListener() { // from class: com.epsxe.ePSXe.util.ReportUtil.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
            }
        }).setView(message).create();
        builder.show();
    }

    public static void showReportFullPreferencesDialog(final Context mcont, ePSXeReadPreferences mePSXeReadPreferences, int emu_enable_x86, int emu_enable_cores, int emu_enable_64bits) {
        String prefInfo;
        TextView message = new TextView(mcont);
        String prefInfo2 = ((((((((((("GENERAL\nePSXe Version=" + DeviceUtil.getAppVersion(mcont) + "\n") + "Device=" + DeviceUtil.getDeviceName() + "\n") + "Android=" + Build.VERSION.SDK_INT + "\n") + "Model=" + Build.DEVICE + "\n") + "Arch=" + emu_enable_x86 + "\n") + "Cores=" + emu_enable_cores + "\n") + "64bit=" + emu_enable_64bits + "\n") + "Cpu=" + DeviceUtil.getCPUFrequencyMax() + "\n") + "Lang=" + Resources.getSystem().getConfiguration().locale.toString() + "\n") + "\nBIOS PREFERENCES\n") + "BiosPath=" + mePSXeReadPreferences.getBios() + "\n") + "BiosHLE=" + mePSXeReadPreferences.getBiosHle() + "\n";
        File mBios = new File(mePSXeReadPreferences.getBios());
        if (!mBios.exists()) {
            prefInfo = prefInfo2 + "BiosCheck=Bios NOT found\n";
        } else if (mBios.length() == 524288) {
            prefInfo = prefInfo2 + "BiosCheck=Bios Size OK\n";
            try {
                prefInfo = prefInfo + "BiosMd5=" + FileUtil.getMD5fromFile(mePSXeReadPreferences.getBios()) + "\n";
            } catch (Exception e) {
            }
        } else {
            prefInfo = prefInfo2 + "BiosCheck=Bios found but WRONG size(" + mBios.length() + ") (maybe corrupted)\n";
        }
        String prefInfo3 = (((((((((((((((((((((((((((((((((prefInfo + "\nCPU PREFERENCES\n") + "MME=" + mePSXeReadPreferences.getCpuMME() + "\n") + "Frameskip=" + mePSXeReadPreferences.getCpuFrameSkip() + "\n") + "\nSCREEN PREFERENCES\n") + "Orientation=" + mePSXeReadPreferences.getScreenOrientation() + "\n") + "Ratio=" + mePSXeReadPreferences.getScreenRatio() + "\n") + "Bands=" + mePSXeReadPreferences.getScreenBlackbands() + "\n") + "\nVIDEO PREFERENCES\n") + "Renderer=" + mePSXeReadPreferences.getVideoRenderer() + "\n") + "iResolution=" + mePSXeReadPreferences.getGpuIresolution() + "\n") + "Plugin=" + mePSXeReadPreferences.getGpu() + "\n") + "TexMode=" + mePSXeReadPreferences.getGpuNamePref() + "\n") + "ThreadingH=" + mePSXeReadPreferences.getGpuMtPref(DeviceUtil.isAndroidTV(mcont)) + "\n") + "ThreadingS=" + mePSXeReadPreferences.getGpuSoftMtPref() + "\n") + "Subpixel=" + mePSXeReadPreferences.getGpuperspectivecorrection() + "\n") + "Chaincore=" + mePSXeReadPreferences.getGpuDmachaincore() + "\n") + "\nSOUND PREFERENCES\n") + "Quality=" + mePSXeReadPreferences.getSoundQA() + "\n") + "Latency=" + mePSXeReadPreferences.getSoundLatency() + "\n") + "\nINPUT PREFERENCES\n") + "Mode=" + mePSXeReadPreferences.getInputPlayerMode() + "\n") + "\nPLAYER 1 INFO\n") + "Selected=" + mePSXeReadPreferences.getPadAnalogPadID(1) + "\n") + "Label=" + mePSXeReadPreferences.getPadAnalogPadDesc(1) + "\n") + "VP=" + mePSXeReadPreferences.getPadAnalogPadvpIdDesc(1) + "\n") + "PSX Type=" + mePSXeReadPreferences.getInputPadMode() + "\n") + "Range=" + mePSXeReadPreferences.getPadAnalogRange(1) + "\n") + "AxisLeftX=" + mePSXeReadPreferences.getPadAnalogLeftx(1) + "\n") + "AxisLeftY=" + mePSXeReadPreferences.getPadAnalogLefty(1) + "\n") + "AxisLeftX=" + mePSXeReadPreferences.getPadAnalogRightx(1) + "\n") + "AxisLeftY=" + mePSXeReadPreferences.getPadAnalogRighty(1) + "\n") + "AnalogL2=" + mePSXeReadPreferences.getPadAnalogL2(1) + "\n") + "AnalogR2=" + mePSXeReadPreferences.getPadAnalogR2(1) + "\n") + "AnalogHat=" + mePSXeReadPreferences.getPadAnalogHat(1) + "\n";
        for (int i = 0; i < 17; i++) {
            prefInfo3 = prefInfo3 + ButtonDisplayNames[i] + "=" + mePSXeReadPreferences.getButtonKeycode(0, i) + "\n";
        }
        String prefInfo4 = (((((((((prefInfo3 + "\nMEMCARDS PREFERENCES\n") + "Mode=" + mePSXeReadPreferences.getMemcardFileMode() + "\n") + "Mcd1=" + mePSXeReadPreferences.getMemcard1() + "\n") + "Mcd2=" + mePSXeReadPreferences.getMemcard2() + "\n") + "Enable=" + mePSXeReadPreferences.getMemcardMode() + "\n") + "\nMISC PREFERENCES\n") + "AutoSave=" + mePSXeReadPreferences.getMiscAutosave() + "\n") + "BrowserMode=" + mePSXeReadPreferences.getMiscBrowsermode(DeviceUtil.isAndroidTV(mcont)) + "\n") + "\nDEBUG PREFERENCES\n") + "Interpreter=" + mePSXeReadPreferences.getDebugInterpreter() + "\n";
        String mc1 = mePSXeReadPreferences.getMemcard1();
        String mc2 = mePSXeReadPreferences.getMemcard2();
        if (mc1.equals("default")) {
            mc1 = ContextCompat.getDataDir(ePSXeApplication.getApplication()) + "/epsxe/memcards/epsxe000.mcr";
        }
        if (mc2.equals("default")) {
            mc2 = ContextCompat.getDataDir(ePSXeApplication.getApplication()) + "/epsxe/memcards/epsxe001.mcr";
        }
        File base = new File(ContextCompat.getDataDir(ePSXeApplication.getApplication()), "");
        File fr = new File(ContextCompat.getDataDir(ePSXeApplication.getApplication()), "epsxe/");
        File fm = new File(ContextCompat.getDataDir(ePSXeApplication.getApplication()), "epsxe/memcards/");
        File fs = new File(ContextCompat.getDataDir(ePSXeApplication.getApplication()), "epsxe/sstates/");
        File fmc1 = new File(mc1);
        File fmc2 = new File(mc2);
        final String prefInfo5 = ((((((((((((prefInfo4 + "\nFOLDERS INFO\n")
                + "sdcard root path = " + ContextCompat.getDataDir(ePSXeApplication.getApplication()) + "\n") + "   exists=" + base.exists() + " writable=" + base.canWrite() + "\n")
                + "ePSXe root path = " + ContextCompat.getDataDir(ePSXeApplication.getApplication()) + "/epsxe\n") + "   exists=" + fr.exists() + " writable=" + fr.canWrite() + "\n")
                + "ePSXe memcards path = " + ContextCompat.getDataDir(ePSXeApplication.getApplication()) + "/epsxe/memcards/\n") + "   exists=" + fm.exists() + " writable=" + fm.canWrite() + "\n")
                + "ePSXe sstates path = " + ContextCompat.getDataDir(ePSXeApplication.getApplication()) + "/epsxe/sstates/\n") + "   exists=" + fs.exists() + " writable=" + fs.canWrite() + "\n")
                + "ePSXe memcard1 = " + mc1 + "\n") + "   exists=" + fmc1.exists() + " writable=" + fmc1.canWrite() + "\n")
                + "ePSXe memcard2 = " + mc2 + "\n") + "   exists=" + fmc2.exists() + " writable=" + fmc2.canWrite() + "\n";
        SpannableString s = new SpannableString(prefInfo5);
        Linkify.addLinks(s, 1);
        message.setText(s);
        message.setMovementMethod(LinkMovementMethod.getInstance());
        AlertDialog builder = new AlertDialog.Builder(mcont).setTitle("Preferences Report").setCancelable(true).setIcon(android.R.drawable.ic_dialog_info).setPositiveButton(R.string.dialog_action_email, new DialogInterface.OnClickListener() { // from class: com.epsxe.ePSXe.util.ReportUtil.4
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                DeviceUtil.sendEmail(mcont, "Preferences Report", prefInfo5);
            }
        }).setNegativeButton(R.string.dialog_action_cancel, new DialogInterface.OnClickListener() { // from class: com.epsxe.ePSXe.util.ReportUtil.3
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
            }
        }).setView(message).create();
        builder.show();
    }

    public static void showReportShortPreferencesDialog(final Context mcont, ePSXeReadPreferences mePSXeReadPreferences, int emu_enable_x86, int emu_enable_cores, int emu_enable_64bits) {
        String prefInfo;
        TextView message = new TextView(mcont);
        String prefInfo2 = ((((((((((((((((((("\nPlease click in Email and fill your question\n\n\n====GENERAL INFO ABOUT THE ENVIRONMENT====\n") + "ePSXe Version=" + DeviceUtil.getAppVersion(mcont) + "\n") + "Device=" + DeviceUtil.getDeviceName() + "\n") + "Android=" + Build.VERSION.SDK_INT + "\n") + "Model=" + Build.DEVICE + "\n") + "Arch=" + emu_enable_x86 + "\n") + "Cores=" + emu_enable_cores + "\n") + "64bit=" + emu_enable_64bits + "\n") + "MME=" + mePSXeReadPreferences.getCpuMME() + "\n") + "Cpu=" + DeviceUtil.getCPUFrequencyMax() + "\n") + "Lang=" + Resources.getSystem().getConfiguration().locale.toString() + "\n") + "BiosPath=" + mePSXeReadPreferences.getBios() + "\n") + "BiosHLE=" + mePSXeReadPreferences.getBiosHle() + "\n") + "Renderer=" + mePSXeReadPreferences.getVideoRenderer() + "\n") + "ThreadingS=" + mePSXeReadPreferences.getGpuSoftMtPref() + "\n") + "Latency=" + mePSXeReadPreferences.getSoundLatency() + "\n") + "AutoSave=" + mePSXeReadPreferences.getMiscAutosave() + "\n") + "Selected=" + mePSXeReadPreferences.getPadAnalogPadID(1) + "\n") + "Label=" + mePSXeReadPreferences.getPadAnalogPadDesc(1) + "\n") + "VP=" + mePSXeReadPreferences.getPadAnalogPadvpIdDesc(1) + "\n";
        File mBios = new File(mePSXeReadPreferences.getBios());
        if (!mBios.exists()) {
            prefInfo = prefInfo2 + "BiosCheck=Bios NOT found\n";
        } else if (mBios.length() == 524288) {
            prefInfo = prefInfo2 + "BiosCheck=Bios Size OK\n";
            try {
                prefInfo = prefInfo + "BiosMd5=" + FileUtil.getMD5fromFile(mePSXeReadPreferences.getBios()) + "\n";
            } catch (Exception e) {
            }
        } else {
            prefInfo = prefInfo2 + "BiosCheck=Bios found but WRONG size(" + mBios.length() + ") (maybe corrupted)\n";
        }
        final String prefInfo3 = prefInfo + "==========================================\n";
        SpannableString s = new SpannableString(prefInfo3);
        Linkify.addLinks(s, 1);
        message.setText(s);
        message.setMovementMethod(LinkMovementMethod.getInstance());
        AlertDialog builder = new AlertDialog.Builder(mcont).setTitle("Help Request").setCancelable(true).setIcon(android.R.drawable.ic_dialog_info).setPositiveButton(R.string.dialog_action_email, new DialogInterface.OnClickListener() { // from class: com.epsxe.ePSXe.util.ReportUtil.6
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                DeviceUtil.sendEmail(mcont, "Help Request", prefInfo3);
            }
        }).setNegativeButton(R.string.dialog_action_cancel, new DialogInterface.OnClickListener() { // from class: com.epsxe.ePSXe.util.ReportUtil.5
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
            }
        }).setView(message).create();
        builder.show();
    }

    public static void sendScanningCrashEmail(Context context, String subject, String body) {
        Intent emailIntent = new Intent("android.intent.action.SENDTO");
        emailIntent.setData(Uri.parse("mailto:epsxeandroid@gmail.com"));
        emailIntent.putExtra("android.intent.extra.SUBJECT", subject);
        emailIntent.putExtra("android.intent.extra.TEXT", body);
        Uri uri = Uri.fromFile(new File(ContextCompat.getDataDir(ePSXeApplication.getApplication()), "epsxe/info/tracescan.txt"));
        emailIntent.putExtra("android.intent.extra.STREAM", uri);
        try {
            context.startActivity(Intent.createChooser(emailIntent, "Send email using..."));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "No email clients installed.", 0).show();
        }
    }
}
