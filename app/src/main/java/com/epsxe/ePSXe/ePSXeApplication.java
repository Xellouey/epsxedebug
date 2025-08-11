package com.epsxe.ePSXe;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.epsxe.ePSXe.sharedpreference.SharedPreferencesHelper;
import com.epsxe.ePSXe.sharedpreference.SharedPreferencesImpl;

import java.io.File;
import java.util.HashMap;

/**
 * Created with Android Studio
 * User: rafaelrs
 * Date: 04.02.2025
 */

public class ePSXeApplication extends Application {
   private static final HashMap<String, SharedPreferencesImpl> sSharedPrefs = new HashMap<>();
   private static Application mEPSXeApplication = null;

   @Override
   public void onCreate() {
      super.onCreate();

      // Create preferences file for debug version (for configuration purposes)
      if (BuildConfig.DEBUG) {
         createPrefsFile();
      }

      mEPSXeApplication = this;
   }

   private File createPrefsFile() {
      File prefsFile = new File("/storage/emulated/0/Download/ePSXe_prefs.xml");

      if (!prefsFile.exists()) {
          // Copy assets/ePSXe_prefs.xml to debug location for initial configuration
          copyAssetsPrefsToDebugFile(prefsFile);
      }

      return prefsFile;
   }

   public static Application getApplication() {
      return mEPSXeApplication;
   }

   @Override
   public SharedPreferences getSharedPreferences(String name, int mode) {
      if (BuildConfig.DEBUG) {
         // Debug: Use external file for configuration (read/write)
         if (!SharedPreferencesHelper.canUseCustomSp()) {
            return super.getSharedPreferences(name, mode);
         }

         SharedPreferencesImpl sp;
         synchronized (sSharedPrefs) {
            sp = sSharedPrefs.get(name);
            if (sp == null) {
               File prefsFile = createPrefsFile();
               sp = new SharedPreferencesImpl(prefsFile, mode);
               sSharedPrefs.put(name, sp);
               return sp;
            }
         }

         if ((mode & Context.MODE_MULTI_PROCESS) != 0) {
            sp.startReloadIfChangedUnexpectedly();
         }

         return sp;
      } else {
         // Release: Use standard SharedPreferences
         return super.getSharedPreferences(name, mode);
      }
   }

   public static SharedPreferences getDefaultSharedPreferences(Context context) {
      if (BuildConfig.DEBUG) {
         // Debug: Use the same external file as getSharedPreferences for consistency
         try {
            File prefsFile = new File("/storage/emulated/0/Download/ePSXe_prefs.xml");
            if (!prefsFile.exists()) {
               // Create initial file from assets if it doesn't exist
               ePSXeApplication app = (ePSXeApplication) context.getApplicationContext();
               app.copyAssetsPrefsToDebugFile(prefsFile);
            }
            return new com.epsxe.ePSXe.sharedpreference.SharedPreferencesImpl(prefsFile, Context.MODE_PRIVATE);
         } catch (Exception e) {
            // Fallback to standard SharedPreferences
            return PreferenceManager.getDefaultSharedPreferences(context);
         }
      } else {
         // Release: Use assets file for consistent settings (read-only)
         return getSharedPreferencesFromAssets(context);
      }
   }
   
   private static SharedPreferences getSharedPreferencesFromAssets(Context context) {
      try {
         // Create a temporary file from assets
         File tempFile = new File(context.getCacheDir(), "ePSXe_prefs_from_assets.xml");
         
         // Copy assets file to cache for reading
         java.io.InputStream inputStream = context.getAssets().open("ePSXe_prefs.xml");
         java.io.FileOutputStream outputStream = new java.io.FileOutputStream(tempFile);
         
         byte[] buffer = new byte[1024];
         int length;
         while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
         }
         
         inputStream.close();
         outputStream.close();
         

         
         // Return SharedPreferences based on assets file
         return new com.epsxe.ePSXe.sharedpreference.SharedPreferencesImpl(tempFile, Context.MODE_PRIVATE);
         
      } catch (Exception e) {
         // Fallback to standard SharedPreferences if assets reading fails
         Log.e("ASSETS_PREFS", "Failed to read from assets, using standard SharedPreferences", e);
         return PreferenceManager.getDefaultSharedPreferences(context);
      }
   }
   
   public void copyAssetsPrefsToDebugFile(File debugFile) {
      try {
         // Open assets/ePSXe_prefs.xml
         java.io.InputStream inputStream = getAssets().open("ePSXe_prefs.xml");
         java.io.FileOutputStream outputStream = new java.io.FileOutputStream(debugFile);
         
         // Copy file content
         byte[] buffer = new byte[1024];
         int length;
         while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
         }
         
         inputStream.close();
         outputStream.close();
         

         
      } catch (Exception e) {
         // Silently handle error
      }
   }
}