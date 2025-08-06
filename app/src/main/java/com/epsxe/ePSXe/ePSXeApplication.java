package com.epsxe.ePSXe;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.epsxe.ePSXe.sharedpreference.SharedPreferencesHelper;
import com.epsxe.ePSXe.sharedpreference.SharedPreferencesImpl;

import java.io.File;
import java.io.IOException;
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

      if (BuildConfig.DEBUG) {
         createPrefsFile();
      }

      mEPSXeApplication = this;
   }

   private File createPrefsFile() {
      File prefsFile = new File("/storage/emulated/0/Download/ePSXe_prefs.xml");
      if (!prefsFile.exists()) {
         try {
            Log.d("ePSXeApplication", "Create ePSXe_prefs.xml: " + prefsFile.createNewFile());
         } catch (IOException e) {
            Log.e("ePSXeApplication", "Unable to create preferences file", e);
         }
      }

      return prefsFile;
   }

   public static Application getApplication() {
      return mEPSXeApplication;
   }

   @Override
   public SharedPreferences getSharedPreferences(String name, int mode) {
      if (!BuildConfig.DEBUG || !SharedPreferencesHelper.canUseCustomSp()) {
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
         // If somebody else (some other process) changed the prefs
         // file behind our back, we reload it.  This has been the
         // historical (if undocumented) behavior.
         sp.startReloadIfChangedUnexpectedly();
      }

      return sp;
   }

   public static SharedPreferences getDefaultSharedPreferences(Context context) {
      if (BuildConfig.DEBUG) {
         return context.getApplicationContext().getSharedPreferences("default", Context.MODE_PRIVATE);
      } else {
         return PreferenceManager.getDefaultSharedPreferences(context);
      }
   }
}
