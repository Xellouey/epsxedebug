package com.epsxe.ePSXe.util;

import android.app.UiModeManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;
import com.epsxe.ePSXe.ePSXe;
import com.epsxe.ePSXe.ePSXeApplication;
import com.fasterxml.jackson.core.util.MinimalPrettyPrinter;
import java.io.RandomAccessFile;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/* loaded from: classes.dex */
public final class DeviceUtil {
    public static int getCPUFrequencyMax() {
        try {
            RandomAccessFile reader = new RandomAccessFile("/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq", "r");
            String cpuMaxFreq = reader.readLine();
            reader.close();
            return Integer.parseInt(cpuMaxFreq);
        } catch (Exception e) {
            return 0;
        }
    }

    public static String getLocalIpv4Address() {
        try {
            List<NetworkInterface> nilist = Collections.list(NetworkInterface.getNetworkInterfaces());
            if (nilist.size() > 0) {
                for (NetworkInterface ni : nilist) {
                    List<InetAddress> ialist = Collections.list(ni.getInetAddresses());
                    if (ialist.size() > 0) {
                        for (InetAddress address : ialist) {
                            String ipv4 = address.getHostAddress();
                            if (!address.isLoopbackAddress() && (address instanceof Inet4Address)) {
                                return ipv4;
                            }
                        }
                    }
                }
            }
            return "";
        } catch (SocketException e) {
            return "";
        }
    }

    public static void setInmmersionMode(ePSXe activity) {
        if (Build.VERSION.SDK_INT >= 19) {
            activity.getWindow().getDecorView().setSystemUiVisibility(5894);
        } else if (Build.VERSION.SDK_INT >= 14) {
            activity.getWindow().getDecorView().setSystemUiVisibility(1);
        }
    }

    public static String getAppVersion(Context context) {
        try {
            String version = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
            return version;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("ePSXe", e.getMessage());
            return "X.X.X";
        }
    }

    public static boolean isAndroidTV(Context context) {
        UiModeManager uiModeManager = (UiModeManager) context.getSystemService("uimode");
        return uiModeManager.getCurrentModeType() == 4;
    }

    public static boolean isKindleFire() {
        return Build.MANUFACTURER.equals("Amazon") && (Build.MODEL.equals("Kindle Fire") || Build.MODEL.startsWith("KF"));
    }

    public static int getDevicesWorkaround(int emu_renderer, int emu_menu2_gpumtmodeS) {
        String str = Build.DEVICE;
        int version = Build.VERSION.SDK_INT;
        if (version < 21) {
            return 3;
        }
        return (version < 21 || emu_menu2_gpumtmodeS <= 0 || !(emu_renderer == 1 || emu_renderer == 3)) ? 1 : 1;
    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return StringUtil.capitalize(model);
        }
        if (manufacturer.equalsIgnoreCase("HTC")) {
            return "HTC " + model;
        }
        return StringUtil.capitalize(manufacturer) + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + model;
    }

    public static void sendEmail(Context context, String subject, String body) {
        Intent emailIntent = new Intent("android.intent.action.SENDTO");
        emailIntent.setData(Uri.parse("mailto:epsxeandroid@gmail.com"));
        emailIntent.putExtra("android.intent.extra.SUBJECT", subject);
        emailIntent.putExtra("android.intent.extra.TEXT", body);
        try {
            context.startActivity(Intent.createChooser(emailIntent, "Send email using..."));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "No email clients installed.", 0).show();
        }
    }

    public static boolean isMenuDialog(int emu_ui_menu_mode) {
        int version = Build.VERSION.SDK_INT;
        if (emu_ui_menu_mode == 1) {
            return true;
        }
        return version >= 21 && emu_ui_menu_mode == 2;
    }

    public static void setUILanguage(Context cont, int english) {
        if (english == 1) {
            try {
                Locale mLocale = new Locale("en");
                Locale.setDefault(mLocale);
                Configuration config = cont.getResources().getConfiguration();
                if (!config.locale.equals(mLocale)) {
                    config.locale = mLocale;
                    cont.getResources().updateConfiguration(config, null);
                    return;
                }
                return;
            } catch (Exception e) {
                return;
            }
        }
        try {
            if (!cont.getResources().getConfiguration().locale.toString().equals(Resources.getSystem().getConfiguration().locale.toString())) {
                Locale mLocale2 = new Locale(Resources.getSystem().getConfiguration().locale.toString());
                Locale.setDefault(mLocale2);
                Configuration config2 = cont.getResources().getConfiguration();
                if (!config2.locale.equals(mLocale2)) {
                    config2.locale = mLocale2;
                    cont.getResources().updateConfiguration(config2, null);
                }
            }
        } catch (Exception e2) {
        }
    }

    public static void setLocale(Context context) {
        try {
            String lang = ePSXeApplication.getDefaultSharedPreferences(context).getString("miscuilanguagePref", "0");
            int val = Integer.parseInt(lang);
            if (val == 1) {
                Locale newLocale = new Locale("en");
                Locale.setDefault(newLocale);
                Configuration config = new Configuration();
                config.locale = newLocale;
                Resources res = context.getResources();
                res.updateConfiguration(config, res.getDisplayMetrics());
            }
        } catch (Exception e) {
        }
    }
}
