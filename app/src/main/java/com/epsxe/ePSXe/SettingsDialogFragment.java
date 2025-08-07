package com.epsxe.ePSXe;

import android.app.Dialog;
import android.app.DialogFragment; // <-- ВАЖНО: android.app
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.PreferenceFragment; // <-- ВАЖНО: android.preference

public class SettingsDialogFragment extends DialogFragment {

    private ResumeEmuCallback callback;

    @Override
    public void onAttach(android.app.Activity activity) { // <-- Используем старый onAttach
        super.onAttach(activity);
        if (activity instanceof ResumeEmuCallback) {
            callback = (ResumeEmuCallback) activity;
            callback.onSettingsOpened();
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dlg = new Dialog(getActivity());
        // Мы не можем использовать AppCompat темы, поэтому просто создаем обычный диалог
        // Внешний вид будет системным, а не Material Design, но это не вызовет краша
        dlg.setTitle(R.string.set_preferences);
        return dlg;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Вкладываем PreferenceFragment
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new PrefsFragment())
                .commit();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (callback != null) {
            callback.onSettingsClosed();
        }
    }

    public static class PrefsFragment extends PreferenceFragment { // <-- ВАЖНО
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences); // Используем наш СТАРЫЙ preferences.xml
        }
    }

    public interface ResumeEmuCallback {
        void onSettingsOpened();
        void onSettingsClosed();
    }
}