package com.epsxe.views.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import com.fasterxml.jackson.core.util.MinimalPrettyPrinter;

/* loaded from: classes.dex */
public class DoubleSeekBarPreference extends DialogPreference implements SeekBar.OnSeekBarChangeListener {
    private static final String androidns = "http://schemas.android.com/apk/res/android";
    private String key1;
    private String key2;
    private CheckBox mCheckBox;
    private Context mContext;
    private int mDefault;
    private int mDefault2;
    private String mDialogMessage;
    private String mDialogMessage2;
    private int mMax;
    private int mMax2;
    private int mMin;
    private int mMin2;
    private SeekBar mSeekBar;
    private SeekBar mSeekBar2;
    private TextView mSplashText;
    private TextView mSplashText2;
    private int mStep;
    private int mStep2;
    private String mSuffix1;
    private String mSuffix2;
    private String mTickLabel;
    private int mValue;
    private int mValue2;
    private TextView mValueText;
    private TextView mValueText2;

    public DoubleSeekBarPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mDefault = 0;
        this.mValue = 0;
        this.mDefault2 = 0;
        this.mValue2 = 0;
        setPersistent(false);
        this.mContext = context;
        int mDialogMessageId = attrs.getAttributeResourceValue(androidns, "dialogMessage", 0);
        if (mDialogMessageId == 0) {
            this.mDialogMessage = attrs.getAttributeValue(androidns, "dialogMessage");
        } else {
            this.mDialogMessage = this.mContext.getString(mDialogMessageId);
        }
        int mDialogMessageId2 = attrs.getAttributeResourceValue(null, "dialogMessage2", 0);
        if (mDialogMessageId2 == 0) {
            this.mDialogMessage2 = attrs.getAttributeValue(null, "dialogMessage2");
        } else {
            this.mDialogMessage2 = this.mContext.getString(mDialogMessageId2);
        }
        int mSuffixId = attrs.getAttributeResourceValue(androidns, "text", 0);
        if (mSuffixId == 0) {
            this.mSuffix1 = attrs.getAttributeValue(androidns, "text");
        } else {
            this.mSuffix1 = this.mContext.getString(mSuffixId);
        }
        int mSuffixId2 = attrs.getAttributeResourceValue(null, "suffix2", 0);
        if (mSuffixId2 == 0) {
            this.mSuffix2 = attrs.getAttributeValue(null, "suffix2");
        } else {
            this.mSuffix2 = this.mContext.getString(mSuffixId2);
        }
        if (this.mSuffix2 == null) {
            this.mSuffix2 = this.mSuffix1;
        }
        this.mDefault = attrs.getAttributeIntValue(androidns, "defaultValue", 0);
        this.mMax = attrs.getAttributeIntValue(androidns, "max", 100);
        this.mMin = attrs.getAttributeIntValue(null, "min", 0);
        this.mStep = attrs.getAttributeIntValue(null, "step", 1);
        this.mDefault2 = attrs.getAttributeIntValue(null, "defaultValue2", 0);
        this.mMax2 = attrs.getAttributeIntValue(null, "max2", 100);
        this.mMin2 = attrs.getAttributeIntValue(null, "min2", 0);
        this.mStep2 = attrs.getAttributeIntValue(null, "step2", 1);
        this.key1 = attrs.getAttributeValue(androidns, "key");
        this.key2 = attrs.getAttributeValue(null, "key2");
        this.mTickLabel = attrs.getAttributeValue(null, "ticklabel");
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
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, -2);
        this.mValueText = new TextView(this.mContext);
        this.mValueText.setGravity(1);
        this.mValueText.setTextSize(20.0f);
        layout.addView(this.mValueText, params);
        this.mSeekBar = new SeekBar(this.mContext);
        this.mSeekBar.setOnSeekBarChangeListener(this);
        layout.addView(this.mSeekBar, params);
        this.mSplashText2 = new TextView(this.mContext);
        this.mSplashText2.setPadding(30, 10, 30, 10);
        if (this.mDialogMessage2 != null) {
            this.mSplashText2.setText(this.mDialogMessage2);
        }
        layout.addView(this.mSplashText2);
        this.mValueText2 = new TextView(this.mContext);
        this.mValueText2.setGravity(1);
        this.mValueText2.setTextSize(20.0f);
        layout.addView(this.mValueText2, params);
        this.mSeekBar2 = new SeekBar(this.mContext);
        this.mSeekBar2.setOnSeekBarChangeListener(new onProgressChanged2());
        layout.addView(this.mSeekBar2, params);
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
            this.mValue2 = getPersistedInt(this.mDefault2);
        }
        this.mSeekBar.setMax(this.mMax);
        this.mSeekBar.setProgress(this.mValue);
        this.mSeekBar2.setMax(this.mMax2);
        this.mSeekBar2.setProgress(this.mValue2);
        return layout;
    }

    @Override // android.preference.DialogPreference
    protected void onBindDialogView(View v) {
        int key1value;
        int key2value;
        super.onBindDialogView(v);
        SharedPreferences sharedPreferences = getSharedPreferences();
        try {
            key1value = Integer.parseInt(sharedPreferences.getString(this.key1, "" + this.mDefault));
        } catch (ClassCastException e) {
            key1value = sharedPreferences.getInt(this.key1, this.mMin);
        }
        try {
            key2value = Integer.parseInt(sharedPreferences.getString(this.key2, "" + this.mDefault2));
        } catch (ClassCastException e2) {
            key2value = sharedPreferences.getInt(this.key2, this.mMin);
        }
        if (this.mTickLabel != null) {
            if (key1value == 0) {
                this.mSeekBar.setEnabled(false);
                this.mSeekBar2.setEnabled(false);
                this.mValueText.setText("");
                this.mValueText2.setText("");
                this.mCheckBox.setChecked(true);
            } else {
                this.mSeekBar.setProgress((key1value - this.mMin) / this.mStep);
                this.mSeekBar2.setProgress((key2value - this.mMin2) / this.mStep2);
                this.mSeekBar.setEnabled(true);
                this.mSeekBar2.setEnabled(true);
            }
        } else {
            this.mSeekBar.setProgress((key1value - this.mMin) / this.mStep);
            this.mSeekBar2.setProgress((key2value - this.mMin2) / this.mStep2);
        }
        this.mSeekBar.setMax((this.mMax - this.mMin) / this.mStep);
        this.mSeekBar2.setMax((this.mMax2 - this.mMin2) / this.mStep2);
    }

    @Override // android.preference.DialogPreference
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);
        if (positiveResult) {
            SharedPreferences.Editor editor = getEditor();
            if (this.mTickLabel != null && this.mCheckBox.isChecked()) {
                editor.putInt(this.key1, 0);
                editor.putInt(this.key2, 0);
            } else {
                editor.putInt(this.key1, (this.mSeekBar.getProgress() * this.mStep) + this.mMin);
                editor.putInt(this.key2, (this.mSeekBar2.getProgress() * this.mStep2) + this.mMin2);
            }
            editor.commit();
        }
    }

    @Override // android.widget.SeekBar.OnSeekBarChangeListener
    public void onProgressChanged(SeekBar seek, int value, boolean fromTouch) {
        String t = String.valueOf((this.mStep * value) + this.mMin);
        if (this.mTickLabel != null && this.mCheckBox.isChecked()) {
            this.mValueText.setText("");
            return;
        }
        TextView textView = this.mValueText;
        if (this.mSuffix1 != null) {
            t = t.concat(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + this.mSuffix1);
        }
        textView.setText(t);
    }

    @Override // android.widget.SeekBar.OnSeekBarChangeListener
    public void onStartTrackingTouch(SeekBar seek) {
    }

    @Override // android.widget.SeekBar.OnSeekBarChangeListener
    public void onStopTrackingTouch(SeekBar seek) {
    }

    private class onProgressChanged2 implements SeekBar.OnSeekBarChangeListener {
        private onProgressChanged2() {
        }

        @Override // android.widget.SeekBar.OnSeekBarChangeListener
        public void onProgressChanged(SeekBar seek, int value, boolean fromTouch) {
            String t = String.valueOf((DoubleSeekBarPreference.this.mStep2 * value) + DoubleSeekBarPreference.this.mMin2);
            if (DoubleSeekBarPreference.this.mTickLabel == null || !DoubleSeekBarPreference.this.mCheckBox.isChecked()) {
                TextView textView = DoubleSeekBarPreference.this.mValueText2;
                if (DoubleSeekBarPreference.this.mSuffix2 != null) {
                    t = t.concat(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + DoubleSeekBarPreference.this.mSuffix2);
                }
                textView.setText(t);
                return;
            }
            DoubleSeekBarPreference.this.mValueText2.setText("");
        }

        @Override // android.widget.SeekBar.OnSeekBarChangeListener
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override // android.widget.SeekBar.OnSeekBarChangeListener
        public void onStopTrackingTouch(SeekBar seekBar) {
        }
    }

    private class ticklistener implements CompoundButton.OnCheckedChangeListener {
        private ticklistener() {
        }

        @Override // android.widget.CompoundButton.OnCheckedChangeListener
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            int value = DoubleSeekBarPreference.this.mSeekBar.getProgress();
            int value2 = DoubleSeekBarPreference.this.mSeekBar2.getProgress();
            String t = String.valueOf((DoubleSeekBarPreference.this.mStep * value) + DoubleSeekBarPreference.this.mMin);
            String t2 = String.valueOf((DoubleSeekBarPreference.this.mStep2 * value2) + DoubleSeekBarPreference.this.mMin2);
            if (DoubleSeekBarPreference.this.mTickLabel != null) {
                if (DoubleSeekBarPreference.this.mCheckBox.isChecked()) {
                    DoubleSeekBarPreference.this.mValueText.setText("");
                    DoubleSeekBarPreference.this.mValueText2.setText("");
                    DoubleSeekBarPreference.this.mSeekBar.setEnabled(false);
                    DoubleSeekBarPreference.this.mSeekBar2.setEnabled(false);
                    return;
                }
                TextView textView = DoubleSeekBarPreference.this.mValueText;
                if (DoubleSeekBarPreference.this.mSuffix1 != null) {
                    t = t.concat(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + DoubleSeekBarPreference.this.mSuffix1);
                }
                textView.setText(t);
                TextView textView2 = DoubleSeekBarPreference.this.mValueText2;
                if (DoubleSeekBarPreference.this.mSuffix2 != null) {
                    t2 = t2.concat(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + DoubleSeekBarPreference.this.mSuffix2);
                }
                textView2.setText(t2);
                DoubleSeekBarPreference.this.mSeekBar.setProgress((value - DoubleSeekBarPreference.this.mMin) / DoubleSeekBarPreference.this.mStep);
                DoubleSeekBarPreference.this.mSeekBar2.setProgress((value2 - DoubleSeekBarPreference.this.mMin2) / DoubleSeekBarPreference.this.mStep2);
                DoubleSeekBarPreference.this.mSeekBar.setEnabled(true);
                DoubleSeekBarPreference.this.mSeekBar2.setEnabled(true);
                return;
            }
            DoubleSeekBarPreference.this.mSeekBar.setProgress((value - DoubleSeekBarPreference.this.mMin) / DoubleSeekBarPreference.this.mStep);
            DoubleSeekBarPreference.this.mSeekBar2.setProgress((value2 - DoubleSeekBarPreference.this.mMin2) / DoubleSeekBarPreference.this.mStep2);
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
