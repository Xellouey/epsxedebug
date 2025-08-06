package com.epsxe.ePSXe;

import com.epsxe.ePSXe.jni.libepsxe;

/* loaded from: classes.dex */
interface ePSXeView {
    interface OnTouchListener {
        void onTouch(boolean isInTouch);
    }

    void setHidePad(boolean hide);

    boolean isInTouch();

    default void setOnTouchListener(OnTouchListener listener) {}

    void onAutosave(int i);

    void onPause(int i, int i2);

    void onResume();

    void onStop();

    void queueMotionEvent(int i, int i2, int i3, int i4);

    boolean setIsoName(String str, int i, String str2);

    void setSaveMode(int i, int i2);

    void setSaveslot(int i);

    void setanalogdebug(int i, int i2, int i3, int i4);

    void setautosnaprestoring();

    void setbiosmsg(boolean z);

    void setcustomgameprofile(boolean z);

    void setdebugstring(String str);

    void setdebugstring2(String str);

    void setdevice(int i);

    void setdynamicaction(int i);

    void setdynamicpad(int i);

    void setePSXeLib(libepsxe libepsxeVar, int i, int i2);

    void setemuvolumen(int i);

    void setfps(int i);

    void setframelimit();

    void setframeskip(int i);

    void setgpumtmodeH(int i);

    void setgpumtmodeS(int i);

    void setinputalpha(float f);

    void setinputmag(float f);

    void setinputpadmode(int i, int i2, int i3, int i4);

    void setinputpadmodeanalog(int i);

    void setinputpadtype(int i, int i2);

    void setinputpaintpadmode(int i, int i2);

    void setinputprofile(int i);

    void setinputskinname(String str);

    void setinputvibration(int i, int i2);

    void setlicense(boolean z);

    void setplayermode(int i);

    void setplugin(int i);

    void setportraitmode(int i);

    void setquitonexit();

    void setscreenblackbands(int i);

    void setscreendepth(int i);

    void setscreenorientation(int i);

    void setscreenratio(int i);

    void setscreenvrmode(int i, int i2);

    void setservermode(int i);

    void setshowfps(int i);

    void setsnaprestoring(boolean z);

    void setsoundlatency(int i);

    void setsplitmode(int i);

    void settainted(int i);

    void setverbose(int i);

    void setvideodither(int i);

    void setvideofilter(int i);

    void setvideofilterhw(int i);

    void toggleframelimit();

    void toggletools();

    void unsetframelimit();

    void updatescreenratio(int i);
}
