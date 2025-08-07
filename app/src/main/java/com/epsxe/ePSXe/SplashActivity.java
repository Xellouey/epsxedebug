package com.epsxe.ePSXe;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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
    
    static {
        Log.d("ePSXeDebug", "=== SplashActivity CLASS LOADED ===");
    }
    
    private static final int REQUEST_CODE_EXTERNAL_STORAGE = 1;

    String[] permissions = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("ePSXeDebug", "=== SplashActivity.onCreate() STARTED ===");
        super.onCreate(savedInstanceState);
        
        try {
            setContentView(R.layout.splash_activity);
            Log.d("ePSXeDebug", "SplashActivity: Layout set successfully");
        } catch (Exception e) {
            Log.e("ePSXeDebug", "SplashActivity: Error setting content view", e);
            return;
        }

        Log.d("ePSXeDebug", "SplashActivity: === APP STARTED ===");
        Log.d("ePSXeDebug", "SplashActivity: Build type: " + (BuildConfig.DEBUG ? "DEBUG" : "RELEASE"));
        Log.d("ePSXeDebug", "SplashActivity: Package name: " + getPackageName());
        Log.d("ePSXeDebug", "SplashActivity: Cache dir: " + getCacheDir().getAbsolutePath());
        
        if (BuildConfig.DEBUG) {
            // Debug build: check and request permissions
            Log.d("ePSXeDebug", "SplashActivity: Debug build - checking external storage permissions");
            try {
                if (checkStoragePermission()) {
                    Log.d("ePSXeDebug", "SplashActivity: Permissions OK, calling doOnCreate()");
                    doOnCreate();
                } else {
                    Log.d("ePSXeDebug", "SplashActivity: Permissions not granted, waiting for user response");
                }
            } catch (Exception e) {
                Log.e("ePSXeDebug", "SplashActivity: Error checking permissions", e);
                doOnCreate(); // Fallback to internal storage
            }
        } else {
            // Release build: skip permissions, use internal storage only
            Log.d("ePSXeDebug", "SplashActivity: Release build - using internal storage only");
            doOnCreate();
        }
        
        Log.d("ePSXeDebug", "=== SplashActivity.onCreate() FINISHED ===");
    }



    private void doOnCreate() {
        Log.d("ePSXeDebug", "=== SplashActivity.doOnCreate() STARTED ===");
        
        // Extract game to cache
        TextView loading_progress = findViewById(R.id.loading_progress);
        TextView splash_status = findViewById(R.id.splash_status);
        
        if (loading_progress == null) {
            Log.e("ePSXeDebug", "SplashActivity: loading_progress TextView is null!");
        }
        if (splash_status == null) {
            Log.e("ePSXeDebug", "SplashActivity: splash_status TextView is null!");
        }
        
        Log.d("ePSXeDebug", "SplashActivity: Starting background thread for asset extraction");
        new Thread(() -> {
            android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
            Log.d("ePSXeDebug", "SplashActivity: Background thread started");

            try {
                runOnUiThread(() -> {
                    if (splash_status != null) {
                        splash_status.setText(R.string.splash_loading);
                        Log.d("ePSXeDebug", "SplashActivity: Status text updated");
                    }
                });

                Log.d("ePSXeDebug", "SplashActivity: Initializing SD card directories");
                initSdCard();

                if (!BuildConfig.DEBUG) {
                    Log.d("ePSXeDebug", "SplashActivity: Release build - loading settings");
                    loadSettings();
                } else {
                    Log.d("ePSXeDebug", "SplashActivity: Debug build - skipping settings load");
                }

                Log.d("ePSXeDebug", "SplashActivity: Getting asset file lists");
                String[] biosFiles = getAssets().list("bios");
                String[] gameFiles = getAssets().list("Game");
                
                Log.d("ePSXeDebug", "SplashActivity: Found " + (biosFiles != null ? biosFiles.length : 0) + " BIOS files");
                Log.d("ePSXeDebug", "SplashActivity: Found " + (gameFiles != null ? gameFiles.length : 0) + " game files");
                
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
                Log.e("ePSXeDebug", "SplashActivity: Error during asset extraction", e);
                runOnUiThread(() -> {
                    Toast.makeText(SplashActivity.this, "Error loading game files: " + e.getMessage(), 
                                  Toast.LENGTH_LONG).show();
                });
                return; // Don't continue if extraction failed
            }

            Log.d("ePSXeDebug", "SplashActivity: Asset extraction completed successfully");
            Log.d("ePSXeDebug", "SplashActivity: Starting main ePSXe activity");
            
            try {
                Intent intent = new Intent(SplashActivity.this, ePSXe.class);
                Log.d("ePSXeDebug", "SplashActivity: Intent created: " + intent.toString());
                startActivity(intent);
                Log.d("ePSXeDebug", "SplashActivity: Activity started, finishing splash");
                finish();
            } catch (Exception e) {
                Log.e("ePSXeDebug", "SplashActivity: Error starting main activity", e);
                runOnUiThread(() -> {
                    Toast.makeText(SplashActivity.this, "Error starting game: " + e.getMessage(), 
                                  Toast.LENGTH_LONG).show();
                });
            }

        }).start();
        
        Log.d("ePSXeDebug", "=== SplashActivity.doOnCreate() FINISHED ===");
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
        // Only check permissions in debug builds
        if (!BuildConfig.DEBUG) {
            Log.d("ePSXeDebug", "SplashActivity: Release build - skipping permission check");
            return true;
        }
        
        Log.d("ePSXeDebug", "=== SplashActivity: CHECKING PERMISSIONS (DEBUG BUILD) ===");
        Log.d("ePSXeDebug", "SplashActivity: Android version: " + android.os.Build.VERSION.SDK_INT);
        
        // For debug builds, check external storage permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d("ePSXeDebug", "SplashActivity: Debug build - requesting storage permissions...");
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_EXTERNAL_STORAGE);
            return false;
        }
        
        Log.d("ePSXeDebug", "SplashActivity: Debug build - storage permissions already granted!");
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        
        // Only handle permission results in debug builds
        if (!BuildConfig.DEBUG) {
            Log.d("ePSXeDebug", "SplashActivity: Release build - ignoring permission result");
            return;
        }
        
        Log.d("ePSXeDebug", "=== SplashActivity: DEBUG PERMISSION RESULT ===");
        Log.d("ePSXeDebug", "SplashActivity: Request code: " + requestCode);
        Log.d("ePSXeDebug", "SplashActivity: Results length: " + grantResults.length);
        
        if (requestCode == REQUEST_CODE_EXTERNAL_STORAGE) {
            for (int i = 0; i < grantResults.length && i < permissions.length; i++) {
                Log.d("ePSXeDebug", "SplashActivity: Permission " + permissions[i] + ": " + 
                      (grantResults[i] == PackageManager.PERMISSION_GRANTED ? "GRANTED" : "DENIED"));
            }
            
            // Check if READ_EXTERNAL_STORAGE permission was granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("ePSXeDebug", "SplashActivity: Debug: External storage permission granted!");
                Toast.makeText(this, "Debug: External storage access enabled", Toast.LENGTH_SHORT).show();
            } else {
                Log.d("ePSXeDebug", "SplashActivity: Debug: External storage permission denied");
                Toast.makeText(this, "Debug: External storage denied, using internal storage", 
                              Toast.LENGTH_LONG).show();
            }
            
            // Always proceed with doOnCreate() regardless of permission result
            doOnCreate();
        }
    }

    private void initSdCard() {
        Log.d("ePSXeDebug", "=== SplashActivity.initSdCard() STARTED ===");
        
        File extStore = getCacheDir();
        Log.d("ePSXeDebug", "SplashActivity: Cache directory: " + extStore.getAbsolutePath());
        
        String gameDirPath = extStore.getAbsolutePath() + "/Game";
        File gameDir = new File(gameDirPath);
        Log.d("ePSXeDebug", "SplashActivity: Game directory path: " + gameDirPath);
        
        if (!gameDir.exists()) {
            Log.d("ePSXeDebug", "SplashActivity: Game directory doesn't exist, creating...");
            boolean created = gameDir.mkdir();
            Log.d("ePSXeDebug", "SplashActivity: Game directory created: " + created);
        } else {
            Log.d("ePSXeDebug", "SplashActivity: Game directory already exists");
        }
        
        String biosDirPath = extStore.getAbsolutePath() + "/bios";
        File biosDir = new File(biosDirPath);
        Log.d("ePSXeDebug", "SplashActivity: BIOS directory path: " + biosDirPath);
        
        if (!biosDir.exists()) {
            Log.d("ePSXeDebug", "SplashActivity: BIOS directory doesn't exist, creating...");
            boolean created = biosDir.mkdir();
            Log.d("ePSXeDebug", "SplashActivity: BIOS directory created: " + created);
        } else {
            Log.d("ePSXeDebug", "SplashActivity: BIOS directory already exists");
        }
        
        Log.d("ePSXeDebug", "=== SplashActivity.initSdCard() FINISHED ===");
    }
}