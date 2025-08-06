package com.epsxe.ePSXe;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.epsxe.ePSXe.sharedpreference.SharedPreferencesImpl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;

/**
 * Created with Android Studio
 * User: rafaelrs
 * Date: 15.01.2025
 */

public class SplashActivity extends Activity {
    private static final int REQUEST_CODE_EXTERNAL_STORAGE = 1;

    String[] permissions = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        if (!BuildConfig.DEBUG || checkStoragePermission()) {
            doOnCreate();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                doOnCreate();
            } else {
                Toast.makeText(this, R.string.storage_persmission_non_granted, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void doOnCreate() {
        // Extract game to cache
        TextView loading_progress = findViewById(R.id.loading_progress);
        TextView splash_status = findViewById(R.id.splash_status);
        new Thread(() -> {
            android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);

            try {

                runOnUiThread(() -> splash_status.setText(R.string.splash_loading));

                initSdCard();

                if (!BuildConfig.DEBUG) {
                    loadSettings();
                }

                String[] biosFiles = getAssets().list("bios");
                String[] gameFiles = getAssets().list("Game");
                int fileNum = 0;
                for (String biosFile : biosFiles) {
                    File f = new File(getCacheDir() + "/bios/" + biosFile);
                    InputStream is = getAssets().open("bios/" + biosFile);
                    long bytesInSource = is.available();

                    if (!f.exists() || f.length() != bytesInSource) {

                        FileOutputStream fos = new FileOutputStream(f);

                        byte[] buffer = new byte[512 * 1024];
                        int bytesRead;
                        int bytesTotal = 0;
                        while ((bytesRead = is.read(buffer)) != -1) {
                            fos.write(buffer, 0, bytesRead);
                            bytesTotal += bytesRead;
                            printProgress(loading_progress, fileNum, biosFiles.length + gameFiles.length,
                                    bytesTotal, bytesInSource);
                        }

                        fos.close();
                    }
                    fileNum++;
                }
                for (String gameFile : gameFiles) {
                    File f = new File(getCacheDir() + "/Game/" + gameFile);
                    InputStream is = getAssets().open("Game/" + gameFile);
                    long bytesInSource = is.available();

                    if (!f.exists() || f.length() != bytesInSource) {

                        FileOutputStream fos = new FileOutputStream(f);

                        byte[] buffer = new byte[512 * 1024];
                        int bytesRead;
                        int bytesTotal = 0;
                        while ((bytesRead = is.read(buffer)) != -1) {
                            fos.write(buffer, 0, bytesRead);
                            bytesTotal += bytesRead;
                            printProgress(loading_progress, fileNum, biosFiles.length + gameFiles.length,
                                    bytesTotal, bytesInSource);
                        }

                        fos.close();
                    }
                    fileNum++;
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            startActivity(new Intent(SplashActivity.this, ePSXe.class));
            finish();

        }).start();
    }

    private void printProgress(TextView loading_progress, int fileNum,
                               int totalFiles, int bytesTotal, long bytesInSource) {
        float progressBase = ((float) fileNum / totalFiles) * 100;
        int progress = Math.round(progressBase + (((float) bytesTotal / bytesInSource) * 100) / totalFiles);
        runOnUiThread(() -> loading_progress.setText("" + progress + "%"));
    }

    @SuppressLint("ApplySharedPref")
    private void loadSettings() {
        try {
            File f = new File(getCacheDir() + "/ePSXe_prefs.xml");
            Log.e("loadSettings", f.getAbsolutePath());
            InputStream is = getAssets().open("ePSXe_prefs.xml");
            long bytesInSource = is.available();

            if (!f.exists() || f.length() != bytesInSource) {
                FileOutputStream fos = new FileOutputStream(f);

                byte[] buffer = new byte[512 * 1024];
                int bytesRead;
                while ((bytesRead = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                }
                fos.close();
            }

            SharedPreferencesImpl source = new SharedPreferencesImpl(f, Context.MODE_PRIVATE);
            SharedPreferences dest = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = dest.edit();
            editor.clear();
            for (Map.Entry<String, ?> entry : source.getAll().entrySet()) {
                Object v = entry.getValue();
                String key = entry.getKey();
                Log.e("loadSettings", "key: " + key + ", value: " + v);
                //Now we just figure out what type it is, so we can copy it.
                // Note that i am using Boolean and Integer instead of boolean and int.
                // That's because the Entry class can only hold objects and int and boolean are primatives.
                if (v instanceof Boolean)
                    // Also note that i have to cast the object to a Boolean
                    // and then use .booleanValue to get the boolean
                    editor.putBoolean(key, (Boolean) v);
                else if (v instanceof Float)
                    editor.putFloat(key, (Float) v);
                else if (v instanceof Integer)
                    editor.putInt(key, (Integer) v);
                else if (v instanceof Long)
                    editor.putLong(key, (Long) v);
                else if (v instanceof String)
                    editor.putString(key, ((String) v));
                else if (v instanceof Set)
                    editor.putStringSet(key, ((Set) v));
            }
            Log.e("loadSettings", "commit: " + editor.commit());

        } catch (IOException e) {
            Log.e("loadSettings", "Unable to load preferences", e);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private boolean checkStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    permissions,
                    REQUEST_CODE_EXTERNAL_STORAGE);
            return false;
        } else {
            return true;
        }
    }

    private void initSdCard() {
        boolean status = true;
        File extStore = getCacheDir();
        String gameDirPath = extStore.getAbsolutePath() + "/Game";
        File gameDir = new File(gameDirPath);
        if (!gameDir.exists()) {
            status = gameDir.mkdir();
        }
        String biosDirPath = extStore.getAbsolutePath() + "/bios";
        File biosDir = new File(biosDirPath);
        if (!biosDir.exists()) {
            status = biosDir.mkdir();
        }
    }
}