package com.epsxe.views.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.DialogPreference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.epsxe.ePSXe.ePSXeApplication;
import com.fasterxml.jackson.core.util.MinimalPrettyPrinter;

/* loaded from: classes.dex */
public class TickSeekBarPreference extends DialogPreference implements SeekBar.OnSeekBarChangeListener {
    private static final String androidns = "http://schemas.android.com/apk/res/android";
    private String key1;
    private CheckBox mCheckBox;
    private Context mContext;
    private int mDefault;
    private String mDialogMessage;
    private int mMax;
    private int mMin;
    private SeekBar mSeekBar;
    private TextView mSplashText;
    private int mStep;
    private String mSuffix;
    private String mTickLabel;
    private int mValue;
    private TextView mValueText;

    public TickSeekBarPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mDefault = 0;
        this.mValue = 0;
        setPersistent(false);
        this.mContext = context;
        int mDialogMessageId = attrs.getAttributeResourceValue(androidns, "dialogMessage", 0);
        if (mDialogMessageId == 0) {
            this.mDialogMessage = attrs.getAttributeValue(androidns, "dialogMessage");
        } else {
            this.mDialogMessage = this.mContext.getString(mDialogMessageId);
        }
        int mSuffixId = attrs.getAttributeResourceValue(androidns, "text", 0);
        if (mSuffixId == 0) {
            this.mSuffix = attrs.getAttributeValue(androidns, "text");
        } else {
            this.mSuffix = this.mContext.getString(mSuffixId);
        }
        this.mDefault = attrs.getAttributeIntValue(androidns, "defaultValue", 0);
        this.mMax = attrs.getAttributeIntValue(androidns, "max", 100);
        this.mMin = attrs.getAttributeIntValue(null, "min", 0);
        this.mStep = attrs.getAttributeIntValue(null, "step", 1);
        this.mTickLabel = attrs.getAttributeValue(null, "ticklabel");
        this.key1 = attrs.getAttributeValue(androidns, "key");
    }

    @Override // android.preference.DialogPreference
    protected View onCreateDialogView() {
        LinearLayout layout = new LinearLayout(this.mContext);
        layout.setOrientation(1);
        layout.setPadding(6, 6, 6, 6);
        this.mSplashText = new TextView(this.mContext);
        this.mSplashText.setPadding(30, 10, 30, 10);
        if (this.mDialogMessage != null) {
            this.mSplashText.setText(this.mDialogMessage);
        }
        layout.addView(this.mSplashText);
        this.mValueText = new TextView(this.mContext);
        this.mValueText.setGravity(1);
        this.mValueText.setTextSize(20.0f);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, -2);
        layout.addView(this.mValueText, params);
        this.mSeekBar = new SeekBar(this.mContext);
        this.mSeekBar.setOnSeekBarChangeListener(this);
        layout.addView(this.mSeekBar, params);
        if (this.mTickLabel != null) {
            LinearLayout layout2 = new LinearLayout(this.mContext);
            layout2.setOrientation(0);
            this.mCheckBox = new CheckBox(this.mContext);
            this.mCheckBox.setOnCheckedChangeListener(new ticklistener());
            layout2.addView(this.mCheckBox);
            this.mSplashText = new TextView(this.mContext);
            this.mSplashText.setText(this.mTickLabel);
            this.mSplashText.setTextSize(20.0f);
            layout2.addView(this.mSplashText);
            layout2.setPadding(30, 10, 30, 10);
            layout.addView(layout2);
        }
        if (shouldPersist()) {
            this.mValue = getPersistedInt(this.mDefault);
        }
        this.mSeekBar.setMax((this.mMax - this.mMin) / this.mStep);
        this.mSeekBar.setProgress(this.mValue);
        return layout;
    }

    @Override // android.preference.DialogPreference
    protected void onBindDialogView(View v) {
        int value;
        super.onBindDialogView(v);
        SharedPreferences sharedPreferences = ePSXeApplication.getDefaultSharedPreferences(this.mContext);
        try {
            value = Integer.parseInt(sharedPreferences.getString(this.key1, "" + this.mDefault));
        } catch (ClassCastException e) {
            value = sharedPreferences.getInt(this.key1, this.mDefault);
        }
        if (this.mTickLabel != null) {
            if (value == 0) {
                this.mSeekBar.setEnabled(false);
                this.mCheckBox.setChecked(true);
            } else {
                this.mSeekBar.setProgress((value - this.mMin) / this.mStep);
                this.mSeekBar.setEnabled(true);
            }
        } else {
            this.mSeekBar.setProgress((value - this.mMin) / this.mStep);
        }
        this.mSeekBar.setMax((this.mMax - this.mMin) / this.mStep);
    }

    @Override // android.preference.DialogPreference
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);
        if (positiveResult) {
            SharedPreferences sharedPreferences = ePSXeApplication.getDefaultSharedPreferences(this.mContext);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            int value = (this.mStep * this.mSeekBar.getProgress()) + this.mMin;
            if (this.mTickLabel != null && this.mCheckBox.isChecked()) {
                value = 0;
            }
            editor.putInt(this.key1, value);
            editor.commit();
        }
    }

    @Override // android.widget.SeekBar.OnSeekBarChangeListener
    public void onProgressChanged(SeekBar seek, int value, boolean fromTouch) {
        String t = String.valueOf((this.mStep * value) + this.mMin);
        if (this.mTickLabel != null && this.mCheckBox.isChecked()) {
            this.mValueText.setText(this.mTickLabel);
            return;
        }
        TextView textView = this.mValueText;
        if (this.mSuffix != null) {
            t = t.concat(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + this.mSuffix);
        }
        textView.setText(t);
    }

    @Override // android.widget.SeekBar.OnSeekBarChangeListener
    public void onStartTrackingTouch(SeekBar seek) {
    }

    @Override // android.widget.SeekBar.OnSeekBarChangeListener
    public void onStopTrackingTouch(SeekBar seek) {
    }

    private class ticklistener implements CompoundButton.OnCheckedChangeListener {
        private ticklistener() {
        }

        @Override // android.widget.CompoundButton.OnCheckedChangeListener
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            int value = TickSeekBarPreference.this.mSeekBar.getProgress();
            String t = String.valueOf((TickSeekBarPreference.this.mStep * value) + TickSeekBarPreference.this.mMin);
            if (TickSeekBarPreference.this.mCheckBox.isChecked()) {
                TickSeekBarPreference.this.mValueText.setText(TickSeekBarPreference.this.mTickLabel);
                TickSeekBarPreference.this.mSeekBar.setEnabled(false);
                return;
            }
            TextView textView = TickSeekBarPreference.this.mValueText;
            if (TickSeekBarPreference.this.mSuffix != null) {
                t = t.concat(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + TickSeekBarPreference.this.mSuffix);
            }
            textView.setText(t);
            TickSeekBarPreference.this.mSeekBar.setEnabled(true);
        }
    }

    public void setMax(int max) {
        this.mMax = max;
    }

    public int getMax() {
        return this.mMax;
    }

    public void setProgress(int progress) {
        this.mValue = progress;
        if (this.mSeekBar != null) {
            this.mSeekBar.setProgress(progress);
        }
    }

    public int getProgress() {
        return this.mValue;
    }
}
