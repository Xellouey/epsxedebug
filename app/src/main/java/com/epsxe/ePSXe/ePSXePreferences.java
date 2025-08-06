package com.epsxe.ePSXe;

import android.app.UiModeManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceGroup;
import android.preference.PreferenceScreen;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.Toast;
import com.epsxe.ePSXe.dropbox.DropboxManager;
import com.epsxe.ePSXe.gdrive.GdriveManager;

import java.io.File;
import java.util.Locale;

/* loaded from: classes.dex */
public class ePSXePreferences extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    public static final String PREF_FILE_NAME = "ePSXePreferences";
    private String biosPreference;
    private String gpuPreference;
    private String gpushaderPreference;
    private boolean inputTouchscreenPreference;
    private String secondEditTextPreference;
    private String skinPreference;
    private String soundQAPreference;
    private String soundRatePreference;
    private String videoModePreference;
    private String videoRatePreference;
    private String screen = null;
    private String locale = "en";

    @Override
    public SharedPreferences getSharedPreferences(String name, int mode) {
        return getApplication().getSharedPreferences(name, mode);
    }

    @Override
    public SharedPreferences getPreferences(int mode) {
        return getApplication().getSharedPreferences("pref", mode);
    }

    public boolean onAndroidTV() {
        UiModeManager uiModeManager = (UiModeManager) getSystemService("uimode");
        return uiModeManager.getCurrentModeType() == 4;
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

    @Override // android.preference.PreferenceActivity, android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        int oldvalue = 0;
        super.onCreate(savedInstanceState);
        this.locale = Locale.getDefault().getLanguage();
        setLocale(this);
        SharedPreferences mSharedPreference = ePSXeApplication.getDefaultSharedPreferences(this);
        if (onAndroidTV()) {
            addPreferencesFromResource(R.xml.preferences_tv);
        } else {
            addPreferencesFromResource(R.xml.preferences);
        }
        String params = getIntent().getStringExtra("com.epsxe.ePSXe.screen");
        if (params != null && params.length() > 0) {
            this.screen = params;
        }
        initSummaries(getPreferenceScreen());
        Preference mbiosPref = findPreference("biosPref");
        File biosFile = new File(getCacheDir() + "/bios/" + "Sony PSone BIOS (U)(v4.5)(2000-05-25)[SCPH-101].bin");
        mbiosPref.setSummary(mSharedPreference.getString("biosPref", biosFile.getPath()));
        Preference manalog1padidPref = findPreference("analog1padidPref");
        manalog1padidPref.setSummary(mSharedPreference.getString("analog1padidPref", "none"));
        Preference manalog2padidPref = findPreference("analog2padidPref");
        manalog2padidPref.setSummary(mSharedPreference.getString("analog2padidPref", "none"));
        Preference manalog3padidPref = findPreference("analog3padidPref");
        manalog3padidPref.setSummary(mSharedPreference.getString("analog3padidPref", "none"));
        Preference manalog4padidPref = findPreference("analog4padidPref");
        manalog4padidPref.setSummary(mSharedPreference.getString("analog4padidPref", "none"));
        String param = getIntent().getStringExtra("com.epsxe.ePSXe.biosName");
        if (param != null && param.length() > 0) {
            SharedPreferences.Editor editor2 = mSharedPreference.edit();
            editor2.putString("biosPref", param);
            editor2.commit();
            mbiosPref.setSummary(param);
        }
        mbiosPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() { // from class: com.epsxe.ePSXe.ePSXePreferences.1
            @Override // android.preference.Preference.OnPreferenceClickListener
            public boolean onPreferenceClick(Preference preference) {
                Intent myIntent = new Intent(ePSXePreferences.this, (Class<?>) FileChooser.class);
                myIntent.putExtra("com.epsxe.ePSXe.fcMode", "SELECT_BIOS");
                ePSXePreferences.this.startActivity(myIntent);
                ePSXePreferences.this.finish();
                return true;
            }
        });
        Preference mgpuPref = findPreference("gpuPref");
        mgpuPref.setSummary(mSharedPreference.getString("gpuPref", "/sdcard/libopenglplugin.so"));
        PreferenceGroup prefsGL = (PreferenceGroup) findPreference("videopreferencesGL");
        PreferenceGroup prefsSW = (PreferenceGroup) findPreference("videopreferencesSoft");
        Preference prefsShader = findPreference("gpushaderPref");
        Preference prefsIres = findPreference("gpuIresolutioPref");
        Preference prefgpugl2xres = findPreference("gpuGl2xResPref");
        Preference prefgpugl2fbo = findPreference("gpuGl2FboPref");
        Preference prefgpuPref = findPreference("gpuPref");
        if (Integer.parseInt(mSharedPreference.getString("videoRendererPref", "0")) == 2 || Integer.parseInt(mSharedPreference.getString("videoRendererPref", "0")) == 4 || Integer.parseInt(mSharedPreference.getString("videoRendererPref", "0")) == 5) {
            prefsGL.setEnabled(true);
            prefsSW.setEnabled(false);
            if (Integer.parseInt(mSharedPreference.getString("videoRendererPref", "0")) == 4 || Integer.parseInt(mSharedPreference.getString("videoRendererPref", "0")) == 5) {
                prefgpuPref.setEnabled(false);
                prefgpugl2xres.setEnabled(true);
                if (Integer.parseInt(mSharedPreference.getString("videoRendererPref", "0")) == 4) {
                    prefgpugl2fbo.setEnabled(true);
                } else {
                    prefgpugl2fbo.setEnabled(false);
                }
            } else {
                prefgpuPref.setEnabled(true);
                prefgpugl2xres.setEnabled(false);
                prefgpugl2fbo.setEnabled(false);
            }
        } else {
            if (Integer.parseInt(mSharedPreference.getString("videoRendererPref", "0")) != 3) {
                prefsShader.setEnabled(false);
            } else {
                prefsShader.setEnabled(true);
            }
            prefsGL.setEnabled(false);
            prefsSW.setEnabled(true);
            if (Integer.parseInt(mSharedPreference.getString("gpu2DFilterPref", "0")) > 1) {
                prefsIres.setEnabled(false);
            } else {
                prefsIres.setEnabled(true);
            }
        }
        String param2 = getIntent().getStringExtra("com.epsxe.ePSXe.gpuName");
        if (param2 != null && param2.length() > 0) {
            SharedPreferences.Editor editor3 = mSharedPreference.edit();
            editor3.putString("gpuPref", param2);
            editor3.commit();
            mgpuPref.setSummary(param2);
        }
        mgpuPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() { // from class: com.epsxe.ePSXe.ePSXePreferences.2
            @Override // android.preference.Preference.OnPreferenceClickListener
            public boolean onPreferenceClick(Preference preference) {
                Intent myIntent = new Intent(ePSXePreferences.this, (Class<?>) FileChooser.class);
                myIntent.putExtra("com.epsxe.ePSXe.fcMode", "SELECT_GPU");
                ePSXePreferences.this.startActivity(myIntent);
                ePSXePreferences.this.finish();
                return true;
            }
        });
        if (!onAndroidTV()) {
            Preference mskinPref = findPreference("skinPref");
            mskinPref.setSummary(mSharedPreference.getString("skinPref", "/sdcard/skin.png"));
            String param3 = getIntent().getStringExtra("com.epsxe.ePSXe.skinName");
            if (param3 != null && param3.length() > 0) {
                SharedPreferences.Editor editor4 = mSharedPreference.edit();
                editor4.putString("skinPref", param3);
                editor4.commit();
                mskinPref.setSummary(param3);
            }
            mskinPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() { // from class: com.epsxe.ePSXe.ePSXePreferences.3
                @Override // android.preference.Preference.OnPreferenceClickListener
                public boolean onPreferenceClick(Preference preference) {
                    Intent myIntent = new Intent(ePSXePreferences.this, (Class<?>) FileChooser.class);
                    myIntent.putExtra("com.epsxe.ePSXe.fcMode", "SELECT_SKIN");
                    ePSXePreferences.this.startActivity(myIntent);
                    ePSXePreferences.this.finish();
                    return true;
                }
            });
        }
        Preference mmcr1Pref = findPreference("mcr1Pref");
        mmcr1Pref.setSummary(mSharedPreference.getString("mcr1Pref", "default"));
        String param4 = getIntent().getStringExtra("com.epsxe.ePSXe.mcr1Name");
        if (param4 != null && param4.length() > 0) {
            SharedPreferences.Editor editor5 = mSharedPreference.edit();
            editor5.putString("mcr1Pref", param4);
            editor5.commit();
            mmcr1Pref.setSummary(param4);
        }
        mmcr1Pref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() { // from class: com.epsxe.ePSXe.ePSXePreferences.4
            @Override // android.preference.Preference.OnPreferenceClickListener
            public boolean onPreferenceClick(Preference preference) {
                Intent myIntent = new Intent(ePSXePreferences.this, (Class<?>) FileChooser.class);
                myIntent.putExtra("com.epsxe.ePSXe.fcMode", "SELECT_MCR1");
                ePSXePreferences.this.startActivity(myIntent);
                ePSXePreferences.this.finish();
                return true;
            }
        });
        Preference mmcr2Pref = findPreference("mcr2Pref");
        mmcr2Pref.setSummary(mSharedPreference.getString("mcr2Pref", "default"));
        String param5 = getIntent().getStringExtra("com.epsxe.ePSXe.mcr2Name");
        if (param5 != null && param5.length() > 0) {
            SharedPreferences.Editor editor6 = mSharedPreference.edit();
            editor6.putString("mcr2Pref", param5);
            editor6.commit();
            mmcr2Pref.setSummary(param5);
        }
        mmcr2Pref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() { // from class: com.epsxe.ePSXe.ePSXePreferences.5
            @Override // android.preference.Preference.OnPreferenceClickListener
            public boolean onPreferenceClick(Preference preference) {
                Intent myIntent = new Intent(ePSXePreferences.this, (Class<?>) FileChooser.class);
                myIntent.putExtra("com.epsxe.ePSXe.fcMode", "SELECT_MCR2");
                ePSXePreferences.this.startActivity(myIntent);
                ePSXePreferences.this.finish();
                return true;
            }
        });
        Preference mgpushaderPref = findPreference("gpushaderPref");
        mgpushaderPref.setSummary(mSharedPreference.getString("gpushaderPref", "FXAA"));
        if (Integer.parseInt(mSharedPreference.getString("videoRendererPref", "0")) != 3) {
            mgpushaderPref.setEnabled(false);
        } else {
            mgpushaderPref.setEnabled(true);
        }
        String param6 = getIntent().getStringExtra("com.epsxe.ePSXe.gpushaderName");
        if (param6 != null && param6.length() > 0) {
            SharedPreferences.Editor editor7 = mSharedPreference.edit();
            editor7.putString("gpushaderPref", param6);
            editor7.commit();
            mgpushaderPref.setSummary(param6);
        }
        mgpushaderPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() { // from class: com.epsxe.ePSXe.ePSXePreferences.6
            @Override // android.preference.Preference.OnPreferenceClickListener
            public boolean onPreferenceClick(Preference preference) {
                Intent myIntent = new Intent(ePSXePreferences.this, (Class<?>) FileChooser.class);
                myIntent.putExtra("com.epsxe.ePSXe.fcMode", "SELECT_SHADER");
                ePSXePreferences.this.startActivity(myIntent);
                ePSXePreferences.this.finish();
                return true;
            }
        });
        Preference mmcrDropbox = findPreference("mcrDropbox");
        if (mmcrDropbox != null) {
            mmcrDropbox.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() { // from class: com.epsxe.ePSXe.ePSXePreferences.7
                @Override // android.preference.Preference.OnPreferenceClickListener
                public boolean onPreferenceClick(Preference preference) {
                    Intent myIntent = new Intent(ePSXePreferences.this, (Class<?>) DropboxManager.class);
                    myIntent.putExtra("com.epsxe.ePSXe.psexe", "memcards");
                    myIntent.putExtra("com.epsxe.ePSXe.activity", "ePSXePreferences");
                    ePSXePreferences.this.startActivity(myIntent);
                    ePSXePreferences.this.finish();
                    return true;
                }
            });
        }
        Preference mmcrssDropbox = findPreference("mcrssDropbox");
        if (mmcrssDropbox != null) {
            mmcrssDropbox.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() { // from class: com.epsxe.ePSXe.ePSXePreferences.8
                @Override // android.preference.Preference.OnPreferenceClickListener
                public boolean onPreferenceClick(Preference preference) {
                    Intent myIntent = new Intent(ePSXePreferences.this, (Class<?>) FileChooser.class);
                    myIntent.putExtra("com.epsxe.ePSXe.fcMode", "SELECT_DROPBOX");
                    ePSXePreferences.this.startActivity(myIntent);
                    ePSXePreferences.this.finish();
                    return true;
                }
            });
        }
        Preference mmcrGdrive = findPreference("mcrGdrive");
        if (mmcrGdrive != null) {
            mmcrGdrive.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() { // from class: com.epsxe.ePSXe.ePSXePreferences.9
                @Override // android.preference.Preference.OnPreferenceClickListener
                public boolean onPreferenceClick(Preference preference) {
                    Intent myIntent = new Intent(ePSXePreferences.this, (Class<?>) GdriveManager.class);
                    myIntent.putExtra("com.epsxe.ePSXe.psexe", "memcards");
                    myIntent.putExtra("com.epsxe.ePSXe.activity", "ePSXePreferences");
                    ePSXePreferences.this.startActivity(myIntent);
                    ePSXePreferences.this.finish();
                    return true;
                }
            });
        }
        Preference mmcrssGdrive = findPreference("mcrssGdrive");
        if (mmcrssGdrive != null) {
            mmcrssGdrive.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() { // from class: com.epsxe.ePSXe.ePSXePreferences.10
                @Override // android.preference.Preference.OnPreferenceClickListener
                public boolean onPreferenceClick(Preference preference) {
                    Intent myIntent = new Intent(ePSXePreferences.this, (Class<?>) FileChooser.class);
                    myIntent.putExtra("com.epsxe.ePSXe.fcMode", "SELECT_GDRIVE");
                    ePSXePreferences.this.startActivity(myIntent);
                    ePSXePreferences.this.finish();
                    return true;
                }
            });
        }
        Preference padPref = findPreference("controller1");
        padPref.setSummary(mSharedPreference.getString("analog1paddescPref", "default"));
        for (int i = 2; i < 5; i++) {
            Preference tmpPref = findPreference("controller" + i);
            tmpPref.setSummary(mSharedPreference.getString("analog" + i + "paddescPref", "none"));
        }
        if (this.screen != null) {
            openPreference(this.screen);
        }
        this.screen = null;
    }

    private void initSummaries(PreferenceGroup pg) {
        for (int i = 0; i < pg.getPreferenceCount(); i++) {
            Preference p = pg.getPreference(i);
            if (p instanceof PreferenceGroup) {
                initSummaries((PreferenceGroup) p);
            } else if (p != null) {
                setSummary(p);
            }
        }
    }

    private void setSummary(Preference pref) {
        if (pref instanceof ListPreference) {
            ListPreference listPref = (ListPreference) pref;
            pref.setSummary(listPref.getEntry());
        }
    }

    @Override // android.preference.PreferenceActivity
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference pref) {
        InputMethodManager mgr;
        if (pref.getKey().equals("button_controller1_map_key")) {
            Intent myIntent = new Intent(this, (Class<?>) InputList.class);
            myIntent.putExtra("com.epsxe.ePSXe.player", 0);
            startActivity(myIntent);
            finish();
        }
        if (pref.getKey().equals("button_controller2_map_key")) {
            Intent myIntent2 = new Intent(this, (Class<?>) InputList.class);
            myIntent2.putExtra("com.epsxe.ePSXe.player", 1);
            startActivity(myIntent2);
            finish();
        }
        if (pref.getKey().equals("button_controller3_map_key")) {
            Intent myIntent3 = new Intent(this, (Class<?>) InputList.class);
            myIntent3.putExtra("com.epsxe.ePSXe.player", 2);
            startActivity(myIntent3);
            finish();
        }
        if (pref.getKey().equals("button_controller4_map_key")) {
            Intent myIntent4 = new Intent(this, (Class<?>) InputList.class);
            myIntent4.putExtra("com.epsxe.ePSXe.player", 3);
            startActivity(myIntent4);
            finish();
        }
        if (pref.getKey().equals("button_extra_map_key")) {
            Intent myIntent5 = new Intent(this, (Class<?>) InputList.class);
            myIntent5.putExtra("com.epsxe.ePSXe.player", 10);
            startActivity(myIntent5);
            finish();
        }
        if (pref.getKey().equals("inputgamepadtest1_key")) {
            Intent myIntent6 = new Intent(this, (Class<?>) GamepadTest.class);
            myIntent6.putExtra("com.epsxe.ePSXe.player", 1);
            startActivity(myIntent6);
            finish();
        }
        if (pref.getKey().equals("inputgamepadtest2_key")) {
            Intent myIntent7 = new Intent(this, (Class<?>) GamepadTest.class);
            myIntent7.putExtra("com.epsxe.ePSXe.player", 2);
            startActivity(myIntent7);
            finish();
        }
        if (pref.getKey().equals("inputgamepadtest3_key")) {
            Intent myIntent8 = new Intent(this, (Class<?>) GamepadTest.class);
            myIntent8.putExtra("com.epsxe.ePSXe.player", 3);
            startActivity(myIntent8);
            finish();
        }
        if (pref.getKey().equals("inputgamepadtest4_key")) {
            Intent myIntent9 = new Intent(this, (Class<?>) GamepadTest.class);
            myIntent9.putExtra("com.epsxe.ePSXe.player", 4);
            startActivity(myIntent9);
            finish();
        }
        if (pref.getKey().equals("inputeditpad1_key")) {
            Intent myIntent10 = new Intent(this, (Class<?>) ePSXePadEditor.class);
            myIntent10.putExtra("com.epsxe.ePSXe.pad", 1);
            myIntent10.putExtra("com.epsxe.ePSXe.padprofile", "");
            startActivity(myIntent10);
            finish();
        }
        if (pref.getKey().equals("inputeditpad2_key")) {
            Intent myIntent11 = new Intent(this, (Class<?>) ePSXePadEditor.class);
            myIntent11.putExtra("com.epsxe.ePSXe.pad", 1);
            myIntent11.putExtra("com.epsxe.ePSXe.padprofile", "PF2");
            startActivity(myIntent11);
            finish();
        }
        if (pref.getKey().equals("inputeditpad3_key")) {
            Intent myIntent12 = new Intent(this, (Class<?>) ePSXePadEditor.class);
            myIntent12.putExtra("com.epsxe.ePSXe.pad", 1);
            myIntent12.putExtra("com.epsxe.ePSXe.padprofile", "PF3");
            startActivity(myIntent12);
            finish();
        }
        if (pref.getKey().equals("inputeditpad4_key")) {
            Intent myIntent13 = new Intent(this, (Class<?>) ePSXePadEditor.class);
            myIntent13.putExtra("com.epsxe.ePSXe.pad", 1);
            myIntent13.putExtra("com.epsxe.ePSXe.padprofile", "PF4");
            startActivity(myIntent13);
            finish();
        }
        if (pref.getKey().equals("inputeditpadp1_key")) {
            Intent myIntent14 = new Intent(this, (Class<?>) ePSXePadEditor.class);
            myIntent14.putExtra("com.epsxe.ePSXe.pad", 1);
            myIntent14.putExtra("com.epsxe.ePSXe.padprofile", "PFP1");
            startActivity(myIntent14);
            finish();
        }
        if (pref.getKey().equals("analog1padidPref")) {
            Intent myIntent15 = new Intent(this, (Class<?>) GamepadList.class);
            myIntent15.putExtra("com.epsxe.ePSXe.player", 0);
            startActivity(myIntent15);
            finish();
        }
        if (pref.getKey().equals("analog2padidPref")) {
            Intent myIntent16 = new Intent(this, (Class<?>) GamepadList.class);
            myIntent16.putExtra("com.epsxe.ePSXe.player", 1);
            startActivity(myIntent16);
            finish();
        }
        if (pref.getKey().equals("analog3padidPref")) {
            Intent myIntent17 = new Intent(this, (Class<?>) GamepadList.class);
            myIntent17.putExtra("com.epsxe.ePSXe.player", 2);
            startActivity(myIntent17);
            finish();
        }
        if (pref.getKey().equals("analog4padidPref")) {
            Intent myIntent18 = new Intent(this, (Class<?>) GamepadList.class);
            myIntent18.putExtra("com.epsxe.ePSXe.player", 3);
            startActivity(myIntent18);
            finish();
        }
        if (pref.getKey().equals("inputmethod_select") && (mgr = (InputMethodManager) getSystemService("input_method")) != null) {
            mgr.showInputMethodPicker();
        }
        return super.onPreferenceTreeClick(preferenceScreen, pref);
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4 && event.getRepeatCount() == 0) {
            SharedPreferences mSharedPreference = ePSXeApplication.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = mSharedPreference.edit();
            editor.commit();
            Toast.makeText(this, R.string.preferences_saved, 0).show();
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override // android.app.Activity
    protected void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override // android.app.Activity
    protected void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    private PreferenceScreen findPreferenceScreenForPreference(String key, PreferenceScreen screen) {
        PreferenceScreen result;
        if (screen == null) {
            screen = getPreferenceScreen();
        }
        Adapter ada = screen.getRootAdapter();
        for (int i = 0; i < ada.getCount(); i++) {
            String prefKey = ((Preference) ada.getItem(i)).getKey();
            if (prefKey == null || !prefKey.equals(key)) {
                if (ada.getItem(i).getClass().equals(PreferenceScreen.class) && (result = findPreferenceScreenForPreference(key, (PreferenceScreen) ada.getItem(i))) != null) {
                    return result;
                }
            } else {
                return screen;
            }
        }
        return null;
    }

    private int findPreferenceOrder(String key, PreferenceScreen screen) {
        if (screen == null) {
            screen = getPreferenceScreen();
        }
        Adapter ada = screen.getRootAdapter();
        for (int i = 0; i < ada.getCount(); i++) {
            String prefKey = ((Preference) ada.getItem(i)).getKey();
            if (prefKey != null && prefKey.equals(key)) {
                return i;
            }
        }
        return -1;
    }

    private void openPreference(String key) {
        if (key.contains(".")) {
            String[] keys = key.split("\\.");
            PreferenceScreen screen = null;
            for (String value : keys) {
                screen = findPreferenceScreenForPreference(value, screen);
                int pos = findPreferenceOrder(value, screen);
                if (screen != null && pos != -1) {
                    screen.onItemClick(null, null, findPreferenceOrder(value, screen), 0L);
                }
            }
            return;
        }
        PreferenceScreen screen2 = findPreferenceScreenForPreference(key, null);
        int pos2 = findPreferenceOrder(key, null);
        if (screen2 != null && pos2 != -1) {
            screen2.onItemClick(null, null, findPreferenceOrder(key, null), 0L);
        }
    }

    @Override // android.content.SharedPreferences.OnSharedPreferenceChangeListener
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        ListPreference analogrange;
        ListPreference analogleftx;
        ListPreference analoglefty;
        ListPreference analogrightx;
        ListPreference analogrighty;
        ListPreference analogl2;
        ListPreference analogr2;
        Preference pref = findPreference(key);
        PreferenceGroup prefsGL = (PreferenceGroup) findPreference("videopreferencesGL");
        PreferenceGroup prefsSW = (PreferenceGroup) findPreference("videopreferencesSoft");
        Preference prefsShader = findPreference("gpushaderPref");
        Preference prefsIres = findPreference("gpuIresolutioPref");
        Preference prefgpuPref = findPreference("gpuPref");
        Preference prefgpugl2xres = findPreference("gpuGl2xResPref");
        Preference prefgpugl2fbo = findPreference("gpuGl2FboPref");
        if (Integer.parseInt(sharedPreferences.getString("videoRendererPref", "0")) == 2 || Integer.parseInt(sharedPreferences.getString("videoRendererPref", "0")) == 4 || Integer.parseInt(sharedPreferences.getString("videoRendererPref", "0")) == 5) {
            prefsGL.setEnabled(true);
            prefsSW.setEnabled(false);
            if (Integer.parseInt(sharedPreferences.getString("videoRendererPref", "0")) == 4 || Integer.parseInt(sharedPreferences.getString("videoRendererPref", "0")) == 5) {
                prefgpuPref.setEnabled(false);
                prefgpugl2xres.setEnabled(true);
                if (Integer.parseInt(sharedPreferences.getString("videoRendererPref", "0")) == 4) {
                    prefgpugl2fbo.setEnabled(true);
                } else {
                    prefgpugl2fbo.setEnabled(false);
                }
            } else {
                prefgpuPref.setEnabled(true);
                prefgpugl2xres.setEnabled(false);
                prefgpugl2fbo.setEnabled(false);
            }
        } else {
            if (Integer.parseInt(sharedPreferences.getString("videoRendererPref", "0")) != 3) {
                prefsShader.setEnabled(false);
            } else {
                prefsShader.setEnabled(true);
            }
            prefsGL.setEnabled(false);
            prefsSW.setEnabled(true);
            if (Integer.parseInt(sharedPreferences.getString("gpu2DFilterPref", "0")) > 1) {
                prefsIres.setEnabled(false);
            } else {
                prefsIres.setEnabled(true);
            }
        }
        if (pref instanceof ListPreference) {
            ListPreference listPref = (ListPreference) pref;
            pref.setSummary(listPref.getEntry());
            if (key.equals("inputHidePref")) {
                ListPreference padPaint2 = (ListPreference) findPreference("inputPaintPadModePref");
                if (listPref.getValue().equals("0")) {
                    padPaint2.setValue("0");
                    padPaint2.setSummary(padPaint2.getEntry());
                } else if (listPref.getValue().equals("1")) {
                    padPaint2.setValue("2");
                    padPaint2.setSummary(padPaint2.getEntry());
                }
            }
            if (key.equals("analog1profilePref") || key.equals("analog2profilePref")) {
                if (key.equals("analog1profilePref")) {
                    analogrange = (ListPreference) findPreference("analog1rangePref");
                    analogleftx = (ListPreference) findPreference("analog1leftxPref");
                    analoglefty = (ListPreference) findPreference("analog1leftyPref");
                    analogrightx = (ListPreference) findPreference("analog1rightxPref");
                    analogrighty = (ListPreference) findPreference("analog1rightyPref");
                    analogl2 = (ListPreference) findPreference("analog1l2Pref");
                    analogr2 = (ListPreference) findPreference("analog1r2Pref");
                } else if (key.equals("analog2profilePref")) {
                    analogrange = (ListPreference) findPreference("analog2rangePref");
                    analogleftx = (ListPreference) findPreference("analog2leftxPref");
                    analoglefty = (ListPreference) findPreference("analog2leftyPref");
                    analogrightx = (ListPreference) findPreference("analog2rightxPref");
                    analogrighty = (ListPreference) findPreference("analog2rightyPref");
                    analogl2 = (ListPreference) findPreference("analog2l2Pref");
                    analogr2 = (ListPreference) findPreference("analog2r2Pref");
                } else if (key.equals("analog3profilePref")) {
                    analogrange = (ListPreference) findPreference("analog3rangePref");
                    analogleftx = (ListPreference) findPreference("analog3leftxPref");
                    analoglefty = (ListPreference) findPreference("analog3leftyPref");
                    analogrightx = (ListPreference) findPreference("analog3rightxPref");
                    analogrighty = (ListPreference) findPreference("analog3rightyPref");
                    analogl2 = (ListPreference) findPreference("analog3l2Pref");
                    analogr2 = (ListPreference) findPreference("analog3r2Pref");
                } else {
                    analogrange = (ListPreference) findPreference("analog4rangePref");
                    analogleftx = (ListPreference) findPreference("analog4leftxPref");
                    analoglefty = (ListPreference) findPreference("analog4leftyPref");
                    analogrightx = (ListPreference) findPreference("analog4rightxPref");
                    analogrighty = (ListPreference) findPreference("analog4rightyPref");
                    analogl2 = (ListPreference) findPreference("analog4l2Pref");
                    analogr2 = (ListPreference) findPreference("analog4r2Pref");
                }
                if (listPref.getValue().equals("0")) {
                    analogrange.setValue("0");
                    analogrange.setSummary(analogrange.getEntry());
                    analogleftx.setValue("48");
                    analogleftx.setSummary(analogleftx.getEntry());
                    analoglefty.setValue("48");
                    analoglefty.setSummary(analoglefty.getEntry());
                    analogrightx.setValue("48");
                    analogrightx.setSummary(analogrightx.getEntry());
                    analogrighty.setValue("48");
                    analogrighty.setSummary(analogrighty.getEntry());
                    analogl2.setValue("48");
                    analogl2.setSummary(analogl2.getEntry());
                    analogr2.setValue("48");
                    analogr2.setSummary(analogr2.getEntry());
                    return;
                }
                if (listPref.getValue().equals("1")) {
                    analogrange.setValue("1");
                    analogrange.setSummary(analogrange.getEntry());
                    analogleftx.setValue("0");
                    analogleftx.setSummary(analogleftx.getEntry());
                    analoglefty.setValue("1");
                    analoglefty.setSummary(analoglefty.getEntry());
                    analogrightx.setValue("11");
                    analogrightx.setSummary(analogrightx.getEntry());
                    analogrighty.setValue("14");
                    analogrighty.setSummary(analogrighty.getEntry());
                    analogl2.setValue("17");
                    analogl2.setSummary(analogl2.getEntry());
                    analogr2.setValue("18");
                    analogr2.setSummary(analogr2.getEntry());
                    return;
                }
                if (listPref.getValue().equals("3") || listPref.getValue().equals("ouya")) {
                    analogrange.setValue("1");
                    analogrange.setSummary(analogrange.getEntry());
                    analogleftx.setValue("0");
                    analogleftx.setSummary(analogleftx.getEntry());
                    analoglefty.setValue("1");
                    analoglefty.setSummary(analoglefty.getEntry());
                    analogrightx.setValue("12");
                    analogrightx.setSummary(analogrightx.getEntry());
                    analogrighty.setValue("13");
                    analogrighty.setSummary(analogrighty.getEntry());
                    analogl2.setValue("11");
                    analogl2.setSummary(analogl2.getEntry());
                    analogr2.setValue("14");
                    analogr2.setSummary(analogr2.getEntry());
                    return;
                }
                if (listPref.getValue().equals("moga")) {
                    analogrange.setValue("1");
                    analogrange.setSummary(analogrange.getEntry());
                    analogleftx.setValue("0");
                    analogleftx.setSummary(analogleftx.getEntry());
                    analoglefty.setValue("1");
                    analoglefty.setSummary(analoglefty.getEntry());
                    analogrightx.setValue("12");
                    analogrightx.setSummary(analogrightx.getEntry());
                    analogrighty.setValue("13");
                    analogrighty.setSummary(analogrighty.getEntry());
                    analogl2.setValue("48");
                    analogl2.setSummary(analogl2.getEntry());
                    analogr2.setValue("48");
                    analogr2.setSummary(analogr2.getEntry());
                    return;
                }
                if (listPref.getValue().equals("mogapro")) {
                    analogrange.setValue("1");
                    analogrange.setSummary(analogrange.getEntry());
                    analogleftx.setValue("0");
                    analogleftx.setSummary(analogleftx.getEntry());
                    analoglefty.setValue("1");
                    analoglefty.setSummary(analoglefty.getEntry());
                    analogrightx.setValue("12");
                    analogrightx.setSummary(analogrightx.getEntry());
                    analogrighty.setValue("13");
                    analogrighty.setSummary(analogrighty.getEntry());
                    analogl2.setValue("17");
                    analogl2.setSummary(analogl2.getEntry());
                    analogr2.setValue("18");
                    analogr2.setSummary(analogr2.getEntry());
                    return;
                }
                if (listPref.getValue().equals("2") || listPref.getValue().equals("4") || listPref.getValue().equals("nvidia") || listPref.getValue().equals("xboxw")) {
                    analogrange.setValue("0");
                    analogrange.setSummary(analogrange.getEntry());
                    analogleftx.setValue("0");
                    analogleftx.setSummary(analogleftx.getEntry());
                    analoglefty.setValue("1");
                    analoglefty.setSummary(analoglefty.getEntry());
                    analogrightx.setValue("11");
                    analogrightx.setSummary(analogrightx.getEntry());
                    analogrighty.setValue("14");
                    analogrighty.setSummary(analogrighty.getEntry());
                    analogl2.setValue("17");
                    analogl2.setSummary(analogl2.getEntry());
                    analogr2.setValue("18");
                    analogr2.setSummary(analogr2.getEntry());
                    return;
                }
                analogrange.setValue("1");
                analogrange.setSummary(analogrange.getEntry());
                analogleftx.setValue("49");
                analogleftx.setSummary(analogleftx.getEntry());
                analoglefty.setValue("49");
                analoglefty.setSummary(analoglefty.getEntry());
                analogrightx.setValue("49");
                analogrightx.setSummary(analogrightx.getEntry());
                analogrighty.setValue("49");
                analogrighty.setSummary(analogrighty.getEntry());
                analogl2.setValue("49");
                analogl2.setSummary(analogl2.getEntry());
                analogr2.setValue("49");
                analogr2.setSummary(analogr2.getEntry());
            }
        }
    }
}
