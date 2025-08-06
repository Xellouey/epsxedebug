package com.epsxe.ePSXe.util;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.epsxe.ePSXe.FileCache;
import com.epsxe.ePSXe.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/* loaded from: classes.dex */
public class ShortcutUtil {
    private String shortcutId = "99";

    private Bitmap decodeFile(File f) {
        try {
            BitmapFactory.Options o = new BitmapFactory.Options();
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o);
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    private String md5(String md5) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (byte b : array) {
                sb.append(Integer.toHexString((b & 255) | 256).substring(1, 3));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    public void addShortcut(Context c, Class cs, String path, int slot, String name, String code, int size, String ptype) {
        Intent shortcutIntent = new Intent(c, (Class<?>) cs);
        shortcutIntent.putExtra("com.epsxe.ePSXe.isoName", path);
        shortcutIntent.putExtra("com.epsxe.ePSXe.isoSlot", "" + slot);
        shortcutIntent.putExtra("com.epsxe.ePSXe.gui", "0");
        shortcutIntent.putExtra("com.epsxe.ePSXe.padType", ptype);
        shortcutIntent.setAction("android.intent.action.MAIN");
        Intent addIntent = new Intent();
        addIntent.putExtra("android.intent.extra.shortcut.INTENT", shortcutIntent);
        addIntent.putExtra("android.intent.extra.shortcut.NAME", name);
        FileCache fileCache = new FileCache(c);
        String hash = md5("ePSXe_Cover_128x128_" + code);
        File f = fileCache.getFile(code, hash);
        Bitmap b = decodeFile(f);
        if (b != null) {
            if (size == 128) {
                addIntent.putExtra("android.intent.extra.shortcut.ICON", b);
            } else {
                Bitmap scaledBitmap = Bitmap.createScaledBitmap(b, 64, 64, true);
                addIntent.putExtra("android.intent.extra.shortcut.ICON", scaledBitmap);
            }
        } else {
            addIntent.putExtra("android.intent.extra.shortcut.ICON_RESOURCE", Intent.ShortcutIconResource.fromContext(c, R.drawable.icon));
        }
        addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        c.sendBroadcast(addIntent);
    }

    private class WaitFor extends AsyncTask<Void, Void, Void> {

        /* renamed from: c */
        final Context f194c;
        final int waitPeriod;

        private WaitFor(int N, Context context) {
            this.waitPeriod = N * 1000;
            this.f194c = context;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public Void doInBackground(Void... voids) {
            try {
                Thread.sleep(this.waitPeriod);
                Intent bi = new Intent(ShortcutUtil.this.shortcutId);
                bi.putExtra("msg", "deny");
                this.f194c.sendBroadcast(bi);
                return null;
            } catch (InterruptedException e) {
                return null;
            }
        }
    }

    public void addShortcut26(Context c, Class cs, String path, int slot, String name, String code, int size, String ptype) {
        ShortcutManager sm;
        Icon icon;
        if (Build.VERSION.SDK_INT >= 26 && (sm = (ShortcutManager) c.getSystemService(ShortcutManager.class)) != null && sm.isRequestPinShortcutSupported()) {
            int random = (int) ((Math.random() * 65535.0d) + 1.0d);
            this.shortcutId = "" + random;
            Intent broadcastIntent = new Intent(this.shortcutId);
            broadcastIntent.putExtra("msg", "approve");
            final AsyncTask<Void, Void, Void> waitFor = new WaitFor(10, c).execute(new Void[0]);
            c.registerReceiver(new BroadcastReceiver() { // from class: com.epsxe.ePSXe.util.ShortcutUtil.1
                @Override // android.content.BroadcastReceiver
                public void onReceive(Context c2, Intent intent) {
                    String msg = intent.getStringExtra("msg");
                    if (msg == null) {
                        msg = "NULL";
                    }
                    c2.unregisterReceiver(this);
                    waitFor.cancel(true);
                    Log.d("FileChooser", String.format("ShortcutReceiver activity = \"$1%s\" : msg = %s", intent.getAction(), msg));
                }
            }, new IntentFilter(this.shortcutId));
            Intent shortcutIntent = new Intent(c, (Class<?>) cs);
            shortcutIntent.putExtra("com.epsxe.ePSXe.isoName", path);
            shortcutIntent.putExtra("com.epsxe.ePSXe.isoSlot", "" + slot);
            shortcutIntent.putExtra("com.epsxe.ePSXe.gui", "0");
            shortcutIntent.putExtra("com.epsxe.ePSXe.padType", ptype);
            shortcutIntent.setAction(this.shortcutId);
            FileCache fileCache = new FileCache(c);
            String hash = md5("ePSXe_Cover_128x128_" + code);
            File f = fileCache.getFile(code, hash);
            Bitmap b = decodeFile(f);
            if (b != null) {
                if (size == 128) {
                    icon = Icon.createWithBitmap(b);
                } else {
                    icon = Icon.createWithBitmap(Bitmap.createScaledBitmap(b, 64, 64, true));
                }
            } else {
                icon = Icon.createWithResource(c, R.drawable.icon);
            }
            try {
                ShortcutInfo shortcutInfo = new ShortcutInfo.Builder(c, this.shortcutId).setShortLabel(name).setIcon(icon).setIntent(shortcutIntent).build();
                PendingIntent successCallback = PendingIntent.getBroadcast(c, Integer.parseInt(this.shortcutId), broadcastIntent, 0);
                sm.requestPinShortcut(shortcutInfo, successCallback.getIntentSender());
            } catch (Exception e) {
            }
        }
    }
}
