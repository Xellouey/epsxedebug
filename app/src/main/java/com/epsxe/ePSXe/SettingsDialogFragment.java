package com.epsxe.ePSXe;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsDialogFragment extends DialogFragment {

    private ResumeEmuCallback callback;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ResumeEmuCallback) {
            callback = (ResumeEmuCallback) context;
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dlg = new Dialog(requireContext(), R.style.Theme_AppCompat_Light_Dialog_Alert);
        dlg.setTitle(R.string.set_preferences);
        dlg.setContentView(R.layout.prefs_dialog_root);
        dlg.setCanceledOnTouchOutside(true);
        return dlg;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setLayout(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT);
        }
        if (getChildFragmentManager().findFragmentById(R.id.prefs_container) == null) {
            getChildFragmentManager()
                    .beginTransaction()
                    .replace(R.id.prefs_container, new PrefsFragment())
                    .commitNow();
        }
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if (callback != null) {
            callback.onSettingsClosed(); // Команда на возобновление
        }
    }

    public static class PrefsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.preferences, rootKey);
        }
    }

    public interface ResumeEmuCallback {
        void onSettingsClosed();
    }
}